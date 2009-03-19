package com.progiweb.fbconnect.userprofiling;

import atg.userprofiling.ProfileFormHandler;
import atg.userprofiling.Profile;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import com.progiweb.fbconnect.session.FacebookConnector;

/**
 * Extends ATG ProfileFormHandler class to manage authentication statuses for Facebook Users
 *
 * @author $Author$
 * @version $Revision$
 */
public class FbProfileFormHandler extends ProfileFormHandler {

  private FacebookConnector mFbConnector;

  /**
   * Resets the authStatus property from the profile that's logging out
   * @param pRequest dynamo request
   * @param pResponse dynamo response
   * @throws ServletException if errors
   * @throws IOException if errors
   */
  protected void preLogoutUser (DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws ServletException, IOException {
    Profile profile = getProfile();
    if (profile != null) {
      // erase the auth_status property
      profile.setPropertyValue("authStatus", "not_logged");
    }
    setLogoutSuccessURL(getFbConnector().getLogoutFacebookURL(pRequest, getLogoutSuccessURL()));
    super.preLogoutUser(pRequest, pResponse);
  }


  public FacebookConnector getFbConnector () {
    return mFbConnector;
  }

  public void setFbConnector (FacebookConnector pFbConnector) {
    mFbConnector = pFbConnector;
  }
} // end class

