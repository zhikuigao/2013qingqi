<%@page import="qflag.ucstar.springmvc.util.UcstarSpringJSONUtil"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringStringUtil"%>
<%@page import="java.util.List"%>
<%@page import="qflag.base.zk.annot.BasePage"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsShare"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.IUcmFriendsShareService"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.UcmFriendsShareServiceFactory"%>
<%@page import="qflag.ucstar.zk.ucmfriendsshare.service.UcmFriendsShareServiceUtil"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%
	UcmFriendsShareServiceFactory factory = UcmFriendsShareServiceUtil.getFactory();
	IUcmFriendsShareService service = factory.getUcmFriendsShareService();
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
		String userId = (null==request.getParameter("userId")?"":request.getParameter("userId"));
		if(userId.length() > 0) {
		
			condStr += " and userId like '%" + userId + "%'";
		}
		if(condStr.length() > 0) {
			condStr = "where " + condStr.substring(" and".length(),condStr.length());
		}
		//排序
		if(sort.length() > 0 && dir.length() > 0) {
			condStr += " order by " + sort + " "+dir;
		}
		
		String sql = "from " + UcmFriendsShare.class.getName() + " as obj ";
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
		UcmFriendsShare theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsShare)service.getObject(UcmFriendsShare.class, uri);
		} else { //ADD
			theObj = new UcmFriendsShare();
		}
		String userId = (null==request.getParameter("userId")?"":request.getParameter("userId"));
		if(userId.length() > 0) {
			theObj.setUserId(userId);
		} else {
		

		
		}
		String content = (null==request.getParameter("content")?"":request.getParameter("content"));
		if(content.length() > 0) {
			theObj.setContent(content);
		} else {
		

		
		}
		String createtime = (null==request.getParameter("createtime")?"":request.getParameter("createtime"));
		if(createtime.length() > 0) {
			theObj.setCreatetime(createtime);
		} else {
		

		
		}
		String extend2 = (null==request.getParameter("extend2")?"":request.getParameter("extend2"));
		if(extend2.length() > 0) {
			theObj.setExtend2(extend2);
		} else {
		

		
		}
		String position = (null==request.getParameter("position")?"":request.getParameter("position"));
		if(position.length() > 0) {
			theObj.setPosition(position);
		} else {
		

		
		}
		String extend1 = (null==request.getParameter("extend1")?"":request.getParameter("extend1"));
		if(extend1.length() > 0) {
			theObj.setExtend1(extend1);
		} else {
		

		
		}
		service.saveObject(theObj);
		String data = "{success:true}";
		out.clear();
		out.print(data);
	}
	else if("getobject".equalsIgnoreCase(action)) {
		String uri = (null==request.getParameter("uri")?"":request.getParameter("uri"));
		UcmFriendsShare theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsShare)service.getObject(UcmFriendsShare.class, uri);
		}
		String data = UcstarSpringJSONUtil.toListOneJSONData(theObj);
		out.clear();
		out.print(data);
	}
	else if("delete".equalsIgnoreCase(action)) {
		String uris = (null==request.getParameter("uris")?"":request.getParameter("uris"));
		java.util.List<String> uriList = UcstarSpringStringUtil.stringToList(uris);
        for(String uriStr : uriList) {
            service.delete(uriStr, UcmFriendsShare.class);
        }
		String data = "{success:true}";
		out.clear();
		out.print(data);
	}
%>