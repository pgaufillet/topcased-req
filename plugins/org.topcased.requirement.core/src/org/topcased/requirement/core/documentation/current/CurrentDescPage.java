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
package org.topcased.requirement.core.documentation.current;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.topcased.modeler.documentation.AbstractCommentsComposite;
import org.topcased.modeler.documentation.AbstractDocPage;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.TextAttribute;

/**
 * This class creates the page to display and edit :
 * <ul>
 * <li>the <b>shortDescription</b> feature of a {@link Requirement}</li>
 * <li>the <b>shortDescription</b> feature of a {@link HierarchicalElement}</li>
 * <li><b>value</b> feature of a {@link TextAttribute}.</li>
 * </ul>
 * <br>
 * 
 * Updated : 12 August 2009 <br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public class CurrentDescPage extends AbstractDocPage
{

    /**
     * Builds the page with the editor command stack : used to execute commands.
     * 
     * @param stack the editor command stack
     * @deprecated use {@link #CurrentDescPage()} instead
     */
    public CurrentDescPage(CommandStack stack)
    {
        this();
    }

    /**
     * Builds the page.
     */
    public CurrentDescPage()
    {
        super();
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractDocPage#createCommentsComposite(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected AbstractCommentsComposite createCommentsComposite(Composite parent)
    {
        return new RequirementDescriptionComposite(parent, SWT.NONE, this);
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractDocPage#getSelectedModelElement(org.eclipse.jface.viewers.ISelection)
     */
    protected EObject getSelectedModelElement(ISelection selection)
    {
        if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1)
        {
            Object selectedObject = ((IStructuredSelection) selection).getFirstElement();

            if (selectedObject instanceof Requirement)
            {
                return (Requirement) selectedObject;
            }
            else if (selectedObject instanceof HierarchicalElement)
            {
                return (HierarchicalElement) selectedObject;
            }
            else if (selectedObject instanceof TextAttribute)
            {
                return (TextAttribute) selectedObject;
            }
        }
        return null;
    }
}
