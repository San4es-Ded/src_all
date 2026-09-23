package rockstar.client.internal.script;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import pyrock.utility.render.ColorRGBA;

public class EffectsHud extends AbstractHudWidget {
   private final BooleanSetting internalField0650 = new BooleanSetting(this, "hud.always_display");
   private final BooleanSetting internalField0651 = new BooleanSetting(this, "hud.effects.grouping");
   private final BooleanSetting internalField1261 = new BooleanSetting(this, "hud.effects.alert");
   private final SharedBooleanSetting internalField0381 = new SharedBooleanSetting(this, "hud.uniform_width");
   final Map<String, List<StatusEffectInstance>> internalField0543 = new LinkedHashMap<>();
   private final List<String> internalField0416 = new ArrayList<>();
   private Map<String, StatusEffectInstance> internalField0544 = new HashMap<>();
   private final Map<String, UiNode> internalField1197 = new HashMap<>();
   private final Set<String> internalField0546 = new HashSet<>();
   private boolean internalField0277;
   private UiContainer internalField0634;
   private UiNode internalField0633;
   private final UiTransition internalField0918 = (localValue1, localValue2, localValue3) -> {
      localValue3.internalField0205 = localValue1;
      localValue3.internalField0206 = (this.internalField0277 ? 6.0F : -6.0F) * (1.0F - localValue1);
   };

   public EffectsHud() {
      super("hud.effects", "hud/potion");
   }

   @Override
   public UiContainer build() {
      this.internalField1197.clear();
      this.internalField0546.clear();
      this.internalField0634 = new UiContainer().internalMethod01863().internalMethod01855(this.internalMethod00407()).internalMethod03062(1.0F);
      this.internalField0633 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(3.0F)
         .internalMethod09266(11.0F)
         .internalMethod03514(Insets.internalMethod02086(3.0F))
         .internalMethod07178(
            (localValue1, localValue2) -> {
               localValue1.drawClientRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 3.0F, 3.0F);
               localValue1.drawSquircle(
                  localValue2.x(),
                  localValue2.y(),
                  23.0F,
                  localValue2.h(),
                  3.0F,
                  CornerRadii.internalMethod03908(3.0F),
                  new VerticalColorGradient(ThemeColors.internalField1610.mulAlpha(0.1F), ThemeColors.internalField1610.mulAlpha(0.0F))
               );
            }
         )
         .internalMethod03907(new UiElement().size(7.0F, 7.0F).interactive(false).icon("hud/potion", 7.0F, ThemeColors.internalField1310))
         .internalMethod03907(
            new UiElement()
               .interactive(false)
               .text(
                  Fonts.internalField0449.internalMethod01432(6.0F),
                  () -> LanguageManager.internalMethod07214(this.name),
                  localValue0 -> ThemeColors.internalField1613
               )
         );
      this.internalField0634.internalMethod03907(this.internalField0633);
      this.internalMethod01562();
      return this.internalField0634;
   }

   private UiNode internalMethod03847(String localValue1) {
      UiNode localValue2 = this.internalField1197
         .computeIfAbsent(
            localValue1,
            localValue1x -> {
               SizedFont localValue2x = Fonts.internalField1154.internalMethod01432(6.0F);
               boolean localValue3 = localValue1x.startsWith("g:");
               EffectsHud.InternalType0287 localValue4 = new EffectsHud.InternalType0287(localValue1x);
               if (localValue3) {
                  localValue4.fillWidth();
               }

               UiContainer localValue5 = new UiContainer()
                  .internalMethod05895()
                  .internalMethod01855(TextAlignment.internalField0621)
                  .internalMethod03062(1.0F)
                  .internalMethod07914(this.internalField0918)
                  .internalMethod03907(localValue4);
               if (!localValue3) {
                  localValue5.internalMethod03907(
                     new UiElement()
                        .interactive(false)
                        .fillWidth()
                        .height(10.0F)
                        .radius(2.0F)
                        .padding(Insets.internalMethod02086(3.0F))
                        .background(localValue0 -> ThemeColors.internalField1612.mulAlpha(0.889F))
                        .text(localValue2x, () -> this.internalMethod01574(localValue1x), localValue0 -> ThemeColors.internalField1613)
                  );
               }

               localValue5.internalMethod03907(
                  new UiElement()
                     .interactive(false)
                     .height(10.0F)
                     .minWidth(18.0F)
                     .radius(2.0F)
                     .padding(Insets.internalMethod02086(3.0F))
                     .background(localValue0 -> ThemeColors.internalField1612.mulAlpha(0.94F))
                     .textAlign(TextAlignment.internalField0621)
                     .text(localValue2x, () -> this.internalMethod02539(localValue1x), localValue0 -> ThemeColors.internalField1613.mulAlpha(0.75F))
               );
               return localValue5;
            }
         );
      if (localValue2.phase() == UiNode.InternalType0146.internalField1091
         || localValue2.phase() == UiNode.InternalType0146.internalField1090
         || localValue2.phase() == UiNode.InternalType0146.internalField1089) {
         localValue2.beginEnter(0.0F);
      }

      return localValue2;
   }

   private void internalMethod01562() {
      HashSet localValue1 = new HashSet<>(this.internalField0416);
      if (!localValue1.equals(this.internalField0546)) {
         this.internalField0546.clear();
         this.internalField0546.addAll(localValue1);
         SizedFont localValue2 = Fonts.internalField1154.internalMethod01432(6.0F);
         HashMap localValue3 = new HashMap();

         for (String localValue5 : this.internalField0416) {
            localValue3.put(localValue5, this.internalMethod04850(localValue5, localValue2));
         }

         ArrayList localValue8 = new ArrayList<>(this.internalField0416);
         localValue8.sort(Comparator.<String>comparingDouble(localValue1x -> ((Float)localValue3.get(localValue1x)).floatValue()).reversed());
         ArrayList localValue9 = new ArrayList();
         localValue9.add(this.internalField0633);

         for (String localValue7 : (Iterable<String>)(Iterable<?>)localValue8) {
            localValue9.add(this.internalMethod03847(localValue7));
         }

         this.internalField0634.internalMethod07849(localValue9);
      }
   }

   private float internalMethod04850(String localValue1, SizedFont localValue2) {
      List localValue3 = this.internalField0543.get(localValue1);
      int localValue4 = localValue3 != null && !localValue3.isEmpty() ? localValue3.size() : 1;
      float localValue5 = 4.0F + localValue4 * 10.0F;
      float localValue6 = localValue1.startsWith("g:") ? 0.0F : localValue2.internalMethod00965(this.internalMethod01574(localValue1));
      float localValue7 = Math.max(18.0F, localValue2.internalMethod00965("00:00"));
      return localValue5 + localValue6 + localValue7;
   }

   @Override
   public void update(UiRenderContext localValue1) {
      this.internalField0277 = this.x + this.width / 2.0F >= ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0F;
      if (internalField0149.player != null && internalField0149.world != null) {
         this.internalMethod01564();
      }

      if (this.internalField0634 != null) {
         this.internalField0634.internalMethod01855(this.internalMethod00407());
         this.internalMethod01562();
      }

      super.update(localValue1);
   }

   private TextAlignment internalMethod00407() {
      return this.internalField0381.internalMethod04496()
         ? TextAlignment.internalField1243
         : (this.internalField0277 ? TextAlignment.internalField1242 : TextAlignment.internalField0622);
   }

   private void internalMethod01564() {
      Collection<StatusEffectInstance> localValue1 = internalField0149.player.getStatusEffects();
      TreeMap<String, StatusEffectInstance> localValue2 = new TreeMap<>();

      for (StatusEffectInstance localValue4 : localValue1) {
         StatusEffect localValue5 = (StatusEffect)localValue4.getEffectType().value();
         String localValue6 = localValue5.getName().getString();
         if (localValue6 != null && !ServerUtils.internalMethod01786(KnownServer.internalField1219) && !GameUtils.internalMethod06278(localValue4)) {
            localValue2.put(localValue5.getTranslationKey() + ":" + localValue4.getAmplifier(), localValue4);
         }
      }

      if (this.internalField1261.internalMethod04496()) {
         this.internalField0544
            .forEach(
               (localValue1x, localValue2x) -> {
                  if (!localValue2.containsKey(localValue1x)) {
                     StatusEffect localValue3 = (StatusEffect)localValue2x.getEffectType().value();
                     if (!localValue3.getCategory().equals(StatusEffectCategory.HARMFUL)) {
                        String localValue4x = localValue3.getName().getString() + " " + (localValue2x.getAmplifier() > 0 ? localValue2x.getAmplifier() + 1 : "");
                        RockstarClient.getInstance()
                           .internalMethod02503()
                           .internalMethod02784(
                              new StatusEffectNotification(LanguageManager.internalMethod00160("hud.effects.ended", localValue4x), localValue2x.getEffectType())
                                 .internalMethod01683(localValue4x)
                           );
                     }
                  }
               }
            );
      }

      this.internalField0543.clear();
      this.internalField0416.clear();
      if (this.internalField0651.internalMethod04496()) {
         Map<Integer, List<StatusEffectInstance>> localValue7 = localValue2.values().stream().collect(Collectors.groupingBy(localValue0 -> localValue0.getDuration() * 50, LinkedHashMap::new, Collectors.toList()));

         for (List localValue11 : (Iterable<List>)(Iterable<?>)localValue7.values()) {
            String localValue12 = "g:"
               + localValue11.stream()
                  .map(localValue0 -> ((StatusEffect)((net.minecraft.entity.effect.StatusEffectInstance)localValue0).getEffectType().value()).getTranslationKey() + ":" + ((net.minecraft.entity.effect.StatusEffectInstance)localValue0).getAmplifier())
                  .sorted()
                  .collect(Collectors.joining(","));
            this.internalField0543.put(localValue12, localValue11);
            this.internalField0416.add(localValue12);
         }
      } else {
         for (Entry localValue10 : (Iterable<Entry>)(Iterable<?>)localValue2.entrySet()) {
            this.internalField0543.put((String)localValue10.getKey(), List.of((StatusEffectInstance)localValue10.getValue()));
            this.internalField0416.add((String)localValue10.getKey());
         }
      }

      this.internalField0544 = localValue2;
   }

   private String internalMethod01574(String localValue1) {
      List localValue2 = this.internalField0543.get(localValue1);
      if (localValue2 != null && !localValue2.isEmpty()) {
         StatusEffectInstance localValue3 = (StatusEffectInstance)localValue2.getFirst();
         StatusEffect localValue4 = (StatusEffect)localValue3.getEffectType().value();
         int localValue5 = localValue3.getAmplifier();
         return localValue4.getName().getString() + (localValue5 > 0 ? " " + (localValue5 + 1) : "");
      } else {
         return "";
      }
   }

   private String internalMethod02539(String localValue1) {
      List localValue2 = this.internalField0543.get(localValue1);
      if (localValue2 != null && !localValue2.isEmpty()) {
         StatusEffectInstance localValue3 = (StatusEffectInstance)localValue2.getFirst();
         if (!localValue3.isInfinite() && localValue3.getDuration() < 999999999) {
            int localValue4 = localValue3.getDuration() / 20;
            return String.format("%02d:%02d", localValue4 / 60, localValue4 % 60);
         } else {
            return "**:**";
         }
      } else {
         return "";
      }
   }

   @Override
   public boolean show() {
      if (internalField0149.player == null) {
         return false;
      } else {
         boolean localValue1 = internalField0149.player
            .getStatusEffects()
            .stream()
            .anyMatch(
               localValue0 -> !GameUtils.internalMethod06278(localValue0)
                  && localValue0.getEffectType() != null
                  && !ServerUtils.internalMethod01786(KnownServer.internalField1219)
            );
         return (localValue1 || internalField0149.currentScreen instanceof ChatScreen || this.internalField0650.internalMethod04496())
            && !ServerUtils.internalMethod01786(KnownServer.internalField1219);
      }
   }

   final class InternalType0287 extends UiNode {
      private final String internalField0248;

      InternalType0287(String localValue2) {
         this.internalField0248 = localValue2;
         this.interactive(false);
      }

      private List<StatusEffectInstance> internalMethod05843() {
         return EffectsHud.this.internalField0543.getOrDefault(this.internalField0248, List.of());
      }

      @Override
      protected void measure() {
         int localValue1 = Math.max(1, this.internalMethod05843().size());
         this.prefW = 4 + localValue1 * 10;
         this.prefH = 10.0F;
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         localValue1.drawRoundedRect(
            this.x(), this.y(), this.w(), this.h(), CornerRadii.internalMethod03908(2.0F), ThemeColors.internalField1612.mulAlpha(0.94F)
         );
         float localValue3 = this.x() + 3.0F;
         float localValue4 = this.y() + this.h() / 2.0F - 4.0F;

         for (StatusEffectInstance localValue6 : this.internalMethod05843()) {
            Identifier localValue7 = net.minecraft.client.gui.hud.InGameHud.getEffectTexture(localValue6.getEffectType());
            localValue1.drawGuiTexture(
               net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
               localValue7,
               Math.round(localValue3),
               Math.round(localValue4),
               8,
               8
            );
            localValue3 += 10.0F;
         }
      }
   }
}
