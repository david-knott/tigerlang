package com.chaosopher.tigerlang.compiler.sugar;

import com.chaosopher.tigerlang.compiler.absyn.AssignExp;
import com.chaosopher.tigerlang.compiler.absyn.BreakExp;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.ForExp;
import com.chaosopher.tigerlang.compiler.absyn.IfExp;
import com.chaosopher.tigerlang.compiler.absyn.IntExp;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.OpExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.cloner.AbsynCloner;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.types.Constants;
import com.chaosopher.tigerlang.compiler.types.STRING;

public class Desugar extends AbsynCloner {

    private final boolean stringComparision;
    private final boolean forLoop;

    public Desugar(boolean stringComparision, boolean forLoop) {
        super();
        this.stringComparision = stringComparision;
        this.forLoop = forLoop;
    }

    @Override
    public void visit(OpExp exp) {
        if(
            this.stringComparision
            &&
            exp.left.getType() instanceof STRING
            &&
            exp.right.getType() instanceof STRING
        ) {
            CallExp callExp = new CallExp(
                exp.pos, 
                Symbol.symbol("strcmp"), 
                new ExpList(
                    exp.left, 
                    new ExpList(
                        exp.right,
                        null
                    )
                )
            );

            callExp.setType(Constants.INT);
            this.visitedExp = new OpExp(exp.pos, callExp, exp.oper, new IntExp(exp.pos, 0));
        } else {
            super.visit(exp);
        }
    }

    @Override
    public void visit(ForExp forExp) {
        if(!this.forLoop) {
            super.visit(forExp);
            return;
        }
        //convert forexp into a while loop
        //vardec i = 0
        //test condition 
        // - true execute body back to test condition
        // - false exit
        Symbol _lo = Symbol.symbol("%lo");
        Symbol _hi = Symbol.symbol("%hi");
        Symbol _i = forExp.var.name;
        int _ipos = forExp.var.pos;
        int _hipos = forExp.hi.pos;
        forExp.body.accept(this);
        Exp clonedBody = this.visitedExp;
        forExp.hi.accept(this);
        Exp clonedHi = this.visitedExp;
        forExp.var.accept(this);
        VarDec clonedVarDec = (VarDec)this.visitedDec;
        this.visitedExp = new LetExp(
            forExp.pos,
            new DecList(
                new VarDec(
                    _hipos, _lo, null, clonedVarDec.init
                ), 
                new DecList(
                    new VarDec(_hipos, _hi, null,  clonedHi),
                    new DecList(
                        new VarDec(_ipos, _i, null, new VarExp(0, new SimpleVar(0, _lo))), 
                        null
                    ) 
                )
            ), 
            new SeqExp(
                forExp.pos,
                new ExpList(
                    new IfExp(
                        forExp.pos, 
                        new OpExp(
                            forExp.pos, 
                            new VarExp(
                                forExp.pos, 
                                new SimpleVar(
                                    _ipos, 
                                    _i
                                )
                            ), 
                            OpExp.LE, 
                            new VarExp(
                                _hipos, 
                                new SimpleVar(
                                    _hipos, 
                                    _hi
                                )
                            )
                        ), 
                        new WhileExp(
                            forExp.pos, 
                            new IntExp(forExp.pos, 1), // while(1)
                            new SeqExp(
                               forExp.body.pos,
                               new ExpList(
                                    clonedBody, // body
                                    new ExpList(
                                        new IfExp( // if i = hi
                                            forExp.pos,
                                            new OpExp(
                                                forExp.pos,
                                                new VarExp(
                                                    _ipos, 
                                                    new SimpleVar(
                                                       _ipos, 
                                                       _i
                                                    )
                                                ), 
                                                OpExp.EQ,
                                                new VarExp(
                                                    _hipos, 
                                                    new SimpleVar(
                                                       _hipos, 
                                                       _hi
                                                    )
                                                )
                                            ),
                                            new BreakExp(0), // i = hi then break
                                            new AssignExp(// false add 1 to i
                                                _ipos,
                                                new SimpleVar(
                                                    _ipos, 
                                                    _i
                                                ),
                                                new OpExp(
                                                    _ipos, 
                                                    new VarExp(
                                                        _ipos, 
                                                        new SimpleVar(
                                                            _ipos, 
                                                            _i
                                                        )
                                                    ),
                                                    OpExp.PLUS, 
                                                    new IntExp(
                                                        _ipos, 
                                                        1
                                                    )
                                                )
                                           )
                                        ),
                                        null
                                    )
                               )
                            )
                        ) // end while.
                    ), 
                    null
                )
            )
        );
    }
}