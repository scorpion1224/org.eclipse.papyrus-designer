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

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.GenericVisitor;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public class LongLiteralExpr extends StringLiteralExpr {

	private static final String UNSIGNED_MIN_VALUE = "9223372036854775808";

	protected static final String MIN_VALUE = "-" + UNSIGNED_MIN_VALUE + "L";

	public LongLiteralExpr() {
	}

	public LongLiteralExpr(final String value) {
		super(value);
	}

	public LongLiteralExpr(final int beginLine, final int beginColumn, final int endLine, final int endColumn,
			final String value) {
		super(beginLine, beginColumn, endLine, endColumn, value);
	}

	@Override public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	public final boolean isMinValue() {
		return value != null && //
				value.length() == 20 && //
				value.startsWith(UNSIGNED_MIN_VALUE) && //
				(value.charAt(19) == 'L' || value.charAt(19) == 'l');
	}
}
