/*****************************************************************************
 * Copyright (c) 2013 Atos.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.uml2.uml.Stereotype;
import org.topcased.requirement.document.Activator;

/**
 * Stereotype Composite
 */
public abstract class StereotypeComposite extends Composite
{

    /** The stereotype List viewer. */
    protected ListViewer stereotypeListViewer;

    /** The delete button. */
    protected Button buttonDelete;

    /** The add button. */
    protected Button buttonAdd;

    protected NotifyElement notifyElement;

    /** The add button image. */
    protected static Image imageAdd;

    /** The remove button image. */
    protected static Image imageRemove;

    static
    {
        try
        {
            imageAdd = new Image(Display.getDefault(), Activator.getDefault().getBundle().getResource("icons/add.gif").openStream());
            imageRemove = new Image(Display.getDefault(), Activator.getDefault().getBundle().getResource("icons/remove.gif").openStream());
        }
        catch (IOException e)
        {
        }
    }

    /**
     * Create the composite.
     */
    public StereotypeComposite(Composite parent, int style)
    {
        super(parent, style);

        Composite compo = new Composite(parent, SWT.NONE);
        compo.setLayout(new GridLayout(3, false));
        compo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        // Create the label
        Label label = new Label(compo, SWT.NONE);
        label.setText("Stereotype : ");
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, true, false, 3, 1));

        stereotypeListViewer = new ListViewer(compo, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        stereotypeListViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2));
        stereotypeListViewer.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Stereotype)
                {
                    return ((Stereotype) element).getQualifiedName();
                }
                return null;
            }
        });
        stereotypeListViewer.setContentProvider(new ArrayContentProvider());

        // Create the add button
        buttonAdd = new Button(compo, SWT.PUSH);
        buttonAdd.setImage(imageAdd);
        buttonAdd.setEnabled(false);

        // Create the delete button
        buttonDelete = new Button(compo, SWT.PUSH);
        buttonDelete.setImage(imageRemove);
        buttonDelete.setEnabled(false);

        hookListeners();
    }

    /**
     * Add Listeners 
     */
    protected void hookListeners()
    {
        stereotypeListViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent event)
            {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    buttonDelete.setEnabled(true);
                }
                else
                {
                    buttonDelete.setEnabled(false);
                }
            }
        });

        buttonAdd.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                BusyIndicator.showWhile(Display.getDefault(), new Runnable()
                {
                    public void run()
                    {
                        SelectStereotypeDialog dialog = new SelectStereotypeDialog(Display.getDefault().getActiveShell(), Display.getDefault().getCursorLocation(), notifyElement, getModelType());
                        dialog.open();
                    }
                });
            }
        });

        buttonDelete.addSelectionListener(new SelectionListener()
        {
            public void widgetSelected(SelectionEvent e)
            {
                ISelection selection = stereotypeListViewer.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
                    removeStereotype((Stereotype) iStructuredSelection.getFirstElement());
                    stereotypeListViewer.setInput(getStereotypes());
                    if (notifyElement instanceof ImportRequirementWizardPageSelectDocument)
                    {
                    	ImportRequirementWizardPageSelectDocument pageSelectDoc = (ImportRequirementWizardPageSelectDocument) notifyElement;
                    	pageSelectDoc.handleStereoptypeChange();
					}
                }
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

    }

    /**
     * Removes a stereotype.
     * 
     * @param s the stereotype to remove
     */
    public void removeStereotype(Stereotype s)
    {
        if (s == null)
        {
            return;
        }
        getStereotypes().remove(s);
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
     * Adds the stereotype to the list.
     * 
     * @param s the new stereotype
     */
    @SuppressWarnings("unchecked")
    public void addStereotype(final Stereotype s)
    {
        if (s == null)
        {
            return;
        }

        if (stereotypeListViewer.getInput() instanceof Collection< ? >)
        {
            Collection<Stereotype> input = (Collection<Stereotype>) stereotypeListViewer.getInput();

            String sQualifiedName = s.getQualifiedName();

            for (Stereotype stereotype : input)
            {
                if (sQualifiedName.equals(stereotype.getQualifiedName()))
                {
                    return;
                }
            }

            ArrayList<Stereotype> c = new ArrayList<Stereotype>();
            c.addAll(input);
            c.add(s);
            setStereotypes(c);
            stereotypeListViewer.setInput(c);
        }
        else
        {
            Collection<Stereotype> a = new ArrayList<Stereotype>();
            a.add(s);
            stereotypeListViewer.setInput(a);
        }
    }

    /**
     * Clears the stereotype list
     */
    public void clear()
    {
        stereotypeListViewer.setInput(Collections.emptyList());
    }

    /**
     * Enables or disables the add button
     * @param b
     */
    public void setAddEnabled(boolean b)
    {
        if (buttonAdd != null)
        {
            buttonAdd.setEnabled(b);
        }
    }

    /**
     * Enables or disables the delete button 
     * @param b
     */
    public void setDeleteEnabled(boolean b)
    {
        if (buttonDelete != null)
        {
            buttonDelete.setEnabled(b);
        }
    }

    /**
     * Sets the stereotypes list input
     * @param stereotypes the stereotypes input
     */
    public void setInput(Collection<Stereotype> stereotypes)
    {
        if (stereotypeListViewer != null)
        {
            stereotypeListViewer.setInput(stereotypes);
        }
    }

    /**
     * Returns the stereotypes list
     * @return the stereotypes list
     */
    public abstract Collection<Stereotype> getStereotypes();

    /**
     * Sets the stereotype list
     * @param s the stereotype list
     */
    public abstract void setStereotypes(Collection<Stereotype> s);

    /**
     * Returns the model type
     * @return the model type
     */
    public abstract String getModelType();

}
