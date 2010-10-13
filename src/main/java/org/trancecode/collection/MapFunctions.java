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

import com.google.common.base.Function;

import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link Function} implementations related to {@link Map}.
 * 
 * @author Herve Quiroz
 */
public final class MapFunctions
{
    /**
     * @see Entry#getValue()
     */
    public static final <K, V> Function<Entry<K, V>, V> getValue()
    {
        @SuppressWarnings("unchecked")
        final GetValueFunction<K, V> function = (GetValueFunction<K, V>) GetValueFunction.INSTANCE;
        return function;
    }

    private static final class GetValueFunction<K, V> implements Function<Entry<K, V>, V>
    {
        private static final GetValueFunction<?, ?> INSTANCE = new GetValueFunction<Object, Object>();

        @Override
        public V apply(final Entry<K, V> entry)
        {
            return entry.getValue();
        }
    }

    private MapFunctions()
    {
        // No instantiation
    }
}
