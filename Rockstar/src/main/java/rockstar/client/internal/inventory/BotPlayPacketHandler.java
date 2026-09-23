package rockstar.client.internal.inventory;




import rockstar.client.bot.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import lombok.Generated;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
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
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockValueDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkBiomeDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkSentS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkValueDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.CraftFailedResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.DebugSampleS2CPacket;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EndCombatS2CPacket;
import net.minecraft.network.packet.s2c.play.EventDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.EnterCombatS2CPacket;
import net.minecraft.network.packet.s2c.play.EnterReconfigurationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityValueDebugS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.GameTestHighlightPosS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.LookAtS2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MoveMinecartAlongTrackS2CPacket;
import net.minecraft.network.packet.s2c.play.NbtQueryResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenMountScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRotationS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProjectilePowerS2CPacket;
import net.minecraft.network.packet.s2c.play.RecipeBookAddS2CPacket;
import net.minecraft.network.packet.s2c.play.RecipeBookRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.RecipeBookSettingsS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreResetS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.SelectAdvancementTabS2CPacket;
import net.minecraft.network.packet.s2c.play.ServerMetadataS2CPacket;
import net.minecraft.network.packet.s2c.play.SetCameraEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.SetCursorItemS2CPacket;
import net.minecraft.network.packet.s2c.play.SetPlayerInventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.network.packet.s2c.play.SimulationDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.StartChunkSendS2CPacket;
import net.minecraft.network.packet.s2c.play.StatisticsS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.network.packet.s2c.play.TestInstanceBlockStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.TickStepS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateTickRateS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.network.packet.s2c.play.WaypointS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInterpolateSizeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderWarningBlocksChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderWarningTimeChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import rockstar.client.internal.script.BotRosterManager;
import rockstar.client.internal.inventory.BotPlayerState;
import rockstar.client.RockstarClient;
import rockstar.client.bot.OfflineBotConnection;
import rockstar.client.bot.BotTargetManager;

public class BotPlayPacketHandler
implements ClientPlayPacketListener,
TickablePacketListener {
    private final OfflineBotConnection internalField0713;
    private final BotTargetManager internalField0716;
    private final BotPlayerState internalField0172;
    private final GameProfile internalField0021;

    public BotPlayPacketHandler(OfflineBotConnection typedValue054, BotTargetManager typedValue055, GameProfile gameProfile) {
        this.internalField0713 = typedValue054;
        this.internalField0716 = typedValue055;
        this.internalField0172 = typedValue055.internalMethod06688();
        this.internalField0021 = gameProfile;
    }

    public void tick() {
    }

    public void onGameJoin(GameJoinS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod07201(packet);
        this.internalField0172.internalMethod09038(packet.playerEntityId());
        RockstarClient.internalField0572.info("Bot {} joined game with entity id {}", (Object)this.internalField0716.internalMethod03426(), (Object)packet.playerEntityId());
    }

    public void onPlayerPositionLook(PlayerPositionLookS2CPacket packet) {
        EntityPosition playerPosition = packet.change();
        this.internalField0172.internalMethod07202(playerPosition.position().x, playerPosition.position().y, playerPosition.position().z);
        this.internalField0172.internalMethod06850(playerPosition.yaw(), playerPosition.pitch());
        this.internalField0172.internalMethod09022(true);
        this.internalField0716.internalMethod08828(true);
        this.internalField0716.internalMethod00273().internalMethod00561(this.internalField0172);
        this.internalField0713.internalMethod05322().send((Packet)new TeleportConfirmC2SPacket(packet.teleportId()));
    }

    public void onHealthUpdate(HealthUpdateS2CPacket packet) {
        this.internalField0172.internalMethod09020(packet.getHealth());
        this.internalField0172.internalMethod04881(packet.getFood());
        if (this.internalField0172.internalMethod02820() && this.internalField0716.internalMethod06687().internalMethod04163()) {
            this.internalField0713.internalMethod05322().send((Packet)new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
        }
    }

    public void onKeepAlive(KeepAliveS2CPacket packet) {
        this.internalField0713.internalMethod05322().send((Packet)new KeepAliveC2SPacket(packet.getId()));
    }

    public void onDisconnect(DisconnectS2CPacket packet) {
        String string = packet.reason().getString();
        this.internalField0713.internalMethod06741("Disconnected: " + string);
        RockstarClient.internalField0572.info("Bot {} disconnected: {}", (Object)this.internalField0716.internalMethod03426(), (Object)string);
    }

    public void onPing(CommonPingS2CPacket packet) {
        this.internalField0713.internalMethod05322().send((Packet)new CommonPongC2SPacket(packet.getParameter()));
    }

    public void onUpdateSelectedSlot(UpdateSelectedSlotS2CPacket packet) {
        this.internalField0172.internalMethod09021(packet.slot());
    }

    public void onExperienceBarUpdate(ExperienceBarUpdateS2CPacket packet) {
        this.internalField0172.internalMethod07204(packet.getBarProgress(), packet.getExperienceLevel(), packet.getExperience());
    }

    public void onPlayerRespawn(PlayerRespawnS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod07601(packet);
        this.internalField0172.internalMethod02819();
        this.internalField0172.internalMethod09020(20.0f);
    }

    public void onDeathMessage(DeathMessageS2CPacket packet) {
        if (this.internalField0716.internalMethod06687().internalMethod04163()) {
            this.internalField0713.internalMethod05322().send((Packet)new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
        }
    }

    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
        if (packet.getSyncId() == 0) {
            this.internalField0172.internalMethod01460(packet.getSlot(), packet.getStack());
        } else {
            this.internalField0172.internalMethod03129(packet.getSyncId(), packet.getRevision(), packet.getSlot(), packet.getStack());
        }
    }

    public void onInventory(InventoryS2CPacket packet) {
        if (packet.syncId() == 0) {
            this.internalField0172.internalMethod05107(packet.contents());
        } else {
            this.internalField0172.internalMethod04427(packet.syncId(), packet.revision(), packet.contents());
        }
    }

    public void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod02065(packet);
    }

    public void onEntityPosition(EntityPositionS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod02272(packet);
    }

    public void onEntityPositionSync(EntityPositionSyncS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod01780(packet);
    }

    public void onCustomPayload(CustomPayloadS2CPacket packet) {
    }

    public void onResourcePackSend(ResourcePackSendS2CPacket packet) {
        this.internalField0713.internalMethod05322().send((Packet)new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
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

    public void onCookieRequest(CookieRequestS2CPacket packet) {
    }

    public void onDisconnected(DisconnectionInfo info) {
        this.internalField0713.internalMethod06741(info.reason().getString());
        RockstarClient.internalField0572.info("Bot {} connection closed", (Object)this.internalField0716.internalMethod03426());
    }

    public boolean isConnectionOpen() {
        return this.internalField0713.internalMethod05322() != null && this.internalField0713.internalMethod05322().isOpen();
    }

    public NetworkPhase getPhase() {
        return NetworkPhase.PLAY;
    }

    public void addCustomCrashReportInfo(CrashReport report, CrashReportSection section) {
        section.add("Bot", (Object)this.internalField0716.internalMethod03426());
        section.add("Connection State", (Object)this.internalField0713.internalMethod07176().toString());
    }

    public void onEntitySpawn(EntitySpawnS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod06989(packet);
    }

    public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod04685(packet);
    }

    public void onEntityAnimation(EntityAnimationS2CPacket packet) {
    }

    public void onDamageTilt(DamageTiltS2CPacket packet) {
    }

    public void onStatistics(StatisticsS2CPacket packet) {
    }

    public void onRecipeBookAdd(RecipeBookAddS2CPacket packet) {
    }

    public void onRecipeBookRemove(RecipeBookRemoveS2CPacket packet) {
    }

    public void onRecipeBookSettings(RecipeBookSettingsS2CPacket packet) {
    }

    public void onBlockBreakingProgress(BlockBreakingProgressS2CPacket packet) {
    }

    public void onSignEditorOpen(SignEditorOpenS2CPacket packet) {
    }

    public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket packet) {
    }

    public void onBlockEvent(BlockEventS2CPacket packet) {
    }

    public void onBlockUpdate(BlockUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod07515(packet);
    }

    public void onGameMessage(GameMessageS2CPacket packet) {
        BotRosterManager.internalMethod03065().internalMethod07003(this.internalField0716, packet.content().getString());
    }

    public void onChatMessage(ChatMessageS2CPacket packet) {
        if (packet.unsignedContent() != null) {
            BotRosterManager.internalMethod03065().internalMethod07003(this.internalField0716, packet.unsignedContent().getString());
        }
    }

    public void onProfilelessChatMessage(ProfilelessChatMessageS2CPacket packet) {
        BotRosterManager.internalMethod03065().internalMethod07003(this.internalField0716, packet.message().getString());
    }

    public void onRemoveMessage(RemoveMessageS2CPacket packet) {
    }

    public void onChunkDeltaUpdate(ChunkDeltaUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod04409(packet);
    }

    public void onMapUpdate(MapUpdateS2CPacket packet) {
    }

    public void onCloseScreen(CloseScreenS2CPacket packet) {
        this.internalField0172.internalMethod04821(packet.getSyncId());
    }

    public void onOpenMountScreen(OpenMountScreenS2CPacket packet) {
        this.internalField0172.internalMethod04044(packet.getSyncId(), "Mount " + packet.getMountId());
    }

    public void onScreenHandlerPropertyUpdate(ScreenHandlerPropertyUpdateS2CPacket packet) {
    }

    public void onEntityStatus(EntityStatusS2CPacket packet) {
    }

    public void onEntityAttach(EntityAttachS2CPacket packet) {
    }

    public void onEntityPassengersSet(EntityPassengersSetS2CPacket packet) {
    }

    public void onExplosion(ExplosionS2CPacket packet) {
    }

    public void onGameStateChange(GameStateChangeS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod05908(packet);
    }

    public void onChunkData(ChunkDataS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod05814(packet);
    }

    public void onChunkBiomeData(ChunkBiomeDataS2CPacket packet) {
    }

    public void onUnloadChunk(UnloadChunkS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod00845(packet.pos());
    }

    public void onWorldEvent(WorldEventS2CPacket packet) {
    }

    public void onEntity(EntityS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod04586(packet);
    }

    public void onMoveMinecartAlongTrack(MoveMinecartAlongTrackS2CPacket packet) {
    }

    public void onPlayerRotation(PlayerRotationS2CPacket packet) {
    }

    public void onParticle(ParticleS2CPacket packet) {
    }

    public void onPlayerAbilities(PlayerAbilitiesS2CPacket packet) {
        this.internalField0172.internalMethod03425(packet.isInvulnerable(), packet.isFlying(), packet.allowFlying(), packet.isCreativeMode(), packet.getFlySpeed(), packet.getWalkSpeed());
    }

    public void onPlayerRemove(PlayerRemoveS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod00644(packet);
    }

    public void onPlayerList(PlayerListS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod01676(packet);
    }

    public void onEntitiesDestroy(EntitiesDestroyS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod07324(packet);
    }

    public void onRemoveEntityStatusEffect(RemoveEntityStatusEffectS2CPacket packet) {
    }

    public void onEntitySetHeadYaw(EntitySetHeadYawS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod02190(packet);
    }

    public void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod04798(packet);
    }

    public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet) {
    }

    public void onEntityEquipmentUpdate(EntityEquipmentUpdateS2CPacket packet) {
        if (packet.getEntityId() != this.internalField0172.internalMethod08592() && packet.getEntityId() != this.internalField0716.internalMethod00273().internalMethod08709()) {
            return;
        }
        for (Pair pair : packet.getEquipmentList()) {
            this.internalField0172.internalMethod04174((EquipmentSlot)pair.getFirst(), (ItemStack)pair.getSecond());
        }
    }

    public void onTeam(TeamS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod06053(packet);
    }

    public void onScoreboardScoreUpdate(ScoreboardScoreUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod07153(packet);
    }

    public void onScoreboardScoreReset(ScoreboardScoreResetS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod05972(packet);
    }

    public void onPlayerSpawnPosition(PlayerSpawnPositionS2CPacket packet) {
    }

    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod00170(packet);
    }

    public void onPlaySound(PlaySoundS2CPacket packet) {
    }

    public void onPlaySoundFromEntity(PlaySoundFromEntityS2CPacket packet) {
    }

    public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet) {
    }

    public void onUpdateTickRate(UpdateTickRateS2CPacket packet) {
    }

    public void onTickStep(TickStepS2CPacket packet) {
    }

    public void onEntityAttributes(EntityAttributesS2CPacket packet) {
    }

    public void onEntityStatusEffect(EntityStatusEffectS2CPacket packet) {
    }

    public void onEndCombat(EndCombatS2CPacket packet) {
    }

    public void onEnterCombat(EnterCombatS2CPacket packet) {
    }

    public void onDifficulty(DifficultyS2CPacket packet) {
    }

    public void onSetCameraEntity(SetCameraEntityS2CPacket packet) {
    }

    public void onWorldBorderInitialize(WorldBorderInitializeS2CPacket packet) {
    }

    public void onWorldBorderInterpolateSize(WorldBorderInterpolateSizeS2CPacket packet) {
    }

    public void onWorldBorderSizeChanged(WorldBorderSizeChangedS2CPacket packet) {
    }

    public void onWorldBorderWarningTimeChanged(WorldBorderWarningTimeChangedS2CPacket packet) {
    }

    public void onWorldBorderWarningBlocksChanged(WorldBorderWarningBlocksChangedS2CPacket packet) {
    }

    public void onWorldBorderCenterChanged(WorldBorderCenterChangedS2CPacket packet) {
    }

    public void onPlayerListHeader(PlayerListHeaderS2CPacket packet) {
    }

    public void onBossBar(BossBarS2CPacket packet) {
    }

    public void onCooldownUpdate(CooldownUpdateS2CPacket packet) {
    }

    public void onVehicleMove(VehicleMoveS2CPacket packet) {
    }

    public void onAdvancements(AdvancementUpdateS2CPacket packet) {
    }

    public void onSelectAdvancementTab(SelectAdvancementTabS2CPacket packet) {
    }

    public void onCraftFailedResponse(CraftFailedResponseS2CPacket packet) {
    }

    public void onCommandTree(CommandTreeS2CPacket packet) {
    }

    public void onStopSound(StopSoundS2CPacket packet) {
    }

    public void onCommandSuggestions(CommandSuggestionsS2CPacket packet) {
    }

    public void onSynchronizeRecipes(SynchronizeRecipesS2CPacket packet) {
    }

    public void onLookAt(LookAtS2CPacket packet) {
    }

    public void onNbtQueryResponse(NbtQueryResponseS2CPacket packet) {
    }

    public void onLightUpdate(LightUpdateS2CPacket packet) {
    }

    public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet) {
    }

    public void onOpenScreen(OpenScreenS2CPacket packet) {
        this.internalField0172.internalMethod04044(packet.getSyncId(), packet.getName().getString());
    }

    public void onSetTradeOffers(SetTradeOffersS2CPacket packet) {
    }

    public void onChunkLoadDistance(ChunkLoadDistanceS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod01261(packet);
    }

    public void onSimulationDistance(SimulationDistanceS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod00842(packet);
    }

    public void onChunkRenderDistanceCenter(ChunkRenderDistanceCenterS2CPacket packet) {
        this.internalField0716.internalMethod00273().internalMethod02864(packet);
    }

    public void onPlayerActionResponse(PlayerActionResponseS2CPacket packet) {
        this.internalField0172.internalMethod09425(packet.sequence());
    }

    public void onOverlayMessage(OverlayMessageS2CPacket packet) {
    }

    public void onSubtitle(SubtitleS2CPacket packet) {
    }

    public void onTitle(TitleS2CPacket packet) {
    }

    public void onTitleFade(TitleFadeS2CPacket packet) {
    }

    public void onTitleClear(ClearTitleS2CPacket packet) {
    }

    public void onServerMetadata(ServerMetadataS2CPacket packet) {
    }

    public void onChatSuggestions(ChatSuggestionsS2CPacket packet) {
    }

    public void onBundle(BundleS2CPacket packet) {
    }

    public void onEntityDamage(EntityDamageS2CPacket packet) {
    }

    public void onEnterReconfiguration(EnterReconfigurationS2CPacket packet) {
    }

    public void onStartChunkSend(StartChunkSendS2CPacket packet) {
    }

    public void onChunkSent(ChunkSentS2CPacket packet) {
    }

    public void onDebugSample(DebugSampleS2CPacket packet) {
    }

    public void onProjectilePower(ProjectilePowerS2CPacket packet) {
    }

    public void onGameTestHighlightPos(GameTestHighlightPosS2CPacket packet) {
    }

    public void onEventDebug(EventDebugS2CPacket packet) {
    }

    public void onClearDialog(ClearDialogS2CPacket packet) {
    }

    public void onShowDialog(ShowDialogS2CPacket packet) {
    }

    public void onSetCursorItem(SetCursorItemS2CPacket packet) {
        this.internalField0172.internalMethod04965(packet.contents());
    }

    public void onSetPlayerInventory(SetPlayerInventoryS2CPacket packet) {
        this.internalField0172.internalMethod02596(packet.slot(), packet.contents());
    }

    public void onTestInstanceBlockStatus(TestInstanceBlockStatusS2CPacket packet) {
    }

    public void onWaypoint(WaypointS2CPacket packet) {
    }

    public void onChunkValueDebug(ChunkValueDebugS2CPacket packet) {
    }

    public void onBlockValueDebug(BlockValueDebugS2CPacket packet) {
    }

    public void onEntityValueDebug(EntityValueDebugS2CPacket packet) {
    }

    public void onPingResult(PingResultS2CPacket packet) {
    }

    @Generated
    public OfflineBotConnection internalMethod07447() {
        return this.internalField0713;
    }

    @Generated
    public BotTargetManager internalMethod07448() {
        return this.internalField0716;
    }

    @Generated
    public BotPlayerState internalMethod06441() {
        return this.internalField0172;
    }

    @Generated
    public GameProfile internalMethod00873() {
        return this.internalField0021;
    }
}
