package qflag.ucstar.zk.biz.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import qflag.base.pojo.ZKBaseEntity;

/**
 * @author polarrwl
 */
@Entity
@Table(name = "ucm_friends_cover")
public class UcmFriendsCover extends ZKBaseEntity {

	protected String userId = "";

	protected String fileId = "";

	protected String extend1 = "";

	protected String extend2 = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String _userId) {
		this.userId = _userId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String _fileId) {
		this.fileId = _fileId;
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
