package com.hx.editor.dao;

import com.xpoplarsoft.framework.db.DBResult;
import com.xpoplarsoft.framework.interfaces.bean.LoginUserBean;
import java.util.List;
import java.util.Map;

public interface IProjectDao {
  List<Map<String, Object>> getSat();
  
  List<Map<String, Object>> getProj();
  
  String addNode(String paramString, int paramInt1, int paramInt2, LoginUserBean paramLoginUserBean);
  
  boolean delNode(String paramString, LoginUserBean paramLoginUserBean);
  
  String editNode(String paramString1, String paramString2, LoginUserBean paramLoginUserBean);
  
  List<Map<String, Object>> getGuding();
  
  String addFile(String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, LoginUserBean paramLoginUserBean);
  
  String AddCheckout(String paramString1, String paramString2);
  
  Map<String, Object> getCheckout(String paramString);
  
  boolean delCheckout(String paramString);
  
  boolean delUserCheckout(String paramString);
  
  Map<String, Object> getFile(String paramString);
  
  boolean save(String paramString1, String paramString2, LoginUserBean paramLoginUserBean);
  
  List<Map<String, Object>> getTm(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  DBResult getRawTm(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  int getTmCount(String paramString1, String paramString2);
  
  String getGudingUrl(String paramString);
}
