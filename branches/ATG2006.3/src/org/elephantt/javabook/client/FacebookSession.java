package org.elephantt.javabook.client;

public class FacebookSession {
  private final String key;
  private final int uid;

  public FacebookSession (String key, int uid) {
    this.key = key;
    this.uid = uid;
  }

  public String getKey () {
    return key;
  }

  public int getUid () {
    return uid;
  }
}
