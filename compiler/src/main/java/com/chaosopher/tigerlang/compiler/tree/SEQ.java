package com.chaosopher.tigerlang.compiler.tree;

public class SEQ extends Stm {
    public Stm left, right;

    public SEQ(Stm l, Stm r) {
        left = l;
        right = r;
    }

    public ExpList kids() {
        throw new Error("kids() not applicable to SEQ");
    }

    public Stm build(ExpList kids) {
        throw new Error("build() not applicable to SEQ");
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    private class Normalizer {

        public Stm result;

        public Normalizer(SEQ seq) {
            tranverse(seq);
        }

        public void addOrCreate(Stm smt) {
            if (smt == null)
                return;
            if (result == null) {
                result = smt;
            } else {
                result = new SEQ(smt, result);
            }
        }

        public void tranverse(SEQ seq) {
            if (seq.right instanceof SEQ) {
                this.tranverse(((SEQ) seq.right));
            } else {
                this.addOrCreate(seq.right);
            }
            if (seq.left instanceof SEQ) {
                this.tranverse(((SEQ) seq.left));
            } else {
                this.addOrCreate(seq.left);
            }
        }
    }

    /**
     * Returns a normalized version of this SEQ in the form s (1, s(2, s(3, s(4,
     * s(5, 6))))) This is generated from a list of the terminal nodes, listed from
     * left to right.
     * 
     * @return
     */
    public SEQ normalise() {
        return (SEQ) (new Normalizer(this).result);
        // return SEQ.linearize(this);
    }

    static com.chaosopher.tigerlang.compiler.tree.SEQ linear(com.chaosopher.tigerlang.compiler.tree.SEQ s, com.chaosopher.tigerlang.compiler.tree.SEQ l) {
        // calls function below with s.right and the list, which returns a statement
        // then passes the s.left and the statement list into the function below
        return linear(s.left, linear(s.right, l));
    }

    static com.chaosopher.tigerlang.compiler.tree.SEQ linear(com.chaosopher.tigerlang.compiler.tree.Stm s, com.chaosopher.tigerlang.compiler.tree.SEQ l) {
        if (s instanceof com.chaosopher.tigerlang.compiler.tree.SEQ)
            return linear((com.chaosopher.tigerlang.compiler.tree.SEQ) s, l); // calls the function above
        else
            return new com.chaosopher.tigerlang.compiler.tree.SEQ(s, l);
    }

    static public com.chaosopher.tigerlang.compiler.tree.SEQ linearize(com.chaosopher.tigerlang.compiler.tree.Stm s) {
        return linear(s, null);
    }

    @Override
    public int getOperator() {
        return TreeKind.SEQ;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public IR getNthChild(int index) {
        switch (index) {
            case 0:
                return this.left;
            case 1:
                return this.right;
            default:
                throw new Error("Index out of range");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SEQ) {
            return
            ((SEQ)obj).left.equals(this.left)
            && ((SEQ)obj).right.equals(this.right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.left.hashCode();
        result = 31 * result + this.right.hashCode();
        return result;
    }
}
