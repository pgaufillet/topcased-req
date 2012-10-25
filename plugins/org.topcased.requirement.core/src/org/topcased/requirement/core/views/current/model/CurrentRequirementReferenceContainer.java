/***********************************************************************************************************************
 * Copyright (c) 2011 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.views.current.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.views.current.model.internal.ModelSetReferencesChangeListener;
import org.topcased.requirement.util.RequirementResource;

/**
 * {@link CurrentRequirementReferenceContainer} is a virtual object which is used as container of
 * {@link CurrentRequirementReference}
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 * @since 5.1
 */
public class CurrentRequirementReferenceContainer implements IItemLabelProvider, ITreeContentProvider, IAdaptable
{
    /**
     * All linked Requirement references which is not the Upstream model or the current requirement
     */
    private Map<URI, CurrentRequirementReference> references = new HashMap<URI, CurrentRequirementReference>();

    /**
     * {@link RequirementProject} which is currently displayed
     */
    private RequirementProject reqProject;

    /**
     * {@link ResourceSet} of the {@link RequirementProject} WARNING : Always use
     * {@link CurrentRequirementReferenceContainer#getResourceSet()} because it is lazily affected
     */
    private ResourceSet resourceset;

    /**
     * {@link INotifyChangedListener}
     */
    private INotifyChangedListener notifier;

    /**
     * Constructor.
     * 
     * @param reqProject {@link RequirementProject} which has the following references
     * @param currentRequirementContentProvider
     */
    public CurrentRequirementReferenceContainer(RequirementProject reqProject, INotifyChangedListener currentRequirementContentProvider)
    {
        super();
        this.reqProject = reqProject;
        notifier = currentRequirementContentProvider;
        initCurrentRequirementLinked();

    }

    /**
     * Get the resourceSet
     * 
     * @return {@link ResourceSet} of the current requirement
     */
    protected ResourceSet getResourceSet()
    {
        if (resourceset == null)
        {
            Resource re = reqProject.eResource();
            if (re != null)
            {
                resourceset = re.getResourceSet();
            }
        }
        return resourceset;
    }

    /**
     * Initialize the linked requirement references ALGO: For all ObjectAttribute If the URI do not refer to the
     * Upstream or to the current Requirement then Add to references End if End for All
     */
    private void initCurrentRequirementLinked()
    {
        getResourceSet().eAdapters().add(new ModelSetReferencesChangeListener(this));
        for (TreeIterator<EObject> iterator = reqProject.eAllContents(); iterator.hasNext();)
        {
            EObject next = iterator.next();
            if (next instanceof ObjectAttribute)
            {
                ObjectAttribute link = (ObjectAttribute) next;
                EObject value = link.getValue();
                if (value != null)
                {
                    /*
                     * If the resource if loaded implies it's not the upstream model
                     */
                    URI uri = null;
                    if (value.eIsProxy())
                    {
                        uri = EcoreUtil.getURI(value);
                    }
                    Resource eRessource = value.eResource();
                    if (eRessource != null)
                    {
                        /* test if the resource is upstreamRequirement (if it is do not add) */
                        if (eRessource.isLoaded())
                        {
                            EObject root = eRessource.getContents().get(0);
                            /* test the project is not the current displayed requirement */
                            if (root instanceof RequirementProject)
                            {
                                if (!root.equals(reqProject))
                                {
                                    uri = eRessource.getURI();
                                }
                            }
                        }
                        else
                        {
                            uri = eRessource.getURI();
                        }
                    }
                    if (uri != null && RequirementResource.FILE_EXTENSION.equals(uri.fileExtension()))
                    {
                        URI trimedURI = uri.trimFragment();
                        CurrentRequirementReference currentRequirementReference = references.get(trimedURI);

                        if (currentRequirementReference == null)
                        {
                            currentRequirementReference = new CurrentRequirementReference(this, uri.trimFragment(), getResourceSet());
                            references.put(trimedURI, currentRequirementReference);
                        }
                        /** Add the next object as a referencer of the {@link CurrentRequirementReference} object */
                        currentRequirementReference.addReferencedBy(next);
                    }
                }
            }
        }
    }

    /**
     * {@link IItemLabelProvider#getText(Object)}
     */
    public String getText(Object object)
    {
        if (object instanceof CurrentRequirementReferenceContainer)
        {
            return Messages.CurrentRequirementReferenceContainer_references;
        }
        return Messages.CurrentRequirementReferenceContainer_1;
    }

    /**
     * {@link IItemLabelProvider#getImage(Object)}
     */
    public Object getImage(Object object)
    {
        if (object instanceof CurrentRequirementReferenceContainer)
        {
            return RequirementCorePlugin.getDefault().getImageRegistry().get(RequirementCorePlugin.ICONS_CURRENT_REQUIREMENT_REFERENCES_GIF);
        }

        return null;
    }

    /**
     * {@link ITreeItemContentProvider#getChildren(Object)}
     */
    public Object[] getChildren(Object object)
    {
        return references.values().toArray();
    }

    /**
     * {@link ITreeItemContentProvider#dispose()} !Normally never used!
     */
    public void dispose()
    {
        references = null;
        reqProject = null;
        resourceset = null;
    }

    /**
     * {@link ITreeItemContentProvider#getElements(Object)} !Normally never used!
     */
    public Object[] getElements(Object inputElement)
    {
        return Collections.singletonList(this).toArray();
    }

    /**
     * {@link ITreeItemContentProvider#getParent(Object)} !Normally never used!
     */
    public Object getParent(Object element)
    {
        return reqProject;
    }

    /**
     * {@link ITreeItemContentProvider#getChildren(Object)} !Normally never used!
     */
    public boolean hasChildren(Object element)
    {
        if (element instanceof CurrentRequirementReferenceContainer)
        {
            return !references.isEmpty();
        }
        else if (element instanceof CurrentRequirementReference)
        {
            return false;
        }
        return false;
    }

    /**
     * Test if the object can be added has reference to the reference list.
     * 
     * @param ob {@link ObjectAttribute}. but for now only handle RequirementProject or {@link Collection} of
     *        RequirementProject
     * @return true if something has been added
     */
    public boolean addReference(Object ob)
    {
        boolean result = false;
        if (ob instanceof RequirementProject)
        {
            Resource res = ((RequirementProject) ob).eResource();
            URI trimFragment = res.getURI().trimFragment();
            if (references.get(trimFragment) == null)
            {
                references.put(trimFragment, new CurrentRequirementReference(this, trimFragment, getResourceSet()));
            }
            result = true;

        }
        else if (ob instanceof Collection< ? >)
        {
            @SuppressWarnings("unchecked")
            Collection<Object> ref = (Collection<Object>) ob;
            if (!ref.isEmpty())
            {
                Iterator<Object> ite = ref.iterator();
                while (ite.hasNext())
                {
                    Object o = ite.next();
                    if (o instanceof RequirementProject)
                    {
                        Resource res = ((RequirementProject) o).eResource();
                        references.put(res.getURI().trimFragment(), new CurrentRequirementReference(this, res.getURI().trimFragment(), getResourceSet()));
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Return the element to notify when chages
     * 
     * @return {@link INotifyChangedListener}
     */
    public INotifyChangedListener getNotifier()
    {
        return notifier;
    }

    /**
     * No use.
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        // Nothing to do
    }

    /**
     * Allow this object to be adapt in: CurrentRequirementReferenceContainer EObject this will return the reqProject
     * which contain this RessourceSet this return the resourceSet which contain the RequirementProject
     */
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
    {
        if (adapter == CurrentRequirementReferenceContainer.class)
        {
            return this;
        }
        else if (adapter == EObject.class)
        {
            return reqProject;
        }
        else if (adapter == ResourceSet.class)
        {
            return resourceset;
        }
        else if (adapter == INotifyChangedListener.class)
        {
            return notifier;
        }
        return null;
    }

    /**
     * Get all the references contained by this container
     * 
     * @return
     */
    public Map<URI, CurrentRequirementReference> getReferences()
    {
        return references;
    }

}
