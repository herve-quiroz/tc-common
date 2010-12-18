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

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;
import org.trancecode.logging.Logger;

/**
 * Utility methods related to {@link SaxonBuilder}.
 * 
 * @author Herve Quiroz
 */
public final class SaxonBuilders
{
    private static final Logger LOG = Logger.getLogger(SaxonBuilders.class);

    public static final QName addAttribute(final QName name, final String value, final XdmNode namespaceContext,
            final SaxonBuilder builder)
    {
        // Namespace Fixup
        QName fixedupQName = name;
        boolean isNamespaceDeclared = false;
        boolean shouldChangePrefix = false;
        final Iterator<XdmNode> inscopeNamespaces = SaxonAxis.namespaces(namespaceContext).iterator();
        final List<String> inscopePrefixes = Lists.newLinkedList();
        // Check if the namespace is already declared...
        while (!isNamespaceDeclared && inscopeNamespaces.hasNext())
        {
            final XdmNode inscopeNamespace = inscopeNamespaces.next();
            final String inscopeNamespacePrefix = inscopeNamespace.getNodeName().getLocalName();
            final String inscopeNamespaceUri = inscopeNamespace.getStringValue();
            if (inscopeNamespaceUri.equals(fixedupQName.getNamespaceURI()))
            {
                LOG.trace("Namespace {} already declared", fixedupQName.getNamespaceURI());
                isNamespaceDeclared = true;
                shouldChangePrefix = false;
                if (!inscopeNamespacePrefix.equals(fixedupQName.getPrefix()))
                {
                    LOG.trace("Prefix '{}' changed to existing prefix '{}'", fixedupQName.getNamespaceURI(),
                            inscopeNamespacePrefix);
                    fixedupQName = new QName(inscopeNamespacePrefix, inscopeNamespaceUri, fixedupQName.getLocalName());
                }
            }
            else if (inscopeNamespacePrefix.equals(fixedupQName.getPrefix()))
            {
                LOG.trace("Prefix '{}' already in use for namespace '{}'", inscopeNamespacePrefix, inscopeNamespaceUri);
                shouldChangePrefix = true;
            }
            inscopePrefixes.add(inscopeNamespacePrefix);
        }
        // If the attribute namespace has no prefix, create a dummy one
        if (!isNamespaceDeclared && "".equals(fixedupQName.getPrefix()) && !"".equals(fixedupQName.getNamespaceURI()))
        {
            fixedupQName = new QName("ns", fixedupQName.getNamespaceURI(), fixedupQName.getLocalName());
            shouldChangePrefix = true;
        }
        if (shouldChangePrefix)
        {
            final int count = 1;
            String newPrefix = fixedupQName.getPrefix();
            while (shouldChangePrefix)
            {
                newPrefix = newPrefix + count;
                shouldChangePrefix = false;
                final Iterator<String> prefixIterator = inscopePrefixes.iterator();
                while (!shouldChangePrefix && prefixIterator.hasNext())
                {
                    if (newPrefix.equals(prefixIterator.next()))
                    {
                        shouldChangePrefix = true;
                    }
                }
            }
            fixedupQName = new QName(newPrefix, fixedupQName.getNamespaceURI(), fixedupQName.getLocalName());
        }

        // If the attribute namespace is not declared, explicitly declare it
        if (!isNamespaceDeclared)
        {
            builder.namespace(fixedupQName.getPrefix(), fixedupQName.getNamespaceURI());
        }

        // Do add the attribute
        builder.attribute(fixedupQName, value);

        return fixedupQName;
    }

    private SaxonBuilders()
    {
        // No instantiation
    }
}
