/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.actions;

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.topcased.requirement.core.Messages;

/**
 * An action to load a resource into an editing domain's resource set.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 */
public class LoadResourceAction extends Action
{
    private EditingDomain domain;

    public LoadResourceAction(EditingDomain domain)
    {
        this();
        setText(Messages.getString("CurrentRequirementView.25")); //$NON-NLS-1$
        this.domain = domain;
        update();
    }

    public LoadResourceAction()
    {
        super(EMFEditUIPlugin.INSTANCE.getString("_UI_LoadResource_menu_item")); //$NON-NLS-1$
        setDescription(EMFEditUIPlugin.INSTANCE.getString("_UI_LoadResource_menu_item_description")); //$NON-NLS-1$
    }

    /**
     * This returns the action's domain.
     */
    public EditingDomain getEditingDomain()
    {
        return domain;
    }

    /**
     * This sets the action's domain.
     */
    public void setEditingDomain(EditingDomain d)
    {
        this.domain = d;
    }

    @Override
    public void run()
    {
        LoadResourceDialog loadResourceDialog = new LoadResourceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), domain);

        loadResourceDialog.open();

        if (loadResourceDialog.getReturnCode() == Dialog.OK)
        {
            URI uri = null;
            String res = loadResourceDialog.getModelFile();
            if (loadResourceDialog.isPlatform())
            {
                uri = URI.createPlatformResourceURI(res, false);
            }
            else
            {
                uri = URI.createFileURI(res);
            }
            domain.getResourceSet().getResource(uri, true);
        }
    }

    public void update()
    {
        setEnabled(domain != null);
    }

    /**
     * @deprecated As of EMF 2.1.0, replaced by {@link #setActiveWorkbenchPart}.
     */
    @Deprecated
    public void setActiveEditor(IEditorPart editorPart)
    {
        setActiveWorkbenchPart(editorPart);
    }

    /**
     * @since 2.1.0
     */
    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
        setEditingDomain(workbenchPart instanceof IEditingDomainProvider ? ((IEditingDomainProvider) workbenchPart).getEditingDomain() : null);
    }

    /**
     * A dialog to load a model
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     */
    public static class LoadResourceDialog extends ResourceDialog
    {
        private EditingDomain domain;

        private String modelFile;

        private boolean isPlatform;

        public LoadResourceDialog(Shell parent)
        {
            this(parent, null);
        }

        public LoadResourceDialog(Shell parent, EditingDomain domain)
        {
            super(parent, EMFEditUIPlugin.INSTANCE.getString("_UI_LoadResourceDialog_title"), SWT.OPEN | SWT.MULTI); //$NON-NLS-1$
            this.domain = domain;
        }

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
}
