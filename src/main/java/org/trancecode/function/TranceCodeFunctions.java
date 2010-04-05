/*
 * Copyright (C) 2008 TranceCode Software
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
 *
 * $Id$
 */
package org.trancecode.function;

import org.trancecode.core.AbstractImmutableObject;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;

/**
 * Utility methods related to {@link Function}.
 * 
 * @author Herve Quiroz
 * @version $Revision$
 */
public final class TranceCodeFunctions
{
    private TranceCodeFunctions()
    {
        // No instantiation
    }

    public static <E, P> E apply(final E initialElement, final Iterable<P> parameters,
            final Function<Pair<E, P>, E> function)
    {
        E currentElement = initialElement;
        for (final P parameter : parameters)
        {
            currentElement = function.apply(Pairs.newImmutablePair(currentElement, parameter));
        }

        return currentElement;
    }

    public static <F, T> Function<Function<F, T>, T> applyTo(final F argument)
    {
        return new ApplyToFunction<F, T>(argument);
    }

    private static class ApplyToFunction<F, T> extends AbstractImmutableObject implements Function<Function<F, T>, T>
    {
        private final F argument;

        public ApplyToFunction(final F argument)
        {
            super(argument);
            this.argument = argument;
        }

        @Override
        public T apply(final Function<F, T> function)
        {
            return function.apply(argument);
        }
    }

    public static <F, T> Function<F, T> cache(final Function<F, T> function)
    {
        return new CacheFunction<F, T>(function);
    }

    private static class CacheFunction<F, T> extends AbstractImmutableObject implements Function<F, T>
    {
        private final Map<F, T> cache;

        private CacheFunction(final Function<F, T> function)
        {
            super(function);
            cache = new MapMaker().softValues().makeComputingMap(function);
        }

        @Override
        public T apply(final F from)
        {
            return cache.get(from);
        }
    }

    public static <F, T> Function<F, T> conditional(final Predicate<F> predicate, final Function<? super F, T> ifTrue,
            final Function<? super F, T> ifFalse)
    {
        return new ConditionalFunction<F, T>(predicate, ifTrue, ifFalse);
    }

    private static class ConditionalFunction<F, T> implements Function<F, T>
    {
        private final Predicate<F> predicate;
        private final Function<? super F, T> ifTrue;
        private final Function<? super F, T> ifFalse;

        public ConditionalFunction(final Predicate<F> predicate, final Function<? super F, T> ifTrue,
                final Function<? super F, T> ifFalse)
        {
            super();
            Preconditions.checkNotNull(predicate);
            Preconditions.checkNotNull(ifTrue);
            Preconditions.checkNotNull(ifFalse);
            this.predicate = predicate;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        @Override
        public T apply(final F from)
        {
            if (predicate.apply(from))
            {
                return ifTrue.apply(from);
            }

            return ifFalse.apply(from);
        }
    }

    public static <T> Function<T, Iterable<T>> toIterable(final Class<T> elementClass)
    {
        return new ToIterableFunction<T>();
    }

    private static class ToIterableFunction<T> implements Function<T, Iterable<T>>
    {
        @Override
        public Iterable<T> apply(final T element)
        {
            return ImmutableList.of(element);
        }
    }

    public static <T> Function<T, Boolean> asFunction(final Predicate<T> predicate)
    {
        return new PredicateAsFunction<T>(predicate);
    }

    private static class PredicateAsFunction<T> implements Function<T, Boolean>
    {
        private final Predicate<T> predicate;

        public PredicateAsFunction(final Predicate<T> predicate)
        {
            super();
            Preconditions.checkNotNull(predicate);
            this.predicate = predicate;
        }

        @Override
        public Boolean apply(final T from)
        {
            return predicate.apply(from);
        }
    }
}
