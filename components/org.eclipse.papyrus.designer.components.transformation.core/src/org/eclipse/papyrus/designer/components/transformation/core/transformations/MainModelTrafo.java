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

package org.eclipse.papyrus.designer.components.transformation.core.transformations;

/**
 * This file is part of Qompass GenTools
 * Copyright (C) 2008 CEA LIST (http://www-list.cea.fr/)

 * initial developer : Christophe JOUVRAY from CEA LIST
 * Major contributions: Ansgar Radermacher from CEA LIST
 */

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.designer.components.FCM.ContainerRule;
import org.eclipse.papyrus.designer.components.FCM.ContainerRuleKind;
import org.eclipse.papyrus.designer.components.FCM.DeploymentPlan;
import org.eclipse.papyrus.designer.components.FCM.InteractionComponent;
import org.eclipse.papyrus.designer.components.FCM.util.FCMUtil;
import org.eclipse.papyrus.designer.components.transformation.core.Log;
import org.eclipse.papyrus.designer.components.transformation.core.Messages;
import org.eclipse.papyrus.designer.components.transformation.core.PortUtils;
import org.eclipse.papyrus.designer.components.transformation.core.StUtils;
import org.eclipse.papyrus.designer.components.transformation.core.UMLTool;
import org.eclipse.papyrus.designer.components.transformation.core.deployment.AllocUtils;
import org.eclipse.papyrus.designer.components.transformation.core.deployment.DepCreation;
import org.eclipse.papyrus.designer.components.transformation.core.deployment.DepPlanUtils;
import org.eclipse.papyrus.designer.components.transformation.core.deployment.DepUtils;
import org.eclipse.papyrus.designer.components.transformation.core.extensions.AbstractContainerTrafo;
import org.eclipse.papyrus.designer.components.transformation.core.templates.TemplateInstantiation;
import org.eclipse.papyrus.designer.components.transformation.core.transformations.connector.ConnectorReification;
import org.eclipse.papyrus.designer.components.transformation.core.transformations.container.ContainerTrafo;
import org.eclipse.papyrus.designer.components.transformation.core.transformations.container.CustomTrafoDelegate;
import org.eclipse.papyrus.designer.components.transformation.core.transformations.container.LWContainerTrafo;
import org.eclipse.papyrus.uml.tools.utils.StereotypeUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.EncapsulatedClassifier;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * This class executes the main model transformation. It traverses the
 * instances of a deployment plan in a recursive way and executes
 * connector reification and container expansion.
 */
public class MainModelTrafo {


	public static final String HW_COMP_PREFIX = "Hwc"; //$NON-NLS-1$

	/**
	 * Create a new instance of main-model-transformation
	 *
	 * @param copier
	 *            Copier
	 * @param tmCDP
	 *            deployment plan in target model
	 */
	public MainModelTrafo(LazyCopier copy, Package tmCDP) {
		nodeHandled = new HashMap<InstanceSpecification, Boolean>();
		this.copier = copy;
		this.tmCDP = tmCDP;
	}

	/**
	 * Return an instance specification that corresponds to a part. This
	 * function is useful in the connector context, since it allows to retrieve
	 * the instance specification that is reference by a connection end-point
	 * (which points to the part).
	 *
	 * @param system
	 *            the instance specification for the assembly
	 * @param part
	 *            the part within a class
	 * @return The instance specification for the passed part
	 */
	public static InstanceSpecification getInstanceForPart(
			InstanceSpecification system, Property part) {
		for (Slot slot : system.getSlots()) {
			if (slot.getDefiningFeature() == part) {
				for (ValueSpecification value : slot.getValues()) {
					// instances are accessible via ValueSpecification subclass
					// InstanceValue
					if (value instanceof InstanceValue) {
						return (((InstanceValue) value).getInstance());
					}
				}
			}
		}
		return null;
	}

	/**
	 * Find a port that would match a connection
	 *
	 * @param connectorType
	 *            a connector type, i.e. a component with ports
	 * @param the
	 *            port on the other side of the connection
	 * @return the first port (of all ports owned or inherited by the type) that
	 *         is compatible with the passed otherPort.
	 */
	public static Port getConnectorPort(EncapsulatedClassifier connectorType,
			Port otherPort, boolean isAssembly) {
		EList<Port> ports = PortUtils.getAllPorts(connectorType);
		// try to find match via kind
		for (Port port : ports) {
			if (PortUtils.matches(port, otherPort, isAssembly)) {
				return port;
			}
		}
		// no match found, try weaker condition: find 1st match for provided ...
		boolean otherEndProvides = PortUtils.getProvided(otherPort) != null;
		for (Port port : ports) {
			Interface intf;
			// if isAssembly: take "opposite" port
			if (otherEndProvides != isAssembly) {
				intf = PortUtils.getProvided(port);
			} else {
				intf = PortUtils.getRequired(port);
			}
			if (intf != null) {
				return port;
			}
		}
		return null;
	}


	/**
	 * This method performs a model transformation that replaces an Qompass
	 * connector by a property and a set of simple connectors. The type of the
	 * newly added connector must be instantiated from its template definition
	 * in order to adapt the connector to its context. This is done in a
	 * recursive manner on a compositeInstance. The result is a (composite)
	 * class which has the added connector. The function will also trigger
	 * container expansion, if necessary. Please note that we use the naming
	 * convention to prefix elements of the source model (wrt. to the
	 * transformation) with sm and elements of the target model with tm.
	 *
	 * @param smIS
	 *            source model instance specification
	 * @param smDF
	 *            source model defining feature (null for top-level instance).
	 * @param inheritedRules
	 *            container rules inherited from containing composite
	 * @return new instance specification in traget model
	 *
	 * @throws TransformationException
	 */
	public InstanceSpecification transformInstance(
			InstanceSpecification smIS, StructuralFeature smDF) throws TransformationException {
		Class smComponent = null;
		Classifier smCl = DepUtils.getClassifier(smIS);
		if (smCl instanceof Class) {
			smComponent = (Class) smCl;
		} else {
			// should not happen
			return null;
		}

		String instName = smIS.getName();
		// first check, if instance specification exists already. This may be the case for explicitly modeled singleton instances.
		InstanceSpecification tmIS = (InstanceSpecification)
				tmCDP.getPackagedElement(instName);
		if (tmIS == null) {
			tmIS = (InstanceSpecification)
					tmCDP.createPackagedElement(instName, UMLPackage.eINSTANCE.getInstanceSpecification());
		}
		if (smDF == null) {
			// no defining feature => must be main instance
			// => apply deployment plan stereotype and set main instance
			DeploymentPlan newCDP = StereotypeUtil.applyApp(tmCDP, DeploymentPlan.class);
			newCDP.setMainInstance(tmIS);
		}

		Class tmComponent = copier.getCopy(smComponent);
		if (tmComponent == null) {
			return null;
		}
		tmIS.getClassifiers().add(tmComponent);

		// retrieve the component type object from the static profile (in order
		// to process rules)
		EList<ContainerRule> rules = FCMUtil.getAllContainerRules(smComponent);

		// get container trafo instance, if already existing
		AbstractContainerTrafo containerTrafo = AbstractContainerTrafo.get(tmComponent);

		// general idea: an instance of class ContainerTafo is responsible for a specific component.
		// the method createContainerInstance must be called for each instance of the application component
		// (caveat: don't mix-up with instance of Java classes of the development tool)
		// TODO: since we support multiple container kinds, we need to keep their container map separate!
		InstanceSpecification containerIS = null;
		if (containerTrafo == null) {
			// no container exists, check rules and create eventually
			for (ContainerRule rule : rules) {
				if (RuleManagement.isRuleActive(rule)) {
					// at least one active rule => create container (or get previously instantiated))
					if (containerTrafo == null) {
						if (rule.getKind() == ContainerRuleKind.LIGHT_WEIGHT_OO_RULE) {
							containerTrafo = new LWContainerTrafo(copier, tmCDP);
						}
						else if (rule.getKind() == ContainerRuleKind.COMPONENT_RULE) {
							containerTrafo = new  ContainerTrafo(copier, tmCDP, tmIS);
						}
						else {
							containerTrafo = new CustomTrafoDelegate(copier, tmCDP, rule.getBase_Class().getQualifiedName());
						}
						containerTrafo.createContainer(smComponent, tmComponent);
					}
					else {
						// configure only??
					}
					containerTrafo.applyRule(rule, smComponent, tmComponent);
				}
			}
			if (containerTrafo != null) {
				containerTrafo.finalize();
			}
		}
		if (containerTrafo != null) {
			// create instance of container. This is done after rule application, since
			// elements that are added by the rules need to be instantiated as well.
			// TODO: Cannot mix both rules.
			containerIS = containerTrafo.createContainerInstance(tmComponent, tmIS);
		}
		// ------------------- end of container handling of SW nodes

		// copier node allocation
		for (InstanceSpecification smNode : AllocUtils.getNodes(smIS)) {
			InstanceSpecification tmNode = copier.getCopy(smNode);
			if (containerIS != null) {
				// allocate container instead of executor.
				AllocUtils.allocate(containerIS, tmNode);
			}
			else {
				AllocUtils.allocate(tmIS, tmNode);
			}

			if (!nodeHandled.containsKey(tmNode)) {
				// check if node (on an instance level) has already been treated. This is required, since many
				// instances might be allocated to the same node.
				nodeHandled.put(tmNode, true);

				// check, whether a container rule is applied on the tmNode
				Classifier tmCS = DepUtils.getClassifier(tmNode);
				if (tmCS instanceof Class) {
					// ---------------------------
					EList<ContainerRule> hwRules = FCMUtil.getAllContainerRules(tmCS);

					if (hwRules.size() > 0) {

						ContainerTrafo nodeContainerTrafo = (ContainerTrafo) AbstractContainerTrafo.get((Class) tmCS);

						// issues
						// - unlike SW component container, don't update references pointing towards the HW node
						// - create additional part in system (top-level) component for the node container
						// - container (_cc class) appears in platform component (same package as HW node)
						// - port copying and creation of delegation connectors does not make much sense, creation of
						// executor itself does not make much sense, additional operation "createHwContainer"

						// obtain property related to node instance
						Package smCDP = smIS.getNearestPackage();
						DeploymentPlan smFCM_CDP = UMLUtil.getStereotypeApplication(smCDP, DeploymentPlan.class);

						if (nodeContainerTrafo == null) {
							// container does not exist, check rules and create eventually
							for (ContainerRule rule : hwRules) {
								if (RuleManagement.isRuleActive(rule)) {
									if (nodeContainerTrafo == null) {
										// at least one active rule => create container (or get previously instantiated))
										nodeContainerTrafo = new ContainerTrafo(copier, tmCDP, tmIS);
										nodeContainerTrafo.createHwContainer((Class) tmCS);
										nodeContainerTrafo.applyRule(rule, smComponent, tmComponent);
									}
								}
							}
						}
						if (nodeContainerTrafo != null) {
							InstanceSpecification hwContainerIS =
									nodeContainerTrafo.createHwContainerInstance(tmComponent, tmNode);
							// now add attribute in system (obtain via classifier of main instance in smCDP)
							if (smFCM_CDP != null) {
								InstanceSpecification smMI = smFCM_CDP.getMainInstance();
								Classifier smSystem = DepUtils.getClassifier(smMI);
								Classifier tmSystem = copier.getCopy(smSystem);
								InstanceSpecification tmMI = DepUtils.getInstanceForClassifier(tmCDP, tmSystem);
								if (tmSystem instanceof Class) {
									Property hwcPart =
											((Class) tmSystem).createOwnedAttribute(smNode.getName() + HW_COMP_PREFIX, nodeContainerTrafo.getContainer());
									// and now create a slot for the created instance.
									DepPlanUtils.createSlot(tmCDP, tmMI, hwContainerIS, hwcPart);
								}

								// now allocate instance
								AllocUtils.allocate(hwContainerIS, tmNode);
							}
						}
					}
				}
			}
		} // ------------------- end of container handling of HW nodes


		// reread instName (may have been changed by container transformation).
		instName = tmIS.getName();

		// copy parts, i.e. referenced slots
		// loop on instances (instead of parts), since reification needs to be
		// based on used implementations.
		for (Slot slot : smIS.getSlots()) {
			if (slot.getDefiningFeature() == null) {
				throw new TransformationException(
						String.format(Messages.MainModelTrafo_NoDefiningFeature, smIS.getName()));
			}
			StructuralFeature smPartDF = slot.getDefiningFeature();
			Type type = smPartDF.getType();
			if(type==null){
				throw new TransformationException(String.format(Messages.MainModelTrafo_NoTypeDefinedFor, smPartDF.getName()));	
			}
			if (StereotypeUtil.isApplied(smPartDF.getType(), InteractionComponent.class)) {
				if (smPartDF instanceof Property) {
					Property tmPart = ConnectorReification.reifyConnector(copier, tmComponent, (Property) smPartDF, tmIS);
					// update value specification (to the one just created)
					InstanceSpecification tmPartIS = EcoreUtil.copy(DepUtils.getInstance(slot));
					tmCDP.getPackagedElements().add(tmPartIS);
					// instance specification for connector parts points to connector within package template
					copier.putPair(DepUtils.getInstance(slot), tmPartIS);

					Type newType = tmPart.getType();
					if (newType instanceof Classifier) {
						// update classifier reference, otherwise it would not point to
						// bound type, but to original type in package template
						if (tmPartIS.getClassifiers().size() > 0) {
							tmPartIS.getClassifiers().set(0, (Classifier) newType);
						}
					}
					DepCreation.createSlot(tmIS, tmPartIS, tmPart);
				}
			}
			else if (smPartDF.getType() instanceof Class) {

				InstanceSpecification smPartIS = DepUtils.getInstance(slot);
				if (smPartIS == null) {
					throw new TransformationException(
							String.format(Messages.MainModelTrafo_NoInstanceAssociated, smPartDF.getName()));
				}

				// recursive reification
				// returned instance specification in target model is an instance specification for the part
				// or for a container for that part.
				InstanceSpecification tmPartIS = transformInstance(smPartIS, smPartDF);
				// AllocUtils.propagateNodesViaPort (tmPartIS, null, AllocUtils.getNodes(containedInstance));

				// retrieve part in the target model (it has been created during
				// the copyClassifier operation before)
				// TODO: avoid cast (use StructuralFeature instead)
				Property tmPart = (Property) copier.getCopy(smPartDF);

				// check whether the instance specification has been modified
				// (due to a container transformation)
				// modification would not be required, if
				if ((tmPartIS != null) && (DepUtils.getClassifier(tmPartIS) != tmPart.getType())) {
					Log.log(IStatus.INFO, Log.TRAFO_CONNECTOR, String.format(Messages.MainModelTrafo_ChangePartType, tmPart.getName()));
					tmPart.setType(DepUtils.getClassifier(tmPartIS));
				}
				DepCreation.createSlot(tmIS, tmPartIS, tmPart);
			} else {
				// assume primitive type, copy slot values
				// don't use the copier to avoid duplicate entries (since not all instance specifications created
				// before are correctly inserted into the map of the copier).
				Slot tmSlot = EcoreUtil.copy(slot);
				tmSlot.setDefiningFeature(copier.getCopy(slot.getDefiningFeature()));
				tmIS.getSlots().add(tmSlot);
			}

		}

		// NamedElement socket = Utils.getQualifiedElement(smComponent.getModel(), "SocketRuntime::Socket");
		// NamedElement socketCopy = copier.getCopy(socket);

		// loop on connectors
		// TODO: check, if true (no instance specification exists for these - unlike a connector which is explicitly specified via a part)
		// TODO: quite inefficient (and likely wrong), if same composite is instantiated several times (re-reification of connectors)
		for (Connector smConnector : smComponent.getOwnedConnectors()) {
			if (StUtils.isConnector(smConnector)) {
				org.eclipse.papyrus.designer.components.FCM.Connector fcmConn = StUtils.getConnector(smConnector);
				if (fcmConn != null) {
					// found an Qompass connector, connector type is specified.
					// => Reify the connector within the target component, i.e. create a new part and
					// additional connections for it.
					Property connectorPart = ConnectorReification.reifyConnector(copier, tmComponent,
							UMLTool.varName(smConnector), smConnector, tmIS);

					if (connectorPart == null) {
						continue;
					}

					// remove simple connector from composite in target model
					Connector tmConnector = copier.getCopy(smConnector);
					tmConnector.destroy();

					// Now create an instance specification for the reified connector
					InstanceSpecification tmReifiedConnectorIS = DepCreation.createDepPlan(
							tmCDP, (Class) connectorPart.getType(),
							instName + "." + smConnector.getName(), false); //$NON-NLS-1$
					
					// copy slots from the source deployment plan that are related to connector configuration
					InstanceSpecification smConnectorIS = DepUtils.getNamedSubInstance(smIS, smConnector.getName());
					if (smConnectorIS != null) {
						// use putPair instead of put only - see bug 426748, avoid that classifier attribute points
						// to two classifiers (bound and unbound)
						copier.putPair(smConnectorIS, tmReifiedConnectorIS);
						// problem: the defining feature of the slot points to the original connector which we don't
						// want to copy (would have to be done in context of template binding)
						TemplateInstantiation ti = new TemplateInstantiation(copier, ConnectorReification.binding);
						for (Slot smSlot : smConnectorIS.getSlots()) {
							ti.bindElement(smSlot);
						}
					}

					Slot partSlot =
							DepCreation.createSlot(tmIS, tmReifiedConnectorIS, connectorPart);

					ConnectorReification.propagateNodeAllocation(tmComponent, tmIS, partSlot);
				}
			}
		}

		// needs to be called independently
		// propagateNodeAllocation (cdp, compositeInstance);
		AllocTransfo at = new AllocTransfo();
		at.transformAllocs(copier, tmComponent);

		if (containerIS != null) {
			// return containerIS
			return containerIS;
		} else {
			return tmIS;
		}
	}

	protected Map<InstanceSpecification, Boolean> nodeHandled;

	/**
	 * Copier from source to target model
	 */
	protected LazyCopier copier;

	/**
	 * deployment plan within target model
	 */
	protected Package tmCDP;
}
