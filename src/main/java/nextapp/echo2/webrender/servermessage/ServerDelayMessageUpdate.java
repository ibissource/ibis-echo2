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

package nextapp.echo2.webrender.servermessage;

import nextapp.echo2.webrender.ServerDelayMessage;
import nextapp.echo2.webrender.ServerMessage;
import nextapp.echo2.webrender.output.HtmlDocument;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A <code>ServerMessage</code>-utility class to render <Code>EchoServerDelayMessage</code>
 * message processor directives to configure the client-side message displayed
 * during short- and long-running server interactions.
 */
public class ServerDelayMessageUpdate {
    
    /**
     * Creates a new <code>setMessage</code> directive.
     * 
     * @param serverMessage the relevant <code>ServerMessage</code>
     */
    public static void renderUpdateDirective(ServerMessage serverMessage, ServerDelayMessage serverDelayMessage) {
        Document document = serverMessage.getDocument();
        Element setMessageElement = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_INIT, 
                "EchoServerDelayMessage.MessageProcessor", "set-message");
        if (serverDelayMessage == null) {
            return;
        }
        
        Element contentContainerElement = serverMessage.getDocument().createElement("content");
        setMessageElement.appendChild(contentContainerElement);
        
        Element messageElement = (Element) document.importNode(serverDelayMessage.getMessage(), true);
        messageElement.setAttribute("xmlns", HtmlDocument.XHTML_1_0_NAMESPACE_URI);
        
        if (!ServerDelayMessage.ELEMENT_ID_MESSAGE.equals(messageElement.getAttribute("id"))) {
            throw new IllegalStateException("Invalid ServerDelayMessage: incorrect root element id: " 
                    + messageElement.getAttribute("id"));
        }
        
        contentContainerElement.appendChild(messageElement);
    }
}
