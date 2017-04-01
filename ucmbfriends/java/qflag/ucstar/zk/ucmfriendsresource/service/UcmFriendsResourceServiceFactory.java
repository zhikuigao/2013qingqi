package qflag.ucstar.zk.ucmfriendsresource.service;

import org.springframework.transaction.annotation.Transactional;

import qflag.base.service.IBaseService;
import qflag.base.service.factory.IBaseServiceFactory;

@Transactional
public class UcmFriendsResourceServiceFactory implements IBaseServiceFactory {
	
	public UcmFriendsResourceServiceFactory() {
	}

	private IUcmFriendsResourceService ucmFriendsResourceService = null;
	
	public IUcmFriendsResourceService getUcmFriendsResourceService() {
		return ucmFriendsResourceService;
	}
	
	public void setUcmFriendsResourceService(IUcmFriendsResourceService ucmFriendsResourceService) {
		this.ucmFriendsResourceService = ucmFriendsResourceService;
	}
	
	public IBaseService getBaseService() {
		return ucmFriendsResourceService;
	}
}
