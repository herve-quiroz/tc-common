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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.AbstractTest;

/**
 * @author Romain Deltour
 */
@Test
public class SaxonPatternMatcherTest extends AbstractTest
{
    private final Processor processor = new Processor(false);

    @Test(expectedExceptions = { NullPointerException.class })
    public void nullProcessor() throws Exception
    {
        new SaxonPatternMatcher(null, "pattern");
    }

    @Test(expectedExceptions = { NullPointerException.class })
    public void nullPattern() throws Exception
    {
        new SaxonPatternMatcher(processor, null);
    }

    @Test(expectedExceptions = { NullPointerException.class })
    public void matchNullNode() throws Exception
    {
        final SaxonPatternMatcher matcher = new SaxonPatternMatcher(processor, "pattern");
        matcher.match(null);
    }

    @Test
    public void match() throws Exception
    {
        final XdmNode doc = Saxon.parse("<doc><elem/></doc>", processor);
        final XdmNode node = select("/doc/elem", doc);
        final SaxonPatternMatcher matcher = new SaxonPatternMatcher(processor, "elem");
        Assert.assertTrue(matcher.match(node));
    }

    @Test
    public void noMatch() throws Exception
    {
        final XdmNode doc = Saxon.parse("<doc><elem/></doc>", processor);
        final XdmNode node = select("/doc/elem", doc);
        final SaxonPatternMatcher matcher = new SaxonPatternMatcher(processor, "item");
        Assert.assertFalse(matcher.match(node));
    }

    private XdmNode select(final String select, final XdmNode xpathContext)
    {

        try
        {
            final XPathCompiler xpathCompiler = processor.newXPathCompiler();
            final XPathSelector selector = xpathCompiler.compile(select).load();
            selector.setContextItem(xpathContext);
            final XdmValue value = selector.evaluate();
            assert value != null;
            assert value instanceof XdmNode;
            return (XdmNode) value;
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("error while evaluating XPath query: " + select, e);
        }
    }
}
