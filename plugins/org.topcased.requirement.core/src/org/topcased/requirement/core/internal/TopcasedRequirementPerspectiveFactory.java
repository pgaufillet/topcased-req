/***********************************************************************************************************************
 * Copyright (c) 2008,2009 Communication & Systems.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.internal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.topcased.modeler.documentation.DocView;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.requirement.core.views.upstream.UpstreamRequirementView;

/**
 * Creates the specific perspective for the topcased requirement module.<br>
 * 
 * Creation : 25 may 2009.<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.6.0
 */
public class TopcasedRequirementPerspectiveFactory implements IPerspectiveFactory
{

    private final static String navigatorId = "org.topcased.ui.navigator.view"; //$NON-NLS-1$
    
    /**
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout)
    {
        defineActions(layout);
        defineLayout(layout);
    }

    /**
     * Defines the initial actions for a page.
     * 
     * @param layout the initial page layout
     */
    public void defineActions(IPageLayout layout)
    {
        layout.addShowViewShortcut(DocView.VIEW_ID);
        layout.addShowViewShortcut(UpstreamRequirementView.VIEW_ID);
        layout.addShowViewShortcut(CurrentRequirementView.VIEW_ID);
    }

    /**
     * Changes the page layout
     * 
     * @param layout the initial page layout
     */
    public void defineLayout(IPageLayout layout)
    {

        // Editors are placed for free.
        String editorArea = layout.getEditorArea();

        // Top Left
        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, (float) 0.25, editorArea); //$NON-NLS-1$
        topLeft.addView(UpstreamRequirementView.VIEW_ID);
        topLeft.addView(navigatorId); 
        layout.getViewLayout(navigatorId); 

        // Bottom Left
        IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, (float) 0.66, "topLeft"); //$NON-NLS-1$ //$NON-NLS-2$
        bottomLeft.addView(CurrentRequirementView.VIEW_ID);
        bottomLeft.addView(IPageLayout.ID_OUTLINE);
        layout.getViewLayout(CurrentRequirementView.VIEW_ID);

        // Bottom
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.66, editorArea); //$NON-NLS-1$
        bottom.addView(DocView.VIEW_ID);
        layout.getViewLayout(DocView.VIEW_ID);

        // Bottom Right
        IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.RIGHT, (float) 0.66, "bottom"); //$NON-NLS-1$ //$NON-NLS-2$
        bottomRight.addView(IPageLayout.ID_PROP_SHEET);
        bottomRight.addView(IPageLayout.ID_PROBLEM_VIEW);
        layout.getViewLayout(IPageLayout.ID_PROP_SHEET);
    }

}
