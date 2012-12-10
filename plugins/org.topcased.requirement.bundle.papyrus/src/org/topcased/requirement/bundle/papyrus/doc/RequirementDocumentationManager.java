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
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.papyrus.documentation.DocumentationUnsupportedException;
import org.eclipse.papyrus.documentation.IDocumentationChangedListener;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementFactory;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.bundle.papyrus.internal.Messages;


public class RequirementDocumentationManager implements org.eclipse.papyrus.documentation.IDocumentationManager {

	protected Set<IDocumentationChangedListener> docChangedListenerList = new HashSet<IDocumentationChangedListener>();
	
	public RequirementDocumentationManager() {
	}

	public String getDocumentation(EObject documentedElement) throws DocumentationUnsupportedException
	{
		String docValue = ""; //$NON-NLS-1$
        if (documentedElement instanceof Requirement)
        {
            docValue = ((Requirement) documentedElement).getShortDescription();
        }
        else if (documentedElement instanceof HierarchicalElement)
        {
            docValue = ((HierarchicalElement) documentedElement).getShortDescription();
        }
        else if (documentedElement instanceof TextAttribute)
        {
            docValue = ((TextAttribute) documentedElement).getValue();
        }
        return docValue == null ? "" : docValue; //$NON-NLS-1$
	}
	
	public List<URI> getAssociatedResources(EObject documentedElement) throws DocumentationUnsupportedException
	{
		List<URI> uriList = new ArrayList<URI>();
		if (documentedElement instanceof Requirement)
        {
            Requirement req = (Requirement)documentedElement;
            for(Attribute att:req.getAttribute())
            {
            	if (att instanceof TextAttribute) {
					TextAttribute textAttribute = (TextAttribute) att;
					if(Messages.getString("RequirementDocumentationManager_Resource").equals(textAttribute.getName())) //$NON-NLS-1$
					{
						try
						{
							uriList.add(URI.createURI(textAttribute.getValue()));
						}
						catch(IllegalArgumentException e)
						{
						}
					}
				}
            }
        }
		return uriList;
	}
	
	public Command getAddAssociatedResourceCommand(EObject documentedElement, URI resourceURI) {
		if (documentedElement instanceof Requirement || documentedElement instanceof HierarchicalElement)
        {
			TextAttribute att = RequirementFactory.eINSTANCE.createTextAttribute();
			att.setName(Messages.getString("RequirementDocumentationManager_Resource")); //$NON-NLS-1$
			att.setValue(resourceURI.toString());
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(documentedElement);
			return new AddCommand(domain, documentedElement, RequirementPackage.Literals.REQUIREMENT__ATTRIBUTE,att);
        }
		return null;
	}

	public Command getChangeDocumentationCommand(EObject documentedElement, String doc)
	{
		if (documentedElement instanceof Requirement || documentedElement instanceof HierarchicalElement)
        {
            EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(documentedElement);
            return new SetCommand(domain, documentedElement, RequirementPackage.Literals.IDENTIFIED_ELEMENT__SHORT_DESCRIPTION,doc);
        }
        else if (documentedElement instanceof TextAttribute)
        {
        	 EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(documentedElement);
             return new SetCommand(domain, documentedElement, RequirementPackage.Literals.TEXT_ATTRIBUTE__VALUE,doc);
        }
		return null;
	}

	

	public Command getRemoveAssociatedResourceCommand(EObject documentedElement, URI resourceURI)
	{
		if (documentedElement instanceof Requirement)
        {
            Requirement req = (Requirement)documentedElement;
            for(Attribute att:req.getAttribute())
            {
            	if (att instanceof TextAttribute) {
					TextAttribute textAttribute = (TextAttribute) att;
					if(Messages.getString("RequirementDocumentationManager_Resource").equals(textAttribute.getName())) //$NON-NLS-1$
					{
						if(resourceURI.toString().equals(textAttribute.getValue()))
						{
							EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(documentedElement);
							return new RemoveCommand(domain, documentedElement, RequirementPackage.Literals.REQUIREMENT__ATTRIBUTE,textAttribute);
						}
					}
				}
            }
        }
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
