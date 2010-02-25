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

import org.trancecode.annotation.Immutable;
import org.trancecode.xml.Location;

import net.sf.saxon.s9api.XdmNode;


/**
 * @author Herve Quiroz
 * @version $Revision$
 */
@Immutable
public class SaxonLocation implements Location
{
	private static final long serialVersionUID = 7136827002407040675L;

	public final XdmNode node;

	private String toString;


	public SaxonLocation(final XdmNode node)
	{
		this.node = node;
	}


	public String getSystemId()
	{
		return node.getUnderlyingNode().getSystemId();
	}


	public int getColumnNumber()
	{
		return node.getUnderlyingNode().getColumnNumber();
	}


	public int getLineNumber()
	{
		return node.getUnderlyingNode().getLineNumber();
	}


	public String getPublicId()
	{
		return null;
	}


	@Override
	public String toString()
	{
		if (toString == null)
		{
			toString = String.format("line %s, column %s in %s", getLineNumber(), getColumnNumber(), getSystemId());
		}

		return toString;
	}
}
