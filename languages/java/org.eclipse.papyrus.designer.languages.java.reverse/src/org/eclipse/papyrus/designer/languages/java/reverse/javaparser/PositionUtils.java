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

package org.eclipse.papyrus.designer.languages.java.reverse.javaparser;

import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.Node;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.TypedNode;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body.AnnotableNode;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body.FieldDeclaration;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.body.MethodDeclaration;
import org.eclipse.papyrus.designer.languages.java.reverse.javaparser.ast.expr.AnnotationExpr;

import java.lang.Override;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.signum;

public final class PositionUtils {

    private PositionUtils() {
        // prevent instantiation
    }

    public static <T extends Node> void sortByBeginPosition(List<T> nodes){
        sortByBeginPosition(nodes, false);
    }

    public static <T extends Node> void sortByBeginPosition(List<T> nodes, final boolean ignoringAnnotations){
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return PositionUtils.compare(o1, o2, ignoringAnnotations);
            }
        });
    }

    public static boolean areInOrder(Node a, Node b){
        return areInOrder(a, b, false);
    }

    public static boolean areInOrder(Node a, Node b, boolean ignoringAnnotations){
        return compare(a, b, ignoringAnnotations) <= 0;
    }

    private static int compare(Node a, Node b, boolean ignoringAnnotations) {
        if (ignoringAnnotations) {
            int signLine = signum(beginLineWithoutConsideringAnnotation(a) - beginLineWithoutConsideringAnnotation(b));
            if (signLine == 0) {
                return signum(beginColumnWithoutConsideringAnnotation(a) - beginColumnWithoutConsideringAnnotation(b));
            } else {
                return signLine;
            }
        }

        int signLine = signum( a.getBeginLine() - b.getBeginLine() );
        if (signLine == 0) {
            return signum(a.getBeginColumn() - b.getBeginColumn());
        } else {
            return signLine;
        }
    }

    public static AnnotationExpr getLastAnnotation(Node node) {
        if (node instanceof AnnotableNode){
            List<AnnotationExpr> annotations = new LinkedList<AnnotationExpr>();
            annotations.addAll(((AnnotableNode) node).getAnnotations());
            if (annotations.isEmpty()){
                return null;
            }
            sortByBeginPosition(annotations);
            return annotations.get(annotations.size()-1);
        } else {
            return null;
        }
    }

    private static int beginLineWithoutConsideringAnnotation(Node node) {
        return beginNodeWithoutConsideringAnnotations(node).getBeginLine();
    }


    private static int beginColumnWithoutConsideringAnnotation(Node node) {
        return beginNodeWithoutConsideringAnnotations(node).getBeginColumn();
    }

    private static Node beginNodeWithoutConsideringAnnotations(Node node) {
        if (node instanceof MethodDeclaration || node instanceof FieldDeclaration) {
            TypedNode casted = (TypedNode) node;
            return casted.getType();
        } else if (node instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration casted = (ClassOrInterfaceDeclaration) node;
            return casted.getNameExpr();
        }  else {
            return node;
        }
    }

    public static boolean nodeContains(Node container, Node contained, boolean ignoringAnnotations){
        if (!ignoringAnnotations || PositionUtils.getLastAnnotation(container)==null){
            return container.contains(contained);
        }
        if (!container.contains(contained)){
            return false;
        }
        // if the node is contained, but it comes immediately after the annotations,
        // let's not consider it contained
        if (container instanceof AnnotableNode){
            int bl = beginLineWithoutConsideringAnnotation(container);
            int bc = beginColumnWithoutConsideringAnnotation(container);
            if (bl>contained.getBeginLine()) return false;
            if (bl==contained.getBeginLine() && bc>contained.getBeginColumn()) return false;
            if (container.getEndLine()<contained.getEndLine()) return false;
            if (container.getEndLine()==contained.getEndLine() && container.getEndColumn()<contained.getEndColumn()) return false;
            return true;
        }
        return true;
    }

}
