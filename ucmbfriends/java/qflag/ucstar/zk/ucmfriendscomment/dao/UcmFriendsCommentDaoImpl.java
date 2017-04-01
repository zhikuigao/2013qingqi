package qflag.ucstar.zk.ucmfriendscomment.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import qflag.base.persist.BaseDao;
import qflag.ucstar.zk.biz.pojo.UcmFriendsComment;


@Scope("idspace")
@Component
public class UcmFriendsCommentDaoImpl extends BaseDao<UcmFriendsComment> implements IUcmFriendsCommentDao {
	public UcmFriendsCommentDaoImpl() {
	}
}
