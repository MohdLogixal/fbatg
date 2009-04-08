package com.progiweb.fbconnect.userprofiling;

import atg.userprofiling.ProfileTools;
import atg.userprofiling.Profile;
import atg.nucleus.GenericService;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryException;

/**
 * Creates ATG profiles programmatically
 *
 * @author $Author$
 * @version $Revision$
 */
public class FbProfileTools extends GenericService {

  // the ProfileTools component
  private ProfileTools mProfileTools;


  /**
   * Creates a new profile with the given id and default type, then sets the datasource of the given profile object to
   * the newly created item
   * @param pNewProfileId id of the new profile
   * @param pProfile profile object
   * @return true if the new profile was created successfully, false otherwise
   */
  public boolean createNewUser (String pNewProfileId, Profile pProfile) {
    return createNewUser(pNewProfileId, getProfileTools().getDefaultProfileType(), pProfile);
  }


  /**
   * Creates a new profile with the given id and type, then sets the datasource of the given profile object to
   * the newly created item
   * @param pNewProfileId id of the new profile
   * @param pProfileType type of the new profile
   * @param pProfile profile object
   * @return true if the new profile was created successfully, false otherwise
   */
  public boolean createNewUser (String pNewProfileId, String pProfileType, Profile pProfile) {
    String ctx = "createNewUser - ";
    try {
      RepositoryItem item = getProfileTools().getProfileRepository().createItem(pNewProfileId, pProfileType);
      pProfile.setDataSource(item);
      return true;
    }
    catch (RepositoryException re) {
      if (isLoggingError()) {
        logError(ctx + "RepositoryException creating profile with id " + pNewProfileId, re);
      }
    }
    return false;
  } // end createNewUser

  public ProfileTools getProfileTools () {
    return mProfileTools;
  }

  public void setProfileTools (ProfileTools pProfileTools) {
    mProfileTools = pProfileTools;
  }
} // end class

