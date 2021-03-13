package com.chaosopher.tigerlang.compiler.tree;

import java.util.HashSet;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.temp.Temp;

public class CALL extends Exp {
    public Exp func;
    public ExpList args;

    public CALL(Exp f, ExpList a) {
        func = f;
        args = a;
    }

    public ExpList kids() {
        return new ExpList(func, args);
    }

    public Exp build(ExpList kids) {
        return new CALL(kids.head, kids.tail);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.CALL;
    }

    @Override
    public int getArity() {
        return this.args != null ? this.args.size() : 0;
    }

    @Override
    public IR getNthChild(int index) {
        if(index >= this.getArity()) {
            throw new Error("Call: index greater than arity.");
        }
        ExpList expList = this.args;
        while(index-- != 0) {
            expList = expList.tail;
        }
        return expList.head;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CALL) {
            return
            ((CALL)obj).func == this.func
            && ((CALL)obj).args.equals(this.args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.func.hashCode();
        if(this.args != null) {
            result = 31 * result + this.args.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("call: { func: %d, args: tbd }", this.func);
    }
}
