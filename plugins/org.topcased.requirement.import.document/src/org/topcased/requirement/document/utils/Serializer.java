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
package org.topcased.requirement.document.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
            myParam = stringToUnSerialize;
            byte[] bytes = getBytes(myParam);
            if (bytes == null)
            {
                return null;
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
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

    private byte[] getBytes(String myParam)
    {
        byte[] bytes = null;
        try
        {
            String[] strings = myParam.split(",");
            bytes = new byte[strings.length];
            for (int i = 0; i < strings.length; i++)
            {
                bytes[i] = Byte.parseByte(strings[i]);
            }
        }
        catch (NumberFormatException e)
        {
        }
        return bytes;
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
                byte[] bytes = writer.toByteArray();
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < bytes.length; i++)
                {
                    if (i != 0)
                    {
                        buffer.append(",");
                    }
                    buffer.append(bytes[i]);
                }
                encoded = buffer.toString();
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
