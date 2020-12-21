package com.chaosopher.tigerlang.compiler.types;

import java.util.Collection;
import java.util.LinkedList;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.ArrayExp;
import com.chaosopher.tigerlang.compiler.absyn.AssignExp;
import com.chaosopher.tigerlang.compiler.absyn.BreakExp;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.Exp;
import com.chaosopher.tigerlang.compiler.absyn.ExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldExpList;
import com.chaosopher.tigerlang.compiler.absyn.FieldList;
import com.chaosopher.tigerlang.compiler.absyn.FieldVar;
import com.chaosopher.tigerlang.compiler.absyn.ForExp;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.IfExp;
import com.chaosopher.tigerlang.compiler.absyn.IntExp;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.NilExp;
import com.chaosopher.tigerlang.compiler.absyn.OpExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.StringExp;
import com.chaosopher.tigerlang.compiler.absyn.TypeDec;
import com.chaosopher.tigerlang.compiler.absyn.Var;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.util.Assert;

/**
 * A TypeChecker visitor is used to check the <see ref="com.chaosopher.tigerlang.compiler.absyn.Exp. 
 * Againts the defined language specification, also know as the semantic rules.
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d4.html#TC_002d4
 * https://gitlab.lrde.epita.fr/tiger/tc-base/-/blob/2022/src/type/type-checker.cc
 * This class implements the default visitor. Each visit method sets the correct type on its
 * formal argument. It uses the information gathered from visiting the desecdant nodes.
 */
public class TypeChecker extends DefaultVisitor {

    final ErrorMsg errorMsg;
    Collection<Absyn> readonlyVarDecs = null;

    public TypeChecker(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        this.readonlyVarDecs = new LinkedList<>();
    }

    private void checkTypes(Absyn loc, String synCat1, Type first, String synCat2, Type second) {
        if (!second.coerceTo(first)) {
            this.errorMsg.error(loc.pos,
                    String.format("type mismatch%n%s:%s%n%s:%s", synCat1, first.actual(), synCat2, second.actual()));
        }
    }


    /** ============== AST Expressions ============================== **/

    /**
     * Sets current visited type to Int.
     */
    @Override
    public void visit(IntExp exp) {
        exp.setType(Constants.INT);
    }

    /**
     * Sets current visisted type to String.
     */
    @Override
    public void visit(StringExp exp) {
        exp.setType(Constants.STRING);
    }

    /**
     * Sets current visisted type to Nil.
     */
    @Override
    public void visit(NilExp exp) {
        exp.setType(Constants.NIL);
    }

    /**
     * Visits a break expression and sets the type of the expression to void.
     */
    @Override
    public void visit(BreakExp exp) {
        exp.setType(Constants.VOID);
    }

    /**
     * Visit a record expression and sets it type to that of its definition. This method
     * also visits the record expression's field list to ensure the record fields are correct.
     */
    @Override
    public void visit(RecordExp exp) {
        VarDec def = (VarDec)exp.def;
        Assert.assertIsTrue(def.getType() instanceof RECORD);
        // set this expressions type.
        exp.setType(def.getType());
        // visit the field list inline as we need its record type. 
        RECORD record = (RECORD)def.getType();
        FieldExpList fieldExpList = exp.fields;
        // loop through all fields and check types match. 
        while(record != null && fieldExpList != null) {
            // visit field expression to compute type.
            fieldExpList.init.accept(this);
            // check types.
            Type fieldType = fieldExpList.init.getType();
            this.checkTypes(fieldExpList, "expected field type", record.fieldType, "supplied field type", fieldType);
            // move to next pair of fields.
            record = record.tail;
            fieldExpList = fieldExpList.tail;
        }
        // if either linked list is not empty, we have either too many or too few parameters.
        if(record != null) {
            this.errorMsg.error(exp.pos, "Missing record fields: " + record.fieldName);
        }
        if(fieldExpList != null) {
            this.errorMsg.error(exp.pos, "Uknown record field: " + fieldExpList.name);
        }
    }

    /**
     * Visits an array expression and sets its type.
     */
    @Override
    public void visit(ArrayExp exp) {
        exp.init.accept(this);
        Type initType = exp.init.getType();
        TypeDec def = (TypeDec)exp.def;
        Assert.assertIsTrue(def.getType() instanceof ARRAY);
        ARRAY arrayType = (ARRAY)def.ty.getType(); 
        this.checkTypes(exp, "array initialiser", initType, "expected type", arrayType.element);
        exp.size.accept(this);
        Type sizeType = exp.size.getType();
        this.checkTypes(exp, "array size", sizeType, "expected type", Constants.INT);
        exp.setType(arrayType);
    }

    /**
     * Set the type of the field var expression to the type of the field being accessed.
     */
    @Override
    public void visit(FieldVar exp) {
        // get the defining variable ( VarDec ) and its type.
        VarDec def = (VarDec)exp.var.def;
        Assert.assertIsTrue(def.getType() instanceof RECORD);
        RECORD record = (RECORD)def.getType();
        for(; record != null; record = record.tail) {
            if(record.fieldName == exp.field) {
                exp.setType(record.fieldType);
                return;
            }
        }
        this.errorMsg.error(exp.pos, "Unknown record type:" + exp.field);
    }

    /**
     * Visits a call node. The implementation checks that the actual arguments match
     * the formal arguments in the function definition. It sets the current visited
     * type to the return type of the function.
     */
    @Override
    public void visit(CallExp exp) {
        FUNCTION function = (FUNCTION)((FunctionDec)exp.def).getType();
        ExpList actuals = exp.args;
        RECORD formals = function.formals;
        while(actuals != null && formals != null) {
            // visit each actual parameter
            actuals.head.accept(this);
            Type actualType = actuals.head.getType();
            // get the type of the formal from its types def.
            this.checkTypes(actuals.head, "supplied arg type", actualType, "expected arg type", formals.fieldType);
            actuals = actuals.tail;
            formals = formals.tail;
        }
        if(actuals != null) {
            this.errorMsg.error(actuals.head.pos, "more actuals than expected:" + actuals.head);
        }
        if(formals != null) {
            this.errorMsg.error(exp.pos, "less actuals than expected:" + formals.fieldName);
        }
        exp.setType(function.result);
    }

    /**
     * Visit an if expression. Check type rules.
     */
    @Override
    public void visit(IfExp exp) {
        exp.test.accept(this);
        Type testType = exp.test.getType();
        this.checkTypes(exp.test, "", testType, "", Constants.INT);
        exp.thenclause.accept(this);
        Type thenType = exp.thenclause.getType();
        if(exp.elseclause != null) {
            exp.elseclause.accept(this);
            Type elseType = exp.elseclause.getType();
            this.checkTypes(exp.elseclause, "then", thenType, "else", elseType);
        }
        exp.setType(exp.elseclause != null ? thenType : Constants.VOID);
    }

    /**
     * Type check implementation for simple var. Sets the simple var type to 
     * its defining variable declarations initializer type.
     */
    @Override
    public void visit(SimpleVar exp) {
        VarDec def = (VarDec)exp.def;
        exp.setType(def.init.getType());
    }

    /**
     * Visit sequence expression and set its type to the last expression.
     */
    @Override
    public void visit(SeqExp exp) {
        super.visit(exp);
        if(exp.list == null) {
            exp.setType(Constants.VOID);
        } else {
            ExpList last = exp.list;
            for(; last.tail != null; last = last.tail);
            exp.setType(last.head.getType());
        }
    }

    /**
     * Visit a NameTy node.
     */
    @Override
    public void visit(NameTy exp) {
        // todo: implement.
    }

    /**
     * Type check implementation for ForExp. Checks that the hi and low values are
     * integers and marks the loop low variable declaration as readonly.
     */
    @Override
    public void visit(ForExp exp) {
        exp.hi.accept(this);
        Type hiType = exp.hi.getType();
        this.checkTypes(exp, "for hi type", hiType, "expected type", Constants.INT);
        exp.var.accept(this);
      //  exp.var.init.accept(this);
        Type varType = exp.var.init.getType();
        // loop var marked as readonly.
        this.readonlyVarDecs.add(exp.var);
        this.checkTypes(exp, "for var type", varType, "expected type", Constants.INT);
        exp.body.accept(this);
        exp.setType(Constants.VOID);
    }

    /**
     * Visit a binary operator and check the types are compatible.
     */
    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        Type leftType = exp.left.getType();
        exp.right.accept(this);
        Type rightType = exp.right.getType();
        this.checkTypes(exp, "left type", leftType, "right type", rightType);
        // if the operation is a comparision, rather than arithmetic
        switch(exp.oper) {
            case OpExp.DIV: case OpExp.MINUS: case OpExp.MUL: case OpExp.PLUS:
                exp.setType(rightType);
                break;
            default:
                exp.setType(Constants.INT);
        }
    }

    /**
     * Type checks an AssignExp. Ensures that the lvalue and rvalue types are
     * compatible. If they are not, an error message is generated.
     */
    @Override
    public void visit(AssignExp exp) {
        // check lvaue type is same type as rvalue
        exp.var.accept(this);
        Type leftType = exp.var.getType();
        exp.exp.accept(this);
        Type rightType = exp.exp.getType();
        this.checkTypes(exp, "right operand type", rightType, "expected type", leftType);
        if (this.readonlyVarDecs.contains(exp.var.def)) {
            VarDec varr = (VarDec)exp.var.def;            
            this.errorMsg.error(exp.pos, "variable " + varr.name + " is read only");
        }
        exp.setType(Constants.VOID);
    }

    /**
     * Type check a WhileExp. Ensures the body returns void and the test is an int.
     */
    @Override
    public void visit(WhileExp exp) {
        exp.test.accept(this);
        Type testType = exp.test.getType();
        this.checkTypes(exp.test, "while test type", testType, "expected type", Constants.INT);
        exp.body.accept(this);
        Type bodyType = exp.body.getType();
        this.checkTypes(exp.body, "while body type", bodyType, "expected type", Constants.VOID);
        exp.setType(Constants.VOID);
    }


    /** ============== AST Declarations ============================== **/

    /**
     * Represents the declaration of a variable which may include the type for
     * example: var myVar:int = 1, or as a function argument.
     */
    @Override
    public void visit(VarDec exp) {
        // visit initializer, if present.
        Type initType = null;
        if(exp.init != null) {
            exp.init.accept(this);
            initType = exp.init.getType();
        }
        // expect that the initizer type is set here ( either int, string, namety, recordty, arrayty)
        if (exp.typ != null) {
            exp.typ.accept(this);
            Type declaredType = exp.typ.getType();
            if(initType != null) {
                this.checkTypes(exp, "declared", declaredType, "actual", initType);
            }
        }
        exp.setType(Constants.VOID);
    }

    /**
     * Visit a variable expression. Sets the type of this variable expression to the same type
     * as the referenced variable.
     */
    @Override
    public void visit(VarExp exp) {
        exp.var.accept(this);
        exp.setType(exp.var.getType());
    }

    /**
     * Visit a type declaration, set the type.
     */
    @Override
    public void visit(TypeDec exp) {
        //figure out the type type 
    }


    /**
     * Visit a function declaration, compute the type of the
     * of the function.
     */
    @Override
    public void visit(FunctionDec exp) {
        // first pass
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
 
        }
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {

        }
        //figure out the function type.
    }

}