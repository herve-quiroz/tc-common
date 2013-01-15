/*
 * Copyright (C) 2013 Herve Quiroz
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

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility methods related to {@link Executor} and {@link ExecutorService}.
 * 
 * @author Herve Quiroz
 */
public final class TcExecutors
{
    private static final ExecutorService EXECUTOR = newSingleUseThreadExecutorService();

    private TcExecutors()
    {
        // No instantiation
    }

    public static ExecutorService newSingleUseThreadExecutorService()
    {
        return newSingleUseThreadExecutorService(TcExecutors.class.getName());
    }

    public static ExecutorService newSingleUseThreadExecutorService(final String threadNamePrefix)
    {
        return new AbstractExecutorService()
        {
            private final AtomicLong numberOfThreads = new AtomicLong(0);

            @Override
            public void shutdown()
            {
                // TODO .shutdown()
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Runnable> shutdownNow()
            {
                // TODO .shutdownNow()
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isShutdown()
            {
                // TODO .isShutdown()
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isTerminated()
            {
                // TODO .isTerminated()
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException
            {
                // TODO .awaitTermination()
                throw new UnsupportedOperationException();
            }

            @Override
            public void execute(final Runnable command)
            {
                final Thread thread = new Thread(command);
                thread.setName(threadNamePrefix + numberOfThreads.incrementAndGet());
                thread.setDaemon(true);
                thread.start();
            }
        };
    }

    public static <T> Future<T> concurrentExecute(final Callable<T> command)
    {
        return EXECUTOR.submit(command);
    }
}
