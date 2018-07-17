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

import java.io.IOException;
import nextapp.echo2.app.AwtImageReference;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.webrender.Connection;
import nextapp.echo2.webrender.ContentType;
import nextapp.echo2.webrender.WebRenderServlet;

/**
 * Renders an <code>AwtImageReference</code> to the client.
 */
public class AwtImageService extends AbstractImageService {
    
    private static final String SERVICE_ID = "Echo.AwtImage"; 
    
    public static final AwtImageService INSTANCE = new AwtImageService();

    static {
        WebRenderServlet.getServiceRegistry().add(INSTANCE);
    }

    /**
     * @see nextapp.echo2.webrender.Service#getId()
     */
    public String getId() {
        return SERVICE_ID;
    }
    
    /**
     * @see nextapp.echo2.webrender.Service#getVersion()
     */
    public int getVersion() {
        return 0; // Enable caching.
    }

    /**
     * @see nextapp.echo2.webcontainer.image.AbstractImageService#renderImage(
     *      nextapp.echo2.webrender.Connection, nextapp.echo2.app.ImageReference)
     */
    public void renderImage(Connection conn, ImageReference imageReference) 
    throws IOException {
        try {
            if (!(imageReference instanceof AwtImageReference)) {
                throw new IOException("Image is not an AwtImageReference.");
            }
            PngEncoder encoder = new PngEncoder(((AwtImageReference) imageReference).getImage(), true, null, 3);
            conn.setContentType(ContentType.IMAGE_PNG);
            encoder.encode(conn.getOutputStream());
        } catch (IOException ex) {
            // Internet Explorer appears to enjoy making half-hearted requests for images, wherein it resets the connection
            // leaving us with an IOException.  This exception is silently eaten.
        }
    }
}
