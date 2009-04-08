package org.elephantt.javabook.client;

/**
 * A Facebook exception caused by a Facebook RPC error code.
 *
 * Modified by Progiweb
 */
public class FacebookErrorCodeException extends FacebookException {

  private static final long serialVersionUID = 98815070169539831L;

  private int code;
  private Parameter[] params;

  public FacebookErrorCodeException (int code, String s, Parameter[] params) {
    super(s);
    this.code = code;
    this.params = params;
  }

  public int getCode () {
    return code;
  }

  public Parameter[] getParams () {
    return params;
  }

  public String toString () {
    return super.toString() + " (" + code + ")";
  }

  /*public enum ErrorCode {
    UNKNOWN(1, "An unknown error occurred. Please resubmit the request."),
    SERVICE_NOT_AVAILABLE(2, "The service is not available at this time."),
    MAX_REQUESTS_EXCEEDED(4, "The application has reached the maximum number of requests allowed. More requests are allowed once the time window has completed."),
    UNALLOWED_REMOTE_ADDRESS(5, "The request came from a remote address not allowed by this application."),
    INVALID_PARAMETERS(100, "One of the parameters specified was missing or invalid."),
    INVALID_API_KEY(101, "The api key submitted is not associated with any known application."),
    INVALID_SESSION(102, "The session key was improperly submitted or has reached its timeout. Direct the user to log in again to obtain another key."),
    INVALID_CALL_ID(103, "The submitted call_id was not greater than the previous call_id for this session."),
    INVALID_SIGNATURE(104, "Incorrect signature.");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }*/
}
