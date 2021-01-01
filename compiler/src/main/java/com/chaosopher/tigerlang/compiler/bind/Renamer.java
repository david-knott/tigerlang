package com.chaosopher.tigerlang.compiler.bind;

import java.util.Hashtable;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FieldList;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.IntTypeDec;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.StringTypeDec;
import com.chaosopher.tigerlang.compiler.absyn.TypeDec;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * Ensures that all variables, types and functions have unique names
 * within a AST. This is done on the original AST. 
 * Note the field names are unaffected.
 */
public class Renamer extends DefaultVisitor {

    private int id = 0;
    private Hashtable<Absyn, Symbol> newNames = new Hashtable<Absyn, Symbol>();

    public Renamer() {
        this.newNames.put(IntTypeDec.instance, Symbol.symbol("int"));
        this.newNames.put(StringTypeDec.instance, Symbol.symbol("string"));
    }

    @Override
    public void visit(TypeDec exp) {
        String uniqueTypeName = exp.name.toString() + "_" + (this.id++);
        Symbol newSymbol = Symbol.symbol(uniqueTypeName);
        newNames.put(exp, newSymbol);
        exp.name = newSymbol;
        if(exp.next != null) {
            exp.next.accept(this);
        }
    }

    @Override
    public void visit(VarDec exp) {
        if(exp.typ != null) {
            exp.typ.name = newNames.get(exp.typ.def);
        }
        String uniqueVarName = exp.name + "_" + (this.id++);
        Symbol newSymbol = Symbol.symbol(uniqueVarName);
        newNames.put(exp, newSymbol);
        exp.name = newSymbol;
        exp.init.accept(this);
    }

    private boolean isSpecialName(FunctionDec exp) {
        return (exp.body == null || exp.name.toString().equals("tigermain"));
    }

    @Override
    public void visit(FunctionDec exp) {
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(!isSpecialName(functionDec)) {
                String uniqueFunctionName = functionDec.name.toString() + "_" + (this.id++);
                Symbol newSymbol = Symbol.symbol(uniqueFunctionName);
                newNames.put(functionDec, newSymbol);
                functionDec.name = newSymbol;
            }
        }
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(!isSpecialName(functionDec)) {
                if(functionDec.params != null) {
                    // update the ty name, create new symbol for formal args, except for int & strings.
                    for(DecList decList = functionDec.params; decList != null; decList = decList.tail) {
                        VarDec varDec = (VarDec)decList.head;
                        // set the renamed type symbol.
                        // null would indicate typ is int or string.
                     //   if(varDec.typ.def != null) {
                            varDec.typ.name =  newNames.get(varDec.typ.def);
                            Assert.assertNotNull(varDec.typ.name);
                      //  }
                        // create new param names for formal arguments.
                        String uniqueParamName = varDec.name + "_" + (this.id++);
                        Symbol newPSymbol = Symbol.symbol(uniqueParamName);
                        newNames.put(varDec, newPSymbol);
                        varDec.name = newPSymbol;
                    }
                }
            }
            // rename the return type of the function, except for int & strings.
            if (functionDec.result != null) {
                functionDec.result.accept(this);
            }
            // process body.
            if(functionDec.body != null) {
                functionDec.body.accept(this);
            }
        }
    }

    /**
     * Sets the name field in the NameTy to its renamed
     * equivalent, except when then the NameTy references 
     * a string or int type.
     */
    @Override
    public void visit(NameTy exp) {
        if(exp.name == Symbol.symbol("int") || exp.name == Symbol.symbol("string")) {
            return;
        }
        Symbol newSymbol = newNames.get(exp.def);
        exp.name = newSymbol;
    }

    /**
     * Sets the function name in the CallExp to its
     * renamed equivalent, except when the function is
     * a runtime function.
     */
    @Override
    public void visit(CallExp exp) {
        // rename call if not primitive.
        if(((FunctionDec)exp.def).body != null) {
            exp.func = newNames.get(exp.def);
        }
        if(exp.args != null) {
            exp.args.accept(this);
        }
    }

    /**
     * Sets the record type to its renamed equivalent.
     */
    @Override
    public void visit(RecordExp exp) {
        exp.typ.name = newNames.get(exp.typ.def);
        exp.fields.accept(this);
    }
    
    /**
     * Sets a simple variable reference to its renamed
     * equivalent.
     */
    @Override
    public void visit(SimpleVar exp) {
        exp.name = newNames.get(exp.def);
    }
}