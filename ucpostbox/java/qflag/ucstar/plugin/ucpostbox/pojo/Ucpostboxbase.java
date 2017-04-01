package qflag.ucstar.plugin.ucpostbox.pojo;

/**
 * @author gzkui 邮件基本信息pojo类
 */
public class Ucpostboxbase {

	private String eamilSubject = ""; // 主题
	private String eamilFrom = ""; // 发件人
	private String eamilSentdate = ""; // 日期
	private String eamilContent = ""; // 内容
	private int countMax = 0; // 邮件总数量
	private int countUnread = 0; // 未读数量
	private int countNew = 0; // 新邮件数量

	public String getEamilSubject() {
		return eamilSubject;
	}

	public void setEamilSubject(String eamilSubject) {
		this.eamilSubject = eamilSubject;
	}

	public String getEamilFrom() {
		return eamilFrom;
	}

	public void setEamilFrom(String eamilFrom) {
		this.eamilFrom = eamilFrom;
	}

	public String getEamilSentdate() {
		return eamilSentdate;
	}

	public void setEamilSentdate(String eamilSentdate) {
		this.eamilSentdate = eamilSentdate;
	}

	public String getEamilContent() {
		return eamilContent;
	}

	public void setEamilContent(String eamilContent) {
		this.eamilContent = eamilContent;
	}

	public int getCountMax() {
		return countMax;
	}

	public void setCountMax(int countMax) {
		this.countMax = countMax;
	}

	public int getCountUnread() {
		return countUnread;
	}

	public void setCountUnread(int countUnread) {
		this.countUnread = countUnread;
	}

	public int getCountNew() {
		return countNew;
	}

	public void setCountNew(int countNew) {
		this.countNew = countNew;
	}

}
