package qflag.ucstar.plugin.ucpostbox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;

import qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxCofing;
import qflag.ucstar.plugin.ucpostbox.pojo.Ucpostboxbase;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * @author gzkui
 */
public class UcpostboxreciveMail {
	private static UcpostboxreciveMail instance = null;
	private List<Ucpostboxbase> postList = new ArrayList<Ucpostboxbase>();// 用来储存邮件信息

	private UcpostboxreciveMail() {
		init();
	}

	private void init() {
	}

	public static UcpostboxreciveMail getInstance() {
		if (null == instance) {
			synchronized (UcpostBoxCofing.class) {
				instance = new UcpostboxreciveMail();
			}
		}
		return instance;
	}

	public List<Ucpostboxbase> getOneMail() {
		//String imapserver = "imap.164.com";
		//String user = "18565741311@163.com";
		//String pwd = "360431gzk";
		String imapserver = JiveGlobals.getProperty("szgs.sycdata.wujixin.mail");
		String user = JiveGlobals.getProperty("szgs.sycdata.wujixin.urlmail");
		String pwd = JiveGlobals.getProperty("szgs.sycdata.wujixin.pass");
		Properties prop = System.getProperties();
		prop.put("mail.imap.host", imapserver);
		prop.put("mail.imap.auth.plain.disable", "true");
		Session mailsession = Session.getInstance(prop, null);
		mailsession.setDebug(false); // 是否启用debug模式
		Ucpostboxbase ub = new Ucpostboxbase();
		try {
			IMAPStore store = (IMAPStore) mailsession.getStore("imap");
			store.connect(imapserver, user, pwd);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message message[] = folder.getMessages();
			UcpostReciveOneMail pmm = null;
			for (int msgNum = 0; msgNum < message.length; msgNum++) {
				pmm = new UcpostReciveOneMail((MimeMessage) message[msgNum]);
				ub.setEamilSubject(pmm.getSubject());
				ub.setEamilFrom(pmm.getFrom());
				ub.setEamilSentdate(pmm.getSentDate());
				ub.setCountMax(message.length);
				ub.setCountUnread(folder.getUnreadMessageCount());
				ub.setCountNew(folder.getNewMessageCount());
				pmm.getMailContent((Part) message[msgNum]);
				ub.setEamilContent(pmm.getBodyText());
				postList.add(ub);
			}
			folder.close(false);
			store.close();

		} catch (MessagingException e) {
			e.getMessage();
			Log.error("【请检查用户名/密码/邮件服务器是否正确】：" + e.getMessage());
		} catch (Exception e3) {
			e3.getMessage();
			Log.error("【邮件收取出现错误】：" + e3.getMessage());
		}
		return postList;
	}
}
