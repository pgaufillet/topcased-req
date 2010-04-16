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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import ttm.HierarchicalElement;

/**
 * The upstream view drag adapter.
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class DragSourceUpstreamAdapter extends DragSourceAdapter
{
    private ISelectionProvider selectionProvider;

    /**
     * Constructs a new drag adapter.
     * 
     * @param provider the object that provide the selected object
     */
    public DragSourceUpstreamAdapter(ISelectionProvider provider)
    {
        selectionProvider = provider;
    }

    /**
     * Erases data from the clipboard object
     * 
     * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
     */
    public void dragFinished(DragSourceEvent event)
    {
        RequirementTransfer.getInstance().setSelection(null);
    }

    /**
     * @see org.eclipse.swt.dnd.DragSourceAdapter#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
     */
    public void dragSetData(DragSourceEvent event)
    {
        event.data = selectionProvider.getSelection();
    }

    /**
     * Puts selection on the clipboard object
     * 
     * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
     */
    public void dragStart(DragSourceEvent event)
    {
        super.dragStart(event);

        event.doit = true;
        Tree source = (Tree) ((DragSource) event.getSource()).getControl();

        if (source.getSelection().length > 0)
        {
            event.doit = true;
            for (TreeItem item : source.getSelection())
            {
                if (!(item.getData() instanceof HierarchicalElement))
                {
                    event.doit = false;
                }
            }
        }
        else
        {
            event.doit = false;
        }

        ISelection data = selectionProvider.getSelection();
        if (data.isEmpty())
        {
            // cancel drag
            event.doit = false;
        }
        RequirementTransfer.getInstance().setSelection(data);
    }
}
