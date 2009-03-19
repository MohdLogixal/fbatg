package org.elephantt.javabook.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.*;
import java.util.*;

import net.sf.json.*;

/**
 * A client for the Facebook RPC-over-HTTP protocol.
 *
 * Modified by Progiweb (Java 1.4 compatibility, removed logging)
 * <p/>
 * TODO:
 * - Support for JSONP
 */
public class FacebookRpcClient {

  private static final Set RESERVED_PARAM_SET = asSet(new String[]{"method_name", "api_key", "v", "call_id", "session_key", "format", "sig"});
  private static final String DEFAULT_TARGET_API_VERSION = "1.0";
  private static final String DEFAULT_ENDPOINT = "http://api.facebook.com/restserver.php";

  private final String apiKey;
  private final String secret;
  private final URI httpEndpoint;
  private final String targetApiVersion;
  private final HttpClient httpClient;

  public FacebookRpcClient (String apiKey, String secret) {
    this(apiKey, secret, DEFAULT_ENDPOINT, DEFAULT_TARGET_API_VERSION);
  }

  public FacebookRpcClient (String apiKey, String secret, String httpEndpoint, String targetApiVersion) {
    this.apiKey = apiKey;
    this.secret = secret;
    try {
      this.httpEndpoint = new URI(httpEndpoint, true);
    } catch (URIException e) {
      throw new IllegalArgumentException(httpEndpoint + " not a valid URI - " + e.getMessage());
    }
    this.targetApiVersion = targetApiVersion;

    HttpClientParams params = new HttpClientParams(); // TODO
    httpClient = new HttpClient(params);
  }

  public String getApiKey () {
    return apiKey;
  }

  private FacebookPostMethod createPostMethod () {
    FacebookPostMethod method = new FacebookPostMethod();
    method.setRequestHeader("User-Agent", "Javabook 1.0");
    method.setFollowRedirects(false);
    method.setDoAuthentication(false);
    try {
      method.setURI(httpEndpoint);
    } catch (URIException e) {
      throw new RuntimeException("URI not valid: " + httpEndpoint, e);
    }
    return method;
  }

  /**
   * Calls the specified method with the given params.
   *
   * @param method
   * @param params
   * @return
   */
  public JSON call (String method, Collection params) {
    return call(method, null, params);
  }

  public JSON call (String method, String sessionKey, String paramName, Object paramValue) {
    Collection params = new ArrayList(1);
    params.add(new Parameter(paramName, paramValue));
    return call(method, sessionKey, params);
  }

  public JSON call (String method, String sessionKey, Collection params) {
    FacebookPostMethod post = createPostMethod();

    post.addParameter("method", method);
    post.addParameter("api_key", apiKey);
    post.addParameter("v", targetApiVersion);
    if (sessionKey != null) {
      post.addParameter("call_id", String.valueOf(System.currentTimeMillis()));
      post.addParameter("session_key", sessionKey);
    }
    post.addParameter("format", "JSON");

    for (Iterator it = params.iterator(); it.hasNext(); ) {
      Parameter p = (Parameter)it.next();
      if (!RESERVED_PARAM_SET.contains(p.getName())) {
        post.addParameter(p.getName(), p.getValue().toString());
      } else {
        throw new IllegalArgumentException("application cannot use reserved param name: " + p.getName());
      }
    }

    post.sign(secret);

    try {
      long t0 = System.currentTimeMillis();
      int sc = httpClient.executeMethod(post);
      long t1 = System.currentTimeMillis();

      if (sc == 200) {
        String responseBody = extractResponse(post);
        JSON json = JSONSerializer.toJSON(responseBody);
        if (json instanceof JSONObject && ((JSONObject) json).has("error_code")) {
          JSONObject obj = (JSONObject) json;
          FacebookErrorCodeException exception;

          int errCode = obj.getInt("error_code");
          String errMsg = obj.getString("error_msg");
          Parameter[] errParams = createErrorParams(obj.getJSONArray("request_args"));

          if (errCode == 102) {
            exception = new InvalidSessionException(errCode, errMsg, errParams, sessionKey);
          } else {
            exception = new FacebookErrorCodeException(errCode, errMsg, errParams);
          }

          throw exception;
        }

        return json;
      } else {
        throw new FacebookException("Unexpected HTTP status code from " + post.getURI() + ": " + sc);
      }
    } catch (IOException e) {
      throw new FacebookException(e);
    }
  }

  private static final int BUF_SIZE = 4096;
  private static final int MAX_MSG_SIZE = 1024 * 1024 * 10;

  private String extractResponse (FacebookPostMethod post) throws IOException {
    InputStream in = post.getResponseBodyAsStream();
    ByteArrayOutputStream baos = new ByteArrayOutputStream(BUF_SIZE);
    byte[] buf = new byte[BUF_SIZE];
    int len;
    while ((len = in.read(buf)) > 0) {
      if (baos.size() >= MAX_MSG_SIZE) {
        throw new FacebookException("message too large: " + len);
      }
      baos.write(buf, 0, len);
    }

    String responseBody = new String(baos.toByteArray(), "UTF-8");

    // Facebook's JSON seems wrong, and json-lib can't parse it. fix it up.

    // sometimes FB erroneously specifies empty string rather than null
    if (responseBody.equals("\"\"")) {
      responseBody = "null";
    }

    // sometimes they return a naked value, which is not JSON (I think)
    if (!responseBody.startsWith("[") && !responseBody.startsWith("{")) {
      responseBody = "{\"returnValue\":" + responseBody + "}";
    }

    return responseBody;
  }

  private Parameter[] createErrorParams (JSONArray args) {
    Parameter[] errParams = null;
    if (args != null) {
      errParams = new Parameter[args.size()];
      for (int i = 0; i < args.size(); i++) {
        JSONObject arg = args.optJSONObject(i);
        if (arg != null) {
          errParams[i] = new Parameter(arg.optString("key"), arg.optString("value"));
        }
      }
    }
    return errParams;
  }

  private static Set asSet (String[] strings) {
    Set set = new HashSet();
    set.addAll(Arrays.asList(strings));
    return set;
  }
}
