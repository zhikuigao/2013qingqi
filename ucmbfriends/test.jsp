<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>TEST</title>
</head>
<body>
	<div>
		<p align="center">请您选择需要上传的文件</p>
		<form id="form1" name="form1" method="post"
			action="http://192.168.1.111:9090/ucstar_plugins/ucmbfriends/ucmbImageUpload.jsp"
			enctype="multipart/form-data">
			<table border="0" align="center">
				<tr>
					<td>上传文件：</td>
					<td><input name="file" type="file" size="20">
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" name="submit" value="提交"> <input
						type="reset" name="reset" value="重置"></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="border: 1px solid blue;">
		<form action="ucmbShare.jsp"  method="post">
			<table>
				<tr>
					<td>ucmbShare.jsp页面测试</td>
				</tr>
				<tr>
					<td>ACTION:<select  name="ACTION"><option value="GET">GET</option><option value="POST">POST</option></select></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>DATA:<textarea rows="5" cols="100" name="DATA"></textarea></td>
				</tr>
				
				
				<tr><td><input type="submit" value="SUBMIT"></td></tr>
			</table>
		</form>
	</div>
		<div style="border: 1px solid blue;">
		<form action="ucmbShare.jsp"  method="post">
			<table>
				<tr>
					<td>ucmbShareComment.jsp页面测试</td>
				</tr>
				<tr>
					<td>ACTION:<select  name="ACTION"><option value="GET">GET</option><option value="POST">POST</option></select></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>DATA:<textarea rows="5" cols="100" name="DATA"></textarea></td>
				</tr>
				
				
				<tr><td><input type="submit" value="SUBMIT"></td></tr>
			</table>
		</form>
	</div>
	</div>
		<div style="border: 1px solid blue;">
		<form action="ucmbCover.jsp"  method="post">
			<table>
				<tr>
					<td>ucmbShareComment.jsp页面测试</td>
				</tr>
				<tr>
					<td>ACTION:<select  name="ACTION"><option value="GET">GET</option><option value="POST">POST</option></select></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>DATA:<textarea rows="5" cols="100" name="DATA"></textarea></td>
				</tr>
				
				
				<tr><td><input type="submit" value="SUBMIT"></td></tr>
			</table>
		</form>
	</div>
	</div>
		<div style="border: 1px solid blue;">
		<form action="ucmbCover.jsp"  method="post">
			<table>
				<tr>
					<td>ucmbCover.jsp页面测试</td>
				</tr>
				<tr>
					<td>ACTION:<select  name="ACTION"><option value="GET">GET</option><option value="POST">POST</option></select></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>DATA:<textarea rows="5" cols="100" name="DATA"></textarea></td>
				</tr>
				
				
				<tr><td><input type="submit" value="SUBMIT"></td></tr>
			</table>
		</form>
	</div>
</body>
</html>