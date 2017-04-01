package qflag.ucstar.zk.ucmfriendsshare.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import qflag.base.persist.BaseDao;
import qflag.ucstar.zk.biz.pojo.UcmFriendsShare;


@Scope("idspace")
@Component
public class UcmFriendsShareDaoImpl extends BaseDao<UcmFriendsShare> implements IUcmFriendsShareDao {
	public UcmFriendsShareDaoImpl() {
	}
}
