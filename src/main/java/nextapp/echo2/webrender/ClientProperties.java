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

package nextapp.echo2.webrender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A description of the client browser environment.
 */
public class ClientProperties
implements Serializable {
    
    // General CSS/DOM Quirks describing specific out-of-spec behaviors particular to certain browsers.
    
    /**
     * A quirk flag indicating that CSS positioning values do not work correctly when either both 
     * "top" and "bottom" or "left" and "right" positions are set at the same time.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows) [<em>Corrected in IE7 Beta 2 Refresh</em>]</li>
     * </ul>
     */
    public static final String QUIRK_CSS_POSITIONING_ONE_SIDE_ONLY = "quirkCssPositioningOneSideOnly";
    
    /**
     * A quirk flag indicating the only means of achieving 0 padding in table cells is to use 0px padding.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_CSS_BORDER_COLLAPSE_FOR_0_PADDING = "quirkCssBorderCollapseFor0Padding";
    
    /**
     * A quirk flag indicating whether the client will incorrectly render CSS 
     * collapsed borders such that they reside entirely within the region
     * of a component.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     * <p>
     */
    public static final String QUIRK_CSS_BORDER_COLLAPSE_INSIDE = "quirkCssBorderCollapseInside";
    
    /**
     * A quirk flag indicating that the 'fixed' attribute should be used to
     * for fixed-to-element background attachment.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_CSS_BACKGROUND_ATTACHMENT_USE_FIXED = "quirkCssBackgroundAttachmentUseFixed";

    /**
     * A quirk flag indicating that focusing elements should be delayed until the next
     * JavaScript execution context.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_DELAYED_FOCUS_REQUIRED = "quirkDelayedFocusRequired";
    
    // Mozilla-specific Quirk Behaviors (behaviors that are more likely to be described as bugs)
    
    /**
     * A quirk flag indicating whether the client has poor performance when
     * attempting to remove large element hierarchies from a DOM.  This quirk can
     * be alleviated by removing the hierarchy in smaller chunks.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Mozilla (all platforms)</li>
     *  <li>Mozilla Firefox ((all platforms)</li>
     * </ul>
     */
    public static final String QUIRK_MOZILLA_PERFORMANCE_LARGE_DOM_REMOVE = "quirkMozillaPerformanceLargeDomRemove";

    /**
     * A quirk flag describing a Mozilla-specific behavior where the text
     * contained within text input fields may be drawn outside of text
     * input component due to the component having shifted its location
     * on the page.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Mozilla (all platforms)</li>
     *  <li>Mozilla Firefox ((all platforms)</li>
     * </ul>
     */
    public static final String QUIRK_MOZILLA_TEXT_INPUT_REPAINT = "quirkMozillaTextInputRepaint";
    
    /**
     * A quirk flag describing an Opera-specific issue where the 
     * CSSStyleDeclaration.cssText property is unsupported.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Opera</li>
     * </ul>
     */
    public static final String QUIRK_OPERA_NO_CSS_TEXT = "quirkOperaNoCssText";

    /**
     *  A quirk flag indicating that Safari does not properly escape XML when sending over
     *  XmlHttpRequest. See https://bugs.webkit.org/show_bug.cgi?id=18421
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Safari (Windows/Mac)</li>
     * </ul>
     */
    public static final String QUIRK_SAFARI_UNESCAPED_XHR = "quirkSafariUnescapedXHR";

    /**
     * The test value for determining if the browser escapes XHR requests properly
     */
    public static final String UNESCAPED_XHR_TEST = "unescapedXhrTest";
    
    // Internet Explorer-specific Quirk Behaviors (behaviors that are more likely to be described as bugs)
    
    /**
     * A quirk flag describing the issue of "windowed" select fields in Internet Explorer, which do not
     * render correctly with regard to z-index value.
     * See http://support.microsoft.com/kb/q177378/ for an explanation of the underlying issue.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows) [<em>Corrected in IE7 Beta 2 Refresh</em>]</li>
     * </ul>
     */
    public static final String QUIRK_IE_SELECT_Z_INDEX = "quirkIESelectZIndex";
    
    /**
     * A quirk flag describing a behavior where a specific "null" option be 
     * must be added to a drop-down-list-style SELECT element in order to to
     * render a "no items selected" state.   
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Mozilla (all platforms)</li>
     *  <li>Mozilla Firefox ((all platforms)</li>
     * </ul>
     */
    public static final String QUIRK_SELECT_REQUIRES_NULL_OPTION = "quirkSelectRequiresNullOption";
    
    /**
     * A quirk flag describing various issues with rendering content in TEXTAREA elements.
     * This quirk includes IE problems with "newline obliteration", and Opera8 problems with
     * simply ignoring textarea content with importNode().
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     *  <li>Opera 8 (Linux and Windows Tested, assuming all))</li>
     * </ul>
     */
    public static final String QUIRK_TEXTAREA_CONTENT = "quirkTextareaContent";
    
    /**
     * A quirk flag indicating the incorrect parsing of newlines in the content of a 'textarea'.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     * 
     * This a more specific version of <code>QUIRK_TEXTAREA_CONTENT</code>.  Now that it has been
     * discovered that Opera has similar issues, <code>QUIRK_TEXTAREA_CONTENT</code> should be used
     * instead.
     */
    public static final String QUIRK_IE_TEXTAREA_NEWLINE_OBLITERATION = "quirkIETextareaNewlineObliteration";
    
    /**
     * A quirk flag describing the curious repaint behavior found in Internet 
     * Explorer 6, where repaints may be excessively delayed.  
     * This quirky behavior is most visible when the DOM hierarchy is large and
     * complex.  
     * The unlikely workaround for this quirky behavior is to "tickle" (adjust
     * and then reset) the CSS width of an element, which will force an 
     * immediate repaint.   
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_IE_REPAINT = "quirkIERepaint";
    
    /**
     * A quirk flag indicating incorrect calculation of 100% table widths when within a vertically scrolling
     * region.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_IE_TABLE_PERCENT_WIDTH_SCROLLBAR_ERROR = "quirkIETablePercentWidthScrollbarError";
    
    /**
     * A quirk flag indicating that listbox-style select fields cannot be reliably manipulated using the client DOM API.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_IE_SELECT_LIST_DOM_UPDATE = "quirkIESelectListDomUpdate";
    
    /**
     * A quirk flag indicating that select fields with percentage widths are not reliably rendered.
     * <p>
     * This quirk occurs with:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String QUIRK_IE_SELECT_PERCENT_WIDTH = "quirkIESelectPercentWidth";

    // Internet Explorer-specific Proprietary Features
    // These features are used only to compensate for IE6's lack of proper CSS support.
    
     /**
     * A proprietary feature flag indicating support for IE-style CSS expressions.
     * <p>
     * This proprietary feature is provided by:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String PROPRIETARY_IE_CSS_EXPRESSIONS_SUPPORTED = "proprietaryIECssExpressionsSupported";
    
    /**
     * A proprietary feature flag indicating that PNG alpha channel support is
     * available only by using a 'filter'.
     * <p>
     * This proprietary feature is provided by:
     * <ul>
     *  <li>Internet Explorer 6 (Windows) [<em>Not necessary with IE7 Beta 2 Refresh</em>]</li>
     * </ul>
     */
    public static final String PROPRIETARY_IE_PNG_ALPHA_FILTER_REQUIRED = "proprietaryIEPngAlphaFilterRequired";
    
    /**
     * A proprietary feature flag indicating that CSS opacity support is
     * available only by using a 'filter'.
     * <p>
     * This proprietary feature is provided by:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String PROPRIETARY_IE_OPACITY_FILTER_REQUIRED = "proprietaryIEOpacityFilterRequired";
    
    /**
     * A proprietary feature flag indicating that 'mouseenter' and 
     * 'mouseleave' events are supported.
     * <p>
     * This proprietary feature is provided by:
     * <ul>
     *  <li>Internet Explorer 6 (Windows)</li>
     * </ul>
     */
    public static final String PROPRIETARY_EVENT_MOUSE_ENTER_LEAVE_SUPPORTED = "proprietaryEventMouseEnterLeaveSupported";
    
    /**
     * An unsupported feature flag indicating that CSS opacity effects are 
     * not supported.
     * <p>
     * This issue occurs with:
     * <ul>
     *  <li>Opera</li>
     * </ul>
     */
    public static final String NOT_SUPPORTED_CSS_OPACITY = "notSupportedCssOpacity";
    
    /**
     * An unsupported feature flag indicating that manipulating CSS stylesheets is not
     * supported by the browser (using add/insert rule).
     * <p>
     * This issue occurs with:
     * <ul>
     *  <li>Safari (verified)</li>
     *  <li>KHTML (assumed)</li>
     * </ul>
     */
    public static final String NOT_SUPPORTED_CSS_MANIPULATION = "notSupportedCssManipulation"; 
    
    // General Browser Properties
    
    /**
     * Width of the screen in pixels (integer).
     */
    public static final String SCREEN_WIDTH = "screenWidth";

    /**
     * Height of the screen in pixels (integer).
     */
    public static final String SCREEN_HEIGHT = "screenHeight";

    /**
     * Color depth of the screen in bits (integer).
     */
    public static final String SCREEN_COLOR_DEPTH = "screenColorDepth";
    
    /**
     * Flag indicating that the browser is a derivative of the Mozilla 
     * 1.0-1.8+ browser platform. 
     */
    public static final String BROWSER_MOZILLA = "browserMozilla";
    
    /**
     * Flag indicating that the browser is a derivative of the Mozilla
     * Firefox 1.0+ browser platform.
     */
    public static final String BROWSER_MOZILLA_FIREFOX = "browserMozillaFirefox";
    
    /**
     * Flag indicating that the browser is a derivative of the Microsoft
     * Internet Explorer browser platform.
     */
    public static final String BROWSER_INTERNET_EXPLORER = "browserInternetExplorer";
    
    /**
     * Flag indicating that the browser is a derivative of the KDE Konqueror
     * browser platform.
     */
    public static final String BROWSER_KONQUEROR = "browserKonqueror";
    
    /**
     * Flag indicating that the browser is a derivative of the Apple Safari
     * browser platform.
     */
    public static final String BROWSER_SAFARI = "browserSafari";
    
    /**
     * Flag indicating that the browser is a derivative of the Opera
     * browser platform.
     */
    public static final String BROWSER_OPERA = "browserOpera";
    
    /**
     * The major version number of the browser.
     */
    public static final String BROWSER_VERSION_MAJOR = "browserVersionMajor";
    
    /**
     * The minor version number of the browser.
     */
    public static final String BROWSER_VERSION_MINOR = "browserVersionMinor";
    
    /**
     * The <code>Locale</code> of the client, derived from the language property.
     */
    public static final String LOCALES = "locales";
    
    /**
     * The client's navigator.appName property.
     */
    public static final String NAVIGATOR_APP_NAME = "navigatorAppName";

    /**
     * The client's navigator.appVersion property.
     */
    public static final String NAVIGATOR_APP_VERSION = "navigatorAppVersion";

    /**
     * The client's navigator.appCodeName property.
     */
    public static final String NAVIGATOR_APP_CODE_NAME = "navigatorAppCodeName";

    /**
     * The client's navigator.cookieEnabled property.
     */
    public static final String NAVIGATOR_COOKIE_ENABLED = "navigatorCookieEnabled";

    /**
     * The client's navigator.javaEnabled property.
     */
    public static final String NAVIGATOR_JAVA_ENABLED = "navigatorJavaEnabled";

    /**
     * The client's navigator.language (or navigator.userLanguage) property.
     */
    public static final String NAVIGATOR_LANGUAGE = "navigatorLanguage";

    /**
     * The client's navigator.platform property.
     */
    public static final String NAVIGATOR_PLATFORM = "navigatorPlatform";

    /**
     * The client's navigator.userAgent property.
     */
    public static final String NAVIGATOR_USER_AGENT = "navigatorUserAgent";

    /**
     * The client host.  Note this is the original host address used when the 
     * <code>ClientProperties</code> object was created, which is not 
     * necessarily the same as that making the current HTTP request. 
     */
    public static final String REMOTE_HOST = "remoteHost";
    
    /**
     * The client's time offset from UTC in minutes.
     */
    public static final String UTC_OFFSET = "utcOffset";
    
    private Map data = new HashMap();
    
    /**
     * Creates a new <code>ClientProperties</code> object.
     */
    public ClientProperties() {
        super();
    }
    
    /**
     * Returns the value of the specified property as an <code>Object</code>.
     *
     * @param propertyName the property name
     * @return the property value 
     */
    public Object get(String propertyName) {
        return data.get(propertyName);
    }
    
    /**
     * Returns a <code>boolean</code> property.  
     * If the property is not set, <code>false</code> is returned.
     * 
     * @param propertyName the property name
     * @return the property value
     */
    public boolean getBoolean(String propertyName) {
        Boolean value = (Boolean) data.get(propertyName);
        return value == null ? false : value.booleanValue();
    }
    
    /**
     * Returns a <code>int</code> property.  
     * If the property is not set, <code>nullValue</code> is returned.
     * 
     * @param propertyName the property name
     * @param nullValue the returned value when the property is not set
     * @return the property value
     */
    public int getInt(String propertyName, int nullValue) {
        Integer value = (Integer) data.get(propertyName);
        return value == null ? nullValue : value.intValue();
    }
    
    /**
     * Returns a <code>String</code> property.  
     * If the property is not set, <code>null</code> is returned.
     * 
     * @param propertyName the property name
     * @return the property value
     */
    public String getString(String propertyName) {
        Object value = data.get(propertyName);
        return value == null ? "" : value.toString();
    }
    
    /**
     * Returns an array of all property names which are set.
     * 
     * @return the array
     */
    public String[] getPropertyNames() {
        return (String[]) data.keySet().toArray(new String[data.size()]);
    }
    
    /**
     * Sets the value of the specified property.
     * 
     * @param propertyName the property name
     * @param propertyValue the property value
     */
    public void setProperty(String propertyName, Object propertyValue) {
        data.put(propertyName, propertyValue);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "ClientProperties: " + data.toString();
    }
}
