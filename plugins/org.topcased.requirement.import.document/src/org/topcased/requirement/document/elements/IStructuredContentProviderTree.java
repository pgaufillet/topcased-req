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
package org.topcased.requirement.document.elements;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class IStructuredContentProviderTree.
 */
public class IStructuredContentProviderTree implements ITreeContentProvider
{

    /** The display selected. */
    private final boolean displaySelected;

    /**
     * Instantiates a new i structured content provider tree.
     */
    public IStructuredContentProviderTree()
    {
        this(true);
    }

    /**
     * Instantiates a new i structured content provider tree.
     * 
     * @param displaySelected the display selected
     */
    public IStructuredContentProviderTree(boolean displaySelected)
    {
        this.displaySelected = displaySelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement)
    {
        Collection toDisplay = new LinkedList();
        if (inputElement instanceof RecognizedTree)
        {
            RecognizedTree tree = (RecognizedTree) inputElement;
            for (Iterator<RecognizedElement> i = tree.getChildren().iterator(); i.hasNext();)
            {
                RecognizedElement r = (RecognizedElement) i.next();
                if (displaySelected || !displaySelected && !r.isSelected())
                {
                    toDisplay.add(r);
                }
            }
            return toDisplay.toArray();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement)
    {
        if (parentElement instanceof RecognizedElement)
        {
            Collection toDisplay = new LinkedList();
            RecognizedElement element = (RecognizedElement) parentElement;
            if (element.getChildren() != null)
            {
                for (RecognizedElement r : element.getChildren())
                {
                    if (!r.isSelected())
                    {
                        toDisplay.add(r);
                    }
                }
                return toDisplay.toArray();
            }
        }
        else if (parentElement instanceof RecognizedTree)
        {
            return ((RecognizedTree) parentElement).getChildren().toArray();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element)
    {
        if (element instanceof RecognizedElement)
        {
            RecognizedElement rElement = (RecognizedElement) element;
            return rElement.getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element)
    {
        if (getChildren(element) != null)
        {
            return getChildren(element).length > 0;
        }
        else
        {
            return false;
        }
    }

}
