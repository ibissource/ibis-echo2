package echopointng.model;

/* 
 * This file is part of the Echo Point Project.  This project is a collection
 * of Components that have extended the Echo Web Application Framework.
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

import nextapp.echo2.app.event.ChangeListener;

/**
 * A model that supports at most one indexed selection.
 *  
 */
public interface SingleSelectionModel {
	/**
	 * Adds <I>listener </I> as a listener to changes in the model.
	 * 
	 * @param listener
	 *            the ChangeListener to add
	 */
	public void addChangeListener(ChangeListener listener);

	/**
	 * Clears the selection (to -1).
	 */
	public void clearSelection();

	/**
	 * Returns the model's selection.
	 * 
	 * @return the model's selection, or -1 if there is no selection
	 */
	public int getSelectedIndex();

	/**
	 * Returns true if the selection model currently has a selected value.
	 * 
	 * @return true if a value is currently selected
	 */
	public boolean isSelected();

	/**
	 * Removes <I>listener </I> as a listener to changes in the model.
	 * 
	 * @param listener
	 *            the ChangeListener to remove
	 */
	public void removeChangeListener(ChangeListener listener);

	/**
	 * Sets the model's selected index to <I>index </I>. <br>
	 * Notifies any listeners if the model changes
	 *  
	 */
	public void setSelectedIndex(int index);
}