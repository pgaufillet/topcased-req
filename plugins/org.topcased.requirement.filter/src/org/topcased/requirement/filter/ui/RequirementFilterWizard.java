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
 *  Amine Bouchikhi (ATOS ORIGIN INTEGRATION) amine.bouchikhi@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.filter.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.topcased.requirement.filter.FilterProcess;

/**
 * The Class RequirementFilterWizard.
 */
public class RequirementFilterWizard extends Wizard
{

    /** The page. */
    RequirementPage page = new RequirementPage("Filter");

    /** The ipathes. */
    private IPath[] ipathes;

    /**
     * Instantiates a new requirement filter wizard.
     * 
     * @param pathes the pathes
     */
    public RequirementFilterWizard(IPath... pathes)
    {
        super();
        setWindowTitle("Requirements Filtering Wizard");
        setNeedsProgressMonitor(true);
        ipathes = pathes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#canFinish()
     */
    @Override
    public boolean canFinish()
    {
        // TODO Auto-generated method stub
        return page.canFlipToNextPage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages()
    {
        addPage(page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish()
    {
        try
        {
            getContainer().run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    List<String> attributes = page.getAttributes();
                    List<String> regexes = page.getRegexes();
                    String nameRegex = page.getNameRegex();
                    boolean andSelected = page.isANDSelected();
                    FilterProcess process = new FilterProcess(attributes, regexes, nameRegex, andSelected, ipathes);
                    process.execute(monitor);
                    page.savePreferences(attributes, regexes, nameRegex);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}