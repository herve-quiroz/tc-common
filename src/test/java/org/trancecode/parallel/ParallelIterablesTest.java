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
package org.trancecode.parallel;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;
import org.trancecode.math.NumberFunctions;

/**
 * Tests for {@link ParallelIterables}.
 * 
 * @author Herve Quiroz
 */
public final class ParallelIterablesTest extends AbstractTest
{
    private static void assertEquals(final Iterable<?> actual, final Iterable<?> expected)
    {
        Assert.assertEquals(ImmutableList.copyOf(actual), ImmutableList.copyOf(expected));
    }

    @Test
    public void transform()
    {
        final List<String> strings = ImmutableList.of("1", "2", "3", "4", "5");
        final List<Integer> integers = ImmutableList.of(1, 2, 3, 4, 5);

        final ExecutorService executor = Executors.newFixedThreadPool(3);
        final Iterable<Integer> result = ParallelIterables.transform(strings, NumberFunctions.parseInt(), executor);

        assertEquals(result, integers);
    }
}
