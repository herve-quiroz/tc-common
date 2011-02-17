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

import java.util.EnumSet;

import net.sf.saxon.s9api.XdmNode;

/**
 * A processor delegate that merely copies the processed nodes to the result
 * tree.
 * 
 * @author Romain Deltour
 */
public class CopyingSaxonProcessorDelegate implements SaxonProcessorDelegate
{
    @Override
    public void attribute(final XdmNode node, final SaxonBuilder builder)
    {
        builder.attribute(node.getNodeName(), node.getStringValue());
    }

    @Override
    public void comment(final XdmNode node, final SaxonBuilder builder)
    {
        builder.comment(node.getStringValue());
    }

    @Override
    public void endDocument(final XdmNode node, final SaxonBuilder builder)
    {
        builder.endDocument();
    }

    @Override
    public void endElement(final XdmNode node, final SaxonBuilder builder)
    {
        builder.endElement();
    }

    @Override
    public void processingInstruction(final XdmNode node, final SaxonBuilder builder)
    {
        builder.processingInstruction(node.getNodeName().getLocalName(), node.getStringValue());
    }

    @Override
    public boolean startDocument(final XdmNode node, final SaxonBuilder builder)
    {
        builder.startDocument();
        return true;
    }

    @Override
    public EnumSet<NextSteps> startElement(final XdmNode node, final SaxonBuilder builder)
    {
        builder.startElement(node.getNodeName(), node);
        return EnumSet.of(NextSteps.PROCESS_ATTRIBUTES, NextSteps.PROCESS_CHILDREN, NextSteps.START_CONTENT);
    }

    @Override
    public void text(final XdmNode node, final SaxonBuilder builder)
    {
        builder.text(node.getStringValue());
    }
}
