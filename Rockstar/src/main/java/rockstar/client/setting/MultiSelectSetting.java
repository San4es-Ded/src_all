package rockstar.client.setting;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import pyrock.utility.render.ColorRGBA;

public class MultiSelectSetting extends AbstractSetting {
   final List<MultiSelectSetting.InternalType0091> internalField0416 = new ArrayList<>();
   private List<MultiSelectSetting.InternalType0091> internalField0417 = new ArrayList<>();
   private boolean internalField0277;
   private int internalField0227;

   public MultiSelectSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3, @NotNull BooleanSupplier localValue4) {
      super(localValue1, localValue2, localValue4);
   }

   public MultiSelectSetting(@NotNull SettingOwner localValue1, String localValue2, @NotNull BooleanSupplier localValue3) {
      super(localValue1, localValue2, localValue3);
   }

   public MultiSelectSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3) {
      super(localValue1, localValue2);
   }

   public MultiSelectSetting(@NotNull SettingOwner localValue1, String localValue2) {
      super(localValue1, localValue2);
   }

   public MultiSelectSetting internalMethod05559() {
      this.internalField0277 = true;
      return this;
   }

   public MultiSelectSetting internalMethod03035(int localValue1) {
      this.internalField0227 = Math.max(0, localValue1);
      return this;
   }

   public void internalMethod02088(MultiSelectSetting.InternalType0091 localValue1) {
      this.internalField0416.add(localValue1);
   }

   public void internalMethod05668(MultiSelectSetting.InternalType0091 localValue1) {
      this.internalMethod04459(localValue1, true);
   }

   void internalMethod04459(MultiSelectSetting.InternalType0091 localValue1, boolean localValue2) {
      if (localValue1 != null && this.internalField0417.contains(localValue1) != localValue2) {
         this.notifyChanged();
         if (localValue2) {
            this.internalField0417.add(localValue1);
         } else {
            this.internalField0417.remove(localValue1);
         }
      }
   }

   void internalMethod07964(MultiSelectSetting.InternalType0091 localValue1) {
      if (localValue1 != null && !localValue1.alwaysEnabled) {
         if (this.internalField0417.contains(localValue1)) {
            if (this.internalField0417.size() > this.internalField0227) {
               this.internalMethod04459(localValue1, false);
            }
         } else {
            this.internalMethod04459(localValue1, true);
         }
      }
   }

   @Override
   public JsonElement toJson() {
      JsonObject localValue1 = new JsonObject();
      JsonArray localValue2 = new JsonArray();

      for (MultiSelectSetting.InternalType0091 localValue4 : this.internalField0417) {
         localValue2.add(new JsonPrimitive(localValue4.getName()));
      }

      localValue1.add("selected", localValue2);
      if (this.internalField0277) {
         JsonArray localValue6 = new JsonArray();

         for (MultiSelectSetting.InternalType0091 localValue5 : this.internalField0416) {
            localValue6.add(new JsonPrimitive(localValue5.getName()));
         }

         localValue1.add("order", localValue6);
      }

      return localValue1;
   }

   @Override
   public void fromJson(JsonElement localValue1) {
      if (localValue1 != null) {
         if (localValue1.isJsonObject()) {
            JsonObject localValue2 = localValue1.getAsJsonObject();
            if (this.internalField0277 && localValue2.has("order") && !this.internalMethod00181(localValue2.get("order"))) {
               return;
            }

            if (localValue2.has("selected") && !this.internalMethod00181(localValue2.get("selected"))) {
               return;
            }
         } else if (!this.internalMethod00181(localValue1)) {
            return;
         }

         this.internalField0417.clear();
         if (localValue1.isJsonObject()) {
            JsonObject localValue8 = localValue1.getAsJsonObject();
            if (this.internalField0277 && localValue8.has("order")) {
               JsonArray localValue3 = localValue8.getAsJsonArray("order");
               ArrayList localValue4 = new ArrayList();

               for (JsonElement localValue6 : localValue3) {
                  String localValue7 = localValue6.getAsString();
                  this.internalField0416.stream().filter(localValue1x -> localValue1x.getName().equalsIgnoreCase(localValue7)).findFirst().ifPresent(localValue4::add);
               }

               for (MultiSelectSetting.InternalType0091 localValue19 : this.internalField0416) {
                  if (!localValue4.contains(localValue19)) {
                     localValue4.add(localValue19);
                  }
               }

               this.internalField0416.clear();
               this.internalField0416.addAll(localValue4);
            }

            if (localValue8.has("selected")) {
               for (JsonElement localValue17 : localValue8.getAsJsonArray("selected")) {
                  String localValue20 = localValue17.getAsString();
                  this.internalField0416.stream().filter(localValue1x -> localValue1x.getName().equalsIgnoreCase(localValue20)).findFirst().ifPresent(this.internalField0417::add);
               }
            }
         } else if (localValue1.isJsonArray()) {
            for (JsonElement localValue15 : localValue1.getAsJsonArray()) {
               String localValue18 = localValue15.getAsString();
               this.internalField0416.stream().filter(localValue1x -> localValue1x.getName().equalsIgnoreCase(localValue18)).findFirst().ifPresent(this.internalField0417::add);
            }
         }

         for (MultiSelectSetting.InternalType0091 localValue13 : this.internalField0416) {
            if (localValue13.isAlwaysEnabled() && !this.internalField0417.contains(localValue13)) {
               this.internalField0417.add(localValue13);
            }
         }

         if (this.internalField0417.size() < this.internalField0227) {
            this.internalField0416
               .stream()
               .filter(localValue1x -> !this.internalField0417.contains(localValue1x))
               .limit(this.internalField0227 - this.internalField0417.size())
               .forEach(this.internalField0417::add);
         }
      }
   }

   private boolean internalMethod00181(JsonElement localValue1) {
      if (localValue1 != null && localValue1.isJsonArray()) {
         for (JsonElement localValue3 : localValue1.getAsJsonArray()) {
            if (!localValue3.isJsonPrimitive() || !localValue3.getAsJsonPrimitive().isString()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isValidJson(JsonElement localValue1) {
      if (localValue1 == null) {
         return false;
      } else if (!localValue1.isJsonObject()) {
         return this.internalMethod08463(localValue1);
      } else {
         JsonObject localValue2 = localValue1.getAsJsonObject();
         return localValue2.has("selected") && this.internalMethod08463(localValue2.get("selected"))
            ? !this.internalField0277 || localValue2.has("order") && this.internalMethod08463(localValue2.get("order"))
            : false;
      }
   }

   private boolean internalMethod08463(JsonElement localValue1) {
      if (!this.internalMethod00181(localValue1)) {
         return false;
      } else {
         for (JsonElement localValue3 : localValue1.getAsJsonArray()) {
            if (this.internalField0416.stream().noneMatch(localValue1x -> localValue1x.getName().equalsIgnoreCase(localValue3.getAsString()))) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public UiContainer createComponent() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod03907(
            new TextLabel(Fonts.internalField1154.internalMethod01432(8.0F), () -> LanguageManager.internalMethod07214(this.internalField0248))
               .internalMethod02959(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F + 0.25F * localValue0.hover()))
               .internalMethod02902()
               .fill()
         )
         .internalMethod03907(
            new UiContainer()
               .internalMethod01192(FlexDirection.internalField1246)
               .internalMethod01855(TextAlignment.internalField0621)
               .internalMethod03907(
                  new AnimatedNumberLabel(
                        Fonts.internalField1154.internalMethod01432(7.0F),
                        () -> Math.toIntExact(this.internalField0417.stream().filter(localValue0 -> !localValue0.isHidden()).count())
                     )
                     .internalMethod01032(localValue0 -> ThemeColors.internalField1310)
               )
               .internalMethod03907(
                  new UiElement()
                     .text(
                        Fonts.internalField1154.internalMethod01432(7.0F),
                        () -> " "
                           + LanguageManager.internalMethod07214("setting_of")
                           + " "
                           + this.internalField0416.stream().filter(localValue0 -> !localValue0.isHidden()).count(),
                        localValue0 -> ThemeColors.internalField1310
                     )
               )
         )
         .internalMethod03062(6.0F)
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03514(Insets.internalMethod00105(6.0F, 0.0F, 0.0F, 0.0F))
         .internalMethod09609();
      Object localValue2;
      if (this.internalField0277) {
         localValue2 = this.internalMethod06168();
      } else {
         localValue2 = (new UiContainer() {
               private final IdentityHashMap<MultiSelectSetting.InternalType0091, UiNode> internalField0594 = new IdentityHashMap<>();
               private List<MultiSelectSetting.InternalType0091> internalField0416;

               {
                  for (MultiSelectSetting.InternalType0091 localValue3 : MultiSelectSetting.this.internalField0416) {
                     UiElement localValue4 = localValue3.buildComponent();
                     this.internalField0594.put(localValue3, localValue4);
                     this.internalMethod03907(localValue4);
                  }

                  this.internalField0416 = new ArrayList<>(MultiSelectSetting.this.internalField0416);
               }

               @Override
               protected void onTick(float localValue1, float localValue2x, float localValue3) {
                  if (!MultiSelectSetting.internalMethod03941(this.internalField0416, MultiSelectSetting.this.internalField0416)) {
                     ArrayList localValue4 = new ArrayList(MultiSelectSetting.this.internalField0416.size());

                     for (MultiSelectSetting.InternalType0091 localValue6 : MultiSelectSetting.this.internalField0416) {
                        localValue4.add(this.internalField0594.computeIfAbsent(localValue6, MultiSelectSetting.InternalType0091::buildComponent));
                     }

                     this.internalField0594.keySet().removeIf(localValue1x -> !MultiSelectSetting.this.internalField0416.contains(localValue1x));
                     this.internalMethod07849(localValue4);
                     this.internalField0416 = new ArrayList<>(MultiSelectSetting.this.internalField0416);
                  }

                  super.onTick(localValue1, localValue2x, localValue3);
               }
            })
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod03062(2.0F)
            .internalMethod09609()
            .internalMethod03514(Insets.internalMethod00105(0.0F, 0.0F, 5.0F, 0.0F))
            .internalMethod07971();
      }

      return new UiContainer().internalMethod01192(FlexDirection.internalField0629).internalMethod03062(5.0F).internalMethod03907(localValue1).internalMethod03907((UiNode)localValue2);
   }

   static boolean internalMethod03941(List<?> localValue0, List<?> localValue1) {
      if (localValue0.size() != localValue1.size()) {
         return false;
      } else {
         for (int localValue2 = 0; localValue2 < localValue0.size(); localValue2++) {
            if (localValue0.get(localValue2) != localValue1.get(localValue2)) {
               return false;
            }
         }

         return true;
      }
   }

   int internalMethod00568() {
      return (int)this.internalField0416.stream().filter(localValue0 -> !localValue0.isHidden()).count();
   }

   private UiElement internalMethod06168() {
      final Stopwatch localValue1 = new Stopwatch();
      UiElement localValue2 = new UiElement() {
         private MultiSelectSetting.InternalType0091 internalField0245;
         private float internalField0205;
         private float internalField0206;

         @Override
         public float desiredH() {
            return 8 + 12 * MultiSelectSetting.this.internalMethod00568();
         }

         @Override
         protected void onTick(float localValue1x, float localValue2x, float localValue3) {
            this.internalField0205 = localValue2x;
            this.internalField0206 = localValue3;
         }

         @Override
         protected void drawSelf(UiRenderContext localValue1x, float localValue2x) {
            float localValue3 = this.x();
            float localValue4 = this.y();
            float localValue5 = this.w();
            localValue1x.drawRoundedRect(
               localValue3, localValue4, localValue5, this.h(), CornerRadii.internalMethod03908(6.0F), ThemeColors.internalMethod07738().withAlpha(76.5F * localValue2x)
            );
            float localValue6 = 0.0F;

            for (MultiSelectSetting.InternalType0091 localValue8 : MultiSelectSetting.this.internalField0416) {
               if (!localValue8.isHidden()) {
                  float localValue9 = this.internalField0245 == localValue8
                     ? Math.clamp(this.internalField0206 - 2.0F, localValue4 + 1.0F, localValue4 + 3.0F + 12 * MultiSelectSetting.this.internalMethod00568())
                     : localValue4 + 7.0F + localValue6;
                  boolean localValue10 = this.inFlow()
                     && this.internalField0205 >= localValue3 - 1.0F
                     && this.internalField0205 <= localValue3 + localValue5 + 1.0F
                     && this.internalField0206 >= localValue9 - 4.0F
                     && this.internalField0206 <= localValue9 + 8.0F;
                  localValue8.getYAnim().internalMethod06645(Easing.internalField0811);
                  localValue8.getYAnim().internalMethod07059(localValue9 - localValue4);
                  localValue8.setYFactor(localValue9);
                  if (localValue10 && this.internalField0245 != localValue8 && !localValue8.isAlwaysEnabled()) {
                     CursorManager.internalMethod06882(CursorType.internalField0567);
                  }

                  if (localValue10 && this.internalField0205 <= localValue3 + 17.0F || localValue8 == this.internalField0245) {
                     CursorManager.internalMethod06882(CursorType.internalField1205);
                  }

                  localValue8.getHoverAnimation().internalMethod07062(localValue10);
                  localValue8.getActiveAnimation().internalMethod06645(Easing.internalField0812);
                  localValue8.getActiveAnimation().internalMethod07062(localValue8.isSelected());
                  float localValue11 = localValue8.getActiveAnimation().internalMethod02881();
                  float localValue12 = localValue4 + localValue8.getYAnim().internalMethod02881();
                  localValue1x.drawIcon("hud/drag", localValue3 + 7.0F, localValue12, 6.0F, ThemeColors.internalMethod08459().mulAlpha(localValue2x));
                  localValue1x.drawFadeoutText(
                     Fonts.internalField1154.internalMethod01432(7.0F),
                     LanguageManager.internalMethod07214(localValue8.getName()),
                     localValue3 + 18.0F,
                     localValue12 + 0.5F,
                     ThemeColors.internalMethod08459()
                        .withAlpha(255.0F * localValue2x * (0.75F + 0.25F * localValue8.getHoverAnimation().internalMethod02881() + 0.25F * localValue11)),
                     0.8F,
                     1.0F,
                     localValue5 - 30.0F - localValue11 * 9.0F
                  );
                  if (localValue11 > 0.01F) {
                     float localValue13 = 0.5F + 0.5F * localValue11;
                     float localValue14 = 6.0F * localValue13;
                     localValue1x.drawIcon(
                        "check",
                        localValue3 + localValue5 - 8.0F - localValue11 * 2.0F - localValue14 / 2.0F,
                        localValue12 + 3.0F - localValue14 / 2.0F,
                        localValue14,
                        ThemeColors.internalField1613.mulAlpha(localValue2x * Math.min(1.0F, localValue11))
                     );
                  }

                  localValue6 += 12.0F;
               }
            }

            if (this.internalField0245 != null && localValue1.internalMethod02365(100L)) {
               MultiSelectSetting.this.notifyChanged();
               MultiSelectSetting.this.internalField0416.sort(Comparator.comparingDouble(MultiSelectSetting.InternalType0091::getYFactor));
               localValue1.internalMethod00701();
            }
         }

         @Override
         public boolean mouseClicked(float localValue1x, float localValue2x, MouseButton localValue3) {
            if (localValue3 == MouseButton.internalField0102 && this.inFlow() && this.contains(localValue1x, localValue2x)) {
               float localValue4 = this.x();
               float localValue5 = this.y();
               float localValue6 = this.w();
               float localValue7 = 0.0F;

               for (MultiSelectSetting.InternalType0091 localValue9 : MultiSelectSetting.this.internalField0416) {
                  if (!localValue9.isHidden()) {
                     boolean localValue10 = localValue1x >= localValue4 - 1.0F && localValue1x <= localValue4 + localValue6 + 1.0F && localValue2x >= localValue5 + 3.0F + localValue7 && localValue2x <= localValue5 + 15.0F + localValue7;
                     if (localValue10 && localValue1x <= localValue4 + 17.0F) {
                        this.internalField0245 = localValue9;
                     } else if (localValue10) {
                        localValue9.toggle();
                     }

                     localValue7 += 12.0F;
                  }
               }

               return true;
            } else {
               return false;
            }
         }

         @Override
         public void mouseReleased(float localValue1x, float localValue2x, MouseButton localValue3) {
            this.internalField0245 = null;
            super.mouseReleased(localValue1x, localValue2x, localValue3);
         }
      };
      return localValue2.fillWidth();
   }

   @Generated
   public List<MultiSelectSetting.InternalType0091> internalMethod01792() {
      return this.internalField0416;
   }

   @Generated
   public List<MultiSelectSetting.InternalType0091> internalMethod07492() {
      return this.internalField0417;
   }

   @Generated
   public boolean internalMethod04496() {
      return this.internalField0277;
   }

   @Generated
   public int internalMethod00564() {
      return this.internalField0227;
   }

   public static class InternalType0091 {
      private final MultiSelectSetting parent;
      private final String name;
      private final String description;
      private final AnimatedValue hoverAnimation = new AnimatedValue(300L, Easing.internalField1626);
      private final AnimatedValue activeAnimation = new AnimatedValue(300L, Easing.internalField1626);
      private final AnimatedValue yAnim = new AnimatedValue(300L, Easing.internalField0812);
      private float yFactor;
      boolean alwaysEnabled;
      private final BooleanSupplier hideCondition;
      private SpriteAnimationPlayer enableAnimation;
      private SpriteAnimationPlayer disableAnimation;
      private SpriteAnimationPlayer currentAnimation;
      private boolean lastState;

      public InternalType0091(MultiSelectSetting localValue1, String localValue2) {
         this(localValue1, localValue2, "", () -> false);
      }

      public InternalType0091(MultiSelectSetting localValue1, String localValue2, BooleanSupplier localValue3) {
         this(localValue1, localValue2, "", localValue3);
      }

      public InternalType0091(MultiSelectSetting localValue1, String localValue2, String localValue3) {
         this(localValue1, localValue2, localValue3, () -> false);
      }

      public InternalType0091(MultiSelectSetting localValue1, String localValue2, String localValue3, BooleanSupplier localValue4) {
         this.parent = localValue1;
         this.name = localValue2;
         this.description = localValue3;
         this.hideCondition = localValue4;
         localValue1.internalMethod02088(this);
      }

      public boolean isHidden() {
         return this.hideCondition != null && this.hideCondition.getAsBoolean();
      }

      public MultiSelectSetting.InternalType0091 select() {
         this.parent.internalMethod05668(this);
         return this;
      }

      public MultiSelectSetting.InternalType0091 deselect() {
         if (!this.alwaysEnabled) {
            this.parent.internalMethod04459(this, false);
         }

         return this;
      }

      public MultiSelectSetting.InternalType0091 alwaysEnabled() {
         this.alwaysEnabled = true;
         this.parent.internalMethod05668(this);
         return this;
      }

      public MultiSelectSetting.InternalType0091 toggle() {
         this.parent.internalMethod07964(this);
         return this;
      }

      public boolean isSelected() {
         return this.parent.internalMethod07492().contains(this);
      }

      public UiElement buildComponent() {
         return new UiElement()
            .bind("selected", this::isSelected, Motion.internalMethod01328(220L, Easing.internalField1626))
            .text(
               Fonts.internalField0449.internalMethod01432(7.0F),
               () -> LanguageManager.internalMethod07214(this.name),
               localValue0 -> ThemeColors.internalMethod01303(background(localValue0)).mulAlpha(0.75F + 0.25F * localValue0.sig("selected"))
            )
            .textAlign(TextAlignment.internalField0621)
            .background(MultiSelectSetting.InternalType0091::background)
            .radius(2.5F)
            .padding(Insets.internalMethod00172(3.0F))
            .cursor(CursorType.internalField0567)
            .onClick(this::toggle);
      }

      private static ColorRGBA background(UiElement localValue0) {
         return ThemeColors.internalField1614
            .mix(ThemeColors.internalField1310, 0.2F * localValue0.hover())
            .mix(ThemeColors.internalField1310.mix(ThemeColors.internalField1614, 0.2F * localValue0.hover()), localValue0.sig("selected"));
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public boolean equals(Object localValue1) {
         if (localValue1 == this) {
            return true;
         } else if (localValue1 != null && localValue1.getClass() == this.getClass()) {
            MultiSelectSetting.InternalType0091 localValue2 = (MultiSelectSetting.InternalType0091)localValue1;
            return Objects.equals(this.parent, localValue2.parent) && Objects.equals(this.name, localValue2.name) && Objects.equals(this.description, localValue2.description);
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.parent, this.name, this.description);
      }

      @Generated
      public MultiSelectSetting getParent() {
         return this.parent;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public String getDescription() {
         return this.description;
      }

      @Generated
      public AnimatedValue getHoverAnimation() {
         return this.hoverAnimation;
      }

      @Generated
      public AnimatedValue getActiveAnimation() {
         return this.activeAnimation;
      }

      @Generated
      public AnimatedValue getYAnim() {
         return this.yAnim;
      }

      @Generated
      public float getYFactor() {
         return this.yFactor;
      }

      @Generated
      public boolean isAlwaysEnabled() {
         return this.alwaysEnabled;
      }

      @Generated
      public BooleanSupplier getHideCondition() {
         return this.hideCondition;
      }

      @Generated
      public SpriteAnimationPlayer getEnableAnimation() {
         return this.enableAnimation;
      }

      @Generated
      public SpriteAnimationPlayer getDisableAnimation() {
         return this.disableAnimation;
      }

      @Generated
      public SpriteAnimationPlayer getCurrentAnimation() {
         return this.currentAnimation;
      }

      @Generated
      public boolean isLastState() {
         return this.lastState;
      }

      @Generated
      public void setYFactor(float localValue1) {
         this.yFactor = localValue1;
      }

      @Generated
      public void setAlwaysEnabled(boolean localValue1) {
         this.alwaysEnabled = localValue1;
      }

      @Generated
      public void setEnableAnimation(SpriteAnimationPlayer localValue1) {
         this.enableAnimation = localValue1;
      }

      @Generated
      public void setDisableAnimation(SpriteAnimationPlayer localValue1) {
         this.disableAnimation = localValue1;
      }

      @Generated
      public void setCurrentAnimation(SpriteAnimationPlayer localValue1) {
         this.currentAnimation = localValue1;
      }

      @Generated
      public void setLastState(boolean localValue1) {
         this.lastState = localValue1;
      }
   }
}
