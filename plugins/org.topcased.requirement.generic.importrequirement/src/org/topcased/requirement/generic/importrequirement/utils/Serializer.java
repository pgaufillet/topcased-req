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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * The Class Serializer.
 */
public class Serializer<T>
{

    /**
     * Un serialize.
     * 
     * @param stringToUnSerialize the string to un serialize
     * 
     * @return the t
     */
    public T unSerialize(String stringToUnSerialize)
    {
        if (stringToUnSerialize != null && stringToUnSerialize.length() > 0)
        {
            String myParam = "";
            try
            {
                myParam = URLDecoder.decode(stringToUnSerialize, "UTF-8");
            }
            catch (UnsupportedEncodingException e1)
            {
                e1.printStackTrace();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(myParam.getBytes());
            try
            {
                ObjectInputStream obj_in = new ObjectInputStream(bis);
                return (T) obj_in.readObject();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (ClassCastException e)
            {
                // cast problem we return null
            }
        }
        return null;

    }

    /**
     * Serialize.
     * 
     * @param objToSerialze the obj to serialze
     * 
     * @return the string
     */
    public String serialize(T objToSerialze)
    {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        ObjectOutputStream objstream;
        String encoded = "";
        try
        {
            objstream = new ObjectOutputStream(writer);
            try
            {
                objstream.writeObject(objToSerialze);
                encoded = writer.toString();
                encoded = URLEncoder.encode(encoded, "UTF-8");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    objstream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return encoded;
    }
}
