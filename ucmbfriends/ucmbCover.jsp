<%@page import="java.io.File"%>
<%@page import="org.jivesoftware.util.Log"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsCover"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.IUcmFriendsCoverService"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.UcmFriendsCoverServiceUtil"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.UcmFriendsCoverServiceFactory"%>
<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.utils.UcmbFriendsUtils"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringMVCUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	//封面接口请求处理
	UcmFriendsCoverServiceFactory factory = UcmFriendsCoverServiceUtil.getFactory();
	IUcmFriendsCoverService service = factory.getUcmFriendsCoverService();
	
	String action = (null==request.getParameter("ACTION")?"":request.getParameter("ACTION"));
	//data为JSON数据格式 
	String data = (null==request.getParameter("DATA")?"":request.getParameter("DATA"));
	if(JiveGlobals.isEmpty(action) || JiveGlobals.isEmpty(data)){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1030\", \"RESULTMESSAGE\":\"(1030)请求参数为空\"}");  
		Log.debug("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1030)请求参数为空");
		return;
	}
	JSONObject tmpObj = UcmbFriendsUtils.getJsonObjByStr(data);
	if(tmpObj == null){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1031\", \"RESULTMESSAGE\":\"(1031)请求参数DATA格式错误\"}"); 
		Log.debug("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)请求参数DATA格式错误");
		return;
	}
	
	if("GET".equalsIgnoreCase(action)){   
		//获取封面数据接口请求
		String userId = tmpObj.get("USERID").toString();   
		String version = tmpObj.get("VERSION").toString(); 
		if(JiveGlobals.isEmpty(userId) || JiveGlobals.isEmpty(version)){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数USERID或者VERSION为空\"}");
			Log.debug("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)请求参数USERID或者VERSION为空");
			return;
		}
		
		//根据userId获取用户的封面文件信息，然后返回文件ID
		UcmFriendsCover ur = null;
		ur = service.getVersionWork(userId,version);
		if(JiveGlobals.isEmpty(ur.getFileId())){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1033)版本已是最新\"}");  
			return;
		}
		if(JiveGlobals.isEmpty(ur.getExtend1())){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1033)返回文件名为空\"}");  
			return;
		}
		
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\", \"FILEID\":\""+ur.getFileId()+"\", \"FILENAME\":\""+ur.getExtend1()+"\"}");  
		return;
		
		
	} else if("POST".equalsIgnoreCase(action)){  
		//保存封面数据接口请求 
		//1：获取相应参数；2：验证相应参数的正确性；3：保存数据库，返回相应结果           其他接口也是类似处理(自己根据业务要求变通处理) 
		String userId = tmpObj.get("USERID").toString();   
		String fileid = tmpObj.get("FILEID").toString(); 
		String filename = tmpObj.get("FILENAME").toString(); 
		String version = "";
		//先判断用户存不存在、存在即是有版本号了   
		if(JiveGlobals.isEmpty(userId) || JiveGlobals.isEmpty(fileid) ||  JiveGlobals.isEmpty(filename)){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数USERID或者fileid或者filename为空\"}"); 
			return;
		}
		//判断文件是否已经上传至服务器
		File f = new File(UcmbFriendsUtils.getUploadFile()+filename);
		if(!f.exists()){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)图片未上传至服务器,请先上传图片\"}"); 
			Log.debug("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)图片未上传至服务器");
			return;
		}
		
		
		if(service.isuserExtis(userId)){
			if(service.updateVer(fileid)){
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1038)程序错误\"}"); 
				return;
			}
			//version = ;
		}else{
			UcmFriendsCover theObj = null;
			theObj = new UcmFriendsCover();
			theObj.setUserId(userId);
			theObj.setFileId(fileid);
			theObj.setExtend1(filename);
			theObj.setExtend2("");
			service.saveObject(theObj);
			version = "0";
		}
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\", \"VERSION\":\""+service.getVersion(fileid)+"\"}");  
		return;
	} else {
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1030\", \"RESULTMESSAGE\":\"(1030)请求参数错误\"}"); 
		Log.debug("【工作圈:保存封面数据】<<<<<<<<<<<<<<<<<(1031)请求参数错误");
		return;
	}


%>