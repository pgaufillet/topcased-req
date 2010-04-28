/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RequirementCorePlugin extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.topcased.requirement.core"; //$NON-NLS-1$

    // The shared instance
    private static RequirementCorePlugin plugin;

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
     * Gets the Id of the plugin
     * 
     * @return the plugin identifier
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
}
