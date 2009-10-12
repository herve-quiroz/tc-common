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
package org.trancecode.xml.saxon;

import java.util.Map;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;


/**
 * Utility methods related to {@link Map} and Saxon.
 * 
 * @author Herve Quiroz
 * @version $Revision$
 */
public final class SaxonMaps
{
	private SaxonMaps()
	{
		// No instantiation
	}


	public static Map<QName, String> attributes(final Iterable<XdmNode> nodes)
	{
		assert Iterables.all(nodes, SaxonPredicates.isAttribute());
		final Map<QName, XdmNode> nodeMap = Maps.uniqueIndex(nodes, SaxonFunctions.getNodeName());
		return Maps.transformValues(nodeMap, SaxonFunctions.getStringValue());
	}
}
