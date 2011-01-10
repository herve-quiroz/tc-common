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

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.NoSuchElementException;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * Tests for {@link TcIterables}.
 * 
 * @author Herve Quiroz
 */
public final class TcIterablesTest extends AbstractTest
{
    private static void assertEquals(final Iterable<?> actual, final Iterable<?> expected)
    {
        Assert.assertEquals(ImmutableList.copyOf(actual), ImmutableList.copyOf(expected));
    }

    @Test
    public void until()
    {
        final List<String> elements = ImmutableList.of("a", "b", "c", "d");
        assertEquals(TcIterables.until(elements, Predicates.equalTo("a")), ImmutableList.of());
        assertEquals(TcIterables.until(elements, Predicates.equalTo("c")), ImmutableList.of("a", "b"));
        assertEquals(TcIterables.until(elements, Predicates.equalTo("d")), ImmutableList.of("a", "b", "c"));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void untilError()
    {
        ImmutableList.copyOf(TcIterables.until(ImmutableList.of("a", "b", "c"), Predicates.equalTo("d")));
    }

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

        AssertJUnit.assertEquals(ImmutableList.of("abcd", "a", "bcd", "b", "cd", "c", "d"),
                ImmutableList.copyOf(elements));
    }
}
