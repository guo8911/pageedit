package com.hx.editor.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarTool {
  public static void generateJar(String sourcePath, String targetFile) throws FileNotFoundException, IOException {
    System.out.println("*** --> 开始生成jar包...");
    System.out.println("sourcePath --> " + sourcePath);
    System.out.println("targetPath --> " + targetFile);
    String targetDirPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
    File targetDir = new File(targetDirPath);
    if (!targetDir.exists())
      targetDir.mkdirs(); 
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    JarOutputStream target = new JarOutputStream(new FileOutputStream(targetFile), manifest);
    writeFile(new File(sourcePath), target, sourcePath);
    target.close();
    System.out.println("*** --> jar包生成完毕。");
  }
  
  private static void writeFile(File source, JarOutputStream target, String sourcePath) throws IOException {
    BufferedInputStream in = null;
    try {
      if (source.isDirectory()) {
        String name = source.getPath().replace("\\", "/");
        if (!name.isEmpty()) {
          if (!name.endsWith("/"))
            name = String.valueOf(name) + "/"; 
          if (!name.equals(sourcePath)) {
            name = name.substring(sourcePath.length());
            JarEntry jarEntry = new JarEntry(name);
            jarEntry.setTime(source.lastModified());
            target.putNextEntry(jarEntry);
            target.closeEntry();
          } 
        } 
        byte b;
        int i;
        File[] arrayOfFile;
        for (i = (arrayOfFile = source.listFiles()).length, b = 0; b < i; ) {
          File nestedFile = arrayOfFile[b];
          writeFile(nestedFile, target, sourcePath);
          b++;
        } 
        return;
      } 
      String middleName = source.getPath().replace("\\", "/").substring(sourcePath.length());
      JarEntry entry = new JarEntry(middleName);
      entry.setTime(source.lastModified());
      target.putNextEntry(entry);
      in = new BufferedInputStream(new FileInputStream(source));
      byte[] buffer = new byte[1024];
      while (true) {
        int count = in.read(buffer);
        if (count == -1)
          break; 
        target.write(buffer, 0, count);
      } 
    } finally {
      if (in != null)
        in.close(); 
    } 
    if (in != null)
      in.close(); 
  }
  
  public static void decompress(String sourceFile, String targetPath) throws IOException {
    System.out.println("*** --> 开始解压jar包...");
    System.out.println("sourcePath --> " + sourceFile);
    System.out.println("targetPath --> " + targetPath);
    if (!targetPath.endsWith(File.separator))
      targetPath = String.valueOf(targetPath) + File.separator; 
    File dir = new File(targetPath);
    if (!dir.exists())
      dir.mkdirs(); 
    JarFile jf = new JarFile(sourceFile);
    for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements(); ) {
      JarEntry je = e.nextElement();
      String outFileName = String.valueOf(targetPath) + je.getName();
      File f = new File(outFileName);
      if (je.isDirectory()) {
        if (!f.exists())
          f.mkdirs(); 
        continue;
      } 
      File pf = f.getParentFile();
      if (!pf.exists())
        pf.mkdirs(); 
      InputStream in = jf.getInputStream(je);
      OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
      byte[] buffer = new byte[2048];
      int nBytes = 0;
      while ((nBytes = in.read(buffer)) > 0)
        out.write(buffer, 0, nBytes); 
      out.flush();
      out.close();
      in.close();
    } 
    System.out.println("*** --> jar包解压完毕。");
  }
  
  public static void main(String[] args) throws IOException, InterruptedException {
    generateJar(args[0], args[1]);
  }
}
