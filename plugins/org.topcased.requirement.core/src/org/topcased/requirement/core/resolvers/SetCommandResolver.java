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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.gef.commands.Command;
import org.topcased.modeler.commands.EMFtoGEFCommandWrapper;
import org.topcased.modeler.editor.TopcasedAdapterFactoryEditingDomain.TopcasedAdapterFactoryLabeler;
import org.topcased.requirement.core.commands.RenameRequirementCommand;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * This Class handles specific behavior for requirements when a SetCommand is executed. This resolver allow hierarchical
 * elements or requirements to rename themself if the graphical element corresponding has name change. This resolver is
 * also used to synchronize commands enablement from a "revert as non impacted" command.
 * 
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * @since Topcased 3.4.0
 */
public class SetCommandResolver extends AdditionalCommand<SetCommand>
{

    private Map<SetCommand, EMFtoGEFCommandWrapper> commands;

    public SetCommandResolver()
    {
        this(SetCommand.class);
        commands = new HashMap<SetCommand, EMFtoGEFCommandWrapper>();
    }

    public SetCommandResolver(Class< ? super SetCommand> clazz)
    {
        super(clazz);
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_execute(java.util.List)
     */
    @Override
    protected void pre_execute(List<SetCommand> setCommands)
    {
        for (SetCommand setCommand : setCommands)
        {
            EStructuralFeature efs = TopcasedAdapterFactoryLabeler.getInstance().getLabelFeature(setCommand.getOwner());
            if (efs != null && efs.equals(setCommand.getFeature()))
            {
                String oldValue = "" ;
                if (setCommand.getOldValue() instanceof String)
                {
                    oldValue = (String) setCommand.getOldValue();
                }
                String newValue = "" ;
                if (setCommand.getValue() instanceof String)
                {
                    newValue = (String) setCommand.getValue();
                }
                EMFtoGEFCommandWrapper cmd = new EMFtoGEFCommandWrapper(new RenameRequirementCommand(setCommand.getOwner(), oldValue, newValue));
                cmd.execute();
                commands.put(setCommand, cmd);
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#pre_redo(java.util.List)
     */
    @Override
    protected void pre_redo(List<SetCommand> setCommands)
    {
        for (SetCommand setCommand : setCommands)
        {
            EMFtoGEFCommandWrapper compound = commands.get(setCommand);
            if (compound != null)
            {
                compound.redo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#post_undo(java.util.List)
     */
    @Override
    protected void post_undo(List<SetCommand> setCommands)
    {
        for (ListIterator<SetCommand> i = setCommands.listIterator(setCommands.size()); i.hasPrevious();)
        {
            SetCommand dndCommand = i.previous();
            EMFtoGEFCommandWrapper compound = commands.get(dndCommand);
            if (compound != null)
            {
                compound.undo();
            }
        }
    }

    /**
     * @see org.topcased.requirement.core.resolvers.AdditionalCommand#getSpecificCommands(org.eclipse.gef.commands.Command,
     *      java.lang.Class)
     */
    @Override
    protected List<Object> getSpecificCommands(Command command, Class< ? > clazz)
    {
        List<Object> result = new ArrayList<Object>();

        // deals with SetCommand (specific behaviour)
        if (command instanceof EMFtoGEFCommandWrapper)
        {
            org.eclipse.emf.common.command.Command cmd = ((EMFtoGEFCommandWrapper) command).getEMFCommand();
            if (cmd instanceof CompoundCommand)
            {
                CompoundCommand compound = (CompoundCommand) cmd;
                // specific compound command name from AbstractTabbedPropertySection to filter the setCommands
                if ("Property Value Change".equals(compound.getLabel())) //$NON-NLS-1$
                {
                    List< ? > commands = compound.getCommandList();
                    for (Object o : commands)
                    {
                        if (o instanceof SetCommand)
                        {
                            if (((SetCommand) o).getClass().equals(clazz))
                            {
                                result.add((SetCommand) o);
                            }
                        }
                    }
                }
                // Special case for undo or redo to synchronize
                // the enablement of the update model command and the update attribute conf command
                else if (compound.getLabel().equals(Messages.getString("SetAsValidHandler.0"))) //$NON-NLS-1$
                {
                    RequirementUtils.fireIsImpactedVariableChanged();
                }
            }
        }
        return result;
    }
    
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