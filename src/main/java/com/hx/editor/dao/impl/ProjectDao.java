package com.hx.editor.dao.impl;

import com.hx.editor.dao.IProjectDao;
import com.hx.editor.util.Common;
import com.xpoplarsoft.framework.db.DBParameter;
import com.xpoplarsoft.framework.db.DBResult;
import com.xpoplarsoft.framework.db.ISQL;
import com.xpoplarsoft.framework.db.SQLFactory;
import com.xpoplarsoft.framework.db.impl.DBTools;
import com.xpoplarsoft.framework.db.template.DBtemplate;
import com.xpoplarsoft.framework.interfaces.bean.LoginUserBean;
import com.xpoplarsoft.framework.utils.DateTools;
import com.yk.log.LogRecordFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao implements IProjectDao {
  private static Log log = LogFactory.getLog(ProjectDao.class);
  
  public List<Map<String, Object>> getSat() {
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getSat", null);
    return Common.getMaps(result);
  }
  
  public List<Map<String, Object>> getProj() {
    if (log.isDebugEnabled())
      log.debug("查询航天器列表前时间为：" + new Date()); 
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getProj", null);
    if (log.isDebugEnabled())
      log.debug("查询航天器列表后时间为：" + new Date()); 
    return Common.getMaps(result);
  }
  
  public String addNode(String name, int owner, int type, LoginUserBean loginUser) {
    DBParameter para = new DBParameter();
    para.setObject("name", name);
    para.setObject("owner", Integer.valueOf(owner));
    para.setObject("type", Integer.valueOf(type));
    String userid = loginUser.getUserId();
    String localip = loginUser.getClientIp();
    Map<String, String> info = new HashMap<>();
    if (SQLFactory.getSqlComponent().updateInfo("project", "addNode", para, 
        info)) {
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置节点添加", "页面" + name + "添加", "成功", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置节点添加", "页面" + name + "添加", "成功", 
          DateTools.getCurryDateTime(), null, null, "info");
      return info.get("id");
    } 
    LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
        0, "2", "页面配置节点添加", "页面" + name + "添加", "失败", localip, 
        DateTools.getCurryDateTime(), null, null, "info");
    LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
        0, "2", "页面配置节点添加", "页面" + name + "添加", "失败", 
        DateTools.getCurryDateTime(), null, null, "info");
    if (((String)info.get("msg")).contains("NameUnique"))
      return "R"; 
    return "";
  }
  
  public boolean delNode(String set, LoginUserBean loginUser) {
    boolean delSx = SQLFactory.getSqlComponent().updateInfo(
        "delete from sx_file where proj_id in (" + set + ")");
    boolean flag = false;
    if (delSx)
      flag = SQLFactory.getSqlComponent().updateInfo(
          "delete FROM sx_project WHERE id IN (" + set + ")"); 
    String userid = loginUser.getUserId();
    String localip = loginUser.getClientIp();
    if (flag) {
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置", "页面" + set + "删除", "成功", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置", "页面" + set + "删除", "成功", 
          DateTools.getCurryDateTime(), null, null, "info");
    } else {
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置", "页面" + set + "删除", "失败", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置", "页面" + set + "删除", "失败", 
          DateTools.getCurryDateTime(), null, null, "info");
    } 
    return flag;
  }
  
  public String editNode(String id, String name, LoginUserBean loginUser) {
    DBParameter para = new DBParameter();
    para.setObject("id", id);
    para.setObject("name", name);
    Map<String, String> info = new HashMap<>();
    boolean flag = SQLFactory.getSqlComponent().updateInfo("project", 
        "editNode", para, info);
    if (loginUser != null) {
      String userid = loginUser.getUserId();
      String localip = loginUser.getClientIp();
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置节点修改名称", name, flag ? "成功" : "失败", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置节点修改名称", name, flag ? "成功" : "失败", 
          DateTools.getCurryDateTime(), null, null, "info");
    } 
    if (flag)
      return "T"; 
    if (((String)info.get("msg")).contains("NameUnique"))
      return "R"; 
    return "F";
  }
  
  public List<Map<String, Object>> getGuding() {
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getGuding", null);
    return Common.getMaps(result);
  }
  
  public String addFile(String name, int owner, int type, String data, String uid, LoginUserBean loginUser) {
    ISQL sql = SQLFactory.getSqlComponent();
    Connection conn = null;
    PreparedStatement pstmt = null;
    String tableSpace = "project";
    String id = null;
    DBParameter para = new DBParameter();
    para.setObject("name", name);
    para.setObject("owner", Integer.valueOf(owner));
    para.setObject("type", Integer.valueOf(type));
    para.setObject("data", data);
    para.setObject("uid", uid);
    para.setObject("time", DateTools.getCurryDateTime());
    String userid = loginUser.getUserId();
    String localip = loginUser.getClientIp();
    LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 0, 
        "2", "页面配置", "页面" + name + "添加", "成功", localip, 
        DateTools.getCurryDateTime(), null, null, "info");
    LogRecordFactory.getLogRecordComponent().recordSysLogLevel(0, 
        "2", "页面配置", "页面" + name + "添加", "成功", 
        DateTools.getCurryDateTime(), null, null, "info");
    try {
      conn = sql.getConnect();
      conn.setAutoCommit(false);
      List<String> sqlList = DBtemplate.getSqlStr(tableSpace, "addNode", 
          para);
      pstmt = conn.prepareStatement(sqlList.get(0), 
          1);
      DBTools.setPreparedStatement(pstmt, para, 
          DBtemplate.getParaList(tableSpace, "addNode").get(0));
      pstmt.executeUpdate();
      ResultSet rst = pstmt.getGeneratedKeys();
      if (rst.next()) {
        id = rst.getString(1);
        para.setObject("id", id);
        sqlList = DBtemplate.getSqlStr(tableSpace, "addFile", para);
        pstmt = conn.prepareStatement(sqlList.get(0));
        DBTools.setPreparedStatement(pstmt, para, 
            DBtemplate.getParaList(tableSpace, "addFile").get(0));
        pstmt.executeUpdate();
        conn.commit();
        return id;
      } 
    } catch (Exception e) {
      if (e.getMessage().contains("NameUnique"))
        return "R"; 
      try {
        conn.rollback();
      } catch (SQLException sQLException) {}
    } finally {
      try {
        pstmt.close();
        conn.close();
      } catch (SQLException sQLException) {}
    } 
    return "";
  }
  
  public String AddCheckout(String proId, String uid) {
    DBParameter para = new DBParameter();
    para.setObject("proId", proId);
    Map<String, String> info = new HashMap<>();
    if (log.isDebugEnabled())
      log.debug("查询显示页面前时间为：" + new Date()); 
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getDataIntoCheckout", para);
    if (log.isDebugEnabled())
      log.debug("查询显示页面后时间为：" + new Date()); 
    Map<String, Object> map = Common.getMap(result);
    DBParameter para1 = new DBParameter();
    if (map != null) {
      String data = (String)map.get("data");
      para1.setObject("proId", proId);
      para1.setObject("uid", uid);
      para1.setObject("data", data);
    } 
    if (SQLFactory.getSqlComponent().updateInfo("project", "AddCheckout", 
        para1, info))
      return "T"; 
    if (((String)info.get("msg")).contains("unique"))
      return "R"; 
    return "F";
  }
  
  public Map<String, Object> getCheckout(String proId) {
    DBParameter para = new DBParameter();
    para.setObject("proId", proId);
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getCheckout", para);
    return Common.getMap(result);
  }
  
  public boolean delCheckout(String proId) {
    DBParameter para = new DBParameter();
    para.setObject("proId", proId);
    return SQLFactory.getSqlComponent().updateInfo("project", 
        "delCheckout", para);
  }
  
  public boolean delUserCheckout(String uid) {
    DBParameter para = new DBParameter();
    para.setObject("uid", uid);
    return SQLFactory.getSqlComponent().updateInfo("project", 
        "delUserCheckout", para);
  }
  
  public Map<String, Object> getFile(String proId) {
    DBParameter para = new DBParameter();
    para.setObject("proId", proId);
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getFile", para);
    return Common.getMap(result);
  }
  
  public boolean save(String proId, String data, LoginUserBean loginUser) {
    DBParameter para = new DBParameter();
    para.setObject("proId", proId);
    para.setObject("data", data);
    para.setObject("time", DateTools.getCurryDateTime());
    boolean flag = SQLFactory.getSqlComponent().updateInfo("project", 
        "save", para);
    String userid = loginUser.getUserId();
    String localip = loginUser.getClientIp();
    if (flag) {
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置", "保存遥测页面文件" + proId, "成功", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置", "保存遥测页面文件" + proId, "成功", 
          DateTools.getCurryDateTime(), null, null, "info");
    } else {
      LogRecordFactory.getLogRecordComponent().recordOperateLogLevel(userid, 
          0, "2", "页面配置", "保存遥测页面文件" + proId, "失败", localip, 
          DateTools.getCurryDateTime(), null, null, "info");
      LogRecordFactory.getLogRecordComponent().recordSysLogLevel(
          0, "2", "页面配置", "保存遥测页面文件" + proId, "失败", 
          DateTools.getCurryDateTime(), null, null, "info");
    } 
    return flag;
  }
  
  public List<Map<String, Object>> getTm(String satId, String key, int page, int pagesize) {
    DBParameter para = new DBParameter();
    para.setObject("satId", satId);
    para.setObject("start", Integer.valueOf(pagesize * (page - 1)));
    para.setObject("size", Integer.valueOf(pagesize));
    para.setObject("key", "%" + key + "%");
    DBResult result = SQLFactory.getSqlComponent().pagingQueryInfo(
        "project", "getTm", para, pagesize * (page - 1), pagesize);
    return Common.getMaps(result);
  }
  
  public DBResult getRawTm(String satId, String key, int page, int pagesize) {
    DBParameter para = new DBParameter();
    para.setObject("satId", satId);
    para.setObject("key", "%" + key + "%");
    return SQLFactory.getSqlComponent().queryInfo("project", "getRawTm", 
        para);
  }
  
  public int getTmCount(String satId, String key) {
    DBParameter para = new DBParameter();
    para.setObject("satId", satId);
    para.setObject("key", "%" + key + "%");
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getTmCount", para);
    return Integer.parseInt(Common.getObject(result).toString());
  }
  
  public String getGudingUrl(String id) {
    DBParameter para = new DBParameter();
    para.setObject("id", id);
    DBResult result = SQLFactory.getSqlComponent().queryInfo("project", 
        "getGudingUrl", para);
    Object obj = Common.getObject(result);
    return (obj != null) ? obj.toString() : "";
  }
}
