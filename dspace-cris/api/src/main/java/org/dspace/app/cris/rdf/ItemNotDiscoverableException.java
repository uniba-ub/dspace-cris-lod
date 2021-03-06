/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.app.cris.rdf;

/**
 *
 * @author Pascal-Nicolas Becker (dspace -at- pascal -hyphen- becker -dot- de)
 */
public class ItemNotDiscoverableException extends Exception {
    public ItemNotDiscoverableException()
    {
        super("The processed item is not discoverable.");
    }
}
