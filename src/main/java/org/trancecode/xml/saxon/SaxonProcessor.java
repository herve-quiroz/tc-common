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

import com.google.common.base.Function;

import java.util.EnumSet;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XdmNode;
import org.trancecode.logging.Logger;
import org.trancecode.xml.saxon.SaxonProcessorDelegate.NextSteps;

/**
 * A processor that walks over s9api documents and calls a delegate on each
 * encountered node, possibly returning a result document.
 * <p>
 * Before each document processing, the processor initializes a
 * {@link SaxonBuilder} that is passed to the delegate to create the result
 * tree.
 * </p>
 * <p>
 * The processor is thread-safe (i.e. can process independent documents
 * concurrently) if and only if the delegate {@link SaxonProcessorDelegate} is
 * thread-safe.
 * </p>
 * 
 * @author Romain Deltour
 * @see SaxonBuilder
 * @see SaxonProcessorDelegate
 */
public final class SaxonProcessor implements Function<XdmNode, XdmNode>
{
    private static final Logger LOG = Logger.getLogger(SaxonProcessor.class);

    private final Processor processor;
    private final SaxonProcessorDelegate delegate;

    /**
     * Creates a new document processor using the given delegate.
     * 
     * @param processor
     *            The Saxon processor used to initialize a new
     *            {@link SaxonBuilder} for each new document processing
     * @param delegate
     *            The delegate that will be called by this processor
     */
    public SaxonProcessor(final Processor processor, final SaxonProcessorDelegate delegate)
    {
        this.processor = processor;
        this.delegate = delegate;
    }

    /**
     * Walks over the given s9api document and return the result of the
     * processing.
     * <p>
     * For each visited node, the processing is delegated to the internal
     * {@link SaxonProcessorDelegate}.
     * </p>
     * <p>
     * A shared {@link SaxonBuilder} instance is created and can be used by the
     * delegate to build the returned result tree.
     * </p>
     * 
     * @param node
     *            A s9api document node to process
     * @return The tree built while processing the document
     */
    public XdmNode apply(final XdmNode node)
    {
        final SaxonBuilder nodeBuilder = new SaxonBuilder(processor.getUnderlyingConfiguration());
        doProcess(node, nodeBuilder, delegate);
        return nodeBuilder.getNode();
    }

    private void doProcess(final XdmNode node, final SaxonBuilder builder, final SaxonProcessorDelegate delegate)
    {
        switch (node.getNodeKind())
        {
            case DOCUMENT:
                final boolean processChildren = delegate.startDocument(node, builder);
                if (processChildren)
                {
                    for (final XdmNode child : SaxonAxis.childNodes(node))
                    {
                        doProcess(child, builder, delegate);
                    }
                }
                delegate.endDocument(node, builder);
                break;
            case ELEMENT:
                final EnumSet<NextSteps> nextSteps = delegate.startElement(node, builder);
                if (nextSteps.contains(NextSteps.PROCESS_ATTRIBUTES))
                {
                    for (final XdmNode child : SaxonAxis.attributes(node))
                    {
                        doProcess(child, builder, delegate);
                    }
                }
                if (nextSteps.contains(NextSteps.START_CONTENT))
                {
                    builder.startContent();
                }
                if (nextSteps.contains(NextSteps.PROCESS_CHILDREN))
                {
                    for (final XdmNode child : SaxonAxis.childNodesNoAttributes(node))
                    {
                        doProcess(child, builder, delegate);
                    }
                }
                delegate.endElement(node, builder);
                break;
            case ATTRIBUTE:
                delegate.attribute(node, builder);
                break;
            case TEXT:
                delegate.text(node, builder);
                break;
            case COMMENT:
                delegate.comment(node, builder);
                break;
            case PROCESSING_INSTRUCTION:
                delegate.processingInstruction(node, builder);
                break;
            default:
                LOG.warn("Unexpected node kind: {}", node.getNodeKind());
                break;
        }
    }
}
