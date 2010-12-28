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
package org.trancecode.concurrent;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;
import org.trancecode.lang.StringPredicates;
import org.trancecode.math.NumberFunctions;

/**
 * Tests for {@link ParallelIterables}.
 * 
 * @author Herve Quiroz
 */
public final class ParallelIterablesTest extends AbstractTest
{
    private static final int MANY_ELEMENTS = 1000;

    private static void assertEquals(final Iterable<?> actual, final Iterable<?> expected)
    {
        Assert.assertEquals(ImmutableList.copyOf(actual), ImmutableList.copyOf(expected));
    }

    @Test
    public void filter()
    {
        final List<String> unfiltered = buildInputList(50);
        final ExecutorService executor = Executors.newFixedThreadPool(3);
        final Iterable<String> filtered = ParallelIterables
                .filter(unfiltered, StringPredicates.endsWith("2"), executor);
        assertEquals(filtered, ImmutableList.of("2", "12", "22", "32", "42"));

    }

    @Test
    public void transform()
    {
        final List<String> strings = buildInputList(5);
        final List<Integer> integers = ImmutableList.of(0, 1, 2, 3, 4);

        final ExecutorService executor = Executors.newFixedThreadPool(3);
        final Iterable<Integer> result = ParallelIterables.transform(strings, NumberFunctions.parseInt(), executor);

        assertEquals(result, integers);
    }

    @Test
    public void transformMany()
    {
        transformMany(Executors.newFixedThreadPool(3));
    }

    @Test
    public void transformManyWithSingleThread()
    {
        transformMany(Executors.newSingleThreadExecutor());
    }

    private static void transformMany(final ExecutorService executor)
    {
        final List<String> strings = buildInputList(MANY_ELEMENTS);
        final Iterable<Integer> result = ParallelIterables.transform(strings, NumberFunctions.parseInt(), executor);
        Assert.assertEquals(Iterables.size(result), MANY_ELEMENTS);
    }

    private static List<String> buildInputList(final int numberOfElements)
    {
        final List<String> strings = Lists.newArrayList();
        for (int i = 0; i < numberOfElements; i++)
        {
            strings.add(Integer.toString(i));
        }
        return strings;
    }

    @Test
    public void transformCancel()
    {
        final int limit = MANY_ELEMENTS / 10;
        final List<String> strings = buildInputList(MANY_ELEMENTS);
        final AtomicInteger count = new AtomicInteger();
        final RuntimeException expectedError = new IllegalStateException();
        final Function<String, String> function = new Function<String, String>()
        {
            @Override
            public String apply(final String string)
            {
                if (count.get() == limit)
                {
                    throw expectedError;
                }

                count.incrementAndGet();
                return string;
            }
        };

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try
        {
            Iterables.size(ParallelIterables.transform(strings, function, executor));
        }
        catch (final Exception e)
        {
            if (e != expectedError)
            {
                throw new IllegalStateException(e);
            }
        }

        Assert.assertEquals(count.get(), limit);
    }
}
