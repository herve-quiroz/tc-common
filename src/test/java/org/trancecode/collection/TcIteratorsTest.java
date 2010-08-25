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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * Tests for {@link TcIterators}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class TcIteratorsTest extends AbstractTest
{
    @Test
    public void concurrentModifiable()
    {
        final ArrayList<String> sequence = Lists.newArrayList();
        final Iterator<String> iterator = TcIterators.concurrentModifiable(sequence);
        assert !iterator.hasNext();

        sequence.add("1");
        assert iterator.hasNext();
        Assert.assertEquals("1", iterator.next());
        assert !iterator.hasNext();

        sequence.add("2");
        sequence.add("3");
        assert iterator.hasNext();
        Assert.assertEquals("2", iterator.next());
        assert iterator.hasNext();

        sequence.add("4");
        Assert.assertEquals("3", iterator.next());
        Assert.assertEquals("4", iterator.next());
        assert !iterator.hasNext();
    }
}
