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
package org.trancecode.io;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * Tests for {@link ClasspathInputResolver}.
 * 
 * @author Herve Quiroz
 */
public class ClasspathInputResolverTest extends AbstractTest
{
    private static final URI URI_BASE = URI.create("classpath:/" + ClasspathInputResolverTest.class.getSimpleName()
            + "/");

    @Test
    public void resolverInputStream() throws Exception
    {
        final String expected = "123";
        final String actual = IOUtils.toString(ClasspathInputResolver.instance().resolveInputStream(
                URI_BASE.resolve("resolverInputStream")));
        Assert.assertEquals(actual, expected);
    }
}
