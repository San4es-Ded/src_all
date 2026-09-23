package rockstar.modules.combat;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.window.MouseEvent;

@ModuleInfo(
   name = "Auto Explosion",
   internalMethod09633 = "modules.descriptions.auto_explosion",
   category = ModuleCategory.COMBAT
)
public class AutoExplosionModule extends Module {
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private BlockPos internalField0352;
   private BlockPos internalField0351;
   private int internalField0227 = -1;
   private final EventListener<MouseEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (internalField0149.currentScreen == null) {
            if (localValue1.getButton() == 1 && localValue1.getAction() == 1) {
               if (internalField0149.player.getMainHandStack().isEmpty() || LegacyItemTypes.isSword(internalField0149.player.getMainHandStack())) {
                  if (internalField0149.crosshairTarget instanceof BlockHitResult localValue2) {
                     if (internalField0149.world.getBlockState(localValue2.getBlockPos()).isOf(Blocks.OBSIDIAN)) {
                        BlockPos localValue4 = localValue2.getBlockPos().up();
                        if (internalField0149.world.getBlockState(localValue4).isAir()) {
                           if (!this.internalMethod05190(localValue4.getY())) {
                              this.internalField0352 = localValue4.toImmutable();
                              this.internalField0519.internalMethod00701();
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   };

   public AutoExplosionModule() {
      this.internalMethod09387();
   }

   private void internalMethod09387() {
      this.internalField0675 = new MultiSelectSetting(this, "\u041d\u0435 \u0432\u0437\u0440\u044b\u0432\u0430\u0442\u044c");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u0421\u0435\u0431\u044f").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u0414\u0440\u0443\u0437\u0435\u0439").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "\u041f\u0440\u0435\u0434\u043c\u0435\u0442\u044b").select();
   }

   @Override
   public void onDisable() {
      this.internalField0352 = null;
      this.internalField0351 = null;
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null) {
         SlotCollection localValue1 = InventorySlots.internalMethod02872();
         HotbarSlot localValue2 = (HotbarSlot)localValue1.internalMethod02510(Items.END_CRYSTAL);
         if (localValue2 == null) {
            this.internalField0352 = null;
            this.internalField0351 = null;
         } else {
            if (this.internalField0352 != null && this.internalField0519.internalMethod02365(1L)) {
               if (!this.internalMethod05190(this.internalField0352.getY())) {
                  this.internalMethod03037(localValue2.internalMethod06662(), this.internalField0352);
                  this.internalField0351 = this.internalField0352;
               }

               this.internalField0352 = null;
               this.internalField0519.internalMethod00701();
            }

            EndCrystalEntity localValue3 = this.internalMethod05893(this.internalField0351);
            if (localValue3 != null) {
               Vec3d localValue4 = localValue3.getEntityPos().add(0.0, 0.5, 0.0);
               float[] localValue5 = this.internalMethod05699(localValue4);
               RockstarClient.getInstance().internalMethod02368().internalMethod04698(new Rotation(localValue5[0], localValue5[1]));
               this.internalMethod03488(localValue3);
            }

            super.internalMethod08229();
         }
      }
   }

   private void internalMethod03037(int localValue1, BlockPos localValue2) {
      if (internalField0149.player != null && internalField0149.world != null) {
         int localValue3 = localValue1 - 36;
         if (localValue3 >= 0 && localValue3 <= 8) {
            BlockPos localValue4 = localValue2.down();
            Vec3d localValue5 = new Vec3d(localValue4.getX() + 0.5, localValue4.getY() + 1.0, localValue4.getZ() + 0.5);
            float[] localValue6 = this.internalMethod05699(localValue5);
            RockstarClient.getInstance().internalMethod02368().internalMethod04698(new Rotation(localValue6[0], localValue6[1]));
            int localValue7 = internalField0149.player.getInventory().getSelectedSlot();
            internalField0149.player.getInventory().setSelectedSlot(localValue3);
            BlockHitResult localValue8 = new BlockHitResult(localValue5, Direction.UP, localValue4, false);
            internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue8);
            internalField0149.player.swingHand(Hand.MAIN_HAND);
            internalField0149.player.getInventory().setSelectedSlot(localValue7);

            for (Entity localValue10 : internalField0149.world.getEntities()) {
               if (localValue10 instanceof EndCrystalEntity localValue11 && localValue11.squaredDistanceTo(localValue5) < 1.0) {
                  return;
               }
            }
         }
      }
   }

   private EndCrystalEntity internalMethod05893(BlockPos localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         Box localValue2 = new Box(localValue1.getX(), localValue1.getY(), localValue1.getZ(), localValue1.getX() + 1.0, localValue1.getY() + 2.0, localValue1.getZ() + 1.0);

         for (Entity localValue4 : internalField0149.world.getOtherEntities(null, localValue2)) {
            if (localValue4 instanceof EndCrystalEntity localValue5 && localValue5.isAlive()) {
               return localValue5;
            }
         }

         return null;
      }
   }

   private void internalMethod03488(EndCrystalEntity localValue1) {
      if (this.internalMethod03489(localValue1)) {
         if (this.internalField0518.internalMethod02365(80L)) {
            internalField0149.interactionManager.attackEntity(internalField0149.player, localValue1);
            internalField0149.player.swingHand(Hand.MAIN_HAND);
            this.internalField0227 = localValue1.getId();
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private boolean internalMethod03489(EndCrystalEntity localValue1) {
      if (localValue1 == null || !localValue1.isAlive()) {
         return false;
      } else if (this.internalMethod05696(localValue1)) {
         return false;
      } else if (internalField0149.player.distanceTo(localValue1) > 4.0) {
         return false;
      } else {
         return this.internalField0227 == localValue1.getId() && !this.internalField0518.internalMethod02365(300L)
            ? false
            : internalField0149.player.getAttackCooldownProgress(1.0F) >= 1.0F;
      }
   }

   private boolean internalMethod05696(EndCrystalEntity localValue1) {
      if (this.internalMethod05190(localValue1.getY())) {
         return true;
      } else {
         return this.internalField0244.isSelected() && this.internalMethod08458(localValue1)
            ? true
            : this.internalField1075.isSelected() && this.internalMethod08870(localValue1);
      }
   }

   private boolean internalMethod05190(double localValue1) {
      return this.internalField0245.isSelected() && localValue1 <= internalField0149.player.getY() + 0.1;
   }

   private boolean internalMethod08458(EndCrystalEntity localValue1) {
      Box localValue2 = localValue1.getBoundingBox().expand(6.0);

      for (PlayerEntity localValue4 : internalField0149.world.getEntitiesByClass(PlayerEntity.class, localValue2, localValue0 -> localValue0 != internalField0149.player)) {
         if (localValue4.isAlive() && RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue4.getName().getString())) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod08870(EndCrystalEntity localValue1) {
      Box localValue2 = localValue1.getBoundingBox().expand(6.0);

      for (ItemEntity localValue4 : internalField0149.world.getEntitiesByClass(ItemEntity.class, localValue2, localValue0 -> true)) {
         if (localValue4 != null && localValue4.getStack() != null) {
            Item localValue5 = localValue4.getStack().getItem();
            if (localValue5 == Items.TOTEM_OF_UNDYING
               || localValue5 == Items.END_CRYSTAL
               || localValue5 == Items.ENCHANTED_GOLDEN_APPLE
               || localValue5 == Items.NETHERITE_HELMET
               || localValue5 == Items.NETHERITE_CHESTPLATE
               || localValue5 == Items.NETHERITE_LEGGINGS
               || localValue5 == Items.NETHERITE_BOOTS
               || localValue5 == Items.NETHERITE_SWORD
               || localValue5 == Items.DIAMOND_SWORD
               || localValue5 == Items.ELYTRA
               || localValue5 == Items.TRIDENT) {
               return true;
            }
         }
      }

      return false;
   }

   private float[] internalMethod05699(Vec3d localValue1) {
      Vec3d localValue2 = new Vec3d(
         internalField0149.player.getX(),
         internalField0149.player.getY() + internalField0149.player.getEyeHeight(internalField0149.player.getPose()),
         internalField0149.player.getZ()
      );
      double localValue3 = localValue1.x - localValue2.x;
      double localValue5 = localValue1.y - localValue2.y;
      double localValue7 = localValue1.z - localValue2.z;
      double localValue9 = Math.sqrt(localValue3 * localValue3 + localValue7 * localValue7);
      float localValue11 = (float)Math.toDegrees(Math.atan2(localValue7, localValue3)) - 90.0F;
      float localValue12 = (float)(-Math.toDegrees(Math.atan2(localValue5, localValue9)));
      return new float[]{
         internalField0149.player.getYaw() + MathHelper.wrapDegrees(localValue11 - internalField0149.player.getYaw()),
         internalField0149.player.getPitch() + MathHelper.wrapDegrees(localValue12 - internalField0149.player.getPitch())
      };
   }
}
