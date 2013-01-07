/*****************************************************************************
 * Copyright (c) 2013 Atos.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Refactoring the initial API implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.checker;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Property;
import org.topcased.doc2model.documents.Checker;
import org.topcased.doc2model.documents.ParsingProcess;
import org.topcased.doc2model.parser.Activator;
import org.topcased.requirement.document.elements.Attribute;

public abstract class AbstractDescriptionChecker implements Checker
{

    protected static String END_TEXT;

    protected static String REQ_IDENT;

    protected static String ARG_END_TEXT = "endText";

    protected static String ARG_REQ_IDENT = "reqIdent";

    protected static Pattern pattern = Pattern.compile(".*" + END_TEXT + ".*");

    protected static Pattern req = Pattern.compile("E_.*[^\\s]");

    protected static Pattern regDescription = Pattern.compile("");
    
    protected static Attribute descriptionAttribute;

    protected static boolean flagReqInit = false;
    
    protected ParsingProcess process;

    protected boolean flagIdent = false;

    protected String lastDescription;

    protected String lastReqId;

    protected Map<String, String> idDescription = new HashMap<String, String>();

    StringBuffer buffer;

    public static void setStereotypeAttribute(Attribute attribute)
    {
        descriptionAttribute = attribute;
    }
    
    public static void setEndText(String endText)
    {
        pattern = Pattern.compile(".*" + endText + ".*");
    }

    public static void setReqIdent(String regexIdent)
    {
        req = Pattern.compile(regexIdent);
        flagReqInit = true;
    }

    public static void setStyleIdent(String style)
    {
        REQ_IDENT = style;
    }

    public static void rollback()
    {
        init();
    }

    static
    {
        init();
    }

    /**
     * 
     */
    protected static void init()
    {
        END_TEXT = "#EndText";
        REQ_IDENT = "REQ_Identif";
        Pattern pEndText = Pattern.compile("-" + ARG_END_TEXT + "=(.*)");
        Pattern pIdent = Pattern.compile("-" + ARG_REQ_IDENT + "=(.*)");
        String[] args = Platform.getApplicationArgs();
        for (String s : args)
        {
            Matcher matcher = pIdent.matcher(s);
            if (matcher.matches() && matcher.groupCount() > 0)
            {
                REQ_IDENT = matcher.group(1);
            }
            Matcher matcher2 = pEndText.matcher(s);
            if (matcher2.matches() && matcher2.groupCount() > 0)
            {
                END_TEXT = matcher2.group(1);
            }
        }
        pattern = Pattern.compile(".*" + END_TEXT + ".*");
        regDescription = Pattern.compile("");
    }

    public void initContext(ParsingProcess process)
    {
        this.process = process;
    }

    public void check(String string)
    {
        Matcher m = req.matcher(string);
        boolean regMatche = m.matches();
        boolean styleMatche = REQ_IDENT.equals(process.getCurrentStyle());
        boolean flagIdOrDescription = false;

        if (regMatche || styleMatche)
        {
            if (regMatche && !string.equals(""))
            {
                buffer = new StringBuffer();
                try
                {
                    if (m.groupCount() == 0)
                    {
                        lastReqId = m.group(0);
                    }
                    else
                    {
                        lastReqId = m.group(1);
                    }
                    Matcher desc = regDescription.matcher(string);
                    flagIdOrDescription = true;
                    if (desc.matches())
                    {
                        if (desc.groupCount() == 0)
                        {
                            lastDescription = desc.group(0);
                        }
                        else
                        {
                            lastDescription = desc.group(1);
                        }
                        idDescription.put(lastReqId, lastDescription);
                    }
                }
                catch (Exception e)
                {
                }
            }
            if (styleMatche && !flagReqInit)
            {
                String requirementId = process.getTextCorrespondingToCurrentStyle();
                if (requirementId != null)
                {
                    lastReqId = requirementId;
                    buffer = new StringBuffer();
                }

            }
            flagIdent = true;
        }

        if (flagIdent)
        {
            addDescription();
        }
        // if its an id or a description, its useless to buffer it
        if (buffer != null && !flagIdOrDescription)
        {
            bufferDescription(string);
        }

    }

    /**
     * Sets the regex expression
     * 
     * @param regex the regex expression
     */
    public static void setRegDescription(String regex)
    {
    	regDescription = Pattern.compile(regex);
    }
    
    
    abstract protected void bufferDescription(String string);

    abstract protected void addDescription();

}
