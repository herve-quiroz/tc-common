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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.ItemTypeFactory;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltTransformer;
import org.apache.commons.lang.StringUtils;
import org.trancecode.xml.XmlSchemaTypes;
import org.w3c.dom.Document;

/**
 * Utility methods related to Saxon.
 * 
 * @author Herve Quiroz
 */
public final class Saxon
{
    private Saxon()
    {
        // No instantiation
    }

    public static QName getAttributeAsQName(final XdmNode node, final QName attributeName)
    {
        final String value = node.getAttributeValue(attributeName);
        if (value != null)
        {
            return new QName(value, node);
        }

        return null;
    }

    public static Document asDomDocument(final XdmNode node, final Processor processor)
    {
        try
        {
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final DOMResult domResult = new DOMResult();
            final XsltTransformer transformer = processor.newXsltCompiler().compile(null).load();
            transformer.setSource(node.asSource());
            transformer.setDestination(new DOMDestination(document));
            transformer.transform();

            return (Document) domResult.getNode();
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    public static XdmNode asDocumentNode(final XdmNode node, final Processor processor)
    {
        if (node.getNodeKind() == XdmNodeKind.DOCUMENT)
        {
            return node;
        }
        try
        {
            return processor.newDocumentBuilder().build(node.asSource());
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    public static XdmNode asDocumentNode(final Processor processor, final Iterable<XdmNode> nodes)
    {
        final SaxonBuilder builder = new SaxonBuilder(processor.getUnderlyingConfiguration());
        builder.startDocument();
        builder.nodes(nodes);
        builder.endDocument();
        return builder.getNode();
    }

    public static Object nodesToString(final XdmNode... nodes)
    {
        return nodesToString(ImmutableList.copyOf(nodes));
    }

    public static Object nodesToString(final Iterable<XdmNode> nodes)
    {
        return new Object()
        {
            @Override
            public String toString()
            {
                final List<QName> qnames = Lists.newArrayList();
                for (final XdmNode node : nodes)
                {
                    if (node.getNodeKind() == XdmNodeKind.DOCUMENT)
                    {
                        qnames.add(new QName("document"));
                    }
                    else
                    {
                        qnames.add(node.getNodeName());
                    }
                }

                return qnames.toString();
            }
        };
    }

    public static XdmNode parse(final String xmlContent, final Processor processor)
    {
        final StringReader reader = new StringReader(xmlContent);
        try
        {
            return processor.newDocumentBuilder().build(new StreamSource(reader));
        }
        catch (final SaxonApiException e)
        {
            throw new IllegalStateException(e);
        }
        finally
        {
            Closeables.closeQuietly(reader);
        }
    }

    public static XdmNode getEmptyDocument(final Processor processor)
    {
        return parse("<?xml version=\"1.0\"?><document/>", processor);
    }

    public static XdmItem getUntypedXdmItem(final String value, final Processor processor)
    {
        try
        {
            return new XdmAtomicValue(value,
                    new ItemTypeFactory(processor).getAtomicType(XmlSchemaTypes.UNTYPED_ATOMIC));
        }
        catch (final SaxonApiException e)
        {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isElement(final XdmNode node)
    {
        return Preconditions.checkNotNull(node).getNodeKind().equals(XdmNodeKind.ELEMENT);
    }

    public static boolean isDocument(final XdmNode node)
    {
        final XdmNodeKind kind = node.getNodeKind();

        if (XdmNodeKind.ELEMENT.equals(kind))
        {
            return true;
        }
        else if (XdmNodeKind.DOCUMENT.equals(kind))
        {
            final Iterable<XdmNode> childs = SaxonAxis.childNodes(node);
            int nbChildsElts = 0;
            for (final XdmNode child : childs)
            {
                if (XdmNodeKind.TEXT.equals(child.getNodeKind()) && StringUtils.isNotBlank(child.toString()))
                {
                    return false;
                }
                if (XdmNodeKind.ELEMENT.equals(child.getNodeKind()))
                {
                    nbChildsElts++;
                    if (nbChildsElts > 1)
                    {
                        return false;
                    }

                }
            }
            return true;
        }

        return false;
    }

    public static Iterable<Source> asSources(final Iterable<XdmNode> nodes)
    {
        return Iterables.transform(nodes, SaxonFunctions.asSource());
    }

    /**
     * Returns {@code true} if the specified XDM value represents {@code true}.
     */
    public static boolean isTrue(final XdmValue value)
    {
        if (value.size() == 0)
        {
            return false;
        }

        if (value.size() > 1)
        {
            return true;
        }

        final XdmItem resultNode = value.iterator().next();
        if (resultNode.isAtomicValue())
        {
            try
            {
                return ((XdmAtomicValue) resultNode).getBooleanValue();
            }
            catch (final SaxonApiException e)
            {
                throw new IllegalStateException(e);
            }
        }

        // TODO what shall we do with this value then?
        throw new IllegalStateException(value.toString());
    }
}
