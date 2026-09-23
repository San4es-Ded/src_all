package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.data.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent.Builder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.RotationAxis;
import pyrock.utility.render.ColorRGBA;

public class AutoBuyScreen extends LayeredRockstarScreen {
   private static final float internalField0205 = 11.0F;
   private static final float internalField1048 = 10.0F;
   private static final Motion internalField0913 = Motion.internalMethod01328(300L, Easing.internalField1822);
   private static final UiTransition internalField0918 = (localValue0, localValue1, localValue2) -> {
      localValue2.internalField0205 = localValue0;
      localValue2.internalField1048 = (1.0F - localValue0) * 6.0F;
   };
   private static boolean internalField0277;
   private AutoBuyScreen.InternalType0153 internalField0422;
   private AutoBuyScreen.InternalType0153 internalField0423;
   private DonItemCatalog.InternalType0189 internalField0406;
   private DonItemCatalog.InternalType0190 internalField0408;
   private String internalField0248;
   private long internalField0229;
   private boolean internalField0276;
   private double internalField0194;
   private long internalField0230;
   private int internalField0227;
   private int internalField0228;
   private String internalField0247;
   private final LinkedHashMap<RegistryEntry<Enchantment>, Integer> internalField0499;
   private final LinkedHashMap<RegistryEntry<StatusEffect>, Integer> internalField0498;
   private boolean internalField1099;
   private boolean internalField1100;
   private String internalField1077;
   private UiContainer internalField0634;
   private UiContainer internalField0635;
   private UiContainer internalField1257;
   private UiContainer internalField1254;
   private UiContainer internalField1255;
   private UiContainer internalField1256;
   private UiContainer internalField1586;
   private UiContainer internalField1585;
   private String internalField1076;
   private String internalField1079;
   private String internalField1078;
   private String internalField1501;
   private String internalField1499;
   private String internalField1498;
   private String internalField1497;

   public AutoBuyScreen() {
      this.internalField0422 = AutoBuyScreen.InternalType0153.internalField0422;
      this.internalField0406 = DonItemCatalog.InternalType0189.internalField0407;
      this.internalField0248 = "";
      this.internalField0229 = 25000L;
      this.internalField0194 = 20.0;
      this.internalField0227 = 1;
      this.internalField0228 = 10;
      this.internalField0247 = "";
      this.internalField0499 = new LinkedHashMap<>();
      this.internalField0498 = new LinkedHashMap<>();
      this.internalField1077 = "";
      this.internalField1076 = "";
      this.internalField1079 = "";
      this.internalField1078 = "";
      this.internalField1501 = "";
      this.internalField1499 = "";
      this.internalField1498 = "";
      this.internalField1497 = "";
   }

   @Override
   public void init() {
      if (!internalField0277) {
         DonItemCatalog.internalMethod00744();
         internalField0277 = true;
      }

      super.init();
      this.clearRoots();
      this.internalField0423 = null;
      UiContainer localValue1 = new UiContainer().internalMethod01863().internalMethod03062(9.0F).internalMethod01855(TextAlignment.internalField0621).internalMethod09784();
      localValue1.internalMethod03907(this.internalMethod06773());
      this.internalField0634 = new UiContainer()
         .internalMethod05895()
         .internalMethod03062(10.0F)
         .internalMethod09266(236.0F)
         .internalMethod09339(562.0F)
         .internalMethod07607(LayoutAlignment.internalField0912);
      localValue1.internalMethod03907(this.internalField0634);
      this.add(localValue1);
      this.overlays.clear();
      this.openWindow(this.internalMethod09065());
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalMethod08015();
      super.render(localValue1);
   }

   private UiContainer internalMethod06773() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod05895()
         .internalMethod03062(2.0F)
         .internalMethod03514(Insets.internalMethod00172(2.0F))
         .internalMethod09018(7.0F)
         .internalMethod07178(
            (localValue0, localValue1x) -> {
               localValue0.drawBlurredRect(
                  localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 5.0F, 3.0F, CornerRadii.internalMethod03908(7.0F), ThemeColors.internalField1312
               );
               localValue0.drawSquircle(
                  localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 3.0F, CornerRadii.internalMethod03908(7.0F), ThemeColors.internalField1612.mulAlpha(0.55F)
               );
            }
         );
      localValue1.internalMethod03907(this.internalMethod03210("\u0417\u0430\u043a\u0443\u043f\u043a\u0430", AutoBuyScreen.InternalType0153.internalField0422));
      localValue1.internalMethod03907(
         this.internalMethod03210("\u0410\u043a\u0442\u0438\u0432\u043d\u043e\u0441\u0442\u044c", AutoBuyScreen.InternalType0153.internalField0423)
      );
      localValue1.internalMethod03907(
         this.internalMethod03210("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438", AutoBuyScreen.InternalType0153.internalField1149)
      );
      return localValue1;
   }

   private UiElement internalMethod03210(String localValue1, AutoBuyScreen.InternalType0153 localValue2) {
      UiElement localValue3 = new UiElement()
         .text(
            Fonts.internalField1157.internalMethod01432(8.0F),
            localValue1,
            localValue2x -> this.internalField0422 == localValue2 ? ThemeColors.internalField1613 : ThemeColors.internalField1613.mulAlpha(0.5F)
         )
         .padding(Insets.internalMethod05266(4.0F, 10.0F))
         .radius(5.0F)
         .cursor(CursorType.internalField0567);
      localValue3.background(
         localValue2x -> this.internalField0422 == localValue2 ? ThemeColors.internalField1614 : ThemeColors.internalField1312.mulAlpha(0.05F * localValue2x.hover())
      );
      localValue3.onClick(() -> this.internalField0422 = localValue2);
      return localValue3;
   }

   private UiContainer internalMethod00470(float localValue1, String localValue2) {
      return new UiContainer()
         .internalMethod01863()
         .internalMethod09339(localValue1)
         .internalMethod09186()
         .internalMethod03062(4.0F)
         .internalMethod03514(Insets.internalMethod00105(32.0F, 11.0F, 11.0F, 11.0F))
         .internalMethod07914(internalField0918)
         .internalMethod05305(internalField0913)
         .internalMethod09801()
         .internalMethod07178((localValue2x, localValue3) -> this.internalMethod03076(localValue2x, localValue3, localValue2));
   }

   private void internalMethod03076(UiRenderContext localValue1, UiContainer localValue2, String localValue3) {
      float localValue4 = localValue2.x();
      float localValue5 = localValue2.y();
      float localValue6 = localValue2.w();
      float localValue7 = localValue2.h();
      localValue1.drawShadow(localValue4, localValue5, localValue6, localValue7, 25.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.15F));
      localValue1.drawBlurredRect(localValue4, localValue5, localValue6, localValue7, 5.0F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1312);
      localValue1.drawClientRect(localValue4, localValue5, localValue6, localValue7, 1.0F, 0.0F, 3.0F, 11.0F, true);
      localValue1.drawText(Fonts.internalField1157.internalMethod01432(8.0F), localValue3, localValue4 + 11.0F, localValue5 + 9.5F, ThemeColors.internalField1613);
      localValue1.drawRect(localValue4 + 1.0F, localValue5 + 25.0F - 1.0F, localValue6 - 2.0F, 1.0F, ThemeColors.internalField1616);
   }

   private UiContainer internalMethod01579(float localValue1) {
      return new UiContainer()
         .internalMethod01863()
         .internalMethod03062(4.0F)
         .internalMethod09609()
         .internalMethod09266(localValue1)
         .internalMethod08755()
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(2.0F)
               .internalMethod02066(1.0F, 4.0F)
               .internalMethod00894(2.0F)
               .internalMethod04404(
                  localValue0x -> ColorRGBA.BLACK
                     .mix(ColorRGBA.WHITE, 0.3F)
                     .withAlpha(255.0F * (0.3F + 0.3F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
               )
         );
   }

   private List<UiNode> internalMethod05086(AutoBuyScreen.InternalType0153 localValue1) {
      this.internalField0635 = this.internalField1257 = this.internalField1254 = this.internalField1255 = this.internalField1256 = this.internalField1586 = null;
      ArrayList localValue2 = new ArrayList();
      switch (localValue1) {
         case internalField0422:
            localValue2.add(this.internalMethod02679());
            localValue2.add(this.internalMethod08447());
            localValue2.add(this.internalMethod08461());
            break;
         case internalField0423:
            localValue2.add(this.internalMethod09547());
            break;
         case internalField1149:
            localValue2.add(this.internalMethod09501());
      }

      return localValue2;
   }

   private UiContainer internalMethod02679() {
      UiContainer localValue1 = this.internalMethod00470(160.0F, "\u0417\u0430\u043a\u0443\u043f\u043a\u0430");
      this.internalField1257 = this.internalMethod01579(193.0F);
      localValue1.internalMethod03907(this.internalField1257);
      return localValue1;
   }

   private UiContainer internalMethod05684(AuctionItem.InternalType0159 localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(6.0F)
         .internalMethod09609()
         .internalMethod09266(26.0F)
         .internalMethod03514(Insets.internalMethod02086(1.0F))
         .internalMethod09018(5.0F)
         .internalMethod06812(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.04F * localValue0.hover()));
      localValue2.internalMethod03907(
         new UiElement()
            .size(17.0F, 17.0F)
            .paint(
               (localValue1x, localValue2x) -> internalMethod00586(
                  localValue1x, localValue1.internalMethod00723(), localValue2x.x() + localValue2x.w() / 2.0F - 6.8F, localValue2x.y() + localValue2x.h() / 2.0F - 6.8F, 0.85F
               )
            )
      );
      UiContainer localValue3 = new UiContainer().internalMethod01863().internalMethod03062(1.0F).internalMethod09609();
      localValue3.internalMethod03907(
         new UiElement()
            .text(Fonts.internalField0449.internalMethod01432(8.0F), internalMethod03539(localValue1), ThemeColors.internalField1613)
            .fillWidth()
            .height(9.0F)
            .fade()
      );
      localValue3.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1154.internalMethod01432(7.0F),
               () -> internalMethod03038(localValue1),
               localValue0 -> ThemeColors.internalField1613.mulAlpha(0.5F)
            )
            .fillWidth()
            .height(7.0F)
            .fade()
      );
      localValue2.internalMethod03907(localValue3);
      localValue2.internalMethod03907(
         new UiElement()
            .size(14.0F, 14.0F)
            .cursor(CursorType.internalField0567)
            .onClick(() -> AuctionItem.internalMethod04444(localValue1.internalMethod00891()))
            .paint((localValue0, localValue1x) -> internalMethod03116(localValue0, localValue1x, 3.5F, 1.4F, ThemeColors.internalField1613.mulAlpha(0.35F + 0.4F * localValue1x.hover())))
      );
      return localValue2;
   }

   private UiContainer internalMethod08447() {
      UiContainer localValue1 = this.internalMethod00470(216.0F, "\u041a\u0430\u0442\u0430\u043b\u043e\u0433");
      localValue1.internalMethod03907(
         new TextInputField(Fonts.internalField1154.internalMethod01432(8.0F), this.internalField0248, localValue1x -> this.internalField0248 = localValue1x)
            .internalMethod01789("\u041f\u043e\u0438\u0441\u043a\u2026")
            .internalMethod06744()
            .internalMethod07996(15.0F)
            .internalMethod01901(4.0F)
            .internalMethod06836(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.07F))
            .internalMethod03161(3.0F)
            .internalMethod03704(ThemeColors.internalField1613)
      );
      localValue1.internalMethod03907(this.internalMethod08426());
      this.internalField0635 = this.internalMethod01579(154.0F);
      localValue1.internalMethod03907(this.internalField0635);
      return localValue1;
   }

   private UiContainer internalMethod08426() {
      UiContainer localValue1 = new UiContainer().internalMethod05895().internalMethod03062(3.0F).internalMethod09266(16.0F);

      for (DonItemCatalog.InternalType0189 localValue5 : DonItemCatalog.InternalType0189.values()) {
         UiElement localValue6 = new UiElement()
            .text(
               Fonts.internalField0449.internalMethod01432(7.0F),
               localValue5.internalMethod00534(),
               localValue2 -> this.internalField0406 == localValue5 ? ThemeColors.internalField1611 : ThemeColors.internalField1613.mulAlpha(0.45F)
            )
            .padding(Insets.internalMethod05266(3.0F, 7.0F))
            .radius(4.0F)
            .cursor(CursorType.internalField0567);
         localValue6.background(
            localValue2 -> this.internalField0406 == localValue5 ? ThemeColors.internalField1310 : ThemeColors.internalField1312.mulAlpha(0.05F * localValue2.hover())
         );
         localValue6.onClick(() -> this.internalField0406 = localValue5);
         localValue1.internalMethod03907(localValue6);
      }

      return localValue1;
   }

   private List<UiNode> internalMethod00623() {
      ArrayList localValue1 = new ArrayList();
      String localValue2 = this.internalField0248.trim().toLowerCase();

      for (DonItemCatalog.InternalType0503 localValue4 : DonItemCatalog.internalMethod02566(this.internalField0406)) {
         List localValue5 = localValue4.internalMethod07260().stream().filter(localValue1x -> localValue2.isEmpty() || internalMethod04793(localValue1x).toLowerCase().contains(localValue2)).toList();
         if (!localValue5.isEmpty()) {
            localValue1.add(
               new UiElement()
                  .text(
                     Fonts.internalField1157.internalMethod01432(7.0F),
                     localValue4.internalMethod00725().toUpperCase(),
                     localValue0 -> ThemeColors.internalField1613.mulAlpha(0.45F)
                  )
                  .fillWidth()
                  .height(10.0F)
                  .textInset(1.0F)
            );
            UiContainer localValue6 = new UiContainer().internalMethod02070(5).internalMethod03062(3.0F).internalMethod09609();

            for (DonItemCatalog.InternalType0190 localValue8 : (Iterable<DonItemCatalog.InternalType0190>)(Iterable<?>)localValue5) {
               localValue6.internalMethod03907(this.internalMethod06842(localValue8));
            }

            localValue1.add(localValue6);
         }
      }

      if (localValue1.isEmpty()) {
         localValue1.add(
            this.internalMethod00127(
               localValue2.isEmpty()
                  ? "\u041d\u0435\u0442 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432."
                  : "\u041d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e."
            )
         );
      }

      return localValue1;
   }

   private UiContainer internalMethod06842(DonItemCatalog.InternalType0190 localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod09266(40.0F)
         .internalMethod09018(7.0F)
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03514(Insets.internalMethod00105(6.0F, 2.0F, 4.0F, 2.0F))
         .internalMethod05305(internalField0913);
      localValue2.internalMethod06812(
         localValue2x -> this.internalMethod02433(localValue1)
            ? ThemeColors.internalField1310.mulAlpha(0.22F)
            : ThemeColors.internalField1312.mulAlpha(0.04F + 0.05F * localValue2x.hover())
      );
      localValue2.internalMethod07178(
         (localValue2x, localValue3x) -> {
            if (this.internalMethod02619(localValue1)) {
               localValue2x.drawRoundedRect(
                  localValue3x.x() + localValue3x.w() - 7.0F, localValue3x.y() + 5.0F, 3.0F, 3.0F, CornerRadii.internalMethod03908(1.5F), ThemeColors.internalField1310
               );
            }
         }
      );
      localValue2.internalMethod03907(
         new UiElement()
            .fillWidth()
            .height(18.0F)
            .paint(
               (localValue1x, localValue2x) -> internalMethod00586(
                  localValue1x, localValue1.internalMethod05752(), localValue2x.x() + localValue2x.w() / 2.0F - 8.0F, localValue2x.y() + localValue2x.h() / 2.0F - 8.0F, 1.0F
               )
            )
      );
      UiElement localValue3 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(6.0F),
            internalMethod04793(localValue1),
            localValue2x -> this.internalMethod02433(localValue1) ? ThemeColors.internalField1613 : ThemeColors.internalField1613.mulAlpha(0.8F)
         )
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .fillHeight()
         .textInset(1.0F);
      if (Fonts.internalField1154.internalMethod01432(6.0F).internalMethod00965(internalMethod04793(localValue1)) > 30.0F) {
         localValue3.fade();
      }

      localValue2.internalMethod03907(localValue3);
      localValue2.internalMethod05690(() -> {
         this.internalField0408 = localValue1;
         this.internalField0229 = 25000L;
         this.internalField0230 = 0L;
         this.internalField0227 = 1;
         this.internalField0228 = 10;
         this.internalField0499.clear();
         this.internalField0498.clear();

         for (StatusEffectInstance localValue3x : PotionItemUtils.internalMethod00984(localValue1.internalMethod05752())) {
            this.internalField0498.put(localValue3x.getEffectType(), localValue3x.getAmplifier() + 1);
         }

         this.internalField1099 = false;
         this.internalField1100 = false;
      });
      return localValue2;
   }

   private UiContainer internalMethod08461() {
      UiContainer localValue1 = this.internalMethod00470(166.0F, "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430");
      UiContainer localValue2 = this.internalMethod01579(193.0F);
      localValue1.internalMethod03907(localValue2);
      UiElement localValue3 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(8.0F),
            "\u0412\u044b\u0431\u0435\u0440\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u043a\u0430\u0442\u0430\u043b\u043e\u0433\u0435",
            localValue0 -> ThemeColors.internalField1613.mulAlpha(0.4F)
         )
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .height(60.0F);
      localValue3.visibleWhen(() -> this.internalField0408 == null);
      localValue2.internalMethod03907(localValue3);
      UiContainer localValue4 = new UiContainer().internalMethod01863().internalMethod03062(6.0F).internalMethod09609();
      localValue4.internalMethod03855(() -> this.internalField0408 != null);
      UiContainer localValue5 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09609()
         .internalMethod09266(16.0F);
      localValue5.internalMethod03907(new UiElement().size(14.0F, 16.0F).paint((localValue1x, localValue2x) -> {
         if (this.internalField0408 != null) {
            internalMethod00586(localValue1x, this.internalField0408.internalMethod05752(), localValue2x.x(), localValue2x.y() + localValue2x.h() / 2.0F - 6.8F, 0.85F);
         }
      }));
      localValue5.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField0449.internalMethod01432(8.0F),
               () -> this.internalField0408 == null ? "" : internalMethod04793(this.internalField0408),
               localValue0 -> ThemeColors.internalField1613
            )
            .fillWidth()
            .height(10.0F)
            .fade()
      );
      localValue4.internalMethod03907(localValue5);
      localValue4.internalMethod03907(this.internalMethod06771());
      localValue4.internalMethod03907(
         this.internalMethod00207(
            "\u041d\u0438\u0436\u0435 \u0440\u044b\u043d\u043a\u0430",
            new ToggleSwitchWidget(() -> this.internalField0276).size(15.0F, 9.0F).onClick(() -> this.internalField0276 = !this.internalField0276)
         )
      );
      UiContainer localValue6 = this.internalMethod00207(
         "\u041c\u0430\u043a\u0441. \u0446\u0435\u043d\u0430",
         new NumberInputField(
               Fonts.internalField0449.internalMethod01432(8.0F), () -> (float)this.internalField0229, localValue1x -> this.internalField0229 = (long)localValue1x, 0.0F, 1.0E9F
            )
            .internalMethod03023(() -> "$")
            .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
            .internalMethod05286(ThemeColors.internalField1613)
      );
      localValue6.internalMethod03855(() -> !this.internalField0276);
      localValue4.internalMethod03907(localValue6);
      UiContainer localValue7 = this.internalMethod00207(
         "\u041f\u0440\u043e\u0446\u0435\u043d\u0442 \u043d\u0438\u0436\u0435",
         new NumberInputField(Fonts.internalField0449.internalMethod01432(8.0F), () -> (float)this.internalField0194, localValue1x -> this.internalField0194 = localValue1x, 0.0F, 95.0F)
            .internalMethod03023(() -> "%")
            .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
            .internalMethod05286(ThemeColors.internalField1613)
      );
      localValue7.internalMethod03855(() -> this.internalField0276);
      localValue4.internalMethod03907(localValue7);
      this.internalField1586 = new UiContainer().internalMethod01863().internalMethod03062(3.0F).internalMethod09609();
      this.internalField1586.internalMethod03855(this::internalMethod02714);
      localValue4.internalMethod03907(this.internalField1586);
      UiElement localValue8 = new UiElement()
         .text(Fonts.internalField0449.internalMethod01432(8.0F), "+ \u042d\u0444\u0444\u0435\u043a\u0442", localValue0 -> ThemeColors.internalField1310)
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .height(15.0F)
         .radius(5.0F)
         .cursor(CursorType.internalField0567);
      localValue8.background(localValue0 -> ThemeColors.internalField1310.mulAlpha(0.12F + 0.1F * localValue0.hover()));
      localValue8.onClick(() -> {
         this.internalField1077 = "";
         this.internalField1100 = true;
      });
      localValue8.visibleWhen(this::internalMethod02714);
      localValue4.internalMethod03907(localValue8);
      UiContainer localValue9 = this.internalMethod00207(
         "\u0421\u0442\u0440\u043e\u0433\u0438\u0435 \u0437\u0430\u0447\u0430\u0440\u044b",
         new ToggleSwitchWidget(() -> this.internalField1099).size(15.0F, 9.0F).onClick(() -> this.internalField1099 = !this.internalField1099)
      );
      localValue9.internalMethod03855(this::internalMethod02717);
      localValue4.internalMethod03907(localValue9);
      this.internalField1256 = new UiContainer().internalMethod01863().internalMethod03062(3.0F).internalMethod09609();
      this.internalField1256.internalMethod03855(this::internalMethod02717);
      localValue4.internalMethod03907(this.internalField1256);
      UiElement localValue10 = new UiElement()
         .text(
            Fonts.internalField0449.internalMethod01432(8.0F),
            "+ \u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u0435",
            localValue0 -> ThemeColors.internalField1310
         )
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .height(15.0F)
         .radius(5.0F)
         .cursor(CursorType.internalField0567);
      localValue10.background(localValue0 -> ThemeColors.internalField1310.mulAlpha(0.12F + 0.1F * localValue0.hover()));
      localValue10.onClick(() -> {
         this.internalField1077 = "";
         this.internalField1100 = true;
      });
      localValue10.visibleWhen(this::internalMethod02717);
      localValue4.internalMethod03907(localValue10);
      localValue4.internalMethod03907(this.internalMethod06771());
      localValue4.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1157.internalMethod01432(7.0F),
               "\u041f\u0420\u041e\u0414\u0410\u0416\u0410",
               localValue0 -> ThemeColors.internalField1613.mulAlpha(0.45F)
            )
            .fillWidth()
            .height(9.0F)
            .textInset(1.0F)
      );
      localValue4.internalMethod03907(
         this.internalMethod00207(
            "\u0426\u0435\u043d\u0430 \u043f\u0440\u043e\u0434\u0430\u0436\u0438",
            new NumberInputField(
                  Fonts.internalField0449.internalMethod01432(8.0F), () -> (float)this.internalField0230, localValue1x -> this.internalField0230 = (long)localValue1x, 0.0F, 1.0E9F
               )
               .internalMethod03023(() -> this.internalField0230 > 0L ? "$" : "")
               .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
               .internalMethod05286(ThemeColors.internalField1613)
               .internalMethod05772(() -> this.internalField0230 > 0L ? TextUtils.internalMethod00670((float)this.internalField0230) : "\u0440\u044b\u043d\u043e\u043a")
         )
      );
      localValue4.internalMethod03907(
         this.internalMethod00207(
            "\u041a\u043e\u043b-\u0432\u043e \u0432 \u043b\u043e\u0442\u0435",
            new NumberInputField(
                  Fonts.internalField0449.internalMethod01432(8.0F),
                  () -> this.internalField0227,
                  localValue1x -> this.internalField0227 = Math.max(1, (int)localValue1x),
                  1.0F,
                  64.0F
               )
               .internalMethod03023(() -> " \u0448\u0442")
               .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
               .internalMethod05286(ThemeColors.internalField1613)
         )
      );
      localValue4.internalMethod03907(
         this.internalMethod00207(
            "\u041f\u043e\u0440\u043e\u0433 \u043f\u0435\u0440\u0435\u043f\u0440\u043e\u0434\u0430\u0436\u0438",
            new NumberInputField(
                  Fonts.internalField0449.internalMethod01432(8.0F),
                  () -> this.internalField0228,
                  localValue1x -> this.internalField0228 = Math.max(1, (int)localValue1x),
                  1.0F,
                  999.0F
               )
               .internalMethod03023(() -> " \u0448\u0442")
               .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
               .internalMethod05286(ThemeColors.internalField1613)
         )
      );
      localValue4.internalMethod03907(this.internalMethod06771());
      UiElement localValue11 = new UiElement()
         .text(
            Fonts.internalField1157.internalMethod01432(8.0F),
            "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0432 \u0437\u0430\u043a\u0443\u043f\u043a\u0443",
            ThemeColors.internalField1611
         )
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .height(20.0F)
         .radius(6.0F)
         .cursor(CursorType.internalField0567);
      localValue11.background(localValue0 -> ThemeColors.internalField1310.mulAlpha(localValue0.hovered() ? 1.0F : 0.85F));
      localValue11.onClick(this::internalMethod02713);
      localValue4.internalMethod03907(localValue11);
      localValue2.internalMethod03907(localValue4);
      return localValue1;
   }

   private UiContainer internalMethod00207(String localValue1, UiNode localValue2) {
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09609()
         .internalMethod09266(14.0F)
         .internalMethod03907(
            new UiElement().text(Fonts.internalField1154.internalMethod01432(8.0F), localValue1, localValue0 -> ThemeColors.internalField1613.mulAlpha(0.6F))
         )
         .internalMethod03907(localValue2);
   }

   private void internalMethod02713() {
      if (this.internalField0408 != null) {
         ItemStack localValue1 = this.internalField0408.internalMethod05752().copy();
         if (localValue1.getItem() instanceof PotionItem && !this.internalField0498.isEmpty()) {
            ArrayList localValue2 = new ArrayList();
            this.internalField0498.forEach((localValue1x, localValue2x) -> localValue2.add(new StatusEffectInstance(localValue1x, 3600, Math.max(0, localValue2x - 1))));
            localValue1.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), localValue2, Optional.empty()));
         }

         if (!this.internalField0499.isEmpty()) {
            Builder localValue3 = new Builder(ItemEnchantmentsComponent.DEFAULT);
            this.internalField0499.forEach(localValue3::set);
            localValue1.set(localValue1.isOf(Items.ENCHANTED_BOOK) ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS, localValue3.build());
         }

         AuctionItem.internalMethod02533(
            localValue1,
            this.internalField0229,
            this.internalField0408.internalMethod06194(),
            this.internalField0408.internalMethod02702(),
            this.internalField0276 ? AuctionItem.InternalType0158.internalField0591 : AuctionItem.InternalType0158.internalField0592,
            this.internalField0194,
            this.internalField1099,
            this.internalField0230,
            this.internalField0227,
            this.internalField0228
         );
         this.internalField0408 = null;
      }
   }

   private boolean internalMethod02714() {
      return this.internalField0408 != null && this.internalField0408.internalMethod05752().getItem() instanceof PotionItem;
   }

   private boolean internalMethod02717() {
      if (this.internalField0408 == null) {
         return false;
      } else {
         ItemStack localValue1 = this.internalField0408.internalMethod05752();
         return localValue1.isDamageable() || localValue1.isOf(Items.ENCHANTED_BOOK) || localValue1.isOf(Items.BOOK);
      }
   }

   private static String internalMethod04247(RegistryEntry<Enchantment> localValue0) {
      return ((Enchantment)localValue0.value()).description().getString();
   }

   private List<RegistryEntry<Enchantment>> internalMethod06344() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.world == null) {
         return List.of();
      } else {
         ArrayList localValue2 = new ArrayList();
         localValue1.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).streamEntries().forEach(localValue2::add);
         return localValue2;
      }
   }

   private UiContainer internalMethod09065() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod09339(190.0F)
         .internalMethod09266(190.0F)
         .internalMethod09018(9.0F)
         .internalMethod03062(7.0F)
         .internalMethod03514(Insets.internalMethod00172(9.0F))
         .internalMethod08296((this.width - 190) / 2.0F, (this.height - 190) / 2.0F)
         .internalMethod03855(() -> this.internalField1100)
         .internalMethod07178(
            (localValue0, localValue1x) -> {
               localValue0.drawShadow(
                  localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 25.0F, CornerRadii.internalMethod03908(9.0F), ThemeColors.internalField1309.mulAlpha(0.2F)
               );
               localValue0.drawBlurredRect(
                  localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 5.0F, 3.0F, CornerRadii.internalMethod03908(9.0F), ThemeColors.internalField1312
               );
               localValue0.drawClientRect(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 1.0F, 0.0F, 3.0F, 9.0F, true);
            }
         );
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09609()
         .internalMethod09266(12.0F);
      localValue2.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1157.internalMethod01432(8.0F),
               () -> this.internalMethod02714() ? "\u042d\u0444\u0444\u0435\u043a\u0442" : "\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u0435",
               localValue0 -> ThemeColors.internalField1613
            )
      );
      localValue2.internalMethod03907(
         new UiElement()
            .size(11.0F, 11.0F)
            .cursor(CursorType.internalField0567)
            .onClick(() -> this.internalField1100 = false)
            .paint((localValue0, localValue1x) -> internalMethod03116(localValue0, localValue1x, 3.0F, 1.3F, ThemeColors.internalField1613.mulAlpha(0.45F + 0.4F * localValue1x.hover())))
      );
      localValue1.internalMethod03907(localValue2);
      localValue1.internalMethod03907(
         new TextInputField(Fonts.internalField1154.internalMethod01432(8.0F), "", localValue1x -> this.internalField1077 = localValue1x)
            .internalMethod01789("\u041f\u043e\u0438\u0441\u043a\u2026")
            .internalMethod06744()
            .internalMethod07996(15.0F)
            .internalMethod01901(4.0F)
            .internalMethod06836(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.07F))
            .internalMethod03161(5.0F)
            .internalMethod03704(ThemeColors.internalField1613)
      );
      this.internalField1585 = this.internalMethod01579(131.0F);
      localValue1.internalMethod03907(this.internalField1585);
      return localValue1;
   }

   private UiElement internalMethod06746(RegistryEntry<Enchantment> localValue1) {
      UiElement localValue2 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(7.0F),
            internalMethod04247(localValue1),
            localValue2x -> this.internalField0499.containsKey(localValue1) ? ThemeColors.internalField1310 : ThemeColors.internalField1613.mulAlpha(0.85F)
         )
         .fillWidth()
         .height(14.0F)
         .radius(4.0F)
         .textInset(4.0F)
         .cursor(CursorType.internalField0567);
      localValue2.background(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.05F * localValue0.hover()));
      localValue2.onClick(() -> {
         this.internalField0499.putIfAbsent(localValue1, 1);
         this.internalField1100 = false;
      });
      return localValue2;
   }

   private UiContainer internalMethod06748(RegistryEntry<Enchantment> localValue1) {
      int localValue2 = Math.max(1, ((Enchantment)localValue1.value()).getMaxLevel());
      UiContainer localValue3 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09609()
         .internalMethod09266(13.0F);
      localValue3.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1154.internalMethod01432(7.0F), internalMethod04247(localValue1), localValue0 -> ThemeColors.internalField1613.mulAlpha(0.85F)
            )
            .fillWidth()
            .height(9.0F)
            .fade()
      );
      UiContainer localValue4 = new UiContainer().internalMethod05895().internalMethod01855(TextAlignment.internalField0621).internalMethod03062(5.0F);
      localValue4.internalMethod03907(
         new NumberInputField(
               Fonts.internalField0449.internalMethod01432(8.0F),
               () -> this.internalField0499.getOrDefault(localValue1, 1).intValue(),
               localValue3x -> this.internalField0499.put(localValue1, Math.max(1, Math.min(localValue2, (int)localValue3x))),
               1.0F,
               localValue2
            )
            .internalMethod05286(ThemeColors.internalField1613)
      );
      localValue4.internalMethod03907(
         new UiElement()
            .size(9.0F, 11.0F)
            .cursor(CursorType.internalField0567)
            .onClick(() -> this.internalField0499.remove(localValue1))
            .paint((localValue0, localValue1x) -> internalMethod03116(localValue0, localValue1x, 2.6F, 1.2F, ThemeColors.internalField1613.mulAlpha(0.4F + 0.4F * localValue1x.hover())))
      );
      localValue3.internalMethod03907(localValue4);
      return localValue3;
   }

   private List<RegistryEntry<StatusEffect>> internalMethod08890() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.world == null) {
         return List.of();
      } else {
         ArrayList localValue2 = new ArrayList();
         localValue1.world.getRegistryManager().getOrThrow(RegistryKeys.STATUS_EFFECT).streamEntries().forEach(localValue2::add);
         return localValue2;
      }
   }

   private static String internalMethod01969(RegistryEntry<StatusEffect> localValue0) {
      return ((StatusEffect)localValue0.value()).getName().getString();
   }

   private UiElement internalMethod03060(RegistryEntry<StatusEffect> localValue1) {
      UiElement localValue2 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(7.0F),
            internalMethod01969(localValue1),
            localValue2x -> this.internalField0498.containsKey(localValue1) ? ThemeColors.internalField1310 : ThemeColors.internalField1613.mulAlpha(0.85F)
         )
         .fillWidth()
         .height(14.0F)
         .radius(4.0F)
         .textInset(4.0F)
         .cursor(CursorType.internalField0567);
      localValue2.background(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.05F * localValue0.hover()));
      localValue2.onClick(() -> {
         this.internalField0498.putIfAbsent(localValue1, 1);
         this.internalField1100 = false;
      });
      return localValue2;
   }

   private UiContainer internalMethod03063(RegistryEntry<StatusEffect> localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod09609()
         .internalMethod09266(13.0F);
      localValue2.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1154.internalMethod01432(7.0F), internalMethod01969(localValue1), localValue0 -> ThemeColors.internalField1613.mulAlpha(0.85F)
            )
            .fillWidth()
            .height(9.0F)
            .fade()
      );
      UiContainer localValue3 = new UiContainer().internalMethod05895().internalMethod01855(TextAlignment.internalField0621).internalMethod03062(5.0F);
      localValue3.internalMethod03907(
         new NumberInputField(
               Fonts.internalField0449.internalMethod01432(8.0F),
               () -> this.internalField0498.getOrDefault(localValue1, 1).intValue(),
               localValue2x -> this.internalField0498.put(localValue1, Math.max(1, Math.min(255, (int)localValue2x))),
               1.0F,
               255.0F
            )
            .internalMethod05286(ThemeColors.internalField1613)
      );
      localValue3.internalMethod03907(
         new UiElement()
            .size(9.0F, 11.0F)
            .cursor(CursorType.internalField0567)
            .onClick(() -> this.internalField0498.remove(localValue1))
            .paint((localValue0, localValue1x) -> internalMethod03116(localValue0, localValue1x, 2.6F, 1.2F, ThemeColors.internalField1613.mulAlpha(0.4F + 0.4F * localValue1x.hover())))
      );
      localValue2.internalMethod03907(localValue3);
      return localValue2;
   }

   private UiContainer internalMethod09547() {
      UiContainer localValue1 = this.internalMethod00470(300.0F, "\u0410\u043a\u0442\u0438\u0432\u043d\u043e\u0441\u0442\u044c");
      this.internalField1254 = this.internalMethod01579(193.0F);
      localValue1.internalMethod03907(this.internalField1254);
      return localValue1;
   }

   private UiContainer internalMethod07624(AutoBuyModule.InternalType0475 localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09609()
         .internalMethod09266(24.0F)
         .internalMethod03514(Insets.internalMethod02086(1.0F));
      localValue2.internalMethod03907(
         new UiElement()
            .size(17.0F, 17.0F)
            .paint(
               (localValue1x, localValue2x) -> internalMethod00586(
                  localValue1x, localValue1.internalMethod02102(), localValue2x.x() + localValue2x.w() / 2.0F - 6.8F, localValue2x.y() + localValue2x.h() / 2.0F - 6.8F, 0.85F
               )
            )
      );
      localValue2.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField0449.internalMethod01432(8.0F),
               localValue1.internalMethod00336() + (localValue1.internalMethod07392() > 1 ? " x" + localValue1.internalMethod07392() : ""),
               ThemeColors.internalField1613
            )
            .fillWidth()
            .height(9.0F)
            .fade()
      );
      localValue2.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1157.internalMethod01432(8.0F),
               "-" + MathUtils.internalMethod00005(localValue1.internalMethod07393()),
               localValue0 -> new ColorRGBA(255.0F, 105.0F, 105.0F)
            )
            .textAlign(TextAlignment.internalField1242)
            .width(58.0F)
            .height(9.0F)
      );
      return localValue2;
   }

   private UiContainer internalMethod09501() {
      UiContainer localValue1 = this.internalMethod00470(264.0F, "\u0410\u043d\u0430\u0440\u0445\u0438\u0438 \u0438 \u0442\u0430\u0439\u043c\u0438\u043d\u0433\u0438");
      UiContainer localValue2 = this.internalMethod01579(193.0F).internalMethod03062(8.0F);
      localValue2.internalMethod03907(
         this.internalMethod00207(
            "\u0410\u0432\u0442\u043e\u043f\u0440\u043e\u0434\u0430\u0436\u0430 \u043a\u0443\u043f\u043b\u0435\u043d\u043d\u043e\u0433\u043e",
            new ToggleSwitchWidget(AuctionAutoParseConfig::internalMethod05797)
               .size(15.0F, 9.0F)
               .onClick(() -> AuctionAutoParseConfig.internalMethod06082(!AuctionAutoParseConfig.internalMethod05797()))
         )
      );
      localValue2.internalMethod03907(
         this.internalMethod00207(
            "\u0410\u0432\u0442\u043e\u043f\u0430\u0440\u0441 \u0446\u0435\u043d",
            new ToggleSwitchWidget(AuctionAutoParseConfig::internalMethod05800)
               .size(15.0F, 9.0F)
               .onClick(() -> AuctionAutoParseConfig.internalMethod06151(!AuctionAutoParseConfig.internalMethod05800()))
         )
      );
      UiContainer localValue3 = this.internalMethod00207(
         "\u041f\u0435\u0440\u0438\u043e\u0434 \u043f\u0430\u0440\u0441\u0430",
         new NumberInputField(
               Fonts.internalField0449.internalMethod01432(8.0F),
               () -> (float)AuctionAutoParseConfig.internalMethod08854() / 60000.0F,
               localValue0 -> AuctionAutoParseConfig.internalMethod09110((long)(localValue0 * 60000.0F)),
               1.0F,
               120.0F
            )
            .internalMethod03023(() -> " \u043c\u0438\u043d")
            .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
            .internalMethod05286(ThemeColors.internalField1613)
      );
      localValue3.internalMethod03855(AuctionAutoParseConfig::internalMethod05800);
      localValue2.internalMethod03907(localValue3);
      UiElement localValue4 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(7.0F),
            AutoBuyScreen::internalMethod04486,
            localValue0 -> ThemeColors.internalField1613.mulAlpha(0.5F)
         )
         .fillWidth()
         .height(9.0F)
         .fade();
      localValue4.visibleWhen(AuctionAutoParseConfig::internalMethod05800);
      localValue2.internalMethod03907(localValue4);
      localValue2.internalMethod03907(this.internalMethod06771());
      this.internalField1255 = new UiContainer().internalMethod05895().internalMethod07971().internalMethod03062(4.0F).internalMethod09609();
      localValue2.internalMethod03907(this.internalField1255);
      SizedFont localValue5 = Fonts.internalField0449.internalMethod01432(8.0F);
      float localValue6 = Math.max(0.0F, 9.0F - (19.0F - localValue5.internalMethod04890()) / 2.0F);
      UiContainer localValue7 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(6.0F)
         .internalMethod09609()
         .internalMethod09266(19.0F);
      localValue7.internalMethod03907(
         new TextInputField(localValue5, "", localValue1x -> this.internalField0247 = localValue1x)
            .internalMethod01789("\u043d\u0430\u043f\u0440. 208")
            .internalMethod04564(true)
            .internalMethod06323(4)
            .internalMethod06744()
            .internalMethod07996(19.0F)
            .internalMethod01901(6.0F)
            .internalMethod06836(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.07F))
            .internalMethod03161(localValue6)
            .internalMethod03704(ThemeColors.internalField1613)
      );
      localValue7.internalMethod03907(
         new UiElement()
            .text(
               Fonts.internalField1157.internalMethod01432(8.0F),
               "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c",
               ThemeColors.internalField1611
            )
            .textAlign(TextAlignment.internalField0621)
            .height(19.0F)
            .padding(Insets.internalMethod02086(12.0F))
            .radius(6.0F)
            .cursor(CursorType.internalField0567)
            .background(localValue0 -> ThemeColors.internalField1310.mulAlpha(localValue0.hovered() ? 1.0F : 0.85F))
            .onClick(this::internalMethod02716)
      );
      localValue2.internalMethod03907(localValue7);
      localValue2.internalMethod03907(this.internalMethod06771());
      localValue2.internalMethod03907(
         this.internalMethod00207(
            "\u041f\u0435\u0440\u0438\u043e\u0434 \u0441\u043c\u0435\u043d\u044b",
            new NumberInputField(
                  Fonts.internalField0449.internalMethod01432(8.0F),
                  () -> (float)AuctionAutoParseConfig.internalMethod05796() / 60000.0F,
                  localValue0 -> AuctionAutoParseConfig.internalMethod06081((long)(localValue0 * 60000.0F)),
                  1.0F,
                  60.0F
               )
               .internalMethod03023(() -> " \u043c\u0438\u043d")
               .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
               .internalMethod05286(ThemeColors.internalField1613)
         )
      );
      localValue2.internalMethod03907(
         this.internalMethod00207(
            "\u041f\u0430\u0443\u0437\u0430 \u043f\u0440\u043e\u0433\u0440\u0443\u0437\u043a\u0438",
            new NumberInputField(
                  Fonts.internalField0449.internalMethod01432(8.0F),
                  () -> (float)AuctionAutoParseConfig.internalMethod05799() / 1000.0F,
                  localValue0 -> AuctionAutoParseConfig.internalMethod06150((long)(localValue0 * 1000.0F)),
                  1.0F,
                  60.0F
               )
               .internalMethod03023(() -> " \u0441")
               .internalMethod02106(ThemeColors.internalField1613.mulAlpha(0.5F))
               .internalMethod05286(ThemeColors.internalField1613)
         )
      );
      localValue1.internalMethod03907(localValue2);
      return localValue1;
   }

   private static String internalMethod04486() {
      AutoBuyModule localValue0 = RockstarClient.getInstance().getModuleManager().getModule(AutoBuyModule.class);
      if (localValue0 == null || !localValue0.isEnabled()) {
         return "\u0421\u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442 \u0442\u043e\u043b\u044c\u043a\u043e \u043f\u0440\u0438 \u0432\u043a\u043b\u044e\u0447\u0451\u043d\u043d\u043e\u0439 \u0430\u0432\u0442\u043e\u0437\u0430\u043a\u0443\u043f\u043a\u0435.";
      } else if (AuctionParserService.internalMethod07210().internalMethod02126()) {
         return "\u041f\u0430\u0440\u0441\u0438\u043d\u0433 \u0438\u0434\u0451\u0442\u2026";
      } else if (AuctionItem.internalMethod00895().isEmpty()) {
         return "\u0421\u043f\u0438\u0441\u043e\u043a \u0437\u0430\u043a\u0443\u043f\u043a\u0438 \u043f\u0443\u0441\u0442 \u2014 \u043f\u0430\u0440\u0441\u0438\u0442\u044c \u043d\u0435\u0447\u0435\u0433\u043e.";
      } else {
         long localValue1 = AuctionParserService.internalMethod07210().internalMethod02125() / 1000L;
         return "\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u043f\u0430\u0440\u0441 \u0447\u0435\u0440\u0435\u0437 "
            + localValue1 / 60L
            + ":"
            + String.format("%02d", localValue1 % 60L);
      }
   }

   private void internalMethod02716() {
      try {
         AuctionAutoParseConfig.internalMethod06080(Integer.parseInt(this.internalField0247.trim()));
      } catch (NumberFormatException localValue2) {
      }
   }

   private UiContainer internalMethod00479(int localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(3.0F)
         .internalMethod09266(19.0F)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 7.0F, 0.0F, 9.0F))
         .internalMethod09018(6.0F)
         .internalMethod06812(localValue0 -> ThemeColors.internalField1312.mulAlpha(0.07F));
      localValue2.internalMethod03907(new UiElement().text(Fonts.internalField0449.internalMethod01432(8.0F), "an" + localValue1, ThemeColors.internalField1613));
      localValue2.internalMethod03907(
         new UiElement()
            .size(9.0F, 11.0F)
            .cursor(CursorType.internalField0567)
            .onClick(() -> AuctionAutoParseConfig.internalMethod06149(localValue1))
            .paint((localValue0, localValue1x) -> internalMethod03116(localValue0, localValue1x, 2.8F, 1.3F, ThemeColors.internalField1613.mulAlpha(0.4F + 0.45F * localValue1x.hover())))
      );
      return localValue2;
   }

   private static void internalMethod00586(UiRenderContext localValue0, ItemStack localValue1, float localValue2, float localValue3, float localValue4) {
      float localValue5 = RenderSystem.getShaderColor()[3];
      if (localValue5 >= 0.999F) {
         localValue0.drawItem(localValue1, localValue2, localValue3, localValue4);
      } else {
         ScaledItemQueue.internalMethod02541(localValue0, localValue1, localValue2, localValue3, localValue4, localValue5);
      }
   }

   private static void internalMethod03116(UiRenderContext localValue0, UiElement localValue1, float localValue2, float localValue3, ColorRGBA localValue4) {
      org.joml.Matrix3x2fStack localValue5 = localValue0.getMatrices();
      localValue5.pushMatrix();
      localValue5.translate(localValue1.x() + localValue1.w() / 2.0F, localValue1.y() + localValue1.h() / 2.0F);
      localValue5.rotate((float)Math.toRadians(45.0));
      localValue0.drawRect(-localValue2, -localValue3 / 2.0F, localValue2 * 2.0F, localValue3, localValue4);
      localValue0.drawRect(-localValue3 / 2.0F, -localValue2, localValue3, localValue2 * 2.0F, localValue4);
      localValue5.popMatrix();
   }

   private void internalMethod08015() {
      if (this.internalField0422 != this.internalField0423) {
         this.internalField0423 = this.internalField0422;
         this.internalField0634.internalMethod07849(this.internalMethod05086(this.internalField0422));
         this.internalField1076 = this.internalField1079 = this.internalField1078 = this.internalField1501 = this.internalField1499 = this.internalField1498 = "";
      }

      if (this.internalField0635 != null) {
         String localValue1 = this.internalField0406.name() + "|" + this.internalField0248;
         if (!localValue1.equals(this.internalField1076)) {
            this.internalField1076 = localValue1;
            this.internalField0635.internalMethod07849(this.internalMethod00623());
         }
      }

      if (this.internalField1257 != null) {
         List localValue6 = AuctionItem.internalMethod00895();
         String localValue2 = (String)localValue6.stream().map(value -> ((AuctionItem.InternalType0159)value).internalMethod00891()).reduce("", (localValue0, localValue1x) -> localValue0 + "," + localValue1x);
         if (!localValue2.equals(this.internalField1079)) {
            this.internalField1079 = localValue2;
            ArrayList localValue3 = new ArrayList();
            if (localValue6.isEmpty()) {
               localValue3.add(
                  this.internalMethod00127(
                     "\u041f\u0443\u0441\u0442\u043e. \u0412\u044b\u0431\u0435\u0440\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u2192"
                  )
               );
            } else {
               for (AuctionItem.InternalType0159 localValue5 : (Iterable<AuctionItem.InternalType0159>)(Iterable<?>)localValue6) {
                  localValue3.add(this.internalMethod05684(localValue5));
               }
            }

            this.internalField1257.internalMethod07849(localValue3);
         }
      }

      if (this.internalField1254 != null) {
         List localValue7 = internalMethod08429();
         String localValue12 = localValue7.size() + "|" + (localValue7.isEmpty() ? "" : ((AutoBuyModule.InternalType0475)localValue7.get(localValue7.size() - 1)).internalMethod00336());
         if (!localValue12.equals(this.internalField1078)) {
            this.internalField1078 = localValue12;
            ArrayList localValue17 = new ArrayList();
            if (localValue7.isEmpty()) {
               localValue17.add(this.internalMethod00127("\u041f\u043e\u043a\u0443\u043f\u043e\u043a \u043f\u043e\u043a\u0430 \u043d\u0435\u0442."));
            } else {
               for (int localValue22 = localValue7.size() - 1; localValue22 >= 0; localValue22--) {
                  localValue17.add(this.internalMethod07624((AutoBuyModule.InternalType0475)localValue7.get(localValue22)));
               }
            }

            this.internalField1254.internalMethod07849(localValue17);
         }
      }

      if (this.internalField1255 != null) {
         List localValue8 = AuctionAutoParseConfig.internalMethod04345();
         String localValue13 = localValue8.toString();
         if (!localValue13.equals(this.internalField1501)) {
            this.internalField1501 = localValue13;
            ArrayList localValue18 = new ArrayList();

            for (int localValue28 : (Iterable<Integer>)(Iterable<?>)localValue8) {
               localValue18.add(this.internalMethod00479(localValue28));
            }

            this.internalField1255.internalMethod07849(localValue18);
         }
      }

      if (this.internalField1256 != null) {
         String localValue9 = this.internalField0499.keySet().toString();
         if (!localValue9.equals(this.internalField1499)) {
            this.internalField1499 = localValue9;
            ArrayList localValue14 = new ArrayList();

            for (RegistryEntry localValue24 : this.internalField0499.keySet()) {
               localValue14.add(this.internalMethod06748(localValue24));
            }

            this.internalField1256.internalMethod07849(localValue14);
         }
      }

      if (this.internalField1586 != null) {
         String localValue10 = this.internalField0498.keySet().toString();
         if (!localValue10.equals(this.internalField1498)) {
            this.internalField1498 = localValue10;
            ArrayList localValue15 = new ArrayList();

            for (RegistryEntry localValue25 : this.internalField0498.keySet()) {
               localValue15.add(this.internalMethod03063(localValue25));
            }

            this.internalField1586.internalMethod07849(localValue15);
         }
      }

      if (this.internalField1585 != null) {
         String localValue11 = this.internalField1100 + "|" + this.internalMethod02714() + "|" + this.internalField1077;
         if (!localValue11.equals(this.internalField1497)) {
            this.internalField1497 = localValue11;
            ArrayList localValue16 = new ArrayList();
            if (this.internalField1100) {
               String localValue21 = this.internalField1077.trim().toLowerCase();
               if (this.internalMethod02714()) {
                  for (RegistryEntry localValue30 : this.internalMethod08890()) {
                     if (localValue21.isEmpty() || internalMethod01969(localValue30).toLowerCase().contains(localValue21)) {
                        localValue16.add(this.internalMethod03060(localValue30));
                     }
                  }
               } else {
                  for (RegistryEntry localValue29 : this.internalMethod06344()) {
                     if (localValue21.isEmpty() || internalMethod04247(localValue29).toLowerCase().contains(localValue21)) {
                        localValue16.add(this.internalMethod06746(localValue29));
                     }
                  }
               }
            }

            this.internalField1585.internalMethod07849(localValue16);
         }
      }
   }

   private UiElement internalMethod06771() {
      return new UiElement()
         .fillWidth()
         .height(1.0F)
         .paint((localValue0, localValue1) -> localValue0.drawRect(localValue1.x() - 11.0F, localValue1.y(), localValue1.w() + 22.0F, 1.0F, ThemeColors.internalField1616));
   }

   private UiElement internalMethod00127(String localValue1) {
      return new UiElement()
         .text(Fonts.internalField1154.internalMethod01432(8.0F), localValue1, localValue0 -> ThemeColors.internalField1613.mulAlpha(0.4F))
         .textAlign(TextAlignment.internalField0621)
         .fillWidth()
         .height(40.0F);
   }

   private boolean internalMethod02433(DonItemCatalog.InternalType0190 localValue1) {
      return this.internalField0408 != null && this.internalField0408.internalMethod02702().equals(localValue1.internalMethod02702());
   }

   private boolean internalMethod02619(DonItemCatalog.InternalType0190 localValue1) {
      for (AuctionItem.InternalType0159 localValue3 : AuctionItem.internalMethod00895()) {
         if (localValue3.internalMethod00891().equals(localValue1.internalMethod02702())) {
            return true;
         }
      }

      return false;
   }

   private static String internalMethod04793(DonItemCatalog.InternalType0190 localValue0) {
      return localValue0.internalMethod06194() != null ? localValue0.internalMethod06194() : localValue0.internalMethod05752().getName().getString();
   }

   private static String internalMethod03539(AuctionItem.InternalType0159 localValue0) {
      return localValue0.internalMethod04343() != null ? localValue0.internalMethod04343() : localValue0.internalMethod00723().getName().getString();
   }

   private static String internalMethod03038(AuctionItem.InternalType0159 localValue0) {
      String localValue1 = localValue0.internalMethod05344() == AuctionItem.InternalType0158.internalField0591
         ? "-" + (int)localValue0.internalMethod00347() + "% \u0440\u044b\u043d\u043a\u0430"
         : "\u0434\u043e " + MathUtils.internalMethod00005(localValue0.internalMethod00349());
      long localValue2 = (long)AuctionParserService.internalMethod07210().internalMethod03919(localValue0.internalMethod00891());
      return localValue2 > 0L ? localValue1 + " \u00b7 \u0440\u044b\u043d\u043e\u043a " + MathUtils.internalMethod00005(localValue2) : localValue1;
   }

   private static List<AutoBuyModule.InternalType0475> internalMethod08429() {
      AutoBuyModule localValue0 = RockstarClient.getInstance().getModuleManager().getModule(AutoBuyModule.class);
      return localValue0 == null ? List.of() : localValue0.internalMethod04335();
   }

   public void tick() {
      GuiMoveModule.internalMethod09597();
      super.tick();
   }

   public boolean shouldCloseOnEsc() {
      return true;
   }

   public void close() {
      super.close();
      MinecraftClient.getInstance().setScreen(RockstarClient.getInstance().internalMethod04334());
   }

   static enum InternalType0153 {
      internalField0422,
      internalField0423,
      internalField1149;
   }
}
