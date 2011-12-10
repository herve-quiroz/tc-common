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

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;

/**
 * @author Emmanuel Tourdot
 */
public class BodypartResponseParser
{
    private InputStream stream;
    private String boundary;
    private HttpParams params;
    private String contentType;
    private String partContentType;
    private String charset;
    private String partCharset;
    private final TubularSessionInputBuffer sessionBuffer;
    private final HeaderGroup headers;

    public BodypartResponseParser(final InputStream stream, final String boundary, final HttpParams params,
            final String contentType, final String charset)
    {
        this.stream = stream;
        this.boundary = "--" + boundary;
        this.params = params;
        this.contentType = contentType;
        this.charset = charset;
        sessionBuffer = new TubularSessionInputBuffer(stream, 4096, params == null ? new BasicHttpParams() : params);
        this.headers = new HeaderGroup();
    }

    private void passPreamble()
    {
        String line;
        try
        {
            do
            {
                line = sessionBuffer.readLine();
            }
            while (line != null && !StringUtils.equals(boundary, line));
        }
        catch (IOException e)
        {
        }
    }

    public List<BodypartEntity> parseMultipart()
    {
        final List<BodypartEntity> entities = Lists.newArrayList();
        try
        {
            passPreamble();
            BodypartEntity part = parseBodypart(true);
            while (part != null)
            {
                entities.add(part);
                headers.clear();
                part = parseBodypart(true);
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return entities;
    }

    public BodypartEntity parseBodypart(final boolean hasHeaders) throws IOException
    {
        if (hasHeaders)
        {
            parseHeaders();
        }
        if (isBinary())
        {
            if (headers.containsHeader(HttpHeaders.CONTENT_LENGTH))
            {
                int length = Integer.parseInt(headers.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue());
                final byte[] in = new byte[length];
                int off = 0;
                while (length > 0)
                {
                    final int lect = sessionBuffer.read(in, off, length);
                    if (lect == -1)
                    {
                        break;
                    }
                    off += lect;
                    length -= lect;
                }
                sessionBuffer.readLine();
                sessionBuffer.readLine();
                return new BodypartEntity(new ByteArrayEntity(in), hasHeaders ? headers.copy() : null);
            }
            else
            {
                final ByteArrayBuffer buffer = new ByteArrayBuffer(40);
                final byte[] in = new byte[40];
                while (true)
                {
                    final int lect = sessionBuffer.read(in, 0, 40);
                    if (lect == -1)
                    {
                        break;
                    }
                    buffer.append(in, 0, lect);
                }
                if (!buffer.isEmpty())
                {
                    sessionBuffer.readLine();
                    sessionBuffer.readLine();
                    return new BodypartEntity(new ByteArrayEntity(buffer.toByteArray()), hasHeaders ? headers.copy()
                            : null);
                }
            }
        }
        else
        {
            final String ch = partCharset == null ? charset : partCharset;
            final String mime = partContentType == null ? contentType : partContentType;
            final StringBuilder builder = new StringBuilder();
            boolean finish = false;
            do
            {
                final String line = sessionBuffer.readLine();
                if (line == null || StringUtils.startsWith(line, boundary))
                {
                    finish = true;
                }
                else
                {
                    if (builder.length() > 0 && !isBinary())
                    {
                        builder.append("\n");
                    }
                    builder.append(line);
                }
            }
            while (!finish);
            if (builder.length() > 0)
            {
                return new BodypartEntity(new StringEntity(builder.toString(), mime, ch), hasHeaders ? headers.copy()
                        : null);
            }
        }
        return null;
    }

    private void parseHeaders()
    {
        try
        {
            do
            {
                final String line = sessionBuffer.readLine();
                if (StringUtils.isBlank(line))
                {
                    return;
                }
                final Header header = BasicLineParser.parseHeader(line, null);
                if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(header.getName()))
                {
                    final HeaderElement[] values = header.getElements();
                    if (values.length > 0)
                    {
                        partContentType = values[0].getName();
                        final NameValuePair param = values[0].getParameterByName("charset");
                        if (param != null)
                        {
                            partCharset = param.getValue();
                        }
                    }
                }
                headers.addHeader(header);
            }
            while (true);
        }
        catch (IOException e)
        {
        }
        catch (ParseException e)
        {
        }
    }

    public class BodypartEntity
    {
        private final HttpEntity entity;
        private final HeaderGroup headerGroup;

        public BodypartEntity(final HttpEntity entity, final HeaderGroup headerGroup)
        {
            this.entity = entity;
            this.headerGroup = headerGroup;
        }

        public HttpEntity getEntity()
        {
            return entity;
        }

        public HeaderGroup getHeaderGroup()
        {
            return headerGroup;
        }
    }

    private boolean isBinary()
    {
        if (partContentType != null)
        {
            return !partContentType.endsWith("xml") && !partContentType.startsWith("text");
        }
        else
        {
            if (contentType != null)
            {
                return !contentType.startsWith("multipart") && !contentType.endsWith("xml")
                        && !contentType.startsWith("text");
            }
        }
        return false;
    }

    public void setBoundary(final String boundary)
    {
        this.boundary = "--" + boundary;
    }

    public void setStream(final InputStream stream)
    {
        this.stream = stream;
    }

    public void setCharset(final String charset)
    {
        this.charset = charset;
    }

    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }
}
