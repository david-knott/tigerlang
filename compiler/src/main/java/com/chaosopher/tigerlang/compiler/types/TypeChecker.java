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
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;

/**
 * A TypeChecker visitor is used to check the <see ref="com.chaosopher.tigerlang.compiler.absyn.Exp. 
 * Againts the defined language specification, also know as the semantic rules.
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d4.html#TC_002d4
 * https://gitlab.lrde.epita.fr/tiger/tc-base/-/blob/2022/src/type/type-checker.cc
 */
public class TypeChecker extends DefaultVisitor {

    final ErrorMsg errorMsg;
    Collection<Absyn> readonlyVarDecs = null;

    public TypeChecker(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        this.readonlyVarDecs = new LinkedList<Absyn>();
    }

    private void checkTypes(Absyn loc, String synCat1, Type first, String synCat2, Type second) {
        if (!second.coerceTo(first)) {
            this.errorMsg.error(loc.pos,
                    String.format("type mismatch\n%s:%s\n%s:%s", synCat1, first.actual(), synCat2, second.actual()));
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
     * Sets the type of break to void.
     */
    @Override
    public void visit(BreakExp exp) {
        exp.setType(Constants.VOID);
    }

    /**
     * Get the type of the field var.
     */
    @Override
    public void visit(FieldVar exp) {
        // get the defining variable
        Absyn def = exp.var.def;

        this.expType = exp.getType();
    }
    /**
     * Visits a call node. The implementation checks that the actual arguments match
     * the formal arguments in the function definition. It sets the current visited
     * type to the return type of the function.
     */
    @Override
    public void visit(CallExp exp) {
        //FUNCTION function = (FUNCTION)exp.getType();
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
            //todo check no nil.
            this.checkTypes(exp.elseclause, "then", thenType, "else", elseType);
            exp.setType(elseType);
        } else {
            exp.setType(Constants.VOID);
        }
    }

    @Override
    public void visit(ArrayExp exp) {
        exp.init.accept(this);
        Type initType = exp.init.getType();
        TypeDec def = (TypeDec)exp.def;
        ARRAY arrayType = (ARRAY)def.ty.getType(); 
        this.checkTypes(exp, "array initialiser", initType, "expected type", arrayType.element);
        exp.size.accept(this);
        Type sizeType = exp.size.getType();
        this.checkTypes(exp, "array size", sizeType, "expected type", Constants.INT);
        exp.setType(arrayType);
    }

    @Override
    public void visit(FieldExpList exp) {
        RECORD record = (RECORD)this.expType.actual();
        while(record != null && exp != null) {
            exp.init.accept(this);
            Type fieldType = exp.init.getType();
            this.checkTypes(exp, "expected field type", record.fieldType, "supplied field type", fieldType);
            record = record.tail;
            exp = exp.tail;
        }
    }

    /**
     * Type check implementation for simple var. This sets the current visited type
     * to its own type.
     */
    @Override
    public void visit(SimpleVar exp) {
        VarDec def = (VarDec)exp.def;
        exp.setType(def.getType());
    }

    /**
     * Ensures that the expType member is set to VOID
     * If the ExpList is not null, this will be modified
     * to the last item in the list. If the ExpList is null
     * the expType member is have been unchanged and still
     * referencing the VOID type. This ensures that expressions such
     * as () are evaluated as VOID.
     */
    @Override
    public void visit(SeqExp exp) {
        exp.setType(Constants.VOID);
        super.visit(exp);
        //this.expTyp is set to the last visited item in exp.
    }

    /**
     * Visit a NameTy node. This in turn visits its definition to retreive its type.
     * Currently there is a problem if the NameTy refers to an int or string. Native types
     * do not have a definition and because of the the NameTy def property is null.
     */
    @Override
    public void visit(NameTy exp) {
        this.expType = exp.getType();
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
        exp.var.init.accept(this);
        Type varType = exp.var.init.getType();
        this.readonlyVarDecs.add(exp.var);
        this.checkTypes(exp, "for var type", varType, "expected type", Constants.INT);
        exp.body.accept(this);
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
            this.errorMsg.error(exp.pos, "variable " + exp.var.def + " is read only");
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

    @Override
    public void visit(VarExp exp) {
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
       
        //figure out the function type.
        
    }
}