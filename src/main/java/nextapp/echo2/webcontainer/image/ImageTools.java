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

package nextapp.echo2.webcontainer.image;

import nextapp.echo2.app.AwtImageReference;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.HttpImageReference;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.StreamImageReference;
import nextapp.echo2.webcontainer.RenderContext;

/**
 * Main application interface to application container image rendering API.
 */
public class ImageTools {
    
    /**
     * Retrieves the URI to render an image for a component.
     * 
     * @param rc the relevant <code>RenderContext</code>
     * @param irs the <code>ImageRenderSupport</code>-implementing 
     *        <code>ComponentSynchronizePeer</code>.
     * @param component the component for which the image is being rendered
     * @param imageId the component-specific id of the image
     * @return the URI
     */
    public static String getUri(RenderContext rc, ImageRenderSupport irs, Component component, String imageId) {
        ImageReference imageReference = irs.getImage(component, imageId);
        if (imageReference instanceof StreamImageReference) {
            rc.getContainerInstance().getIdTable().register(imageReference);
            return StreamImageService.INSTANCE.createUri(rc.getContainerInstance(), imageReference.getRenderId());
        } else if (imageReference instanceof HttpImageReference) {
            return ((HttpImageReference) imageReference).getUri();
        } else if (imageReference instanceof AwtImageReference) {
            rc.getContainerInstance().getIdTable().register(imageReference);
            return AwtImageService.INSTANCE.createUri(rc.getContainerInstance(), imageReference.getRenderId());
        } else {
            throw new IllegalArgumentException("Unsupported image type.");
        }
    }
}
