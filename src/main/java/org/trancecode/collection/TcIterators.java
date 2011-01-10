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
package org.trancecode.collection;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

import org.trancecode.api.Nullable;
import org.trancecode.api.ReturnsNullable;

/**
 * Utility methods related to {@link Iterator}.
 * 
 * @author Herve Quiroz
 */
public final class TcIterators
{
    private TcIterators()
    {
        // No instantiation
    }

    /**
     * Returns an {@link Iterator} over a sequence that allows new elements to
     * be added while the iteration is performed.
     * <p>
     * The returned {@link Iterator} is using its own internal index and it is
     * up the client application to avoid inserting or removing elements to the
     * part of the sequence that has already been iterated over.
     */
    public static <T> Iterator<T> concurrentModifiable(final List<T> sequence)
    {
        Preconditions.checkNotNull(sequence);

        return new Iterator<T>()
        {
            int index = 0;

            @Override
            public boolean hasNext()
            {
                return index < sequence.size();
            }

            @Override
            public T next()
            {
                if (hasNext())
                {
                    return sequence.get(index++);
                }

                throw new NoSuchElementException();
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns the elements from the passed sequence up to the first element
     * that matches the passed predicate (this element is excluded from the
     * result sequence).
     * <p>
     * Returned {@link Iterator} will throw {@link NoSuchElementException} if
     * the predicate does not match for any element from the sequence.
     */
    public static <T> Iterator<T> until(final Iterator<T> elements, final Predicate<T> predicate)
    {
        Preconditions.checkNotNull(elements);
        Preconditions.checkNotNull(predicate);

        return new AbstractIterator<T>()
        {
            @Override
            protected T computeNext()
            {
                if (elements.hasNext())
                {
                    final T next = elements.next();
                    if (predicate.apply(next))
                    {
                        return endOfData();
                    }

                    return next;
                }

                throw new NoSuchElementException();
            }
        };
    }

    /**
     * Returns an {@link Iterator} that contains the elements from the queue.
     * <p>
     * The {@link Iterator#next()} method delegates to
     * {@link BlockingQueue#take()} and thus will block while the queue is
     * empty. The {@link Iterator} has an unbound size and
     * {@link Iterator#hasNext()} will only return {@code false} if the
     * {@link BlockingQueue#take()} call is interrupted.
     */
    public static <T> Iterator<T> removeAll(final BlockingQueue<T> fromQueue)
    {
        return new AbstractIterator<T>()
        {
            @Override
            protected T computeNext()
            {
                try
                {
                    return fromQueue.take();
                }
                catch (final InterruptedException e)
                {
                    return endOfData();
                }
            }
        };
    }

    public static <T> Iterator<T> handleErrors(final Iterator<T> fromIterator,
            final Function<Throwable, Void> errorHandlingFunction)
    {
        return new AbstractIterator<T>()
        {
            @Override
            protected T computeNext()
            {
                try
                {
                    if (fromIterator.hasNext())
                    {
                        return fromIterator.next();
                    }
                    else
                    {
                        return endOfData();
                    }
                }
                catch (final Exception e)
                {
                    errorHandlingFunction.apply(e);
                    Throwables.propagateIfPossible(e);
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    @ReturnsNullable
    public static <T> T getFirst(final Iterator<T> elements)
    {
        return getFirst(elements, null);
    }

    @ReturnsNullable
    public static <T> T getFirst(final Iterator<T> elements, @Nullable final T defaultElement)
    {
        if (elements.hasNext())
        {
            return elements.next();
        }

        return defaultElement;
    }
}
