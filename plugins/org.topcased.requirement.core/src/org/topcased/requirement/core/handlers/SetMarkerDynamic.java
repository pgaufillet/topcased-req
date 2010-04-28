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
package org.topcased.requirement.core.handlers;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementHelper;
import org.topcased.requirement.core.views.AddRequirementMarker;
import org.topcased.requirement.core.views.current.CurrentPage;

/**
 * 
 * This dynamic menu set the marker position for the creation of a new requirement
 * {@link org.topcased.requirement.AnonymousRequirement} and {@link org.topcased.requirement.CurrentRequirement}
 * 
 * FIXME : there was no possibility to make the setMarker menu commands as radio with org.eclipse.ui.menus (or I missed something).
 * This return dynamic items who have no need to be dynamic.
 * The radio button checked style is set on line 87 and everytime we open this menu, we need to set
 * which button will be checked thanks to the setSelection method on lines 96 and 102.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 */
public class SetMarkerDynamic extends CompoundContributionItem
{
    private static String message = "SetMarkerDynamic."; //$NON-NLS-1$
    
    private static int typeStart = 1;
    
    private static int typeEnd = 2;
    
    private static int typeHere = 3;
    
    private static int previouslySelected = 0;
    
    private List<?> currentSelection;

    /**
     * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
     */
    @Override
    protected IContributionItem[] getContributionItems()
    {
        CurrentPage page = RequirementHelper.INSTANCE.getCurrentPage();
        StructuredSelection sel = (StructuredSelection) page.getViewer().getSelection();
        currentSelection = sel.toList();
        
        IContributionItem[] items = new IContributionItem[3];

        for (int i = 0; i < 3; i++) {
            items[i] = getContributionItem(i+1);
        }
        
        return items;
    }

    /**
     * Return the 3 IContributionItem for the set marker menu
     * 
     * @param type the type of the item
     * @return IContributionItem
     */
    private IContributionItem getContributionItem(final int type) {
        return new ContributionItem() {

            /**
             * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu,int)
             */
            public void fill(Menu menu, int index) {
                MenuItem item = new MenuItem(menu, SWT.RADIO);
                item.setText(Messages.getString(message+String.valueOf(type)));
                item.addListener(SWT.Selection, getItemListener());
                
                if (menu != null && previouslySelected == 0)
                {
                    if (type == typeEnd)
                    {
                        item.setSelection(true);
                    }
                }
                else
                {
                    boolean checked = previouslySelected == type;
                    item.setSelection(checked);
                }
            }

            /**
             * Return the item listener for which item has been selected in the popup menu
             * 
             * @return Listener
             */
            private Listener getItemListener() {
                return new Listener() {
                    
                    /**
                     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
                     */
                    public void handleEvent(Event event) {

                        MenuItem item = (MenuItem) event.widget;

                        if (item.getSelection())
                        {
                            if (item.getText().equals(Messages.getString(message+String.valueOf(typeStart))))
                            {
                              previouslySelected = typeStart;
                              addMarker(typeStart);
                            }
                            if (item.getText().equals(Messages.getString(message+String.valueOf(typeEnd))))
                            {
                              previouslySelected = typeEnd;
                              addMarker(typeEnd);
                            }
                            if (item.getText().equals(Messages.getString(message+String.valueOf(typeHere))))
                            {
                              previouslySelected = typeHere;
                              addMarker(typeHere);
                            }
                        }
                    }
                };
            }
        };
    }
    
    /**
     * @param type the type of the marker (where the marker will be put)
     */
    private void addMarker(int type)
    {
        AddRequirementMarker.eINSTANCE.setPosition(type);
        if (type == AddRequirementMarker.eINSTANCE.pOS)
        {
            AddRequirementMarker.eINSTANCE.setMarkedObject(currentSelection.get(0));
        }
    }
}
