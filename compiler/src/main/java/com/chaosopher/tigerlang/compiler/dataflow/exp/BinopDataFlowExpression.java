package com.chaosopher.tigerlang.compiler.dataflow.exp;

import com.chaosopher.tigerlang.compiler.temp.Temp;

class BinopDataFlowExpression extends DataFlowExpression {
    private final Temp a;
    private final Temp b;
    private final int op;

    public BinopDataFlowExpression(int op, Temp a, Temp b) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BinopDataFlowExpression) {
            BinopDataFlowExpression other = (BinopDataFlowExpression)obj;
            return other.a == this.a
            && other.b == this.b
            && other.op == this.op;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("binopExpression: { left: %s, right: %s, op: %s }", this.a, this.b, this.op);
    }


    @Override
    public int hashCode() {
        return this.a.toString().hashCode() ^ this.b.toString().hashCode() ^ this.op;
    }
}