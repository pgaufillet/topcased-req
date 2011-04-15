/*****************************************************************************
 * Copyright (c) 2008,2010 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  	Christophe Mertz (CS) <christophe.mertz@c-s.fr>
 *      Maxime AUDRAIN (CS) - API changes
 *    
 ******************************************************************************/

package org.topcased.requirement.core.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.topcased.facilities.resources.SharedImageHelper;
import org.topcased.requirement.Attribute;
import org.topcased.requirement.AttributeAllocate;
import org.topcased.requirement.AttributeConfiguration;
import org.topcased.requirement.AttributeLink;
import org.topcased.requirement.AttributeValue;
import org.topcased.requirement.ConfiguratedAttribute;
import org.topcased.requirement.ObjectAttribute;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.TextAttribute;
import org.topcased.requirement.core.internal.Messages;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.preferences.CurrentPreferenceHelper;
import org.topcased.requirement.core.utils.LabelHelper;
import org.topcased.requirement.core.utils.RequirementUtils;

/**
 * A dialog for the attribute modification
 * 
 * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
 * @author <a href="mailto:maxime.audrain@c-s.fr">Maxime AUDRAIN</a>
 * 
 */
public class UpdateAttributeDialog extends TitleAreaDialog
{
    private List<?> selection;

    private Text textValue;

    private Combo comboValue;

    private Combo comboAttribute;

    private String attributeName;

    private EObject attributeValueEObject;

    private String attributeValueText;

    private Collection<EObject> listEObjects;

    /**
     * Constructor
     * 
     * @param selection The current selection
     * @param parentShell The parent shell
     */
    public UpdateAttributeDialog(List<?> selection, Shell parentShell)
    {
        super(parentShell);
        this.selection = selection;
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setTitleImage(SharedImageHelper.getTopcasedDialogImage());
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText(Messages.getString("UpdateAttributeDialog.0")); //$NON-NLS-1$
        shell.setSize(550, 450);
    }

    /**
     * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {
        setTitle(Messages.getString("UpdateAttributeDialog.1")); //$NON-NLS-1$
        setMessage(Messages.getString("UpdateAttributeDialog.2")); //$NON-NLS-1$

        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Group group = new Group(composite, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setText(Messages.getString("UpdateAttributeDialog.6")); //$NON-NLS-1$

        getLabelAttribute(group);
        getComboAttribute(group);

        getTextValue(group);
        getComboValue(group);

        return parent;
    }

    private void getLabelAttribute(Composite composite)
    {
        final Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("UpdateAttributeDialog.3")); //$NON-NLS-1$
        label.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
    }

    private void getComboAttribute(Composite composite)
    {
        comboAttribute = new Combo(composite, SWT.READ_ONLY);
        comboAttribute.setText(Messages.getString("UpdateAttributeDialog.3")); //$NON-NLS-1$
        comboAttribute.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        Object firstElement = selection.get(0);
        if (firstElement instanceof EObject)
        {
            AttributeConfiguration configuration = RequirementUtils.getAttributeConfiguration(((EObject) firstElement).eResource());

            List<String> labelList = new ArrayList<String>();
            for (ConfiguratedAttribute anAttribute : configuration.getListAttributes())
            {
                labelList.add(anAttribute.getName());
                comboAttribute.setData(anAttribute.getName(), anAttribute.getListValue());
            }

            comboAttribute.setItems(labelList.toArray(new String[0]));
            comboAttribute.addSelectionListener(new AttributeSelectionListener());
            comboAttribute.addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent event)
                {
                    attributeNameModified();
                }
            });
        }
    }

    private void getTextValue(Composite composite)
    {
        final Label labelTextValue = new Label(composite, SWT.NONE);
        labelTextValue.setText(Messages.getString("UpdateAttributeDialog.4")); //$NON-NLS-1$
        labelTextValue.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));

        textValue = new Text(composite, SWT.BORDER);
        textValue.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        textValue.setEnabled(false);
        textValue.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent event)
            {
                attributeTextModified();
            }
        });
    }

    private void getComboValue(Composite composite)
    {
        final Label labelComboValue = new Label(composite, SWT.NONE);
        labelComboValue.setText(Messages.getString("UpdateAttributeDialog.5")); //$NON-NLS-1$
        labelComboValue.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));

        comboValue = new Combo(composite, 1);
        comboValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        comboValue.setItems(new String[0]);
        comboValue.setEnabled(false);
        comboValue.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent event)
            {
                attributeValueModified();
            }
        });
    }

    private String[] listToArray(Collection< ? extends EObject> list)
    {
        Collection<String> toReturn = new ArrayList<String>();
        for (EObject o : list)
        {
            toReturn.add(LabelHelper.INSTANCE.getName(o));
        }
        return toReturn.toArray(new String[0]);
    }

    /**
     * Manage the selection of an attribute in the dialog
     * 
     * @author <a href="mailto:christophe.mertz@c-s.fr">Christophe Mertz</a>
     * 
     */
    private class AttributeSelectionListener extends SelectionAdapter
    {
        @SuppressWarnings("unchecked")
        public void widgetSelected(SelectionEvent e)
        {
            EObject currObject = (EObject) selection.get(0);
            listEObjects = new ArrayList<EObject>();

            if (currObject instanceof Requirement)
            {
                Attribute attribute = CurrentPreferenceHelper.getAttribute((Requirement) currObject, comboAttribute.getText());
                EList<AttributeValue> values = (EList<AttributeValue>) comboAttribute.getData(comboAttribute.getText());

                if (attribute instanceof TextAttribute && values.size() > 0)
                {
                    comboValue.setEnabled(true);
                    textValue.setEnabled(false);
                    Collection<String> choice = CurrentPreferenceHelper.getTextualValues((TextAttribute) attribute);
                    comboValue.setItems(choice.toArray(new String[0]));
                    comboValue.setSelection(new Point(0, comboValue.getText().length()));
                }
                else if (attribute instanceof AttributeAllocate)
                {
                    comboValue.setEnabled(true);
                    textValue.setEnabled(false);
                    listEObjects.addAll(RequirementUtils.getAllObjects(EcorePackage.eINSTANCE.getEModelElement()));
                    comboValue.setItems(listToArray(listEObjects));
                    comboValue.setSelection(new Point(0, comboValue.getText().length()));
                }
                else if (attribute instanceof AttributeLink)
                {
                    comboValue.setEnabled(true);
                    textValue.setEnabled(false);
                    listEObjects.addAll(RequirementUtils.getAllUpstreams(currObject.eResource()));
                    listEObjects.addAll(RequirementUtils.getAllCurrents(currObject.eResource()));
                    comboValue.setItems(listToArray(listEObjects));
                    comboValue.setSelection(new Point(0, comboValue.getText().length()));
                }
                else if (attribute instanceof ObjectAttribute)
                {
                    comboValue.setEnabled(true);
                    textValue.setEnabled(false);
                    listEObjects.addAll(RequirementUtils.getAllUpstreams(currObject.eResource()));
                    listEObjects.addAll(RequirementUtils.getAllCurrents(currObject.eResource()));
                    listEObjects.addAll(RequirementUtils.getAllObjects(EcorePackage.eINSTANCE.getEModelElement()));
                    comboValue.setItems(listToArray(listEObjects));
                    comboValue.setSelection(new Point(0, comboValue.getText().length()));
                }
                else
                {
                    comboValue.setEnabled(false);
                    textValue.setEnabled(true);
                    comboValue.setItems(new String[0]);
                }
            }
        }
    }

    private void attributeNameModified()
    {
        attributeName = comboAttribute.getText();
    }

    private void attributeTextModified()
    {
        attributeValueText = textValue.getText();
    }

    private void attributeValueModified()
    {
        if (listEObjects.isEmpty())
        {
            attributeValueText = CurrentPreferenceHelper.getDefaultAttributeValue(comboValue.getText());
        }
        else
        {
            EObject[] tabEObjects = listEObjects.toArray(new EObject[listEObjects.size()]);
            try
            {
                if (comboValue.getSelectionIndex() == -1)
                {
                    attributeValueEObject = null;
                }
                else
                {
                    attributeValueEObject = tabEObjects[comboValue.getSelectionIndex()];
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public String getAttributeValueText()
    {
        return attributeValueText;
    }

    public EObject getAttributeValueObject()
    {
        return attributeValueEObject;
    }

}
