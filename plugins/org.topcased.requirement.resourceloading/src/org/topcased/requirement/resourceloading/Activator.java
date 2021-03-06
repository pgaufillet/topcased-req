/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.resourceloading;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.topcased.requirement.resourceloading"; //$NON-NLS-1$

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

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
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

    /**
     * Gets the Id of the plugin
     * 
     * @return the plugin identifier
     */
    public static String getId()
    {
        return getDefault().getBundle().getSymbolicName();
    }

}
