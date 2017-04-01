package qflag.ucstar.zk.ucmfriendscomment.service;

import qflag.ucstar.util.SpringFactoryUtil;
import qflag.ucstar.zk.biz.pojo.UcmFriendsComment;

public class UcmFriendsCommentServiceUtil {
	
	private static UcmFriendsCommentServiceFactory factory = null;
	
	public static UcmFriendsCommentServiceFactory getFactory() {
		if(factory == null) {
			factory = new SpringFactoryUtil<UcmFriendsCommentServiceFactory>().getBean(UcmFriendsComment.class.getName()+".factory.impl");
		}
		return factory;
	}
}
