package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.inventory.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.List;
import lombok.Generated;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pyrock.utility.render.ColorRGBA;

public final class ElytraUtils implements MinecraftClientAccess {
   private static Vec3d internalField0283;
   private static final Stopwatch internalField0519 = new Stopwatch();

   public static void internalMethod07049(boolean localValue0) {
      SlotCollection localValue1 = InventorySlots.internalMethod02872();
      HotbarSlot localValue2 = localValue0
         ? (HotbarSlot)localValue1.internalMethod03297(
            localValue0x -> LegacyItemTypes.getArmorSlot((ItemStack)localValue0x) == net.minecraft.entity.EquipmentSlot.CHEST
         )
         : (HotbarSlot)localValue1.internalMethod02510(Items.ELYTRA);
      if (localValue2 != null) {
         HotbarSlot localValue3 = InventoryUtils.internalMethod06160();
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue2.internalMethod08745()));
         InventoryUtils.internalMethod01980(localValue2);
         internalField0149.interactionManager.interactItem(internalField0149.player, Hand.MAIN_HAND);
         ((Slot)internalField0149.player.currentScreenHandler.slots.get(6)).setStack(new ItemStack(localValue0 ? Items.NETHERITE_CHESTPLATE : Items.ELYTRA));
         InventoryUtils.internalMethod01980(localValue3);
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(internalField0149.player.getInventory().getSelectedSlot()));
      }
   }

   public static void internalMethod00696(MatrixStack localValue0, BufferBuilder localValue1, Box localValue2, ColorRGBA localValue3) {
      Render3DUtils.internalMethod08795(localValue0, localValue1, localValue2, localValue3);
      Render3DUtils.internalMethod09146(localValue0, localValue1, localValue2, localValue3);
   }

   public static void internalMethod07048(float localValue0) {
      SlotCollection localValue1 = InventorySlots.internalMethod02872();
      HotbarSlot localValue2 = (HotbarSlot)localValue1.internalMethod02510(Items.FIREWORK_ROCKET);
      if (localValue2 != null) {
         LivingEntity localValue5 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
         Rotation localValue6 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue2.internalMethod08745()));
         internalField0149.interactionManager
            .sendSequencedPacket(
               internalField0149.world, localValue1x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, localValue1x, localValue6.internalMethod00169(), localValue6.internalMethod00171())
            );
         internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(internalField0149.player.getInventory().getSelectedSlot()));
         internalField0519.internalMethod00701();
      } else {
         SlotCollection localValue3 = InventorySlots.internalMethod03558();
         MainInventorySlot localValue4 = (MainInventorySlot)localValue3.internalMethod02510(Items.FIREWORK_ROCKET);
         if (localValue4 != null) {
            InventoryUtils.internalMethod08821(localValue4.internalMethod06662(), (int)(localValue0 - 1.0F));
            internalField0519.internalMethod00701();
         }
      }
   }

   public static Vec3d internalMethod00261(LivingEntity localValue0) {
      return RotationUtils.internalMethod01839(
         localValue0,
         RockstarClient.getInstance().getModuleManager().getModule(ElytraTargetModule.class).isEnabled() && localValue0 instanceof PlayerEntity localValue1
            ? PlayerMotionPredictor.internalMethod03269(localValue1)
            : localValue0.getEntityPos()
      );
   }

   public static Vec3d internalMethod01674(LivingEntity localValue0) {
      List localValue1 = List.of(
         new Vec3d(0.0, 20.0, 0.0),
         new Vec3d(0.0, -20.0, 0.0),
         new Vec3d(20.0, 0.0, 0.0),
         new Vec3d(-20.0, 0.0, 0.0),
         new Vec3d(0.0, 0.0, 20.0),
         new Vec3d(0.0, 0.0, -20.0)
      );
      if (CombatUtils.internalMethod06028() != null) {
         localValue1 = List.of(new Vec3d(0.0, 20.0, 0.0));
      }

      Vec3d localValue2 = Vec3d.ZERO;

      for (Vec3d localValue4 : (Iterable<Vec3d>)(Iterable<?>)localValue1) {
         if (MathUtils.internalMethod06610(localValue0.getEyePos().add(localValue4)) && !localValue4.equals(internalField0283)) {
            localValue2 = localValue4;
            break;
         }
      }

      return localValue2;
   }

   public static float[] internalMethod05851(float localValue0) {
      float localValue1 = localValue0 - localValue0 % 360.0F;
      float localValue2 = localValue0 % 360.0F;
      if (localValue2 < 0.0F) {
         localValue2 += 360.0F;
         localValue1 -= 360.0F;
      }

      float localValue3 = Math.round(localValue2 / 45.0F) * 45.0F;
      float localValue4;
      float localValue5;
      if (localValue3 % 90.0F == 0.0F) {
         float localValue6 = (localValue3 - 45.0F) % 360.0F;
         float localValue7 = (localValue3 + 45.0F) % 360.0F;
         localValue4 = localValue6 < localValue2 ? localValue6 : localValue6 - 45.0F;
         localValue5 = localValue7 > localValue2 ? localValue7 : localValue7 + 45.0F;
      } else if (localValue3 < localValue2) {
         localValue4 = localValue3;
         localValue5 = localValue3 + 90.0F;
      } else {
         localValue4 = localValue3 - 90.0F;
         localValue5 = localValue3;
      }

      localValue4 += localValue1;
      localValue5 += localValue1;
      return new float[]{localValue4, localValue5};
   }

   @Generated
   private ElytraUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static void internalMethod03028(Vec3d localValue0) {
      internalField0283 = localValue0;
   }

   @Generated
   public static Stopwatch internalMethod07135() {
      return internalField0519;
   }
}
