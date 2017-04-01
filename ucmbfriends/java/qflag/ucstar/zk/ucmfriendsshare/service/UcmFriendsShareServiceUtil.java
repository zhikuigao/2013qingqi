package qflag.ucstar.zk.ucmfriendsshare.service;

import qflag.ucstar.util.SpringFactoryUtil;
import qflag.ucstar.zk.biz.pojo.UcmFriendsShare;

public class UcmFriendsShareServiceUtil {
	
	private static UcmFriendsShareServiceFactory factory = null;
	
	public static UcmFriendsShareServiceFactory getFactory() {
		if(factory == null) {
			factory = new SpringFactoryUtil<UcmFriendsShareServiceFactory>().getBean(UcmFriendsShare.class.getName()+".factory.impl");
		}
		return factory;
	}
}
