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

    private static String END_TEXT ;
    private static String REQ_IDENT ;
    private static String ARG_END_TEXT = "endText" ;
    private static String ARG_REQ_IDENT = "reqIdent" ;
    private static Pattern pattern = Pattern.compile(".*" + END_TEXT + ".*");
    private static Pattern req = Pattern.compile("E_.*[^\\s]");
    private ParsingProcess process;
    boolean flagIdent = false ;
    StringBuffer buffer ;

    public static void setEndText (String endText)
    {
        pattern = Pattern.compile(".*" + endText + ".*");
    }
    
    public static void setReqIdent (String regexIdent)
    {
        req = Pattern.compile(regexIdent);
    }
    
    public static void setStyleIdent (String style)
    {
        REQ_IDENT = style ;
    }
    
    public static void rollback ()
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
    	REQ_IDENT = "REQ_Identif" ;
    	Pattern pEndText = Pattern.compile("-"+ARG_END_TEXT+"=(.*)");
    	Pattern pIdent = Pattern.compile("-"+ARG_REQ_IDENT+"=(.*)");
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
    }
    
    public void initContext(ParsingProcess process)
    {
        this.process = process;
    }

    public void check(String string)
    {
        if (REQ_IDENT.equals(process.getCurrentStyle())
                || req.matcher(string).matches())
        {
            flagIdent = true ;
            buffer = new  StringBuffer();
        }
        else
        {
            // identification of the requirement is finished
            if (flagIdent)
            {
                if (string.length() > 0)
                {
                    if (pattern.matcher(string).matches())
                    {
                        EObject eobject = process.getLatestCreatedElement();
                        if (eobject instanceof ttm.Requirement)
                        {
                            ttm.Requirement req = (ttm.Requirement) eobject;
                            Text createText = TtmFactory.eINSTANCE.createText();
                            createText.setValue(buffer.toString());
                            req.getTexts().add(createText);
                        }
                        flagIdent = false ;
                    }
                    else
                    {
                        buffer.append(string);
                        buffer.append("\n");
                    }
                }
            }
        }
    }

}
