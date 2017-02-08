package cn.rxframework.utils.mail;

import cn.rxframework.utils.mail.MailSendUtil;
import cn.rxframework.utils.mail.bean.MailMsgBean;
import org.junit.Test;

import java.util.Date;

/**
 * TODO 一句话描述该类用途
 * <p>
 * 创建时间: 16/12/16<br/>
 *
 * @author xule
 * @since v0.0.1
 */
public class MailSendUtilTest {

    @Test
    public void testSend2Admin() throws Exception {
        //		MailSendUtil mailSendUtil = new MailSendUtil();
//		MailMsgBean mail = new MailMsgBean();
//		mail.setMailFrom("xuxutest");
//		mail.setMailTo(new String[] { "richard.xu@163.com" });
//		mail.setHasCC(false);
////		mail.setMailCC(new String[] { "richard.xu@163.com" });
//		mail.setMailSubject("尊敬的bo.wang,您ERP中申请的业务已处理!");
//
//		mail.setContentFlag(true);
//		mail.setHtmlContent(true);
//		mail.setTextContent("safd");
//
//		   mail.setAttchFlag(false);
////		mail.setAttchFlag(true);
////		mail.setAttachRootPath("E:/aa/");
////		mail.setAttachFileName(new String[] {"data4CubeAD-20120423.xls", "data4CubeAD-20120424.xls" });
////		mail.setAttachName(new String[] {"附件文件1.xls", "附件文件2.xls" });
//
//		mail.setSendDate(new Date());
//		String[] AttachFileName = new String[]{"新员工使用.xlsx"};
//		String[] AttachName = new String[]{"新员工使用.xlsx"};
        MailSendUtil.getInstance().sendAdmin(true, "【系统消息】全通notify", "这是一封测试邮件");
//		MailSendUtil.sendAdmin(true, "【系统消息】全通notify", "这是一封测试邮件","/tmp/notify/mail/",AttachFileName,AttachName);
    }

    @Test
    public void testSendCommonMail() throws Exception {
        for(int i = 1; i < 2; i++){
            MailMsgBean mail = new MailMsgBean();
            mail.setMailTo(new String[]{"xule@qtonecloud.cn"});
            mail.setMailSubject("徐乐测试");
            mail.setContentFlag(true);
            mail.setMailContent("test - " + System.currentTimeMillis());
            mail.setSendDate(new Date());

            MailSendUtil.getInstance().send(true, mail);
        }
    }
}
