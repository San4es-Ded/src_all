package rockstar.client.setting;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RegistryListSetting extends AbstractSetting {
   private final List<RegistryListSetting.InternalType0387> internalField0416 = new ArrayList<>();
   private final Map<Identifier, RegistryListSetting.InternalType0387> internalField0543 = new ConcurrentHashMap<>();
   private final LinkedHashSet<Identifier> internalField0500 = new LinkedHashSet<>();
   private final List<RegistryListSetting.InternalType0387> internalField0417 = new ArrayList<>();
   private String internalField0247 = "";
   private String internalField1077 = "";
   private Set<Identifier> internalField0546 = null;
   private final Map<Identifier, Item> internalField0544 = new LinkedHashMap<>();
   private boolean internalField0277;
   private Predicate<Block> internalField0486 = null;

   public RegistryListSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3, @NotNull BooleanSupplier localValue4) {
      super(localValue1, localValue2, localValue4);
      this.internalMethod02612();
   }

   public RegistryListSetting(@NotNull SettingOwner localValue1, String localValue2, @NotNull BooleanSupplier localValue3) {
      super(localValue1, localValue2, localValue3);
      this.internalMethod02612();
   }

   public RegistryListSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3) {
      super(localValue1, localValue2);
      this.internalMethod02612();
   }

   public RegistryListSetting(@NotNull SettingOwner localValue1, String localValue2) {
      super(localValue1, localValue2);
      this.internalMethod02612();
   }

   public RegistryListSetting internalMethod01531(Block... localValue1) {
      if (localValue1 != null && localValue1.length != 0) {
         this.internalField0546 = Arrays.stream(localValue1).<Identifier>map(Registries.BLOCK::getId).collect(Collectors.toCollection(LinkedHashSet::new));
      } else {
         this.internalField0546 = null;
      }

      this.internalField0486 = null;
      this.internalMethod02612();
      return this;
   }

   public RegistryListSetting internalMethod01370(Predicate<Block> localValue1) {
      this.internalField0486 = localValue1;
      this.internalField0546 = null;
      this.internalMethod02612();
      return this;
   }

   public RegistryListSetting internalMethod02278(Block localValue1) {
      if (localValue1 == null) {
         return this;
      } else {
         if (this.internalField0546 == null) {
            this.internalField0546 = new LinkedHashSet<>();
         }

         this.internalField0546.add(Registries.BLOCK.getId(localValue1));
         this.internalField0486 = null;
         this.internalMethod02612();
         return this;
      }
   }

   public RegistryListSetting internalMethod02504(Item localValue1) {
      if (localValue1 == null) {
         return this;
      } else {
         this.internalField0544.put(Registries.ITEM.getId(localValue1), localValue1);
         this.internalMethod02612();
         return this;
      }
   }

   public RegistryListSetting internalMethod06411() {
      this.internalField0277 = true;
      this.internalMethod02612();
      return this;
   }

   private void internalMethod02612() {
      this.internalField0416.clear();
      this.internalField0543.clear();

      for (Block localValue2 : Registries.BLOCK) {
         Identifier localValue3 = Registries.BLOCK.getId(localValue2);
         if ((this.internalField0546 == null || this.internalField0546.contains(localValue3)) && (this.internalField0486 == null || this.internalField0486.test(localValue2))) {
            RegistryListSetting.InternalType0387 localValue4 = RegistryListSetting.InternalType0387.internalMethod03081(localValue2);
            if (localValue4 != null) {
               this.internalField0416.add(localValue4);
               this.internalField0543.put(localValue4.internalMethod00654(), localValue4);
            }
         }
      }

      for (Item localValue7 : this.internalField0277 ? Registries.ITEM : this.internalField0544.values()) {
         RegistryListSetting.InternalType0387 localValue8 = RegistryListSetting.InternalType0387.internalMethod00624(localValue7);
         if (localValue8 != null && !this.internalField0543.containsKey(localValue8.internalMethod00654())) {
            this.internalField0416.add(localValue8);
            this.internalField0543.put(localValue8.internalMethod00654(), localValue8);
         }
      }

      this.internalField0416.sort(Comparator.comparing(RegistryListSetting.InternalType0387::internalMethod02690, String.CASE_INSENSITIVE_ORDER));
      this.internalField0500.retainAll(this.internalField0543.keySet());
      this.internalMethod08678();
   }

   public final void internalMethod03086(String localValue1) {
      String localValue2 = localValue1 == null ? "" : localValue1;
      if (!Objects.equals(this.internalField0247, localValue2)) {
         this.internalField0247 = localValue2;
         this.internalField1077 = localValue2.trim().toLowerCase(Locale.ROOT);
         this.internalMethod08678();
      }
   }

   public final List<RegistryListSetting.InternalType0387> internalMethod01166() {
      this.internalMethod08678();
      return Collections.unmodifiableList(this.internalField0417);
   }

   private void internalMethod08678() {
      this.internalField0417.clear();
      if (this.internalField1077.isEmpty()) {
         this.internalField0417.addAll(this.internalField0416);
      } else {
         for (RegistryListSetting.InternalType0387 localValue2 : this.internalField0416) {
            if (localValue2.internalMethod02690().toLowerCase(Locale.ROOT).contains(this.internalField1077)
               || localValue2.internalMethod00654().toString().toLowerCase(Locale.ROOT).contains(this.internalField1077)) {
               this.internalField0417.add(localValue2);
            }
         }
      }

      this.internalField0417.sort(Comparator.comparing(localValue1 -> !this.internalMethod01657(localValue1)));
   }

   public final void internalMethod01656(RegistryListSetting.InternalType0387 localValue1) {
      if (localValue1 != null) {
         Identifier localValue2 = localValue1.internalMethod00654();
         if (this.internalField0543.containsKey(localValue2)) {
            this.notifyChanged();
            if (!this.internalField0500.remove(localValue2)) {
               this.internalField0500.add(localValue2);
            }
         }
      }
   }

   public final void internalMethod05585(Block localValue1) {
      if (localValue1 != null) {
         Identifier localValue2 = Registries.BLOCK.getId(localValue1);
         if (this.internalField0543.containsKey(localValue2)) {
            this.notifyChanged();
            if (!this.internalField0500.remove(localValue2)) {
               this.internalField0500.add(localValue2);
            }
         }
      }
   }

   public final RegistryListSetting internalMethod03936(Block localValue1) {
      if (localValue1 == null) {
         return this;
      } else {
         Identifier localValue2 = Registries.BLOCK.getId(localValue1);
         if (this.internalField0543.containsKey(localValue2)) {
            if (this.internalField0500.contains(localValue2)) {
               return this;
            }

            this.notifyChanged();
            this.internalField0500.add(localValue2);
         }

         return this;
      }
   }

   public final RegistryListSetting internalMethod02288(Identifier localValue1) {
      if (localValue1 != null && this.internalField0543.containsKey(localValue1)) {
         if (this.internalField0500.contains(localValue1)) {
            return this;
         }

         this.notifyChanged();
         this.internalField0500.add(localValue1);
      }

      return this;
   }

   public final boolean internalMethod01657(RegistryListSetting.InternalType0387 localValue1) {
      return localValue1 != null && this.internalField0500.contains(localValue1.internalMethod00654());
   }

   public final boolean internalMethod05586(Block localValue1) {
      return localValue1 == null ? false : this.internalField0500.contains(Registries.BLOCK.getId(localValue1));
   }

   public final boolean internalMethod05933(Identifier localValue1) {
      return localValue1 != null && this.internalField0500.contains(localValue1);
   }

   public final Set<Identifier> internalMethod00086() {
      return Collections.unmodifiableSet(this.internalField0500);
   }

   public final List<RegistryListSetting.InternalType0387> internalMethod06877() {
      return this.internalField0500.stream().map(this.internalField0543::get).filter(Objects::nonNull).collect(Collectors.toList());
   }

   public final Set<Block> internalMethod04825() {
      return this.internalField0500
         .stream()
         .map(this.internalField0543::get)
         .filter(localValue0 -> localValue0 != null && localValue0.internalMethod04115() != null)
         .map(RegistryListSetting.InternalType0387::internalMethod04115)
         .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   public final Set<Item> internalMethod09099() {
      return this.internalField0500
         .stream()
         .map(this.internalField0543::get)
         .filter(Objects::nonNull)
         .map(RegistryListSetting.InternalType0387::internalMethod06517)
         .filter(Objects::nonNull)
         .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   public final int internalMethod02610() {
      return this.internalField0500.size();
   }

   @Override
   public final JsonElement toJson() {
      JsonArray localValue1 = new JsonArray();

      for (Identifier localValue3 : this.internalField0500) {
         localValue1.add(new JsonPrimitive(localValue3.toString()));
      }

      return localValue1;
   }

   @Override
   public final void fromJson(JsonElement localValue1) {
      if (localValue1 != null) {
         if (localValue1.isJsonArray()) {
            for (JsonElement localValue3 : localValue1.getAsJsonArray()) {
               if (!localValue3.isJsonPrimitive() || !localValue3.getAsJsonPrimitive().isString()) {
                  return;
               }
            }
         } else if (!localValue1.isJsonPrimitive() || !localValue1.getAsJsonPrimitive().isString()) {
            return;
         }

         this.internalField0500.clear();
         if (localValue1.isJsonArray()) {
            for (JsonElement localValue4 : localValue1.getAsJsonArray()) {
               this.internalMethod01700(localValue4.getAsString());
            }
         } else if (localValue1.isJsonPrimitive()) {
            this.internalMethod01700(localValue1.getAsString());
         }
      }
   }

   @Override
   public boolean isValidJson(JsonElement localValue1) {
      if (localValue1 == null) {
         return false;
      } else if (localValue1.isJsonPrimitive()) {
         return this.internalMethod07269(localValue1);
      } else if (!localValue1.isJsonArray()) {
         return false;
      } else {
         for (JsonElement localValue3 : localValue1.getAsJsonArray()) {
            if (!this.internalMethod07269(localValue3)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean internalMethod07269(JsonElement localValue1) {
      if (localValue1.isJsonPrimitive() && localValue1.getAsJsonPrimitive().isString()) {
         Identifier localValue2 = Identifier.tryParse(localValue1.getAsString());
         return localValue2 != null && this.internalField0543.containsKey(localValue2);
      } else {
         return false;
      }
   }

   private void internalMethod01700(String localValue1) {
      Identifier localValue2 = Identifier.tryParse(localValue1);
      if (localValue2 != null && this.internalField0543.containsKey(localValue2)) {
         this.internalField0500.add(localValue2);
      }
   }

   @Override
   public UiContainer createComponent() {
      SizedFont localValue1 = Fonts.internalField1154.internalMethod01432(8.0F);
      SizedFont localValue2 = Fonts.internalField0449.internalMethod01432(7.0F);
      SizedFont localValue3 = Fonts.internalField1154.internalMethod01432(7.0F);
      UiContainer localValue4 = new UiContainer()
         .internalMethod03907(
            new TextLabel(localValue1, () -> LanguageManager.internalMethod07214(this.internalField0248))
               .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
               .internalMethod02902()
               .fill()
         )
         .internalMethod03907(
            new UiElement()
               .text(
                  localValue2,
                  () -> this.internalMethod02610() + "/" + this.internalField0416.size(),
                  localValue0 -> ThemeColors.internalField1613.mulAlpha(0.6F + 0.4F * localValue0.hover())
               )
               .textAlign(TextAlignment.internalField1242)
         )
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(6.0F)
         .internalMethod03514(Insets.internalMethod00105(6.0F, 0.0F, 0.0F, 0.0F))
         .internalMethod09609();
      UiContainer localValue5 = new UiContainer()
         .internalMethod03907(
            new TextInputField(localValue3, this.internalField0247, this::internalMethod03086)
               .internalMethod07533(() -> LanguageManager.internalMethod07214("search"))
               .internalMethod06836(localValue0 -> ThemeColors.internalField1614)
               .internalMethod01901(4.0F)
               .internalMethod06744()
               .internalMethod07996(15.0F)
         )
         .internalMethod03907(
            new ItemGrid<>(this::internalMethod01166, RegistryListSetting.InternalType0387::internalMethod02202, this::internalMethod01657, this::internalMethod01656)
               .internalMethod06059(18.0F)
               .internalMethod06817()
         )
         .internalMethod01192(FlexDirection.internalField0629)
         .internalMethod03062(3.0F)
         .internalMethod09609();
      return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(4.0F).internalMethod03907(localValue4).internalMethod03907(localValue5);
   }

   @Generated
   public List<RegistryListSetting.InternalType0387> internalMethod08970() {
      return this.internalField0416;
   }

   @Generated
   public Map<Identifier, RegistryListSetting.InternalType0387> internalMethod00084() {
      return this.internalField0543;
   }

   @Generated
   public String internalMethod08875() {
      return this.internalField0247;
   }

   @Generated
   public String internalMethod08218() {
      return this.internalField1077;
   }

   @Generated
   public Set<Identifier> internalMethod08427() {
      return this.internalField0546;
   }

   @Generated
   public Map<Identifier, Item> internalMethod04824() {
      return this.internalField0544;
   }

   @Generated
   public boolean internalMethod04496() {
      return this.internalField0277;
   }

   @Generated
   public Predicate<Block> internalMethod04402() {
      return this.internalField0486;
   }

   public static final class InternalType0387 {
      private final Block internalField0792;
      private final Item internalField0152;
      private final Identifier internalField0354;
      private final ItemStack internalField0878;
      private final AnimatedColor internalField0814;
      private final AnimatedValue internalField0808;

      public InternalType0387(Block localValue1, Item localValue2, Identifier localValue3, ItemStack localValue4) {
         this.internalField0792 = localValue1;
         this.internalField0152 = localValue2;
         this.internalField0354 = localValue3;
         this.internalField0878 = localValue4;
         this.internalField0814 = new AnimatedColor(300L);
         this.internalField0808 = new AnimatedValue(300L, Easing.internalField1818);
      }

      public String internalMethod02690() {
         return Text.translatable(this.internalField0792 != null ? this.internalField0792.getTranslationKey() : this.internalField0152.getTranslationKey())
            .getString();
      }

      static RegistryListSetting.InternalType0387 internalMethod03081(Block localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            Identifier localValue1 = Registries.BLOCK.getId(localValue0);
            ItemStack localValue2 = localValue0.asItem().getDefaultStack();
            return localValue2.isEmpty() ? null : new RegistryListSetting.InternalType0387(localValue0, localValue2.getItem(), localValue1, localValue2);
         }
      }

      static RegistryListSetting.InternalType0387 internalMethod00624(Item localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            Identifier localValue1 = Registries.ITEM.getId(localValue0);
            ItemStack localValue2 = localValue0.getDefaultStack();
            return localValue2.isEmpty() ? null : new RegistryListSetting.InternalType0387(null, localValue0, localValue1, localValue2);
         }
      }

      @Generated
      public Block internalMethod04115() {
         return this.internalField0792;
      }

      @Generated
      public Item internalMethod06517() {
         return this.internalField0152;
      }

      @Generated
      public Identifier internalMethod00654() {
         return this.internalField0354;
      }

      @Generated
      public ItemStack internalMethod02202() {
         return this.internalField0878;
      }

      @Generated
      public AnimatedColor internalMethod05804() {
         return this.internalField0814;
      }

      @Generated
      public AnimatedValue internalMethod05801() {
         return this.internalField0808;
      }
   }
}
