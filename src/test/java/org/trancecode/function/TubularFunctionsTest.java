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
package org.trancecode.function;

import org.trancecode.AbstractTest;
import org.trancecode.core.function.TubularFunctions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;


/**
 * Tests for {@link TubularFunctions}.
 * 
 * @author Herve Quiroz
 * @version $Revision: 3842 $
 */
@Test
public class TubularFunctionsTest extends AbstractTest
{
	@Test
	public void toIterable()
	{
		final Iterable<String> iterable =
			Iterables.concat(Iterables.transform(ImmutableList.of("abc", "def"), TubularFunctions
				.toIterable(String.class)));
		AssertJUnit.assertEquals(ImmutableList.of("abc", "def"), ImmutableList.copyOf(iterable));
	}
}
