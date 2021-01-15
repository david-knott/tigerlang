package com.chaosopher.tigerlang.compiler.regalloc;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.assem.Instr;
import com.chaosopher.tigerlang.compiler.assem.InstrList;
import com.chaosopher.tigerlang.compiler.assem.MOVE;
import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.core.Component;
import com.chaosopher.tigerlang.compiler.core.LL;
import com.chaosopher.tigerlang.compiler.flowgraph.AssemFlowGraph;
import com.chaosopher.tigerlang.compiler.flowgraph.FlowGraph;
import com.chaosopher.tigerlang.compiler.frame.Access;
import com.chaosopher.tigerlang.compiler.frame.Frame;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.intel.IntelFrame;
import com.chaosopher.tigerlang.compiler.liveness.Liveness;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.temp.TempList;
import com.chaosopher.tigerlang.compiler.temp.TempMap;

/**
 * RegAllocWithCoalescing class manages the spilling.
 */
public class IterativeCoalescing extends Component implements RegAlloc {
    public InstrList instrList;
    public Frame frame;
    private int K;
    private boolean coalesce = true;
    // move related worklists
    private Hashtable<Temp, LL<Temp>> adjList = new Hashtable<Temp, LL<Temp>>();
    private Hashtable<Temp, LL<Temp>> addSet = new Hashtable<Temp, LL<Temp>>();
    private LL<Temp> spilledNodes; // nodes marked for spilling.
    private LL<Temp> colouredNodes; // nodes that have been coloured.
    private LL<Temp> precoloured; // can be created from our list of registers
    private Hashtable<Temp, Temp> colour = new Hashtable<Temp, Temp>();
    private LL<Temp> initial;
    private Hashtable<Temp, Integer> degree = new Hashtable<Temp, Integer>();
    /**
     * High degree temps that are potential spill candidates.
     */
    private LL<Temp> spillWorkList;
    /**
     * Low degree non move related temps.
     */
    private LL<Temp> simplifyWorkList;
    private LL<Temp> stack;
    private LL<Temp> spillTemps;
    private LL<Temp> coalescedNodes;
    private FlowGraph flowGraph;
    private Liveness liveness;
    private Hashtable<Temp, Integer> useCount = new Hashtable<Temp, Integer>();
    private Hashtable<Temp, Integer> defCount = new Hashtable<Temp, Integer>();

    private LL<Instr> workListMoves;
    private LL<Instr> activeMoves;
    private LL<Instr> frozenMoves;
    private LL<Instr> coalescedMoves;
    /**
     * Instructions that have been coalesced.
     */
    private LL<Instr> constrainedMoves;
    /**
     * List containing all low degree move related temps.
     */
    private LL<Temp> freezeWorkList;
    private Hashtable<Temp, LL<Instr>> moveList = new Hashtable<Temp, LL<Instr>>();
    /**
     * Hashtable that maps coalesced temps.
     */
    private Hashtable<Temp, Temp> alias = new Hashtable<Temp, Temp>();

    private void updateUseAndDefCounts() {
        useCount = new Hashtable<Temp, Integer>();
        defCount = new Hashtable<Temp, Integer>();
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            Node node = nodes.head;
            for (TempList defs = flowGraph.def(node); defs != null; defs = defs.tail) {
                Temp defNode = defs.head;
                useCount.put(defNode, defCount.getOrDefault(node, 1) + 1);
            }
            for (TempList uses = flowGraph.use(node); uses != null; uses = uses.tail) {
                Temp useNode = uses.head;
                useCount.put(useNode, defCount.getOrDefault(node, 1) + 1);
            }
        }
    }

    private LL<Temp> liveOut(Instr head) {
        return liveness.liveOut(this.flowGraph.node(head));
    }

    private void liveness() {
        flowGraph = new AssemFlowGraph(this.instrList);
        liveness = new Liveness(flowGraph);
    }

    private LL<Temp> use(Instr instr) {
        LL<Temp> nodeWorkList = null;
        for (TempList tempList = instr.use(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = LL.<Temp>or(nodeWorkList, new LL<Temp>(tempList.head));
        }
        return nodeWorkList;
    }

    private LL<Temp> def(Instr instr) {
        LL<Temp> nodeWorkList = null;
        for (TempList tempList = instr.def(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = LL.<Temp>or(nodeWorkList, new LL<Temp>(tempList.head));
        }
        return nodeWorkList;
    }

    /**
     * Builds the interference graph and move list.
     */
    private void build() {
        //System.out.println("# build");
        for (InstrList il = this.instrList; il != null; il = il.tail) {
            LL<Temp> live = liveOut(il.head);
            var uses = use(il.head);
            var defs = def(il.head);
            if (il.head instanceof MOVE) {
                if (this.coalesce) {
                    this.workListMoves = LL.<Instr>or(this.workListMoves, new LL<Instr>(il.head));
                    for (LL<Temp> n = LL.<Temp>or(uses, defs); n != null; n = n.tail) {
                        this.moveList.put(n.head,
                                LL.<Instr>or(this.moveList.getOrDefault(n.head, null), new LL<Instr>(il.head)));
                    }
                }
                for (var l = live; l != null; l = l.tail) { // foreach live var
                    if (uses.head != l.head) { // if live var is not a use
                        this.addEdge(defs.head, l.head); // add an edge with the def
                    }
                }
            } else {
                for (; defs != null; defs = defs.tail) {
                    for (var l = live; l != null; l = l.tail) {
                        this.addEdge(defs.head, l.head);
                    }
                }
            }
        }
    }

    /**
     * Add temps and their related nodes into the correct worklist. If the degree is
     * greater or equal to K, we add to the spill work list If the node is move
     * related, add it to the freeze work list. Otherwise add it to the simplify
     * work list
     */
    private void makeWorklist() {
        //System.out.println("# makeworklist");
        while (initial != null) {
            Temp temp = initial.head;
            initial = LL.<Temp>andNot(initial, new LL<Temp>(temp));
            if (this.degree.get(temp) >= K) {
                this.spillWorkList = LL.<Temp>or(this.spillWorkList, new LL<Temp>(temp));
            } else if (this.moveRelated(temp)) {
                this.freezeWorkList = LL.<Temp>or(this.freezeWorkList, new LL<Temp>(temp));
            } else {
                this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(temp));
            }
        }
    }

    /**
     * Checks if temporary is used by any moves that are active or moves enabled for
     * possible coalescing.
     * 
     * @param temp
     * @return true if temp is a move instruction.
     */
    private boolean moveRelated(Temp temp) {
        var r = this.nodeMoves(temp);
        return r != null;
    }

    /**
     * Returns a list of all move instructions that this temp is used in.
     * 
     * @param temp
     * @return
     */
    private LL<Instr> nodeMoves(Temp temp) {
        var one = LL.or(this.activeMoves, this.workListMoves);
        var two = this.moveList.get(temp);
        var moves = LL.and(one, two);
        return moves;
    }

    /**
     * Returns a list of all temporaries that are adjacent to head and not in either
     * the stack or the coalesced nodes list.
     * 
     * @param head
     * @return
     */
    private LL<Temp> adjacent(Temp head) {
        var one = this.adjList.get(head);
        var sortedStack = LL.<Temp>or(LL.<Temp>sort(this.stack), this.coalescedNodes);
        return LL.<Temp>andNot(one, sortedStack);
    }

    /**
     * Add nodes to the select stack and decrement their adjacent nodes degrees.
     * 
     * Removes item from simplifyWorkList and pushes it onto the stack
     */
    private void simplify() {
        //System.out.println("# simplify");
        LL<Temp> n = new LL<Temp>(this.simplifyWorkList.head);
        this.simplifyWorkList = LL.<Temp>andNot(this.simplifyWorkList, n);
        this.stack = LL.<Temp>insertRear(this.stack, n.head);
        for (LL<Temp> adjacent = this.adjacent(n.head); adjacent != null; adjacent = adjacent.tail) {
            this.decrementDegree(adjacent.head);
        }
    }

    /**
     * Decrement node head's degree by one. If this node moves from > K to K we
     * enable moves for this node and its adjacent nodes. We then remove this node
     * from the spill work list. If the node is move related, we move it to the
     * freeze worklist, if not, move it to the simplify worklist. This method can be
     * called from either the simplify method or the combine method.
     * 
     * @param m
     */
    private void decrementDegree(Temp m) {
        Integer d = this.degree.get(m);
        this.degree.put(m, d - 1);
        //System.out.println("decrement degree " + m + " " + d);
        if (d == this.K) {
            this.enableMoves(LL.<Temp>or(new LL<Temp>(m), this.adjacent(m)));
            this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(m));
            if (this.moveRelated(m)) {
                this.freezeWorkList = LL.<Temp>or(this.freezeWorkList, new LL<Temp>(m));
            } else {
                this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(m));
            }
        }
    }

    private void enableMoves(LL<Temp> nodes) {
        for (; nodes != null; nodes = nodes.tail) {
            for (var m = this.nodeMoves(nodes.head); m != null; m = m.tail) {
                if (LL.<Instr>contains(this.activeMoves, m.head)) {
                    this.activeMoves = LL.<Instr>andNot(this.activeMoves, new LL<Instr>(m.head));
                    this.workListMoves = LL.<Instr>or(this.workListMoves, new LL<Instr>(m.head));
                }
            }
        }
    }

    private void coalesce() {
        //System.out.println("# coalesce");
        Instr m = this.workListMoves.head;
        Temp u, v;
        // defuse
        Temp y = m.def().head;
        Temp x = m.use().head;
        x = this.getAlias(x);
        y = this.getAlias(y);
        if (LL.<Temp>contains(this.precoloured, y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        this.workListMoves = LL.<Instr>andNot(this.workListMoves, new LL<Instr>(m));
        if (u == v) { /* alias(u) = alias(v) => add to coalesced moves */
            this.coalescedMoves = LL.<Instr>or(this.coalescedMoves, new LL<Instr>(m));
            //System.out.println("u == v u:" + u + " v:" + v);
            this.addSimplifyWorkList(u);
        } else if (LL.<Temp>contains(this.precoloured, v)
                || this.inAdjSet(u, v)) { /* v is precoloured or u & v are in adjSet => constrained */
            this.constrainedMoves = LL.<Instr>or(this.constrainedMoves, new LL<Instr>(m));
            this.addSimplifyWorkList(u);
            this.addSimplifyWorkList(v);
            //System.out.println("Constrained td:" + m.def() + " tu:" + m.use());
        } else if ((LL.<Temp>contains(this.precoloured, u) && isOkay(u, v)) || (!LL.<Temp>contains(this.precoloured, u)
                && conservative(u, v))) { /*
                                           * u is precoloured and its okay to coalesce u & v OR u is not precoloured and
                                           * u & v can be conservatively coalesced => combine & coalesce
                                           */
            this.coalescedMoves = LL.<Instr>or(this.coalescedMoves, new LL<Instr>(m));
            //System.out.println("Combining: u:"+ u + " v:"+ v);
            this.combine(u, v);
            this.addSimplifyWorkList(u);
        } else { /* otherwise move not yet ready for coalescing */
            this.activeMoves = LL.<Instr>or(this.activeMoves, new LL<Instr>(m));
        }
    }

    /**
     * Replace temporary v with temporary u. All of temporary v moves are added to
     * temporary v's. All of temporary v's edges are added to u and their degrees
     * incremented. We also decremenet the degree of each ajacent node of v as we
     * have merged it. If the degree of u is greater or equal to K and u is on the
     * freeze list, we removed it from the freeze list and add it to the spill list.
     * 
     * @param u
     * @param v
     */
    private void combine(Temp u, Temp v) {
        // remove temp from invariant work lists
        if (LL.<Temp>contains(this.freezeWorkList, v)) {
            this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(v));
        } else {
            this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(v));
        }
        // add to coalesce worklist
        this.coalescedNodes = LL.<Temp>or(this.coalescedNodes, new LL<Temp>(v));
        //System.out.println("Moving " + v + " to coalesce nodes.");
        this.alias.put(v, u);
        this.moveList.put(u, LL.<Instr>or(this.moveList.get(u), this.moveList.get(v)));
        this.enableMoves(new LL<Temp>(v));
        // get all nodes adjacent to v not on stach or coalesced
        for (LL<Temp> t = this.adjacent(v); t != null; t = t.tail) {
            // create new edge between v adj and u ( increments degree of v adj & u).
            this.addEdge(t.head, u);
            // degrement degree of t as its degree doesn't change after merge.
            this.decrementDegree(t.head);
            ////System.out.println("merge " + t.head + " into " + u);
        }
        if (this.degree.get(u) >= this.K && LL.<Temp>contains(this.freezeWorkList, u)) {
            this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
            this.spillWorkList = LL.<Temp>or(this.spillWorkList, new LL<Temp>(u));
        }
    }

    private boolean conservative(Temp u, Temp v) {
        int k = 0;
        for (var nodes = LL.<Temp>or(this.adjacent(u), this.adjacent(v)); nodes != null; nodes = nodes.tail) {
            if (this.degree.get(nodes.head) >= this.K) {
                k++;
            }
        }
        return k < this.K;
    }

    private boolean isOkay(Temp u, Temp v) {
        boolean result = true;
        for (var t = this.adjacent(v); t != null; t = t.tail) {
            result &= (this.degree.get(t.head) < this.K || LL.<Temp>contains(this.precoloured, t.head)
                    || this.inAdjSet(t.head, u));
            if (!result)
                break;
        }
        return result;
    }

    private boolean inAdjSet(Temp u, Temp v) {
        return (this.addSet.get(u) != null && LL.<Temp>contains(this.addSet.get(u), v))
                || (this.addSet.get(v) != null && LL.<Temp>contains(this.addSet.get(v), u));
    }

    private void addSimplifyWorkList(Temp u) {
        if (!LL.<Temp>contains(this.precoloured, u) && !this.moveRelated(u) && this.degree.get(u) < this.K) {
            this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
            this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(u));
        }
    }

    private Temp getAlias(Temp x) {
        if (LL.<Temp>contains(this.coalescedNodes, x)) {
            return this.getAlias(this.alias.get(x));
        }
        return x;
    }

    private void freeze() {
        ////System.out.println("# freeze");
        Temp u = this.freezeWorkList.head;
        this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
        this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(u));
        this.freezeMoves(u);
    }

    private void freezeMoves(Temp u) {
        for (var m = this.nodeMoves(u); m != null; m = m.tail) {
            Temp v = null;
            // defuse
            Temp y = m.head.def().head;
            Temp x = m.head.use().head;
            if (this.getAlias(y) == this.getAlias(u)) {
                v = this.getAlias(x);
            } else {
                v = this.getAlias(y);
            }
            this.activeMoves = LL.<Instr>andNot(this.activeMoves, new LL<Instr>(m.head));
            this.frozenMoves = LL.<Instr>or(this.frozenMoves, new LL<Instr>(m.head));
            if (this.nodeMoves(v) == null && this.degree.get(v) < this.K) {
                this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(v));
                this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(v));
            }
        }
    }

    /**
     * Returns the weight of the temp.
     * 
     * @param temp
     * @return
     */
    private double weight(Temp temp) {
        int originalDegree = LL.<Temp>size(this.adjList.get(temp));
        return (((float) this.defCount.getOrDefault(temp, 0) + this.useCount.getOrDefault(temp, 0))) / originalDegree;
    }

    /**
     * Selects the node to spill from the spill worklist.
     * 
     * @return a Temp
     */
    private Temp nodeToSpill() {
        LL<Temp> potentialSpills = this.spillWorkList;
        Temp maxSpill = null;
        double sp = 0;
        for (LL<Temp> ps = potentialSpills; ps != null; ps = ps.tail) {
            double vsp = weight(ps.head);
            if (sp == 0 || vsp < sp) {
                maxSpill = ps.head;
                sp = vsp;
            }
        }
        return maxSpill;
    }

    /**
     * Selects a node to spill. Removes node from spillWorkList Adds node to
     * simplifyWorkList.
     */
    private void selectSpill() {
        //System.out.println("# selectSpill");
        Temp m = this.nodeToSpill();
        this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(m));
        this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(m));
        this.freezeMoves(m);
    }

    /**
     * Add edge to both adjacent list and bit matrix Keys are non precoloured temps.
     * 
     * @param u the first temp
     * @param v the second temp
     */
    private void addEdge(Temp u, Temp v) {
        if (!this.inAdjSet(u, v) && u != v) {
            this.addAdjacentSet(u, v);
            if (!LL.<Temp>contains(this.precoloured, u)) {
                this.adjList.put(u, LL.<Temp>or(this.adjList.get(u), new LL<Temp>(v)));
                int d = this.degree.getOrDefault(u, 0);
                this.degree.put(u, d + 1);
                //System.out.println("increment degree " + u + " " + d);
            }
            if (!LL.<Temp>contains(this.precoloured, v)) {
                this.adjList.put(v, LL.<Temp>or(this.adjList.get(v), new LL<Temp>(u)));
                int d = this.degree.getOrDefault(v, 0);
                this.degree.put(v, d + 1);
                //System.out.println("increment degree " + v + " " + d);
            }
        }
    }

    private void addAdjacentSet(Temp u, Temp v) {
        LL<Temp> value = addSet.get(u);
        addSet.put(u, LL.<Temp>or(value, new LL<Temp>(v)));
        LL<Temp> value2 = addSet.get(v);
        addSet.put(v, LL.<Temp>or(value2, new LL<Temp>(u)));
    }

    private void assignColours() {
        while (this.stack != null) {
            Temp n = LL.<Temp>last(this.stack);
            this.stack = LL.<Temp>removeLast(this.stack);
            TempList okColours = this.frame.registers();
            for (var w = this.adjList.get(n); w != null; w = w.tail) {
                Temp alias = this.getAlias(w.head);
                if (LL.<Temp>contains(LL.<Temp>or(this.colouredNodes, this.precoloured), alias)) {
                    Temp clr = this.colour.get(alias);
                    okColours = TempList.andNot(okColours, new TempList(clr));
                }
            }
            if (okColours == null) {
                this.spilledNodes = LL.<Temp>or(this.spilledNodes, new LL<Temp>(n));
            } else {
                this.colouredNodes = LL.<Temp>or(this.colouredNodes, new LL<Temp>(n));
                var c = okColours.head;
                Assert.assertIsFalse(c == IntelFrame.rsp, "Cannot use rsp for colour");
                Assert.assertIsFalse(c == IntelFrame.rbp, "Cannont use rbp for colour");
                this.colour.put(n, c);
            }
        }
        for (var n = this.coalescedNodes; n != null; n = n.tail) {
            Temp c =  this.getAlias(n.head);
            Assert.assertIsFalse(c == IntelFrame.rsp, "Cannont use rsp for coalesce colour");
            Assert.assertIsFalse(c == IntelFrame.rbp, "Cannot use rbp for coalesce colour");
            this.colour.put(n.head, c);
        }
    }

    private void rewrite() {
        LL<Temp> spills = null;
        Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>();
        for (LL<Temp> sn = this.spilledNodes; sn != null; sn = sn.tail) {
            Access access = this.frame.allocLocal(true);
            accessHash.put(sn.head, access);
            spills = LL.<Temp>insertOrdered(spills, sn.head);
        }
        Access access = null;
        InstrList newList = null;
        LL<Temp> newTemps = null;
        for (InstrList il = this.instrList; il != null; il = il.tail) {
            LL<Temp> spilledDefs = LL.<Temp>and(def(il.head), spills);
            LL<Temp> spilledUses = LL.<Temp>and(use(il.head), spills);
            if (LL.<Temp>or(spilledDefs, spilledUses) == null) {
                newList = InstrList.append(newList, il.head);
                continue;
            }
            for (; spilledUses != null; spilledUses = spilledUses.tail) {
                access = accessHash.get(spilledUses.head);
                Temp spillTemp = Temp.create();
                this.spillTemps = LL.<Temp>insertOrdered(this.spillTemps, spillTemp);
                newTemps = LL.<Temp>or(newTemps, new LL<Temp>(spillTemp));
                InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
                newList = InstrList.append(newList, memoryToTemp);
            }
            newList = InstrList.append(newList, il.head);
            for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
                access = accessHash.get(spilledDefs.head);
                Temp spillTemp = Temp.create();
                newTemps = LL.<Temp>or(newTemps, new LL<Temp>(spillTemp));
                this.spillTemps = LL.<Temp>insertOrdered(this.spillTemps, spillTemp);
                InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
                newList = InstrList.append(newList, tempToMemory);
            }
        }
        this.adjList.clear();
        this.addSet.clear();
        this.degree.clear();
        this.alias.clear();
        this.colour.clear();
        this.defCount.clear();
        this.useCount.clear();
        this.moveList.clear();
        this.instrList = newList;
        this.coalescedMoves = null;
        this.constrainedMoves = null;
        this.activeMoves = null;
        this.spilledNodes = null;
        this.coalescedNodes = null;
        this.colouredNodes = null;
    }

    int maxTries = 0;

    private void main() {
        Assert.assertLE(maxTries++, 3);
        for (TempList pc = this.frame.registers(); pc != null; pc = pc.tail) {
            this.precoloured = LL.<Temp>insertOrdered(this.precoloured, pc.head);
            this.colour.put(pc.head, pc.head);
            this.degree.put(pc.head, Integer.MAX_VALUE / 2);
        }
        this.initial = null;
        for (InstrList ins = this.instrList; ins != null; ins = ins.tail) {
            this.initial = LL.<Temp>or(this.initial, LL.<Temp>or(use(ins.head), def(ins.head)));
        }
        this.initial = LL.<Temp>andNot(this.initial, this.precoloured);
        this.liveness();
        this.updateUseAndDefCounts();
        this.build();
        this.makeWorklist();
        do {
            this.checkWorkListInvariants();
            if (this.simplifyWorkList != null) {
                this.simplify();
            } else if (this.workListMoves != null) {
                this.coalesce();
            } else if (this.freezeWorkList != null) {
                this.freeze();
            } else if (this.spillWorkList != null) {
                this.selectSpill();
            }
            this.checkWorkListInvariants();
        } while (this.simplifyWorkList != null || this.spillWorkList != null || this.workListMoves != null
                || this.freezeWorkList != null);
        this.checkWorkListInvariants();
        this.assignColours();
        if (this.spilledNodes != null) {
            this.rewrite();
            this.main();
        }
       // this.liveness.dumpLiveness(this.instrList);
    }

    private void checkWorkListInvariants() {
        for (LL<Temp> u = this.simplifyWorkList; u != null; u = u.tail) {
            // If a temp is to be spilled, it is high degree and will be
            // placed on the spillWorkList in the selectSpill phase.
            // this will violate this invariant.
            if (this.degree.get(u.head) >= K) {
                // throw new Error("SimplifyWorkList invariant violation: degree < K : " +
                // u.head + ":" + this.degree.get(u.head));
            }
            if (LL.<Instr>and(this.moveList.get(u.head), LL.<Instr>or(this.activeMoves, this.workListMoves)) != null) {
                throw new Error(
                        "SimplifyWorkList invariant violation: moveList[n] ⋂ activeMoves ⋃ workListMoves = {} : "
                                + u.head);
            }
        }
        for (LL<Temp> u = this.freezeWorkList; u != null; u = u.tail) {
            if (this.degree.get(u.head) >= K) {
                throw new Error(
                        "FreezeWorkList invariant violation: degree < K :" + u.head + ":" + this.degree.get(u.head));
            }
            if (LL.<Instr>and(this.moveList.get(u.head), LL.<Instr>or(this.activeMoves, this.workListMoves)) == null) {
                throw new Error("FreezeWorkList invariant violation: moveList[n] ⋂ activeMoves ⋃ workListMoves != {} : "
                        + u.head);
            }
        }
        for (LL<Temp> u = this.spillWorkList; u != null; u = u.tail) {
            if (this.degree.get(u.head) < K) {
                throw new Error("SpillWorkList invariant violation: degree >= K");
            }
        }
        for (LL<Temp> u = LL.<Temp>or(LL.<Temp>or(this.simplifyWorkList, this.freezeWorkList),
                this.spillWorkList); u != null; u = u.tail) {
            LL<Temp> check = LL.<Temp>and(this.adjList.get(u.head),
                    LL.<Temp>or(LL.<Temp>or(LL.<Temp>or(this.precoloured, this.simplifyWorkList), this.freezeWorkList),
                            this.spillWorkList));
            int deg = this.degree.get(u.head);
            int setCount = LL.<Temp>size(check);
            if (deg != setCount) {
                throw new Error(
                        "Degree invariant violation for temp:" + u.head + " : degree:" + deg + " != count:" + setCount);
            }
        }
    }

    public IterativeCoalescing(Frame frame, InstrList instrList) {
        this.instrList = instrList;
        this.frame = frame;
        this.K = this.frame.registers().size(); // 14
        this.main();
    }

    @Override
    public String tempMap(Temp t) {
        var clr = this.colour.get(this.getAlias(t));
        Assert.assertNotNull(clr, "No colour for " + t);
        return this.frame.tempMap(clr);
    }

    @Override
    public InstrList getInstrList() {
        return this.instrList;
    }
}