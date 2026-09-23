package globals.client;


import rockstar.client.server.*;
import globals.shared.proto.Packets;
import moscow.rockstar.mixin.accessors.BiomeAccesAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;

public final class WorldKey implements MinecraftClientAccess {
   private final String server;
   private final int anarchy;
   private final String world;
   private final boolean single;
   private static final WorldKey NONE = new WorldKey("", -1, "none", true);
   public WorldKey(String localValue1, int localValue2, String localValue3, boolean localValue4) {
      this.server = localValue1;
      this.anarchy = localValue2;
      this.world = localValue3;
      this.single = localValue4;
   }

   public static WorldKey local() {
      ClientWorld localValue0 = internalField0149.world;
      if (localValue0 == null) {
         return NONE;
      } else {
         try {
            return new WorldKey(ServerUtils.internalMethod06458(false), localAnarchy(), token(localValue0), internalField0149.isInSingleplayer());
         } catch (Exception localValue2) {
            return NONE;
         }
      }
   }

   public static WorldKey of(Packets.InternalType0031 localValue0) {
      return localValue0 == null ? NONE : new WorldKey(nz(localValue0.server()), anarchy(localValue0.anarchy()), nz(localValue0.hash()), "single".equals(localValue0.ip()));
   }

   public static boolean sameWorld(Packets.InternalType0031 localValue0) {
      if (localValue0 == null) {
         return false;
      } else {
         WorldKey localValue1 = local();
         WorldKey localValue2 = of(localValue0);
         if (!localValue1.sameServer(localValue2)) {
            return false;
         } else {
            return localValue1.identified() && localValue2.identified() ? localValue1.world.equals(localValue2.world) : inTab(localValue0.nickname());
         }
      }
   }

   public boolean identified() {
      return this.world.startsWith("w1;") && !this.world.endsWith(";?");
   }

   public String stamp() {
      return this.server + "|" + this.anarchy + "|" + this.world;
   }

   private boolean sameServer(WorldKey localValue1) {
      if (!this.single && !localValue1.single) {
         boolean localValue2 = "???".equals(this.server) || "???".equals(localValue1.server);
         return localValue2 || !this.server.isBlank() && this.server.equals(localValue1.server)
            ? this.anarchy < 0 || localValue1.anarchy < 0 || this.anarchy == localValue1.anarchy
            : false;
      } else {
         return false;
      }
   }

   private static String token(ClientWorld localValue0) {
      String localValue1 = localValue0.getRegistryKey().getValue().toString();
      long localValue2 = ((BiomeAccesAccessor)(Object)localValue0.getBiomeAccess()).rockstar$getSeed();
      if (localValue2 != 0L) {
         return "w1;" + localValue1 + ";s" + Long.toUnsignedString(localValue2, 36);
      } else {
         BlockPos localValue4 = localValue0.getSpawnPoint().getPos();
         return localValue4 != null && !BlockPos.ORIGIN.equals(localValue4) ? "w1;" + localValue1 + ";p" + localValue4.getX() + "," + localValue4.getY() + "," + localValue4.getZ() : "w1;" + localValue1 + ";?";
      }
   }

   private static boolean inTab(String localValue0) {
      ClientPlayerEntity localValue1 = internalField0149.player;
      if (localValue0 != null && !localValue0.isBlank() && localValue1 != null && localValue1.networkHandler != null) {
         for (PlayerListEntry localValue3 : localValue1.networkHandler.getPlayerList()) {
            if (localValue0.equals(localValue3.getProfile().name())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static int localAnarchy() {
      try {
         return ServerUtils.internalMethod07319();
      } catch (Exception localValue1) {
         return -1;
      }
   }

   private static int anarchy(String localValue0) {
      try {
         return Integer.parseInt(localValue0.trim());
      } catch (Exception localValue2) {
         return -1;
      }
   }

   private static String nz(String localValue0) {
      return localValue0 == null ? "" : localValue0;
   }

   @Override
   public final String toString() {
      return "WorldKey[server=" + this.server() + ", anarchy=" + this.anarchy() + ", world=" + this.world() + ", single=" + this.single() + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.server());
      result = 31 * result + java.util.Objects.hashCode(this.anarchy());
      result = 31 * result + java.util.Objects.hashCode(this.world());
      result = 31 * result + java.util.Objects.hashCode(this.single());
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      WorldKey other = (WorldKey) localValue1;
      return java.util.Objects.equals(this.server(), other.server())
         && java.util.Objects.equals(this.anarchy(), other.anarchy())
         && java.util.Objects.equals(this.world(), other.world())
         && java.util.Objects.equals(this.single(), other.single());
   }

   public String server() {
      return this.server;
   }

   public int anarchy() {
      return this.anarchy;
   }

   public String world() {
      return this.world;
   }

   public boolean single() {
      return this.single;
   }
}
