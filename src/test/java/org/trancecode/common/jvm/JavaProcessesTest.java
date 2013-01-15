/*
 * Copyright (C) 2013 Herve Quiroz
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
package org.trancecode.common.jvm;

import com.google.common.io.Closeables;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link JavaProcesses}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class JavaProcessesTest
{
    @Test
    public void helloWorld() throws Exception
    {
        ByteArrayOutputStream stdout = null;
        try
        {
            stdout = new ByteArrayOutputStream();
            final URL jarUrl = getClass()
                    .getResource("/" + JavaProcessesTest.class.getSimpleName() + "/helloworld.jar");
            Assert.assertNotNull(jarUrl);
            final String jarPath = jarUrl.getFile();
            Assert.assertNotNull(jarPath);
            final int exitValue = JavaProcesses.newJavaProcess().jarFile(jarPath).stdout(stdout).execute().get();
            Assert.assertEquals(exitValue, 0);
        }
        finally
        {
            Closeables.closeQuietly(stdout);
        }
        final String stdoutText = new String(stdout.toByteArray(), Charset.defaultCharset());
        Assert.assertEquals(stdoutText, "Hello, World!\n");
    }
}
