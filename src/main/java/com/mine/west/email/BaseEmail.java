package com.mine.west.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class BaseEmail {
    private static final String HOST = "smtp.sina.com";//smtp服务器
    private static final String FROM = "xydflxy@sina.com";//发件人地址
    private static final String USER = "xydflxy@sina.com";//用户名
    private static final String PWD = "2b0bbf79b6c62665";//授权码

    private static String AFFIX;//附件地址
    private static String AFFIXNAME;//附件名称
    private static String SUBJECT;//邮件标题
    private static String[] TOS;//收件人地址

    /**
     * 用于发送邮件
     *
     * @param context 邮件内容
     * @param tos 收件人数组
     * @param affix 附件地址
     * @param affixName 附件名称
     * @param title 邮件标题
     */
    public static void send(String context, String[] tos, String affix, String affixName, String title) {
        if (tos != null)
            TOS = tos;
        AFFIX = affix;
        AFFIXNAME = affixName;
        SUBJECT = title;

        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置发送邮件的邮件服务器的属性
        props.put("mail.smtp.auth", "true");//需要经过授权，有户名和密码的校验
        Session session = Session.getDefaultInstance(props);//用props对象构建一个session
//        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);//用session为参数定义消息对象
        try {
            message.setFrom(new InternetAddress(FROM));//加载发件人地址
            InternetAddress[] sendTo = new InternetAddress[TOS.length];//加载收件人地址
            for (int i = 0; i < TOS.length; i++)
                sendTo[i] = new InternetAddress(TOS[i]);
            message.addRecipients(Message.RecipientType.TO, sendTo);
            message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(FROM));//设置在发送给收信人之前给自己发一份
            message.setSubject(SUBJECT);//加载标题
            Multipart multipart = new MimeMultipart();//向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            BodyPart contentPart = new MimeBodyPart();//设置邮件的文本内容
            contentPart.setText(context);
            multipart.addBodyPart(contentPart);
            if (AFFIX != null) {//添加附件
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(AFFIX);
                messageBodyPart.setDataHandler(new DataHandler(source));//添加附件的内容
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();//添加附件的标题
                messageBodyPart.setFileName("=?GBK?B?" + enc.encode(AFFIXNAME.getBytes()) + "?=");
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);//将multipart对象放到message中
            message.saveChanges();//保存邮件
            Transport transport = session.getTransport("smtp");//发送邮件
            transport.connect(HOST, USER, PWD);//连接服务器的邮箱
            transport.sendMessage(message, message.getAllRecipients());//把邮件发送出去
            transport.close();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
