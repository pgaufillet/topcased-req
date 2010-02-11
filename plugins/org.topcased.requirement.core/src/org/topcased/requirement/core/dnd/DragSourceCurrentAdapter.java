/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.dnd;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Tree;
import org.topcased.requirement.Requirement;

/**
 * 
 * The current requirement view drag adapter
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class DragSourceCurrentAdapter extends DragSourceAdapter
{
    private TreeViewer treeViewer;

    public DragSourceCurrentAdapter(EditingDomain domain, TreeViewer viewer)
    {
        super();
        this.treeViewer = viewer;
    }

    /**
     * @see org.eclipse.swt.dnd.DragSourceAdapter#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
     */
    public void dragStart(DragSourceEvent event)
    {
        super.dragStart(event);
        event.doit = true;
        Tree source = (Tree) ((DragSource) event.getSource()).getControl();

        if (source.getSelection().length > 0)
        {
            event.doit = true;
            if (!(source.getSelection()[0].getData() instanceof ttm.Attribute || source.getSelection()[0].getData() instanceof Requirement))
            {
                event.doit = false;
            }
        }
        else
        {
            event.doit = false;
        }

        ISelection data = getSelection(event);
        if (data.isEmpty())
        {
            // cancel drag
            event.doit = false;
        }
        RequirementTransfer.getInstance().setSelection(data);
    }

    /**
     * Get transfered data
     */
    public void dragSetData(DragSourceEvent event)
    {
        event.data = getSelection(event);
    }

    /**
     * @see org.eclipse.swt.dnd.DragSourceAdapter#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
     */
    public void dragFinished(DragSourceEvent event)
    {
        RequirementTransfer.getInstance().setSelection(null);
    }

    /**
     * 
     * The getter of the current selection 
     * 
     * @param event
     * 
     * @return ISelection
     */
    protected ISelection getSelection(DragSourceEvent event)
    {
        return treeViewer.getSelection();
    }

}
