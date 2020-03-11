package cn.rxframework.utils.mail;

import cn.rxframework.utils.mail.bean.MailConfig;
import cn.rxframework.utils.mail.bean.MessageSendEntity;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.CompatibilityHints;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

public class MailSendUtil extends MailSessionInit {
    private static Log log = LogFactory.getLog(MailSendUtil.class);
    private static String protocol = "pop";

    private static MessageSendEntity defaultMailBean = MailConfig.getMailBean();

    private MailSendUtil() {
        try {
            init(protocol);
        } catch (Exception e) {
            log.error("Fail to initial mail session", e);
        }
    }

    public static MailSendUtil getInstance() {
        return new MailSendUtil();
    }

    /**
     * <b>DEDCRIPTION:</b> You can send a mail use this method, with which you can send content, attachment or both
     *
     * @param mail  use default mailBean while this param is null
     */
    public boolean send(MessageSendEntity mail) {
//        new MailSendUtil();
        try {
            Message message = createMessage(mail);

            if (message != null) {

                transport.sendMessage(message, message.getAllRecipients());
                log.debug("- " + (mail.isMeetingFlag() ? "meeting " : "") + "mail send successfully!");
                log.debug("- TO : " + Arrays.toString(mail.getTo()));
                if (ArrayUtils.isNotEmpty(mail.getCc()))
                    log.debug("- CC  : " + Arrays.toString(mail.getCc()));
                if (ArrayUtils.isNotEmpty(mail.getBcc()))
                    log.debug("- BCC : " + Arrays.toString(mail.getBcc()));
                if (mail.isAttachFlag()) {
                    log.debug("- Attachments Path : " + mail.getAttachRootPath());
                    log.debug("- Attachments Real Name : " + Arrays.toString(mail.getAttachFileName()));
                    log.debug("- Attachments Mail Name : " + Arrays.toString(mail.getAttachName()));
                }
                log.debug("----------------------------------------------------------\n");
            } else {
                throw new NullPointerException("message Object should not be null");
            }

        } catch (Exception e) {
            log.error("When sending mail, here catch exception!!!!! ", e);
            return false;

        } finally {
            try {
                close();
            } catch (MessagingException e) {
                log.error("", e);
            }
        }

        return true;
    }

    /**
     * <b>DEDCRIPTION:</b> send mail to default receiver in configuration
     *
     * @param debug
     * @param subject
     * @param content
     * @return
     */
    public boolean sendAdmin(boolean debug, String subject, String content) {
        return sendAdmin(debug, subject, content, null, null, null);
    }

    /**
     * <b>DEDCRIPTION:</b> send mail to default receiver in configuration
     *
     * @param debug
     * @param subject
     * @param content
     * @param attachPath
     * @param attachFileName
     * @param attachName
     * @return
     */
    public boolean sendAdmin(boolean debug, String subject, String content, String attachPath, String[] attachFileName, String[] attachName) {
        MessageSendEntity mail = new MessageSendEntity();

        mail.setTo(defaultMailBean.getTo());
        mail.setSubject(StringUtils.isEmpty(subject) ? MailConfig.getMailBean().getSubject() : subject);
        mail.setContentFlag(true);
        mail.setMailContent(StringUtils.isEmpty(content) ? MailConfig.getMailBean().getMailContent() : content);

        if (StringUtils.isNotEmpty(attachPath)) {
            mail.setAttachFlag(true);
            mail.setAttachRootPath(attachPath);
            mail.setAttachFileName(attachFileName);
            mail.setAttachName(attachName);
        }

        mail.setSendDate(new Date());
        return send(mail);
    }

    /**
     * build the mail Object
     *
     * @return Message
     * @throws Exception
     */
    private Message createMessage(MessageSendEntity mail) throws Exception {

        MimeMessage message = null;
        mail = mail == null ? defaultMailBean : mail;

        if (mail.getTo() != null || mail.isMeetingFlag()) {
            message = new MimeMessage(session);

            // set sender address
            String from = mail.getFrom() == null ? defaultMailBean.getFrom() : mail.getFrom();
            message.setFrom(new InternetAddress(from, from));

            message.setSentDate(mail.getSendDate());

            // set receiver addresses
            message.addRecipients(Message.RecipientType.TO, getAddress(mail.getTo()));

            // set carbon copy(CC) receiver addresses
            if (ArrayUtils.isNotEmpty(mail.getCc())) {
                message.addRecipients(Message.RecipientType.CC, getAddress(mail.getCc()));
            }
            // set blind carbon copy(BCC) receiver addresses
            if (ArrayUtils.isNotEmpty(mail.getBcc())) {
                message.addRecipients(Message.RecipientType.BCC, getAddress(mail.getBcc()));
            }

            // set mail subject
            message.setSubject(mail.getSubject());
//			message.setSubject(MimeUtility.encodeText(mail.getMailSubject(), "GB2312", "B"));

            // get mail object
            Multipart multipart = mail.isMeetingFlag() ? getMeetingMailBody(mail) : getCommonMailBody(mail);

            // set multipart object to mail message object
            message.setContent(multipart);
            // save
            message.saveChanges();
        }

        return message;
    }

    private Multipart getCommonMailBody(MessageSendEntity mail) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        // set mail content and attachment
        if (mail.isContentFlag() || mail.isAttachFlag()) {
            // set content
            if (mail.isContentFlag()) {
                MimeBodyPart contentPart = new MimeBodyPart();
//				contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
                String contentEncoding = mail.getMailContentEncoding() == null ? defaultMailBean.getMailContentEncoding() : mail.getMailContentEncoding();
                if (mail.isHtmlContentFlag()) {
                    contentPart.setContent(mail.getMailContent(), "text/html;charset=" + contentEncoding);
                } else {
                    contentPart.setText(mail.getMailContent(), contentEncoding);
                }
                multipart.addBodyPart(contentPart);
            }

            // set attachments to multipart
            if (mail.isAttachFlag() && StringUtils.isNotEmpty(mail.getAttachRootPath()) && ArrayUtils.isNotEmpty(mail.getAttachFileName())) {
                addAttach(mail.getAttachRootPath(), mail.getAttachFileName(), mail.getAttachName(), multipart);
            }
        }
        return multipart;
    }

    private Multipart getMeetingMailBody(MessageSendEntity mail) throws Exception {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart iCalAttachment = new MimeBodyPart();
        MimeBodyPart contentPart = new MimeBodyPart();

//		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
//		TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
//		VTimeZone tz = timezone.getVTimeZone();

        byte[] invite = createICalInvitation(mail);
        /*setDataHandler的处理方式也是非常关键
		如果直接按照处理  
		mimeMessage.setContent(iCalAttachment , "text/calendar");  
		则在邮件发送的时候会抛出如下异常  
		javax.activation.UnsupportedDataTypeException: no object DCH for MIME type text/calendar  
		这样发送能否成功目前没有找到解决方案  
		*/
//		ByteArrayDataSource dataSource = new ByteArrayDataSource(
//				new ByteArrayInputStream(invite),
//				"text/calendar;method=REQUEST;charset=" + defaultMailBean.getMailContentEncoding());
//		iCalAttachment.setDataHandler(new DataHandler(dataSource));
        contentPart.setText(mail.getMailContent(), "UTF-8");
        iCalAttachment.setContent(invite, "text/calendar;method=REQUEST;charset=UTF-8");
        multipart.addBodyPart(contentPart);
        multipart.addBodyPart(iCalAttachment);
        if (mail.isAttachFlag()) {
            addAttach(mail.getAttachRootPath(), mail.getAttachFileName(), mail.getAttachName(), multipart);
        }
        return multipart;
    }

    /**
     * mail
     * 1.通过ical4j创建一个Calendar，这个Calendar中可以包括VEvent、VAlarm、TODO等多项内容。
     * 而会议邀请则必须要包含VEvent，如果需要提醒，则可以包含VAlarm<br/>
     * 2.获取到Calendar后，将Calendar放入Message中通过javamail进行发送<br/>
     * <p>
     * <p>
     * 目前，所有流行日历工具比如：Lotus Notes、Outlook、GMail 和 Apple 的 iCal 都支持 iCalendar 标准，<br/>
     * 其文件扩展名为 .ical、.ics、.ifb 或者 .icalendar。C&S（Calendaring and Scheduling） 核心对象是一系列日历和行程安排信息。<br/>
     * 通常情况下，这些日历和行程信息仅仅包含一个 iCalendar 组件<br/>
     * （iCalendar 组件分为 Events(VEVENT)、To-do(VTODO)、Journal(VJOURNAL)、Free/busy time (VFREEBUSY)、VTIMEZONE (time zones) <br/>
     * 和 VALARM (alarms)），但是多个 iCalendar 组件可以被组织在一起<br/>
     */
    private byte[] createICalInvitation(MessageSendEntity mail) throws Exception {

        /**  以下两步骤的处理也是为了防止outlook或者是notes将日历当做附件使用增加的 */
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_OUTLOOK_COMPATIBILITY, true);
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_NOTES_COMPATIBILITY, true);

        // define calendar
        Calendar cal = new Calendar();
        cal.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        cal.getProperties().add(Version.VERSION_2_0);
        cal.getProperties().add(CalScale.GREGORIAN);
        cal.getProperties().add(Method.REQUEST);

        // add TIMEZONE to calendar
//		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
//		VTimeZone tz = registry.getTimeZone(_tz.getID()).getVTimeZone();
//		cal.getComponents().add(tz);

        // add meeting event to calendar
        VEvent vEvent = new VEvent();
//		VEvent meeting = new VEvent(start, end, _subject);

        UidGenerator ug = new UidGenerator("uidGen");
        vEvent.getProperties().add(ug.generateUid());
        vEvent.getProperties().add(new Summary(mail.getSubject()));
        vEvent.getProperties().add(new Description(mail.getMailContent()));
        vEvent.getProperties().add(new DtStart(new DateTime(mail.getMeetingStart())));
        vEvent.getProperties().add(new DtEnd(new DateTime(mail.getMeetingEnd())));
        vEvent.getProperties().add(new Location(mail.getLocation()));
        vEvent.getProperties().add(new Organizer(mail.getFrom()));
        // add attendees
        for (String[] participant : mail.getMeetingReqs()) {
            Attendee attendee = new Attendee(URI.create("mailto:" + participant[0]));
            attendee.getParameters().add(Role.REQ_PARTICIPANT);
//			attendee.getParameters().add(PartStat.NEEDS_ACTION);
//			attendee.getParameters().add(Rsvp.TRUE);
            attendee.getParameters().add(new Cn(participant[1]));
            vEvent.getProperties().add(attendee);
        }
        if (mail.getMeetingOpts() != null && !mail.getMeetingOpts().isEmpty()) {
            for (String[] participant : mail.getMeetingOpts()) {
                Attendee attendee = new Attendee(URI.create("mailto:" + participant[0]));
                attendee.getParameters().add(Role.OPT_PARTICIPANT);
                attendee.getParameters().add(new Cn(participant[1]));
                vEvent.getProperties().add(attendee);
            }
        }

        // add alerm to meeting
        if (mail.isAlarmFlag()) {
            VAlarm reminder = new VAlarm(new Dur(0, 0, -30, 0));

            // repeat reminder four (4) more times every fifteen (2) minutes..
            reminder.getProperties().add(new Repeat(30));
            reminder.getProperties().add(new Duration(new Dur(0, 0, 1, 0)));

            // display a message
            reminder.getProperties().add(Action.DISPLAY);
            reminder.getProperties().add(new Description(mail.getMeetingStart() + " 开会"));

            vEvent.getAlarms().add(reminder);
//			cal.getComponents().add(reminder);
        }

        cal.getComponents().add(vEvent);

        //如果邮件得到的日历内容时乱码，可以考虑通过将日历内容按照某种编码转换成bytes后，在生成stream
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(cal, bout);
        return bout.toByteArray();
    }

    /**
     * <b>DEDCRIPTION:</b> add attachments to the mail
     *
     * @param rootPath
     * @param attachFileName
     * @param attachName
     * @param multipart
     * @return
     */
    private void addAttach(String rootPath, String[] attachFileName, String[] attachName, Multipart multipart) {
        for (int i = 0; i < attachFileName.length; i++) {
            try {
                String path = rootPath + attachFileName[i];
                BodyPart affixBody = new MimeBodyPart();

                DataSource source = new FileDataSource(new File(path));

                // add attach file
                affixBody.setDataHandler(new DataHandler(source));
                // set mail attach display name
                affixBody.setFileName(MimeUtility.encodeText(attachName[i], defaultMailBean.getMailContentEncoding(), "B"));

                multipart.addBodyPart(affixBody, i);
            } catch (Exception e) {
                log.error("attach generate failed ---- likely attach is not exist", e);
            }
        }
    }

}
