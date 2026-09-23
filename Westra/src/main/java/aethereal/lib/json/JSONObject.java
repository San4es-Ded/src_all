package aethereal.lib.json;

public class JSONObject {
   private final org.json.JSONObject delegate;

   public JSONObject() {
      this.delegate = new org.json.JSONObject();
   }

   public JSONObject(String json) {
      this.delegate = new org.json.JSONObject(json);
   }

   public JSONObject(org.json.JSONObject delegate) {
      this.delegate = delegate;
   }

   public org.json.JSONObject unwrap() {
      return this.delegate;
   }

   public JSONObject c(String key, Object value) {
      if (value instanceof JSONObject jsonObject) {
         this.delegate.put(key, jsonObject.delegate);
      } else if (value instanceof JSONArray jsonArray) {
         this.delegate.put(key, jsonArray.unwrap());
      } else {
         this.delegate.put(key, value);
      }

      return this;
   }

   public JSONObject b(String key, boolean value) {
      this.delegate.put(key, value);
      return this;
   }

   public JSONObject b(String key, double value) {
      this.delegate.put(key, value);
      return this;
   }

   public JSONObject a(String key, Object value) {
      if (value instanceof JSONObject jsonObject) {
         this.delegate.put(key, jsonObject.delegate);
      } else if (value instanceof JSONArray jsonArray) {
         this.delegate.put(key, jsonArray.unwrap());
      } else {
         this.delegate.put(key, value);
      }

      return this;
   }

   public String toPrettyString(int indentFactor) {
      return this.delegate.toString(indentFactor);
   }

   public String l(String key) {
      return this.delegate.optString(key, null);
   }

   public boolean m(String key) {
      return this.delegate.has(key);
   }

   public double e(String key) {
      return this.delegate.optDouble(key);
   }

   public float f(String key) {
      return (float)this.delegate.optDouble(key);
   }

   public boolean b(String key) {
      return this.delegate.optBoolean(key);
   }

   public int h(String key) {
      return this.delegate.optInt(key);
   }

   public JSONObject j(String key) {
      org.json.JSONObject obj = this.delegate.optJSONObject(key);
      return obj == null ? null : new JSONObject(obj);
   }

   public JSONArray i(String key) {
      org.json.JSONArray arr = this.delegate.optJSONArray(key);
      return arr == null ? null : new JSONArray(arr);
   }

   public JSONArray y(String key) {
      return this.i(key);
   }

   public Object a(String key) {
      return this.delegate.opt(key);
   }

   public boolean a(String key, boolean defaultValue) {
      return this.delegate.optBoolean(key, defaultValue);
   }

   public int a(String key, int defaultValue) {
      return this.delegate.optInt(key, defaultValue);
   }

   public String a(String key, String defaultValue) {
      return this.delegate.optString(key, defaultValue);
   }

   public boolean q(String key) {
      return this.delegate.optBoolean(key);
   }

   public String a(int indentFactor) {
      return this.delegate.toString(indentFactor);
   }
}
