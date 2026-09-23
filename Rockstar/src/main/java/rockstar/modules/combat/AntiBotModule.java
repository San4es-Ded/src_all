package rockstar.modules.combat;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import moscow.rockstar.mixin.accessors.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;
import pyrock.events.game.WorldChangeEvent;

@ModuleInfo(
   name = "Anti Bot",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.anti_bot"
)
public class AntiBotModule extends Module {
   public static List<Entity> internalField0416 = new ArrayList<>();
   private BooleanSetting internalField0650;
   private final Map<Integer, PlayerEntity> internalField0543 = new HashMap<>();
   private final Set<Integer> internalField0546 = new HashSet<>();
   private final EventListener<WorldChangeEvent> internalField0157 = localValue1 -> this.internalMethod09486();

   public AntiBotModule() {
      this.internalMethod09761();
   }

   private void internalMethod09761() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.anti_bot.remove_from_world").internalMethod06630();
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.world != null && internalField0149.player != null) {
         this.internalMethod09762();

         for (PlayerEntity localValue2 : (Iterable<PlayerEntity>)(Iterable<?>)new ArrayList(internalField0149.world.getPlayers())) {
            if (internalField0149.player != localValue2 && !(localValue2 instanceof FakePlayerEntity)) {
               boolean localValue3 = this.internalMethod01551(localValue2);
               boolean localValue4 = this.internalMethod00632(localValue2).internalMethod05630();
               boolean localValue5 = this.internalMethod06995(localValue2);
               if (!localValue3 && !localValue4 && !localValue5) {
                  internalField0416.remove(localValue2);
               } else {
                  if (!internalField0416.contains(localValue2)) {
                     internalField0416.add(localValue2);
                  }

                  if (this.internalField0650.internalMethod04496() && !this.internalField0543.containsKey(localValue2.getId())) {
                     this.internalMethod05888(localValue2, localValue5);
                  }
               }
            }
         }
      }
   }

   private void internalMethod05888(PlayerEntity localValue1, boolean localValue2) {
      this.internalField0543.put(localValue1.getId(), localValue1);
      if (localValue2) {
         this.internalField0546.add(localValue1.getId());
      }

      assert internalField0149.world != null;

      internalField0149.world.removeEntity(localValue1.getId(), RemovalReason.DISCARDED);
   }

   private void internalMethod09762() {
      Iterator localValue1 = this.internalField0546.iterator();

      while (localValue1.hasNext()) {
         int localValue2 = (Integer)localValue1.next();
         PlayerEntity localValue3 = this.internalField0543.get(localValue2);
         if (localValue3 == null) {
            localValue1.remove();
         } else if (!(GameUtils.internalMethod02919(localValue3) <= 0.0F)) {
            ((EntityAccessor)(Object)localValue3).invokeUnsetRemoved();
            internalField0149.world.addEntity(localValue3);
            internalField0416.remove(localValue3);
            this.internalField0543.remove(localValue2);
            localValue1.remove();
         }
      }
   }

   private boolean internalMethod06995(PlayerEntity localValue1) {
      return localValue1 == null ? false : !(GameUtils.internalMethod02919(localValue1) > 0.0F);
   }

   private boolean internalMethod01551(PlayerEntity localValue1) {
      if (localValue1 == null) {
         return false;
      } else {
         String localValue2 = localValue1.getName().getString();
         UUID localValue3 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + localValue2).getBytes(StandardCharsets.UTF_8));
         boolean localValue4 = !localValue1.getUuid().equals(localValue3);
         boolean localValue5 = !localValue2.contains("NPC") && !localValue2.startsWith("[ZNPC]");
         return localValue4 && localValue5;
      }
   }

   private AntiBotModule.InternalType0250 internalMethod00632(PlayerEntity localValue1) {
      String localValue2 = localValue1.getName().getString();
      String localValue3 = localValue1.getDisplayName().getString();
      int localValue4 = localValue3.indexOf(localValue2);
      String localValue5 = localValue4 > 0 ? localValue3.substring(0, localValue4).trim() : "";
      return new AntiBotModule.InternalType0250(localValue3, localValue5, localValue5.isBlank());
   }

   public static boolean internalMethod07596(LivingEntity localValue0) {
      return localValue0 instanceof PlayerEntity && internalField0416.contains(localValue0);
   }

   @Override
   public void onDisable() {
      this.internalMethod09486();
      super.onDisable();
   }

   private void internalMethod09486() {
      internalField0416.clear();
      this.internalField0543.clear();
      this.internalField0546.clear();
   }

   static final class InternalType0250 {
      private final String internalField0248;
      private final String internalField0247;
      private final boolean internalField0277;

      InternalType0250(String localValue1, String localValue2, boolean localValue3) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField0277 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0250[fullTag=" + this.internalField0248 + ", prefix=" + this.internalField0247 + ", noPrefix=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AntiBotModule.InternalType0250 other = (AntiBotModule.InternalType0250) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public String internalMethod05533() {
         return this.internalField0248;
      }

      public String internalMethod02031() {
         return this.internalField0247;
      }

      public boolean internalMethod05630() {
         return this.internalField0277;
      }
   }
}
