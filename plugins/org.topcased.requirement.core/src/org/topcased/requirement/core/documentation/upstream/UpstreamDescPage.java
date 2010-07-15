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
package org.topcased.requirement.core.documentation.upstream;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.topcased.modeler.documentation.AbstractCommentsComposite;
import org.topcased.modeler.documentation.AbstractDocPage;
import org.topcased.requirement.AttributeLink;

import ttm.Attribute;
import ttm.Element;
import ttm.Requirement;
import ttm.Text;

/**
 * This class creates the page to visualize description of a {@link Element}.<br>
 * Two kinds of objects should be visualized : <br>
 * <ul>
 * <li>The short description of an Upstream {@link Requirement}</li>
 * <li>A {@link Text} that is contained in a {@link Requirement}</li>
 * </ul>
 * 
 * Updated : 12 August 2009 <br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public class UpstreamDescPage extends AbstractDocPage
{
    /**
     * Builds the page with the editor command stack : used to execute commands.
     * 
     * @param stack the editor command stack
     * @deprecated use {@link #UpstreamDescPage()} instead
     */
    public UpstreamDescPage(CommandStack stack)
    {
        this();
    }

    /**
     * Builds the page with the editor command stack : used to execute commands.
     */
    public UpstreamDescPage()
    {
        super();
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractDocPage#createCommentsComposite(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected AbstractCommentsComposite createCommentsComposite(Composite parent)
    {
        return new ElementDescriptionComposite(parent, SWT.NONE, this);
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractDocPage#getSelectedModelElement(org.eclipse.jface.viewers.ISelection)
     */
    @Override
    protected EObject getSelectedModelElement(ISelection selection)
    {
        if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1)
        {
            Object selectedObject = ((IStructuredSelection) selection).getFirstElement();

            if (selectedObject instanceof Requirement)
            {
                return (Requirement) selectedObject;
            }
            else if (selectedObject instanceof Text)
            {
                return (Text) selectedObject;
            }
            else if (selectedObject instanceof Attribute)
            {
                return (Attribute) selectedObject;
            }
            else if (selectedObject instanceof AttributeLink)
            {
                return getSelectedModelElement(new StructuredSelection(((AttributeLink) selectedObject).getValue()));
            }
        }
        return null;
    }
}
