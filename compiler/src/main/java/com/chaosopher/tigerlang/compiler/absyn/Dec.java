package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;
import com.chaosopher.tigerlang.compiler.util.Assert;

abstract public class Dec extends Absyn implements Typable {
    Type type;

    /**
     * Returns the type of the declared object: 
     * - FunctionDec would return the functions return type ?
     * - VarDec would return the variable Type ( INT, STRING, NAME, RECORD, ARRAY)
     * - TypeDec would return the type Type ( INT, STRING, NAME, RECORD, ARRAY )
     */
    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        Assert.assertNotNull(type);
        this.type = type;
    }
}
