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
package org.trancecode.xml.saxon;

import java.util.EnumSet;

import net.sf.saxon.s9api.XdmNode;

/**
 * A {@link SaxonProcessorDelegate} implementation that deletes all the nodes.
 * 
 * @author Herve Quiroz
 */
public final class DeleteSaxonProcessorDelegate implements SaxonProcessorDelegate
{
    @Override
    public boolean startDocument(final XdmNode node, final SaxonBuilder builder)
    {
        return true;
    }

    @Override
    public void endDocument(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }

    @Override
    public EnumSet<NextSteps> startElement(final XdmNode node, final SaxonBuilder builder)
    {
        return EnumSet.noneOf(NextSteps.class);
    }

    @Override
    public void endElement(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }

    @Override
    public void text(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }

    @Override
    public void comment(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }

    @Override
    public void processingInstruction(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }

    @Override
    public void attribute(final XdmNode node, final SaxonBuilder builder)
    {
        // Ignore
    }
}
