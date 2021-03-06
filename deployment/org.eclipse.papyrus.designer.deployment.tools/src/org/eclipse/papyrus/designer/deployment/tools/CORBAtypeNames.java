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

package org.eclipse.papyrus.designer.deployment.tools;

/**
 * A collection of numerical CORBA type names. Used to determine whether a
 * numerical value specification (LiteralInteger) should be used during deployment
 * plan creation. 
 */
public class CORBAtypeNames {
	public static final String Octet = "corba::octet"; //$NON-NLS-1$
	public static final String Long = "corba::long"; //$NON-NLS-1$
	public static final String UnsignedLong = "corba::unsigned long"; //$NON-NLS-1$
	public static final String Short = "corba::short"; //$NON-NLS-1$
	public static final String UnsignedShort = "corba::unsigned short"; //$NON-NLS-1$
}
