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
package org.trancecode.io;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.trancecode.logging.Logger;

/**
 * Utilty methods related to I/O.
 * 
 * @author Herve Quiroz
 */
public final class IOUtil
{
    public static final byte[] EMPTY_BYTE_BUFFER = new byte[0];

    private IOUtil()
    {
        // To prevent instantiation
    }

    public static void close(final Closeable closeable) throws IOException
    {
        if (closeable != null)
        {
            closeable.close();
        }
    }

    public static void closeQuietly(final Closeable closeable)
    {
        closeQuietly(closeable, null);
    }

    public static void closeQuietly(final Closeable closeable, final Logger logger)
    {
        try
        {
            if (closeable != null)
            {
                closeable.close();
            }
        }
        catch (final IOException e)
        {
            if (logger != null)
            {
                logger.warn(e.toString(), e);
            }
        }
    }

    public static InputStream newNullInputStream()
    {
        return new ByteArrayInputStream(EMPTY_BYTE_BUFFER);
    }
}
