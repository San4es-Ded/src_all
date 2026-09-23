package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class PotionFarmTask extends AutoFarmModeState {
   private final ModeSetting internalField0668;
   private final ModeSetting.InternalType0088 internalField0237;
   private final ModeSetting.InternalType0088 internalField0238;
   private final ModeSetting.InternalType0088 internalField1066;
   private final ModeSetting.InternalType0088 internalField1067;
   private final ModeSetting.InternalType0088 internalField1068;
   private final ModeSetting.InternalType0088 internalField1065;
   private final ModeSetting.InternalType0088 internalField1480;
   private final ModeSetting internalField0669;
   private final ModeSetting.InternalType0088 internalField1481;
   private final ModeSetting.InternalType0088 internalField1483;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final Stopwatch internalField1189 = new Stopwatch();
   private PotionFarmTask.InternalType0370 internalField0755;
   private BlockPos internalField0352;
   private BlockPos internalField0351;
   private final List<BlockPos> internalField0416;
   private final Map<BlockPos, Long> internalField0543;
   private final Map<Item, BlockPos> internalField0544;
   private final Set<BlockPos> internalField0546;
   private Item internalField0152;
   private Rotation internalField0118;
   private int internalField1055;
   private final EventListener<ClientPlayerTickEvent> internalField0157;
   private static final ColorRGBA internalField0777 = new ColorRGBA(99.0F, 196.0F, 255.0F);
   private static final ColorRGBA internalField0776 = new ColorRGBA(255.0F, 196.0F, 64.0F);
   private final EventListener<Render3DEvent> internalField0158;

   public PotionFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.potion");
      this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
      this.internalField0416 = new ArrayList<>();
      this.internalField0543 = new HashMap<>();
      this.internalField0544 = new HashMap<>();
      this.internalField0546 = new HashSet<>();
      this.internalField0157 = localValue1x -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            switch (this.internalField0755) {
               case internalField0755:
                  this.internalMethod08446();
                  break;
               case internalField0756:
                  this.internalMethod09773();
                  break;
               case internalField1300:
                  this.internalMethod09774();
                  break;
               case internalField1301:
                  this.internalMethod02978(true);
                  break;
               case internalField1298:
                  this.internalMethod09782();
                  break;
               case internalField1299:
                  this.internalMethod02978(false);
                  break;
               case internalField1607:
                  this.internalMethod09783();
                  break;
               case internalField1608:
                  this.internalMethod09791();
            }
         }
      };
      this.internalField0158 = localValue1x -> {
         if (this.internalField1261.internalMethod04496()) {
            if (internalField0149.world != null && internalField0149.player != null) {
               BlockPos localValue2x = this.internalField0755 != PotionFarmTask.InternalType0370.internalField0756
                     && this.internalField0755 != PotionFarmTask.InternalType0370.internalField1300
                  ? null
                  : this.internalField0352;
               BlockPos localValue3 = this.internalField0755 != PotionFarmTask.InternalType0370.internalField1301
                     && this.internalField0755 != PotionFarmTask.InternalType0370.internalField1298
                     && this.internalField0755 != PotionFarmTask.InternalType0370.internalField1299
                     && this.internalField0755 != PotionFarmTask.InternalType0370.internalField1607
                  ? null
                  : this.internalField0351;
               if (localValue2x != null || localValue3 != null) {
                  MatrixStack localValue4 = localValue1x.getMatrices();
                  Camera localValue5 = internalField0149.gameRenderer.getCamera();
                  Vec3d localValue6 = localValue5.getCameraPos();
                  RenderSystem.enableBlend();
                  RenderSystem.disableDepthTest();
                  RenderSystem.disableCull();
                  RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
                  RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
                  BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                  if (localValue2x != null) {
                     Render3DUtils.internalMethod02535(
                        localValue4, localValue7, this.internalMethod07020(localValue2x).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0777.withAlpha(45.0F)
                     );
                  }

                  if (localValue3 != null) {
                     Render3DUtils.internalMethod02535(
                        localValue4, localValue7, this.internalMethod07020(localValue3).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0776.withAlpha(45.0F)
                     );
                  }

                  HudRenderUtils.internalMethod05816(localValue7);
                  BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
                  if (localValue2x != null) {
                     Render3DUtils.internalMethod08795(
                        localValue4, localValue8, this.internalMethod07020(localValue2x).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0777.withAlpha(180.0F)
                     );
                  }

                  if (localValue3 != null) {
                     Render3DUtils.internalMethod08795(
                        localValue4, localValue8, this.internalMethod07020(localValue3).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0776.withAlpha(180.0F)
                     );
                  }

                  HudRenderUtils.internalMethod05816(localValue8);
                  RenderSystem.defaultBlendFunc();
                  RenderSystem.enableCull();
                  RenderSystem.enableDepthTest();
                  RenderSystem.disableBlend();
               }
            }
         }
      };
      this.internalField0668 = new ModeSetting(localValue1, "modules.settings.potion_farm.brew", () -> !this.isSelected());
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.strength").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.speed");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.fire_resistance");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.invisibility");
      this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.regen");
      this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.healing");
      this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_farm.potion.strong_healing");
      this.internalField0669 = new ModeSetting(localValue1, "modules.settings.potion_farm.stack_mode", () -> !this.isSelected());
      this.internalField1481 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.potion_farm.stack_mode.multiple").select();
      this.internalField1483 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.potion_farm.stack_mode.single");
      this.internalField0650 = new BooleanSetting(
         localValue1, "modules.settings.potion_farm.enhance", () -> !this.isSelected() || !this.internalField1067.isSelected()
      );
      this.internalField0651 = new BooleanSetting(localValue1, "modules.settings.potion_farm.use_chests", () -> !this.isSelected()).internalMethod06630();
      this.internalField1261 = new BooleanSetting(localValue1, "modules.settings.potion_farm.target_esp", () -> !this.isSelected()).internalMethod06630();
      this.internalField0383 = new SliderSetting(
            localValue1, "modules.settings.potion_farm.delay", "modules.settings.potion_farm.delay.description", () -> !this.isSelected()
         )
         .internalMethod08673(10.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(1500.0F)
         .internalMethod08074(250.0F);
      this.internalField0382 = new SliderSetting(
            localValue1,
            "modules.settings.potion_farm.single_stack_delay",
            "modules.settings.potion_farm.single_stack_delay.description",
            () -> !this.isSelected() || !this.internalField1483.isSelected()
         )
         .internalMethod08673(10.0F)
         .internalMethod05900(50.0F)
         .internalMethod02732(2000.0F)
         .internalMethod08074(250.0F);
   }

   @Override
   public void internalMethod04694() {
      this.internalMethod08288();
   }

   @Override
   public void internalMethod04697() {
      this.internalMethod08288();
      if (internalField0149.player != null && internalField0149.currentScreen instanceof BrewingStandScreen) {
         internalField0149.player.closeHandledScreen();
      }
   }

   private void internalMethod08288() {
      this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
      this.internalField0352 = null;
      this.internalField0351 = null;
      this.internalField0416.clear();
      this.internalField0546.clear();
      this.internalField0152 = null;
      this.internalField0519.internalMethod00701();
      this.internalField0118 = null;
      this.internalField1055 = 0;
      this.internalField1189.internalMethod00701();
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      return switch (this.internalField0755) {
         case internalField0755, internalField1608 -> AutoFarmPhase.internalField0848;
         case internalField0756, internalField1300 -> AutoFarmPhase.internalField0847;
         case internalField1301, internalField1298 -> AutoFarmPhase.internalField1639;
         case internalField1299, internalField1607 -> AutoFarmPhase.internalField1638;
      };
   }

   @Override
   public AutoFarmUnit internalMethod02317() {
      return AutoFarmUnit.internalField0852;
   }

   @Override
   public ItemStack internalMethod04126() {
      return new ItemStack(Items.POTION);
   }

   private void internalMethod08446() {
      if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
         if (this.internalField0416.isEmpty()) {
            this.internalField0416.addAll(this.internalMethod00440());
            if (this.internalField0416.isEmpty()) {
               return;
            }
         }

         long localValue1 = System.currentTimeMillis();
         BlockPos localValue3 = null;

         for (BlockPos localValue5 : this.internalField0416) {
            Long localValue6 = this.internalField0543.get(localValue5);
            if (localValue6 == null || localValue6 <= localValue1) {
               localValue3 = localValue5;
               break;
            }
         }

         if (localValue3 == null) {
            if (this.internalField0519.internalMethod02365(2000L)) {
               this.internalField0416.clear();
               this.internalField0519.internalMethod00701();
            }
         } else {
            this.internalField0416.remove(localValue3);
            this.internalField0352 = localValue3;
            this.internalMethod09794();
            this.internalField0755 = PotionFarmTask.InternalType0370.internalField0756;
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod09773() {
      if (this.internalField0352 == null) {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
      } else if (internalField0149.currentScreen instanceof BrewingStandScreen) {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1300;
         this.internalField0519.internalMethod00701();
      } else if (!this.internalMethod03174(this.internalField0352)) {
         this.internalField0543.put(this.internalField0352, System.currentTimeMillis() + 5000L);
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
      } else if (this.internalMethod07632(Vec3d.ofCenter(this.internalField0352))) {
         if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
            this.internalMethod03173(this.internalField0352);
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod09774() {
      if (!(internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler localValue1)) {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
      } else if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576() / 2L)) {
         if (localValue1.getBrewTime() > 0) {
            long localValue8 = (long)(localValue1.getBrewTime() / 20.0 * 1000.0);
            this.internalField0543.put(this.internalField0352, System.currentTimeMillis() + localValue8 + 250L);
            this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
            this.internalField0519.internalMethod00701();
         } else {
            Potion localValue7 = this.internalMethod03438();

            for (int localValue3 = 0; localValue3 < 3; localValue3++) {
               ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
               if (!localValue4.isEmpty() && this.internalMethod01264(localValue4, localValue7)) {
                  InventoryUtils.internalMethod03592(localValue3);
                  this.internalMethod00320().internalMethod04318(Math.max(1, localValue4.getCount()));
                  this.internalField0519.internalMethod00701();
                  return;
               }
            }

            if (localValue1.getFuel() == 0 && localValue1.getSlot(4).getStack().isEmpty()) {
               if (!this.internalMethod00516(Items.BLAZE_POWDER, 4)) {
                  this.internalMethod07152(Items.BLAZE_POWDER);
               }
            } else {
               if (this.internalField1483.isSelected()) {
                  int[] localValue9 = new int[3];
                  int localValue12 = 0;

                  for (int localValue5 = 0; localValue5 < 3; localValue5++) {
                     if (localValue1.getSlot(localValue5).getStack().isEmpty()) {
                        localValue9[localValue12++] = localValue5;
                     }
                  }

                  if (localValue12 > 0) {
                     if (!this.internalField1189.internalMethod02365((long)this.internalField0382.internalMethod08576())) {
                        return;
                     }

                     int localValue14 = this.internalMethod07394(localValue1);
                     if (localValue14 == -1) {
                        this.internalMethod07152(Items.POTION);
                        return;
                     }

                     int[] localValue6 = new int[localValue12];
                     System.arraycopy(localValue9, 0, localValue6, 0, localValue12);
                     this.internalMethod06218(localValue14, localValue6);
                     this.internalField1189.internalMethod00701();
                     this.internalField0519.internalMethod00701();
                     return;
                  }
               } else {
                  for (int localValue10 = 0; localValue10 < 3; localValue10++) {
                     if (localValue1.getSlot(localValue10).getStack().isEmpty()) {
                        int localValue13 = this.internalMethod07394(localValue1);
                        if (localValue13 == -1) {
                           this.internalMethod07152(Items.POTION);
                           return;
                        }

                        InventoryUtils.internalMethod03592(localValue13);
                        this.internalField0519.internalMethod00701();
                        return;
                     }
                  }
               }

               if (localValue1.getSlot(3).getStack().isEmpty()) {
                  Item localValue11 = this.internalMethod03668(localValue1);
                  if (localValue11 == null) {
                     this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
                     this.internalField0519.internalMethod00701();
                     return;
                  }

                  if (!this.internalMethod00516(localValue11, 3)) {
                     this.internalMethod07152(localValue11);
                     return;
                  }

                  this.internalField0543.put(this.internalField0352, System.currentTimeMillis() + 20000L + 500L);
                  this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
                  this.internalField0519.internalMethod00701();
               }
            }
         }
      }
   }

   private void internalMethod02978(boolean localValue1) {
      if (internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler) {
         internalField0149.player.closeHandledScreen();
         this.internalField0519.internalMethod00701();
      } else {
         if (this.internalField0351 == null) {
            this.internalField0351 = localValue1 ? this.internalMethod02861() : this.internalMethod07299();
            this.internalMethod09794();
            if (this.internalField0351 == null) {
               RockstarClient.getInstance()
                  .internalMethod02503()
                  .internalMethod00599(
                     NotificationType.internalField0705,
                     localValue1 ? LanguageManager.internalMethod07214("potion_farm.no_chest_title") : LanguageManager.internalMethod07214("potion_farm.no_item_title"),
                     localValue1
                        ? LanguageManager.internalMethod07214("potion_farm.no_chest")
                        : LanguageManager.internalMethod00160("potion_farm.no_item", this.internalMethod00231(this.internalField0152))
                  );
               if (!localValue1) {
                  this.internalField0152 = null;
               }

               this.internalField0546.clear();
               this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
               this.internalField0519.internalMethod00701();
               return;
            }
         }

         if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            this.internalField0755 = localValue1 ? PotionFarmTask.InternalType0370.internalField1298 : PotionFarmTask.InternalType0370.internalField1607;
            this.internalField0519.internalMethod00701();
         } else if (!this.internalMethod03174(this.internalField0351)) {
            if (localValue1) {
               this.internalField0351 = null;
               this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
            } else {
               this.internalMethod01902(this.internalField0351);
               this.internalField0351 = null;
               this.internalMethod09794();
            }
         } else if (this.internalMethod07632(Vec3d.ofCenter(this.internalField0351))) {
            if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
               this.internalMethod03173(this.internalField0351);
               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09782() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
            DefaultedList localValue6 = localValue1.slots;

            for (int localValue3 = 0; localValue3 < localValue6.size(); localValue3++) {
               Slot localValue4 = (Slot)localValue6.get(localValue3);
               if (localValue4.inventory == internalField0149.player.getInventory()) {
                  ItemStack localValue5 = localValue4.getStack();
                  if (!localValue5.isEmpty() && this.internalMethod04524(localValue5)) {
                     InventoryUtils.internalMethod03592(localValue3);
                     this.internalField0519.internalMethod00701();
                     return;
                  }
               }
            }

            this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
            this.internalField0519.internalMethod00701();
         }
      } else {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
      }
   }

   private void internalMethod07152(Item localValue1) {
      if (!this.internalField0651.internalMethod04496()) {
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod00599(
               NotificationType.internalField0705,
               LanguageManager.internalMethod07214("potion_farm.no_item_title"),
               LanguageManager.internalMethod00160("potion_farm.no_item", this.internalMethod00231(localValue1))
            );
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
      } else {
         this.internalField0152 = localValue1;
         this.internalField0546.clear();
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1299;
         this.internalField0519.internalMethod00701();
      }
   }

   private String internalMethod00231(Item localValue1) {
      if (localValue1 == null) {
         return "";
      } else {
         return localValue1 == Items.POTION ? LanguageManager.internalMethod07214("potion_farm.item.water_bottle") : localValue1.getName().getString();
      }
   }

   private void internalMethod09783() {
      if (!(internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1)) {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
      } else if (this.internalField0152 == null) {
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
      } else if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
         DefaultedList localValue6 = localValue1.slots;

         for (int localValue3 = 0; localValue3 < localValue6.size(); localValue3++) {
            Slot localValue4 = (Slot)localValue6.get(localValue3);
            if (localValue4.inventory != internalField0149.player.getInventory()) {
               ItemStack localValue5 = localValue4.getStack();
               if (!localValue5.isEmpty() && this.internalMethod01034(localValue5, this.internalField0152)) {
                  InventoryUtils.internalMethod03592(localValue3);
                  if (this.internalField0351 != null) {
                     this.internalField0544.put(this.internalField0152, this.internalField0351);
                  }

                  this.internalField0152 = null;
                  this.internalField0546.clear();
                  this.internalField0519.internalMethod00701();
                  this.internalField0755 = PotionFarmTask.InternalType0370.internalField1608;
                  return;
               }
            }
         }

         if (this.internalField0351 != null) {
            this.internalMethod01902(this.internalField0351);
            BlockPos localValue7 = this.internalField0544.get(this.internalField0152);
            if (this.internalField0351.equals(localValue7) || this.internalField0351.equals(this.internalMethod03355(localValue7))) {
               this.internalField0544.remove(this.internalField0152);
            }
         }

         internalField0149.player.closeHandledScreen();
         this.internalField0351 = null;
         this.internalMethod09794();
         this.internalField0755 = PotionFarmTask.InternalType0370.internalField1299;
         this.internalField0519.internalMethod00701();
      }
   }

   private BlockPos internalMethod07299() {
      if (internalField0149.player != null && internalField0149.world != null && this.internalField0152 != null) {
         BlockPos localValue1 = this.internalField0544.get(this.internalField0152);
         if (localValue1 != null && !this.internalMethod01903(localValue1) && internalField0149.world.getBlockEntity(localValue1) instanceof ChestBlockEntity) {
            return localValue1;
         } else {
            BlockPos localValue2 = BlockPos.ofFloored(internalField0149.player.getEntityPos());
            BlockPos localValue3 = null;
            double localValue4 = Double.MAX_VALUE;

            for (BlockPos localValue7 : BlockPos.iterateOutwards(localValue2, 8, 8, 8)) {
               if (internalField0149.world.getBlockEntity(localValue7) instanceof ChestBlockEntity) {
                  BlockPos localValue8 = localValue7.toImmutable();
                  if (!this.internalMethod01903(localValue8)) {
                     double localValue9 = localValue7.getSquaredDistance(internalField0149.player.getEntityPos());
                     if (localValue9 < localValue4) {
                        localValue4 = localValue9;
                        localValue3 = localValue8;
                     }
                  }
               }
            }

            return localValue3;
         }
      } else {
         return null;
      }
   }

   private void internalMethod01902(BlockPos localValue1) {
      this.internalField0546.add(localValue1);
      BlockPos localValue2 = this.internalMethod03355(localValue1);
      if (localValue2 != null) {
         this.internalField0546.add(localValue2);
      }
   }

   private boolean internalMethod01903(BlockPos localValue1) {
      if (this.internalField0546.contains(localValue1)) {
         return true;
      } else {
         BlockPos localValue2 = this.internalMethod03355(localValue1);
         return localValue2 != null && this.internalField0546.contains(localValue2);
      }
   }

   private BlockPos internalMethod03355(BlockPos localValue1) {
      if (localValue1 != null && internalField0149.world != null) {
         BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
         if (!(localValue2.getBlock() instanceof ChestBlock)) {
            return null;
         } else {
            ChestType localValue3 = (ChestType)localValue2.get(ChestBlock.CHEST_TYPE);
            if (localValue3 == ChestType.SINGLE) {
               return null;
            } else {
               Direction localValue4 = (Direction)localValue2.get(ChestBlock.FACING);
               Direction localValue5 = localValue3 == ChestType.LEFT ? localValue4.rotateYClockwise() : localValue4.rotateYCounterclockwise();
               return localValue1.offset(localValue5);
            }
         }
      } else {
         return null;
      }
   }

   private boolean internalMethod01034(ItemStack localValue1, Item localValue2) {
      if (localValue2 != Items.POTION) {
         return localValue1.getItem() == localValue2;
      } else {
         PotionContentsComponent localValue3 = (PotionContentsComponent)localValue1.get(DataComponentTypes.POTION_CONTENTS);
         return localValue1.getItem() == Items.POTION
            && localValue3 != null
            && localValue3.potion().isPresent()
            && ((RegistryEntry)localValue3.potion().get()).value() == Potions.WATER.value();
      }
   }

   private void internalMethod09791() {
      if (internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
         if (this.internalField0651.internalMethod04496() && this.internalMethod00874()) {
            this.internalField0351 = null;
            this.internalMethod09794();
            this.internalField0755 = PotionFarmTask.InternalType0370.internalField1301;
            this.internalField0519.internalMethod00701();
         } else {
            this.internalField0352 = null;
            this.internalField0351 = null;
            this.internalMethod09794();
            this.internalField0755 = PotionFarmTask.InternalType0370.internalField0755;
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod09794() {
      this.internalField0118 = null;
      this.internalField1055 = 0;
   }

   private boolean internalMethod07632(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1055 = 0;
      }

      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      Rotation localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      if (localValue3 != null && localValue3.internalMethod00735(localValue2) <= 1.5F) {
         this.internalField1055++;
         return this.internalField1055 >= 2;
      } else {
         return false;
      }
   }

   private boolean internalMethod00874() {
      for (int localValue1 = 0; localValue1 < internalField0149.player.getInventory().size(); localValue1++) {
         ItemStack localValue2 = internalField0149.player.getInventory().getStack(localValue1);
         if (this.internalMethod04524(localValue2)) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod04524(ItemStack localValue1) {
      if (localValue1.getItem() != Items.POTION) {
         return false;
      } else {
         PotionContentsComponent localValue2 = (PotionContentsComponent)localValue1.get(DataComponentTypes.POTION_CONTENTS);
         return localValue2 != null && !localValue2.potion().isEmpty() ? ((RegistryEntry)localValue2.potion().get()).value() == this.internalMethod03438() : false;
      }
   }

   private Potion internalMethod03438() {
      if (this.internalField0237.isSelected()) {
         return (Potion)Potions.STRONG_STRENGTH.value();
      } else if (this.internalField0238.isSelected()) {
         return (Potion)Potions.STRONG_SWIFTNESS.value();
      } else if (this.internalField1066.isSelected()) {
         return (Potion)Potions.LONG_FIRE_RESISTANCE.value();
      } else if (this.internalField1068.isSelected()) {
         return (Potion)Potions.STRONG_REGENERATION.value();
      } else if (this.internalField1065.isSelected()) {
         return (Potion)Potions.HEALING.value();
      } else if (this.internalField1480.isSelected()) {
         return (Potion)Potions.STRONG_HEALING.value();
      } else {
         return this.internalField1067.isSelected() && this.internalField0650.internalMethod04496()
            ? (Potion)Potions.LONG_INVISIBILITY.value()
            : (Potion)Potions.INVISIBILITY.value();
      }
   }

   private Item internalMethod03668(BrewingStandScreenHandler localValue1) {
      if (this.internalMethod00486(localValue1, (Potion)Potions.WATER.value())) {
         return Items.NETHER_WART;
      } else {
         if (this.internalMethod00486(localValue1, (Potion)Potions.AWKWARD.value())) {
            if (this.internalField0237.isSelected()) {
               return Items.BLAZE_POWDER;
            }

            if (this.internalField0238.isSelected()) {
               return Items.SUGAR;
            }

            if (this.internalField1066.isSelected()) {
               return Items.MAGMA_CREAM;
            }

            if (this.internalField1067.isSelected()) {
               return Items.GOLDEN_CARROT;
            }

            if (this.internalField1068.isSelected()) {
               return Items.GHAST_TEAR;
            }

            if (this.internalField1065.isSelected() || this.internalField1480.isSelected()) {
               return Items.GLISTERING_MELON_SLICE;
            }
         }

         if (this.internalMethod00486(localValue1, (Potion)Potions.NIGHT_VISION.value()) && this.internalField1067.isSelected()) {
            return Items.FERMENTED_SPIDER_EYE;
         } else if (this.internalMethod00486(localValue1, (Potion)Potions.STRENGTH.value()) || this.internalMethod00486(localValue1, (Potion)Potions.SWIFTNESS.value())) {
            return Items.GLOWSTONE_DUST;
         } else if (this.internalMethod00486(localValue1, (Potion)Potions.FIRE_RESISTANCE.value())) {
            return Items.REDSTONE;
         } else if (this.internalField1067.isSelected()
            && this.internalField0650.internalMethod04496()
            && this.internalMethod00486(localValue1, (Potion)Potions.INVISIBILITY.value())) {
            return Items.REDSTONE;
         } else if (this.internalField1480.isSelected() && this.internalMethod00486(localValue1, (Potion)Potions.HEALING.value())) {
            return Items.GLOWSTONE_DUST;
         } else {
            return this.internalField1068.isSelected() && this.internalMethod00486(localValue1, (Potion)Potions.REGENERATION.value()) ? Items.GLOWSTONE_DUST : null;
         }
      }
   }

   private boolean internalMethod00486(BrewingStandScreenHandler localValue1, Potion localValue2) {
      boolean localValue3 = false;

      for (int localValue4 = 0; localValue4 < 3; localValue4++) {
         ItemStack localValue5 = localValue1.getSlot(localValue4).getStack();
         if (!localValue5.isEmpty()) {
            if (localValue5.getItem() != Items.POTION) {
               return false;
            }

            PotionContentsComponent localValue6 = (PotionContentsComponent)localValue5.get(DataComponentTypes.POTION_CONTENTS);
            if (localValue6 == null || localValue6.potion().isEmpty()) {
               return false;
            }

            if (((RegistryEntry)localValue6.potion().get()).value() != localValue2) {
               return false;
            }

            localValue3 = true;
         }
      }

      return localValue3;
   }

   private boolean internalMethod01264(ItemStack localValue1, Potion localValue2) {
      if (localValue1.getItem() != Items.POTION) {
         return false;
      } else {
         PotionContentsComponent localValue3 = (PotionContentsComponent)localValue1.get(DataComponentTypes.POTION_CONTENTS);
         return localValue3 != null && !localValue3.potion().isEmpty() ? ((RegistryEntry)localValue3.potion().get()).value() == localValue2 : false;
      }
   }

   private boolean internalMethod00516(Item localValue1, int localValue2) {
      if (!this.internalField0518.internalMethod02365((long)(this.internalField0383.internalMethod08576() * 1.2))) {
         return true;
      } else {
         int localValue3 = this.internalMethod07151(localValue1);
         if (localValue3 == -1) {
            return false;
         } else {
            if (this.internalField1483.isSelected()) {
               this.internalMethod06218(localValue3, localValue2);
            } else {
               InventoryUtils.internalMethod08185(localValue3, localValue2);
            }

            this.internalField0518.internalMethod00701();
            return true;
         }
      }
   }

   private void internalMethod06218(int localValue1, int... localValue2) {
      int localValue3 = internalField0149.player.currentScreenHandler.syncId;
      internalField0149.interactionManager.clickSlot(localValue3, localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
      internalField0149.interactionManager.clickSlot(localValue3, -999, 4, SlotActionType.QUICK_CRAFT, internalField0149.player);

      for (int localValue7 : localValue2) {
         internalField0149.interactionManager.clickSlot(localValue3, localValue7, 5, SlotActionType.QUICK_CRAFT, internalField0149.player);
      }

      internalField0149.interactionManager.clickSlot(localValue3, -999, 6, SlotActionType.QUICK_CRAFT, internalField0149.player);
      internalField0149.interactionManager.clickSlot(localValue3, localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
   }

   private int internalMethod07151(Item localValue1) {
      for (int localValue2 = 5; localValue2 < 41; localValue2++) {
         if (((Slot)internalField0149.player.currentScreenHandler.slots.get(localValue2)).getStack().getItem() == localValue1) {
            return localValue2;
         }
      }

      return -1;
   }

   private int internalMethod07394(BrewingStandScreenHandler localValue1) {
      for (int localValue2 = 5; localValue2 < 41; localValue2++) {
         ItemStack localValue3 = ((Slot)localValue1.slots.get(localValue2)).getStack();
         if (localValue3.getItem() == Items.POTION) {
            PotionContentsComponent localValue4 = (PotionContentsComponent)localValue3.get(DataComponentTypes.POTION_CONTENTS);
            if (localValue4 != null && !localValue4.potion().isEmpty() && ((RegistryEntry)localValue4.potion().get()).value() == Potions.WATER.value()) {
               return localValue2;
            }
         }
      }

      return -1;
   }

   private List<BlockPos> internalMethod00440() {
      ArrayList localValue1 = new ArrayList();
      BlockPos localValue2 = BlockPos.ofFloored(internalField0149.player.getEntityPos());

      for (BlockPos localValue4 : BlockPos.iterateOutwards(localValue2, 5, 5, 5)) {
         if (internalField0149.world.getBlockEntity(localValue4) instanceof BrewingStandBlockEntity) {
            localValue1.add(localValue4.toImmutable());
         }
      }

      localValue1.sort(Comparator.comparingDouble(localValue0 -> ((net.minecraft.util.math.BlockPos)localValue0).getSquaredDistance(internalField0149.player.getEntityPos())));
      return localValue1;
   }

   private BlockPos internalMethod02861() {
      BlockPos localValue1 = BlockPos.ofFloored(internalField0149.player.getEntityPos());
      BlockPos localValue2 = null;
      double localValue3 = Double.MAX_VALUE;

      for (BlockPos localValue6 : BlockPos.iterateOutwards(localValue1, 8, 8, 8)) {
         if (internalField0149.world.getBlockEntity(localValue6) instanceof ChestBlockEntity) {
            double localValue7 = localValue6.getSquaredDistance(internalField0149.player.getEntityPos());
            if (localValue7 < localValue3) {
               localValue3 = localValue7;
               localValue2 = localValue6.toImmutable();
            }
         }
      }

      return localValue2;
   }

   private boolean internalMethod03174(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange();
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private void internalMethod03173(BlockPos localValue1) {
      Vec3d localValue2 = Vec3d.ofCenter(localValue1);
      BlockHitResult localValue3 = new BlockHitResult(localValue2, Direction.UP, localValue1, false);
      internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue3);
      internalField0149.player.swingHand(Hand.MAIN_HAND);
   }

   private Box internalMethod07020(BlockPos localValue1) {
      return new Box(localValue1).contract(0.02);
   }

   static enum InternalType0370 {
      internalField0755,
      internalField0756,
      internalField1300,
      internalField1301,
      internalField1298,
      internalField1299,
      internalField1607,
      internalField1608;
   }
}
