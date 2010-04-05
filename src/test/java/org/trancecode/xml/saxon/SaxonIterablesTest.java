/*
 * Copyright (C) 2008 TranceCode Software
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

import org.trancecode.AbstractTest;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.google.common.collect.Iterables;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests for {@link SaxonIterables}.
 * 
 * @author Herve Quiroz
 */
@Test
public class SaxonIterablesTest extends AbstractTest
{
    private XdmNode document;
    private XdmNode documentRoot;

    @BeforeTest
    public void parseDocument() throws Exception
    {
        final String documentString = "<root attribute1='value1' attribute2='value2'>TEXT<element1/><!-- comment --><element2/></root>";
        final Source source = new StreamSource(new StringReader(documentString));
        document = new Processor(false).newDocumentBuilder().build(source);
        documentRoot = SaxonIterables.childElement(document);
        AssertJUnit.assertEquals(new QName("root"), documentRoot.getNodeName());
    }

    @Test
    public void childNodes() throws Exception
    {
        final Iterable<XdmNode> childNodes = SaxonIterables.childNodes(documentRoot);
        AssertJUnit.assertEquals(2, Iterables.size(Iterables.filter(childNodes, SaxonPredicates.isAttribute())));
    }
}
