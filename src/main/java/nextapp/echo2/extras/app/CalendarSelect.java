/* 
 * This file is part of the Echo2 Extras Project.
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

package nextapp.echo2.extras.app;

import java.util.Date;

import nextapp.echo2.app.Border;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.FillImage;

/**
 * A user-input component which allows for the selection of a single date.
 */
public class CalendarSelect extends Component {

    public static final String DATE_CHANGED_PROPERTY = "date";
    public static final String PROPERTY_BORDER = "border";
    public static final String PROPERTY_BACKGROUND_IMAGE = "backgroundImage";

    public static final String PROPERTY_ADJACENT_MONTH_DATE_FOREGROUND = "adjacentMonthDateForeground";
    public static final String PROPERTY_SELECTED_DATE_BACKGROUND = "selectedDateBackground";
    public static final String PROPERTY_SELECTED_DATE_BACKGROUND_IMAGE = "selectedDateBackgroundImage";
    public static final String PROPERTY_SELECTED_DATE_FOREGROUND = "selectedDateForeground";
    
    private Date date;
    
    /**
     * Creates a new <code>CalendarSelect</code>.
     */
    public CalendarSelect() {
        this(null);
    }
    
    /**
     * Creates a new <code>CalendarSelect</code>.
     * 
     * @param date the initially selected date
     */
    public CalendarSelect(Date date) {
        super();
        this.date = date;
    }
    
    /**
     * Returns the foreground color of dates in adjacent (previous/next) months.
     * 
     * @return the foreground 
     */
    public Color getAdjacentMonthDateForeground() {
        return (Color) getProperty(PROPERTY_ADJACENT_MONTH_DATE_FOREGROUND);
    }
    
    /**
     * Returns the background image of the displayed calendar month.
     * 
     * @return the background image
     */
    public FillImage getBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the border surrounding the displayed calendar month.
     * 
     * @return the border
     */
    public Border getBorder() {
        return (Border) getProperty(PROPERTY_BORDER);
    }
    
    /**
     * Returns the selected date.
     * 
     * @return the selected date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Returns the background color of the selected date
     * 
     * @return the background 
     */
    public Color getSelectedDateBackground() {
        return (Color) getProperty(PROPERTY_SELECTED_DATE_BACKGROUND);
    }
    
    /**
     * Returns the background image of the selected date
     * 
     * @return the background image
     */
    public FillImage getSelectedDateBackgroundImage() {
        return (FillImage) getProperty(PROPERTY_SELECTED_DATE_BACKGROUND_IMAGE);
    }
    
    /**
     * Returns the foreground color of the selected date
     * 
     * @return the foreground 
     */
    public Color getSelectedDateForeground() {
        return (Color) getProperty(PROPERTY_SELECTED_DATE_FOREGROUND);
    }
    
    /**
     * @see nextapp.echo2.app.Component#processInput(java.lang.String, java.lang.Object)
     */
    public void processInput(String inputName, Object inputValue) {
        if (DATE_CHANGED_PROPERTY.equals(inputName)) {
            setDate((Date) inputValue);
        }
    }

    /**
     * Sets the background image of the displayed calendar month.
     * 
     * @param newValue the new background image
     */
    public void setBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_BACKGROUND_IMAGE, newValue);
    }
    
    /**
     * Sets the border surrounding the displayed calendar month.
     * 
     * @param newValue the new border
     */
    public void setBorder(Border newValue) {
        setProperty(PROPERTY_BORDER, newValue);
    }
    
    /**
     * Sets the selected date.
     * 
     * @param newValue the new date
     */
    public void setDate(Date newValue) {
        Date oldValue = date;
        date = newValue;
        firePropertyChange(DATE_CHANGED_PROPERTY, oldValue, newValue);
    }

    /**
     * Sets the foreground color of dates in adjacent (previous/next) months.
     * 
     * @param newValue the new foreground
     */
    public void setAdjacentMonthDateForeground(Color newValue) {
        setProperty(PROPERTY_SELECTED_DATE_FOREGROUND, newValue);
    }

    /**
     * Sets the background color of the selected date
     * 
     * @param newValue the new background
     */
    public void setSelectedDateBackground(Color newValue) {
        setProperty(PROPERTY_SELECTED_DATE_BACKGROUND, newValue);
    }

    /**
     * Sets the background image of the selected date
     * 
     * @param newValue the new background image
     */
    public void setSelectedDateBackgroundImage(FillImage newValue) {
        setProperty(PROPERTY_SELECTED_DATE_BACKGROUND_IMAGE, newValue);
    }
    
    /**
     * Sets the foreground color of the selected date
     * 
     * @param newValue the new foreground
     */
    public void setSelectedDateForeground(Color newValue) {
        setProperty(PROPERTY_SELECTED_DATE_FOREGROUND, newValue);
    }

}
