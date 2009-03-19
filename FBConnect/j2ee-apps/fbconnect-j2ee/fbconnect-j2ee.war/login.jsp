<%@ taglib uri="/dsp" prefix="dsp" %>
<dsp:page>

<dsp:importbean bean="/atg/userprofiling/Profile"/>
<dsp:importbean bean="/atg/userprofiling/ProfileFormHandler"/>
<dsp:importbean bean="/atg/dynamo/droplet/Switch"/>

<html>

<head>
  <title>Facebook Connect for ATG - ATG Login Page</title>
</head>

<body>
  <h1>Facebook Connect for ATG - ATG Login Page</h1>

  <dsp:droplet name="Switch">
    <dsp:param name="value" bean="Profile.transient"/>
    <dsp:oparam name="false">
      <h3>Hello <dsp:valueof bean="Profile.firstName"/> <dsp:valueof bean="Profile.lastName"/> !</h3>
      <p style="color:green">You are logged in !</p>
    </dsp:oparam>
    <dsp:oparam name="true">
      <h3>Hello guest !</h3>
      <dsp:form formid="loginform" action="index.jsp" method="POST">
        Login name:
        <dsp:input bean="ProfileFormHandler.value.login" name="login" type="text"/><br>
        Password:
        <dsp:input bean="ProfileFormHandler.value.password" name="password" type="password"/><br>
        <dsp:input bean="ProfileFormHandler.loginSuccessURL" type="hidden" value="index.jsp"/>
        <dsp:input bean="ProfileFormHandler.login" type="submit" value="Login"/>
      </dsp:form>
    </dsp:oparam>
  </dsp:droplet>

</body>
</html>
</dsp:page>