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

import org.eclipse.emf.ecore.EObject;

import ttm.Text;
import ttm.TtmFactory;

public class RequirementDescriptionChecker extends AbstractDescriptionChecker
{
    
    protected void bufferDescription(String string)
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

    protected void addDescription()
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

    protected boolean containsText(ttm.Requirement requirement, String text)
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

}
