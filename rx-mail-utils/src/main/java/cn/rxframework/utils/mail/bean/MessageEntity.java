package cn.rxframework.utils.mail.bean;

import java.util.Date;
import java.util.List;

public class MessageEntity {

    private String subject;
    private String from;
    private String[] to;
    private String[] cc;
    private Date sendDate;
    private String contentType;
    private String content;
    private List<MessageFile> attachments;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MessageFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MessageFile> attachments) {
        this.attachments = attachments;
    }
}
