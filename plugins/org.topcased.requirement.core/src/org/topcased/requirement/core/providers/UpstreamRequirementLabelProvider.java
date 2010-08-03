/*****************************************************************************
 * Copyright (c) 2008,2010 Communication & Systems.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Vincent Hemery (Atos Origin) - Use upstreamstyles extension point
 *    
 ******************************************************************************/
package org.topcased.requirement.core.providers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.topcased.modeler.utils.Utils;
import org.topcased.requirement.core.extensions.internal.UpstreamCombinedStyle;
import org.topcased.requirement.core.internal.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.UpstreamStylesPreferenceHelper;

import ttm.Requirement;

/**
 * This customized label provider manages the font's style to applied to the requirement contained in the Upstream Page.<br>
 * 
 * Updated : 28th July 2010<br>
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 */
public class UpstreamRequirementLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider, IColorProvider
{

    /** Font with bold and italic */
    private Font boldItalicFont;

    /** Font without bold nor italic */
    private Font defaultFont;

    /** Font with bold only */
    private Font boldFont;

    /** Font with italic only */
    private Font italicFont;

    /**
     * Constructor
     * 
     * @param adapterFactory The adapter factory to use into this provider.
     */
    public UpstreamRequirementLabelProvider(AdapterFactory adapterFactory)
    {
        super(adapterFactory);

        defaultFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.NONE);
        boldFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.BOLD);
        italicFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.ITALIC);
        boldItalicFont = Utils.getFont(Display.getCurrent().getSystemFont().getFontData()[0], SWT.BOLD | SWT.ITALIC);
    }

    /**
     * Get the font to display the element
     * 
     * @param element element to display
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
     */
    public Font getFont(Object element)
    {
        if (element instanceof Requirement)
        {
            IPreferenceStore store = getPreferenceStore(element);
            UpstreamCombinedStyle style = UpstreamStylesPreferenceHelper.getCombinedStyle(store, (Requirement) element);
            if (style.getBold() && style.getItalic())
            {
                return boldItalicFont;
            }
            else if (style.getBold())
            {
                return boldFont;
            }
            else if (style.getItalic())
            {
                return italicFont;
            }
            else
            {
                return defaultFont;
            }
        }
        return super.getFont(element);
    }

    /**
     * Get the color to display the element
     * 
     * @param element element to display
     */
    @Override
    public Color getForeground(Object element)
    {
        if (element instanceof Requirement)
        {
            IPreferenceStore store = getPreferenceStore(element);
            UpstreamCombinedStyle style = UpstreamStylesPreferenceHelper.getCombinedStyle(store, (Requirement) element);
            RGB colorRGB = style.getColor();
            // inspired by org.topcased.modeler.utils.Utils.getColor(String)
            if (colorRGB != null)
            {
                Color color = JFaceResources.getColorRegistry().get(StringConverter.asString(colorRGB));
                if (color == null)
                {
                    JFaceResources.getColorRegistry().put(StringConverter.asString(colorRGB), colorRGB);
                    color = JFaceResources.getColorRegistry().get(StringConverter.asString(colorRGB));
                }
                return color;
            }
        }
        return super.getForeground(element);
    }

    /**
     * Get the appropriate preference store. This is not the store of the active file editor, but the one of the project
     * containing objects which this class provides labels for.
     * 
     * @param element an element for which label is provided
     * @return appropriate preference store.
     */
    private IPreferenceStore getPreferenceStore(Object element)
    {
        if (element instanceof EObject)
        {
            // get IProject from element
            URI finalUri = ((EObject) element).eResource().getURI();
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot rootWS = workspace.getRoot();
            Path path = new Path(finalUri.toPlatformString(true));
            IFile file = rootWS.getFile(path);
            if (file != null)
            {
                IProject project = file.getProject();
                if (project != null)
                {
                    // now, get specific project scoped preference
                    Preferences root = Platform.getPreferencesService().getRootNode();
                    try
                    {
                        if (root.node(ProjectScope.SCOPE).node(project.getName()).nodeExists(RequirementCorePlugin.getId()))
                        {
                            return new ScopedPreferenceStore(new ProjectScope(project), RequirementCorePlugin.getId());
                        }
                    }
                    catch (BackingStoreException e)
                    {
                        RequirementCorePlugin.log(e);
                    }
                }
            }
        }
        return RequirementCorePlugin.getDefault().getPreferenceStore();
    }
}
