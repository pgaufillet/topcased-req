package org.topcased.requirement.search.ui.l10n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * l10n/i18n localization, internationalization 
 *
 */
public class Messages
{
    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages()
    {
    }

    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}
