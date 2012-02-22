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

package org.topcased.requirement.core.views;

import org.eclipse.emf.edit.command.CommandParameter;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This class provides a marker for the creation of a new requirement :
 * {@link org.topcased.requirement.AnonymousRequirement} and {@link org.topcased.requirement.CurrentRequirement}
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * 
 */
public class AddRequirementMarker
{
    public static final AddRequirementMarker eINSTANCE = new AddRequirementMarker();

    public final int sTART = 1;

    public final int eND = 2;

    public final int pOS = 3;

    private int position = eND;

    private Object markedObject;

    /**
     * Compute the position to insert the Requirement
     * 
     * @param hierarchicalElement
     * 
     * @return the position to insert the Requirement
     */
    public int computeIndex(Object hierarchicalElement)
    {
        int pos = CommandParameter.NO_INDEX; // default value
        int size = ((HierarchicalElement) hierarchicalElement).getRequirement().size();

        if (!(hierarchicalElement instanceof HierarchicalElement))
        {
            return pos;
        }

        if (size > 0)
        {
            if (AddRequirementMarker.eINSTANCE.getPosition() == AddRequirementMarker.eINSTANCE.sTART)
            {
                pos = 0;
            }
            else if (AddRequirementMarker.eINSTANCE.getPosition() == AddRequirementMarker.eINSTANCE.eND)
            {
                pos = CommandParameter.NO_INDEX;
            }
            else if (AddRequirementMarker.eINSTANCE.getPosition() == AddRequirementMarker.eINSTANCE.pOS)
            {
                pos = ((HierarchicalElement) hierarchicalElement).getRequirement().indexOf(AddRequirementMarker.eINSTANCE.getMarkedObject()) + RequirementUtils.countChildrens(hierarchicalElement);
            }
        }

        return pos;
    }

    /**
     * The getter of the type of marker
     * 
     * @return position : type of marker ({@link org.topcased.requirement.core.views.AddRequirementMarker.sTART}
     *         {@link org.topcased.requirement.core.views.AddRequirementMarker.eND}
     *         {@link org.topcased.requirement.core.views.AddRequirementMarker.pOS})
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * The setter of the type of marker
     * 
     * @param position : type of marker ({@link org.topcased.requirement.core.views.AddRequirementMarker.sTART}
     *        {@link org.topcased.requirement.core.views.AddRequirementMarker.eND}
     *        {@link org.topcased.requirement.core.views.AddRequirementMarker.pOS})
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * The getter of the marked object
     * 
     * @return the marked object
     */
    public Object getMarkedObject()
    {
        return markedObject;
    }

    /**
     * The setter of the marked object
     * 
     * @param markedObject
     */
    public void setMarkedObject(Object markedObject)
    {
        this.markedObject = markedObject;
    }
}
