package aethereal.lib.json;

public class JSONArray {
   private final org.json.JSONArray delegate;

   public JSONArray() {
      this.delegate = new org.json.JSONArray();
   }

   public JSONArray(String json) {
      this.delegate = new org.json.JSONArray(json);
   }

   public JSONArray(org.json.JSONArray delegate) {
      this.delegate = delegate;
   }

   public org.json.JSONArray unwrap() {
      return this.delegate;
   }

   public int a() {
      return this.delegate.length();
   }

   public JSONObject j(int index) {
      return new JSONObject(this.delegate.getJSONObject(index));
   }

   public String l(int index) {
      Object value = this.delegate.opt(index);
      return value == null ? null : String.valueOf(value);
   }

   public JSONArray a(Object value) {
      this.delegate.put(value instanceof JSONObject jsonObject ? jsonObject.unwrap() : value);
      return this;
   }

   public String E(int indentFactor) {
      return this.delegate.toString(indentFactor);
   }
}
