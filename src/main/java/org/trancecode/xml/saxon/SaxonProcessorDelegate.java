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
 * A delegate used to process a Saxon document and create a result tree, notably
 * used in the {@link SaxonProcessor} facility.
 * <p>
 * The methods in this delegate are called during when processing a document.
 * Each method is given the {@link XdmNode} to process and the shared
 * {@link SaxonBuilder} that can be used to create the result document.
 * </p>
 * 
 * @author Romain Deltour
 */
public interface SaxonProcessorDelegate
{
    /**
     * An enumeration used to indicate what to do after a delegate
     * {@link SaxonProcessorDelegate#startElement(XdmNode, SaxonBuilder)} method
     * has been called.
     */
    enum NextSteps {
        PROCESS_CHILDREN, PROCESS_ATTRIBUTES, START_CONTENT
    }

    /**
     * Called when starting the processing of a document.
     * 
     * @return <code>true</code> iff the processor should keep on processing the
     *         children nodes
     * @param node
     *            the document node
     * @param builder
     *            the shared builder used to create the result document
     */
    boolean startDocument(XdmNode node, SaxonBuilder builder);

    /**
     * Called after having processed a document.
     * 
     * @param node
     *            the document node
     * @param builder
     *            the shared builder used to create the result document
     */
    void endDocument(XdmNode node, SaxonBuilder builder);

    /**
     * Called when starting the processing of an element.
     * 
     * @return A set of flags indicating what to do next (e.g. process the
     *         attributes, process the children, start the element content)
     * @param node
     *            the element node
     * @param builder
     *            the shared builder used to create the result document
     */
    public EnumSet<NextSteps> startElement(XdmNode node, SaxonBuilder builder);

    /**
     * Called after having processed an element.
     * 
     * @param node
     *            the element node
     * @param builder
     *            the shared builder used to create the result document
     */
    void endElement(XdmNode node, SaxonBuilder builder);

    /**
     * Called when starting the processing of a text node.
     * 
     * @param node
     *            the text node
     * @param builder
     *            the shared builder used to create the result document
     */
    void text(XdmNode node, SaxonBuilder builder);

    /**
     * Called when starting the processing of a comment node.
     * 
     * @param node
     *            the comment node
     * @param builder
     *            the shared builder used to create the result document
     */
    void comment(XdmNode node, SaxonBuilder builder);

    /**
     * Called when starting the processing of a processing instruction node
     * 
     * @param node
     *            the processing instruction node.
     * @param builder
     *            the shared builder used to create the result document
     */
    void processingInstruction(XdmNode node, SaxonBuilder builder);

    /**
     * Called when starting the processing of an attribute node.
     * 
     * @param node
     *            the attribute node
     * @param builder
     *            the shared builder used to create the result document
     */
    void attribute(XdmNode node, SaxonBuilder builder);
}
