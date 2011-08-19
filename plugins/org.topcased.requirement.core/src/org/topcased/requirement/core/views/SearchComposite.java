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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
public abstract class SearchComposite extends Composite
{

    private IRequirementFilter filter;

    /**
     * Constructor
     * 
     * @param parent The parent composite
     * @param style The creation style
     */
    public SearchComposite(Composite parent, int style)
    {
        super(parent, style);
        final GridLayout layout = new GridLayout(4, false);
        // layout.marginHeight = 0;
        // layout.marginWidth = 0;

        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        final Label label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        label.setText(Messages.getString("SearchComposite.0")); //$NON-NLS-1$

        final Text textFilter = new Text(this, SWT.BORDER | SWT.FILL);
        textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        /*
         * Global accelerator are overridden for copy, paste and cut (CTRL+C or CTRL+INSERT, CTRL+V or SHIFT+INSERT, and
         * CTRL+X or SHIFT+DELETE). Hence, we need to call Text.copy(), Text.paste() and Text.cut() manually when the
         * text widget has focus.
         */
        textFilter.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent ke)
            {
                // enter => activate filter search
                if (ke.character == SWT.CR || ke.character == SWT.KEYPAD_CR)
                {
                    doFilter(textFilter);
                }
                // CTRL + ?
                else if ((ke.stateMask & SWT.CTRL) != 0)
                {
                    // copy (CTRL+C or CTRL+INSERT)
                    if (ke.keyCode == 'c' || ke.keyCode == SWT.INSERT)
                    {
                        textFilter.copy();
                    }
                    // paste (CTRL+V)
                    else if (ke.keyCode == 'v')
                    {
                        textFilter.paste();
                    }
                    // cut (CTRL+X)
                    else if (ke.keyCode == 'x')
                    {
                        textFilter.cut();
                    }
                }
                // SHIFT + ?
                else if ((ke.stateMask & SWT.SHIFT) != 0)
                {
                    // paste (SHIFT+INSERT)
                    if (ke.keyCode == SWT.INSERT)
                    {
                        textFilter.paste();
                    }
                    // cut (SHIFT+DELETE)
                    else if (ke.keyCode == SWT.DEL)
                    {
                        textFilter.cut();
                    }
                }
            }

        });


        final Button btn = new Button(this, SWT.PUSH);
        btn.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
        btn.setText(Messages.getString("SearchComposite.1")); //$NON-NLS-1$
        btn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                doFilter(textFilter);
            }
        });

        final Button caseSensitive = new Button(this, SWT.CHECK);
        caseSensitive.setText(Messages.getString("SearchComposite.2"));
        caseSensitive.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                filter.setCaseSensitive(caseSensitive.getSelection());
                doFilter(textFilter);
            }
        });
    }

    /**
     * Do filter
     * 
     * @param textFilter the text top filter
     */
    private void doFilter(final Text textFilter)
    {
        String text = textFilter.getText();
        filter.setSearched(text);

        if (text != null && text.length() != 0)
        {
            doAfterSearch();
        }
        else
        {
            doAfterEmptySearch();
        }
    }

    /**
     * Sets the filter applied
     * 
     * @param filter The {@link IRequirementFilterviewer} filter
     */
    public void setFilter(IRequirementFilter filter)
    {
        this.filter = filter;
    }

    protected abstract void doAfterSearch();

    protected abstract void doAfterEmptySearch();

}
