<%@ taglib uri="/dsp" prefix="dsp" %>
<%@ taglib uri="/c" prefix="c" %>
<dsp:page>

<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>
<dsp:importbean bean="/atg/dynamo/droplet/IsEmpty"/>
<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler"/>
<dsp:importbean bean="/fbconnect/FacebookConfig"/>

<dsp:getvalueof var="request" bean="/OriginatingRequest" />

<html>

<head>
  <title>Facebook Connect for ATG</title>
  <link rel="stylesheet" type="text/css" href="style.css" media="all">
</head>

<body>
  <div class="content">
  <div class="main">
    <h1>Facebook Connect for ATG</h1>

    <p>
      This mini-application demonstrates the Facebook Connect integration with the ATG framework.
    </p>
    <p>
      How to use this application:
    </p>

    <dsp:droplet name="Switch">
      <dsp:param name="value" bean="Profile.transient"/>
      <dsp:oparam name="false">
        <h3>Hello <dsp:valueof bean="Profile.firstName"/> <dsp:valueof bean="Profile.lastName"/> !</h3>
        <dsp:droplet name="IsEmpty">
          <dsp:param name="value" bean="Profile.facebookUserId"/>
          <dsp:oparam name="false">
            <fb:profile-pic uid="<dsp:valueof bean="Profile.facebookUserId"/>" facebook-logo="true" size="thumb"></fb:profile-pic>
          </dsp:oparam>
        </dsp:droplet>
        <p style="color:green">You look very nice today!</p>
      </dsp:oparam>
      <dsp:oparam name="true">
        <h3>Hello guest !</h3>
        <p style="color:red">You are not logged in !</p>
      </dsp:oparam>
    </dsp:droplet>

    <dsp:droplet name="Switch">
      <dsp:param name="value" bean="Profile.transient"/>
      <dsp:oparam name="false">

        <br><br>
        <dsp:form formid="logoutform" action="index.jsp" method="POST">
          <dsp:input bean="ProfileFormHandler.logoutSuccessURL" type="hidden" value="index.jsp"/>
          <dsp:input bean="ProfileFormHandler.logout" type="Submit" value="Logout"/>
        </dsp:form>
      </dsp:oparam>
    </dsp:droplet>

  	<script src="<dsp:valueof bean="FacebookConfig.staticURL"/>/js/api_lib/v0.4/FeatureLoader.js.php" type="text/javascript"></script>
  	<script type="text/javascript">
   			FB.init("<dsp:valueof bean="FacebookConfig.apiKey"/>", "xd_receiver.jsp", {"reloadIfSessionStateChanged":true});
  	</script>
  	<script src="fbconnect.js" type="text/javascript"></script>

    <dsp:droplet name="Switch">
      <dsp:param name="value" bean="Profile.transient"/>
      <dsp:oparam name="true">
        <a href="#" onclick="FB.Connect.requireSession(); return false;" >
          <img border="0" id="fb_login_image" src="http://static.ak.fbcdn.net/images/fbconnect/login-buttons/connect_light_medium_long.gif" alt="Connect"/>
        </a>
      </dsp:oparam>
    </dsp:droplet>
  </div>
  </div>

</body>
</html>
</dsp:page>
