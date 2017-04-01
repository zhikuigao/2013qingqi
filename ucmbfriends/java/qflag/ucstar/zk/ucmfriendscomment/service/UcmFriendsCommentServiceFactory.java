package qflag.ucstar.zk.ucmfriendscomment.service;

import org.springframework.transaction.annotation.Transactional;

import qflag.base.service.IBaseService;
import qflag.base.service.factory.IBaseServiceFactory;

@Transactional
public class UcmFriendsCommentServiceFactory implements IBaseServiceFactory {
	
	public UcmFriendsCommentServiceFactory() {
	}

	private IUcmFriendsCommentService ucmFriendsCommentService = null;
	
	public IUcmFriendsCommentService getUcmFriendsCommentService() {
		return ucmFriendsCommentService;
	}
	
	public void setUcmFriendsCommentService(IUcmFriendsCommentService ucmFriendsCommentService) {
		this.ucmFriendsCommentService = ucmFriendsCommentService;
	}
	
	public IBaseService getBaseService() {
		return ucmFriendsCommentService;
	}
}
