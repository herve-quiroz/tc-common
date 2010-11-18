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

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods related to {@link InputStream} and {@link OutputStream}.
 * 
 * @author Herve Quiroz
 */
public final class TcByteStreams
{
    public static long copy(final InputStream in, final OutputStream out, final boolean close)
    {
        try
        {
            final long bytes = ByteStreams.copy(in, out);
            Closeables.close(in, false);
            Closeables.close(out, false);
            return bytes;
        }
        catch (final IOException e)
        {
            throw new RuntimeIOException(e);
        }
        finally
        {
            if (close)
            {
                Closeables.closeQuietly(in);
                Closeables.closeQuietly(out);
            }
        }
    }

    private TcByteStreams()
    {
        // No instantiation
    }
}
