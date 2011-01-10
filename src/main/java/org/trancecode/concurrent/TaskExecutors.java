/*
 * Copyright (C) 2010 Herve Quiroz
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */
package org.trancecode.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Utility methods related to {@link TaskExecutor}
 * 
 * @author Herve Quiroz
 */
public final class TaskExecutors
{
    public static TaskExecutor forExecutorService(final ExecutorService executor)
    {
        return new TaskExecutor()
        {
            @Override
            public <T> Future<T> submit(final Callable<T> task)
            {
                return executor.submit(task);
            }
        };
    }

    public static TaskExecutor onDemandExecutor()
    {
        return OnDemandTaskExecutor.INSTANCE;
    }

    private static final class OnDemandTaskExecutor implements TaskExecutor
    {
        private static final OnDemandTaskExecutor INSTANCE = new OnDemandTaskExecutor();

        @Override
        public <T> Future<T> submit(final Callable<T> task)
        {
            return new Future<T>()
            {
                private T result;
                private boolean cancelled;
                private boolean done;

                @Override
                public boolean cancel(final boolean mayInterruptIfRunning)
                {
                    if (done)
                    {
                        return false;
                    }

                    cancelled = true;
                    return true;
                }

                @Override
                public boolean isCancelled()
                {
                    return cancelled;
                }

                @Override
                public boolean isDone()
                {
                    return done;
                }

                @Override
                public synchronized T get() throws InterruptedException, ExecutionException
                {
                    // TODO use lightweight lock
                    if (cancelled)
                    {
                        throw new CancellationException();
                    }

                    if (!done)
                    {
                        try
                        {
                            result = task.call();
                        }
                        catch (final Exception e)
                        {
                            throw new ExecutionException(e);
                        }

                        done = true;
                    }

                    return result;
                }

                @Override
                public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException,
                        TimeoutException
                {
                    return get();
                }
            };
        }
    }

    private TaskExecutors()
    {
        // No instantiation
    }
}
