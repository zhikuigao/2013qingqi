package qflag.ucstar.zk.ucmfriendsshare.service;

import java.util.List;

import javax.annotation.Resource;
import qflag.base.service.IBaseService;
import qflag.ucstar.zk.biz.pojo.UcmFriendsResource;

public interface IUcmFriendsShareService extends IBaseService {
	public boolean getImagedel(String fkshare);
	public UcmFriendsResource getObjectShare(String share);
	public boolean getCommDelete(String fkshare);
	
	}
