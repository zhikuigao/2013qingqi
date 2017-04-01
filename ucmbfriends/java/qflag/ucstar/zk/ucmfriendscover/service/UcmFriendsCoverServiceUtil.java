package qflag.ucstar.zk.ucmfriendscover.service;

import qflag.ucstar.util.SpringFactoryUtil;
import qflag.ucstar.zk.biz.pojo.UcmFriendsCover;

public class UcmFriendsCoverServiceUtil {
	
	private static UcmFriendsCoverServiceFactory factory = null;
	
	public static UcmFriendsCoverServiceFactory getFactory() {
		if(factory == null) {
			factory = new SpringFactoryUtil<UcmFriendsCoverServiceFactory>().getBean(UcmFriendsCover.class.getName()+".factory.impl");
		}
		return factory;
	}
}
