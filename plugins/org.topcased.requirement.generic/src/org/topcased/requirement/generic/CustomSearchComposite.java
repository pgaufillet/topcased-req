/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.topcased.requirement.generic.actions.CurrentSearchFilter;
import org.topcased.sam.requirement.AttributeConfiguration;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.core.Messages;

/**
 * The Class CustomSearchComposite.
 */
public class CustomSearchComposite extends Composite
{

    private CurrentSearchFilter filter;

    private TreeViewer viewer;

    /**
     * Constructor
     * 
     * @param parent The parent composite
     * @param style The creation style
     */
    public CustomSearchComposite(Composite parent, int style)
    {
        super(parent, style);
        final GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        final Label label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        label.setText(Messages.getString("SearchComposite.0")); //$NON-NLS-1$

        final Text textFilter = new Text(this, SWT.BORDER | SWT.FILL);
        textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        final Button btn = new Button(this, SWT.PUSH);
        btn.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        btn.setText(Messages.getString("SearchComposite.1")); //$NON-NLS-1$
        btn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String text = textFilter.getText();
                if (text.length() == 0)
                {
                    viewer.removeFilter(filter);
                }
                else
                {
                    viewer.setSelection(null);
                    setFilter(viewer, CurrentSearchFilter.getInstance());
                    filter.setSearched(text);
                }
                viewer.refresh();
                viewer.expandAll();
                for (Object element : viewer.getExpandedElements())
                {
                    if (element instanceof AttributeConfiguration)
                    {
                        viewer.collapseToLevel(element, 1);
                    }
                    else if (element instanceof CurrentRequirement)
                    {
                        viewer.collapseToLevel(element, 1);
                    }
                }
            }
        });
    }

    /**
     * Sets the filter applied on a given viewer
     * 
     * @param theViewer The {@link Viewer} on which the filter is applied.
     * @param theFilter The {@link IRequirementFilterviewer} filter
     */
    public void setFilter(Viewer theViewer, CurrentSearchFilter theFilter)
    {
        viewer = (TreeViewer) theViewer;
        boolean found = false;
        for (int i = 0; i < viewer.getFilters().length; i++)
        {
            ViewerFilter filter = viewer.getFilters()[i];
            if (filter.getClass().getName().contains("sam"))
            {
                viewer.removeFilter(filter);
            }
            if (filter == theFilter)
            {
                found = true;
            }
        }
        if (!found)
        {
            viewer.addFilter(theFilter);
        }
        filter = theFilter;
    }

}
