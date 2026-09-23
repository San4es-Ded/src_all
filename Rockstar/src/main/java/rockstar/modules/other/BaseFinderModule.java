package rockstar.modules.other;




import rockstar.client.util.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import pyrock.events.game.WorldChangeEvent;

@ModuleInfo(
   name = "Base Finder",
   category = ModuleCategory.OTHER
)
public class BaseFinderModule extends Module {
   private static final Set<Block> internalField0546 = Set.of(
      Blocks.AMETHYST_CLUSTER,
      Blocks.LARGE_AMETHYST_BUD,
      Blocks.MEDIUM_AMETHYST_BUD,
      Blocks.SMALL_AMETHYST_BUD,
      Blocks.GLOW_LICHEN,
      Blocks.REDSTONE_TORCH,
      Blocks.REDSTONE_WALL_TORCH
   );
   private final ArrayDeque<Long> internalField0881 = new ArrayDeque<>();
   private final Set<Long> internalField0545 = new HashSet<>();
   private final Set<Long> internalField1200 = new HashSet<>();
   private final List<BaseFinderModule.InternalType0022> internalField0416 = new ArrayList<>();
   private int internalField1465;
   private int internalField1463 = 1;
   private final EventListener<WorldChangeEvent> internalField0157 = localValue1 -> {
      this.internalMethod09536();
      this.internalMethod09062();
   };

   @Override
   public void onEnable() {
      this.internalMethod09536();
      super.onEnable();
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.world != null && internalField0149.player != null) {
         int localValue1 = internalField0149.player.getChunkPos().x;
         int localValue2 = internalField0149.player.getChunkPos().z;
         int localValue3 = this.internalMethod09063();
         if (this.internalField1465++ % 5 == 0 || this.internalField0881.isEmpty()) {
            this.internalMethod04703(localValue1, localValue2, localValue3);
            this.internalMethod07803(localValue1, localValue2, localValue3 + 2);
         }

         this.internalMethod01565(localValue1, localValue2, localValue3 + 2);
         super.internalMethod08229();
      }
   }

   public List<BaseFinderModule.InternalType0022> internalMethod01743() {
      synchronized (this.internalField0416) {
         return List.copyOf(this.internalField0416);
      }
   }

   public int internalMethod09062() {
      synchronized (this.internalField0416) {
         int localValue2 = this.internalField0416.size();
         this.internalField0416.clear();
         this.internalField1463 = 1;
         return localValue2;
      }
   }

   public BaseFinderModule.InternalType0022 internalMethod02420(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         String localValue2 = this.internalMethod04565(localValue1);
         synchronized (this.internalField0416) {
            for (int localValue4 = 0; localValue4 < this.internalField0416.size(); localValue4++) {
               BaseFinderModule.InternalType0022 localValue5 = this.internalField0416.get(localValue4);
               if (this.internalMethod04565(localValue5.internalMethod05742()).equals(localValue2)) {
                  return this.internalField0416.remove(localValue4);
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public BaseFinderModule.InternalType0022 internalMethod05915(BlockPos localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         synchronized (this.internalField0416) {
            for (int localValue3 = 0; localValue3 < this.internalField0416.size(); localValue3++) {
               BaseFinderModule.InternalType0022 localValue4 = this.internalField0416.get(localValue3);
               if (localValue4.internalMethod02033().equals(localValue1)) {
                  return this.internalField0416.remove(localValue3);
               }
            }

            return null;
         }
      }
   }

   private void internalMethod04703(int localValue1, int localValue2, int localValue3) {
      if (internalField0149.world != null) {
         ClientChunkManager localValue4 = internalField0149.world.getChunkManager();

         for (int localValue5 = 0; localValue5 <= localValue3; localValue5++) {
            for (int localValue6 = -localValue5; localValue6 <= localValue5; localValue6++) {
               for (int localValue7 = -localValue5; localValue7 <= localValue5; localValue7++) {
                  if (Math.max(Math.abs(localValue6), Math.abs(localValue7)) == localValue5) {
                     this.internalMethod02877(localValue4, localValue1 + localValue6, localValue2 + localValue7);
                  }
               }
            }
         }
      }
   }

   private void internalMethod02877(ClientChunkManager localValue1, int localValue2, int localValue3) {
      long localValue4 = ChunkPos.toLong(localValue2, localValue3);
      if (!this.internalField1200.contains(localValue4) && !this.internalField0545.contains(localValue4)) {
         if (internalField0149.world.isChunkLoaded(localValue2, localValue3)) {
            if (localValue1.getChunk(localValue2, localValue3, ChunkStatus.FULL, false) != null) {
               this.internalField0881.addLast(localValue4);
               this.internalField0545.add(localValue4);
            }
         }
      }
   }

   private void internalMethod01565(int localValue1, int localValue2, int localValue3) {
      if (internalField0149.world != null) {
         ClientChunkManager localValue4 = internalField0149.world.getChunkManager();
         int localValue5 = 0;

         while (localValue5 < 2 && !this.internalField0881.isEmpty()) {
            long localValue6 = this.internalField0881.removeFirst();
            this.internalField0545.remove(localValue6);
            if (this.internalMethod03868(localValue6, localValue1, localValue2, localValue3)) {
               int localValue8 = ChunkPos.getPackedX(localValue6);
               int localValue9 = ChunkPos.getPackedZ(localValue6);
               WorldChunk localValue10 = localValue4.getChunk(localValue8, localValue9, ChunkStatus.FULL, false);
               if (localValue10 != null) {
                  this.internalMethod03737(localValue10);
                  this.internalField1200.add(localValue6);
                  localValue5++;
               }
            }
         }
      }
   }

   private void internalMethod03737(WorldChunk localValue1) {
      if (internalField0149.world != null) {
         int localValue2 = localValue1.getPos().getStartX();
         int localValue3 = localValue1.getPos().getStartZ();
         int localValue4 = localValue1.getBottomY();
         int localValue5 = localValue4 + localValue1.getHeight();
         Mutable localValue6 = new Mutable();

         for (int localValue7 = localValue2; localValue7 < localValue2 + 16; localValue7++) {
            for (int localValue8 = localValue3; localValue8 < localValue3 + 16; localValue8++) {
               for (int localValue9 = localValue4; localValue9 < localValue5; localValue9++) {
                  localValue6.set(localValue7, localValue9, localValue8);
                  if (this.internalMethod03500(localValue1, localValue6)) {
                     this.internalMethod00282(localValue6.toImmutable());
                  }
               }
            }
         }
      }
   }

   private boolean internalMethod03500(WorldChunk localValue1, BlockPos localValue2) {
      if (this.internalMethod00281(localValue2) != 7) {
         return false;
      } else {
         BlockState localValue3 = localValue1.getBlockState(localValue2);
         return localValue3.isOf(Blocks.ENDER_CHEST) ? true : this.internalMethod02792(localValue3, localValue2) && !this.internalMethod00283(localValue2) && this.internalMethod01696(localValue2);
      }
   }

   private boolean internalMethod02792(BlockState localValue1, BlockPos localValue2) {
      return !localValue1.isAir() && localValue1.getLuminance() <= 0 ? localValue1.isOpaqueFullCube() || localValue1.isFullCube(internalField0149.world, localValue2) : false;
   }

   private boolean internalMethod00283(BlockPos localValue1) {
      Mutable localValue2 = new Mutable();

      for (int localValue3 = -7; localValue3 <= 7; localValue3++) {
         for (int localValue4 = -7; localValue4 <= 7; localValue4++) {
            for (int localValue5 = -7; localValue5 <= 7; localValue5++) {
               int localValue6 = Math.abs(localValue3) + Math.abs(localValue4) + Math.abs(localValue5);
               if (localValue6 != 0 && localValue6 <= 7) {
                  localValue2.set(localValue1.getX() + localValue3, localValue1.getY() + localValue4, localValue1.getZ() + localValue5);
                  if (!internalField0149.world.isChunkLoaded(localValue2.getX() >> 4, localValue2.getZ() >> 4)) {
                     return true;
                  }

                  BlockState localValue7 = internalField0149.world.getBlockState(localValue2);
                  if (!localValue7.isOf(Blocks.ENDER_CHEST) && (localValue7.getLuminance() > 0 || internalField0546.contains(localValue7.getBlock()))) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean internalMethod01696(BlockPos localValue1) {
      Mutable localValue2 = new Mutable();
      int localValue3 = 0;
      int localValue4 = 0;

      for (int localValue5 = -7; localValue5 <= 7; localValue5++) {
         for (int localValue6 = -7; localValue6 <= 7; localValue6++) {
            for (int localValue7 = -7; localValue7 <= 7; localValue7++) {
               int localValue8 = Math.abs(localValue5) + Math.abs(localValue6) + Math.abs(localValue7);
               if (localValue8 != 0 && localValue8 <= 7) {
                  localValue2.set(localValue1.getX() + localValue5, localValue1.getY() + localValue6, localValue1.getZ() + localValue7);
                  if (!internalField0149.world.isChunkLoaded(localValue2.getX() >> 4, localValue2.getZ() >> 4)) {
                     return false;
                  }

                  int localValue9 = this.internalMethod00281(localValue2);
                  int localValue10 = 7 - localValue8;
                  if (localValue9 > localValue10) {
                     return false;
                  }

                  if (localValue8 == 1 && localValue9 == localValue10) {
                     localValue3++;
                  } else if (localValue8 == 2 && localValue9 == localValue10) {
                     localValue4++;
                  }
               }
            }
         }
      }

      return localValue3 >= 4 && localValue4 >= 6;
   }

   private int internalMethod00281(BlockPos localValue1) {
      return internalField0149.world == null ? 0 : internalField0149.world.getLightLevel(LightType.BLOCK, localValue1);
   }

   private void internalMethod00282(BlockPos localValue1) {
      synchronized (this.internalField0416) {
         if (!this.internalMethod07771(localValue1)) {
            BaseFinderModule.InternalType0022 localValue3 = new BaseFinderModule.InternalType0022("\u0411\u0430\u0437\u0430-" + this.internalField1463++, localValue1);
            this.internalField0416.add(localValue3);
            ClientMessages.internalMethod01809(Text.of(internalMethod07316(localValue3)));
         }
      }
   }

   private boolean internalMethod07771(BlockPos localValue1) {
      for (BaseFinderModule.InternalType0022 localValue3 : this.internalField0416) {
         long localValue4 = (long)localValue1.getX() - localValue3.internalMethod02033().getX();
         long localValue6 = (long)localValue1.getY() - localValue3.internalMethod02033().getY();
         long localValue8 = (long)localValue1.getZ() - localValue3.internalMethod02033().getZ();
         if (localValue4 * localValue4 + localValue6 * localValue6 + localValue8 * localValue8 <= 2304L) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod07803(int localValue1, int localValue2, int localValue3) {
      this.internalField1200.removeIf(localValue4 -> !this.internalMethod03868(localValue4, localValue1, localValue2, localValue3));
   }

   private boolean internalMethod03868(long localValue1, int localValue3, int localValue4, int localValue5) {
      return Math.abs(ChunkPos.getPackedX(localValue1) - localValue3) <= localValue5 && Math.abs(ChunkPos.getPackedZ(localValue1) - localValue4) <= localValue5;
   }

   private int internalMethod09063() {
      int localValue1 = internalField0149.options != null ? (Integer)internalField0149.options.getViewDistance().getValue() : 8;
      return Math.max(1, Math.min(localValue1, 10));
   }

   private void internalMethod09536() {
      this.internalField1465 = 0;
      this.internalField0881.clear();
      this.internalField0545.clear();
      this.internalField1200.clear();
   }

   private String internalMethod04565(String localValue1) {
      String localValue2 = localValue1.trim().toLowerCase();
      return localValue2.matches("\\d+") ? "base-" + localValue2 : localValue2.replace("\u0431\u0430\u0437\u0430", "base").replace("baza", "base").replace(" ", "");
   }

   public static String internalMethod07316(BaseFinderModule.InternalType0022 localValue0) {
      return internalMethod03312(localValue0.internalMethod05742(), localValue0.internalMethod02033());
   }

   public static String internalMethod03312(String localValue0, BlockPos localValue1) {
      return localValue0 + " - \u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b: " + localValue1.getX() + " " + localValue1.getY() + " " + localValue1.getZ();
   }

   public static final class InternalType0022 {
      private final String internalField0248;
      private final BlockPos internalField0352;

      public InternalType0022(String localValue1, BlockPos localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0352 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0022[name=" + this.internalField0248 + ", pos=" + this.internalField0352 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BaseFinderModule.InternalType0022 other = (BaseFinderModule.InternalType0022) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0352, other.internalField0352);
      }

      public String internalMethod05742() {
         return this.internalField0248;
      }

      public BlockPos internalMethod02033() {
         return this.internalField0352;
      }
   }
}
