package rockstar.modules.player;



import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.*;
import rockstar.modules.combat.AuraModule;
import rockstar.client.module.Module;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Generated;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

@ModuleInfo(
   name = "No Interact",
   category = ModuleCategory.PLAYER
)
public class NoInteractModule extends Module {
   RegistryListSetting internalField0649;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;

   public NoInteractModule() {
      this.internalMethod09828();
   }

   private void internalMethod09828() {
      this.internalField0649 = new RegistryListSetting(this, "modules.settings.no_interact.blocks")
         .internalMethod01531(
            Blocks.CRAFTING_TABLE,
            Blocks.ENCHANTING_TABLE,
            Blocks.RED_BED,
            Blocks.CHEST,
            Blocks.ENDER_CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.FURNACE,
            Blocks.BARREL,
            Blocks.SHULKER_BOX,
            Blocks.DROPPER,
            Blocks.DISPENSER,
            Blocks.HOPPER,
            Blocks.ANVIL,
            Blocks.CAULDRON,
            Blocks.OAK_SIGN,
            Blocks.BELL,
            Blocks.COMPOSTER,
            Blocks.BREWING_STAND,
            Blocks.JUKEBOX,
            Blocks.COMMAND_BLOCK,
            Blocks.BEACON,
            Blocks.RESPAWN_ANCHOR,
            Blocks.GRINDSTONE,
            Blocks.LECTERN,
            Blocks.CARTOGRAPHY_TABLE,
            Blocks.LOOM,
            Blocks.COBWEB,
            Blocks.TRIPWIRE,
            Blocks.SMITHING_TABLE,
            Blocks.STONECUTTER,
            Blocks.PLAYER_HEAD,
            Blocks.OAK_TRAPDOOR
         )
         .internalMethod02504(Items.ARMOR_STAND)
         .internalMethod02504(Items.MINECART)
         .internalMethod02504(Items.CHEST_MINECART)
         .internalMethod02504(Items.FURNACE_MINECART)
         .internalMethod02504(Items.TNT_MINECART)
         .internalMethod02504(Items.HOPPER_MINECART)
         .internalMethod02504(Items.COMMAND_BLOCK_MINECART);
      this.internalField0650 = new BooleanSetting(this, "modules.settings.no_interact.onlyAura");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.no_interact.onlyUtilityItems");
      this.internalField1261 = new BooleanSetting(this, "\u0410\u0440\u043c\u043e\u0440 \u0441\u0442\u0435\u043d\u0434", () -> true) {
         @Override
         public JsonElement toJson() {
            return new JsonPrimitive(false);
         }

         @Override
         public void fromJson(JsonElement localValue1) {
            super.fromJson(localValue1);
            if (this.internalMethod04496()) {
               NoInteractModule.this.internalField0649.internalMethod02288(Registries.ITEM.getId(Items.ARMOR_STAND));
            }
         }
      };
   }

   public boolean internalMethod00044(Block localValue1, ItemStack localValue2) {
      if (!this.internalMethod06249(localValue2)) {
         return false;
      } else if (localValue1 instanceof CraftingTableBlock && this.internalField0649.internalMethod05586(Blocks.CRAFTING_TABLE)) {
         return true;
      } else if (localValue1 instanceof EnchantingTableBlock && this.internalField0649.internalMethod05586(Blocks.ENCHANTING_TABLE)) {
         return true;
      } else if (localValue1 instanceof BedBlock && this.internalField0649.internalMethod05586(Blocks.RED_BED)) {
         return true;
      } else if (localValue1 instanceof TrapdoorBlock && this.internalField0649.internalMethod05586(Blocks.OAK_TRAPDOOR)) {
         return true;
      } else if (localValue1 instanceof ChestBlock && this.internalField0649.internalMethod05586(Blocks.CHEST)) {
         return true;
      } else if (localValue1 instanceof EnderChestBlock && this.internalField0649.internalMethod05586(Blocks.ENDER_CHEST)) {
         return true;
      } else if (localValue1 instanceof TrappedChestBlock && this.internalField0649.internalMethod05586(Blocks.TRAPPED_CHEST)) {
         return true;
      } else if (localValue1 instanceof AbstractFurnaceBlock && this.internalField0649.internalMethod05586(Blocks.FURNACE)) {
         return true;
      } else if (localValue1 instanceof BarrelBlock && this.internalField0649.internalMethod05586(Blocks.BARREL)) {
         return true;
      } else if (localValue1 instanceof ShulkerBoxBlock && this.internalField0649.internalMethod05586(Blocks.SHULKER_BOX)) {
         return true;
      } else if (localValue1 instanceof DropperBlock && this.internalField0649.internalMethod05586(Blocks.DROPPER)) {
         return true;
      } else if (localValue1 instanceof DispenserBlock && this.internalField0649.internalMethod05586(Blocks.DISPENSER)) {
         return true;
      } else if (localValue1 instanceof HopperBlock && this.internalField0649.internalMethod05586(Blocks.HOPPER)) {
         return true;
      } else if (localValue1 instanceof AnvilBlock && this.internalField0649.internalMethod05586(Blocks.ANVIL)) {
         return true;
      } else if (localValue1 instanceof AbstractCauldronBlock && this.internalField0649.internalMethod05586(Blocks.CAULDRON)) {
         return true;
      } else if (localValue1 instanceof AbstractSignBlock && this.internalField0649.internalMethod05586(Blocks.OAK_SIGN)) {
         return true;
      } else if (localValue1 instanceof BellBlock && this.internalField0649.internalMethod05586(Blocks.BELL)) {
         return true;
      } else if (localValue1 instanceof ComposterBlock && this.internalField0649.internalMethod05586(Blocks.COMPOSTER)) {
         return true;
      } else if (localValue1 instanceof BrewingStandBlock && this.internalField0649.internalMethod05586(Blocks.BREWING_STAND)) {
         return true;
      } else if (localValue1 instanceof JukeboxBlock && this.internalField0649.internalMethod05586(Blocks.JUKEBOX)) {
         return true;
      } else if (localValue1 instanceof CommandBlock && this.internalField0649.internalMethod05586(Blocks.COMMAND_BLOCK)) {
         return true;
      } else if (localValue1 instanceof BeaconBlock && this.internalField0649.internalMethod05586(Blocks.BEACON)) {
         return true;
      } else if (localValue1 instanceof RespawnAnchorBlock && this.internalField0649.internalMethod05586(Blocks.RESPAWN_ANCHOR)) {
         return true;
      } else if (localValue1 instanceof GrindstoneBlock && this.internalField0649.internalMethod05586(Blocks.GRINDSTONE)) {
         return true;
      } else if (localValue1 instanceof LecternBlock && this.internalField0649.internalMethod05586(Blocks.LECTERN)) {
         return true;
      } else if (localValue1 instanceof CobwebBlock && this.internalField0649.internalMethod05586(Blocks.COBWEB)) {
         return true;
      } else if (localValue1 instanceof TripwireBlock && this.internalField0649.internalMethod05586(Blocks.TRIPWIRE)) {
         return true;
      } else if (localValue1 instanceof CartographyTableBlock && this.internalField0649.internalMethod05586(Blocks.CARTOGRAPHY_TABLE)) {
         return true;
      } else if (localValue1 instanceof LoomBlock && this.internalField0649.internalMethod05586(Blocks.LOOM)) {
         return true;
      } else {
         return localValue1 instanceof SmithingTableBlock && this.internalField0649.internalMethod05586(Blocks.SMITHING_TABLE)
            ? true
            : localValue1 instanceof StonecutterBlock && this.internalField0649.internalMethod05586(Blocks.STONECUTTER);
      }
   }

   public boolean internalMethod00145(Entity localValue1, ItemStack localValue2) {
      if (!this.internalMethod06249(localValue2)) {
         return false;
      } else if (localValue1 instanceof ArmorStandEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.ARMOR_STAND))) {
         return true;
      } else if (localValue1 instanceof MinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.MINECART))) {
         return true;
      } else if (localValue1 instanceof ChestMinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.CHEST_MINECART))) {
         return true;
      } else if (localValue1 instanceof FurnaceMinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.FURNACE_MINECART))) {
         return true;
      } else if (localValue1 instanceof TntMinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.TNT_MINECART))) {
         return true;
      } else {
         return localValue1 instanceof HopperMinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.HOPPER_MINECART))
            ? true
            : localValue1 instanceof CommandBlockMinecartEntity && this.internalField0649.internalMethod05933(Registries.ITEM.getId(Items.COMMAND_BLOCK_MINECART));
      }
   }

   public boolean internalMethod01109(ItemStack localValue1) {
      if (!this.internalMethod06249(localValue1)) {
         return false;
      } else {
         if (localValue1.getItem() instanceof BlockItem localValue2) {
            if (localValue2.getBlock() instanceof SkullBlock && this.internalField0649.internalMethod05586(Blocks.PLAYER_HEAD)) {
               return true;
            }

            if (localValue2.getBlock() instanceof CobwebBlock && this.internalField0649.internalMethod05586(Blocks.COBWEB)) {
               return true;
            }

            if (localValue2.getBlock() instanceof TripwireBlock && this.internalField0649.internalMethod05586(Blocks.TRIPWIRE)) {
               return true;
            }
         }

         return localValue1.getItem() == Items.STRING && this.internalField0649.internalMethod05586(Blocks.TRIPWIRE);
      }
   }

   private boolean internalMethod06249(ItemStack localValue1) {
      return this.internalField0650.internalMethod04496()
            && !RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).isEnabled()
         ? false
         : !this.internalField0651.internalMethod04496() || this.internalMethod08076(localValue1);
   }

   private boolean internalMethod08076(ItemStack localValue1) {
      return localValue1 != null && !localValue1.isEmpty()
         ? localValue1.isOf(Items.ENDER_PEARL)
            || localValue1.isOf(Items.CHORUS_FRUIT)
            || localValue1.isOf(Items.FIREWORK_ROCKET)
            || localValue1.isOf(Items.WIND_CHARGE)
            || localValue1.isOf(Items.EXPERIENCE_BOTTLE)
            || localValue1.isOf(Items.POTION)
            || localValue1.isOf(Items.SPLASH_POTION)
            || localValue1.isOf(Items.LINGERING_POTION)
            || localValue1.isOf(Items.SNOWBALL)
            || localValue1.isOf(Items.EGG)
         : false;
   }

   @Generated
   public RegistryListSetting internalMethod00395() {
      return this.internalField0649;
   }

   @Generated
   public BooleanSetting internalMethod00396() {
      return this.internalField0650;
   }
}
