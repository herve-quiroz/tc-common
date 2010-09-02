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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import javax.xml.transform.Source;

import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;

/**
 * {@link Function} implementations related to Saxon.
 * 
 * @author Herve Quiroz
 */
public final class SaxonFunctions
{
    private SaxonFunctions()
    {
        // No instantiation
    }

    public static Function<XdmNode, XdmNodeKind> getNodeKind()
    {
        return GetNodeKindFunction.INSTANCE;
    }

    private static final class GetNodeKindFunction implements Function<XdmNode, XdmNodeKind>
    {
        private static GetNodeKindFunction INSTANCE = new GetNodeKindFunction();

        @Override
        public XdmNodeKind apply(final XdmNode node)
        {
            return node.getNodeKind();
        }
    }

    public static Function<XdmNode, Iterable<XdmItem>> childAxis()
    {
        return AxisFunction.CHILD;
    }

    public static Function<XdmNode, Iterable<XdmItem>> axis(final Axis axis)
    {
        return new AxisFunction(axis);
    }

    private static final class AxisFunction implements Function<XdmNode, Iterable<XdmItem>>
    {
        private static final AxisFunction CHILD = new AxisFunction(Axis.CHILD);

        private final Axis axis;

        public AxisFunction(final Axis axis)
        {
            this.axis = Preconditions.checkNotNull(axis);
        }

        @Override
        public Iterable<XdmItem> apply(final XdmNode node)
        {
            return SaxonAxis.axis(node, axis);
        }
    }

    public static Function<XdmNode, String> getStringValue()
    {
        return GetStringValueFunction.INSTANCE;
    }

    private static final class GetStringValueFunction implements Function<XdmNode, String>
    {
        private static GetStringValueFunction INSTANCE = new GetStringValueFunction();

        @Override
        public String apply(final XdmNode node)
        {
            return node.getStringValue();
        }
    }

    public static Function<XdmNode, QName> getNodeName()
    {
        return GetNodeNameFunction.INSTANCE;
    }

    private static final class GetNodeNameFunction implements Function<XdmNode, QName>
    {
        private static GetNodeNameFunction INSTANCE = new GetNodeNameFunction();

        @Override
        public QName apply(final XdmNode node)
        {
            return node.getNodeName();
        }
    }

    public static Function<Source, XdmNode> buildDocument(final Processor processor)
    {
        return new Function<Source, XdmNode>()
        {
            private DocumentBuilder documentBuilder;

            @Override
            public XdmNode apply(final Source source)
            {
                Preconditions.checkNotNull(source);

                if (documentBuilder == null)
                {
                    documentBuilder = processor.newDocumentBuilder();
                }

                try
                {
                    return documentBuilder.build(source);
                }
                catch (final SaxonApiException e)
                {
                    throw new IllegalStateException("error building document from: " + source.getSystemId(), e);
                }
            }
        };
    }
}
