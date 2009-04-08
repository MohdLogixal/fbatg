package com.progiweb.fbconnect.session;

import atg.nucleus.GenericService;
import atg.nucleus.ServiceException;
import com.progiweb.fbconnect.FacebookConfig;
import org.elephantt.javabook.client.FacebookRpcClient;
import org.elephantt.javabook.client.FacebookSession;
import org.elephantt.javabook.client.FacebookSessionClientImpl;
import java.util.Collection;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Facade to the Javabook library. Configure as a session-scoped component
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookSessionClient extends GenericService implements org.elephantt.javabook.client.FacebookSessionClient {

  private FacebookConfig mFbConfig;
  private org.elephantt.javabook.client.FacebookSessionClient mFbSessionClient;
  private String mFacebookUserId;
  private FacebookConnectContext mConnectContext;

  /**
   * Starts the service
   * @throws ServiceException if errors starting the service
   */
  public void doStartService () throws ServiceException {
    String ctx = "doStartService - ";
    super.doStartService();
    mFacebookUserId = (String)mConnectContext.getPropertyValue(FacebookConnectContext.USER_ID);
    String sessionKey = (String)mConnectContext.getPropertyValue(FacebookConnectContext.SESSION_KEY);
    if (isLoggingDebug()) {
      logDebug(ctx + "session key = " + sessionKey);
    }
    FacebookRpcClient fbRpcClient = new FacebookRpcClient(mFbConfig.getApiKey(), mFbConfig.getSecretKey());
    FacebookSession session = new FacebookSession(sessionKey, Integer.parseInt(mFacebookUserId));
    mFbSessionClient = new FacebookSessionClientImpl(fbRpcClient, session);
  }

  // getters and setters
  public FacebookConfig getFbConfig () {
    return mFbConfig;
  }

  public void setFbConfig (FacebookConfig pFbConfig) {
    this.mFbConfig = pFbConfig;
  }

  public FacebookConnectContext getConnectContext () {
    return mConnectContext;
  }

  public void setConnectContext (FacebookConnectContext pConnectContext) {
    this.mConnectContext = pConnectContext;
  }

  public String getFacebookUserId () {
    return mFacebookUserId;
  }

  public void setFacebookUserId (String pFacebookUserId) {
    this.mFacebookUserId = pFacebookUserId;
  }


  // implementation of the FacebookSessionClient interface
  public JSON call (String pMethod, Collection pParameters) {
    return mFbSessionClient.call(pMethod, pParameters);
  }

  public Collection createStoryParam (String pTitle, String pBody, String pTemplateBundleId, Collection pImages, Integer pPriority) {
    return mFbSessionClient.createStoryParam(pTitle, pBody, pTemplateBundleId, pImages, pPriority);
  }

  public String getFbml () {
    return mFbSessionClient.getFbml();
  }

  public boolean isAppAdded () {
    return mFbSessionClient.isAppAdded();
  }

  public void publishActionToUser (String pTitle, String pBody, String pTemplateBundleId, Collection pImages, Integer pPriority) {
    mFbSessionClient.publishActionToUser(pTitle, pBody, pTemplateBundleId, pImages, pPriority);
  }

  public void publishStoryToUser (String pTitle, String pBody, String pTemplateBundleId, Collection pImages, Integer pPriority) {
    mFbSessionClient.publishStoryToUser(pTitle, pBody, pTemplateBundleId, pImages, pPriority);
  }

  public JSONArray query (String query) {
    return mFbSessionClient.query(query);
  }

  public Object queryForBean (String query, Class clazz) {
    return mFbSessionClient.queryForBean(query, clazz);
  }

  public Object[] queryForBeans (String query, Class clazz) {
    return mFbSessionClient.queryForBeans(query, clazz);
  }

  public Integer queryForInt (String query) {
    return mFbSessionClient.queryForInt(query);
  }

  public int[] queryForInts (String query) {
    return mFbSessionClient.queryForInts(query);
  }

  public JSONObject queryForObject (String query) {
    return mFbSessionClient.queryForObject(query);
  }

  public Object queryForSingleObject (String query, Class clazz) {
    return mFbSessionClient.queryForSingleObject(query, clazz);
  }

  public String sendNotification (String pNotification, String pEmail, Collection pRecipientUids) {
    return mFbSessionClient.sendNotification(pNotification, pEmail, pRecipientUids);
  }

  public String sendRequest (String pType, String pContent, Collection pRecipientUids, String pImageUrl, boolean pIsInvite) {
    return mFbSessionClient.sendRequest(pType, pContent, pRecipientUids, pImageUrl, pIsInvite);
  }

  public void setFbml (String fbml) {
    mFbSessionClient.setFbml(fbml);
  }
}
