/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.papyrus.designer.languages.cpp.codegen.transformation;

import org.eclipse.papyrus.designer.languages.common.base.HierarchyLocationStrategy;
import org.eclipse.papyrus.infra.tools.file.IPFileSystemAccess;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;


public class CppLocationStrategy extends HierarchyLocationStrategy {
	
	public static final String PKG_PREFIX = "Pkg_"; //$NON-NLS-1$

	/**
	 * Return the filename for a given named element.
	 *
	 * @param element
	 *            a named element
	 * @return filename for this element
	 */
	public String getFileName(NamedElement element) {
		if (element instanceof Package) {
			// specific prefix for packages
			String folder = super.getFileName(element);
			if (folder.length() > 0) {
				folder += IPFileSystemAccess.SEP_CHAR;
			}
			return folder + PKG_PREFIX + element.getName();
		}
		else return super.getFileName(element);
	}
}
