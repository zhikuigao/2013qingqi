package qflag.ucstar.plugin.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.user.User;
import org.jivesoftware.wildfire.user.UserManager;
import org.qqtech.util.Validator;

import qflag.ucstar.plugin.cache.UcpostCache;
import qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxCofing;
import qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxcofingData;
import qflag.ucstar.plugin.ucpostbox.pojo.UcpostExchangePoji;
import qflag.ucstar.webservice.bean.generated.UcstarUser;


/**
 * @author gzkui 线程1：获取邮件
 */
public class UcpostGetNewMessage implements Runnable {

	public UcpostGetNewMessage() {

	}

	public void run() {
		while (true) {
			Log.console("【outlook集成】: mail check..............");
			Log.info("【outlook集成】: mail check................");
			// 邮箱账号
			String username = "";
			String eamil = "";
			int mailCount = 0;
			Collection<ClientSession> clientSessions = SessionManager.getInstance().getSessions();
			if (clientSessions != null && clientSessions.size()>0) {
				Iterator it = clientSessions.iterator();
				while (it.hasNext()) {
					//POP3Client client = new POP3Client();
					boolean isLogin = false;
					try {
						ClientSession clientSession = (ClientSession) it.next();
						//User us = new User();
						username = clientSession.getUsername();
						eamil = UcpostBoxCofing.getInstance().requestEamil(username);
						if(username.equalsIgnoreCase("admin")){
							Log.debug("【outlook集成】用户admin:" + username + "(账号)");
							continue;
						}
						String pwd = UcpostBoxCofing.getInstance().requestPassword(username);
						if (Validator.isNull(username) || Validator.isNull(pwd)|| "admin".equals(username)) {
							Log.debug("【outlook集成】邮箱出现错误:" + username + "(账号)");
							continue;
						}
						if(eamil == null || eamil.length()<1){
							Log.debug("【outlook集成】无配置邮箱:" + username + "(账号)");
							continue;
						}
						
						// 获取该用户的所有的邮件
						Message[] infos = UcpostBoxcofingData.getInstance().getMailPost(eamil, pwd);
						//Message[] infos = client.listMessages();
						if (infos != null) {
							mailCount = infos.length;
						}
						// 若邮箱中邮件数量为0的话则清空邮箱喝缓存
						if (mailCount == 0) {
							UcpostBoxCofing.getInstance().deleteUserMails(eamil);
							UcpostCache.getInstance().removeExchangeMail(eamil);
						}
						// 邮件处理
						if (mailCount > 0 && infos != null) {
							List<UcpostExchangePoji> addList = new ArrayList<UcpostExchangePoji>();
							List deleteList = new ArrayList();
							//获取所有邮件ID
							List<String> mailIds = UcpostBoxCofing.getInstance().getExchangeMailIds(username);
							deleteList = mailIds;
							 for (int msgNum = 0; msgNum < infos.length; msgNum++) {
								//POP3MessageInfo temp = client.listUniqueIdentifier(info.number);
								 	if(!infos[msgNum].getFolder().isOpen()){
								 		infos[msgNum].getFolder().open(Folder.READ_WRITE);
								 	}
								   if(mailIds.contains(infos[msgNum].getSize() +"")){
						            	deleteList.remove(infos[msgNum].getSize()+"");
						            }else{
						            	UcpostExchangePoji pojo = new UcpostExchangePoji();
										pojo.setMailId(infos[msgNum].getSize()+"");
										pojo.setMailNumber(infos[msgNum].getMessageNumber());
										pojo.setUsername(username);
						            	addList.add(pojo);
						            }
								
							}
							UcpostSendMain.getInstance().addExchangeMail(eamil, addList);
							UcpostSendMain.getInstance().addDeleteMail(eamil, deleteList);
						}

					} catch (Exception e) {
						Log.error("【outlook集成】出现错误:" + e.getMessage());
					} finally {
					}

				}
			}

		}

	}

}
