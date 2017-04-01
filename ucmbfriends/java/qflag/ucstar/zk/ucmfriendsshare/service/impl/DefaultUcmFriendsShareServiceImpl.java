package qflag.ucstar.zk.ucmfriendsshare.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.apache.log4j.Logger;
import qflag.ucstar.cache.UcstarEHCache;
import qflag.base.cache.UcstarZKCacheManager;
import qflag.base.exception.UcstarPersistException;
import qflag.base.persist.BaseDao;
import qflag.base.zk.annot.BasePage;
import qflag.ucstar.zk.biz.pojo.UcmFriendsResource;
import qflag.ucstar.zk.biz.pojo.UcmFriendsShare;
import qflag.ucstar.zk.ucmfriendsshare.service.IUcmFriendsShareService;

@Transactional
public class DefaultUcmFriendsShareServiceImpl implements IUcmFriendsShareService {
	private static Logger log = Logger.getLogger(DefaultUcmFriendsShareServiceImpl.class);
	protected UcstarEHCache cache = null;
	public DefaultUcmFriendsShareServiceImpl() {
		cache = UcstarZKCacheManager.getInstance().getEhCache(UcmFriendsShare.class);
	}
	
	private BaseDao ucmFriendsShareDao = null;
	
	public BaseDao getUcmFriendsShareDao() {
		return ucmFriendsShareDao;
	}

	public void setUcmFriendsShareDao(BaseDao ucmFriendsShareDao) {
		this.ucmFriendsShareDao = ucmFriendsShareDao;
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void delete(String value, Class entityClass) throws UcstarPersistException {
		ucmFriendsShareDao.delete(value, entityClass);
		if(cache != null) {
			cache.remove(value);
		}
	}

	public Object getObject(Class clazz, Serializable id) throws UcstarPersistException {
		
		Object result = null;
		if(cache != null) {
			result = cache.get(""+id);
			if(result == null) {
				synchronized (id) {
					if(result == null) {
						try {
							result = ucmFriendsShareDao.getObject(clazz, id);
							cache.put(""+id, result);
						} catch (UcstarPersistException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		} else {
			try {
				result = ucmFriendsShareDao.getObject(clazz, id);
			} catch (UcstarPersistException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public long getObjectCount(String query) {
		return ucmFriendsShareDao.getObjectCount(query);
	}

	public List getObjects(String query, BasePage _page) {
		return ucmFriendsShareDao.getObjects(query, _page);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void saveObject(Object t) throws UcstarPersistException {
		ucmFriendsShareDao.saveObject(t);
	}
	//删除图片
	public boolean getImagedel(String fkshare){
		boolean isTrue =  false; 
		String sql = "DELETE FROM ucm_friends_resource WHERE shareUri = '"+fkshare+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);  
			ps.executeUpdate(); 
			isTrue = true;
		} catch (Exception e) {
			isTrue = false;
			Log.debug("【工作圈:删除图片】<<<<<<<<<<<<<<<<<"+e);
		} finally{
			DbConnectionManager.closeConnection(ps, conn);
		}
		return isTrue;
	}
	
	//评论
	public boolean getCommDelete(String fkshare){
		boolean isTrue =  false; 
		String sql = "DELETE FROM ucm_friends_comment WHERE shareUri = '"+fkshare+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);  
			ps.executeUpdate(); 
			isTrue = true;
		} catch (Exception e) {
			isTrue = false;
			Log.debug("【工作圈:删除评论】<<<<<<<<<<<<<<<<<"+e);
		} finally{
			DbConnectionManager.closeConnection(ps, conn);
		}
		return isTrue;
	}
	
	
	
	public UcmFriendsResource getObjectShare(String share){
		String sql = "select * from ucm_friends_resource where shareUri = '"+share+"' ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//str2/str3 为大图小图 ID
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String str = "";
		UcmFriendsResource ur = new UcmFriendsResource();
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				str1 = rs.getString("fileId");
				str3 = rs.getString("fileName");
				if(!JiveGlobals.isEmpty(str1)){
					if(JiveGlobals.isEmpty(str3) || str3.length()<4){
						str = ".jpg";
					}else{
						str = str3.substring(str3.length()-4, str3.length());
					}
					str2 += "{"+ "\"BIG\":"+ "\""+str1+"\","+ "\"BIGFILENAME\":"+ "\""+str3+"\"" +","+ "\"SMALLFILENAME\":"+ "\""+str1+"_samll"+str+"\"" +"},";
				}
			}
			ur.setExtend1(str2.substring(0, str2.length()-1));
		} catch (Exception e) {
			Log.debug("【工作圈:组装图片ID】<<<<<<<<<<<<<<<<<"+e);
		}finally{
			DbConnectionManager.closeConnection(rs, ps, conn);
		}
		return ur;
	 }
		
	
	
	
}
