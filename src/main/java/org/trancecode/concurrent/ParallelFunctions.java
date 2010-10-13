/*
 * Copyright (C) 2010 TranceCode Software
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

import com.google.common.base.Function;
import com.google.common.base.Throwables;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * {@link Function} implementations related to concurrent processing.
 * 
 * @author Herve Quiroz
 * @see Future
 * @see Callable
 */
public final class ParallelFunctions
{
    public static <T> Function<Future<T>, T> get()
    {
        @SuppressWarnings("unchecked")
        final GetFunction<T> function = (GetFunction<T>) GetFunction.INSTANCE;
        return function;
    }

    private static final class GetFunction<T> implements Function<Future<T>, T>
    {
        private static final GetFunction<Object> INSTANCE = new GetFunction<Object>();

        @Override
        public T apply(final Future<T> future)
        {
            try
            {
                return future.get();
            }
            catch (final InterruptedException e)
            {
                throw new RuntimeInterruptedException(e);
            }
            catch (final ExecutionException e)
            {
                Throwables.propagateIfPossible(e.getCause());
                throw new RuntimeExecutionException(e);
            }
        }
    }

    public static <T> Function<Callable<T>, T> call()
    {
        @SuppressWarnings("unchecked")
        final CallFunction<T> function = (CallFunction<T>) CallFunction.INSTANCE;
        return function;
    }

    private static final class CallFunction<T> implements Function<Callable<T>, T>
    {
        private static final CallFunction<Object> INSTANCE = new CallFunction<Object>();

        @Override
        public T apply(final Callable<T> callable)
        {
            try
            {
                return callable.call();
            }
            catch (final Exception e)
            {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <F, T> Function<F, Callable<T>> apply(final Function<? super F, ? extends T> function)
    {
        return new Function<F, Callable<T>>()
        {
            @Override
            public Callable<T> apply(final F from)
            {
                return new Callable<T>()
                {
                    @Override
                    public T call() throws Exception
                    {
                        return function.apply(from);
                    }
                };
            }
        };
    }

    public static <T> Function<Callable<T>, Future<T>> submit(final ExecutorService executor)
    {
        return new Function<Callable<T>, Future<T>>()
        {
            @Override
            public Future<T> apply(final Callable<T> task)
            {
                return executor.submit(task);
            }
        };
    }

    private ParallelFunctions()
    {
        // No instantiation
    }
}
