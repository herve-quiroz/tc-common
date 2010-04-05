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
package org.trancecode.core;

import org.trancecode.AbstractTest;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests for {@link TubularObjects}.
 * 
 * @author Herve Quiroz
 * @version $Revision: 3842 $
 */
@Test
public class TubularObjectsTest extends AbstractTest
{
    private static final int TOO_MANY_INSTANCES = 1024 * 1024;

    /** Check that memory from internalized objects gets reclaimed. */
    @Test
    public void internMemoryReclaim()
    {
        for (int i = 0; i < TOO_MANY_INSTANCES; i++)
        {
            final Object object = new Object();
            final Object intern = TubularObjects.intern(object);
            AssertJUnit.assertEquals(object, intern);
        }
    }

    /** Check that internalized object is indeed internalized. */
    @Test
    public void internIdentity()
    {
        final String string = new StringBuilder().append("a").append("bc").toString();
        final String internString = TubularObjects.intern(string);
        AssertJUnit.assertEquals(string, internString);
        AssertJUnit.assertSame(internString, string);

        for (int i = 0; i < TOO_MANY_INSTANCES; i++)
        {
            final Object object = new Object();
            final Object intern = TubularObjects.intern(object);
            AssertJUnit.assertEquals(object, intern);
        }

        final String stringAfter = new StringBuilder().append("ab").append("c").toString();
        assert stringAfter != string;
        final String internStringAfter = TubularObjects.intern(stringAfter);
        AssertJUnit.assertEquals(string, internStringAfter);
        AssertJUnit.assertSame(string, internStringAfter);
    }
}
