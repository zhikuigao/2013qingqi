package qflag.ucstar.plugin.thread;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.auth.AuthFactory;
import org.jivesoftware.wildfire.user.User;
import org.jivesoftware.wildfire.user.UserManager;
import org.qqtech.util.Validator;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
/**
 * 线程2：发送邮件消息
 * Validator
 */

import qflag.ucstar.plugin.cache.UcpostCache;
import qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxCofing;
import qflag.ucstar.plugin.ucpostbox.pojo.UcpostExchangePoji;

public class UcpostSendMain {
	private static UcpostSendMain instance = null;
	private Map<String, String> userPwds = null;
	private Map<String, String> userEamil = null;
	private Map<String, List<UcpostExchangePoji>> exchangeMails = null;
	private Map<String, List> deleteMails = null;

	private UcpostSendMain() {
		_init();
	}

	private void _init() {
		userPwds = new ConcurrentHashMap<String, String>();
		exchangeMails = new ConcurrentHashMap<String, List<UcpostExchangePoji>>();
		deleteMails = new ConcurrentHashMap<String, List>();
		userEamil = new ConcurrentHashMap<String, String>();
	}

	public static UcpostSendMain getInstance() {
		if (instance == null) {
			synchronized (UcpostSendMain.class) {
				if (instance == null) {
					instance = new UcpostSendMain();
				}
			}
		}
		return instance;
	}

	public void startCheckUnreadMail() {
		new Thread(new GetUnreadMailCountMysoftThread()).start();
	}

	public String getUserEamil(String username) {
		if (this.userEamil.size() > 0) {
			return this.userEamil.get(username);
		}
		return null;
	}

	public void addEamil(String username, String userEamil) {
		this.userEamil.put(username, userEamil);
	}
	public String getUserPwd(String username) {
		if (this.userPwds.size() > 0) {
			return this.userPwds.get(username);
		}
		return null;
	}

	public void addUserPwd(String username, String password) {
		this.userPwds.put(username, password);
	}

	public void addExchangeMail(String _username,
			List<UcpostExchangePoji> _mails) {
		this.exchangeMails.put(_username, _mails);
	}

	public List<UcpostExchangePoji> removeExchnageMail(String _username) {
		return this.exchangeMails.remove(_username);
	}

	public void addDeleteMail(String _username, List _mailids) {
		this.deleteMails.put(_username, _mailids);
	}

	public List removeDeleteMail(String _username) {
		return this.deleteMails.remove(_username);
	}
}

class GetUnreadMailCountMysoftThread implements Runnable {

	public void run() {
		while (true) {
			try {
				Log.console("【outlook集成】: mail message check..............");
				Log.info("【outlook集成】: mail message check................");
				Collection<ClientSession> clientSessions = SessionManager.getInstance().getSessions();
				if (clientSessions != null && clientSessions.size()>0) {
					Iterator it = clientSessions.iterator();
					while (it.hasNext()) {
						try {
							ClientSession clientSession = (ClientSession) it.next();
							String name = clientSession.getUsername();
							String username = UcpostBoxCofing.getInstance().requestEamil(name);
							//String password = UcpostBoxCofing.getInstance().requestPassword(name);
							//User user = UserManager.getInstance().getUser(username);
							if(username.equalsIgnoreCase("admin")){
								Log.debug("【outlook集成】用户admin:" + username + "(账号)");
								continue;
							}
							/*
							if (password == null || password.length() == 0) {
								String pwd = user.getProperties().get(username + "_mysoft");
								if (pwd != null && pwd.length() > 0) {
									password = JiveGlobals.decordByDES(pwd);
								}
							}*/
							if (Validator.isNull(username)||  "admin".equals(username)) {
								continue;
							}
							// 获取新增的邮件
							List<UcpostExchangePoji> addMails = UcpostSendMain.getInstance().removeExchnageMail(username);
							// 获取需要被删除的邮件
							List deleteMails = UcpostSendMain.getInstance().removeDeleteMail(username);

							UcpostBoxCofing.getInstance().deleteUserMailsByMailIds(deleteMails);
							UcpostBoxCofing.getInstance().saveUserMail(addMails);
							if ((addMails != null && addMails.size() > 0)|| (deleteMails != null && deleteMails.size() > 0)) {
								UcpostCache.getInstance().removeExchangeMail(username);
								//UcpostBoxCofing.getInstance().getExchangeMails(username);
							}
							if (addMails != null) {
								String msgContent = "";
								if (addMails.size() > 0) {
									msgContent = "您有" + addMails.size()+ "封新邮件！";
								}
								if (addMails.size() > 0&& !msgContent.equals("")) {
									Message msg = new Message();
									msg.setType(Type.system);
									msg.setScope("1");
									msg.setResource("Mail");
									msg.setFrom(XMPPServer.getInstance().getServerInfo().getName());
									msg.setTo(XMPPServer.getInstance().createJID(name, null));
									msg.setBody(msgContent);

									XMPPServer.getInstance().getMessageRouter().route(msg);
								}
							}
							IQ iq = new IQ();
							Element responseElement = DocumentHelper.createElement(QName.get("x","jabber:iq:mailreceive"));
							iq.setType(IQ.Type.result);
							JID userJid = XMPPServer.getInstance().createJID(name, null);
							iq.setTo(userJid);
							responseElement.addElement("count").addText(addMails == null ? "" + 0 : addMails.size()+ "");
							iq.setChildElement(responseElement);
							clientSession.process(iq);

						} catch (Exception e) {
							Log.error("【outlook集成】出现错误:" + e.getMessage());
						}
					}
				}
				Thread.sleep(Long.parseLong(JiveGlobals.getProperty("mysoft.mail.sendmessage.sleeptime", "" + 60 * 1000)));
			} catch (Exception e) {
				Log.error("【outlook集成】出现错误:" + e.getMessage());
			}
		}

	}

}
