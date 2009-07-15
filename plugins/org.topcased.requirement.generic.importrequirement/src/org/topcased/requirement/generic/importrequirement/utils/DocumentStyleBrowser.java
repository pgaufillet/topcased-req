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
package org.topcased.requirement.generic.importrequirement.utils;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.topcased.document.parser.Activator;
import org.topcased.document.parser.documents.Document;
import org.topcased.document.parser.documents.DocumentFactory;
import org.topcased.document.parser.documents.Document.PROPERTY;

/**
 * The Class DocumentStyleBrowser.
 */
public class DocumentStyleBrowser
{

    /** The map. */
    private Map<File, SortedSet<String>> map = new HashMap<File, SortedSet<String>>();

    /**
     * Instantiates a new document style browser.
     */
    public DocumentStyleBrowser()
    {
    }

    /**
     * Gets the all styles.
     * 
     * @param f the f
     * 
     * @return the all styles
     * 
     * @throws IllegalArgumentException the illegal argument exception
     */
    public SortedSet<String> getAllStyles(File f) throws IllegalArgumentException
    {
        SortedSet<String> result = map.get(f);
        if (result == null)
        {
            result = new TreeSet<String>(new Comparator<String>()
            {
                public int compare(String o1, String o2)
                {
                    if (o1 != null)
                    {
                        return o1.compareTo(o2);
                    }
                    return -1;
                }
            });
            DocumentFactory factory = Activator.getFactoryFromExtension(f.getAbsolutePath());
            if (factory == null)
            {
                throw new IllegalArgumentException("your input file is not a valid document");
            }
            Document doc = factory.loadDocument(f);
            fillSet(doc, result);
            map.put(f, result);
        }
        return result;
    }

    /**
     * Fill set.
     * 
     * @param doc the doc
     * @param result the result
     */
    private void fillSet(Document doc, SortedSet<String> result)
    {
        boolean ok = doc.next();
        while (ok)
        {
            String style = (String) doc.get(PROPERTY.style);
            if (style != null && style.length() > 0)
            {
                result.add(style);
            }
            ok = doc.next();
        }
    }

    // public static void test()
    // {
    // String path =
    // "D:/TFE/TRV/Topcased-Ganymede/runtime-EclipseApplication/090414TestDoc2Model/Models/BBT-BRD-0-0002-AO.docx";
    // File f = new File(path);
    // if (f.exists())
    // {
    // SortedSet<String> set = DocumentStyleBrowser.INSTANCE.getAllStyles(f);
    // for (String s : set)
    // {
    // System.out.println(s);
    // }
    // }
    // }

}
