/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.general;

import org.apache.cocoon.ResourceNotFoundException;
import org.apache.cocoon.environment.http.HttpEnvironment;
import org.apache.commons.lang.reflect.FieldUtils;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingConstants;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.authorize.AuthorizeException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This special component checks to see if the body element is empty (has no sub elements) and if
 * it is then displays some page not found text.
 * 
 * @author Scott Phillips
 * @author Kim Shepherd
 */
public class PageNotFoundTransformer extends AbstractDSpaceTransformer
{
    /** Language Strings */
    private static final Message T_title =
        message("xmlui.PageNotFound.title");
    
    private static final Message T_head =
        message("xmlui.PageNotFound.head");
    
    private static final Message T_para1 =
        message("xmlui.PageNotFound.para1");
    
    private static final Message T_go_home =
        message("xmlui.general.go_home");
    
    private static final Message T_dspace_home =
        message("xmlui.general.dspace_home");
    
    
    /** Where the body element is stored while we wait to see if it is empty */
    private SAXEvent bodyEvent;
    
    /** Have we determined that the body is empty, and hence a we should generate a page not found. */
    private boolean bodyEmpty;
    
    /**
     * Receive notification of the beginning of a document.
     */
    @Override
    public void startDocument() throws SAXException
    {
        // Reset our parameters before starting a new document.
        this.bodyEvent = null;
        this.bodyEmpty = false;
        super.startDocument();
    }

    /**
     * Process the SAX event.
     * @see org.xml.sax.ContentHandler#startElement
     */
    @Override
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) throws SAXException
    {          
        if (this.bodyEvent != null)
        {
            // If we have recorded the startElement for body and we are
            // receiving another start event, then there must be something 
            // inside the body element, so we send the held body and carry
            // on as normal.
            sendEvent(this.bodyEvent);
            this.bodyEvent = null;
        }
        
        if (WingConstants.DRI.URI.equals(namespaceURI) && Body.E_BODY.equals(localName))
        {
            // Save the element and see if there is anything inside the body.
            this.bodyEvent = SAXEvent.startElement(namespaceURI,localName,qName,attributes);
            return;
        }

       super.startElement(namespaceURI, localName, qName, attributes);
    }

    /**
     * Process the SAX event.
     * @see org.xml.sax.ContentHandler#endElement
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
    {
        if (this.bodyEvent != null && WingConstants.DRI.URI.equals(namespaceURI) && Body.E_BODY.equals(localName))
        {
            // If we are receiving an endElement event for body while we
            // still have a startElement body event recorded then
            // the body element must have been empty. In this case, record
            // that the body is empty, and send both the start and end body events.

            this.bodyEmpty = true;
            // Sending the body will trigger the Wing framework to ask
            // us if we want to add a body to the page.
            sendEvent(this.bodyEvent);
            this.bodyEvent = null;
        }

        super.endElement(namespaceURI, localName, qName);
    } 
  
    
    /** What to add at the end of the body */
    @Override
    public void addBody(Body body) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException, ResourceNotFoundException
    {
        if (!isRedirect() && this.bodyEmpty)
        {
            Division notFound = body.addDivision("page-not-found","primary");
            
            notFound.setHead(T_head);
            
            notFound.addPara(T_para1); 

            notFound.addPara().addXref(contextPath + "/",T_go_home);

            HttpServletResponse response = (HttpServletResponse) objectModel.get(HttpEnvironment.HTTP_RESPONSE_OBJECT);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private boolean isRedirect() {
        final HttpServletResponse response = (HttpServletResponse) objectModel.get(HttpEnvironment.HTTP_RESPONSE_OBJECT);
        try
        {
            return ((int) FieldUtils.readField(response, "statusCode", true)) == HttpServletResponse.SC_TEMPORARY_REDIRECT;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** What page metadata to add to the document */
    @Override
    public void addPageMeta(PageMeta pageMeta) throws SAXException,
            WingException, UIException, SQLException, IOException,
            AuthorizeException
    {
        if (this.bodyEmpty)
        {
            // Set the page title
            pageMeta.addMetadata("title").addContent(T_title);
            
            // Give theme a base trail
            pageMeta.addTrailLink(contextPath + "/",T_dspace_home);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Send the given recorded sax event.
     */
    public void sendEvent(SAXEvent event) throws SAXException
    {
        if (event.type == SAXEvent.EventType.START)
        {
            super.startElement(event.namespaceURI,event.localName,event.qName,event.attributes);
        }
        else if (event.type == SAXEvent.EventType.END)
        {
            super.endElement(event.namespaceURI,event.localName,event.qName);
        }
    }
    
    
    /**
     * This private class remembers start and end element SAX events.
     */
    private static class SAXEvent {
        
        public enum EventType { START, END };
        
        protected EventType type = null;
        protected String namespaceURI = null;
        protected String localName = null;
        protected String qName = null;
        protected Attributes attributes = null;
        
        /**
         * Create a new StartElement recorded sax event.
         */
        public static SAXEvent startElement(String namespaceURI, String localName, String qName, Attributes attributes) 
        {
            SAXEvent event = new SAXEvent();
            event.type = EventType.START;
            event.namespaceURI = namespaceURI;
            event.localName = localName;
            event.qName = qName;
            event.attributes = attributes;
            return event;
        }
        
        /**
         * Create a new EndElement recorded sax event.
         */
        public static SAXEvent endElement(String namespaceURI, String localName, String qName) 
        {
            SAXEvent event = new SAXEvent();
            event.type = EventType.END;
            event.namespaceURI = namespaceURI;
            event.localName = localName;
            event.qName = qName;
            return event;
        }
    }

    @Override
    public void recycle() {
        this.bodyEvent = null;
        super.recycle();
    }
}
