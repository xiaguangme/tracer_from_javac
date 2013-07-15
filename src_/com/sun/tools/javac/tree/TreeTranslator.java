/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.tools.javac.tree;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.JCTree.*;

/** A subclass of Tree.Visitor, this class defines
 *  a general tree translator pattern. Translation proceeds recursively in
 *  left-to-right order down a tree, constructing translated nodes by
 *  overwriting existing ones. There is one visitor method in this class
 *  for every possible kind of tree node.  To obtain a specific
 *  translator, it suffices to override those visitor methods which
 *  do some interesting work. The translator class itself takes care of all
 *  navigational aspects.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class TreeTranslator extends JCTree.Visitor {

    /** Visitor result field: a tree
     */
    protected JCTree result;

    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends JCTree> T translate(T tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	if (tree == null) {
            return null;
        } else {
            tree.accept(this);
            JCTree result = this.result;
            this.result = null;
            return (T)result; // XXX cast
        }
    }

    /** Visitor method: translate a list of nodes.
     */
    public <T extends JCTree> List<T> translate(List<T> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	if (trees == null) return null;
        for (List<T> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

    /**  Visitor method: translate a list of variable definitions.
     */
    public List<JCVariableDecl> translateVarDefs(List<JCVariableDecl> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	for (List<JCVariableDecl> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

    /**  Visitor method: translate a list of type parameters.
     */
    public List<JCTypeParameter> translateTypeParams(List<JCTypeParameter> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	for (List<JCTypeParameter> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

    /**  Visitor method: translate a list of case parts of switch statements.
     */
    public List<JCCase> translateCases(List<JCCase> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	for (List<JCCase> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

    /**  Visitor method: translate a list of catch clauses in try statements.
     */
    public List<JCCatch> translateCatchers(List<JCCatch> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	for (List<JCCatch> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

    /**  Visitor method: translate a list of catch clauses in try statements.
     */
    public List<JCAnnotation> translateAnnotations(List<JCAnnotation> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	for (List<JCAnnotation> l = trees; l.nonEmpty(); l = l.tail)
            l.head = translate(l.head);
        return trees;
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public void visitTopLevel(JCCompilationUnit tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.pid = translate(tree.pid);
        tree.defs = translate(tree.defs);
        result = tree;
    }

    public void visitImport(JCImport tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.qualid = translate(tree.qualid);
        result = tree;
    }

    public void visitClassDef(JCClassDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.mods = translate(tree.mods);
        tree.typarams = translateTypeParams(tree.typarams);
        tree.extending = translate(tree.extending);
        tree.implementing = translate(tree.implementing);
        tree.defs = translate(tree.defs);
        result = tree;
    }

    public void visitMethodDef(JCMethodDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.mods = translate(tree.mods);
        tree.restype = translate(tree.restype);
        tree.typarams = translateTypeParams(tree.typarams);
        tree.params = translateVarDefs(tree.params);
        tree.thrown = translate(tree.thrown);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitVarDef(JCVariableDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.mods = translate(tree.mods);
        tree.vartype = translate(tree.vartype);
        tree.init = translate(tree.init);
        result = tree;
    }

    public void visitSkip(JCSkip tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitBlock(JCBlock tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.stats = translate(tree.stats);
        result = tree;
    }

    public void visitDoLoop(JCDoWhileLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.body = translate(tree.body);
        tree.cond = translate(tree.cond);
        result = tree;
    }

    public void visitWhileLoop(JCWhileLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.cond = translate(tree.cond);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitForLoop(JCForLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.init = translate(tree.init);
        tree.cond = translate(tree.cond);
        tree.step = translate(tree.step);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.var = translate(tree.var);
        tree.expr = translate(tree.expr);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitLabelled(JCLabeledStatement tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.body = translate(tree.body);
        result = tree;
    }

    public void visitSwitch(JCSwitch tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.selector = translate(tree.selector);
        tree.cases = translateCases(tree.cases);
        result = tree;
    }

    public void visitCase(JCCase tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.pat = translate(tree.pat);
        tree.stats = translate(tree.stats);
        result = tree;
    }

    public void visitSynchronized(JCSynchronized tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.lock = translate(tree.lock);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitTry(JCTry tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.body = translate(tree.body);
        tree.catchers = translateCatchers(tree.catchers);
        tree.finalizer = translate(tree.finalizer);
        result = tree;
    }

    public void visitCatch(JCCatch tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.param = translate(tree.param);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitConditional(JCConditional tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.cond = translate(tree.cond);
        tree.truepart = translate(tree.truepart);
        tree.falsepart = translate(tree.falsepart);
        result = tree;
    }

    public void visitIf(JCIf tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.cond = translate(tree.cond);
        tree.thenpart = translate(tree.thenpart);
        tree.elsepart = translate(tree.elsepart);
        result = tree;
    }

    public void visitExec(JCExpressionStatement tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitBreak(JCBreak tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitContinue(JCContinue tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitReturn(JCReturn tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitThrow(JCThrow tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitAssert(JCAssert tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.cond = translate(tree.cond);
        tree.detail = translate(tree.detail);
        result = tree;
    }

    public void visitApply(JCMethodInvocation tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.meth = translate(tree.meth);
        tree.args = translate(tree.args);
        result = tree;
    }

    public void visitNewClass(JCNewClass tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.encl = translate(tree.encl);
        tree.clazz = translate(tree.clazz);
        tree.args = translate(tree.args);
        tree.def = translate(tree.def);
        result = tree;
    }

    public void visitNewArray(JCNewArray tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.elemtype = translate(tree.elemtype);
        tree.dims = translate(tree.dims);
        tree.elems = translate(tree.elems);
        result = tree;
    }

    public void visitParens(JCParens tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitAssign(JCAssign tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.lhs = translate(tree.lhs);
        tree.rhs = translate(tree.rhs);
        result = tree;
    }

    public void visitAssignop(JCAssignOp tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.lhs = translate(tree.lhs);
        tree.rhs = translate(tree.rhs);
        result = tree;
    }

    public void visitUnary(JCUnary tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.arg = translate(tree.arg);
        result = tree;
    }

    public void visitBinary(JCBinary tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.lhs = translate(tree.lhs);
        tree.rhs = translate(tree.rhs);
        result = tree;
    }

    public void visitTypeCast(JCTypeCast tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.clazz = translate(tree.clazz);
        tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitTypeTest(JCInstanceOf tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.expr = translate(tree.expr);
        tree.clazz = translate(tree.clazz);
        result = tree;
    }

    public void visitIndexed(JCArrayAccess tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.indexed = translate(tree.indexed);
        tree.index = translate(tree.index);
        result = tree;
    }

    public void visitSelect(JCFieldAccess tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.selected = translate(tree.selected);
        result = tree;
    }

    public void visitIdent(JCIdent tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitLiteral(JCLiteral tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitTypeArray(JCArrayTypeTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.elemtype = translate(tree.elemtype);
        result = tree;
    }

    public void visitTypeApply(JCTypeApply tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.clazz = translate(tree.clazz);
        tree.arguments = translate(tree.arguments);
        result = tree;
    }

    public void visitTypeParameter(JCTypeParameter tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.bounds = translate(tree.bounds);
        result = tree;
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.kind = translate(tree.kind);
        tree.inner = translate(tree.inner);
        result = tree;
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitErroneous(JCErroneous tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = tree;
    }

    public void visitLetExpr(LetExpr tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.defs = translateVarDefs(tree.defs);
        tree.expr = translate(tree.expr);
        result = tree;
    }

    public void visitModifiers(JCModifiers tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.annotations = translateAnnotations(tree.annotations);
        result = tree;
    }

    public void visitAnnotation(JCAnnotation tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	tree.annotationType = translate(tree.annotationType);
        tree.args = translate(tree.args);
        result = tree;
    }

    public void visitTree(JCTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	throw new AssertionError(tree);
    }
}
          