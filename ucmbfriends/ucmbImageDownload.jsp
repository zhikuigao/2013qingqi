
<%@page import="org.jivesoftware.util.Log"%>
<%@page import="qflag.ucstar.springmvc.util.UcstarSpringMVCUtil"%>
<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="qflag.ucstar.plugin.ucmbfriends.utils.UcmbFriendsUtils"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	
	//所有的资源文件下载请求处理

	response.setHeader("Content-Encoding","none");
	response.setHeader("Cache-Control","cache, must-revalidate");
	response.setHeader("Expires","0");

	String fileName = request.getParameter("FILENAME");
	if(JiveGlobals.isEmpty(fileName)){
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1020\", \"RESULTMESSAGE\":\"(1020)文件名参数错误\"}");
		return;
	}	
	String sfileName = fileName;
	String fullFilePath = UcmbFriendsUtils.getUploadFile() + '/' + fileName;
	File file = new File(fullFilePath);
	if("text".equals(request.getParameter("format"))){
		response.setHeader("Content-type", "text/plain; charset=utf-8");
	}else{
		//下载时对文件名编码
		String agent = (String) request.getHeader("USER-AGENT");
    	if (agent != null && agent.indexOf("MSIE") == -1) {
    		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859_1");  //FF
    	} else {
    		fileName = URLEncoder.encode(fileName, "UTF-8");
    		fileName = fileName.replaceAll("\\+", "%20");
    	}
		response.setHeader("Content-type","application/octet-stream; name=\""+sfileName+"\"");
		response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
	}
	response.setHeader("Content-Length", new Long(file.length()).toString());
	int maxCount = Integer.parseInt("10");
	if(file.exists()){
		BufferedOutputStream bos = null;
		try{
			InputStream fileIS = new FileInputStream(file);	
			BufferedInputStream bis = new BufferedInputStream(fileIS);//输入缓冲流
			OutputStream os = response.getOutputStream();
		    bos = new BufferedOutputStream(os);//输出缓冲流
		    byte data[] = new byte[4096];//缓冲字节数
		    int bufferSize = bis.read(data);
		    while (bufferSize!=-1) {
		       bos.write(data,0,bufferSize);
		       bufferSize=bis.read(data);
		    }
		}catch(Exception e){
			Log.debug("【工作圈:下载】<<<<<<<<<<<<<<<<<"+e);
			UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1020\", \"RESULTMESSAGE\":\"(1020)下载异常\"}");
			return;
			
		}finally{
			if(bos != null){
				try {
					bos.flush();  //清空输出缓冲流
				    bos.close();
				} catch (IOException e) {
					Log.debug("【工作圈:下载】<<<<<<<<<<<<<<<<<"+e);
					UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1020\", \"RESULTMESSAGE\":\"(1020)文件不存在\"}");
					return;
				}
			}
		}
	}else{
		UcstarSpringMVCUtil.rendText(response, "{\"MSGID\":\"1020\", \"RESULTMESSAGE\":\"(1020)文件不存在\"}");
		return;
	}
%>