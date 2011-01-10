/*
 * Copyright (C) 2010 Herve Quiroz
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;
import org.trancecode.api.Nullable;

/**
 * Utility and factory methods related to {@link SaxonProcessorDelegate}.
 * 
 * @author Herve Quiroz
 */
public final class SaxonProcessorDelegates
{
    /**
     * Creates a new {@link MatchSaxonProcessorDelegate} that uses the given
     * XSLT match pattern to dispatch events to the two given delegates.
     * 
     * @param processor
     *            The Saxon processor used to initialize a new
     *            {@link SaxonBuilder} for each new document processing.
     * @param pattern
     *            The XSLT match pattern evaluated for each node
     * @param namespaceContext
     *            A node from which to retrieve namespaces.
     * @param matchDelegate
     *            The processor delegate called if the pattern matches a node
     * @param nomatchDelegate
     *            The processor delegate called if the pattern does not match a
     *            node
     */
    public static SaxonProcessorDelegate forXsltMatchPattern(final Processor processor, final String pattern,
            @Nullable final XdmNode namespaceContext, final SaxonProcessorDelegate matchDelegate,
            final SaxonProcessorDelegate nomatchDelegate)
    {
        return new MatchSaxonProcessorDelegate(new SaxonPatternMatcher(processor, pattern, namespaceContext),
                matchDelegate, nomatchDelegate);
    }

    /**
     * Creates a new {@link MatchSaxonProcessorDelegate} that dispatches events
     * to the two given delegates based on the kind of the node (e.g.
     * {@link XdmNodeKind#ELEMENT}).
     * 
     * @param nodeKinds
     *            The list of node kinds for which the events should be
     *            dispatched to {@code matchDelegate}.
     * @param matchDelegate
     *            The processor delegate called if the node is of one of the
     *            specified kinds.
     * @param nomatchDelegate
     *            The processor delegate called if the node is not of one of the
     *            specified kinds.
     */
    public static SaxonProcessorDelegate forNodeKinds(final Set<XdmNodeKind> nodeKinds,
            final SaxonProcessorDelegate matchDelegate, final SaxonProcessorDelegate nomatchDelegate)
    {
        return new MatchSaxonProcessorDelegate(SaxonPredicates.hasNodeKind(nodeKinds), matchDelegate, nomatchDelegate);
    }

    /**
     * Creates a new {@link SaxonProcessorDelegate} that throws a
     * {@link RuntimeException} for any event.
     * 
     * @param exceptionFactory
     *            a {@link Function} to build the exception that is to be thrown
     *            based on the node.
     */
    public static SaxonProcessorDelegate error(final Function<XdmNode, ? extends RuntimeException> exceptionFactory)
    {
        return new SaxonProcessorDelegate()
        {
            @Override
            public boolean startDocument(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void endDocument(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public EnumSet<NextSteps> startElement(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void endElement(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void text(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void comment(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void processingInstruction(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }

            @Override
            public void attribute(final XdmNode node, final SaxonBuilder builder)
            {
                throw exceptionFactory.apply(node);
            }
        };
    }

    public static SaxonProcessorDelegate countMatchingNodes(final AtomicInteger count)
    {
        return new SaxonProcessorDelegate()
        {
            @Override
            public boolean startDocument(final XdmNode node, final SaxonBuilder builder)
            {
                count.incrementAndGet();
                return false;
            }

            @Override
            public void endDocument(final XdmNode node, final SaxonBuilder builder)
            {
                // Ignore
            }

            @Override
            public EnumSet<NextSteps> startElement(final XdmNode node, final SaxonBuilder builder)
            {
                count.incrementAndGet();
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
                count.incrementAndGet();
            }

            @Override
            public void comment(final XdmNode node, final SaxonBuilder builder)
            {
                count.incrementAndGet();
            }

            @Override
            public void processingInstruction(final XdmNode node, final SaxonBuilder builder)
            {
                count.incrementAndGet();
            }

            @Override
            public void attribute(final XdmNode node, final SaxonBuilder builder)
            {
                count.incrementAndGet();
            }
        };
    }

    private SaxonProcessorDelegates()
    {
        // No instantiation
    }
}
