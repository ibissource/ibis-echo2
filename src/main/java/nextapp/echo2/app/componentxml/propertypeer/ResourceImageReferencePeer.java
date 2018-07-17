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

package nextapp.echo2.app.componentxml.propertypeer;

import org.w3c.dom.Element;

import nextapp.echo2.app.Extent;
import nextapp.echo2.app.ResourceImageReference;
import nextapp.echo2.app.componentxml.InvalidPropertyException;
import nextapp.echo2.app.componentxml.PropertyXmlPeer;
import nextapp.echo2.app.util.DomUtil;

/**
 * <code>PropertyXmlPeer</code> implementation for 
 * <code>nextapp.echo2.app.ResourceImageReference</code> properties.
 */
public class ResourceImageReferencePeer 
implements PropertyXmlPeer {

    /**
     * @see nextapp.echo2.app.componentxml.PropertyXmlPeer#getValue(java.lang.ClassLoader, 
     *      java.lang.Class, org.w3c.dom.Element)
     */
    public Object getValue(ClassLoader classLoader, Class objectClass, Element propertyElement)
    throws InvalidPropertyException {
        if (propertyElement.hasAttribute("value")) {
            return new ResourceImageReference(propertyElement.getAttribute("value"));
        } else {
            Element resourceImageReferenceElement = DomUtil.getChildElementByTagName(propertyElement, 
                    "resource-image-reference");
            if (!resourceImageReferenceElement.hasAttribute("resource")) {
                throw new InvalidPropertyException("Invalid ResourceImageReference property (resource not specified).", null);
            }
            String resource = resourceImageReferenceElement.getAttribute("resource");
            String contentType = null;
            if (resourceImageReferenceElement.hasAttribute("content-type")) {
                contentType = resourceImageReferenceElement.getAttribute("content-type");
            }
            Extent width = null;
            if (resourceImageReferenceElement.hasAttribute("width")) {
                width = ExtentPeer.toExtent(resourceImageReferenceElement.getAttribute("width"));
            }
            Extent height = null;
            if (resourceImageReferenceElement.hasAttribute("height")) {
                height = ExtentPeer.toExtent(resourceImageReferenceElement.getAttribute("height"));
            }
            return new ResourceImageReference(resource, contentType, width, height);
        }
    }
}
