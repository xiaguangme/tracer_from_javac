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

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

import com.sun.tools.javac.tree.JCTree.*;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;

/** Factory class for trees.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class TreeMaker implements JCTree.Factory {

    /** The context key for the tree factory. */
    protected static final Context.Key<TreeMaker> treeMakerKey =
        new Context.Key<TreeMaker>();

    /** Get the TreeMaker instance. */
    public static TreeMaker instance(Context context) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(context);
	TreeMaker instance = context.get(treeMakerKey);
        if (instance == null)
            instance = new TreeMaker(context);
        return instance;
    }

    /** The position at which subsequent trees will be created.
     */
    public int pos = Position.NOPOS;

    /** The toplevel tree to which created trees belong.
     */
    public JCCompilationUnit toplevel;

    /** The current name table. */
    Name.Table names;

    Types types;

    /** The current symbol table. */
    Symtab syms;

    /** Create a tree maker with null toplevel and NOPOS as initial position.
     */
    protected TreeMaker(Context context) {
        context.put(treeMakerKey, this);
        this.pos = Position.NOPOS;
        this.toplevel = null;
        this.names = Name.Table.instance(context);
        this.syms = Symtab.instance(context);
        this.types = Types.instance(context);
    }

    /** Create a tree maker with a given toplevel and FIRSTPOS as initial position.
     */
    TreeMaker(JCCompilationUnit toplevel, Name.Table names, Types types, Symtab syms) {
        this.pos = Position.FIRSTPOS;
        this.toplevel = toplevel;
        this.names = names;
        this.types = types;
        this.syms = syms;
    }

    /** Create a new tree maker for a given toplevel.
     */
    public TreeMaker forToplevel(JCCompilationUnit toplevel) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toplevel);
	return new TreeMaker(toplevel, names, types, syms);
    }

    /** Reassign current position.
     */
    public TreeMaker at(int pos) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(pos);
	this.pos = pos;
        return this;
    }

    /** Reassign current position.
     */
    public TreeMaker at(DiagnosticPosition pos) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(pos);
	this.pos = (pos == null ? Position.NOPOS : pos.getStartPosition());
        return this;
    }

    /**
     * Create given tree node at current position.
     * @param defs a list of ClassDef, Import, and Skip
     */
    public JCCompilationUnit TopLevel(List<JCAnnotation> packageAnnotations,
                                      JCExpression pid,
                                      List<JCTree> defs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(packageAnnotations, pid, defs);
	assert packageAnnotations != null;
        for (JCTree node : defs)
            assert node instanceof JCClassDecl
                || node instanceof JCImport
                || node instanceof JCSkip
                || node instanceof JCErroneous
                || (node instanceof JCExpressionStatement
                    && ((JCExpressionStatement)node).expr instanceof JCErroneous)
                 : node.getClass().getSimpleName();
        JCCompilationUnit tree = new JCCompilationUnit(packageAnnotations, pid, defs,
                                     null, null, null, null);
        tree.pos = pos;
        return tree;
    }

    public JCImport Import(JCTree qualid, boolean importStatic) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(qualid, importStatic);
	JCImport tree = new JCImport(qualid, importStatic);
        tree.pos = pos;
        return tree;
    }

    public JCClassDecl ClassDef(JCModifiers mods,
                                Name name,
                                List<JCTypeParameter> typarams,
                                JCTree extending,
                                List<JCExpression> implementing,
                                List<JCTree> defs)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(mods, name, typarams, extending, implementing, defs);
	JCClassDecl tree = new JCClassDecl(mods,
                                     name,
                                     typarams,
                                     extending,
                                     implementing,
                                     defs,
                                     null);
        tree.pos = pos;
        return tree;
    }

    public JCMethodDecl MethodDef(JCModifiers mods,
                               Name name,
                               JCExpression restype,
                               List<JCTypeParameter> typarams,
                               List<JCVariableDecl> params,
                               List<JCExpression> thrown,
                               JCBlock body,
                               JCExpression defaultValue)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(mods, name, restype, typarams, params, thrown, body, defaultValue);
	JCMethodDecl tree = new JCMethodDecl(mods,
                                       name,
                                       restype,
                                       typarams,
                                       params,
                                       thrown,
                                       body,
                                       defaultValue,
                                       null);
        tree.pos = pos;
        return tree;
    }

    public JCVariableDecl VarDef(JCModifiers mods, Name name, JCExpression vartype, JCExpression init) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(mods, name, vartype, init);
	JCVariableDecl tree = new JCVariableDecl(mods, name, vartype, init, null);
        tree.pos = pos;
        return tree;
    }

    public JCSkip Skip() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	JCSkip tree = new JCSkip();
        tree.pos = pos;
        return tree;
    }

    public JCBlock Block(long flags, List<JCStatement> stats) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(flags, stats);
	JCBlock tree = new JCBlock(flags, stats);
        tree.pos = pos;
        return tree;
    }

    public JCDoWhileLoop DoLoop(JCStatement body, JCExpression cond) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(body, cond);
	JCDoWhileLoop tree = new JCDoWhileLoop(body, cond);
        tree.pos = pos;
        return tree;
    }

    public JCWhileLoop WhileLoop(JCExpression cond, JCStatement body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(cond, body);
	JCWhileLoop tree = new JCWhileLoop(cond, body);
        tree.pos = pos;
        return tree;
    }

    public JCForLoop ForLoop(List<JCStatement> init,
                           JCExpression cond,
                           List<JCExpressionStatement> step,
                           JCStatement body)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(init, cond, step, body);
	JCForLoop tree = new JCForLoop(init, cond, step, body);
        tree.pos = pos;
        return tree;
    }

    public JCEnhancedForLoop ForeachLoop(JCVariableDecl var, JCExpression expr, JCStatement body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(var, expr, body);
	JCEnhancedForLoop tree = new JCEnhancedForLoop(var, expr, body);
        tree.pos = pos;
        return tree;
    }

    public JCLabeledStatement Labelled(Name label, JCStatement body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(label, body);
	JCLabeledStatement tree = new JCLabeledStatement(label, body);
        tree.pos = pos;
        return tree;
    }

    public JCSwitch Switch(JCExpression selector, List<JCCase> cases) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(selector, cases);
	JCSwitch tree = new JCSwitch(selector, cases);
        tree.pos = pos;
        return tree;
    }

    public JCCase Case(JCExpression pat, List<JCStatement> stats) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(pat, stats);
	JCCase tree = new JCCase(pat, stats);
        tree.pos = pos;
        return tree;
    }

    public JCSynchronized Synchronized(JCExpression lock, JCBlock body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(lock, body);
	JCSynchronized tree = new JCSynchronized(lock, body);
        tree.pos = pos;
        return tree;
    }

    public JCTry Try(JCBlock body, List<JCCatch> catchers, JCBlock finalizer) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(body, catchers, finalizer);
	JCTry tree = new JCTry(body, catchers, finalizer);
        tree.pos = pos;
        return tree;
    }

    public JCCatch Catch(JCVariableDecl param, JCBlock body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(param, body);
	JCCatch tree = new JCCatch(param, body);
        tree.pos = pos;
        return tree;
    }

    public JCConditional Conditional(JCExpression cond,
                                   JCExpression thenpart,
                                   JCExpression elsepart)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(cond, thenpart, elsepart);
	JCConditional tree = new JCConditional(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public JCIf If(JCExpression cond, JCStatement thenpart, JCStatement elsepart) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(cond, thenpart, elsepart);
	JCIf tree = new JCIf(cond, thenpart, elsepart);
        tree.pos = pos;
        return tree;
    }

    public JCExpressionStatement Exec(JCExpression expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(expr);
	JCExpressionStatement tree = new JCExpressionStatement(expr);
        tree.pos = pos;
        return tree;
    }

    public JCBreak Break(Name label) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(label);
	JCBreak tree = new JCBreak(label, null);
        tree.pos = pos;
        return tree;
    }

    public JCContinue Continue(Name label) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(label);
	JCContinue tree = new JCContinue(label, null);
        tree.pos = pos;
        return tree;
    }

    public JCReturn Return(JCExpression expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(expr);
	JCReturn tree = new JCReturn(expr);
        tree.pos = pos;
        return tree;
    }

    public JCThrow Throw(JCTree expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(expr);
	JCThrow tree = new JCThrow(expr);
        tree.pos = pos;
        return tree;
    }

    public JCAssert Assert(JCExpression cond, JCExpression detail) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(cond, detail);
	JCAssert tree = new JCAssert(cond, detail);
        tree.pos = pos;
        return tree;
    }

    public JCMethodInvocation Apply(List<JCExpression> typeargs,
                       JCExpression fn,
                       List<JCExpression> args)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(typeargs, fn, args);
	JCMethodInvocation tree = new JCMethodInvocation(typeargs, fn, args);
        tree.pos = pos;
        return tree;
    }

    public JCNewClass NewClass(JCExpression encl,
                             List<JCExpression> typeargs,
                             JCExpression clazz,
                             List<JCExpression> args,
                             JCClassDecl def)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(encl, typeargs, clazz, args, def);
	JCNewClass tree = new JCNewClass(encl, typeargs, clazz, args, def);
        tree.pos = pos;
        return tree;
    }

    public JCNewArray NewArray(JCExpression elemtype,
                             List<JCExpression> dims,
                             List<JCExpression> elems)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(elemtype, dims, elems);
	JCNewArray tree = new JCNewArray(elemtype, dims, elems);
        tree.pos = pos;
        return tree;
    }

    public JCParens Parens(JCExpression expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(expr);
	JCParens tree = new JCParens(expr);
        tree.pos = pos;
        return tree;
    }

    public JCAssign Assign(JCExpression lhs, JCExpression rhs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(lhs, rhs);
	JCAssign tree = new JCAssign(lhs, rhs);
        tree.pos = pos;
        return tree;
    }

    public JCAssignOp Assignop(int opcode, JCTree lhs, JCTree rhs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(opcode, lhs, rhs);
	JCAssignOp tree = new JCAssignOp(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public JCUnary Unary(int opcode, JCExpression arg) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(opcode, arg);
	JCUnary tree = new JCUnary(opcode, arg);
        tree.pos = pos;
        return tree;
    }

    public JCBinary Binary(int opcode, JCExpression lhs, JCExpression rhs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(opcode, lhs, rhs);
	JCBinary tree = new JCBinary(opcode, lhs, rhs, null);
        tree.pos = pos;
        return tree;
    }

    public JCTypeCast TypeCast(JCTree clazz, JCExpression expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(clazz, expr);
	JCTypeCast tree = new JCTypeCast(clazz, expr);
        tree.pos = pos;
        return tree;
    }

    public JCInstanceOf TypeTest(JCExpression expr, JCTree clazz) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(expr, clazz);
	JCInstanceOf tree = new JCInstanceOf(expr, clazz);
        tree.pos = pos;
        return tree;
    }

    public JCArrayAccess Indexed(JCExpression indexed, JCExpression index) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(indexed, index);
	JCArrayAccess tree = new JCArrayAccess(indexed, index);
        tree.pos = pos;
        return tree;
    }

    public JCFieldAccess Select(JCExpression selected, Name selector) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(selected, selector);
	JCFieldAccess tree = new JCFieldAccess(selected, selector, null);
        tree.pos = pos;
        return tree;
    }

    public JCIdent Ident(Name name) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(name);
	JCIdent tree = new JCIdent(name, null);
        tree.pos = pos;
        return tree;
    }

    public JCLiteral Literal(int tag, Object value) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tag, value);
	JCLiteral tree = new JCLiteral(tag, value);
        tree.pos = pos;
        return tree;
    }

    public JCPrimitiveTypeTree TypeIdent(int typetag) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(typetag);
	JCPrimitiveTypeTree tree = new JCPrimitiveTypeTree(typetag);
        tree.pos = pos;
        return tree;
    }

    public JCArrayTypeTree TypeArray(JCExpression elemtype) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(elemtype);
	JCArrayTypeTree tree = new JCArrayTypeTree(elemtype);
        tree.pos = pos;
        return tree;
    }

    public JCTypeApply TypeApply(JCExpression clazz, List<JCExpression> arguments) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(clazz, arguments);
	JCTypeApply tree = new JCTypeApply(clazz, arguments);
        tree.pos = pos;
        return tree;
    }

    public JCTypeParameter TypeParameter(Name name, List<JCExpression> bounds) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(name, bounds);
	JCTypeParameter tree = new JCTypeParameter(name, bounds);
        tree.pos = pos;
        return tree;
    }

    public JCWildcard Wildcard(TypeBoundKind kind, JCTree type) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(kind, type);
	JCWildcard tree = new JCWildcard(kind, type);
        tree.pos = pos;
        return tree;
    }

    public TypeBoundKind TypeBoundKind(BoundKind kind) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(kind);
	TypeBoundKind tree = new TypeBoundKind(kind);
        tree.pos = pos;
        return tree;
    }

    public JCAnnotation Annotation(JCTree annotationType, List<JCExpression> args) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(annotationType, args);
	JCAnnotation tree = new JCAnnotation(annotationType, args);
        tree.pos = pos;
        return tree;
    }

    public JCModifiers Modifiers(long flags, List<JCAnnotation> annotations) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(flags, annotations);
	JCModifiers tree = new JCModifiers(flags, annotations);
        boolean noFlags = (flags & Flags.StandardFlags) == 0;
        tree.pos = (noFlags && annotations.isEmpty()) ? Position.NOPOS : pos;
        return tree;
    }

    public JCModifiers Modifiers(long flags) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(flags);
	return Modifiers(flags, List.<JCAnnotation>nil());
    }

    public JCErroneous Erroneous() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return Erroneous(List.<JCTree>nil());
    }

    public JCErroneous Erroneous(List<? extends JCTree> errs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(errs);
	JCErroneous tree = new JCErroneous(errs);
        tree.pos = pos;
        return tree;
    }

    public LetExpr LetExpr(List<JCVariableDecl> defs, JCTree expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(defs, expr);
	LetExpr tree = new LetExpr(defs, expr);
        tree.pos = pos;
        return tree;
    }

/* ***************************************************************************
 * Derived building blocks.
 ****************************************************************************/

    public JCClassDecl AnonymousClassDef(JCModifiers mods,
                                         List<JCTree> defs)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(mods, defs);
	return ClassDef(mods,
                        names.empty,
                        List.<JCTypeParameter>nil(),
                        null,
                        List.<JCExpression>nil(),
                        defs);
    }

    public LetExpr LetExpr(JCVariableDecl def, JCTree expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(def, expr);
	LetExpr tree = new LetExpr(List.of(def), expr);
        tree.pos = pos;
        return tree;
    }

    /** Create an identifier from a symbol.
     */
    public JCIdent Ident(Symbol sym) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(sym);
	return (JCIdent)new JCIdent((sym.name != names.empty)
                                ? sym.name
                                : sym.flatName(), sym)
            .setPos(pos)
            .setType(sym.type);
    }

    /** Create a selection node from a qualifier tree and a symbol.
     *  @param base   The qualifier tree.
     */
    public JCExpression Select(JCExpression base, Symbol sym) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(base, sym);
	return new JCFieldAccess(base, sym.name, sym).setPos(pos).setType(sym.type);
    }

    /** Create a qualified identifier from a symbol, adding enough qualifications
     *  to make the reference unique.
     */
    public JCExpression QualIdent(Symbol sym) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(sym);
	return isUnqualifiable(sym)
            ? Ident(sym)
            : Select(QualIdent(sym.owner), sym);
    }

    /** Create an identifier that refers to the variable declared in given variable
     *  declaration.
     */
    public JCExpression Ident(JCVariableDecl param) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(param);
	return Ident(param.sym);
    }

    /** Create a list of identifiers referring to the variables declared
     *  in given list of variable declarations.
     */
    public List<JCExpression> Idents(List<JCVariableDecl> params) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(params);
	ListBuffer<JCExpression> ids = new ListBuffer<JCExpression>();
        for (List<JCVariableDecl> l = params; l.nonEmpty(); l = l.tail)
            ids.append(Ident(l.head));
        return ids.toList();
    }

    /** Create a tree representing `this', given its type.
     */
    public JCExpression This(Type t) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(t);
	return Ident(new VarSymbol(FINAL, names._this, t, t.tsym));
    }

    /** Create a tree representing a class literal.
     */
    public JCExpression ClassLiteral(ClassSymbol clazz) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(clazz);
	return ClassLiteral(clazz.type);
    }

    /** Create a tree representing a class literal.
     */
    public JCExpression ClassLiteral(Type t) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(t);
	VarSymbol lit = new VarSymbol(STATIC | PUBLIC | FINAL,
                                      names._class,
                                      t,
                                      t.tsym);
        return Select(Type(t), lit);
    }

    /** Create a tree representing `super', given its type and owner.
     */
    public JCIdent Super(Type t, TypeSymbol owner) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(t, owner);
	return Ident(new VarSymbol(FINAL, names._super, t, owner));
    }

    /**
     * Create a method invocation from a method tree and a list of
     * argument trees.
     */
    public JCMethodInvocation App(JCExpression meth, List<JCExpression> args) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(meth, args);
	return Apply(null, meth, args).setType(meth.type.getReturnType());
    }

    /**
     * Create a no-arg method invocation from a method tree
     */
    public JCMethodInvocation App(JCExpression meth) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(meth);
	return Apply(null, meth, List.<JCExpression>nil()).setType(meth.type.getReturnType());
    }

    /** Create a method invocation from a method tree and a list of argument trees.
     */
    public JCExpression Create(Symbol ctor, List<JCExpression> args) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(ctor, args);
	Type t = ctor.owner.erasure(types);
        JCNewClass newclass = NewClass(null, null, Type(t), args, null);
        newclass.constructor = ctor;
        newclass.setType(t);
        return newclass;
    }

    /** Create a tree representing given type.
     */
    public JCExpression Type(Type t) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(t);
	if (t == null) return null;
        JCExpression tp;
        switch (t.tag) {
        case BYTE: case CHAR: case SHORT: case INT: case LONG: case FLOAT:
        case DOUBLE: case BOOLEAN: case VOID:
            tp = TypeIdent(t.tag);
            break;
        case TYPEVAR:
            tp = Ident(t.tsym);
            break;
        case WILDCARD: {
            WildcardType a = ((WildcardType) t);
            tp = Wildcard(TypeBoundKind(a.kind), Type(a.type));
            break;
        }
        case CLASS:
            Type outer = t.getEnclosingType();
            JCExpression clazz = outer.tag == CLASS && t.tsym.owner.kind == TYP
                ? Select(Type(outer), t.tsym)
                : QualIdent(t.tsym);
            tp = t.getTypeArguments().isEmpty()
                ? clazz
                : TypeApply(clazz, Types(t.getTypeArguments()));
            break;
        case ARRAY:
            tp = TypeArray(Type(types.elemtype(t)));
            break;
        case ERROR:
            tp = TypeIdent(ERROR);
            break;
        default:
            throw new AssertionError("unexpected type: " + t);
        }
        return tp.setType(t);
    }
//where
        private JCExpression Selectors(JCExpression base, Symbol sym, Symbol limit) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(base, sym, limit);
	if (sym == limit) return base;
            else return Select(Selectors(base, sym.owner, limit), sym);
        }

    /** Create a list of trees representing given list of types.
     */
    public List<JCExpression> Types(List<Type> ts) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(ts);
	ListBuffer<JCExpression> types = new ListBuffer<JCExpression>();
        for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
            types.append(Type(l.head));
        return types.toList();
    }

    /** Create a variable definition from a variable symbol and an initializer
     *  expression.
     */
    public JCVariableDecl VarDef(VarSymbol v, JCExpression init) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(v, init);
	return (JCVariableDecl)
            new JCVariableDecl(
                Modifiers(v.flags(), Annotations(v.getAnnotationMirrors())),
                v.name,
                Type(v.type),
                init,
                v).setPos(pos).setType(v.type);
    }

    /** Create annotation trees from annotations.
     */
    public List<JCAnnotation> Annotations(List<Attribute.Compound> attributes) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(attributes);
	if (attributes == null) return List.nil();
        ListBuffer<JCAnnotation> result = new ListBuffer<JCAnnotation>();
        for (List<Attribute.Compound> i = attributes; i.nonEmpty(); i=i.tail) {
            Attribute a = i.head;
            result.append(Annotation(a));
        }
        return result.toList();
    }

    public JCLiteral Literal(Object value) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(value);
	JCLiteral result = null;
        if (value instanceof String) {
            result = Literal(CLASS, value).
                setType(syms.stringType.constType(value));
        } else if (value instanceof Integer) {
            result = Literal(INT, value).
                setType(syms.intType.constType(value));
        } else if (value instanceof Long) {
            result = Literal(LONG, value).
                setType(syms.longType.constType(value));
        } else if (value instanceof Byte) {
            result = Literal(BYTE, value).
                setType(syms.byteType.constType(value));
        } else if (value instanceof Character) {
            result = Literal(CHAR, value).
                setType(syms.charType.constType(value));
        } else if (value instanceof Double) {
            result = Literal(DOUBLE, value).
                setType(syms.doubleType.constType(value));
        } else if (value instanceof Float) {
            result = Literal(FLOAT, value).
                setType(syms.floatType.constType(value));
        } else if (value instanceof Short) {
            result = Literal(SHORT, value).
                setType(syms.shortType.constType(value));
        } else {
            throw new AssertionError(value);
        }
        return result;
    }

    class AnnotationBuilder implements Attribute.Visitor {
        JCExpression result = null;
        public void visitConstant(Attribute.Constant v) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(v);
	result = Literal(v.value);
        }
        public void visitClass(Attribute.Class clazz) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(clazz);
	result = ClassLiteral(clazz.type).setType(syms.classType);
        }
        public void visitEnum(Attribute.Enum e) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(e);
	result = QualIdent(e.value);
        }
        public void visitError(Attribute.Error e) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(e);
	result = Erroneous();
        }
        public void visitCompound(Attribute.Compound compound) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(compound);
	result = visitCompoundInternal(compound);
        }
        public JCAnnotation visitCompoundInternal(Attribute.Compound compound) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(compound);
	ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            for (List<Pair<Symbol.MethodSymbol,Attribute>> values = compound.values; values.nonEmpty(); values=values.tail) {
                Pair<MethodSymbol,Attribute> pair = values.head;
                JCExpression valueTree = translate(pair.snd);
                args.append(Assign(Ident(pair.fst), valueTree).setType(valueTree.type));
            }
            return Annotation(Type(compound.type), args.toList());
        }
        public void visitArray(Attribute.Array array) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(array);
	ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
            for (int i = 0; i < array.values.length; i++)
                elems.append(translate(array.values[i]));
            result = NewArray(null, List.<JCExpression>nil(), elems.toList()).setType(array.type);
        }
        JCExpression translate(Attribute a) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(a);
	a.accept(this);
            return result;
        }
        JCAnnotation translate(Attribute.Compound a) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(a);
	return visitCompoundInternal(a);
        }
    }
    AnnotationBuilder annotationBuilder = new AnnotationBuilder();

    /** Create an annotation tree from an attribute.
     */
    public JCAnnotation Annotation(Attribute a) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(a);
	return annotationBuilder.translate((Attribute.Compound)a);
    }

    /** Create a method definition from a method symbol and a method body.
     */
    public JCMethodDecl MethodDef(MethodSymbol m, JCBlock body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(m, body);
	return MethodDef(m, m.type, body);
    }

    /** Create a method definition from a method symbol, method type
     *  and a method body.
     */
    public JCMethodDecl MethodDef(MethodSymbol m, Type mtype, JCBlock body) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(m, mtype, body);
	return (JCMethodDecl)
            new JCMethodDecl(
                Modifiers(m.flags(), Annotations(m.getAnnotationMirrors())),
                m.name,
                Type(mtype.getReturnType()),
                TypeParams(mtype.getTypeArguments()),
                Params(mtype.getParameterTypes(), m),
                Types(mtype.getThrownTypes()),
                body,
                null,
                m).setPos(pos).setType(mtype);
    }

    /** Create a type parameter tree from its name and type.
     */
    public JCTypeParameter TypeParam(Name name, TypeVar tvar) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(name, tvar);
	return (JCTypeParameter)
            TypeParameter(name, Types(types.getBounds(tvar))).setPos(pos).setType(tvar);
    }

    /** Create a list of type parameter trees from a list of type variables.
     */
    public List<JCTypeParameter> TypeParams(List<Type> typarams) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(typarams);
	ListBuffer<JCTypeParameter> tparams = new ListBuffer<JCTypeParameter>();
        int i = 0;
        for (List<Type> l = typarams; l.nonEmpty(); l = l.tail)
            tparams.append(TypeParam(l.head.tsym.name, (TypeVar)l.head));
        return tparams.toList();
    }

    /** Create a value parameter tree from its name, type, and owner.
     */
    public JCVariableDecl Param(Name name, Type argtype, Symbol owner) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(name, argtype, owner);
	return VarDef(new VarSymbol(0, name, argtype, owner), null);
    }

    /** Create a a list of value parameter trees x0, ..., xn from a list of
     *  their types and an their owner.
     */
    public List<JCVariableDecl> Params(List<Type> argtypes, Symbol owner) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(argtypes, owner);
	ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        MethodSymbol mth = (owner.kind == MTH) ? ((MethodSymbol)owner) : null;
        if (mth != null && mth.params != null && argtypes.length() == mth.params.length()) {
            for (VarSymbol param : ((MethodSymbol)owner).params)
                params.append(VarDef(param, null));
        } else {
            int i = 0;
            for (List<Type> l = argtypes; l.nonEmpty(); l = l.tail)
                params.append(Param(paramName(i++), l.head, owner));
        }
        return params.toList();
    }

    /** Wrap a method invocation in an expression statement or return statement,
     *  depending on whether the method invocation expression's type is void.
     */
    public JCStatement Call(JCExpression apply) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(apply);
	return apply.type.tag == VOID ? Exec(apply) : Return(apply);
    }

    /** Construct an assignment from a variable symbol and a right hand side.
     */
    public JCStatement Assignment(Symbol v, JCExpression rhs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(v, rhs);
	return Exec(Assign(Ident(v), rhs).setType(v.type));
    }

    /** Construct an index expression from a variable and an expression.
     */
    public JCArrayAccess Indexed(Symbol v, JCExpression index) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(v, index);
	JCArrayAccess tree = new JCArrayAccess(QualIdent(v), index);
        tree.type = ((ArrayType)v.type).elemtype;
        return tree;
    }

    /** Make an attributed type cast expression.
     */
    public JCTypeCast TypeCast(Type type, JCExpression expr) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(type, expr);
	return (JCTypeCast)TypeCast(Type(type), expr).setType(type);
    }

/* ***************************************************************************
 * Helper methods.
 ****************************************************************************/

    /** Can given symbol be referred to in unqualified form?
     */
    boolean isUnqualifiable(Symbol sym) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(sym);
	if (sym.name == names.empty ||
            sym.owner == null ||
            sym.owner.kind == MTH || sym.owner.kind == VAR) {
            return true;
        } else if (sym.kind == TYP && toplevel != null) {
            Scope.Entry e;
            e = toplevel.namedImportScope.lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
            e = toplevel.packge.members().lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
            e = toplevel.starImportScope.lookup(sym.name);
            if (e.scope != null) {
                return
                  e.sym == sym &&
                  e.next().scope == null;
            }
        }
        return false;
    }

    /** The name of synthetic parameter number `i'.
     */
    public Name paramName(int i)   {  
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(i);
	return names.fromString("x" + i); }

    /** The name of synthetic type parameter number `i'.
     */
    public Name typaramName(int i) {  
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(i);
	return names.fromString("A" + i); }
}
          