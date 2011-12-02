/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/

package org.topcased.requirement.document.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Composite of Description
 */
public class DescriptionComposite extends Composite
{
    private Text text;

    private NotifyElement notifyElement;
    
    /**
     * Create the composite.
     */
    public DescriptionComposite(Composite parent, int style)
    {
        super(parent, style);
        setLayout(new GridLayout(1, false));
        
        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        
        Label lblEndlabel = new Label(group, SWT.NONE);
        lblEndlabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblEndlabel.setText("EndLabel");
        
        text = new Text(group, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        hookListeners();
    }

    private void hookListeners()
    {
        text.addModifyListener(new ModifyListener()
        {
            
            public void modifyText(ModifyEvent e)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });
    }
    
    /**
     * return if the description is filled or not
     * 
     * @return
     */
    public boolean isComplete()
    {
        return !(text.getText().isEmpty());
    }
    
    /**
     * Sets the notifier
     * 
     * @param notifyElement
     */
    public void setNotifyElement(NotifyElement notifyElement)
    {
        this.notifyElement = notifyElement;
    }
    
    /**
     * Gets the description
     * 
     * @return
     */
    public String getText()
    {
        return text.getText();
    }
    
    /**
     * Sets a description
     * 
     * @param textToSet
     */
    public void setText(String textToSet)
    {
        text.setText(textToSet);
    }
    
}
