/*
 * Copyright (C) 2008 Herve Quiroz
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
package org.trancecode.lang;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.trancecode.api.Nullable;
import org.trancecode.function.TcSuppliers;

/**
 * @author Herve Quiroz
 */
public final class TcObjects
{
    private TcObjects()
    {
        // No instantiation
    }

    public static int hashCode(@Nullable final Object object)
    {
        if (object == null)
        {
            return 0;
        }

        return object.hashCode();
    }

    public static int hashCode(final Object... objects)
    {
        if (objects.length == 0)
        {
            throw new IllegalArgumentException();
        }

        int hashCode = hashCode(objects[0]);
        for (int i = 1; i < objects.length; i++)
        {
            hashCode <<= 4;
            hashCode ^= hashCode(objects[i]);
        }

        return hashCode;
    }

    public static boolean equals(@Nullable final Object object1, @Nullable final Object object2)
    {
        if (object1 == object2)
        {
            return true;
        }

        if (object1 != null)
        {
            return object1.equals(object2);
        }

        return false;
    }

    public static boolean pairEquals(final Object... objects)
    {
        if (objects.length % 2 != 0)
        {
            throw new IllegalArgumentException("size = " + objects.length);
        }

        for (int i = 0; i < objects.length; i += 2)
        {
            if (!equals(objects[i], objects[i + 1]))
            {
                return false;
            }
        }

        return true;
    }

    public static <T> T conditional(final boolean condition, final T ifTrue, final T ifFalse)
    {
        if (condition)
        {
            return ifTrue;
        }
        else
        {
            return ifFalse;
        }
    }

    public static Supplier<Integer> immutableObjectHashCode(final Object... properties)
    {
        Preconditions.checkNotNull(properties);

        return TcSuppliers.memoize(new Supplier<Integer>()
        {
            @Override
            public Integer get()
            {
                return TcObjects.hashCode(properties);
            }
        });
    }

    public static Supplier<String> immutableObjectToString(final String format, final Object... args)
    {
        Preconditions.checkNotNull(format);

        return TcSuppliers.memoize(new Supplier<String>()
        {
            @Override
            public String get()
            {
                return String.format(format, args);
            }
        });
    }
}
