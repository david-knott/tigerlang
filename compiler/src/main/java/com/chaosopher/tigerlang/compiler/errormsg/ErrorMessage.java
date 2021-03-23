package com.chaosopher.tigerlang.compiler.errormsg;

public class ErrorMessage {
    private final String fileName;
    private final int col;
    private final int line;
    private final String message;

    public ErrorMessage(String fileName, int line, int col, String message) {
        this.fileName = fileName;
        this.line = line;
        this.col = col;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public String getFileName() {
        return fileName;
    }
}