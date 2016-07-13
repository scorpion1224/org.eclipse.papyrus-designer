/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.stmt;

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.expr.Expression;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.type.Type;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.GenericVisitor;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.VoidVisitor;

import java.util.List;

import static org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.internal.Utils.*;

/**
 * @author Julio Vilmar Gesser
 */
public final class ExplicitConstructorInvocationStmt extends Statement {

	private List<Type> typeArgs;

	private boolean isThis;

	private Expression expr;

	private List<Expression> args;

	public ExplicitConstructorInvocationStmt() {
	}

	public ExplicitConstructorInvocationStmt(final boolean isThis,
			final Expression expr, final List<Expression> args) {
		setThis(isThis);
		setExpr(expr);
		setArgs(args);
	}

	public ExplicitConstructorInvocationStmt(final int beginLine,
			final int beginColumn, final int endLine, final int endColumn,
			final List<Type> typeArgs, final boolean isThis,
			final Expression expr, final List<Expression> args) {
		super(beginLine, beginColumn, endLine, endColumn);
		setTypeArgs(typeArgs);
		setThis(isThis);
		setExpr(expr);
		setArgs(args);
	}

	@Override
	public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	public List<Expression> getArgs() {
        args = ensureNotNull(args);
        return args;
	}

	public Expression getExpr() {
		return expr;
	}

	public List<Type> getTypeArgs() {
        typeArgs = ensureNotNull(typeArgs);
        return typeArgs;
	}

	public boolean isThis() {
		return isThis;
	}

	public void setArgs(final List<Expression> args) {
		this.args = args;
		setAsParentNodeOf(this.args);
	}

	public void setExpr(final Expression expr) {
		this.expr = expr;
		setAsParentNodeOf(this.expr);
	}

	public void setThis(final boolean isThis) {
		this.isThis = isThis;
	}

	public void setTypeArgs(final List<Type> typeArgs) {
		this.typeArgs = typeArgs;
		setAsParentNodeOf(this.typeArgs);
	}
}
