/**
 */
package org.eclipse.papyrus.designer.deployment.profile.Deployment;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Copy Attribute Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.papyrus.designer.deployment.profile.Deployment.CopyAttributeValue#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link org.eclipse.papyrus.designer.deployment.profile.Deployment.CopyAttributeValue#getSource <em>Source</em>}</li>
 * </ul>
 *
 * @see org.eclipse.papyrus.designer.deployment.profile.Deployment.DeploymentPackage#getCopyAttributeValue()
 * @model
 * @generated
 */
public interface CopyAttributeValue extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Property</em>' reference.
	 * @see #setBase_Property(Property)
	 * @see org.eclipse.papyrus.designer.deployment.profile.Deployment.DeploymentPackage#getCopyAttributeValue_Base_Property()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.designer.deployment.profile.Deployment.CopyAttributeValue#getBase_Property <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Property</em>' reference.
	 * @see #getBase_Property()
	 * @generated
	 */
	void setBase_Property(Property value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(Property)
	 * @see org.eclipse.papyrus.designer.deployment.profile.Deployment.DeploymentPackage#getCopyAttributeValue_Source()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	Property getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.designer.deployment.profile.Deployment.CopyAttributeValue#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Property value);

} // CopyAttributeValue
