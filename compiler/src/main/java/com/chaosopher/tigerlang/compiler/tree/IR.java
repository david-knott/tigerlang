package com.chaosopher.tigerlang.compiler.tree;

import java.io.Serializable;

public abstract class IR implements Serializable {

    private static final long serialVersionUID = 538341853519182529L;

    abstract public void accept(TreeVisitor treeVisitor);

    public abstract int getOperator();

    public abstract int getArity();

    public abstract IR getNthChild(int index);
}