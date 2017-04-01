package qflag.ucstar.zk.biz.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import qflag.base.pojo.ZKBaseEntity;

/**
 * @author polarrwl
 */
@Entity
@Table(name = "ucm_friends_comment")
public class UcmFriendsComment extends ZKBaseEntity {

	protected String shareUri = "";

	protected String fromUserId = "";

	protected String toUserId = "";

	protected String createtime = "";

	protected String content = "";

	protected String commentType = "";

	protected String extend1 = "";

	protected String extend2 = "";

	public String getShareUri() {
		return shareUri;
	}

	public void setShareUri(String _shareUri) {
		this.shareUri = _shareUri;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String _fromUserId) {
		this.fromUserId = _fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String _toUserId) {
		this.toUserId = _toUserId;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String _createtime) {
		this.createtime = _createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String _content) {
		this.content = _content;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String _commentType) {
		this.commentType = _commentType;
	}

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String _extend1) {
		this.extend1 = _extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String _extend2) {
		this.extend2 = _extend2;
	}

}
