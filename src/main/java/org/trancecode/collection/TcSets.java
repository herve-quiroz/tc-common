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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

/**
 * Utility methods related to {@link Set}.
 * 
 * @author Herve Quiroz
 */
public final class TcSets
{
    public static <T> ImmutableSet<T> immutableSet(final Iterable<T> set, final T element)
    {
        Preconditions.checkNotNull(set);
        Preconditions.checkNotNull(element);

        if (set instanceof Set && ((Set<?>) set).contains(element))
        {
            return ImmutableSet.copyOf(set);
        }

        final Builder<T> builder = ImmutableSet.builder();
        return builder.addAll(set).add(element).build();
    }

    public static <T> ImmutableSet<T> immutableSet(final Set<T> set1, final Set<T> set2)
    {
        Preconditions.checkNotNull(set1);
        Preconditions.checkNotNull(set2);

        if (set1.isEmpty())
        {
            return ImmutableSet.copyOf(set2);
        }

        if (set2.isEmpty())
        {
            return ImmutableSet.copyOf(set1);
        }

        final Builder<T> builder = ImmutableSet.builder();
        return builder.addAll(set1).addAll(set2).build();
    }

    private TcSets()
    {
        // No instantiation
    }
}
