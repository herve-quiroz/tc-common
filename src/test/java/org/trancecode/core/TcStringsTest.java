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

import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;
import org.trancecode.TcAssert;

/**
 * Tests for {@link TcStrings}.
 * 
 * @author Herve Quiroz
 */
public final class TcStringsTest extends AbstractTest
{
    @Test
    public void split()
    {
        TcAssert.assertIterableEquals(TcStrings.split("abc|-|defgh|-|i", "|-|"), ImmutableList.of("abc", "defgh", "i"));
        TcAssert.assertIterableEquals(TcStrings.split("ab_c_", "_"), ImmutableList.of("ab", "c", ""));
        TcAssert.assertIterableEquals(TcStrings.split("a___b", "_"), ImmutableList.of("a", "", "", "b"));
        TcAssert.assertIterableEquals(TcStrings.split("___", "_"), ImmutableList.of("", "", "", ""));
    }
}
