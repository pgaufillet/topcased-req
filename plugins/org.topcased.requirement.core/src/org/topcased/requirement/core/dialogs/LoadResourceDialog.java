/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *                Maxime AUDRAIN (CS) - API Changes
 * 
 *****************************************************************************/

package org.topcased.requirement.core.dialogs;

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog to load a model
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class LoadResourceDialog extends ResourceDialog
{
    private EditingDomain domain;

    private String modelFile;

    private boolean isPlatform;

    /**
     * Constructor
     * 
     * @param parent the parent shell
     */
    public LoadResourceDialog(Shell parent)
    {
        this(parent, null);
    }

    /**
     * Constructor
     * 
     * @param parent the parent shell
     * @param domain the editing domain
     */
    public LoadResourceDialog(Shell parent, EditingDomain domain)
    {
        super(parent, EMFEditUIPlugin.INSTANCE.getString("_UI_LoadResourceDialog_title"), SWT.OPEN | SWT.MULTI); //$NON-NLS-1$
        this.domain = domain;
    }

    /**
     * @see org.eclipse.emf.common.ui.dialogs.ResourceDialog#processResources()
     */
    @Override
    protected boolean processResources()
    {
        if (domain != null)
        {
            for (URI uri : getURIs())
            {
                if (uri.isFile())
                {
                    setModelFile(uri.toFileString());
                    setPlatform(false);
                }
                if (uri.isPlatform())
                {
                    setModelFile(uri.toPlatformString(true));
                    setPlatform(true);
                }
            }
        }
        return true;
    }

    protected boolean processResource(Resource resource)
    {
        return true;
    }

    public void setModelFile(String modelFile)
    {
        this.modelFile = modelFile;
    }

    public String getModelFile()
    {
        return modelFile;
    }

    public void setPlatform(boolean isplatform)
    {
        this.isPlatform = isplatform;
    }

    public boolean isPlatform()
    {
        return isPlatform;
    }
}
