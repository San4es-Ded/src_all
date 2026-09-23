package aethereal.lib.javassist;

public enum OpCode {
   HANDSHAKE(0),
   FRAME(1),
   CLOSE(2),
   PING(3),
   PONG(4);

   private final int code;

   private OpCode(int code) {
      this.code = code;
   }

   public int a() {
      return this.code;
   }

   public static OpCode a(int code) {
      for (OpCode opCode : values()) {
         if (opCode.code == code) {
            return opCode;
         }
      }

      return null;
   }
}
