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

import com.google.common.base.Preconditions;

import java.io.InputStream;
import java.net.URI;

/**
 * @author Herve Quiroz
 */
public final class ClasspathInputResolver extends AbstractInputResolver
{
    public static final String URI_SCHEME = "classpath";
    private static final ClasspathInputResolver INSTANCE = new ClasspathInputResolver();

    public static ClasspathInputResolver instance()
    {
        return INSTANCE;
    }

    private ClasspathInputResolver()
    {
        // Singleton
    }

    @Override
    public InputStream resolveInputStream(final URI uri)
    {
        Preconditions.checkArgument(uri.getScheme().equals("classpath"), "unsupported URI scheme: %s", uri);
        Preconditions.checkArgument(!uri.isOpaque(), "URI is opaque: %s", uri);

        return getClass().getResourceAsStream(uri.getPath());
    }
}
