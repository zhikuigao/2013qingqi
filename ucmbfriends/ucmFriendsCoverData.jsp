<%@page import="qflag.ucstar.springmvc.util.UcstarSpringJSONUtil"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringStringUtil"%>
<%@page import="java.util.List"%>
<%@page import="qflag.base.zk.annot.BasePage"%>
<%@page import="qflag.ucstar.zk.biz.pojo.UcmFriendsCover"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.IUcmFriendsCoverService"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.UcmFriendsCoverServiceFactory"%>
<%@page import="qflag.ucstar.zk.ucmfriendscover.service.UcmFriendsCoverServiceUtil"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%
	UcmFriendsCoverServiceFactory factory = UcmFriendsCoverServiceUtil.getFactory();
	IUcmFriendsCoverService service = factory.getUcmFriendsCoverService();
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
		
		String sql = "from " + UcmFriendsCover.class.getName() + " as obj ";
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
		UcmFriendsCover theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsCover)service.getObject(UcmFriendsCover.class, uri);
		} else { //ADD
			theObj = new UcmFriendsCover();
		}
		String userId = (null==request.getParameter("userId")?"":request.getParameter("userId"));
		if(userId.length() > 0) {
			theObj.setUserId(userId);
		} else {
		}
		String fileId = (null==request.getParameter("fileId")?"":request.getParameter("fileId"));
		if(fileId.length() > 0) {
			theObj.setFileId(fileId);
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
		UcmFriendsCover theObj = null;
		if(uri.length() > 0) { //UPDATE
			theObj = (UcmFriendsCover)service.getObject(UcmFriendsCover.class, uri);
		}
		String data = UcstarSpringJSONUtil.toListOneJSONData(theObj);
		out.clear();
		out.print(data);
	}
	else if("delete".equalsIgnoreCase(action)) {
		String uris = (null==request.getParameter("uris")?"":request.getParameter("uris"));
		java.util.List<String> uriList = UcstarSpringStringUtil.stringToList(uris);
        for(String uriStr : uriList) {
            service.delete(uriStr, UcmFriendsCover.class);
        }
		String data = "{success:true}";
		out.clear();
		out.print(data);
	}
%>