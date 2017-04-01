<%@page import="org.jivesoftware.util.JiveGlobals"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EXT IFRAME</title>
        
        <link type="text/css" rel="stylesheet" href="../../ext/resources/css/ext-all.css" />
        <link type="text/css" rel="stylesheet" href="../../ext/plugins/fileupload/file-upload.css" />
        <link type="text/css" rel="stylesheet" href="ucstarExtjsChatSession.css" />
        <script type="text/javascript" src="../../ext/ext-base.js">
        </script>
        <script type="text/javascript" src="../../ext/ext-all.js">
       	</script>
        <script type="text/javascript" src="../../ext/TabCloseMenu.js">
        </script>
        <script type="text/javascript" src="../../ext/plugins/fileupload/FileUploadField.js">
        </script>
        <script type="text/javascript" src="../../js/ucstarutil.js">
        </script>
        <script type="text/javascript" src="../../ext/locale/ext-lang-zh_CN.js">
        </script>
		<script type="text/javascript" src="../../js/i18n/ucstar_i18n-zh-CN.js">
        </script>
		
		<script type="text/javascript" src="ucmFriendsShareControl.js">
		</script>
		<script>Ext.BLANK_IMAGE_URL='../../images/blank.gif';</script>
		<%
			
		%>
		
		<script language="JavaScript">
			var theGrid = null;
			var mainComp = null;
			var theMask = null;
            function extInit() {
            	theMask = new Ext.LoadMask(document.body, {msg : '...'});
            	mainComp = new UcmFriendsShareControl();
        		theGrid = mainComp.initGrid();
        		theGrid.render(document.body);
        		mainComp.searchData();
        		window.onresize();
            }
            
           	window.onresize = function(){
        		theGrid.setWidth(0);
        		theGrid.setWidth(Ext.get('content').getWidth());
        		/*theGrid.setHeight(Ext.get('content').getHeight());*/
			};
			
			
        </script>
<title></title>
</head>

<body id="content" leftmargin="1" topmargin="0" marginwidth="0" marginheight="0"
	style="width:100%;height:100%" onload="extInit();">
</body>
</html>

