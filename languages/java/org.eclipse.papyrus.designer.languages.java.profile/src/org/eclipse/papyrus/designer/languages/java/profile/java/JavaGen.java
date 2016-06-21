/*******************************************************************************
 * Copyright (c) 2006, 2016 CEA LIST and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     CEA LIST - initial API and implementation
 *     
 *     
 *******************************************************************************/
/**
 */
package org.eclipse.papyrus.designer.languages.java.profile.java;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Gen</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.papyrus.designer.languages.java.profile.java.JavaGen#getSrcName <em>Src Name</em>}</li>
 *   <li>{@link org.eclipse.papyrus.designer.languages.java.profile.java.JavaGen#getProjectName <em>Project Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.papyrus.designer.languages.java.profile.java.JavaPackage#getJavaGen()
 * @model abstract="true"
 * @generated
 */
public interface JavaGen extends JavaNamedElement {
	/**
	 * Returns the value of the '<em><b>Src Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Src Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Src Name</em>' attribute.
	 * @see #setSrcName(String)
	 * @see org.eclipse.papyrus.designer.languages.java.profile.java.JavaPackage#getJavaGen_SrcName()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	String getSrcName();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.designer.languages.java.profile.java.JavaGen#getSrcName <em>Src Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Src Name</em>' attribute.
	 * @see #getSrcName()
	 * @generated
	 */
	void setSrcName(String value);

	/**
	 * Returns the value of the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Project Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project Name</em>' attribute.
	 * @see #setProjectName(String)
	 * @see org.eclipse.papyrus.designer.languages.java.profile.java.JavaPackage#getJavaGen_ProjectName()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
	String getProjectName();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.designer.languages.java.profile.java.JavaGen#getProjectName <em>Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project Name</em>' attribute.
	 * @see #getProjectName()
	 * @generated
	 */
	void setProjectName(String value);

} // JavaGen
