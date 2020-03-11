package cn.rxframework.utils.mail;

import cn.rxframework.utils.mail.bean.MailConfig;
import cn.rxframework.utils.mail.bean.MessageEntity;
import cn.rxframework.utils.mail.bean.MessageFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MailRetrieveUtil extends MailSessionInit {
    private static Log log = LogFactory.getLog(MailRetrieveUtil.class);
    private static String protocol = "imap";

    private MailRetrieveUtil() {
        try {
            init(protocol);
        } catch (Exception e) {
            log.error("Fail to initial mail session", e);
        }
    }

    public static MailRetrieveUtil getInstance() {
        return new MailRetrieveUtil();
    }

    /**
     * Get latest mails(in default 'hoursIn' hours) without attachments
     *
     * @param size
     * @return
     */
    public List<MessageEntity> getLatestEmails(int size) {
        return getLatestEmails(size, MailConfig.getMailHoursIn());
    }

    /**
     * Get latest mails without attachments
     *
     * @param size
     * @param hoursIn
     * @return
     */
    public List<MessageEntity> getLatestEmails(int size, int hoursIn) {
        return getLatestEmails(size, hoursIn, false);
    }

    /**
     * Get latest mails
     *
     * @param size
     * @param hoursIn
     * @param withAttachment
     * @return
     */
    public List<MessageEntity> getLatestEmails(int size, int hoursIn, boolean withAttachment) {

        List<MessageEntity> messageEntityList = new ArrayList<>();
        try {
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            size = size <= 0 ? 5 : size;
            int start = folderInbox.getMessageCount() >= size ? folderInbox.getMessageCount() - size + 1 : 1;
            Message[] messages = folderInbox.getMessages(start, folderInbox.getMessageCount());

            // return mails in x hours
            List<Message> filteredMessages = filterMessages(messages, hoursIn);

            for (int i = filteredMessages.size() - 1; i >= 0; i--) {
                MessageEntity messageEntity = new MessageEntity();
                Message msg = filteredMessages.get(i);
                Address[] fromAddress = msg.getFrom();

                messageEntity.setFrom(fromAddress[0].toString());
                messageEntity.setSubject(msg.getSubject());
                messageEntity.setTo(parseAddresses(msg.getRecipients(Message.RecipientType.TO)));
                messageEntity.setCc(parseAddresses(msg.getRecipients(Message.RecipientType.CC)));
                messageEntity.setSendDate(msg.getSentDate());

                String contentType = msg.getContentType();
                messageEntity.setContentType(contentType);
                String messageContent = "";
                // Common mail without attachment
                if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    try {
                        messageContent = Objects.toString(msg.getContent(), "'");
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        log.error(messageContent, ex);
                    }
                    messageEntity.setContent(messageContent);
                }
                // Multipart mail with attachments
                List<MessageFile> files = new ArrayList<>();
                if (contentType.contains("multipart/mixed")) {
                    Multipart multipart = (Multipart) msg.getContent();
                    try {
                        for (int k = 0; k < multipart.getCount(); k++) {
                            MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(k);
                            if (bodyPart.getContentType().contains("text/plain") || bodyPart.getContentType().contains("text/html")) {
                                messageContent = Objects.toString(bodyPart.getContent(), "'");
                            } else if (withAttachment) {
                                FileUtils.forceMkdir(new File(MailConfig.getAttachPath()));
                                File file = new File(String.format(MailConfig.getAttachPath() + "%s.data", new Date().getTime()));
                                bodyPart.saveFile(file);
                                files.add(new MessageFile(bodyPart.getFileName(), file));
                            }
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        log.error(messageContent, ex);
                    }
                    messageEntity.setContent(messageContent);
                    messageEntity.setAttachments(files);
                }

                messageEntityList.add(messageEntity);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (MessagingException | IOException ex) {
            log.error("Could not connect to the message store", ex);
        }
        return messageEntityList;
    }

    /**
     * Filter messages in 'hoursIn' hours
     *
     * @param messages
     * @param hoursIn
     * @return
     * @throws MessagingException
     */
    private List<Message> filterMessages(Message[] messages, int hoursIn) throws MessagingException {
        List<Message> filteredMessages = new ArrayList<>();
        for (Message message : messages) {
            if (DateUtils.addHours(message.getSentDate(), hoursIn).getTime() > new Date().getTime()) {
                filteredMessages.add(message);
            }
        }
        return filteredMessages;
    }
}
