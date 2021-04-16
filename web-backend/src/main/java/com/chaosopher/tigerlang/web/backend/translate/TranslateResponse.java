package com.chaosopher.tigerlang.web.backend.translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.temp.Label;
import com.chaosopher.tigerlang.compiler.translate.DataFrag;
import com.chaosopher.tigerlang.compiler.translate.FragmentVisitor;
import com.chaosopher.tigerlang.compiler.translate.ProcFrag;
import com.chaosopher.tigerlang.compiler.tree.BINOP;
import com.chaosopher.tigerlang.compiler.tree.CALL;
import com.chaosopher.tigerlang.compiler.tree.CJUMP;
import com.chaosopher.tigerlang.compiler.tree.ESEQ;
import com.chaosopher.tigerlang.compiler.tree.CONST;
import com.chaosopher.tigerlang.compiler.tree.EXP;
import com.chaosopher.tigerlang.compiler.tree.JUMP;
import com.chaosopher.tigerlang.compiler.tree.LABEL;
import com.chaosopher.tigerlang.compiler.tree.MEM;
import com.chaosopher.tigerlang.compiler.tree.MOVE;
import com.chaosopher.tigerlang.compiler.tree.NAME;
import com.chaosopher.tigerlang.compiler.tree.SEQ;
import com.chaosopher.tigerlang.compiler.tree.TEMP;
import com.chaosopher.tigerlang.compiler.tree.DefaultTreeVisitor;
import com.chaosopher.tigerlang.compiler.tree.ExpList;
import com.chaosopher.tigerlang.compiler.tree.IR;

public class TranslateResponse implements FragmentVisitor {

    public class IRLine {

        private final int rank;
        private final String code;
        private final int position;

        public IRLine(final int rank, final int position, final String code) {
            this.rank = rank;
            this.code = code;
            this.position = position;
        }

        public int getRank() {
            return this.rank;
        }

        public int getPosition() {
            return position;
        }

        public String getCode() {
            return code;
        }
    }

    public class DataLine {
        
        private final String code;
        private final Label label;

        public DataLine(Label label, String code) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return this.code;
        }

        public String getLabel() {
            return this.label.toString();
        }
    }

    class TreeVisistorImpl extends DefaultTreeVisitor {

        private int pos = -1;

        private void write(IR ir, String s) {
            if(sourceMap.containsKey(ir)) {
                this.pos = sourceMap.get(ir).pos;
            }
            lines.add(new IRLine(lines.size(), this.pos, s));
        }

        @Override
        public void visit(BINOP op) {
            this.write(op, "binop(");
            switch (op.binop) {
                case BINOP.PLUS:
                    this.write(op, "PLUS");
                    break;
                case BINOP.MINUS:
                    this.write(op, "MINUS");
                    break;
                case BINOP.MUL:
                    this.write(op, "MUL");
                    break;
                case BINOP.DIV:
                    this.write(op, "DIV");
                    break;
                default:
                    this.write(op, Integer.toString(op.binop));
                    break;
            }
            op.left.accept(this);
            op.right.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(CALL op) {
            this.write(op, "call(");
            op.func.accept(this);
            for (ExpList el = op.args; el != null; el = el.tail) {
                el.head.accept(this);
            }
            this.write(op, ")");
        }

        @Override
        public void visit(CONST op) {
            this.write(op, "const(" + op.value + ")");
        }

        @Override
        public void visit(ESEQ op) {
            this.write(op, "eseq(");
            op.stm.accept(this);
            op.exp.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(EXP op) {
            this.write(op, "sxp(");
            op.exp.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(JUMP op) {
            this.write(op, "jump(");
            op.exp.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(LABEL op) {
            this.write(op, "label(" + op.label + ")");
        }

        @Override
        public void visit(MEM op) {
            this.write(op, "mem(");
            op.exp.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(MOVE op) {
            this.write(op, "move(");
            op.dst.accept(this);
            op.src.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(NAME op) {
            this.write(op, "name(" + op.label + ")");
        }

        @Override
        public void visit(SEQ op) {
            this.write(op, "seq(");
            op.left.accept(this);
            op.right.accept(this);
            this.write(op, ")");
        }

        @Override
        public void visit(TEMP op) {
            this.write(op, "temp(" + op.temp.toString() + ")");
        }

        @Override
        public void visit(CJUMP cjump) {
            this.write(cjump, "cjump(");
            String op = null;
            switch(cjump.relop) {
                case CJUMP.EQ:
                    op = "EQ";
                    break;
                case CJUMP.GE:
                    op = "GE";
                    break;
                case CJUMP.GT:
                    op = "GT";
                    break;
                case CJUMP.LE:
                    op = "LE";
                    break;
                case CJUMP.LT:
                    op = "LT";
                    break;
                case CJUMP.NE:
                    op = "NE";
                    break;
                case CJUMP.UGE:
                    op = "UGE";
                    break;
                case CJUMP.UGT:
                    op = "UGT";
                    break;
                case CJUMP.ULE:
                    op = "ULE";
                    break;
                case CJUMP.ULT:
                    op = "ULT";
                    break;
            }
            this.write(cjump, op);
            cjump.left.accept(this);
            cjump.right.accept(this);
            this.write(cjump, cjump.iftrue.toString());
            this.write(cjump, cjump.iffalse.toString());
            this.write(cjump, ")");
        }
    }

    private List<IRLine> lines = new ArrayList<>();
    private List<DataLine> data = new ArrayList<>();
    private volatile Map<IR, Absyn> sourceMap;

    public TranslateResponse(Map<IR, Absyn> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public List<IRLine> getIRLine() {
        return Collections.unmodifiableList(this.lines);
    }

    public List<DataLine> getDataLine() {
        return Collections.unmodifiableList(this.data);
    }

    @Override
    public void visit(ProcFrag procFrag) {
        TreeVisistorImpl treeVisistorImpl = new TreeVisistorImpl();
        procFrag.body.accept(treeVisistorImpl);
    }

    @Override
    public void visit(DataFrag dataFrag) {
       this.data.add(new DataLine(dataFrag.getLabel(), dataFrag.getData())); 
    }
}