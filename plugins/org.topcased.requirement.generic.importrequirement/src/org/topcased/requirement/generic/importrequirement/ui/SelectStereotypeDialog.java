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

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.util.UMLUtil;
import org.topcased.requirement.generic.importrequirement.Activator;
import org.topcased.requirement.generic.importrequirement.component.ComponentHelp;
import org.topcased.requirement.generic.importrequirement.component.CustomPopupDialog;
import org.topcased.requirement.generic.importrequirement.component.HelpDialog;
import org.topcased.requirement.generic.importrequirement.utils.Constants;
import org.topcased.sysml.SysMLPackage;

/**
 * The Class SelectStereotypeDialog.
 */
public class SelectStereotypeDialog extends CustomPopupDialog
{

    /** The Constant PREFERENCE_FOR_PROFILE_URI. */
    public static final String PREFERENCE_FOR_PROFILE_URI = "profile_uri_csvimport";

    /** The Constant PREFERENCE_FOR_STEREO. */
    public static final String PREFERENCE_FOR_STEREO = "stereo_csvimport";

    /** The list viewer. */
    private ListViewer listViewer;

    /** The section. */
    private Section section;

    /** The text for uri. */
    private Text textForURI;

    /** The label profile. */
    private Label labelProfile;

    /** The label stereotype. */
    private Label labelStereotype;

    /** The wizard. */
    private ImportRequirementWizardPageSelectDocument wizard;

    /** The current profile. */
    private Profile currentProfile;

    /** The list stereotypes. */
    private ListViewer listStereotypes;

    /** The URI pref string. */
    private String uriPrefString;

    /** The selected stereotype. */
    private Stereotype selectedStereotype = null;

    /** The requirement inheritance. */
    private static Collection<EClass> requirementInheritance;

    /**
     * Instantiates a new select stereotype dialog.
     * 
     * @param parentShell the parent shell
     * @param point the point
     * @param pageParent the page parent
     * @param model the model
     */
    public SelectStereotypeDialog(Shell parentShell, Point point, NotifyElement pageParent, String model)
    {
        super(parentShell, point, "Stereotype Selection");
        if (pageParent instanceof ImportRequirementWizardPageSelectDocument)
        {
            wizard = (ImportRequirementWizardPageSelectDocument) pageParent;
        }
        requirementInheritance = getAllClasses(model);
    }

    /**
     * Gets the all classes.
     * 
     * @param model the model
     * 
     * @return the all classes
     */
    private static Collection<EClass> getAllClasses(String model)
    {
        Collection<EClass> result;
        if (Constants.SYSML_EXTENSION.equals(model.toLowerCase()))
        {
            result = new LinkedList<EClass>(SysMLPackage.Literals.REQUIREMENT.getEAllSuperTypes());
            result.add(SysMLPackage.Literals.REQUIREMENT);
        }
        else
        {
            result = new LinkedList<EClass>(UMLPackage.Literals.CLASS.getEAllSuperTypes());
            result.add(UMLPackage.Literals.CLASS);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.component.CustomPopupDialog#createSubsection()
     */
    @Override
    public void createSubsection()
    {
        section = createSection(formHead.getForm(), "Select a profile", "", 2);
        Composite compo = (Composite) section.getClient();
        compo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        createList(compo);
        createLabel(compo, "Selected Profile : ", labelProfile);
        createFieldBrows(compo);
        createLabel(compo, "Selected Stereotype : ", labelStereotype);
        createListStereotypes(compo);

        // Load profile from a previous use
        currentProfile = wizard.getProfile();
        if (currentProfile != null)
        {
            textForURI.setText(currentProfile.getName());
            listStereotypes.setInput(currentProfile.getOwnedStereotypes());
            uriPrefString = currentProfile.eResource().getURI().toString();
        }

        // Select the stereotype from previous use
        selectedStereotype = wizard.getStereotype();
        if (currentProfile != null && selectedStereotype != null)
        {
            listStereotypes.setSelection(new StructuredSelection(selectedStereotype));
        }

    }

    /**
     * Creates the list stereotypes.
     * 
     * @param section2 the section2
     */
    private void createListStereotypes(Composite section2)
    {
        listStereotypes = new ListViewer(section2, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        listStereotypes.getList().setLayoutData(data);

        // Set the label provider
        listStereotypes.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Stereotype)
                {
                    Stereotype stereotype = (Stereotype) element;
                    return stereotype.getName();
                }
                return null;
            }
        });

        // Set the content provider
        listStereotypes.setContentProvider(new IStructuredContentProvider()
        {

            public Object[] getElements(Object inputElement)
            {
                if (inputElement instanceof Collection)
                {
                    int countStereo = 0;
                    Collection<Stereotype> result = new LinkedList<Stereotype>();
                    Collection< ? > collec = (Collection< ? >) inputElement;
                    // for all stereotypes
                    for (Object o : collec)
                    {
                        if (o instanceof Stereotype)
                        {
                            Stereotype stereo = (Stereotype) o;
                            // for all association to the current stereotype
                            for (Association a : stereo.getAssociations())
                            {
                                if (a instanceof Extension)
                                {
                                    Extension e = (Extension) a;
                                    // get the MetaClass
                                    EClassifier metaClass = CustomUMLUtil.getEClass(e.getMetaclass());
                                    // if the MetaClass is in the requirement hierarchie
                                    if (requirementInheritance.contains(metaClass))
                                    {
                                        result.add(stereo);
                                        countStereo++;
                                    }
                                }
                            }
                        }
                    }
                    // Display error if there 0 stereotype
                    if (countStereo == 0)
                    {
                        section.setDescription("No stereotype applicable on requirement element in the selected profile");
                        section.layout(true);
                    }
                    else
                    {
                        section.setDescription("");
                        section.layout(true);
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
        listStereotypes.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listStereotypes.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;
                    if (stru.getFirstElement() instanceof Stereotype)
                    {
                        selectedStereotype = (Stereotype) stru.getFirstElement();
                    }
                }

            }
        });
    }

    /**
     * Creates the label.
     * 
     * @param section2 the section2
     * @param desc the desc
     * @param label the label
     */
    private void createLabel(Composite section2, String desc, Label label)
    {
        label = toolkit.createLabel(section, desc, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    }

    /**
     * Creates the field brows.
     * 
     * @param section the section
     */
    private void createFieldBrows(Composite section)
    {
        textForURI = toolkit.createText(section, "", SWT.BORDER);
        textForURI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textForURI.setEnabled(false);
        Button b = toolkit.createButton(section, "browse", SWT.PUSH);
        b.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                ResourceDialog dialog = new ResourceDialog(getShell(), "Select a profile", SWT.OPEN);
                if (dialog.open() == ResourceDialog.OK)
                {
                    loadProfile(dialog.getURIText());
                    // Save for the pref
                    uriPrefString = dialog.getURIText();
                }
            }
        });
    }

    /**
     * Load profile.
     * 
     * @param uriText the uri text
     */
    protected void loadProfile(String uriText)
    {
        URI uri = URI.createURI(uriText);
        EObject eobject = (EObject) EcoreUtil.getObjectByType(new ResourceSetImpl().getResource(uri, true).getContents(), UMLPackage.Literals.PROFILE);
        if (eobject != null)
        {
            textForURI.setText(((Profile) eobject).getName());
            currentProfile = (Profile) eobject;
            listStereotypes.setInput(((Profile) eobject).getOwnedStereotypes());
        }
        else
        {
            MessageDialog.openError(getShell(), "Problem", "The selected model is not a profile");
        }
    }

    /**
     * Creates the section.
     * 
     * @param mform the mform
     * @param title the title
     * @param desc the desc
     * @param numColumns the num columns
     * 
     * @return the composite
     */
    private Section createSection(Form mform, String title, String desc, int numColumns)
    {
        Section section = toolkit.createSection(mform.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        section.setText(title);
        section.setDescription(desc);
        // toolkit.createCompositeSeparator(section);
        Composite client = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        layout.numColumns = numColumns;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        section.setClient(client);
        return section;
    }

    /**
     * Creates the UI.
     * 
     * @param parent the parent composite
     */
    protected void createList(Composite parent)
    {
        listViewer = new ListViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        listViewer.getList().setLayoutData(data);
        // Set the label provider
        listViewer.setLabelProvider(new LabelProvider()
        {
            public String getText(Object element)
            {
                // Return the resolution's label.
                if (element instanceof Profile)
                {
                    Profile profile = (Profile) element;
                    return profile.getName();
                }
                return null;
            }
        });

        // Set the content provider
        profileContentProvider provider = new profileContentProvider();
        listViewer.setContentProvider(provider);
        listViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private ISelection selection;

            public void selectionChanged(SelectionChangedEvent event)
            {
                selection = listViewer.getSelection();
                if (selection instanceof IStructuredSelection)
                {
                    IStructuredSelection stru = (IStructuredSelection) selection;
                    if (stru.getFirstElement() instanceof Profile)
                    {
                        currentProfile = (Profile) stru.getFirstElement();
                        selectedStereotype = null;
                        textForURI.setText(currentProfile.getName());
                        listStereotypes.setInput(currentProfile.getOwnedStereotypes());
                        uriPrefString = currentProfile.eResource().getURI().toString();
                    }
                }
            }
        });
        listViewer.setInput(UMLPlugin.getEPackageNsURIToProfileLocationMap().values());
    }

    /**
     * The Class HelpAction.
     */
    private class HelpAction extends Action
    {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#getImageDescriptor()
         */
        public ImageDescriptor getImageDescriptor()
        {
            return ImageDescriptor.createFromImage(ComponentHelp.image);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run()
        {
            HelpDialog dialog = new HelpDialog(getShell(), Display.getDefault().getCursorLocation(), "Help", "Select a stereotype to applid on the generated class or requirement");
            dialog.open();
        }
    }

    /**
     * The Class SaveAction.
     */
    private class SaveAction extends Action
    {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#getImageDescriptor()
         */
        public ImageDescriptor getImageDescriptor()
        {
            return ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getResource("icons/save_edit-1.gif"));
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        public void run()
        {
            wizard.setStereotype(selectedStereotype);
            wizard.setProfile(currentProfile);
            wizard.setProfileURI(uriPrefString);
            close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.topcased.doc2model.requirement.component.CustomPopupDialog#getActions()
     */
    protected Collection<Action> getActions()
    {
        LinkedList<Action> actions = new LinkedList<Action>();
        actions.add(new SaveAction());
        actions.add(new HelpAction());
        return actions;
    }

}

class CustomUMLUtil extends UMLUtil
{
    public static EClassifier getEClass(Class c)
    {
        return getEClassifier(c);
    }
}

class profileContentProvider implements IStructuredContentProvider
{
    public Object[] getElements(Object inputElement)
    {
        Collection<Profile> profiles = new LinkedList<Profile>();
        if (inputElement instanceof Collection)
        {
            Collection< ? > collec = (Collection< ? >) inputElement;
            for (Object o : collec)
            {
                if (o instanceof URI)
                {
                    URI uri = (URI) o;
                    profiles.add((Profile) new ResourceSetImpl().getResource(uri.trimFragment(), true).getEObject(uri.fragment()));
                }
            }
        }
        return profiles.toArray();
    }

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }
}
