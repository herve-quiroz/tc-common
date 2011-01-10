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
package org.trancecode.xml.saxon;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Map;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;

/**
 * Utility methods related to {@link Map} and Saxon.
 * 
 * @author Herve Quiroz
 */
public final class SaxonMaps
{
    private SaxonMaps()
    {
        // No instantiation
    }

    public static Map<QName, String> attributes(final XdmNode element)
    {
        Preconditions.checkArgument(Saxon.isElement(element));
        return attributes(SaxonAxis.attributes(element));
    }

    public static Map<QName, String> attributes(final Iterable<XdmNode> attributes)
    {
        assert Iterables.all(attributes, SaxonPredicates.isAttribute());
        final Map<QName, XdmNode> nodeMap = Maps.uniqueIndex(attributes, SaxonFunctions.getNodeName());
        return Maps.transformValues(nodeMap, SaxonFunctions.getStringValue());
    }
}
