/*******************************************************************************
 * Copyright (c) 2010, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.topcased.requirement.current2upstream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class RequirementCurrent2UpstreamPlugin extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.topcased.requirement.current2upstream";

	/**
	 * The shared instance.
	 */
	private static RequirementCurrent2UpstreamPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public RequirementCurrent2UpstreamPlugin() {
	}

	/**{@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**{@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static RequirementCurrent2UpstreamPlugin getDefault() {
		return plugin;
	}
	
	
	public static void log(String message, int severity, Exception e)
    {
        IStatus status = new Status(severity, getId(), severity, message, e);
        getDefault().getLog().log(status);
    }

}
