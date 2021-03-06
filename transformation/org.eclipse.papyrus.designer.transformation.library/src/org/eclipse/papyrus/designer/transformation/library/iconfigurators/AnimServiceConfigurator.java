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

package org.eclipse.papyrus.designer.transformation.library.iconfigurators;

import org.eclipse.emf.common.util.EList;
import org.eclipse.papyrus.designer.deployment.tools.AllocUtils;
import org.eclipse.papyrus.designer.transformation.extensions.IInstanceConfigurator;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;

/**
 * Configurator for the Eclipse animation server. This instance is systematically
 * allocated to a node named "Eclipse" call event (for a state machine): it sets the
 * portID attribute of the call event interceptor. The interceptor uses this
 * attribute to initialize the portID attribute within the produced CallEvent
 * data structure.
 *
 * @author ansgar
 *
 */
public class AnimServiceConfigurator implements IInstanceConfigurator {

	public final static String eclipseAnimService = "Eclipse"; //$NON-NLS-1$

	/**
	 * Configure the instance of the animation service.
	 *
	 * @see org.eclipse.papyrus.designer.transformation.extensions.transformation.core.extensions.IInstanceConfigurator#configureInstance(org.eclipse.uml2.uml.InstanceSpecification, org.eclipse.uml2.uml.InstanceSpecification, org.eclipse.uml2.uml.Port)
	 *
	 * @param instance
	 *            the instance that should be configured
	 * @param componentPart
	 *            the part representing this instance
	 * @param parentInstance
	 *            The instance specification for the parent (container). If null, no configuration is done (may indicates that the call is done from a lightweight container) 
	 */
	@Override
	public void configureInstance(InstanceSpecification instance, Property componentPart, InstanceSpecification parentInstance)
	{
		if (parentInstance != null) {
			EList<InstanceSpecification> nodes = AllocUtils.getAllNodesOrThreadsParent(parentInstance);
			if (nodes.size() > 0) {
				InstanceSpecification node = nodes.get(0);
				// problem: instance specification is within intermediate model, thus incomplete.
				// option: explicitly pre-create singletons (and allocate these?)
				NamedElement animService = node.getNearestPackage().getMember(eclipseAnimService);
				if (animService instanceof InstanceSpecification) {
					AllocUtils.allocate(instance, (InstanceSpecification) animService);
					return;
				}
			}
		}
		// throw new TransformationRTException(String.format("Cannot find node <%s> in platform definition", eclipseAnimService));
	}

}
