/**
 */
package org.eclipse.papyrus.C_Cpp.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.papyrus.C_Cpp.C_CppPackage;
import org.eclipse.papyrus.C_Cpp.Variadic;

import org.eclipse.uml2.uml.Operation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variadic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.papyrus.C_Cpp.impl.VariadicImpl#getBase_operation <em>Base operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VariadicImpl extends MinimalEObjectImpl.Container implements Variadic {
	/**
	 * The cached value of the '{@link #getBase_operation() <em>Base operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_operation()
	 * @generated
	 * @ordered
	 */
	protected Operation base_operation;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VariadicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return C_CppPackage.Literals.VARIADIC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operation getBase_operation() {
		if (base_operation != null && base_operation.eIsProxy()) {
			InternalEObject oldBase_operation = (InternalEObject)base_operation;
			base_operation = (Operation)eResolveProxy(oldBase_operation);
			if (base_operation != oldBase_operation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, C_CppPackage.VARIADIC__BASE_OPERATION, oldBase_operation, base_operation));
			}
		}
		return base_operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operation basicGetBase_operation() {
		return base_operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_operation(Operation newBase_operation) {
		Operation oldBase_operation = base_operation;
		base_operation = newBase_operation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, C_CppPackage.VARIADIC__BASE_OPERATION, oldBase_operation, base_operation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case C_CppPackage.VARIADIC__BASE_OPERATION:
				if (resolve) return getBase_operation();
				return basicGetBase_operation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case C_CppPackage.VARIADIC__BASE_OPERATION:
				setBase_operation((Operation)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case C_CppPackage.VARIADIC__BASE_OPERATION:
				setBase_operation((Operation)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case C_CppPackage.VARIADIC__BASE_OPERATION:
				return base_operation != null;
		}
		return super.eIsSet(featureID);
	}

} //VariadicImpl
