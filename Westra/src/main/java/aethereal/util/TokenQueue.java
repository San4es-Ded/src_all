package aethereal.util;

public final class TokenQueue {
   private String remaining;

   public TokenQueue(String value) {
      this.remaining = value == null ? "" : value;
   }

   public String g(String delimiter) {
      int index = this.remaining.indexOf(delimiter);
      if (index < 0) {
         String value = this.remaining;
         this.remaining = "";
         return value;
      } else {
         String value = this.remaining.substring(0, index);
         this.remaining = this.remaining.substring(index + delimiter.length());
         return value;
      }
   }

   public String e(String delimiter) {
      int index = this.remaining.indexOf(delimiter);
      if (index < 0) {
         String value = this.remaining;
         this.remaining = "";
         return value;
      } else {
         String value = this.remaining.substring(0, index);
         this.remaining = this.remaining.substring(index + delimiter.length());
         return value;
      }
   }
}
