package com.hx.editor.controller;

import com.alibaba.fastjson.JSONArray;
import com.hx.editor.util.JarTool;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/dynamic"})
public class DynamicController {
  @RequestMapping({"getDebugFiles"})
  @ResponseBody
  public String getDebugFiles(HttpSession session) {
    String debug = session.getServletContext().getRealPath("debug");
    return getFiles(debug);
  }
  
  @RequestMapping({"getRunFiles"})
  @ResponseBody
  public String getRunFiles(HttpSession session, String pid) {
    String cur = session.getServletContext().getRealPath("/").replace("\\", "/");
    String src = String.valueOf(cur) + "jars/" + pid + ".jar";
    String run = String.valueOf(cur) + "run/" + pid;
    try {
      JarTool.decompress(src, run);
      return getFiles(run);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static String getFiles(String path) {
    File[] files = (new File(String.valueOf(path) + "/css")).listFiles();
    List<String> css = new ArrayList<>();
    byte b;
    int i;
    File[] arrayOfFile1;
    for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) {
      File file = arrayOfFile1[b];
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.endsWith(".css"))
          css.add(name); 
      } 
      b++;
    } 
    files = (new File(String.valueOf(path) + "/js")).listFiles();
    List<String> js = new ArrayList<>();
    File[] arrayOfFile2;
    for (int j = (arrayOfFile2 = files).length; i < j; ) {
      File file = arrayOfFile2[i];
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.endsWith(".js"))
          js.add(name); 
      } 
      i++;
    } 
    return "{css:" + JSONArray.toJSONString(css) + ",js:" + JSONArray.toJSONString(js) + "}";
  }
}
