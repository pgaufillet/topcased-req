/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.topcased.sam.presentation.SAMEditor;
import org.topcased.sam.requirement.core.views.upstream.UpstreamPage;
import org.topcased.sam.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.topcased.requirement.generic";

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    public static boolean isCurrentEditorSamEditor(IEditorPart m)
    {
        return m instanceof SAMEditor;
    }

    public static boolean isCurrentEditorSamEditor()
    {
        return getActiveEditor() instanceof SAMEditor;
    }

    public static IEditorPart getActiveEditor()
    {
        IEditorPart result = null;
        if (PlatformUI.getWorkbench() != null)
        {
            if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null)
            {
                if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null)
                {
                    if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null)
                    {
                        result = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }

    public static boolean currentEditorHasRequirements()
    {
        return UpstreamRequirementView.getInstance() != null && ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage() instanceof UpstreamPage
                && ((UpstreamPage) ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage()).getViewer() != null
                && ((UpstreamPage) ((UpstreamRequirementView) UpstreamRequirementView.getInstance()).getCurrentPage()).getViewer().getInput() != null;
    }

}
