/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body;

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.Node;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.expr.AnnotationExpr;

import java.util.List;

import static org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.internal.Utils.*;

public abstract class BaseParameter extends Node implements AnnotableNode {
    private int modifiers;

    private List<AnnotationExpr> annotations;
    
    private VariableDeclaratorId id;
    
    public BaseParameter() {
    }
    
    public BaseParameter(VariableDeclaratorId id) {
        setId(id);
	}

	public BaseParameter(int modifiers, VariableDeclaratorId id) {
        setModifiers(modifiers);
        setId(id);
	}
	
	public BaseParameter(int modifiers, List<AnnotationExpr> annotations, VariableDeclaratorId id) {
        setModifiers(modifiers);
        setAnnotations(annotations);
        setId(id);
	}

	public BaseParameter(int beginLine, int beginColumn, int endLine, int endColumn, int modifiers, List<AnnotationExpr> annotations, VariableDeclaratorId id) {
	    super(beginLine, beginColumn, endLine, endColumn);
        setModifiers(modifiers);
        setAnnotations(annotations);
        setId(id);
	}

    /**
     * @return the list returned could be immutable (in that case it will be empty)
     */
    public List<AnnotationExpr> getAnnotations() {
        annotations = ensureNotNull(annotations);
        return annotations;
    }

    public VariableDeclaratorId getId() {
        return id;
    }

    /**
     * Return the modifiers of this parameter declaration.
     * 
     * @see ModifierSet
     * @return modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * @param annotations a null value is currently treated as an empty list. This behavior could change
     *                    in the future, so please avoid passing null
     */
    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
        setAsParentNodeOf(this.annotations);
    }

    public void setId(VariableDeclaratorId id) {
        this.id = id;
        setAsParentNodeOf(this.id);
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}
