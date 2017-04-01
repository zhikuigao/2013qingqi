<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.utils.UcmbFriendsUtils"%>
<%@page import="org.jivesoftware.util.Log"%>
<%@page import="org.jivesoftware.util.StringUtils"%>
<%@page import="qflag.ucstar.commons.UcstarUtil"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.manager.UcmbFriendsConfigManager"%>
<%@page import="java.util.Map"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringMVCUtil"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//所有的资源文件上传请求处理
	String savePath = UcmbFriendsConfigManager.getInstance().getUploadPath();
	Log.debug("【工作圈:上传路径】<<<<<<<<<<<<<<<<<"+savePath);
	String fileName = "";
	String errorCodeStr = "";
	String fileID = "";
	int fileSize = 52000000;
	boolean flag = false;
	String prefix = "";
	try{
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		Iterator<FileItem> fileItr = upload.parseRequest(request).iterator();
		while (fileItr.hasNext()) {
			FileItem f = fileItr.next();
			if(f.getSize()>0){
				fileName = f.getName() == null ? "":f.getName();
				if (fileName == null || fileName.length() <= 0) {
					continue;
				}
				prefix=fileName.substring(fileName.lastIndexOf(".")+1);
				if(JiveGlobals.isEmpty(prefix) || UcmbFriendsConfigManager.getInstance().isValid(prefix)){
					flag = false;
					Log.debug("【工作圈:上传】<<<<<<<<<<<<<<<<<文件格式错误");
					errorCodeStr = "文件格式错误：仅支持上传图片";
					break;
				}
				//File saveFile = new File(savePath, fileId + "_" +fileName);
				fileID = StringUtils.randomString(20).toLowerCase();
				File saveFile = new File(savePath, fileID+"." +prefix);
				if (saveFile.exists()){
					flag = false;
					errorCodeStr = "文件已存在";
					break;
				}
				f.write(saveFile);
				flag = true;	
				if(UcmbFriendsUtils.getSmall(fileID, prefix)){
				flag = false;
				errorCodeStr = "生成缩略图失败";
			}
				
			}else if(f.getSize()>fileSize){
				flag = false;
				errorCodeStr = "文件过大";
				break;
			}else{
				flag = false;
				errorCodeStr = "文件为空";
				break;
			}
		}

		
	}catch(Exception e){
		errorCodeStr = "程序异常";
		flag = false;
		Log.error("【工作圈:上传】<<<<<<<<<<<<<<<<<"+e);
	}
	if(flag){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"0\", \"RESULTMESSAGE\":\"文件上传成功!\", FILEID:\""+fileID+"\",\"FILENAME\":\""+fileID+"."+prefix+"\",\"FILENAMESMALL\":\""+fileID+"_small."+prefix+"\"}");
	} else {
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1001\",  \"RESULTMESSAGE\":\""+errorCodeStr+"\"}");
	}
	
%>