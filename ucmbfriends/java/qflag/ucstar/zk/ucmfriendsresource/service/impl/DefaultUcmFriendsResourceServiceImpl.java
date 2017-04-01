package qflag.ucstar.zk.ucmfriendsresource.service.impl;

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
import qflag.ucstar.zk.biz.pojo.UcmFriendsResource;
import qflag.ucstar.zk.ucmfriendsresource.service.IUcmFriendsResourceService;

@Transactional
public class DefaultUcmFriendsResourceServiceImpl implements IUcmFriendsResourceService {
	private static Logger log = Logger.getLogger(DefaultUcmFriendsResourceServiceImpl.class);
	protected UcstarEHCache cache = null;
	public DefaultUcmFriendsResourceServiceImpl() {
		cache = UcstarZKCacheManager.getInstance().getEhCache(UcmFriendsResource.class);
	}
	
	private BaseDao ucmFriendsResourceDao = null;
	
	public BaseDao getUcmFriendsResourceDao() {
		return ucmFriendsResourceDao;
	}

	public void setUcmFriendsResourceDao(BaseDao ucmFriendsResourceDao) {
		this.ucmFriendsResourceDao = ucmFriendsResourceDao;
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void delete(String value, Class entityClass) throws UcstarPersistException {
		ucmFriendsResourceDao.delete(value, entityClass);
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
							result = ucmFriendsResourceDao.getObject(clazz, id);
							cache.put(""+id, result);
						} catch (UcstarPersistException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		} else {
			try {
				result = ucmFriendsResourceDao.getObject(clazz, id);
			} catch (UcstarPersistException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public long getObjectCount(String query) {
		return ucmFriendsResourceDao.getObjectCount(query);
	}

	public List getObjects(String query, BasePage _page) {
		return ucmFriendsResourceDao.getObjects(query, _page);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void saveObject(Object t) throws UcstarPersistException {
		ucmFriendsResourceDao.saveObject(t);
	}
	
		
	
}
