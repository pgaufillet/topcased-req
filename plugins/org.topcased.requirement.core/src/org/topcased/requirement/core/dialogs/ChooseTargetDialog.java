/***********************************************************************************************************************
 * Copyright (c) 2009 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 *               Maxime AUDRAIN (CS) - API Changes
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.dialogs;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.sam.Flow;
import org.topcased.sam.FlowGroup;

/**
 * This dialog allows to choose the target where requirement(s) should be attached. It can be either on a {@link Flow} or
 * on a {@link FlowGroup}.<br>
 * 
 * Creation : 21 January 2009<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.4.0
 */
public class ChooseTargetDialog extends Dialog
{
    /** eObject selected */
    private Flow flow;

    /** This adaper factory */
    private AdapterFactoryLabelProvider adapter;

    /** The result to return 0 = Flow, 1 = FlowGroup */
    private int result;

    /**
     * Constructor
     * 
     * @param parentShell The parent shell
     * @param selectedFlow The dropped Flow.
     */
    public ChooseTargetDialog(Shell parentShell, Flow selectedFlow)
    {
        super(parentShell);
        flow = selectedFlow;
        adapter = new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText("Target selection");
        shell.setSize(400, 250);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Label label = new Label(composite, SWT.NONE);
        label.setText("Select a target for the drop operation.");

        final Group group = new Group(composite, SWT.BORDER);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setLayout(new GridLayout());
        group.setText("Attach on : ");

        final Button flowOption = new Button(group, SWT.RADIO);
        flowOption.setText(getFlowLabel());
        flowOption.setSelection(true);
        flowOption.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                result = 0;
            }
        });

        final Button flowgroupOption = new Button(group, SWT.RADIO);
        flowgroupOption.setText(getFlowGroupLabel());
        flowgroupOption.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                result = 1;
            }
        });

        return parent;
    }

    /**
     * Gets the result set by the user (Flow or FlowGroup ?).
     * 
     * @see org.eclipse.jface.window.Window#getReturnCode() @return <code>0</code> if the user has selected a flow
     *      element, <code>1</code> if the user has selected a flow group element.
     */
    @Override
    public int getReturnCode()
    {
        return result;
    }

    /**
     * Gets the label of a {@link Flow}.
     * 
     * @return The label of the {@link Flow} given by the adapted label provider
     */
    protected String getFlowLabel()
    {
        return adapter != null ? adapter.getText(flow) : "Flow";
    }

    /**
     * Gets the label of a {@link FlowGroup}.
     * 
     * @return The label of the {@link FlowGroup} given by the adapted label provider
     */
    protected String getFlowGroupLabel()
    {
        FlowGroup flowGroup = flow.getGroup();
        return adapter != null ? adapter.getText(flowGroup) : "FlowGroup";
    }
    
    
    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // just create an OK button
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
    }
}
