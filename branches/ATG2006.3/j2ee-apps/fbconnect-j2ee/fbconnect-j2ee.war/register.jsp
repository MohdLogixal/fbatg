<%@ taglib uri="/dsp" prefix="dsp" %>

<dsp:page>
<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler"/>
<dsp:importbean bean="/atg/userprofiling/Profile"/>

<html>
<head>
  <title>Facebook Connect for ATG - Register</title>
  <link rel="stylesheet" type="text/css" href="style.css" media="all">
</head>
<body>
  <div class="content">
  <div class="main">
    <dsp:form action="register.jsp" method="post">
      <dsp:droplet name="/atg/dynamo/droplet/ErrorMessageForEach">
        <dsp:oparam name="output">
          <dsp:valueof param="message"/>
        </dsp:oparam>
        <dsp:oparam name="outputStart"><li class="error"></dsp:oparam>
        <dsp:oparam name="outputEnd"></li></dsp:oparam>
      </dsp:droplet>

  		<dsp:droplet name="/atg/dynamo/droplet/Switch">
    	  <dsp:param name="value" bean="Profile.facebookUserId"/>
    		<dsp:oparam name="unset">
          <dsp:input bean="ProfileFormHandler.confirmPassword" type="hidden" value="false"/>
  			  Login: <dsp:input bean="ProfileFormHandler.value.login" name="login" size="24" type="text"/><br/>
          Password: <dsp:input bean="ProfileFormHandler.value.password" name="password" size="24" type="password"/><br/>
  			  First name: <dsp:input bean="ProfileFormHandler.value.firstName" name="firstName" size="24" type="text"/><br>
  		    Last name: <dsp:input bean="ProfileFormHandler.value.lastName" name="lastName" size="24" type="text"/><br>
    		</dsp:oparam>
   			<dsp:oparam name="default">
    		  <dsp:getvalueof id="fbid" bean="Profile.facebookUserId"/>
    			<dsp:input bean="ProfileFormHandler.value.id" name="id" value="${fbid}@facebook" type="hidden"/>
    			<dsp:input bean="ProfileFormHandler.value.login" name="login" value="${fbid}@facebook" type="hidden"/>
    			<dsp:input bean="ProfileFormHandler.value.password" name="password" value="${fbid}@secretKey" type="hidden"/>
    			<h3>Hello <dsp:valueof bean="Profile.firstName"/> <dsp:valueof bean="Profile.lastName"/> !</h3><br/>
           Please, fill in some additional information :<br/>
    		</dsp:oparam>
  		</dsp:droplet>
  		Email: <dsp:input bean="ProfileFormHandler.value.email" name="email" type="text"/><br/>
      <dsp:input bean="ProfileFormHandler.createSuccessURL" type="hidden" value="index.jsp"/>
      <br>
      <dsp:input bean="ProfileFormHandler.create" type="Submit" value="Complete your registration"/>

      <dsp:droplet name="/atg/dynamo/droplet/Switch">
    	  <dsp:param name="value" value="Profile.facebookUserId"/>
    		<dsp:oparam name="unset">
          <dsp:input bean="ProfileFormHandler.cancelURL" type="hidden" value="index.jsp"/>
          <dsp:input bean="ProfileFormHandler.cancel" type="Submit" value="Cancel"/>
        </dsp:oparam>
      </dsp:droplet>
    </dsp:form>
  </div>
  </div>
</body>
</html>

</dsp:page>