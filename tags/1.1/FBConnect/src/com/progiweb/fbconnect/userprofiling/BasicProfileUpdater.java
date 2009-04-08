package com.progiweb.fbconnect.userprofiling;

import atg.nucleus.GenericService;
import atg.servlet.DynamoHttpServletRequest;
import atg.userprofiling.Profile;
import com.progiweb.fbconnect.session.FacebookSessionClient;
import net.sf.json.JSONObject;
import java.text.MessageFormat;

/**
 * Implements the FacebookProfileUpdater class to update the user first and last name
 *
 * @author $Author$
 * @version $Revision$
 */
public class BasicProfileUpdater extends GenericService implements FacebookProfileUpdater {

  // the FQL query to retrieve the first and last name
  private static final String FQL_QUERY = "SELECT name,first_name,last_name FROM user WHERE uid={0}";

  // Nucleus component path of the Facebook session client component
  private String mFacebookClientPath;

  // Nucleus component path of the Profile component
  private String mProfilePath;


  /**
   * Issues a FQL request to get the user's first and last name and updates the ATG profile with the returned values
   * @param pRequest dynamo request
   */
  public void update (DynamoHttpServletRequest pRequest) {
    String ctx = "update - ";

    // resolve those session-scoped components
    FacebookSessionClient fbClient = (FacebookSessionClient)pRequest.resolveName(mFacebookClientPath);
    Profile profile = (Profile)pRequest.resolveName(mProfilePath);

    if (profile != null && fbClient != null) {
      // compose and execute the fql query
      Object[] args = {fbClient.getFacebookUserId()};
      String fqlQuery = MessageFormat.format(FQL_QUERY, args);
      JSONObject resultFQL = fbClient.queryForObject(fqlQuery);

      // update the ATG profile properties
      profile.setPropertyValue("firstName", resultFQL.get("first_name"));
      profile.setPropertyValue("lastName", resultFQL.get("last_name"));
      profile.setPropertyValue("facebookUserId", fbClient.getFacebookUserId());

      if (isLoggingDebug()) {
        logDebug(ctx + "updating: " + resultFQL);
      }
    } else {
      if (isLoggingError()) {
        logError(ctx + "could not resolve " + mProfilePath + " or " + mFacebookClientPath);
      }
    }
  } // end update


  public String getFacebookClientPath () {
    return mFacebookClientPath;
  }

  public void setFacebookClientPath (String pFacebookClientPath) {
    mFacebookClientPath = pFacebookClientPath;
  }

  public String getProfilePath () {
    return mProfilePath;
  }


  public void setProfilePath (String pProfilePath) {
    mProfilePath = pProfilePath;
  }
}
