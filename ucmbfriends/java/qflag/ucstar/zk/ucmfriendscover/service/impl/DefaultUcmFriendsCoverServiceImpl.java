package qflag.ucstar.zk.ucmfriendscover.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.util.Log;
import org.apache.log4j.Logger;
import qflag.ucstar.cache.UcstarEHCache;
import qflag.base.cache.UcstarZKCacheManager;
import qflag.base.exception.UcstarPersistException;
import qflag.base.persist.BaseDao;
import qflag.base.zk.annot.BasePage;
import qflag.ucstar.zk.biz.pojo.UcmFriendsCover;
import qflag.ucstar.zk.ucmfriendscover.service.IUcmFriendsCoverService;

@Transactional
public class DefaultUcmFriendsCoverServiceImpl implements IUcmFriendsCoverService {
	private static Logger log = Logger.getLogger(DefaultUcmFriendsCoverServiceImpl.class);
	protected UcstarEHCache cache = null;
	public DefaultUcmFriendsCoverServiceImpl() {
		cache = UcstarZKCacheManager.getInstance().getEhCache(UcmFriendsCover.class);
	}
	
	private BaseDao ucmFriendsCoverDao = null;
	
	public BaseDao getUcmFriendsCoverDao() {
		return ucmFriendsCoverDao;
	}

	public void setUcmFriendsCoverDao(BaseDao ucmFriendsCoverDao) {
		this.ucmFriendsCoverDao = ucmFriendsCoverDao;
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void delete(String value, Class entityClass) throws UcstarPersistException {
		ucmFriendsCoverDao.delete(value, entityClass);
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
							result = ucmFriendsCoverDao.getObject(clazz, id);
							cache.put(""+id, result);
						} catch (UcstarPersistException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		} else {
			try {
				result = ucmFriendsCoverDao.getObject(clazz, id);
			} catch (UcstarPersistException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public long getObjectCount(String query) {
		return ucmFriendsCoverDao.getObjectCount(query);
	}

	public List getObjects(String query, BasePage _page) {
		return ucmFriendsCoverDao.getObjects(query, _page);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void saveObject(Object t) throws UcstarPersistException {
		ucmFriendsCoverDao.saveObject(t);
	}
	
	//获取封面数据
	public UcmFriendsCover getVersionWork(String userId, String version){
		String sql = "select * from ucm_friends_cover where userId = '"+userId+"' ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ver = -1;
		String fileid ="";
		String str = "";
		String filename = "";
		UcmFriendsCover ur =  new UcmFriendsCover();
		/**
		 * 1、数据库没有版本号   1无法查找封面数据
		 * 2、传递过来的值比数据库的还新   最新  1
		 * 3、数据库版本号跟传递过来的一致  1、已是最新  
		 * 4、传递过来的值比数据库的还旧 0  给他传封面ID
		 */
		try {
			int veron = Integer.parseInt(version);
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				ver = rs.getInt("version");
				str = rs.getString("fileId");
				filename = rs.getString("extend1");
			}
			if(ver>veron){
				ur.setFileId(str);
				ur.setExtend1(filename);
			}
		} catch (Exception e) {
			Log.error("【工作圈:获取封面数据】<<<<<<<<<<<<<<<<<(1031)获取封面数据出现程序错误");
		}finally{
			DbConnectionManager.closeConnection(rs, ps, conn);
		}
		return ur;
	}
	
	
	
	//判断用户是否存在
	public boolean isuserExtis(String userId){
		boolean isTrue =  false; 
		String sql = "select count(*) from ucm_friends_cover where userId = '"+userId+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		String test = "";
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				 count = rs.getInt(1);
			}
			if(count>0){
				isTrue = true;
			}
		} catch (Exception e) {
			Log.error("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)判断用户是否存在出现程序错误");
		} finally{
			DbConnectionManager.closeConnection(rs, ps, conn);
		}
		return isTrue;
	}
	
	//更改版本号
	public boolean updateVer(String fileid){
		boolean isTrue = true; 
		String sql = "update ucm_friends_cover SET VERSION = VERSION +1 where fileId = '"+fileid+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);  
			ps.executeUpdate(); 
			isTrue = false;
		} catch (Exception e) {
			Log.error("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)更改版本号出现程序错误");
		} finally{
			DbConnectionManager.closeConnection(ps, conn);
		}
		return isTrue;
		
	}
	
	//查询版本号
	public String getVersion(String fileid){
		String sql = "select * from ucm_friends_cover where fileid = '"+fileid+"' ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String str = "";
		
		try {
			conn = DbConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				str = rs.getString("version");
			}
		} catch (Exception e) {
			Log.error("<<<<<<workroil"+e);
		}finally{
			DbConnectionManager.closeConnection(rs, ps, conn);
		}
		return str;
	}
	
	
}
