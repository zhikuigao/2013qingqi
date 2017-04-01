<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.IUcmFriendsCommentService"%>
<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.UcmFriendsCommentServiceUtil"%>
<%@page import="qflag.ucstar.zk.ucmfriendscomment.service.UcmFriendsCommentServiceFactory"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsComment"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringJSONUtil"%>
<%@page import="java.util.List"%>
<%@page import="qflag.base.zk.annot.BasePage"%>
<%@page import="org.jivesoftware.util.Log"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsResource"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsShare"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.IUcmFriendsResourceService"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.UcmFriendsResourceServiceUtil"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.UcmFriendsResourceServiceFactory"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.IUcmFriendsShareService"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.UcmFriendsShareServiceUtil"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.UcmFriendsShareServiceFactory"%>
<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.utils.UcmbFriendsUtils"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringMVCUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	//所有分享的相关接口 
	//192.168.1.111:9090/ucstar_plugins/ucmbfriends/ucmbShare.jsp?ACTION=post&DATA={"TYPE": "PUBLISH","USERID": "test001","IMAGEIDBIG": "[{111},{333}]","IMAGEIDSMALL": "[{123},{321}]","CONTENT": "2352"}
	String action = (null==request.getParameter("ACTION")?"":request.getParameter("ACTION"));
	//分享接口
	UcmFriendsShareServiceFactory factory = UcmFriendsShareServiceUtil.getFactory();
	IUcmFriendsShareService serviceShare = factory.getUcmFriendsShareService();
	
	//图片接口
	UcmFriendsResourceServiceFactory factorys = UcmFriendsResourceServiceUtil.getFactory();
	IUcmFriendsResourceService serviceResource = factorys.getUcmFriendsResourceService();
	
	//评论接口
	UcmFriendsCommentServiceFactory factoryes = UcmFriendsCommentServiceUtil.getFactory();
	IUcmFriendsCommentService service = factoryes.getUcmFriendsCommentService();
	
	//data为JSON数据格式 
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
		String type = tmpObj.get("TYPE").toString();
		if(JiveGlobals.isEmpty(type)){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数TYPE为空\"}");  
			return;
		}
		if("PERLIST".equalsIgnoreCase(type)){   //个人分享列表接口 
			String userid = tmpObj.get("USERID").toString(); 
			String start = tmpObj.get("START").toString(); 
			String limit = tmpObj.get("LIMIT").toString(); 
			String contentStr = "";
			String condStr = "";
			String createtime = "";
			String imageIdsmall = "";
			String imageIdBig = "";
			String content = "";
			String fkshare =  "";
			String position = "";
			String datajson = "";
			if(!JiveGlobals.isEmpty(userid) && !JiveGlobals.isEmpty(start) && !JiveGlobals.isEmpty(limit) ){
				int iStart = Integer.parseInt(start);
				int iLimit = Integer.parseInt(limit);
				if(userid.length() > 0) {
					condStr += " and userId like '%" + userid + "%'";
				}
				if(condStr.length() > 0) {
					condStr = "where " + condStr.substring(" and".length(),condStr.length());
				}
				String sql = "from " + UcmFriendsShare.class.getName() + " as obj ";
				sql += condStr;
				BasePage thePage = new BasePage();
				if(iLimit == 0) {
					iLimit = 100;
				}
				thePage.setCurrentPage(iStart/iLimit);
				thePage.setPageSize(iLimit);
				List objs = serviceShare.getObjects(sql,thePage);
				datajson = UcstarSpringJSONUtil.toPageListJSONData(thePage, objs);
				
				JSONObject tmpObjShare = UcmbFriendsUtils.getJsonObjByStr(datajson);
				//解析JSON
				JSONArray  dataJson = JSONObject.fromObject(datajson).getJSONArray("root");
				UcmFriendsResource ur = new UcmFriendsResource();
				if(Integer.parseInt(tmpObjShare.get("totalProperty").toString())>0){ 
					try{
						for(int i=0;  i<dataJson.size(); i++){		
							JSONObject json = dataJson.getJSONObject(i);	
							createtime = json.getString("createtime");
							fkshare = json.getString("uri");
							content = json.getString("content");
							ur = serviceShare.getObjectShare(fkshare);
							imageIdBig = ur.getExtend1();
							if(JiveGlobals.isEmpty("imageIdBig")){
								imageIdBig = "[]";
							}else{
								imageIdBig = "["+imageIdBig+"]";
							}
							if(i==dataJson.size()-1){
								contentStr+= "{\"POSITION\":\"\", \"CREATETIME\":\""+createtime+"\",\"FKSHARE\":\""+fkshare+"\",\"CONTENT\":\""+content+"\",\"IMAGEIDBIG\":"+imageIdBig+"}";
							}else{
								contentStr+= "{\"POSITION\":\"\", \"CREATETIME\":\""+createtime+"\",\"FKSHARE\":\""+fkshare+"\",\"CONTENT\":\""+content+"\",\"IMAGEIDBIG\":"+imageIdBig+"},";
							}
						}
						contentStr = "\"list\":" + "["  + contentStr +  "]";
					}catch(Exception e){
						UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)JSON解析出错\"}");  
						return;
					}
				}else{
					UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)个人分享为空\"}");  
					return;
				}
				
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\": \"请求处理成功\", \"COUNT\": \""+tmpObjShare.get("totalProperty").toString()+"\","+contentStr+"}");  
				return;
				
			}
			
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数DATA有误\"}");  
			return;
			
		} else if("WITH".equalsIgnoreCase(type)){   //个人分享信息详细接口 
			String fkshare = tmpObj.get("FKSHARE").toString(); 
			if(JiveGlobals.isEmpty(fkshare)){
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数FKSHARE为空\"}");  
				return;
			}
			int iStart = Integer.parseInt("0");
			int iLimit = Integer.parseInt("100");
			String condStr = "";
			if(fkshare.length() > 0) {
				condStr += " and shareUri like '%" + fkshare + "%'";
			}
			if(condStr.length() > 0) {
				condStr = "where " + condStr.substring(" and".length(),condStr.length());
			}
			String sql = "from " + UcmFriendsComment.class.getName() + " as obj ";
			sql += condStr;
			BasePage thePage = new BasePage();
			if(iLimit == 0) {
				iLimit = 100;
			}
			thePage.setCurrentPage(iStart/iLimit);
			thePage.setPageSize(iLimit);
			List objs = service.getObjects(sql,thePage);
			String dataShare = UcstarSpringJSONUtil.toPageListJSONData(thePage, objs);
			JSONObject tmpObjShare = UcmbFriendsUtils.getJsonObjByStr(dataShare);
			JSONArray  dataJson = JSONObject.fromObject(dataShare).getJSONArray("root");
			UcmFriendsComment uc = new UcmFriendsComment();
			String contentStr = "";
			String praise = "";
			if(Integer.parseInt(tmpObjShare.get("totalProperty").toString())>0){ 
				try{
					for(int i=0;  i<dataJson.size(); i++){		
					JSONObject json = dataJson.getJSONObject(i);
					String commentType = json.getString("commentType");
					String createtime = json.getString("createtime");
					String fromUserId = json.getString("fromUserId");
					String content = json.getString("content");
					String toUserId = json.getString("toUserId");
					String commId = json.getString("uri");
						if(commentType.equals("0")){
							//此处处理点赞
							if(!JiveGlobals.isEmpty(fromUserId)){
								 praise += fromUserId + ",";
							}
						}
					
					if(commentType.equals("1")){
						//此处处理评论
						if(!JiveGlobals.isEmpty(fromUserId)){
							contentStr += "{\"CREATETIME\":\""+createtime+"\",\"COMMENTTO\":\""+toUserId+"\",\"COMMENTMAN\":\""+toUserId+"\",\"COMMENTID\":\""+commId+"\",\"CONTENT\":\""+content+"\"},";
						}
					}				
		 		}
					//组装json字符串
					if(JiveGlobals.isEmpty(praise)){
						praise = "\"PRAISES\":\"\"" + ",";
					}else{
						praise = "\"praise\": \""+praise.substring(0,praise.length()-1)+"\"";
					}

					if(JiveGlobals.isEmpty(contentStr)){
						contentStr = "\"list\":[]";
					}else{
						contentStr = "\"list\":["+contentStr.substring(0,contentStr.length()-1)+"]";
					}
					UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\": \"请求处理成功\","+praise+","+contentStr+"}");  
					return;
				}catch(Exception e){
					UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)JSON解析出错\"}");  
					return;
				}
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"分享数据为空\"}");  
			return;
			
		} else if("PERRING".equalsIgnoreCase(type)){   //朋友圈分享接口  
			String start = tmpObj.get("START").toString(); 
			String limit = tmpObj.get("LIMIT").toString(); 
			int iStart = Integer.parseInt(start);
			int iLimit = Integer.parseInt(limit);
			String contentStr = "";
			String condStr = "";
			String createtime = "";
			String imageIdsmall = "";
			String str = "";
			String imageIdBig = "";
			String content = "";
			String fkshare =  "";
			String position = "";
			String sql = "from " + UcmFriendsShare.class.getName() + " as obj ";
			sql += condStr;
			BasePage thePage = new BasePage();
			if(iStart == 0) {
				iLimit = 100;
			}
			thePage.setCurrentPage(iStart/iLimit);
			thePage.setPageSize(iLimit);
			List objs = service.getObjects(sql,thePage);
			String dataShare = UcstarSpringJSONUtil.toPageListJSONData(thePage, objs);
			JSONObject tmpObjShare = UcmbFriendsUtils.getJsonObjByStr(dataShare);
			JSONArray  dataJson = JSONObject.fromObject(dataShare).getJSONArray("root");
			UcmFriendsResource ur = new UcmFriendsResource();
			if(Integer.parseInt(tmpObjShare.get("totalProperty").toString())>0){ 
				for(int i=0;  i<dataJson.size(); i++){		
					JSONObject json = dataJson.getJSONObject(i);
					createtime = json.getString("createtime");
					content = json.getString("content");
					fkshare = json.getString("uri");
					ur = serviceShare.getObjectShare(fkshare);
					imageIdBig = ur.getExtend1();
					if(JiveGlobals.isEmpty("imageIdBig")){
						imageIdBig = "[]";
					}else{
						imageIdBig = "["+imageIdBig+"]";
					}
					
					
					//处理评论
					sql = "from qflag.ucstar.zk.biz.pojo.UcmFriendsComment as obj where  shareUri like '%"+fkshare+"%'";
					thePage.setCurrentPage(iStart/iLimit);
					thePage.setPageSize(iLimit);
					List obj = service.getObjects(sql,thePage);
					String dataComment = UcstarSpringJSONUtil.toPageListJSONData(thePage, obj);
					JSONObject tmpObjShares = UcmbFriendsUtils.getJsonObjByStr(dataComment);
					JSONArray  dataComm = JSONObject.fromObject(dataComment).getJSONArray("root");
					String praise = "";
					String contentStrComm = "";
					if(Integer.parseInt(tmpObjShares.get("totalProperty").toString())>0){ 
						for(int j=0;  j<dataComm.size(); j++){		
							JSONObject jsons = dataComm.getJSONObject(j);
							String commentTypeComm = jsons.getString("commentType");
							String createtimeComm = jsons.getString("createtime");
							String fromUserIdComm = jsons.getString("fromUserId");
							String contentComm = jsons.getString("content");
							String toUserIdComm = jsons.getString("toUserId");
							String commIdComm = jsons.getString("uri");
								if(commentTypeComm.equals("0")){
									//此处处理点赞
									if(!JiveGlobals.isEmpty(fromUserIdComm)){
										 praise += fromUserIdComm + ",";
									}
								}
							
							if(commentTypeComm.equals("1")){
								//此处处理评论
								if(!JiveGlobals.isEmpty(fromUserIdComm)){
									contentStrComm += "{\"CREATETIME\":\""+createtimeComm+"\",\"COMMENTTO\":\""+toUserIdComm+"\",\"COMMENTMAN\":\""+toUserIdComm+"\",\"COMMENTID\":\""+commIdComm+"\",\"CONTENT\":\""+contentComm+"\"},";
								}
							}	
							
				 		}
					}
						//组装json字符串
						if(JiveGlobals.isEmpty(praise)){
							praise = ","+ "\"PRAISES\":\"\"";
						}else{
							praise = ","+"\"PRAISES\": \""+praise.substring(0,praise.length()-1)+"\"";
						}
						if(JiveGlobals.isEmpty(contentStrComm)){
							contentStrComm = ","+"\"LISTER\":[]";
						}else{
							contentStrComm = ","+"\"LISTER\":["+contentStrComm.substring(0,contentStrComm.length()-1)+"]";
						}
						
					if(i==dataJson.size()-1){
						contentStr+="{\"fkshare\":\""+fkshare+"\",\"POSITION\":\"\",\"TIME\":\""+createtime+"\",\"MESSAGE\":\""+content+"\",\"IMAGEIDBIG\":"+imageIdBig+""+praise+""+contentStrComm+"}";
					}else{
						contentStr+="{\"fkshare\":\""+fkshare+"\",\"POSITION\":\"\",\"TIME\":\""+createtime+"\",\"MESSAGE\":\""+content+"\",\"IMAGEIDBIG\":"+imageIdBig+""+praise+""+contentStrComm+"},";
					}
					
				}
				
				contentStr = "\"list\":["+contentStr+"]";
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求回复成功\","+contentStr+"}");  
				return;
			}
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"分享数据为空\"}");  
			return;
		} else {
			
		}
	} else if("POST".equalsIgnoreCase(action)){  //保存封面数据接口请求 
		String type = tmpObj.get("TYPE").toString();
		if(JiveGlobals.isEmpty(type)){
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数TYPE为空\"}");  
			return;
		}
		if("PUBLISH".equalsIgnoreCase(type)){   //发布分享接口  
			String userid = tmpObj.get("USERID").toString(); 
			String content = tmpObj.getString("CONTENT");
			JSONArray  datajson = tmpObj.getJSONArray("IMAGEIDBIG");
			if(!JiveGlobals.isEmpty(userid)){
				//存入数据库  动态对应2个表    1、图片表 2、分享表
				UcmFriendsShare theObj = null;
				UcmFriendsResource theObjImage = null;
				theObj = new UcmFriendsShare();
				theObj.setUserId(userid);
				theObj.setContent(content);
				theObj.setCreatetime(System.currentTimeMillis()+"");
				serviceShare.saveObject(theObj);
				//加图    
				if(datajson.size()>0){
					for(int i=0; i<datajson.size();i++){
						String share = theObj.getUri();
						JSONObject json =  datajson.getJSONObject(i);
						theObjImage = new UcmFriendsResource();
						theObjImage.setShareUri(share);
						theObjImage.setCreatetime(System.currentTimeMillis()+"");
						theObjImage.setFileId(json.getString("IMGEID"));
						theObjImage.setFileName(json.getString("FILENAME"));
						serviceResource.saveObject(theObjImage);
					}
				}
				
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"(1032)请求处理成功\",\"FKSHARE\":\""+theObj.getUri()+"\"}");  
				return;
			}	
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数userid或者其他为空\"}");  
			return;
			
		} else if("DELETE".equalsIgnoreCase(type)){   //删除分享接口  
			String fkshare = tmpObj.get("FKSHARE").toString(); 
			if(JiveGlobals.isEmpty(fkshare)){
				UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数fkshare为空\"}");  
				return;
			}
			//删除分享和图片和评论
			serviceShare.delete(fkshare,UcmFriendsShare.class);
			serviceShare.getImagedel(fkshare);
			serviceShare.getCommDelete(fkshare);
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"请求处理成功\"}");  
			return;
			
		} else {
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1032\", \"RESULTMESSAGE\":\"(1032)请求参数错误\"}");  
		}
	} else {
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1030\", \"RESULTMESSAGE\":\"(1030)请求参数错误\"}"); 
		return;
	}


%>