package com.chaosopher.tigerlang.compiler.util;

public class Assert {

    public static void assertNotNull(Object o) {
        if (o == null)
            throw new Error("Object is null");
    }

    public static void assertIsTrue(boolean b) {
        if (!b)
            throw new Error("Value is false.");
    }

    public static void assertNotNull(Object o, String message) {
        if (o == null)
            throw new Error("Object is null:" + message);
    }

    public static void assertNotNegative(int i) {
        if (i < 0)
            throw new Error("Less than zero");
    }

    public static void assertLE(int iterations, int mAX_ITERATIONS) {
        if (iterations > mAX_ITERATIONS)
            throw new Error("Exceeded max");
    }

	public static void unreachable() {
        throw new Error("unreachable");
	}
}