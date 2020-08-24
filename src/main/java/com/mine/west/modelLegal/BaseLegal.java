package com.mine.west.modelLegal;

public class BaseLegal {
    protected static final int TEXT_MAX_SIZE = (1 << 16);
    protected static final String regex1 = "^[0-9]*$";
    protected static final String regex2 = "^[A-Za-z]+$";
    protected static final String regex3 = "^[0-9a-zA-Z_]{1,}$";
    protected static final String emailRegex = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";

    public static boolean stringIsEmpty(String s) {
        if (s == null || "".equals(s) || s.length() < 1)
            return true;
        return false;
    }
}
