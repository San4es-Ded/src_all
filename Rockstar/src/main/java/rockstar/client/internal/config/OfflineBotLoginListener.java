package rockstar.client.internal.config;



import rockstar.client.bot.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import lombok.Generated;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.c2s.login.EnterConfigurationC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.common.CookieRequestS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginCompressionS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.state.ConfigurationStates;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public class OfflineBotLoginListener implements ClientLoginPacketListener {
   private final OfflineBotConnection internalField0713;
   private final BotTargetManager internalField0716;
   private GameProfile internalField0021;

   public OfflineBotLoginListener(OfflineBotConnection localValue1, BotTargetManager localValue2) {
      this.internalField0713 = localValue1;
      this.internalField0716 = localValue2;
   }

   public void onHello(LoginHelloS2CPacket packet) {
      this.internalField0713.internalMethod00075("Server requires online mode authentication");
      this.internalField0713.internalMethod06538();
   }

   public void onSuccess(LoginSuccessS2CPacket packet) {
      this.internalField0021 = packet.profile();
      BotConfigurationHandler localValue2 = new BotConfigurationHandler(this.internalField0713, this.internalField0716, this.internalField0021);
      this.internalField0713.internalMethod05322().transitionInbound(ConfigurationStates.S2C, localValue2);
      this.internalField0713.internalMethod05322().send(EnterConfigurationC2SPacket.INSTANCE);
      this.internalField0713.internalMethod05322().transitionOutbound(ConfigurationStates.C2S);
      this.internalField0713.internalMethod01399(BotState.internalField1285);
      RockstarClient.internalField0572.info("Bot {} logged in successfully", this.internalField0716.internalMethod03426());
   }

   public void onDisconnect(LoginDisconnectS2CPacket packet) {
      String localValue2 = packet.reason().getString();
      this.internalField0713.internalMethod00075("Login failed: " + localValue2);
      RockstarClient.internalField0572.warn("Bot {} login disconnect: {}", this.internalField0716.internalMethod03426(), localValue2);
   }

   public void onCompression(LoginCompressionS2CPacket packet) {
      this.internalField0713.internalMethod00411(packet.getCompressionThreshold());
   }

   public void onQueryRequest(LoginQueryRequestS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(new LoginQueryResponseC2SPacket(packet.queryId(), null));
   }

   public void onCookieRequest(CookieRequestS2CPacket packet) {
   }

   public void onDisconnected(DisconnectionInfo info) {
      this.internalField0713.internalMethod00075("Disconnected during login: " + info.reason().getString());
      this.internalField0713.internalMethod01399(BotState.internalField0714);
   }

   public boolean isConnectionOpen() {
      return this.internalField0713.internalMethod05322() != null && this.internalField0713.internalMethod05322().isOpen();
   }

   public NetworkPhase getPhase() {
      return NetworkPhase.LOGIN;
   }

   public void addCustomCrashReportInfo(CrashReport report, CrashReportSection section) {
      section.add("Bot", this.internalField0716.internalMethod03426());
      section.add("Connection State", this.internalField0713.internalMethod07176().toString());
   }

   @Generated
   public OfflineBotConnection internalMethod02486() {
      return this.internalField0713;
   }

   @Generated
   public BotTargetManager internalMethod02487() {
      return this.internalField0716;
   }

   @Generated
   public GameProfile internalMethod07633() {
      return this.internalField0021;
   }
}
