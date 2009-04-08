package org.elephantt.javabook.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSON;

import java.util.Collection;

/**
 * This is an interface so that applications can easily implement mocks for testing.
 * <p/>
 * Modified by Progiweb (Java 1.4 compatibility)
 */
public interface FacebookSessionClient {
  boolean isAppAdded ();

  JSONArray query (String query);

  JSONObject queryForObject (String query);

  Object queryForBean (String query, Class clazz);

  Object[] queryForBeans (String query, Class clazz);

  int[] queryForInts (String query);

  Object queryForSingleObject (String query, Class clazz);

  Integer queryForInt (String query);

  void publishActionToUser (String title, String body, String templateBundleId, Collection images, Integer priority);

  void publishStoryToUser (String title, String body, String templateBundleId, Collection images, Integer priority);

  Collection createStoryParam (String title, String body, String templateBundleId, Collection images, Integer priority);

  void setFbml (String fbml);

  String getFbml ();

  String sendRequest (String type, String content, Collection recipientUids, String imageUrl, boolean isInvite);

  /**
   * The notification and email parameters are a very stripped-down set of FBML which allows only tags
   * that result in just text and links, and in the email, linebreaks. There is one additional tag
   * supported within the email parameter - use fb:notif-subject around the subject of the email.
   *
   * @param notification
   * @param email
   * @param recipientUids
   * @return null or a string. If a string is returned it is a URL that the calling application should
   *         redirect the user to in order to confirm sending of the notification.
   * @throws InvalidSessionException if the session is no longer valid
   * @throws FacebookException       if another type of Facebook exception occurs
   */
  String sendNotification (String notification, String email, Collection recipientUids);

  JSON call (String method, Collection parameters);
}
