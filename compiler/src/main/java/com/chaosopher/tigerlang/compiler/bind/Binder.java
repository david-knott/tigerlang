package com.chaosopher.tigerlang.compiler.bind;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.ArrayExp;
import com.chaosopher.tigerlang.compiler.absyn.ArrayTy;
import com.chaosopher.tigerlang.compiler.absyn.BreakExp;
import com.chaosopher.tigerlang.compiler.absyn.CallExp;
import com.chaosopher.tigerlang.compiler.absyn.DecList;
import com.chaosopher.tigerlang.compiler.absyn.DefaultVisitor;
import com.chaosopher.tigerlang.compiler.absyn.FieldList;
import com.chaosopher.tigerlang.compiler.absyn.ForExp;
import com.chaosopher.tigerlang.compiler.absyn.FunctionDec;
import com.chaosopher.tigerlang.compiler.absyn.LetExp;
import com.chaosopher.tigerlang.compiler.absyn.NameTy;
import com.chaosopher.tigerlang.compiler.absyn.RecordExp;
import com.chaosopher.tigerlang.compiler.absyn.SimpleVar;
import com.chaosopher.tigerlang.compiler.absyn.TypeDec;
import com.chaosopher.tigerlang.compiler.absyn.VarDec;
import com.chaosopher.tigerlang.compiler.absyn.WhileExp;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.symbol.Symbol;

/**
 * This class implements the default visitor and traverses the AST
 * looking for definitions of variables, types and functions and then
 * binds these to their usages. It also checks for breaks correctly nested
 * within while expressions and for expressions. All type checking is
 * defered to the @see com.chaosopher.tigerlang.compiler.types.TypeChecker .
 */
public class Binder extends DefaultVisitor {

    private final SymbolTable typeSymbolTable;
    private final SymbolTable varSymbolTable;
    private final SymbolTable functionSymbolTable;
    private final Deque<Absyn> loops = new ArrayDeque<>();
    private final ErrorMsg errorMsg;

    public Binder(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        // base system types
        HashMap<Symbol, SymbolTableElement> tinit = new HashMap<>();
        tinit.put(Symbol.symbol("int"), new SymbolTableElement(null));
        tinit.put(Symbol.symbol("string"), new SymbolTableElement(null));
        this.typeSymbolTable = new SymbolTable(tinit);
        // base functions
        this.functionSymbolTable = new SymbolTable();
        // var table
        this.varSymbolTable = new SymbolTable();
    }

    /**
     * Visit the letexp expression. This creates new function, variable and type
     * scopes.
     */
    @Override
    public void visit(LetExp exp) {
        this.typeSymbolTable.beginScope();
        this.varSymbolTable.beginScope();
        this.functionSymbolTable.beginScope();
        super.visit(exp);
        this.functionSymbolTable.endScope();
        this.varSymbolTable.endScope();
        this.typeSymbolTable.endScope();
    }

    /**
     * Visit a simple var expression and bind it to its declaration. Sets the simple
     * var type to its definition type.
     */
    @Override
    public void visit(SimpleVar exp) {
        if (this.varSymbolTable.contains(exp.name)) {
            // lookup definition in var symbol table.
            SymbolTableElement def = this.varSymbolTable.lookup(exp.name);
            // set this simple variables defintition to def.
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undeclared variable:" + exp.name);
        }
    }

    /**
     * Visit a call expression and bind it to the function declaration.
     */
    @Override
    public void visit(CallExp exp) {
        if (this.functionSymbolTable.contains(exp.func)) {
            SymbolTableElement def = this.functionSymbolTable.lookup(exp.func);
            exp.setDef(def.exp);
            if(exp.args != null) {
                exp.args.accept(this);
            }
        } else {
            this.errorMsg.error(exp.pos, "undeclared function:" + exp.func);
        }
    }

    /**
     * Visit a variable declaration and add it to the symbol table.
     * This is only used for variable declarations inside a let expression.
     * VarDec used as formal argument is handled directly inside the FunctionDec
     * visit method. The var dec's type is set to the same as its initializer.
     */
    @Override
    public void visit(VarDec exp) {
        if (exp.typ != null) {
            exp.typ.accept(this);
        }
        exp.init.accept(this);
        // set the type of the var dec to its initializer.
        if (!this.varSymbolTable.contains(exp.name, false)) {
            this.varSymbolTable.put(exp.name, new SymbolTableElement(exp));
        } else {
            this.errorMsg.error(exp.pos, "redefinition:" + exp.name);
        }
    }

    /**
     * Visit an array expression. This is where an array is used in a rvalue expression.
     */
    @Override
    public void visit(ArrayExp exp) {
        super.visit(exp);
        if (this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
    }

    /**
     * Visit a record expression. This is where a record is used in a rvalue expression.
     */
    @Override
    public void visit(RecordExp exp) {
        super.visit(exp);
        if (this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
    }

    /**
     * Visit break expression and assign it to member lastBreak.
     */
    @Override
    public void visit(BreakExp exp) {
        if (this.loops.isEmpty()) {
            this.errorMsg.error(exp.pos, "`break' outside any loop:" + exp.pos);
        } else {
            exp.loop = this.loops.peek();
        }
    }

    /**
     * Visit while loop and capture and breaks within its body.
     */
    @Override
    public void visit(WhileExp exp) {
        this.loops.push(exp);
        super.visit(exp);
        this.loops.pop();
    }

    /**
     * Visit for loop and capture and breaks within its body.
     */
    @Override
    public void visit(ForExp exp) {
        this.loops.push(exp);
        super.visit(exp);
        this.loops.pop();
    }

    /**
     * Visit a function declaration. Visit the function header first, this includes
     * the function name its formal arguments and return type. These are added to
     * the function symbol table. A second pass then examines each contigious
     * function body and adds the formals to variable environment.
     */
    @Override
    public void visit(FunctionDec exp) {
        // first pass for function headers.
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if (functionDec.result != null) {
                // lookup the type of the return value
                if (this.typeSymbolTable.contains(functionDec.result.name)) {
                    SymbolTableElement def = this.typeSymbolTable.lookup(functionDec.result.name);
                    functionDec.result.setDef(def.exp);
                } else {
                    this.errorMsg.error(functionDec.result.pos, "undefined type:" + functionDec.result.name);
                }
            }
            // function definition
            if (!this.functionSymbolTable.contains(functionDec.name, false)) {
                this.functionSymbolTable.put(functionDec.name, new SymbolTableElement(functionDec));
            } else {
                this.errorMsg.error(exp.pos, "redefinition:" + functionDec.name);
            }
        }
        // second pass for function body.
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            this.varSymbolTable.beginScope();
            for (DecList decList = functionDec.params; decList != null; decList = decList.tail) {
                // params are a list of variable declarations.
                VarDec param = (VarDec)decList.head; 
                // formal variable definition
                if (!this.varSymbolTable.contains(param.name, false)) {
                    this.varSymbolTable.put(param.name, new SymbolTableElement(param));
                } else {
                    this.errorMsg.error(param.pos, "redefinition:" + param.name);
                }
            }
            if(functionDec.body != null) {
                functionDec.body.accept(this);
            }
            this.varSymbolTable.endScope();
        }
    }

    /**
     * Visits a user defined type declaration and binds the symbol to its type
     * representation. This allows further lookups using the exp.name which will
     * resolve the the user defined type.
     * 
     * type a = int // TypeDec(NameTy(SymbolTy)) tpye intArray = array of int //
     * TypeDec(ArrayTy(SymbolTy)) type rec = {first: typ1, secont: ty2} //
     * TypeDec(RecordTy(FieldList)))
     */
    @Override
    public void visit(TypeDec exp) {
        // for a block of type declarations, visit the lvalue
        for (TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            // install new type definitio. in symbol table.
            if (!this.typeSymbolTable.contains(typeDec.name)) {
                this.typeSymbolTable.put(typeDec.name, new SymbolTableElement(typeDec));
            } else {
                this.errorMsg.error(typeDec.pos, "redefinition:" + typeDec.name);
            }
        }
        // visit again and process the rvalue, which could be namety, arrayty or recordty.
        for (TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            if (this.typeSymbolTable.contains(typeDec.name)) {
                typeDec.ty.accept(this);
            }
        }
    }

    /**
     * Visit a explicit type in a var declaration, eg var a:int = 1, where int is
     * the NameTy or visit the return type defined in a function declaration eg
     * function a():int, where int is the NameTy, or type t = int, where int is
     * NameTy. It can also be used in a FieldList {a: int, b: b}
     */
    @Override
    public void visit(NameTy exp) {
        // lookup the rvalue type in the symbol table.
        if (this.typeSymbolTable.contains(exp.name)) {
            // value present so set the exp's definition.
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.name);
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.name);
        }
    }

    @Override
    public void visit(FieldList exp) {
        // check that types are defined.
        if (!this.typeSymbolTable.contains(exp.typ.name)) {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ.name);
        }
        if(exp.tail != null) {
            this.visit(exp.tail);
        }
    }

    /**
     * Visits an array type within a type declaration. Contructs an ARRAY type and
     * stores it in the symbol table.
     */
    @Override
    public void visit(ArrayTy exp) {
        if (!this.typeSymbolTable.contains(exp.typ)) {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
    }
}