/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.app.cris.rdf.storage;

import org.dspace.app.cris.rdf.storage.DOIURIGenerator;
import org.dspace.app.cris.rdf.storage.HandleURIGenerator;
import org.dspace.app.cris.rdf.storage.URIGenerator;

/**
 * Extends the DOIURIGenerator but uses handles as fallback to DOIs.
 * @author pbecker
 */
public class DOIHandleURIGenerator
extends DOIURIGenerator
implements URIGenerator
{

    protected final static URIGenerator fallback = new HandleURIGenerator();
    
}
