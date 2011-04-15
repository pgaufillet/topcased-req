/*****************************************************************************
 * Copyright (c) 2010 Rockwell Collins.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Vincent Hemery (Atos Origin) - Initial API and implementation
 *    
 ******************************************************************************/
package org.topcased.requirement.core.preferences;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.topcased.facilities.preferences.AbstractTopcasedPreferencePage;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.extensions.internal.UpstreamStyle;
import org.topcased.requirement.core.internal.Messages;

/**
 * Manage preferences related to Upstream View styles setting.<br>
 * The user can define the different styles for each extension defining an upstream requirement style thanks to
 * org.topcased.requirement.core.upstream.style extension point.
 * 
 * @author vhemery
 */
public class UpstreamStylesPreferencePage extends AbstractTopcasedPreferencePage
{
    /** the styles which are modified */
    private Collection<UpstreamStyle> allStyles;

    /** the editors which edit styles */
    private ArrayList<StyleEditor> styleEditors;

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        mainComposite.setLayout(layout);

        Group stylesGroup = new Group(mainComposite, SWT.NONE);
        stylesGroup.setText(Messages.getString("UpstreamStylesPreferencePage.0"));//$NON-NLS-1$
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        stylesGroup.setLayoutData(gd);
        stylesGroup.setLayout(new GridLayout(4, false));
        allStyles = UpstreamStylesPreferenceHelper.getLoadedStyles(getPreferenceStore());
        styleEditors = new ArrayList<StyleEditor>(allStyles.size());
        for (UpstreamStyle style : allStyles)
        {
            // label "Name (Priority)"
            String styleLabel = String.format(Messages.getString("UpstreamStylesPreferencePage.1"), style.getName(), style.getPriority());//$NON-NLS-1$
            Label label = new Label(stylesGroup, SWT.NONE);
            label.setText(styleLabel);
            label.setFont(getFont());
            gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
            label.setLayoutData(gd);

            // bold button
            Button boldButton = new Button(stylesGroup, SWT.TOGGLE);
            boldButton.setImage(RequirementCorePlugin.getImageDescriptor("icons/bold.gif").createImage()); //$NON-NLS-1$
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            boldButton.setLayoutData(gd);

            // italic button
            Button italicButton = new Button(stylesGroup, SWT.TOGGLE);
            italicButton.setImage(RequirementCorePlugin.getImageDescriptor("icons/italic.gif").createImage()); //$NON-NLS-1$
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            italicButton.setLayoutData(gd);

            // color button
            ColorSelector colorEditor = new ColorSelector(stylesGroup);
            colorEditor.setColorValue(style.getColor());
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            colorEditor.getButton().setLayoutData(gd);

            // second line
            // override label
            Label overrideLabel = new Label(stylesGroup, SWT.NONE);
            overrideLabel.setText(Messages.getString("UpstreamStylesPreferencePage.2"));//$NON-NLS-1$
            gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
            overrideLabel.setLayoutData(gd);

            // override check boxes
            Button overrideBoldButton = new Button(stylesGroup, SWT.CHECK);
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            overrideBoldButton.setLayoutData(gd);
            Button overrideItalicButton = new Button(stylesGroup, SWT.CHECK);
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            overrideItalicButton.setLayoutData(gd);
            Button overrideColorButton = new Button(stylesGroup, SWT.CHECK);
            gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
            overrideColorButton.setLayoutData(gd);

            // create the style editor to maintain style values
            StyleEditor editor = new StyleEditor(style, boldButton, overrideBoldButton, italicButton, overrideItalicButton, colorEditor, overrideColorButton);
            editor.load();
            styleEditors.add(editor);
        }

        return mainComposite;
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
        // Do nothing
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk()
    {
        // Update editors with widgets' values
        for (StyleEditor editor : styleEditors)
        {
            editor.store();
        }

        // save preferences
        UpstreamStylesPreferenceHelper.storeInPreferences(getPreferenceStore(), allStyles);
        return super.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults()
    {
        // Update widgets' values with editors (and editors' styles)
        for (StyleEditor editor : styleEditors)
        {
            editor.loadDefault();
        }

        // reset preferences
        UpstreamStylesPreferenceHelper.storeInPreferences(getPreferenceStore(), allStyles);
        super.performDefaults();
    }

    /**
     * An editor which stores its values in a style. This way, the style will later store up to date values in
     * preferences.
     * 
     * @author vhemery
     */
    protected class StyleEditor
    {
        /** the edited style */
        private UpstreamStyle style;

        /** button determining the bold value */
        private Button bold;

        /** button determining the override bold value */
        private Button overrideBold;

        /** button determining the italic value */
        private Button italic;

        /** button determining the override italic value */
        private Button overrideItalic;

        /** selector determining the color value */
        private ColorSelector color;

        /** button determining the override color value */
        private Button overrideColor;

        /**
         * Construct an editor to maintain values of a style
         * 
         * @param editedStyle the style to edit
         */
        public StyleEditor(UpstreamStyle editedStyle, Button boldButton, Button overrideBoldButton, Button italicButton, Button overrideItalicButton, ColorSelector colorSelector,
                Button overrideColorButton)
        {
            style = editedStyle;
            bold = boldButton;
            overrideBold = overrideBoldButton;
            italic = italicButton;
            overrideItalic = overrideItalicButton;
            color = colorSelector;
            overrideColor = overrideColorButton;
        }

        /**
         * Stores this style editor's value back into the style (and not preference store).
         */
        public void store()
        {
            style.setBold(bold.getSelection());
            style.setOverrideBold(overrideBold.getSelection());
            style.setItalic(italic.getSelection());
            style.setOverrideItalic(overrideItalic.getSelection());
            style.setColor(color.getColorValue());
            style.setOverrideColor(overrideColor.getSelection());
        }

        /**
         * Initializes this style editor with the preference value from the style (from preference store).
         */
        public void load()
        {
            bold.setSelection(style.getBold());
            overrideBold.setSelection(style.getOverrideBold());
            italic.setSelection(style.getItalic());
            overrideItalic.setSelection(style.getOverrideItalic());
            color.setColorValue(style.getColor());
            overrideColor.setSelection(style.getOverrideColor());
        }

        /**
         * Initializes this field editor with the default preference value from the style.
         */
        public void loadDefault()
        {
            style.restoreDefaultValues();
            load();
        }
    }

    @Override
    protected String getBundleId()
    {
        return RequirementCorePlugin.getId();
    }
}
