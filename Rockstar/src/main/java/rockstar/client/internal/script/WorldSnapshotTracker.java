package rockstar.client.internal.script;



import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import moscow.rockstar.mixin.accessors.EntityS2CPacketAccessor;
import moscow.rockstar.mixin.accessors.EntitySetHeadYawS2CPacketAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityPosition;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.network.packet.s2c.play.CommonPlayerSpawnInfo;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreResetS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardScoreUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.SimulationDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket.Operation;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket.SerializableTeam;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.AbstractTeam.CollisionRule;
import net.minecraft.scoreboard.AbstractTeam.VisibilityRule;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.dimension.DimensionType;

public class WorldSnapshotTracker {
   private final String internalField0248;
   private final Map<Integer, WorldSnapshotTracker.InternalType0205> internalField0543 = new ConcurrentHashMap<>();
   private final Map<UUID, WorldSnapshotTracker.InternalType0206> internalField0544 = new ConcurrentHashMap<>();
   private final Map<Long, Long> internalField1197 = new ConcurrentHashMap<>();
   private final Map<BlockPos, BlockState> internalField1196 = new ConcurrentHashMap<>();
   private final Scoreboard internalField0707 = new Scoreboard();
   private static final Pattern internalField0293 = Pattern.compile("\\d+");
   private String internalField0247 = "";
   private int internalField0227 = 25565;
   private int internalField0228 = -1;
   private RegistryKey<World> internalField0595;
   private Set<RegistryKey<World>> internalField0546 = Collections.emptySet();
   private GameMode internalField0907;
   private GameMode internalField0908;
   private long internalField0229;
   private long internalField0230;
   private long internalField1059;
   private boolean internalField0277 = true;
   private boolean internalField0276;
   private boolean internalField1099;
   private int internalField1053;
   private int internalField1055;
   private int internalField1056;
   private int internalField1054;
   private int internalField1464 = -64;
   private int internalField1470 = 384;
   private int internalField1465;
   private long internalField1058;
   private boolean internalField1100;
   private double internalField0194 = 4096.0;

   public WorldSnapshotTracker(String localValue1) {
      this.internalField0248 = localValue1;
   }

   public void internalMethod00864(String localValue1, int localValue2) {
      this.internalField0247 = localValue1 == null ? "" : localValue1;
      this.internalField0227 = localValue2;
   }

   public void internalMethod04193() {
      this.internalField1100 = true;
   }

   public void internalMethod04199() {
      this.internalField1100 = false;
   }

   public void internalMethod07201(GameJoinS2CPacket localValue1) {
      this.internalField0228 = localValue1.playerEntityId();
      this.internalField0546 = Set.copyOf(localValue1.dimensionIds());
      this.internalField1053 = localValue1.viewDistance();
      this.internalField1055 = localValue1.simulationDistance();
      this.internalMethod06422(localValue1.commonPlayerSpawnInfo(), true);
      this.internalField0543.clear();
      this.internalField1197.clear();
      this.internalField1196.clear();
      this.internalMethod08711();
   }

   public void internalMethod07601(PlayerRespawnS2CPacket localValue1) {
      RegistryKey localValue2 = this.internalField0595;
      this.internalMethod06422(localValue1.commonPlayerSpawnInfo(), false);
      if (localValue2 != null && this.internalField0595 != null && localValue2 != this.internalField0595) {
         this.internalField0543.clear();
         this.internalField1197.clear();
         this.internalField1196.clear();
         this.internalMethod08711();
      }
   }

   public void internalMethod00170(WorldTimeUpdateS2CPacket localValue1) {
      this.internalField0230 = localValue1.time();
      this.internalField1059 = localValue1.timeOfDay();
      this.internalField0277 = localValue1.tickDayTime();
   }

   public void internalMethod05814(ChunkDataS2CPacket localValue1) {
      this.internalField1197.put(ChunkPos.toLong(localValue1.getChunkX(), localValue1.getChunkZ()), this.internalField1058);
      this.internalMethod02010(localValue1);
   }

   public void internalMethod00845(ChunkPos localValue1) {
      if (localValue1 != null) {
         this.internalField1197.remove(localValue1.toLong());
         this.internalField1196.keySet().removeIf(localValue1x -> localValue1x.getX() >> 4 == localValue1.x && localValue1x.getZ() >> 4 == localValue1.z);
      }
   }

   public void internalMethod07515(BlockUpdateS2CPacket localValue1) {
      if (localValue1 != null) {
         this.internalMethod02450(localValue1.getPos(), localValue1.getState());
      }
   }

   public void internalMethod04409(ChunkDeltaUpdateS2CPacket localValue1) {
      if (localValue1 != null) {
         localValue1.visitUpdates(this::internalMethod02450);
      }
   }

   public void internalMethod06989(EntitySpawnS2CPacket localValue1) {
      WorldSnapshotTracker.InternalType0205 localValue2 = new WorldSnapshotTracker.InternalType0205(
         localValue1.getEntityId(),
         localValue1.getUuid(),
         this.internalMethod06828(localValue1.getEntityType()),
         new Vec3d(localValue1.getX(), localValue1.getY(), localValue1.getZ()),
         localValue1.getYaw(),
         localValue1.getPitch(),
         localValue1.getHeadYaw(),
         localValue1.getVelocity().x,
         localValue1.getVelocity().y,
         localValue1.getVelocity().z,
         this.internalField1058
      );
      this.internalField0543.put(localValue2.internalMethod05993(), localValue2);
   }

   public void internalMethod04586(EntityS2CPacket localValue1) {
      int localValue2 = ((EntityS2CPacketAccessor)(Object)localValue1).getId();
      WorldSnapshotTracker.InternalType0205 localValue3 = this.internalField0543.get(localValue2);
      if (localValue3 != null) {
         Vec3d localValue4 = localValue3.internalMethod02495();
         if (localValue1.isPositionChanged()) {
            localValue4 = localValue4.add(localValue1.getDeltaX() / this.internalField0194, localValue1.getDeltaY() / this.internalField0194, localValue1.getDeltaZ() / this.internalField0194);
         }

         float localValue5 = localValue3.internalMethod05992();
         float localValue6 = localValue3.internalMethod05997();
         if (localValue1.hasRotation()) {
            localValue5 = localValue1.getYaw();
            localValue6 = localValue1.getPitch();
         }

         this.internalField0543.put(localValue3.internalMethod05993(), localValue3.internalMethod00126(localValue4, localValue5, localValue6, localValue3.internalMethod08641(), this.internalField1058));
      }
   }

   public void internalMethod02065(EntityVelocityUpdateS2CPacket localValue1) {
      WorldSnapshotTracker.InternalType0205 localValue2 = this.internalField0543.get(localValue1.getEntityId());
      if (localValue2 != null) {
         this.internalField0543
            .put(localValue2.internalMethod05993(), localValue2.internalMethod06019(localValue1.getVelocity().x, localValue1.getVelocity().y, localValue1.getVelocity().z, this.internalField1058));
      }
   }

   public void internalMethod02190(EntitySetHeadYawS2CPacket localValue1) {
      int localValue2 = ((EntitySetHeadYawS2CPacketAccessor)(Object)localValue1).getEntityId();
      WorldSnapshotTracker.InternalType0205 localValue3 = this.internalField0543.get(localValue2);
      if (localValue3 != null) {
         this.internalField0543.put(localValue3.internalMethod05993(), localValue3.internalMethod07236(localValue1.getHeadYaw(), this.internalField1058));
      }
   }

   public void internalMethod02272(EntityPositionS2CPacket localValue1) {
      this.internalMethod01540(localValue1.entityId(), localValue1.change(), localValue1.onGround());
   }

   public void internalMethod01780(EntityPositionSyncS2CPacket localValue1) {
      this.internalMethod01540(localValue1.id(), localValue1.values(), localValue1.onGround());
   }

   public void internalMethod07324(EntitiesDestroyS2CPacket localValue1) {
      localValue1.getEntityIds().forEach(localValue1x -> this.internalField0543.remove(localValue1x));
   }

   public void internalMethod01676(PlayerListS2CPacket localValue1) {
      for (Entry localValue3 : localValue1.getEntries()) {
         WorldSnapshotTracker.InternalType0206 localValue4 = this.internalField0544.get(localValue3.profileId());
         if (!localValue1.getActions().contains(Action.ADD_PLAYER) && localValue4 != null) {
            String localValue8 = localValue4.internalMethod00545();
            localValue4 = new WorldSnapshotTracker.InternalType0206(
               localValue3.profileId(),
               localValue8,
               localValue3.gameMode() == null ? localValue4.internalMethod01239() : localValue3.gameMode(),
               localValue3.latency(),
               localValue3.listed(),
               localValue3.displayName() == null ? localValue4.internalMethod05901() : localValue3.displayName(),
               this.internalField1058
            );
         } else {
            GameProfile localValue5 = localValue3.profile();
            String localValue6 = localValue5 == null ? localValue3.profileId().toString() : localValue5.name();
            localValue4 = new WorldSnapshotTracker.InternalType0206(
               localValue3.profileId(), localValue6, localValue3.gameMode(), localValue3.latency(), localValue3.listed(), localValue3.displayName(), this.internalField1058
            );
         }

         this.internalField0544.put(localValue3.profileId(), localValue4);
      }
   }

   public void internalMethod00644(PlayerRemoveS2CPacket localValue1) {
      for (UUID localValue3 : localValue1.profileIds()) {
         this.internalField0544.remove(localValue3);
      }
   }

   public void internalMethod05908(GameStateChangeS2CPacket localValue1) {
      if (localValue1 != null) {
         if (localValue1.getReason() == GameStateChangeS2CPacket.GAME_MODE_CHANGED) {
            GameMode localValue2 = GameMode.byIndex(MathHelper.floor(localValue1.getValue()));
            if (localValue2 != null) {
               this.internalField0908 = this.internalField0907;
               this.internalField0907 = localValue2;
            }
         }
      }
   }

   public void internalMethod01261(ChunkLoadDistanceS2CPacket localValue1) {
      if (localValue1 != null) {
         this.internalField1053 = localValue1.getDistance();
      }
   }

   public void internalMethod00842(SimulationDistanceS2CPacket localValue1) {
      if (localValue1 != null) {
         this.internalField1055 = localValue1.simulationDistance();
      }
   }

   public void internalMethod02864(ChunkRenderDistanceCenterS2CPacket localValue1) {
      if (localValue1 != null) {
         this.internalField1056 = localValue1.getChunkX();
         this.internalField1054 = localValue1.getChunkZ();
      }
   }

   public void internalMethod04685(ScoreboardObjectiveUpdateS2CPacket localValue1) {
      if (localValue1 != null) {
         String localValue2 = localValue1.getName();
         ScoreboardObjective localValue3 = this.internalField0707.getNullableObjective(localValue2);
         if (localValue1.getMode() == 1) {
            if (localValue3 != null) {
               this.internalField0707.removeObjective(localValue3);
            }
         } else if (localValue3 == null) {
            this.internalField0707
               .addObjective(localValue2, ScoreboardCriterion.DUMMY, localValue1.getDisplayName(), localValue1.getType(), false, (NumberFormat)localValue1.getNumberFormat().orElse(null));
         } else {
            localValue3.setDisplayName(localValue1.getDisplayName());
            localValue3.setRenderType(localValue1.getType());
            localValue3.setNumberFormat((NumberFormat)localValue1.getNumberFormat().orElse(null));
         }
      }
   }

   public void internalMethod04798(ScoreboardDisplayS2CPacket localValue1) {
      if (localValue1 != null) {
         String localValue2 = localValue1.getName();
         ScoreboardObjective localValue3 = localValue2 == null ? null : this.internalField0707.getNullableObjective(localValue2);
         this.internalField0707.setObjectiveSlot(localValue1.getSlot(), localValue3);
      }
   }

   public void internalMethod07153(ScoreboardScoreUpdateS2CPacket localValue1) {
      if (localValue1 != null) {
         ScoreboardObjective localValue2 = this.internalField0707.getNullableObjective(localValue1.objectiveName());
         if (localValue2 != null) {
            ScoreAccess localValue3 = this.internalField0707.getOrCreateScore(ScoreHolder.fromName(localValue1.scoreHolderName()), localValue2);
            localValue3.setScore(localValue1.score());
            localValue1.display().ifPresent(localValue3::setDisplayText);
            localValue1.numberFormat().ifPresent(localValue3::setNumberFormat);
         }
      }
   }

   public void internalMethod05972(ScoreboardScoreResetS2CPacket localValue1) {
      if (localValue1 != null) {
         ScoreHolder localValue2 = ScoreHolder.fromName(localValue1.scoreHolderName());
         if (localValue1.objectiveName() == null) {
            this.internalField0707.removeScores(localValue2);
         } else {
            ScoreboardObjective localValue3 = this.internalField0707.getNullableObjective(localValue1.objectiveName());
            if (localValue3 != null) {
               this.internalField0707.removeScore(localValue2, localValue3);
            }
         }
      }
   }

   public void internalMethod06053(TeamS2CPacket localValue1) {
      if (localValue1 != null) {
         Team localValue2 = this.internalField0707.getTeam(localValue1.getTeamName());
         Operation localValue3 = localValue1.getTeamOperation();
         if (localValue3 == Operation.ADD) {
            localValue2 = localValue2 == null ? this.internalField0707.addTeam(localValue1.getTeamName()) : localValue2;
            Team targetTeam = localValue2;
            localValue1.getTeam().ifPresent(localValue2x -> this.internalMethod02424(targetTeam, localValue2x));
         } else {
            if (localValue3 == Operation.REMOVE) {
               if (localValue2 != null) {
                  this.internalField0707.removeTeam(localValue2);
               }

               return;
            }

            if (localValue2 != null) {
               Team targetTeam = localValue2;
               localValue1.getTeam().ifPresent(localValue2x -> this.internalMethod02424(targetTeam, localValue2x));
            }
         }

         Operation localValue4 = localValue1.getPlayerListOperation();
         if (localValue4 != null) {
            if (localValue2 == null && localValue4 == Operation.ADD) {
               localValue2 = this.internalField0707.addTeam(localValue1.getTeamName());
            }

            if (localValue2 != null) {
               for (String localValue6 : localValue1.getPlayerNames()) {
                  if (localValue4 == Operation.ADD) {
                     this.internalField0707.addScoreHolderToTeam(localValue6, localValue2);
                  } else if (localValue4 == Operation.REMOVE) {
                     this.internalField0707.removeScoreHolderFromTeam(localValue6, localValue2);
                  }
               }
            }
         }
      }
   }

   public void internalMethod04644(BotPlayerState localValue1) {
      this.internalField1058++;
      if (this.internalField0277) {
         this.internalField1059++;
      }

      this.internalField0230++;
      this.internalMethod00561(localValue1);
   }

   public void internalMethod00561(BotPlayerState localValue1) {
      if (localValue1 != null && this.internalField0228 >= 0) {
         WorldSnapshotTracker.InternalType0205 localValue2 = this.internalField0543.get(this.internalField0228);
         UUID localValue3 = localValue2 == null ? null : localValue2.internalMethod05005();
         String localValue4 = localValue2 == null ? "minecraft:player" : localValue2.internalMethod06416();
         WorldSnapshotTracker.InternalType0205 localValue5 = new WorldSnapshotTracker.InternalType0205(
            this.internalField0228,
            localValue3,
            localValue4,
            localValue1.internalMethod06297(),
            localValue1.internalMethod02808(),
            localValue1.internalMethod02817(),
            localValue1.internalMethod02808(),
            0.0,
            0.0,
            0.0,
            this.internalField1058
         );
         this.internalField0543.put(this.internalField0228, localValue5);
      }
   }

   public boolean internalMethod06540(int localValue1, int localValue2) {
      return this.internalField1197.containsKey(ChunkPos.toLong(localValue1, localValue2));
   }

   public boolean internalMethod03187(BlockPos localValue1) {
      return localValue1 != null && this.internalMethod06540(localValue1.getX() >> 4, localValue1.getZ() >> 4);
   }

   public boolean internalMethod04617(BlockPos localValue1) {
      return localValue1 != null && this.internalField1196.containsKey(localValue1);
   }

   public int internalMethod04191() {
      return this.internalField1196.size();
   }

   public BlockState internalMethod00820(BlockPos localValue1) {
      if (localValue1 == null) {
         return Blocks.AIR.getDefaultState();
      } else {
         BlockState localValue2 = this.internalField1196.get(localValue1);
         if (localValue2 != null) {
            return localValue2;
         } else {
            MinecraftClient localValue3 = MinecraftClient.getInstance();
            return !this.internalMethod05328(localValue3) ? Blocks.AIR.getDefaultState() : localValue3.world.getBlockState(localValue1);
         }
      }
   }

   public boolean internalMethod08212(BlockPos localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      ClientWorld localValue3 = localValue2.world;
      if (localValue1 == null) {
         return false;
      } else {
         BlockState localValue4 = this.internalMethod00820(localValue1);
         return !localValue4.isAir() && !localValue4.getCollisionShape((BlockView)(localValue3 == null ? EmptyBlockView.INSTANCE : localValue3), localValue1).isEmpty();
      }
   }

   public int internalMethod05054(String... localValue1) {
      for (String localValue3 : this.internalMethod07016()) {
         String localValue4 = localValue3.toUpperCase(Locale.ROOT);

         for (String localValue8 : localValue1) {
            if (localValue8 != null && !localValue8.isBlank() && localValue4.contains(localValue8.toUpperCase(Locale.ROOT))) {
               Matcher localValue9 = internalField0293.matcher(localValue3);
               if (localValue9.find()) {
                  try {
                     return Integer.parseInt(localValue9.group());
                  } catch (NumberFormatException localValue11) {
                     return -1;
                  }
               }
            }
         }
      }

      return -1;
   }

   public Collection<String> internalMethod07016() {
      KeySetView localValue1 = ConcurrentHashMap.newKeySet();

      for (ScoreboardDisplaySlot localValue5 : ScoreboardDisplaySlot.values()) {
         ScoreboardObjective localValue6 = this.internalField0707.getObjectiveForSlot(localValue5);
         if (localValue6 != null) {
            localValue1.add(localValue6.getDisplayName().getString());

            for (ScoreboardEntry localValue8 : this.internalField0707.getScoreboardEntries(localValue6)) {
               String localValue9 = localValue8.owner();
               String localValue10 = localValue8.display() == null ? localValue9 : localValue8.display().getString();
               Team localValue11 = this.internalField0707.getScoreHolderTeam(localValue9);
               if (localValue11 != null) {
                  localValue10 = localValue11.getPrefix().getString() + localValue10 + localValue11.getSuffix().getString();
               }

               localValue1.add(localValue10);
            }
         }
      }

      return localValue1;
   }

   public Collection<WorldSnapshotTracker.InternalType0205> internalMethod03121() {
      return Collections.unmodifiableCollection(this.internalField0543.values());
   }

   public Optional<WorldSnapshotTracker.InternalType0205> internalMethod01750(int localValue1) {
      return Optional.ofNullable(this.internalField0543.get(localValue1));
   }

   public Collection<WorldSnapshotTracker.InternalType0206> internalMethod07859() {
      return Collections.unmodifiableCollection(this.internalField0544.values());
   }

   private void internalMethod06422(CommonPlayerSpawnInfo localValue1, boolean localValue2) {
      if (localValue1 != null) {
         this.internalField0595 = localValue1.dimension();
         this.internalField1464 = ((DimensionType)localValue1.dimensionType().value()).minY();
         this.internalField1470 = ((DimensionType)localValue1.dimensionType().value()).height();
         this.internalField0229 = localValue1.seed();
         this.internalField0907 = localValue1.gameMode();
         this.internalField0908 = localValue1.lastGameMode();
         this.internalField0276 = localValue1.isDebug();
         this.internalField1099 = localValue1.isFlat();
         this.internalField1465 = localValue1.seaLevel();
         if (localValue2) {
            this.internalField1058 = 0L;
         }
      }
   }

   private void internalMethod02450(BlockPos localValue1, BlockState localValue2) {
      if (localValue1 != null && localValue2 != null) {
         this.internalField1196.put(localValue1.toImmutable(), localValue2);
      }
   }

   private void internalMethod02010(ChunkDataS2CPacket localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      if (localValue1 != null && localValue2.world != null) {
         Registry localValue3 = localValue2.world.getRegistryManager().getOrThrow(RegistryKeys.BIOME);
         PacketByteBuf localValue4 = new PacketByteBuf(localValue1.getChunkData().getSectionsDataBuf().copy());
         int localValue5 = localValue1.getChunkX();
         int localValue6 = localValue1.getChunkZ();
         this.internalField1196.keySet().removeIf(localValue2x -> localValue2x.getX() >> 4 == localValue5 && localValue2x.getZ() >> 4 == localValue6);
         int localValue7 = Math.max(0, this.internalField1470 >> 4);

         try {
            for (int localValue8 = 0; localValue8 < localValue7 && localValue4.isReadable(); localValue8++) {
               ChunkSection localValue9 = new ChunkSection(localValue2.world.getPalettesFactory());
               localValue9.readDataPacket(localValue4);
               if (!localValue9.isEmpty()) {
                  int localValue10 = localValue5 << 4;
                  int localValue11 = this.internalField1464 + (localValue8 << 4);
                  int localValue12 = localValue6 << 4;

                  for (int localValue13 = 0; localValue13 < 16; localValue13++) {
                     for (int localValue14 = 0; localValue14 < 16; localValue14++) {
                        for (int localValue15 = 0; localValue15 < 16; localValue15++) {
                           BlockState localValue16 = localValue9.getBlockState(localValue15, localValue13, localValue14);
                           if (!localValue16.isAir()) {
                              this.internalField1196.put(new BlockPos(localValue10 + localValue15, localValue11 + localValue13, localValue12 + localValue14), localValue16);
                           }
                        }
                     }
                  }
               }
            }
         } catch (Exception localValue20) {
            this.internalField1196.keySet().removeIf(localValue2x -> localValue2x.getX() >> 4 == localValue5 && localValue2x.getZ() >> 4 == localValue6);
         } finally {
            localValue4.release();
         }
      }
   }

   private boolean internalMethod05328(MinecraftClient localValue1) {
      if (localValue1 == null || localValue1.world == null || this.internalField0595 == null || !this.internalField0595.equals(localValue1.world.getRegistryKey())) {
         return false;
      } else if (localValue1.getCurrentServerEntry() == null) {
         return false;
      } else {
         WorldSnapshotTracker.InternalType0458 localValue2 = this.internalMethod05738(localValue1.getCurrentServerEntry().address);
         return localValue2 != null && localValue2.internalMethod07077().equalsIgnoreCase(this.internalField0247) && localValue2.internalMethod06368() == this.internalField0227;
      }
   }

   private WorldSnapshotTracker.InternalType0458 internalMethod05738(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         String localValue2 = localValue1.trim();
         int localValue3 = 25565;
         int localValue4 = localValue2.lastIndexOf(58);
         if (localValue4 > 0 && localValue4 < localValue2.length() - 1) {
            try {
               localValue3 = Integer.parseInt(localValue2.substring(localValue4 + 1));
               localValue2 = localValue2.substring(0, localValue4);
            } catch (NumberFormatException localValue6) {
            }
         }

         return new WorldSnapshotTracker.InternalType0458(localValue2, localValue3);
      } else {
         return null;
      }
   }

   private void internalMethod02424(Team localValue1, SerializableTeam localValue2) {
      if (localValue1 != null && localValue2 != null) {
         localValue1.setDisplayName(localValue2.getDisplayName());
         localValue1.setFriendlyFlagsBitwise(localValue2.getFriendlyFlagsBitwise());
         Formatting localValue3 = localValue2.getColor();
         if (localValue3 != null) {
            localValue1.setColor(localValue3);
         }

         localValue1.setPrefix(localValue2.getPrefix());
         localValue1.setSuffix(localValue2.getSuffix());
         VisibilityRule localValue4 = java.util.Arrays.stream(VisibilityRule.values())
            .filter(rule -> rule.asString().equals(localValue2.getNameTagVisibilityRule()))
            .findFirst().orElse(null);
         if (localValue4 != null) {
            localValue1.setNameTagVisibilityRule(localValue4);
         }

         CollisionRule localValue5 = java.util.Arrays.stream(CollisionRule.values())
            .filter(rule -> rule.asString().equals(localValue2.getCollisionRule()))
            .findFirst().orElse(null);
         if (localValue5 != null) {
            localValue1.setCollisionRule(localValue5);
         }
      }
   }

   private void internalMethod08711() {
      for (ScoreboardObjective localValue2 : Set.copyOf(this.internalField0707.getObjectives())) {
         this.internalField0707.removeObjective(localValue2);
      }

      for (Team localValue4 : Set.copyOf(this.internalField0707.getTeams())) {
         this.internalField0707.removeTeam(localValue4);
      }
   }

   private void internalMethod01540(int localValue1, EntityPosition localValue2, boolean localValue3) {
      if (localValue2 != null) {
         WorldSnapshotTracker.InternalType0205 localValue4 = this.internalField0543.get(localValue1);
         if (localValue4 == null) {
            localValue4 = new WorldSnapshotTracker.InternalType0205(
               localValue1,
               null,
               localValue1 == this.internalField0228 ? "minecraft:player" : "minecraft:unknown",
               localValue2.position(),
               localValue2.yaw(),
               localValue2.pitch(),
               localValue2.yaw(),
               localValue2.deltaMovement().x,
               localValue2.deltaMovement().y,
               localValue2.deltaMovement().z,
               this.internalField1058
            );
         } else {
            localValue4 = localValue4.internalMethod00126(localValue2.position(), localValue2.yaw(), localValue2.pitch(), localValue4.internalMethod08641(), this.internalField1058)
               .internalMethod06019(localValue2.deltaMovement().x, localValue2.deltaMovement().y, localValue2.deltaMovement().z, this.internalField1058);
         }

         this.internalField0543.put(localValue1, localValue4);
      }
   }

   private String internalMethod06828(EntityType<?> localValue1) {
      Identifier localValue2 = Registries.ENTITY_TYPE.getId(localValue1);
      return localValue2 == null ? "minecraft:unknown" : localValue2.toString();
   }

   @Generated
   public String internalMethod05848() {
      return this.internalField0248;
   }

   @Generated
   public Map<Integer, WorldSnapshotTracker.InternalType0205> internalMethod02136() {
      return this.internalField0543;
   }

   @Generated
   public Map<UUID, WorldSnapshotTracker.InternalType0206> internalMethod06766() {
      return this.internalField0544;
   }

   @Generated
   public Map<Long, Long> internalMethod08534() {
      return this.internalField1197;
   }

   @Generated
   public Map<BlockPos, BlockState> internalMethod07895() {
      return this.internalField1196;
   }

   @Generated
   public Scoreboard internalMethod01078() {
      return this.internalField0707;
   }

   @Generated
   public String internalMethod02411() {
      return this.internalField0247;
   }

   @Generated
   public int internalMethod04197() {
      return this.internalField0227;
   }

   @Generated
   public int internalMethod08709() {
      return this.internalField0228;
   }

   @Generated
   public RegistryKey<World> internalMethod06834() {
      return this.internalField0595;
   }

   @Generated
   public Set<RegistryKey<World>> internalMethod02137() {
      return this.internalField0546;
   }

   @Generated
   public GameMode internalMethod01148() {
      return this.internalField0907;
   }

   @Generated
   public GameMode internalMethod07022() {
      return this.internalField0908;
   }

   @Generated
   public long internalMethod04192() {
      return this.internalField0229;
   }

   @Generated
   public long internalMethod04198() {
      return this.internalField0230;
   }

   @Generated
   public long internalMethod08710() {
      return this.internalField1059;
   }

   @Generated
   public boolean internalMethod04194() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod04200() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod08712() {
      return this.internalField1099;
   }

   @Generated
   public int internalMethod08713() {
      return this.internalField1053;
   }

   @Generated
   public int internalMethod08726() {
      return this.internalField1055;
   }

   @Generated
   public int internalMethod08727() {
      return this.internalField1056;
   }

   @Generated
   public int internalMethod09799() {
      return this.internalField1054;
   }

   @Generated
   public int internalMethod09800() {
      return this.internalField1464;
   }

   @Generated
   public int internalMethod09804() {
      return this.internalField1470;
   }

   @Generated
   public int internalMethod09805() {
      return this.internalField1465;
   }

   @Generated
   public long internalMethod08714() {
      return this.internalField1058;
   }

   @Generated
   public boolean internalMethod08715() {
      return this.internalField1100;
   }

   @Generated
   public double internalMethod04190() {
      return this.internalField0194;
   }

   public static final class InternalType0205 {
      private final int internalField0227;
      private final UUID internalField0428;
      private final String internalField0248;
      private final Vec3d internalField0283;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;
      private final double internalField0194;
      private final double internalField0193;
      private final double internalField1045;
      private final long internalField0229;

      public InternalType0205(
         int localValue1, UUID localValue2, String localValue3, Vec3d localValue4, float localValue5, float localValue6, float localValue7, double localValue8, double localValue10, double localValue12, long localValue14
      ) {
         this.internalField0227 = localValue1;
         this.internalField0428 = localValue2;
         this.internalField0248 = localValue3;
         this.internalField0283 = localValue4;
         this.internalField0205 = localValue5;
         this.internalField0206 = localValue6;
         this.internalField1048 = localValue7;
         this.internalField0194 = localValue8;
         this.internalField0193 = localValue10;
         this.internalField1045 = localValue12;
         this.internalField0229 = localValue14;
      }

      WorldSnapshotTracker.InternalType0205 internalMethod00126(Vec3d localValue1, float localValue2, float localValue3, float localValue4, long localValue5) {
         return new WorldSnapshotTracker.InternalType0205(
            this.internalField0227,
            this.internalField0428,
            this.internalField0248,
            localValue1,
            MathHelper.wrapDegrees(localValue2),
            MathHelper.clamp(localValue3, -90.0F, 90.0F),
            localValue4,
            this.internalField0194,
            this.internalField0193,
            this.internalField1045,
            localValue5
         );
      }

      WorldSnapshotTracker.InternalType0205 internalMethod06019(double localValue1, double localValue3, double localValue5, long localValue7) {
         return new WorldSnapshotTracker.InternalType0205(
            this.internalField0227,
            this.internalField0428,
            this.internalField0248,
            this.internalField0283,
            this.internalField0205,
            this.internalField0206,
            this.internalField1048,
            localValue1,
            localValue3,
            localValue5,
            localValue7
         );
      }

      WorldSnapshotTracker.InternalType0205 internalMethod07236(float localValue1, long localValue2) {
         return new WorldSnapshotTracker.InternalType0205(
            this.internalField0227,
            this.internalField0428,
            this.internalField0248,
            this.internalField0283,
            this.internalField0205,
            this.internalField0206,
            MathHelper.wrapDegrees(localValue1),
            this.internalField0194,
            this.internalField0193,
            this.internalField1045,
            localValue2
         );
      }

      @Override
      public final String toString() {
         return "InternalType0205[id=" + this.internalField0227 + ", uuid=" + this.internalField0428 + ", typeId=" + this.internalField0248 + ", position=" + this.internalField0283 + ", yaw=" + this.internalField0205 + ", pitch=" + this.internalField0206 + ", headYaw=" + this.internalField1048 + ", velocityX=" + this.internalField0194 + ", velocityY=" + this.internalField0193 + ", velocityZ=" + this.internalField1045 + ", lastSeenTick=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0428);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1045);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldSnapshotTracker.InternalType0205 other = (WorldSnapshotTracker.InternalType0205) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0428, other.internalField0428)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193)
            && java.util.Objects.equals(this.internalField1045, other.internalField1045)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public int internalMethod05993() {
         return this.internalField0227;
      }

      public UUID internalMethod05005() {
         return this.internalField0428;
      }

      public String internalMethod06416() {
         return this.internalField0248;
      }

      public Vec3d internalMethod02495() {
         return this.internalField0283;
      }

      public float internalMethod05992() {
         return this.internalField0205;
      }

      public float internalMethod05997() {
         return this.internalField0206;
      }

      public float internalMethod08641() {
         return this.internalField1048;
      }

      public double internalMethod05991() {
         return this.internalField0194;
      }

      public double internalMethod05996() {
         return this.internalField0193;
      }

      public double internalMethod08640() {
         return this.internalField1045;
      }

      public long internalMethod05994() {
         return this.internalField0229;
      }
   }

   public static final class InternalType0206 {
      private final UUID internalField0428;
      private final String internalField0248;
      private final GameMode internalField0907;
      private final int internalField0227;
      private final boolean internalField0277;
      private final Text internalField0125;
      private final long internalField0229;

      public InternalType0206(UUID localValue1, String localValue2, GameMode localValue3, int localValue4, boolean localValue5, Text localValue6, long localValue7) {
         this.internalField0428 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0907 = localValue3;
         this.internalField0227 = localValue4;
         this.internalField0277 = localValue5;
         this.internalField0125 = localValue6;
         this.internalField0229 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0206[uuid=" + this.internalField0428 + ", name=" + this.internalField0248 + ", gameMode=" + this.internalField0907 + ", latency=" + this.internalField0227 + ", listed=" + this.internalField0277 + ", displayName=" + this.internalField0125 + ", lastSeenTick=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0428);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0907);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0125);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldSnapshotTracker.InternalType0206 other = (WorldSnapshotTracker.InternalType0206) localValue1;
         return java.util.Objects.equals(this.internalField0428, other.internalField0428)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0907, other.internalField0907)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0125, other.internalField0125)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public UUID internalMethod04243() {
         return this.internalField0428;
      }

      public String internalMethod00545() {
         return this.internalField0248;
      }

      public GameMode internalMethod01239() {
         return this.internalField0907;
      }

      public int internalMethod00736() {
         return this.internalField0227;
      }

      public boolean internalMethod00738() {
         return this.internalField0277;
      }

      public Text internalMethod05901() {
         return this.internalField0125;
      }

      public long internalMethod00737() {
         return this.internalField0229;
      }
   }

   static final class InternalType0458 {
      private final String internalField0248;
      private final int internalField0227;

      InternalType0458(String localValue1, int localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0458[address=" + this.internalField0248 + ", port=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldSnapshotTracker.InternalType0458 other = (WorldSnapshotTracker.InternalType0458) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public String internalMethod07077() {
         return this.internalField0248;
      }

      public int internalMethod06368() {
         return this.internalField0227;
      }
   }
}
