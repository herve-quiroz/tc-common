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

import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import org.trancecode.api.Immutable;

/**
 * @author Herve Quiroz
 */
@Immutable
public class Namespace
{
    private final String uri;
    private final String prefix;

    public Namespace(final String uri, final String prefix)
    {
        this.uri = uri;
        this.prefix = prefix;
    }

    public String uri()
    {
        return uri;
    }

    public String prefix()
    {
        return prefix;
    }

    public QName newSaxonQName(final String localName)
    {
        if (uri != null)
        {
            if (prefix != null)
            {
                return new QName(prefix, uri, localName);
            }

            return new QName(uri, localName);
        }

        return new QName(localName);
    }

    public StructuredQName newStructuredQName(final String localName)
    {
        return new StructuredQName(prefix, uri, localName);
    }
}
