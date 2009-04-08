package com.progiweb.fbconnect.pipeline;

import atg.servlet.pipeline.InsertableServletImpl;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import com.progiweb.fbconnect.userprofiling.FacebookProfileUpdater;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

/**
 * Pipeline servlet used to redirect a user logged into Facebook but unknown to ATG to a "register" page
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookAuthStatusServlet extends InsertableServletImpl {

  // true to redirect to a "register" page
  private boolean mRedirect;

  // the URI of the "register" page
  private String mRedirectURI;

  // a list of static file extensions that bypass this servlet
  private List mBypassExtensions;

  private FacebookProfileUpdater mProfileUpdater;


  /**
   * Main entry point
   * @param pRequest dynamo request
   * @param pResponse dynamo response
   * @throws IOException if errors
   * @throws ServletException if errors
   */
  public void service (DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse) throws IOException, ServletException {
    String ctx = "service - ";

    String authStatus = (String)pRequest.getAttribute("authStatus");
    if (isLoggingDebug()) {
      logDebug(ctx + "authStatus = " + authStatus);
    }

    // if we're serving static files (.css, .gif, .jpg, etc.) via Serverina or JBoss, this servlet will always redirect
    // a request for one of them to the register URI, effectively disabling style sheets and images
    // look up the extension of the request against a list of static file extensions, and pass the request unchanged when it's the case
    if (bypass(pRequest)) {
      passRequest(pRequest, pResponse);
      return;
    }

    if (!"true".equals(pRequest.getCookieParameter("fbconnect_redirecting"))) {
      if ("FB_new_user".equals(authStatus) && checkRedirect(pRequest)) {
        getProfileUpdater().update(pRequest);
        Cookie redirectCookie = new Cookie("fbconnect_redirecting", "true");
        pResponse.addCookie(redirectCookie);
        pResponse.sendRedirect(getRedirectURI());
      }
    } else {
      if (isLoggingDebug()) {
        logDebug(ctx + "not redirecting");
      }
      Cookie redirectCookie = new Cookie("fbconnect_redirecting", "false");
      redirectCookie.setMaxAge(0);
      pResponse.addCookie(redirectCookie);
    }
    passRequest(pRequest, pResponse);
  } // end service


  /**
   * Decides if a request should be handled by this servlet or if it can continue unchanged
   * @param pRequest dynamo request
   * @return true if the request should continue in the pipeline immediately, false if this servlet can handle it
   */
  protected boolean bypass (DynamoHttpServletRequest pRequest) {
    String ctx = "bypass - ";
    List extensions = getBypassExtensions();
    if (extensions == null || extensions.isEmpty()) {
      return false;
    }

    boolean result = false;
    String reqExt = pRequest.getRequestURI();
    int pos = reqExt.lastIndexOf(".");
    if (pos == -1) {
      result = false;
    } else {
      reqExt = reqExt.substring(pos);
      for (Iterator it = extensions.iterator(); it.hasNext(); ) {
        String ext = (String)it.next();
        if (ext.equals(reqExt)) {
          if (isLoggingDebug()) {
            logDebug(ctx + "found extension " + ext + " for request URI " + pRequest.getRequestURI());
          }
          result = true;
          break;
        }
      }
    }
    return result;
  } // end bypass


  /**
   * Determines if this servlet should redirect to the redirectURI page (the registration URL)
   * @param pRequest original request
   * @return true if redirecting, false otherwise
   */
  private boolean checkRedirect (DynamoHttpServletRequest pRequest) {
    String requestUri = pRequest.getRequestURI();
    if (requestUri.endsWith(getRedirectURI())) {
      // returning false since we would redirect to the same page the user is coming from
      return false;
    } else {
      return isRedirect();
    }
  }

  public boolean isRedirect () {
    return mRedirect;
  }

  public void setRedirect (boolean pRedirect) {
    mRedirect = pRedirect;
  }

  public String getRedirectURI () {
    return mRedirectURI;
  }

  public void setRedirectURI (String pRedirectURI) {
    mRedirectURI = pRedirectURI;
  }

  public FacebookProfileUpdater getProfileUpdater () {
    return mProfileUpdater;
  }

  public void setProfileUpdater (FacebookProfileUpdater pProfileUpdater) {
    mProfileUpdater = pProfileUpdater;
  }

  public List getBypassExtensions () {
    return mBypassExtensions;
  }

  public void setBypassExtensions (List pBypassExtensions) {
    mBypassExtensions = pBypassExtensions;
  }
} // end class
