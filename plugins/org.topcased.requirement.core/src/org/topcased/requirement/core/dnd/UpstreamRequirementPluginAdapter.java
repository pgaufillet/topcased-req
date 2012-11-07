/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Laurent Devernay (ATOS) <laurent.devernay@atos.net>
 *    
 ******************************************************************************/
package org.topcased.requirement.core.dnd;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IDropActionDelegate;
import org.topcased.requirement.core.commands.CreateCurrentReqCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;
import org.topcased.requirement.core.utils.RequirementUtils;

import ttm.Requirement;

public class UpstreamRequirementPluginAdapter implements IDropActionDelegate
{

    public boolean run(Object source, Object target)
    {
        EObject targetEObject = getAdapter(target, EObject.class);
        if (!(source instanceof byte[] || targetEObject == null))
        {
            return false;
        }
        byte[] data = (byte[]) source;
        Resource requirementDroppedResource = UpstreamPluginTransferData.getUpstreamResource(data);
        if (requirementDroppedResource == null)
        {
            return false;
        }
        List<Requirement> requirements = UpstreamPluginTransferData.getDroppedRequirements(requirementDroppedResource, data);
        if (requirements == null || requirements.isEmpty())
        {
            return false;
        }
        Command dropCmd = createSpecificDropAction(requirements, targetEObject);
        // Re-using mostly unmodified RequirementDropListener code, but this is fairly generic
        // execution of the command
        if (dropCmd != null && dropCmd.canExecute())
        {
            IEditorPart editor = RequirementUtils.getCurrentEditor();
            IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
            if (services != null)
            {
                EditingDomain domain = services.getEditingDomain(editor);
                domain.getCommandStack().execute(dropCmd);
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Object o, Class< ? extends T> aClass)
    {
        if (aClass.isInstance(o))
        {
            return (T) o;
        }
        if (o instanceof IAdaptable)
        {
            IAdaptable a = (IAdaptable) o;
            Object adapter = a.getAdapter(aClass);
            if (adapter != null)
            {
                return (T) adapter;
            }
        }
        return (T) Platform.getAdapterManager().getAdapter(o, aClass);
    }

    public Command createSpecificDropAction(Collection< ? > source, EObject target)
    {
        IEditorPart editor = RequirementUtils.getCurrentEditor();
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(editor);
        if (services != null)
        {
            EditingDomain ed = services.getEditingDomain(editor);
            if (ed instanceof TransactionalEditingDomain)
            {
                CompoundCommand command = new CompoundCommand();
                Collection<Object> singleton = Collections.singleton(source.toArray()[0]);
                CreateCurrentReqCommand emptyReqsSubCommand = new CreateCurrentReqCommand("Create Current Requirement");
                emptyReqsSubCommand.setTarget(target);
                emptyReqsSubCommand.setRequirements(singleton);
                command.append(emptyReqsSubCommand);
                if (!command.isEmpty())
                {
                    return command;
                }
            }
        }
        return null;
    }

}
