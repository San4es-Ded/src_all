package rockstar.modules.player;






import rockstar.client.util.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.List;
import java.util.Set;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import pyrock.events.game.FinishEatEvent;
import pyrock.events.network.ReceivePacketEvent;

@ModuleInfo(
   name = "Tracker",
   category = ModuleCategory.PLAYER
)
public class TrackerModule extends Module {
   private static final Set<Item> internalField0546 = Set.of(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.CHORUS_FRUIT, Items.POTION);
   private final EventListener<FinishEatEvent> internalField0157 = localValue1 -> {
      ItemStack localValue2 = localValue1.getStack();
      if (internalField0546.contains(localValue2.getItem()) && localValue1.getUser() != internalField0149.player) {
         String localValue3 = localValue2.getItem() == Items.POTION ? this.internalMethod01480(localValue2) : localValue2.getName().getString();
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod02784(
               new ItemNotification(
                     localValue1.getUser().getName().getString()
                        + (localValue2.getItem() == Items.POTION ? " \u0432\u044b\u043f\u0438\u043b " : " \u0441\u044a\u0435\u043b ")
                        + localValue3,
                     localValue2
                  )
                  .internalMethod03390(localValue3)
            );
      }
   };
   private final EventListener<ReceivePacketEvent> internalField0158 = localValue0 -> {
      if (localValue0.getPacket() instanceof EntityStatusS2CPacket localValue1 && localValue1.getStatus() == 35) {
         Entity localValue8 = localValue1.getEntity(internalField0149.world);
         if (localValue8 == null || localValue8 == internalField0149.player || !(localValue8 instanceof PlayerEntity localValue3)) {
            return;
         }

         ItemStack localValue4 = localValue3.getOffHandStack().contains(DataComponentTypes.DEATH_PROTECTION)
            ? localValue3.getOffHandStack().copy()
            : (localValue3.getMainHandStack().contains(DataComponentTypes.DEATH_PROTECTION) ? localValue3.getMainHandStack().copy() : new ItemStack(Items.TOTEM_OF_UNDYING));
         CustomItemUtils.InternalType0254 localValue5 = CustomItemUtils.internalMethod03238(localValue4);
         String localValue6 = localValue5 != null ? localValue5.internalMethod00671(localValue4) : localValue4.getName().getString();
         ItemNotification localValue7 = new ItemNotification(localValue3.getName().getString() + " \u043f\u043e\u0442\u0435\u0440\u044f\u043b " + localValue6, localValue4)
            .internalMethod03390(localValue6);
         if (localValue5 != null) {
            localValue7.internalMethod05942(localValue5.internalMethod01667(localValue4));
         }

         RockstarClient.getInstance().internalMethod02503().internalMethod02784(localValue7);
      }
   };

   private String internalMethod01480(ItemStack localValue1) {
      List localValue2 = PotionItemUtils.internalMethod00984(localValue1);
      return localValue2.isEmpty()
         ? "\u0417\u0435\u043b\u044c\u0435"
         : "\u0417\u0435\u043b\u044c\u0435 "
            + ((StatusEffect)((StatusEffectInstance)localValue2.getFirst()).getEffectType().value()).getName().getString().toLowerCase();
   }
}
