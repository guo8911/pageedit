package com.hx.editor.service;

import com.xpoplarsoft.framework.interfaces.bean.LoginUserBean;
import java.util.List;
import java.util.Map;

public interface IProjectService {
  List<Map<String, Object>> getSat();
  
  List<Map<String, Object>> getProj(String paramString);
  
  String addNode(String paramString, int paramInt1, int paramInt2, LoginUserBean paramLoginUserBean);
  
  String editNode(String paramString1, String paramString2, LoginUserBean paramLoginUserBean);
  
  boolean delNode(String paramString, LoginUserBean paramLoginUserBean);
  
  List<Map<String, Object>> getGuding();
  
  String addFile(String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, LoginUserBean paramLoginUserBean);
  
  boolean checkout(String paramString1, String paramString2);
  
  boolean checkin(String paramString);
  
  boolean checkallin(String paramString);
  
  String getLastFile(String paramString, boolean paramBoolean);
  
  boolean save(String paramString1, String paramString2, LoginUserBean paramLoginUserBean);
  
  String getTm(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  String getGudingUrl(String paramString);
  
  String copyFile(String paramString1, int paramInt, String paramString2, String paramString3, LoginUserBean paramLoginUserBean);
}
