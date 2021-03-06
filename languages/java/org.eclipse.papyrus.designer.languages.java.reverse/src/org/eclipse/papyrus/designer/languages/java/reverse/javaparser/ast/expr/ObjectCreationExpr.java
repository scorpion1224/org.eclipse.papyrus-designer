/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
  */
/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2015 The JavaParser Team.
 *
 * This file is part of JavaParser.
 * 
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License 
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.expr;

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body.BodyDeclaration;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.type.ClassOrInterfaceType;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.type.Type;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.GenericVisitor;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.VoidVisitor;

import java.util.List;

import static org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.internal.Utils.*;

/**
 * Defines constructor call expression.
 * Example:
 * <code>
 *     new Object()
 * </code>
 *
 * @author Julio Vilmar Gesser
 */
public final class ObjectCreationExpr extends Expression {

	private Expression scope;

	private ClassOrInterfaceType type;

	private List<Type> typeArgs;

	private List<Expression> args;

    // This can be null, to indicate there is no body
    private List<BodyDeclaration> anonymousClassBody;

	public ObjectCreationExpr() {
	}

	/**
	 * Defines a call to a constructor.
	 * @param scope may be null
	 * @param type this is the class that the constructor is being called for.
	 * @param args Any arguments to pass to the constructor
	 */
	public ObjectCreationExpr(final Expression scope, final ClassOrInterfaceType type, final List<Expression> args) {
		setScope(scope);
		setType(type);
		setArgs(args);
	}

	public ObjectCreationExpr(final int beginLine, final int beginColumn, final int endLine, final int endColumn,
			final Expression scope, final ClassOrInterfaceType type, final List<Type> typeArgs,
			final List<Expression> args, final List<BodyDeclaration> anonymousBody) {
		super(beginLine, beginColumn, endLine, endColumn);
		setScope(scope);
		setType(type);
		setTypeArgs(typeArgs);
		setArgs(args);
		setAnonymousClassBody(anonymousBody);
	}

	@Override public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

    /**
     * This can be null, to indicate there is no body
     */
	public List<BodyDeclaration> getAnonymousClassBody() {
		return anonymousClassBody;
	}

	public List<Expression> getArgs() {
		args = ensureNotNull(args);
		return args;
	}

	public Expression getScope() {
		return scope;
	}

	public ClassOrInterfaceType getType() {
		return type;
	}

	public List<Type> getTypeArgs() {
		typeArgs = ensureNotNull(typeArgs);
        return typeArgs;
	}

	public void setAnonymousClassBody(final List<BodyDeclaration> anonymousClassBody) {
		this.anonymousClassBody = anonymousClassBody;
        setAsParentNodeOf(this.anonymousClassBody);
	}

	public void setArgs(final List<Expression> args) {
		this.args = args;
		setAsParentNodeOf(this.args);
	}

	public void setScope(final Expression scope) {
		this.scope = scope;
		setAsParentNodeOf(this.scope);
	}

	public void setType(final ClassOrInterfaceType type) {
		this.type = type;
		setAsParentNodeOf(this.type);
	}

	public void setTypeArgs(final List<Type> typeArgs) {
		this.typeArgs = typeArgs;
		setAsParentNodeOf(this.typeArgs);
	}
}
