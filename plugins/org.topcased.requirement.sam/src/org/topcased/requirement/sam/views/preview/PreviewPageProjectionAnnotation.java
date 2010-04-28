/*******************************************************************************
 * Copyright (c) 2008 TOPCASED. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Topcased contributors and others - initial API and implementation
 *******************************************************************************/
package org.topcased.requirement.sam.views.preview;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

/**
 * Defines new ProjectionAnnotation without ruler button. Used to hide a part of a text without buttons in the vertical
 * ruler <br>
 * Creation : 6 nov. 2008
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 */
public class PreviewPageProjectionAnnotation extends ProjectionAnnotation
{

    /**
     * defines PreviewPageProjectionAnnotation constructor
     * 
     * @param isCollapsed
     */
    public PreviewPageProjectionAnnotation(boolean isCollapsed)
    {
        super(isCollapsed);
    }

    /**
     * Override the paint method and do not paint to have projection without buttons in the vertical ruler
     * 
     * @see org.eclipse.jface.text.source.projection.ProjectionAnnotation#paint(org.eclipse.swt.graphics.GC,
     *      org.eclipse.swt.widgets.Canvas, org.eclipse.swt.graphics.Rectangle)
     */
    @Override
    public void paint(GC gc, Canvas canvas, Rectangle rectangle)
    {
        // Do not paint
        // super.paint(gc, canvas, rectangle);
    }

}
