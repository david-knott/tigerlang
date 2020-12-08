package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;
import com.chaosopher.tigerlang.compiler.util.Assert;

abstract public class Exp extends Absyn implements Typable {
    Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        Assert.assertNotNull(type);
        this.type = type;
    }
}
