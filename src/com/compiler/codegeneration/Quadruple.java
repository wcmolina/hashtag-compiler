package com.compiler.codegeneration;

import com.compiler.hashtag.SemanticAnalyzer;

/**
 * Created by Wilmer Carranza on 14/06/2015.
 */
public class Quadruple {

    private String op = "", arg1 = "", arg2 = "", result = "";

    public Quadruple(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        isTemporal(arg1);
        isTemporal(arg2);
    }

    public Quadruple(String op, String arg1, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.result = result;
        isTemporal(arg1);
    }

    public Quadruple(String op, String result) {
        this.op = op;
        this.result = result;
    }

    public Quadruple(String label) {
        this.op = label;
    }

    public String getOperator() {
        return op;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResult() {
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

    //all this just for pretty printing.......
    public String visualize() {
        StringBuilder builder = new StringBuilder();
        if (SemanticAnalyzer.ARITHMETIC_OPERATORS.contains(op)) {
            builder.append(result + " = ");
            builder.append(arg1 + " ");
            builder.append(op + " ");
            builder.append(arg2);
            return builder.toString();
        } else if (op == "=") {
            builder.append(result + " ");
            builder.append(op + " ");
            builder.append(arg1 + " ");
            builder.append(arg2);
            return builder.toString();
        } else {
            builder.append(op + " ");
            builder.append(arg1 + " ");
            builder.append(arg2 + " ");
            builder.append(result);
            return builder.toString();
        }
    }

    private void isTemporal(String arg) {
        if (arg.startsWith("$")) {
            IntermediateCode.TEMPORALS.get(Integer.parseInt(arg.split("t")[1])).setFree(true);
        }
    }
}
