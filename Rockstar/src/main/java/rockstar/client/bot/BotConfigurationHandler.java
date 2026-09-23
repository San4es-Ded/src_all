package rockstar.client.bot;


import rockstar.client.*;
import rockstar.client.internal.inventory.*;
import com.mojang.authlib.GameProfile;
import java.util.Collections;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.c2s.config.ReadyC2SPacket;
import net.minecraft.network.packet.c2s.config.AcceptCodeOfConductC2SPacket;
import net.minecraft.network.packet.c2s.config.SelectKnownPacksC2SPacket;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.ClearDialogS2CPacket;
import net.minecraft.network.packet.s2c.common.CookieRequestS2CPacket;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.common.CustomReportDetailsS2CPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackRemoveS2CPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.common.ServerLinksS2CPacket;
import net.minecraft.network.packet.s2c.common.ServerTransferS2CPacket;
import net.minecraft.network.packet.s2c.common.StoreCookieS2CPacket;
import net.minecraft.network.packet.s2c.common.ShowDialogS2CPacket;
import net.minecraft.network.packet.s2c.common.SynchronizeTagsS2CPacket;
import net.minecraft.network.packet.s2c.config.DynamicRegistriesS2CPacket;
import net.minecraft.network.packet.s2c.config.CodeOfConductS2CPacket;
import net.minecraft.network.packet.s2c.config.FeaturesS2CPacket;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;
import net.minecraft.network.packet.s2c.config.ResetChatS2CPacket;
import net.minecraft.network.packet.s2c.config.SelectKnownPacksS2CPacket;
import net.minecraft.network.state.PlayStateFactories;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.DynamicRegistryManager.Immutable;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public class BotConfigurationHandler implements ClientConfigurationPacketListener {
   private final OfflineBotConnection internalField0713;
   private final BotTargetManager internalField0716;
   private final GameProfile internalField0021;
   private Immutable internalField0601;

   public BotConfigurationHandler(OfflineBotConnection localValue1, BotTargetManager localValue2, GameProfile localValue3) {
      this.internalField0713 = localValue1;
      this.internalField0716 = localValue2;
      this.internalField0021 = localValue3;
   }

   public void onReady(ReadyS2CPacket packet) {
      Immutable localValue2;
      if (this.internalField0601 != null) {
         localValue2 = this.internalField0601;
      } else if (MinecraftClient.getInstance().world != null) {
         localValue2 = MinecraftClient.getInstance().world.getRegistryManager().toImmutable();
      } else {
         localValue2 = DynamicRegistryManager.of(Registries.REGISTRIES);
      }

      BotPlayPacketHandler localValue3 = new BotPlayPacketHandler(this.internalField0713, this.internalField0716, this.internalField0021);
      this.internalField0713.internalMethod05322().transitionInbound(PlayStateFactories.S2C.bind(RegistryByteBuf.makeFactory(localValue2)), localValue3);
      this.internalField0713.internalMethod05322().send(ReadyC2SPacket.INSTANCE);
      this.internalField0713.internalMethod05322().transitionOutbound(PlayStateFactories.C2S.bind(RegistryByteBuf.makeFactory(localValue2), () -> true));
      this.internalField0716.internalMethod00500(localValue3);
      this.internalField0713.internalMethod01399(BotState.internalField1284);
      this.internalField0713.internalMethod07755();
      RockstarClient.internalField0572.info("Bot {} entered play state", this.internalField0716.internalMethod03426());
   }

   public void onDynamicRegistries(DynamicRegistriesS2CPacket packet) {
   }

   public void onFeatures(FeaturesS2CPacket packet) {
   }

   public void onSelectKnownPacks(SelectKnownPacksS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(new SelectKnownPacksC2SPacket(Collections.emptyList()));
   }

   public void onResetChat(ResetChatS2CPacket packet) {
   }

   public void onCodeOfConduct(CodeOfConductS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(AcceptCodeOfConductC2SPacket.INSTANCE);
   }

   public void onKeepAlive(KeepAliveS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(new KeepAliveC2SPacket(packet.getId()));
   }

   public void onPing(CommonPingS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(new CommonPongC2SPacket(packet.getParameter()));
   }

   public void onCustomPayload(CustomPayloadS2CPacket packet) {
   }

   public void onDisconnect(DisconnectS2CPacket packet) {
      String localValue2 = packet.reason().getString();
      this.internalField0713.internalMethod00075("Disconnected during configuration: " + localValue2);
      RockstarClient.internalField0572.warn("Bot {} config disconnect: {}", this.internalField0716.internalMethod03426(), localValue2);
   }

   public void onResourcePackSend(ResourcePackSendS2CPacket packet) {
      this.internalField0713.internalMethod05322().send(new ResourcePackStatusC2SPacket(packet.id(), Status.SUCCESSFULLY_LOADED));
   }

   public void onResourcePackRemove(ResourcePackRemoveS2CPacket packet) {
   }

   public void onSynchronizeTags(SynchronizeTagsS2CPacket packet) {
   }

   public void onStoreCookie(StoreCookieS2CPacket packet) {
   }

   public void onServerTransfer(ServerTransferS2CPacket packet) {
   }

   public void onCustomReportDetails(CustomReportDetailsS2CPacket packet) {
   }

   public void onServerLinks(ServerLinksS2CPacket packet) {
   }

   public void onClearDialog(ClearDialogS2CPacket packet) {
   }

   public void onShowDialog(ShowDialogS2CPacket packet) {
   }

   public void onCookieRequest(CookieRequestS2CPacket packet) {
   }

   public void onDisconnected(DisconnectionInfo info) {
      this.internalField0713.internalMethod00075("Disconnected during configuration: " + info.reason().getString());
      this.internalField0713.internalMethod01399(BotState.internalField0714);
   }

   public boolean isConnectionOpen() {
      return this.internalField0713.internalMethod05322() != null && this.internalField0713.internalMethod05322().isOpen();
   }

   public NetworkPhase getPhase() {
      return NetworkPhase.CONFIGURATION;
   }

   public void addCustomCrashReportInfo(CrashReport report, CrashReportSection section) {
      section.add("Bot", this.internalField0716.internalMethod03426());
      section.add("Connection State", this.internalField0713.internalMethod07176().toString());
   }

   @Generated
   public OfflineBotConnection internalMethod06494() {
      return this.internalField0713;
   }

   @Generated
   public BotTargetManager internalMethod06495() {
      return this.internalField0716;
   }

   @Generated
   public GameProfile internalMethod04716() {
      return this.internalField0021;
   }

   @Generated
   public Immutable internalMethod02954() {
      return this.internalField0601;
   }
}
