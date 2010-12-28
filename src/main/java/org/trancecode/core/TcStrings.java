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
package org.trancecode.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.trancecode.base.TcPreconditions;
import org.trancecode.collection.TcIterables;

/**
 * Utility methods related to {@link String} objects.
 * 
 * @author Herve Quiroz
 */
public final class TcStrings
{
    /**
     * Splits this string around matches of the given separator sequence.
     * 
     * @param separatorSequence
     *            the separator (not a regular expression).
     */
    public static Iterable<String> split(final String string, final String separatorSequence)
    {
        Preconditions.checkNotNull(string);
        TcPreconditions.checkNotEmpty(separatorSequence);

        return new Iterable<String>()
        {
            @Override
            public Iterator<String> iterator()
            {
                if (string.isEmpty())
                {
                    return Iterators.singletonIterator(string);
                }

                final int index = string.indexOf(separatorSequence);
                if (index < 0)
                {
                    return Iterators.singletonIterator(string);
                }

                final String after = string.substring(index + separatorSequence.length());
                return TcIterables.prepend(split(after, separatorSequence), string.substring(0, index)).iterator();
            }
        };
    }

    private TcStrings()
    {
        // No instantiation
    }
}
