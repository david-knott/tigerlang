package com.chaosopher.tigerlang.compiler.translate;

import java.util.HashMap;
import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.IR;

/**
 * This class walks the IR tree and inserts mappings from IR nodes to the Absyn
 * node that generated them.
 */
class SourceMapHelper extends DefaultTreeVisitor {

    private final Absyn absyn;

    public SourceMapHelper(Absyn absyn) {
        this.absyn = absyn;
    }

    public static SourceMapHelper apply(IR target, Absyn absyn) {
        SourceMapHelper helper = new SourceMapHelper(absyn);
        target.accept(helper);
        return helper;
    }

    public Map<? extends IR, ? extends Absyn> getMappings() {
        return new HashMap<>();
    }
}

/**
 * Translate.Exp is a base class that all translated expresssion must extend
 * from. It provides 3 methods that contextualise a tree expression 1) unEx - an
 * expression that returns a result 2) unNx - an expression wihout a result 3)
 * unCx - an expression used in a conditional, arguments contain true and false
 * labels.
 */
abstract public class TranslateContext {

    protected final LabelFactory labelFactory;
    
    public TranslateContext(LabelFactory labelFactory) {
        this.labelFactory = labelFactory;
    }

    abstract com.chaosopher.tigerlang.compiler.tree.Exp unEx();

    abstract com.chaosopher.tigerlang.compiler.tree.Stm unNx();

    abstract com.chaosopher.tigerlang.compiler.tree.Stm unCx(Label t, Label f);

    
}