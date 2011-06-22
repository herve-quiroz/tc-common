/*
 * Copyright (C) 2011 Herve Quiroz
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
package org.trancecode.event;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trancecode.collection.TcSets;
import org.trancecode.concurrent.TaskExecutor;
import org.trancecode.concurrent.TaskExecutors;
import org.trancecode.concurrent.TcFutures;

/**
 * @author Herve Quiroz
 */
public abstract class AbstractEventObservable<T extends Event> implements EventObservable<T>, EventObserver<T>
{
    private final boolean blockingNotification;
    private final TaskExecutor eventDispatcher;
    private final NotificationFailurePolicy notificationFailurePolicy;

    private Iterable<EventObserver<T>> observers = ImmutableSet.of();

    public static interface NotificationFailurePolicy
    {
        void notificationFailure(EventObserver<?> observer, Event event, Throwable error);
    }

    private static NotificationFailurePolicy FAIL = new NotificationFailurePolicy()
    {
        @Override
        public void notificationFailure(final EventObserver<?> observer, final Event event, final Throwable error)
        {
            throw Throwables.propagate(error);
        }
    };

    private static NotificationFailurePolicy IGNORE = new NotificationFailurePolicy()
    {
        @Override
        public void notificationFailure(final EventObserver<?> observer, final Event event, final Throwable error)
        {
            // Ignore
        }
    };

    public static final NotificationFailurePolicy alwaysFail()
    {
        return FAIL;
    }

    public static final NotificationFailurePolicy ignoreErrors()
    {
        return IGNORE;
    }

    protected AbstractEventObservable()
    {
        this(alwaysFail());
    }

    protected AbstractEventObservable(final NotificationFailurePolicy notificationFailurePolicy)
    {
        this(true, TaskExecutors.directExecutor(), notificationFailurePolicy);
    }

    protected AbstractEventObservable(final boolean blockingNotification, final TaskExecutor eventDispatcher,
            final NotificationFailurePolicy notificationFailurePolicy)
    {
        this.blockingNotification = blockingNotification;
        this.eventDispatcher = eventDispatcher;
        this.notificationFailurePolicy = Preconditions.checkNotNull(notificationFailurePolicy);
    }

    @Override
    public void addObserver(final EventObserver<T> observer)
    {
        observers = TcSets.immutableSet(observers, observer);
    }

    @Override
    public void removeObserver(final EventObserver<T> observer)
    {
        observers = TcSets.immutableSetWithout(observers, observer);
    }

    @Override
    public void notify(final T event)
    {
        final Iterable<Callable<Object>> notificationTasks = Iterables.transform(observers,
                new Function<EventObserver<T>, Callable<Object>>()
                {
                    @Override
                    public Callable<Object> apply(final EventObserver<T> observer)
                    {
                        return new Callable<Object>()
                        {
                            @Override
                            public Void call()
                            {
                                try
                                {
                                    observer.notify(event);
                                }
                                catch (final Throwable error)
                                {
                                    notificationFailurePolicy.notificationFailure(observer, event, error);
                                }
                                return null;
                            }
                        };
                    }
                });
        final Iterable<Future<Object>> results = TcFutures.submit(eventDispatcher, notificationTasks);
        if (blockingNotification)
        {
            for (final Future<Object> result : results)
            {
                try
                {
                    result.get();
                }
                catch (final InterruptedException e)
                {
                    // XXX not sure about this
                    throw new IllegalStateException(e);
                }
                catch (final ExecutionException e)
                {
                    throw Throwables.propagate(e.getCause());
                }
            }
        }
    }
}
