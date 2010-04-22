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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.ui;

import java.io.File;
import java.util.SortedSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.topcased.requirement.document.utils.DocumentStyleBrowser;

/**
 * The Class NewStylePopup.
 */
public class NewStylePopup extends PopupRegexDialog
{

    /** The input. */
    protected Text input;

    /** The input value. */
    protected String inputValue = "";

    /** The document. */
    private final File document;

    /** The style input. */
    private String styleInput;

    /** The combo. */
    private Combo combo;

    /** The browser. */
    private final DocumentStyleBrowser browser;

    /**
     * Instantiates a new new style popup.
     * 
     * @param parentShell the parent shell
     * @param documentFile the document file
     * @param browser the browser
     */
    protected NewStylePopup(Shell parentShell, File documentFile, DocumentStyleBrowser browser)
    {
        super(parentShell);
        this.document = documentFile;
        this.browser = browser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.topcased.doc2model.requirement.ui.PopupRegexDialog#createContent(org.eclipse.ui.forms.widgets.FormToolkit,
     * org.eclipse.swt.widgets.Composite)
     */
    @Override
    void createContent(FormToolkit toolkit, Composite compo)
    {
        getShell().setText("New Style");
        toolkit.createLabel(compo, "Style: ");
        combo = new Combo(compo, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        SortedSet<String> styles = browser.getAllStyles(document);
        for (String style : styles)
        {
            combo.add(style);
        }
        combo.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent e)
            {
                styleInput = combo.getText();
            }
        });
    }

    /**
     * Gets the style input.
     * 
     * @return the style input
     */
    public String getStyleInput()
    {
        return styleInput;
    }

}
