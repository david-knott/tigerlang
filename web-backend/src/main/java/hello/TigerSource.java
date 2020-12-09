package hello;

public class TigerSource {

    private String code;
    private String args;
    private boolean hirDisplay;
    private boolean lirDisplay;

    public String getCode() {
        return this.code;
    }

    public boolean isLirDisplay() {
        return lirDisplay;
    }

    public void setLirDisplay(boolean lirDisplay) {
        this.lirDisplay = lirDisplay;
    }

    public boolean isHirDisplay() {
        return hirDisplay;
    }

    public void setHirDisplay(boolean hirDisplay) {
        this.hirDisplay = hirDisplay;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setCode(String code) {
        this.code = code;
    }
}