package org.elephantt.javabook.client;

/**
 * Exception returned when the session key was invalid for the request (obtain a new FacebookSessionClient)
 * <p/>
 * Modified by Progiweb
 */
public class InvalidSessionException extends FacebookErrorCodeException {

  private static final long serialVersionUID = 508303146209623784L;

  private String session;

  public InvalidSessionException (int code, String message, Parameter[] params, String session) {
    super(code, message, params);
    this.session = session;
  }

  public String getSession () {
    return session;
  }
}
