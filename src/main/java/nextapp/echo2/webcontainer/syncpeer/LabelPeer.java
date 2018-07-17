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

package nextapp.echo2.webcontainer.syncpeer;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.update.ServerComponentUpdate;
import nextapp.echo2.webcontainer.ContainerInstance;
import nextapp.echo2.webcontainer.DomUpdateSupport;
import nextapp.echo2.webcontainer.RenderContext;
import nextapp.echo2.webcontainer.ComponentSynchronizePeer;
import nextapp.echo2.webcontainer.image.ImageRenderSupport;
import nextapp.echo2.webcontainer.propertyrender.AlignmentRender;
import nextapp.echo2.webcontainer.propertyrender.ColorRender;
import nextapp.echo2.webcontainer.propertyrender.FontRender;
import nextapp.echo2.webcontainer.propertyrender.ImageReferenceRender;
import nextapp.echo2.webcontainer.propertyrender.LayoutDirectionRender;
import nextapp.echo2.webrender.output.CssStyle;
import nextapp.echo2.webrender.servermessage.DomUpdate;
import nextapp.echo2.webrender.util.DomUtil;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Synchronization peer for <code>nextapp.echo2.app.Label</code> components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class LabelPeer
implements DomUpdateSupport, ImageRenderSupport, ComponentSynchronizePeer {
    
    private static final Alignment DEFAULT_TEXT_POSITION = new Alignment(Alignment.TRAILING, Alignment.DEFAULT);
    private static final Extent DEFAULT_ICON_TEXT_MARGIN = new Extent(3);
    private static final String IMAGE_ID_ICON = "icon";
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderAdd(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String, nextapp.echo2.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update,
            String targetId, Component component) {
        Element domAddElement = DomUpdate.renderElementAdd(rc.getServerMessage());
        DocumentFragment htmlFragment = rc.getServerMessage().getDocument().createDocumentFragment();
        renderHtml(rc, update, htmlFragment, component);
        DomUpdate.renderElementAddContent(rc.getServerMessage(), domAddElement, targetId, htmlFragment);
    }

    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#getContainerId(nextapp.echo2.app.Component)
     */
    public String getContainerId(Component child) {
        throw new UnsupportedOperationException("Component does not support children.");
    }
    
    /**
     * @see nextapp.echo2.webcontainer.image.ImageRenderSupport#getImage(nextapp.echo2.app.Component, java.lang.String)
     */
    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(Label.PROPERTY_ICON);
        } else {
            return null;
        }
    }

    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderDispose(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, nextapp.echo2.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        // Do nothing.
    }
    
    /**
     * @see nextapp.echo2.webcontainer.DomUpdateSupport#renderHtml(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, org.w3c.dom.Node, nextapp.echo2.app.Component)
     */
    public void renderHtml(RenderContext rc, ServerComponentUpdate update, Node parentNode, Component component) {
        Label label = (Label) component;
        ImageReference icon = (ImageReference) label.getRenderProperty(Label.PROPERTY_ICON);
        String text = (String) label.getRenderProperty(Label.PROPERTY_TEXT);
        
        if (icon != null) {
            if (text != null) {
                renderIconTextLabel(rc, parentNode, label);
            } else {
                renderIconLabel(rc, parentNode, label);
            }
        } else if (text != null) {
            renderTextLabel(rc, parentNode, label);
        }
    }
    
    /**
     * Renders a label containing only an icon.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param parentNode the parent node
     * @param label the <code>Label</code>
     */
    private void renderIconLabel(RenderContext rc, Node parentNode, Label label) {
        Element imgElement = ImageReferenceRender.renderImageReferenceElement(rc, this, label, IMAGE_ID_ICON);
        imgElement.setAttribute("id", ContainerInstance.getElementId(label));
        imgElement.setAttribute("style", "border:0px none;");

        String toolTipText = (String) label.getRenderProperty(Label.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            imgElement.setAttribute("title", toolTipText);
        }
        
        parentNode.appendChild(imgElement);
    }
    
    /**
     * Renders a label containing both an icon and text.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param parentNode the parent node
     * @param label the <code>Label</code>
     */
    private void renderIconTextLabel(RenderContext rc, Node parentNode, Label label) {
        // TriCellTable rendering note:
        // Cell 0 = Text
        // Cell 1 = Icon
        
        Document document = rc.getServerMessage().getDocument();
        String text = (String) label.getRenderProperty(Label.PROPERTY_TEXT);
        Alignment textPosition = (Alignment) label.getRenderProperty(Label.PROPERTY_TEXT_POSITION, DEFAULT_TEXT_POSITION);
        Extent iconTextMargin = (Extent) label.getRenderProperty(Label.PROPERTY_ICON_TEXT_MARGIN, 
                DEFAULT_ICON_TEXT_MARGIN);
        String elementId = ContainerInstance.getElementId(label);
        
        int orientation = TriCellTableConfigurator.convertIconTextPositionToOrientation(textPosition, label);
        TriCellTable tct = new TriCellTable(rc, document, elementId, orientation, iconTextMargin);
        tct.addCellCssText("padding:0px;");
        
        Element textTdElement = tct.getTdElement(0);
        CssStyle textTdCssStyle = new CssStyle();
        textTdCssStyle.setAttribute("border", "0px none");
        if (Boolean.FALSE.equals(label.getRenderProperty(Label.PROPERTY_LINE_WRAP))) {
            textTdCssStyle.setAttribute("white-space", "nowrap");
        }
        AlignmentRender.renderToStyle(textTdCssStyle, (Alignment) label.getRenderProperty(Label.PROPERTY_TEXT_ALIGNMENT), label);
        textTdElement.setAttribute("style", textTdElement.getAttribute("style") + textTdCssStyle.renderInline());
        DomUtil.setElementText(textTdElement, text);
 
        Element imgElement = ImageReferenceRender.renderImageReferenceElement(rc, this, label, IMAGE_ID_ICON);
        Element iconTdElement = tct.getTdElement(1);
        iconTdElement.appendChild(imgElement);
        
        Element tableElement = tct.getTableElement();
        tableElement.setAttribute("id", elementId);
        
        String toolTipText = (String) label.getRenderProperty(Label.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            tableElement.setAttribute("title", toolTipText);
        }
        
        CssStyle cssStyle = new CssStyle();
        LayoutDirectionRender.renderToStyle(cssStyle, label.getLayoutDirection(), label.getLocale());
        ColorRender.renderToStyle(cssStyle, (Color) label.getRenderProperty(Label.PROPERTY_FOREGROUND), 
                (Color) label.getRenderProperty(Label.PROPERTY_BACKGROUND));
        FontRender.renderToStyle(cssStyle, (Font) label.getRenderProperty(Label.PROPERTY_FONT));
        cssStyle.setAttribute("border", "0px none");
        cssStyle.setAttribute("border-collapse", "collapse");
        tableElement.setAttribute("style", cssStyle.renderInline());
        
        parentNode.appendChild(tableElement);
    }

    /**
     * Renders a label containing only text
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param parentNode the parent node
     * @param label the <code>Label</code>
     */
    private void renderTextLabel(RenderContext rc, Node parentNode, Label label) {
        Document document = rc.getServerMessage().getDocument();
        
        Element spanElement = document.createElement("span");
        spanElement.setAttribute("id", ContainerInstance.getElementId(label));
        DomUtil.setElementText(spanElement, (String) label.getRenderProperty(Label.PROPERTY_TEXT));

        CssStyle cssStyle = new CssStyle();
        if (Boolean.FALSE.equals(label.getRenderProperty(Label.PROPERTY_LINE_WRAP))) {
            cssStyle.setAttribute("white-space", "nowrap");
        }
        ColorRender.renderToStyle(cssStyle, (Color) label.getRenderProperty(Label.PROPERTY_FOREGROUND), 
                (Color) label.getRenderProperty(Label.PROPERTY_BACKGROUND));
        FontRender.renderToStyle(cssStyle, (Font) label.getRenderProperty(Label.PROPERTY_FONT));
        if (cssStyle.hasAttributes()) {
            spanElement.setAttribute("style", cssStyle.renderInline());
        }

        String toolTipText = (String) label.getRenderProperty(Label.PROPERTY_TOOL_TIP_TEXT);
        if (toolTipText != null) {
            spanElement.setAttribute("title", toolTipText);
        }
        
        parentNode.appendChild(spanElement);
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext, 
     * nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return false;
    }
}
