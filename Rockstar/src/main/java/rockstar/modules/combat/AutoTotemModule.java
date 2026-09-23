package rockstar.modules.combat;







import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Auto Totem",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.auto_totem"
)
public class AutoTotemModule extends Module {
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private BooleanSetting internalField0650;
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private SliderSetting internalField1142;
   private SliderSetting internalField1140;
   private final AutoTotemSwapController internalField0024 = new AutoTotemSwapController(new RankedSlotLocator(), new OffhandSwapExecutor());
   private int internalField0228;
   private int internalField1053;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (!(internalField0149.player.getMaxHealth() <= 2.0F)) {
            this.internalMethod09476();
            this.internalField0024.internalMethod03734(false);
            boolean localValue2 = this.internalMethod09477();
            boolean localValue3 = this.internalMethod09624();
            boolean localValue4 = internalField0149.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
            boolean localValue5 = localValue2 && (!localValue3 || localValue4);
            if (!localValue5 && this.internalField1053 > 0) {
               this.internalField1053--;
            }

            if (localValue5) {
               this.internalField1053 = 20;
            }

            boolean localValue6 = localValue5 || this.internalField1053 > 0;
            if (!localValue2 || !localValue3 || localValue4) {
               if (this.internalField0228 > 0) {
                  this.internalField0024.internalMethod01087(Items.TOTEM_OF_UNDYING, localValue6, localValue0 -> true, this::internalMethod09623);
               } else if (!localValue2) {
                  this.internalField0024.internalMethod01087(Items.TOTEM_OF_UNDYING, false, localValue0 -> true, this::internalMethod09623);
               }
            }
         }
      }
   };

   public AutoTotemModule() {
      this.internalMethod09474();
   }

   private void internalMethod09474() {
      this.internalField0383 = new SliderSetting(this, "modules.settings.auto_totem.health")
         .internalMethod05900(1.0F)
         .internalMethod02732(20.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(6.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.auto_totem.elytra_health")
         .internalMethod05900(1.0F)
         .internalMethod02732(20.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(6.0F);
      this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_totem.stop_using");
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.auto_totem.select_with");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_totem.select_with.fall").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_totem.select_with.crystal");
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_totem.select_with.tnt");
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auto_totem.select_with.trident");
      this.internalField1142 = new SliderSetting(this, "modules.settings.auto_totem.tnt_distance", () -> !this.internalField1075.isSelected())
         .internalMethod05900(1.0F)
         .internalMethod02732(40.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(19.0F);
      this.internalField1140 = new SliderSetting(this, "modules.settings.auto_totem.crystal_distance", () -> !this.internalField0244.isSelected())
         .internalMethod05900(1.0F)
         .internalMethod02732(40.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(19.0F);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.internalField0024.internalMethod05365(new AutoTotemSwapController.InternalType0363() {
         @Override
         public void internalMethod00133(float localValue1) {
         }

         @Override
         public void internalMethod05133(ItemStack localValue1) {
         }
      });
   }

   private boolean internalMethod09623() {
      return this.internalField0650.internalMethod04496() || !internalField0149.player.isUsingItem();
   }

   public boolean internalMethod09475() {
      if (internalField0149.player == null || !this.isEnabled()) {
         return false;
      } else {
         return this.internalMethod09477()
            ? true
            : this.internalField0024.internalMethod06520() && internalField0149.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
      }
   }

   public boolean internalMethod09477() {
      if (internalField0149.player == null) {
         return false;
      } else if (this.internalMethod09628()) {
         return true;
      } else if (this.internalField0244.isSelected() && this.internalMethod09629()) {
         return true;
      } else if (this.internalField1075.isSelected() && this.internalMethod09966()) {
         return true;
      } else {
         return this.internalField1074.isSelected() && this.internalMethod09968() ? true : this.internalField0245.isSelected() && this.internalMethod09967();
      }
   }

   private boolean internalMethod09624() {
      if (!ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
         return false;
      } else {
         return internalField0149.player != null && internalField0149.player.getItemCooldownManager() != null
            ? internalField0149.player.getItemCooldownManager().isCoolingDown(Items.TOTEM_OF_UNDYING.getDefaultStack())
            : false;
      }
   }

   private boolean internalMethod09628() {
      float localValue1 = internalField0149.player.getHealth() + internalField0149.player.getAbsorptionAmount();
      float localValue2 = internalField0149.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA
         ? this.internalField0382.internalMethod08576()
         : this.internalField0383.internalMethod08576();
      return localValue1 <= localValue2;
   }

   private boolean internalMethod09629() {
      double localValue1 = this.internalField1140.internalMethod08576();
      return !internalField0149.world.getEntitiesByClass(EndCrystalEntity.class, internalField0149.player.getBoundingBox().expand(localValue1), localValue0 -> true).isEmpty();
   }

   private boolean internalMethod09966() {
      double localValue1 = this.internalField1142.internalMethod08576();
      return !internalField0149.world.getEntitiesByClass(TntEntity.class, internalField0149.player.getBoundingBox().expand(localValue1), localValue0 -> true).isEmpty();
   }

   private boolean internalMethod09967() {
      if (!internalField0149.player.isOnGround()
         && !internalField0149.player.isGliding()
         && !internalField0149.player.isTouchingWater()
         && !internalField0149.player.isClimbing()
         && !internalField0149.player.isInLava()) {
         float localValue1 = internalField0149.player.getHealth() + internalField0149.player.getAbsorptionAmount();
         float localValue2 = FallDistancePredictor.internalMethod02789(internalField0149.player, 30);
         return localValue2 >= localValue1;
      } else {
         return false;
      }
   }

   private boolean internalMethod09968() {
      return internalField0149.world
         .getEntitiesByClass(
            TridentEntity.class, internalField0149.player.getBoundingBox().expand(5.0), localValue0 -> localValue0.isAlive() && localValue0.getOwner() != internalField0149.player
         )
         .stream()
         .anyMatch(localValue0 -> localValue0.getVelocity().lengthSquared() > 0.1);
   }

   private void internalMethod09476() {
      this.internalField0228 = InventorySlots.internalMethod03558()
         .internalMethod07591(InventorySlots.internalMethod02872())
         .internalMethod07591(InventorySlots.internalMethod07766())
         .internalMethod07591(InventorySlots.internalMethod08231())
         .internalMethod07613(Items.TOTEM_OF_UNDYING)
         .size();
   }

   @Override
   public void onDisable() {
      this.internalField0024.internalMethod05365(null);
      super.onDisable();
      this.internalField0024.internalMethod06519();
      this.internalField1053 = 0;
   }
}
