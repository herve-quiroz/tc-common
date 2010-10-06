/*
 * Copyright (C) 2010 TranceCode Software
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
package org.trancecode.core;

import com.google.common.base.Preconditions;

/**
 * Utility methods related to {@link Exception}.
 * 
 * @author Herve Quiroz
 */
public final class TcExceptions
{
    /**
     * Throws the passed exception or error as an unchecked exception.
     * <p>
     * If the passed exception is a checked one, it is wrapped within an
     * {@link IllegalStateException}.
     * 
     * @throws Error
     *             if the passed exception is an {@link Error}.
     * @throws RuntimeException
     *             if the passed exception is a {@link RuntimeException}.
     * @throws IllegalStateException
     *             if the passed exception is a checked one.
     */
    public static void throwUnchecked(final Throwable throwable)
    {
        Preconditions.checkNotNull(throwable);
        if (throwable instanceof RuntimeException)
        {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error)
        {
            throw (Error) throwable;
        }

        throw new IllegalStateException(throwable);
    }

    private TcExceptions()
    {
        // No instantiation
    }
}
