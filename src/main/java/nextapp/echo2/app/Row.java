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

package nextapp.echo2.app;

/**
 * A layout <code>Component</code> which renders its contents in a single 
 * horizontal row of cells.
 * <p>
 * <b>Child LayoutData</b>: Children of this component may provide
 * layout information using the 
 * <code>nextapp.echo2.app.layout.RowLayoutData</code> layout data object.
 * 
 * @see nextapp.echo2.app.layout.RowLayoutData
 */
public class Row extends Component {
    
    public static final String PROPERTY_ALIGNMENT = "alignment";
    public static final String PROPERTY_BORDER = "border";
    public static final String PROPERTY_CELL_SPACING = "cellSpacing";
    public static final String PROPERTY_INSETS = "insets";
    
    /**
     * Creates a new <code>Row</code>.
     */
    public Row() {
        super();
    }

    /**
     * Returns the alignment of the row's content.
     * Only horizontal alignments are supported.
     * 
     * @return the alignment
     */
    public Alignment getAlignment() {
        return (Alignment) getProperty(PROPERTY_ALIGNMENT);
    }

    /**
     * Returns the <code>Border</code> that encloses the entire <code>Column</code>.
     * 
     * @return the border
     */
    public Border getBorder() {
        return (Border) getProperty(PROPERTY_BORDER);
    }
    
    /**
     * Returns the spacing between individual cells of the <code>Column</code>.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @return the cell spacing
     */
    public Extent getCellSpacing() {
        return (Extent) getProperty(PROPERTY_CELL_SPACING);
    }
    
    /**
     * Returns the inset between the border and cells of the <code>Column</code>.
     *
     * @return the inset
     */
    public Insets getInsets() {
        return (Insets) getProperty(PROPERTY_INSETS);
    }

    /**
     * Sets the alignment of the row's content.
     * Only horizontal alignments are supported.
     * 
     * @param newValue the new alignment
     */
    public void setAlignment(Alignment newValue) {
        setProperty(PROPERTY_ALIGNMENT, newValue);
    }
    
    /**
     * Sets the <code>Border</code> that encloses the entire <code>Column</code>.
     * 
     * @param newValue the new border
     */
    public void setBorder(Border newValue) {
        setProperty(PROPERTY_BORDER, newValue);
    }
    
    /**
     * Sets the spacing between individual cells of the <code>Column</code>.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @param newValue the new spacing
     */
    public void setCellSpacing(Extent newValue) {
        setProperty(PROPERTY_CELL_SPACING, newValue);
    }
    
    /**
     * Sets the inset between the border and cells of the <code>Column</code>.
     * 
     * @param newValue the new inset
     */
    public void setInsets(Insets newValue) {
        setProperty(PROPERTY_INSETS, newValue);
    }
}
