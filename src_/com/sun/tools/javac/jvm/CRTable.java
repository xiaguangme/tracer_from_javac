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

package com.sun.tools.javac.jvm;

import java.util.*;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.tree.JCTree.*;

/** This class contains the CharacterRangeTable for some method
 *  and the hashtable for mapping trees or lists of trees to their
 *  ending positions.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class CRTable
implements CRTFlags {

    private final boolean crtDebug = false;

    /** The list of CRTable entries.
     */
    private ListBuffer<CRTEntry> entries = new ListBuffer<CRTEntry>();

    /** The hashtable for source positions.
     */
    private Map<Object,SourceRange> positions = new HashMap<Object,SourceRange>();

    /** The hashtable for ending positions stored in the parser.
     */
    private Map<JCTree, Integer> endPositions;

    /** The tree of the method this table is intended for.
     *  We should traverse this tree to get source ranges.
     */
    JCTree.JCMethodDecl methodTree;

    /** Constructor
     */
    public CRTable(JCTree.JCMethodDecl tree, Map<JCTree, Integer> endPositions) {
        this.methodTree = tree;
        this.endPositions = endPositions;
    }

    /** Create a new CRTEntry and add it to the entries.
     *  @param tree     The tree or the list of trees for which
     *                  we are storing the code pointers.
     *  @param flags    The set of flags designating type of the entry.
     *  @param startPc  The starting code position.
     *  @param endPc    The ending code position.
     */
    public void put(Object tree, int flags, int startPc, int endPc) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree, flags, startPc, endPc);
	entries.append(new CRTEntry(tree, flags, startPc, endPc));
    }

    /** Compute source positions and write CRT to the databuf.
     *  @param databuf  The buffer to write bytecodes to.
     */
    public int writeCRT(ByteBuffer databuf, Position.LineMap lineMap, Log log) {

         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(databuf, lineMap, log);
	int crtEntries = 0;

        // compute source positions for the method
        new SourceComputer().csp(methodTree);

        for (List<CRTEntry> l = entries.toList(); l.nonEmpty(); l = l.tail) {

            CRTEntry entry = l.head;

            // eliminate entries that do not produce bytecodes:
            // for example, empty blocks and statements
            if (entry.startPc == entry.endPc)
                continue;

            SourceRange pos = positions.get(entry.tree);
            assert pos != null : "CRT: tree source positions are undefined";
            if ((pos.startPos == Position.NOPOS) || (pos.endPos == Position.NOPOS))
                continue;

            if (crtDebug) {
                System.out.println("Tree: " + entry.tree + ", type:" + getTypes(entry.flags));
                System.out.print("Start: pos = " + pos.startPos + ", pc = " + entry.startPc);
            }

            // encode startPos into line/column representation
            int startPos = encodePosition(pos.startPos, lineMap, log);
            if (startPos == Position.NOPOS)
                continue;

            if (crtDebug) {
                System.out.print("End:   pos = " + pos.endPos + ", pc = " + (entry.endPc - 1));
            }

            // encode endPos into line/column representation
            int endPos = encodePosition(pos.endPos, lineMap, log);
            if (endPos == Position.NOPOS)
                continue;

            // write attribute
            databuf.appendChar(entry.startPc);
            // 'endPc - 1' because endPc actually points to start of the next command
            databuf.appendChar(entry.endPc - 1);
            databuf.appendInt(startPos);
            databuf.appendInt(endPos);
            databuf.appendChar(entry.flags);

            crtEntries++;
        }

        return crtEntries;
    }

    /** Return the number of the entries.
     */
    public int length() {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return entries.length();
    }

    /** Return string describing flags enabled.
     */
    private String getTypes(int flags) {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(flags);
	String types = "";
        if ((flags & CRT_STATEMENT)       != 0) types += " CRT_STATEMENT";
        if ((flags & CRT_BLOCK)           != 0) types += " CRT_BLOCK";
        if ((flags & CRT_ASSIGNMENT)      != 0) types += " CRT_ASSIGNMENT";
        if ((flags & CRT_FLOW_CONTROLLER) != 0) types += " CRT_FLOW_CONTROLLER";
        if ((flags & CRT_FLOW_TARGET)     != 0) types += " CRT_FLOW_TARGET";
        if ((flags & CRT_INVOKE)          != 0) types += " CRT_INVOKE";
        if ((flags & CRT_CREATE)          != 0) types += " CRT_CREATE";
        if ((flags & CRT_BRANCH_TRUE)     != 0) types += " CRT_BRANCH_TRUE";
        if ((flags & CRT_BRANCH_FALSE)    != 0) types += " CRT_BRANCH_FALSE";
        return types;
    }

    /** Source file positions in CRT are integers in the format:
     *  line-number << LINESHIFT + column-number
     */
     private int encodePosition(int pos, Position.LineMap lineMap, Log log) {
          
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(pos, lineMap, log);
	int line = lineMap.getLineNumber(pos);
         int col = lineMap.getColumnNumber(pos);
         int new_pos = Position.encodePosition(line, col);
         if (crtDebug) {
             System.out.println(", line = " + line + ", column = " + col +
                                ", new_pos = " + new_pos);
         }
         if (new_pos == Position.NOPOS)
             log.warning(pos, "position.overflow", line);

        return new_pos;
     }

/* ************************************************************************
 * Traversal methods
 *************************************************************************/

    /**
     *  This class contains methods to compute source positions for trees.
     *  Extends Tree.Visitor to traverse the abstract syntax tree.
     */
    class SourceComputer extends JCTree.Visitor {

        /** The result of the tree traversal methods.
         */
        SourceRange result;

        /** Visitor method: compute source positions for a single node.
         */
        public SourceRange csp(JCTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	if (tree == null) return null;
            tree.accept(this);
            if (result != null) {
                positions.put(tree, result);
            }
            return result;
        }

        /** Visitor method: compute source positions for a list of nodes.
         */
        public SourceRange csp(List<? extends JCTree> trees) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        /**  Visitor method: compute source positions for
         *    a list of case blocks of switch statements.
         */
        public SourceRange cspCases(List<JCCase> trees) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<JCCase> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        /**  Visitor method: compute source positions for
         *   a list of catch clauses in try statements.
         */
        public SourceRange cspCatchers(List<JCCatch> trees) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(trees);
	if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<JCCatch> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        public void visitMethodDef(JCMethodDecl tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitVarDef(JCVariableDecl tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            csp(tree.vartype);
            sr.mergeWith(csp(tree.init));
            result = sr;
        }

        public void visitSkip(JCSkip tree) {
            // endPos is the same as startPos for the empty statement
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), startPos(tree));
            result = sr;
        }

        public void visitBlock(JCBlock tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            csp(tree.stats);    // doesn't compare because block's ending position is defined
            result = sr;
        }

        public void visitDoLoop(JCDoWhileLoop tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            sr.mergeWith(csp(tree.cond));
            result = sr;
        }

        public void visitWhileLoop(JCWhileLoop tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitForLoop(JCForLoop tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.init));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.step));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitForeachLoop(JCEnhancedForLoop tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.var));
            sr.mergeWith(csp(tree.expr));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitLabelled(JCLabeledStatement tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitSwitch(JCSwitch tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.selector));
            sr.mergeWith(cspCases(tree.cases));
            result = sr;
        }

        public void visitCase(JCCase tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.pat));
            sr.mergeWith(csp(tree.stats));
            result = sr;
        }

        public void visitSynchronized(JCSynchronized tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lock));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitTry(JCTry tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            sr.mergeWith(cspCatchers(tree.catchers));
            sr.mergeWith(csp(tree.finalizer));
            result = sr;
        }

        public void visitCatch(JCCatch tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.param));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitConditional(JCConditional tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.truepart));
            sr.mergeWith(csp(tree.falsepart));
            result = sr;
        }

        public void visitIf(JCIf tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.thenpart));
            sr.mergeWith(csp(tree.elsepart));
            result = sr;
        }

        public void visitExec(JCExpressionStatement tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitBreak(JCBreak tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitContinue(JCContinue tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitReturn(JCReturn tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitThrow(JCThrow tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitAssert(JCAssert tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.detail));
            result = sr;
        }

        public void visitApply(JCMethodInvocation tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.meth));
            sr.mergeWith(csp(tree.args));
            result = sr;
        }

        public void visitNewClass(JCNewClass tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.encl));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.args));
            sr.mergeWith(csp(tree.def));
            result = sr;
        }

        public void visitNewArray(JCNewArray tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.elemtype));
            sr.mergeWith(csp(tree.dims));
            sr.mergeWith(csp(tree.elems));
            result = sr;
        }

        public void visitParens(JCParens tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitAssign(JCAssign tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitAssignop(JCAssignOp tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitUnary(JCUnary tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.arg));
            result = sr;
        }

        public void visitBinary(JCBinary tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitTypeCast(JCTypeCast tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitTypeTest(JCInstanceOf tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            sr.mergeWith(csp(tree.clazz));
            result = sr;
        }

        public void visitIndexed(JCArrayAccess tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.indexed));
            sr.mergeWith(csp(tree.index));
            result = sr;
        }

        public void visitSelect(JCFieldAccess tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.selected));
            result = sr;
        }

        public void visitIdent(JCIdent tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitLiteral(JCLiteral tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitTypeIdent(JCPrimitiveTypeTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitTypeArray(JCArrayTypeTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.elemtype));
            result = sr;
        }

        public void visitTypeApply(JCTypeApply tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.arguments));
            result = sr;
        }

        public void visitTypeParameter(JCTypeParameter tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.bounds));
            result = sr;
        }

        public void visitWildcard(JCWildcard tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = null;
        }

        public void visitErroneous(JCErroneous tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	result = null;
        }

        public void visitTree(JCTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	assert false;
        }

        /** The start position of given tree.
         */
        public int startPos(JCTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	if (tree == null) return Position.NOPOS;
            return tree.pos;
        }

        /** The end position of given tree, if it has
         *  defined endpos, NOPOS otherwise.
         */
        public int endPos(JCTree tree) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(tree);
	if (tree == null) return Position.NOPOS;
            if (tree.getTag() == JCTree.BLOCK)
                return ((JCBlock) tree).endpos;
            Integer endpos = endPositions.get(tree);
            if (endpos != null)
                return endpos.intValue();
            return Position.NOPOS;
        }
    }

    /** This class contains a CharacterRangeTableEntry.
     */
    static class CRTEntry {

        /** A tree or a list of trees to obtain source positions.
         */
        Object tree;

        /** The flags described in the CharacterRangeTable spec.
         */
        int flags;

        /** The starting code position of this entry.
         */
        int startPc;

        /** The ending code position of this entry.
         */
        int endPc;

        /** Constructor */
        CRTEntry(Object tree, int flags, int startPc, int endPc) {
            this.tree = tree;
            this.flags = flags;
            this.startPc = startPc;
            this.endPc = endPc;
        }
    }


    /** This class contains source positions
     *  for some tree or list of trees.
     */
    static class SourceRange {

        /** The starting source position.
         */
        int startPos;

        /** The ending source position.
         */
        int endPos;

        /** Constructor */
        SourceRange() {
            startPos = Position.NOPOS;
            endPos = Position.NOPOS;
        }

        /** Constructor */
        SourceRange(int startPos, int endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        /** Compare the starting and the ending positions
         *  of the source range and combines them assigning
         *  the widest range to this.
         */
        SourceRange mergeWith(SourceRange sr) {
             
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(sr);
	if (sr == null) return this;
            if (startPos == Position.NOPOS)
                startPos = sr.startPos;
            else if (sr.startPos != Position.NOPOS)
                startPos = (startPos < sr.startPos ? startPos : sr.startPos);
            if (endPos == Position.NOPOS)
                endPos = sr.endPos;
            else if (sr.endPos != Position.NOPOS)
                endPos = (endPos > sr.endPos ? endPos : sr.endPos);
            return this;
        }
    }

}
          