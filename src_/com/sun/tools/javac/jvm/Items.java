/*
 * Copyright (c) 1999, 2007, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tools.javac.jvm;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;

import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.jvm.Code.*;
import com.sun.tools.javac.tree.JCTree;

import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javac.jvm.ByteCodes.*;

/** A helper class for code generation. Items are objects
 *  that stand for addressable entities in the bytecode. Each item
 *  supports a fixed protocol for loading the item on the stack, storing
 *  into it, converting it into a jump condition, and several others.
 *  There are many individual forms of items, such as local, static,
 *  indexed, or instance variables, values on the top of stack, the
 *  special values this or super, etc. Individual items are represented as
 *  inner classes in class Items.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class Items {

    /** The current constant pool.
     */
    Pool pool;

    /** The current code buffer.
     */
    Code code;

    /** The current symbol table.
     */
    Symtab syms;

    /** Type utilities. */
    Types types;

    /** Items that exist only once (flyweight pattern).
     */
    private final Item voidItem;
    private final Item thisItem;
    private final Item superItem;
    private final Item[] stackItem = new Item[TypeCodeCount];

    public Items(Pool pool, Code code, Symtab syms, Types types) {
        this.code = code;
        this.pool = pool;
        this.types = types;
        voidItem = new Item(VOIDcode) {
                public String toString() {  
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "void"; }
            };
        thisItem = new SelfItem(false);
        superItem = new SelfItem(true);
        for (int i = 0; i < VOIDcode; i++) stackItem[i] = new StackItem(i);
        stackItem[VOIDcode] = voidItem;
        this.syms = syms;
    }

    /** Make a void item
     */
    Item makeVoidItem() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return voidItem;
    }
    /** Make an item representing `this'.
     */
    Item makeThisItem() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return thisItem;
    }

    /** Make an item representing `super'.
     */
    Item makeSuperItem() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return superItem;
    }

    /** Make an item representing a value on stack.
     *  @param type    The value's type.
     */
    Item makeStackItem(Type type) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(type);
	return stackItem[Code.typecode(type)];
    }

    /** Make an item representing an indexed expression.
     *  @param type    The expression's type.
     */
    Item makeIndexedItem(Type type) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(type);
	return new IndexedItem(type);
    }

    /** Make an item representing a local variable.
     *  @param v    The represented variable.
     */
    LocalItem makeLocalItem(VarSymbol v) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(v);
	return new LocalItem(v.erasure(types), v.adr);
    }

    /** Make an item representing a local anonymous variable.
     *  @param type  The represented variable's type.
     *  @param reg   The represented variable's register.
     */
    private LocalItem makeLocalItem(Type type, int reg) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(type, reg);
	return new LocalItem(type, reg);
    }

    /** Make an item representing a static variable or method.
     *  @param member   The represented symbol.
     */
    Item makeStaticItem(Symbol member) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(member);
	return new StaticItem(member);
    }

    /** Make an item representing an instance variable or method.
     *  @param member       The represented symbol.
     *  @param nonvirtual   Is the reference not virtual? (true for constructors
     *                      and private members).
     */
    Item makeMemberItem(Symbol member, boolean nonvirtual) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(member, nonvirtual);
	return new MemberItem(member, nonvirtual);
    }

    /** Make an item representing a literal.
     *  @param type     The literal's type.
     *  @param value    The literal's value.
     */
    Item makeImmediateItem(Type type, Object value) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(type, value);
	return new ImmediateItem(type, value);
    }

    /** Make an item representing an assignment expression.
     *  @param lhs      The item representing the assignment's left hand side.
     */
    Item makeAssignItem(Item lhs) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(lhs);
	return new AssignItem(lhs);
    }

    /** Make an item representing a conditional or unconditional jump.
     *  @param opcode      The jump's opcode.
     *  @param trueJumps   A chain encomassing all jumps that can be taken
     *                     if the condition evaluates to true.
     *  @param falseJumps  A chain encomassing all jumps that can be taken
     *                     if the condition evaluates to false.
     */
    CondItem makeCondItem(int opcode, Chain trueJumps, Chain falseJumps) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(opcode, trueJumps, falseJumps);
	return new CondItem(opcode, trueJumps, falseJumps);
    }

    /** Make an item representing a conditional or unconditional jump.
     *  @param opcode      The jump's opcode.
     */
    CondItem makeCondItem(int opcode) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(opcode);
	return makeCondItem(opcode, null, null);
    }

    /** The base class of all items, which implements default behavior.
     */
    abstract class Item {

        /** The type code of values represented by this item.
         */
        int typecode;

        Item(int typecode) {
            this.typecode = typecode;
        }

        /** Generate code to load this item onto stack.
         */
        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	throw new AssertionError();
        }

        /** Generate code to store top of stack into this item.
         */
        void store() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	throw new AssertionError("store unsupported: " + this);
        }

        /** Generate code to invoke method represented by this item.
         */
        Item invoke() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	throw new AssertionError(this);
        }

        /** Generate code to use this item twice.
         */
        void duplicate() { 
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	}

        /** Generate code to avoid having to use this item.
         */
        void drop() { 
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	}

        /** Generate code to stash a copy of top of stack - of typecode toscode -
         *  under this item.
         */
        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	stackItem[toscode].duplicate();
        }

        /** Generate code to turn item into a testable condition.
         */
        CondItem mkCond() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	load();
            return makeCondItem(ifne);
        }

        /** Generate code to coerce item to given type code.
         *  @param targetcode    The type code to coerce to.
         */
        Item coerce(int targetcode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(targetcode);
	if (typecode == targetcode)
                return this;
            else {
                load();
                int typecode1 = Code.truncate(typecode);
                int targetcode1 = Code.truncate(targetcode);
                if (typecode1 != targetcode1) {
                    int offset = targetcode1 > typecode1 ? targetcode1 - 1
                        : targetcode1;
                    code.emitop0(i2l + typecode1 * 3 + offset);
                }
                if (targetcode != targetcode1) {
                    code.emitop0(int2byte + targetcode - BYTEcode);
                }
                return stackItem[targetcode];
            }
        }

        /** Generate code to coerce item to given type.
         *  @param targettype    The type to coerce to.
         */
        Item coerce(Type targettype) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(targettype);
	return coerce(Code.typecode(targettype));
        }

        /** Return the width of this item on stack as a number of words.
         */
        int width() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return 0;
        }

        public abstract String toString();
    }

    /** An item representing a value on stack.
     */
    class StackItem extends Item {

        StackItem(int typecode) {
            super(typecode);
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return this;
        }

        void duplicate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(width() == 2 ? dup2 : dup);
        }

        void drop() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(width() == 2 ? pop2 : pop);
        }

        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	code.emitop0(
                (width() == 2 ? dup_x2 : dup_x1) + 3 * (Code.width(toscode) - 1));
        }

        int width() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return Code.width(typecode);
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "stack(" + typecodeNames[typecode] + ")";
        }
    }

    /** An item representing an indexed expression.
     */
    class IndexedItem extends Item {

        IndexedItem(Type type) {
            super(Code.typecode(type));
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(iaload + typecode);
            return stackItem[typecode];
        }

        void store() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(iastore + typecode);
        }

        void duplicate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(dup2);
        }

        void drop() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(pop2);
        }

        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	code.emitop0(dup_x2 + 3 * (Code.width(toscode) - 1));
        }

        int width() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return 2;
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "indexed(" + ByteCodes.typecodeNames[typecode] + ")";
        }
    }

    /** An item representing `this' or `super'.
     */
    class SelfItem extends Item {

        /** Flag which determines whether this item represents `this' or `super'.
         */
        boolean isSuper;

        SelfItem(boolean isSuper) {
            super(OBJECTcode);
            this.isSuper = isSuper;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop0(aload_0);
            return stackItem[typecode];
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return isSuper ? "super" : "this";
        }
    }

    /** An item representing a local variable.
     */
    class LocalItem extends Item {

        /** The variable's register.
         */
        int reg;

        /** The variable's type.
         */
        Type type;

        LocalItem(Type type, int reg) {
            super(Code.typecode(type));
            assert reg >= 0;
            this.type = type;
            this.reg = reg;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	if (reg <= 3)
                code.emitop0(iload_0 + Code.truncate(typecode) * 4 + reg);
            else
                code.emitop1w(iload + Code.truncate(typecode), reg);
            return stackItem[typecode];
        }

        void store() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	if (reg <= 3)
                code.emitop0(istore_0 + Code.truncate(typecode) * 4 + reg);
            else
                code.emitop1w(istore + Code.truncate(typecode), reg);
            code.setDefined(reg);
        }

        void incr(int x) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(x);
	if (typecode == INTcode && x >= -32768 && x <= 32767) {
                code.emitop1w(iinc, reg, x);
            } else {
                load();
                if (x >= 0) {
                    makeImmediateItem(syms.intType, x).load();
                    code.emitop0(iadd);
                } else {
                    makeImmediateItem(syms.intType, -x).load();
                    code.emitop0(isub);
                }
                makeStackItem(syms.intType).coerce(typecode);
                store();
            }
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "localItem(type=" + type + "; reg=" + reg + ")";
        }
    }

    /** An item representing a static variable or method.
     */
    class StaticItem extends Item {

        /** The represented symbol.
         */
        Symbol member;

        StaticItem(Symbol member) {
            super(Code.typecode(member.erasure(types)));
            this.member = member;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop2(getstatic, pool.put(member));
            return stackItem[typecode];
        }

        void store() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop2(putstatic, pool.put(member));
        }

        Item invoke() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	MethodType mtype = (MethodType)member.erasure(types);
            int argsize = Code.width(mtype.argtypes);
            int rescode = Code.typecode(mtype.restype);
            int sdiff = Code.width(rescode) - argsize;
            code.emitInvokestatic(pool.put(member), mtype);
            return stackItem[rescode];
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "static(" + member + ")";
        }
    }

    /** An item representing an instance variable or method.
     */
    class MemberItem extends Item {

        /** The represented symbol.
         */
        Symbol member;

        /** Flag that determines whether or not access is virtual.
         */
        boolean nonvirtual;

        MemberItem(Symbol member, boolean nonvirtual) {
            super(Code.typecode(member.erasure(types)));
            this.member = member;
            this.nonvirtual = nonvirtual;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop2(getfield, pool.put(member));
            return stackItem[typecode];
        }

        void store() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	code.emitop2(putfield, pool.put(member));
        }

        Item invoke() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	MethodType mtype = (MethodType)member.externalType(types);
            int rescode = Code.typecode(mtype.restype);
            if ((member.owner.flags() & Flags.INTERFACE) != 0) {
                code.emitInvokeinterface(pool.put(member), mtype);
            } else if (nonvirtual) {
                code.emitInvokespecial(pool.put(member), mtype);
            } else {
                code.emitInvokevirtual(pool.put(member), mtype);
            }
            return stackItem[rescode];
        }

        void duplicate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	stackItem[OBJECTcode].duplicate();
        }

        void drop() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	stackItem[OBJECTcode].drop();
        }

        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	stackItem[OBJECTcode].stash(toscode);
        }

        int width() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return 1;
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "member(" + member + (nonvirtual ? " nonvirtual)" : ")");
        }
    }

    /** An item representing a literal.
     */
    class ImmediateItem extends Item {

        /** The literal's value.
         */
        Object value;

        ImmediateItem(Type type, Object value) {
            super(Code.typecode(type));
            this.value = value;
        }

        private void ldc() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	int idx = pool.put(value);
            if (typecode == LONGcode || typecode == DOUBLEcode) {
                code.emitop2(ldc2w, idx);
            } else if (idx <= 255) {
                code.emitop1(ldc1, idx);
            } else {
                code.emitop2(ldc2, idx);
            }
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	switch (typecode) {
            case INTcode: case BYTEcode: case SHORTcode: case CHARcode:
                int ival = ((Number)value).intValue();
                if (-1 <= ival && ival <= 5)
                    code.emitop0(iconst_0 + ival);
                else if (Byte.MIN_VALUE <= ival && ival <= Byte.MAX_VALUE)
                    code.emitop1(bipush, ival);
                else if (Short.MIN_VALUE <= ival && ival <= Short.MAX_VALUE)
                    code.emitop2(sipush, ival);
                else
                    ldc();
                break;
            case LONGcode:
                long lval = ((Number)value).longValue();
                if (lval == 0 || lval == 1)
                    code.emitop0(lconst_0 + (int)lval);
                else
                    ldc();
                break;
            case FLOATcode:
                float fval = ((Number)value).floatValue();
                if (isPosZero(fval) || fval == 1.0 || fval == 2.0)
                    code.emitop0(fconst_0 + (int)fval);
                else {
                    ldc();
                }
                break;
            case DOUBLEcode:
                double dval = ((Number)value).doubleValue();
                if (isPosZero(dval) || dval == 1.0)
                    code.emitop0(dconst_0 + (int)dval);
                else
                    ldc();
                break;
            case OBJECTcode:
                ldc();
                break;
            default:
                assert false;
            }
            return stackItem[typecode];
        }
        //where
            /** Return true iff float number is positive 0.
             */
            private boolean isPosZero(float x) {
                 
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(x);
	return x == 0.0f && 1.0f / x > 0.0f;
            }
            /** Return true iff double number is positive 0.
             */
            private boolean isPosZero(double x) {
                 
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(x);
	return x == 0.0d && 1.0d / x > 0.0d;
            }

        CondItem mkCond() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	int ival = ((Number)value).intValue();
            return makeCondItem(ival != 0 ? goto_ : dontgoto);
        }

        Item coerce(int targetcode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(targetcode);
	if (typecode == targetcode) {
                return this;
            } else {
                switch (targetcode) {
                case INTcode:
                    if (Code.truncate(typecode) == INTcode)
                        return this;
                    else
                        return new ImmediateItem(
                            syms.intType,
                            ((Number)value).intValue());
                case LONGcode:
                    return new ImmediateItem(
                        syms.longType,
                        ((Number)value).longValue());
                case FLOATcode:
                    return new ImmediateItem(
                        syms.floatType,
                        ((Number)value).floatValue());
                case DOUBLEcode:
                    return new ImmediateItem(
                        syms.doubleType,
                        ((Number)value).doubleValue());
                case BYTEcode:
                    return new ImmediateItem(
                        syms.byteType,
                        (int)(byte)((Number)value).intValue());
                case CHARcode:
                    return new ImmediateItem(
                        syms.charType,
                        (int)(char)((Number)value).intValue());
                case SHORTcode:
                    return new ImmediateItem(
                        syms.shortType,
                        (int)(short)((Number)value).intValue());
                default:
                    return super.coerce(targetcode);
                }
            }
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "immediate(" + value + ")";
        }
    }

    /** An item representing an assignment expressions.
     */
    class AssignItem extends Item {

        /** The item representing the assignment's left hand side.
         */
        Item lhs;

        AssignItem(Item lhs) {
            super(lhs.typecode);
            this.lhs = lhs;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	lhs.stash(typecode);
            lhs.store();
            return stackItem[typecode];
        }

        void duplicate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	load().duplicate();
        }

        void drop() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	lhs.store();
        }

        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	assert false;
        }

        int width() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return lhs.width() + Code.width(typecode);
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "assign(lhs = " + lhs + ")";
        }
    }

    /** An item representing a conditional or unconditional jump.
     */
    class CondItem extends Item {

        /** A chain encomassing all jumps that can be taken
         *  if the condition evaluates to true.
         */
        Chain trueJumps;

        /** A chain encomassing all jumps that can be taken
         *  if the condition evaluates to false.
         */
        Chain falseJumps;

        /** The jump's opcode.
         */
        int opcode;

        /*
         *  An abstract syntax tree of this item. It is needed
         *  for branch entries in 'CharacterRangeTable' attribute.
         */
        JCTree tree;

        CondItem(int opcode, Chain truejumps, Chain falsejumps) {
            super(BYTEcode);
            this.opcode = opcode;
            this.trueJumps = truejumps;
            this.falseJumps = falsejumps;
        }

        Item load() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	Chain trueChain = null;
            Chain falseChain = jumpFalse();
            if (!isFalse()) {
                code.resolve(trueJumps);
                code.emitop0(iconst_1);
                trueChain = code.branch(goto_);
            }
            if (falseChain != null) {
                code.resolve(falseChain);
                code.emitop0(iconst_0);
            }
            code.resolve(trueChain);
            return stackItem[typecode];
        }

        void duplicate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	load().duplicate();
        }

        void drop() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	load().drop();
        }

        void stash(int toscode) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(toscode);
	assert false;
        }

        CondItem mkCond() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return this;
        }

        Chain jumpTrue() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	if (tree == null) return code.mergeChains(trueJumps, code.branch(opcode));
            // we should proceed further in -Xjcov mode only
            int startpc = code.curPc();
            Chain c = code.mergeChains(trueJumps, code.branch(opcode));
            code.crt.put(tree, CRTable.CRT_BRANCH_TRUE, startpc, code.curPc());
            return c;
        }

        Chain jumpFalse() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	if (tree == null) return code.mergeChains(falseJumps, code.branch(code.negate(opcode)));
            // we should proceed further in -Xjcov mode only
            int startpc = code.curPc();
            Chain c = code.mergeChains(falseJumps, code.branch(code.negate(opcode)));
            code.crt.put(tree, CRTable.CRT_BRANCH_FALSE, startpc, code.curPc());
            return c;
        }

        CondItem negate() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	CondItem c = new CondItem(code.negate(opcode), falseJumps, trueJumps);
            c.tree = tree;
            return c;
        }

        int width() {
            // a CondItem doesn't have a size on the stack per se.
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	throw new AssertionError();
        }

        boolean isTrue() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return falseJumps == null && opcode == goto_;
        }

        boolean isFalse() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return trueJumps == null && opcode == dontgoto;
        }

        public String toString() {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return "cond(" + Code.mnem(opcode) + ")";
        }
    }
}
          