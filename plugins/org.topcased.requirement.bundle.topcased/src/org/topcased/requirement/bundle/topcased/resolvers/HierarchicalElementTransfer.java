/***********************************************************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastien GABEL (CS) - initial API and implementation
 * 
 **********************************************************************************************************************/
package org.topcased.requirement.bundle.topcased.resolvers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class allows to keep trace of selection source for generic actions cut, copy and paste proposed in the
 * contextual menu of the Topcased modeler. When a Copy or Cut is performed, the hierarchical elements to duplicate are
 * set in this transfer area. When the paste action is invoked, this area is accessed.<br>
 * Mainly, this singleton will be used in a Topcased Modeler context and more accurately when actions on requirements
 * will be necessary.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @since Topcased 2.3.0
 * 
 */
public final class HierarchicalElementTransfer
{
    /** singleton instance */
    public static final HierarchicalElementTransfer INSTANCE = new HierarchicalElementTransfer();

    /** A collection of hierarchical elements */
    private Collection<Object> clipboard;

    /**
     * Constructor
     */
    private HierarchicalElementTransfer()
    {
        clipboard = new ArrayList<Object>();
    }

    /**
     * Sets the result of a Cut action
     * 
     * @param clipboardResult The result after a Cut or Copy action
     */
    public void setResult(Collection< ? > clipboardResult)
    {
        clipboard.addAll(clipboardResult);
    }

    /**
     * 
     * Gets the result list fill in after a cut action
     * 
     * @return the result of cut hierarchical elements
     */
    public Collection< ? > getResult()
    {
        return clipboard;
    }

    /**
     * 
     * Clears the content of this transfer.
     */
    public void clear()
    {
        clipboard.clear();
    }
}
