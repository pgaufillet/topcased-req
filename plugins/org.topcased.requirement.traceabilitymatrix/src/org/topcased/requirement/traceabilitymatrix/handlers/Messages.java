package org.topcased.requirement.traceabilitymatrix.handlers;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.topcased.requirement.traceabilitymatrix.handlers.messages"; //$NON-NLS-1$

    public static String ExportProjectRequirementTraceability_FOOTER;

    public static String ExportProjectRequirementTraceability_HEADER;
    
    public static String ExportProjectRequirementTraceability_ASSOCIATED_COL_TITLE;

    public static String ExportProjectRequirementTraceability_ASSOCIATED_PATH_COL_TITLE;

    public static String ExportProjectRequirementTraceability_CHARSET;

    public static String ExportProjectRequirementTraceability_CURRENT_ID_COL_TITLE;

    public static String ExportProjectRequirementTraceability_DATE_FORMAT;

    public static String ExportProjectRequirementTraceability_DESCRIPTION_COL_TITLE;

    public static String ExportProjectRequirementTraceability_DOCUMENT_COL_TITLE;

    public static String ExportProjectRequirementTraceability_LINK_COL_TITLE;

    public static String ExportProjectRequirementTraceability_PARTIAL_COL_TITLE;

    public static String ExportProjectRequirementTraceability_REQUIREMENT_FILE_EXTENSION;

    public static String ExportProjectRequirementTraceability_UPSTREAM_COL_TITLE;

    public static String ExportProjectRequirementTraceability_XLS_EXTENSION;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
