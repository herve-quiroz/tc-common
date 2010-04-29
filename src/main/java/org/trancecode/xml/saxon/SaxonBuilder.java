/*
 * Copyright (C) 2010 Romain Deltour
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
package org.trancecode.xml.saxon;

import net.sf.saxon.Configuration;
import net.sf.saxon.event.TreeReceiver;
import net.sf.saxon.om.NamePool;
import net.sf.saxon.om.NamespaceIterator;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.trans.XPathException;

/**
 * @author Romain Deltour
 */
public class SaxonBuilder
{
    // TODO check null params
    // TODO handle exceptions
    private final XdmDestination destination = new XdmDestination();
    private final TreeReceiver receiver;
    private final NamePool namePool;

    public SaxonBuilder(final Configuration configuration)
    {
        try
        {
            receiver = new TreeReceiver(destination.getReceiver(configuration));
            receiver.setPipelineConfiguration(configuration.makePipelineConfiguration());
            namePool = configuration.getNamePool();
            receiver.open();
        }
        catch (final SaxonApiException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void startDocument()
    {
        try
        {
            // receiver.open();
            receiver.startDocument(0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void endDocument()
    {
        try
        {
            receiver.endDocument();
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void startElement(final QName qname)
    {
        try
        {
            final int nameCode = namePool.allocate(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalName());
            receiver.startElement(nameCode, -1, -1, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void startElement(final QName qname, final XdmNode nsContext)
    {
        try
        {
            startElement(qname);
            final int[] inscopeNsCodes = NamespaceIterator.getInScopeNamespaceCodes(nsContext.getUnderlyingNode());
            for (final int nsCode : inscopeNsCodes)
            {
                receiver.namespace(nsCode, 0);
            }
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void endElement()
    {
        try
        {
            receiver.endElement();
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void startContent()
    {
        try
        {
            receiver.startContent();
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void attribute(final QName qname, final String value)
    {
        try
        {

            final int nameCode = namePool.allocate(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalName());
            receiver.attribute(nameCode, -1, value, 0, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void comment(final String comment)
    {
        try
        {
            receiver.characters(comment, 0, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void namespace(final String prefix, final String uri)
    {
        try
        {
            final int nsCode = namePool.allocateNamespaceCode((prefix != null) ? prefix : "", uri);
            receiver.namespace(nsCode, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void nodes(final XdmNode... nodes)
    {
        try
        {
            for (final XdmNode node : nodes)
            {
                receiver.append(node.getUnderlyingNode(), 0, NodeInfo.NO_NAMESPACES);
            }
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void processingInstruction(final String name, final String data)
    {
        try
        {
            receiver.processingInstruction(name, data, 0, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public void text(final String text)
    {
        try
        {
            receiver.characters(text, 0, 0);
        }
        catch (final XPathException e)
        {
            // TODO Auto-generated catch block
            throw new IllegalStateException(e);
        }
    }

    public XdmNode getNode()
    {
        return destination.getXdmNode();
    }
}
