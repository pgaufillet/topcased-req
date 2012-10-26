/*****************************************************************************
 * Copyright (c) 2011 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Initial API and implementation
 *
 *****************************************************************************/
package org.topcased.typesmodel;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.topcased.typesmodel.messages"; //$NON-NLS-1$

    public static String AttributeName;

    public static String AttributeRegex;

    public static String Column;

    public static String ColumnName;

    public static String ColumnRegex;

    public static String DeletedFile;

	public static String DeletionParametersComposite_ButtonAdd;

	public static String DeletionParametersComposite_ButtonEdit;

	public static String DeletionParametersComposite_ButtonRemove;

	public static String DeletionParametersComposite_ColumnAttributeName;

	public static String DeletionParametersComposite_ColumnAttributeRegex;

	public static String DeletionParametersComposite_DialogConfirmationText;

	public static String DeletionParametersComposite_DialogConfirmationTitle;

	public static String DeletionParametersComposite_GroupAttributes;

	public static String DeletionParametersComposite_LabelDescriptionRegex;

	public static String DeletionParametersComposite_LabelIdRegex;

	public static String DeletionParametersComposite_TestIdRegex;

	public static String DeletionParametersComposite_TextDescriptionRegex;

	public static String DeletionParametersDialog_Title;
    
    public static String DescriptionDeletionRegex;

    public static String EndText;

    public static String Hierarchical;

    public static String ModifiedFile;

    public static String Name;

    public static String NewFile;

    public static String RequirementColumn;

    public static String RequirementRegex;

    public static String RequirementStyle;

    public static String Style;

    public static String StyleLabel;

    public static String StyleName;

    public static String StyleRegex;

    public static String Types;
    
    public static String lblToMatch;
    
    public static String btnMatchId;
    
    public static String btnMatchDescription;
    
    public static String btnAttributesToMatch;
    
    public static String IniManagerRegistry_DeletionParameterAttributeName;

	public static String IniManagerRegistry_DeletionParameterAttributeRegex;

	public static String IniManagerRegistry_DeletionParameterDescriptionRegex;

	public static String IniManagerRegistry_DeletionParameterIdRegex;

	public static String txtAttributesToMatch;
    
    public static String lblRegex;
    
    public static String txtRegex;
    
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
