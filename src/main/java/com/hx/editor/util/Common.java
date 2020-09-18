package com.hx.editor.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xpoplarsoft.framework.db.DBResult;
import com.xpoplarsoft.framework.db.SQLFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oracle.sql.CLOB;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import sun.misc.BASE64Encoder;

public class Common {
  private static String path;
  
  public static String getPath(HttpServletRequest request) {
    if (path == null) {
      StringBuilder paths = new StringBuilder();
      paths.append(request.getScheme()).append("://")
        .append(request.getServerName()).append(":")
        .append(request.getServerPort())
        .append(request.getContextPath()).append("/");
      path = paths.toString();
    } 
    return path;
  }
  
  public static String getUtf8Str(String str) {
    try {
      return new String(str.getBytes("ISO8859_1"), "UTF8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static String uploadFile(HttpServletRequest request, HttpSession session) {
    DiskFileItemFactory fac = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload((FileItemFactory)fac);
    upload.setHeaderEncoding("utf-8");
    String fileName = null;
    Map<String, String> params = new HashMap<>();
    FileItem fileItem = null;
    File targetFile = null;
    try {
      List<FileItem> fileItems = upload.parseRequest(request);
      String path = String.valueOf(session.getServletContext().getRealPath("/upload")) + 
        "/";
      for (FileItem fi : fileItems) {
        if (fi.isFormField()) {
          params.put(fi.getFieldName(), fi.getString());
          continue;
        } 
        fileItem = fi;
      } 
      if (((String)params.get("Filetype")).toString().equals("1")) {
        fileName = ((String)params.get("Filename")).toString();
        fileName = fileName.substring(fileName.lastIndexOf("."));
        fileName = String.valueOf(UUID.randomUUID().toString()) + fileName;
      } else {
        fileName = String.valueOf(UUID.randomUUID().toString()) + ".png";
      } 
      targetFile = new File(String.valueOf(path) + fileName);
      fileItem.write(targetFile);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } 
    return fileName;
  }
  
  public static boolean delDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        if (!delDir(new File(dir, children[i])))
          return false; 
      } 
    } 
    return dir.delete();
  }
  
  private static final int[] key = new int[] { 4, 1, 8, 5, 7, 3, 9, 6, 2 };
  
  private static final int BUFFER_SIZE = 16384;
  
  public static String randCode;
  
  private static final int codeSize = 4;
  
  private static int mod10(int n) {
    int ret = n % 10;
    if (ret < 0)
      ret += 10; 
    return ret;
  }
  
  public static String encrypt(String m) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0, k = 0; i < m.length(); i++) {
      char ch = m.charAt(i);
      if ('0' <= ch && ch <= '9') {
        ch = (char)(mod10(mod10(ch - 48 + key[k++]) * 3 + 7) + 48);
        if (k >= key.length)
          k = 0; 
      } 
      sb.append(ch);
    } 
    return sb.toString();
  }
  
  public static String decrypt(String c) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0, k = 0; i < c.length(); i++) {
      char ch = c.charAt(i);
      if ('0' <= ch && ch <= '9') {
        ch = (char)(mod10(mod10((ch - 48) * 7 + 1) - key[k++]) + 48);
        if (k >= key.length)
          k = 0; 
      } 
      sb.append(ch);
    } 
    return sb.toString();
  }
  
  public static boolean copy(String src, String target) {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(src));
      bos = new BufferedOutputStream(new FileOutputStream(target));
      byte[] buff = new byte[16384];
      for (int len = -1; (len = bis.read(buff)) > -1;)
        bos.write(buff, 0, len); 
      bos.close();
      bis.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } 
  }
  
  public static boolean mkdir(String dir) {
    File file = new File(dir);
    if (!file.exists() && !file.isDirectory())
      return file.mkdir(); 
    return true;
  }
  
  public static boolean deldir(String dir) {
    return deldir1(new File(dir));
  }
  
  private static boolean deldir1(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        if (!deldir1(new File(dir, children[i])))
          return false; 
      } 
    } 
    return dir.delete();
  }
  
  public static String getConfigVal(String name, String... joins) {
    Properties prop = new Properties();
    try {
      prop.load(Common.class.getClassLoader().getResourceAsStream(
            "config.properties"));
      String val = prop.getProperty(name);
      if (val != null) {
        if (joins.length == 0)
          return val; 
        val = val.replaceAll("/$", "");
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = joins).length, b = 0; b < i; ) {
          String join = arrayOfString[b];
          val = String.valueOf(val) + "/" + join;
          b++;
        } 
        return val;
      } 
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return null;
  }
  
  public static InputStream getJarFile(InputStream is, String entryFile) {
    try {
      JarInputStream jarInput = new JarInputStream(is);
      JarEntry entry = jarInput.getNextJarEntry();
      while (entry != null) {
        if (entryFile.equals(entry.getName()))
          return jarInput; 
        entry = jarInput.getNextJarEntry();
      } 
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static InputStream getJarFile(String jarFile, String entryFile) {
    try {
      if (!(new File(jarFile)).exists())
        throw new Exception("文件：‘" + jarFile + "’不存在！"); 
      JarFile jarFile1 = new JarFile(jarFile);
      JarEntry entry = jarFile1.getJarEntry(entryFile);
      return jarFile1.getInputStream(entry);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static Map<String, Object> getJson(Map<String, Object> map, String dataField) {
    if (map == null)
      return null; 
    Object data = map.get(dataField);
    if (data == null)
      return map; 
    JSONObject json = JSONObject.parseObject(data.toString());
    for (String key : json.keySet())
      map.put(key, json.get(key)); 
    map.remove(dataField);
    return map;
  }
  
  public static ByteArrayInputStream createImage() {
    newRandCode();
    int w = 60, h = 22;
    BufferedImage image = new BufferedImage(w, h, 
        1);
    Graphics2D g = image.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, w, h);
    g.setColor(Color.LIGHT_GRAY);
    g.drawRect(0, 0, w - 1, h - 1);
    g.setColor(Color.BLUE);
    int fontSize = h - 8;
    g.setFont(new Font("Algerian", 1, fontSize));
    char[] chars = randCode.toCharArray();
    for (int i = 0; i < 4; i++)
      g.drawChars(chars, i, 1, (w - 8) / 4 * i + 4, h / 2 + 
          fontSize / 2 - 2); 
    g.dispose();
    return image2Stream(image);
  }
  
  private static Random random = new Random();
  
  private static void newRandCode() {
    String codes = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    StringBuilder sb = new StringBuilder(4);
    for (int i = 0; i < 4; i++)
      sb.append(codes.charAt(random.nextInt(7))); 
    randCode = sb.toString();
  }
  
  private static ByteArrayInputStream image2Stream(BufferedImage image) {
    ByteArrayInputStream inputStream = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(bos);
    try {
      jpeg.encode(image);
      byte[] bts = bos.toByteArray();
      inputStream = new ByteArrayInputStream(bts);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return inputStream;
  }
  
  public static String getBase64(byte[] bytes) {
    return "data:image/png;base64," + (new BASE64Encoder()).encode(bytes);
  }
  
  public static List<Map<String, Object>> base64(List<Map<String, Object>> data, String... fields) {
    for (Map<String, Object> dat : data) {
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = fields).length, b = 0; b < i; ) {
        String field = arrayOfString[b];
        if (dat.get(field) != null) {
          byte[] bytes = (byte[])dat.get(field);
          dat.put(field, getBase64(bytes));
        } 
        b++;
      } 
    } 
    return data;
  }
  
  public static String md5(String s) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] bytes = md5.digest(s.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < bytes.length; i++) {
        int val = bytes[i] & 0xFF;
        if (val < 16)
          sb.append("0"); 
        sb.append(Integer.toHexString(val));
      } 
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static List<List<String>> parseDoc(String fileName, InputStream is) {
    List<List<String>> list = new ArrayList<>();
    String extend = fileName.substring(fileName.lastIndexOf(".") + 1, 
        fileName.length());
    try {
      if (extend.equals("xls") || extend.equals("xlsx")) {
        Workbook wb = WorkbookFactory.create(is);
        Sheet sheet = wb.getSheetAt(0);
        for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
          Row row = sheet.getRow(i);
          List<String> rowList = new ArrayList<>();
          for (int j = row.getFirstCellNum(); j < row
            .getPhysicalNumberOfCells(); j++)
            rowList.add(row.getCell(j).toString()); 
          list.add(rowList);
        } 
      } else if (extend.equals("doc")) {
        POIFSFileSystem pfs = new POIFSFileSystem(is);
        HWPFDocument hwpf = new HWPFDocument(pfs);
        Range range = hwpf.getRange();
        TableIterator it = new TableIterator(range);
        Table table = null;
        if (it.hasNext())
          table = it.next(); 
        for (int i = 0; i < table.numRows(); i++) {
          TableRow tr = table.getRow(i);
          List<String> rowList = new ArrayList<>();
          for (int j = 0; j < tr.numCells(); j++) {
            TableCell td = tr.getCell(j);
            String v = "";
            for (int k = 0; k < td.numParagraphs(); k++)
              v = String.valueOf(v) + td.getParagraph(k).text().trim(); 
            rowList.add(v);
          } 
          list.add(rowList);
        } 
      } else if (extend.equals("docx")) {
        XWPFDocument doc = new XWPFDocument(is);
        XWPFTable table = doc.getTables().get(0);
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
          List<XWPFTableCell> tableCells = row.getTableCells();
          List<String> rowList = new ArrayList<>();
          for (XWPFTableCell cell : tableCells) {
            String text = cell.getText();
            rowList.add(text);
          } 
          list.add(rowList);
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return list;
  }
  
  public static Object getObject(DBResult result) {
    return (result != null && result.getRows() > 0) ? result.getObject(0, 0) : 
      null;
  }
  
  public static Map<String, Object> getMap(DBResult result) {
    if (result != null && result.getRows() > 0) {
      String[] column_names = result.getColName();
      Map<String, Object> map = new HashMap<>();
      byte b;
      int i;
      String[] arrayOfString1;
      for (i = (arrayOfString1 = column_names).length, b = 0; b < i; ) {
        String name = arrayOfString1[b];
        map.put(name.toLowerCase(), result.getObject(0, name));
        b++;
      } 
      return map;
    } 
    return null;
  }
  
  public static List<Map<String, Object>> getMaps(DBResult result) {
    if (result != null && result.getRows() > 0) {
      List<Map<String, Object>> list = new ArrayList<>();
      String[] column_names = result.getColName();
      for (int i = 0; i < result.getRows(); i++) {
        Map<String, Object> map = new HashMap<>();
        byte b;
        int j;
        String[] arrayOfString;
        for (j = (arrayOfString = column_names).length, b = 0; b < j; ) {
          String name = arrayOfString[b];
          map.put(name.toLowerCase(), result.getObject(i, name));
          b++;
        } 
        list.add(map);
      } 
      return list;
    } 
    return null;
  }
  
  public static String getId() {
    DBResult result = SQLFactory.getSqlComponent().queryInfo(
        "select LAST_INSERT_ID() from dual");
    return getObject(result).toString();
  }
  
  public static Map<String, Object> getClob(DBResult result) {
    Map<String, Object> map = getMap(result);
    CLOB clob = (CLOB)map.get("data");
    try {
      map.put("data", clob.getSubString(1L, (int)clob.length()));
    } catch (SQLException e) {
      e.printStackTrace();
    } 
    return map;
  }
  
  public static List<Map<String, Object>> getClobs(DBResult result) {
    if (result != null && result.getRows() > 0) {
      List<Map<String, Object>> list = new ArrayList<>();
      String[] column_names = result.getColName();
      for (int i = 0; i < result.getRows(); i++) {
        Map<String, Object> map = new HashMap<>();
        byte b;
        int j;
        String[] arrayOfString;
        for (j = (arrayOfString = column_names).length, b = 0; b < j; ) {
          String name = arrayOfString[b];
          if (name.equals("data")) {
            CLOB clob = (CLOB)result.getObject(i, name);
            try {
              map.put("data", 
                  clob.getSubString(1L, (int)clob.length()));
            } catch (SQLException e) {
              e.printStackTrace();
            } 
          } else {
            map.put(name, result.getObject(i, name));
          } 
          b++;
        } 
        list.add(map);
      } 
      return list;
    } 
    return null;
  }
}
