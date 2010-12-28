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

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;

import java.net.URI;

/**
 * {@link Function} implementations related to {@link URI}.
 * 
 * @author Herve Quiroz
 */
public final class UriFunctions
{
    private UriFunctions()
    {
        // No instantiation
    }

    public static Function<URI, URI> resolveUri(final URI baseUri)
    {
        if (baseUri == null)
        {
            return Functions.identity();
        }

        return new ResolveUriFunction(baseUri);
    }

    private static class ResolveUriFunction implements Function<URI, URI>
    {
        private final URI baseUri;

        public ResolveUriFunction(final URI baseUri)
        {
            this.baseUri = Preconditions.checkNotNull(baseUri);
        }

        @Override
        public URI apply(final URI uri)
        {
            return Uris.resolve(uri, baseUri);
        }
    }

    public static Function<String, URI> resolveString(final URI baseUri)
    {
        return Functions.compose(resolveUri(baseUri), createUri());
    }

    public static Function<String, URI> createUri()
    {
        return CreateUriFunction.INSTANCE;
    }

    private static class CreateUriFunction implements Function<String, URI>
    {
        public static final CreateUriFunction INSTANCE = new CreateUriFunction();

        private CreateUriFunction()
        {
            // Singleton
        }

        @Override
        public URI apply(final String uri)
        {
            return Uris.createUri(uri);
        }
    }
}
