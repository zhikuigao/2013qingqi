package qflag.ucstar.plugin.ucpostbox.service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.jivesoftware.util.Log;

/**
 * @author gzkui 获取邮件信息方法 主题、地址和姓名、日期、内容、已读/未读、已读数量/未读数量
 */

public class UcpostReciveOneMail {

	private MimeMessage mimeMessage = null;
	private String dateformat = "yy-MM-dd HH:mm"; // 默认的日前显示格式
	private StringBuffer bodytext = new StringBuffer();// 存放邮件内容

	public UcpostReciveOneMail(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/*
	 * 获取主题
	 */
	public String getSubject() {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (UnsupportedEncodingException e) {
			Log.error("【邮件获取主题出现错误】：" + e.getMessage());
		} catch (MessagingException e1) {
			Log.error("【邮件获取主题出现错误】：" + e1.getMessage());
		}
		return subject;
	}

	/*
	 * 获取地址和姓名
	 */
	public String getFrom() {
		String fromaddr = "";
		try {
			InternetAddress address[] = (InternetAddress[]) mimeMessage
					.getFrom();
			String from = address[0].getAddress();
			if (from == null)
				from = "";
			String personal = address[0].getPersonal();
			if (personal == null)
				personal = "";
			fromaddr = personal + "<" + from + ">";
		} catch (Exception e) {
			Log.error("【邮件获取地址/姓名出现错误】：" + e.getMessage());
		}
		return fromaddr;
	}

	/*
	 * 获取日期
	 */
	public String getSentDate() {
		String dateMl = "";
		try {
			Date sentdate = mimeMessage.getSentDate();
			SimpleDateFormat format = new SimpleDateFormat(dateformat);
			dateMl = format.format(sentdate);
		} catch (Exception e) {
			Log.error("【邮件获取日期出现错误】：" + e.getMessage());
		}
		return dateMl;
	}

	/*
	 * 获取内容
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/*
	 * 判断已读未读
	 */
	public boolean isNew() {
		boolean isnew = false;
		try {
			Flags flags = ((Message) mimeMessage).getFlags();
			Flags.Flag[] flag = flags.getSystemFlags();
			System.out.println("flags's length: " + flag.length);
			for (int i = 0; i < flag.length; i++) {
				if (flag[i] == Flags.Flag.SEEN) {
					isnew = true;
					System.out.println("seen Message.......");
					break;
				}
			}
		} catch (MessagingException e) {
			Log.error("【判断已/未读出现错误 】：" + e.getMessage());
		}
		return isnew;
	}

	/**
	 * 　　*　解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件
	 * 　　*　主要是根据MimeType类型的不同执行不同的操作，一步一步的解析 　　
	*/
	public void getMailContent(Part part) {
		boolean conname = false;
		try {
			String contenttype = part.getContentType();
			int nameindex = contenttype.indexOf("name");
			if (nameindex != -1)
				conname = true;
			if (part.isMimeType("text/plain") && !conname) {
				bodytext.append((String) part.getContent());
			} else if (part.isMimeType("text/html") && !conname) {
				bodytext.append((String) part.getContent());
			} else if (part.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) part.getContent();
				int counts = multipart.getCount();
				for (int i = 0; i < counts; i++) {
					getMailContent(multipart.getBodyPart(i));
				}
			} else if (part.isMimeType("message/rfc822")) {
				getMailContent((Part) part.getContent());
			} else {
			}
		} catch (Exception e) {
			Log.error("【解析邮件内容出现错误 】：" + e.getMessage());
		}
	}

}
