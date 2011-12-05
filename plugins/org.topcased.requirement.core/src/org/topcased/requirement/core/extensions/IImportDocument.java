package org.topcased.requirement.core.extensions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.topcased.typesmodel.model.inittypes.DocumentType;

public interface IImportDocument
{
    void getDocument(DocumentType type, URI document, URI outputModel, IProgressMonitor monitor);
    
}
