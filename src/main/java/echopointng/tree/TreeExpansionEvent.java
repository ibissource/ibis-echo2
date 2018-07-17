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

import java.util.EventObject;

/**
 * An event used to identify a single path in a tree. The source returned by
 * <b>getSource </b> will be an instance of <code>Tree</code>.
 * <p>
 */
public class TreeExpansionEvent extends EventObject {
	/**
	 * Path to the value this event represents.
	 */
	protected TreePath path;

	/**
	 * Constructs a TreeExpansionEvent object.
	 * 
	 * @param source
	 *            the Object that originated the event 
	 * @param path
	 *            a TreePath object identifying the newly expanded node
	 */
	public TreeExpansionEvent(Object source, TreePath path) {
		super(source);
		this.path = path;
	}

	/**
	 * Returns the path to the value that has been expanded/collapsed.
	 */
	public TreePath getPath() {
		return path;
	}
}
