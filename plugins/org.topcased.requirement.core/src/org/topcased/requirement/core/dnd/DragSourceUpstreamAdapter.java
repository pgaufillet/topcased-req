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
 *      Philippe Roland (ATOS) <philippe.roland@atos.net> - PluginTransfer support
 *    
 ******************************************************************************/
package org.topcased.requirement.core.dnd;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.PluginTransfer;
import org.topcased.requirement.core.RequirementCorePlugin;

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

    /** constant representing the name of the extension point */
    private static final String UPSTREAM_DROP_ADAPTER_POINT = RequirementCorePlugin.getId() + "." + "upstreamDropAdapter";

    /** Value of the extension point attribute containing the extension id for the drop delegate. */
    static final String DELEGATE_ID = "dropActionId";

    /**
     * Constructs a new drag adapter.
     * 
     * @param provider the object that provide the selected object
     */
    public DragSourceUpstreamAdapter(ISelectionProvider provider)
    {
        super();
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
        IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();

        // Due to the way UpstreamPage is coded, we will always have >= 1 extension if
        // PluginTransfer is enabled
        if (PluginTransfer.getInstance().isSupportedType(event.dataType))
        {
            String extensionId = null;
            IExtensionRegistry reg = Platform.getExtensionRegistry();
            IConfigurationElement[] extensions = reg.getConfigurationElementsFor(UPSTREAM_DROP_ADAPTER_POINT);
            for (int i = 0; i < extensions.length; i++)
            {
                IConfigurationElement element = extensions[i];
                extensionId = element.getAttribute(DELEGATE_ID);
                if (extensionId != null)
                {
                    event.data = UpstreamPluginTransferData.getInstance(extensionId, selection);
                    break;
                }
            }
        }
        else
        {
            event.data = selectionProvider.getSelection();
        }
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
