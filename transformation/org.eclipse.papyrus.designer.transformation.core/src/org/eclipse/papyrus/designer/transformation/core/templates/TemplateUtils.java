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

package org.eclipse.papyrus.designer.transformation.core.templates;

import java.util.Iterator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.papyrus.designer.deployment.tools.DepUtils;
import org.eclipse.papyrus.designer.transformation.base.utils.CreationUtils;
import org.eclipse.papyrus.designer.transformation.base.utils.TransformationException;
import org.eclipse.papyrus.designer.transformation.core.Activator;
import org.eclipse.papyrus.designer.transformation.core.Messages;
import org.eclipse.papyrus.designer.transformation.core.transformations.LazyCopier;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.TemplateableElement;
import org.eclipse.uml2.uml.Type;

/**
 * A set of template/signature based helper functions.
 *
 */
public class TemplateUtils {

	/**
	 * Get the template signature of a classifier. If the classifier does not
	 * have a signature, examine if the owning class provides one
	 *
	 * @param template
	 *            The potential template
	 * @return the signature or null, if none can be found.
	 */
	public static TemplateSignature getSignature(TemplateableElement template) {
		TemplateSignature signature = getSignatureDirect(template);
		if (signature != null) {
			return signature;
		}

		// no signature found, try signature of owning classifier (nested class)
		// or package
		Element owner = template.getOwner();
		if (owner instanceof TemplateableElement) {
			// owned by a classifier, not a package
			return getSignature((TemplateableElement) owner);
		} else {
			return null;
		}
	}

	public static Element getTemplateOwner(Element ne, TemplateSignature signature) {
		if ((ne instanceof TemplateableElement) && getSignatureDirect((TemplateableElement) ne) == signature) {
			return ne;
		}
		Element owner = ne.getOwner();
		if (owner != null) {
			return getTemplateOwner(owner, signature);
		}
		return null;
	}

	/**
	 * Get the template signature of a templateable element (typically a package). The class must
	 * (1) either own the signature
	 * (2) or merge with a package which owns the signature.
	 * SW Designer enables the "extension" of existing packages via the package merge mechanism
	 *
	 * @param template
	 *            The potential template
	 * @return the signature or null, if none can be found.
	 */

	public static TemplateSignature getSignatureDirect(TemplateableElement template) {
		for (Element element : template.getOwnedElements()) {
			if (element instanceof TemplateSignature) {
				return (TemplateSignature) element;
			}
		}

		// enable multiple package templates sharing the same signature.
		if (template instanceof Package) {
			Package pkg = (Package) template;
			for (PackageMerge pkgImport : pkg.getPackageMerges()) {
				Package importedPkg = pkgImport.getMergedPackage();
				return getSignature(importedPkg);
			}
		}
		return null;
	}

	/**
	 * @return true, if the passed instance reference an element that is within a package template.
	 *         The caller of this function can use the result to decide whether the classifier behind an
	 *         instance should be copied (it should not be done, but rather use template instantiation).
	 *         A cleaner option is to reference an instantiated (bound) copy of the package template, but it
	 *         would require to manage and synchronize template bindings in the user model.
	 * @param instance
	 *            an instance specification
	 */
	public static boolean withinPkgTemplate(InstanceSpecification instance) {
		Classifier cl = DepUtils.getClassifier(instance);
		TemplateSignature ts = TemplateUtils.getSignature(cl);
		if (ts == null) {
			return false;
		}
		else {
			return ts.getOwner() instanceof Package;
		}
	}

	/**
	 * Create a template binding by using a single, fixed actual (used within
	 * SW Designer for the binding of container extensions depending on the component
	 * executor)
	 *
	 * @param model
	 *            : target model in which to create the bound package
	 * @param template
	 * @param fixedActual
	 * @return
	 * @throws TransformationException
	 */
	public static TemplateBinding fixedBinding(Package model, TemplateableElement template, Classifier fixedActual) throws TransformationException {
		// obtain the signature of an element within a package template.

		TemplateSignature signature = getSignature(template);
		if (signature == null) {
			// not a template, retain original name
			return null;
		}

		EList<Type> actuals = new BasicEList<Type>();

		Package pkgTemplate = (Package) signature.getOwner();
		String name = pkgTemplate.getName();

		// loop on template parameters;
		// for (TemplateParameter parameter : signature.getOwnedParameters ()) {
		// ParameterableElement formal = parameter.getParameteredElement();

		// now obtain suitable binding for this parameter - look for ports that
		// are typed with the formal template parameter

		actuals.add(fixedActual);
		name = name + "_" + fixedActual.getName(); //$NON-NLS-1$
		// }

		// the bound package is instantiated in the same model, in which the
		// composite can be found (avoid modifying an imported model).
		// todo: root model as an additional parameter?
		Namespace owner = (Namespace) pkgTemplate.getOwner();
		owner = CreationUtils.getAndCreate(model, owner.allNamespaces());

		Package boundPackage = (Package) owner.getMember(name);
		if (boundPackage == null) {
			// class does not exist yet, needs to be created.
			boundPackage = ((Package) owner).createNestedPackage(name);

			Activator.log.info(String.format(Messages.TemplateUtils_InfoCreateBoundPackage, name, owner.getName()));
		}

		TemplateBinding binding = boundPackage.getTemplateBinding(signature);
		if (binding == null) {
			// binding is not existing yet (should normally only happen, if the class has
			// just been created - but it's better to re-check, even if the bound package
			// was already there)
			binding = boundPackage.createTemplateBinding(signature);

			Iterator<Type> actualsIter = actuals.iterator();

			// loop on template parameters;
			for (TemplateParameter parameter : signature.getOwnedParameters()) {

				TemplateParameterSubstitution substitution = binding.createParameterSubstitution();
				substitution.setFormal(parameter);

				// now obtain suitable binding for this parameter - look for
				// ports that are typed with
				// the formal template parameter
				Type actual = actualsIter.next();
				substitution.setActual(actual);
			}
		}
		return binding;
	}

	/**
	 * create a "sub" binding in which the first parameter of type Cl is assumed
	 * to bind the sub-signature TODO: compared type and name of parameters in
	 * two signatures TODO: support more than one template parameter
	 *
	 * @param model
	 * @param te
	 * @param existingBinding
	 * @return
	 * @throws TransformationException
	 */
	public static TemplateBinding getSubBinding(Package model, TemplateableElement te, TemplateBinding existingBinding) throws TransformationException {

		for (TemplateParameterSubstitution tps : existingBinding.getParameterSubstitutions()) {
			ParameterableElement pe = tps.getActual();
			if (pe instanceof Classifier) {
				return fixedBinding(model, te, (Classifier) pe);
			}
		}
		return null;
	}

	/**
	 * Adapt the actuals within the binding (which correspond either to an
	 * element of the source model or an imported element) to the target model.
	 *
	 * @param sat
	 * @param binding
	 */
	public static void adaptActualsToTargetModel(LazyCopier copy, TemplateBinding binding) {
		for (TemplateParameterSubstitution substitution : binding.getParameterSubstitutions()) {
			substitution.setActual(copy.getCopy(substitution.getActual()));
		}
	}

	/**
	 * Return the actual for a potential formal parameter within a
	 * templateBinding (move to TemplateUtils?)
	 *
	 * @param binding
	 *            a template binding
	 * @param formal
	 *            a potential formal parameter, i.e. a parameter for which we
	 *            check, if it really corresponds to a formal parameter within
	 *            the template binding.
	 * @return the actual parameter that is associated with the potential formal
	 *         parameter, or null if the 2nd parameter does not correspond to a
	 *         formal parameter of the binding.
	 */
	public static Classifier getActualFromBinding(TemplateBinding binding, Type formal) {
		for (TemplateParameterSubstitution substitution : binding.getParameterSubstitutions()) {
			ParameterableElement pe = substitution.getFormal().getParameteredElement();
			if (pe == formal) {
				Activator.log.info(String.format(Messages.TemplateUtils_InfoGetActualFrom, pe));
				return (Classifier) substitution.getActual();
			}
		}
		return null;
	}

	public static Classifier getActualFromBinding(TemplateBinding binding, String formalName) {
		for (TemplateParameterSubstitution substitution : binding.getParameterSubstitutions()) {
			ParameterableElement pe = substitution.getFormal().getParameteredElement();
			if (pe instanceof NamedElement) {
				Activator.log.info(String.format(Messages.TemplateUtils_InfoGetActualFrom, ((NamedElement) pe).getName()));
			}
			if ((pe instanceof NamedElement) && ((NamedElement) pe).getName().equals(formalName)) {
				return (Classifier) substitution.getActual();
			}
		}
		return null;
	}

	/**
	 * Get the first actual from the binding.
	 *
	 * @param binding
	 *            the template binding
	 * @return the first actual.
	 */
	public static Classifier getFirstActualFromBinding(TemplateBinding binding) {
		for (TemplateParameterSubstitution substitution : binding.getParameterSubstitutions()) {
			ParameterableElement pe = substitution.getFormal().getParameteredElement();
			Activator.log.info(String.format(Messages.TemplateUtils_InfoGetActualFrom, pe));
			return (Classifier) substitution.getActual();
		}
		return null;
	}

	/**
	 * Return a sequence of namespaces for a given element, starting from the "bottom"
	 * one, i.e. the one in which the element is contained. It will end before the
	 * searchNS namespace is reached. Returns null, if the element is not contained
	 * within the search namespace.
	 * This function will put a merged package into the path (instead of the owner), enabling
	 * the extension of existing packages.
	 *
	 * @param element
	 * @param searchNS
	 * @return
	 */
	public static EList<Namespace> relativePathWithMerge(Element element, Namespace searchNS) {
		EList<Namespace> pathList = new BasicEList<Namespace>();
		Element owner = element.getOwner();
		if (!(owner instanceof Namespace)) {
			// happens, if element is contained in a template signature
			return null;
		}
		Namespace ns = (Namespace) owner;
		while (ns != null) {
			if (ns == searchNS) {
				return pathList;
			}
			pathList.add(ns);

			if (ns instanceof Package) {
				Package pkg = (Package) ns;
				Iterator<PackageMerge> pkgMerges = pkg.getPackageMerges().iterator();
				// if package merge exists, get first merged package and add it to path
				if (pkgMerges.hasNext()) {
					PackageMerge pkgImport = pkgMerges.next();
					ns = pkgImport.getMergedPackage();
					continue;
				}
			}

			ns = (Namespace) ns.getOwner();
		}
		return null;
	}
}
