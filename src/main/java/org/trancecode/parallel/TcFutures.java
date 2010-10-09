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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.concurrent.Future;

/**
 * Utility methods related to {@link Future}.
 * 
 * @author Herve Quiroz
 */
public final class TcFutures
{
    public static <T> Iterable<Future<T>> cancelled(final Iterable<Future<T>> tasks)
    {
        final Predicate<Future<T>> filter = FuturePredicates.isCancelled();
        return Iterables.filter(tasks, filter);
    }

    public static <T> Iterable<Future<T>> done(final Iterable<Future<T>> tasks)
    {
        final Predicate<Future<T>> filter = FuturePredicates.isDone();
        return Iterables.filter(tasks, filter);
    }

    public static <T> Iterable<Future<T>> notDone(final Iterable<Future<T>> tasks)
    {
        final Predicate<Future<T>> filter = FuturePredicates.isDone();
        return Iterables.filter(tasks, Predicates.not(filter));
    }

    public static <T> void cancel(final Iterable<Future<T>> tasks)
    {
        for (final Future<T> future : notDone(tasks))
        {
            future.cancel(true);
        }
    }

    private TcFutures()
    {
        // No instantiation
    }
}
