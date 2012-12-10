/*****************************************************************************
 * Copyright (c) 2012 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Matthieu Boivineau (Atos Origin}) {matthieu.boivineau@atos.net} - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.papyrus.doc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.documentation.DocumentationUnsupportedException;
import org.eclipse.papyrus.documentation.IDocumentationChangedListener;
import org.topcased.requirement.bundle.papyrus.internal.Messages;

import ttm.Attribute;
import ttm.HierarchicalElement;
import ttm.Requirement;
import ttm.Text;


public class TtmRequirementDocumentationManager implements org.eclipse.papyrus.documentation.IDocumentationManager {

	protected Set<IDocumentationChangedListener> docChangedListenerList = new HashSet<IDocumentationChangedListener>();
	
	
	public TtmRequirementDocumentationManager() {
	}

	public String getDocumentation(EObject documentedElement)
			throws DocumentationUnsupportedException {
		String docValue = ""; //$NON-NLS-1$
        if (documentedElement instanceof Requirement)
        {
            for (Text text : ((Requirement) documentedElement).getTexts())
            {
                if(text.getValue() != null)
                {
                	docValue = docValue.concat(text.getValue()).concat("\n");
                }
            }
        }
        else if (documentedElement instanceof Text)
        {
            docValue = ((Text) documentedElement).getValue();
        }
        else if (documentedElement instanceof HierarchicalElement)
        {
            docValue = ((HierarchicalElement) documentedElement).getShortDescription();
        }
        else if (documentedElement instanceof Attribute)
        {
            docValue = ((Attribute) documentedElement).getValue();
        }
        return docValue == null ? "" : docValue;
	}

	public Command getChangeDocumentationCommand(EObject documentedElement, String doc)
	{
		return null;
	}
	
	public List<URI> getAssociatedResources(EObject documentedElement) throws DocumentationUnsupportedException
	{
		List<URI> uriList = new ArrayList<URI>();
		if (documentedElement instanceof Requirement)
        {
            Requirement req = (Requirement)documentedElement;
            for(Attribute att:req.getAttributes())
            {
            	if(Messages.getString("RequirementDocumentationManager_Resource").equals(att.getName())) //$NON-NLS-1$
            	{
            		try
            		{
            			uriList.add(URI.createURI(att.getValue()));
            		}
            		catch(IllegalArgumentException e)
            		{
            		}
            	}
            }
        }
		return uriList;
	}
	
	public Command getAddAssociatedResourceCommand(EObject documentedElement, URI resourceURI) {
		return null;
	}

 	public Command getRemoveAssociatedResourceCommand(EObject documentedElement, URI resourceURI)
	{
		return null;
	}

	
	public Set<IDocumentationChangedListener> getRegisteredDocumentationChangedListeners()
	{
		return docChangedListenerList;
	}
	
	public void registerDocumentationChangedListener(IDocumentationChangedListener docChangedListener)
	{
		docChangedListenerList.add(docChangedListener);
	}

	public void unregisterDocumentationChangedListener(IDocumentationChangedListener docChangedListener)
	{
		docChangedListenerList.remove(docChangedListener);
	}

}
