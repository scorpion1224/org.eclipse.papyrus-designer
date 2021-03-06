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

package org.eclipse.papyrus.designer.components.transformation.listeners;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.papyrus.designer.components.transformation.sync.CompImplSync;
import org.eclipse.papyrus.infra.core.listenerservice.IPapyrusListener;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;

public class ParameterListener implements IPapyrusListener {

	public static ParameterListener getInstance() {
		if (instance == null) {
			instance = new ParameterListener();
		}
		return instance;
	}

	private static ParameterListener instance = null;

	@Override
	public void notifyChanged(Notification notification) {
		int evtType = notification.getEventType();
		Object notifier = notification.getNotifier();
		if ((evtType == Notification.SET) || (evtType == Notification.ADD)) {
			if (notifier instanceof Parameter) {
				Element owner = ((Parameter) notifier).getOwner();
				if (owner instanceof Operation) {
					owner = owner.getOwner();
					if (owner instanceof Interface) {
						CompImplSync.syncViaInterface((Interface) owner);
					}
				}
			}
		}
	}
}
