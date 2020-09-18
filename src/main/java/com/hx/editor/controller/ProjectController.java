package com.hx.editor.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hx.editor.service.IProjectService;
import com.xpoplarsoft.framework.interfaces.bean.LoginUserBean;
import com.xpoplarsoft.framework.parameter.SystemParameter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/project"})
public class ProjectController {
  @Autowired
  private IProjectService projectService;
  
  @RequestMapping({"getSat"})
  @ResponseBody
  public String getSat() {
    return JSONArray.toJSONString(this.projectService.getSat());
  }
  
  @RequestMapping({"getProj"})
  @ResponseBody
  public String getProj(String key) {
    return JSONArray.toJSONString(this.projectService.getProj(key));
  }
  
  @RequestMapping({"getGuding"})
  @ResponseBody
  public String getGuding() {
    return JSONArray.toJSONString(this.projectService.getGuding());
  }
  
  @RequestMapping({"addFile"})
  @ResponseBody
  public String addFile(HttpSession session, String name, int owner, int type, String data, String uid) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.addFile(name, owner, type, data, uid, loginUser);
  }
  
  @RequestMapping({"copyFile"})
  @ResponseBody
  public String copyFile(HttpSession session, String srcId, int owner, String name, String uid) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.copyFile(srcId, owner, name, uid, loginUser);
  }
  
  @RequestMapping({"addNode"})
  @ResponseBody
  public String addNode(HttpSession session, String name, int owner, int type) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.addNode(name, owner, type, loginUser);
  }
  
  @RequestMapping({"editNode"})
  @ResponseBody
  public String editNode(HttpSession session, String id, String name) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.editNode(id, name, loginUser);
  }
  
  @RequestMapping({"delNode"})
  @ResponseBody
  public String delNode(HttpSession session, String id) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.delNode(id, loginUser) ? "T" : "F";
  }
  
  @RequestMapping({"checkout"})
  @ResponseBody
  public String checkout(String proId, String uid) {
    return this.projectService.checkout(proId, uid) ? "T" : "F";
  }
  
  @RequestMapping({"checkin"})
  @ResponseBody
  public String checkin(HttpSession session, String proId, String data, boolean isSave) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    if (isSave) {
      this.projectService.checkin(proId);
      return this.projectService.save(proId, data, loginUser) ? "T" : "F";
    } 
    return this.projectService.checkin(proId) ? "T" : "F";
  }
  
  @RequestMapping({"checkallin"})
  @ResponseBody
  public String checkallin(String uid) {
    return this.projectService.checkallin(uid) ? "T" : "F";
  }
  
  @RequestMapping({"getLastFile"})
  @ResponseBody
  public String getLastFile(String proId, boolean readOnly) {
    return this.projectService.getLastFile(proId, readOnly);
  }
  
  @RequestMapping({"save"})
  @ResponseBody
  public String save(HttpSession session, String proId, String data) {
    LoginUserBean loginUser = (LoginUserBean)session
      .getAttribute("LoginUser");
    return this.projectService.save(proId, data, loginUser) ? "T" : "F";
  }
  
  @RequestMapping({"getTm"})
  @ResponseBody
  public String getTm(String satId, String key, int page, int pagesize) {
    if (key == null)
      key = ""; 
    return this.projectService.getTm(satId, key, page, pagesize);
  }
  
  @RequestMapping({"getGudingUrl"})
  @ResponseBody
  public String getGudingUrl(String id) {
    return this.projectService.getGudingUrl(id);
  }
  
  @RequestMapping({"/getSysManagerAddr"})
  @ResponseBody
  public String getSysManagerAddr(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();
    String pageUrl = SystemParameter.getInstance().getParameter(
        "sysManagerAddr");
    map.put("result", Boolean.valueOf(true));
    map.put("url", pageUrl);
    return JSON.toJSONString(map);
  }
}
