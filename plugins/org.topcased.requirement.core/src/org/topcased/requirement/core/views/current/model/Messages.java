package org.topcased.requirement.core.views.current.model;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.topcased.requirement.core.views.current.model.messages"; //$NON-NLS-1$

    public static String CurrentRequirementReference_0;

    public static String CurrentRequirementReference_2;

    public static String CurrentRequirementReference_Error;
    public static String CurrentRequirementReferenceContainer_1;

    public static String CurrentRequirementReferenceContainer_references;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
