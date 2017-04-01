package qflag.ucstar.zk.biz.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import qflag.base.pojo.ZKBaseEntity;

/**
 * @author polarrwl
 */
@Entity
@Table(name = "ucm_friends_resource")
public class UcmFriendsResource extends ZKBaseEntity {

	protected String shareUri = "";

	protected String fileId = "";

	protected String fileName = "";

	protected String fileSize = "";

	protected String fileExt = "";

	protected String fileType = "";

	protected String createtime = "";

	protected String modifytime = "";

	protected String location = "";

	protected String extend1 = "";

	protected String extend2 = "";

	public String getShareUri() {
		return shareUri;
	}

	public void setShareUri(String _shareUri) {
		this.shareUri = _shareUri;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String _fileId) {
		this.fileId = _fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String _fileName) {
		this.fileName = _fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String _fileSize) {
		this.fileSize = _fileSize;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String _fileExt) {
		this.fileExt = _fileExt;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String _fileType) {
		this.fileType = _fileType;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String _createtime) {
		this.createtime = _createtime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String _modifytime) {
		this.modifytime = _modifytime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String _location) {
		this.location = _location;
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
