/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * Contributors: Christophe Mertz (CS) christophe.mertz@c-s.fr
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.topcased.requirement.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.topcased.sam.presentation.SAMEditor;

/**
 * The activator class controls the plug-in life cycle
 */
public class RequirementCorePlugin extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.topcased.requirement.core";

    // The shared instance
    private static RequirementCorePlugin plugin;
    
    protected static boolean once = false;

    /**
     * The constructor
     */
    public RequirementCorePlugin()
    {
        super();
        plugin = this;
    }

    public void start(BundleContext context) throws Exception
    {
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception
    {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static RequirementCorePlugin getDefault()
    {
        return plugin;
    }

    /**
     * Returns the active editor or <code>null</code> if none.
     * 
     * @return the active editor
     */
    public static IEditorPart getActiveEditor()
    {
        IWorkbenchPage page = getActivePage();
        if (page != null)
        {
            return page.getActiveEditor();
        }
        return null;
    }

    /**
     * Returns the active workbench page or <code>null</code> if none.
     * 
     * @return the active workbench page
     */
    public static IWorkbenchPage getActivePage()
    {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        if (window != null)
        {
            return window.getActivePage();
        }
        return null;
    }

    /**
     * Returns the active workbench window
     * 
     * @return the active workbench window
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow()
    {
        if (getDefault() == null)
        {
            return null;
        }
        IWorkbench workBench = getDefault().getWorkbench();
        if (workBench == null)
        {
            return null;
        }
        return workBench.getActiveWorkbenchWindow();
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return AbstractUIPlugin.imageDescriptorFromPlugin(getId(), path);
    }

    /**
     * Gets the Id of the Plugin
     * 
     * @return the Plugin Identifer
     */
    public static String getId()
    {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * Logs a message with a given level into the PDE Error Log
     * 
     * @param e the exception with its stack trace
     */
    public static void log(Exception e)
    {
        log(e.getMessage(), IStatus.ERROR, e);
    }

    /**
     * Logs a message with a given level into the PDE Error Log
     * 
     * @param severity The severity of the item
     * @param message the message to log
     * @param e the exception containing the stack trace
     */
    public static void log(String message, int severity, Exception e)
    {
        IStatus status = new Status(severity, getId(), severity, message, e);
        getDefault().getLog().log(status);
    }
    
    public static boolean getOnce()
    {
        return once;
    }
    
    public static void setOnce(boolean once1)
    {
        once = once1;
    }
}
