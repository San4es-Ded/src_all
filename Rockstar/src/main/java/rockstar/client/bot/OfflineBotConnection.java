package rockstar.client.bot;


import rockstar.client.*;
import rockstar.client.internal.config.*;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkingBackend;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.state.LoginStates;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;

public class OfflineBotConnection {
   private final BotTargetManager internalField0716;
   private ClientConnection internalField0833;
   private BotState internalField0714;
   private Consumer<String> internalField0922;
   private Consumer<Void> internalField0921;
   private Consumer<String> internalField1390;
   private long internalField0229;
   private long internalField0230;

   public OfflineBotConnection(BotTargetManager localValue1) {
      this.internalField0714 = BotState.internalField0714;
      this.internalField0229 = 0L;
      this.internalField0230 = 0L;
      this.internalField0716 = localValue1;
   }

   public void internalMethod01000(String localValue1, int localValue2) {
      if (this.internalField0714 == BotState.internalField0714) {
         this.internalField0714 = BotState.internalField0715;

         try {
            InetSocketAddress localValue3 = new InetSocketAddress(localValue1, localValue2);
            this.internalField0833 = ClientConnection.connect(localValue3, NetworkingBackend.remote(false), (MultiValueDebugSampleLogImpl)null);
            OfflineBotLoginListener localValue4 = new OfflineBotLoginListener(this, this.internalField0716);
            this.internalField0833.connect(localValue1, localValue2, LoginStates.C2S, LoginStates.S2C, localValue4, false);
            UUID localValue5 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.internalField0716.internalMethod03426()).getBytes());
            this.internalField0833.send(new LoginHelloC2SPacket(this.internalField0716.internalMethod03426(), localValue5));
            this.internalField0714 = BotState.internalField1283;
         } catch (Exception localValue6) {
            this.internalField0714 = BotState.internalField0714;
            if (this.internalField0922 != null) {
               this.internalField0922.accept("Connection failed: " + localValue6.getMessage());
            }

            RockstarClient.internalField0572.error("Bot {} failed to connect: {}", this.internalField0716.internalMethod03426(), localValue6.getMessage());
         }
      }
   }

   public void internalMethod06538() {
      if (this.internalField0833 != null && this.internalField0833.isOpen()) {
         this.internalField0714 = BotState.internalField1286;
         this.internalField0833.disconnect(Text.literal("Bot disconnected"));
      }

      this.internalMethod06741("Disconnected");
   }

   public void internalMethod07163(Packet<?> localValue1) {
      if (this.internalField0833 != null && this.internalField0833.isOpen() && this.internalField0714.internalMethod08353()) {
         this.internalField0833.send(localValue1);
      }
   }

   public boolean internalMethod06539() {
      return this.internalField0833 != null && this.internalField0833.isOpen() && this.internalField0714 == BotState.internalField1284;
   }

   public void internalMethod06982() {
      if (this.internalField0833 != null) {
         this.internalField0833.tick();
         if (!this.internalField0833.isOpen() && this.internalField0714 != BotState.internalField0714) {
            this.internalMethod06741("Connection lost");
         }
      }
   }

   public void internalMethod00411(int localValue1) {
      if (this.internalField0833 != null && !this.internalField0833.isLocal()) {
         this.internalField0833.setCompressionThreshold(localValue1, false);
      }
   }

   public void internalMethod03974(Consumer<String> localValue1) {
      this.internalField0922 = localValue1;
   }

   public void internalMethod01814(Consumer<Void> localValue1) {
      this.internalField0921 = localValue1;
   }

   public void internalMethod08394(Consumer<String> localValue1) {
      this.internalField1390 = localValue1;
   }

   void internalMethod07755() {
      this.internalField0714 = BotState.internalField1284;
      this.internalField0716.internalMethod00273().internalMethod04193();
      if (this.internalField0921 != null) {
         this.internalField0921.accept(null);
      }
   }

   public void internalMethod00075(String localValue1) {
      if (this.internalField0922 != null) {
         this.internalField0922.accept(localValue1);
      }
   }

   public void internalMethod06741(String localValue1) {
      if (this.internalField0714 != BotState.internalField0714) {
         this.internalField0714 = BotState.internalField0714;
         this.internalField0716.internalMethod00273().internalMethod04199();
         if (this.internalField1390 != null) {
            this.internalField1390.accept(localValue1);
         }
      }
   }

   @Generated
   public BotTargetManager internalMethod07177() {
      return this.internalField0716;
   }

   @Generated
   public ClientConnection internalMethod05322() {
      return this.internalField0833;
   }

   @Generated
   public BotState internalMethod07176() {
      return this.internalField0714;
   }

   @Generated
   public Consumer<String> internalMethod03686() {
      return this.internalField0922;
   }

   @Generated
   public Consumer<Void> internalMethod03898() {
      return this.internalField0921;
   }

   @Generated
   public Consumer<String> internalMethod07798() {
      return this.internalField1390;
   }

   @Generated
   public long internalMethod06537() {
      return this.internalField0229;
   }

   @Generated
   public long internalMethod06981() {
      return this.internalField0230;
   }

   @Generated
   public void internalMethod01399(BotState localValue1) {
      this.internalField0714 = localValue1;
   }
}
