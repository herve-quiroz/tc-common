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
package org.trancecode.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link TcByteStreams}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class TcByteStreamsTest
{
    @Test
    public void concurrentCopy() throws Exception
    {
        final byte[] bytes = new byte[] { 1, 2, 3, 4 };
        final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        final long length = TcByteStreams.concurrentCopy(new ByteArrayInputStream(bytes), bytesOut, true).get();
        Assert.assertEquals(length, bytes.length);
        Assert.assertEquals(bytesOut.toByteArray(), bytes);
    }
}
