<%@ taglib uri="/dspTaglib" prefix="dsp" %>
<%@ page import="atg.servlet.*" %>
<%@ page import="com.progiweb.fbconnect.*" %>
<%@ page import="java.util.Enumeration" %>
<dsp:page>

<dsp:importbean bean="/atg/dynamo/Configuration"/>
<html>
<head>
<title>fbconnect logged Page</title>
</head>
<body>
<h1>fbconnect logged Test Page</h1>

<br/>
<dsp:droplet name="/fbconnect/droplet/FacebookDroplet">
	<dsp:oparam name="output">
		Droplet message :<dsp:valueof param="session_status_message"/><br/>
		
		Your id : <dsp:valueof param="user_id"/><br/>
		Your name is : <dsp:valueof param="name"/><br/>
		Your first name is : <dsp:valueof param="first_name"/><br/>
		Your last name is :<dsp:valueof param="last_name"/><br/>
		Your pic : <br/><img src="<dsp:valueof param="pic"/>"/><br/>
		Your pic small : <br/><img src="<dsp:valueof param="pic_small"/>"/><br/>
		Your pic big : <br/><img src="<dsp:valueof param="pic_big"/>"/><br/>
		Your pic square : <br/><img src="<dsp:valueof param="pic_square"/>"/><br/>
		Your pic square with logo : <br/><img src="<dsp:valueof param="pic_square_with_logo"/>"/><br/>
		
	</dsp:oparam>
</dsp:droplet>

</body>
</html>
</dsp:page>
<%-- @version $Id$$Change: 425088 $--%>
