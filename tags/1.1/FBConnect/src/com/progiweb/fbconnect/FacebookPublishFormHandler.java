package com.progiweb.fbconnect;

import atg.droplet.GenericFormHandler;
import atg.droplet.DropletFormException;
import atg.droplet.DropletException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import com.progiweb.fbconnect.session.FacebookSessionClient;

/**
 * Form handler to post stories to the Facebook wall
 *
 * @author $Author$
 * @version $Revision$
 */
public class FacebookPublishFormHandler extends GenericFormHandler {

  public static final String CLASS_VERSION = "$Id$";

  // the story title
  private String mTitle;

  // the story body
  private String mBody;

  // the id of the template bundle to publish stories with
  // (someone should really tell Facebook that hardcoding ids is bad)
  private String mTemplateBundleId;

  // the URLs for the destination pages
  private String mSuccessURL;
  private String mErrorURL;

  // the session client used to post stories
  private FacebookSessionClient mFacebookSessionClient;


  /**
   * Creates a requets to post the story to the user's wall
   * @param pReq dynamo request
   * @param pRes dynamo response
   * @return true if the submission is complete, false if we redirect to an error or confirmation page
   */
  public boolean handlePublish (DynamoHttpServletRequest pReq, DynamoHttpServletResponse pRes) throws IOException, ServletException {
    String ctx = "handlePublish - ";

    // handle errors before posting to FB
    String storyTitle = getTitle();
    String storyBody = getBody();
    String templateBundleId = getTemplateBundleId();
    if (storyTitle == null || "".equals(storyTitle)) {
      logError(ctx + "storyTitle is null or empty");
      addFormException(new DropletFormException("The field is empty", getAbsoluteName() + ".title"));
    }
    if (storyBody == null || "".equals(storyBody)) {
      logError(ctx + "storyBody is null or empty");
      addFormException(new DropletFormException("The field is empty", getAbsoluteName() + ".body"));
    }
    if (templateBundleId == null || "".equals(templateBundleId)) {
      logError(ctx + "templateBundleId is null or empty");
      addFormException(new DropletFormException("The field is empty", getAbsoluteName() + ".templateBundleId"));
    }
    if (getFormError()) {
      pRes.sendLocalRedirect(getErrorURL(), pReq);
      return false;
    }

    // post the story
    // FIXME: anything can happen here, so we'll just catch a generic exception; the library should be however rethought to return better exceptions and return codes
    try {
      getFacebookSessionClient().publishStoryToUser(storyTitle, storyBody, templateBundleId, null, new Integer(1));
    } catch (Exception e) {
      logError(ctx + "exception publishing story", e);
      addFormException(new DropletException("The story could not be published"));
    }

    // not sure whether there's a way to get the result...
    return checkFormRedirect(getSuccessURL(), getErrorURL(), pReq, pRes);
  } // end handlePublish


  public String getTitle () {
    return mTitle;
  }

  public void setTitle (String pTitle) {
    mTitle = pTitle;
  }

  public String getBody () {
    return mBody;
  }

  public void setBody (String pBody) {
    mBody = pBody;
  }

  public String getTemplateBundleId () {
    return mTemplateBundleId;
  }

  public void setTemplateBundleId (String pTemplateBundleId) {
    mTemplateBundleId = pTemplateBundleId;
  }

  public String getSuccessURL () {
    return mSuccessURL;
  }

  public void setSuccessURL (String pSuccessURL) {
    mSuccessURL = pSuccessURL;
  }

  public String getErrorURL () {
    return mErrorURL;
  }

  public void setErrorURL (String pErrorURL) {
    mErrorURL = pErrorURL;
  }

  public FacebookSessionClient getFacebookSessionClient () {
    return mFacebookSessionClient;
  }

  public void setFacebookSessionClient (FacebookSessionClient pFacebookSessionClient) {
    mFacebookSessionClient = pFacebookSessionClient;
  }
} // end class

