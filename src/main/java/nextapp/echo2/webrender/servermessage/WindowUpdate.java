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

import nextapp.echo2.webrender.ServerMessage;

import org.w3c.dom.Element;

/**
 * A utility class to add <code>EchoWindowUpdate</code> message parts to the 
 * <code>ServerMessage</code>.  <code>EchoWindowUpdate</code> message parts
 * are used to perform miscellaneous updates to a top-level window. 
 */
public class WindowUpdate {

    private static final String MESSAGE_PART_NAME = "EchoWindowUpdate";
    
    /**
     * Creates a <code>reload</code> operation to reload the contents of the window.
     * 
     * @param serverMessage the outgoing <code>ServerMessage</code>
     */
    public static void renderReload(ServerMessage serverMessage) {
        serverMessage.appendPartDirective(ServerMessage.GROUP_ID_POSTUPDATE, 
                MESSAGE_PART_NAME, "reload");
    }
    
    /**
     * Creates a <code>focus</code> operation to set the focused
     * component within the window.
     * 
     * @param serverMessage the outgoing <code>ServerMessage</code>
     * @param elementId the new focused element id
     */
    public static void renderSetFocus(ServerMessage serverMessage, String elementId) {
        Element element = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_POSTUPDATE, 
                MESSAGE_PART_NAME, "set-focus");
        element.setAttribute("element-id", elementId);
    }
    
    /**
     * Creates a <code>set-title</code> operation to set the title of a 
     * top-level browser window.
     * 
     * @param serverMessage the outgoing <code>ServerMessage</code>
     * @param newValue the new window title
     */
    public static void renderSetWindowTitle(ServerMessage serverMessage, String newValue) {
        Element element = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_UPDATE, 
                MESSAGE_PART_NAME, "set-title");
        element.setAttribute("title", newValue);
    }
}
