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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XdmNode;
import org.trancecode.annotation.Nullable;

/**
 * Utility and factory methods related to {@link SaxonProcessorDelegate}.
 * 
 * @author Herve Quiroz
 */
public final class SaxonProcessorDelegates
{
    /**
     * Creates a new ${@link MatchSaxonProcessorDelegate} that uses the given
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

    private SaxonProcessorDelegates()
    {
        // No instantiation
    }
}
