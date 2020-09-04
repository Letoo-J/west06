package com.mine.west.modelLegal;

import com.mine.west.constant.AccountConstants;

public class AccountLegal extends BaseLegal {
    public static boolean accountIDLegal(Integer accountID) {
        if (accountID < 1)
            return false;
        return true;
    }

    public static boolean emailLegal(String email) {
        //return true;
        if (!email.matches(AccountConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    public static boolean phoneLegal(String phone) {
        if (stringIsEmpty(phone))
            return true;
        if (phone.length() != 11)
            return false;
        if (phone.matches(regex1) || regex1.matches(phone))
            return true;
        return false;
    }

    public static boolean nicknameLegal(String nickname) {
        if (stringIsEmpty(nickname))
            return true;
        if (nickname.length() > 20)
            return false;
        return true;
    }

    public static boolean sexLegal(String sex) {
        if (stringIsEmpty(sex))
            return true;
        if (sex.equals("男") || sex.equals("女") || sex.equals("保密"))
            return true;
        return false;
    }

    public static boolean individualitySignatureLegal(String individualitySignature) {
        if (stringIsEmpty(individualitySignature))
            return true;
        if (individualitySignature.length() >= 255)
            return false;
        return true;
    }

    public static boolean passwordLegal(String password) {
        if ((password == null) || (password == ""))
            return true;
        int len = password.length();
        if ((len <= 0) || (len >= 20))
            return false;
        if (!password.matches(regex3))
            return false;
        if (password.matches(regex1) || password.matches(regex2))
            return false;
        for (int i = 0; i < password.length(); i++)
            if (password.charAt(i) != '_')
                return true;
        return false;
    }
}
