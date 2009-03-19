package org.elephantt.javabook.client;

/**
 * Modified by Progiweb
 */
public class InvalidFormatException extends FacebookException {

  private static final long serialVersionUID = -5352502462961276801L;

  public InvalidFormatException (Throwable throwable) {
    super(throwable);
  }

  public InvalidFormatException (String s) {
    super(s);
  }
}
