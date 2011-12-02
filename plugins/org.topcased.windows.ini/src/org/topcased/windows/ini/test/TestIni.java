package org.topcased.windows.ini.test;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.junit.Test;
import org.topcased.windows.ini.Activator;

public class TestIni
{

    @Test
    public void test()
    {
        Map<String, String> names = new HashMap<String, String>();
        Map<String, String> regex = new HashMap<String, String>();
        
        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(Activator.getDefault().getBundle().getResource("/test/IVP.types").openStream()));
            Ini ini = new Ini(reader);
            Section types = ini.get("Types");
            assertNotNull(types);
            
            Section sd = ini.get("SD");
            assertNotNull(sd);
            
            for (Entry<String, String> entryset : sd.entrySet())
            {
                if (entryset.getKey().matches("[a-zA-Z]*[0-9]Name"))
                {
                    names.put(entryset.getKey(), entryset.getValue());
                } else if (entryset.getKey().matches("[a-zA-Z]*[0-9]"))
                {
                    regex.put(entryset.getKey(), entryset.getValue());
                }
            }
            
            sd.get("Variable1Name");
            
            System.out.println(names);
            
            Ini saver = new Ini();
            Section section = saver.add("Types");
            boolean bool = true;
            section.add("Names", "save");
            section.add("bool", bool);
            File file = new File("C:\\Documents and Settings\\aradouan\\Bureau\\iniStore.types");
            saver.store(file);
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
