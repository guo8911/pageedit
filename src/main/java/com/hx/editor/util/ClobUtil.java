package com.hx.editor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobUtil {
  public static String clobToString(Clob clob_obj) {
    String rsStr = "";
    if (clob_obj != null) {
      Reader inStream = null;
      BufferedReader br = null;
      try {
        inStream = clob_obj.getCharacterStream();
        br = new BufferedReader(inStream);
        String str = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (str != null) {
          sb.append(str);
          str = br.readLine();
        } 
        rsStr = sb.toString();
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (br != null)
            br.close(); 
          if (inStream != null)
            inStream.close(); 
        } catch (IOException e) {
          e.printStackTrace();
        } 
      } 
    } 
    return rsStr;
  }
  
  public static String getClob(Object object) {
    String clob = object.toString();
    return clob;
  }
}
