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
package org.topcased.requirement.core.resolvers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.CommandStack;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This Class handle specific behaviour for requirements when a CopyToClipboardCommand is executed.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class CopyCommandResolver extends AdditionalCommand<CopyToClipboardCommand>
{

    public CopyCommandResolver()
    {
        this(CopyToClipboardCommand.class);
    }

    public CopyCommandResolver(Class< ? super CopyToClipboardCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_execute(java.util.List)
     */
    @Override
    protected void post_execute(List<CopyToClipboardCommand> copyCommands)
    {
        HierarchicalElementTransfer.INSTANCE.clear();
        for (CopyToClipboardCommand copyCommand : copyCommands)
        {
            List<EObject> hierarchicalElt = new ArrayList<EObject>();
            List< ? > inSelection = (List< ? >) copyCommand.getSourceObjects();

            for (int i = 0; i < inSelection.size(); i++)
            {
                HierarchicalElement elt = RequirementUtils.getHierarchicalElementFor(inSelection.get(i));
                if (elt != null)
                {
                    EObject copy = EcoreUtil.copy(elt);
                    Object inClipboard = AdapterFactoryEditingDomain.getEditingDomainFor(inSelection.get(i)).getClipboard().toArray()[i];
                    copy.eSet(RequirementPackage.eINSTANCE.getHierarchicalElement_Element(), inClipboard);
                    hierarchicalElt.add(copy);
                }
            }
            HierarchicalElementTransfer.INSTANCE.setResult(hierarchicalElt);

        }

    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command,
     *      java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        return CommandStack.getCommands(command, clazz);
    }
}
