package qflag.ucstar.zk.ucmfriendsshare.service;

import org.springframework.transaction.annotation.Transactional;

import qflag.base.service.IBaseService;
import qflag.base.service.factory.IBaseServiceFactory;

@Transactional
public class UcmFriendsShareServiceFactory implements IBaseServiceFactory {
	
	public UcmFriendsShareServiceFactory() {
	}

	private IUcmFriendsShareService ucmFriendsShareService = null;
	
	public IUcmFriendsShareService getUcmFriendsShareService() {
		return ucmFriendsShareService;
	}
	
	public void setUcmFriendsShareService(IUcmFriendsShareService ucmFriendsShareService) {
		this.ucmFriendsShareService = ucmFriendsShareService;
	}
	
	public IBaseService getBaseService() {
		return ucmFriendsShareService;
	}
}
