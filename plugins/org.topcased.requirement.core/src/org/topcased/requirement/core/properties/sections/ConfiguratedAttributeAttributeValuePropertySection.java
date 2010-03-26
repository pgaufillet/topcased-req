/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.core.properties.sections;

import org.eclipse.draw2d.GridData;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.swt.SWT;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.RequirementPackage;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.tabbedproperties.sections.AbstractListPropertySection;
import org.topcased.tabbedproperties.sections.widgets.TableViewerComposite;

/**
 * The section for the list of AttributeValue contained in a {@link ConfiguratedAttribute}.
 * 
 * Creation 16 dec. 2008
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 */
public class ConfiguratedAttributeAttributeValuePropertySection extends AbstractListPropertySection
{

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getLabelText()
     */
    @Override
    protected String getLabelText()
    {
        return Messages.getString("ConfiguratedAttributeAttributeValuePropertySection.0"); //$NON-NLS-1$
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractListPropertySection#getLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getLabelProvider()
    {
        return new AdapterFactoryLabelProvider(RequirementUtils.getAdapterFactory());
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractListPropertySection#getListValues()
     */
    @Override
    protected Object getListValues()
    {
        ConfiguratedAttribute configuratedAtt = (ConfiguratedAttribute) getEObject();
        return configuratedAtt.getListValue().toArray();
    }

    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#getFeature()
     */
    @Override
    protected EStructuralFeature getFeature()
    {
        return RequirementPackage.eINSTANCE.getConfiguratedAttribute_ListValue();
    }
    
    /**
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#shouldUseExtraSpace()
     */
    @Override
    public boolean shouldUseExtraSpace()
    {
        return true;
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractTabbedPropertySection#isReadOnly()
     */
    @Override
    protected boolean isReadOnly()
    {
        return true;
    }
    
    /**
     * @see org.topcased.tabbedproperties.sections.AbstractListPropertySection#getTable()
     */
    @Override
    public TableViewerComposite getTable()
    {
        TableViewerComposite table = super.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        return table;
    }
}
