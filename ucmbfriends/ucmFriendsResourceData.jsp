<%@page import="qflag.ucstar.springmvc.util.UcstarSpringJSONUtil"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringStringUtil"%>
<%@page import="java.util.List"%>
<%@page import="qflag.base.zk.annot.BasePage"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsResource"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.IUcmFriendsResourceService"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.UcmFriendsResourceServiceFactory"%>
<%@page import="qflag.ucstar.zk.ucmfriendsresource.service.UcmFriendsResourceServiceUtil"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%
	UcmFriendsResourceServiceFactory factory = UcmFriendsResourceServiceUtil.getFactory();
	IUcmFriendsResourceService service = factory.getUcmFriendsResourceService();
	String action = (null==request.getParameter("action")?"":request.getParameter("action"));
	
	if("search".equalsIgnoreCase(action)) {
		String start = (null==request.getParameter("start")?"0":request.getParameter("start"));
		String limit = (null==request.getParameter("limit")?"0":request.getParameter("limit"));
		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String sort = (null==request.getParameter("sort")?"":request.getParameter("sort"));
		String dir = (null==request.getParameter("dir")?"":request.getParameter("dir"));
		//组装请求条件
		String condStr = "";
		String shareUri = (null==request.getParameter("shareUri")?"":request.getParameter("shareUri"));
		if(shareUri.length() > 0) {
		
			condStr += " and shareUri like '%" + shareUri + "%'";
		}
		if(condStr.length() > 0) {
			condStr = "where " + condStr.substring(" and".length(),condStr.length());
		}
		//排序
		if(sort.length() > 0 && dir.length() > 0) {
			condStr += " order by " + sort + " "+dir;
		}
		
		String sql = "from " + UcmFriendsResource.class.getName() + " as obj ";
		sql += condStr;
		BasePage thePage = new BasePage();
		if(iLimit == 0) {
			iLimit = 100;
		}
		thePage.setCurrentPage(iStart/iLimit);
		thePage.setPageSize(iLimit);
		List objs = service.getObjects(sql,thePage);
		String data = UcstarSpringJSONUtil.toPageListJSONData(thePage, objs);
		out.clear();
		out.print(data);
	} 
	else if("save".equalsIgnoreCase(action)) {
		String uri = (null==request.getParameter("uri")?"":request.getParameter("uri"));
		UcmFriendsResource theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsResource)service.getObject(UcmFriendsResource.class, uri);
		} else { //ADD
			theObj = new UcmFriendsResource();
		}
		String shareUri = (null==request.getParameter("shareUri")?"":request.getParameter("shareUri"));
		if(shareUri.length() > 0) {
			theObj.setShareUri(shareUri);
		} else {
		

		
		}
		String fileId = (null==request.getParameter("fileId")?"":request.getParameter("fileId"));
		if(fileId.length() > 0) {
			theObj.setFileId(fileId);
		} else {
		

		
		}
		String fileName = (null==request.getParameter("fileName")?"":request.getParameter("fileName"));
		if(fileName.length() > 0) {
			theObj.setFileName(fileName);
		} else {
		

		
		}
		String fileSize = (null==request.getParameter("fileSize")?"":request.getParameter("fileSize"));
		if(fileSize.length() > 0) {
			theObj.setFileSize(fileSize);
		} else {
		

		
		}
		String fileExt = (null==request.getParameter("fileExt")?"":request.getParameter("fileExt"));
		if(fileExt.length() > 0) {
			theObj.setFileExt(fileExt);
		} else {
		

		
		}
		String fileType = (null==request.getParameter("fileType")?"":request.getParameter("fileType"));
		if(fileType.length() > 0) {
			theObj.setFileType(fileType);
		} else {
		

		
		}
		String createtime = (null==request.getParameter("createtime")?"":request.getParameter("createtime"));
		if(createtime.length() > 0) {
			theObj.setCreatetime(createtime);
		} else {
		

		
		}
		String modifytime = (null==request.getParameter("modifytime")?"":request.getParameter("modifytime"));
		if(modifytime.length() > 0) {
			theObj.setModifytime(modifytime);
		} else {
		

		
		}
		String location = (null==request.getParameter("location")?"":request.getParameter("location"));
		if(location.length() > 0) {
			theObj.setLocation(location);
		} else {
		

		
		}
		String extend1 = (null==request.getParameter("extend1")?"":request.getParameter("extend1"));
		if(extend1.length() > 0) {
			theObj.setExtend1(extend1);
		} else {
		

		
		}
		String extend2 = (null==request.getParameter("extend2")?"":request.getParameter("extend2"));
		if(extend2.length() > 0) {
			theObj.setExtend2(extend2);
		} else {
		

		
		}
		service.saveObject(theObj);
		String data = "{success:true}";
		out.clear();
		out.print(data);
	}
	else if("getobject".equalsIgnoreCase(action)) {
		String uri = (null==request.getParameter("uri")?"":request.getParameter("uri"));
		UcmFriendsResource theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsResource)service.getObject(UcmFriendsResource.class, uri);
		}
		String data = UcstarSpringJSONUtil.toListOneJSONData(theObj);
		out.clear();
		out.print(data);
	}
	else if("delete".equalsIgnoreCase(action)) {
		String uris = (null==request.getParameter("uris")?"":request.getParameter("uris"));
		java.util.List<String> uriList = UcstarSpringStringUtil.stringToList(uris);
        for(String uriStr : uriList) {
            service.delete(uriStr, UcmFriendsResource.class);
        }
		String data = "{success:true}";
		out.clear();
		out.print(data);
	}
%>