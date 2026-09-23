package aethereal.lib.jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public final class Connection_2 {
   private final org.jsoup.Connection delegate;
   private final Map<String, String> formData = new HashMap<>();

   private Connection_2(String url) {
      this.delegate = org.jsoup.Jsoup.connect(url);
   }

   public static Connection_2 b(String url) {
      return new Connection_2(url);
   }

   public Connection_2 c(String key, String value) {
      this.delegate.cookie(key, value);
      return this;
   }

   public Connection_2 c(Map<String, String> cookies) {
      cookies.forEach(this.delegate::cookie);
      return this;
   }

   public Connection_2 b(Map<String, String> headers) {
      headers.forEach(this.delegate::header);
      return this;
   }

   public Connection_2 a(Connection_2.c method) {
      this.delegate.method(Method.valueOf(method.name()));
      return this;
   }

   public Connection_2 a(Map<String, String> data) {
      this.formData.putAll(data);
      return this;
   }

   public Connection_2 a(String key, String value) {
      this.formData.put(key, value);
      return this;
   }

   public Connection_2 c(boolean followRedirects) {
      this.delegate.followRedirects(followRedirects);
      return this;
   }

   public Connection_2 a(int timeoutMs) {
      this.delegate.timeout(timeoutMs);
      return this;
   }

   public Connection_2.e e() throws IOException {
      this.formData.forEach(this.delegate::data);
      Response response = this.delegate.execute();
      return new Connection_2.e(response);
   }

   public Document c() throws IOException {
      return new Document(this.delegate.get());
   }

   public static enum c {
      GET,
      POST;
   }

   public static class d {
      private final Map<String, String> cookies = new HashMap<>();

      public Map<String, String> e() {
         return this.cookies;
      }
   }

   public static class e {
      private final Response response;

      e(Response response) {
         this.response = response;
      }

      public Map<String, String> e() {
         return this.response.cookies();
      }

      public String k() {
         return this.response.body();
      }

      public Document j() throws IOException {
         return new Document(this.response.parse());
      }
   }
}
