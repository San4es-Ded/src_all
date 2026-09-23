package aethereal.util;

import aethereal.lib.log4j.Logger;
import aethereal.lib.log4j.StatusLogger;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class Base64Util {
   private static final Logger a = StatusLogger.x();
   private static Method b = null;
   private static Object c = null;

   private Base64Util() {
   }

   @Deprecated
   public static String a(String str) {
      if (str == null) {
         return null;
      } else {
         byte[] data = str.getBytes(Charset.defaultCharset());
         if (b != null) {
            try {
               return (String)b.invoke(c, data);
            } catch (Exception var3) {
               throw new LoggingException("Unable to encode String", var3);
            }
         } else {
            throw new LoggingException("No Encoder, unable to encode string");
         }
      }
   }

   static {
      try {
         Class<?> clazz = LoaderUtil.b("java.util.Base64");
         Class<?> encoderClazz = LoaderUtil.b("java.util.Base64.Encoder");
         Method method = clazz.getMethod("getEncoder");
         c = method.invoke(null);
         b = encoderClazz.getMethod("encodeToString", byte[].class);
      } catch (Exception var4) {
         try {
            Class<?> clazz2 = LoaderUtil.b("javax.xml.bind.DataTypeConverter");
            b = clazz2.getMethod("printBase64Binary");
         } catch (Exception var3) {
            a.b("Unable to create a Base64 Encoder", var3);
         }
      }
   }
}
