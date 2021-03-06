/*****************************************************************************
 * Copyright (c) 2013 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Ansgar Radermacher  ansgar.radermacher@cea.fr
 *
 *****************************************************************************/

package org.eclipse.papyrus.designer.transformation.base.utils;

/**
 * Main exception class that is used to represent errors during model transformations
 */
public class TransformationException extends Exception {

	public TransformationException(String reason) {
		super(reason);
	}

	// String m_reason;
	final static long serialVersionUID = 1234;
}
