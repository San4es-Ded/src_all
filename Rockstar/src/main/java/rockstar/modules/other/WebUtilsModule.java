package rockstar.modules.other;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.modules.combat.AuraModule;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.NotNull;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Web Utils",
   category = ModuleCategory.OTHER
)
public class WebUtilsModule extends Module {
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting internalField0674;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (this.internalField0245.isSelected()) {
            this.internalMethod09911();
         }

         if (this.internalField0244.isSelected()) {
            LivingEntity localValue2 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
            if (localValue2 != null
               && internalField0149.player.getEntityPos().distanceTo(localValue2.getEntityPos())
                  <= RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).internalMethod05151().internalMethod08576()) {
               if (this.internalMethod04809(localValue2)) {
                  return;
               }

               List localValue3 = this.internalMethod05310(localValue2);
               int localValue4 = Math.min((int)this.internalField0383.internalMethod08576(), localValue3.size());
               int localValue5 = 0;

               for (BlockPos localValue7 : (Iterable<BlockPos>)(Iterable<?>)localValue3) {
                  if (localValue5 >= localValue4) {
                     break;
                  }

                  localValue5++;
                  WebUtilsModule.InternalType0378 localValue8 = this.internalMethod07002(localValue7);
                  if (localValue8 != null) {
                     this.internalMethod04144(localValue8.internalMethod04520());
                     if (this.internalMethod04145(localValue8.internalMethod04520()) && this.internalField0519.internalMethod02365(this.internalMethod07265())) {
                        if (this.internalMethod04717(localValue8) == null) {
                           return;
                        }

                        WebUtilsModule.InternalType0379 localValue9 = this.internalMethod05737();
                        if (localValue9 == null) {
                           return;
                        }

                        try {
                           if (this.internalMethod06007(localValue9.internalMethod03632(), localValue8)) {
                              this.internalField0519.internalMethod00701();
                           }
                        } finally {
                           this.internalMethod02699(localValue9);
                        }

                        return;
                     }

                     return;
                  }
               }
            }
         }
      }
   };

   public WebUtilsModule() {
      this.internalMethod09909();
   }

   private void internalMethod09909() {
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.web_utils");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.web_utils.no_web");
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.web_utils.trap_web");
      this.internalField0674 = new MultiSelectSetting(this, "modules.settings.web_utils.placement_mode", () -> !this.internalField0244.isSelected());
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.web_utils.placement_mode.default").select();
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.web_utils.placement_mode.full_body");
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.web_utils.placement_mode.sides");
      this.internalField0383 = new SliderSetting(this, "modules.settings.web_utils.count", () -> !this.internalField0244.isSelected())
         .internalMethod05900(1.0F)
         .internalMethod02732(6.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(1.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.web_utils.delay", () -> !this.internalField0244.isSelected())
         .internalMethod05900(100.0F)
         .internalMethod02732(1000.0F)
         .internalMethod08673(50.0F)
         .internalMethod08074(150.0F)
         .internalMethod06240("ms");
      this.internalField1142 = new SliderSetting(this, "modules.settings.web_utils.no_web_speed", () -> !this.internalField0245.isSelected())
         .internalMethod05900(0.1F)
         .internalMethod02732(1.0F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.57F);
   }

   private void internalMethod09911() {
      if (internalField0149.player != null && internalField0149.world != null && this.internalMethod09910()) {
         double localValue1 = internalField0149.options.jumpKey.isPressed() ? 1.3 : (internalField0149.options.sneakKey.isPressed() ? -1.3 : 0.0);
         float localValue3 = internalField0149.player.getYaw() * (float) (Math.PI / 180.0);
         float localValue4 = this.internalField1142.internalMethod08576();
         float localValue5 = internalField0149.player.forwardSpeed * localValue4;
         float localValue6 = internalField0149.player.sidewaysSpeed * localValue4;
         if (localValue5 == 0.0F && localValue6 == 0.0F) {
            internalField0149.player.setVelocity(0.0, localValue1, 0.0);
         } else {
            internalField0149.player
               .setVelocity(-MathHelper.sin(localValue3) * localValue5 + MathHelper.cos(localValue3) * localValue6, localValue1, MathHelper.cos(localValue3) * localValue5 + MathHelper.sin(localValue3) * localValue6);
         }
      }
   }

   private boolean internalMethod09910() {
      return this.internalMethod04809(internalField0149.player);
   }

   private boolean internalMethod04809(LivingEntity localValue1) {
      Box localValue2 = localValue1.getBoundingBox();
      int localValue3 = MathHelper.floor(localValue2.minX);
      int localValue4 = MathHelper.floor(localValue2.minY);
      int localValue5 = MathHelper.floor(localValue2.minZ);
      int localValue6 = MathHelper.ceil(localValue2.maxX);
      int localValue7 = MathHelper.ceil(localValue2.maxY);
      int localValue8 = MathHelper.ceil(localValue2.maxZ);
      Mutable localValue9 = new Mutable();

      for (int localValue10 = localValue3; localValue10 < localValue6; localValue10++) {
         for (int localValue11 = localValue4; localValue11 < localValue7; localValue11++) {
            for (int localValue12 = localValue5; localValue12 < localValue8; localValue12++) {
               if (internalField0149.world.getBlockState(localValue9.set(localValue10, localValue11, localValue12)).isOf(Blocks.COBWEB)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private WebUtilsModule.InternalType0379 internalMethod05737() {
      int localValue1 = internalField0149.player.getInventory().getSelectedSlot();
      OffhandSlot localValue2 = InventorySlots.internalMethod07766().internalMethod03297(localValue0 -> localValue0.getItem() == Items.COBWEB);
      if (localValue2 != null) {
         return new WebUtilsModule.InternalType0379(localValue2, -1, -1);
      } else {
         HotbarSlot localValue3 = InventorySlots.internalMethod02872().internalMethod03297(localValue0 -> localValue0.getItem() == Items.COBWEB);
         if (localValue3 != null) {
            InventoryUtils.internalMethod03663(localValue3.internalMethod08745());
            return new WebUtilsModule.InternalType0379(localValue3, localValue1, -1);
         } else {
            MainInventorySlot localValue4 = InventorySlots.internalMethod03558().internalMethod03297(localValue0 -> localValue0.getItem() == Items.COBWEB);
            if (localValue4 != null) {
               InventoryUtils.internalMethod08821(localValue4.internalMethod06662(), internalField0149.player.getInventory().getSelectedSlot());
               return new WebUtilsModule.InternalType0379(new HotbarSlot(localValue1), localValue1, localValue4.internalMethod06662());
            } else {
               return null;
            }
         }
      }
   }

   private void internalMethod02699(WebUtilsModule.InternalType0379 localValue1) {
      if (internalField0149.player != null && localValue1 != null) {
         if (localValue1.internalMethod00360() != -1) {
            InventoryUtils.internalMethod08821(localValue1.internalMethod00360(), localValue1.internalMethod00359());
         } else {
            if (localValue1.internalMethod00359() != -1) {
               InventoryUtils.internalMethod03663(localValue1.internalMethod00359());
            }
         }
      }
   }

   private boolean internalMethod06007(InventorySlot localValue1, WebUtilsModule.InternalType0378 localValue2) {
      if (internalField0149.player != null && internalField0149.interactionManager != null && internalField0149.world != null) {
         Hand localValue3 = this.internalMethod05422(localValue1);
         if (localValue3 == null) {
            return false;
         } else {
            BlockHitResult localValue4 = this.internalMethod04717(localValue2);
            if (localValue4 == null) {
               return false;
            } else {
               ActionResult localValue5 = internalField0149.interactionManager.interactBlock(internalField0149.player, localValue3, localValue4);
               if (!localValue5.isAccepted()) {
                  return false;
               } else {
                  internalField0149.player.swingHand(localValue3);
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private Hand internalMethod05422(InventorySlot localValue1) {
      if (localValue1 instanceof OffhandSlot) {
         return Hand.OFF_HAND;
      } else if (localValue1 instanceof HotbarSlot localValue2) {
         InventoryUtils.internalMethod03663(localValue2.internalMethod08745());
         return Hand.MAIN_HAND;
      } else {
         return null;
      }
   }

   private WebUtilsModule.InternalType0378 internalMethod07002(BlockPos localValue1) {
      if (internalField0149.player != null && internalField0149.world != null && this.internalMethod03202(localValue1)) {
         Vec3d localValue2 = internalField0149.player.getEyePos();
         double localValue3 = internalField0149.player.getBlockInteractionRange();

         for (Direction localValue8 : Direction.values()) {
            BlockPos localValue9 = localValue1.offset(localValue8);
            Direction localValue10 = localValue8.getOpposite();
            if (this.internalMethod04640(localValue9)) {
               Vec3d localValue11 = this.internalMethod00914(localValue9, localValue10);
               if (!(localValue2.squaredDistanceTo(localValue11) > localValue3 * localValue3)) {
                  Rotation localValue12 = this.internalMethod01731(localValue2, localValue11);
                  return new WebUtilsModule.InternalType0378(localValue1, localValue9, localValue10, localValue12);
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean internalMethod03202(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return localValue2.isAir() || localValue2.getCollisionShape(internalField0149.world, localValue1).isEmpty() && internalField0149.world.getFluidState(localValue1).isEmpty();
   }

   private boolean internalMethod04640(BlockPos localValue1) {
      BlockState localValue2 = internalField0149.world.getBlockState(localValue1);
      return !localValue2.isAir();
   }

   private Vec3d internalMethod00914(BlockPos localValue1, Direction localValue2) {
      return localValue1.toCenterPos().add(localValue2.getOffsetX() * 0.5, localValue2.getOffsetY() * 0.5, localValue2.getOffsetZ() * 0.5);
   }

   private Rotation internalMethod01731(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue2.x - localValue1.x;
      double localValue5 = localValue2.y - localValue1.y;
      double localValue7 = localValue2.z - localValue1.z;
      double localValue9 = Math.sqrt(localValue3 * localValue3 + localValue7 * localValue7);
      return new Rotation((float)Math.toDegrees(Math.atan2(localValue7, localValue3)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(localValue5, localValue9))));
   }

   private void internalMethod04144(Rotation localValue1) {
      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue1, RotationBehavior.internalField1003, 90.0F, 90.0F, 75.0F, RotationPriority.internalField1012);
   }

   private boolean internalMethod04145(Rotation localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      float localValue3 = Math.abs(MathHelper.wrapDegrees(localValue2.internalMethod00169() - localValue1.internalMethod00169()));
      float localValue4 = Math.abs(localValue2.internalMethod00171() - localValue1.internalMethod00171());
      return localValue3 <= 5.0F && localValue4 <= 5.0F;
   }

   private BlockHitResult internalMethod04717(WebUtilsModule.InternalType0378 localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      BlockHitResult localValue3 = this.internalMethod02815(localValue2);
      return !this.internalMethod03789(localValue3, localValue1) ? null : localValue3;
   }

   private BlockHitResult internalMethod02815(Rotation localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      Vec3d localValue3 = localValue2.add(
         internalField0149.player
            .getRotationVector(localValue1.internalMethod00171(), localValue1.internalMethod00169())
            .multiply(internalField0149.player.getBlockInteractionRange())
      );
      return internalField0149.world.raycast(new RaycastContext(localValue2, localValue3, ShapeType.OUTLINE, FluidHandling.NONE, internalField0149.player));
   }

   private boolean internalMethod03789(BlockHitResult localValue1, WebUtilsModule.InternalType0378 localValue2) {
      return localValue1 != null && localValue1.getType() == Type.BLOCK
         ? localValue1.getBlockPos().equals(localValue2.internalMethod00267())
            && localValue1.getSide() == localValue2.internalMethod02673()
            && localValue1.getBlockPos().offset(localValue1.getSide()).equals(localValue2.internalMethod04742())
            && this.internalMethod03202(localValue2.internalMethod04742())
            && this.internalMethod04640(localValue2.internalMethod00267())
         : false;
   }

   private long internalMethod07265() {
      return Math.max(100L, (long)this.internalField0382.internalMethod08576());
   }

   @NotNull
   private List<BlockPos> internalMethod05310(LivingEntity localValue1) {
      BlockPos localValue2 = localValue1.getBlockPos();
      ArrayList localValue3 = new ArrayList();
      if (this.internalField1075.isSelected()) {
         this.internalMethod05827(localValue3, localValue2);
      }

      if (this.internalField1074.isSelected()) {
         this.internalMethod05827(localValue3, localValue2);
         this.internalMethod05827(localValue3, localValue2.up());
      }

      if (this.internalField1073.isSelected()) {
         this.internalMethod05827(localValue3, localValue2.north());
         this.internalMethod05827(localValue3, localValue2.south());
         this.internalMethod05827(localValue3, localValue2.east());
         this.internalMethod05827(localValue3, localValue2.west());
         this.internalMethod05827(localValue3, localValue2.north().up());
         this.internalMethod05827(localValue3, localValue2.south().up());
      }

      return localValue3;
   }

   private void internalMethod05827(List<BlockPos> localValue1, BlockPos localValue2) {
      if (!localValue1.contains(localValue2)) {
         localValue1.add(localValue2);
      }
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod05823() {
      return this.internalField0245;
   }

   static final class InternalType0378 {
      private final BlockPos internalField0352;
      private final BlockPos internalField0351;
      private final Direction internalField0150;
      private final Rotation internalField0118;

      InternalType0378(BlockPos localValue1, BlockPos localValue2, Direction localValue3, Rotation localValue4) {
         this.internalField0352 = localValue1;
         this.internalField0351 = localValue2;
         this.internalField0150 = localValue3;
         this.internalField0118 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0378[placePos=" + this.internalField0352 + ", support=" + this.internalField0351 + ", side=" + this.internalField0150 + ", rotation=" + this.internalField0118 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0351);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0150);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0118);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WebUtilsModule.InternalType0378 other = (WebUtilsModule.InternalType0378) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0351, other.internalField0351)
            && java.util.Objects.equals(this.internalField0150, other.internalField0150)
            && java.util.Objects.equals(this.internalField0118, other.internalField0118);
      }

      public BlockPos internalMethod04742() {
         return this.internalField0352;
      }

      public BlockPos internalMethod00267() {
         return this.internalField0351;
      }

      public Direction internalMethod02673() {
         return this.internalField0150;
      }

      public Rotation internalMethod04520() {
         return this.internalField0118;
      }
   }

   static final class InternalType0379 {
      private final InventorySlot internalField0022;
      private final int internalField0227;
      private final int internalField0228;

      InternalType0379(InventorySlot localValue1, int localValue2, int localValue3) {
         this.internalField0022 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0228 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0379[slot=" + this.internalField0022 + ", previousHotbarSlot=" + this.internalField0227 + ", swappedInventorySlot=" + this.internalField0228 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0022);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WebUtilsModule.InternalType0379 other = (WebUtilsModule.InternalType0379) localValue1;
         return java.util.Objects.equals(this.internalField0022, other.internalField0022)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228);
      }

      public InventorySlot internalMethod03632() {
         return this.internalField0022;
      }

      public int internalMethod00359() {
         return this.internalField0227;
      }

      public int internalMethod00360() {
         return this.internalField0228;
      }
   }
}
