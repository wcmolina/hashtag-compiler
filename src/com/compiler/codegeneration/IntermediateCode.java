package com.compiler.codegeneration;

import com.compiler.ast.*;
import com.compiler.hashtag.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Wilmer Carranza on 15/06/2015.
 */
public class IntermediateCode {

    private static int temporalCount;
    private static int labelCount;
    private static ArrayList<Quadruple> quadruplesList;

    public IntermediateCode() {
        quadruplesList = new ArrayList<Quadruple>();
        temporalCount = 0;
        labelCount = 0;
    }

    public static void generateIntermediateCode(Node node) {
        ArrayList<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node current = children.get(i);
            if (current.label.equalsIgnoreCase("if")) {
                generateIf(current);
            } else {
                if (current.label.equalsIgnoreCase("assign")) {
                    generateAssignment(current);
                } else
                    generateIntermediateCode(children.get(i));
            }
        }
    }

    public static void generateIf(Node node) {
        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node body = node.getChildren().get(1);
        String trueLabel = generateLabel();
        String falseLabel = generateLabel();
        if (conditions.label.equalsIgnoreCase("or")) {
            generateOr(conditions, trueLabel, falseLabel);
        }
        Quadruple quadruple = new Quadruple(trueLabel.concat(":"));
        quadruplesList.add(quadruple);
        generateIntermediateCode(body);
        quadruple = new Quadruple(falseLabel.concat(":"));
        quadruplesList.add(quadruple);
    }

    public static void generateIfElse(Node node) {

    }

    public static void generateAssignment(Node assign) {

    }

    public static void generateInitialization(Node init) {

    }

    public static Object generateArithmetic(Node expr) {
        if (expr.isLeaf()) {
            return expr;
        } else {
            Node left = expr.getChildren().get(1);
            Node right = expr.getChildren().get(0);
            Quadruple quadruple = new Quadruple(expr.label, generateArithmetic(left), generateArithmetic(right), generateTemporal());
            quadruplesList.add(quadruple);
            return quadruple.getResult();
        }
    }

    public static String generateTemporal() {
        return "$t" + (++temporalCount);
    }

    public static String generateLabel() {
        return "label" + (++labelCount);
    }

    public static void generateOr(Node bool, String trueLabel, String falseLabel) {
        if (SemanticAnalyzer.COMPARISON_OPERATORS.contains(bool.label)) {
            Node arg1 = bool.getChildren().get(1);
            Node arg2 = bool.getChildren().get(0);
            //el trueLabel lo pasa el bloque padre (un if o while o algo que tenga esta condicion), y dicho label se refiere al body del bloque
            Quadruple quadruple = new Quadruple("IF ".concat(bool.label), generateArithmetic(arg1), generateArithmetic(arg2), "goto " + trueLabel);
            quadruplesList.add(quadruple);
            quadruple = new Quadruple("goto ".concat(falseLabel));
            quadruplesList.add(quadruple);
        } else if (bool.label.equalsIgnoreCase("and")) {
            generateAnd(bool, trueLabel, falseLabel);
        } else if (bool.label.equalsIgnoreCase("or")) {
            //OR specific code generation
            Node expr1 = bool.getChildren().get(1);
            Node expr2 = bool.getChildren().get(0);
            String ifFalse = generateLabel();
            generateOr(expr1, trueLabel, ifFalse);
            Quadruple quadruple = new Quadruple(ifFalse.concat(":"));
            quadruplesList.add(quadruple);
            generateOr(expr2, trueLabel, falseLabel);
        } else {

        }
    }

    public static void generateAnd(Node bool, String trueLabel, String falseLabel) {
        System.out.println("missing and generator :)");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Quadruple quad : quadruplesList) {
            builder.append(quad.visualize()).append("\n");
        }
        return builder.toString();
    }
}
