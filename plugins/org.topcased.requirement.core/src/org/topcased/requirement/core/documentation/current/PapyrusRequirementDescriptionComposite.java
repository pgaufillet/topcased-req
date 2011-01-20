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

import org.eclipse.emf.common.command.Command;
import org.eclipse.papyrus.documentation.view.CommentsComposite;
import org.eclipse.papyrus.documentation.view.DocViewPlugin;
import org.eclipse.papyrus.documentation.view.DocumentionPartHandlerRegistry;
import org.eclipse.papyrus.documentation.view.IDocumentationPartHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.TextAttribute;

/**
 * A class defining a composite to edit either :
 * <ul>
 * <li>the <b>shortDescription</b> feature of a {@link Requirement}</li>
 * <li>the <b>shortDescription</b> feature of a {@link HierarchicalElement}</li>
 * <li><b>value</b> feature of a {@link TextAttribute}.</li>
 * </ul>
 * <br>
 * 
 * Updated : 11 Aug. 2009 (Completely refactor that class into an abstract class {@link AbstractCommentsComposite} and a
 * default implementation one<br>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class PapyrusRequirementDescriptionComposite extends CommentsComposite
{

    /**
     * Constructor.
     * 
     * @param parent the parent composite
     * @param style the composite style
     */
    public PapyrusRequirementDescriptionComposite(Composite parent, int style)
    {
        super(parent, style);
    }

    /**
     * @see org.eclipse.papyrus.documentation.view.CommentsComposite#getDocumentationValueFromElement()
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
     * @see org.eclipse.papyrus.documentation.view.CommentsComposite#handleDocChanged()
     */
    @Override
    public void handleDocChanged()
    {
        IEditorPart activeEditor = DocViewPlugin.getActiveEditor();
        IDocumentationPartHandler documentationEditor = DocumentionPartHandlerRegistry.getInstance().getDocumentationPartHandler(activeEditor);
        if (documentationEditor != null)
        {
            Command cmd = new ChangeRequirementElementCommand(getDocumentedElement(), getUseRichTextEditorButton().getSelection() ? getRichTextComposite().getDocumentationValue()
                    : getPlainTextComposite().getDocumentationValue());
            documentationEditor.executeCommand(activeEditor, cmd);
        }
    }

}