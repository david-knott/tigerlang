package hello;

public class CompilerResponse {

    private String assembly;
    private String errors;

    public CompilerResponse(String assembly, String errors) {
        this.assembly = assembly;
        this.setErrors(errors);
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