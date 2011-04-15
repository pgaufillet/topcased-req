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
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
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
import org.topcased.requirement.core.utils.RequirementHelper;
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
            boolean loadRequirementsFirst = false;
            if (editor == null)
            {
                // editor is being started. No problem, we can acces it through the editing domain
                EditDomain dom = editPart.getViewer().getEditDomain();
                if (dom instanceof DefaultEditDomain)
                {
                    editor = ((DefaultEditDomain) dom).getEditorPart();
                }
                // ensure requirements are loaded
                loadRequirementsFirst = true;
            }
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            if (services != null)
            {
                EObject eobject = services.getEObject(editPart);
                // this is the main element's edit part only if its parent part does not point the same element
                boolean isMainEditPart = editPart.getParent() == null;
                isMainEditPart |= eobject != null && !eobject.equals(services.getEObject(editPart.getParent()));
                if (eobject != null && isMainEditPart)
                {
                    if(loadRequirementsFirst){
                        // ensure requirements are loaded
                        RequirementHelper.INSTANCE.getRequirementProject(eobject.eResource());
                    }
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
