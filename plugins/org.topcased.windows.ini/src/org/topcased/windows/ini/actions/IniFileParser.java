package org.topcased.windows.ini.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

public class IniFileParser
{

    private Ini ini;
    
    public IniFileParser(IFile resource)
    {
        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(resource.getContents()));
            this.ini = new Wini(reader);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }

    }
    
    public String[] getTypes()
    {
        if (ini!=null)
        {
            Section section = ini.get("Types");
            if (section != null)
            {
                String arrayResult = (String) section.values().toArray()[0];
                String[] result = arrayResult.split(",");
                return result; 
            }
        }
        return null;
    }
    
    public Section getElements(String type){
        if (ini != null)
        {
            return ini.get(type);
        }
        return null;
    }

}
