package qflag.ucstar.zk.ucmfriendscomment.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import qflag.ucstar.cache.UcstarEHCache;
import qflag.base.cache.UcstarZKCacheManager;
import qflag.base.exception.UcstarPersistException;
import qflag.base.persist.BaseDao;
import qflag.base.zk.annot.BasePage;
import qflag.ucstar.zk.biz.pojo.UcmFriendsComment;
import qflag.ucstar.zk.ucmfriendscomment.service.IUcmFriendsCommentService;

@Transactional
public class DefaultUcmFriendsCommentServiceImpl implements IUcmFriendsCommentService {
	private static Logger log = Logger.getLogger(DefaultUcmFriendsCommentServiceImpl.class);
	protected UcstarEHCache cache = null;
	public DefaultUcmFriendsCommentServiceImpl() {
		cache = UcstarZKCacheManager.getInstance().getEhCache(UcmFriendsComment.class);
	}
	
	private BaseDao ucmFriendsCommentDao = null;
	
	public BaseDao getUcmFriendsCommentDao() {
		return ucmFriendsCommentDao;
	}

	public void setUcmFriendsCommentDao(BaseDao ucmFriendsCommentDao) {
		this.ucmFriendsCommentDao = ucmFriendsCommentDao;
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void delete(String value, Class entityClass) throws UcstarPersistException {
		ucmFriendsCommentDao.delete(value, entityClass);
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
							result = ucmFriendsCommentDao.getObject(clazz, id);
							cache.put(""+id, result);
						} catch (UcstarPersistException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		} else {
			try {
				result = ucmFriendsCommentDao.getObject(clazz, id);
			} catch (UcstarPersistException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public long getObjectCount(String query) {
		return ucmFriendsCommentDao.getObjectCount(query);
	}

	public List getObjects(String query, BasePage _page) {
		return ucmFriendsCommentDao.getObjects(query, _page);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void saveObject(Object t) throws UcstarPersistException {
		ucmFriendsCommentDao.saveObject(t);
	}
	
		
	
}
