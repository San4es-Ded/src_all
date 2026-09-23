package aethereal.network;

import aethereal.lib.javassist.CannotCompileException;
import aethereal.lib.javassist.ClassPool;
import aethereal.lib.javassist.NotFoundException;
import aethereal.lib.javassist.Translator;
import aethereal.lib.websocket.BadHttpRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javassist.CtClass;

public class Webserver_2 {
   private ServerSocket d;
   private ClassPool e;
   protected Translator a;
   private static final byte[] f = new byte[]{13, 10};
   private static final int g = 1;
   private static final int h = 2;
   private static final int i = 3;
   private static final int j = 4;
   private static final int k = 5;
   public String b = null;
   public String c = null;

   public static void a(String[] args) throws IOException {
      if (args.length == 1) {
         Webserver_2 web = new Webserver_2(args[0]);
         web.a();
      } else {
         System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
      }
   }

   public Webserver_2(String port) throws IOException {
      this(Integer.parseInt(port));
   }

   public Webserver_2(int port) throws IOException {
      this.d = new ServerSocket(port);
      this.e = null;
      this.a = null;
   }

   public void a(ClassPool loader) {
      this.e = loader;
   }

   public void a(ClassPool cp, Translator t) throws CannotCompileException, NotFoundException {
      this.e = cp;
      this.a = t;
      t.a(this.e);
   }

   public void b() throws IOException {
      this.d.close();
   }

   public void a(String msg) {
      System.out.println(msg);
   }

   public void a(String msg1, String msg2) {
      System.out.print(msg1);
      System.out.print("");
      System.out.println(msg2);
   }

   public void a(String msg1, String msg2, String msg3) {
      System.out.print(msg1);
      System.out.print("");
      System.out.print(msg2);
      System.out.print("");
      System.out.println(msg3);
   }

   public void b(String msg) {
      System.out.print("    ");
      System.out.println(msg);
   }

   public void a() {
      System.err.println("ready to service...");

      while (true) {
         try {
            Webserver th = new Webserver(this, this.d.accept());
            th.start();
         } catch (IOException var2) {
            this.a(var2.toString());
         }
      }
   }

   final void a(Socket clnt) throws IOException {
      InputStream in = new BufferedInputStream(clnt.getInputStream());
      String cmd = this.a(in);
      this.a(clnt.getInetAddress().getHostName(), new Date().toString(), cmd);

      while (this.b(in) > 0) {
      }

      OutputStream out = new BufferedOutputStream(clnt.getOutputStream());

      try {
         this.a(in, out, cmd);
      } catch (BadHttpRequest var6) {
         this.a(out, var6);
      }

      out.flush();
      in.close();
      out.close();
      clnt.close();
   }

   private String a(InputStream in) throws IOException {
      StringBuffer buf = new StringBuffer();

      while (true) {
         int c = in.read();
         if (c < 0 || c == 13) {
            in.read();
            return buf.toString();
         }

         buf.append((char)c);
      }
   }

   private int b(InputStream in) throws IOException {
      int len = 0;

      while (true) {
         int c = in.read();
         if (c < 0 || c == 13) {
            in.read();
            return len;
         }

         len++;
      }
   }

   public void a(InputStream in, OutputStream out, String cmd) throws BadHttpRequest, IOException {
      if (!cmd.startsWith("GET /")) {
         throw new BadHttpRequest();
      } else {
         String urlName = cmd.substring(5, cmd.indexOf(32, 5));
         String filename = urlName;
         int fileType;
         if (urlName.endsWith(".class")) {
            fileType = 2;
         } else if (urlName.endsWith(".html") || urlName.endsWith(".htm")) {
            fileType = 1;
         } else if (urlName.endsWith(".gif")) {
            fileType = 3;
         } else if (urlName.endsWith(".jpg")) {
            fileType = 4;
         } else {
            fileType = 5;
         }

         int len = urlName.length();
         if (fileType != 2 || !this.a(out, urlName, len)) {
            this.a(urlName, len);
            if (this.c != null) {
               filename = this.c + urlName;
            }

            if (File.separatorChar != '/') {
               filename = filename.replace('/', File.separatorChar);
            }

            File file = new File(filename);
            if (file.canRead()) {
               this.a(out, file.length(), fileType);
               FileInputStream fin2 = new FileInputStream(file);
               byte[] filebuffer = new byte[8192];

               while (true) {
                  int len2 = fin2.read(filebuffer);
                  if (len2 <= 0) {
                     fin2.close();
                     return;
                  }

                  out.write(filebuffer, 0, len2);
               }
            } else {
               InputStream fin;
               if (fileType == 2 && (fin = this.getClass().getResourceAsStream("/" + urlName)) != null) {
                  ByteArrayOutputStream barray = new ByteArrayOutputStream();
                  byte[] filebuffer2 = new byte[8192];

                  while (true) {
                     int len3 = fin.read(filebuffer2);
                     if (len3 <= 0) {
                        byte[] classfile = barray.toByteArray();
                        this.a(out, classfile.length, 2);
                        out.write(classfile);
                        fin.close();
                        return;
                     }

                     barray.write(filebuffer2, 0, len3);
                  }
               } else {
                  throw new BadHttpRequest();
               }
            }
         }
      }
   }

   private void a(String filename, int len) throws BadHttpRequest {
      for (int i2 = 0; i2 < len; i2++) {
         char c = filename.charAt(i2);
         if (!Character.isJavaIdentifierPart(c) && c != '.' && c != '/') {
            throw new BadHttpRequest();
         }
      }

      if (filename.indexOf("..") >= 0) {
         throw new BadHttpRequest();
      }
   }

   private boolean a(OutputStream out, String filename, int length) throws IOException, BadHttpRequest {
      if (this.e == null) {
         return false;
      } else {
         String classname = filename.substring(0, length - 6).replace('/', '.');

         try {
            if (this.a != null) {
               this.a.a(this.e, classname);
            }

            CtClass c = this.e.f(classname);
            byte[] classfile = c.toBytecode();
            if (this.b != null) {
               c.detach();
            }

            this.a(out, classfile.length, 2);
            out.write(classfile);
            return true;
         } catch (Exception var7) {
            throw new BadHttpRequest(var7);
         }
      }
   }

   private void a(OutputStream out, long dataLength, int filetype) throws IOException {
      out.write("HTTP/1.0 200 OK".getBytes());
      out.write(f);
      out.write("Content-Length: ".getBytes());
      out.write(Long.toString(dataLength).getBytes());
      out.write(f);
      if (filetype == 2) {
         out.write("Content-Type: application/octet-stream".getBytes());
      } else if (filetype == 1) {
         out.write("Content-Type: text/html".getBytes());
      } else if (filetype == 3) {
         out.write("Content-Type: image/gif".getBytes());
      } else if (filetype == 4) {
         out.write("Content-Type: image/jpg".getBytes());
      } else if (filetype == 5) {
         out.write("Content-Type: text/plain".getBytes());
      }

      out.write(f);
      out.write(f);
   }

   private void a(OutputStream out, BadHttpRequest e) throws IOException {
      this.b("bad request: " + e.toString());
      out.write("HTTP/1.0 400 Bad Request".getBytes());
      out.write(f);
      out.write(f);
      out.write("<H1>Bad Request</H1>".getBytes());
   }
}
