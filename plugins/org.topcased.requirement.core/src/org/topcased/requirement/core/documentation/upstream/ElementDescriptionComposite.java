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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.topcased.modeler.documentation.AbstractCommentsComposite;
import ttm.Attribute;
import ttm.Requirement;
import ttm.Text;

/**
 * A class defining a composite to visualize in read only both :
 * <ul>
 * <li>the <b>shortDescription</b> feature of a {@link Requirement}.</li> <br>
 * <li>A {@link Text} contained in an Upstream Requirement</li>
 * </ul>
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.2.0
 */
public class ElementDescriptionComposite extends AbstractCommentsComposite
{
    /**
     * Constructor.
     * 
     * @param parent the parent composite
     * @param style the composite style
     * @param commandStack the command stack to use to execute commands
     */
    public ElementDescriptionComposite(Composite parent, int style, CommandStack commandStack)
    {
        super(parent, style, commandStack);
    }

    /**
     * @see org.topcased.modeler.documentation.EAnnotationCommentsComposite#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContents(Composite parent)
    {
        super.createContents(parent);

        initialize();

        // add a new selection listener for keeping disabled the Edit button.
        getUseRichTextEditorButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                initialize();
            }
        });
    }

    /**
     * Initializes some default behaviors
     */
    private void initialize()
    {
        getEditButton().setEnabled(false);
        // in all the cases, the text can not be edited !
        if (getPlainTextComposite() != null && !getPlainTextComposite().isDisposed())
        {
            getPlainTextComposite().getText().setEditable(false);
        }
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
            for (Text text : ((Requirement) getDocumentedElement()).getTexts())
            {
                docValue = docValue.concat(text.getValue()).concat("\n"); //$NON-NLS-1$
            }
        }
        else if (getDocumentedElement() instanceof Text)
        {
            docValue = ((Text) getDocumentedElement()).getValue();
        }
        else
        {
            docValue = ((Attribute) getDocumentedElement()).getValue();
        }
        return docValue == null ? "" : docValue; //$NON-NLS-1$
    }

    /**
     * @see org.topcased.modeler.documentation.AbstractCommentsComposite#handleDocChanged()
     */
    @Override
    public void handleDocChanged()
    {
        // No implementation to provide

    }
}
