/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.papyrus.designer.transformation.core.transformations;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.uml2.uml.Element;

/**
 * This class enables the copy from a source to a given target element.
 * It assumes that a copy has already been created, but will just not copy it...
 * Unlike the @see Copier class, the element might be copied to a different place
 */
public class CopyTo {
	/**
	 * Copy an element from the source to a target
	 *
	 * @param source
	 *            The element that should be copied
	 * @param targetContainer
	 *            The container into which it the source should be copied
	 * @return the copied object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Element> T copyTo(T source, Element targetContainer) {
		Copier copier = new Copier();
		EObject sourceContainer = source.eContainer();
		if (sourceContainer != null) {
			copier.put(sourceContainer, targetContainer);
		}

		EObject copy = copier.copy(source);
		copier.copyAll(source.getStereotypeApplications());
		copier.copyReferences();
		return (T) copy;
	}
}
