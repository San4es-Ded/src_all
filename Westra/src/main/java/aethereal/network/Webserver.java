package aethereal.network;

import java.io.IOException;
import java.net.Socket;

public class Webserver extends Thread {
   Webserver_2 a;
   Socket b;

   public Webserver(Webserver_2 w, Socket s) {
      this.a = w;
      this.b = s;
   }

   @Override
   public void run() {
      try {
         this.a.a(this.b);
      } catch (IOException var2) {
      }
   }
}
