package aethereal.network;

import aethereal.lib.websocket.IProtocol;
import java.util.regex.Pattern;

public class Protocol implements IProtocol {
   private static final Pattern a = Pattern.compile("");
   private static final Pattern b = Pattern.compile(",");
   private final String c;

   public Protocol(String providedProtocol) {
      if (providedProtocol == null) {
         throw new IllegalArgumentException();
      } else {
         this.c = providedProtocol;
      }
   }

   public boolean a(String inputProtocolHeader) {
      if ("".equals(this.c)) {
         return true;
      } else {
         String protocolHeader = a.matcher(inputProtocolHeader).replaceAll("");
         String[] headers = b.split(protocolHeader);

         for (String header : headers) {
            if (this.c.equals(header)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public String a() {
      return this.c;
   }

   @Override
   public IProtocol b() {
      return new Protocol(this.a());
   }

   @Override
   public String toString() {
      return this.a();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Protocol protocol = (Protocol)o;
         return this.c.equals(protocol.c);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.c.hashCode();
   }
}
