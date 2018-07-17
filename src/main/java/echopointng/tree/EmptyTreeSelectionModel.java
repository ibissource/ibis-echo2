package echopointng.tree;
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

 

 /*
 * The design paradigm and class name used within have been taken directly from
 * the java.swing package has been retro-fitted to work with the NextApp Echo web framework.
 *
 * This file was made part of the EchoPoint project on the 25/07/2002.
 *
 */
 
/**
 * This <code>TreeSelectionModel</code> does not allow any selection to occur.
 */
public class EmptyTreeSelectionModel extends DefaultTreeSelectionModel {

	/** Unique shared instance. */
	protected static final EmptyTreeSelectionModel sharedInstance =
		new EmptyTreeSelectionModel();

/**
 * EmptyTreeSelectionModel constructor comment.
 */
public EmptyTreeSelectionModel() {
	super();
}
	/** A null implementation that adds nothing */
	public void addSelectionPaths(TreePath[] paths) {
	}
	/** Returns a shared instance of an empty selection model */
	static public EmptyTreeSelectionModel getInstance() {
		return sharedInstance;
	}
	/** A null implementation that removes nothing */
	public void removeSelectionPaths(TreePath[] paths) {
	}
	/** A null implementation that selects nothing */
	public void setSelectionPaths(TreePath[] pPaths) {
	}
}
