/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast;

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.expr.AnnotationExpr;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.type.ClassOrInterfaceType;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.GenericVisitor;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.VoidVisitor;

import java.util.List;

import static org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.internal.Utils.ensureNotNull;

/**
 * <p>
 * This class represents the declaration of a generics argument.
 * </p>
 * The TypeParameter is constructed following the syntax:<br>
 * <pre>
 * {@code
 * TypeParameter ::= <IDENTIFIER> ( "extends" }{@link ClassOrInterfaceType}{@code ( "&" }{@link ClassOrInterfaceType}{@code )* )?
 * }
 * </pre>
 * @author Julio Vilmar Gesser
 */
public final class TypeParameter extends Node implements NamedNode {

	private String name;

    private List<AnnotationExpr> annotations;

	private List<ClassOrInterfaceType> typeBound;

	public TypeParameter() {
	}

	public TypeParameter(final String name, final List<ClassOrInterfaceType> typeBound) {
		setName(name);
		setTypeBound(typeBound);
	}

	public TypeParameter(final int beginLine, final int beginColumn, final int endLine, final int endColumn,
			final String name, final List<ClassOrInterfaceType> typeBound) {
		super(beginLine, beginColumn, endLine, endColumn);
		setName(name);
		setTypeBound(typeBound);
	}

    public TypeParameter(int beginLine, int beginColumn, int endLine,
                         int endColumn, String name, List<ClassOrInterfaceType> typeBound, List<AnnotationExpr> annotations) {
        this(beginLine, beginColumn, endLine, endColumn, name, typeBound);
        setName(name);
        setTypeBound(typeBound);
        this.annotations = annotations;
    }

	@Override public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	/**
	 * Return the name of the paramenter.
	 * 
	 * @return the name of the paramenter
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Return the list of {@link ClassOrInterfaceType} that this parameter
	 * extends. Return <code>null</code> null if there are no type.
	 * 
	 * @return list of types that this paramente extends or <code>null</code>
	 */
	public List<ClassOrInterfaceType> getTypeBound() {
        typeBound = ensureNotNull(typeBound);
        return typeBound;
	}

	/**
	 * Sets the name of this type parameter.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the list o types.
	 * 
	 * @param typeBound
	 *            the typeBound to set
	 */
	public void setTypeBound(final List<ClassOrInterfaceType> typeBound) {
		this.typeBound = typeBound;
		setAsParentNodeOf(typeBound);
	}

    public List<AnnotationExpr> getAnnotations() {
        annotations = ensureNotNull(annotations);
        return annotations;
    }

    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
    }
}
