/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.topcased.requirement.core.filters.IRequirementFilter;
import org.topcased.requirement.core.internal.Messages;

/**
 * Defines the search composite that is composed by a label, a text field allowing to fill in a particular request and a
 * button to launch the filtering operation.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public class SearchComposite extends Composite
{

    private IRequirementFilter filter;

    private Viewer viewer;

    /**
     * Constructor
     * 
     * @param parent The parent composite
     * @param style The creation style
     */
    public SearchComposite(Composite parent, int style)
    {
        super(parent, style);
        final GridLayout layout = new GridLayout(3, false);
        // layout.marginHeight = 0;
        // layout.marginWidth = 0;

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
                filter.setSearched(text);
                viewer.refresh();
            }
        });
    }

    /**
     * Sets the filter applied on a given viewer
     * 
     * @param theViewer The {@link Viewer} on which the filter is applied.
     * @param theFilter The {@link IRequirementFilterviewer} filter
     */
    public void setFilter(Viewer theViewer, IRequirementFilter theFilter)
    {
        viewer = theViewer;
        filter = theFilter;
    }

}
