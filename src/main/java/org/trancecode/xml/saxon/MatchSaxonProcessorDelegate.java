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

import com.google.common.base.Predicate;

import java.util.EnumSet;

import net.sf.saxon.s9api.XdmNode;

/**
 * A processor delegate that dispatches to internal sub-delegates depending on
 * the evaluation of a predicate on the node.
 * 
 * @author Romain Deltour
 * @author Herve Quiroz
 */
public class MatchSaxonProcessorDelegate implements SaxonProcessorDelegate
{
    private final Predicate<XdmNode> predicate;
    private final SaxonProcessorDelegate matchDelegate;
    private final SaxonProcessorDelegate nomatchDelegate;

    /**
     * Creates a new instance that uses the given predicate to dispatch events
     * to the two given delegates.
     * 
     * @param pattern
     *            The XSLT match pattern evaluated for each node
     * @param matchDelegate
     *            The processor delegate called if the pattern matches a node
     * @param nomatchDelegate
     *            The processor delegate called if the pattern does not match a
     *            node
     */
    public MatchSaxonProcessorDelegate(final Predicate<XdmNode> predicate, final SaxonProcessorDelegate matchDelegate,
            final SaxonProcessorDelegate nomatchDelegate)
    {
        this.matchDelegate = matchDelegate;
        this.nomatchDelegate = nomatchDelegate;
        this.predicate = predicate;
    }

    @Override
    public void attribute(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.attribute(node, builder);
        }
        else
        {
            nomatchDelegate.attribute(node, builder);
        }
    }

    @Override
    public void comment(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.comment(node, builder);
        }
        else
        {
            nomatchDelegate.comment(node, builder);
        }
    }

    @Override
    public void endDocument(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.endDocument(node, builder);
        }
        else
        {
            nomatchDelegate.endDocument(node, builder);
        }
    }

    @Override
    public void endElement(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.endElement(node, builder);
        }
        else
        {
            nomatchDelegate.endElement(node, builder);
        }
    }

    @Override
    public void processingInstruction(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.processingInstruction(node, builder);
        }
        else
        {
            nomatchDelegate.processingInstruction(node, builder);
        }
    }

    @Override
    public boolean startDocument(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            return matchDelegate.startDocument(node, builder);
        }
        else
        {
            return nomatchDelegate.startDocument(node, builder);
        }
    }

    @Override
    public EnumSet<NextSteps> startElement(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            return matchDelegate.startElement(node, builder);
        }
        else
        {
            return nomatchDelegate.startElement(node, builder);
        }
    }

    @Override
    public void text(final XdmNode node, final SaxonBuilder builder)
    {
        if (predicate.apply(node))
        {
            matchDelegate.text(node, builder);
        }
        else
        {
            nomatchDelegate.text(node, builder);
        }
    }
}
