package cn.rxframework.utils.mail;

import cn.rxframework.utils.mail.bean.MailConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailSessionInit {
    protected static Log log = LogFactory.getLog(MailSessionInit.class);
    protected static Session session;
    protected static Transport transport; // send mail
    protected static Store store; // receive mail
    protected static boolean isSend;

    private static final String MAIL_SERVER_HOST = "mail.%s.host";
    private static final String MAIL_SERVER_AUTH = "mail.%s.auth";
    private static final String MAIL_SERVER_PORT = "mail.%s.port";

    protected void init(String protocol) throws Exception {

        isSend = StringUtils.containsIgnoreCase(protocol, "smtp") || StringUtils.containsIgnoreCase(protocol, "pop");
        String host = isSend ? MailConfig.getMailHostSender() : MailConfig.getMailHostReceiver();
        String port = isSend ? MailConfig.getMailSendPort() : MailConfig.getMailReceivePort();

        try {
            log.debug("----- start init mail environment -----");
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MailConfig.getMailFromUserName(), MailConfig.getMailFromUserPswd());
                }
            };

            Properties props = new Properties();
            // Mail server setting
            props.put(String.format(MAIL_SERVER_HOST, protocol), host);
            props.put(String.format(MAIL_SERVER_PORT, protocol), port);
            props.put(String.format(MAIL_SERVER_AUTH, protocol), MailConfig.isValidate());

            // SSL setting
            props.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
            props.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
            props.setProperty(String.format("mail.%s.socketFactory.port", protocol), port);

//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true);
//            props.put("mail.smtp.ssl.enable", "true");
//            props.put("mail.smtp.ssl.socketFactory", sf);

            session = Session.getDefaultInstance(props, auth);
            session.setDebug(MailConfig.isMailDebug());

            if(isSend){
                // Create sender connection
                transport = session.getTransport(protocol);
                transport.connect(host, MailConfig.getMailFromUserName(), MailConfig.getMailFromUserPswd());
            } else {
                // Create receiver connection
                store = session.getStore(protocol);
                store.connect(MailConfig.getMailFromUserName(), MailConfig.getMailFromUserPswd());
            }

            log.debug("----- Connect with " + host + " successfully -----");
        } catch (NoSuchProviderException e) {
            log.error(String.format("----- Connect with %s failed, no provider for protocol: %s -----", host, protocol), e);
        } catch (MessagingException e) {
            log.error(String.format("----- Connect with %s failed, could not connect to the message store -----", host), e);
        } catch (Exception e) {
            log.error(String.format("----- Connect with %s failed -----", host), e);
        }
    }

    protected static void close() throws MessagingException {
        if (null != transport) {
            transport.close();
        }
        if (null != store) {
            store.close();
        }
    }

    protected String[] parseAddresses(Address[] address) {
        List<String> addresses = new ArrayList<>();
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                addresses.add(address[i].toString());
            }
        }
        return addresses.toArray(new String[]{});
    }

    protected Address[] getAddress(String[] address) throws AddressException {
        Address[] addresses = new InternetAddress[address.length];
        for (int i = 0; i < address.length; i++)
            addresses[i] = new InternetAddress(address[i]);
        return addresses;
    }
}
