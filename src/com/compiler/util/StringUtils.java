package com.compiler.util;

/**
 * Created by Wilmer Carranza on 04/06/2015.
 */
public class StringUtils {

    public StringUtils() {
        super();
    }

    public static <T> T operate(String operator, T val1, T val2) throws ArithmeticException {
        if (val1.getClass() == Integer.class) {
            if (operator.equalsIgnoreCase("+")) {
                return (T) (Integer) ((Integer) val1 + (Integer) val2);
            } else if (operator.equalsIgnoreCase("-")) {
                return (T) (Integer) ((Integer) val1 - (Integer) val2);
            } else if (operator.equalsIgnoreCase("*")) {
                return (T) (Integer) ((Integer) val1 * (Integer) val2);
            } else if (operator.equalsIgnoreCase("/")) {
                if ((Integer) val2 != 0) {
                    return (T) (Integer) ((Integer) val1 / (Integer) val2);
                } else {
                    throw new ArithmeticException();
                }
            } else if (operator.equalsIgnoreCase("%")) {
                if ((Integer) val2 != 0) {
                    return (T) (Integer) ((Integer) val1 % (Integer) val2);
                } else {
                    throw new ArithmeticException();
                }
            }
        } else if (val1.getClass() == Double.class) {
            if (operator.equalsIgnoreCase("+")) {
                return (T) (Double) ((Double) val1 + (Double) val2);
            } else if (operator.equalsIgnoreCase("-")) {
                return (T) (Double) ((Double) val1 - (Double) val2);
            } else if (operator.equalsIgnoreCase("*")) {
                return (T) (Double) ((Double) val1 * (Double) val2);
            } else if (operator.equalsIgnoreCase("/")) {
                if ((Double) val2 != 0) {
                    return (T) (Double) ((Double) val1 / (Double) val2);
                } else {
                    throw new ArithmeticException();
                }
            } else if (operator.equalsIgnoreCase("%")) {
                if ((Double) val2 != 0) {
                    return (T) (Double) ((Double) val1 % (Double) val2);
                } else {
                    throw new ArithmeticException();
                }
            }
        }

        return null;
    }
}
