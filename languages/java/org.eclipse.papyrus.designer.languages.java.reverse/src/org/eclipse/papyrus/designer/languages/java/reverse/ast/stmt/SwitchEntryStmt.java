/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/*
 * Created on 04/11/2006
 */
package org.eclipse.papyrus.designer.languages.java.reverse.ast.stmt;

import java.util.List;

import org.eclipse.papyrus.designer.languages.java.reverse.ast.expr.Expression;
import org.eclipse.papyrus.designer.languages.java.reverse.ast.visitor.GenericVisitor;
import org.eclipse.papyrus.designer.languages.java.reverse.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class SwitchEntryStmt extends Statement {

	private Expression label;

	private List<Statement> stmts;

	public SwitchEntryStmt() {
	}

	public SwitchEntryStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression label, List<Statement> stmts) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.label = label;
		this.stmts = stmts;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public Expression getLabel() {
		return label;
	}

	public List<Statement> getStmts() {
		return stmts;
	}

	public void setLabel(Expression label) {
		this.label = label;
	}

	public void setStmts(List<Statement> stmts) {
		this.stmts = stmts;
	}
}
