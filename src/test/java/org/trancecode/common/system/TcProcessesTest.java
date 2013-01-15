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
package org.trancecode.common.system;

import com.google.common.io.Closeables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Future;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.io.Files;

/**
 * Tests for {@link TcProcesses}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class TcProcessesTest
{
    @Test
    public void execute() throws Exception
    {
        // TODO skip test if not Unix-like system
        final byte[] bytes = new byte[] { 1, 2, 3, 4 };
        final File file = Files.createTempFile(this).getAbsoluteFile();
        Files.write(bytes, file);
        ByteArrayOutputStream stdout = null;
        try
        {
            stdout = new ByteArrayOutputStream();
            final int exitValue = TcProcesses.newProcess("cat").stdout(stdout).args(file.getPath()).execute().get();
            Assert.assertEquals(exitValue, 0);
        }
        finally
        {
            Closeables.closeQuietly(stdout);
        }
        Assert.assertEquals(stdout.toByteArray(), bytes);
    }

    @Test
    public void cancel() throws Exception
    {
        // TODO skip test if not Unix-like system
        final Future<Integer> process = TcProcesses.newProcess("cat").execute();
        Thread.sleep(1);
        Assert.assertFalse(process.isCancelled());
        Assert.assertFalse(process.isDone());
        final boolean cancelled = process.cancel(true);
        Assert.assertTrue(cancelled);
        Assert.assertTrue(process.isCancelled());
        Assert.assertTrue(process.isDone());
        final int exitValue = process.get();
        Assert.assertEquals(exitValue, 143);
    }
}
