package com.chaosopher.tigerlang.compiler.tree;

import java.io.IOException;

import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.temp.LabelList;
import com.chaosopher.tigerlang.compiler.temp.Temp;

/**
 * The Parser class converts a textual representation of LIR
 * and HIR into the corresponding model. This is used for both
 * the interpreter and for testing data flow optimisations.
 */
class Parser {

    // lexical scanner instance
    private final Lexer lexer;
    // lookahead token.
    private Token look;

    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * This method retrieves the next token from the lexer.
     * @throws IOException
     */
    void move() throws IOException {
        this.look = this.lexer.scan();
    }

    /**
     * This method checks that the next token matches one of the supplied arguments.
     * If it does, the next token is retrieved from the lexer. If it does
     * not an error is thrown.
     * @param t
     * @throws IOException
     */
    void match(TokenType t) throws IOException {
            if(look.getTokenType() == t) {
                move();
                return;
            }
        error(String.format("Syntax error: expected %s, found %s", t, look.getTokenType()));
    }

    void error(String string) {
        System.out.println(string);
    }

    /**
     * This method parses the input stream using the lexer supplied in the
     * constructor to break the stream into tokens. This method delegates the
     * terminal productions to private methods in this class.
     * 
     * @return
     * @throws IOException
     */
    public IR parse() throws IOException {
        move();
        StmList stmList = null, start = null;
        Stm stm;
        do {
            stm = statement();
            if(start == null) {
                start = stmList = new StmList(stm, stmList);
            } else {
                stmList.tail =  stmList = new StmList(stm, null);
            }
        }
        while(this.look.getTokenType() != TokenType.EOF);
		return start;
	}

    private Stm handleMove() throws IOException {
        this.match(TokenType.MOVE);
        this.match(TokenType.LEFT_PAREN);
        Exp expDst = this.expression();
        this.match(TokenType.COMMA);
        Exp expSrc = this.expression();
        this.match(TokenType.RIGHT_PAREN);
        return new MOVE(expDst, expSrc);
    }

    private Stm handlJump() throws IOException {
        this.match(TokenType.JUMP);
        this.match(TokenType.LEFT_PAREN);
        this.match(TokenType.NAME);
        this.match(TokenType.LEFT_PAREN);
        Token id = this.look;
        this.match(TokenType.ID);
        this.match(TokenType.RIGHT_PAREN);
        this.match(TokenType.RIGHT_PAREN);
        Label label = Label.create(id.getLexeme());
        return new JUMP(new NAME(label), new LabelList(label, null));
    }

    private Stm handleCjump() throws IOException {
        Exp cjumpLeft = null, cjumpRight = null;
        String cjumpIdLeft = null, cjumpIdRight = null;
        this.match(TokenType.CJUMP);
        this.match(TokenType.LEFT_PAREN);
        cjumpLeft = this.expression();
        this.match(TokenType.COMMA);
        cjumpRight = this.expression();
        this.match(TokenType.COMMA);
        Token tok2 = this.look;
        this.match(TokenType.ID);
        cjumpIdLeft = tok2.getLexeme();
        this.match(TokenType.COMMA);
        tok2 = this.look;
        this.match(TokenType.ID);
        cjumpIdRight = tok2.getLexeme();
        this.match(TokenType.RIGHT_PAREN);
        int op;
        switch(tok2.getLexeme()) {
            case "LT":
                op = CJUMP.LT;
                break;
            case "LE":
                op = CJUMP.LE;
                break;
            case "GT":
                op = CJUMP.GT;
                break;
            case "GE":
                op = CJUMP.GE;
                break;
            case "NE":
                op = CJUMP.NE;
                break;
            case "EQ":
                op = CJUMP.EQ;
                break;
            case "ULT":
                op = CJUMP.ULT;
                break;
            case "ULE":
                op = CJUMP.ULE;
                break;
            case "UGT":
                op = CJUMP.UGT;
                break;
            case "UGE":
                op = CJUMP.UGE;
                break;
            default:
                throw new Error("Unknown CJUMP op:" + tok2.getLexeme());
        }
        return new CJUMP(op, cjumpLeft, cjumpRight, Label.create(cjumpIdLeft), Label.create(cjumpIdRight));
    }

    private Stm handleLabel() throws IOException {
        this.match(TokenType.LABEL);
        this.match(TokenType.LEFT_PAREN);
        Token tok = this.look;
        this.match(TokenType.ID);
        this.match(TokenType.RIGHT_PAREN);
        return new LABEL(Label.create(tok.getLexeme()));
    }

    private Stm handleSeq() throws IOException {
        this.match(TokenType.SEQ);
        this.match(TokenType.LEFT_PAREN);
        Stm leftStm = this.statement();
        this.match(TokenType.COMMA);
        Stm rightStm = this.statement();
        this.match(TokenType.RIGHT_PAREN);
        return new SEQ(leftStm, rightStm);
    }

    private Stm handleSxp() throws IOException {
        this.match(TokenType.SXP);
        this.match(TokenType.LEFT_PAREN);
        Exp sxpExp = this.expression();
        this.match(TokenType.RIGHT_PAREN);
        return new EXP(sxpExp);
    }

    private Stm statement() throws IOException {
        switch (this.look.getTokenType()) {
            case MOVE: /* move(exp, exp) */
                return this.handleMove();
            case JUMP: /* jump(exp) */
                return this.handlJump();
            case CJUMP: /* cjump(id, exp, exp, id, id) */
                return this.handleCjump();
            case LABEL: /* label(id) */
                return this.handleLabel();
            case SEQ: /* seq(exp, exp) */
                return this.handleSeq();
            case SXP: /* sxp(exp) - called Tree.EXP in model */
                return this.handleSxp();
            default:
                break;
        }
        throw new Error("Unhandled statement:" + this.look.getTokenType());
    }

    private ExpList expressionList() throws IOException {
        ExpList result = null, start = null;
        do
        {
            this.match(TokenType.COMMA);
            Exp arg = this.expression();
            if(start == null) {
                start = result = new ExpList(arg, result);
            } else {
                result.tail = result = new ExpList(arg, null);

            }
        }
        while(this.look.getTokenType() == TokenType.COMMA);
        return start;
    }

    private Exp handleMem() throws IOException {
        Exp exp;
        this.match(TokenType.MEM);
        this.match(TokenType.LEFT_PAREN);
        exp = this.expression();
        this.match(TokenType.RIGHT_PAREN);
        return new MEM(exp);
    }

    private Exp handleBinop() throws IOException {
        Token tok;
        this.match(TokenType.BINOP);
        this.match(TokenType.LEFT_PAREN);
        tok = this.look;
        this.match(TokenType.COMMA);
        Exp leftExp = this.expression();
        this.match(TokenType.COMMA);
        Exp rightExp = this.expression();
        this.match(TokenType.RIGHT_PAREN);
        int op = 0;
        if (tok.getLexeme().equals("PLUS")) {
            op = BINOP.PLUS;
        } else if (tok.getLexeme().equals("MINUS")) {
            op = BINOP.MINUS;
        } else if (tok.getLexeme().equals("MUL")) {
            op = BINOP.MUL;
        } else if (tok.getLexeme().equals("DIV")) {
            op = BINOP.DIV;
        } else {
            throw new Error("Binop operator not implemented." + tok.getLexeme());
        }
        return new BINOP(op, leftExp, rightExp);
    }

    private Exp handleConst() throws IOException {
        Token tok;
        this.match(TokenType.CONST);
        this.match(TokenType.LEFT_PAREN);
        tok = this.look;
        this.match(TokenType.DIGIT);
        this.match(TokenType.RIGHT_PAREN);
        return new CONST(Integer.parseInt(tok.getLexeme()));
    }

    private Exp handleTemp() throws IOException {
        Token tok;
        this.match(TokenType.TEMP);
        this.match(TokenType.LEFT_PAREN);
        tok = this.look;
        this.match(TokenType.ID);
        this.match(TokenType.RIGHT_PAREN);
        return new TEMP(Temp.create(tok.getLexeme()));
    }

    private Exp handleEseq() throws IOException {
        this.match(TokenType.ESEQ);
        this.match(TokenType.LEFT_PAREN);
        Stm leftStm = this.statement();
        this.match(TokenType.COMMA);
        Exp eseqRightExp = this.expression();
        this.match(TokenType.RIGHT_PAREN);
        return new ESEQ(leftStm, eseqRightExp);
    }

    private Exp handleCall() throws IOException {
        Exp callExp = null;
        ExpList callExpList = null;
        this.match(TokenType.CALL);
        this.match(TokenType.LEFT_PAREN);
        callExp = this.expression();
        // either expression list or expression.
        if (look.getTokenType() == TokenType.COMMA) {
            callExpList = this.expressionList();
        }
        // move to right paren.
        this.match(TokenType.RIGHT_PAREN);
        return new CALL(callExp, callExpList);
    }

    private Exp handleName() throws IOException {
        this.match(TokenType.NAME);
        this.match(TokenType.LEFT_PAREN);
        Token nameToken = this.look;
        this.match(TokenType.ID);
        this.match(TokenType.RIGHT_PAREN);
        return new NAME(Label.create(nameToken.getLexeme()));
    }

    private Exp expression() throws IOException {
        switch(this.look.getTokenType()) {
            case MEM: /* mem(exp) */
                return this.handleMem();
            case BINOP: /* binop(id, exp, exp) */
                return this.handleBinop(); 
            case CONST: /* const(digit) */
                return this.handleConst();
            case TEMP: /* temp(id) */
                return this.handleTemp();
            case ESEQ: /* eseq(statement, exp) */
                return this.handleEseq();
            case CALL: /* call(exp, expList) */
                return this.handleCall(); 
            case NAME:
                return this.handleName();
            default:
                break;
        }
        throw new Error("Unhandled expression:" + this.look.getTokenType());
    }
}