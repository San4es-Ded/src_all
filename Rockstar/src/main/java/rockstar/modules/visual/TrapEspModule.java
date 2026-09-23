package rockstar.modules.visual;









import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Direction.Type;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Trap ESP",
   category = ModuleCategory.VISUALS
)
public class TrapEspModule extends Module {
   private volatile List<TrapEspModule.InternalType0101> internalField0416 = Collections.emptyList();
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Deque<BlockPos> internalField0796 = new ConcurrentLinkedDeque<>();
   private final Set<BlockPos> internalField0546 = Collections.newSetFromMap(new ConcurrentHashMap<>());
   private final Map<Long, TrapEspModule.InternalType0101> internalField0543 = new ConcurrentHashMap<>();
   private static final int internalField0227 = 64;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (this.internalField0519.internalMethod02365(5000L) && this.internalField0796.isEmpty()) {
            this.internalMethod09282();
            this.internalField0519.internalMethod00701();
         }

         int localValue2 = 0;
         long localValue3 = 2000000L;
         long localValue5 = System.nanoTime();

         BlockPos localValue7;
         while (localValue2 < 96 && (localValue7 = this.internalField0796.poll()) != null) {
            this.internalField0546.remove(localValue7);
            localValue2++;
            TrapEspModule.InternalType0101 localValue8 = this.internalMethod02362(localValue7);
            long localValue9 = internalMethod01185(localValue7.getX(), localValue7.getZ());
            if (localValue8 != null) {
               this.internalField0543.put(localValue9, localValue8);
            } else {
               this.internalField0543.remove(localValue9);
            }

            if (System.nanoTime() - localValue5 > localValue3) {
               break;
            }
         }

         this.internalField0416 = new ArrayList<>(this.internalField0543.values());
      }
   };
   private final EventListener<ReceivePacketEvent> internalField0158 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         try {
            Packet localValue2 = localValue1.getPacket();
            if (localValue2 instanceof ChunkDataS2CPacket localValue3) {
               this.internalMethod03178(new ChunkPos(localValue3.getChunkX(), localValue3.getChunkZ()));
            }

            if (localValue2 instanceof ChunkDeltaUpdateS2CPacket localValue6) {
               localValue6.visitUpdates((localValue1x, localValue2x) -> this.internalMethod01186(localValue1x.getX(), localValue1x.getZ()));
            }

            if (localValue2 instanceof BlockUpdateS2CPacket localValue7) {
               BlockPos localValue4 = localValue7.getPos();
               this.internalMethod01186(localValue4.getX(), localValue4.getZ());
            }
         } catch (Throwable localValue5) {
         }
      }
   };
   private final EventListener<WorldChangeEvent> internalField1028 = localValue1 -> this.internalMethod09281();
   private final EventListener<PreHudRenderEvent> internalField1029 = localValue1 -> {
      CustomDrawContext localValue2 = localValue1.getContext();
      org.joml.Matrix3x2fStack localValue3 = localValue2.getMatrices();
      SizedFont localValue4 = Fonts.internalField0449.internalMethod01432(9.0F);
      int localValue5 = (int)localValue4.internalMethod04890();

      for (TrapEspModule.InternalType0101 localValue7 : this.internalField0416) {
         Vec2f localValue8 = ScreenProjectionUtils.internalMethod00612(localValue7.internalField0352.toCenterPos().add(0.0, 0.5, 0.0));
         if (localValue8 != null) {
            String localValue9 = LanguageManager.internalMethod07214("modules.trap_esp.label");
            String localValue10 = LanguageManager.internalMethod00160("modules.trap_esp.depth", localValue7.internalField0227);
            String localValue11 = localValue7.internalField0277
               ? LanguageManager.internalMethod07214("modules.trap_esp.private")
               : LanguageManager.internalMethod07214("modules.trap_esp.no_private");
            byte localValue12 = 9;
            byte localValue13 = 6;
            int localValue14 = (int)(localValue4.internalMethod00965(localValue10) + localValue13);
            float localValue15 = localValue4.internalMethod00965(localValue9) + 6.0F + localValue12 + 2.0F;
            float localValue16 = localValue5 * 2;
            float localValue17 = localValue16 * 3.0F;
            localValue2.pushMatrix();
            localValue3.translate(localValue8.x, localValue8.y - localValue17);
            localValue2.drawRect(-localValue15 / 2.0F, 0.0F, localValue15, localValue16, ColorRGBA.BLACK.withAlpha(150.0F));
            localValue2.drawText(localValue4, localValue9, -localValue15 / 2.0F + localValue12 + 5.0F, 3.0F, ThemeColors.internalField1312);
            localValue2.drawTexture(
               RockstarClient.id("icons/trap.png"), -localValue15 / 2.0F + 2.0F, localValue5 - localValue12 / 2.0F, localValue12, localValue12, ThemeColors.internalField1312
            );
            localValue2.drawRect(-localValue14 / 2.0F, localValue16, localValue14, localValue16, ColorRGBA.BLACK.withAlpha(150.0F));
            localValue2.drawText(localValue4, localValue10, -localValue14 / 2.0F + 2.0F, localValue16 + 3.0F, ThemeColors.internalField1312.withAlpha(200.0F));
            localValue2.drawRect(
               -(localValue4.internalMethod00965(localValue11) + 6.0F) / 2.0F, localValue16 * 2.0F, localValue4.internalMethod00965(localValue11) + 6.0F, localValue16, ColorRGBA.BLACK.withAlpha(150.0F)
            );
            localValue2.drawText(localValue4, localValue11, -localValue4.internalMethod00965(localValue11) / 2.0F, localValue16 * 2.0F + 3.0F, ThemeColors.internalField1312.withAlpha(200.0F));
            localValue2.popMatrix();
         }
      }
   };

   @Override
   public void onDisable() {
      this.internalMethod09281();
      super.onDisable();
   }

   @Override
   public void onEnable() {
      this.internalMethod09281();
      this.internalMethod09282();
      super.onEnable();
   }

   public void internalMethod09281() {
      this.internalField0796.clear();
      this.internalField0546.clear();
      this.internalField0543.clear();
      this.internalField0416 = Collections.emptyList();
      this.internalField0519.internalMethod00701();
   }

   private void internalMethod09282() {
      if (internalField0149.player != null) {
         BlockPos localValue1 = internalField0149.player.getBlockPos();
         ArrayList localValue2 = new ArrayList(16641);

         for (int localValue3 = -64; localValue3 <= 64; localValue3++) {
            for (int localValue4 = -64; localValue4 <= 64; localValue4++) {
               BlockPos localValue5 = new BlockPos(localValue1.getX() + localValue3, localValue1.getY(), localValue1.getZ() + localValue4);
               if (this.internalField0546.add(localValue5)) {
                  localValue2.add(localValue5);
               }
            }
         }

         localValue2.sort(Comparator.comparingDouble(localValue1x -> ((net.minecraft.util.math.BlockPos)localValue1x).getSquaredDistance(localValue1)));
         this.internalField0796.addAll(localValue2);
      }
   }

   private void internalMethod03178(ChunkPos localValue1) {
      if (internalField0149.player != null) {
         int localValue2 = internalField0149.player.getBlockPos().getY();
         int localValue3 = localValue1.getStartX();
         int localValue4 = localValue1.getStartZ();

         for (int localValue5 = localValue3; localValue5 < localValue3 + 16; localValue5++) {
            for (int localValue6 = localValue4; localValue6 < localValue4 + 16; localValue6++) {
               BlockPos localValue7 = new BlockPos(localValue5, localValue2, localValue6);
               if (this.internalField0546.add(localValue7)) {
                  this.internalField0796.add(localValue7);
               }
            }
         }
      }
   }

   private void internalMethod01186(int localValue1, int localValue2) {
      if (internalField0149.player != null) {
         int localValue3 = internalField0149.player.getBlockPos().getY();
         BlockPos localValue4 = new BlockPos(localValue1, localValue3, localValue2);
         if (this.internalField0546.add(localValue4)) {
            this.internalField0796.add(localValue4);
         }
      }
   }

   private TrapEspModule.InternalType0101 internalMethod02362(BlockPos localValue1) {
      if (internalField0149.player != null && internalField0149.world != null) {
         int localValue2 = localValue1.getY();

         for (int localValue3 = 20; localValue3 >= -20; localValue3--) {
            BlockPos localValue4 = new BlockPos(localValue1.getX(), localValue2 + localValue3, localValue1.getZ());
            if (this.internalMethod05673(localValue4)) {
               int localValue5 = 0;
               boolean localValue6 = false;

               for (int localValue7 = 0; localValue7 < 24; localValue7++) {
                  BlockPos localValue8 = localValue4.down(localValue7);
                  BlockState localValue9 = internalField0149.world.getBlockState(localValue8);
                  if (localValue9.isAir() && internalField0149.world.getFluidState(localValue8).isEmpty()) {
                     int localValue10 = 0;

                     for (Direction localValue12 : Type.HORIZONTAL) {
                        BlockPos localValue13 = localValue8.offset(localValue12);
                        BlockState localValue14 = internalField0149.world.getBlockState(localValue13);
                        if (!localValue14.isAir() && !localValue14.getCollisionShape(internalField0149.world, localValue13).isEmpty()) {
                           localValue10++;
                        }
                     }

                     if (localValue10 < 4) {
                        if (localValue6) {
                           break;
                        }
                     } else {
                        if (!localValue6) {
                           localValue6 = true;
                        }

                        localValue5++;
                     }
                  } else if (localValue6) {
                     break;
                  }
               }

               if (localValue5 >= 5) {
                  BlockPos localValue15 = localValue4.down(localValue5);
                  BlockState localValue16 = internalField0149.world.getBlockState(localValue15);
                  if (!localValue16.isAir() && !localValue16.getCollisionShape(internalField0149.world, localValue15).isEmpty() && this.internalMethod01755(localValue4, localValue5)) {
                     boolean localValue17 = this.internalMethod02684(localValue4, 6);
                     return new TrapEspModule.InternalType0101(localValue4, localValue5, localValue17);
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean internalMethod05673(BlockPos localValue1) {
      if (internalField0149.world == null) {
         return false;
      } else {
         int localValue2 = 0;

         for (int localValue3 = -1; localValue3 <= 1; localValue3++) {
            for (int localValue4 = -1; localValue4 <= 1; localValue4++) {
               if (localValue3 != 0 || localValue4 != 0) {
                  for (int localValue5 = 0; localValue5 <= 3; localValue5++) {
                     BlockPos localValue6 = localValue1.add(localValue3, localValue5, localValue4);
                     BlockState localValue7 = internalField0149.world.getBlockState(localValue6);
                     if (!localValue7.isAir() && !localValue7.getCollisionShape(internalField0149.world, localValue6).isEmpty()) {
                        localValue2++;
                        break;
                     }
                  }
               }
            }
         }

         return localValue2 >= 4;
      }
   }

   private boolean internalMethod01755(BlockPos localValue1, int localValue2) {
      if (internalField0149.world == null) {
         return false;
      } else {
         int localValue3 = 0;
         int localValue4 = 0;

         for (int localValue5 = -2; localValue5 <= 2; localValue5++) {
            for (int localValue6 = -2; localValue6 <= 2; localValue6++) {
               if (Math.abs(localValue5) > 1 || Math.abs(localValue6) > 1) {
                  localValue4++;
                  BlockPos localValue7 = localValue1.add(localValue5, 0, localValue6);
                  BlockState localValue8 = internalField0149.world.getBlockState(localValue7);
                  if (!localValue8.isAir() && !localValue8.getCollisionShape(internalField0149.world, localValue7).isEmpty()) {
                     Block localValue9 = localValue8.getBlock();
                     if (localValue9.getDefaultState().isOpaque()) {
                        localValue3++;
                     }
                  }
               }
            }
         }

         return localValue3 >= localValue4 * 0.6;
      }
   }

   private boolean internalMethod02684(BlockPos localValue1, int localValue2) {
      if (internalField0149.player != null && internalField0149.world != null) {
         BlockPos localValue3 = localValue1.add(-localValue2, -localValue2, -localValue2);
         BlockPos localValue4 = localValue1.add(localValue2, localValue2, localValue2);

         for (BlockPos localValue6 : BlockPos.iterate(localValue3, localValue4)) {
            BlockState localValue7 = internalField0149.world.getBlockState(localValue6);
            if (!localValue7.isAir()) {
               Block localValue8 = localValue7.getBlock();
               if (localValue8 instanceof TntBlock
                  || localValue8 instanceof PressurePlateBlock
                  || localValue8 instanceof TripwireBlock
                  || localValue8 instanceof TripwireHookBlock
                  || localValue8 instanceof HopperBlock
                  || localValue8 instanceof ChestBlock) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static long internalMethod01185(int localValue0, int localValue1) {
      return (long)localValue0 << 32 ^ localValue1 & 4294967295L;
   }

   static class InternalType0101 {
      BlockPos internalField0352;
      int internalField0227;
      boolean internalField0277;

      @Generated
      public InternalType0101(BlockPos localValue1, int localValue2, boolean localValue3) {
         this.internalField0352 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0277 = localValue3;
      }
   }
}
