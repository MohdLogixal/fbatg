package org.elephantt.javabook.client;

import java.util.*;

import net.sf.json.*;

/**
 * Facebook client for operations requiring a user session.
 * <p/>
 * Modified by Progiweb (Java 1.4 compatibility)
 * <p/>
 * TODO:
 * - what do facebook.fbml.refreshRefUrl and facebook.fbml.refreshImgSrc do?
 */
public class FacebookSessionClientImpl implements FacebookSessionClient {

  private final FacebookRpcClient rpcClient;
  private final FacebookSession session;

  public FacebookSessionClientImpl (FacebookRpcClient rpcClient, FacebookSession session) {
    this.rpcClient = rpcClient;
    this.session = session;
  }

  public boolean isAppAdded () {
    return extractBoolean(call("facebook.users.isAppAdded", Collections.EMPTY_LIST));
  }

  private boolean extractBoolean (JSON json) {
    assert json instanceof JSONObject;
    return ((JSONObject) json).getBoolean("returnValue");
  }

  /**
   * Sends the specified FQL query and returns the responses.
   *
   * @param query
   * @return
   */
  public JSONArray query (String query) {
    List params = new ArrayList();
    params.add(new Parameter("query", query));
    JSON json = call("facebook.fql.query", params);
    assert json.isArray();
    return (JSONArray) json;
  }

  /**
   * Sends the specified FQL query and returns a singular result.
   *
   * @param query
   * @return Query result object, or null
   * @throws TooManyResultsException If not 0 or 1 results.
   */
  public JSONObject queryForObject (String query) {
    if (query == null) {
      throw new IllegalArgumentException();
    }

    JSONArray arr = query(query);
    if (arr.size() == 0) {
      return null;
    } else if (arr.size() == 1) {
      return arr.getJSONObject(0);
    } else {
      throw new TooManyResultsException("Expected 1 results, instead there were " + arr.size());
    }
  }

  public Object queryForBean (String query, Class clazz) {
    JSONObject obj = queryForObject(query);
    return obj != null ? FqlUtil.convertToBean(query, obj, clazz) : null;
  }

  /**
   * Queries for
   *
   * @param query
   * @param clazz
   * @return
   */
  public Object[] queryForBeans (String query, Class clazz) {
    JSONArray arr = query(query);
    Object[] objArr = new Object[arr.size()];
    Iterator iterator = arr.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      JSONObject obj = (JSONObject) iterator.next();
      objArr[i++] = FqlUtil.convertToBean(query, obj, clazz); // TODO: ridiculous to do this in a loop
    }
    return objArr;
  }

  public int[] queryForInts (String query) {
    try {
      JSONArray jsonArray = query(query);
      int[] arr = new int[jsonArray.size()];
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject obj = jsonArray.getJSONObject(i);
        Iterator keyIterator = obj.keys();
        if (keyIterator.hasNext()) {
          arr[i] = obj.getInt((String) keyIterator.next());
        }
      }
      return arr;
    } catch (JSONException e) {
      throw new InvalidFormatException(e);
    }
  }


  public Object queryForSingleObject (String query, Class clazz) {
    return FqlUtil.convertToBean(query, queryForObject(query), clazz);
  }

  public Integer queryForInt (String query) {
    try {
      JSONObject obj = queryForObject(query);
      Iterator keyIterator = obj.keys();
      if (keyIterator.hasNext()) {
        return new Integer(obj.getInt((String) keyIterator.next()));
      }
    } catch (JSONException e) {
      throw new InvalidFormatException(e);
    }
    return null;
  }

  public void publishActionToUser (String title, String body, String templateBundleId, Collection images, Integer priority) {
    call("facebook.feed.publishUserAction", createStoryParam(title, body, templateBundleId, images, priority));
  }

  public void publishStoryToUser (String title, String body, String templateBundleId, Collection images, Integer priority) {
    call("facebook.feed.publishUserAction", createStoryParam(title, body, templateBundleId, images, priority));
  }

  public Collection createStoryParam (String title, String body, String templateBundleId, Collection images, Integer priority) {
    if (title == null) {
      throw new IllegalArgumentException("title must be non-null");
    }
    if (images != null && images.size() > 4) {
      throw new IllegalArgumentException("images too long; max size is 4");
    }
    int priorityVal = priority.intValue();
    if (priority != null && (priorityVal < 1 || priorityVal > 100)) {
      throw new IllegalArgumentException("priority is out of range; must be > 0 and < 100");
    }

    // note: the parameter names below must match the tokens in the Facebook feed templates
    List params = new LinkedList();
    Map templateData = new HashMap();
    if (title != null) {
      templateData.put("actionsubject", title);
    }
    if (body != null) {
      templateData.put("body", body);
    }
    params.add(new Parameter("template_data", JSONObject.fromObject(templateData)));    
    if (templateBundleId != null) {
      params.add(new Parameter("template_bundle_id", templateBundleId));
    }
    if (priority != null) {
      params.add(new Parameter("priority", priority));
    }
    if (images != null) {
      int i = 1;
      for (Iterator it = images.iterator(); it.hasNext(); ) {
        Image img = (Image)it.next();
        params.add(new Parameter("image_" + i, img.getUrl()));
        if (img.getLink() != null) {
          params.add(new Parameter("image_" + i + "_link", img.getLink()));
        }
        i++;
      }
    }

    return params;
  }

  public void setFbml (String fbml) {
    List params = new ArrayList();
    params.add(new Parameter("markup", fbml));
    params.add(new Parameter("uid", new Integer(session.getUid())));
    call("facebook.profile.setFBML", params);
  }

  public String getFbml () {
    // TODO: verify
    List params = new ArrayList();
    params.add(new Parameter("uid", new Integer(session.getUid())));
    JSON json = call("facebook.profile.getFBML", params);
    JSONObject obj = (JSONObject) json;
    return obj.getString("returnValue");
  }

  // TODO: verify correctness
  private String extractUrl (JSON json) {
    if (json instanceof JSONNull) {
      return null;
    } else if (json instanceof JSONObject) {
      return ((JSONObject) json).getString("url");
    } else {
      throw new FacebookException("unexpected value: " + json);
    }
  }

  // TODO: check correctness. Based on the documentation, I believe this is currently the correct behavior.
  private String extractReturnValue (JSON json) {
    if (json instanceof JSONNull) {
      return null;
    } else if (json instanceof JSONObject) {
      JSONObject jobj = (JSONObject) json;
      if (jobj.has("returnValue")) {
        return jobj.getString("returnValue");
      } else {
        return null;
      }
    } else {
      throw new FacebookException("unexpected value: " + json);
    }
  }

  public String sendRequest (String type, String content, Collection recipientUids, String imageUrl, boolean isInvite) {
    List params = new ArrayList();
    params.add(new Parameter("to_ids", delimit(recipientUids)));
    params.add(new Parameter("type", type));
    params.add(new Parameter("content", content));
    params.add(new Parameter("image", imageUrl));
    params.add(new Parameter("invite", isInvite ? "1" : "0"));
    return extractUrl(call("facebook.notifications.sendRequest", params));
  }

  public String sendNotification (String notification, String email, Collection recipientUids) {
    List params = new ArrayList();
    params.add(new Parameter("to_ids", delimit(recipientUids)));
    params.add(new Parameter("notification", notification));
    params.add(new Parameter("email", email));
    return extractReturnValue(call("facebook.notifications.send", params));
  }

  private String delimit (Collection recipientUids) {
    StringBuffer sb = new StringBuffer();
    for (Iterator it = recipientUids.iterator(); it.hasNext(); ) {
      Integer recipientUid = (Integer)it.next();
      sb.append(recipientUid);
      if (it.hasNext()) {
        sb.append(",");
      }
    }
    return sb.toString();
  }


  /**
   * Calls with the current session
   *
   * @param method
   * @param parameters
   * @return
   */
  public JSON call (String method, Collection parameters) {
    return rpcClient.call(method, session.getKey(), parameters);
  }
}