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

//_________________________
// Object EchoListComponent

/**
 * Rendering/synchronization services for AbstractListComponent-based components.
 * This object/namespace should not be used externally.
 */
EchoListComponent = Core.extend({

    $static: {
    
        /**
         * CSS style for selected items in DHTML-rendered list box component. 
         */
        DHTML_SELECTION_STYLE: "background-color:#0a246a;color:#ffffff;",

        /**
         * Returns the ListComponent data object instance based on the root element
         * of the ListComponent.
         *
         * @param element the root element or element id of the ListComponent
         * @return the relevant ListComponent instance
         */
        getComponent: function(element) {
            return EchoDomPropertyStore.getPropertyValue(element, "component");
        },
        
        /**
         * Click event listener for DHTML-based list box components.
         * Looks up appropriate EchoListComponent instance based on event
         * target id and then delegates to corresponding method in
         * EchoListComponent instance.
         * 
         * @param echoEvent the event (that has been fired via EchoEventProcessor)
         */
        processClickDhtml: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var listComponent = EchoListComponent.getComponent(componentId);
            listComponent.processClickDhtml(echoEvent);
        },
        
        /**
         * Click event listener for item mouse enter events.
         * Looks up appropriate EchoListComponent instance based on event
         * target id and then delegates to corresponding method in
         * EchoListComponent instance.
         * 
         * @param echoEvent the event (that has been fired via EchoEventProcessor)
         */
        processRolloverEnter: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var listComponent = EchoListComponent.getComponent(componentId);
            listComponent.processRolloverEnter(echoEvent);
        },
        
        /**
         * Click event listener for item mouse exit events.
         * Looks up appropriate EchoListComponent instance based on event
         * target id and then delegates to corresponding method in
         * EchoListComponent instance.
         * 
         * @param echoEvent the event (that has been fired via EchoEventProcessor)
         */
        processRolloverExit: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var listComponent = EchoListComponent.getComponent(componentId);
            listComponent.processRolloverExit(echoEvent);
        },
        
        /**
         * Click event listener for selection events (for SELECT-element-based components).
         * Looks up appropriate EchoListComponent instance based on event
         * target id and then delegates to corresponding method in
         * EchoListComponent instance.
         * 
         * @param echoEvent the event (that has been fired via EchoEventProcessor)
         */
        processSelection: function(echoEvent) {
            var componentId = EchoDomUtil.getComponentId(echoEvent.registeredTarget.id);
            var listComponent = EchoListComponent.getComponent(componentId);
            listComponent.processSelection(echoEvent);
        },
        
        /**
         * Array.sort() algorithm to sort an array numerically.
         */
        sortArrayNumeric: function(a, b) {
            return a - b;
        },
        
        /**
         * IE-specific event handler to avoid selection highlighting.
         * 
         * @param e the event
         */
        processSelectStart: function(e) {
            EchoDomUtil.preventEventDefault(e);
        }
    },

    /**
     * Static object/namespace for SelectField/ListBox support.
     * This object/namespace should not be used externally.
     * 
     * Creates a new ListComponent
     */
    $construct: function(elementId, containerElementId) { 
        this.elementId = elementId;
        this.containerElementId = containerElementId;
        this.values = null;
        this.styles = null;
        this.selectedIndices = null;
        this.rolloverIndex = -1;
    },

    /**
     * Creates the new list component in the DOM, adding elements beneath the element
     * specified in the containerElementId property.
     * 
     * This method delegates to createSelect()/createDhtml() based on whether or not
     * a DHTML-based listbox component is required in order to appease Internet Explorer's
     * list component quirks.
     */
    create: function() {
        if (this.renderAsListBox) {
            this.renderAsDhtml = EchoClientProperties.get("quirkIESelectListDomUpdate") ? true : false;
            if (this.renderAsDhtml) {
                this.createDhtml();
            } else {
                this.createSelect();
            }
        } else {
            this.createSelect();
        }
        this.loadSelection();
    },
   
    /**
     * Creates a DHTML-based listbox component and adds it to the DOM.
     */
    createDhtml: function() {
        var containerElement = document.getElementById(this.containerElementId);
        
        var selectElement = document.createElement("div");
        selectElement.id = this.elementId;
        selectElement.selectedIndex = -1;
        
        EchoDomUtil.setCssText(selectElement, this.style);
        
        if (this.toolTip) {
            selectElement.setAttribute("title", this.toolTip);
        }
        if (this.tabIndex) {
            selectElement.setAttribute("tabindex", this.tabIndex);
        }
    
        selectElement.style.cursor = "default";
        selectElement.style.overflow = "auto";
        
        if (!selectElement.style.height) {
            selectElement.style.height = "6em";
        }
        if (!selectElement.style.border) {
            selectElement.style.border = "1px inset #cfcfcf";
        }
        
        if (this.values) {
            for (var i = 0; i < this.values.length; ++i) {
                var optionElement = document.createElement("div");
                optionElement.setAttribute("id", this.elementId + "_item_" + i);
                optionElement.appendChild(document.createTextNode(this.values[i]));
                if (this.styles && this.styles[i]) {
                    optionElement.setAttribute("style", this.styles[i]);
                }
                selectElement.appendChild(optionElement);
            }
        }
    
        containerElement.appendChild(selectElement);
        
        EchoEventProcessor.addHandler(selectElement, "click", "EchoListComponent.processClickDhtml");
        if (this.rolloverStyle) {
            EchoEventProcessor.addHandler(selectElement, "mouseover", "EchoListComponent.processRolloverEnter");
            EchoEventProcessor.addHandler(selectElement, "mouseout", "EchoListComponent.processRolloverExit");
        }
        if (EchoClientProperties.get("browserInternetExplorer")) {
            EchoEventProcessor.addHandler(selectElement, "selectstart", "EchoListComponent.processSelectStart");
        }
        
        EchoDomPropertyStore.setPropertyValue(selectElement, "component", this);
    },
    
    /**
     * Creates a list component based on a SELECT element and adds it to the DOM.
     */
    createSelect: function() {
        var containerElement = document.getElementById(this.containerElementId);
        
        var selectElement = document.createElement("select");
        selectElement.id = this.elementId;
        
        if (!this.enabled) {
            selectElement.disabled = true;
        }
    
        EchoDomUtil.setCssText(selectElement, this.style);
        
        if (this.toolTip) {
            selectElement.setAttribute("title", this.toolTip);
        }
        if (this.tabIndex) {
            selectElement.setAttribute("tabindex", this.tabIndex);
        }
        
        if (this.renderAsListBox) {
            selectElement.setAttribute("size", "5");
            if (this.multipleSelection) {
                selectElement.setAttribute("multiple", "multiple");
            }
        }
    
        if (this.values) {
    
            // Sort selectedIndices such that we can iterate through them in order. 
            if (this.selectedIndices) {
                this.selectedIndices.sort(EchoListComponent.sortArrayNumeric);
            }
            var selectionIndex = 0;
            
            for (var i = 0; i < this.values.length; ++i) {
                var optionElement = document.createElement("option");
                optionElement.setAttribute("id", this.elementId + "_item_" + i);
                optionElement.appendChild(document.createTextNode(this.values[i]));
                if (this.styles && this.styles[i]) {
                    optionElement.setAttribute("style", this.styles[i]);
                }
                
                // Set initial selection state.
                // Note that this step is somewhat redundant, as loadSelection()
                // will later be called, but ensuring selections are set here
                // as well removes disconcerting flash in Firefox 
                // (and possibly other browsers as well).
                if (this.selectedIndices && this.selectedIndices[selectionIndex] == i) {
                    optionElement.setAttribute("selected", "selected");
                    ++selectionIndex;
                }
                
                selectElement.appendChild(optionElement);
            }
        }
        
        containerElement.appendChild(selectElement);
        
        EchoEventProcessor.addHandler(selectElement, "change", "EchoListComponent.processSelection");
        if (this.rolloverStyle) {
            EchoEventProcessor.addHandler(selectElement, "mouseover", "EchoListComponent.processRolloverEnter");
            EchoEventProcessor.addHandler(selectElement, "mouseout", "EchoListComponent.processRolloverExit");
        }
        
        EchoDomPropertyStore.setPropertyValue(selectElement, "component", this);
    },
    
    /**
     * Disposes of an <code>EchoListComponent</code> instance, releasing resources
     * and unregistering event handlers.
     */
    dispose: function() {
        this.values = null;
        this.styles = null;
    
        var selectElement = this.getElement();
        if (this.renderAsDhtml) {
            EchoEventProcessor.removeHandler(selectElement, "click");
            if (EchoClientProperties.get("browserInternetExplorer")) {
                EchoEventProcessor.removeHandler(selectElement, "selectstart");
            }
        } else {
            EchoEventProcessor.removeHandler(selectElement, "change");
        }
    
        if (this.rolloverStyle) {
            EchoEventProcessor.removeHandler(selectElement, "mouseover");
            EchoEventProcessor.removeHandler(selectElement, "mouseout");
        }
        
        EchoDomPropertyStore.dispose(selectElement);
    },
    
    /**
     * Configures a DHTML-based list box component to display the currently
     * selected items, if any are present.
     */
    ensureSelectionVisibleDhtml: function() {
        var selectElement = this.getElement();
    
        //TODO finish
        if (!this.selectedIndices) {
            return;
        }
        
        var scrollTop = selectElement.scrollTop;
        var scrollBottom = scrollTop + selectElement.scrollHeight;
        for (var i = 0; i < this.selectedIndices.length; ++i) {
            var itemBounds = new EchoCssUtil.Bounds(selectElement.childNodes[i]);
        }
    },
    
    /**
     * Returns the root DOM element of the rendered component.
     * 
     * @return the root DOM element, or null if it has not 
     *          been rendered / does not exist
     */
    getElement: function() {
        return document.getElementById(this.elementId);
    },
    
    /**
     * Returns the selection index of an item element in a list
     * component.  The item element can be either an OPTION in the
     * case of a normal SELECT-based list component or a DIV in the
     * case of a DHTML-rendered list box.
     * 
     * @param node the DOM node
     * @return the selection index of the node, if applicable, or -1 otherwise
     */
    getNodeIndex: function(node) {
        var selectElement = this.getElement();
        
        var itemPrefix = this.elementId + "_item_";
        while (node != selectElement) {
            if (node.nodeType == 1) {
                var id = node.getAttribute("id");
                if (id && id.indexOf(itemPrefix) === 0) {
                    return parseInt(id.substring(itemPrefix.length), 10);
                }
            }
            node = node.parentNode;
        }
        return -1;
    },
    
    /**
     * Temporarily adds an empty option a drop-down style SELECT element 
     * that will be selected in cases where no option has been selected
     * by the user.  The option will be removed once the user makes
     * an initial selection. 
     */
    addNullOption: function() {
        if (EchoClientProperties.get("quirkSelectRequiresNullOption") &&
                !this.renderAsListBox && !this.nullOptionActive) {
            // Add null option.
            var selectElement = this.getElement();
            var nullOptionElement = document.createElement("option");
            if (selectElement.childNodes.length > 0) {
                selectElement.insertBefore(nullOptionElement, selectElement.options[0]);
            } else {
                selectElement.appendChild(nullOptionElement);
            }
            this.nullOptionActive = true;
        }
    },
    
    /**
     * Removes the temporary empty option from a drop-down style SELECT
     * element that is selected in the case where no option has been
     * selected by the user.
     */
    removeNullOption: function() {
        if (this.nullOptionActive) {
            // Remove null option.
            var selectElement = this.getElement();
            selectElement.removeChild(selectElement.options[0]);
            this.nullOptionActive = false;
        }
    },
    
    /**
     * Updates the selection state based on the values stored in
     * this data object.
     * Delegates this operation to loadSelectionDhtml() in the case
     * of a DHTML listbox component.
     */
    loadSelection: function() {
        if (this.renderAsDhtml) {
            this.loadSelectionDhtml();
        } else {
            var selectElement = this.getElement();
            selectElement.selectedIndex = -1;
            if (selectElement.options.length > 0) {
                selectElement.options[0].selected = false;
            }
            
            if (!this.selectedIndices || this.selectedIndices.length === 0) {
                this.addNullOption();
            } else {
                this.removeNullOption();
                var selectionSet;
                for (var i = 0; i < this.selectedIndices.length; ++i) {
                    if (this.selectedIndices[i] < selectElement.options.length) {
                        selectionSet = true;
                        selectElement.options[this.selectedIndices[i]].selected = true;
                    }
                }
                if (!selectionSet) {
                    this.addNullOption();
                }
            }
        }
    },
    
    /**
     * loadSelection() implementation for DHTML listbox components.
     */
    loadSelectionDhtml: function() {
        var i, selectElement = this.getElement();
        
        for (i = 0; i < selectElement.childNodes.length; ++i) {
            if (this.styles && this.styles[i]) {
                EchoDomUtil.setCssText(selectElement.childNodes[i], this.styles[i]);
            } else {
                EchoDomUtil.setCssText(selectElement.childNodes[i], "");
            }
        }
        if (!this.selectedIndices) {
            return;
        }
        
        for (i = 0; i < this.selectedIndices.length; ++i) {
            if (this.selectedIndices[i] < selectElement.childNodes.length) {
                EchoCssUtil.applyStyle(selectElement.childNodes[this.selectedIndices[i]], 
                        EchoListComponent.DHTML_SELECTION_STYLE);
            }
        }
    },
    
    /**
     * Processes a click event on a DHTML-based list box component.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processClickDhtml: function(echoEvent) {
        var selectElement = this.getElement();
        
        EchoDomUtil.preventEventDefault(echoEvent);
        if (!this.enabled || !EchoClientEngine.verifyInput(selectElement)) {
            return;
        }
        var index = this.getNodeIndex(echoEvent.target);
        if (index == -1) {
            return;
        }
        if (this.multipleSelection && echoEvent.ctrlKey && this.selectedIndices) {
            var oldState = false;
            for (var i = 0; i < this.selectedIndices.length; ++i) {
                if (this.selectedIndices[i] == index) {
                    // Remove item from selection if found.
                    this.selectedIndices[i] = this.selectedIndices.pop();
                    oldState = true;
                    break;
                }
            }
            if (!oldState) {
                // Item was not found in selection: add it to selection.
                this.selectedIndices.push(index);
            }
        } else {
            this.selectedIndices = [];
            this.selectedIndices.push(index);
        }
        this.loadSelectionDhtml();
        this.updateClientMessage();
    },
    
    /**
     * Processes an item mouse enter event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processRolloverEnter: function(echoEvent) {
        var selectElement = this.getElement();
        
        EchoDomUtil.preventEventDefault(echoEvent);
        if (!this.enabled || !EchoClientEngine.verifyInput(selectElement)) {
            return;
        }
        var index = this.getNodeIndex(echoEvent.target);
        if (index == -1) {
            return;
        }
        this.setRolloverIndex(index);
    },
    
    /**
     * Processes an item mouse exit event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processRolloverExit: function(echoEvent) {
        var selectElement = this.getElement();
    
        EchoDomUtil.preventEventDefault(echoEvent);
        if (!this.enabled || !EchoClientEngine.verifyInput(selectElement)) {
            return;
        }
        var index = this.getNodeIndex(echoEvent.target);
        if (index == -1) {
            return;
        }
        this.setRolloverIndex(-1);
    },
    
    /**
     * Processes an item selection (change) event.
     *
     * @param echoEvent the event, preprocessed by the 
     *        <code>EchoEventProcessor</code>
     */
    processSelection: function(echoEvent) {    
        var selectElement = this.getElement();
    
        if (!this.enabled || !EchoClientEngine.verifyInput(selectElement)) {
            this.loadSelection();
            return;
        }
        this.removeNullOption();
        this.storeSelection();
        this.updateClientMessage();
    },
    
    /**
     * Sets the index of the list component which is displaying a mouse rollover effect.
     * 
     * @param rolloverIndex the new rolloverIndex, -1 indicating no rollover effect
     */
    setRolloverIndex: function(rolloverIndex) {
        var selectElement = this.getElement();
        
        if (this.rolloverIndex != -1) {
            var oldElement;
            var oldStyle = null;
            if (this.renderAsDhtml) {
                oldElement = selectElement.childNodes[this.rolloverIndex];
                // Determine if element was selected and if so load selection style as "old style".
                if (this.selectedIndices) {
                    for (var i = 0; i < this.selectedIndices.length; ++i) {
                        if (this.selectedIndices[i] == this.rolloverIndex) {
                            oldStyle = EchoListComponent.DHTML_SELECTION_STYLE;
                            break;
                        }
                    }
                }
            } else {
                oldElement = selectElement.options[this.rolloverIndex];
            }
            if (!oldStyle) {
                oldStyle = this.styles ? this.styles[this.rolloverIndex] : "";
            }
            EchoDomUtil.setCssText(oldElement, oldStyle);
        }
        this.rolloverIndex = rolloverIndex;
        if (this.rolloverIndex != -1) {
            var newElement = this.renderAsDhtml ? 
                    selectElement.childNodes[this.rolloverIndex] : selectElement.options[this.rolloverIndex];
            EchoCssUtil.applyStyle(newElement, this.rolloverStyle);
        }
    },
    
    /**
     * Stores the current selection in the <code>selectedIndices</code> property.
     */
    storeSelection: function() {
        var selectElement = this.getElement();
    
        this.selectedIndices = [];
        for (var i = 0; i < selectElement.options.length; ++i) {
            if (selectElement.options[i].selected) {
                this.selectedIndices.push(i);
            }
        }
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
    
        if (!this.selectedIndices) {
            return;
        }
        
        // add new values        
        for (var i = 0; i < this.selectedIndices.length; ++i) {
            var clientMessageItemElement = EchoClientMessage.messageDocument.createElement("item");
            clientMessageItemElement.setAttribute("index", this.selectedIndices[i]);
            propertyElement.appendChild(clientMessageItemElement);
        }
    
        EchoDebugManager.updateClientMessage();
        
        if (this.serverNotify) {
            EchoClientMessage.setActionValue(this.elementId, "action");
            EchoServerTransaction.connect();
        }
    }
});


/**
 * Static object/namespace for SelectField/ListBox MessageProcessor 
 * implementation.
 */
EchoListComponent.MessageProcessor = {

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
                case "dispose":
                    EchoListComponent.MessageProcessor.processDispose(messagePartElement.childNodes[i]);
                    break;
                case "load-content":
                    EchoListComponent.MessageProcessor.processLoadContent(messagePartElement.childNodes[i]);
                    break;
                case "init":
                    EchoListComponent.MessageProcessor.processInit(messagePartElement.childNodes[i]);
                    break;
                }
            }
        }
    },
    
    /**
     * Processes a <code>dispose</code> message to finalize the state of a
     * selection component that is being removed.
     *
     * @param disposeElement the <code>dispose</code> element to process
     */
    processDispose: function(disposeElement) {
        var elementId = disposeElement.getAttribute("eid");
        var listComponent = EchoListComponent.getComponent(elementId);
        if (listComponent) {
            listComponent.dispose();
        }
    },
    
    /**
     * Processes an <code>init</code> message to initialize the state of a 
     * selection component that is being added.
     *
     * @param initElement the <code>init</code> element to process
     */
    processInit: function(initElement) {
        var elementId = initElement.getAttribute("eid");
        var containerElementId = initElement.getAttribute("container-eid");
    
        var listComponent = new EchoListComponent(elementId, containerElementId);
        listComponent.enabled = initElement.getAttribute("enabled") != "false";
    
        listComponent.serverNotify = initElement.getAttribute("server-notify") == "true";
    
        listComponent.style = initElement.getAttribute("style");
        listComponent.tabIndex = initElement.getAttribute("tab-index");
        listComponent.toolTip = initElement.getAttribute("tool-tip");
        
        var contentId = initElement.getAttribute("content-id");
        listComponent.values = EchoServerMessage.getTemporaryProperty("EchoListComponent.Values." + contentId);
        listComponent.styles = EchoServerMessage.getTemporaryProperty("EchoListComponent.Styles." + contentId);
        
        if (initElement.getAttribute("type") == "list-box") {
            listComponent.rolloverStyle = initElement.getAttribute("rollover-style");
            listComponent.renderAsListBox = true;
            if (initElement.getAttribute("multiple") == "true") {
                listComponent.multipleSelection = true;
            }
        }
    
        // Retrieve selection information.
        listComponent.selectedIndices = [];
        if (listComponent.multipleSelection) {
            var selectionElements = initElement.getElementsByTagName("selection");
            if (selectionElements.length == 1) {
                var itemElements = selectionElements[0].getElementsByTagName("item");
                for (var i = 0; i < itemElements.length; ++i) {
                    listComponent.selectedIndices.push(parseInt(itemElements[i].getAttribute("index"), 10));
                }
            }
        } else {
            var selectedIndex = parseInt(initElement.getAttribute("selection-index"), 10);
            if (!isNaN(selectedIndex)) {
                listComponent.selectedIndices.push(selectedIndex);
            }
        }
    
        listComponent.create();
    },
    
    /**
     * Processes a <code>load-content</code> message to store model/content
     * information in the EchoServerMessage's temporary property store.
     * These model values will be later used during processInit() 
     * (possibly for multiple individual list components).
     *
     * @param loadContentElement the <code>loadContent</code> element to process
     */
    processLoadContent: function(loadContentElement) {
        var item, 
            value,
            contentId = loadContentElement.getAttribute("content-id"), 
            valueArray = [],
            styleArray;
        
        for (item = loadContentElement.firstChild; item; item = item.nextSibling) {
            value = item.getAttribute("value");
            valueArray.push(value);
        }
        EchoServerMessage.setTemporaryProperty("EchoListComponent.Values." + contentId, valueArray);
    
        if (loadContentElement.getAttribute("styled") == "true") {
            styleArray = [];
            for (item = loadContentElement.firstChild; item; item = item.nextSibling) {
                value = item.getAttribute("style");
                styleArray.push(value);
            }
            EchoServerMessage.setTemporaryProperty("EchoListComponent.Styles." + contentId, styleArray);
        }
    }
};
