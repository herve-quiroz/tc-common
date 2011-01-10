/*
 * Copyright (C) 2008 Herve Quiroz
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
package org.trancecode.xml;

import com.google.common.io.Closeables;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.trancecode.logging.Logger;

/**
 * Utility methods related to JAXP (Java API for XML Processing).
 * 
 * @author Herve Quiroz
 */
public final class Jaxp
{
    private Jaxp()
    {
        // To prevent instantiation
    }

    public static void closeQuietly(final Source source)
    {
        closeQuietly(source, null);
    }

    public static void closeQuietly(final Source source, final Logger logger)
    {
        if (source instanceof StreamSource)
        {
            final StreamSource streamSource = (StreamSource) source;
            try
            {
                Closeables.close(streamSource.getInputStream(), false);
                Closeables.close(streamSource.getReader(), false);
            }
            catch (final IOException e)
            {
                if (logger != null)
                {
                    logger.warn(e.toString(), e);
                }
            }
        }
    }
}
