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
package org.trancecode.parallel;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ValueFuture;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.trancecode.collection.TcIterables;
import org.trancecode.function.TcPredicates;

/**
 * @author Herve Quiroz
 */
public final class ParallelIterables
{
    private static Future<?> LAST = ValueFuture.create();

    private static <T> Future<T> last()
    {
        @SuppressWarnings("unchecked")
        final Future<T> last = (Future<T>) LAST;
        return last;
    }

    /**
     * @see Iterables#transform(Iterable, Function)
     */
    public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable,
            final Function<? super F, ? extends T> function, final ExecutorService executor)
    {
        // TODO the logic should probably be moved to some Iterator instead

        final Function<F, Callable<T>> applyFunction = ParallelFunctions.apply(function);
        final Iterable<Callable<T>> callables = Iterables.transform(fromIterable, applyFunction);

        final Queue<Future<T>> futures = new LinkedBlockingQueue<Future<T>>();
        final Function<Callable<T>, Future<T>> submitFunction = ParallelFunctions.submit(executor);
        Iterables.addAll(futures, Iterables.transform(callables, submitFunction));
        final Future<T> last = last();
        futures.add(last);

        return getUntilLast(futures);
    }

    /**
     * @see Iterables#filter(Iterable, Predicate)
     */
    public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate,
            final ExecutorService executorService)
    {
        // TODO
        return Iterables.filter(unfiltered, predicate);
    }

    private static <T> Iterable<T> getUntilLast(final Iterable<Future<T>> futures)
    {
        final Future<T> last = last();
        final Iterable<Future<T>> futuresUntilLast = TcIterables.until(futures, TcPredicates.identicalTo(last));
        final Function<Future<T>, T> function = ParallelFunctions.get();
        return Iterables.transform(futuresUntilLast, function);
    }

    public static <T> Iterable<T> get(final Iterable<Future<T>> futures)
    {
        final Function<Future<T>, T> function = ParallelFunctions.get();
        return Iterables.transform(futures, function);
    }

    private ParallelIterables()
    {
        // No instantiation
    }
}
