package com.chaosopher.tigerlang.compiler.intel;

import com.chaosopher.tigerlang.compiler.frame.Access;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.TEMP;

class InReg extends Access {
    Temp temp;

    InReg(Temp tmp) {
        temp = tmp;
    }

    @Override
    public Exp exp(Exp framePtr) {
        return new TEMP(temp);
    }
}