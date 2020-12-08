package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;

public interface Typable {
    
    public Type getType();
    public void setType(Type type);

}