package qflag.ucstar.zk.ucmfriendsresource.service;

import qflag.ucstar.util.SpringFactoryUtil;
import qflag.ucstar.zk.biz.pojo.UcmFriendsResource;

public class UcmFriendsResourceServiceUtil {
	
	private static UcmFriendsResourceServiceFactory factory = null;
	
	public static UcmFriendsResourceServiceFactory getFactory() {
		if(factory == null) {
			factory = new SpringFactoryUtil<UcmFriendsResourceServiceFactory>().getBean(UcmFriendsResource.class.getName()+".factory.impl");
		}
		return factory;
	}
}
