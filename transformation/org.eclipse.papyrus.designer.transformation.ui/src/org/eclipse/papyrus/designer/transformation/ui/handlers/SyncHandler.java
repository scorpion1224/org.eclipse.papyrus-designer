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

package org.eclipse.papyrus.designer.transformation.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.papyrus.designer.transformation.base.utils.CommandSupport;
import org.eclipse.papyrus.designer.transformation.base.utils.RunnableWithResult;
import org.eclipse.papyrus.designer.transformation.base.utils.TransformationRTException;
import org.eclipse.papyrus.designer.transformation.core.sync.DepPlanSync;
import org.eclipse.papyrus.designer.transformation.core.sync.InterfaceSync;
import org.eclipse.papyrus.uml.diagram.common.handlers.CmdHandler;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

/**
 * Handler for synchronizing derived elements. Will do different
 * things, depending on the currently selected object.
 */
public class SyncHandler extends CmdHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		updateSelectedEObject();
		// if a property is selected, use the associated type
		if (selectedEObject instanceof Property) {
			selectedEObject = ((Property) selectedEObject).getType();
		}
		
		if (selectedEObject instanceof Interface) {
			return true;
		}
		else if (selectedEObject instanceof Package) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// if a property is selected, use the associated type
		if (selectedEObject instanceof Property) {
			selectedEObject = ((Property) selectedEObject).getType();
		}

		if (selectedEObject instanceof Interface) {
			final Interface selectedIntf = (Interface) selectedEObject;
			CommandSupport.exec("Synchronize interface", event, new Runnable() {

				@Override
				public void run() {
					InterfaceSync.syncSignalReceptionSupport(selectedIntf);
				}
			});
		}
		
		if (selectedEObject instanceof Package) {
			final Package selectedPkg = (Package) selectedEObject;
			CommandSupport.exec("Synchronize deployment plan", event, new RunnableWithResult() {

				@Override
				public CommandResult run() {
					try {
						DepPlanSync.syncDepPlan(selectedPkg);
						return CommandResult.newOKCommandResult();
					}
					catch (TransformationRTException e) {
						Shell shell = Display.getDefault().getActiveShell();
						MessageDialog.openError(shell, "Can not synchronize deployment plan", e.getMessage());
						return CommandResult.newErrorCommandResult(e.getMessage());
					}
				}
			});
		}
		return null;
	}
}
