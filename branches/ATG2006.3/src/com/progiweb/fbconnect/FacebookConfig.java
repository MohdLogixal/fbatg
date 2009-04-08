package com.progiweb.fbconnect;

import atg.nucleus.GenericService;

public class FacebookConfig extends GenericService {

  private String mProfilePath;
  private String mApiKey;
  private String mSecretKey;
  private String mStaticUrl;
  private String mUserIdPrefix = "";
  private String mUserIdSuffix = "";


  public String getApiKey () {
    return mApiKey;
  }

  public void setApiKey (String apiKey) {
    this.mApiKey = apiKey;
  }

  public String getSecretKey () {
    return mSecretKey;
  }

  public void setSecretKey (String secretKey) {
    this.mSecretKey = secretKey;
  }

  public String getStaticUrl () {
    return mStaticUrl;
  }

  public void setStaticUrl (String staticUrl) {
    this.mStaticUrl = staticUrl;
  }

  public String getProfilePath () {
    return mProfilePath;
  }

  public void setProfilePath (String profilePath) {
    this.mProfilePath = profilePath;
  }

  public String getUserIdPrefix () {
    return mUserIdPrefix;
  }

  public void setUserIdPrefix (String userIDPrefix) {
    this.mUserIdPrefix = userIDPrefix;
  }

  public String getUserIdSuffix () {
    return mUserIdSuffix;
  }

  public void setUserIdSuffix (String userIDSuffix) {
    this.mUserIdSuffix = userIDSuffix;
  }
}
