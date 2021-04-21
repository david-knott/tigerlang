package com.chaosopher.tigerlang.compiler.absyn;

import com.chaosopher.tigerlang.compiler.types.Type;
import com.chaosopher.tigerlang.compiler.util.Assert;

abstract public class Var extends Absyn implements Typable {

    Type type;
    
    /**
     * Pointer to the definition of this variable.
     */
    public Absyn def;

    public Type getType() {
        return this.type;
    }

	public void setType(Type type) {
        Assert.assertNotNull(type);
        this.type = type;
	}
}
