package qflag.ucstar.zk.biz.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import qflag.base.pojo.ZKBaseEntity;

/**
 * @author polarrwl
 */
@Entity
@Table(name = "ucm_friends_share")
public class UcmFriendsShare extends ZKBaseEntity {

	protected String userId = "";

	protected String content = "";

	protected String createtime = "";

	protected String extend2 = "";

	protected String position = "";

	protected String extend1 = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String _userId) {
		this.userId = _userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String _content) {
		this.content = _content;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String _createtime) {
		this.createtime = _createtime;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String _extend2) {
		this.extend2 = _extend2;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String _position) {
		this.position = _position;
	}

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String _extend1) {
		this.extend1 = _extend1;
	}

}
