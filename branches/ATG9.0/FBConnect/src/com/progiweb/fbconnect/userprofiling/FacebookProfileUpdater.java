package com.progiweb.fbconnect.userprofiling;

import atg.servlet.DynamoHttpServletRequest;

/**
 * Interface to be implemented by all classes that want to modify an ATG profile property when something happens on the Facebook side
 * (e.g. user login or logout)
 */
public interface FacebookProfileUpdater {

  /**
   * Updates a profile according to the information in the request
   * @param pRequest dynamo request
   */
  public void update (DynamoHttpServletRequest pRequest);

} // end interface
