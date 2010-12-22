/*****************************************************************************
 * Copyright (c) 2009,2010 ATOS ORIGIN INTEGRATION.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) - Initial API and implementation
 *  Maxime AUDRAIN (CS) - API changes
 *  Sebastien GABEL (CS) - evolutions
 *
 *****************************************************************************/
package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.topcased.modeler.commands.DeleteModelCommand;
import org.topcased.requirement.core.commands.RemoveRequirementCommand;
import org.topcased.requirement.core.extensions.IEditorServices;
import org.topcased.requirement.core.extensions.SupportingEditorsManager;

/**
 * This Class handle specific behaviour for requirements when a <b>DeleteModelCommand</b> is executed.
 * 
 * @author <a href="tristan.faure@atosorigin.com">Tristan FAURE</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class DeleteCommandResolver extends AdditionalCommand<DeleteModelCommand>
{

    private Map<DeleteModelCommand, AbstractCommand> commands;

    public DeleteCommandResolver()
    {
        this(DeleteModelCommand.class);
        commands = new HashMap<DeleteModelCommand, AbstractCommand>();
    }

    public DeleteCommandResolver(Class< ? super DeleteModelCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void pre_execute(List<DeleteModelCommand> deleteModelCommands)
    {
        IEditorServices services = SupportingEditorsManager.getInstance().getServices(getEditor());
        if (services != null)
        {
            EditingDomain domain = services.getEditingDomain(getEditor());
            for (DeleteModelCommand deleteModelCommand : deleteModelCommands)
            {
                Collection<EObject> eObjects = deleteModelCommand.getObjectsDeleting();

                AbstractCommand deleteCmd = new RemoveRequirementCommand(domain, eObjects);
                deleteCmd.execute();
                commands.put(deleteModelCommand, deleteCmd);
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<DeleteModelCommand> deleteModelCommands)
    {
        for (DeleteModelCommand deleteModelCommand : deleteModelCommands)
        {
            AbstractCommand command = commands.get(deleteModelCommand);
            if (command != null)
            {
                command.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<DeleteModelCommand> deleteModelCommands)
    {
        for (ListIterator<DeleteModelCommand> i = deleteModelCommands.listIterator(deleteModelCommands.size()); i.hasPrevious();)
        {
            DeleteModelCommand deleteModelCommand = i.previous();
            AbstractCommand command = commands.get(deleteModelCommand);
            if (command != null)
            {
                command.undo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.bundle.topcased.resolvers.AdditionalCommand#dipose()
     */
    @Override
    public void dipose()
    {
        super.dipose();
        if (commands != null)
        {
            commands.clear();
        }
    }
}