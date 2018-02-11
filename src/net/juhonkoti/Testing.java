package net.juhonkoti;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Testing {

    public static <T> void assertEquals(T actual, T expected) {
        if (actual != expected) {
            System.err.println("assert: " + actual + " != " + expected);
            throw new RuntimeException("assert: " + actual + " != " + expected);
        }
    }

    public static <T> void assertEquals(Comparable actual, Comparable expected) {
        if (!expected.equals(actual)) {
            System.err.println("assert: " + actual + " != " + expected);
            throw new RuntimeException("assert: " + actual + " != " + expected);
        }
    }

    public Testing() {
        Method[] methods = getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                try {
                    System.out.println("Running test case " + method);
                    method.invoke(this);
                } catch (InvocationTargetException e) {
                    System.err.println("InvocationTargetException on method " + method + ": " + e);
                } catch (IllegalAccessException e) {
                    System.err.println("InvocationTargetException on method " + method + ": " + e);
                }
            }
        }
    }

    public void testFirst() {
        final int [][] block = {
            {0, 0},
            {0, 0},
                {0, 0}

        };
        assertEquals(block.length, 3);
        assertEquals(block[0].length, 2);
    }

}
