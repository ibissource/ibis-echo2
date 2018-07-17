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

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Window;
import nextapp.echo2.app.update.ServerComponentUpdate;
import nextapp.echo2.webcontainer.PartialUpdateManager;
import nextapp.echo2.webcontainer.PartialUpdateParticipant;
import nextapp.echo2.webcontainer.RenderContext;
import nextapp.echo2.webcontainer.RootSynchronizePeer;
import nextapp.echo2.webcontainer.ComponentSynchronizePeer;
import nextapp.echo2.webcontainer.SynchronizePeerFactory;
import nextapp.echo2.webcontainer.WindowHtmlService;
import nextapp.echo2.webrender.servermessage.DomUpdate;
import nextapp.echo2.webrender.servermessage.WindowUpdate;

/**
 * Synchronization peer for <code>nextapp.echo2.app.Window</code> components.
 * <p>
 * This class should not be extended or used by classes outside of the
 * Echo framework.
 */
public class WindowPeer 
implements RootSynchronizePeer {

    private PartialUpdateManager partialUpdateManager;

    /**
     * Default constructor.
     */
    public WindowPeer() {
        super();
        partialUpdateManager = new PartialUpdateManager();
        partialUpdateManager.add(Window.PROPERTY_TITLE, new PartialUpdateParticipant() {
            
            /**
             * @see nextapp.echo2.webcontainer.PartialUpdateParticipant#canRenderProperty(nextapp.echo2.webcontainer.RenderContext, 
             *      nextapp.echo2.app.update.ServerComponentUpdate)
             */
            public boolean canRenderProperty(RenderContext rc, ServerComponentUpdate update) {
                return true;
            }

            /**
             * @see nextapp.echo2.webcontainer.PartialUpdateParticipant#renderProperty(
             *      nextapp.echo2.webcontainer.RenderContext, nextapp.echo2.app.update.ServerComponentUpdate)
             */
            public void renderProperty(RenderContext rc, ServerComponentUpdate update) {
                Window window = (Window) update.getParent();
                String title = (String) window.getRenderProperty(Window.PROPERTY_TITLE);
                WindowUpdate.renderSetWindowTitle(rc.getServerMessage(), title);
            }
        });
    }
       
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderAdd(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String, nextapp.echo2.app.Component)
     */
    public void renderAdd(RenderContext rc, ServerComponentUpdate update,
            String targetId, Component component) {
        throw new UnsupportedOperationException("Cannot add window.");
    }

    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#getContainerId(nextapp.echo2.app.Component)
     */
    public String getContainerId(Component child) {
        return WindowHtmlService.ROOT_ID;
    }
    
    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderDispose(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, nextapp.echo2.app.Component)
     */
    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        // Do nothing.
    }
    
    /**
     * @see nextapp.echo2.webcontainer.RootSynchronizePeer#renderRefresh(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, nextapp.echo2.app.Component)
     */
    public void renderRefresh(RenderContext rc, ServerComponentUpdate update, Component component) {
        Window window = (Window) component;
        
        String title = (String) window.getRenderProperty(Window.PROPERTY_TITLE);
        if (title != null) {
            WindowUpdate.renderSetWindowTitle(rc.getServerMessage(), title);
        }
        
        DomUpdate.renderElementRemoveChildren(rc.getServerMessage(), WindowHtmlService.ROOT_ID);
        Component[] addedChildren = window.getVisibleComponents();
        for (int i = 0; i < addedChildren.length; ++i) {
            ComponentSynchronizePeer childSyncPeer = SynchronizePeerFactory.getPeerForComponent(addedChildren[i].getClass());
            childSyncPeer.renderAdd(rc, update, WindowHtmlService.ROOT_ID, addedChildren[i]);
        }
    }

    /**
     * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext, 
     *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
     */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        boolean fullRefresh;
        if (update.hasAddedChildren() || update.hasRemovedChildren() || update.hasUpdatedLayoutDataChildren()) {
            fullRefresh = true;
        } else if (update.hasUpdatedProperties() && partialUpdateManager.canProcess(rc, update)) {
            fullRefresh = false;
        } else {
            fullRefresh = true;
        }
        
        if (fullRefresh) {
            renderRefresh(rc, update, update.getParent());
        } else {
            partialUpdateManager.process(rc, update);
        }
        
        return fullRefresh;
    }
}
