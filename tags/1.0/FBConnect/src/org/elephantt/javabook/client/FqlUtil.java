package org.elephantt.javabook.client;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;
import net.sf.json.JSONArray;

/**
 * Modified by Progiweb (Java 1.4 compatibility)
 */
public class FqlUtil {
  /**
   * Returns an array of the fields in the given FQL query
   *
   * @param query
   * @return
   */
  public static String[] getFqlFields (String query) {
    String fqlLower = query.toLowerCase();
    String propStr = fqlLower.substring(fqlLower.indexOf("select") + "select".length(), fqlLower.indexOf("from"));
    String[] dirtyProps = propStr.split(",");
    String[] cleanProps = new String[dirtyProps.length];
    int i = 0;
    for (int k = 0; k < dirtyProps.length; k++) {
      String dirtyProp = dirtyProps[i];
      cleanProps[i++] = dirtyProp.trim();
    }
    return cleanProps;
  }


  /**
   * Returns a parallel array to getFqlFields(), where field names are converted into appropriate Java property names.
   * (e.g., 'first_name' is converted into 'firstName')
   */
  public static String[] getJavaPropertiesFromQuery (String query) {
    String[] fbProps = getFqlFields(query);
    String[] javaProps = new String[fbProps.length];
    for (int i = 0; i < fbProps.length; i++) {
      char[] chars = fbProps[i].toCharArray();
      StringBuffer sb = new StringBuffer();
      boolean upcaseNext = false;
      for (int k = 0; k < chars.length; k++) {
        char aChar = chars[k];
        if (aChar == '_') {
          upcaseNext = true;
        } else {
          sb.append(upcaseNext ? Character.toUpperCase(aChar) : aChar);
          upcaseNext = false;
        }
      }
      javaProps[i] = sb.toString();
    }
    return javaProps;
  }


  public static Object convertToBean (JSONObject jsonObj, Class clazz, String[] jsonProps, String[] javaProps) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    Object bean = clazz.newInstance();
    for (int i = 0; i < jsonProps.length; i++) {
      BeanUtils.setProperty(bean, javaProps[i], jsonObj.get(jsonProps[i]));
    }
    return bean;
  }

  /**
   * TODO: currently fails silently if type conversion from String to another type in the bean fails. e.g., "a1" will
   * get set as 0 in an Integer or int property. This should be an error.
   * <p/>
   * TODO: ridiculously inefficient to parse the query every time. This is just a prototype.
   *
   * @param query
   * @param jsonObj
   * @param clazz
   * @return
   */
  public static Object convertToBean (String query, JSONObject jsonObj, Class clazz) {
    try {
      return convertToBean(jsonObj, clazz, FqlUtil.getFqlFields(query), FqlUtil.getJavaPropertiesFromQuery(query));
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (JSONException e) {
      throw new RuntimeException(e); // TODO
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e); // TODO
    }
  }

  public static Object[] convertToBeanAray (String query, JSONArray arr, Class clazz) {
    String[] jsonProps = FqlUtil.getFqlFields(query);
    String[] javaProps = FqlUtil.getJavaPropertiesFromQuery(query);
    Object[] beanArr = new Object[arr.size()];
    Iterator jsonObjects = arr.iterator();
    int i = 0;
    try {
      while (jsonObjects.hasNext()) {
        JSONObject jsonObj = (JSONObject) jsonObjects.next();
        beanArr[i++] = convertToBean(jsonObj, clazz, jsonProps, javaProps);
      }
      return beanArr;
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (JSONException e) {
      throw new RuntimeException(e); // TODO
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e); // TODO
    }
  }
}
