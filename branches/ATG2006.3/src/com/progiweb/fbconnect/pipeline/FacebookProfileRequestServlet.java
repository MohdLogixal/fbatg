package com.progiweb.fbconnect.pipeline;

import atg.servlet.pipeline.InsertableServletImpl;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.security.ProfileIdentityManager;
import atg.userprofiling.Profile;
import atg.userprofiling.ProfileRequestServlet;
import com.progiweb.fbconnect.FacebookConfig;
import com.progiweb.fbconnect.userprofiling.FacebookProfileUpdater;
import com.progiweb.fbconnect.userprofiling.FbProfileTools;
import com.progiweb.fbconnect.session.FacebookConnector;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 * Pipeline servlet that logs the user into ATG if logged into Facebook
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookProfileRequestServlet extends InsertableServletImpl {

  // handles logins
  private String mIdentityManagerPath;

  // path to the Profile component
  private String mProfilePath;

  // configuration of this Facebook app
  private FacebookConfig mFbConfig;

  // the Facebook connector
  private FacebookConnector mFbConnector;

  // the service that updates the ATG profile according to the informations in Facebook
  private FacebookProfileUpdater mProfileUpdater;

  // the service that creates new profiles in ATG
  private FbProfileTools mFbProfileTools;

  // set this to true to have the ATG profile updated to reflect the informations in Facebook
  private boolean mUpdateProfile;


  /**
   * Main service method
   * @param pRequest dynamo request
   * @param pResponse dynamo response
   * @throws IOException if errors
   * @throws ServletException if errors
   */
  public void service (DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws IOException, ServletException {
    String ctx = "service - ";

    // find out if a Profile is associated to this request/session
    Profile profile = (Profile) pRequest.resolveName(mProfilePath);
    if (profile == null) {
      if (isLoggingError()) {
        logError(ctx + "no ATG profile associated with the current request");
      }
    } else {
      String authStatus = "not_logged"; // anonymous by default
      if (null != pRequest.getParameter(ProfileRequestServlet.LOGOUT_PARAM)) {
        if (isLoggingDebug()) {
          logDebug(ctx + "logout request received");
        }
        authStatus = "not_logged";
      } else {
        // log in a transient profile, if already logged into facebook
        if (profile.isTransient()) {
          if (mFbConnector.isLoggedInFacebook(pRequest, pResponse)) {
            // this user is logged into facebook, synthetize a user id
            String fbUserId = mFbConnector.getUserId(pRequest);
            String dpsUserId = mFbConfig.getUserIdPrefix() + fbUserId + mFbConfig.getUserIdSuffix();

            // try to login into ATG
            if (isLoggingDebug()) {
              logDebug(ctx + "trying to log facebook user " + fbUserId + " into ATG as user " + dpsUserId);
            }
            ProfileIdentityManager identityManager = (ProfileIdentityManager)pRequest.resolveName(mIdentityManagerPath);
            if (identityManager.assumeIdentityById(dpsUserId)) {
              if (isLoggingDebug()) {
                logDebug(ctx + "successfully logged in Facebook user with DPS User Id = " + dpsUserId);
              }
              authStatus = "FB_logged";
            } else {
              if (isLoggingDebug()) {
                logDebug(ctx + "Facebook user with DPS User Id = " + dpsUserId + " not found in ATG profile database");
              }
              authStatus = "FB_new_user";
            }
            if (mUpdateProfile) {
              mProfileUpdater.update(pRequest);
            }
          } else {
            if (isLoggingDebug()) {
              logDebug(ctx + "user is not logged in (transient)");
            }
          }
        }
      }
      pRequest.setAttribute("authStatus", authStatus);
    }
    passRequest(pRequest, pResponse);
  } // end service

  public String getIdentityManagerPath () {
    return mIdentityManagerPath;
  }

  public void setIdentityManagerPath (String pIdentityManagerPath) {
    mIdentityManagerPath = pIdentityManagerPath;
  }

  public String getProfilePath () {
    return mProfilePath;
  }

  public void setProfilePath (String pProfilePath) {
    mProfilePath = pProfilePath;
  }

  public FacebookConfig getFbConfig () {
    return mFbConfig;
  }

  public void setFbConfig (FacebookConfig pFbConfig) {
    mFbConfig = pFbConfig;
  }

  public FacebookConnector getFbConnector () {
    return mFbConnector;
  }

  public void setFbConnector (FacebookConnector pFbConnector) {
    mFbConnector = pFbConnector;
  }

  public FacebookProfileUpdater getProfileUpdater () {
    return mProfileUpdater;
  }

  public void setProfileUpdater (FacebookProfileUpdater pProfileUpdater) {
    mProfileUpdater = pProfileUpdater;
  }

  public boolean isUpdateProfile () {
    return mUpdateProfile;
  }

  public void setUpdateProfile (boolean pUpdateProfile) {
    mUpdateProfile = pUpdateProfile;
  }

  public FbProfileTools getFbProfileTools () {
    return mFbProfileTools;
  }

  public void setFbProfileTools (FbProfileTools pFbProfileTools) {
    mFbProfileTools = pFbProfileTools;
  }
} // end class
