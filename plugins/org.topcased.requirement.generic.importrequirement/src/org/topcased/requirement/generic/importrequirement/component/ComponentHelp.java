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
package org.topcased.requirement.generic.importrequirement.component;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.topcased.requirement.generic.importrequirement.Activator;
import org.topcased.requirement.generic.importrequirement.ui.NotifyElement;

/**
 * The Class ComponentHelp.
 */
public abstract class ComponentHelp extends Composite
{

    /** The page parent. */
    protected NotifyElement pageParent;

    /** The image. */
    public static Image image = null;

    /** The help description. */
    private String helpDescription;

    // Load only one time the help icon
    static
    {
        try
        {
            image = new Image(Display.getDefault(), Activator.getDefault().getBundle().getResource("icons/help_contents-1.gif").openStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Constructor
    /**
     * Instantiates a new component help.
     * 
     * @param parentElement the parent element
     * @param parent the parent
     * @param toolkit the toolkit
     * @param style the style
     * @param helpText the help text
     * @param numColumns the num columns
     */
    public ComponentHelp(NotifyElement parentElement, Composite parent, FormToolkit toolkit, int style, String helpText, int numColumns)
    {
        super(parent, style);
        GridLayout layout = new GridLayout(numColumns + 1, false);
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        this.setLayout(layout);
        createContent(toolkit);
        createHelp(toolkit, helpText);
        pageParent = parentElement;
        this.helpDescription = helpText;
    }

    /**
     * Creates the content.
     * 
     * @param toolkit the toolkit
     * @param numColumns the num columns
     * @param buttonLabels the button labels
     */
    void createContent(FormToolkit toolkit, int numColumns, String[] buttonLabels)
    {
        createContent(toolkit);
    }

    // Insert a help icon at the end line
    /**
     * Creates the help.
     * 
     * @param toolkit the toolkit
     * @param toolTip the tool tip
     */
    private void createHelp(FormToolkit toolkit, final String toolTip)
    {
        ImageHyperlink helpImage = toolkit.createImageHyperlink(this, SWT.NONE);
        helpImage.setImage(image);
        helpImage.setToolTipText("Help");
        helpImage.addHyperlinkListener(new IHyperlinkListener()
        {
            public void linkActivated(HyperlinkEvent e)
            {
                HelpDialog dialog = new HelpDialog(getShell(), Display.getDefault().getCursorLocation(), "Help", helpDescription);
                dialog.open();
            }

            public void linkEntered(HyperlinkEvent e)
            {
            }

            public void linkExited(HyperlinkEvent e)
            {
            }

        });
    }

    /**
     * Creates the content.
     * 
     * @param toolkit the toolkit
     */
    abstract void createContent(FormToolkit toolkit);

}
