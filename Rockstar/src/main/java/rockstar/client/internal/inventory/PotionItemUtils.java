package rockstar.client.internal.inventory;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.registry.entry.RegistryEntry;

public final class PotionItemUtils {
   public static boolean internalMethod05184(ItemStack localValue0, RegistryEntry<StatusEffect> localValue1) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         if (!(localValue0.getItem() instanceof PotionItem)) {
            return false;
         } else {
            PotionContentsComponent localValue2 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
            if (localValue2 == null) {
               return false;
            } else {
               for (StatusEffectInstance localValue4 : localValue2.getEffects()) {
                  if (localValue4.getEffectType() == localValue1) {
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static List<StatusEffectInstance> internalMethod00984(ItemStack localValue0) {
      ArrayList localValue1 = new ArrayList();
      if (localValue0 == null || localValue0.isEmpty()) {
         return localValue1;
      } else if (!(localValue0.getItem() instanceof PotionItem)) {
         return localValue1;
      } else {
         PotionContentsComponent localValue2 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
         if (localValue2 == null) {
            return localValue1;
         } else {
            localValue2.getEffects().forEach(localValue1::add);
            return localValue1;
         }
      }
   }

   public static ItemStack internalMethod06437(ItemStack localValue0, int localValue1) {
      if (!(localValue0.getItem() instanceof PotionItem)) {
         return localValue0;
      } else {
         PotionContentsComponent localValue2 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
         if (localValue2 == null) {
            return localValue0;
         } else {
            ArrayList localValue3 = new ArrayList();

            for (StatusEffectInstance localValue5 : localValue2.getEffects()) {
               localValue3.add(
                  new StatusEffectInstance(localValue5.getEffectType(), localValue5.getDuration(), localValue1, localValue5.isAmbient(), localValue5.shouldShowParticles(), localValue5.shouldShowIcon())
               );
            }

            ItemStack localValue6 = localValue0.copy();
            localValue6.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(localValue2.potion(), localValue2.customColor(), localValue3, localValue2.customName()));
            return localValue6;
         }
      }
   }

   public static int internalMethod02700(ItemStack localValue0) {
      if (!(localValue0.getItem() instanceof PotionItem)) {
         return 0;
      } else {
         PotionContentsComponent localValue1 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
         if (localValue1 == null) {
            return 0;
         } else {
            Iterator localValue2 = localValue1.getEffects().iterator();
            if (localValue2.hasNext()) {
               StatusEffectInstance localValue3 = (StatusEffectInstance)localValue2.next();
               return localValue3.getAmplifier();
            } else {
               return 0;
            }
         }
      }
   }

   public static boolean internalMethod06478(ItemStack localValue0, ItemStack localValue1) {
      PotionContentsComponent localValue2 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
      PotionContentsComponent localValue3 = (PotionContentsComponent)localValue1.get(DataComponentTypes.POTION_CONTENTS);
      if (localValue2 != null && localValue3 != null) {
         Map localValue4 = internalMethod04680(localValue2);
         Map localValue5 = internalMethod04680(localValue3);

         for (Entry localValue7 : (Iterable<Entry>)(Iterable<?>)localValue4.entrySet()) {
            if (!localValue5.containsKey(localValue7.getKey())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static Map<PotionItemUtils.InternalType0221, Integer> internalMethod04680(PotionContentsComponent localValue0) {
      HashMap localValue1 = new HashMap();

      for (StatusEffectInstance localValue3 : localValue0.getEffects()) {
         localValue1.merge(PotionItemUtils.InternalType0221.internalMethod04961(localValue3), 1, (left, right) -> (Integer)left + (Integer)right);
      }

      return localValue1;
   }

   public static boolean internalMethod02701(ItemStack localValue0) {
      if (!(localValue0.getItem() instanceof PotionItem)) {
         return false;
      } else {
         PotionContentsComponent localValue1 = (PotionContentsComponent)localValue0.get(DataComponentTypes.POTION_CONTENTS);
         if (localValue1 == null) {
            return false;
         } else {
            for (StatusEffectInstance localValue3 : localValue1.getEffects()) {
               RegistryEntry localValue4 = localValue3.getEffectType();
               if (!localValue4.equals(StatusEffects.INVISIBILITY)
                  && !localValue4.equals(StatusEffects.NIGHT_VISION)
                  && !localValue4.equals(StatusEffects.WATER_BREATHING)
                  && !localValue4.equals(StatusEffects.FIRE_RESISTANCE)
                  && !localValue4.equals(StatusEffects.SLOW_FALLING)
                  && !localValue4.equals(StatusEffects.LUCK)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   @Generated
   private PotionItemUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static final class InternalType0221 {
      private final RegistryEntry<StatusEffect> internalField0151;
      private final int internalField0227;

      private InternalType0221(RegistryEntry<StatusEffect> localValue1, int localValue2) {
         this.internalField0151 = localValue1;
         this.internalField0227 = localValue2;
      }

      static PotionItemUtils.InternalType0221 internalMethod04961(StatusEffectInstance localValue0) {
         return new PotionItemUtils.InternalType0221(localValue0.getEffectType(), localValue0.getAmplifier());
      }

      @Override
      public final String toString() {
         return "InternalType0221[type=" + this.internalField0151 + ", amplifier=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0151);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PotionItemUtils.InternalType0221 other = (PotionItemUtils.InternalType0221) localValue1;
         return java.util.Objects.equals(this.internalField0151, other.internalField0151)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public RegistryEntry<StatusEffect> internalMethod01128() {
         return this.internalField0151;
      }

      public int internalMethod07113() {
         return this.internalField0227;
      }
   }
}
