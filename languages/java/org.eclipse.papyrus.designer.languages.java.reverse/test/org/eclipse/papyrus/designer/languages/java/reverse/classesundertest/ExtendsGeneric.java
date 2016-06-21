/*****************************************************************************
 * Copyright (c) 2016 CEA LIST and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *   
 *****************************************************************************/

package org.eclipse.papyrus.designer.languages.java.reverse.classesundertest;

import java.util.ArrayList;

/**
 * @author dumoulin
 *
 */
public class ExtendsGeneric extends ArrayList<String> {

	/**
	 * Constructor.
	 *
	 */
	public ExtendsGeneric() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Extends with generic
	 * @author dumoulin
	 *
	 * @param <T>
	 */
	class A<T> extends ArrayList<T> {
		
	}
}
