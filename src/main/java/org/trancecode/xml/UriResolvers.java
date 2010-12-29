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
package org.trancecode.xml;

import com.google.common.base.Preconditions;

import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import org.trancecode.io.InputResolver;
import org.trancecode.io.Uris;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Utility methods related to {@link URIResolver}.
 * 
 * @author Herve Quiroz
 * @version $Revision: 3842 $
 */
public final class UriResolvers
{
    private UriResolvers()
    {
        // No instantiation
    }

    public static URIResolver newUriResolver(final InputResolver inputResolver)
    {
        return new EntityResolverURIResolver(inputResolver);
    }

    private static class EntityResolverURIResolver implements URIResolver
    {
        private final InputResolver inputResolver;
        private final EntityResolver entityResolver;

        public EntityResolverURIResolver(final InputResolver inputResolver)
        {
            this.inputResolver = Preconditions.checkNotNull(inputResolver);
            entityResolver = EntityResolvers.newEntityResolver(inputResolver);
        }

        @Override
        public Source resolve(final String href, final String base) throws TransformerException
        {
            final URI uri = Uris.resolve(href, base);
            if (uri == null)
            {
                throw new TransformerException(String.format("not a valid URI ; href = %s ; base = %s", href, base));
            }

            final InputSource inputSource = new InputSource(inputResolver.resolveInputStream(uri));
            inputSource.setSystemId(uri.toString());

            try
            {
                final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                xmlReader.setEntityResolver(entityResolver);

                final SAXSource source = new SAXSource(xmlReader, inputSource);
                source.setSystemId(uri.toString());

                return source;
            }
            catch (final SAXException e)
            {
                Sax.closeQuietly(inputSource);
                throw new TransformerException(String.format("href = %s ; base = %s", href, base), e);
            }
        }
    }

    /**
     * Resolves a pair of URIs as a {@link Source}.
     * <p>
     * This method is similar to {@link URIResolver#resolve(String, String)} but
     * handles {@link URI} arguments and only throws unchecked exceptions. The
     * method also handles {@code null} arguments silently.
     */
    public static Source resolve(final URIResolver resolver, final URI href, final URI base)
    {
        final String hrefString;
        if (href != null)
        {
            hrefString = href.toString();
        }
        else
        {
            hrefString = "";
        }

        final String baseString;
        if (base != null)
        {
            baseString = base.toString();
        }
        else
        {
            baseString = "";
        }

        try
        {
            return resolver.resolve(hrefString, baseString);
        }
        catch (final TransformerException e)
        {
            throw new XmlException(e, "cannot resolve '%' against '%s'", hrefString, baseString);
        }
    }
}
