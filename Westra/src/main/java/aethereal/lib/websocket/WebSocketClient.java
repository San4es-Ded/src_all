package aethereal.lib.websocket;

import java.net.URI;
import java.util.Map;

public abstract class WebSocketClient {
   protected WebSocketClient(URI uri, Map<String, String> headers) {
   }

   public void connect() {
   }

   public void close() {
   }

   public void send(String data) {
   }

   public abstract void a(ServerHandshake var1);

   public abstract void c(String var1);

   public abstract void b(int var1, String var2, boolean var3);

   public boolean isOpen() {
      return false;
   }

   public abstract void a(Exception var1);
}
