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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.saxon.om.InscopeNamespaceResolver;
import net.sf.saxon.s9api.XdmNode;
import org.trancecode.collection.TcMaps;

/**
 * Utility methods realted to namespaces for Saxon.
 * 
 * @author Herve Quiroz
 */
public final class SaxonNamespaces
{
    public static Iterable<String> prefixes(final XdmNode node)
    {
        return new Iterable<String>()
        {
            @Override
            public Iterator<String> iterator()
            {
                final InscopeNamespaceResolver namespaceResolver = new InscopeNamespaceResolver(
                        node.getUnderlyingNode());
                @SuppressWarnings("unchecked")
                final Iterator<String> prefixes = namespaceResolver.iteratePrefixes();
                return prefixes;
            }
        };
    }

    public static Iterable<Entry<String, String>> namespaceSequence(final XdmNode node)
    {
        final InscopeNamespaceResolver namespaceResolver = new InscopeNamespaceResolver(node.getUnderlyingNode());
        return Iterables.transform(prefixes(node), new Function<String, Entry<String, String>>()
        {
            @Override
            public Entry<String, String> apply(final String prefix)
            {
                return Maps.immutableEntry(prefix, namespaceResolver.getURIForPrefix(prefix, true));
            }
        });
    }

    public static Map<String, String> namespaceMap(final XdmNode node)
    {
        return TcMaps.fromEntries(namespaceSequence(node));
    }

    private SaxonNamespaces()
    {
        // No instantiation
    }
}
