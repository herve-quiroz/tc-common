/*
 * Copyright (C) 2011 Herve Quiroz
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
package org.trancecode.xml.saxon;

import net.sf.saxon.s9api.QName;
import org.trancecode.api.Nullable;
import org.trancecode.api.ReturnsNullable;

/**
 * Utility methods related to Saxon API {@link QName}.
 * 
 * @author Herve Quiroz
 */
public final class SaxonQNames
{
    private SaxonQNames()
    {
        // No instantiation
    }

    public static String toPrefixString(final QName qname)
    {
        if (qname.getPrefix() != null)
        {
            return qname.getPrefix() + ":" + qname.getLocalName();
        }

        return qname.toString();
    }

    @ReturnsNullable
    public static javax.xml.namespace.QName asXmlQName(@Nullable final QName qname)
    {
        if (qname == null)
        {
            return null;
        }

        return new javax.xml.namespace.QName(qname.getNamespaceURI(), qname.getLocalName(), qname.getPrefix());
    }

    @ReturnsNullable
    public static QName asSaxonQName(@Nullable final javax.xml.namespace.QName qname)
    {
        if (qname == null)
        {
            return null;
        }

        return new QName(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart());
    }
}
