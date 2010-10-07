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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.trancecode.core.AbstractImmutableObject;

/**
 * Utility methods related to {@link Supplier}.
 * 
 * @author Herve Quiroz
 */
public final class TcSuppliers
{
    private TcSuppliers()
    {
        // No instantiation
    }

    public static <F, T> Supplier<T> fromFunction(final Function<F, T> function, final F argument)
    {
        return new FunctionSupplier<F, T>(function, argument);
    }

    private static class FunctionSupplier<F, T> extends AbstractImmutableObject implements Supplier<T>
    {
        private final Function<F, T> function;
        private final F argument;

        public FunctionSupplier(final Function<F, T> function, final F argument)
        {
            super(function, argument);
            this.function = Preconditions.checkNotNull(function);
            this.argument = Preconditions.checkNotNull(argument);
        }

        @Override
        public T get()
        {
            return function.apply(argument);
        }
    }

    public static <T> Supplier<T> singleton(final T value)
    {
        return new Supplier<T>()
        {
            @Override
            public T get()
            {
                return value;
            }
        };
    }
}
