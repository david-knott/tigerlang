package com.chaosopher.tigerlang.web.backend.test;

public class CompilerResponse {

    private String assembly;
    private String errors;
    private String command;

    public CompilerResponse(String assembly, String errors, String command) {
        this.assembly = assembly;
        this.setErrors(errors);
        this.setCommand(command);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

}