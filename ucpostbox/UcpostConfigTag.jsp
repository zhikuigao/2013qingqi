<%@page import="qflag.ucstar.plugin.thread.UcpostThread"%>
<%@page
	import="qflag.ucstar.plugin.ucpostbox.manager.UcpostBoxcofingData"%>
<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>邮箱配置界面</title>
<script language="javascript">
	function check() {
		if (theform.imap.value == "") {
			alert("请检查邮件服务器地址");
			theform.imap.focus();
			return false;
		}
		if (theform.urlmail.value == "") {
			alert("请检查邮箱地址！！");
			theform.urlmail.focus();
			return false;
		} else {
			if (theform.urlmail.value != "") {
				var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
				isok = reg.test(theform.urlmail.value);
				if (!isok) {
					alert("邮箱格式不正确，请重新输入！");
					//document.getElementById("emailname").focus();
					theform.urlmail.focus();
					return false;
				}
			}
			;

		}
		if (theform.pass.value == "") {
			alert("请检查邮箱密码！！");
			theform.pass.focus();
			return false;
		}
	}
	function ischeckemail(str) {
		if (str != "") {
			var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			isok = reg.test(str);
			if (!isok) {
				alert("邮箱格式不正确，请重新输入！");
				//document.getElementById("emailname").focus();
				return false;
			}
		}
		;
	}
</script>
</head>
<body>
	<%
		String imap = request.getParameter("imap") == null ? "" : request
				.getParameter("imap").trim();
		String urlmail = request.getParameter("urlmail") == null ? ""
				: request.getParameter("urlmail").trim();
		String pass = request.getParameter("pass") == null ? "" : request
				.getParameter("pass").trim();
		String submit = request.getParameter("submit") == null ? ""
				: request.getParameter("submit").trim();
		String startSubmit = request.getParameter("startSubmit") == null ? ""
				: request.getParameter("startSubmit").trim();

		boolean submits = request.getParameter("submits") != null;
		boolean startSubmits = request.getParameter("startSubmits") != null;
		boolean successFlag = false;
		boolean successStart = true;
		boolean successSuccess = false;
		UcpostBoxcofingData dhconfig = UcpostBoxcofingData.getInstance();
		if (startSubmits) {
			successStart = false;
			if (dhconfig != null && imap != null && imap.length() > 0
					&& urlmail != null && urlmail.length() > 0
					&& pass != null && pass.length() > 0) {

				dhconfig.setMail(imap);
				dhconfig.setUrlmail(urlmail);
				dhconfig.setPass(pass);
				successStart = true;
				successSuccess = true;
			}
		}
		imap = JiveGlobals.getProperty("szgs.sycdata.wujixin.mail") == null ? ""
				: JiveGlobals.getProperty("szgs.sycdata.wujixin.mail");
		urlmail = JiveGlobals.getProperty("szgs.sycdata.wujixin.urlmail") == null ? ""
				: JiveGlobals.getProperty("szgs.sycdata.wujixin.urlmail");
		pass = JiveGlobals.getProperty("szgs.sycdata.wujixin.pass") == null ? ""
				: JiveGlobals.getProperty("szgs.sycdata.wujixin.pass");
		
		if (submits) {
			%>
		<div class="jive-success">
		<table cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="jive-icon"><img
						src="../../../images/success-16x16.gif" width="16" height="16"
						border="0"></td>
					<td class="jive-icon-label">邮件开始同步</td>
				</tr>
			</tbody>
		</table>
		</div>
		<br> 
	<% new UcpostThread().start(); }


	
		if (successStart == false) {
	%>
	<div class="jive-success">
		<table cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="jive-icon"><img
						src="../../../images/error-16x16.gif" width="16" height="16"
						border="0"></td>
					<td class="jive-icon-label">保存失败</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br>
	<%
		} else if (successSuccess == true) {
	%>
	<div class="jive-success">
		<table cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="jive-icon"><img
						src="../../../images/success-16x16.gif" width="16" height="16"
						border="0"></td>
					<td class="jive-icon-label">保存成功</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br>
	<%
		} else if (successFlag == true) {
	%>

	<%
		}
	%>

	<form method="post" id="theform" name="theform"
		action="UcpostConfigTag.jsp">
		<div class="jive-contentBox">
			<table>
				<tr>
					<td>邮件服务器:</td>
					<td><input type="text" id="imap" name="imap" value="<%=imap%>"
						size="80" /></td>
				</tr>
				<tr>
					<td>邮箱地址:</td>
					<td><input type="text" id="urlmail" name="urlmail"
						value="<%=urlmail%>" size="80" /></td>
				</tr>
				<tr>
					<td>邮箱密码:</td>
					<td><input type="text" id="pass" name="pass" value="<%=pass%>"
						size="80" /></td>
				</tr>
				<tr>
					<td><input type="submit" name="startSubmits" id="startSubmits"
						value="保存设置" onClick="return check()">
					</td>
					<td><input type="submit" name="submits" id="submits"
						value="开始同步">
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>