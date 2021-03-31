package com.chaosopher.tigerlang.compiler.tree;

import java.util.Iterator;

import com.chaosopher.tigerlang.compiler.util.Assert;

public class StmList extends Stm implements Iterable<Stm> {

    public static StmList append(StmList me, Stm t) {
        if (me == null && t == null) {
            return null;
        }
        if (me == null && t != null) {
            return new StmList(t);
        }
        if (me.tail == null) {
            return new StmList(me.head, new StmList(t));
        }
        return new StmList(me.head, StmList.append(me.tail, t));
    }

    public Stm head;

    public StmList tail;

    public StmList(Stm h) {
        Assert.assertNotNull(h);
        head = h;
        tail = null;
    }

    public StmList(Stm h, StmList t) {
        Assert.assertNotNull(h);
        head = h;
        tail = t;
    }

    /**
     * Returns this statement list with a new statement added to its tail.
     * 
     * @param stmList the statement list to add.
     * @return this statement list.
     */
    public StmList append(StmList stmList) {
        StmList end = this;
        for (; end.tail != null; end = end.tail)
            ;
        end.tail = stmList;
        return this;
    }

    /**
     * Returns the parameter stmlist with this appened to its end.
     * 
     * @param stmList the statement list to prepend to this.
     * @return the statment list.
     */
    public StmList prepend(StmList stmList) {
        return stmList.append(this);
    }

    /**
     * Returns the same statement list with a new statement list added to the tail.
     * 
     * @param stm the statement to append.
     * @return this statement list.
     */
    public StmList append(Stm stm) {
        StmList end = this;
        for (; end.tail != null; end = end.tail)
            ;
        end.tail = new StmList(stm);
        return this;
    }

    /**
     * Returns a new statement list, with the newly create item as the head of the
     * list.
     * 
     * @param stm the statement to prepend.
     * @return a new statement list.
     */
    public StmList prepend(Stm stm) {
        return new StmList(stm, this);
    }

    public com.chaosopher.tigerlang.compiler.tree.Stm toSEQ() {
        if (this.tail != null) {
            return new SEQ(this.head, this.tail.toSEQ());
        }
        return this.head;
    }

    @Override
    public ExpList kids() {
        return null;
    }

    @Override
    public Stm build(ExpList kids) {
        return null;
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return 0;
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public IR getNthChild(int index) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("stmList: { head: %s, tail: %s}", this.head, this.tail);
    }

    @Override
    public Iterator<Stm> iterator() {
        return new StmListIterator(this);
    }

    class StmListIterator implements Iterator<Stm> {

        private StmList stmList;

        StmListIterator(final StmList stmList) {
            this.stmList = stmList;
        }

        @Override
        public boolean hasNext() {
            return this.stmList != null;
        }

        @Override
        public Stm next() {
            // current item
            Stm current = this.stmList.head;
            // advance to next;
            this.stmList = this.stmList.tail;
            return current;
        }
    }
}