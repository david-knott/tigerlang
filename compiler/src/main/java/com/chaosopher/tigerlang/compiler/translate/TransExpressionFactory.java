package com.chaosopher.tigerlang.compiler.translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.tree.Exp;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.Stm;

public class TransExpressionFactory {

    /**
     * Creates a new instance to create translation objects.
     * 
     * @return
     */
    public static TransExpressionFactory create(LabelFactory labelFactory, HashMap<IR, Absyn> sourceMap) {
        return new TransExpressionFactory(labelFactory, sourceMap);
    }

    private final HashMap<IR, Absyn> sourceMap;
    private final LabelFactory labelFactory;

    private TransExpressionFactory(LabelFactory labelFactory, HashMap<IR, Absyn> sourceMap) {
        this.sourceMap = sourceMap;
        this.labelFactory = labelFactory;
    }

    public Map<IR, Absyn> getSourceMap() {
        return Collections.unmodifiableMap(this.sourceMap);
    }

    public ExContext createEx(Absyn absyn, Exp exp) {
        this.sourceMap.put(exp, absyn);
        return new ExContext(labelFactory, exp);
    }

    public NxContext createNx(Absyn absyn, Stm s) {
        this.sourceMap.put(s, absyn);
        return new NxContext(labelFactory, s);
    }

    public RelCxContext createRelCx(Absyn absyn, Exp l, Exp r, int op) {
        this.sourceMap.put(l, absyn);
        this.sourceMap.put(r, absyn);
        return new RelCxContext(labelFactory, l, r, op);
    }

    public IfThenElseContext createIfThenElseExp(TranslateContext tst, TranslateContext aa, TranslateContext bb) {
        return new IfThenElseContext(labelFactory, tst, aa, bb);
    }
}