package com.chaosopher.tigerlang.compiler.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.ArrayExp;
import com.chaosopher.tigerlang.compiler.absyn.ArrayTy;
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
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.NilExp;
import com.chaosopher.tigerlang.compiler.absyn.OpExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.RecordTy;
import com.chaosopher.tigerlang.compiler.absyn.SeqExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.StringExp;
import com.chaosopher.tigerlang.compiler.absyn.SubscriptVar;
import com.chaosopher.tigerlang.compiler.absyn.TypeDec;
import com.chaosopher.tigerlang.compiler.absyn.Var;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.VarExp;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;
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
        if (!first.coerceTo(second)) {
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
    public void visit(RecordExp recordExp) {
        // visit the record NameTy
        recordExp.typ.accept(this);
        // exp.typ.type is either RECORD or NIL.
        recordExp.setType(recordExp.typ.getType());
        // if there are no fields, this type will be NIL.
        if(recordExp.fields != null) {
            FieldExpList fieldExpList = recordExp.fields;
            // visit the field list inline as we need its record type. 
            RECORD record = (RECORD)recordExp.typ.getType().actual();
            // loop through all fields and check types match. 
            while(record != null && fieldExpList != null) {
                // visit field expression to compute type.
                fieldExpList.init.accept(this);
                // check types.
                Type fieldType = fieldExpList.init.getType();
                this.checkTypes(fieldExpList, "expected field type", fieldType, "supplied field type", record.fieldType);
                // move to next pair of fields.
                record = record.tail;
                fieldExpList = fieldExpList.tail;
            }
            // if either linked list is not empty, we have either too many or too few parameters.
            if(record != null) {
                this.errorMsg.error(recordExp.pos, "Missing record fields: " + record.fieldName);
            }
            if(fieldExpList != null) {
                this.errorMsg.error(recordExp.pos, "Uknown record field: " + fieldExpList.name);
            }
        }
    }

    /**
     * Visits an array expression and sets its type.
     */
    @Override
    public void visit(ArrayExp exp) {
        exp.typ.accept(this);
        Type def = exp.typ.getType();
        Assert.assertIsTrue(def.actual() instanceof ARRAY);
        ARRAY arrayType = (ARRAY)def.actual(); 
        exp.init.accept(this);
        Type initType = exp.init.getType();
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
        exp.var.accept(this);
        RECORD record = (RECORD)exp.var.getType().actual();
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
    public void visit(SimpleVar simpleVar) {
        VarDec def = (VarDec)simpleVar.def;
     //   def.typ.getType();
     //   exp.setType(def.init.getType());
        simpleVar.setType(def.getType());
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

    @Override
    public void visit(LetExp letExp) {
        if(letExp.decs != null) {
            letExp.decs.accept(this);
        }
        if(letExp.body != null) {
            letExp.body.accept(this);
            letExp.setType(letExp.body.getType());
        } else {
            letExp.setType(Constants.VOID);
        }
    }

    @Override
    public void visit(SubscriptVar subscriptVar) {
        subscriptVar.index.accept(this);
        this.checkTypes(subscriptVar, "subscript index type", subscriptVar.index.getType(), "expected type", Constants.INT);
        VarDec def = (VarDec)subscriptVar.var.def;
        Assert.assertIsTrue(def.getType().actual() instanceof ARRAY);
        ARRAY arrayType = (ARRAY)def.getType().actual();
        subscriptVar.var.accept(this);
        subscriptVar.setType(arrayType.element);
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


    /**
     * Represents the declaration of a variable which may include the type for
     * example: var myVar:int = 1, or as a function argument.
     */
    @Override
    public void visit(VarDec varDec) {
        // visit initializer, if present.
        Type initType = null;
        if(varDec.init != null) {
            varDec.init.accept(this);
            initType = varDec.init.getType();
            varDec.setType(initType);
        }
        // expect that the initizer type is set here ( either int, string, namety, recordty, arrayty)
        if (varDec.typ != null) {
            varDec.typ.accept(this);
            Type declaredType = varDec.typ.getType();
            if(initType != null) {
                this.checkTypes(varDec, "declared", declaredType, "actual", initType);
            }
            varDec.setType(declaredType);
        }
        //exp.setType(Constants.VOID);
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
        HashMap<Symbol, NAME> types = new HashMap<>();
        // create placeholders first for the left side
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            NAME nameType = new NAME(typeDec.name);
            types.put(typeDec.name, nameType);
            // set the type.
            typeDec.setType(nameType);
        }
        //visit the right site to back patch the item created above.
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            NAME namedType = types.get(typeDec.name);
            // visit the type definition.
            typeDec.ty.accept(this);
            // fill in the type
            namedType.bind(typeDec.ty.getType());
            //set the type
            //typeDec.setType(typeDec.ty.getType());
        }
    }

    /**
     * Visit an explicit type, eg a:int := 1, where int
     * is a NameTy. This can be used in a type declation 
     * or a variable declaration.
     */
    @Override
    public void visit(NameTy nameTy) {
        // find the type declaration for this NameTy.
        TypeDec typeDec = (TypeDec)nameTy.def;
        Assert.assertNotNull(typeDec, "Missing type definition for NameTy (" + nameTy.name + "), check Binder.");
        // set the type of this NameTy to the same types as the definition.
        nameTy.setType(typeDec.getType());
    }

    /**
     * Vist an array type. 
     * This can only ever be used in a TypeDec.
     * It will never be present as a use.
      */
    @Override
    public void visit(ArrayTy exp) {
        exp.typ.accept(this);
        ARRAY arrayType = new ARRAY(exp.typ.getType());
        exp.setType(arrayType);
    }

    /**
     * Visit a record type.
     * This can only ever be used in a TypeDec.
     * It will never be present anywhere else.
     */
    @Override
    public void visit(RecordTy recordTy) {
        RECORD recordType = null, last = null; 
        for (FieldList fieldList = recordTy.fields; fieldList != null; fieldList = fieldList.tail) {
            // visit the NameTy to set its type.
            fieldList.typ.accept(this);
            // build the record type.
            if(recordType == null) {
                recordType = last = new RECORD(fieldList.name, fieldList.typ.getType(), null);
            } else {
                last = last.tail = new RECORD(fieldList.name, fieldList.typ.getType(), null);
            }
        }
        recordTy.setType(recordType != null ? recordType : Constants.NIL);
    }

    /**
     * Visit a function declaration, compute the constructor type of the
     * of the function.
     */
    @Override
    public void visit(FunctionDec exp) {
        // first pass
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            RECORD formals = null, last = null;
            // visit each variable declarion
            for(DecList args = functionDec.params; args != null; args = args.tail) {
                // visit declarations
                VarDec varDec = (VarDec)args.head;
                varDec.accept(this);
                // varDec type is not set.
                Type decType = varDec.getType();
                // set the type of this function.
                if(formals == null) {
                    formals = last = new RECORD(varDec.name, decType, null);
                } else {
                    last = last.tail = new RECORD(varDec.name, decType, null);
                }
            }
            if(functionDec.result != null) {
                functionDec.result.accept(this);
                Type returnType = functionDec.result.getType();
                functionDec.setType(new FUNCTION(formals, returnType));
            } else {

                functionDec.setType(new FUNCTION(formals, Constants.VOID));
            }
        }
        // check the return type of the body matches the definition.
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(functionDec.body != null) {
                functionDec.body.accept(this);
                FUNCTION functionType = (FUNCTION)functionDec.getType();
                checkTypes(functionDec, "f1", functionDec.body.getType(), "f2", functionType.result);
            }
        }
    }
}