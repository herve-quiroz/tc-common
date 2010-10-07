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
package org.trancecode.core;

import com.google.common.base.Supplier;
import org.trancecode.function.TcSuppliers;

/**
 * @author Herve Quiroz
 * @see <a href="http://en.wikipedia.org/wiki/Thunk">Thunk</a>
 */
public abstract class AbstractThunk<T> implements Supplier<T>
{
    private Supplier<T> value = new Supplier<T>()
    {
        @Override
        public T get()
        {
            final T computedValue = compute();
            value = TcSuppliers.singleton(computedValue);
            return computedValue;
        }
    };

    public final T get()
    {
        return value.get();
    }

    protected abstract T compute();
}
