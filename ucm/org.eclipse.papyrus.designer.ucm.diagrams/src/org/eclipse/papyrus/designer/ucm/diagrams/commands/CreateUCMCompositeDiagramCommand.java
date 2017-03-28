/*****************************************************************************
 * Copyright (c) 2017 CEA LIST and Thales
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

package org.eclipse.papyrus.designer.ucm.diagrams.commands;

import org.eclipse.papyrus.uml.diagram.composite.CreateCompositeDiagramCommand;

/**
 * Create UCM composite diagram command
 */
public class CreateUCMCompositeDiagramCommand extends CreateCompositeDiagramCommand {

	/**
	 * UCM Composite diagram type.
	 */
	public static final String DIAGRAM_TYPE = "UCMCompositeDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.infra.gmfdiag.common.AbstractPapyrusGmfCreateDiagramCommandHandler#getCreatedDiagramType()
	 *
	 * @return
	 */
	@Override
	public String getCreatedDiagramType() {
		return DIAGRAM_TYPE;
	}
}
