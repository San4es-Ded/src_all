package aethereal.lib.websocket;

public class BadHttpRequest extends Exception {
   public BadHttpRequest() {
   }

   public BadHttpRequest(String message) {
      super(message);
   }

   public BadHttpRequest(Throwable cause) {
      super(cause);
   }
}
