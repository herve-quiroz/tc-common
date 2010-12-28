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
package org.trancecode.base;

/**
 * @author Herve Quiroz
 */
public class BaseException extends RuntimeException
{
    private static final long serialVersionUID = -9019452747824422285L;

    public BaseException()
    {
        super();
    }

    public BaseException(final String message, final Object... parameters)
    {
        super(format(message, parameters));
    }

    public BaseException(final Throwable cause)
    {
        super(cause);
    }

    public BaseException(final Throwable cause, final String message, final Object... parameters)
    {
        super(format(message, parameters), cause);
    }

    protected static String format(final String message, final Object... parameters)
    {
        if (parameters == null || parameters.length == 0)
        {
            return message;
        }

        try
        {
            return String.format(message, parameters);
        }
        catch (final Exception e)
        {
            return message;
        }
    }
}
