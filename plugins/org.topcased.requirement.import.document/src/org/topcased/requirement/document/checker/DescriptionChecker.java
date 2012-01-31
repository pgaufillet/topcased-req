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
 *  Anass RADOUANI (Atos) anass.radouani@atos.net - Refactoring the initial API implementation
 *
 *****************************************************************************/
package org.topcased.requirement.document.checker;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.topcased.doc2model.documents.Checker;
import org.topcased.doc2model.documents.ParsingProcess;
import ttm.Text;
import ttm.TtmFactory;

public class DescriptionChecker implements Checker
{

    private static String END_TEXT;

    private static String REQ_IDENT;

    private static String ARG_END_TEXT = "endText";

    private static String ARG_REQ_IDENT = "reqIdent";

    private static Pattern pattern = Pattern.compile(".*" + END_TEXT + ".*");

    private static Pattern req = Pattern.compile("E_.*[^\\s]");

    private static Pattern regDescription = Pattern.compile("");

    private static boolean flagReqInit = false;

    private ParsingProcess process;

    boolean flagIdent = false;

    private String lastDescription;

    private String lastReqId;

    private Map<String, String> idDescription = new HashMap<String, String>();

    StringBuffer buffer;

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
    private static void init()
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

    private void bufferDescription(String string)
    {
        if (pattern.matcher(string).matches())
        {
            EObject eo = process.getLatestCreatedElement();
            if (eo instanceof ttm.Requirement && !buffer.toString().equals(""))
            {
                ttm.Requirement req = (ttm.Requirement) eo;
                Text createText = TtmFactory.eINSTANCE.createText();
                createText.setValue(buffer.toString());
                req.getTexts().add(createText);
                buffer = null;
            }
            flagIdent = false;
        }
        else if (string.length() > 0 && buffer != null)
        {
            buffer.append(string);
            buffer.append("\n");
        }
    }

    private void addDescription()
    {
        EObject eobject = process.getLatestCreatedElement();
        if (eobject instanceof ttm.Requirement)
        {
            ttm.Requirement req = (ttm.Requirement) eobject;
            // if the requirement has a description: there is an entry in the map idDescription
            // if the requirement contains already the description, it's not added a second time
            if (idDescription.containsKey(req.getIdent()) && !containsText(req, idDescription.get(req.getIdent())))
            {
                Text createText = TtmFactory.eINSTANCE.createText();
                createText.setValue(idDescription.get(req.getIdent()));
                req.getTexts().add(createText);
            }
            // tries to add a description if the requirement doesn't have one and the regex description matches
            // ( this is for style match)
            else if (req.getTexts() != null && req.getTexts().isEmpty())
            {
                Matcher desc = regDescription.matcher(req.getIdent());
                if (desc.matches())
                {
                    try
                    {
                        if (desc.groupCount() == 0)
                        {
                            lastDescription = desc.group(0);
                        }
                        else
                        {
                            lastDescription = desc.group(1);
                        }
                        idDescription.put(req.getIdent(), lastDescription);
                        Text createText = TtmFactory.eINSTANCE.createText();
                        createText.setValue(idDescription.get(req.getIdent()));
                        req.getTexts().add(createText);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            flagIdent = false;
        }
    }

    private boolean containsText(ttm.Requirement requirement, String text)
    {
        for (Text reqText : requirement.getTexts())
        {
            if (reqText.getValue().equals(text))
            {
                return true;
            }
        }
        return false;
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

}
