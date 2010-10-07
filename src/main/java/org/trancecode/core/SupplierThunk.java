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

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

/**
 * @author Herve Quiroz
 * @see <a href="http://en.wikipedia.org/wiki/Thunk">Thunk</a>
 */
public final class SupplierThunk<T> extends AbstractThunk<T>
{
    private final Supplier<T> supplier;

    public SupplierThunk(final Supplier<T> supplier)
    {
        this.supplier = Preconditions.checkNotNull(supplier);
    }

    @Override
    protected T compute()
    {
        return supplier.get();
    }
}
