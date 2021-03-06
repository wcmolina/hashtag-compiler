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
    private ArrayList<Quadruple> quadrupleList;
    public static final ArrayList<Temporal> TEMPORALS = new ArrayList<>(Arrays.asList(
            new Temporal("$t0", true),
            new Temporal("$t1", true),
            new Temporal("$t2", true),
            new Temporal("$t3", true),
            new Temporal("$t4", true),
            new Temporal("$t5", true),
            new Temporal("$t6", true),
            new Temporal("$t7", true),
            new Temporal("$t8", true)));
    ;
    private String propagateLabel;

    public IntermediateCode() {
        quadrupleList = new ArrayList<Quadruple>();
        temporalCount = 0;
        labelCount = 0;
        propagateLabel = "";

    }

    public void generateCode(Node node, String next) {
        ArrayList<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node current = children.get(i);
            switch (current.label.toLowerCase()) {
                case "if":
                    if (current.getData().getType().equalsIgnoreCase("if_statement"))
                        generateIf(current, next, i == children.size() - 1);
                    else generateIfElse(current, next, i == children.size() - 1);
                    break;
                case "assign":
                case "init":
                    generateAssignment(current);
                    break;
                case "while":
                    generateWhile(current, next, i == children.size() - 1);
                    break;
                case "for":
                    generateFor(current, next, i == children.size() - 1);
                    break;
                default:
                    generateCode(children.get(i), next);
                    break;
            }
        }
        if (node.label.equalsIgnoreCase("main")) {
            checkPropagatedLabel();
            Quadruple quadruple = new Quadruple("END");
            quadrupleList.add(quadruple);
        }
    }

    private void generateIf(Node node, String nextJump, boolean isLastElement) {
        checkPropagatedLabel();

        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node body = node.getChildren().get(1);
        String ifLabel = generateLabel();
        String nextLabel;
        if (isLastElement) {
            if (nextJump.isEmpty()) {
                nextJump = generateLabel();
                nextLabel = nextJump;
            } else nextLabel = nextJump;

        } else nextLabel = generateLabel();

        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, ifLabel, nextLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, ifLabel, nextLabel);
        }
        propagateLabel = ifLabel;
        generateCode(body, nextLabel);

        propagateLabel = nextLabel;
    }

    private void generateIfElse(Node node, String nextJump, boolean isLastElement) {
        checkPropagatedLabel();

        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node ifBody = node.getChildren().get(1);
        Node elseBody = node.getChildren().get(2).getChildren().get(0);
        String ifLabel = generateLabel();
        String elseLabel = generateLabel();
        String nextLabel;
        if (isLastElement) {
            if (nextJump.isEmpty()) {
                nextJump = generateLabel();
                nextLabel = nextJump;
            } else nextLabel = nextJump;

        } else nextLabel = generateLabel();
        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, ifLabel, elseLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, ifLabel, elseLabel);
        }

        propagateLabel = ifLabel;
        generateCode(ifBody, nextLabel);

        //jump to next instruction
        Quadruple quadruple = new Quadruple("goto ".concat(nextLabel));
        quadrupleList.add(quadruple);

        propagateLabel = elseLabel;
        generateCode(elseBody, nextLabel);

        propagateLabel = nextLabel;
    }

    private void generateWhile(Node node, String nextJump, boolean isLastElement) {

        Node conditions = node.getChildren().get(0).getChildren().get(0);
        Node whileBody = node.getChildren().get(1);
        String startLabel;
        startLabel = (propagateLabel.isEmpty()) ? generateLabel() : propagateLabel;
        Quadruple quadruple = new Quadruple(startLabel.concat(":"));
        quadrupleList.add(quadruple);

        String whileLabel = generateLabel();
        String nextLabel;
        if (isLastElement) {
            if (nextJump.isEmpty()) {
                nextJump = generateLabel();
                nextLabel = nextJump;
            } else nextLabel = nextJump;

        } else nextLabel = generateLabel();

        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, whileLabel, nextLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, whileLabel, nextLabel);
        }

        propagateLabel = whileLabel;
        generateCode(whileBody, nextLabel);
        quadruple = new Quadruple("goto ".concat(startLabel));
        quadrupleList.add(quadruple);

        propagateLabel = nextLabel;
    }

    private void generateFor(Node node, String nextJump, boolean isLastElement) {
        checkPropagatedLabel();

        Node structure = node.getChildren().get(0);
        Node init = structure.getChildren().get(0).getChildren().get(0);
        Node conditions = structure.getChildren().get(1);
        Node update = structure.getChildren().get(2);
        Node forBody = node.getChildren().get(1);

        String startLabel;
        String forLabel = generateLabel();
        String nextLabel;
        String updateLabel = generateLabel();

        if (isLastElement) {
            if (nextJump.isEmpty()) {
                nextJump = generateLabel();
                nextLabel = nextJump;
            } else nextLabel = nextJump;

        } else nextLabel = generateLabel();

        generateAssignment(init);
        startLabel = (propagateLabel.isEmpty()) ? generateLabel() : propagateLabel;
        Quadruple quadruple = new Quadruple(startLabel.concat(":"));
        quadrupleList.add(quadruple);

        if (conditions.label.equalsIgnoreCase("or") || SemanticAnalyzer.COMPARISON_OPERATORS.contains(conditions.label)) {
            generateOr(conditions, forLabel, nextLabel);
        } else if (conditions.label.equalsIgnoreCase("and")) {
            generateAnd(conditions, forLabel, nextLabel);
        }
        quadruple = new Quadruple(updateLabel.concat(":"));
        quadrupleList.add(quadruple);
        generateAssignment(update);
        quadruple = new Quadruple("goto ".concat(startLabel));
        quadrupleList.add(quadruple);

        propagateLabel = forLabel;

        generateCode(forBody, updateLabel);

        //this if is optional, if it causes problems try removing it.
        if (!quadrupleList.get(quadrupleList.size() - 1).getOperator().startsWith("goto")) {
            quadruple = new Quadruple("goto ".concat(updateLabel));
            quadrupleList.add(quadruple);
        }

        propagateLabel = nextLabel;
    }

    private void checkPropagatedLabel() {
        if (!propagateLabel.isEmpty()) {
            Quadruple quadruple = new Quadruple(propagateLabel.concat(":"));
            quadrupleList.add(quadruple);
        }
        propagateLabel = "";
    }

    private void generateAssignment(Node assign) {
        checkPropagatedLabel();
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

    private String generateArithmetic(Node expr) {
        if (expr.isLeaf()) {
            return expr.label;
        } else {
            Node left = expr.getChildren().get(1);
            Node right = expr.getChildren().get(0);
            Quadruple quadruple = new Quadruple(expr.label, generateArithmetic(left), generateArithmetic(right), generateFreeTemporal());
            quadrupleList.add(quadruple);
            return quadruple.getResult();
        }
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
            //check if its 'true' or 'false'; these will generate 'goto' right away
        } else {
        }
    }

/*    private String generateTemporal() {
        return "$t" + (++temporalCount);
    }*/

    private String generateFreeTemporal() {
        for (Temporal temporal : TEMPORALS) {
            if (temporal.isFree()) {
                temporal.setFree(false);
                return temporal.getName();
            }
        }
        //no free temp, possible?
        return "$t";
    }

    private String generateLabel() {
        return "etiq" + (++labelCount);
    }

    public ArrayList<Quadruple> getQuadrupleList() {
        return this.quadrupleList;
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
