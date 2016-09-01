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

package org.eclipse.papyrus.designer.transformation.library.transformations;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.papyrus.MARTE.MARTE_DesignModel.SRM.SW_Concurrency.SwSchedulableResource;
import org.eclipse.papyrus.designer.deployment.profile.Deployment.InitPrecedence;
import org.eclipse.papyrus.designer.deployment.tools.AllocUtils;
import org.eclipse.papyrus.designer.deployment.tools.DepUtils;
import org.eclipse.papyrus.designer.languages.cpp.profile.C_Cpp.Include;
import org.eclipse.papyrus.designer.transformation.base.utils.ElementUtils;
import org.eclipse.papyrus.designer.transformation.base.utils.StUtils;
import org.eclipse.papyrus.designer.transformation.base.utils.TransformationException;
import org.eclipse.papyrus.designer.transformation.core.Messages;
import org.eclipse.papyrus.designer.transformation.core.m2minterfaces.IM2MTrafoCDP;
import org.eclipse.papyrus.designer.transformation.core.transformations.LazyCopier;
import org.eclipse.papyrus.designer.transformation.core.transformations.TransformationContext;
import org.eclipse.papyrus.designer.transformation.profile.Transformation.M2MTrafo;
import org.eclipse.papyrus.uml.tools.utils.ConnectorUtil;
import org.eclipse.papyrus.uml.tools.utils.StereotypeUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * The task of the boot-loader is twofold: create the instances of all
 * implementations (non-recursive).
 * - create Connections: what should be done?
 *
 * TODO: factor out common code (TemplateInstantiation mechanism & createConnections below)
 * Split between C++ specific and C++ independent aspects
 */
public class BootLoaderGen implements IM2MTrafoCDP {

	private static final String EMPTYSTR = ""; //$NON-NLS-1$

	final public static String NODE_INFO = "NodeInfo"; //$NON-NLS-1$

	final public static String INIT_OP_NAME = "init"; //$NON-NLS-1$

	final public static String NL = "\n"; //$NON-NLS-1$

	final public static String EOL = ";\n"; //$NON-NLS-1$

	final public static String BOOTLOADER_NAME = "BootLoader"; //$NON-NLS-1$

	/**
	 * Create a new boot-loader in a specific package
	 * (which represents a node of the system).
	 *
	 * @param copier a lazy copier
	 * @param nodeIndex the index of the node
	 * @param numberOfNodes the number of nodes
	 * @throws TransformationException
	 */
	public void init(LazyCopier copier, int nodeIndex, int numberOfNodes)
			throws TransformationException {
		// Class composite = (Class) ut.getClassifier (mainInstance);
		// place in root (getModel()) to avoid the problem that the declaration of the bootLoader
		// instance is within a namespace (a static attribute on the model level would not solve the
		// problem as it must be accessed by function main).

		Package root = TransformationContext.current.modelRoot;
		
		Class nodeInfo = root.createOwnedClass(NODE_INFO, false);
		String headerStr =
				"const int nodeIndex = " + nodeIndex + ";" + NL + //$NON-NLS-1$//$NON-NLS-2$
						"const int numberOfNodes = " + numberOfNodes + ";" + NL; //$NON-NLS-1$ //$NON-NLS-2$
		Include cppIncludeNodeInfo = StereotypeUtil.applyApp(nodeInfo, Include.class);
		if (cppIncludeNodeInfo == null) {
			throw new TransformationException("Can not apply C++ stereotypes during bootloader generation. Please apply the C++ profile to the source model");
		}
		cppIncludeNodeInfo.setHeader(headerStr);

		// bootLoader.createOwnedAttribute (mainInstance.getName (), composite);

		m_bootLoader = root.createOwnedClass(BOOTLOADER_NAME, false);
		outputSizeof = false;
		m_copier = copier;
		Class template = (Class) ElementUtils.getQualifiedElement(copier.source, BOOT_LOADER_QNAME);
		if (template == null) {
			throw new TransformationException(String.format(
					Messages.BootLoaderGen_CannotRetrieveTemplate, BOOT_LOADER_QNAME));
		}
		// TODO: currently, only stereotypes are copied from template
		// these contain the main functions via the stereotype C_Cpp:Include
		StUtils.copyStereotypes(template, m_bootLoader);
		// TODO: commented code below already fixed?
		/*
		 * Problem: defaultValue not taken into account by code generator
		 * => use global variables via cppInclude instead (see below: "bodyStr = ...")
		 * NamedElement corba_long = ElementUtils.getQualifiedElement (owner, "CORBA::Long");
		 * if (corba_long instanceof Type) {
		 * Property nodeNumber =
		 * m_bootLoader.createOwnedAttribute ("nodeNumber", (Type) corba_long);
		 * nodeNumber.setIsStatic (true);
		 * nodeNumber.setDefault ("0"); // for testing
		 * Property numberOfNodes =
		 * m_bootLoader.createOwnedAttribute ("numberOfNodes", (Type) corba_long);
		 * numberOfNodes.setIsStatic (true);
		 * numberOfNodes.setDefault ("2");
		 * }
		 */
		Include cppInclude = StereotypeUtil.applyApp(m_bootLoader, Include.class);
		if (cppInclude == null) {
			throw new TransformationException(Messages.BootLoaderGen_CannotApplyCppInclude);
		}
		String existingBody = cppInclude.getBody();
		String bodyStr = EMPTYSTR;

		if (outputSizeof) {
			bodyStr +=
					"#include <iostream>" + NL + //$NON-NLS-1$
							"using namespace std;" + NL; //$NON-NLS-1$
		}
		cppInclude.setBody(existingBody + bodyStr);

		// bootLoader.createOwnedAttribute (mainInstance.getName (), composite);

		m_initCode = EMPTYSTR;
		m_initCodeRun = EMPTYSTR;
		m_activation = new HashMap<Class, EList<String>>();
		m_initCodeCConnections = EMPTYSTR;
		m_initCodeCConfig = EMPTYSTR;

		if (outputSizeof) {
			m_initCode += "cout << \"sizeof bootloader: \" << sizeof (bootloader) << endl;" + EOL; //$NON-NLS-1$
		}
		// indexMap = new HashMap<String, Integer>();
	}

	/**
	 * Return the path from the main instance towards a sub-instance using the proper dereference
	 * operators, i.e. ".", if the sub-instance is enclosed via composition or "->" if the sub-instance
	 * is a pointer (since created by the bootloader).
	 *
	 * @param slotPath
	 * @param instance
	 * @param accessName
	 *            return the name to access the feature. Returns access path to instance, not
	 *            the name of the variable for this instance (if instantiated by bootloader)
	 * @return
	 */
	public String getPath(Stack<Slot> slotPath, InstanceSpecification instance, boolean accessName) {
		if (slotPath.size() > 0) {
			// start with first instance
			String path = slotPath.get(0).getOwningInstance().getName();
			boolean previousInstantiatedByBL = false;
			for (Slot pathElement : slotPath) {
				if (pathElement != null) {
					if (previousInstantiatedByBL && accessName) {
						// If an instance is instantiated by the bootloader, it is only referenced via its type in the
						// owning composite. Thus, configuration (and activation calls) might fail as the type might not
						// have these configuration properties or operations.
						// Therefore, configuration and initial calls use
						// - the path, if instantiated by the composite
						// - the variable name, if done by the bootloader
						path = ElementUtils.varName(path); // use variable name instead.
					}
					path += "." + pathElement.getDefiningFeature().getName(); //$NON-NLS-1$
					previousInstantiatedByBL = instantiateViaBootloader(pathElement.getDefiningFeature());
				}
			}
			if (previousInstantiatedByBL && !accessName) {
				// name of the variable for this expression instantiated by the bootloader
				path = ElementUtils.varName(path);
			}
			return path;
		}
		else {
			return instance.getName(); // instance has no path via slots, it is a top level instance
		}
	}

	public Property addInstance(Stack<Slot> slotPath, InstanceSpecification instance, Class implementation)
			throws TransformationException
	{
		// TODO: comments not clear. seems unnecessary complex. Problem in general is that access to
		// shared instances needs to be configured.
		// It should always be possible to configure this instance via a path w/o sharing.
		String accessName = getPath(slotPath, instance, true);
		Package tst = TransformationContext.current.modelRoot;
		String varName = getPath(slotPath, instance, false);

		Property implemPart = null;

		// containing instance not null (=> neither main instance nor singleton)
		Slot containerSlot = null;
		if (slotPath.size() > 0) {
			containerSlot = slotPath.peek();

			// initialize part/property in containing instance. The containing instance itself is accessed
			// via the naming of the associated instance, the part itself via the name of the defining feature.
			if (DepUtils.isShared(containerSlot)) {
				// we need to initialize the property (a reference) with the given instance
				Stack<Slot> referencePath = DepUtils.getAccessPath(instance);
				String referenceVarName = getPath(referencePath, instance, false);

				// add code for initialization
				m_initCode += accessName + " = &" + referenceVarName + EOL; //$NON-NLS-1$
				// is a reference which should not be called via activation & start
				// return now and skip code below
				return implemPart;
			}
			else if (instantiateViaBootloader(containerSlot.getDefiningFeature())) {
				// let bootloader instantiate
				implemPart = m_bootLoader.createOwnedAttribute(/* "i_" + */varName, implementation);
				// add code for initialization (TODO: specific to C++!)
				m_initCode += accessName + " = &" + varName + EOL; //$NON-NLS-1$
				implemPart.setIsComposite(true);
			}
		}
		else {
			// top level instance => bootloader instantiates, create attribute
			implemPart = m_bootLoader.createOwnedAttribute(/* "i_" + */varName, implementation);
			implemPart.setIsComposite(true);
		}
		if (outputSizeof) {
			m_initCode += "cout << \"sizeof " + implementation.getName() + ": \" << sizeof (" + varName + ") << endl;" + EOL; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}

		// if start thread => existing thread activation interceptor? Connection?
		if (StereotypeUtil.isApplied(implementation, SwSchedulableResource.class)) {
			// yes, but is the thread instance part of the deployment plan?? [mmh, probably yes...]
			// call threads start routine here? (via main thread?) which in turn will activate the start routine?
		}

		// implementation contains get_start operation => has start port
		// which is called automatically
		//String get_start = PrefixConstants.getP_Prefix + "start"; //$NON-NLS-1$
		// TODO
		String get_start = "get_start";
		
		// Need to check whether implementation is an executor which is encapsulated in a container. In this case, only
		// the method of the container and not the method of the executor (which owns the same port) maybe called.
		// Currently, this check is based on the use of "executor" as reserved part name (validation checks that the
		// user does not use this name for application components)
		if (hasUnconnectedStartRoutine(m_copier, implementation, containerSlot)) {
			if (m_initCodeRun.equals(EMPTYSTR)) {
				// call start's run method
				// TODO: Need path that uses the right dereference operator ("->" or ".")
				m_initCodeRun = varName + "." + get_start + "()->run()" + EOL; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				throw new TransformationException(String.format(
						Messages.BootLoaderGen_AtLeastOneBlockingCall,
						varName, m_initCodeRun));
			}
		}
		if (hasUnconnectedLifeCycle(m_copier, implementation, containerSlot)) {
			// precedence is checked below (when code is actually produced)
			// multiple varNames might share the same implementation. Put a list of variable names into the table
			EList<String> varNameList = m_activation.get(implementation);
			if (varNameList == null) {
				varNameList = new BasicEList<String>();
			}
			varNameList.add(varName + "."); //$NON-NLS-1$
			m_activation.put(implementation, varNameList);
		}

		// check, if implementation contains a composite with assembly connectors
		boolean bCreateConn = false;
		for (Connector connector : implementation.getOwnedConnectors()) {
			if (ConnectorUtil.isAssembly(connector)) {
				bCreateConn = true;
				break;
			}
		}

		if (bCreateConn) {
			// TODO
			//m_initCodeCConnections += varName + "." + PrefixConstants.createConnections +"();\n"; //$NON-NLS-1$ //$NON-NLS-2$
			m_initCodeCConnections += varName + ".createConnections();\n"; //$NON-NLS-1$
		}
		return implemPart;
	}

	/**
	 * Check whether the passed implementation has an unconnected start port.
	 * This information is required, since only unconnected start ports are automatically called by the
	 * bootloader, in particular we want to avoid calling a start port of an executor (which is connected)
	 * and its container.
	 *
	 * @param implementation
	 * @param containerSlot
	 * @return
	 */
	public static boolean hasUnconnectedStartRoutine(LazyCopier copier, Class implementation, Slot containerSlot) {
		if (implementation != null) {
			Port startPort = AllocUtils.getStartPort(implementation);
			if (startPort != null) {
				return !isConnected(copier, containerSlot, startPort);
			}
		}
		return false;
	}

	/**
	 * Check, if the passed implementation has an unconnected life-cycle interface (activate/deactivate).
	 * This information is required, since only unconnected life cycle ports are automatically called by the
	 * bootloader, in particular we want to avoid calling a life cycle port of an executor (which is connected)
	 * and its container.
	 *
	 * @param implementation
	 * @param name
	 * @return
	 */
	public static boolean hasUnconnectedLifeCycle(LazyCopier copy, Class implementation, Slot containerSlot) {
		if (implementation != null) {
			Element lcPortElem = ElementUtils.getNamedElementFromList(implementation.getAllAttributes(), "lc"); //$NON-NLS-1$
			if (lcPortElem instanceof Port) {
				Port lcPort = (Port) lcPortElem;
				// check, if port typed with ILifeCycle interface
				if (lcPort.getType().getName().equals("ILifeCycle")) { //$NON-NLS-1$
					return !isConnected(copy, containerSlot, lcPort);
				}
			}

		}
		return false;
	}

	/**
	 * The check verifies whether the passed port is connected within
	 * the context of the composite represented by the passed slot
	 *
	 * @param containerSlot
	 *            a slot within an instance that represents a composite class
	 * @Param a port that is checked for being connected
	 * @return true, if connected
	 */
	private static boolean isConnected(LazyCopier copier, Slot containerSlot, Port port) {
		if (containerSlot != null) {
			StructuralFeature sf = containerSlot.getDefiningFeature();
			if (sf instanceof Property) {
				Property part = (Property) sf;
				Class composite = part.getClass_();
				for (Connector connector : composite.getOwnedConnectors()) {
					// must assure same connector end connects part & port
					ConnectorEnd end = ConnectorUtil.connEndForPart(connector, part);
					if ((end != null) && (end.getRole() == port)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void instanceConfig(Stack<Slot> slotPath, InstanceSpecification instance) throws TransformationException {
		Slot slot = slotPath.peek();
		// String varName = getPath(slotPath, instance, false);
		StructuralFeature sf = slot.getDefiningFeature();
		if (sf == null) {
			throw new TransformationException(String.format("A slot for instance %s has no defining feature", instance.getName())); //$NON-NLS-1$
		}

		String varName = instance.getName() + "." + sf.getName(); //$NON-NLS-1$
		for (ValueSpecification value : slot.getValues()) {

			// only set value, if not null
			if (value.stringValue() != null) {
				m_initCodeCConfig += varName + " = " + value.stringValue() + EOL; //$NON-NLS-1$
			}
		}
	}

	public void addCreateConnections() {
		// code generators do not handle connectors in a suitable way. Remove
		// (alternative: no-code-gen tag, as available for C++)
		m_bootLoader.getOwnedConnectors().clear();
	}

	/**
	 * add the initialize operation. Must be called after a set of addInstance invocations
	 * @param mainInstance
	 */
	public void addInit(String language) {
		// TODO: use template
		Operation init = m_bootLoader.createOwnedOperation(INIT_OP_NAME, null, null);
		OpaqueBehavior initBehavior = (OpaqueBehavior)
				m_bootLoader.createOwnedBehavior(INIT_OP_NAME, UMLPackage.eINSTANCE.getOpaqueBehavior());
		init.getMethods().add(initBehavior);

		initBehavior.getLanguages().add(language);
		String code = m_initCode + "\n"; //$NON-NLS-1$
		if (m_initCodeCConfig.length() > 0) {
			code += "\n// instance configuration\n" + //$NON-NLS-1$
					m_initCodeCConfig + "\n"; //$NON-NLS-1$
		}
		if (m_initCodeCConnections.length() > 0) {
			code += "\n// create connections between instances\n" + //$NON-NLS-1$
					m_initCodeCConnections + "\n"; //$NON-NLS-1$
		}
		Comparator<Class> comparator = new Comparator<Class>() {

			@Override
			public int compare(Class clazz1, Class clazz2) {

				InitPrecedence precedenceC1 = UMLUtil.getStereotypeApplication(clazz1, InitPrecedence.class);
				InitPrecedence precedenceC2 = UMLUtil.getStereotypeApplication(clazz2, InitPrecedence.class);
				if (precedenceC1 != null) {
					// need to use named comparison instead of precedenceC1.getInvokeAfter ().contains (clazz2)
					// since class referenced by stereotype attribute still points to element in source model
					if (ElementUtils.getNamedElementFromList(precedenceC1.getInvokeAfter(), clazz2.getName()) != null) {
						return 1;
					}
					else if (ElementUtils.getNamedElementFromList(precedenceC1.getInvokeBefore(), clazz2.getName()) != null) {
						return -1;
					}
				}
				else if (precedenceC2 != null) {
					if (ElementUtils.getNamedElementFromList(precedenceC2.getInvokeAfter(), clazz1.getName()) != null) {
						return -1;
					}
					else if (ElementUtils.getNamedElementFromList(precedenceC2.getInvokeBefore(), clazz1.getName()) != null) {
						return 1;
					}
				}
				// singletons have precedence over "normal" classes
				boolean ci1IsSingleton = DepUtils.isSingleton(clazz1);
				boolean ci2IsSingleton = DepUtils.isSingleton(clazz2);
				if (ci1IsSingleton) {
					if (!ci2IsSingleton) {
						// not both are singletons
						return -1;
					}
				}
				else if (ci2IsSingleton) {
					return 1;
				}
				return 0;
			}
		};
		Class[] activationKeys = m_activation.keySet().toArray(new Class[0]);
		// String get_lc = PrefixConstants.getP_Prefix + "lc"; //$NON-NLS-1$
		// TODO
		String get_lc = "get_lc"; //$NON-NLS-1$
		if (activationKeys.length > 0) {
			Arrays.sort(activationKeys, comparator);
			code += "// activation code\n"; //$NON-NLS-1$
			for (Class implementation : activationKeys) {
				EList<String> varNameList = m_activation.get(implementation);
				for (String varName : varNameList) {
					code += varName + get_lc + "()->activate();\n"; //$NON-NLS-1$
				}
			}
		}
		
		Include cppInclude = UMLUtil.getStereotypeApplication(m_bootLoader, Include.class);
		if (m_initCodeRun.length() > 0) {
			code += "// initial user start\n" + m_initCodeRun; //$NON-NLS-1$
		} else {
			// no component implements the initial start, therefore enter a sleep
			// unistd is required (at least on unix systems) for sleep
			cppInclude.setPreBody("#include <unistd.h> // for sleep\n"); //$NON-NLS-1$

			code += "// sleep forever\n"; //$NON-NLS-1$
			code += "for (;;) { sleep(100); }\n"; //$NON-NLS-1$
			// throw new TransformationRTException("no component implements the initial start. Assure that one component inherits from the CStart component");
		}
		if (activationKeys.length > 0) {
			code += "// deactivation code (reverse order)\n"; //$NON-NLS-1$

			// traverse in reverse order
			for (int i = activationKeys.length - 1; i >= 0; i--) {
				Class implementation = activationKeys[i];
				EList<String> varNameList = m_activation.get(implementation);
				for (String varName : varNameList) {
					code += varName + get_lc + "()->deactivate();\n"; //$NON-NLS-1$
				}
			}
		}

		initBehavior.getBodies().add(code);
	}
	
	public boolean instantiateViaBootloader(StructuralFeature slot) {
		//TODO: always false.
		return false;
	}

	public Class getUML() {
		return m_bootLoader;
	}

	private Class m_bootLoader;

	public final static String BOOT_LOADER_QNAME = "trafos::composites::BootLoader"; //$NON-NLS-1$

	/**
	 * Initialization code, in particular assignment of part properties within composites
	 */
	private String m_initCode;

	/**
	 * Init code for create connections calls in composites with at least one assembly
	 * connector
	 */
	private String m_initCodeCConfig;

	/**
	 * Init code for create connections calls in composites with at least one assembly
	 * connector
	 */
	private String m_initCodeCConnections;

	/**
	 * Init code for blocking "run" calls (related to CStart system component)
	 */
	private String m_initCodeRun;

	/**
	 * Map containing activations/de-activations
	 */
	private Map<Class, EList<String>> m_activation;

	private boolean outputSizeof;

	/**
	 * copier variable (instances still point to non-copied classes)
	 */
	private LazyCopier m_copier;

	
	@Override
	public void applyTrafo(M2MTrafo trafo, Package deploymentPlan) throws TransformationException {
		// TODO Auto-generated method stub
		init(TransformationContext.current.copier, 0, 0);
		addCreateConnections();

		Stack<Slot> slotPath = new Stack<Slot>();
		for (InstanceSpecification is : DepUtils.getTopLevelInstances(deploymentPlan))  {
			addInstance(is, slotPath);
		}
		addInit("C/C++");
	}

	public void addInstance(InstanceSpecification is, Stack<Slot> slotPath) throws TransformationException {
		Classifier implementation = DepUtils.getClassifier(is);
		if (implementation instanceof Class) {
			addInstance(slotPath, is, (Class) implementation);
			// instanceConfig(slotPath, is);
		}
		for (Slot slot : is.getSlots()) {
			InstanceSpecification subIS = DepUtils.getInstance(slot);
			if (subIS != null) {
				slotPath.push(slot);
				addInstance(subIS, slotPath);
				slotPath.pop();
			}
		}
	}


	/**
	 * Store a map with index values to manage configuration of arrays
	 */
	// protected Map<String, Integer> indexMap;
}