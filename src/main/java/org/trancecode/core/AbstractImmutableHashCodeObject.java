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
package org.trancecode.core;

/**
 * @author Herve Quiroz
 * @version $Revision$
 */
public abstract class AbstractImmutableHashCodeObject extends AbstractImmutableObject
{
	private int hashCode;
	private boolean hashCodeComputed = false;


	public AbstractImmutableHashCodeObject(final Object... properties)
	{
		super(properties);
	}


	@Override
	public boolean equals(final Object o)
	{
		// To comply with Object.hashCode() and Object.equals() contract
		return super.equals(o);
	}


	@Override
	public int hashCode()
	{
		if (!hashCodeComputed)
		{
			hashCode = super.hashCode();
			hashCodeComputed = true;
		}

		return hashCode;
	}
}
