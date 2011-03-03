/*
 * Copyright (C) 2011 Emmanuel Tourdot
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
package org.trancecode.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;

/**
 * @author Emmanuel Tourdot
 */
class TubularSessionInputBuffer extends AbstractSessionInputBuffer
{
    public static final int BUFFER_SIZE = 16;

    public TubularSessionInputBuffer(final InputStream instream, final int buffersize,
            final HttpParams params)
    {
        super();
        init(instream, buffersize, params);
    }

    public TubularSessionInputBuffer(final InputStream instream, final int buffersize)
    {
        this(instream, buffersize, new BasicHttpParams());
    }

    public TubularSessionInputBuffer(final byte[] bytes, final HttpParams params)
    {
        this(bytes, BUFFER_SIZE, params);
    }

    public TubularSessionInputBuffer(final byte[] bytes)
    {
        this(bytes, BUFFER_SIZE, new BasicHttpParams());
    }

    public TubularSessionInputBuffer(final byte[] bytes, final int buffersize,
            final HttpParams params)
    {
        this(new ByteArrayInputStream(bytes), buffersize, params);
    }

    public TubularSessionInputBuffer(final byte[] bytes, final int buffersize)
    {
        this(new ByteArrayInputStream(bytes), buffersize, new BasicHttpParams());
    }

    public TubularSessionInputBuffer(final String s, final String charset, final int buffersize, final HttpParams params)
        throws UnsupportedEncodingException
    {
        this(s.getBytes(charset), buffersize, params);
    }

    public TubularSessionInputBuffer(final String s, final String charset, final int buffersize)
        throws UnsupportedEncodingException
    {
        this(s.getBytes(charset), buffersize, new BasicHttpParams());
    }

    public TubularSessionInputBuffer(final String s, final String charset, final HttpParams params)
        throws UnsupportedEncodingException
    {
        this(s.getBytes(charset), params);
    }

    public TubularSessionInputBuffer(final String s, final String charset)
        throws UnsupportedEncodingException
    {
        this(s.getBytes(charset), new BasicHttpParams());

    }

    public boolean isDataAvailable(final int timeout) throws IOException
    {
        return true;
    }
}
