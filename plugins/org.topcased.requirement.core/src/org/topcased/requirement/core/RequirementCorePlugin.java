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
package org.topcased.requirement.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RequirementCorePlugin extends AbstractUIPlugin
{
    /** Icon path of a container of all references of currents requirement */
    public static final String ICONS_CURRENT_REQUIREMENT_REFERENCES_GIF = "icons/CurrentRequirementReferences.GIF";

    /** Icon path of a current requirement reference */
    public static final String ICONS_CURRENT_REQUIREMENT_REFERENCE_GIF = "icons/CurrentRequirementReference.GIF";

    /** Icon path of the trash overlay */
    public static final String ICONS_TRASH_OVERLAY_GIF = "icons/trashOverlay.gif";

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

    /**
     * Initialize all image needed in the plugin
     */
    private void initImageRegistry()
    {
        getImageRegistry().put(ICONS_CURRENT_REQUIREMENT_REFERENCE_GIF,getImageDescriptor(ICONS_CURRENT_REQUIREMENT_REFERENCE_GIF));
        getImageRegistry().put(ICONS_CURRENT_REQUIREMENT_REFERENCES_GIF,getImageDescriptor(ICONS_CURRENT_REQUIREMENT_REFERENCES_GIF));
        getImageRegistry().put(ICONS_TRASH_OVERLAY_GIF,getImageDescriptor(ICONS_TRASH_OVERLAY_GIF));
    }

    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        initImageRegistry();
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
    
    public Image getImage(String key)
    {
        return getImageRegistry().get(key);
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
