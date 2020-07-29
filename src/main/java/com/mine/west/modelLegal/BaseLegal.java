package com.mine.west.modelLegal;

public class BaseLegal {
    protected static final int TEXT_MAX_SIZE = (1 << 16);

    public static boolean stringIsEmpty(String s) {
        if (s == null || "".equals(s) || s.length() < 1)
            return true;
        return false;
    }
}
