package hello;

public class TigerSource {

    private String code;
    private String args;
    private boolean astDisplay;
    private boolean bindingsDisplay;
    private boolean escapesDisplay;
    private boolean rename;
    private boolean inline;
    private boolean prune;
    private boolean hirDisplay;
    private boolean lirDisplay;
    private boolean staticLinkEscapes;
    private boolean staticLinks;
    private boolean escapes;
    private boolean demove;
    private boolean regAlloc;

    public String getCode() {
        return this.code;
    }

    public boolean isPrune() {
        return prune;
    }

    public void setPrune(boolean prune) {
        this.prune = prune;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isRename() {
        return rename;
    }

    public void setRename(boolean rename) {
        this.rename = rename;
    }

    public boolean isEscapesDisplay() {
        return escapesDisplay;
    }

    public void setEscapesDisplay(boolean escapesDisplay) {
        this.escapesDisplay = escapesDisplay;
    }

    public boolean isBindingsDisplay() {
        return bindingsDisplay;
    }

    public void setBindingsDisplay(boolean bindingsDisplay) {
        this.bindingsDisplay = bindingsDisplay;
    }

    public boolean isRegAlloc() {
        return regAlloc;
    }

    public void setRegAlloc(boolean regAlloc) {
        this.regAlloc = regAlloc;
    }

    public boolean isAstDisplay() {
        return astDisplay;
    }

    public void setAstDisplay(boolean astDisplay) {
        this.astDisplay = astDisplay;
    }

    public boolean isDemove() {
        return demove;
    }

    public void setDemove(boolean demove) {
        this.demove = demove;
    }

    public boolean isEscapes() {
        return escapes;
    }

    public void setEscapes(boolean escapes) {
        this.escapes = escapes;
    }

    public boolean isStaticLinks() {
        return staticLinks;
    }

    public void setStaticLinks(boolean staticLinks) {
        this.staticLinks = staticLinks;
    }

    public boolean isStaticLinkEscapes() {
        return staticLinkEscapes;
    }

    public void setStaticLinkEscapes(boolean staticLinkEscapes) {
        this.staticLinkEscapes = staticLinkEscapes;
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