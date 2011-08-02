/*****************************************************************************
 * Copyright (c) 2011 Atos
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors : Tristan FAURE (Atos) - initial API and implementation
 * 
 *****************************************************************************/
package org.topcased.requirement.core.extensions;

/**
 * An interface for classes providing type factories useful for requirement behavior
 * @author tfaure
 *
 */
public interface IRequirementFactoryProvider {
	/**
	 * Create an instance of the desired type
	 * @param type
	 * @return
	 */
	<T> T create (Class<T> type) ;
	
	
	/**
	 * Return true if the implementation can create the type
	 * @param type
	 * @return
	 */
	boolean provides (Class<?> type);
}
