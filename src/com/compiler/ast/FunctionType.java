package com.compiler.ast;

/**
 * Created by Wilmer Carranza on 26/05/2015.
 */
public class FunctionType {
    private String domain;
    private String range;
    private Node identifier;
    private String signature;

    public FunctionType(Node id, String dom, String ran) {
        identifier = id;
        domain = dom;
        range = ran;
        signature = dom.concat("->").concat(ran);
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Node getIdentifier() {
        return identifier;
    }

    public String getSignature() {
        return identifier.label.concat(":").concat(signature);
    }

    public String toString() {
        return getSignature();
    }
}
