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
  
//_________________
// Object EchoTable

/**
 * Object/namespace for Table support.
 * This object/namespace should not be used externally.
 */
EchoTable = Core.extend({

    $static: {

        /**
         * Returns the Table data object instance based on the root element 
         * of the Table.
         *
         * @param element the root element or element id of the Table
         * @return the relevant table instance
         */
        getComponent: function(element) {
            return EchoDomPropertyStore.getPropertyValue(element, "component");
        },

        /**
         * Processes a row selection (click) event.
         * Finds the appropriate <code>EchoTable</code> instance and
         * delegates processing to it.
         *
         * @param echoEvent the event, preprocessed by the 
         *        <code>EchoEventProcessor</code>
         */
        processClick: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var table = EchoTable.getComponent(componentId);
            table.processClick(echoEvent);
        },
        
        /**
         * Process a mouse down event.
         * Finds the appropriate <code>EchoTable</code> instance and
         * delegates processing to it.
         *
         * @param echoEvent the event, preprocessed by the 
         *        <code>EchoEventProcessor</code>
         */
        processMouseDown: function(echoEvent) {
            EchoDomUtil.preventEventDefault(echoEvent);
        },
        
        /**
         * Processes a row mouse over event.
         * Finds the appropriate <code>EchoTable</code> instance and
         * delegates processing to it.
         *
         * @param echoEvent the event, preprocessed by the 
         *        <code>EchoEventProcessor</code>
         */
        processRolloverEnter: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var table = EchoTable.getComponent(componentId);
            table.processRolloverEnter(echoEvent);
        },
        
        /**
         * Processes a row mouse out event.
         * Finds the appropriate <code>EchoTable</code> instance and
         * delegates processing to it.
         *
         * @param echoEvent the event, preprocessed by the 
         *        <code>EchoEventProcessor</code>
         */
        processRolloverExit: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var table = EchoTable.getComponent(componentId);
            table.processRolloverExit(echoEvent);
        }
    },

    /**
     * Constructor to create new <code>EchoTable</code> instance.
     * 
     * @param element the supported <code>TABLE</code> DOM element 
     */
    $construct: function(elementId) {
        this.elementId = elementId;
        
        this.multipleSelect = false;
        this.rolloverEnabled = false;
        this.rolloverStyle = null;
        this.selectionEnabled = false;
        this.selectionStyle = null;
        this.rowCount = 0;
        this.selectionState = null;
        this.headerVisible = false;
        this.lastSelectedIndex = -1;
    },
    
    /**
     * Deselects all selected rows in a Table.
     */
    clearSelected: function() {
        for (var i = 0; i < this.rowCount; ++i) {
            if (this.isSelected(i)) {
                this.setSelected(i, false);
            }
        }
    },
    
    /**
     * Disposes of an <code>EchoTable</code> instance, de-registering 
     * listeners and cleaning up resources.
     */
    dispose: function() {
        var element = this.getElement();
        
        if (this.rolloverEnabled || this.selectionEnabled) {
            var mouseEnterLeaveSupport = EchoClientProperties.get("proprietaryEventMouseEnterLeaveSupported");
            for (var rowIndex = 0; rowIndex < this.rowCount; ++rowIndex) {
                var trElement = element.rows[rowIndex + (this.headerVisible ? 1 : 0)];
                if (this.rolloverEnabled) {
                    if (mouseEnterLeaveSupport) {
                        EchoEventProcessor.removeHandler(trElement, "mouseenter");
                        EchoEventProcessor.removeHandler(trElement, "mouseleave");
                    } else {
                        EchoEventProcessor.removeHandler(trElement, "mouseout");
                        EchoEventProcessor.removeHandler(trElement, "mouseover");
                    }
                }
                if (this.selectionEnabled) {
                    EchoEventProcessor.removeHandler(trElement, "click");
                    EchoEventProcessor.removeHandler(trElement, "mousedown");
                }
            }
        }
        
        EchoDomPropertyStore.dispose(element);
    },
    
    /**
     * Redraws a row in the appropriate style (i.e., selected or deselected).
     *
     * @param rowIndex the index of the row to redraw
     */
    drawRowStyle: function(rowIndex) {
        var selected = this.isSelected(rowIndex);
        var trElement = this.getRowElement(rowIndex);
        
        for (var i = 0; i < trElement.cells.length; ++i) {
            if (selected) {
                EchoCssUtil.restoreOriginalStyle(trElement.cells[i]);
                EchoCssUtil.applyTemporaryStyle(trElement.cells[i], this.selectionStyle);
            } else {
                EchoCssUtil.restoreOriginalStyle(trElement.cells[i]);
            }
        }
    },
    
    /**
     * Returns the table DOM element.
     */
    getElement: function() {
        return document.getElementById(this.elementId);
    },
    
    /**
     * Returns the <code>TR</code> element associated with a specific
     * row index.
     * 
     * @param rowIndex the row index
     * @return the relevant <code>TR</code> element
     */
    getRowElement: function(rowIndex) {
        var element = this.getElement();
        if (this.headerVisible) {
            if (rowIndex == -1) {
                return element.rows[0];
            } else if (rowIndex >= 0 && rowIndex < this.rowCount) {
                return element.rows[rowIndex + 1];
            }
        } else {
            if (rowIndex >= 0 && rowIndex < this.rowCount) {
                return element.rows[rowIndex];
            }
        }
        return null;
    },
    
    /**
     * Determines the index of a table row based on a 
     * <code>TR</code> element.  This method is used for
     * processing events.
     * 
     * @param trElement the <code>TR</code> element to evaluate
     * @return the row index
     */
    getRowIndex: function(trElement) {
        var stringIndex = trElement.id.lastIndexOf("_tr_") + 4;
        var rowIndex = trElement.id.substring(stringIndex);
        if (rowIndex == "header") {
            return -1;
        } else {
            return parseInt(rowIndex, 10);
        }
    },
    
    /**
     * Initializes the state of an <code>EchoTable</code> instance,
     * registering event handlers and binding it to it target
     * <code>TABLE</code> DOM element.
     */
    init: function() {
        var element = this.getElement();
    
        this.selectionState = [];
        this.rowCount = element.rows.length - (this.headerVisible ? 1 : 0); 
        
        if (this.rolloverEnabled || this.selectionEnabled) {
            var mouseEnterLeaveSupport = EchoClientProperties.get("proprietaryEventMouseEnterLeaveSupported");
            for (var rowIndex = 0; rowIndex < this.rowCount; ++rowIndex) {
                var trElement = element.rows[rowIndex + (this.headerVisible ? 1 : 0)];
                if (this.rolloverEnabled) {
                    if (mouseEnterLeaveSupport) {
                        EchoEventProcessor.addHandler(trElement, "mouseenter", "EchoTable.processRolloverEnter");
                        EchoEventProcessor.addHandler(trElement, "mouseleave", "EchoTable.processRolloverExit");
                    } else {
                        EchoEventProcessor.addHandler(trElement, "mouseout", "EchoTable.processRolloverExit");
                        EchoEventProcessor.addHandler(trElement, "mouseover", "EchoTable.processRolloverEnter");
                    }
                }
                if (this.selectionEnabled) {
                    EchoEventProcessor.addHandler(trElement, "click", "EchoTable.processClick");
                    EchoEventProcessor.addHandler(trElement, "mousedown", "EchoTable.processMouseDown");
                }
            }
        }
        
        EchoDomPropertyStore.setPropertyValue(element, "component", this);
    },
    
    /**
     * Determines if a row is selected.
     * 
     * @param index the index of the row to evaluate
     * @return true if the row is selected
     */
    isSelected: function(index) {
        if (this.selectionState.length <= index) {
            return false;
        } else {
            return this.selectionState[index];
        }
    },
    
    /**
     * Processes a row selection (click) event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processClick: function(echoEvent) {
        if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
            return;
        }
    
        if (!this.selectionEnabled) {
            return;
        }
    
        var trElement = echoEvent.registeredTarget;
        var rowIndex = this.getRowIndex(trElement);
        if (rowIndex == -1) {
            return;
        }
        
        EchoDomUtil.preventEventDefault(echoEvent);
    
        if (!this.multipleSelect || !(echoEvent.shiftKey || echoEvent.ctrlKey || echoEvent.metaKey || echoEvent.altKey)) {
            this.clearSelected();
        }
    
        if (echoEvent.shiftKey && this.lastSelectedIndex != -1) {
            var startIndex, endIndex;
            if (this.lastSelectedIndex < rowIndex) {
                startIndex = this.lastSelectedIndex;
                endIndex = rowIndex;
            } else {
                startIndex = rowIndex;
                endIndex = this.lastSelectedIndex;
            }
            for (var i = startIndex; i <= endIndex; ++i) {
                this.setSelected(i, true);
            }
        } else {
            this.lastSelectedIndex = rowIndex;
            this.setSelected(rowIndex, !this.isSelected(rowIndex));
        }
        
        // Update ClientMessage.
        this.updateClientMessage();
        
        // Notify server if required.
        if (this.serverNotify) {
            EchoClientMessage.setActionValue(this.elementId, "action");
            EchoServerTransaction.connect();
        }
    },
    
    /**
     * Processes a row mouse over event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processRolloverEnter: function(echoEvent) {
        if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
            return;
        }
    
        var trElement = echoEvent.registeredTarget;
        var rowIndex = this.getRowIndex(trElement);
        
        if (rowIndex == -1) {
            return;
        }
        
        if (this.rolloverStyle) {
            for (var i = 0; i < trElement.cells.length; ++i) {
                EchoCssUtil.applyTemporaryStyle(trElement.cells[i], this.rolloverStyle);
            }
        }
    },
    
    /**
     * Processes a row mouse out event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processRolloverExit: function(echoEvent) {
        if (!this.enabled || !EchoClientEngine.verifyInput(this.getElement())) {
            return;
        }
    
        var trElement = echoEvent.registeredTarget;
        var rowIndex = this.getRowIndex(trElement);
    
        if (rowIndex == -1) {
            return;
        }
    
        this.drawRowStyle(rowIndex);
    },
    
    /**
     * Sets the selection state of a table row.
     *
     * @param rowIndex the index of the row
     * @param newValue the new selection state (a boolean value)
     */
    setSelected: function(rowIndex, newValue) {
        this.selectionState[rowIndex] = newValue;
    
        // Redraw.
        this.drawRowStyle(rowIndex);
    },
    
    /**
     * Updates the selection state in the outgoing <code>ClientMessage</code>.
     * If any server-side <code>ActionListener</code>s are registered, an action
     * will be set in the ClientMessage and a client-server connection initiated.
     */
    updateClientMessage: function() {
        var propertyElement = EchoClientMessage.createPropertyElement(this.elementId, "selection");
    
        // remove previous values
        while(propertyElement.hasChildNodes()){
            propertyElement.removeChild(propertyElement.firstChild);
        }
        
        for (var i = 0; i < this.rowCount; ++i) {
            if (this.isSelected(i)) {
                var rowElement = EchoClientMessage.messageDocument.createElement("row");
                rowElement.setAttribute("index", i);
                propertyElement.appendChild(rowElement);
            }
        }
    
        EchoDebugManager.updateClientMessage();
    }
});

/**
 * Static object/namespace for Table MessageProcessor 
 * implementation.
 */
EchoTable.MessageProcessor = {

    /**
     * MessageProcessor process() implementation 
     * (invoked by ServerMessage processor).
     *
     * @param messagePartElement the <code>message-part</code> element to process.
     */
    process: function(messagePartElement) {
        for (var i = 0; i < messagePartElement.childNodes.length; ++i) {
            if (messagePartElement.childNodes[i].nodeType == 1) {
                switch (messagePartElement.childNodes[i].tagName) {
                case "init":
                    EchoTable.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                    break;
                case "dispose":
                    EchoTable.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                    break;
                }
            }
        }
    },
    
    /**
     * Processes a <code>dispose</code> message to finalize the state of a
     * Table component that is being removed.
     *
     * @param disposeMessageElement the <code>dispose</code> element to process
     */
    processDispose: function(disposeMessageElement) {
        for (var item = disposeMessageElement.firstChild; item; item = item.nextSibling) {
            var tableElementId = item.getAttribute("eid");
            var table = EchoTable.getComponent(tableElementId);
            if (table) {
                table.dispose();
            }
        }
    },
    
    /**
     * Processes an <code>init</code> message to initialize the state of a 
     * Table component that is being added.
     *
     * @param initMessageElement the <code>init</code> element to process
     */
    processInit: function(initMessageElement) {
        var rolloverStyle = initMessageElement.getAttribute("rollover-style");
        var selectionStyle = initMessageElement.getAttribute("selection-style");
    
        for (var item = initMessageElement.firstChild; item; item = item.nextSibling) {
            var tableElementId = item.getAttribute("eid");
            
            var table = new EchoTable(tableElementId);
            table.enabled = item.getAttribute("enabled") != "false";
            table.headerVisible = item.getAttribute("header-visible") == "true";
            table.rolloverEnabled = item.getAttribute("rollover-enabled") == "true";
            if (table.rolloverEnabled) {
                table.rolloverStyle = rolloverStyle;
            }
            table.selectionEnabled = item.getAttribute("selection-enabled") == "true";
            if (table.selectionEnabled) {
                table.selectionStyle = selectionStyle;
                table.multipleSelect = item.getAttribute("selection-mode") == "multiple";
                table.serverNotify = item.getAttribute("server-notify") == "true";
            }
            
            table.init();
            
            var rowElements = item.getElementsByTagName("row");
            for (var rowIndex = 0; rowIndex < rowElements.length; ++rowIndex) {
                var tableRowIndex = parseInt(rowElements[rowIndex].getAttribute("index"), 10);
                table.setSelected(tableRowIndex, true);
            }
        }
    }
};
