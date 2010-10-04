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
package org.trancecode.function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * Tests for {@link TcPredicates}.
 * 
 * @author Herve Quiroz
 */
public final class TcPredicatesTest extends AbstractTest
{
    @Test
    public void identicalTo()
    {
        final Object object = new Object();
        final List<Object> list = ImmutableList.of(object, object, object);
        assert Iterables.all(list, TcPredicates.identicalTo(object));
    }
}
