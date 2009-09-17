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
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.topcased.modeler.editor.Modeler;
import org.topcased.requirement.generic.actions.CurrentRequirementFilter;
import org.topcased.sam.requirement.CurrentRequirement;
import org.topcased.sam.requirement.HierarchicalElement;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.core.views.current.CurrentPage;
import org.topcased.sam.requirement.core.views.current.ICurrentRequirementPage;
import org.topcased.sam.requirement.core.views.upstream.IUpstreamRequirementPage;

/**
 * A factory for creating Adapter objects.
 */
public class AdapterFactory implements IAdapterFactory
{
    private CustomUpstreamPage upstreamPage;

    private CustomCurrentPage currentPage;

    public Object getAdapter(Object adaptableObject, Class adapterType)
    {

        if (adapterType == IUpstreamRequirementPage.class && adaptableObject instanceof Modeler)
        {
            upstreamPage = new CustomUpstreamPage((Modeler) adaptableObject);
            Injector.getInstance().syncFollowLinkTo();
            return upstreamPage;
        }
        else if (adapterType == ICurrentRequirementPage.class && adaptableObject instanceof Modeler)
        {
            currentPage = new CustomCurrentPage((Modeler) adaptableObject);
            return currentPage;
        }
        return null;
    }


    public Class[] getAdapterList()
    {
        return null;
    }

//    private void manageEvent(SelectionChangedEvent event)
//    {
//        if (event instanceof SelectionChangedEvent)
//        {
//            SelectionChangedEvent change = (SelectionChangedEvent) event;
//            if (change.getSelection() instanceof IStructuredSelection)
//            {
//                IStructuredSelection selection = (IStructuredSelection) change.getSelection();
//                if (selection.getFirstElement() instanceof EditPart)
//                {
//                    EditPart part = (EditPart) selection.getFirstElement();
//                    EObject selected = null;
//                    if (part instanceof EMFGraphEdgeEditPart)
//                    {
//                        EMFGraphEdgeEditPart edgePart = (EMFGraphEdgeEditPart) part;
//                        selected = edgePart.getEObject();
//                    }
//                    else if (part instanceof EMFGraphNodeEditPart)
//                    {
//                        EMFGraphNodeEditPart nodepart = (EMFGraphNodeEditPart) part;
//                        selected = nodepart.getEObject();
//                    }
//                    if (selected != null)
//                    {
//                        if (((CurrentRequirementView) CurrentRequirementView.getInstance()).getCurrentPage() instanceof CurrentPage)
//                        {
//                            CurrentPage page = (CurrentPage) ((CurrentRequirementView) CurrentRequirementView.getInstance()).getCurrentPage();
//                            Object input = page.getViewer().getInput();
//                            if (input instanceof RequirementProject)
//                            {
//                                RequirementProject project = (RequirementProject) input;
//                                manageViewer(selected, project, page);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private void manageViewer(EObject selected, RequirementProject project, CurrentPage page)
    {
        Collection<TreePath> objects = new LinkedList<TreePath>();
        for (TreeIterator<EObject> i = project.eAllContents(); i.hasNext();)
        {
            EObject e = i.next();
            if (e instanceof CurrentRequirement)
            {
                CurrentRequirement current = (CurrentRequirement) e;
                if (current.eContainer() instanceof HierarchicalElement)
                {
                    HierarchicalElement hier = (HierarchicalElement) current.eContainer();
                    if (hier.getSamElement() == selected)
                    {
                        objects.add(getTreePath(current));
                    }
                }
            }
        }
        if (!objects.isEmpty())
        {
            ISelection selection = new TreeSelection(objects.toArray(new TreePath[0]));
            CurrentRequirementFilter.getInstance().setSearched("");
            ((TreeViewer) page.getViewer()).setSelection(selection, true);
        }
    }

    private TreePath getTreePath(CurrentRequirement current)
    {
        LinkedList<EObject> list = new LinkedList<EObject>();
        EObject tmp = current;
        list.addLast(tmp);
        while (tmp.eContainer() != null)
        {
            tmp = tmp.eContainer();
            list.addFirst(tmp);
        }
        TreePath path = new TreePath(list.toArray());
        return path;
    }

}
