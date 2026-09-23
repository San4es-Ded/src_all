package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent.Builder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import pyrock.utility.render.ColorRGBA;

public class ItemSettingsPopup {
   private final List<ItemSettingsElement> internalField0416;
   private final ItemStack internalField0878;
   private float internalField0205;
   private float internalField0206;
   private float internalField1048 = 120.0F;
   private boolean internalField0277;

   public static ItemSettingsPopup.InternalType0295 internalMethod07568() {
      return new ItemSettingsPopup.InternalType0295();
   }

   public void internalMethod01415(float localValue1, float localValue2) {
      this.internalField0205 = localValue1;
      this.internalField0206 = localValue2;
      this.internalField1048 = this.internalMethod08039();
      this.internalField0277 = true;
   }

   public void internalMethod04060() {
      this.internalField0277 = false;
   }

   public void internalMethod05666(UiRenderContext localValue1) {
      if (this.internalField0277) {
         this.internalMethod01200(localValue1, this.internalField0205, this.internalField0206, this.internalField1048, this.internalMethod08030());
         float localValue2 = this.internalField0206 + 6.0F;
         localValue1.drawText(
            Fonts.internalField0449.internalMethod01432(7.0F),
            "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430",
            this.internalField0205 + 6.0F,
            localValue2,
            ColorRGBA.WHITE
         );
         localValue2 += 12.0F;
         if (this.internalField0878 != null && !this.internalField0878.isEmpty()) {
            this.internalMethod04914(localValue1, this.internalField0205 + 4.0F, localValue2, this.internalField1048 - 8.0F);
            localValue2 += 24.0F;
         }

         for (ItemSettingsElement localValue4 : this.internalField0416) {
            localValue4.internalMethod05576(localValue1, this.internalField0205 + 6.0F, localValue2, this.internalField1048 - 12.0F);
            localValue2 += localValue4.internalMethod05812() + 4.0F;
         }
      }
   }

   private void internalMethod04914(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4) {
      localValue1.drawRoundedRect(localValue2, localValue3, localValue4, 20.0F, CornerRadii.internalMethod03908(4.0F), ThemeColors.internalMethod08573());
      localValue1.getMatrices().pushMatrix();
      localValue1.getMatrices().translate(0.0F, 0.0F);
      localValue1.drawBatchItem(this.internalField0878, localValue2 + 4.0F, localValue3 + 4.0F, 0.75F);
      localValue1.getMatrices().popMatrix();
      localValue1.drawText(
         Fonts.internalField0449.internalMethod01432(7.0F),
         ItemNbtUtils.internalMethod02181(this.internalField0878),
         localValue2 + 22.0F,
         localValue3 + 8.0F,
         ThemeColors.internalMethod08459()
      );
   }

   public boolean internalMethod07328(double localValue1, double localValue3, int localValue5) {
      if (!this.internalField0277) {
         return false;
      } else if (!(localValue1 < this.internalField0205)
         && !(localValue1 > this.internalField0205 + this.internalField1048)
         && !(localValue3 < this.internalField0206)
         && !(localValue3 > this.internalField0206 + this.internalMethod08030())) {
         float localValue6 = this.internalField0206 + 6.0F + 12.0F;
         if (this.internalField0878 != null && !this.internalField0878.isEmpty()) {
            localValue6 += 24.0F;
         }

         for (ItemSettingsElement localValue8 : this.internalField0416) {
            if (localValue3 >= localValue6 && localValue3 < localValue6 + localValue8.internalMethod05812()) {
               return localValue8.internalMethod06574(this, localValue1, localValue3, localValue5);
            }

            localValue6 += localValue8.internalMethod05812() + 4.0F;
         }

         return true;
      } else {
         this.internalMethod04060();
         return true;
      }
   }

   public void internalMethod04068() {
      for (ItemSettingsElement localValue2 : this.internalField0416) {
         if (localValue2 instanceof ItemSettingsSliderRow localValue3) {
            localValue3.internalMethod00813();
         }
      }
   }

   public void internalMethod01756(double localValue1) {
      for (ItemSettingsElement localValue4 : this.internalField0416) {
         if (localValue4 instanceof ItemSettingsCategory localValue5 && localValue5.internalMethod02650()) {
            localValue5.internalMethod06557(localValue1);
         }
      }
   }

   public void internalMethod08029() {
      for (ItemSettingsElement localValue2 : this.internalField0416) {
         if (localValue2 instanceof ItemSettingsCategory localValue3) {
            for (ItemSettingsElement localValue5 : localValue3.internalMethod06935()) {
               if (localValue5 instanceof EnchantLevelRow localValue6) {
                  localValue6.internalMethod00942();
               }

               if (localValue5 instanceof EffectLevelDurationRow localValue7) {
                  localValue7.internalMethod00942();
               }
            }
         }
      }
   }

   private float internalMethod08030() {
      float localValue1 = 24.0F;
      if (this.internalField0878 != null && !this.internalField0878.isEmpty()) {
         localValue1 += 24.0F;
      }

      for (ItemSettingsElement localValue3 : this.internalField0416) {
         localValue1 += localValue3.internalMethod05812() + 4.0F;
      }

      return localValue1 - 4.0F;
   }

   private float internalMethod08039() {
      float localValue1 = Fonts.internalField0449
            .internalMethod01432(7.0F)
            .internalMethod00965("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430")
         + 8.0F;
      float localValue2 = 0.0F;
      if (this.internalField0878 != null && !this.internalField0878.isEmpty()) {
         localValue2 = 22.0F
            + Fonts.internalField0449.internalMethod01432(7.0F).internalMethod00965(ItemNbtUtils.internalMethod02181(this.internalField0878))
            + 14.0F;
      }

      return Math.max(120.0F, Math.max(localValue1, localValue2));
   }

   public List<RoundedTextFieldWidget> internalMethod03792() {
      ArrayList localValue1 = new ArrayList();

      for (ItemSettingsElement localValue3 : this.internalField0416) {
         if (localValue3 instanceof EnchantLevelRow localValue4) {
            localValue1.add(localValue4.internalMethod01113());
         }

         if (localValue3 instanceof ItemSettingsCategory localValue8) {
            for (ItemSettingsElement localValue6 : localValue8.internalMethod06935()) {
               if (localValue6 instanceof EnchantLevelRow localValue7) {
                  localValue1.add(localValue7.internalMethod01113());
               }

               if (localValue6 instanceof EffectLevelDurationRow localValue9) {
                  localValue1.add(localValue9.internalMethod03675());
                  localValue1.add(localValue9.internalMethod05066());
               }
            }
         }
      }

      return localValue1;
   }

   private void internalMethod01200(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, 45.0F, 5.0F, CornerRadii.internalMethod03908(6.0F), ColorRGBA.WHITE.withAlpha(255.0F));
      localValue1.drawSquircle(
         localValue2,
         localValue3,
         localValue4,
         localValue5,
         2.0F,
         CornerRadii.internalMethod03908(6.0F),
         ThemeColors.internalMethod07738().withAlpha(255.0F * ThemeColors.internalMethod02435().internalMethod08704())
      );
   }

   @Generated
   public List<ItemSettingsElement> internalMethod01462() {
      return this.internalField0416;
   }

   @Generated
   public ItemStack internalMethod05646() {
      return this.internalField0878;
   }

   @Generated
   public float internalMethod04059() {
      return this.internalField0205;
   }

   @Generated
   public float internalMethod04067() {
      return this.internalField0206;
   }

   @Generated
   public float internalMethod08028() {
      return this.internalField1048;
   }

   @Generated
   public boolean internalMethod04061() {
      return this.internalField0277;
   }

   @Generated
   public ItemSettingsPopup(List<ItemSettingsElement> localValue1, ItemStack localValue2) {
      this.internalField0416 = localValue1;
      this.internalField0878 = localValue2;
   }

   public static class InternalType0026 implements MinecraftClientAccess {
      private final ItemStack internalField0878;

      public Optional<ItemSettingsCategory> internalMethod04142() {
         List localValue1 = this.internalMethod02447();
         if (localValue1.isEmpty()) {
            return Optional.empty();
         } else {
            ItemSettingsCategory localValue2 = new ItemSettingsCategory("\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u044f");

            for (RegistryEntry localValue4 : (Iterable<RegistryEntry>)(Iterable<?>)localValue1) {
               String localValue5 = ((Enchantment)localValue4.value()).description().getString();
               int localValue6 = ((Enchantment)localValue4.value()).getMaxLevel();
               int localValue7 = EnchantmentUtils.internalMethod03526(this.internalField0878, (RegistryKey<Enchantment>)localValue4.getKey().get());
               localValue2.internalMethod05394(new EnchantLevelRow(localValue5, localValue6, localValue7, localValue2x -> this.internalMethod06215(localValue4, localValue2x)));
            }

            return Optional.of(localValue2);
         }
      }

      private List<Reference<Enchantment>> internalMethod02447() {
         return internalField0149.world
            .getRegistryManager()
            .getOrThrow(RegistryKeys.ENCHANTMENT)
            .streamEntries()
            .filter(localValue1 -> ((Enchantment)localValue1.value()).isAcceptableItem(this.internalField0878))
            .toList();
      }

      private void internalMethod06215(RegistryEntry<Enchantment> localValue1, int localValue2) {
         Builder localValue3 = new Builder(
            (ItemEnchantmentsComponent)this.internalField0878.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
         );
         if (localValue2 <= 0) {
            localValue3.remove(localValue1x -> localValue1x.equals(localValue1));
         } else {
            localValue3.set(localValue1, localValue2);
         }

         this.internalField0878.set(DataComponentTypes.ENCHANTMENTS, localValue3.build());
      }

      @Generated
      public InternalType0026(ItemStack localValue1) {
         this.internalField0878 = localValue1;
      }
   }

   public static class InternalType0295 implements MinecraftClientAccess {
      private final List<ItemSettingsElement> internalField0416 = new ArrayList<>();
      private ItemStack internalField0878;

      public ItemSettingsPopup.InternalType0295 internalMethod01352(ItemStack localValue1) {
         this.internalField0878 = localValue1;
         return this;
      }

      public ItemSettingsPopup.InternalType0295 internalMethod02196(String localValue1, Consumer<ItemSettingsPopup> localValue2) {
         this.internalField0416.add(new ItemSettingsButtonRow(localValue1, ThemeColors.internalMethod08459(), localValue2));
         return this;
      }

      public ItemSettingsPopup.InternalType0295 internalMethod01017(String localValue1, ColorRGBA localValue2, Consumer<ItemSettingsPopup> localValue3) {
         this.internalField0416.add(new ItemSettingsButtonRow(localValue1, localValue2, localValue3));
         return this;
      }

      public ItemSettingsPopup.InternalType0295 internalMethod03796(ItemStack localValue1) {
         new ItemSettingsPopup.InternalType0026(localValue1).internalMethod04142().ifPresent(this.internalField0416::add);
         return this;
      }

      public ItemSettingsPopup.InternalType0295 internalMethod08810(ItemStack localValue1) {
         new ItemSettingsPopup.InternalType0296(localValue1).internalMethod05272().ifPresent(this.internalField0416::add);
         return this;
      }

      public ItemSettingsPopup.InternalType0295 internalMethod07194(String localValue1, int localValue2, int localValue3, int localValue4, Consumer<Integer> localValue5) {
         this.internalField0416.add(new ItemSettingsSliderRow(localValue1, localValue2, localValue3, localValue4, localValue5));
         return this;
      }

      public ItemSettingsPopup internalMethod06231() {
         return new ItemSettingsPopup(new ArrayList<>(this.internalField0416), this.internalField0878);
      }
   }

   public static class InternalType0296 implements MinecraftClientAccess {
      private final ItemStack internalField0878;

      public Optional<ItemSettingsCategory> internalMethod05272() {
         if (!this.internalMethod02585()) {
            return Optional.empty();
         } else {
            ItemSettingsCategory localValue1 = new ItemSettingsCategory("\u042d\u0444\u0444\u0435\u043a\u0442\u044b", new ItemStack(Items.POTION));
            PotionContentsComponent localValue2 = (PotionContentsComponent)this.internalField0878
               .getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);

            for (RegistryEntry localValue4 : Registries.STATUS_EFFECT.getIndexedEntries()) {
               String localValue5 = ((StatusEffect)localValue4.value()).getName().getString();
               StatusEffectInstance localValue6 = this.internalMethod03696(localValue2, localValue4);
               int localValue7 = localValue6 != null ? localValue6.getAmplifier() : -1;
               int localValue8 = localValue6 != null ? localValue6.getDuration() : 0;
               localValue1.internalMethod05394(new EffectLevelDurationRow(localValue5, localValue4, localValue7, localValue8, (localValue2x, localValue3) -> this.internalMethod00846(localValue4, localValue2x, localValue3)));
            }

            return Optional.of(localValue1);
         }
      }

      private StatusEffectInstance internalMethod03696(PotionContentsComponent localValue1, RegistryEntry<StatusEffect> localValue2) {
         for (StatusEffectInstance localValue4 : localValue1.getEffects()) {
            if (localValue4.getEffectType().equals(localValue2)) {
               return localValue4;
            }
         }

         return null;
      }

      private boolean internalMethod02585() {
         return this.internalField0878.isOf(Items.POTION)
            || this.internalField0878.isOf(Items.SPLASH_POTION)
            || this.internalField0878.isOf(Items.LINGERING_POTION)
            || this.internalField0878.isOf(Items.TIPPED_ARROW);
      }

      private void internalMethod00846(RegistryEntry<StatusEffect> localValue1, int localValue2, int localValue3) {
         PotionContentsComponent localValue4 = (PotionContentsComponent)this.internalField0878
            .getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
         ArrayList localValue5 = new ArrayList();

         for (StatusEffectInstance localValue7 : localValue4.getEffects()) {
            if (!localValue7.getEffectType().equals(localValue1)) {
               localValue5.add(localValue7);
            }
         }

         if (localValue2 >= 0 && localValue3 > 0) {
            localValue5.add(new StatusEffectInstance(localValue1, localValue3, localValue2));
         }

         PotionContentsComponent localValue9 = PotionContentsComponent.DEFAULT;

         for (StatusEffectInstance localValue8 : (Iterable<StatusEffectInstance>)(Iterable<?>)localValue5) {
            localValue9 = localValue9.with(localValue8);
         }

         this.internalField0878.set(DataComponentTypes.POTION_CONTENTS, localValue9);
      }

      @Generated
      public InternalType0296(ItemStack localValue1) {
         this.internalField0878 = localValue1;
      }
   }
}
