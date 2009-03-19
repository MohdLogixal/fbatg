package org.elephantt.javabook.client;

/**
 * Generic Facebook exception
 *
 * Modified by Progiweb
 */
public class FacebookException extends RuntimeException {

  private static final long serialVersionUID = 8099120059318074911L;

  public FacebookException () {
  }

  public FacebookException (String s) {
    super(s);
  }

  public FacebookException (String s, Throwable throwable) {
    super(s, throwable);
  }

  public FacebookException (Throwable throwable) {
    super(throwable);
  }
}
