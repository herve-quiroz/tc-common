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
package org.trancecode.xml;

import javax.xml.namespace.QName;

/**
 * Utility methods related to {@link QName}.
 * 
 * @author Herve Quiroz
 */
public final class QNames
{
    private QNames()
    {
        // No instantiation
    }

    /**
     * Workaround for a bug in PMD.
     * 
     * @see <a
     *      href="http://sourceforge.net/p/pmd/discussion/188192/thread/651de749">AvoidThreadGroup
     *      Violations in PMD 4.2.6</a>
     */
    public static QName newQName(final String namespaceURI, final String localPart, final String prefix)
    {
        return new QName(namespaceURI, localPart, prefix);
    }
}
