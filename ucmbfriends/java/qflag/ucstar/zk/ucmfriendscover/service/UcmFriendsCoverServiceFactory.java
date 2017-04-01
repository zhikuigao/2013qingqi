package qflag.ucstar.zk.ucmfriendscover.service;

import org.springframework.transaction.annotation.Transactional;

import qflag.base.service.IBaseService;
import qflag.base.service.factory.IBaseServiceFactory;

@Transactional
public class UcmFriendsCoverServiceFactory implements IBaseServiceFactory {
	
	public UcmFriendsCoverServiceFactory() {
	}

	private IUcmFriendsCoverService ucmFriendsCoverService = null;
	
	public IUcmFriendsCoverService getUcmFriendsCoverService() {
		return ucmFriendsCoverService;
	}
	
	public void setUcmFriendsCoverService(IUcmFriendsCoverService ucmFriendsCoverService) {
		this.ucmFriendsCoverService = ucmFriendsCoverService;
	}
	
	public IBaseService getBaseService() {
		return ucmFriendsCoverService;
	}
}
