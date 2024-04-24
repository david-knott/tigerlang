package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Abstract base class that represents type information in the abstract syntax
 * tree. This class can be used in both variable declarations, eg var a:int = 1
 * or in type declarations type a = int
 */
abstract public class Ty extends Absyn implements Typable, TypeConstructor {
    Type type;
    private Type createdType;

    /**
     * Returns the type of this AST node ( VarDec, TypeDec, FunctionDec, All Expressions, All Types )
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Sets the type of this AST node.
     */
    public void setType(Type type) {
        Assert.assertNotNull(type);
        this.type = type;
    }

    /**
     * Gets the type this AST node can create, ( ArrayTy, NameTy, RecordTy, FunctionDec, NilExp, TypeDec )
     */
    public Type getCreatedType() {
        return this.createdType;
    }

    /**
     * Sets the type this AST node can create.
     */
    public void setCreatedType(Type type) {
        this.createdType = type;
    }
}
