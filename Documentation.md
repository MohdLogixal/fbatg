# Facebook Connect module for ATG applications #
### Version 1.1 - ATG 9.0 ###

## Brought to you by PROGIWEB ##
This ATG module has been designed and developed by PROGIWEB.

PROGIWEB is a french company whose activity is to support ATG System Integrators and Customers by providing proven expertise in ATG design, development, architecture, push to production, tuning and maintaining.
PROGIWEB is an accredited ATG partner and an official Authorized ATG Training Center.

## Presentation and functionalities ##
With this project we at PROGIWEB aim to produce an ATG module that allows anyone with a Facebook account to seamlessly log in into an ATG application, bypassing the account creation step.

For the end user, the main advantage is that another, potentially redundant set of credentials is not needed. For the application publisher, the main advantage in our view is the simplification of the account creation process (which may lead to increased conversions for example), followed by the interactivity and social network features available via the Facebook API.

The demonstration application displays log in and logout links which use the Facebook Connect API, and shows the user full name and Facebook profile picture once successfully logged in; also it shows how to post short one-line stories to the user's Wall on Facebook (when authorized by the user).

By using the Facebook Connect API, your application can also:

  * publish messages on the user's "wall"
  * publish pictures in the photo album
  * get the list of the user's friends
  * compare the user's friends list with the users already present on your site

What you cannot do with the Facebook Connect API:

  * you cannot get the user's email address
  * you cannot fetch informations from other Facebook applications

This module is licensed under the Apache License, Version 2.0 (see the following section and the LICENSE file included in the distribution).

## Copyright and License Informations, Trademarks ##
Copyright 2009 Progiweb

<pre>
Licensed under the Apache License, Version 2.0 (the "License");<br>
you may not use this file except in compliance with the License.<br>
You may obtain a copy of the License at<br>
<br>
http://www.apache.org/licenses/LICENSE-2.0<br>
<br>
Unless required by applicable law or agreed to in writing, software<br>
distributed under the License is distributed on an "AS IS" BASIS,<br>
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br>
See the License for the specific language governing permissions and<br>
limitations under the License.<br>
</pre>

## Trademarks ##
All marks are the properties of their respective owners.

## External libraries used in this project ##
  * Javabook http://code.google.com/p/javabook/
  * Commons BeanUtils http://commons.apache.org/beanutils/
  * Commons Codec http://commons.apache.org/codec/
  * Commons Collections http://commons.apache.org/collections/
  * Commons HTTP Client http://hc.apache.org/httpclient-3.x/
  * Commons Lang http://commons.apache.org/lang/
  * EZMorph http://ezmorph.sourceforge.net/
  * JSON-lib http://json-lib.sourceforge.net/

## Compatibility ##
The 1.1 release is built with JDK 1.5.0, JBoss 4.2.0 and ATG 9.0.
The 1.0 release is built with JDK 1.4.2, DAS 6.3.0 patch 3 and ATG 2006.3.

## Version history ##
  * 1.1: upgraded to JDK 1.5.0, ATG 9.0; added posting of one-line stories to the user's wall thru an ATG form handler
  * 1.0: initial release

## Facebook Connect specific policies ##
To use the Facebook Connect functionalities on your application there are a number of policies that your application has to adhere to. Those policies are listed at: http://wiki.developers.facebook.com/index.php/Facebook_Connect_Policies

## Facebook API ##
The documentation for the Facebook API is available at: http://wiki.developers.facebook.com/index.php/API

## How to use this module ##
Before doing anything else, you must register your application with Facebook in order to use the Facebook Connect functionalities. Registering your application makes it available to all Facebook users, which in turn will have to authorize your application in order to use it.

To register your application with Facebook, log in to Facebook and go to http://www.facebook.com/developers, then click on the "Set Up New Application" button
The absolute minimum informations that you have to fill in to make your application available are:

  * Application name: a unique and distinguishable name of the application (the name of your site, for example)
  * Authentication: enable "Users" in the "Who can use your application" section
  * Connect URL: an externally-accessible URL to the home page of your application

_Note that the Connect URL must be accessible from the public Internet: this may imply configuring a firewall or reverse proxy to allow HTTP requests from the outside world to get to your application (even on a development or staging box)._

Once you have registered your application with Facebook, go to the "Basic" page and copy the values of the API Key and Secret Key into the `/fbconnect/FacebookConfig.properties` file, as the `apiKey` and `secretKey` properties respectively.

Any user with a Facebook account may then authorize your application by going to the Application Settings page.

## How to build and start the module ##
We use Ant http://ant.apache.org to build this module: to build the module on your system, edit the file `build/fbconnect.properties` and modify the following values according to your configuration

```
# path to the JDK installation
jdk.home=/usr/lib/jvm/java-1.5.0-sun

# path to the root of the ATG installation
atg.root=/usr/local/atg/atg9.0

# path to the root of the JBoss installation
jboss.root=/usr/local/atg/jboss4.2.0
```

then open a command prompt, navigate to the FBConnect directory and type ant:

```
cd dev\facebookatg\FBConnect\build
ant
```

Once the module is built, you can start it by including in the startDynamo command:

```
bin\startDynamo -m FBConnect
```

## How the integration works ##
This integration works with RPC calls to the Facebook API, where JSON objects are exchanged thru HTTP; we elected to use the javabook library to implement the actual communication (see the `FacebookSessionClient` class for instance).

### Authenticating to Facebook ###
When you click on the Facebook Connect button, the Facebook JavaScript library loads a popup where you can enter your Facebook login credentials. When Facebook successfully logs you in, it generates several cookies which are in turn read by the pipeline servlets that we placed in the ATG pipeline to determine the authentication status.

### Authenticating to ATG ###
To enable a transparent authentication for the user, we inserted 2 servlets in the ATG servlet pipeline (DAF pipeline on ATG 9.0): `FacebookProfileRequestServlet` checks if the request carries the Facebook cookies and logs the user into ATG, then calls a `ProfileUpdater` implementation that is responsible to update the ATG user's profile with the informations gathered from Facebook (i.e. first name, last name, Facebook user id to show the profile picture).

A [diagram](http://fbatg.googlecode.com/files/FacebookProfileRequestServlet.png) shows the logical flow of the FacebookProfileRequestServlet.

A second servlet, `FacebookAuthStatusServlet`, can redirect users that log in for the first time to a page where they can complete their own profile (for example with the email address, which the Facebook API does not expose).

A [diagram](http://fbatg.googlecode.com/files/FacebookAuthStatusServlet.png) shows the logical flow of the FacebookAuthStatusServlet.

### Logging out ###
Logging out is the trickiest part of the process, also because it seems that some issues on the Facebook side prevent this from working. We chose to override the standard ATG `ProfileFormHandler` class and to dynamically rewrite the `logoutSuccessURL` to the Facebook logout page: the user is then logged out of ATG, redirected to a Facebook logout page (which clears the Facebook cookies in the browser) and then redirected again to the original `logoutSuccessURL`.

We have however seen that in some occasions the Facebook cookies aren't properly cleared out, which results in the user logging out of Facebook, but still considered as logged in into ATG. In the last days however we haven't seen any problem, so we assume that Facebook has solved the issue on their end.

## Caveats ##
One caveat concerns sending personalized email via the `TemplateEmailSender` component: since this component synthetizes a request with the user's profile, our two pipeline servlets try to intervene and may cause the request to fail. A simple workaround is to specify the `templateRendererServlet` property of the `TemplateEmailSender` component to point to another pipeline that does not include the Facebook servlets.