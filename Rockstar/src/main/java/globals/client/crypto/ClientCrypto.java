package globals.client.crypto;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ClientCrypto {
   private static final byte[] INFO_C2S = "globals:c2s".getBytes(StandardCharsets.UTF_8);
   private static final byte[] INFO_S2C = "globals:s2c".getBytes(StandardCharsets.UTF_8);
   private final PublicKey serverKey;
   private byte[] sendKey;
   private byte[] recvKey;
   private final AtomicLong sendCounter = new AtomicLong(0L);
   private final AtomicLong recvHighWater = new AtomicLong(-1L);

   public ClientCrypto(String localValue1) {
      try {
         byte[] localValue2 = Base64.getDecoder().decode(localValue1.trim());
         this.serverKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(localValue2));
      } catch (Exception localValue3) {
         throw new IllegalStateException("Bad server public key", localValue3);
      }
   }

   public String wrapMaster() {
      if (this.sendKey != null) {
         throw new IllegalStateException(
            "ClientCrypto \u0443\u0436\u0435 \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0438\u0440\u043e\u0432\u0430\u043d \u2014 \u043f\u043e\u0432\u0442\u043e\u0440\u043d\u044b\u0439 wrapMaster \u0437\u0430\u043f\u0440\u0435\u0449\u0451\u043d"
         );
      } else {
         byte[] localValue1 = new byte[32];
         new SecureRandom().nextBytes(localValue1);
         this.sendKey = hkdf(localValue1, INFO_C2S);
         this.recvKey = hkdf(localValue1, INFO_S2C);

         try {
            Cipher localValue2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            localValue2.init(1, this.serverKey);
            return Base64.getEncoder().encodeToString(localValue2.doFinal(localValue1));
         } catch (Exception localValue3) {
            throw new IllegalStateException("RSA wrap failed", localValue3);
         }
      }
   }

   public ClientCrypto.InternalType0005 encrypt(byte[] localValue1) {
      long localValue2 = this.sendCounter.getAndIncrement();
      byte[] localValue4 = gcm(1, this.sendKey, localValue2, localValue1);
      return new ClientCrypto.InternalType0005(localValue2, Base64.getEncoder().encodeToString(localValue4));
   }

   public byte[] decrypt(long localValue1, String localValue3) {
      if (localValue1 <= this.recvHighWater.get()) {
         return null;
      } else {
         try {
            byte[] localValue4 = gcm(2, this.recvKey, localValue1, Base64.getDecoder().decode(localValue3));
            this.recvHighWater.updateAndGet(localValue2 -> Math.max(localValue2, localValue1));
            return localValue4;
         } catch (Exception localValue5) {
            return null;
         }
      }
   }

   private static byte[] gcm(int localValue0, byte[] localValue1, long localValue2, byte[] localValue4) {
      try {
         byte[] localValue5 = ByteBuffer.allocate(12).putInt(0).putLong(localValue2).array();
         Cipher localValue6 = Cipher.getInstance("AES/GCM/NoPadding");
         localValue6.init(localValue0, new SecretKeySpec(localValue1, "AES"), new GCMParameterSpec(128, localValue5));
         return localValue6.doFinal(localValue4);
      } catch (Exception localValue7) {
         throw new IllegalStateException("GCM " + (localValue0 == 1 ? "encrypt" : "decrypt") + " failed", localValue7);
      }
   }

   private static byte[] hkdf(byte[] localValue0, byte[] localValue1) {
      try {
         Mac localValue2 = Mac.getInstance("HmacSHA256");
         localValue2.init(new SecretKeySpec(new byte[32], "HmacSHA256"));
         byte[] localValue3 = localValue2.doFinal(localValue0);
         localValue2.init(new SecretKeySpec(localValue3, "HmacSHA256"));
         ByteArrayOutputStream localValue4 = new ByteArrayOutputStream();
         byte[] localValue5 = new byte[0];

         for (int localValue6 = 1; localValue4.size() < 32; localValue6++) {
            localValue2.reset();
            localValue2.update(localValue5);
            localValue2.update(localValue1);
            localValue2.update((byte)localValue6);
            localValue5 = localValue2.doFinal();
            localValue4.write(localValue5, 0, localValue5.length);
         }

         return Arrays.copyOf(localValue4.toByteArray(), 32);
      } catch (Exception localValue7) {
         throw new IllegalStateException("HKDF failed", localValue7);
      }
   }

   public static final class InternalType0005 {
      private final long n;
      private final String ct;

      public InternalType0005(long localValue1, String localValue3) {
         this.n = localValue1;
         this.ct = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0005[n=" + this.n() + ", ct=" + this.ct() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.n());
         result = 31 * result + java.util.Objects.hashCode(this.ct());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ClientCrypto.InternalType0005 other = (ClientCrypto.InternalType0005) localValue1;
         return java.util.Objects.equals(this.n(), other.n())
            && java.util.Objects.equals(this.ct(), other.ct());
      }

      public long n() {
         return this.n;
      }

      public String ct() {
         return this.ct;
      }
   }
}
