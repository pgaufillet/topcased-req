/*****************************************************************************
 * Copyright (c) 2009 ATOS ORIGIN INTEGRATION.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tristan FAURE (ATOS ORIGIN INTEGRATION) tristan.faure@atosorigin.com - Initial API and implementation
 *
  *****************************************************************************/
package org.topcased.requirement.generic;

import java.lang.reflect.Field;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.topcased.modeler.editor.Modeler;
import org.topcased.sam.requirement.RequirementProject;
import org.topcased.sam.requirement.util.RequirementResourceImpl;

/**
 * The Class RequirementProjectManager.
 */
public class RequirementProjectManager
{
    private static RequirementProjectManager instance = new RequirementProjectManager();

    private RequirementProjectManager()
    {
    }

    public static RequirementProjectManager getInstance()
    {
        return instance;
    }

    private void manageGraphicalViewer(Modeler modeler)
    {
        try
        {
            Field f = GraphicalEditor.class.getDeclaredField("graphicalViewer");
            f.setAccessible(true);
            GraphicalViewer viewer = (GraphicalViewer) f.get(modeler);
            viewer.addDropTargetListener(new Drop(viewer));
            f.setAccessible(false);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }

    public synchronized RequirementProject getRequirementProject(String uri, IEditorPart part)
    {
        RequirementProject result = null;
        IEditorPart editor = Activator.getActiveEditor();
        if (editor instanceof Modeler)
        {
            Modeler modeler = (Modeler) editor;
            EList<Resource> resources = modeler.getEditingDomain().getResourceSet().getResources();
            for (Resource r : resources)
            {
                if (r instanceof RequirementResourceImpl && r.getURI().toString().equals(uri))
                {
                    result = (RequirementProject) r.getContents().get(0);
                }
            }
            if (result == null)
            {
                result = (RequirementProject) modeler.getEditingDomain().getResourceSet().getResource(URI.createURI(uri), true).getContents().get(0);
                modeler.getEditingDomain().getResourceSet().getResources().add(result.eResource());
                manageGraphicalViewer(modeler);
            }
        }

        return result;
    }

    @Deprecated
    public synchronized RequirementProject getRequirementProject(String uri)
    {
        RequirementProject result = null;
        IEditorPart editor = Activator.getActiveEditor();
        if (editor instanceof Modeler)
        {
            result = getRequirementProject(uri, editor);
        }

        return result;
    }

}
