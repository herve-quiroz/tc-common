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
package org.trancecode.collection;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public static <T> Iterator<T> concurrentModifiable(final ArrayList<T> sequence)
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
}
