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
 *  Caroline Bourdeu d'Aguerre (ATOS ORIGIN INTEGRATION) caroline.bourdeudaguerre@atosorigin.com - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.requirement.generic.importrequirement.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.topcased.requirement.generic.importrequirement.Activator;
import org.topcased.requirement.generic.importrequirement.elements.Attribute;
import org.topcased.requirement.generic.importrequirement.elements.AttributeRequirement;
import org.topcased.requirement.generic.importrequirement.elements.AttributeSysml;
import org.topcased.requirement.generic.importrequirement.elements.AttributeUml;
import org.topcased.requirement.generic.importrequirement.elements.IStructuredContentProviderTree;
import org.topcased.requirement.generic.importrequirement.elements.Mapping;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedElement;
import org.topcased.requirement.generic.importrequirement.elements.RecognizedTree;
import org.topcased.requirement.generic.importrequirement.utils.Constants;
import org.topcased.requirement.generic.importrequirement.utils.Serializer;

/**
 * The Class ImportRequirementWizardPageMapping.
 */
public class ImportRequirementWizardPageMapping extends WizardPage
{

    /** The Constants PREFERENCE. */
    public static final String PREFERENCE_FOR_LIST_ATTRIBUT = "value for list of attribut";

    /** The Constant PREFERENCE_FOR_LIST_MAPPING. */
    public static final String PREFERENCE_FOR_LIST_MAPPING = "value for list of mapping";

    /** Page components *. */
    private FormToolkit toolkit;

    /** The form. */
    private Form form;

    /** The section. */
    private Composite section;

    /** The button remove attribute. */
    private Button buttonRemoveAttribute;

    /** The button map. */
    private Button buttonMap;

    /** The button remove mapping. */
    private Button buttonRemoveMapping;

    /** The button add attribute. */
    private Button buttonAddAttribute;

    /** The list for input format. */
    private TreeViewer listFormat;

    /** The tree. */
    private RecognizedTree tree;

    /** The List for attributes *. */
    private ListViewer listViewerAttributes;

    /** The list attributes. */
    private Collection<Attribute> listAttributes;

    /** The List for mapping*. */
    private ListViewer listViewerMapping;

    /** The list mapping. */
    private Collection<Mapping> listMapping = new LinkedList<Mapping>();

    /** Behavior Elements *. */
    private RecognizedElement selectedRule;

    /** The selected attribute. */
    private Attribute selectedAttribute;

    /** The selected mapping. */
    private Mapping selectedMapping;

    /** The model type. */
    private String modelType;

    /** The image add. */
    private static Image imageAdd;

    /** The image remove. */
    private static Image imageRemove;

    static
    {
        try
        {
            imageAdd = new Image(Display.getDefault(), Activator.getDefault().getBundle().getResource("icons/add.gif").openStream());
            imageRemove = new Image(Display.getDefault(), Activator.getDefault().getBundle().getResource("icons/remove.gif").openStream());
        }
        catch (IOException e)
        {
        }
    }

    /**
     * Instantiates a new import requirement wizard page mapping.
     * 
     * @param pageName the page name
     * @param t the t
     * @param listAttributes the list attributes
     * @param model the model
     */
    protected ImportRequirementWizardPageMapping(String pageName, RecognizedTree t, Collection<Attribute> listAttributes, String model)
    {
        super(pageName);
        tree = t;
        this.listAttributes = listAttributes;
        modelType = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FillLayout());
        // setImageDescriptor(workbench.getSharedImages().getImageDescriptor(Wizard.DEFAULT_IMAGE));
        this.setDescription("Requirement import from csv, docx, odt, ods or xlsx");
        toolkit = new FormToolkit(composite.getDisplay());
        form = toolkit.createForm(composite);
        // create the base form
        form.setText("Maping");
        toolkit.decorateFormHeading(form);
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        form.getBody().setLayout(layout);
        createSection();
        setControl(composite);
    }

    /**
     * Creates the section.
     */
    private void createSection()
    {
        createDescriptionText(form.getBody());
        section = createSection(form, "Mapping Requirement attributes", 4);
        createRowForlabels(section);
        createListFormat(section);
        createMapButton(section);
        createListAttributes(section);
        createButtonsAttributes(section);
        createListMapping(section);
        createButtonRemoveMapping(section);
    }

    /**
     * Creates the section.
     * 
     * @param mform the mform
     * @param title the title
     * @param numColumns the num columns
     * 
     * @return the composite
     */
    private Composite createSection(Form mform, String title, int numColumns)
    {
        // Create section
        Section section = toolkit.createSection(mform.getBody(), Section.TITLE_BAR | Section.EXPANDED);
        section.setText(title);

        // Create composite
        Composite client = toolkit.createComposite(section);
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add a grid layout
        GridLayout layout = new GridLayout(numColumns, false);
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        client.setLayout(layout);

        section.setClient(client);
        return client;
    }

    /**
     * Creates the description text.
     * 
     * @param section2 the section2
     */
    private void createDescriptionText(Composite section2)
    {
        FormText text = toolkit.createFormText(section2, false);
        text.setText("<form><p>Use this page to <b>map</b> the section of your <b>document</b> with the <b>attributes</b> of the target model</p><p/></form>", true, true);

    }

    /**
     * Creates the row forlabels.
     * 
     * @param section the section
     */
    private void createRowForlabels(Composite section)
    {
        // Create First Label
        Label label = toolkit.createLabel(section, "Requirement identification");
        label.setData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        toolkit.createLabel(section, "");
        // Create Second Label
        Label label2 = toolkit.createLabel(section, "Attributes");
        label2.setData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        toolkit.createLabel(section, "");

    }

    /**
     * Creates the list stereotypes.
     * 
     * @param section2 the section2
     */
    private void createListFormat(Composite section2)
    {

        // Create treeViewer
        listFormat = new TreeViewer(section2, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
        listFormat.getTree().setLayoutData(data);

        // Set the label provider
        listFormat.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the label.
                if (element instanceof String)
                {
                    return (String) element;
                }
                else if (element instanceof RecognizedElement)
                {
                    return ((RecognizedElement) element).getText();
                }
                else if (element instanceof RecognizedTree)
                {
                    return "root";
                }
                return null;
            }
        });

        // Set the content provider
        listFormat.setContentProvider(new IStructuredContentProviderTree(false));

        // Add inputs
        listFormat.setInput(tree);
        listFormat.refresh();

        listFormat.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listFormat.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;

                    // Get the selection
                    if (stru.getFirstElement() instanceof RecognizedElement)
                        selectedRule = (RecognizedElement) stru.getFirstElement();
                }
            }
        });

    }

    /**
     * Creates the map button.
     * 
     * @param section the section
     */
    private void createMapButton(Composite section)
    {
        buttonMap = toolkit.createButton(section, "<- Map ->", SWT.PUSH);
        buttonMap.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
        buttonMap.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                if (selectedAttribute != null && selectedRule != null && !selectedRule.isSelected() && selectedRule.getChildren() == null && listFormat.getSelection() != null
                        && listViewerAttributes.getSelection() != null)
                {
                    addMapping(selectedRule, selectedAttribute);
                    refreshLists();
                    listViewerMapping.refresh();

                    // Delete entry from lists
                    // if (tree.getChildren().contains(selectedRule))
                    // {
                    // tree.getChildren().remove(selectedRule);
                    // }
                    //						
                    // listFormat.refresh();
                    // listAttributes.remove(selectedAttribute);
                    // listViewerAttributes.refresh();

                }
            }
        });
    }

    /**
     * Creates the buttons attributes.
     * 
     * @param section the section
     */
    private void createButtonsAttributes(Composite section)
    {

        // Create add button
        buttonAddAttribute = toolkit.createButton(section, "", SWT.PUSH);
        buttonAddAttribute.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
        buttonAddAttribute.setImage(imageAdd);
        buttonAddAttribute.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                NewAttributePopup dialog = new NewAttributePopup(getShell());
                if (dialog.open() == Dialog.OK)
                {
                    if (Constants.UML_EXTENSION.equals(modelType))
                    {
                        listAttributes.add(new AttributeUml(dialog.getAttributeName(), dialog.isReference(), "Requirement"));
                    }
                    else if (Constants.SYSML_EXTENSION.equals(modelType))
                    {
                        listAttributes.add(new AttributeSysml(dialog.getAttributeName(), dialog.isReference(), "Requirement"));
                    }
                    else
                    {
                        listAttributes.add(new AttributeRequirement(dialog.getAttributeName(), dialog.isReference(), "Requirement"));
                    }
                    listViewerAttributes.refresh();
                }
            }
        });

        // Create remove button
        buttonRemoveAttribute = toolkit.createButton(section, "", SWT.PUSH);
        buttonRemoveAttribute.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        buttonRemoveAttribute.setImage(imageRemove);
        buttonRemoveAttribute.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                listAttributes.remove(selectedAttribute);
                listViewerAttributes.refresh();
                buttonRemoveAttribute.setEnabled(false);
            }

        });
        buttonRemoveAttribute.setEnabled(false);
    }

    /**
     * Refresh lists.
     */
    public void refreshLists()
    {
        this.listFormat.refresh();
        this.listViewerAttributes.refresh();
    }

    /**
     * Creates the list attributes.
     * 
     * @param parent the parent
     */
    protected void createListAttributes(Composite parent)
    {
        listViewerAttributes = new ListViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
        listViewerAttributes.getList().setLayoutData(data);
        // Set the label provider
        listViewerAttributes.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Attribute)
                {
                    return ((Attribute) element).getName();
                }
                return null;
            }
        });

        // Set the content provider
        listViewerAttributes.setContentProvider(new IStructuredContentProvider()
        {

            public Object[] getElements(Object inputElement)
            {
                if (inputElement instanceof Collection)
                {
                    List<Attribute> result = new LinkedList<Attribute>();
                    Collection< ? > collec = (Collection< ? >) inputElement;
                    // for all stereotypes
                    for (Object o : collec)
                    {
                        if (o instanceof Attribute)
                        {
                            result.add((Attribute) o);
                        }
                    }
                    return result.toArray();
                }
                return null;
            }

            public void dispose()
            {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
            {
            }
        });
        listViewerAttributes.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listViewerAttributes.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;
                    if (stru.getFirstElement() instanceof Attribute)
                    {
                        // Get the selection
                        selectedAttribute = (Attribute) stru.getFirstElement();
                        // Enable remove button
                        buttonRemoveAttribute.setEnabled(true);
                    }
                }
                getWizard().getContainer().updateMessage();
                getWizard().getContainer().updateButtons();
            }
        });
        listViewerAttributes.setInput(listAttributes);
    }

    /**
     * Creates the list mapping.
     * 
     * @param section the section
     */
    private void createListMapping(Composite section)
    {
        // Create label
        Label label = toolkit.createLabel(section, "Mapping");
        label.setData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        toolkit.createLabel(section, "");
        toolkit.createLabel(section, "");
        toolkit.createLabel(section, "");

        // Create list mapping
        listViewerMapping = new ListViewer(section, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        listViewerMapping.getList().setLayoutData(data);
        // Set the label provider
        listViewerMapping.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Mapping)
                {
                    return ((Mapping) element).toString();
                }
                return null;
            }
        });

        // Set the content provider
        listViewerMapping.setContentProvider(new IStructuredContentProvider()
        {

            public Object[] getElements(Object inputElement)
            {
                if (inputElement instanceof Collection)
                {
                    List<Mapping> result = new LinkedList<Mapping>();
                    Collection< ? > collec = (Collection< ? >) inputElement;
                    // for all stereotypes
                    for (Object o : collec)
                    {
                        if (o instanceof Mapping)
                        {
                            result.add((Mapping) o);
                        }
                    }
                    return result.toArray();
                }
                return null;
            }

            public void dispose()
            {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
            {
            }
        });
        listViewerMapping.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listViewerMapping.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;
                    if (stru.getFirstElement() instanceof Mapping)
                    {
                        // Get the selection
                        selectedMapping = (Mapping) stru.getFirstElement();
                        // Enable remove button
                        buttonRemoveMapping.setEnabled(true);
                    }
                }
                getWizard().getContainer().updateMessage();
                getWizard().getContainer().updateButtons();
            }
        });
        listViewerMapping.setInput(listMapping);
    }

    /**
     * Creates the button remove mapping.
     * 
     * @param section2 the section2
     */
    private void createButtonRemoveMapping(Composite section2)
    {
        // Create remove button
        buttonRemoveMapping = toolkit.createButton(section, "", SWT.PUSH);
        buttonRemoveMapping.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
        buttonRemoveMapping.setImage(imageRemove);
        buttonRemoveMapping.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                selectedMapping.getElement().setSelected(false);
                listAttributes.add(selectedMapping.getAttribute());
                listMapping.remove(selectedMapping);
                listViewerMapping.refresh();
                refreshLists();
                buttonRemoveMapping.setEnabled(false);

                // Add removed elements to inputs list
                // tree.getChildren().add(selectedMapping.getElement());
                // listFormat.refresh();
                // listAttributes.add(selectedMapping.getAttribute());
                // listViewerAttributes.refresh();
            }

        });
        buttonRemoveMapping.setEnabled(false);

    }

    /**
     * The mapping preferences are loaded only if the inputs are the same from last use if the input document type, the
     * output model type, the profile or the stereotype Load Preferences from a previous use.
     */
    public void loadPreferecencesMapping()
    {
        // Load preferences for attributes list
        String pref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_LIST_MAPPING);
        if (pref != null && pref.length() > 0)
        {
            Serializer<Collection<Mapping>> serializer = new Serializer<Collection<Mapping>>();
            Collection<Mapping> paramDecoded = serializer.unSerialize(pref);
            if (paramDecoded != null && paramDecoded.size() > 0)
            {
                listMapping.clear();
                for (Iterator<Mapping> iterator = paramDecoded.iterator(); iterator.hasNext();)
                {
                    Mapping mapping = iterator.next();
                    for (Iterator<Attribute> j = listAttributes.iterator(); j.hasNext();)
                    {
                        Attribute a = j.next();
                        for (Iterator<RecognizedElement> k = tree.getChildren().iterator(); k.hasNext();)
                        {
                            RecognizedElement r = k.next();
                            if (a.equals(mapping.getAttribute()) && r.getText().equals(mapping.getElement().getText()))
                            {
                                listMapping.add(new Mapping(r, a));
                                j.remove();
                                r.setSelected(true);
                            }
                        }
                    }
                }
                listViewerMapping.refresh();
                listFormat.refresh();
                listViewerAttributes.refresh();
            }
        }
    }

    /**
     * This method will make visible or not the button according to the output model type.
     * 
     * @param isRequirementModel the is requirement model
     */
    public void setIsRequirementModel(boolean isRequirementModel)
    {
        if (isRequirementModel)
        {
            buttonAddAttribute.setVisible(true);
            buttonRemoveAttribute.setVisible(true);
        }
        else
        {
            buttonAddAttribute.setVisible(false);
            buttonRemoveAttribute.setVisible(false);
        }
    }

    /**
     * Clear list attributes.
     */
    public void clearListAttributes()
    {
        listAttributes.clear();
        listViewerAttributes.refresh();
    }

    /**
     * Clear list mapping.
     */
    public void clearListMapping()
    {
        listMapping.clear();
        listViewerMapping.refresh();
    }

    // /// Getters
    /**
     * Gets the list attributes pref.
     * 
     * @return the list attributes pref
     */
    public String getListAttributesPref()
    {
        Serializer<Collection<Attribute>> serializer = new Serializer<Collection<Attribute>>();
        return serializer.serialize(listAttributes);
    }

    /**
     * Gets the list attributes.
     * 
     * @return the list attributes
     */
    public Collection<Attribute> getListAttributes()
    {
        return listAttributes;
    }

    /**
     * Gets the list mapping pref.
     * 
     * @return the list mapping pref
     */
    public String getListMappingPref()
    {
        Serializer<Collection<Mapping>> serializer = new Serializer<Collection<Mapping>>();
        return serializer.serialize(listMapping);
    }

    /**
     * Gets the list mapping.
     * 
     * @return the list mapping
     */
    public Collection<Mapping> getListMapping()
    {
        return listMapping;
    }

    /**
     * Load preference attributes.
     */
    public void loadPreferenceAttributes()
    {
        // Load preferences for list of attributes
        String pref = Activator.getDefault().getPluginPreferences().getString(PREFERENCE_FOR_LIST_ATTRIBUT);
        if (pref != null && pref.length() > 0)
        {
            Serializer<Collection<Attribute>> serializer = new Serializer<Collection<Attribute>>();
            Collection<Attribute> paramDecoded = serializer.unSerialize(pref);
            if (paramDecoded != null && paramDecoded.size() > 0)
            {
                for (Iterator<Attribute> iterator = paramDecoded.iterator(); iterator.hasNext();)
                {
                    Attribute next = (Attribute) iterator.next();
                    if ((Constants.SYSML_EXTENSION.equals(modelType) && "Requirement".equals(next.getSource())) || (Constants.UML_EXTENSION.equals(modelType) && "Class".equals(next.getSource()))
                            || next instanceof AttributeRequirement)
                    {
                        listAttributes.add(next);
                    }

                }
            }
        }
    }

    /**
     * Adds the mapping.
     * 
     * @param selectedRule the selected rule
     * @param a the a
     */
    private void addMapping(RecognizedElement selectedRule, Attribute a)
    {
        listMapping.add(new Mapping(selectedRule, a));
        listAttributes.remove(a);
        selectedRule.setSelected(true);
    }
}
