package org.elephantt.javabook.client;

public class Image {
  private String url;
  private String link;

  public Image (String url, String link) {
    this.url = url;
    this.link = link;
  }

  public String getUrl () {
    return url;
  }

  public String getLink () {
    return link;
  }
}
