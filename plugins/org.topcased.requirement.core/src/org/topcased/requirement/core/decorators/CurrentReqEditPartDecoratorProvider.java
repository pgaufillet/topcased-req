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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.ui.IEditorPart;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

public class CurrentReqEditPartDecoratorProvider extends AbstractProvider implements IDecoratorProvider
{

    /** The key used for the mood decoration */
    public static final String CURRENT = "CurrentReqEditPart_Decorator"; //$NON-NLS-1$

    public boolean provides(IOperation operation)
    {
        if (operation instanceof CreateDecoratorsOperation)
        {
            IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation).getDecoratorTarget();
            GraphicalEditPart editPart = (GraphicalEditPart) decoratorTarget.getAdapter(GraphicalEditPart.class);
            IEditorPart editor = RequirementUtils.getCurrentEditor();
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            if (services != null)
            {
                EObject eobject = services.getEObject(editPart);
                if (eobject != null)
                {
                    HierarchicalElement currents = RequirementUtils.getHierarchicalElementFor(eobject);
                    return currents != null && !currents.getRequirement().isEmpty();
                }
            }
        }
        return false;
    }

    public void createDecorators(IDecoratorTarget decoratorTarget)
    {
        EObject object = CurrentReqDecorator.getEObjectDecoratorTarget(decoratorTarget);
        if (object != null)
        {
            decoratorTarget.installDecorator(CURRENT, new CurrentReqDecorator(decoratorTarget));
        }
    }

}
