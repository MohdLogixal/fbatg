package org.elephantt.javabook.client;

public class Parameter {
  private String name;
  private Object value;

  public Parameter (String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName () {
    return name;
  }

  public Object getValue () {
    return value;
  }
}
