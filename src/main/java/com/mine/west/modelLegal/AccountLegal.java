package com.mine.west.modelLegal;

public class AccountLegal extends BaseLegal {
    public static boolean accountIDLegal(Integer accountID) {
        if (accountID < 1)
            return false;
        return true;
    }
}
