package org.topcased.requirement.core.utils;

import org.eclipse.core.runtime.IStatus;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.core.internal.RequirementCorePlugin;

import ttm.Document;

/**
 * Get container object to do backup for Document in hierarchy
 *
 */
public interface ContainerAssigner
{
    /**
     * Add Document to container
     */
    void backup();
    
    /**
     * A factory for create container assigner objects
     *
     */
    public class ContainerAssignerFactory
    {
        public ContainerAssigner create(final Document doc)
        {
            if (doc.eContainer() instanceof UpstreamModel)
            {
                return new UpstreamModelContainerAssigner(doc);
            }
            else if (doc.eContainer() instanceof Document)
            {
                return new DocumentContainerAssigner(doc);
            }
            return new ContainerAssigner()
            {
                public void backup()
                {
                    // DO NOTHING
                    RequirementCorePlugin.log("No container assigner found for doc " + doc.getIdent() + " the container is : " + doc.eContainer(), IStatus.ERROR, null);
                }
            };
        }
    }

    public abstract class AbstractContainerAssigner implements ContainerAssigner
    {
        private final Document doc;

        public AbstractContainerAssigner(Document doc)
        {
            this.doc = doc;
        }

        public Document getDocument()
        {
            return doc;
        }

    }

    /**
     * This class do a backup for a document to a Document container
     *
     */
    public class DocumentContainerAssigner extends AbstractContainerAssigner
    {

        private Document container;

        private int index;

        public DocumentContainerAssigner(Document doc)
        {
            super(doc);
            container = (Document) doc.eContainer();
            index = container.getChildren().indexOf(doc);
        }

        public void backup()
        {
            container.getChildren().add(index, getDocument());
        }

    }

    /**
     * This class do a backup for a Document to an upstream model container
     *
     */
    public class UpstreamModelContainerAssigner extends AbstractContainerAssigner
    {

        private UpstreamModel container;

        private int index;

        public UpstreamModelContainerAssigner(Document doc)
        {
            super(doc);
            container = (UpstreamModel) doc.eContainer();
            index = container.getDocuments().indexOf(doc);
        }

        public void backup()
        {
            container.getDocuments().add(index, getDocument());
        }

    }
}