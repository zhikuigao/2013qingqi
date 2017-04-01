package qflag.ucstar.plugin.ucpostbox.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import qflag.base.entity.UriEntity;


@Entity
@Table(name="e_exchange_mail")
public class UcpostExchangePoji extends UriEntity{
	private String username;
	private String mailId;
	private int mailNumber;
	private int isNewMail;
	private String ext;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public int getMailNumber() {
		return mailNumber;
	}

	public void setMailNumber(int mailNumber) {
		this.mailNumber = mailNumber;
	}

	public int getIsNewMail() {
		return isNewMail;
	}

	public void setIsNewMail(int isNewMail) {
		this.isNewMail = isNewMail;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

//	public void setReadedMail(List<String> readedMail) {
//		this.readedMail = readedMail;
//	}

//	private Service service;
//	private SubscribeResponse response;
//	private PullSubscription subscription;
//	private List<String> readedMail = new ArrayList<String>();
//	public UcstarExchangePojo(Service _service, SubscribeResponse _response,PullSubscription _subscription) {
//		service = _service;
//		response = _response;
//		subscription = _subscription;
//	}
//	
//	public Service getService() {
//		return service;
//	}
//	public void setService(Service service) {
//		this.service = service;
//	}
//	public SubscribeResponse getResponse() {
//		return response;
//	}
//	public void setResponse(SubscribeResponse response) {
//		this.response = response;
//	}
//
//	public PullSubscription getSubscription() {
//		return subscription;
//	}
//
//	public void setSubscription(PullSubscription subscription) {
//		this.subscription = subscription;
//	}
//
//	public List<String> getReadedMail() {
//		return readedMail;
//	}
	
}
