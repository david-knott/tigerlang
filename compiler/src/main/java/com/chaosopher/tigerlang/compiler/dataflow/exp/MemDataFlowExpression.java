package com.chaosopher.tigerlang.compiler.dataflow.exp;

import com.chaosopher.tigerlang.compiler.temp.Temp;

class MemDataFlowExpression extends DataFlowExpression {
    private final Temp temp;

    MemDataFlowExpression(Temp t) {
        this.temp = t;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MemDataFlowExpression) {
            MemDataFlowExpression other = (MemDataFlowExpression)obj;
            return other.temp == this.temp;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("memExpression: { exp: %s }", this.temp);
    }

    @Override
    public int hashCode() {
        return this.temp.toString().hashCode();
    }
}