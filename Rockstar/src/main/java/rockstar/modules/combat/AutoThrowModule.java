package rockstar.modules.combat;










import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.modules.player.GuiMoveModule;
import rockstar.client.module.Module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;

@ModuleInfo(
   name = "Auto Throw",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.auto_throw"
)
public class AutoThrowModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private KeybindSetting internalField0648;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private InventorySlot internalField0022;
   private int internalField1053 = -1;
   private int internalField1055;
   private int internalField1056 = -1;
   private boolean internalField0277;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null
         && internalField0149.world != null
         && internalField0149.interactionManager != null
         && internalField0149.getNetworkHandler() != null) {
         if (this.internalField1053 >= 0) {
            this.internalMethod09807();
         } else if (this.internalField0668.internalMethod06103(this.internalField0237) && this.internalField0519.internalMethod02365(5000L)) {
            if (RockstarClient.getInstance().internalMethod04463().internalMethod01783() instanceof PlayerEntity localValue3 && localValue3.isAlive()) {
               if (this.internalMethod00794(localValue3)) {
                  if (!(GameUtils.internalMethod02919(localValue3) + localValue3.getAbsorptionAmount() >= this.internalField0383.internalMethod08576())) {
                     if (!(internalField0149.player.distanceTo(localValue3) > this.internalField0382.internalMethod08576())) {
                        if (this.internalMethod01824(localValue3)) {
                           this.internalMethod07688(false);
                        }
                     }
                  }
               }
            }
         }
      }
   };
   private final EventListener<KeyPressEvent> internalField0158 = localValue1 -> {
      if (localValue1.getAction() == 1 && this.internalField0648.internalMethod02165(localValue1.getKey())) {
         this.internalMethod07688(true);
      }
   };
   private final EventListener<MouseEvent> internalField1028 = localValue1 -> {
      if (localValue1.getAction() == 1 && this.internalField0648.internalMethod02165(localValue1.getButton())) {
         this.internalMethod07688(true);
      }
   };

   public AutoThrowModule() {
      this.internalMethod09806();
   }

   private void internalMethod09806() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.auto_throw.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_throw.mode.automatic").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_throw.mode.bind");
      this.internalField0648 = new KeybindSetting(
         this, "modules.settings.auto_throw.key", () -> this.internalField0668.internalMethod06103(this.internalField0237)
      );
      this.internalField0383 = new SliderSetting(
            this, "modules.settings.auto_throw.health", () -> this.internalField0668.internalMethod06103(this.internalField0238)
         )
         .internalMethod05900(1.0F)
         .internalMethod02732(20.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(15.0F);
      this.internalField0382 = new SliderSetting(
            this, "modules.settings.auto_throw.distance", () -> this.internalField0668.internalMethod06103(this.internalField0238)
         )
         .internalMethod05900(1.0F)
         .internalMethod02732(6.0F)
         .internalMethod08673(0.1F)
         .internalMethod08074(3.0F);
   }

   private void internalMethod07688(boolean localValue1) {
      if (internalField0149.player != null && internalField0149.currentScreen == null && this.internalField1053 < 0) {
         if (localValue1 == this.internalField0668.internalMethod06103(this.internalField0238)) {
            if (this.internalMethod04481(RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class))) {
               InventorySlot localValue2 = this.internalMethod07618();
               if (localValue2 == null) {
                  if (localValue1) {
                     RockstarClient.getInstance()
                        .internalMethod02503()
                        .internalMethod00599(
                           NotificationType.internalField0705,
                           LanguageManager.internalMethod07214("swap.item_not_found"),
                           LanguageManager.internalMethod00160("swap.item_required", Items.SPLASH_POTION.getName().getString())
                        );
                  }
               } else {
                  this.internalField0022 = localValue2;
                  this.internalField1056 = internalField0149.player.getInventory().getSelectedSlot();
                  this.internalField0277 = false;
                  this.internalField1055 = 0;
                  this.internalField1053 = localValue2 instanceof HotbarSlot ? 2 : 0;
               }
            }
         }
      }
   }

   private void internalMethod09807() {
      if (this.internalField0022 != null && !internalField0149.player.isDead() && ++this.internalField1055 <= 60) {
         GuiMoveModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
         switch (this.internalField1053) {
            case 0:
               InventoryUtils.internalMethod08821(this.internalField0022.internalMethod06662(), 8);
               this.internalField0277 = true;
               this.internalField1053 = 1;
               break;
            case 1:
               if (this.internalMethod04481(localValue1)) {
                  this.internalField1053 = 2;
               }
               break;
            case 2:
               int localValue2 = this.internalField0022 instanceof HotbarSlot localValue3 ? localValue3.internalMethod08745() : 8;
               if (!internalMethod07217(InventoryUtils.internalMethod02738(localValue2).internalMethod03427())) {
                  this.internalMethod09164();
                  return;
               }

               this.internalMethod08958(localValue2);
               internalField0149.interactionManager.interactItem(internalField0149.player, Hand.MAIN_HAND);
               this.internalField0519.internalMethod00701();
               this.internalField1053 = 3;
               break;
            case 3:
               if (this.internalMethod04481(localValue1)) {
                  this.internalMethod09164();
               }
         }
      } else {
         this.internalMethod09164();
      }
   }

   private void internalMethod09164() {
      if (this.internalField0277 && this.internalField0022 != null) {
         InventoryUtils.internalMethod08821(this.internalField0022.internalMethod06662(), 8);
      }

      if (this.internalField1056 >= 0) {
         this.internalMethod08958(this.internalField1056);
      }

      this.internalField0022 = null;
      this.internalField1053 = -1;
      this.internalField1055 = 0;
      this.internalField1056 = -1;
      this.internalField0277 = false;
   }

   private boolean internalMethod04481(GuiMoveModule localValue1) {
      if (localValue1 == null || !localValue1.isEnabled()) {
         return true;
      } else {
         return localValue1.internalMethod09771()
            ? !localValue1.internalMethod09603() && !localValue1.internalMethod10016() && localValue1.internalMethod09598()
            : localValue1.internalMethod06994().isEmpty() && !localValue1.internalMethod10016();
      }
   }

   private void internalMethod08958(int localValue1) {
      if (internalField0149.player.getInventory().getSelectedSlot() != localValue1) {
         internalField0149.player.getInventory().setSelectedSlot(localValue1);
         internalField0149.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(localValue1));
      }
   }

   private InventorySlot internalMethod07618() {
      InventorySlot localValue1 = InventorySlots.internalMethod02872().internalMethod03297(AutoThrowModule::internalMethod07217);
      return (InventorySlot)(localValue1 != null ? localValue1 : InventorySlots.internalMethod03558().internalMethod03297(AutoThrowModule::internalMethod07217));
   }

   private boolean internalMethod00794(LivingEntity localValue1) {
      if (localValue1.isUsingItem() && localValue1.getItemUseTime() >= 10) {
         ItemStack localValue2 = localValue1.getActiveItem();
         return !(localValue2.getItem() instanceof SplashPotionItem) && PotionItemUtils.internalMethod05184(localValue2, StatusEffects.INSTANT_HEALTH);
      } else {
         return false;
      }
   }

   private boolean internalMethod01824(LivingEntity localValue1) {
      Rotation localValue2 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
      Vec3d localValue3 = internalField0149.player.getEyePos();
      Vec3d localValue4 = localValue3.add(
         MathUtils.internalMethod06554(localValue2.internalMethod00171(), localValue2.internalMethod00169()).multiply(this.internalField0382.internalMethod08576() + 1.0F)
      );
      Box localValue5 = localValue1.getBoundingBox().expand(0.15);
      return localValue5.contains(localValue3) || localValue5.raycast(localValue3, localValue4).isPresent();
   }

   private static boolean internalMethod07217(ItemStack localValue0) {
      return !localValue0.isEmpty() && localValue0.getItem() instanceof SplashPotionItem && PotionItemUtils.internalMethod05184(localValue0, StatusEffects.INSTANT_HEALTH);
   }

   @Override
   public void onEnable() {
      this.internalField0519.internalMethod02364(0L);
      super.onEnable();
   }

   @Override
   public void onDisable() {
      if (this.internalField1053 >= 0) {
         this.internalMethod09164();
      }

      super.onDisable();
   }
}
