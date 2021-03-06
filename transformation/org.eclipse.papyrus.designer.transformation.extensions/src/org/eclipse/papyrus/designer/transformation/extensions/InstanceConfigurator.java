/*******************************************************************************
 * Copyright (c) 2011 CEA LIST.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     CEA LIST - initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.transformation.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.papyrus.designer.deployment.profile.Deployment.UseInstanceConfigurator;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Property;


/**
 * Support for the configuration of instances via the Eclipse extension mechanism
 */
public class InstanceConfigurator {

	public static final String IINSTANCE_CONFIG_ID = Activator.PLUGIN_ID + ".instanceConfig"; //$NON-NLS-1$

	public static boolean onNodeModel = false;

	/**
	 * Configure an instance with a given configurator
	 *
	 * @param useInstanceConfigurator
	 * @param instance
	 * @param componentPart
	 * @param containerContext
	 */
	public static void configureInstance(UseInstanceConfigurator useInstanceConfigurator, InstanceSpecification instance, Property componentPart, InstanceSpecification parentInstance) {
		if (useInstanceConfigurator != null) {
			org.eclipse.papyrus.designer.deployment.profile.Deployment.InstanceConfigurator instanceConfigurator =
					useInstanceConfigurator.getConfigurator();
			if (instanceConfigurator.isOnNodeModel() == onNodeModel) {
				if (instanceConfigurator != null) {
					String id = instanceConfigurator.getBase_Class().getName();
					IInstanceConfigurator iConfigurator = getInstanceConfigurator(id);
					if (iConfigurator != null) {
						iConfigurator.configureInstance(instance, componentPart, parentInstance);

					}
				}
			}
		}
	}

	protected static IInstanceConfigurator getInstanceConfigurator(String iConfiguratorID) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] configElements = reg.getConfigurationElementsFor(IINSTANCE_CONFIG_ID);
		for (IConfigurationElement configElement : configElements) {
			try {
				final String iConfiguratorIDext = configElement.getAttribute("configuratorID"); //$NON-NLS-1$
				if (iConfiguratorIDext == null) {
					throw new RuntimeException(String.format(
							Messages.InstanceConfigurator_InvalidPluginExtension, iConfiguratorID));
				}
				if (iConfiguratorIDext.equals(iConfiguratorID)) {
					// TODO: cache returned instance (avoid creating a new instance each time => more efficient, no need for static attributes)
					final Object obj = configElement.createExecutableExtension("class"); //$NON-NLS-1$
					if (obj instanceof IInstanceConfigurator) {
						return (IInstanceConfigurator) obj;
					}
				}
			} catch (CoreException exception) {
				exception.printStackTrace();
			}
		}
		return null;
	}
}
