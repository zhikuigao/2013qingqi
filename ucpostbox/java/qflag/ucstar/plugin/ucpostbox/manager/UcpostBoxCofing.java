package qflag.ucstar.plugin.ucpostbox.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.util.Log;

import qflag.ucstar.plugin.cache.UcpostCache;
import qflag.ucstar.plugin.thread.UcpostSendMain;
import qflag.ucstar.plugin.ucpostbox.pojo.UcpostExchangePoji;

/**
 * @author gzkui 数据库操作管理类
 */
public class UcpostBoxCofing {
	private static String _tableName = "ucpostpass";
	private static UcpostBoxCofing instance = null;

	private UcpostBoxCofing() {
		init();
	}

	private void init() {
	}

	public static UcpostBoxCofing getInstance() {
		if (null == instance) {
			synchronized (UcpostBoxCofing.class) {
				instance = new UcpostBoxCofing();
			}
		}
		return instance;
	}

	/*
	 * 根据用户名删除客户的邮件
	 */
	public void deleteUserMails(String _username) {
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = DbConnectionManager.getConnection();
			pstat = conn.prepareStatement("delete from e_exchange_mail where username = ?");
			pstat.setString(1, _username);
			pstat.executeUpdate();
		} catch (SQLException e) {
			Log.error("【outlook集成】删除邮件异常" + e.getMessage());
		} finally {
			DbConnectionManager.closeConnection(pstat, conn);
		}
	}

	/*
	 * 根据list删除
	 */
	public void deleteUserMailsByMailIds(List _mailids) {
		if (_mailids != null && _mailids.size() > 0) {
			Connection conn = null;
			PreparedStatement pstat = null;
			try {
				conn = DbConnectionManager.getConnection();
				pstat = conn
						.prepareStatement("delete from e_exchange_mail where mailid = ?");
				for (Iterator it = _mailids.iterator(); it.hasNext();) {
					Object object = (Object) it.next();
					pstat.setString(1, object.toString());
					pstat.addBatch();
				}
				pstat.executeBatch();
			} catch (SQLException e) {
				Log.error("【outlook集成】删除邮件异常" + e.getMessage());
			} finally {
				DbConnectionManager.closeConnection(pstat, conn);
			}
		}
	}

	/**
	 * 缓存删除邮件1.
	 */
	public void deleteExchangeMails(List _mails) {
		if (_mails != null) {
			Iterator it = _mails.iterator();
			while (it.hasNext()) {
				UcpostExchangePoji exchangeMail = (UcpostExchangePoji) it
						.next();
				if (exchangeMail != null) {
					exchangeMail.delete();
				}
			}
		}
	}

	/**
	 * 缓存删除邮件2
	 */
	public void deleteUserAllMails(String _username) {
		List<UcpostExchangePoji> mails = this.getExchangeMails(_username);
		Iterator<UcpostExchangePoji> it = mails.iterator();
		while (it.hasNext()) {
			UcpostExchangePoji mail = it.next();
			if (mail != null) {
				mail.delete();
			}
		}
	}

	/**
	 * 获取用户名获取邮件
	 */
	@SuppressWarnings("unchecked")
	public List<UcpostExchangePoji> getExchangeMails(String _username) {
		List<UcpostExchangePoji> mails = UcpostCache.getInstance().getExhangeMail(_username);
		if (mails == null) {synchronized (_username.intern()) {
				mails = UcpostCache.getInstance().getExhangeMail(_username);
				if (mails == null) {
					String sql = "from " + UcpostExchangePoji.class.getName()+ " as obj where username = '" + _username + "'";
					mails = UcpostExchangePoji.getObjects(sql);
					if (mails == null) {
						mails = new ArrayList<UcpostExchangePoji>();
					}
					UcpostCache.getInstance().putExchangeMail(_username, mails);
				}
			}
		}
		return mails;
	}

	/**
	 * 获取用户名获取邮件ID
	 */
	public List<String> getExchangeMailIds(String _username) {
		Connection con = null;
		Statement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		List<String> mailIds = new ArrayList<String>();
		//List<UcpostExchangePoji> mails = this.getExchangeMails(_username);
		try {
			sql = "SELECT mailid FROM e_exchange_mail WHERE username = '"+ _username + "'";
			con = DbConnectionManager.getConnection();
			pstmt = con.createStatement();
			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				String pass = rs.getString(1);
				mailIds.add(pass);
			}
			/*
			if (mails != null) {
				Iterator<UcpostExchangePoji> it = mails.iterator();
				while (it.hasNext()) {
					UcpostExchangePoji exchangeMail = it.next();
					if (exchangeMail != null) {
						mailIds.add(exchangeMail.getMailId());
					}
				}
			}  */
		} catch (Exception e) {
			e.getMessage();
		}finally{
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return mailIds;
	}

	/**
	 * 批量插入邮件
	 */
	public void saveUserMail(List _mails) {
		if (_mails != null && _mails.size() > 0) {
			Connection conn = null;
			PreparedStatement pstat = null;
			try {
				conn = DbConnectionManager.getConnection();
				pstat = conn.prepareStatement("insert into e_exchange_mail(uri,version,flags,username,mailid,mailnumber,isnewmail) values (?,?,?,?,?,?,?)");
				for (Iterator it = _mails.iterator(); it.hasNext();) {
					UcpostExchangePoji exchangeMail = (UcpostExchangePoji) it
							.next();
					pstat.setString(1, exchangeMail.getMailId());
					pstat.setInt(2, 0);
					pstat.setInt(3, 0);
					pstat.setString(4, exchangeMail.getUsername());
					pstat.setString(5, exchangeMail.getMailId());
					pstat.setInt(6, exchangeMail.getMailNumber());
					pstat.setInt(7, exchangeMail.getIsNewMail());
					pstat.addBatch();
				}
				pstat.executeBatch();
			} catch (SQLException e) {
				Log.error(e);
			} finally {
				DbConnectionManager.closeConnection(pstat, conn);
			}
		}
	}

	/*
	 * 插入邮件
	 */
	public void addExchangeMails(List _mails) {
		if (_mails != null) {
			Iterator it = _mails.iterator();
			while (it.hasNext()) {
				UcpostExchangePoji exchangeMail = (UcpostExchangePoji) it
						.next();
				if (exchangeMail != null) {
					exchangeMail.save();
				}
			}
		}
	}

	/*
	 * 保存账户密码
	 */
	public void insterPassword(String _username, String _password) {
		Connection con = null;
		Statement pstmt = null;
		try {
			String sql = "insert into e_exchange_savepass(username,password)values('"
					+ _username + "','" + _password + "')";
			con = DbConnectionManager.getConnection();
			pstmt = con.createStatement();
			pstmt.executeUpdate(sql);
		} catch (Exception e) {
			Log.error("【outlook集成】添加密码异常" + e.getMessage());
		} finally {
			DbConnectionManager.closeConnection(pstmt, con);
		}
	}

	/*
	 * 获取用户密码
	 */
	public String requestPassword(String _username) {
		String pass = "";
		pass = UcpostSendMain.getInstance().getUserPwd(_username);
		if (pass == null || pass.length() < 1) {
			Connection con = null;
			Statement pstmt = null;
			ResultSet rs = null;
			String sql = "";
			try {
				sql = "SELECT PASSWORD FROM e_exchange_savepass WHERE username = '"+ _username + "'";
				con = DbConnectionManager.getConnection();
				pstmt = con.createStatement();
				rs = pstmt.executeQuery(sql);
				if (rs.next()) {
					pass = rs.getString(1);
				}
				UcpostSendMain.getInstance().addUserPwd(_username, pass);
			} catch (Exception e) {
				Log.error("【outlook集成】获取密码异常" + e.getMessage());
			} finally {
				DbConnectionManager.closeConnection(rs, pstmt, con);
			}
		}
		return pass;
	}

	/*
	 * 取用户邮箱
	 */
	public String requestEamil(String _username) {
		Connection con = null;
		Statement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String eamil = UcpostSendMain.getInstance().getUserEamil(_username);
		if(eamil == null || eamil.length()<0){
		try {
			sql = "SELECT email FROM jiveuser WHERE username = '" + _username+ "'";
			con = DbConnectionManager.getConnection();
			pstmt = con.createStatement();
			rs = pstmt.executeQuery(sql);
			if (rs.next()) {
				eamil = rs.getString(1);
			}
			UcpostSendMain.getInstance().addEamil(_username, eamil);
		} catch (Exception e) {
			Log.error("【outlook集成】获取密码异常" + e.getMessage());
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		}
		return eamil;
	}
}
