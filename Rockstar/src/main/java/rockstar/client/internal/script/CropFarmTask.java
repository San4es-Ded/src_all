package rockstar.client.internal.script;











import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class CropFarmTask extends AutoFarmModeState {
   private final ModeSetting internalField0668;
   private final ModeSetting.InternalType0088 internalField0237;
   private final ModeSetting.InternalType0088 internalField0238;
   private final ModeSetting.InternalType0088 internalField1066;
   private final ModeSetting.InternalType0088 internalField1067;
   private final ModeSetting.InternalType0088 internalField1068;
   private final ModeSetting.InternalType0088 internalField1065;
   private final SliderSetting internalField0383;
   private final SliderSetting internalField0382;
   private final BooleanSetting internalField0650;
   private final BooleanSetting internalField0651;
   private final BooleanSetting internalField1261;
   private final BooleanSetting internalField1263;
   private BooleanSetting internalField1262;
   private final SliderSetting internalField1142;
   private static final float internalField0205 = 1.5F;
   private static final int internalField0227 = 3;
   private static final float internalField0206 = 0.05F;
   private final List<BlockPos> internalField0416 = new ArrayList<>();
   private final List<BlockPos> internalField0417 = new ArrayList<>();
   private final Map<BlockPos, Integer> internalField0543 = new HashMap<>();
   private final List<ItemEntity> internalField1145 = new ArrayList<>();
   private final Set<BlockPos> internalField0546 = new HashSet<>();
   private BlockPos internalField0352;
   private ItemEntity internalField0831;
   private BlockPos internalField0351;
   private int internalField0228 = -1;
   private int internalField1053 = -1;
   private CropFarmTask.InternalType0361 internalField0485;
   private boolean internalField0277;
   private long internalField0229;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private Rotation internalField0118;
   private int internalField1055;
   private final EventListener<ClientPlayerTickEvent> internalField0157;
   private static final ColorRGBA internalField0777 = new ColorRGBA(120.0F, 220.0F, 96.0F);
   private static final ColorRGBA internalField0776 = new ColorRGBA(255.0F, 196.0F, 64.0F);
   private final EventListener<Render3DEvent> internalField0158;

   public CropFarmTask(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.crop");
      this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField0157 = localValue1x -> {
         if (internalField0149.player != null && internalField0149.world != null) {
            switch (this.internalField0485) {
               case internalField0485:
                  this.internalMethod09349();
                  break;
               case internalField0484:
                  this.internalMethod09350();
                  break;
               case internalField1175:
                  this.internalMethod09360();
                  break;
               case internalField1178:
                  this.internalMethod09361();
                  break;
               case internalField1177:
                  this.internalMethod09558();
                  break;
               case internalField1176:
                  this.internalMethod09945();
                  break;
               case internalField1548:
                  this.internalMethod09946();
                  break;
               case internalField1549:
                  this.internalMethod10007();
                  break;
               case internalField1547:
                  this.internalMethod08991();
            }
         }
      };
      this.internalField0158 = localValue1x -> {
         if (this.internalField1262.internalMethod04496()) {
            if (internalField0149.world != null && internalField0149.player != null) {
               BlockPos localValue2x = this.internalField0485 != CropFarmTask.InternalType0361.internalField0484
                     && this.internalField0485 != CropFarmTask.InternalType0361.internalField1175
                     && this.internalField0485 != CropFarmTask.InternalType0361.internalField1178
                  ? null
                  : this.internalField0352;
               BlockPos localValue3 = this.internalField0485 != CropFarmTask.InternalType0361.internalField1176
                     && this.internalField0485 != CropFarmTask.InternalType0361.internalField1548
                     && this.internalField0485 != CropFarmTask.InternalType0361.internalField1549
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
                        localValue4, localValue7, this.internalMethod01718(localValue2x).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0777.withAlpha(45.0F)
                     );
                  }

                  if (localValue3 != null) {
                     Render3DUtils.internalMethod02535(
                        localValue4, localValue7, this.internalMethod01718(localValue3).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0776.withAlpha(45.0F)
                     );
                  }

                  HudRenderUtils.internalMethod05816(localValue7);
                  BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
                  if (localValue2x != null) {
                     Render3DUtils.internalMethod08795(
                        localValue4, localValue8, this.internalMethod01718(localValue2x).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0777.withAlpha(180.0F)
                     );
                  }

                  if (localValue3 != null) {
                     Render3DUtils.internalMethod08795(
                        localValue4, localValue8, this.internalMethod01718(localValue3).offset(-localValue6.x, -localValue6.y, -localValue6.z), internalField0776.withAlpha(180.0F)
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
      this.internalField0668 = new ModeSetting(localValue1, "modules.settings.crop_farm.crop", () -> !this.isSelected());
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.nether_wart").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.wheat");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.carrots");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.potatoes");
      this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.beetroots");
      this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.crop_farm.crop.sugar_cane");
      this.internalField0383 = new SliderSetting(localValue1, "modules.settings.crop_farm.scan_radius", () -> !this.isSelected())
         .internalMethod08673(2.0F)
         .internalMethod05900(8.0F)
         .internalMethod02732(64.0F)
         .internalMethod08074(24.0F);
      this.internalField0382 = new SliderSetting(localValue1, "modules.settings.crop_farm.vertical_range", () -> !this.isSelected())
         .internalMethod08673(1.0F)
         .internalMethod05900(1.0F)
         .internalMethod02732(8.0F)
         .internalMethod08074(3.0F);
      this.internalField0650 = new BooleanSetting(localValue1, "modules.settings.crop_farm.replant", () -> !this.isSelected()).internalMethod06630();
      this.internalField0651 = new BooleanSetting(localValue1, "modules.settings.crop_farm.use_hoe", () -> !this.isSelected()).internalMethod06630();
      this.internalField1261 = new BooleanSetting(localValue1, "modules.settings.crop_farm.pickup", () -> !this.isSelected()).internalMethod06630();
      this.internalField1263 = new BooleanSetting(localValue1, "modules.settings.crop_farm.auto_deposit", () -> !this.isSelected()).internalMethod06630();
      this.internalField1262 = new BooleanSetting(localValue1, "modules.settings.crop_farm.target_esp", () -> !this.isSelected()).internalMethod06630();
      this.internalField1142 = new SliderSetting(localValue1, "modules.settings.crop_farm.action_delay", () -> !this.isSelected())
         .internalMethod08673(10.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(500.0F)
         .internalMethod08074(80.0F)
         .internalMethod06240("ms");
   }

   @Override
   public void internalMethod04694() {
      if (internalField0149.player == null || internalField0149.world == null) {
         this.internalMethod08387();
      } else if (!NewtonApiHolder.internalMethod05192()) {
         this.internalMethod05961("modules.crop_farm.newton_missing");
         this.internalMethod08387();
      } else {
         this.internalMethod08977();
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      }
   }

   @Override
   public void internalMethod04697() {
      this.internalMethod10117();
      this.internalMethod08977();
      this.internalField0485 = CropFarmTask.InternalType0361.internalField1547;
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      return switch (this.internalField0485) {
         case internalField0485, internalField1547 -> AutoFarmPhase.internalField0848;
         case internalField0484, internalField1176 -> AutoFarmPhase.internalField1350;
         case internalField1175 -> AutoFarmPhase.internalField0847;
         case internalField1178 -> AutoFarmPhase.internalField1348;
         case internalField1177 -> AutoFarmPhase.internalField1349;
         case internalField1548, internalField1549 -> AutoFarmPhase.internalField1639;
      };
   }

   @Override
   public ItemStack internalMethod04126() {
      Item localValue1 = this.internalMethod03340();
      return new ItemStack(localValue1 == null ? Items.WHEAT : localValue1);
   }

   private void internalMethod08977() {
      this.internalField0416.clear();
      this.internalField0417.clear();
      this.internalField0543.clear();
      this.internalField1145.clear();
      this.internalField0546.clear();
      this.internalField0352 = null;
      this.internalField0831 = null;
      this.internalField0351 = null;
      this.internalField0228 = -1;
      this.internalField1053 = -1;
      this.internalField0277 = false;
      this.internalMethod09943();
   }

   private void internalMethod08991() {
      if (this.internalField0518.internalMethod02365(1500L)) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      }
   }

   private void internalMethod09349() {
      this.internalField0416.clear();
      this.internalField0417.clear();
      this.internalField0543.clear();
      this.internalMethod10119();
      if (this.internalField0416.isEmpty() && this.internalField0417.isEmpty()) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1547;
         this.internalField0518.internalMethod00701();
      } else {
         this.internalMethod10120();
         this.internalMethod09549();
      }
   }

   private void internalMethod09350() {
      if (this.internalField0352 == null || !this.internalMethod04802(this.internalField0352)) {
         this.internalMethod10117();
         this.internalMethod09549();
      } else if (this.internalMethod08023(this.internalField0352)) {
         this.internalMethod10117();
         this.internalField0485 = this.internalMethod07832(this.internalField0352)
            ? CropFarmTask.InternalType0361.internalField1175
            : CropFarmTask.InternalType0361.internalField1178;
         this.internalMethod09943();
         this.internalField0519.internalMethod00701();
      } else if (!this.internalField0277) {
         int localValue1 = this.internalField0543.getOrDefault(this.internalField0352, 0);
         if (localValue1 >= 3) {
            this.internalMethod09549();
         } else {
            BlockPos localValue2 = this.internalMethod00657(this.internalField0352);
            if (localValue2 == null) {
               this.internalField0543.put(this.internalField0352, 3);
               this.internalMethod09549();
            } else {
               NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(localValue2, 1));
               this.internalField0543.put(this.internalField0352, localValue1 + 1);
               this.internalField0277 = true;
               this.internalField0229 = System.currentTimeMillis();
            }
         }
      } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         this.internalField0277 = false;
      } else {
         if (System.currentTimeMillis() - this.internalField0229 > 20000L) {
            this.internalMethod10117();
            this.internalMethod09549();
         }
      }
   }

   private void internalMethod09360() {
      if (this.internalField0352 == null) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      } else if (!this.internalMethod07832(this.internalField0352)) {
         BlockState localValue3 = internalField0149.world.getBlockState(this.internalField0352);
         if (localValue3.isAir() && this.internalField0650.internalMethod04496() && this.internalMethod07726(this.internalField0352)) {
            this.internalField0485 = CropFarmTask.InternalType0361.internalField1178;
            this.internalMethod09943();
            this.internalField0519.internalMethod00701();
         } else {
            this.internalMethod09549();
         }
      } else if (!this.internalMethod08023(this.internalField0352)) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0484;
         this.internalField0277 = false;
      } else {
         if (this.internalField0651.internalMethod04496()) {
            this.internalMethod05426(internalField0149.world.getBlockState(this.internalField0352));
         }

         Direction localValue1 = this.internalMethod07179(this.internalField0352);
         Vec3d localValue2 = this.internalMethod03259(this.internalField0352, localValue1);
         if (this.internalMethod02862(localValue2)) {
            if (this.internalField0519.internalMethod02365((long)this.internalField1142.internalMethod08576())) {
               internalField0149.interactionManager.attackBlock(this.internalField0352, localValue1);
               internalField0149.interactionManager.updateBlockBreakingProgress(this.internalField0352, localValue1);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09361() {
      if (this.internalField0352 == null) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      } else {
         BlockState localValue1 = internalField0149.world.getBlockState(this.internalField0352);
         if (!localValue1.isAir()) {
            this.internalMethod09549();
         } else if (!this.internalMethod07726(this.internalField0352)) {
            this.internalMethod09549();
         } else {
            Item localValue2 = this.internalMethod03340();
            if (localValue2 == null || !this.internalMethod04401(localValue2)) {
               this.internalMethod09549();
            } else if (!this.internalMethod08023(this.internalField0352)) {
               this.internalField0485 = CropFarmTask.InternalType0361.internalField0484;
               this.internalField0277 = false;
            } else {
               BlockPos localValue3 = this.internalField0352.down();
               Vec3d localValue4 = new Vec3d(localValue3.getX() + 0.5, localValue3.getY() + 1.0, localValue3.getZ() + 0.5);
               if (this.internalMethod02862(localValue4)) {
                  if (this.internalField0519.internalMethod02365((long)this.internalField1142.internalMethod08576())) {
                     BlockHitResult localValue5 = new BlockHitResult(localValue4, Direction.UP, localValue3, false);
                     internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue5);
                     internalField0149.player.swingHand(Hand.MAIN_HAND);
                     this.internalField0519.internalMethod00701();
                     this.internalMethod09549();
                  }
               }
            }
         }
      }
   }

   private void internalMethod09549() {
      this.internalField0543.remove(this.internalField0352);
      this.internalField0352 = null;
      if (this.internalMethod07354()) {
         this.internalMethod09944();
      } else {
         Vec3d localValue1 = internalField0149.player.getEntityPos();
         int localValue2 = -1;
         boolean localValue3 = false;
         double localValue4 = Double.MAX_VALUE;

         for (int localValue6 = 0; localValue6 < this.internalField0416.size(); localValue6++) {
            BlockPos localValue7 = this.internalField0416.get(localValue6);
            if (this.internalMethod07832(localValue7)) {
               double localValue8 = Vec3d.ofCenter(localValue7).squaredDistanceTo(localValue1);
               if (localValue8 < localValue4) {
                  localValue4 = localValue8;
                  localValue2 = localValue6;
                  localValue3 = false;
               }
            }
         }

         for (int localValue10 = 0; localValue10 < this.internalField0417.size(); localValue10++) {
            BlockPos localValue11 = this.internalField0417.get(localValue10);
            if (this.internalMethod03334(localValue11)) {
               double localValue12 = Vec3d.ofCenter(localValue11).squaredDistanceTo(localValue1);
               if (localValue12 < localValue4) {
                  localValue4 = localValue12;
                  localValue2 = localValue10;
                  localValue3 = true;
               }
            }
         }

         if (localValue2 >= 0) {
            this.internalField0352 = localValue3 ? this.internalField0417.remove(localValue2) : this.internalField0416.remove(localValue2);
         } else {
            this.internalField0416.clear();
            this.internalField0417.clear();
         }

         if (this.internalField0352 == null) {
            this.internalMethod09550();
         } else {
            this.internalField0485 = CropFarmTask.InternalType0361.internalField0484;
            this.internalField0277 = false;
            this.internalMethod09943();
         }
      }
   }

   private boolean internalMethod03334(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return localValue2.isAir() && this.internalMethod07726(localValue1);
   }

   private boolean internalMethod04802(BlockPos localValue1) {
      return this.internalMethod07832(localValue1) || this.internalMethod03334(localValue1);
   }

   private void internalMethod09550() {
      this.internalField0277 = false;
      this.internalMethod09943();
      if (!this.internalField1261.internalMethod04496()) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1547;
         this.internalField0518.internalMethod00701();
      } else {
         this.internalMethod10116();
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1177;
         this.internalMethod09557();
      }
   }

   private void internalMethod09557() {
      this.internalField0831 = null;
      this.internalField0277 = false;
      if (this.internalMethod07354()) {
         this.internalMethod09944();
      } else {
         while (!this.internalField1145.isEmpty()) {
            ItemEntity localValue1 = this.internalField1145.removeFirst();
            if (localValue1 != null && !localValue1.isRemoved() && localValue1.isAlive()) {
               this.internalField0831 = localValue1;
               return;
            }
         }

         this.internalField0485 = CropFarmTask.InternalType0361.internalField1547;
         this.internalField0518.internalMethod00701();
      }
   }

   private void internalMethod09558() {
      if (this.internalField0831 == null) {
         this.internalMethod09557();
      } else if (!this.internalField0831.isRemoved() && this.internalField0831.isAlive()) {
         Vec3d localValue1 = this.internalField0831.getEntityPos();
         if (internalField0149.player.getEntityPos().squaredDistanceTo(localValue1) <= 1.9599999999999997) {
            this.internalMethod10117();
            this.internalMethod09557();
         } else if (!this.internalField0277) {
            NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(BlockPos.ofFloored(localValue1), 1));
            this.internalField0277 = true;
            this.internalField0229 = System.currentTimeMillis();
         } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
            this.internalField0277 = false;
         } else {
            if (System.currentTimeMillis() - this.internalField0229 > 10000L) {
               this.internalMethod10117();
               this.internalMethod09557();
            }
         }
      } else {
         this.internalMethod10117();
         this.internalMethod09557();
      }
   }

   private void internalMethod10116() {
      this.internalField1145.clear();
      if (internalField0149.world != null && internalField0149.player != null) {
         Vec3d localValue1 = internalField0149.player.getEntityPos();
         double localValue2 = this.internalField0383.internalMethod08576() + 4.0F;
         double localValue4 = localValue2 * localValue2;
         ArrayList localValue6 = new ArrayList();

         for (Entity localValue8 : internalField0149.world.getEntities()) {
            if (localValue8 instanceof ItemEntity localValue9
               && !localValue9.isRemoved()
               && localValue9.isAlive()
               && !(localValue1.squaredDistanceTo(localValue9.getEntityPos()) > localValue4)
               && this.internalMethod06941(localValue9.getStack())) {
               localValue6.add(localValue9);
            }
         }

         localValue6.sort(Comparator.comparingDouble(localValue1x -> localValue1.squaredDistanceTo(((net.minecraft.entity.Entity)localValue1x).getEntityPos())));
         this.internalField1145.addAll(localValue6);
      }
   }

   private boolean internalMethod06941(ItemStack localValue1) {
      Item localValue2 = localValue1.getItem();
      if (this.internalField0237.isSelected()) {
         return localValue2 == Items.NETHER_WART;
      } else if (this.internalField0238.isSelected()) {
         return localValue2 == Items.WHEAT || localValue2 == Items.WHEAT_SEEDS;
      } else if (this.internalField1066.isSelected()) {
         return localValue2 == Items.CARROT;
      } else if (this.internalField1067.isSelected()) {
         return localValue2 == Items.POTATO || localValue2 == Items.POISONOUS_POTATO;
      } else if (!this.internalField1068.isSelected()) {
         return this.internalField1065.isSelected() ? localValue2 == Items.SUGAR_CANE : false;
      } else {
         return localValue2 == Items.BEETROOT || localValue2 == Items.BEETROOT_SEEDS;
      }
   }

   private void internalMethod10117() {
      if (NewtonApiHolder.internalMethod05192() && NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         NewtonApiHolder.internalMethod01856().internalMethod00136();
      }

      this.internalField0277 = false;
   }

   private void internalMethod10119() {
      BlockPos localValue1 = internalField0149.player.getBlockPos();
      int localValue2 = (int)this.internalField0383.internalMethod08576();
      int localValue3 = (int)this.internalField0382.internalMethod08576();
      Predicate localValue4 = this.internalMethod05492();
      boolean localValue5 = this.internalField0650.internalMethod04496() && !this.internalField1065.isSelected();

      for (int localValue6 = -localValue3; localValue6 <= localValue3; localValue6++) {
         for (int localValue7 = -localValue2; localValue7 <= localValue2; localValue7++) {
            for (int localValue8 = -localValue2; localValue8 <= localValue2; localValue8++) {
               BlockPos localValue9 = localValue1.add(localValue7, localValue6, localValue8);
               BlockState localValue10 = internalField0149.world.getBlockState(localValue9);
               if (localValue4.test(localValue10) && this.internalMethod00334(localValue9, localValue10)) {
                  this.internalField0416.add(localValue9.toImmutable());
               } else if (localValue5 && localValue10.isAir() && this.internalMethod07726(localValue9)) {
                  this.internalField0417.add(localValue9.toImmutable());
               }
            }
         }
      }
   }

   private void internalMethod10120() {
      if (!this.internalField0416.isEmpty()) {
         int localValue1 = this.internalMethod05637(this.internalField0416, true);
         int localValue2 = this.internalMethod05637(this.internalField0416, false);
         boolean localValue3 = localValue1 >= localValue2;
         Comparator localValue4 = localValue3 ? Comparator.comparingInt(Vec3i::getZ) : Comparator.comparingInt(Vec3i::getX);
         Comparator localValue5 = localValue3 ? Comparator.comparingInt(Vec3i::getX) : Comparator.comparingInt(Vec3i::getZ);
         this.internalField0416.sort(localValue4.thenComparingInt(value -> ((Vec3i)value).getY()).thenComparing(localValue5));
         ArrayList localValue6 = new ArrayList(this.internalField0416.size());
         int localValue7 = 0;
         int localValue8 = 0;

         while (localValue7 < this.internalField0416.size()) {
            int localValue9 = localValue3 ? this.internalField0416.get(localValue7).getZ() : this.internalField0416.get(localValue7).getX();
            int localValue10 = this.internalField0416.get(localValue7).getY();

            int localValue11;
            for (localValue11 = localValue7; localValue11 < this.internalField0416.size(); localValue11++) {
               BlockPos localValue12 = this.internalField0416.get(localValue11);
               int localValue13 = localValue3 ? localValue12.getZ() : localValue12.getX();
               if (localValue13 != localValue9 || localValue12.getY() != localValue10) {
                  break;
               }
            }

            ArrayList localValue14 = new ArrayList<>(this.internalField0416.subList(localValue7, localValue11));
            if ((localValue8 & 1) == 1) {
               Collections.reverse(localValue14);
            }

            localValue6.addAll(localValue14);
            localValue8++;
            localValue7 = localValue11;
         }

         this.internalField0416.clear();
         this.internalField0416.addAll(localValue6);
      }
   }

   private int internalMethod05637(List<BlockPos> localValue1, boolean localValue2) {
      HashMap localValue3 = new HashMap();

      for (BlockPos localValue5 : localValue1) {
         int localValue6 = localValue2 ? localValue5.getZ() : localValue5.getX();
         localValue3.merge(localValue6, 1, (left, right) -> (Integer)left + (Integer)right);
      }

      int localValue7 = 0;

      for (int localValue9 : (Iterable<Integer>)(Iterable<?>)localValue3.values()) {
         if (localValue9 > localValue7) {
            localValue7 = localValue9;
         }
      }

      return localValue7;
   }

   private BlockPos internalMethod00657(BlockPos localValue1) {
      Direction[] localValue2 = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
      BlockPos localValue3 = null;
      Vec3d localValue4 = internalField0149.player.getEntityPos();
      double localValue5 = Double.MAX_VALUE;

      for (Direction localValue10 : localValue2) {
         BlockPos localValue11 = localValue1.offset(localValue10);
         if (this.internalMethod09109(localValue11)) {
            double localValue12 = Vec3d.ofCenter(localValue11).squaredDistanceTo(localValue4);
            if (localValue12 < localValue5) {
               localValue5 = localValue12;
               localValue3 = localValue11;
            }
         }
      }

      return localValue3;
   }

   private boolean internalMethod09109(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1.down());
      BlockState localValue3 = internalField0149.world.getBlockState(localValue1);
      BlockState localValue4 = internalField0149.world.getBlockState(localValue1.up());
      if (localValue2.isAir()) {
         return false;
      } else {
         return !localValue3.getCollisionShape(internalField0149.world, localValue1).isEmpty() ? false : localValue4.getCollisionShape(internalField0149.world, localValue1.up()).isEmpty();
      }
   }

   private Predicate<BlockState> internalMethod05492() {
      if (this.internalField0237.isSelected()) {
         return localValue0 -> localValue0.getBlock() == Blocks.NETHER_WART;
      } else if (this.internalField0238.isSelected()) {
         return localValue0 -> localValue0.getBlock() == Blocks.WHEAT;
      } else if (this.internalField1066.isSelected()) {
         return localValue0 -> localValue0.getBlock() == Blocks.CARROTS;
      } else if (this.internalField1067.isSelected()) {
         return localValue0 -> localValue0.getBlock() == Blocks.POTATOES;
      } else if (this.internalField1068.isSelected()) {
         return localValue0 -> localValue0.getBlock() == Blocks.BEETROOTS;
      } else {
         return this.internalField1065.isSelected() ? localValue0 -> localValue0.getBlock() == Blocks.SUGAR_CANE : localValue0 -> false;
      }
   }

   private boolean internalMethod07832(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return this.internalMethod05492().test(localValue2) && this.internalMethod00334(localValue1, localValue2);
   }

   private boolean internalMethod00334(BlockPos localValue1, BlockState localValue2) {
      Block localValue3 = localValue2.getBlock();
      if (localValue3 instanceof CropBlock localValue4) {
         return localValue4.isMature(localValue2);
      } else if (localValue3 == Blocks.NETHER_WART) {
         return (Integer)localValue2.get(NetherWartBlock.AGE) >= 3;
      } else {
         return localValue3 == Blocks.SUGAR_CANE ? internalField0149.world.getBlockState(localValue1.down()).getBlock() == Blocks.SUGAR_CANE : false;
      }
   }

   private boolean internalMethod07726(BlockPos localValue1) {
      if (this.internalField1065.isSelected()) {
         return false;
      } else {
         Block localValue2 = internalField0149.world.getBlockState(localValue1.down()).getBlock();
         return this.internalField0237.isSelected() ? localValue2 == Blocks.SOUL_SAND : localValue2 == Blocks.FARMLAND;
      }
   }

   private Item internalMethod03340() {
      if (this.internalField0237.isSelected()) {
         return Items.NETHER_WART;
      } else if (this.internalField0238.isSelected()) {
         return Items.WHEAT_SEEDS;
      } else if (this.internalField1066.isSelected()) {
         return Items.CARROT;
      } else if (this.internalField1067.isSelected()) {
         return Items.POTATO;
      } else if (this.internalField1068.isSelected()) {
         return Items.BEETROOT_SEEDS;
      } else {
         return this.internalField1065.isSelected() ? Items.SUGAR_CANE : null;
      }
   }

   private boolean internalMethod08023(BlockPos localValue1) {
      double localValue2 = internalField0149.player.getBlockInteractionRange();
      return internalField0149.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(localValue1)) <= localValue2 * localValue2;
   }

   private Direction internalMethod07179(BlockPos localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      Vec3d localValue3 = Vec3d.ofCenter(localValue1);
      double localValue4 = localValue2.x - localValue3.x;
      double localValue6 = localValue2.y - localValue3.y;
      double localValue8 = localValue2.z - localValue3.z;
      double localValue10 = Math.abs(localValue4);
      double localValue12 = Math.abs(localValue6);
      double localValue14 = Math.abs(localValue8);
      if (localValue12 >= localValue10 && localValue12 >= localValue14) {
         return localValue6 >= 0.0 ? Direction.UP : Direction.DOWN;
      } else if (localValue10 >= localValue14) {
         return localValue4 >= 0.0 ? Direction.EAST : Direction.WEST;
      } else {
         return localValue8 >= 0.0 ? Direction.SOUTH : Direction.NORTH;
      }
   }

   private Vec3d internalMethod03259(BlockPos localValue1, Direction localValue2) {
      return Vec3d.ofCenter(localValue1).add(localValue2.getOffsetX() * 0.5, localValue2.getOffsetY() * 0.5, localValue2.getOffsetZ() * 0.5);
   }

   private boolean internalMethod02862(Vec3d localValue1) {
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
         return this.internalField1055 >= 1;
      } else {
         return false;
      }
   }

   private void internalMethod09943() {
      this.internalField0118 = null;
      this.internalField1055 = 0;
   }

   private void internalMethod05426(BlockState localValue1) {
      Predicate<ItemStack> localValue2 = localValue0 -> localValue0.getItem() instanceof HoeItem && internalMethod04033(localValue0);
      this.internalMethod06273(localValue1, localValue2);
   }

   private boolean internalMethod06273(BlockState localValue1, Predicate<ItemStack> localValue2) {
      ItemStack localValue3 = internalField0149.player.getMainHandStack();
      CropFarmTask.InternalType0362 localValue4 = this.internalMethod05022(localValue3, localValue1, localValue2);
      InventorySlot localValue5 = null;

      for (InventorySlot localValue7 : InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod02638()) {
         ItemStack localValue8 = localValue7.internalMethod03427();
         CropFarmTask.InternalType0362 localValue9 = this.internalMethod05022(localValue8, localValue1, localValue2);
         if (localValue9.internalMethod00869(localValue4)) {
            localValue4 = localValue9;
            localValue5 = localValue7;
         }
      }

      if (!localValue4.internalMethod03228()) {
         return false;
      } else {
         if (localValue5 instanceof HotbarSlot localValue10) {
            InventoryUtils.internalMethod01980(localValue10);
         } else if (localValue5 instanceof MainInventorySlot localValue11) {
            int localValue12 = internalField0149.player.getInventory().getSelectedSlot();
            InventoryUtils.internalMethod08821(localValue11.internalMethod06662(), localValue12);
         }

         return true;
      }
   }

   private CropFarmTask.InternalType0362 internalMethod05022(ItemStack localValue1, BlockState localValue2, Predicate<ItemStack> localValue3) {
      if (localValue1 != null && !localValue1.isEmpty() && localValue3.test(localValue1)) {
         float localValue4 = localValue1.getMiningSpeedMultiplier(localValue2);
         int localValue5 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.EFFICIENCY);
         int localValue6 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FORTUNE);
         float localValue7 = internalMethod06940(localValue1);
         return new CropFarmTask.InternalType0362(localValue4, localValue5, localValue6, localValue7);
      } else {
         return new CropFarmTask.InternalType0362(-1.0F, -1, -1, -1.0F);
      }
   }

   private static float internalMethod06940(ItemStack localValue0) {
      if (!localValue0.isEmpty() && localValue0.isDamageable()) {
         int localValue1 = localValue0.getMaxDamage();
         return localValue1 <= 0 ? 1.0F : (float)(localValue1 - localValue0.getDamage()) / localValue1;
      } else {
         return 1.0F;
      }
   }

   private static boolean internalMethod04033(ItemStack localValue0) {
      return internalMethod06940(localValue0) > 0.05F;
   }

   private boolean internalMethod07354() {
      if (!this.internalField1263.internalMethod04496()) {
         return false;
      } else if (internalField0149.player == null) {
         return false;
      } else {
         PlayerInventory localValue1 = internalField0149.player.getInventory();
         return localValue1.getEmptySlot() != -1 ? false : this.internalMethod07358();
      }
   }

   private boolean internalMethod07358() {
      Item localValue1 = this.internalMethod03340();
      PlayerInventory localValue2 = internalField0149.player.getInventory();
      int localValue3 = 0;

      for (int localValue4 = 0; localValue4 < localValue2.size(); localValue4++) {
         ItemStack localValue5 = localValue2.getStack(localValue4);
         if (!localValue5.isEmpty() && this.internalMethod06941(localValue5)) {
            if (localValue1 == null || localValue5.getItem() != localValue1) {
               return true;
            }

            localValue3 += localValue5.getCount();
         }
      }

      return localValue3 > this.internalMethod07353();
   }

   private int internalMethod07353() {
      Item localValue1 = this.internalMethod03340();
      return localValue1 != null ? localValue1.getDefaultStack().getMaxCount() : 64;
   }

   private void internalMethod09944() {
      this.internalMethod10117();
      this.internalMethod09943();
      this.internalField0277 = false;
      this.internalField0228 = -1;
      this.internalField1053 = -1;
      BlockPos localValue1 = this.internalMethod01670();
      if (localValue1 == null) {
         this.internalMethod04510("modules.crop_farm.no_chest");
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1547;
         this.internalField0518.internalMethod00701();
      } else {
         this.internalField0351 = localValue1;
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1176;
      }
   }

   private BlockPos internalMethod01670() {
      if (internalField0149.world != null && internalField0149.player != null) {
         BlockPos localValue1 = internalField0149.player.getBlockPos();
         int localValue2 = (int)this.internalField0383.internalMethod08576() + 4;
         BlockPos localValue3 = null;
         double localValue4 = Double.MAX_VALUE;

         for (BlockPos localValue7 : BlockPos.iterateOutwards(localValue1, localValue2, localValue2, localValue2)) {
            BlockPos localValue8 = localValue7.toImmutable();
            if (!this.internalField0546.contains(localValue8) && internalField0149.world.getBlockEntity(localValue8) instanceof ChestBlockEntity) {
               double localValue9 = localValue7.getSquaredDistance(internalField0149.player.getEntityPos());
               if (localValue9 < localValue4) {
                  localValue4 = localValue9;
                  localValue3 = localValue8;
               }
            }
         }

         return localValue3;
      } else {
         return null;
      }
   }

   private void internalMethod09945() {
      if (this.internalField0351 == null) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
         this.internalField0518.internalMethod00701();
      } else if (this.internalMethod08023(this.internalField0351)) {
         this.internalMethod10117();
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1548;
         this.internalMethod09943();
         this.internalField0519.internalMethod00701();
      } else if (!this.internalField0277) {
         NewtonApiHolder.internalMethod01856().internalMethod00530(new RadiusGoal(this.internalField0351, 2));
         this.internalField0277 = true;
         this.internalField0229 = System.currentTimeMillis();
      } else if (!NewtonApiHolder.internalMethod01856().internalMethod00137()) {
         this.internalField0277 = false;
      } else {
         if (System.currentTimeMillis() - this.internalField0229 > 20000L) {
            this.internalMethod10117();
            this.internalField0546.add(this.internalField0351);
            this.internalField0351 = null;
            this.internalMethod09944();
         }
      }
   }

   private void internalMethod09946() {
      if (this.internalField0351 == null) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
         this.internalField0518.internalMethod00701();
      } else if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1549;
         this.internalField0519.internalMethod00701();
         this.internalField0228 = -1;
         this.internalField1053 = -1;
      } else if (!this.internalMethod08023(this.internalField0351)) {
         this.internalField0485 = CropFarmTask.InternalType0361.internalField1176;
         this.internalField0277 = false;
      } else {
         Vec3d localValue1 = Vec3d.ofCenter(this.internalField0351);
         if (this.internalMethod02862(localValue1)) {
            if (this.internalField0519.internalMethod02365((long)this.internalField1142.internalMethod08576())) {
               BlockHitResult localValue2 = new BlockHitResult(localValue1, Direction.UP, this.internalField0351, false);
               internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue2);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
               this.internalField0519.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod10007() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         if (this.internalField0519.internalMethod02365((long)this.internalField1142.internalMethod08576())) {
            if (this.internalField0228 != -1) {
               ItemStack localValue7 = ((Slot)localValue1.slots.get(this.internalField0228)).getStack();
               if (!localValue7.isEmpty() && localValue7.getCount() >= this.internalField1053 && this.internalMethod06941(localValue7)) {
                  this.internalMethod10008();
                  return;
               }
            }

            int localValue8 = this.internalMethod07357();
            DefaultedList localValue3 = localValue1.slots;

            for (int localValue4 = 0; localValue4 < localValue3.size(); localValue4++) {
               Slot localValue5 = (Slot)localValue3.get(localValue4);
               if (localValue5.inventory == internalField0149.player.getInventory() && localValue5.getIndex() != localValue8) {
                  ItemStack localValue6 = localValue5.getStack();
                  if (!localValue6.isEmpty() && this.internalMethod06941(localValue6)) {
                     InventoryUtils.internalMethod03592(localValue4);
                     this.internalField0228 = localValue4;
                     this.internalField1053 = localValue6.getCount();
                     this.internalField0519.internalMethod00701();
                     return;
                  }
               }
            }

            this.internalMethod06714(true);
         }
      } else {
         this.internalMethod06714(false);
      }
   }

   private void internalMethod10008() {
      if (this.internalField0351 != null) {
         this.internalField0546.add(this.internalField0351);
      }

      internalField0149.player.closeHandledScreen();
      this.internalField0351 = null;
      this.internalField0228 = -1;
      this.internalField1053 = -1;
      this.internalMethod09944();
   }

   private void internalMethod06714(boolean localValue1) {
      if (localValue1 && internalField0149.currentScreen != null) {
         internalField0149.player.closeHandledScreen();
      }

      this.internalField0351 = null;
      this.internalField0228 = -1;
      this.internalField1053 = -1;
      this.internalField0485 = CropFarmTask.InternalType0361.internalField0485;
      this.internalField0518.internalMethod00701();
      this.internalField0277 = false;
      this.internalMethod09943();
   }

   private int internalMethod07357() {
      Item localValue1 = this.internalMethod03340();
      if (localValue1 == null) {
         return -1;
      } else {
         PlayerInventory localValue2 = internalField0149.player.getInventory();
         int localValue3 = -1;
         int localValue4 = -1;

         for (int localValue5 = 0; localValue5 < localValue2.size(); localValue5++) {
            ItemStack localValue6 = localValue2.getStack(localValue5);
            if (!localValue6.isEmpty() && localValue6.getItem() == localValue1 && localValue6.getCount() > localValue4) {
               localValue4 = localValue6.getCount();
               localValue3 = localValue5;
            }
         }

         return localValue3;
      }
   }

   private Box internalMethod01718(BlockPos localValue1) {
      return new Box(localValue1).contract(0.02);
   }

   private boolean internalMethod04401(Item localValue1) {
      if (internalField0149.player.getMainHandStack().getItem() == localValue1) {
         return true;
      } else {
         Predicate<ItemStack> localValue2 = localValue1x -> localValue1x.getItem() == localValue1;
         HotbarSlot localValue3 = InventorySlots.internalMethod02872().internalMethod03297(localValue2);
         if (localValue3 != null) {
            InventoryUtils.internalMethod01980(localValue3);
            return true;
         } else {
            MainInventorySlot localValue4 = InventorySlots.internalMethod03558().internalMethod03297(localValue2);
            if (localValue4 != null) {
               int localValue5 = internalField0149.player.getInventory().getSelectedSlot();
               InventoryUtils.internalMethod08821(localValue4.internalMethod06662(), localValue5);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   static enum InternalType0361 {
      internalField0485,
      internalField0484,
      internalField1175,
      internalField1178,
      internalField1177,
      internalField1176,
      internalField1548,
      internalField1549,
      internalField1547;
   }

   static final class InternalType0362 {
      private final float internalField0205;
      private final int internalField0227;
      private final int internalField0228;
      private final float internalField0206;

      InternalType0362(float localValue1, int localValue2, int localValue3, float localValue4) {
         this.internalField0205 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0228 = localValue3;
         this.internalField0206 = localValue4;
      }

      boolean internalMethod03228() {
         return this.internalField0205 >= 0.0F;
      }

      boolean internalMethod00869(CropFarmTask.InternalType0362 localValue1) {
         if (this.internalField0228 != localValue1.internalField0228) {
            return this.internalField0228 > localValue1.internalField0228;
         } else if (this.internalField0205 != localValue1.internalField0205) {
            return this.internalField0205 > localValue1.internalField0205;
         } else {
            return this.internalField0227 != localValue1.internalField0227 ? this.internalField0227 > localValue1.internalField0227 : this.internalField0206 > localValue1.internalField0206;
         }
      }

      @Override
      public final String toString() {
         return "InternalType0362[miningSpeed=" + this.internalField0205 + ", efficiency=" + this.internalField0227 + ", fortune=" + this.internalField0228 + ", durability=" + this.internalField0206 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         CropFarmTask.InternalType0362 other = (CropFarmTask.InternalType0362) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206);
      }

      public float internalMethod03226() {
         return this.internalField0205;
      }

      public int internalMethod03227() {
         return this.internalField0227;
      }

      public int internalMethod03233() {
         return this.internalField0228;
      }

      public float internalMethod03232() {
         return this.internalField0206;
      }
   }
}
