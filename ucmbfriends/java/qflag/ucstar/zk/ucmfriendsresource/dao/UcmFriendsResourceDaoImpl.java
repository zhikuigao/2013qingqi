package qflag.ucstar.zk.ucmfriendsresource.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import qflag.base.persist.BaseDao;
import qflag.ucstar.zk.biz.pojo.UcmFriendsResource;


@Scope("idspace")
@Component
public class UcmFriendsResourceDaoImpl extends BaseDao<UcmFriendsResource> implements IUcmFriendsResourceDao {
	public UcmFriendsResourceDaoImpl() {
	}
}
