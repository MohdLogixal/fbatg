package com.progiweb.fbconnect.exception;

/**
 * Connector-specific exception
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookException extends Exception {

  private static final long serialVersionUID = 5406192067554689183L;

  public FacebookException () {
    // no-args constructor
  }

  public FacebookException (String message) {
    super(message);
  }

  public FacebookException (Throwable cause) {
    super(cause);
  }

  public FacebookException (String message, Throwable cause) {
    super(message, cause);
  }
} // end class

