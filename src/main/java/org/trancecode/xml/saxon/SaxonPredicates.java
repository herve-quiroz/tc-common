/*
 * Copyright (C) 2008 Herve Quiroz
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
 *
 * $Id$
 */
package org.trancecode.xml.saxon;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;
import org.trancecode.function.TcPredicates;

/**
 * {@link Predicate} implementations related to Saxon.
 * 
 * @author Herve Quiroz
 */
public final class SaxonPredicates
{
    private SaxonPredicates()
    {
        // No instantiation
    }

    private static final Predicate<XdmNode> IS_ELEMENT = Predicates.compose(Predicates.equalTo(XdmNodeKind.ELEMENT),
            SaxonFunctions.getNodeKind());

    public static Predicate<XdmNode> isElement()
    {
        return IS_ELEMENT;
    }

    private static final Predicate<XdmNode> IS_ATTRIBUTE = Predicates.compose(
            Predicates.equalTo(XdmNodeKind.ATTRIBUTE), SaxonFunctions.getNodeKind());

    public static Predicate<XdmNode> isAttribute()
    {
        return IS_ATTRIBUTE;
    }

    public static Predicate<XdmNode> hasNodeKind(final Iterable<XdmNodeKind> nodeKinds)
    {
        return Predicates.compose(TcPredicates.isContainedBy(ImmutableSet.copyOf(nodeKinds)),
                SaxonFunctions.getNodeKind());
    }
}
