package org.elephantt.javabook.client;

/**
 * Exception returned when the query resulted in more than 1 result
 * <p/>
 * Modified by Progiweb
 */
public class TooManyResultsException extends FacebookException {

  private static final long serialVersionUID = -3462348420435978127L;

  public TooManyResultsException (String msg) {
    super(msg);
  }
}
