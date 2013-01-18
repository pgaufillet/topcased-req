/*****************************************************************************
 * Copyright (c) 2012 Atos
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
package org.topcased.requirement.reqif.utils;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.topcased.requirement.reqif.utils.messages"; //$NON-NLS-1$
	public static String BROWSE;
	public static String DESC;
	public static String DESCRIPTION;
	public static String EXPORT_UPSTREAM2REQIF;
	public static String IDENT;
	public static String IMPORT_REQIF;
	public static String IMPORT_REQIF2TOPCASED;
	public static String ImportRequirementWizard_REQIF_TOPCASED;
	public static String LEADHEADERCOLUMN;
	public static String REQ_EXT;
	public static String REQIF_EXT;
	public static String REQIF_FILE;
	public static String REQIF_FILE_ERROR;
	public static String REQIF2TOPCASED_REQ;
	public static String TOPCASED_REF_FILE_ERROR;
	public static String EXPORT_UPSTREAM;
	public static String GEN_PROR_EXT;
	public static String REQIF_OUTPUT_FILE;
	public static String TOPCASED_FILE_ERROR;
	public static String TOPCASED_INPUT_FILE;
	public static String TOPCASED2REQIF;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
