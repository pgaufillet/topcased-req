package org.topcased.requirement.core.extensions;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.topcased.facilities.extensions.AbstractExtensionManager;
import org.topcased.requirement.core.RequirementCorePlugin;

public class ImportDocumentManager extends AbstractExtensionManager
{
    /** constant representing the name of the extension point */
    private static final String IMPORT_DOCUMENT_EXTENSION_POINT ="importDocument";
    
    /** Value of the extension point attribute corresponding to the value given to the file extension. */
    static final String IMPORT_EXTENSION = "extension";
    
    /** Value of the extension point attribute corresponding to the class. */
    static final String IMPORT_CLASS = "class";
    
    /** the shared instance */
    private static ImportDocumentManager manager;
    
    /** Map of the graphical model file extension patterns and the document importer for it */
    public Set<IImportDocument> setImporter;
    
    
    /**
     * Private constructor
     */
    private ImportDocumentManager()
    {
        super(RequirementCorePlugin.getId() + "." + IMPORT_DOCUMENT_EXTENSION_POINT);
        setImporter = new HashSet<IImportDocument>();
        readRegistry();
    }

    /**
     * Gets the shared instance.
     * 
     * @return the drop restriction manager
     */
    public static ImportDocumentManager getInstance()
    {
        if (manager == null)
        {
            manager = new ImportDocumentManager();
        }
        return manager;
    }
    
    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#addExtension(org.eclipse.core.runtime.IExtension)
     */   
    @Override
    protected void addExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            try
            {
                IImportDocument importer = (IImportDocument) confElt.createExecutableExtension(IMPORT_CLASS);
                setImporter.add(importer);
            }
            catch (CoreException e)
            {
                RequirementCorePlugin.log(e);
            }
        }
        
    }

    /**
     * @see org.topcased.facilities.extensions.AbstractExtensionManager#removeExtension(org.eclipse.core.runtime.IExtension)
     */
    @Override
    protected void removeExtension(IExtension extension)
    {
        IConfigurationElement[] elements = extension.getConfigurationElements();
        for (IConfigurationElement confElt : elements)
        {
            // remove attachment policy with pattern
            String elt = confElt.getAttribute(IMPORT_EXTENSION);
            setImporter.remove(elt);
        }
    }
    
    /**
     * This method return the first document importer
     * 
     * @return the first document importer
     */
    public IImportDocument getImporter()
    {
        if (!setImporter.isEmpty())
        {
            return setImporter.iterator().next();
         }
        return null;
    }

    
}
