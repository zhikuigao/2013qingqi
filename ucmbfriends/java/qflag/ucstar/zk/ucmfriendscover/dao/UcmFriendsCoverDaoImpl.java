package qflag.ucstar.zk.ucmfriendscover.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import qflag.base.persist.BaseDao;
import qflag.ucstar.zk.biz.pojo.UcmFriendsCover;


@Scope("idspace")
@Component
public class UcmFriendsCoverDaoImpl extends BaseDao<UcmFriendsCover> implements IUcmFriendsCoverDao {
	public UcmFriendsCoverDaoImpl() {
	}
}
