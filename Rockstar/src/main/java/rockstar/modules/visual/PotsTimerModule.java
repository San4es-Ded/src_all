package rockstar.modules.visual;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.client.gui.screen.ingame.BlastFurnaceScreen;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.SmokerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Pots Timer",
   category = ModuleCategory.VISUALS,
   internalMethod09633 = "modules.descriptions.pots_timer"
)
public class PotsTimerModule extends Module {
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private final Map<BlockPos, PotsTimerModule.InternalType0436> internalField0543 = new ConcurrentHashMap<>();
   private final LinkedHashSet<BlockPos> internalField0500 = new LinkedHashSet<>();
   private PotsTimerModule.InternalType0332 internalField0475;
   private BlockPos internalField0352;
   private long internalField0229;
   private long internalField0230;
   private int internalField0227;
   private final Stopwatch internalField0519;
   private final EventListener<WorldChangeEvent> internalField0157;
   private final EventListener<ClientPlayerTickEvent> internalField0158;
   private final EventListener<PreHudRenderEvent> internalField1028;

   public PotsTimerModule() {
      this.internalField0475 = PotsTimerModule.InternalType0332.internalField0475;
      this.internalField0227 = -1;
      this.internalField0519 = new Stopwatch();
      this.internalField0157 = localValue1 -> {
         this.internalMethod09758();
         this.internalField0543.clear();
         this.internalField0500.clear();
      };
      this.internalField0158 = localValue1 -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            if (this.internalField0519.internalMethod02365(800L)) {
               this.internalMethod09601();
               this.internalField0519.internalMethod00701();
            }

            long localValue2 = System.currentTimeMillis();
            long localValue4 = 20000L;

            for (Entry localValue7 : this.internalField0543.entrySet()) {
               PotsTimerModule.InternalType0436 localValue8 = (PotsTimerModule.InternalType0436)localValue7.getValue();
               localValue8.internalMethod00881(localValue2);
               if (localValue2 - localValue8.internalField0229 > localValue4) {
                  this.internalField0500.add((BlockPos)localValue7.getKey());
               }
            }

            this.internalMethod09755();
         }
      };
      this.internalField1028 = localValue1 -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            CustomDrawContext localValue2 = localValue1.getContext();

            for (Entry localValue4 : this.internalField0543.entrySet()) {
               BlockPos localValue5 = (BlockPos)localValue4.getKey();
               PotsTimerModule.InternalType0436 localValue6 = (PotsTimerModule.InternalType0436)localValue4.getValue();
               this.internalMethod03461(localValue2, localValue5, localValue6);
            }
         }
      };
      this.internalMethod09599();
   }

   private void internalMethod09599() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.pots_timer.furnaces").internalMethod06630();
      this.internalField0651 = new BooleanSetting(this, "modules.settings.pots_timer.blast_furnaces").internalMethod06630();
      this.internalField1261 = new BooleanSetting(this, "modules.settings.pots_timer.smokers").internalMethod06630();
      this.internalField1263 = new BooleanSetting(this, "modules.settings.pots_timer.brewing").internalMethod06630();
      this.internalField0383 = new SliderSetting(this, "modules.settings.pots_timer.range")
         .internalMethod05900(4.0F)
         .internalMethod02732(32.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(16.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.pots_timer.scale")
         .internalMethod05900(0.3F)
         .internalMethod02732(1.3F)
         .internalMethod08673(0.05F)
         .internalMethod08074(0.4F);
   }

   @Override
   public final void onDisable() {
      super.onDisable();
      this.internalMethod09758();
      this.internalField0543.clear();
      this.internalField0500.clear();
   }

   private void internalMethod09601() {
      int localValue1 = (int)Math.ceil(this.internalField0383.internalMethod08576());
      BlockPos localValue2 = internalField0149.player.getBlockPos();
      double localValue3 = this.internalField0383.internalMethod08576() * this.internalField0383.internalMethod08576();
      ArrayList localValue5 = new ArrayList();

      for (int localValue6 = -localValue1; localValue6 <= localValue1; localValue6++) {
         for (int localValue7 = -localValue1; localValue7 <= localValue1; localValue7++) {
            for (int localValue8 = -localValue1; localValue8 <= localValue1; localValue8++) {
               BlockPos localValue9 = localValue2.add(localValue6, localValue7, localValue8);
               if (!(localValue2.getSquaredDistance(localValue9) > localValue3)) {
                  BlockEntity localValue10 = internalField0149.world.getBlockEntity(localValue9);
                  if (this.internalMethod03151(localValue10)) {
                     localValue5.add(localValue9);
                     if (!this.internalField0543.containsKey(localValue9)) {
                        PotsTimerModule.InternalType0436 localValue11 = new PotsTimerModule.InternalType0436(this.internalMethod06002(localValue10));
                        this.internalField0543.put(localValue9, localValue11);
                        this.internalField0500.add(localValue9);
                     }
                  }
               }
            }
         }
      }

      this.internalField0543.keySet().removeIf(localValue2x -> {
         if (localValue5.contains(localValue2x)) {
            return false;
         } else {
            this.internalField0500.remove(localValue2x);
            return true;
         }
      });
   }

   private boolean internalMethod03151(BlockEntity localValue1) {
      if (localValue1 == null) {
         return false;
      } else if (localValue1 instanceof FurnaceBlockEntity) {
         return this.internalField0650.internalMethod04496();
      } else if (localValue1 instanceof BlastFurnaceBlockEntity) {
         return this.internalField0651.internalMethod04496();
      } else if (localValue1 instanceof SmokerBlockEntity) {
         return this.internalField1261.internalMethod04496();
      } else {
         return localValue1 instanceof BrewingStandBlockEntity ? this.internalField1263.internalMethod04496() : false;
      }
   }

   private PotsTimerModule.InternalType0437 internalMethod06002(BlockEntity localValue1) {
      if (localValue1 instanceof BlastFurnaceBlockEntity) {
         return PotsTimerModule.InternalType0437.internalField0891;
      } else if (localValue1 instanceof SmokerBlockEntity) {
         return PotsTimerModule.InternalType0437.internalField1366;
      } else {
         return localValue1 instanceof BrewingStandBlockEntity
            ? PotsTimerModule.InternalType0437.internalField1367
            : PotsTimerModule.InternalType0437.internalField0892;
      }
   }

   private void internalMethod09755() {
      long localValue1 = System.currentTimeMillis();
      int localValue3 = -1;
      switch (this.internalField0475) {
         case internalField0475:
            if (internalField0149.currentScreen != null) {
               return;
            }

            if (internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
               return;
            }

            if (this.internalField0500.isEmpty()) {
               return;
            }

            BlockPos localValue11 = this.internalField0500.getFirst();
            this.internalField0500.remove(localValue11);
            PotsTimerModule.InternalType0436 localValue12 = this.internalField0543.get(localValue11);
            if (localValue12 == null) {
               return;
            }

            if (!this.internalMethod00313(localValue11)) {
               return;
            }
            Class<? extends net.minecraft.client.gui.screen.Screen> localValue6 = switch (localValue12.internalField0892) {
               case internalField0892 -> FurnaceScreen.class;
               case internalField0891 -> BlastFurnaceScreen.class;
               case internalField1366 -> SmokerScreen.class;
               case internalField1367 -> BrewingStandScreen.class;
            };
            DeferredScreenTracker.internalMethod05914(localValue11, localValue6);
            this.internalField0352 = localValue11;
            this.internalField0229 = localValue1;
            this.internalField0230 = 0L;
            this.internalField0227 = -1;
            byte localValue10 = -1;
            Vec3d localValue7 = Vec3d.ofCenter(localValue11);
            BlockHitResult localValue8 = new BlockHitResult(localValue7, Direction.UP, localValue11, false);
            internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue8);
            this.internalField0475 = PotsTimerModule.InternalType0332.internalField0476;
            break;
         case internalField0476:
            if (localValue1 - this.internalField0229 > 1500L) {
               this.internalMethod08864(false);
               return;
            }

            if (this.internalMethod09600()) {
               this.internalField0227 = this.internalMethod07940();
               this.internalField0230 = localValue1;
               this.internalField0475 = PotsTimerModule.InternalType0332.internalField1169;
            }
            break;
         case internalField1169:
            if (localValue1 - this.internalField0229 > 1500L) {
               this.internalMethod08864(this.internalField0227 != -1);
               return;
            }

            if (!this.internalMethod09600()) {
               this.internalMethod08864(false);
               return;
            }

            if (localValue1 - this.internalField0230 < 220L) {
               return;
            }

            localValue3 = this.internalMethod07940();
            ItemStack localValue4 = this.internalMethod00239();
            PotsTimerModule.InternalType0436 localValue5 = this.internalField0543.get(this.internalField0352);
            if (localValue5 != null) {
               localValue5.internalMethod03478(this.internalField0227, localValue3, localValue4, localValue1);
            }

            this.internalField0475 = PotsTimerModule.InternalType0332.internalField1170;
            break;
         case internalField1170:
            if (internalField0149.player.networkHandler != null) {
               internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
               internalField0149.player.currentScreenHandler = internalField0149.player.playerScreenHandler;
            }

            DeferredScreenTracker.internalMethod03946();
            this.internalField0475 = PotsTimerModule.InternalType0332.internalField0475;
            this.internalField0352 = null;
      }
   }

   private boolean internalMethod09600() {
      if (internalField0149.player != null && internalField0149.player.currentScreenHandler != null) {
         PotsTimerModule.InternalType0436 localValue1 = this.internalField0543.get(this.internalField0352);
         if (localValue1 == null) {
            return false;
         } else {
            return switch (localValue1.internalField0892) {
               case internalField0892, internalField0891, internalField1366 -> internalField0149.player.currentScreenHandler instanceof AbstractFurnaceScreenHandler;
               case internalField1367 -> internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler;
            };
         }
      } else {
         return false;
      }
   }

   private int internalMethod07940() {
      PotsTimerModule.InternalType0436 localValue1 = this.internalField0543.get(this.internalField0352);
      if (localValue1 == null) {
         return -1;
      } else {
         return switch (localValue1.internalField0892) {
            case internalField0892, internalField0891, internalField1366 -> internalField0149.player.currentScreenHandler instanceof AbstractFurnaceScreenHandler localValue4
               ? Math.round(localValue4.getCookProgress() * localValue1.internalField0227)
               : -1;
            case internalField1367 -> internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler localValue2 ? localValue2.getBrewTime() : -1;
         };
      }
   }

   private ItemStack internalMethod00239() {
      PotsTimerModule.InternalType0436 localValue1 = this.internalField0543.get(this.internalField0352);
      if (localValue1 == null) {
         return ItemStack.EMPTY;
      } else {
         try {
            return switch (localValue1.internalField0892) {
               case internalField0892, internalField0891, internalField1366 -> {
                  if (internalField0149.player.currentScreenHandler instanceof AbstractFurnaceScreenHandler localValue5) {
                     ItemStack localValue8 = localValue5.getSlot(0).getStack();
                     yield !localValue8.isEmpty() ? localValue8.copy() : localValue5.getSlot(2).getStack().copy();
                  } else {
                     yield ItemStack.EMPTY;
                  }
               }
               case internalField1367 -> {
                  if (internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler localValue2) {
                     ItemStack localValue6 = localValue2.getSlot(3).getStack();
                     yield !localValue6.isEmpty() ? localValue6.copy() : localValue2.getSlot(0).getStack().copy();
                  } else {
                     yield ItemStack.EMPTY;
                  }
               }
            };
         } catch (Exception localValue4) {
            return ItemStack.EMPTY;
         }
      }
   }

   private void internalMethod08864(boolean localValue1) {
      if (this.internalField0475 != PotsTimerModule.InternalType0332.internalField0475) {
         if (internalField0149.player != null && internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
            if (internalField0149.player.networkHandler != null) {
               internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
            }

            internalField0149.player.currentScreenHandler = internalField0149.player.playerScreenHandler;
         }

         if (!localValue1 && this.internalField0352 != null) {
            PotsTimerModule.InternalType0436 localValue2 = this.internalField0543.get(this.internalField0352);
            if (localValue2 != null) {
               localValue2.internalField0229 = System.currentTimeMillis();
               localValue2.internalField0277 = true;
            }
         }

         DeferredScreenTracker.internalMethod03946();
         this.internalField0475 = PotsTimerModule.InternalType0332.internalField0475;
         this.internalField0352 = null;
      }
   }

   private void internalMethod09758() {
      if (this.internalField0475 != PotsTimerModule.InternalType0332.internalField0475) {
         this.internalMethod08864(false);
      }

      this.internalField0475 = PotsTimerModule.InternalType0332.internalField0475;
      this.internalField0352 = null;
   }

   private boolean internalMethod00313(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange();
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private void internalMethod03461(CustomDrawContext localValue1, BlockPos localValue2, PotsTimerModule.InternalType0436 localValue3) {
      boolean localValue4 = localValue3.internalMethod03788();
      if (localValue4 && !localValue3.internalField0277) {
         Vec3d localValue5 = Vec3d.ofCenter(localValue2).add(0.0, 1.1, 0.0);
         Vec2f localValue6 = ScreenProjectionUtils.internalMethod00612(localValue5);
         if (localValue6 != null) {
            float localValue7 = (float)internalField0149.player.getEntityPos().distanceTo(Vec3d.ofCenter(localValue2));
            float localValue8 = MathHelper.clamp(1.0F - localValue7 / 24.0F, 0.45F, 1.0F) * this.internalField0382.internalMethod08576();
            float localValue9 = localValue3.internalMethod03786();
            long localValue10 = localValue3.internalMethod03787();
            long localValue12 = localValue10 / 60L;
            long localValue14 = localValue10 % 60L;
            String localValue16 = String.format("%02d:%02d", localValue12, localValue14);
            float localValue17 = 110.0F;
            float localValue18 = 110.0F;
            org.joml.Matrix3x2fStack localValue19 = localValue1.getMatrices();
            localValue19.pushMatrix();
            localValue19.translate(localValue6.x - localValue17 / 2.0F, localValue6.y - localValue18 / 2.0F);
            HudRenderUtils.internalMethod08976(localValue19, localValue17 / 2.0F, localValue18 / 2.0F, localValue8);
            ColorRGBA localValue20 = ThemeColors.internalMethod02531();
            ColorRGBA localValue21 = new ColorRGBA(9.0F, 9.0F, 11.0F).mulAlpha(0.55F);
            localValue1.drawBlurredRect(
               0.0F, 0.0F, localValue17, localValue18, 35.0F, 5.0F, CornerRadii.internalMethod03908(22.0F), ThemeColors.internalField1312.mulAlpha(0.35F)
            );
            localValue1.drawSquircle(0.0F, 0.0F, localValue17, localValue18, 5.0F, CornerRadii.internalMethod03908(22.0F), localValue21);
            localValue1.drawCircleProgress(localValue17 / 2.0F, localValue18 / 2.0F, 40.0F, 5.0F, 1.0F, new ColorRGBA(255.0F, 255.0F, 255.0F).mulAlpha(0.12F));
            localValue1.drawCircleProgress(localValue17 / 2.0F, localValue18 / 2.0F, 40.0F, 5.0F, MathHelper.clamp(localValue9, 0.0F, 1.0F), localValue20);
            ItemStack localValue22 = localValue3.internalField0878;
            if (localValue22 != null && !localValue22.isEmpty()) {
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               localValue1.drawItem(localValue22, localValue17 / 2.0F - 8.0F, localValue18 / 2.0F - 12.0F, 1.0F);
            } else {
               Item localValue23 = localValue3.internalField0892.internalMethod00505();
               localValue1.drawItem(localValue23, localValue17 / 2.0F - 8.0F, localValue18 / 2.0F - 12.0F, 1.0F);
            }

            localValue1.drawCenteredText(
               Fonts.internalField1156.internalMethod01432(13.0F), localValue16, localValue17 / 2.0F, localValue18 / 2.0F + 10.0F, ThemeColors.internalMethod08459()
            );
            HudRenderUtils.internalMethod00012(localValue19);
            localValue19.popMatrix();
         }
      }
   }

   static enum InternalType0332 {
      internalField0475,
      internalField0476,
      internalField1169,
      internalField1170;
   }

   static final class InternalType0436 {
      final PotsTimerModule.InternalType0437 internalField0892;
      final int internalField0227;
      ItemStack internalField0878 = ItemStack.EMPTY;
      long internalField0229;
      long internalField0230;
      float internalField0205;
      float internalField0206;
      boolean internalField0277 = true;

      InternalType0436(PotsTimerModule.InternalType0437 localValue1) {
         this.internalField0892 = localValue1;
         this.internalField0227 = localValue1.internalField0227;
      }

      void internalMethod03478(int localValue1, int localValue2, ItemStack localValue3, long localValue4) {
         int localValue6 = localValue2 >= 0 ? localValue2 : localValue1;
         if (localValue6 < 0) {
            localValue6 = 0;
         }

         this.internalField0205 = localValue6;
         if (localValue1 >= 0 && localValue2 >= 0) {
            int localValue7 = localValue2 - localValue1;
            if (this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367) {
               this.internalField0206 = localValue7 < 0 ? -1.0F : 0.0F;
            } else {
               this.internalField0206 = localValue7 > 0 ? 1.0F : 0.0F;
            }

            this.internalField0277 = this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367 ? localValue2 == 0 || localValue7 >= 0 : localValue6 == 0 || localValue7 <= 0;
         } else {
            this.internalField0206 = this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367 ? -1.0F : 1.0F;
            this.internalField0277 = localValue6 == 0;
         }

         if (localValue3 != null && !localValue3.isEmpty()) {
            this.internalField0878 = localValue3;
         }

         this.internalField0229 = localValue4;
         this.internalField0230 = localValue4;
      }

      void internalMethod00881(long localValue1) {
         if (!this.internalField0277 && this.internalField0206 != 0.0F) {
            float localValue3 = (float)(localValue1 - this.internalField0230) / 50.0F;
            this.internalField0205 = this.internalField0205 + this.internalField0206 * localValue3;
            this.internalField0205 = MathHelper.clamp(this.internalField0205, 0.0F, this.internalField0227);
            if (this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367 && this.internalField0205 <= 0.0F) {
               this.internalField0277 = true;
            } else if (this.internalField0892 != PotsTimerModule.InternalType0437.internalField1367 && this.internalField0205 >= this.internalField0227) {
               this.internalField0205 = 0.0F;
            }

            this.internalField0230 = localValue1;
         } else {
            this.internalField0230 = localValue1;
         }
      }

      float internalMethod03786() {
         if (this.internalField0227 <= 0) {
            return 0.0F;
         } else {
            return this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367
               ? 1.0F - this.internalField0205 / this.internalField0227
               : this.internalField0205 / this.internalField0227;
         }
      }

      long internalMethod03787() {
         float localValue1;
         if (this.internalField0892 == PotsTimerModule.InternalType0437.internalField1367) {
            localValue1 = this.internalField0205;
         } else {
            localValue1 = this.internalField0227 - this.internalField0205;
         }

         return Math.max(0L, (long)Math.round(localValue1 / 20.0F));
      }

      boolean internalMethod03788() {
         return !this.internalField0277 && this.internalField0206 != 0.0F;
      }
   }

   static enum InternalType0437 {
      internalField0892(200, Items.FURNACE),
      internalField0891(100, Items.BLAST_FURNACE),
      internalField1366(100, Items.SMOKER),
      internalField1367(400, Items.BREWING_STAND);

      final int internalField0227;
      final Item internalField0152;

      private InternalType0437(int localValue3, Item localValue4) {
         this.internalField0227 = localValue3;
         this.internalField0152 = localValue4;
      }

      Item internalMethod00505() {
         return this.internalField0152;
      }
   }
}
