/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;
import org.topcased.requirement.core.extensions.internal.UpstreamCombinedStyle;
import org.topcased.requirement.core.extensions.internal.UpstreamStyle;
import org.topcased.requirement.core.extensions.internal.UpstreamStylesManager;

import ttm.Requirement;

/**
 * A Helper working around the upstream styles preference page.
 * 
 * @author vhemery
 */
public final class UpstreamStylesPreferenceHelper
{
    /** String delimiter to separate different styles */
    public static final String PREFERENCE_DELIMITER = "\r"; //$NON-NLS-1$

    /** String delimiter to separate styles elements */
    public static final String PREFERENCE_SUB_DELIMITER = "\f"; //$NON-NLS-1$

    /** the preference key for upstream styles */
    public static final String UPSTREAM_STYLES_PREFERENCE = "upstreamStyles"; //$NON-NLS-1$

    /** index for name property in upstream style preference */
    private static final int UPSTREAM_STYLE_NAME_INDEX = 0;

    /** index for bold property in upstream style preference */
    private static final int UPSTREAM_STYLE_BOLD_INDEX = UPSTREAM_STYLE_NAME_INDEX + 1;

    /** index for overriding bold property in upstream style preference */
    private static final int UPSTREAM_STYLE_OVERRIDE_BOLD_INDEX = UPSTREAM_STYLE_BOLD_INDEX + 1;

    /** index for italic property in upstream style preference */
    private static final int UPSTREAM_STYLE_ITALIC_INDEX = UPSTREAM_STYLE_OVERRIDE_BOLD_INDEX + 1;

    /** index for overriding italic property in upstream style preference */
    private static final int UPSTREAM_STYLE_OVERRIDE_ITALIC_INDEX = UPSTREAM_STYLE_ITALIC_INDEX + 1;

    /** index for color property in upstream style preference */
    private static final int UPSTREAM_STYLE_COLOR_INDEX = UPSTREAM_STYLE_OVERRIDE_ITALIC_INDEX + 1;

    /** index for overriding color property in upstream style preference */
    private static final int UPSTREAM_STYLE_OVERRIDE_COLOR_INDEX = UPSTREAM_STYLE_COLOR_INDEX + 1;

    /** number of properties per element in upstream style preference */
    private static final int UPSTREAM_STYLE_PROPERTIES_NUMBER = UPSTREAM_STYLE_OVERRIDE_COLOR_INDEX + 1;

    /**
     * Private unused constructor
     */
    private UpstreamStylesPreferenceHelper()
    {
    }

    /**
     * Store the styles in preferences in the suitable format
     * 
     * @param preferenceStore the preference store to use
     * @param styles the updated styles which must be stored in preferences
     */
    public static void storeInPreferences(IPreferenceStore preferenceStore, Collection<UpstreamStyle> styles)
    {
        StringBuffer buffer = new StringBuffer();
        for (UpstreamStyle style : styles)
        {
            // insert separator if needed
            if (buffer.length() > 0)
            {
                buffer.append(PREFERENCE_DELIMITER);
            }
            // append the style values
            for (int i = 0; i < UPSTREAM_STYLE_PROPERTIES_NUMBER; i++)
            {
                String property = " ";
                switch (i)
                {
                    case UPSTREAM_STYLE_NAME_INDEX:
                        property = style.getName();
                        break;
                    case UPSTREAM_STYLE_BOLD_INDEX:
                        property = StringConverter.asString(style.getBold());
                        break;
                    case UPSTREAM_STYLE_OVERRIDE_BOLD_INDEX:
                        property = StringConverter.asString(style.getOverrideBold());
                        break;
                    case UPSTREAM_STYLE_ITALIC_INDEX:
                        property = StringConverter.asString(style.getItalic());
                        break;
                    case UPSTREAM_STYLE_OVERRIDE_ITALIC_INDEX:
                        property = StringConverter.asString(style.getOverrideItalic());
                        break;
                    case UPSTREAM_STYLE_COLOR_INDEX:
                        property = StringConverter.asString(style.getColor());
                        break;
                    case UPSTREAM_STYLE_OVERRIDE_COLOR_INDEX:
                        property = StringConverter.asString(style.getOverrideColor());
                        break;
                }
                buffer.append(property);
                // insert separator before next value
                if (i < UPSTREAM_STYLE_PROPERTIES_NUMBER - 1)
                {
                    buffer.append(PREFERENCE_SUB_DELIMITER);
                }
            }
        }
        String valueString = buffer.toString();
        preferenceStore.setValue(UPSTREAM_STYLES_PREFERENCE, valueString);
    }

    /**
     * Get the upstream styles loaded from extensions and preferences, ordered by decreasing priority
     * 
     * @param preferenceStore the store to load values from
     * @return list of completed upstream styles
     */
    public static Collection<UpstreamStyle> getLoadedStyles(IPreferenceStore preferenceStore)
    {
        LinkedHashMap<String, UpstreamStyle> styles = UpstreamStylesManager.getInstance().getStylesFromExtensions();
        // load style customizations from preferences
        String preferenceValue = preferenceStore.getString(UPSTREAM_STYLES_PREFERENCE);
        StringTokenizer tokenizer = new StringTokenizer(preferenceValue, PREFERENCE_DELIMITER);
        while (tokenizer.hasMoreTokens())
        {
            String styleCustomization = tokenizer.nextToken();
            StringTokenizer subTokenizer = new StringTokenizer(styleCustomization, PREFERENCE_SUB_DELIMITER);
            int subTokenCount = subTokenizer.countTokens();
            if (subTokenCount == UPSTREAM_STYLE_PROPERTIES_NUMBER)
            {
                // recover properties for this style preference
                String name = "";
                boolean isBold = false;
                boolean overrideBold = false;
                boolean isItalic = false;
                boolean overrideItalic = false;
                RGB color = new RGB(0, 0, 0);
                boolean overrideColor = false;
                for (int i = 0; i < subTokenCount; i++)
                {
                    String prop = subTokenizer.nextToken();
                    switch (i)
                    {
                        case UPSTREAM_STYLE_NAME_INDEX:
                            name = prop;
                            break;
                        case UPSTREAM_STYLE_BOLD_INDEX:
                            isBold = Boolean.valueOf(prop);
                            break;
                        case UPSTREAM_STYLE_OVERRIDE_BOLD_INDEX:
                            overrideBold = Boolean.valueOf(prop);
                            break;
                        case UPSTREAM_STYLE_ITALIC_INDEX:
                            isItalic = Boolean.valueOf(prop);
                            break;
                        case UPSTREAM_STYLE_OVERRIDE_ITALIC_INDEX:
                            overrideItalic = Boolean.valueOf(prop);
                            break;
                        case UPSTREAM_STYLE_COLOR_INDEX:
                            color = StringConverter.asRGB(prop);
                            break;
                        case UPSTREAM_STYLE_OVERRIDE_COLOR_INDEX:
                            overrideColor = Boolean.valueOf(prop);
                            break;
                    }
                }
                if (styles.containsKey(name))
                {
                    // update style with preferences values
                    UpstreamStyle style = styles.get(name);
                    style.setBold(isBold);
                    style.setItalic(isItalic);
                    style.setColor(color);
                    style.setOverrideBold(overrideBold);
                    style.setOverrideItalic(overrideItalic);
                    style.setOverrideColor(overrideColor);
                }
            }

        }
        return styles.values();
    }

    /**
     * Get the style to apply to an element, combining valid extensions and preferences.
     * 
     * @param preferenceStore the preference store to rely on
     * @param requirement the requirement on which style is applied
     * @return the style resulting of all valid styles combination
     */
    public static UpstreamCombinedStyle getCombinedStyle(IPreferenceStore preferenceStore, Requirement requirement)
    {
        // get styles ordered by decreasing priority
        Collection<UpstreamStyle> styles = getLoadedStyles(preferenceStore);
        // the values of style which will be updated
        boolean boldIsFinal = false;
        boolean bold = false;
        boolean italicIsFinal = false;
        boolean italic = false;
        boolean colorIsFinal = false;
        RGB color = new RGB(0, 0, 0);
        for (UpstreamStyle style : styles)
        {
            if (style.appliesTo(requirement))
            {
                // update bold
                if (!boldIsFinal)
                {
                    bold = style.getBold();
                    boldIsFinal = !style.getOverrideBold();
                }
                // update italic
                if (!italicIsFinal)
                {
                    italic = style.getItalic();
                    italicIsFinal = !style.getOverrideItalic();
                }
                // update color
                if (!colorIsFinal)
                {
                    color = style.getColor();
                    colorIsFinal = !style.getOverrideColor();
                }
            }
            // break if style is fully defined
            if (boldIsFinal && italicIsFinal && colorIsFinal)
            {
                return new UpstreamCombinedStyle(bold, italic, color);
            }
        }
        // return the resulting style
        return new UpstreamCombinedStyle(bold, italic, color);
    }
}
