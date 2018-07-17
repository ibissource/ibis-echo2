/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2009 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package nextapp.echo2.webrender.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A utility class which provides methods for working with a W3C DOM.
 */
public class DomUtil {

    /**
     * Entity resolver which throws a SAXException when invoked to avoid external entity injection.
     */
    private static final EntityResolver entityResolver = new EntityResolver() {
    
        /**
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
         */
        public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {
            throw new SAXException("External entities not supported.");
        }
    };

    /**
     * ThreadLocal cache of <code>DocumentBuilder</code> instances.
     */
    private static final ThreadLocal documentBuilders = new ThreadLocal() {
    
        /**
         * @see java.lang.ThreadLocal#initialValue()
         */
        protected Object initialValue() {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setEntityResolver(entityResolver);
                return builder;
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
        }
    };
    
    /**
     * ThreadLocal cache of <code>TransformerFactory</code> instances.
     */
    private static final ThreadLocal transformerFactories = new ThreadLocal() {
    
        /**
         * @see java.lang.ThreadLocal#initialValue()
         */
        protected Object initialValue() {
            TransformerFactory factory = null;
            // Try to find a specific TransformerFactory because:
            // - WebSphere has it's own implementation of Xalan with the same
            //   package names which might result in unexpected different
            //   behaviour (issues seen in Ibis transformations not (yet) in
            //   Echo2).
            // - Echo2 doesn't work with Saxon8 in Firefox, see also:
            //     http://echo.nextapp.com/site/node/3844
            // - The javax.xml.transform.TransformerFactory system property
            //   might have an incorrect value (because of another application
            //   in the same JVM).
            try {
                factory = (TransformerFactory)DomUtil.class.forName("nl.nn.org.apache.xalan.processor.TransformerFactoryImpl").getConstructor(null).newInstance(null);
            } catch(Exception e1) {
                try {
                    factory = (TransformerFactory)DomUtil.class.forName("org.apache.xalan.processor.TransformerFactoryImpl").getConstructor(null).newInstance(null);
                } catch(Exception e2) {
                    // Use the JAXP mechanism.
                    factory = TransformerFactory.newInstance();
                }
            }
            return factory;
        }
    };
    
    /**
     * Retrieves a thread-specific <code>DocumentBuilder</code>.
     * As it is a shared resource, the returned object should not be reconfigured in any fashion.
     * 
     * @return the <code>DocumentBuilder</code> serving the current thread.
     */
    public static DocumentBuilder getDocumentBuilder() {
        return (DocumentBuilder) documentBuilders.get();
    }
    
    /**
     * Retrieves a thread-specific <code>TransformerFactory</code>.
     * As it is a shared resource, the returned object should not be reconfigured in any fashion.
     * 
     * @return the <code>TransformerFactory</code> serving the current thread.
     */
    public static TransformerFactory getTransformerFactory() {
        return (TransformerFactory) transformerFactories.get();
    }
    
    /**
     * Determines whether a specific boolean flag is set on an element.
     * 
     * @param element The element to analyze.
     * @param attributeName The name of the boolean 'flag' attribute.
     * @return True if the value of the attribute is 'true', false if it is
     *         not or if the attribute does not exist.
     */
    public static boolean getBooleanAttribute(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (value == null) {
            return false;
        } else if (value.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the first immediate child element of the specified element  
     * whose name matches the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to search.
     * @param name The name of the child element.
     * @return The child element, or null if none was found. 
     */
    public static Element getChildElementByTagName(Element parentElement, String name) {
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && name.equals(nodes.item(index).getNodeName())) {
                return (Element) nodes.item(index);
            }
        }
        return null;
    }

    /**
     * Retrieves the first immediate child element of the specified element  
     * whose name matches the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to search.
     * @param namespaceURI The namespace URI of the child element.
     * @param localName The name of the child element.
     * @return The child element, or null if none was found. 
     */
    public static Element getChildElementByTagNameNS(Element parentElement, String namespaceURI, String localName) {
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && namespaceURI.equals(nodes.item(index).getNamespaceURI())
                    && localName.equals(nodes.item(index).getNodeName())) {
                return (Element) nodes.item(index);
            }
        }
        return null;
    }

    /**
     * Retrieves all immediate child elements of the specified element whose
     * names match the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to search.
     * @param name The name of the child element.
     * @return An array of matching child elements.
     */
    public static Element[] getChildElementsByTagName(Element parentElement, String name) {
        List children = new ArrayList();
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && name.equals(nodes.item(index).getNodeName())) {
                children.add(nodes.item(index));
            }
        }
        Element[] childElements = new Element[children.size()];
        return (Element[]) children.toArray(childElements);
    }

    /**
     * Retrieves all immediate child elements of the specified element whose
     * names match the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to search.
     * @param namespaceURI The namespace URI of the child element.
     * @param localName The name of the child element.
     * @return An array of matching child elements.
     */
    public static Element[] getChildElementsByTagNameNS(Element parentElement, String namespaceURI, String localName) {
        List children = new ArrayList();
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && namespaceURI.equals(nodes.item(index).getNamespaceURI())
                    && localName.equals(nodes.item(index).getNodeName())) {
                children.add(nodes.item(index));
            }
        }
        Element[] childElements = new Element[children.size()];
        return (Element[]) children.toArray(childElements);
    }

    /**
     * Counts the number of immediate child elements of the specified element
     * whose names match the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to analyze.
     * @param name The name of the child element.
     * @return The number of matching child elements.
     */
    public static int getChildElementCountByTagName(Element parentElement, String name) {
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        int count = 0;
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && name.equals(nodes.item(index).getNodeName())) {
                ++count;
            }
        }
        return count;
    }
    
    /**
     * Counts the number of immediate child elements of the specified element
     * whose names match the provided <code>name</code> parameter.
     * 
     * @param parentElement The element to analyze.
     * @param namespaceURI The namespace URI of the child element.
     * @param localName The name of the child element.
     * @return The number of matching child elements.
     */
    public static int getChildElementCountByTagNameNS(Element parentElement, String namespaceURI, String localName) {
        NodeList nodes = parentElement.getChildNodes();
        int length = nodes.getLength();
        int count = 0;
        for (int index = 0; index < length; ++index) {
            if (nodes.item(index).getNodeType() == Node.ELEMENT_NODE
                    && namespaceURI.equals(nodes.item(index).getNamespaceURI())
                    && localName.equals(nodes.item(index).getNodeName())) {
                ++count;
            }
        }
        return count;
    }
    
    /**
     * Returns the text content of a DOM <code>Element</code>.
     * 
     * @param element The <code>Element</code> to analyze.
     */
    public static String getElementText(Element element) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();
        for (int index = 0; index < childCount; ++index) {
            if (children.item(index) instanceof Text) {
                Text text = (Text) children.item(index);
                return text.getData();
            }
        }
        return null;
    }
    
    /**
     * Sets the text content of a DOM <code>Element</code>.
     * 
     * @param element The <code>Element</code> to modify.
     * @param value The new text value.
     */
    public static void setElementText(Element element, String value) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();
        for (int index = 0; index < childCount; ++index) {
            if (children.item(index) instanceof Text) {
                Text text = (Text) children.item(index);
                text.setData(value);
                return;
            }
        }
        Text text = element.getOwnerDocument().createTextNode(value);
        element.appendChild(text);
    }
    
    /** Non-instantiable class. */
    private DomUtil() { }
}
