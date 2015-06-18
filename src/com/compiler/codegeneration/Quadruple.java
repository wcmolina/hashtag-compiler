package com.compiler.codegeneration;

/**
 * Created by Wilmer Carranza on 14/06/2015.
 */
public class Quadruple {

    private String op;
    private Object arg1, arg2, result;

    public Quadruple() {
        op = "";
        arg1 = arg2 = result = null;
    }

    public Quadruple(String op, Object arg1, Object arg2, Object result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public Quadruple(String op, Object arg1, Object result) {
        this.op = op;
        this.arg1 = arg1;
        this.result = result;
    }

    public Quadruple(String op, Object result) {
        this.op = op;
        this.result = result;
    }

    public Quadruple(String label) {
        this.op = label;
    }

    public String getOperator() {
        return op;
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Quadruple{" +
                "op='" + op + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public String visualize() {
        StringBuilder builder = new StringBuilder();
        if (!op.equalsIgnoreCase("")) {
            builder.append("'").append(op).append("' ");
        }
        if (arg1 != null) {
            builder.append("'").append(arg1).append("' ");
        }
        if (arg2 != null) {
            builder.append("'").append(arg2).append("' ");
        }
        if (result != null) {
            builder.append(result).append(" ");
        }
        return builder.toString();
    }
}
