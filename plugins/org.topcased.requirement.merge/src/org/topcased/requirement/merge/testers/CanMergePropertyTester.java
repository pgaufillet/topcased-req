/*****************************************************************************
 * Copyright (c) 2010 Communication & Systems
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Maxime AUDRAIN (CS) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.merge.testers;

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * A simple property tester to check if the current IFile selected is a ".di" file, And also check that no
 * currentModeler is opened (prevent problems when modifying requirement file while opened)
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CanMergePropertyTester extends PropertyTester
{

    /**
     * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
     *      java.lang.Object)
     */
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
    {
    	IFile iFile = getIFile(receiver);
		return iFile != null && iFile.getFileExtension() != null && iFile.getFileExtension().endsWith("di") ;
    }
    
    public static IFile getIFile (Object receiver)
    {
    	IFile file = null ;
    	if (receiver instanceof IFile)
		{
			file = (IFile) receiver;
		}
		if (file == null && receiver instanceof IAdaptable)
		{
			IAdaptable adaptable = (IAdaptable) receiver ;
			file = (IFile) adaptable.getAdapter(IFile.class);
		}
		if (file == null)
		{
			file = (IFile) Platform.getAdapterManager().getAdapter(receiver, IFile.class);
		}
		if (file == null)
		{
			Collection<?> collec = (Collection<?>) Platform.getAdapterManager().getAdapter(receiver, Collection.class);
    		if (collec != null)
    		{
    			for (Object o : collec)
    			{
    				if (o instanceof IFile) {
						IFile tmp = (IFile) o;
						if (tmp.getFileExtension() != null && tmp.getFileExtension().endsWith("di"))
						{
							file = tmp ;
							break ;
						}
					}
    			}
    		}
		}
		return file ;
    }

}
