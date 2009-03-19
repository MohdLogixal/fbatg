package com.progiweb.fbconnect.session;

import atg.nucleus.GenericService;
import java.util.HashMap;

/**
 * Provides the context for the Facebook connector. Configured as a session-scoped component
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookConnectContext extends GenericService {

  public static String USER_ID = "user";
  public static String SESSION_KEY = "session_key";
  public static String SESSION_SECRET = "ss";
  public static String EXPIRATION = "expires";

  private HashMap mContextProperties;

  public FacebookConnectContext () {
    mContextProperties = new HashMap();
  }

  public void setPropertyValue (String key, String value) {
    mContextProperties.put(key, value);
  }

  public Object getPropertyValue (String key) {
    return mContextProperties.get(key);
  }

}
