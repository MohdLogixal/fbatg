package org.elephantt.javabook.client;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Session-less client for the Facebook API. For operations that require a user session, see FacebookSessionClient.
 *
 * Modified by Progiweb (Java 1.4 compatibility)
 *
 * @see FacebookSessionClientImpl
 */
public class FacebookClient {
  private FacebookRpcClient rpcClient;

  public FacebookClient (FacebookRpcClient rpcClient) {
    this.rpcClient = rpcClient;
  }

  /**
   * Exchanges the auth token for a user session.
   */
  public FacebookSession createSessionKeyForAuthToken (String authToken) {
    List params = new ArrayList();
    params.add(new Parameter("auth_token", authToken));
    JSON json = rpcClient.call("facebook.auth.getSession", params);
    assert json instanceof JSONObject;
    JSONObject obj = (JSONObject) json;
    return new FacebookSession(obj.getString("session_key"), obj.getInt("uid"));
  }

  public void setFbml (String fbml, int uid) {
    List params = new ArrayList();
    params.add(new Parameter("markup", fbml));
    params.add(new Parameter("uid", new Integer(uid)));
    rpcClient.call("facebook.profile.setFBML", params);
  }
}
