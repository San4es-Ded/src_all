package rockstar.client.internal.script;








import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;

public class InventoryBuilderScreen extends AbstractMenuScreen {
   private static final Motion internalField0913 = Motion.internalMethod01328(160L, Easing.internalField1822);
   private static final int internalField0228 = 7;
   private static final int internalField1053 = 9;
   private float internalField1730;
   private float internalField1729;
   private float internalField1725;
   private float internalField1726;
   private float internalField1724;
   InventoryBuilderModule.InternalType0024 internalField0192;
   InventoryBuilderModule.InternalType0023 internalField0191;
   int internalField1055 = -1;
   int internalField1056 = -1;
   private String internalField0248 = "";
   private UiContainer internalField0634;
   private UiContainer internalField0635;
   private UiContainer internalField1257;
   private TextInputField internalField0097;
   private UiContainer internalField1254;
   private InventoryBuilderScreen.InternalType0264 internalField0747;
   private final Map<InventoryBuilderModule.InternalType0024, UiContainer> internalField0543 = new IdentityHashMap<>();
   private List<InventoryBuilderModule.InternalType0024> internalField0416 = List.of();
   private final List<Runnable> internalField0417 = new ArrayList<>();
   private final List<HudContextMenu> internalField1145 = new ArrayList<>();
   private InventoryBuilderModule.InternalType0023 internalField0190;
   private InventoryBuilderModule.InternalType0023 internalField1041;
   private UiContainer internalField1255;
   private UiContainer internalField1256;
   private boolean internalField0277;
   private boolean internalField0276;
   private List<InventoryBuilderModule.InternalType0023> internalField1146;
   private List<String> internalField1148;
   private String internalField0247;
   private List<InventoryBuilderModule.InternalType0023> internalField1147 = new ArrayList<>();
   private final Map<InventoryBuilderModule.InternalType0023, ItemStack> internalField0544 = new IdentityHashMap<>();

   public InventoryBuilderScreen() {
      InventoryBuilderModule.internalMethod09170();
      if (!InventoryBuilderModule.internalMethod05853().isEmpty()) {
         this.internalField0192 = InventoryBuilderModule.internalMethod05853().getFirst();
      }
   }

   @Override
   public boolean lowDrawBatching() {
      return true;
   }

   @Override
   public void init() {
      super.init();
      this.clearRoots();
      this.overlays.clear();
      this.internalField1724 = 200.0F;
      this.internalField1730 = 118.0F + this.internalField1724;
      this.internalField1729 = 149.0F;
      this.internalField1725 = Math.round((this.width - this.internalField1730) / 2.0F);
      this.internalField1726 = Math.round((this.height - this.internalField1729) / 2.0F);
      this.internalField1257 = (new UiContainer() {
            @Override
            protected void drawChildren(UiRenderContext localValue1, float localValue2) {
               ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x(), this.y(), this.w(), this.h());
               super.drawChildren(localValue1, localValue2);
               ScissorStack.internalMethod07643();
            }
         })
         .internalMethod05895()
         .internalMethod03754(Motion.internalField1384)
         .internalMethod03995(this.internalField1730, this.internalField1729)
         .internalMethod07178((localValue1, localValue2) -> this.internalMethod06274(localValue1, localValue2));
      this.internalField1257.internalMethod09801();
      this.internalField1257.snapSize();
      this.internalField1257.snapAt(this.internalField1725, this.internalField1726);
      this.internalField1257.internalMethod03907(this.internalMethod02573());
      this.internalField1257.internalMethod03907(this.internalMethod06649());
      this.add(this.internalField1257);
      this.internalField0416 = List.of();
      this.internalMethod07487(true);
      this.internalMethod07391();
   }

   private float internalMethod07383() {
      return this.internalField1257 == null ? this.internalField1725 : this.internalField1257.x();
   }

   private float internalMethod07390() {
      return this.internalField1257 == null ? this.internalField1726 : this.internalField1257.y();
   }

   private void internalMethod06274(UiRenderContext localValue1, UiContainer localValue2) {
      float localValue3 = localValue2.x();
      float localValue4 = localValue2.y();
      float localValue5 = localValue2.w();
      float localValue6 = localValue2.h();
      ColorRGBA localValue7 = this.internalMethod08589();
      localValue1.drawClientRect(localValue3, localValue4, localValue5, localValue6, 1.0F, 0.0F, 2.0F, 12.0F, false, true);
      float localValue8 = localValue3 + 118.0F - 1.0F;
      localValue1.drawRect(localValue8, localValue4 + 1.0F, 1.0F, localValue6 - 2.0F, localValue7);
      localValue1.drawRect(localValue3 + 1.0F, localValue4 + 22.0F - 1.0F, 116.0F, 1.0F, localValue7);
      localValue1.drawRect(localValue8 + 1.0F, localValue4 + 22.0F - 1.0F, localValue5 - 118.0F - 1.0F, 1.0F, localValue7);
      localValue1.drawRoundedBorder(localValue3, localValue4, localValue5, localValue6, 0.5F, CornerRadii.internalMethod03908(12.0F), localValue7);
   }

   private UiContainer internalMethod02573() {
      UiElement localValue1 = new UiElement()
         .height(22.0F)
         .fillWidth()
         .padding(Insets.internalMethod05266(0.0F, 7.5F))
         .text(Fonts.internalField0449.internalMethod01432(7.0F), "\u041f\u0440\u0435\u0441\u0435\u0442\u044b", localValue1x -> this.internalMethod04084())
         .draggable(DragConstraint.internalField0631);
      float localValue2 = 23.0F;
      this.internalField0634 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod03514(Insets.internalMethod00105(4.0F, 5.0F, 0.0F, 4.0F))
         .internalMethod09339(118.0F)
         .internalMethod09266(Math.max(0.0F, this.internalField1729 - 22.0F - localValue2))
         .internalMethod08755()
         .internalMethod01416(ScrollPolicy.internalField0916)
         .internalMethod05391(this::internalMethod01483);
      UiContainer localValue3 = new UiContainer()
         .internalMethod01863()
         .internalMethod09339(118.0F)
         .internalMethod09266(localValue2)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 4.0F, 4.0F, 4.0F))
         .internalMethod03907(
            new UiElement()
               .height(15.0F)
               .fillWidth()
               .radius(3.0F)
               .text(
                  Fonts.internalField0449.internalMethod01432(7.0F),
                  "\u041d\u043e\u0432\u044b\u0439 \u043f\u0440\u0435\u0441\u0435\u0442",
                  localValue1x -> this.internalMethod04084().mix(ThemeColors.internalField1310, 0.6F).mulAlpha(0.75F + 0.25F * localValue1x.hover())
               )
               .textAlign(TextAlignment.internalField0621)
               .background(localValue1x -> internalMethod01785(this.internalMethod08735(), ThemeColors.internalField1310, 0.05F + 0.045F * localValue1x.hover()))
               .cursor(CursorType.internalField0567)
               .onClick(this::internalMethod07384)
         );
      return new UiContainer()
         .internalMethod01863()
         .internalMethod09339(118.0F)
         .internalMethod09266(this.internalField1729)
         .internalMethod03907(localValue1)
         .internalMethod03907(this.internalField0634)
         .internalMethod03907(localValue3);
   }

   private UiContainer internalMethod06649() {
      TextInputField localValue1 = this.internalField0097 = new TextInputField(
            Fonts.internalField1154.internalMethod01432(7.0F), this.internalField0192 == null ? "" : this.internalField0192.internalField0248, localValue1x -> {
               if (this.internalField0192 != null) {
                  this.internalField0192.internalField0248 = localValue1x.isBlank() ? "\u041f\u0440\u0435\u0441\u0435\u0442" : localValue1x;
                  InventoryBuilderModule.internalMethod09171();
               }
            }
         )
         .internalMethod01789("\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435")
         .internalMethod07676(this.internalField1724 - 8.0F - 50.0F - 4.0F)
         .internalMethod07996(14.0F);
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(4.0F)
         .internalMethod03907(this.internalMethod00758("plus", ThemeColors.internalField1310, () -> {
            if (this.internalField0192 != null) {
               this.internalMethod09120();
            }
         }))
         .internalMethod03907(this.internalMethod00758("play", new ColorRGBA(96.0F, 208.0F, 118.0F), () -> {
            InventoryBuilderModule localValue1x = this.internalMethod07499();
            if (localValue1x != null && this.internalField0192 != null) {
               this.close();
               localValue1x.internalMethod01627(this.internalField0192);
            }
         }).visibleWhen(() -> !this.internalMethod07385()))
         .internalMethod03907(this.internalMethod00758("xmark", new ColorRGBA(228.0F, 92.0F, 92.0F), () -> {
            InventoryBuilderModule localValue1x = this.internalMethod07499();
            if (localValue1x != null) {
               localValue1x.internalMethod01693("\u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e \u0432\u0440\u0443\u0447\u043d\u0443\u044e");
            }
         }).visibleWhen(this::internalMethod07385))
         .internalMethod03907(this.internalMethod00758("trash", new ColorRGBA(228.0F, 92.0F, 92.0F), () -> {
            if (this.internalField0192 != null) {
               this.internalField0543.remove(this.internalField0192);
               InventoryBuilderModule.internalMethod05853().remove(this.internalField0192);
               this.internalField0192 = InventoryBuilderModule.internalMethod05853().isEmpty() ? null : InventoryBuilderModule.internalMethod05853().getFirst();
               this.internalField1055 = -1;
               this.internalField0191 = null;
               InventoryBuilderModule.internalMethod09171();
               this.internalField0277 = true;
            }
         }));
      localValue2.snapSize();
      UiContainer localValue3 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod03514(Insets.internalMethod00172(4.0F))
         .internalMethod09266(22.0F)
         .internalMethod09339(this.internalField1724)
         .internalMethod03907(localValue1)
         .internalMethod03907(localValue2);
      localValue3.snapSize();
      this.internalField0635 = new UiContainer()
         .internalMethod01863()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod03514(Insets.internalMethod00105(10.5F, 11.0F, 10.5F, 11.0F))
         .internalMethod03995(this.internalField1724, Math.max(0.0F, this.internalField1729 - 22.0F));
      this.internalField0635.snapSize();
      UiContainer localValue4 = new UiContainer()
         .internalMethod01863()
         .internalMethod03995(this.internalField1724, this.internalField1729)
         .internalMethod03907(localValue3)
         .internalMethod03907(this.internalField0635);
      localValue4.snapSize();
      return localValue4;
   }

   private UiElement internalMethod00758(String localValue1, ColorRGBA localValue2, Runnable localValue3) {
      return new UiElement()
         .size(14.0F, 14.0F)
         .padding(3.0F)
         .radius(3.0F)
         .icon(localValue1, 8.0F, localValue2x -> this.internalMethod04084().mix(localValue2, 0.6F).mulAlpha(0.72F + 0.28F * localValue2x.hover()))
         .background(localValue2x -> internalMethod01785(this.internalMethod07907(), localValue2, 0.05F + 0.05F * localValue2x.hover() + 0.03F * localValue2x.press()))
         .cursor(CursorType.internalField0567)
         .onClick(localValue3);
   }

   private void internalMethod07384() {
      InventoryBuilderModule.InternalType0024 localValue1 = new InventoryBuilderModule.InternalType0024(
         "\u041f\u0440\u0435\u0441\u0435\u0442 " + (InventoryBuilderModule.internalMethod05853().size() + 1)
      );
      InventoryBuilderModule.internalMethod05853().add(localValue1);
      this.internalField0192 = localValue1;
      this.internalField1055 = -1;
      InventoryBuilderModule.internalMethod09171();
      this.internalField0277 = true;
   }

   private void internalMethod07487(boolean localValue1) {
      if (this.internalField0634 != null) {
         List localValue2 = InventoryBuilderModule.internalMethod05853();
         if (localValue1 || !internalMethod02344(this.internalField0416, localValue2)) {
            ArrayList localValue3 = new ArrayList();

            for (InventoryBuilderModule.InternalType0024 localValue5 : (Iterable<InventoryBuilderModule.InternalType0024>)(Iterable<?>)localValue2) {
               localValue3.add(this.internalField0543.computeIfAbsent(localValue5, this::internalMethod01487));
            }

            this.internalField0634.internalMethod07849(localValue3);
            this.internalField0416 = new ArrayList<>(localValue2);
         }
      }
   }

   private UiContainer internalMethod01487(InventoryBuilderModule.InternalType0024 localValue1) {
      UiElement localValue2 = new UiElement()
         .fill()
         .fade()
         .text(
            Fonts.internalField0449.internalMethod01432(7.0F),
            () -> localValue1.internalField0248,
            localValue1x -> this.internalMethod04084().mix(ThemeColors.internalField1310, 0.5F * localValue1x.sig("active")).mulAlpha(0.6F + 0.4F * localValue1x.sig("active"))
         )
         .bind("active", () -> this.internalField0192 == localValue1, internalField0913)
         .interactive(false);
      UiElement localValue3 = new UiElement()
         .width(18.0F)
         .text(
            Fonts.internalField1154.internalMethod01432(7.0F),
            () -> String.valueOf(localValue1.internalMethod01393()),
            localValue1x -> this.internalMethod04084().mulAlpha(0.42F)
         )
         .textAlign(TextAlignment.internalField1242)
         .interactive(false);
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09266(15.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 3.5F))
         .internalMethod09018(3.0F)
         .internalMethod06812(
            localValue1x -> internalMethod01785(this.internalMethod08735(), ThemeColors.internalField1310, 0.05F * localValue1x.sig("active") + 0.025F * localValue1x.hover())
         )
         .internalMethod01258("active", () -> this.internalField0192 == localValue1, internalField0913)
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(localValue2)
         .internalMethod03907(localValue3)
         .internalMethod05382((localValue2x, localValue3x, localValue4) -> {
            if (localValue2x == MouseButton.internalField0101) {
               this.internalMethod06282(localValue1, localValue3x, localValue4);
            } else if (this.internalField0192 != localValue1) {
               this.internalField0192 = localValue1;
               this.internalField1055 = -1;
               this.internalField0277 = true;
            }
         });
   }

   private void internalMethod06282(InventoryBuilderModule.InternalType0024 localValue1, float localValue2, float localValue3) {
      HudContextMenu localValue4 = new HudContextMenu(localValue2, localValue3, 108.0F)
         .internalMethod04738("\u041f\u0440\u0435\u0441\u0435\u0442")
         .internalMethod03873()
         .internalMethod03323("\u0414\u0443\u0431\u043b\u0438\u0440\u043e\u0432\u0430\u0442\u044c", "copy", localValue2x -> {
            InventoryBuilderModule.InternalType0024 localValue3x = localValue1.internalMethod07222(localValue1.internalField0248 + " (\u043a\u043e\u043f\u0438\u044f)");
            List localValue4x = InventoryBuilderModule.internalMethod05853();
            localValue4x.add(localValue4x.indexOf(localValue1) + 1, localValue3x);
            this.internalField0192 = localValue3x;
            this.internalField1055 = -1;
            InventoryBuilderModule.internalMethod09171();
            this.internalField0277 = true;
            localValue2x.internalMethod05781(false);
         })
         .internalMethod03323("\u041e\u0447\u0438\u0441\u0442\u0438\u0442\u044c", "xmark", localValue2x -> {
            localValue1.internalMethod01394();
            if (this.internalField0192 == localValue1) {
               this.internalField1055 = -1;
            }

            InventoryBuilderModule.internalMethod09171();
            localValue2x.internalMethod05781(false);
         })
         .internalMethod03323("\u0423\u0434\u0430\u043b\u0438\u0442\u044c", "trash", localValue2x -> {
            this.internalField0543.remove(localValue1);
            InventoryBuilderModule.internalMethod05853().remove(localValue1);
            if (this.internalField0192 == localValue1) {
               this.internalField0192 = InventoryBuilderModule.internalMethod05853().isEmpty() ? null : InventoryBuilderModule.internalMethod05853().getFirst();
               this.internalField1055 = -1;
               this.internalField0191 = null;
               this.internalField0277 = true;
            }

            InventoryBuilderModule.internalMethod09171();
            localValue2x.internalMethod05781(false);
         });
      this.internalField1145.add(localValue4);
   }

   private void internalMethod07391() {
      if (this.internalField0097 != null) {
         this.internalField0097.internalMethod06885(this.internalField0192 == null ? "" : this.internalField0192.internalField0248);
      }

      if (this.internalField0635 != null) {
         if (this.internalField0192 == null) {
            this.internalField0747 = null;
            this.internalField0635
               .internalMethod06213(
                  List.of(
                     new UiElement()
                        .fill()
                        .text(
                           Fonts.internalField1154.internalMethod01432(8.0F),
                           "\u0421\u043e\u0437\u0434\u0430\u0439 \u043f\u0440\u0435\u0441\u0435\u0442",
                           localValue1 -> this.internalMethod04084().mulAlpha(0.38F)
                        )
                        .textAlign(TextAlignment.internalField0621)
                        .interactive(false)
                  )
               );
         } else {
            this.internalField0747 = new InventoryBuilderScreen.InternalType0264().internalMethod01871();
            this.internalField0635.internalMethod06213(List.of(this.internalField0747));
         }
      }
   }

   private UiContainer internalMethod07280(float localValue1, float localValue2) {
      UiContainer localValue3 = new UiContainer()
         .internalMethod01863()
         .internalMethod03514(Insets.internalMethod00105(9.0F, 9.0F, 9.0F, 9.0F))
         .internalMethod03062(6.0F)
         .internalMethod09018(11.0F)
         .internalMethod03995(localValue1, localValue2)
         .internalMethod03715(DragConstraint.internalField0631)
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               localValue1x.drawShadow(
                  localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), 10.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.5F)
               );
               localValue1x.drawRoundedRect(localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), CornerRadii.internalMethod03908(11.0F), this.internalMethod00268());
               localValue1x.drawRoundedBorder(localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), 0.5F, CornerRadii.internalMethod03908(11.0F), this.internalMethod08589());
               UiBatchRenderer.internalMethod02576();
            }
         )
         .internalMethod01619(UiTransition.internalField1659)
         .internalMethod02991(UiTransition.internalField1389);
      localValue3.internalMethod09801();
      localValue3.snapSize();
      localValue3.snapAt(
         Math.max(4.0F, Math.min(this.width - localValue1 - 4.0F, this.internalMethod07383() + (this.internalField1730 - localValue1) / 2.0F)),
         Math.max(4.0F, Math.min(this.height - localValue2 - 4.0F, this.internalMethod07390() + (this.internalField1729 - localValue2) / 2.0F))
      );
      return localValue3;
   }

   private UiElement internalMethod01103(String localValue1) {
      return new UiElement()
         .fillWidth()
         .height(9.0F)
         .text(Fonts.internalField0449.internalMethod01432(8.0F), localValue1, localValue1x -> this.internalMethod04084().mulAlpha(0.85F))
         .interactive(false);
   }

   private void internalMethod09120() {
      this.internalField0248 = "";
      UiContainer localValue1 = this.internalMethod07280(214.0F, 206.0F);
      localValue1.internalMethod03907(this.internalMethod01103("\u0412\u044b\u0431\u043e\u0440 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430"));
      localValue1.internalMethod03907(
         new TextInputField(Fonts.internalField1154.internalMethod01432(7.0F), "", localValue1x -> this.internalField0248 = localValue1x == null ? "" : localValue1x)
            .internalMethod01789("\u041f\u043e\u0438\u0441\u043a")
            .internalMethod06836(localValue1x -> this.internalMethod08735())
            .internalMethod01901(3.0F)
            .internalMethod06744()
            .internalMethod07996(15.0F)
      );
      localValue1.internalMethod03907(
         new ItemGrid<>(this::internalMethod01662, this::internalMethod07023, localValue0 -> false, localValue2 -> {
               this.internalField0191 = localValue2.internalMethod06720();
               this.internalField1041 = null;
               this.internalField1254 = null;
               localValue1.beginExit(0.0F);
            })
            .internalMethod01914(localValue1x -> this.internalField1041 = localValue1x)
            .internalMethod05719(500)
            .internalMethod06059(18.0F)
            .internalMethod07352(localValue1x -> this.internalMethod08735())
            .internalMethod06817()
            .internalMethod09696(150.0F)
      );
      this.internalField1254 = this.openWindow(localValue1);
   }

   public void internalMethod01012(InventoryBuilderModule.InternalType0023 localValue1) {
      this.internalField0190 = localValue1;
      this.internalField0417.clear();
      ArrayList localValue2 = new ArrayList();
      SettingOwner localValue3 = () -> localValue2;
      int localValue4 = this.internalMethod01011(localValue1);
      if (localValue4 <= 1) {
         localValue1.internalField0228 = 1;
      } else {
         SliderSetting localValue5 = new SliderSetting(localValue3, "\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e")
            .internalMethod05900(1.0F)
            .internalMethod02732(localValue4)
            .internalMethod08673(1.0F)
            .internalMethod08074(localValue1.internalField0228);
         this.internalField0417.add(() -> localValue1.internalField0228 = (int)localValue5.internalMethod08576());
      }

      TextSetting localValue16 = new TextSetting(localValue3, "\u041c\u0430\u043a\u0441. \u0446\u0435\u043d\u0430 \u0437\u0430 \u0448\u0442\u0443\u043a\u0443")
         .internalMethod00011(String.valueOf(localValue1.internalField0229))
         .internalMethod07009(true);
      this.internalField0417.add(() -> localValue1.internalField0229 = Math.max(0, this.internalMethod02853(localValue16.internalMethod08926(), 0)));
      if (localValue1.internalMethod00511()) {
         SliderSetting localValue6 = new SliderSetting(localValue3, "\u041c\u0438\u043d. \u043f\u0440\u043e\u0447\u043d\u043e\u0441\u0442\u044c")
            .internalMethod05900(0.0F)
            .internalMethod02732(100.0F)
            .internalMethod08673(1.0F)
            .internalMethod08074(localValue1.internalField1053)
            .internalMethod06240("%");
         this.internalField0417.add(() -> localValue1.internalField1053 = (int)localValue6.internalMethod08576());
      }

      if (localValue1.internalField1056 > 0) {
         TimeSetting localValue17 = new TimeSetting(localValue3, "\u041c\u0438\u043d. \u0434\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c")
            .internalMethod04919(false)
            .internalMethod03793(13)
            .internalMethod02845(localValue0 -> Math.min(localValue0, 720))
            .internalMethod08384(localValue1.internalField1055);
         this.internalField0417.add(() -> localValue1.internalField1055 = localValue17.internalMethod09221());
      }

      boolean localValue18 = localValue1.internalMethod06731().isEnchantable();
      String localValue7 = localValue18 ? "\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u0435" : "\u042d\u0444\u0444\u0435\u043a\u0442";
      String localValue8 = localValue18 ? "\u0417\u0430\u0447\u0430\u0440\u043e\u0432\u0430\u043d\u0438\u044f" : "\u042d\u0444\u0444\u0435\u043a\u0442\u044b";
      MultiSelectSetting localValue9 = null;

      for (InventoryBuilderModule.InternalType0066 localValue11 : localValue1.internalField0416) {
         if (localValue11.internalMethod04555() <= 0) {
            String localValue12 = localValue11.internalMethod03562();
            if (localValue9 == null) {
               localValue9 = new MultiSelectSetting(localValue3, localValue8);
            }

            MultiSelectSetting.InternalType0091 localValue13 = new MultiSelectSetting.InternalType0091(localValue9, localValue12);
            if (localValue1.internalField0543.containsKey(localValue12)) {
               localValue13.select();
            }

            this.internalField0417.add(() -> {
               if (localValue13.isSelected()) {
                  localValue1.internalField0543.put(localValue12, 0);
               } else {
                  localValue1.internalField0543.remove(localValue12);
               }
            });
         }
      }

      ArrayList<InventoryBuilderModule.InternalType0066> localValue19 = new ArrayList<>();

      for (InventoryBuilderModule.InternalType0066 localValue22 : localValue1.internalField0416) {
         if (localValue22.internalMethod04555() > 0) {
            localValue19.add(localValue22);
         }
      }

      boolean localValue21 = localValue1.internalField0417.size() == 1;

      for (InventoryBuilderModule.InternalType0066 localValue25 : localValue21 ? List.<InventoryBuilderModule.InternalType0066>of() : localValue19) {
         String localValue14 = localValue25.internalMethod03562();
         if (localValue1.internalField0543.containsKey(localValue14)) {
            SliderSetting localValue15 = new SliderSetting(localValue3, localValue14)
               .internalMethod05900(0.0F)
               .internalMethod02732(localValue25.internalMethod04555())
               .internalMethod08673(1.0F)
               .internalMethod08074(Math.max(1, localValue1.internalField0543.get(localValue14)));
            this.internalField0417.add(() -> {
               int localValue5x = this.internalMethod00385(localValue1, localValue14, localValue25.internalMethod04555());
               int localValue6x = Math.min(localValue5x, (int)localValue15.internalMethod08576());
               if (localValue6x != (int)localValue15.internalMethod08576()) {
                  localValue15.internalMethod08074(localValue6x);
               }

               if (localValue6x <= 0) {
                  localValue1.internalField0543.remove(localValue14);
                  this.internalField0276 = true;
               } else {
                  localValue1.internalField0543.put(localValue14, localValue6x);
               }
            });
         }
      }

      HudContextMenu localValue24 = new HudContextMenu(this.internalMethod07383() + this.internalField1730 + 8.0F, this.internalMethod07390(), 150.0F)
         .internalMethod04738(localValue1.internalMethod01243())
         .internalMethod03873();

      for (Setting localValue28 : (Iterable<Setting>)(Iterable<?>)localValue2) {
         localValue24.internalMethod05995(localValue28);
      }

      boolean localValue27 = !localValue21
         && localValue19.stream()
            .anyMatch(
               localValue2x -> !localValue1.internalField0543.containsKey(localValue2x.internalMethod03562())
                  && this.internalMethod00385(localValue1, localValue2x.internalMethod03562(), localValue2x.internalMethod04555()) > 0
            );
      boolean localValue29 = !localValue21 && localValue19.stream().anyMatch(localValue1x -> localValue1.internalField0543.containsKey(localValue1x.internalMethod03562()));
      if (localValue27 || localValue29) {
         localValue24.internalMethod03873();
      }

      if (localValue27) {
         localValue24.internalMethod03323("+ " + localValue7, "plus", localValue4x -> this.internalMethod03266(localValue1, localValue19, localValue4x, localValue7));
      }

      if (localValue29) {
         localValue24.internalMethod03323(
            "\u0423\u0431\u0440\u0430\u0442\u044c " + localValue7.toLowerCase(Locale.ROOT), "trash", localValue4x -> this.internalMethod03011(localValue1, localValue19, localValue4x, localValue7)
         );
      }

      this.internalField1145.add(localValue24);
      this.internalField1256 = null;
   }

   private void internalMethod03266(InventoryBuilderModule.InternalType0023 localValue1, List<InventoryBuilderModule.InternalType0066> localValue2, HudContextMenu localValue3, String localValue4) {
      ArrayList localValue5 = new ArrayList();

      for (InventoryBuilderModule.InternalType0066 localValue7 : localValue2) {
         if (!localValue1.internalField0543.containsKey(localValue7.internalMethod03562())
            && this.internalMethod00385(localValue1, localValue7.internalMethod03562(), localValue7.internalMethod04555()) > 0) {
            localValue5.add(localValue7);
         }
      }

      if (!localValue5.isEmpty()) {
         HudContextMenu localValue10 = new HudContextMenu(localValue3.internalMethod02045() + 154.0F, localValue3.internalMethod02048(), 130.0F)
            .internalMethod04738(localValue4)
            .internalMethod03873();

         for (InventoryBuilderModule.InternalType0066 localValue8 : (Iterable<InventoryBuilderModule.InternalType0066>)(Iterable<?>)localValue5) {
            int localValue9 = this.internalMethod00385(localValue1, localValue8.internalMethod03562(), localValue8.internalMethod04555());
            localValue10.internalMethod03323(localValue8.internalMethod03562() + "  " + localValue9, "check", localValue5x -> {
               localValue1.internalField0543.put(localValue8.internalMethod03562(), localValue9);
               InventoryBuilderModule.internalMethod09171();
               localValue5x.internalMethod05781(false);
               localValue3.internalMethod05781(false);
               this.internalField0276 = true;
            });
         }

         this.internalField1145.add(localValue10);
      }
   }

   private void internalMethod03011(InventoryBuilderModule.InternalType0023 localValue1, List<InventoryBuilderModule.InternalType0066> localValue2, HudContextMenu localValue3, String localValue4) {
      ArrayList localValue5 = new ArrayList();

      for (InventoryBuilderModule.InternalType0066 localValue7 : localValue2) {
         if (localValue1.internalField0543.containsKey(localValue7.internalMethod03562())) {
            localValue5.add(localValue7);
         }
      }

      if (!localValue5.isEmpty()) {
         HudContextMenu localValue9 = new HudContextMenu(localValue3.internalMethod02045() + 154.0F, localValue3.internalMethod02048(), 130.0F)
            .internalMethod04738(localValue4)
            .internalMethod03873();

         for (InventoryBuilderModule.InternalType0066 localValue8 : (Iterable<InventoryBuilderModule.InternalType0066>)(Iterable<?>)localValue5) {
            localValue9.internalMethod03323(localValue8.internalMethod03562() + "  " + localValue1.internalField0543.get(localValue8.internalMethod03562()), "trash", localValue4x -> {
               localValue1.internalField0543.remove(localValue8.internalMethod03562());
               InventoryBuilderModule.internalMethod09171();
               localValue4x.internalMethod05781(false);
               localValue3.internalMethod05781(false);
               this.internalField0276 = true;
            });
         }

         this.internalField1145.add(localValue9);
      }
   }

   private void internalMethod09122() {
      this.internalField1146 = new ArrayList<>();
      this.internalField1148 = new ArrayList<>();
      ArrayList<InventoryBuilderModule.InternalType0023> localValue1 = new ArrayList<>(InventoryBuilderModule.internalMethod03453());
      localValue1.sort(Comparator.comparingInt(localValue0 -> InventoryBuilderModule.internalMethod01691(localValue0.internalField0248)));

      for (InventoryBuilderModule.InternalType0023 localValue3 : (Iterable<InventoryBuilderModule.InternalType0023>)(Iterable<?>)localValue1) {
         this.internalField1146.add(localValue3);
         this.internalField1148.add((localValue3.internalMethod04718() + " " + localValue3.internalMethod01243()).toLowerCase(Locale.ROOT));
      }

      LinkedHashSet localValue8 = new LinkedHashSet();

      for (Potion localValue4 : Registries.POTION) {
         Identifier localValue5 = Registries.POTION.getId(localValue4);
         if (localValue5 != null) {
            InventoryBuilderModule.InternalType0023 localValue6 = new InventoryBuilderModule.InternalType0023(Registries.ITEM.getId(Items.POTION), "");
            localValue6.internalField1079 = localValue5.toString();
            localValue6.internalField0247 = this.internalMethod05912(localValue4, localValue6);
            localValue6.internalField0277 = true;
            if (localValue8.add(localValue6.internalField0247.toLowerCase(Locale.ROOT))) {
               this.internalField1146.add(localValue6);
               this.internalField1148.add((localValue6.internalField0247 + " " + localValue5).toLowerCase(Locale.ROOT));
            }
         }
      }

      LinkedHashSet localValue10 = new LinkedHashSet();

      for (InventoryBuilderModule.InternalType0023 localValue13 : InventoryBuilderModule.internalMethod03453()) {
         localValue10.add(localValue13.internalMethod04718().toLowerCase(Locale.ROOT));
      }

      for (Item localValue14 : Registries.ITEM) {
         if (localValue14 != Items.POTION && !localValue14.getDefaultStack().isEmpty()) {
            Identifier localValue15 = Registries.ITEM.getId(localValue14);
            String localValue7 = Text.translatable(localValue14.getTranslationKey()).getString();
            if (!localValue10.contains(localValue7.toLowerCase(Locale.ROOT))) {
               this.internalField1146.add(new InventoryBuilderModule.InternalType0023(localValue15, localValue7));
               this.internalField1148.add((localValue7 + " " + localValue15).toLowerCase(Locale.ROOT));
            }
         }
      }
   }

   private List<InventoryBuilderModule.InternalType0023> internalMethod01662() {
      if (this.internalField1146 == null) {
         this.internalMethod09122();
      }

      String localValue1 = this.internalField0248.trim().toLowerCase(Locale.ROOT);
      if (localValue1.equals(this.internalField0247)) {
         return this.internalField1147;
      } else {
         ArrayList localValue2 = new ArrayList();

         for (int localValue3 = 0; localValue3 < this.internalField1146.size(); localValue3++) {
            if (localValue1.isEmpty() || this.internalField1148.get(localValue3).contains(localValue1)) {
               localValue2.add(this.internalField1146.get(localValue3));
            }
         }

         this.internalField0247 = localValue1;
         this.internalField1147 = localValue2;
         return localValue2;
      }
   }

   private ItemStack internalMethod07023(InventoryBuilderModule.InternalType0023 localValue1) {
      return this.internalField0544.computeIfAbsent(localValue1, InventoryBuilderModule.InternalType0023::internalMethod06731);
   }

   private InventoryBuilderModule internalMethod07499() {
      return RockstarClient.getInstance().getModuleManager().getModule(InventoryBuilderModule.class);
   }

   private boolean internalMethod07385() {
      InventoryBuilderModule localValue1 = this.internalMethod07499();
      return localValue1 != null && localValue1.internalMethod09168();
   }

   private String internalMethod05912(Potion localValue1, InventoryBuilderModule.InternalType0023 localValue2) {
      List localValue3 = localValue1.getEffects();
      if (!localValue3.isEmpty()) {
         String localValue4 = ((StatusEffect)((StatusEffectInstance)localValue3.getFirst()).getEffectType().value()).getName().getString();
         if (!localValue4.isBlank()) {
            return "\u0417\u0435\u043b\u044c\u0435 " + localValue4.toLowerCase(Locale.ROOT);
         }
      }

      return localValue2.internalMethod06731().getName().getString();
   }

   private int internalMethod00385(InventoryBuilderModule.InternalType0023 localValue1, String localValue2, int localValue3) {
      if (localValue1.internalField0417.isEmpty()) {
         if (localValue1.internalField1054 > 0) {
            int localValue10 = 0;

            for (Entry localValue12 : localValue1.internalField0543.entrySet()) {
               if (!((String)localValue12.getKey()).equals(localValue2)) {
                  localValue10 += Math.max(0, (Integer)localValue12.getValue());
               }
            }

            return Math.max(0, Math.min(localValue3, localValue1.internalField1054 - localValue10));
         } else {
            return localValue3;
         }
      } else {
         int localValue4 = 0;

         for (Map<String, Integer> localValue6 : localValue1.internalField0417) {
            boolean localValue7 = true;

            for (Entry localValue9 : localValue1.internalField0543.entrySet()) {
               if (!((String)localValue9.getKey()).equals(localValue2) && (Integer)localValue9.getValue() > 0 && localValue6.getOrDefault(localValue9.getKey(), 0) < (Integer)localValue9.getValue()) {
                  localValue7 = false;
                  break;
               }
            }

            if (localValue7) {
               localValue4 = Math.max(localValue4, localValue6.getOrDefault(localValue2, 0));
            }
         }

         return Math.min(localValue3, localValue4);
      }
   }

   public boolean internalMethod04768(InventoryBuilderModule.InternalType0023 localValue1, InventoryBuilderModule.InternalType0023 localValue2) {
      return localValue1.internalField0354.equals(localValue2.internalField0354) && localValue1.internalMethod04718().equalsIgnoreCase(localValue2.internalMethod04718());
   }

   public int internalMethod01011(InventoryBuilderModule.InternalType0023 localValue1) {
      return localValue1.internalMethod00510();
   }

   private int internalMethod02853(String localValue1, int localValue2) {
      try {
         return Integer.parseInt(localValue1.trim());
      } catch (Exception localValue4) {
         return localValue2;
      }
   }

   private static boolean internalMethod03567(float localValue0, float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      return localValue0 < localValue4 + localValue6 && localValue4 < localValue0 + localValue2 && localValue1 < localValue5 + localValue7 && localValue5 < localValue1 + localValue3;
   }

   private static boolean internalMethod02344(List<?> localValue0, List<?> localValue1) {
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

   private ColorRGBA internalMethod04084() {
      return ThemeColors.internalMethod08459();
   }

   private ColorRGBA internalMethod00268() {
      return ThemeColors.internalMethod07738().withAlpha(242.25F);
   }

   private ColorRGBA internalMethod07907() {
      return ThemeColors.internalMethod07738().withAlpha(102.0F);
   }

   public ColorRGBA internalMethod08735() {
      return this.internalMethod07907();
   }

   public ColorRGBA internalMethod08589() {
      return ThemeColors.internalField1616.withAlpha(89.25F);
   }

   public static ColorRGBA internalMethod01785(ColorRGBA localValue0, ColorRGBA localValue1, float localValue2) {
      return localValue0.mix(localValue1.withAlpha(localValue0.getAlpha()), localValue2);
   }

   private void internalMethod01483(ScrollController localValue1) {
      localValue1.internalMethod02712(-3.0F)
         .internalMethod09005(2.0F)
         .internalMethod00894(2.0F)
         .internalMethod08056(18.0F)
         .internalMethod07954(1.0F)
         .internalMethod08313(1100.0F)
         .internalMethod04404(
            localValue1x -> this.internalMethod04084().withAlpha(255.0F * (0.28F + 0.24F * localValue1x.internalMethod05170() + 0.28F * localValue1x.internalMethod05173()))
         );
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalMethod07487(false);
      if (this.internalField0277) {
         this.internalField0277 = false;
         this.internalMethod07391();
      }

      if (this.internalField0276) {
         this.internalField0276 = false;
         InventoryBuilderModule.InternalType0023 localValue2 = this.internalField0190;
         this.internalField0417.clear();
         this.internalField1145.forEach(localValue0 -> localValue0.internalMethod05781(false));
         if (localValue2 != null) {
            this.internalMethod01012(localValue2);
         }
      }

      if (!this.internalField1145.isEmpty() && !this.internalField0417.isEmpty()) {
         this.internalField0417.forEach(Runnable::run);
      }

      super.render(localValue1);
   }

   @Override
   public void afterRender(UiRenderContext localValue1) {
      this.internalField1145.removeIf(localValue0 -> !localValue0.internalMethod08805() && localValue0.internalMethod06960().internalMethod02881() <= 0.01F);

      for (HudContextMenu localValue3 : this.internalField1145) {
         localValue3.internalMethod03398(localValue1);
      }

      SizedFont localValue18 = Fonts.internalField1154.internalMethod01432(6.0F);
      String localValue19 = "\u041b\u041a\u041c - \u0432\u0437\u044f\u0442\u044c   \u041f\u041a\u041c - \u043f\u043e\u043b\u043e\u0432\u0438\u043d\u0430   \u0421\u041a\u041c - \u0443\u0441\u043b\u043e\u0432\u0438\u044f   \u041a\u043e\u043b\u0435\u0441\u043e - \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438   Shift+\u041b\u041a\u041c - \u0443\u0431\u0440\u0430\u0442\u044c";
      float localValue4 = localValue18.internalMethod00965(localValue19);
      float localValue5 = localValue18.internalMethod04890();
      float localValue6 = this.internalMethod07383() + (this.internalField1730 - localValue4) / 2.0F;
      float localValue7 = this.internalMethod07390() + this.internalField1729 + 9.0F;
      boolean localValue8 = this.internalField1254 != null
         && this.internalField1254.alive()
         && internalMethod03567(
            localValue6, localValue7, localValue4, localValue5, this.internalField1254.x(), this.internalField1254.y(), this.internalField1254.w(), this.internalField1254.h()
         );

      for (HudContextMenu localValue10 : this.internalField1145) {
         if (localValue8) {
            break;
         }

         localValue8 = internalMethod03567(
            localValue6, localValue7, localValue4, localValue5, localValue10.internalMethod02045(), localValue10.internalMethod02048(), localValue10.internalMethod08827(), localValue10.internalMethod07809()
         );
      }

      if (!localValue8) {
         localValue1.drawText(localValue18, localValue19, localValue6, localValue7, this.internalMethod04084().mulAlpha(0.6F));
      }

      float localValue20 = localValue1.internalMethod05259();
      float localValue21 = localValue1.internalMethod05261();
      InventoryBuilderModule.InternalType0023 localValue11 = this.internalField1041 != null && this.internalField1254 != null && this.internalField1254.alive()
         ? this.internalField1041
         : this.internalMethod06810();
      if (localValue11 != null && this.internalField0191 == null) {
         SizedFont localValue12 = Fonts.internalField1154.internalMethod01432(7.0F);
         String localValue13 = localValue11.internalMethod01243();
         float localValue14 = localValue12.internalMethod00965(localValue13) + 10.0F;
         float localValue15 = 13.0F;
         float localValue16 = Math.min(localValue20 + 9.0F, this.width - localValue14 - 3.0F);
         float localValue17 = Math.max(3.0F, localValue21 - localValue15 - 3.0F);
         localValue1.drawShadow(localValue16, localValue17, localValue14, localValue15, 8.0F, CornerRadii.internalMethod03908(4.0F), ThemeColors.internalField1309.mulAlpha(0.45F));
         localValue1.drawRoundedRect(localValue16, localValue17, localValue14, localValue15, CornerRadii.internalMethod03908(4.0F), this.internalMethod00268());
         localValue1.drawRoundedBorder(localValue16, localValue17, localValue14, localValue15, 0.5F, CornerRadii.internalMethod03908(4.0F), this.internalMethod08589());
         localValue1.drawText(localValue12, localValue13, localValue16 + 5.0F, localValue17 + localValue15 / 2.0F - localValue12.internalMethod04890() / 2.0F, this.internalMethod04084().mulAlpha(0.9F));
      }

      if (this.internalField0191 != null) {
         ItemStack localValue22 = this.internalField0191.internalMethod06731();
         localValue1.drawItem(localValue22, localValue20 - 8.0F, localValue21 - 8.0F, 1.0F);
         if (this.internalField0191.internalField0228 > 1) {
            SizedFont localValue23 = Fonts.internalField1154.internalMethod01432(7.0F);
            String localValue24 = String.valueOf(this.internalField0191.internalField0228);
            localValue1.drawTextWithShadow(
               localValue23,
               localValue24,
               localValue20 + 7.0F - localValue23.internalMethod00965(localValue24),
               localValue21 + 7.0F - localValue23.internalMethod04890(),
               ThemeColors.internalField1312,
               ThemeColors.internalField1309,
               0.8F,
               0.8F,
               1.2F
            );
         }
      }
   }

   private InventoryBuilderModule.InternalType0023 internalMethod06810() {
      return this.internalField0192 != null && this.internalField1056 >= 0 && this.internalField1056 < 41 ? this.internalField0192.internalField0430[this.internalField1056] : null;
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      float localValue6 = (float)localValue1;
      float localValue7 = (float)localValue3;
      if (!this.internalField1145.isEmpty()) {
         for (HudContextMenu localValue9 : new ArrayList<>(this.internalField1145)) {
            localValue9.internalMethod01643(localValue1, localValue3, localValue5);
            if (!localValue9.internalMethod04931(localValue1, localValue3)) {
               localValue9.internalMethod05781(false);
            }
         }
      } else if (!this.internalMethod02432(localValue6, localValue7)) {
         if (this.internalField0191 == null
            || localValue5 != MouseButton.internalField0102
            || this.internalField0747 != null && this.internalField0747.contains(localValue6, localValue7)) {
            super.onMouseClicked(localValue1, localValue3, localValue5);
         } else {
            this.internalField0191 = null;
            InventoryBuilderModule.internalMethod09171();
         }
      }
   }

   private void internalMethod09134() {
      this.internalField1256 = null;
      this.internalField1255 = null;
      this.internalField0190 = null;
      this.internalField0417.clear();
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      for (HudContextMenu localValue7 : this.internalField1145) {
         localValue7.internalMethod02863(localValue1, localValue3, localValue5);
      }

      super.onMouseReleased(localValue1, localValue3, localValue5);
   }

   private boolean internalMethod02432(float localValue1, float localValue2) {
      if (this.internalField1254 == null) {
         return false;
      } else if (!this.internalField1254.alive()) {
         this.internalField1254 = null;
         return false;
      } else if (this.internalField1254.contains(localValue1, localValue2)) {
         return false;
      } else {
         this.internalField1254.beginExit(0.0F);
         if (this.internalField1254 == this.internalField1256) {
            this.internalMethod09134();
         }

         this.internalField1254 = null;
         InventoryBuilderModule.internalMethod09171();
         return true;
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         if (this.internalField1254 != null && this.internalField1254.alive()) {
            this.internalField1254.beginExit(0.0F);
            if (this.internalField1254 == this.internalField1256) {
               this.internalMethod09134();
            }

            this.internalField1254 = null;
            InventoryBuilderModule.internalMethod09171();
            return true;
         }

         if (this.internalField0191 != null) {
            this.internalField0191 = null;
            return true;
         }
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean shouldPause() {
      return false;
   }

   @Override
   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public void close() {
      this.internalField0191 = null;
      InventoryBuilderModule.internalMethod09171();
      super.close();
      MenuModule.internalMethod09723();
   }

   class InternalType0264 extends UiNode {
      private MouseButton internalField0102;
      private final Set<Integer> internalField0546 = new LinkedHashSet<>();

      InternalType0264() {
         this.cursor(CursorType.internalField0567);
      }

      InventoryBuilderScreen.InternalType0264 internalMethod01871() {
         this.size(178.0F, 106.0F);
         this.snapSize();
         return this;
      }

      private float internalMethod07419(int localValue1) {
         if (localValue1 >= 36) {
            int localValue2 = localValue1 - 36;
            return this.x() + (localValue2 == 4 ? 5 : localValue2) * 20.0F;
         } else {
            return this.x() + localValue1 % 9 * 20.0F;
         }
      }

      private float internalMethod07471(int localValue1) {
         if (localValue1 >= 36) {
            return this.y();
         } else {
            return localValue1 < 27 ? this.y() + 24.0F + localValue1 / 9 * 20.0F : this.y() + 88.0F;
         }
      }

      private int internalMethod00264(float localValue1, float localValue2) {
         for (int localValue3 = 0; localValue3 < 41; localValue3++) {
            float localValue4 = this.internalMethod07419(localValue3);
            float localValue5 = this.internalMethod07471(localValue3);
            if (localValue1 >= localValue4 && localValue1 <= localValue4 + 18.0F && localValue2 >= localValue5 && localValue2 <= localValue5 + 18.0F) {
               return localValue3;
            }
         }

         return -1;
      }

      @Override
      protected void onTick(float localValue1, float localValue2, float localValue3) {
         InventoryBuilderScreen.this.internalField1056 = this.inFlow() && this.contains(localValue2, localValue3) ? this.internalMethod00264(localValue2, localValue3) : -1;
         if (this.internalField0102 == MouseButton.internalField0101
            && InventoryBuilderScreen.this.internalField0191 != null
            && InventoryBuilderScreen.this.internalField0192 != null) {
            int localValue4 = this.internalMethod00264(localValue2, localValue3);
            if (localValue4 >= 0 && !this.internalField0546.contains(localValue4)) {
               InventoryBuilderModule.InternalType0023 localValue5 = InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4];
               if (localValue5 == null) {
                  InventoryBuilderModule.InternalType0023 localValue6 = InventoryBuilderScreen.this.internalField0191.internalMethod06720();
                  localValue6.internalField0228 = 1;
                  InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = localValue6;
               } else {
                  if (!InventoryBuilderScreen.this.internalMethod04768(localValue5, InventoryBuilderScreen.this.internalField0191)
                     || localValue5.internalField0228 >= InventoryBuilderScreen.this.internalMethod01011(localValue5)) {
                     return;
                  }

                  localValue5.internalField0228++;
               }

               this.internalField0546.add(localValue4);
               if (--InventoryBuilderScreen.this.internalField0191.internalField0228 <= 0) {
                  InventoryBuilderScreen.this.internalField0191 = null;
               }

               InventoryBuilderModule.internalMethod09171();
            }
         }
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         CornerRadii localValue3 = CornerRadii.internalMethod03908(3.0F);
         ColorRGBA localValue4 = InventoryBuilderScreen.this.internalMethod08589();
         org.joml.Matrix3x2fStack localValue5 = localValue1.getMatrices();

         for (int localValue6 = 0; localValue6 < 41; localValue6++) {
            float localValue7 = this.internalMethod07419(localValue6);
            float localValue8 = this.internalMethod07471(localValue6);
            boolean localValue9 = InventoryBuilderScreen.this.internalField1055 == localValue6;
            float localValue10 = 0.05F * (localValue9 ? 1.0F : 0.0F) + 0.03F * (InventoryBuilderScreen.this.internalField1056 == localValue6 ? 1.0F : 0.0F);
            localValue1.drawRoundedRect(
               localValue7,
               localValue8,
               18.0F,
               18.0F,
               localValue3,
               InventoryBuilderScreen.internalMethod01785(InventoryBuilderScreen.this.internalMethod08735(), ThemeColors.internalField1310, localValue10).mulAlpha(localValue2)
            );
            localValue1.drawRoundedBorder(localValue7, localValue8, 18.0F, 18.0F, 0.5F, localValue3, (localValue9 ? ThemeColors.internalField1310.withAlpha(140.25F) : localValue4).mulAlpha(localValue2));
         }

         UiBatchRenderer.internalMethod02576();

         for (int localValue11 = 0; localValue11 < 41; localValue11++) {
            InventoryBuilderModule.InternalType0023 localValue13 = InventoryBuilderScreen.this.internalField0192 == null
               ? null
               : InventoryBuilderScreen.this.internalField0192.internalField0430[localValue11];
            if (localValue13 != null) {
               float localValue15 = this.internalMethod07419(localValue11) + 9.0F;
               float localValue17 = this.internalMethod07471(localValue11) + 9.0F;
               HudRenderUtils.internalMethod08976(localValue5, localValue15, localValue17, localValue2);
               localValue1.drawBatchItem(localValue13.internalMethod06731(), localValue15 - 8.0F, localValue17 - 8.0F);
               HudRenderUtils.internalMethod00012(localValue5);
               MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
            }
         }

         SizedFont localValue12 = Fonts.internalField1154.internalMethod01432(7.0F);

         for (int localValue14 = 0; localValue14 < 41; localValue14++) {
            InventoryBuilderModule.InternalType0023 localValue16 = InventoryBuilderScreen.this.internalField0192 == null
               ? null
               : InventoryBuilderScreen.this.internalField0192.internalField0430[localValue14];
            if (localValue16 != null && localValue16.internalField0228 > 1) {
               String localValue18 = String.valueOf(localValue16.internalField0228);
               localValue1.drawTextWithShadow(
                  localValue12,
                  localValue18,
                  this.internalMethod07419(localValue14) + 18.0F - 1.5F - localValue12.internalMethod00965(localValue18),
                  this.internalMethod07471(localValue14) + 18.0F - 1.5F - localValue12.internalMethod04890(),
                  ThemeColors.internalField1312.mulAlpha(localValue2),
                  ThemeColors.internalField1309.mulAlpha(localValue2),
                  0.8F,
                  0.8F,
                  1.2F
               );
            }
         }
      }

      @Override
      public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
         if (this.inFlow() && this.contains(localValue1, localValue2) && InventoryBuilderScreen.this.internalField0192 != null) {
            int localValue4 = this.internalMethod00264(localValue1, localValue2);
            if (localValue4 < 0) {
               return false;
            } else {
               InventoryBuilderModule.InternalType0023 localValue5 = InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4];
               if (localValue3 == MouseButton.internalField0990) {
                  if (localValue5 != null) {
                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                     InventoryBuilderScreen.this.internalMethod01012(localValue5);
                  }

                  return true;
               } else if (rockstar.client.compat.InputCompat.hasShiftDown()) {
                  if (localValue5 == null) {
                     return true;
                  } else {
                     InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = null;
                     if (InventoryBuilderScreen.this.internalField1055 == localValue4) {
                        InventoryBuilderScreen.this.internalField1055 = -1;
                     }

                     InventoryBuilderModule.internalMethod09171();
                     return true;
                  }
               } else if (localValue3 == MouseButton.internalField0102) {
                  if (InventoryBuilderScreen.this.internalField0191 == null) {
                     if (localValue5 == null) {
                        return true;
                     }

                     InventoryBuilderScreen.this.internalField0191 = localValue5;
                     InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = null;
                     InventoryBuilderScreen.this.internalField1055 = -1;
                  } else if (localValue5 == null) {
                     InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = InventoryBuilderScreen.this.internalField0191;
                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                     InventoryBuilderScreen.this.internalField0191 = null;
                  } else if (InventoryBuilderScreen.this.internalMethod04768(localValue5, InventoryBuilderScreen.this.internalField0191)) {
                     int localValue8 = Math.min(InventoryBuilderScreen.this.internalField0191.internalField0228, InventoryBuilderScreen.this.internalMethod01011(localValue5) - localValue5.internalField0228);
                     localValue5.internalField0228 += localValue8;
                     InventoryBuilderScreen.this.internalField0191.internalField0228 -= localValue8;
                     if (InventoryBuilderScreen.this.internalField0191.internalField0228 <= 0) {
                        InventoryBuilderScreen.this.internalField0191 = null;
                     }

                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                  } else {
                     InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = InventoryBuilderScreen.this.internalField0191;
                     InventoryBuilderScreen.this.internalField0191 = localValue5;
                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                  }

                  InventoryBuilderModule.internalMethod09171();
                  return true;
               } else if (localValue3 == MouseButton.internalField0101) {
                  if (InventoryBuilderScreen.this.internalField0191 == null) {
                     if (localValue5 == null) {
                        return true;
                     }

                     if (localValue5.internalField0228 <= 1) {
                        InventoryBuilderScreen.this.internalField0191 = localValue5;
                        InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = null;
                        InventoryBuilderScreen.this.internalField1055 = -1;
                     } else {
                        int localValue6 = (localValue5.internalField0228 + 1) / 2;
                        InventoryBuilderScreen.this.internalField0191 = localValue5.internalMethod06720();
                        InventoryBuilderScreen.this.internalField0191.internalField0228 = localValue6;
                        localValue5.internalField0228 -= localValue6;
                     }
                  } else if (localValue5 == null) {
                     InventoryBuilderModule.InternalType0023 localValue7 = InventoryBuilderScreen.this.internalField0191.internalMethod06720();
                     localValue7.internalField0228 = 1;
                     InventoryBuilderScreen.this.internalField0192.internalField0430[localValue4] = localValue7;
                     if (--InventoryBuilderScreen.this.internalField0191.internalField0228 <= 0) {
                        InventoryBuilderScreen.this.internalField0191 = null;
                     }

                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                  } else if (InventoryBuilderScreen.this.internalMethod04768(localValue5, InventoryBuilderScreen.this.internalField0191)
                     && localValue5.internalField0228 < InventoryBuilderScreen.this.internalMethod01011(localValue5)) {
                     localValue5.internalField0228++;
                     if (--InventoryBuilderScreen.this.internalField0191.internalField0228 <= 0) {
                        InventoryBuilderScreen.this.internalField0191 = null;
                     }

                     InventoryBuilderScreen.this.internalField1055 = localValue4;
                  }

                  this.internalField0102 = MouseButton.internalField0101;
                  this.internalField0546.clear();
                  this.internalField0546.add(localValue4);
                  InventoryBuilderModule.internalMethod09171();
                  return true;
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      }

      @Override
      public void mouseReleased(float localValue1, float localValue2, MouseButton localValue3) {
         this.internalField0102 = null;
         this.internalField0546.clear();
      }

      @Override
      public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
         if (this.inFlow() && this.contains(localValue1, localValue2) && InventoryBuilderScreen.this.internalField0192 != null) {
            int localValue5 = this.internalMethod00264(localValue1, localValue2);
            if (localValue5 < 0) {
               return false;
            } else {
               InventoryBuilderModule.InternalType0023 localValue6 = InventoryBuilderScreen.this.internalField0192.internalField0430[localValue5];
               if (localValue6 == null) {
                  return false;
               } else {
                  int localValue7 = rockstar.client.compat.InputCompat.hasShiftDown() ? 10 : 1;
                  localValue6.internalField0228 = Math.max(1, Math.min(InventoryBuilderScreen.this.internalMethod01011(localValue6), localValue6.internalField0228 + (localValue4 > 0.0F ? localValue7 : -localValue7)));
                  InventoryBuilderScreen.this.internalField1055 = localValue5;
                  InventoryBuilderModule.internalMethod09171();
                  return true;
               }
            }
         } else {
            return false;
         }
      }
   }
}
