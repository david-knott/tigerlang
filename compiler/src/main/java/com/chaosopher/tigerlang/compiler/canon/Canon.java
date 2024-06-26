package com.chaosopher.tigerlang.compiler.canon;

import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.StmList;
import com.chaosopher.tigerlang.compiler.tree.TreeVisitor;

class MoveCall extends com.chaosopher.tigerlang.compiler.tree.Stm {
    com.chaosopher.tigerlang.compiler.tree.TEMP dst;
    com.chaosopher.tigerlang.compiler.tree.CALL src;

    MoveCall(com.chaosopher.tigerlang.compiler.tree.TEMP d, com.chaosopher.tigerlang.compiler.tree.CALL s) {
        dst = d;
        src = s;
    }

    public com.chaosopher.tigerlang.compiler.tree.ExpList kids() {
        return src.kids();
    }

    public com.chaosopher.tigerlang.compiler.tree.Stm build(com.chaosopher.tigerlang.compiler.tree.ExpList kids) {
        return new com.chaosopher.tigerlang.compiler.tree.MOVE(dst, src.build(kids));
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        throw new Error("Not supported by Canon.MoveCall.");
    }

    @Override
    public int getOperator() {
        throw new Error("Not supported by Canon.MoveCall.");
    }

    @Override
    public int getArity() {
        throw new Error("Not supported by Canon.MoveCall.");
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error("Not supported by Canon.MoveCall.");
    }
}

class ExpCall extends com.chaosopher.tigerlang.compiler.tree.Stm {
    com.chaosopher.tigerlang.compiler.tree.CALL call;

    ExpCall(com.chaosopher.tigerlang.compiler.tree.CALL c) {
        call = c;
    }

    public com.chaosopher.tigerlang.compiler.tree.ExpList kids() {
        return call.kids();
    }

    public com.chaosopher.tigerlang.compiler.tree.Stm build(com.chaosopher.tigerlang.compiler.tree.ExpList kids) {
        return new com.chaosopher.tigerlang.compiler.tree.EXPS(call.build(kids));
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        throw new Error("Not supported by Canon.ExpCall.");
    }

    @Override
    public int getOperator() {
        throw new Error("Not supported by Canon.ExpCall.");
    }

    @Override
    public int getArity() {
        throw new Error("Not supported by Canon.ExpCall.");
    }

    @Override
    public IR getNthChild(int index) {
        throw new Error("Not supported by Canon.ExpCall.");
    }
}

class StmExpList {
    com.chaosopher.tigerlang.compiler.tree.Stm stm;
    com.chaosopher.tigerlang.compiler.tree.ExpList exps;

    StmExpList(com.chaosopher.tigerlang.compiler.tree.Stm s, com.chaosopher.tigerlang.compiler.tree.ExpList e) {
        if (s == null)
            throw new IllegalArgumentException("Stm cannot be null");
        stm = s;
        exps = e;
    }
}

public class Canon {

    static boolean isNop(com.chaosopher.tigerlang.compiler.tree.Stm a) {
        return a instanceof com.chaosopher.tigerlang.compiler.tree.EXPS && ((com.chaosopher.tigerlang.compiler.tree.EXPS) a).exp instanceof com.chaosopher.tigerlang.compiler.tree.CONST;
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm seq(com.chaosopher.tigerlang.compiler.tree.Stm a, com.chaosopher.tigerlang.compiler.tree.Stm b) {
        if (isNop(a))
            return b;
        else if (isNop(b))
            return a;
        else
            return new com.chaosopher.tigerlang.compiler.tree.SEQ(a, b);
    }

    static boolean commute(com.chaosopher.tigerlang.compiler.tree.Stm a, com.chaosopher.tigerlang.compiler.tree.Exp b) {
        return isNop(a) || b instanceof com.chaosopher.tigerlang.compiler.tree.NAME || b instanceof com.chaosopher.tigerlang.compiler.tree.CONST;
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm do_stm(com.chaosopher.tigerlang.compiler.tree.SEQ s) {
        return seq(do_stm(s.left), do_stm(s.right));
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm do_stm(com.chaosopher.tigerlang.compiler.tree.MOVE s) {
        if (s.dst instanceof com.chaosopher.tigerlang.compiler.tree.TEMP && s.src instanceof com.chaosopher.tigerlang.compiler.tree.CALL)
            return reorder_stm(new MoveCall((com.chaosopher.tigerlang.compiler.tree.TEMP) s.dst, (com.chaosopher.tigerlang.compiler.tree.CALL) s.src));
        else if (s.dst instanceof com.chaosopher.tigerlang.compiler.tree.ESEQ)
            return do_stm(new com.chaosopher.tigerlang.compiler.tree.SEQ(((com.chaosopher.tigerlang.compiler.tree.ESEQ) s.dst).stm, new com.chaosopher.tigerlang.compiler.tree.MOVE(((com.chaosopher.tigerlang.compiler.tree.ESEQ) s.dst).exp, s.src)));
        else
            return reorder_stm(s);
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm do_stm(com.chaosopher.tigerlang.compiler.tree.EXPS s) {
        if (s.exp instanceof com.chaosopher.tigerlang.compiler.tree.CALL)
            return reorder_stm(new ExpCall((com.chaosopher.tigerlang.compiler.tree.CALL) s.exp));
        else
            return reorder_stm(s);
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm do_stm(com.chaosopher.tigerlang.compiler.tree.Stm s) {
        if (s instanceof com.chaosopher.tigerlang.compiler.tree.SEQ)
            return do_stm((com.chaosopher.tigerlang.compiler.tree.SEQ) s);
        else if (s instanceof com.chaosopher.tigerlang.compiler.tree.MOVE)
            return do_stm((com.chaosopher.tigerlang.compiler.tree.MOVE) s);
        else if (s instanceof com.chaosopher.tigerlang.compiler.tree.EXPS)
            return do_stm((com.chaosopher.tigerlang.compiler.tree.EXPS) s);
        else
            return reorder_stm(s);
    }

    static com.chaosopher.tigerlang.compiler.tree.Stm reorder_stm(com.chaosopher.tigerlang.compiler.tree.Stm s) {
        StmExpList x = reorder(s.kids());
        return seq(x.stm, s.build(x.exps));
    }

    static com.chaosopher.tigerlang.compiler.tree.ESEQ do_exp(com.chaosopher.tigerlang.compiler.tree.ESEQ e) {
        com.chaosopher.tigerlang.compiler.tree.Stm stms = do_stm(e.stm);
        com.chaosopher.tigerlang.compiler.tree.ESEQ b = do_exp(e.exp);
        return new com.chaosopher.tigerlang.compiler.tree.ESEQ(seq(stms, b.stm), b.exp);
    }

    static com.chaosopher.tigerlang.compiler.tree.ESEQ do_exp(com.chaosopher.tigerlang.compiler.tree.Exp e) {
        if (e instanceof com.chaosopher.tigerlang.compiler.tree.ESEQ)
            return do_exp((com.chaosopher.tigerlang.compiler.tree.ESEQ) e);
        else
            return reorder_exp(e);
    }

    static com.chaosopher.tigerlang.compiler.tree.ESEQ reorder_exp(com.chaosopher.tigerlang.compiler.tree.Exp e) {
        StmExpList x = reorder(e.kids());
        return new com.chaosopher.tigerlang.compiler.tree.ESEQ(x.stm, e.build(x.exps));
    }

    static StmExpList nopNull = new StmExpList(new com.chaosopher.tigerlang.compiler.tree.EXPS(new com.chaosopher.tigerlang.compiler.tree.CONST(0)), null);

    static StmExpList reorder(com.chaosopher.tigerlang.compiler.tree.ExpList exps) {
        if (exps == null)
            return nopNull;
        else {
            com.chaosopher.tigerlang.compiler.tree.Exp a = exps.head;
            // handle a call expression
            if (a instanceof com.chaosopher.tigerlang.compiler.tree.CALL) {
                com.chaosopher.tigerlang.compiler.temp.Temp t = com.chaosopher.tigerlang.compiler.temp.Temp.create();
                com.chaosopher.tigerlang.compiler.tree.Exp e = new com.chaosopher.tigerlang.compiler.tree.ESEQ(new com.chaosopher.tigerlang.compiler.tree.MOVE(new com.chaosopher.tigerlang.compiler.tree.TEMP(t), a), new com.chaosopher.tigerlang.compiler.tree.TEMP(t));
                return reorder(new com.chaosopher.tigerlang.compiler.tree.ExpList(e, exps.tail));
            } else { //its not a call, lets build an ESEQ from the head a.
                com.chaosopher.tigerlang.compiler.tree.ESEQ aa = do_exp(a);
                // reorder the tail of the expresion list argument.
                StmExpList bb = reorder(exps.tail);
                // if state bb commutes with aa, just swpy them.
                if (commute(bb.stm, aa.exp))
                    return new StmExpList(seq(aa.stm, bb.stm), new com.chaosopher.tigerlang.compiler.tree.ExpList(aa.exp, bb.exps));
                else { //the dont commute so create new temporaries to hold the expression
                    com.chaosopher.tigerlang.compiler.temp.Temp t = com.chaosopher.tigerlang.compiler.temp.Temp.create();
                    return new StmExpList(seq(aa.stm, seq(new com.chaosopher.tigerlang.compiler.tree.MOVE(new com.chaosopher.tigerlang.compiler.tree.TEMP(t), aa.exp), bb.stm)),
                            new com.chaosopher.tigerlang.compiler.tree.ExpList(new com.chaosopher.tigerlang.compiler.tree.TEMP(t), bb.exps));
                }
            }
        }
    }

    /**
     * Takes a tree sequence and a statement list. The right argument is passed into
     * an overload which recurses until a statement is found, at which point the
     * statememt is returned
     * @param s
     * @param l
     * @return a new statement list
     */
    static com.chaosopher.tigerlang.compiler.tree.StmList linear(com.chaosopher.tigerlang.compiler.tree.SEQ s, com.chaosopher.tigerlang.compiler.tree.StmList l) {
        // calls function below with s.right and the list, which 
        // returns a statement list then passes the s.left and
        // the statement list into the function below
        return linear(s.left, linear(s.right, l));
    }

    static com.chaosopher.tigerlang.compiler.tree.StmList linear(com.chaosopher.tigerlang.compiler.tree.Stm s, com.chaosopher.tigerlang.compiler.tree.StmList l) {
        if (s instanceof com.chaosopher.tigerlang.compiler.tree.SEQ)
            return linear((com.chaosopher.tigerlang.compiler.tree.SEQ) s, l); // calls the function above
        else
            return new com.chaosopher.tigerlang.compiler.tree.StmList(s, l);
    }

    static public com.chaosopher.tigerlang.compiler.tree.StmList removeJumps(com.chaosopher.tigerlang.compiler.tree.StmList stmList, Stm previous) {
        // if the current item and the previous item are JUMPs, ignore the current item.
        if(previous instanceof JUMP && stmList.head instanceof JUMP) {
            if(stmList.tail == null) {
                //Stm nop = new com.chaosopher.tigerlang.compiler.tree.EXPS(new com.chaosopher.tigerlang.compiler.tree.CONST(0));
                //return new StmList(nop);
                throw new Error("Jump/jump removal - inside tail = null branch.");
            }
            stmList = stmList.tail;
        }
        return stmList.tail != null ? new StmList(stmList.head, removeJumps(stmList.tail, stmList.head)) : new StmList(stmList.head);
    }

    static public com.chaosopher.tigerlang.compiler.tree.StmList linearize(com.chaosopher.tigerlang.compiler.tree.Stm s) {
        StmList stmList = linear(do_stm(s), null);
        return removeJumps(stmList, stmList.head);
    }
}
