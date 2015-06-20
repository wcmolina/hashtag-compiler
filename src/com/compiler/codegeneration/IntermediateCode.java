package com.compiler.codegeneration;

import com.compiler.ast.*;
import com.compiler.hashtag.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 15/06/2015.
 */
public class IntermediateCode {

    private static int temporalCount;
    private static int labelCount;
    private static ArrayList<Quadruple> quadrupleList;
    private String propagateLabel;

    public IntermediateCode() {
        quadrupleList = new ArrayList<Quadruple>();
        temporalCount = 0;
        labelCount = 0;
        propagateLabel = "";
    }

    public void generateCode(Node node) {
        ArrayList<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node current = children.get(i);
            if (current.label.equalsIgnoreCase("if")) {
                if (current.getData().getType().equalsIgnoreCase("if_statement")) generateIf(current);
                else generateIfElse(current);
            } else if (current.label.equalsIgnoreCase("assign")) {
                generateAssignment(current);
            } else if (current.label.equalsIgnoreCase("while")) {
                generateWhile(current);
            } else {
                generateCode(children.get(i));
            }

        }
    }

    private void generateIf(Node node) {
        propagateLabel = "";
        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node body = node.getChildren().get(1);
        String ifLabel = generateLabel();
        String nextLabel = generateLabel();
        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, ifLabel, nextLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, ifLabel, nextLabel);
        }
        Quadruple quadruple = new Quadruple(ifLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = ifLabel;
        generateCode(body);
        quadruple = new Quadruple(nextLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = nextLabel;
    }

    private void generateIfElse(Node node) {
        propagateLabel = "";
        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node ifBody = node.getChildren().get(1);
        Node elseBody = node.getChildren().get(2).getChildren().get(0);
        String ifLabel = generateLabel();
        String elseLabel = generateLabel();
        String nextLabel = generateLabel();
        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, ifLabel, elseLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, ifLabel, elseLabel);
        }
        Quadruple quadruple = new Quadruple(ifLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = ifLabel;
        generateCode(ifBody);
        quadruple = new Quadruple("goto ".concat(nextLabel));
        quadrupleList.add(quadruple);
        quadruple = new Quadruple(elseLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = elseLabel;
        generateCode(elseBody);
        quadruple = new Quadruple(nextLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = nextLabel;
    }

    private void generateWhile(Node node) {
        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node whileBody = node.getChildren().get(1);

        String startLabel;
        if (propagateLabel.isEmpty()) {
            startLabel = generateLabel();
            Quadruple quadruple = new Quadruple(startLabel.concat(":"));
            quadrupleList.add(quadruple);
        } else startLabel = propagateLabel;

        propagateLabel = "";
        String whileLabel = generateLabel();
        String nextLabel = generateLabel();

        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, whileLabel, nextLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, whileLabel, nextLabel);
        }

        Quadruple quadruple = new Quadruple(whileLabel.concat(":"));
        quadrupleList.add(quadruple);
        propagateLabel = whileLabel;
        generateCode(whileBody);
        quadruple = new Quadruple("goto ".concat(startLabel));
        quadrupleList.add(quadruple);
        quadruple = new Quadruple(nextLabel.concat(":"));
        quadrupleList.add(quadruple);
        if (propagateLabel.isEmpty())
            propagateLabel = nextLabel;
    }

    private void generateAssignment(Node assign) {
        propagateLabel = "";
        Node variable = assign.getChildren().get(0);
        Node value = assign.getChildren().get(1);
        if (value.isLeaf()) {
            Quadruple quadruple = new Quadruple("=", value.label, variable.label);
            quadrupleList.add(quadruple);
        } else if (SemanticAnalyzer.ARITHMETIC_OPERATORS.contains(value.label)) {
            Quadruple quadruple = new Quadruple("=", generateArithmetic(value), variable.label);
            quadrupleList.add(quadruple);
        }
    }

    private void generateInitialization(Node init) {
        //this is tricky, because i can have something like: int x,y,z = 10;
    }

    private static String generateArithmetic(Node expr) {
        if (expr.isLeaf()) {
            return expr.label;
        } else {
            Node left = expr.getChildren().get(1);
            Node right = expr.getChildren().get(0);
            Quadruple quadruple = new Quadruple(expr.label, generateArithmetic(left), generateArithmetic(right), generateTemporal());
            quadrupleList.add(quadruple);
            return quadruple.getResult();
        }
    }

    private static String generateTemporal() {
        return "t" + (++temporalCount);
    }

    private static String generateLabel() {
        return "etiq" + (++labelCount);
    }

    private void generateOr(Node bool, String trueLabel, String falseLabel) {
        if (SemanticAnalyzer.COMPARISON_OPERATORS.contains(bool.label)) {
            Node arg1 = bool.getChildren().get(1);
            Node arg2 = bool.getChildren().get(0);
            //el trueLabel lo pasa el bloque padre (un if o while o algo que tenga esta condicion), y dicho label se refiere al body del bloque
            Quadruple quadruple = new Quadruple("IF ".concat(bool.label), generateArithmetic(arg1), generateArithmetic(arg2), "goto " + trueLabel);
            quadrupleList.add(quadruple);
            quadruple = new Quadruple("goto ".concat(falseLabel));
            quadrupleList.add(quadruple);
        } else if (bool.label.equalsIgnoreCase("and")) {
            generateAnd(bool, trueLabel, falseLabel);
        } else if (bool.label.equalsIgnoreCase("or")) {
            Node expr1 = bool.getChildren().get(1);
            Node expr2 = bool.getChildren().get(0);
            String ifFalse = generateLabel();
            generateOr(expr1, trueLabel, ifFalse);
            Quadruple quadruple = new Quadruple(ifFalse.concat(":"));
            quadrupleList.add(quadruple);
            generateOr(expr2, trueLabel, falseLabel);
        } else if (bool.isLeaf()) {
            System.out.println("boolean expr is leaf");
        } else {
        }
    }

    private void generateAnd(Node bool, String trueLabel, String falseLabel) {
        if (SemanticAnalyzer.COMPARISON_OPERATORS.contains(bool.label)) {
            Node arg1 = bool.getChildren().get(1);
            Node arg2 = bool.getChildren().get(0);
            Quadruple quadruple = new Quadruple("IF ".concat(bool.label), generateArithmetic(arg1), generateArithmetic(arg2), "goto " + trueLabel);
            quadrupleList.add(quadruple);
            quadruple = new Quadruple("goto ".concat(falseLabel));
            quadrupleList.add(quadruple);
        } else if (bool.label.equalsIgnoreCase("or")) {
            generateOr(bool, trueLabel, falseLabel);
        } else if (bool.label.equalsIgnoreCase("and")) {
            //AND specific code generation
            Node expr1 = bool.getChildren().get(1);
            Node expr2 = bool.getChildren().get(0);
            String ifTrue = generateLabel();
            generateAnd(expr1, ifTrue, falseLabel);
            Quadruple quadruple = new Quadruple(ifTrue.concat(":"));
            quadrupleList.add(quadruple);
            generateAnd(expr2, trueLabel, falseLabel);
        } else if (bool.isLeaf()) {
            System.out.println("bool expr is leaf...");
        } else {
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Quadruple quad : quadrupleList) {
            builder.append(quad.visualize()).append("\n");
        }
        return builder.toString();
    }
}
