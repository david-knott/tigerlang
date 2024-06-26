package com.chaosopher.tigerlang.compiler.translate;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.ArrayExp;
import com.chaosopher.tigerlang.compiler.absyn.AssignExp;
import com.chaosopher.tigerlang.compiler.absyn.BreakExp;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FieldExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldVar;
import com.chaosopher.tigerlang.compiler.absyn.ForExp;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.IfExp;
import com.chaosopher.tigerlang.compiler.absyn.IntExp;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.NilExp;
import com.chaosopher.tigerlang.compiler.absyn.OpExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.StringExp;
import com.chaosopher.tigerlang.compiler.absyn.SubscriptVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.intel.IntelFrame;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelFactory;
import com.chaosopher.tigerlang.compiler.temp.Temp;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.EXPS;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.IR;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.NAME;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.types.Constants;
import com.chaosopher.tigerlang.compiler.types.FUNCTION;
import com.chaosopher.tigerlang.compiler.types.RECORD;
import com.chaosopher.tigerlang.compiler.types.Type;
import com.chaosopher.tigerlang.compiler.util.Assert;
import com.chaosopher.tigerlang.compiler.util.BoolList;

public class TranslatorVisitor extends DefaultVisitor {

    private final Hashtable<VarDec, Access> functionAccesses = new Hashtable<VarDec, Access>();
    private final Hashtable<com.chaosopher.tigerlang.compiler.absyn.Absyn, Label> functionLabels = new Hashtable<com.chaosopher.tigerlang.compiler.absyn.Absyn, Label>();
    private final Hashtable<FunctionDec, Level> functionLevels = new Hashtable<FunctionDec, Level>();
    private final Stack<Label> loopExits = new Stack<Label>();
    private final TransExpressionFactory transExpressionFactory;
    private TranslateContext visitedExp;
    private Level currentLevel;
    private FragList fragList;
    private LabelFactory labelFactory;

    /**
     * This method creates a new TranslatorVisitor instance and applys it
     * to the supplied Absyn.
     * @param absyn the Absyn instance to vist.
     * @return a new TranslatorVisitor instance.
     */
    public static TranslatorVisitor apply(Absyn absyn) {
        TranslatorVisitor translatorVisitor = new TranslatorVisitor();
        absyn.accept(translatorVisitor);
        return translatorVisitor;
    }

    public TranslatorVisitor() {
        this.labelFactory = new LabelFactory();
        this.transExpressionFactory = TransExpressionFactory.create(this.labelFactory, new HashMap<IR, Absyn>());

    }

    public Map<IR,Absyn> getSourceMap() {
        return this.transExpressionFactory.getSourceMap();
    }

    private TranslateContext getVisitedExp() {
        return this.visitedExp;
    }

    private void setVisitedExp(TranslateContext visitedExp) {
        this.visitedExp = visitedExp;
    }

    public FragList getFragList() {
        return this.fragList;
    }

    private void addFrag(Frag frag) {
        if(this.fragList == null) {
            this.fragList = new FragList(frag);
        } else {
            FragList end = this.fragList;
            while(end.tail != null) {
                end = end.tail;
            }
            end.tail = new FragList(frag);
        }
    }

    /**
     * Returns the current level or creates a new one if one is not
     * present.
     * @return existing level or newly created level.
     */
    private Level getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Sets the current level.
     * @param level the level.
     */
    private void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    /**
     * Returns a MEM object which represents a static link to a variable in a frame
     * higher up the stack. If the variable is defined in the current stack frame,
     * the static link will just refer to the current activation records frame
     * pointer.
     * @param access the @see Translate.Access, which contains the @see com.chaosopher.tigerlang.compiler.frame.Access
     *               and @see Translate.Level.
     * @param level  @see Translate.Level where we are accessing the variable from.
     * @return a @see com.chaosopher.tigerlang.compiler.tree.Exp containing MEM expressions.
     */
    private com.chaosopher.tigerlang.compiler.tree.Exp staticLinkOffset(Access access, Level level) {
        // start with the assumption that access is defined at same level 
        // create expression for frame pointer.
        com.chaosopher.tigerlang.compiler.tree.Exp exp = new TEMP(level.frame.FP());
        // if access is defined at same level as usage or
        // we dont need a static link to resolve the frame access, 
        // just return the frame pointer.
        if(!level.useStaticLink || level == access.home) {
            return exp;
        }
        // if level requires static link, the first argument of level formals will be the static link.
        AccessList levelFormals = level.formals;
        Access staticLink = levelFormals.head;
        // resolve the static link to either a temporary access or frame access expression.
        // if its a frame access, the returned value will be ( frame pointer - 8 )
        // if its a temp, it will just be a temp.
        com.chaosopher.tigerlang.compiler.tree.Exp resolvedSl = staticLink.acc.exp(exp);
        var slinkLevel = level;
        int staticLinkOffset = -8;
        // if access defined at a different level to usage, we need to calculate the static link
        while (slinkLevel.parent != access.home) {
            // for each nesting level, follow the pointer at frame pointer - 8, which is the static link.
         //   exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            resolvedSl = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), resolvedSl));
            slinkLevel = slinkLevel.parent;
        }
        return resolvedSl;
    }

    @Override
    public void visit(ArrayExp arrayExp) {
        arrayExp.size.accept(this);
        TranslateContext sizeExp = this.getVisitedExp();
        arrayExp.init.accept(this);
        TranslateContext initExp = this.getVisitedExp();
        Temp arrayPointer = Temp.create();
        ExpList args = new ExpList(sizeExp.unEx(), new ExpList(initExp.unEx(), null));
        this.setVisitedExp(this.transExpressionFactory.createEx(
            arrayExp,
            new ESEQ(
                new MOVE(
                    new TEMP(arrayPointer), 
                    this.getCurrentLevel().frame.externalCall("initArray", args)
                ),
                new TEMP(arrayPointer)
            )
        ));
    }

    @Override
    public void visit(AssignExp assignExp) {
        assignExp.var.accept(this);
        TranslateContext varExp = this.getVisitedExp();
        assignExp.exp.accept(this);
        TranslateContext expExp = this.getVisitedExp();
        this.setVisitedExp(
            this.transExpressionFactory.createNx(
                assignExp,
                new MOVE(varExp.unEx(), expExp.unEx())
            )
        );
    }

    @Override
    public void visit(BreakExp breakExp) {
        if(this.loopExits.empty()) {
            Assert.unreachable();
        }
        Label loopEnd = this.loopExits.peek();
        this.setVisitedExp(
            this.transExpressionFactory.createNx(
                breakExp,
                new JUMP(loopEnd)
            )
        );
    }

    private com.chaosopher.tigerlang.compiler.tree.Exp getFunctionStaticLink(Level usageLevel, Level definedLevel) {
        AccessList levelFormals = usageLevel.formals;
        Access staticLink = levelFormals.head;
        com.chaosopher.tigerlang.compiler.tree.Exp sle = staticLink.acc.exp(new TEMP(definedLevel.frame.FP()));
        return sle;
    }

    /**
     * Translates a call ast node into IR code.
     */
    @Override
    public void visit(CallExp callExp) {
        Level usageLevel = this.getCurrentLevel();
        Level definedLevel = this.functionLevels.get(callExp.def);
        // if the function being called has no body its a primitive
        // and doesn't need a static link.
        FunctionDec defined = (FunctionDec) callExp.def;
        ExpList expList = null;
        // add the static link to expression list. This is a refernce to the
        // current activation records frame pointer address. This is passed
        // as the first argument to the callee function.
        boolean useStaticLink = defined.includeStaticLink();
        if (useStaticLink) {
            // if we calling from a function with a static link..
            com.chaosopher.tigerlang.compiler.tree.Exp staticLink = null;
            if (definedLevel == usageLevel) { // recursive or same level, pass callers static link ( not frame pointer )
                staticLink = getFunctionStaticLink(usageLevel, definedLevel);
            } else {
                // if calling a function in a higher level, we pass the callers frame pointer
                if (definedLevel.parent == usageLevel) {
                    staticLink = new TEMP(usageLevel.frame.FP());
                } else {
                    // if calling a function in a lower level, we pass the callers static link
                    Level l = usageLevel;
                    // get callers static link
                    staticLink = getFunctionStaticLink(usageLevel, definedLevel);
                    // keep adding pointer dereferences until we find a common parent level.
                    while (l.parent != definedLevel.parent) {
                        staticLink = new MEM(new BINOP(BINOP.MINUS, staticLink, new CONST(l.frame.wordSize())));
                        l = l.parent;
                    }
                }
            }
            expList = ExpList.append(expList, staticLink);
        }
        // visit each actual parameter of the called function and add to expList.
        for (com.chaosopher.tigerlang.compiler.absyn.ExpList argList = callExp.args; argList != null; argList = argList.tail) {
            argList.head.accept(this);
            TranslateContext translatedArg = this.getVisitedExp();
            expList = ExpList.append(expList, translatedArg.unEx());
        }
        //
        Label functionLabel = this.functionLabels.get(callExp.def);
        Type returnType = callExp.getType();
        Assert.assertNotNull(returnType);
        if (returnType.coerceTo(Constants.VOID)) {
            this.setVisitedExp(
                this.transExpressionFactory.createNx(
                    callExp,
                    new EXPS(new CALL(new NAME(functionLabel), expList))
                )
            );
        } else {
            this.setVisitedExp(
                this.transExpressionFactory.createEx(
                    callExp,
                    new CALL(new NAME(functionLabel), expList)
                )
            );
        }
    }

    @Override
    public void visit(DecList decList) {
        // only one item in the declist, just return it
        if(decList.tail == null) {
            decList.head.accept(this);
            return;
        }
        // visit the list of items. 
        SEQ first = null, temp = null, last = null;
        for(; decList.tail != null; decList = decList.tail) {
            decList.head.accept(this);
            if(first == null) {
                first = last = new SEQ(this.getVisitedExp().unNx(), null);
            } else {
                temp.right = new SEQ(this.getVisitedExp().unNx(), null);
                last = (SEQ)temp.right;
            }
            temp = last;
        }
        // 
        decList.head.accept(this);
        last.right = this.getVisitedExp().unNx();
        this.setVisitedExp(
            this.transExpressionFactory.createNx(
                decList,
                first
            )
        );
    }

    @Override
    public void visit(com.chaosopher.tigerlang.compiler.absyn.ExpList expList) {
        // only one item in the expression, just return it
        if(expList.tail == null) {
            expList.head.accept(this);
            return;
        }
        // two items in the expressioin list,
        if(expList.tail.tail == null) {
            expList.head.accept(this);
            TranslateContext firstItem = this.getVisitedExp();
            expList.tail.head.accept(this);
            TranslateContext lastItem = this.getVisitedExp();
            // ensure we return correct type
            if(expList.tail.head.getType().coerceTo(Constants.VOID)) {
                this.setVisitedExp(
                    this.transExpressionFactory.createNx(
                        expList,
                        new SEQ(firstItem.unNx(), lastItem.unNx())
                    )
                );
            } else {
                this.setVisitedExp(
                    this.transExpressionFactory.createEx(
                        expList,
                        new ESEQ(firstItem.unNx(), lastItem.unEx())
                    )
                );
            }
            return;
        }
        SEQ first = null, temp = null, last = null;
        TranslateContext sequenceItem = null;
        for(; expList.tail != null; expList = expList.tail) {
            expList.head.accept(this);
            sequenceItem = this.getVisitedExp();
            if(first == null) {
                first = last = new SEQ(sequenceItem.unNx(), null);
            } else {
                // if last item is at end
                if(expList.tail.tail != null) {
                    last = new SEQ(sequenceItem.unNx(), null);
                    temp.right = last;
                } else {
                    temp.right = sequenceItem.unNx();
                }
            }
            temp = last;
        }
        // if the last item is an expression, return an ESEQ
        // if not, we return return a SEQ.
        expList.head.accept(this);
        sequenceItem = this.getVisitedExp();
        if(expList.head.getType().actual() == Constants.VOID) {
            this.setVisitedExp(
                this.transExpressionFactory.createNx(
                    expList,
                    new SEQ(first, sequenceItem.unNx())
                )
            );
        } else {
            this.setVisitedExp(
                this.transExpressionFactory.createEx(
                    expList,
                    new ESEQ(first, sequenceItem.unEx())
                )
            );
        }
   }

    @Override
    public void visit(FieldExpList exp) {
        Assert.unreachable();
    }

    private int fieldIndex(FieldVar fieldVar) {
        RECORD recordType = (RECORD)fieldVar.var.getType().actual();
        Assert.assertNotNull(recordType);
        int fieldIndex = 0;
        for(;recordType != null; recordType = recordType.tail) {
            if(recordType.fieldName.equals(fieldVar.field)) {
               return fieldIndex;
            }
            fieldIndex++;
        }
        Assert.unreachable();
        return -1;
    }

    @Override
    public void visit(FieldVar fieldVar) {
        fieldVar.var.accept(this);
        TranslateContext transExp = this.getVisitedExp();
        int fieldIndex = this.fieldIndex(fieldVar);
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                fieldVar,
                new MEM(
                    new BINOP(
                        BINOP.PLUS, 
                        transExp.unEx(), 
                        new CONST(
                            fieldIndex * this.currentLevel.frame.wordSize()
                        )
                    )           
                )
            )
        );
    }

    @Override
    public void visit(ForExp forExp) {
        // Note that we create the access and labels
        // and add to loop exit stack before we start
        // visiting the sub expressions as they need to
        // present when visit them.
        // Create non escaping variable for lowVar
        Access lowVarTranslateAccess = this.getCurrentLevel().allocLocal(false);
        // store in hash table for future usage.
        functionAccesses.put(forExp.var, lowVarTranslateAccess);
        // create labels and temps
        Temp limit = Temp.create();
        Label forStart = this.labelFactory.create();
        Label loopStart = this.labelFactory.create();
        Label loopExit1 = this.labelFactory.create();
        // Push loopend label onto stack.
        Label loopEnd = this.labelFactory.create();
        this.loopExits.push(loopEnd);
        // visit sub expressions.
        forExp.hi.accept(this);
        TranslateContext exphi = this.getVisitedExp();
        forExp.var.init.accept(this);
        TranslateContext explo = this.getVisitedExp();
        forExp.body.accept(this);
        TranslateContext expbody = this.getVisitedExp();
        // TODO: is static link needed here ? lowVar will always be defined at same nesting level.
        com.chaosopher.tigerlang.compiler.tree.Exp lowVar = lowVarTranslateAccess.acc.exp(staticLinkOffset(lowVarTranslateAccess, this.getCurrentLevel()));
		this.setVisitedExp(
            this.transExpressionFactory.createNx(
                forExp,
                new SEQ(
                    new MOVE(lowVar, explo.unEx()),
                    new SEQ(
                        new MOVE(new TEMP(limit), exphi.unEx()),
                        new SEQ(
                            new LABEL(forStart),
                            new SEQ(
                                new CJUMP(CJUMP.LE, lowVar, new TEMP(limit), loopStart, loopEnd),
                                new SEQ(
                                    new LABEL(loopStart),
                                    new SEQ(
                                        expbody.unNx(),
                                        new SEQ(
                                            new CJUMP(CJUMP.EQ, lowVar, new TEMP(limit), loopEnd, loopExit1), //check if int at max
                                            new SEQ(
                                                new LABEL(loopExit1),
                                                new SEQ(
                                                    new MOVE(lowVar, new BINOP(BINOP.PLUS, lowVar, new CONST(1))),
                                                    new SEQ(
                                                        new JUMP(forStart),
                                                        new LABEL(loopEnd)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ) 
                    )
                )
            )
        );
        // out of loop, pop last exit label.
        this.loopExits.pop();
    }

    @Override
    public void visit(FunctionDec functionDec) {
        //create new level and frame to store locals
        for(FunctionDec current = functionDec; current != null; current = current.next) {
            if(current.body == null) {
                // No level for primitives.
                Label label = this.labelFactory.create(current.name);
                this.functionLabels.put(current, label);
            } else if(current.name.toString().equals("tigermain")) {
                Label label = this.labelFactory.create("tigermain");
                // getBoolList is null here...
                Level level = new Level(new IntelFrame(label, getBoolList(current.params)));
                this.functionLevels.put(current, level);
                this.functionLabels.put(current, label);
            } else {
                // notice that we create the new level
                // using the function formal arguments
                // these are supplied so the level & frame
                // can create com.chaosopher.tigerlang.compiler.frame.Access ( Temp or Mem )
                // for the function.
                Label label = this.labelFactory.create();
                // create level and access for function
                Level level =
                    new Level(
                        this.getCurrentLevel(), 
                        label, 
                        getBoolList(
                            current.params
                        ),
                        current.includeStaticLink() /* create static link */,
                        current.staticLinkEscapes
                    );
                this.functionLabels.put(current, label);
                this.functionLevels.put(current, level);
            }
        }
        // visit the declations again 
        for(FunctionDec current = functionDec; current != null; current = current.next) {
            if(current.body != null) {
                // save current level.
                Level parent = this.getCurrentLevel();
                // find level created for function and enter it.
                this.setCurrentLevel(this.functionLevels.get(current));
                // get functions formal list from the ast, no static link.
                DecList formalVarDecs = current.params;
                // get reference to level formals
                AccessList formals = this.getCurrentLevel().formals;
                // skip the static link, if present.
                if(!current.name.toString().equals("tigermain") && current.includeStaticLink()) {
                    formals = formals.tail;
                }
                // add formal parameters to access lookup so they
                // can be used inside the function body.
                for(; formals != null; formals = formals.tail) {
                    Access access = formals.head;
                    this.functionAccesses.put((VarDec)formalVarDecs.head, access);
                    formalVarDecs = formalVarDecs.tail;
                }
                // visit body of function using new saved level
                current.body.accept(this);
                // get translated fragment.
                TranslateContext translatedBody = this.getVisitedExp();
                // if funcion returns, place result into RV register
                FUNCTION functionType = (FUNCTION)current.getType();
                Assert.assertNotNull(functionType);
                if(!functionType.result.coerceTo(Constants.VOID)) {
                    //translatedBody = new NxContext(new MOVE(new TEMP(this.currentLevel.frame.RV()), translatedBody.unEx()));
                    translatedBody = this.transExpressionFactory.createNx(current.body,  new MOVE(new TEMP(this.currentLevel.frame.RV()), translatedBody.unEx()));
                }
                // creates a new fragment for the function.
                this.procEntryExit(this.getCurrentLevel(), translatedBody);
                // reset the current level back
                this.setCurrentLevel(parent);
            }
        }
        // reset the visitedExp to nil
        // as we create a new Fragment for
        // the next function
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                functionDec,
                new CONST(0)
            )
        );
    }

    /**
     * This function saves a translated rocFrag to an internal
     * linked list. This linked list is used after translate
     * has completed and it passed to the later stages.
     * @param level the current static function level
     * @param body  the body of the function we are translating
     */
    private void procEntryExit(Level level, TranslateContext body) {
        if (body == null)
            return;
        this.addFrag(new ProcFrag(body.unNx(), level.frame));
    }

    /**
     * Converts a field list into a BoolList. Each
     * boolean in this list represents a variable, where
     * a true indicates the variable should escape and false
     * where it should not.
     * @param fields @see com.chaosopher.tigerlang.compiler.absyn.FieldList which can be null.
     * @return a single linked list where each node represents either a true or false value or null.
     */
    private BoolList getBoolList(final DecList decList) {
        BoolList boolList = null; //
        if (decList != null) {
            VarDec varDec = (VarDec)decList.head;
            boolList = new BoolList(varDec.escape, null);
            var fieldTail = decList.tail;
            while (fieldTail != null) {
                VarDec vd2 = (VarDec)fieldTail.head;
                boolList.append(vd2.escape);
                fieldTail = fieldTail.tail;
            }
        }
        return boolList;
    }

    @Override
    public void visit(IfExp ifExp) {
        ifExp.test.accept(this);
        TranslateContext test = this.getVisitedExp();
        ifExp.thenclause.accept(this);
        TranslateContext then = this.getVisitedExp();
        TranslateContext els = null;
        if(ifExp.elseclause != null) {
            ifExp.elseclause.accept(this);
            els = this.getVisitedExp();
        }
        this.setVisitedExp(
            this.transExpressionFactory.createIfThenElseExp(test, then, els)
        );
    }

    @Override
    public void visit(IntExp intExp) {
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                intExp,
                new CONST(intExp.value)
            )
        );
    }

    @Override
    public void visit(LetExp letExp) {
        TranslateContext decs = null;
        if(letExp.decs != null) {
            letExp.decs.accept(this);
            decs = this.getVisitedExp();
        } else {
            decs = this.transExpressionFactory.createEx(letExp, new CONST(0)); //new ExContext(new CONST(0));
        }
        TranslateContext body = null;
        Type bodyType = null;
        if(letExp.body != null) {
            letExp.body.accept(this);
            body = this.getVisitedExp();
            bodyType = letExp.body.getType();
        } else {
            body = this.transExpressionFactory.createEx(letExp, new CONST(0)); //new ExContext(new CONST(0));
            bodyType = Constants.VOID;
        }
        this.setVisitedExp(bodyType.coerceTo(Constants.VOID) 
            ?
            this.transExpressionFactory.createNx(
                letExp,
                new SEQ(
                    decs.unNx(),
                    body.unNx()
                )
            ) : 
            this.transExpressionFactory.createEx(
                letExp,
                new ESEQ(
                    decs.unNx(),
                    body.unEx()
                )
            )
        );
    }

    @Override
    public void visit(NilExp nilExp) {
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                nilExp,
                new CONST(0)
            )
        );
    }

    @Override
    public void visit(OpExp opExp) {
        opExp.left.accept(this);
        TranslateContext leftTrans = this.getVisitedExp();
        opExp.right.accept(this);
        TranslateContext rightTrans = this.getVisitedExp();
        switch(opExp.oper) {
            case OpExp.DIV: case OpExp.MINUS: case OpExp.MUL: case OpExp.PLUS:
                this.setVisitedExp(
                    this.transExpressionFactory.createEx(
                        opExp,
                        new BINOP(
                            opExp.oper, 
                            leftTrans.unEx(), 
                            rightTrans.unEx()
                        )
                    )
                );
                break;
            default:
                int relop = 0;
                String strOp = null;
                switch(opExp.oper) {
                    case OpExp.EQ:
                        relop = CJUMP.EQ;
                        strOp = "streq";
                        break;
                    case OpExp.NE:
                        relop = CJUMP.NE;
                        strOp = "streq";
                        break;
                    case OpExp.GE:
                        relop = CJUMP.GE;
                        strOp = "strcmp";
                        break;
                    case OpExp.GT:
                        relop = CJUMP.GT;
                        strOp = "strcmp";
                        break;
                    case OpExp.LE:
                        relop = CJUMP.LE;
                        strOp = "strcmp";
                        break;
                    case OpExp.LT:
                        relop = CJUMP.LT;
                        strOp = "strcmp";
                        break;
                    default:
                        Assert.unreachable("Unknown operator:" + opExp.oper);
                }
                if(opExp.left.getType().coerceTo(Constants.STRING) && opExp.right.getType().coerceTo(Constants.STRING)) {
                    Temp result = Temp.create();
                    this.setVisitedExp(
                        this.transExpressionFactory.createEx(
                            opExp,
                            new ESEQ(
                                new MOVE(
                                    new TEMP(result),
                                    this.currentLevel.frame.externalCall(
                                        strOp, 
                                        new ExpList(
                                            leftTrans.unEx(),
                                            new ExpList(
                                                rightTrans.unEx()
                                            )
                                        )
                                    )  
                                ), 
                                new TEMP(result)
                            )
                        )
                    );
                } else {
                    this.setVisitedExp(
                        this.transExpressionFactory.createRelCx(opExp, leftTrans.unEx(), rightTrans.unEx(), relop)
                    );
                }
            break;
        }
    }

    /**
     * Helper function used by @see TranslatorVisitor.visit(RecordExp).
     * @param recordPointer the record pointer.
     * @param total the total bytes required to store the record.
     * @param fieldTranslated the translated @see Translate.Exp
     * @return a @see com.chaosopher.tigerlang.compiler.tree.MOVE statement, which moves the fieldTranslated into offset total * 
     * wordSize from record pointer.
     */
    private Stm fieldStatement(Temp recordPointer, int total, TranslateContext fieldTranslated) {
        return new MOVE(
            new MEM(
                new BINOP(
                    BINOP.PLUS, 
                    new TEMP(recordPointer), 
                    new CONST(this.getCurrentLevel().frame.wordSize() * total)
                )
            ),
            fieldTranslated.unEx()
        );
    }

    /**
     * Translates a record expression eg rectype { a = 1, b = 2}, into
     * IR. This IR initializes the record on the heap and returns a pointer
     * which can be stored for future computations.
     */
    @Override
    public void visit(RecordExp recordExp) {
        // no fields, so no data to store, so dont do anything.
        if(recordExp.fields == null) {
            return;
        }
        // base heap pointer for record. 
        Temp recordPointer =  Temp.create();
        // build all the statements that initialise the record.
        int size = 0;
        for (FieldExpList s = recordExp.fields; s != null; s = s.tail) {
            size += this.getCurrentLevel().frame.wordSize();
        }
        // visit field init and capture it in member var visitedExp
        recordExp.fields.init.accept(this);
        TranslateContext fieldTranslated = this.getVisitedExp();
        int fieldCounter = 0;
        Stm stm = this.fieldStatement(recordPointer, fieldCounter++, fieldTranslated);
        // more than one item in list, so create a SEQ.
        if(recordExp.fields.tail != null) {
            stm = new SEQ(stm, null);
            FieldExpList rest = recordExp.fields.tail;
            // we only iterate from second item to second last item
            // because the last item should not be created in a SEQ 
            // with a null right value
            // We dont want this (SEQ(secondLast, SEQ(last, null)))
            // We want this (SEQ(secondLast, last))
            ///
            // last is assigned to stm here as we use it at to capture the last item in the list.
            // as a right value for the last sequence.
            SEQ temp = (SEQ)stm, last = (SEQ)stm;
            for(; rest.tail != null; rest = rest.tail) {
                // visit field init and capture it in member var visitedExp
                rest.init.accept(this);
                fieldTranslated = this.getVisitedExp();
                Stm middle = this.fieldStatement(recordPointer, fieldCounter++, fieldTranslated);
                // create new last item
                last = new SEQ(middle, null);
                // creater point from previously last item to new last
                temp.right = last;
                // set previously last item to new last
                temp = last;
            }
            // visit field init and capture it in member var visitedExp
            rest.init.accept(this);
            fieldTranslated = this.getVisitedExp();
            last.right = this.fieldStatement(recordPointer, fieldCounter++, fieldTranslated);
        }
        // build expression sequence, where left value is a statement
        // and right value is a expression result, which in this case is the 
        // record pointer. 
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                recordExp,
                new ESEQ(
                    new SEQ(
                        new MOVE(
                            new TEMP(recordPointer),
                            this.getCurrentLevel().frame.externalCall(
                                "initRecord", 
                                new ExpList(
                                    new CONST(size), 
                                    null
                                )
                            )
                        ), 
                        stm
                    ),
                    new TEMP(recordPointer)
                )
            )
        );
    }

    @Override
    public void visit(SeqExp seqExp) {
        if(seqExp.list != null) {
            seqExp.list.accept(this);
        } else {
            this.setVisitedExp(
                this.transExpressionFactory.createEx(
                    seqExp,
                    new CONST(0)
                )
            );
        }
    }

    @Override
    public void visit(SimpleVar simpleVar) {
        // Lookup variable declaration reference 
        Assert.assertNotNull(simpleVar.def);
        Access access = functionAccesses.get((VarDec)simpleVar.def);
        Assert.assertNotNull(access);
        // Compute static link to variable using definition and current level
        com.chaosopher.tigerlang.compiler.tree.Exp stateLinkExp =  staticLinkOffset(access, this.getCurrentLevel());
        // Set visited expression.
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                simpleVar,
                access.acc.exp(stateLinkExp)
            )
        );
    }

    /**
     * This method visits a StringExp. It creates a new Label for the string
     * and passes this and the string literal to the frame to convert to create a 
     * new assembly fragment. This assembly fragment is stored in a linked list.
     */
    @Override
    public void visit(StringExp stringExp) {
        Label label = this.labelFactory.create();
        addFrag(new DataFrag(label, stringExp.value, this.getCurrentLevel().frame));
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                stringExp,
                new NAME(label)
            )
        );
    }

    @Override
    public void visit(SubscriptVar subscriptVar) {
        subscriptVar.index.accept(this);
        TranslateContext indexExp = this.getVisitedExp();
        subscriptVar.var.accept(this);
        TranslateContext baseExp = this.getVisitedExp();
        this.setVisitedExp(
            this.transExpressionFactory.createEx(
                subscriptVar,
                new MEM(
                    new BINOP(
                        BINOP.PLUS, 
                        baseExp.unEx(), 
                        new BINOP(
                            BINOP.MUL, 
                            indexExp.unEx(),
                            new CONST(getCurrentLevel().frame.wordSize())
                        )
                    )                
                )
            )
        );
    }
    
    @Override
    public void visit(VarDec varDec) {
        varDec.init.accept(this);
        TranslateContext initExp = this.getVisitedExp();
        // create a new access for this variable.
        Access translateAccess = this.getCurrentLevel().allocLocal(varDec.escape);
        // store in hash table for future usage.
        functionAccesses.put(varDec, translateAccess);
        // create tree expression along with static link calculation ( is this needed ?? )
        com.chaosopher.tigerlang.compiler.tree.Exp decExp = translateAccess.acc.exp(staticLinkOffset(translateAccess, this.getCurrentLevel()));
        this.setVisitedExp(
            this.transExpressionFactory.createNx(
                varDec,
                new MOVE(
                    decExp,
                    initExp.unEx()
                )
            )
        );
    }

    @Override
    public void visit(VarExp varExp) {
        varExp.var.accept(this);
    }

    @Override
    public void visit(WhileExp whileExp) {
        // create labels and temps first
        // add to loop list. Then visit
        // the sub expressions.
        Label loopEnd = this.labelFactory.create();
        this.loopExits.push(loopEnd);
        var whileStart = this.labelFactory.create();
        var loopStart = this.labelFactory.create();
        whileExp.test.accept(this);
        TranslateContext testExp = this.getVisitedExp();
        whileExp.body.accept(this);
        TranslateContext transBody = this.getVisitedExp();
        this.setVisitedExp(
            this.transExpressionFactory.createNx(
                whileExp,
                new SEQ(
                    new com.chaosopher.tigerlang.compiler.tree.LABEL(whileStart),
                    new SEQ(
                        testExp.unCx(loopStart, loopEnd), 
                        new SEQ(
                            new com.chaosopher.tigerlang.compiler.tree.LABEL(loopStart),
                            new SEQ(
                                transBody.unNx(), 
                                new SEQ(
                                    new com.chaosopher.tigerlang.compiler.tree.JUMP(whileStart), 
                                    new com.chaosopher.tigerlang.compiler.tree.LABEL(loopEnd)
                                )
                            )
                        )
                    )
                )
            )
        );
        this.loopExits.pop();
    }
}