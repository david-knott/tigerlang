package com.chaosopher.tigerlang.compiler.errormsg;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorMsg {
    private LineList linePos = new LineList(-1, null);
    private int lineNum = 1;
    private String filename;
    public boolean anyErrors;
    public PrintStream out;
    private List<ErrorMessage> errors;

    public ErrorMsg(String f, PrintStream out) {
        filename = f;
        this.errors = new ArrayList<ErrorMessage>();
        this.out = out;
    }

    public void newline(int pos) {
        lineNum++;
        linePos = new LineList(pos, linePos);
    }

    /**
     * If there are errors, this returns a read-only list of errors.
     * If there are no errors, this returns an empty list. 
     * @return @see List
     */
    public List<ErrorMessage> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    public void error(int pos, String message ) {
        int n = lineNum;
        LineList p = linePos;
        anyErrors = true;
        while (p != null) {
            if (p.head < pos) {
                break;
            }
            p = p.tail;
            n--;
        }
        this.errors.add(new ErrorMessage(this.filename, n, pos - p.head, message));
    }

    public void errorOld(int pos, String msg) {
        int n = lineNum;
        LineList p = linePos;
        String sayPos = "0.0";
        anyErrors = true;
        while (p != null) {
            if (p.head < pos) {
                sayPos = ":" + String.valueOf(n) + "." + String.valueOf(pos - p.head);
                break;
            }
            p = p.tail;
            n--;
        }
        this.out.println(filename + ":" + sayPos + ": " + msg);
    }
}

class LineList {
    int head;
    LineList tail;

    LineList(int h, LineList t) {
        head = h;
        tail = t;
    }
}
