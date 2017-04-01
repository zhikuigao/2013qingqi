package qflag.ucstar.plugin.ucpostbox.manager;

import java.security.Security;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;

public class UcpostBoxcofingData {
	private Message[] msg = null;
	private static UcpostBoxcofingData instance = null;
	public static String adsynchronizeddatabases = "1"; // 默认为完全清空组织架构
	public static String passwordset = "1";// 默认导入用户密码为原始值

	private UcpostBoxcofingData() {
		_init();
	}

	private void _init() {

	}

	public synchronized static UcpostBoxcofingData getInstance() {
		if (instance == null) {
			instance = new UcpostBoxcofingData();
		}
		return instance;
	}

	/**
	 * 是否启用配置信息
	 */
	public void setStatue(String _statue) {
		String statue = "0";
		if (_statue != null && _statue.length() > 0) {
			statue = _statue;
		}
		JiveGlobals.setProperty("xmpp.import.wujixin.shedulestaue", statue);
	}

	/**
	 * 邮箱服务器地址
	 */
	public void setMail(String _mial) {

		JiveGlobals.setProperty("szgs.sycdata.wujixin.mail", _mial);
	}

	/*
	 * 邮箱地址
	 */

	public void setUrlmail(String urlmail) {
		if (urlmail != null && urlmail.length() > 0) {
			JiveGlobals.setProperty("szgs.sycdata.wujixin.urlmail", urlmail);
		}
		
	}

	/*
	 * 邮箱密码
	 */
	public void setPass(String pass) {
		if (pass != null && pass.length() > 0) {
			JiveGlobals.setProperty("szgs.sycdata.wujixin.pass", pass);
		}

	}
	public Message[] getMailPost(String _username, String _pwd) {
		Store store = null;
		Folder folder = null;
		try {
			//邮件连接配置
			Security.setProperty("ssl.SocketFactory.provider","qflag.ucstar.plugin.ucpostbox.ssl.DummySSLSocketFactory");
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = new Properties();
			props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.pop3.socketFactory.fallback", "false");
			props.setProperty("mail.pop3.port", "995");
			props.setProperty("mail.pop3.socketFactory.port", "995");
	
			//connect......
			Session session = Session.getInstance(props);		
			URLName urln = new URLName("pop3",JiveGlobals.getProperty("ucpost.mail.host.url", "mail.infinitus-int.com"),995,null,_username, _pwd);
			store = session.getStore(urln);
			store.connect();
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			msg = folder.getMessages();
			
		} catch (Exception e) {
			Log.debug("【outlook集成】获取邮箱出现错误");
		} finally{
			try {
				 folder.close(false);
				 store.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
			
		}
		return msg;
	}

}
