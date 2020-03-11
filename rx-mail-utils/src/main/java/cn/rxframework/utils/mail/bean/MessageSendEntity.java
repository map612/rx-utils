package cn.rxframework.utils.mail.bean;

import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Repeat;

import java.util.Date;
import java.util.List;

/**
 * @title		JavaMail发送工具
 * @description 
 * @author  	richard.xu
 * @since 		2012-12-5
 * @version  	1.0.0
 * @serial   	
 */
public class MessageSendEntity extends MessageEntity {

	private String[] bcc;
	/**
	 * common mail configuration params
	 */
	private boolean contentFlag;
	private boolean htmlContentFlag;
	private String mailContent;
	private String mailContentEncoding = "UTF-8";
	
	private boolean attachFlag;
	private String attachRootPath;
	private String[] attachFileName; //real path
	private String[] attachName; //logic name
	
	/**
	 * alarm meeting config params
	 */
	private boolean meetingFlag;
	private Date meetingStart;
	private Date meetingEnd;
	private String location;
	private List<String[]> meetingReqs; //meeting request attendees
	private List<String[]> meetingOpts; //meeting optional attendees
	private boolean alarmFlag;
	private Dur alarmDur = new Dur(0, -1, 0, 0);//default. 1 hour in advance to remind
	private Duration alarmDuration = new Duration(new Dur(0, 0, 15, 0));//default. every 15 minutes to remind
	private Repeat alarmRepeat = new Repeat(4);//default. alarm 4 times

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public boolean isContentFlag() {
		return contentFlag;
	}

	public void setContentFlag(boolean contentFlag) {
		this.contentFlag = contentFlag;
	}

	public boolean isHtmlContentFlag() {
		return htmlContentFlag;
	}

	public void setHtmlContentFlag(boolean htmlContentFlag) {
		this.htmlContentFlag = htmlContentFlag;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailContentEncoding() {
		return mailContentEncoding;
	}

	public void setMailContentEncoding(String mailContentEncoding) {
		this.mailContentEncoding = mailContentEncoding;
	}

	public boolean isAttachFlag() {
		return attachFlag;
	}

	public void setAttachFlag(boolean attachFlag) {
		this.attachFlag = attachFlag;
	}

	public String getAttachRootPath() {
		return attachRootPath;
	}

	public void setAttachRootPath(String attachRootPath) {
		this.attachRootPath = attachRootPath;
	}

	public String[] getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String[] attachFileName) {
		this.attachFileName = attachFileName;
	}

	public String[] getAttachName() {
		return attachName;
	}

	public void setAttachName(String[] attachName) {
		this.attachName = attachName;
	}

	public boolean isMeetingFlag() {
		return meetingFlag;
	}

	public void setMeetingFlag(boolean meetingFlag) {
		this.meetingFlag = meetingFlag;
	}

	public Date getMeetingStart() {
		return meetingStart;
	}

	public void setMeetingStart(Date meetingStart) {
		this.meetingStart = meetingStart;
	}

	public Date getMeetingEnd() {
		return meetingEnd;
	}

	public void setMeetingEnd(Date meetingEnd) {
		this.meetingEnd = meetingEnd;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String[]> getMeetingReqs() {
		return meetingReqs;
	}

	public void setMeetingReqs(List<String[]> meetingReqs) {
		this.meetingReqs = meetingReqs;
	}

	public List<String[]> getMeetingOpts() {
		return meetingOpts;
	}

	public void setMeetingOpts(List<String[]> meetingOpts) {
		this.meetingOpts = meetingOpts;
	}

	public Dur getAlarmDur() {
		return alarmDur;
	}

	public boolean isAlarmFlag() {
		return alarmFlag;
	}

	public void setAlarmFlag(boolean alarmFlag) {
		this.alarmFlag = alarmFlag;
	}

	public void setAlarmDur(Dur alarmDur) {
		this.alarmDur = alarmDur;
	}

	public Duration getAlarmDuration() {
		return alarmDuration;
	}

	public void setAlarmDuration(Duration alarmDuration) {
		this.alarmDuration = alarmDuration;
	}

	public Repeat getAlarmRepeat() {
		return alarmRepeat;
	}

	public void setAlarmRepeat(Repeat alarmRepeat) {
		this.alarmRepeat = alarmRepeat;
	}
}
