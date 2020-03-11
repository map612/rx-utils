package cn.rxframework.utils.mail;

import cn.rxframework.utils.mail.bean.MessageEntity;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MailRetrieveUtilTest {

    @Test
    public void testRetrieveMail() {
        MailRetrieveUtil receiver = MailRetrieveUtil.getInstance();
        int hoursIn = 100;
        List<MessageEntity> messages = receiver.getLatestEmails(5, hoursIn);
        if (messages.size() == 0) {
            System.out.println(String.format("No mails in %d hours", hoursIn));
        } else {
            System.out.println(String.format("%d mails retrieved in 72 hours", messages.size()));
            for (MessageEntity message : messages) {
                System.out.println(String.format("subject: %s, from: %s, to: %s, sendDate: %s ", message.getSubject(), message.getFrom(), Arrays.toString(message.getTo()), message.getSendDate()));
            }
        }
    }
}
