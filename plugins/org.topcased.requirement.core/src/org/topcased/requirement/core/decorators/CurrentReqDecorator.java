/***********************************************************************************************************************
 * Copyright (c) 2010 Atos Origin Integration
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Tristan FAURE (Atos Origin Integration) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.decorators;

import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ImageFigureEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.topcased.draw2d.figures.Label;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.RequirementPreferenceConstants;
import org.topcased.requirement.core.utils.RequirementUtils;

@SuppressWarnings("restriction")
public class CurrentReqDecorator implements IDecorator
{

    private static final Image ICON_CUR;

    private static final Image ICON_CUR_DERIVED;

    /** the object to be decorated */
    private IDecoratorTarget decoratorTarget;

    /** the decoration being displayed */
    private IDecoration decoration;

    private boolean displayDecorator;

    static
    {
        ICON_CUR = RequirementCorePlugin.getImageDescriptor("$nl$/icons/current.gif").createImage(); //$NON-NLS-1$
        JFaceResources.getImageRegistry().put("current.gif", ICON_CUR);
        ICON_CUR_DERIVED = RequirementCorePlugin.getImageDescriptor("$nl$/icons/derived.gif").createImage(); //$NON-NLS-1$
        JFaceResources.getImageRegistry().put("derived.gif", ICON_CUR_DERIVED);
    }

    /**
     * Creates a new <code>AbstractDecorator</code> for the decorator target passed in.
     * 
     * @param target the object to be decorated
     */
    public CurrentReqDecorator(IDecoratorTarget target)
    {
        this.decoratorTarget = target;
    }

    /**
     * Method to determine if the decoratorTarget is a supported type for this decorator and return the associated
     * Classifier element.
     * 
     * @param target IDecoratorTarget to check and return valid Classifier target.
     * @return node Node if IDecoratorTarget can be supported, null otherwise.
     */
    public static EObject getEObjectDecoratorTarget(IDecoratorTarget target)
    {
        return (EObject) target.getAdapter(EObject.class);

    }

    /**
     * Gets the object to be decorated.
     * 
     * @return Returns the object to be decorated
     */
    protected IDecoratorTarget getDecoratorTarget()
    {
        return decoratorTarget;
    }

    /**
     * @return Returns the decoration.
     */
    public IDecoration getDecoration()
    {
        return decoration;
    }

    /**
     * @param deco The decoration to set.
     */
    public void setDecoration(IDecoration deco)
    {
        this.decoration = deco;
    }

    /**
     * Removes the decoration if it exists and sets it to null.
     */
    protected void removeDecoration()
    {
        if (decoration != null)
        {
            decoratorTarget.removeDecoration(decoration);
            decoration = null;
        }
    }

    /**
     * Compute the direction using the kind of graphical representation is annotated
     * 
     * @param target
     * @return the position where the decorator is displayed
     */
    private IDecoratorTarget.Direction getDirection(IDecoratorTarget target)
    {
        IDecoratorTarget.Direction direction = IDecoratorTarget.Direction.NORTH_WEST;
        GraphicalEditPart editPart = (GraphicalEditPart) target.getAdapter(GraphicalEditPart.class);
        if (editPart != null)
        {
            if (editPart instanceof ConnectionEditPart)
            {
                direction = IDecoratorTarget.Direction.CENTER;
            }
            else
            {
                IFigure figure = editPart.getFigure();
                if (figure instanceof Label)
                {
                    direction = IDecoratorTarget.Direction.EAST;
                }
            }
        }
        return direction;
    }

    /**
     * Creates the appropriate review decoration if all the criteria is satisfied by the view passed in.
     */
    public void refresh()
    {
        removeDecoration();
        if (displayDecorator && hasCurrents())
        {
            GraphicalEditPart editPart = (GraphicalEditPart) getDecoratorTarget().getAdapter(GraphicalEditPart.class);
            IMapMode mm = MapModeUtil.getMapMode(editPart.getFigure());
            if (editPart instanceof ConnectionEditPart)
            {
                setDecoration(getDecoratorTarget().addConnectionDecoration(getImageFigure(mm), 30, true));
            }
            else
            {
                setDecoration(getDecoratorTarget().addShapeDecoration(getImageFigure(mm), getDirection(getDecoratorTarget()), -1, true));
            }
        }
    }

    private IFigure getImageFigure(IMapMode mm)
    {
        ImageFigureEx figure = new ImageFigureEx();
        if (isDerived())
        {
            figure.setImage(ICON_CUR_DERIVED);
            figure.setSize(mm.DPtoLP(ICON_CUR_DERIVED.getBounds().width), mm.DPtoLP(ICON_CUR_DERIVED.getBounds().height));

        }
        else
        {
            figure.setImage(ICON_CUR);
            figure.setSize(mm.DPtoLP(ICON_CUR.getBounds().width), mm.DPtoLP(ICON_CUR.getBounds().height));

        }
        // IFigure tooltip = new
        // org.eclipse.draw2d.Label(composeTooltip(status));
        // tooltip.setSize(100, 100);
        // figure.setToolTip(tooltip);

        return figure;
    }

    protected boolean hasCurrents()
    {
        Collection<Requirement> currents = getCurrents();
        return currents != null && !currents.isEmpty();
    }

    protected boolean isDerived()
    {
        Collection<Requirement> currents = getCurrents();
        boolean found = false;
        for (Requirement r : currents)
        {
            for (Attribute a : r.getAttribute())
            {
                if (a instanceof AttributeLink)
                {
                    found = true;
                    AttributeLink link = (AttributeLink) a;
                    if (link.getValue() == null)
                    {
                        return true;
                    }
                }
            }
        }
        return !found;
    }

    protected Collection<Requirement> getCurrents()
    {
        EObject eobject = getEObjectDecoratorTarget(decoratorTarget);
        HierarchicalElement element = RequirementUtils.getHierarchicalElementFor(eobject);
        return element == null ? null : element.getRequirement();
    }

    /**
     * Adds decoration if applicable.
     */
    public void activate()
    {
        displayDecorator = RequirementCorePlugin.getDefault().getPreferenceStore().getBoolean(RequirementPreferenceConstants.DISPLAY_CURRENT_DECORATOR);
        refresh();
    }

    /**
     * Removes the decoration.
     */
    public void deactivate()
    {
        removeDecoration();
    }

}
