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
package org.trancecode.collection;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * Tests for {@link TcIterables}.
 * 
 * @author Herve Quiroz
 * @version $Revision: 3842 $
 */
@Test
public class TubularIterablesTest extends AbstractTest
{
    @Test
    public void getDescendants()
    {
        final Function<String, Iterable<String>> split = new Function<String, Iterable<String>>()
        {
            @Override
            public Iterable<String> apply(final String string)
            {
                if (string.length() == 1)
                {
                    return ImmutableList.of();
                }

                return ImmutableList.of(string.substring(0, 1), string.substring(1));
            }
        };

        AssertJUnit.assertEquals(ImmutableList.of("a", "bcd"), ImmutableList.copyOf(split.apply("abcd")));
        AssertJUnit.assertEquals(ImmutableList.of(), ImmutableList.copyOf(split.apply("a")));

        final Iterable<String> elements = TcIterables.getDescendants("abcd", split);

        AssertJUnit.assertEquals(ImmutableList.of("abcd", "a", "bcd", "b", "cd", "c", "d"), ImmutableList
                .copyOf(elements));
    }
}
