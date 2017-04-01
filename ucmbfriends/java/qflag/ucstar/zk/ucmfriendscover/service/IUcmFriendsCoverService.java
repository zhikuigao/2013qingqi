package qflag.ucstar.zk.ucmfriendscover.service;

import java.util.List;

import javax.annotation.Resource;
import qflag.base.service.IBaseService;
import qflag.ucstar.zk.biz.pojo.UcmFriendsCover;

public interface IUcmFriendsCoverService extends IBaseService {
		public UcmFriendsCover getVersionWork(String userId, String version);
		public boolean isuserExtis(String userid);
		public boolean updateVer(String fileid);
		public String getVersion(String fileid);
	}
