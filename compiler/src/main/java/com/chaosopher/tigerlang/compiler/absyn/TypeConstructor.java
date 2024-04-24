package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;

/**
 * Interface that marks a AST node as one that contructs a type.
 * eg, ArrayTy, RecordTy, NameTy
 **/
public interface TypeConstructor {
    
    public Type getCreatedType();
    public void setCreatedType(Type type);

}
