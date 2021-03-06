/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.cris.rdf.storage;

import java.sql.SQLException;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;

/**
 * Please use 
 * {@link org.dspace.app.cris.rdf.RDFUtil#generateIdentifier(Context, DSpaceObject)} and
 * {@link org.dspace.app.cris.rdf.RDFUtil#generateGraphURI(Context, DSpaceObject)} to 
 * get URIs for RDF data.
 * Please note that URIs can be generated for DSpaceObjects of the 
 * type SITE, COMMUNITY, COLLECTION or ITEM only. Currently dspace-rdf 
 * doesn't support Bundles or Bitstreams as independent entity.
 * 
 * @class{org.dspace.app.cris.rdf.RDFizer} uses a URIGenerator to generate URIs to
 * Identify DSpaceObjects in RDF. You can configure which URIGenerator should be
 * used. See DSpace documentation on how to configure RDFizer.
 * @author Pascal-Nicolas Becker (dspace -at- pascal -hyphen- becker -dot- de)
 * @see org.dspace.app.cris.rdf.RDFizer
 * @see org.dspace.app.cris.rdf.RDFUtil
 */
public interface URIGenerator {
    
    /**
     * Generate a URI that can be used to identify the specified DSpaceObject in
     * RDF data. Please note that URIs can be generated for DSpaceObjects of the 
     * type SITE, COMMUNITY, COLLECTION or ITEM only. Currently dspace-rdf 
     * doesn't support Bundles or Bitstreams as independent entity. This method
     * should work even if the DSpaceObject does not exist anymore.
     * @param context
     * @param dso
     * @return May return null, if no URI could be generated.
     * @see org.dspace.app.cris.rdf.RDFUtil#generateIdentifier(Context, DSpaceObject)
     */
    public String generateIdentifier(Context context, int type, int id, String handle, String[] identifiers)
            throws SQLException;
    
    /**
     * Shortcut for {@code generateIdentifier(context, dso.getType(), 
     * dso.getID(), dso.getHandle())}.
     * 
     * @param context
     * @param dso
     * @return May return null, if no URI could be generated.
     */
    public String generateIdentifier(Context context, DSpaceObject dso)
            throws SQLException;
}
