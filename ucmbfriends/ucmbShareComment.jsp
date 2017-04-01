<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.UcmFriendsCommentServiceUtil"%>
<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.IUcmFriendsCommentService"%>
<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.UcmFriendsCommentServiceFactory"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsComment"%>
<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.utils.UcmbFriendsUtils"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringMVCUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	//赞、评论请求处理接口 

	String action = (null==request.getParameter("ACTION")?"":request.getParameter("ACTION"));
	//data为JSON数据格式 
	UcmFriendsCommentServiceFactory factory = UcmFriendsCommentServiceUtil.getFactory();
	IUcmFriendsCommentService service = factory.getUcmFriendsCommentService();
	
	String data = (null==request.getParameter("DATA")?"":request.getParameter("DATA"));
	if(JiveGlobals.isEmpty(action) || JiveGlobals.isEmpty(data)){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1030\", \"RESULTMESSAGE\":\"(1030)请求参数为空\"}");  
		return;
	}
	
	JSONObject tmpObj = UcmbFriendsUtils.getJsonObjByStr(data);
	if(tmpObj == null){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1031\", \"RESULTMESSAGE\":\"(1031)请求参数DATA格式错误\"}"); 
		return;
	}
	
	if("GET".equalsIgnoreCase(action)){
		
	} else if("POST".equalsIgnoreCase(action)){
		String type = tmpObj.get("TYPE").toString();
		if(JiveGlobals.isEmpty(type)){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数TYPE为空\"}");  
			return;
		}
		if("LIKE".equalsIgnoreCase(type)){   //提交点赞接口  
			//点赞类型为0   评论为1
			String fkshare =  tmpObj.get("FKSHARE").toString();
			String userId =  tmpObj.get("USERID").toString();
			String perparise =  tmpObj.get("PERPARISE").toString();
			if(!JiveGlobals.isEmpty(fkshare) && !JiveGlobals.isEmpty(userId) && !JiveGlobals.isEmpty(perparise) ){
				UcmFriendsComment theObj = null;
				theObj = new UcmFriendsComment();
				theObj.setShareUri(fkshare);
				theObj.setFromUserId(userId);
				theObj.setCreatetime(System.currentTimeMillis()+"");
				theObj.setToUserId(perparise);
				theObj.setCommentType("0");
				service.saveObject(theObj);
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\",\"PRAISEID\":\""+theObj.getUri()+"\"}");
				return;
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数中有数据为空\"}"); 
			return;
		} else if("UNLIKE".equalsIgnoreCase(type)){   //提交取消点赞接口  
			String praid = tmpObj.get("PRAISEID").toString();
			if(!JiveGlobals.isEmpty(praid)){
				 service.delete(praid, UcmFriendsComment.class);
				 UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\"}");
				 return;
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"删除动态失败\"}"); 
			return;
		} else if("COMMENT".equalsIgnoreCase(type)){   //提交评论接口  
			String commentTo =  tmpObj.get("COMMENTTO").toString();   //被评论人
			String userId =  tmpObj.get("USERID").toString();       //评论人
			String content =  tmpObj.get("CONTENT").toString();
			String fkshare =  tmpObj.get("FKSHARE").toString();
			if(!JiveGlobals.isEmpty(fkshare) && !JiveGlobals.isEmpty(userId) && !JiveGlobals.isEmpty(content) ){
				UcmFriendsComment theObj = null;
				theObj = new UcmFriendsComment();
				theObj.setShareUri(fkshare);
				theObj.setFromUserId(userId);
				theObj.setCreatetime(System.currentTimeMillis()+"");
				theObj.setContent(content);
				theObj.setToUserId(commentTo);
				theObj.setCommentType("1");
				service.saveObject(theObj);
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\",\"COMMENTID\":\""+theObj.getUri()+"\"}");
				return;
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数中有数据为空\"}"); 
			return;
		} else if("DELETE".equalsIgnoreCase(type)){   //提交删除评论接口  
			String praid = tmpObj.get("COMMENTID").toString();
			if(!JiveGlobals.isEmpty(praid)){
				 service.delete(praid, UcmFriendsComment.class);
				 UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\"}");
				 return;
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"删除评论失败\"}"); 
			return;
		} else {
			
		}
	} else {
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1030\", \"RESULTMESSAGE\":\"(1030)请求参数错误\"}"); 
		return;
	}


%>