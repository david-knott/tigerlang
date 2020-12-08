package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;

abstract public class Ty extends Absyn implements Typable {
    Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
