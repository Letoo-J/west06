package com.mine.west.email;

import com.mine.west.exception.AccountException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.modelLegal.AccountLegal;
import com.mine.west.modelLegal.BaseLegal;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class MailboxVerificationUtil {
    private static final long MAX_TIME = 8 * 60 * 1000L;
    private static final String ORI_CODE = "ABCDEFGHJKMNPQRSTUVWXY023456789";

    private static Map<String, VerificationCode> stringVerificationCodeMap = new HashMap<>();

    /**
     * 发送验证码
     *
     * @param address
     * @return
     * @throws AccountException
     */
    public static boolean sendEmail(String address) throws AccountException {
        if (BaseLegal.stringIsEmpty(address))
            throw new AccountException(ExceptionInfo.ACCOUNT_EMAIL_EMPTY);
        if (!AccountLegal.emailLegal(address))
            throw new AccountException(ExceptionInfo.ACCOUNT_EMAIL_ILLEGAL);

        String code = getCode().toUpperCase();
        stringVerificationCodeMap.put(code, new VerificationCode(address, new Date((new Date()).getTime() + MAX_TIME)));

        String context = "您的邮箱验证码为：\n" + code + "\n" + "有效时间为：" + (MAX_TIME / (60 * 1000L)) + "分钟";
        log.info("您的邮箱验证码为：" + code);
        BaseEmail.send(context, new String[]{address}, null, null, "邮箱验证");

        return true;
    }

    /**
     * 验证验证码
     *
     * @param code
     * @param mailbox
     * @return
     * @throws AccountException
     */
    public static boolean verificationCodeIsLegal(String code, String mailbox) throws AccountException {
        code = code.toUpperCase();
        if (BaseLegal.stringIsEmpty(code))
            throw new AccountException(ExceptionInfo.EMAIL_CODE_EMPTY);
        VerificationCode vc = stringVerificationCodeMap.get(code);
        if ((vc == null) || (!vc.getMailbox().equals(mailbox)) || (vc.getEffectiveTime().before(new Date())))
            throw new AccountException(ExceptionInfo.EMAIL_CODE_ILLEGAL);
        return true;
    }

    private static String getCode() {
        while (true) {
            StringBuilder sb = new StringBuilder(4);
            for (int i = 0; i < 4; i++)
                sb.append(ORI_CODE.charAt(new Random().nextInt(ORI_CODE.length())));
            VerificationCode vc = stringVerificationCodeMap.get(sb.toString());
            if (vc == null)
                return sb.toString();
            if (vc.getEffectiveTime().before(new Date()))
                return sb.toString();
        }
    }

}
