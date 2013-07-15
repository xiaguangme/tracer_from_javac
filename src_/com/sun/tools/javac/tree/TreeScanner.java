/*
 * Copyright (c) 2001, 2006, Oracle and/or its affiliates. All rights reserved.
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
 *  a general tree scanner pattern. Translation proceeds recursively in
 *  left-to-right order down a tree. There is one visitor method in this class
 *  for every possible kind of tree node.  To obtain a specific
 *  scanner, it suffices to override those visitor methods which
 *  do some interesting work. The scanner class itself takes care of all
 *  navigational aspects.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class TreeScanner extends Visitor {

    /** Visitor method: Scan a single node.
     */
    public void scan(JCTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(List<? extends JCTree> trees) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	if (trees != null)
        for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
            scan(l.head);
    }


/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public void visitTopLevel(JCCompilationUnit tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.packageAnnotations);
        scan(tree.pid);
        scan(tree.defs);
    }

    public void visitImport(JCImport tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.qualid);
    }

    public void visitClassDef(JCClassDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.mods);
        scan(tree.typarams);
        scan(tree.extending);
        scan(tree.implementing);
        scan(tree.defs);
    }

    public void visitMethodDef(JCMethodDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.mods);
        scan(tree.restype);
        scan(tree.typarams);
        scan(tree.params);
        scan(tree.thrown);
        scan(tree.defaultValue);
        scan(tree.body);
    }

    public void visitVarDef(JCVariableDecl tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.mods);
        scan(tree.vartype);
        scan(tree.init);
    }

    public void visitSkip(JCSkip tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitBlock(JCBlock tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.stats);
    }

    public void visitDoLoop(JCDoWhileLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.body);
        scan(tree.cond);
    }

    public void visitWhileLoop(JCWhileLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.cond);
        scan(tree.body);
    }

    public void visitForLoop(JCForLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.init);
        scan(tree.cond);
        scan(tree.step);
        scan(tree.body);
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.var);
        scan(tree.expr);
        scan(tree.body);
    }

    public void visitLabelled(JCLabeledStatement tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.body);
    }

    public void visitSwitch(JCSwitch tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.selector);
        scan(tree.cases);
    }

    public void visitCase(JCCase tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.pat);
        scan(tree.stats);
    }

    public void visitSynchronized(JCSynchronized tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.lock);
        scan(tree.body);
    }

    public void visitTry(JCTry tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.body);
        scan(tree.catchers);
        scan(tree.finalizer);
    }

    public void visitCatch(JCCatch tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.param);
        scan(tree.body);
    }

    public void visitConditional(JCConditional tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.cond);
        scan(tree.truepart);
        scan(tree.falsepart);
    }

    public void visitIf(JCIf tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.cond);
        scan(tree.thenpart);
        scan(tree.elsepart);
    }

    public void visitExec(JCExpressionStatement tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.expr);
    }

    public void visitBreak(JCBreak tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitContinue(JCContinue tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitReturn(JCReturn tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.expr);
    }

    public void visitThrow(JCThrow tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.expr);
    }

    public void visitAssert(JCAssert tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.cond);
        scan(tree.detail);
    }

    public void visitApply(JCMethodInvocation tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.meth);
        scan(tree.args);
    }

    public void visitNewClass(JCNewClass tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.encl);
        scan(tree.clazz);
        scan(tree.args);
        scan(tree.def);
    }

    public void visitNewArray(JCNewArray tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.elemtype);
        scan(tree.dims);
        scan(tree.elems);
    }

    public void visitParens(JCParens tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.expr);
    }

    public void visitAssign(JCAssign tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitAssignop(JCAssignOp tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitUnary(JCUnary tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.arg);
    }

    public void visitBinary(JCBinary tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitTypeCast(JCTypeCast tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.clazz);
        scan(tree.expr);
    }

    public void visitTypeTest(JCInstanceOf tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.expr);
        scan(tree.clazz);
    }

    public void visitIndexed(JCArrayAccess tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.indexed);
        scan(tree.index);
    }

    public void visitSelect(JCFieldAccess tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.selected);
    }

    public void visitIdent(JCIdent tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitLiteral(JCLiteral tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitTypeArray(JCArrayTypeTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.elemtype);
    }

    public void visitTypeApply(JCTypeApply tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.clazz);
        scan(tree.arguments);
    }

    public void visitTypeParameter(JCTypeParameter tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.bounds);
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.kind);
        if (tree.inner != null)
            scan(tree.inner);
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind that) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(that);
	}

    public void visitModifiers(JCModifiers tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.annotations);
    }

    public void visitAnnotation(JCAnnotation tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.annotationType);
        scan(tree.args);
    }

    public void visitErroneous(JCErroneous tree) {
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	}

    public void visitLetExpr(LetExpr tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	scan(tree.defs);
        scan(tree.expr);
    }

    public void visitTree(JCTree tree) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	assert false;
    }
}
          