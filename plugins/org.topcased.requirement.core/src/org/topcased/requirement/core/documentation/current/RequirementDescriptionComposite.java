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
import org.eclipse.swt.widgets.Composite;
import org.topcased.modeler.documentation.AbstractCommentsComposite;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.TextAttribute;

/**
 * A class defining a composite to edit either :
 * <ul>
 * <li>the <b>shortDescription</b> feature of a {@link Requirement}</li>
 * <li>the <b>shortDescription</b> feature of a {@link HierarchicalElement}</li>
 * <li><b>value</b> feature of a {@link TextAttribute}.</li>
 * </ul><br>
 * 
 * Updated : 11 Aug. 2009 (Completely refactor that class into an abstract class {@link AbstractCommentsComposite} and a
 * default implementation one<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class RequirementDescriptionComposite extends AbstractCommentsComposite
{

    /**
     * Constructor.
     * 
     * @param parent the parent composite
     * @param style the composite style
     * @param commandStack the command stack to use to execute commands
     */
    public RequirementDescriptionComposite(Composite parent, int style, CommandStack commandStack)
    {
        super(parent, style, commandStack);
    }

    /**
     * @see org.topcased.modeler.documentation.EAnnotationCommentsComposite#getDocumentationValueFromElement()
     */
    @Override
    protected String getDocumentationValueFromElement()
    {
        String docValue = ""; //$NON-NLS-1$
        if (getDocumentedElement() instanceof Requirement)
        {
            docValue = ((Requirement) getDocumentedElement()).getShortDescription();
        }
        else if (getDocumentedElement() instanceof HierarchicalElement)
        {
            docValue = ((HierarchicalElement) getDocumentedElement()).getShortDescription();
        }
        else if (getDocumentedElement() instanceof TextAttribute)
        {
            docValue = ((TextAttribute) getDocumentedElement()).getValue();
        }
        return docValue == null ? "" : docValue; //$NON-NLS-1$
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractCommentsComposite#handleDocChanged()
     */
    @Override
    public void handleDocChanged()
    {
        if (getDocumentedElement() != null && getDocumentedElement() instanceof EObject)
        {
            getCommandStack().execute(
                    new ChangeRequirementElementCommand(getDocumentedElement(), getUseRichTextEditorButton().getSelection() ? getRichTextComposite().getDocumentationValue()
                            : getPlainTextComposite().getDocumentationValue()));
        }
    }

}