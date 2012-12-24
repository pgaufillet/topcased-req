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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.topcased.requirement.document.elements.Attribute;

/**
 * Composite of Description
 */
public class DescriptionComposite extends Composite
{
    private Text text;

    private Text descriptionRegex;

    private NotifyElement notifyElement;
    
    private Label lblDescriptionAttribute;
    
    private ComboViewer comboViewer;

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

        Label lblDescriptionRegex = new Label(group, SWT.NONE);
        lblDescriptionRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblDescriptionRegex.setText("Description Regex");

        descriptionRegex = new Text(group, SWT.BORDER);
        descriptionRegex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        lblDescriptionAttribute = new Label(group, SWT.NONE);
        lblDescriptionAttribute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblDescriptionAttribute.setText("Description Attribute");
        lblDescriptionAttribute.setVisible(false);
        lblDescriptionAttribute.setEnabled(false);
        
        
        comboViewer = new ComboViewer(group, SWT.READ_ONLY);
        comboViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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

        descriptionRegex.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                if (notifyElement != null)
                {
                    notifyElement.handleModelChange();
                }
            }
        });
        
        comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            
            public void selectionChanged(SelectionChangedEvent event)
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
    public boolean isTextComplete()
    {
        return !(text.getText().isEmpty());
    }

    /**
     * return if the description regex is filled or not
     * 
     * @return
     */
    public boolean isDescriptionRegexComplete()
    {
        return !(descriptionRegex.getText().isEmpty());
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

    /**
     * Gets the description regex
     * 
     * @return
     */
    public String getDescriptionRegex()
    {
        return descriptionRegex.getText();
    }

    /**
     * Sets a description regex
     * 
     * @param textToSet
     */
    public void setDescriptionRegex(String textToSet)
    {
        descriptionRegex.setText(textToSet);
    }

    /**
     * Sets if stereotype attribute combo is visible or not
     * @param visible
     */
    public void setAttributeComboVisible(boolean visible)
    {
        lblDescriptionAttribute.setVisible(visible);
        lblDescriptionAttribute.setEnabled(visible);
        comboViewer.getCombo().setVisible(visible);
        comboViewer.getCombo().setEnabled(visible);
    }
    
    /**
     * gets if a stereotype attribute is selected in the combo
     * @return
     */
    public boolean isAttributeSelected()
    {
        
        if (comboViewer != null)
        {
            return ((IStructuredSelection)comboViewer.getSelection()).size()>0;
        }
        
        return false;
    }
    
    /**
     * return the selected stereotype attribute
     * @return
     */
    public Attribute getAttributeSelection()
    {
        
        if (comboViewer != null && ((IStructuredSelection)comboViewer.getSelection()).size()>0)
        {
            return (Attribute)((IStructuredSelection)comboViewer.getSelection()).getFirstElement();
        }
        
        return null;
    }
    
    /**
     * Clear the attribute combo
     */
    public void clearAttributeCombo()
    {
        comboViewer.setInput(Collections.emptyList());
    }
    
    /**
     * fills the combo with the given attributes
     * @param input
     */
    public void fillAttributeCombo(Collection<Attribute> input)
    {
        comboViewer.setContentProvider(new ArrayContentProvider());
        comboViewer.setInput(input);
        comboViewer.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Attribute)
                {
                    Attribute attribute = ((Attribute) element);
                    
                    return attribute.getSource()+"::"+attribute.getProperName();
                }
                return null;
            }
        });
        comboViewer.refresh();
    }
    
}
