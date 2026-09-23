package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AntiBot",
        category = Category.COMBAT,
        description = "Не атакует ботов античита"
)
public final class AntiBot extends Module {
   public static final AntiBot INSTANCE = new AntiBot();
   private final List<PlayerEntity> bots = new ArrayList();

   private AntiBot() {
   }

   @EventTarget
   public void onTick(EventUpdate event) {
      if (mc.player != null && mc.world != null) {
         this.bots.removeIf(bot -> bot == null || !bot.isAlive()
                 || !mc.world.getPlayers().contains(bot) || !this.isBotCandidate(bot));

         Iterator var2 = mc.world.getPlayers().iterator();

         while(var2.hasNext()) {
            PlayerEntity player = (PlayerEntity)var2.next();
            if (player != null && player != mc.player && this.isBotCandidate(player) && !this.bots.contains(player)) {
               this.bots.add(player);
            }
         }

      }
   }

   @FastNative
   private boolean armorCheck(PlayerEntity entity) {
      return this.getArmor(entity, 3).getItem() == Items.LEATHER_HELMET && this.isNotColored(entity, 3) && !this.getArmor(entity, 3).hasEnchantments() || this.getArmor(entity, 2).getItem() == Items.LEATHER_CHESTPLATE && this.isNotColored(entity, 2) && !this.getArmor(entity, 2).hasEnchantments() || this.getArmor(entity, 1).getItem() == Items.LEATHER_LEGGINGS && this.isNotColored(entity, 1) && !this.getArmor(entity, 1).hasEnchantments() || this.getArmor(entity, 0).getItem() == Items.LEATHER_BOOTS && this.isNotColored(entity, 0) && !this.getArmor(entity, 0).hasEnchantments() || this.getArmor(entity, 2).getItem() == Items.IRON_CHESTPLATE && !this.getArmor(entity, 2).hasEnchantments() || this.getArmor(entity, 1).getItem() == Items.IRON_LEGGINGS && !this.getArmor(entity, 1).hasEnchantments();
   }

   @FastNative
   private ItemStack getArmor(PlayerEntity entity, int slot) {
      return entity.getInventory().getArmorStack(slot);
   }

   @FastNative
   private boolean isNotColored(PlayerEntity entity, int slot) {
      return !this.getArmor(entity, slot).contains(DataComponentTypes.DYED_COLOR);
   }

   @FastNative
   private boolean isNaked(PlayerEntity entity) {
      for (ItemStack stack : entity.getArmorItems()) {
         if (!stack.isEmpty()) {
            return false;
         }
      }
      return true;
   }

   @FastNative
   private boolean isInPlayerList(PlayerEntity entity) {
      return mc.getNetworkHandler() != null
              && mc.getNetworkHandler().getPlayerListEntry(entity.getUuid()) != null;
   }

   @FastNative
   private boolean isBotCandidate(PlayerEntity entity) {
      // CakeWorld's flying decoys are invisible, have no armor and never show up
      // in the tab list. Без проверки таба под это правило попадал любой живой
      // игрок под невидимкой без брони, и аура переставала его бить.
      return (entity.isInvisible() && this.isNaked(entity) && !this.isInPlayerList(entity))
              || this.armorCheck(entity);
   }

   @FastNative
   public void onEnable() {
      super.onEnable();
      if (!this.bots.isEmpty()) {
         this.bots.clear();
      }

   }

   @FastNative
   public void onDisable() {
      super.onDisable();
      if (!this.bots.isEmpty()) {
         this.bots.clear();
      }

   }

   @FastNative
   public boolean isBot(PlayerEntity player) {
      if (player == null || player == mc.player) {
         return false;
      }

      if (this.isBotCandidate(player)) {
         if (!this.bots.contains(player)) {
            this.bots.add(player);
         }
         return true;
      }

      this.bots.remove(player);
      return false;
   }
}
