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

package org.topcased.requirement.core.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * The unlink dialog with delete requirement model check box
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 *
 */
public class UnlinkDialog extends MessageDialog
{
    private boolean delete = false;
    
    protected Button deleteFromWorkspace;

    /**
     * The unlink dialog with delete requirement model check box
     * 
     * @param parentShell
     * @param dialogTitle
     * @param message
     */
    public UnlinkDialog(Shell parentShell, String dialogTitle, String message)
    {
        super(parentShell, dialogTitle, null, message, MessageDialog.QUESTION, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, Window.OK);
    }

    /**
     * @see org.eclipse.jface.dialogs.MessageDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite) getContainer(parent);

        createDeleteButton(container);

        return container;
    }

    /**
     * @param parent
     * @return control
     */
    protected Control getContainer(Composite parent)
    {
        return super.createDialogArea(parent);
    }

    /**
     * Create the delete check box
     * 
     * @param parent composite
     */
    protected void createDeleteButton(Composite parent)
    {
        deleteFromWorkspace = new Button(parent, SWT.CHECK);
        deleteFromWorkspace.setText("Do you want to delete the requirement resource (cannot be undone) ?");
    }

    /**
     * @see org.eclipse.jface.window.Window#open()
     */
    public int open()
    {
        int result = super.open();
        if (result == OK)
        {
            if (delete)
            {
                return 2;
            }
            else
            {
                return OK;
            }
        }
        else
        {
            return CANCEL;
        }
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
     */
    protected void buttonPressed(int buttonId)
    {
        // OK pressed
        if (buttonId == Window.OK)
        {
            delete = deleteFromWorkspace.getSelection();
        }
        super.buttonPressed(buttonId);
    }
}
