/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>"
 * 
 **********************************************************************************************************************/

package org.topcased.requirement.presentation;

import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.topcased.requirement.core.filters.RequirementFilter;
import org.topcased.requirement.core.views.SearchComposite;

/**
 * Composite allowing the display and control of requirements.
 * 
 * @author Olivier Mélois <a href="mailto:olivier.melois@atos.net">olivier.melois@atos.net</a>
 * 
 */
public class RequirementViewerComposite extends Composite
{

    protected RequirementFilter currentFilter;

    private ViewerPane viewerPane;

    private IRequestActivationHandler handler;

    public RequirementViewerComposite(Composite parent, int style, IWorkbenchPage page, IWorkbenchPart part)
    {
        super(parent, style);
        currentFilter = new RequirementFilter(true, true);
        final GridLayout mainLayout = new GridLayout();
        mainLayout.marginHeight = 0;
        mainLayout.marginWidth = 0;

        this.setLayout(mainLayout);

        viewerPane = new ViewerPane(page, part)
        {
            @Override
            public Viewer createViewer(Composite composite)
            {
                TreeViewer newTreeViewer = new TreeViewer(composite, SWT.MULTI);
                newTreeViewer.getTree().getParent().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

                newTreeViewer.setFilters(new ViewerFilter[] {currentFilter});

                return newTreeViewer;
            }

            @Override
            public void requestActivation()
            {
                super.requestActivation();
                if (handler != null)
                {
                    handler.requestActivation(this);
                }
            }
        };

        viewerPane.createControl(this);

        // Text filter
        final SearchComposite findIt = new SearchComposite(this, SWT.NONE)
        {
            @Override
            protected void doAfterSearch()
            {
                viewerPane.getViewer().refresh();
                ((TreeViewer) viewerPane.getViewer()).expandAll();
            }

            @Override
            protected void doAfterEmptySearch()
            {
                ((TreeViewer) viewerPane.getViewer()).collapseAll();
                viewerPane.getViewer().refresh();
            }

        };
        findIt.setFilter(currentFilter);

    }

    public void setViewerPane(ViewerPane viewerPane)
    {
        this.viewerPane = viewerPane;
    }

    public ViewerPane getViewerPane()
    {
        return viewerPane;
    }

    public void setRequestActivationHandler(IRequestActivationHandler handler)
    {
        this.handler = handler;
    }

}
