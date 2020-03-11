package cn.rxframework.utils.mail.bean;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

/**
 * JavaMail Config Bean
 */
public class MailConfig {

    private static String mailHostReceiver;
    private static String mailHostSender;
    private static String mailSendPort;
    private static String mailReceivePort;
    private static boolean mailDebug;
    private static String mailPath;
    private static boolean isValidate;
    private static String mailFromUserName;
    private static String mailFromUserPswd;
    private static int mailHoursIn;
    private static String attachPath;
    private static MessageSendEntity mailBean;

    static {
        Properties props = new Properties();

        try {
            InputStream is = MailConfig.class.getResourceAsStream("/mail.properties");//custom directory
            if (is != null) {
                props.load(new InputStreamReader(is, "UTF-8"));
            } else {
                File defaultConfigFile = new File(MailConfig.class.getResource("..") + "mail.properties");//current directory
                props.load(new InputStreamReader(new FileInputStream(defaultConfigFile), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mailHostSender = props.getProperty("mail.sender.host");
        mailHostReceiver = props.getProperty("mail.receiver.host");
        mailSendPort = props.getProperty("mail.sender.port");
        mailReceivePort = props.getProperty("mail.receiver.port");
        mailDebug = Boolean.valueOf(props.getProperty("mail.debug"));
        mailPath = props.getProperty("mail.path");
        isValidate = Boolean.valueOf(props.getProperty("mail.validate"));
        mailFromUserName = props.getProperty("mail.from.username");
        mailFromUserPswd = props.getProperty("mail.from.password");
        mailHoursIn = Integer.valueOf(StringUtils.defaultIfBlank(props.getProperty("mail.hours.in"), "24"));
        attachPath = props.getProperty("mail.attach.path");

        //default MailMsgBean
        MessageSendEntity mailMsgBean = new MessageSendEntity();
        mailMsgBean.setFrom(props.getProperty("mailFrom"));
        if (StringUtils.isNotEmpty(props.getProperty("mailTo"))) {
            mailMsgBean.setTo(props.getProperty("mailTo").split(","));
        } else {
            mailMsgBean.setTo(null);
        }
        if (StringUtils.isNotEmpty(props.getProperty("mailCC"))) {
            mailMsgBean.setCc(props.getProperty("mailCC").split(","));
        }
        if (StringUtils.isNotEmpty(props.getProperty("mailBCC"))) {
            mailMsgBean.setBcc(props.getProperty("mailBCC").split(","));
        }

        mailMsgBean.setSubject(props.getProperty("mailSubject"));
        mailMsgBean.setSendDate(new Date());

        if (Boolean.valueOf(props.getProperty("contentFlag"))) {
            mailMsgBean.setContentFlag(true);
            mailMsgBean.setHtmlContentFlag(Boolean.valueOf(props.getProperty("htmlContentFlag")));
            mailMsgBean.setMailContent(props.getProperty("mailContent"));
            mailMsgBean.setMailContentEncoding(props.getProperty("mailContentEncoding"));
        } else {
            mailMsgBean.setContentFlag(false);
            mailMsgBean.setMailContent(null);
        }

        mailBean = mailMsgBean;
    }

    public static void main(String[] args) {
        System.out.println(new MailConfig().toString());
    }

    public static String getMailHostReceiver() {
        return mailHostReceiver;
    }

    public static void setMailHostReceiver(String mailHostReceiver) {
        MailConfig.mailHostReceiver = mailHostReceiver;
    }

    public static String getMailHostSender() {
        return mailHostSender;
    }

    public static void setMailHostSender(String mailHostSender) {
        MailConfig.mailHostSender = mailHostSender;
    }

    public static String getMailSendPort() {
        return mailSendPort;
    }

    public static void setMailSendPort(String mailSendPort) {
        MailConfig.mailSendPort = mailSendPort;
    }

    public static boolean isMailDebug() {
        return mailDebug;
    }

    public static void setMailDebug(boolean mailDebug) {
        MailConfig.mailDebug = mailDebug;
    }

    public static String getMailReceivePort() {
        return mailReceivePort;
    }

    public static void setMailReceivePort(String mailReceivePort) {
        MailConfig.mailReceivePort = mailReceivePort;
    }

    public static String getMailPath() {
        return mailPath;
    }

    public static void setMailPath(String mailPath) {
        MailConfig.mailPath = mailPath;
    }

    public static boolean isValidate() {
        return isValidate;
    }

    public static void setValidate(boolean isValidate) {
        MailConfig.isValidate = isValidate;
    }

    public static String getMailFromUserName() {
        return mailFromUserName;
    }

    public static void setMailFromUserName(String mailFromUserName) {
        MailConfig.mailFromUserName = mailFromUserName;
    }

    public static String getMailFromUserPswd() {
        return mailFromUserPswd;
    }

    public static void setMailFromUserPswd(String mailFromUserPswd) {
        MailConfig.mailFromUserPswd = mailFromUserPswd;
    }

    public static int getMailHoursIn() {
        return mailHoursIn;
    }

    public static void setMailHoursIn(int mailHoursIn) {
        MailConfig.mailHoursIn = mailHoursIn;
    }

    public static String getAttachPath() {
        return attachPath;
    }

    public static void setAttachPath(String attachPath) {
        MailConfig.attachPath = attachPath;
    }

    public static MessageSendEntity getMailBean() {
        return mailBean;
    }

    public static void setMailBean(MessageSendEntity mailBean) {
        MailConfig.mailBean = mailBean;
    }
}
