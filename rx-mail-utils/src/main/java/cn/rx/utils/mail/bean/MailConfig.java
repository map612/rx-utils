package cn.rx.utils.mail.bean;

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

    private static String mailHost;

    private static String mailPath;

    private static boolean isValidate;

    private static String mailFromUserName;

    private static String mailFromUserPswd;

    private static MailMsgBean mailBean;

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

        mailHost = props.getProperty("mailHost");
        mailPath = props.getProperty("mailPath");
        isValidate = Boolean.valueOf(props.getProperty("mailIsValidate"));
        mailFromUserName = props.getProperty("mailFromUserName");
        mailFromUserPswd = props.getProperty("mailFromUserPswd");

        //default MailMsgBean
        MailMsgBean mailMsgBean = new MailMsgBean();
        mailMsgBean.setMailFrom(props.getProperty("mailFrom"));
        if (StringUtils.isNotEmpty(props.getProperty("mailTo"))) {
            mailMsgBean.setMailTo(props.getProperty("mailTo").split(","));
        } else {
            mailMsgBean.setMailTo(null);
        }
        if (StringUtils.isNotEmpty(props.getProperty("mailCC"))) {
            mailMsgBean.setHasCC(true);
            mailMsgBean.setMailCC(props.getProperty("mailCC").split(","));
        } else {
            mailMsgBean.setHasCC(false);
        }
        if (StringUtils.isNotEmpty(props.getProperty("mailBCC"))) {
            mailMsgBean.setHasBCC(true);
            mailMsgBean.setMailBCC(props.getProperty("mailBCC").split(","));
        } else {
            mailMsgBean.setHasBCC(false);
        }

        mailMsgBean.setMailSubject(props.getProperty("mailSubject"));
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

        mailMsgBean.setAttachRootPath(props.getProperty("attachPath"));

        mailBean = mailMsgBean;
    }

    public static void main(String[] args) {
        System.out.println(new MailConfig().toString());
    }

    public static String getMailHost() {
        return mailHost;
    }

    public static void setMailHost(String mailHost) {
        MailConfig.mailHost = mailHost;
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

    public static MailMsgBean getMailBean() {
        return mailBean;
    }

    public static void setMailBean(MailMsgBean mailBean) {
        MailConfig.mailBean = mailBean;
    }
}
