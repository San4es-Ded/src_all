package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.esp.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import pyrock.utility.render.ColorRGBA;

public class EspEditorScreen extends LayeredRockstarScreen implements MinecraftClientAccess, ScreenMetricsAccess, WindowAccess {
   private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
   private final GlowBloomRenderer internalField0320 = GlowBloomRenderer.internalMethod06776();
   private EntityTargetType internalField0027;
   private EntityTargetType internalField0028;
   private PlayerTargetType internalField0025;
   private ItemTargetType internalField0012;
   private int internalField1053;
   private int internalField1055;
   private boolean internalField0277;
   private ZombieEntity internalField0585;
   private ItemEntity internalField0831;
   private Entity internalField0410;
   private LivingEntity internalField0505;
   private final AnimatedValue internalField0808;
   private boolean internalField0276;
   private Entity internalField0411;
   private UiContainer internalField0634;
   private UiContainer internalField0635;
   private UiContainer internalField1257;
   private KeybindSetting internalField0648;
   private static float internalField1456 = 45.0F;
   private static float internalField1457 = 0.0F;
   private static float internalField1458 = 50.0F;
   private static float internalField1459 = 0.0F;
   private float internalField1460;
   private float internalField1461;
   private float internalField1462;
   private boolean internalField1099;
   private int internalField1056;
   private int internalField1054;
   private float internalField1455;
   private float internalField1723;
   private float internalField1731;
   private float internalField1727;
   private final AnimatedValue internalField0809;
   private final AnimatedValue internalField1321;
   private final AnimatedValue internalField1322;
   private final AnimatedValue internalField1323;
   private final AnimatedValue internalField1324;
   private float internalField1717;
   private long internalField0229;

   public EspEditorScreen() {
      this.internalField0027 = EntityTargetType.internalField0027;
      this.internalField0028 = EntityTargetType.internalField0027;
      this.internalField0025 = PlayerTargetType.internalField0025;
      this.internalField0012 = ItemTargetType.internalField0013;
      this.internalField0808 = new AnimatedValue(400L, 1.0F, Easing.internalField1814);
      this.internalField0276 = true;
      this.internalField0411 = null;
      this.internalField1460 = 45.0F;
      this.internalField1461 = 0.0F;
      this.internalField1462 = 0.0F;
      this.internalField1099 = false;
      this.internalField0809 = new AnimatedValue(400L, Easing.internalField0812);
      this.internalField1321 = new AnimatedValue(400L, Easing.internalField0812);
      this.internalField1322 = new AnimatedValue(400L, 200.0F, Easing.internalField0812);
      this.internalField1323 = new AnimatedValue(400L, 50.0F, Easing.internalField0812);
      this.internalField1324 = new AnimatedValue(300L, Easing.internalField0812);
   }

   @Override
   public void init() {
      super.init();
      this.clearRoots();
      this.internalField0320.internalMethod03093();
      this.internalField0635 = new UiContainer().internalMethod01863().internalMethod03062(4.0F).internalMethod09339(142.0F);
      this.internalField1257 = new UiContainer().internalMethod01863().internalMethod03062(4.0F).internalMethod09339(142.0F);
      this.internalField0634 = new UiContainer()
         .internalMethod01863()
         .internalMethod09339(288.0F)
         .internalMethod08755()
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(-5.0F)
               .internalMethod02066(1.0F, 6.0F)
               .internalMethod00894(2.5F)
               .internalMethod04404(
                  localValue0x -> ColorRGBA.BLACK
                     .mix(ColorRGBA.WHITE, 0.3F)
                     .withAlpha(255.0F * (0.32F + 0.28F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
               )
         )
         .internalMethod03907(
            new UiContainer()
               .internalMethod05895()
               .internalMethod03062(4.0F)
               .internalMethod09609()
               .internalMethod03907(this.internalField0635)
               .internalMethod03907(this.internalField1257)
         );
      this.internalField0634.snapSize();
      this.add(this.internalField0634);
      this.internalMethod00030();
   }

   private void internalMethod00030() {
      List localValue1 = this.internalMethod06948();
      ArrayList localValue2 = new ArrayList();
      ArrayList localValue3 = new ArrayList();

      for (int localValue4 = 0; localValue4 < localValue1.size(); localValue4++) {
         (localValue4 % 2 == 0 ? localValue2 : localValue3).add(this.internalMethod05223((EspFeature)localValue1.get(localValue4)));
      }

      this.internalField0635.internalMethod07849(localValue2);
      this.internalField1257.internalMethod07849(localValue3);
      this.internalField0634.internalMethod03631();
   }

   private UiContainer internalMethod05223(EspFeature localValue1) {
      BooleanSetting localValue2 = this.internalMethod06056(localValue1);
      KeybindSetting localValue3 = this.internalMethod06055(localValue1);
      UiElement localValue4 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(6.0F),
            () -> localValue3 == null ? "" : TextUtils.internalMethod04982(localValue3.internalMethod07477()),
            localValue2x -> (localValue3 != null && this.internalField0648 == localValue3 ? ThemeColors.internalField1310 : ThemeColors.internalField1613)
               .mulAlpha(0.75F)
         )
         .textInset(3.0F)
         .height(10.0F)
         .radius(2.5F)
         .background(localValue0 -> ThemeColors.internalField1614)
         .visibleWhen(() -> localValue3 != null && localValue3.internalMethod07477() != -1)
         .interactive(false);
      UiContainer localValue5 = new UiContainer()
         .internalMethod09266(18.0F)
         .internalMethod03062(5.0F)
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod09609()
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(
            new TextLabel(
                  Fonts.internalField1154.internalMethod01432(8.0F),
                  () -> localValue3 != null && this.internalField0648 == localValue3
                     ? (
                        KeybindUtils.internalMethod06867() != 0
                           ? KeybindUtils.internalMethod07434(KeybindUtils.internalMethod06867()) + "..."
                           : LanguageManager.internalMethod07214("menu.binding")
                     )
                     : (localValue2 == null ? localValue1.internalMethod01940() : LanguageManager.internalMethod07214(localValue2.getName()))
               )
               .internalMethod02959(
                  localValue2x -> (localValue3 != null && this.internalField0648 == localValue3 ? ThemeColors.internalField1310 : ThemeColors.internalField1613)
                     .mulAlpha(0.75F + 0.25F * localValue2x.hover())
               )
               .internalMethod05903(0.75F)
               .fill()
         )
         .internalMethod03907(localValue4)
         .internalMethod03907(
            new ToggleSwitchWidget(() -> localValue2 != null && localValue2.internalMethod04496())
               .internalMethod05792(() -> ThemeColors.internalField1614)
               .size(13.0F, 8.0F)
               .minSize(13.0F, 8.0F)
               .snapSize()
               .transition(UiTransition.internalField0918)
         )
         .internalMethod05382((localValue3x, localValue4x, localValue5x) -> {
            if (localValue3x == MouseButton.internalField0102) {
               if (localValue2 != null) {
                  localValue2.toggle();
               }

               if (this.internalField0648 == localValue3) {
                  this.internalField0648 = null;
               }
            } else if (localValue3 != null) {
               this.internalField0648 = this.internalField0648 == localValue3 ? null : localValue3;
            }
         });
      UiContainer localValue6 = new UiContainer().internalMethod01863().internalMethod09609();

      for (Setting localValue8 : this.internalMethod02494(localValue1)) {
         localValue6.internalMethod03907(SettingComponentFactory.internalMethod03724(localValue8));
      }

      return new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod00105(3.0F, 0.0F, 3.0F, 0.0F))
         .internalMethod08336(Motion.internalMethod01870(60L))
         .internalMethod07178(
            (localValue1x, localValue2x) -> {
               localValue1x.drawRoundedRect(
                  localValue2x.x(), localValue2x.y(), localValue2x.w(), localValue2x.h(), CornerRadii.internalMethod03908(5.0F), ThemeColors.internalMethod08573().mulAlpha(0.3F)
               );
               if (localValue1 instanceof FriendMarkerEspFeature) {
                  FriendMarkerEspFeature.internalMethod02388(localValue2x.hover() > 0.05F);
               }
            }
         )
         .internalMethod03907(localValue5)
         .internalMethod03907(localValue6);
   }

   public void tick() {
      GuiMoveModule.internalMethod09597();
      if (!this.internalField1099 && this.internalField1461 != 0.0F) {
         this.internalField1461 *= 0.85F;
         if (Math.abs(this.internalField1461) < 0.5F) {
            this.internalField1461 = 0.0F;
         }
      }

      this.internalField1462 += 3.0F;
      if (this.internalField1462 >= 360.0F) {
         this.internalField1462 -= 360.0F;
      }

      super.tick();
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.internalField0648 != null && localValue5 != MouseButton.internalField0102) {
         this.internalField0648.internalMethod02164(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
         this.internalField0648 = null;
      } else {
         if (localValue5 == MouseButton.internalField0102) {
            this.internalField0277 = true;
            if (this.internalMethod07543((float)localValue1, (float)localValue3)) {
               this.internalField1099 = true;
               this.internalField1056 = (int)localValue1;
               this.internalField1054 = (int)localValue3;
            }
         }

         super.onMouseClicked(localValue1, localValue3, localValue5);
      }
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      if (localValue5 == MouseButton.internalField0102) {
         this.internalField1099 = false;
      }

      super.onMouseReleased(localValue1, localValue3, localValue5);
   }

   private void internalMethod07544(int localValue1, int localValue2) {
      if (this.internalField1099) {
         float localValue3 = localValue1 - this.internalField1056;
         float localValue4 = localValue2 - this.internalField1054;
         this.internalField1460 -= localValue3 * 1.5F;
         this.internalField1461 -= localValue4 * 0.3F;
         this.internalField1461 = Math.max(-60.0F, Math.min(60.0F, this.internalField1461));
         this.internalField1056 = localValue1;
         this.internalField1054 = localValue2;
      }
   }

   private boolean internalMethod07543(float localValue1, float localValue2) {
      return localValue1 >= this.internalField1455
         && localValue1 <= this.internalField1455 + this.internalField1731
         && localValue2 >= this.internalField1723
         && localValue2 <= this.internalField1723 + this.internalField1727;
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0648 == null && !KeybindButton.internalMethod06277()) {
         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 90 && SettingHistoryManager.internalMethod07603()) {
            return true;
         }

         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 89 && SettingHistoryManager.internalMethod07605()) {
            return true;
         }
      }

      return this.internalMethod07545(keyCode, modifiers) ? true : super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0648 != null) {
         int localValue4 = KeybindUtils.internalMethod08281(keyCode, modifiers);
         if (localValue4 != Integer.MIN_VALUE) {
            this.internalField0648.internalMethod02164(localValue4);
            this.internalField0648 = null;
            return true;
         }
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   private boolean internalMethod07545(int localValue1, int localValue2) {
      if (this.internalField0648 == null) {
         return false;
      } else {
         if (localValue1 != 256 && localValue1 != 261) {
            int localValue3 = KeybindUtils.internalMethod06041(localValue1, localValue2);
            if (localValue3 == Integer.MIN_VALUE) {
               return true;
            }

            this.internalField0648.internalMethod02164(localValue3);
         } else {
            this.internalField0648.internalMethod02164(-1);
         }

         this.internalField0648 = null;
         return true;
      }
   }

   private void internalMethod00035() {
      long localValue1 = System.currentTimeMillis();
      float localValue3 = this.internalField0229 == 0L ? 16.0F : Math.min(64.0F, (float)(localValue1 - this.internalField0229));
      this.internalField0229 = localValue1;
      MenuModule localValue4 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
      int localValue5 = localValue4 != null && localValue4.internalMethod00598() != null ? localValue4.internalMethod00598().internalMethod07477() : -1;
      float localValue6 = internalMethod03669(localValue5) ? 1.0F : 0.0F;
      float localValue7 = localValue3 / 300.0F;
      if (this.internalField1717 < localValue6) {
         this.internalField1717 = Math.min(localValue6, this.internalField1717 + localValue7);
      } else if (this.internalField1717 > localValue6) {
         this.internalField1717 = Math.max(localValue6, this.internalField1717 - localValue7);
      }

      this.contentAlpha = 1.0F - Easing.internalField1626.ease(this.internalField1717, 0.0F, 1.0F, 1.0F);
   }

   private static boolean internalMethod03669(int localValue0) {
      return KeybindUtils.internalMethod08521(localValue0);
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalMethod00035();
      this.internalField1053 = localValue1.internalMethod05259();
      this.internalField1055 = localValue1.internalMethod05261();
      this.internalMethod07544(this.internalField1053, this.internalField1055);
      float localValue2 = internalField0389.internalMethod03585() / 2.0F - 223.5F;
      float localValue3 = internalField0389.internalMethod03589() / 2.0F - 111.5F;
      boolean localValue4 = this.contentAlpha > 0.01F;
      if (localValue4) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.contentAlpha);
      }

      if (localValue4) {
         this.internalMethod03505(localValue1, localValue2, localValue3, 302.0F, 223.0F);
      }

      FriendMarkerEspFeature localValue5 = EspManager.internalMethod06726().internalMethod05464(FriendMarkerEspFeature.class);
      boolean localValue6 = this.internalField0027 == EntityTargetType.internalField0027
         && this.internalField0025 == PlayerTargetType.internalField0961
         && localValue5 != null
         && localValue5.internalMethod01517()
         && FriendMarkerEspFeature.internalMethod08145();
      this.internalField0809.internalMethod07062(localValue6);
      float localValue7 = 1.8F;
      Entity localValue8 = this.internalMethod07615();
      float localValue9 = localValue8 != null ? localValue8.getHeight() : localValue7;
      float localValue10 = localValue9 / localValue7;
      float localValue11 = 200.0F;
      float localValue12 = localValue6 ? 223.0F : localValue11 * localValue10;
      if (this.internalField0027 == EntityTargetType.internalField0963) {
         localValue12 += 50.0F;
      }

      if (this.internalField0027 == EntityTargetType.internalField0964) {
         localValue12 += 100.0F;
      }

      this.internalField1321.internalMethod07059(localValue6 ? 1.4F : (this.internalField0027 == EntityTargetType.internalField0964 ? 0.3F : 0.0F));
      this.internalField1322.internalMethod07059(localValue12);
      float localValue13 = 132.0F;
      float localValue14 = localValue13 + 20.0F;
      float localValue15 = localValue13 + (localValue14 - localValue13) * this.internalField0809.internalMethod02881();
      float localValue16 = this.internalField1322.internalMethod02881();
      float localValue17 = this.internalField0027 == EntityTargetType.internalField0964 ? 70.0F : 50.0F;
      this.internalField1323.internalMethod07059(localValue17);
      int localValue18 = (int)this.internalField1323.internalMethod02881();
      if (localValue4) {
         this.internalMethod03505(localValue1, localValue2 + 245.0F + 65.0F, localValue3, localValue15, localValue16);
      }

      this.internalField1455 = localValue2 + 245.0F + 65.0F;
      this.internalField1723 = localValue3;
      this.internalField1731 = localValue15;
      this.internalField1727 = localValue16;
      float localValue19 = this.internalField1321.internalMethod02881();
      if (localValue4) {
         ScissorStack.internalMethod06303(localValue1.getMatrices(), this.internalField1455, this.internalField1723, localValue15, localValue16);
         this.internalMethod04980(localValue1, (int)this.internalField1455, (int)this.internalField1723, (int)localValue15, (int)localValue16, localValue18, localValue19);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.contentAlpha);
         this.internalMethod03708(localValue1, (int)this.internalField1455, (int)this.internalField1723, (int)localValue15, (int)localValue16);
         ScissorStack.internalMethod07643();
         this.internalMethod00844(localValue1, localValue2, localValue3);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      this.internalField1324.internalMethod07059(this.internalField0027.internalMethod03264() ? 1.0F : 0.0F);
      float localValue20 = localValue3 + 24.0F + 16.0F * this.internalField1324.internalMethod02881();
      float localValue21 = 185.0F - 10.0F * this.internalField1324.internalMethod02881();
      this.internalField0634.snapAt(localValue2 + 7.0F, localValue20);
      this.internalField0634.internalMethod09266(localValue21);
      super.render(localValue1);
      this.internalField0277 = false;
   }

   private boolean internalMethod06393(float localValue1, float localValue2, float localValue3, float localValue4) {
      return this.internalField1053 >= localValue1 && this.internalField1053 <= localValue1 + localValue3 && this.internalField1055 >= localValue2 && this.internalField1055 <= localValue2 + localValue4;
   }

   private void internalMethod00844(UiRenderContext localValue1, float localValue2, float localValue3) {
      float localValue4 = localValue2 + 7.0F;
      float localValue5 = localValue3 + 7.0F;

      for (EntityTargetType localValue9 : EntityTargetType.values()) {
         localValue4 = this.internalMethod00774(localValue1, localValue4, localValue5, localValue9.internalMethod04227(), this.internalField0027 == localValue9, () -> {
            if (this.internalField0027 != localValue9) {
               this.internalField0411 = this.internalMethod07615();
               this.internalField0276 = localValue9.ordinal() > this.internalField0027.ordinal();
               this.internalField0808.internalMethod07060(0.0F);
               this.internalField0648 = null;
               this.internalField0027 = localValue9;
               if (localValue9 == EntityTargetType.internalField0027) {
                  this.internalField0025 = PlayerTargetType.internalField0025;
               }

               if (localValue9 == EntityTargetType.internalField0964) {
                  this.internalField0012 = ItemTargetType.internalField0013;
               }

               this.internalMethod00030();
            }
         });
      }

      if (this.internalField0027 == EntityTargetType.internalField0027) {
         localValue4 = localValue2 + 7.0F;
         localValue5 = localValue3 + 24.0F;

         for (PlayerTargetType localValue20 : PlayerTargetType.values()) {
            localValue4 = this.internalMethod00774(localValue1, localValue4, localValue5, localValue20.internalMethod00118(), this.internalField0025 == localValue20, () -> {
               if (this.internalField0025 != localValue20) {
                  this.internalField0648 = null;
                  this.internalField0025 = localValue20;
                  this.internalMethod00030();
               }
            });
         }
      } else if (this.internalField0027 == EntityTargetType.internalField0964) {
         localValue4 = localValue2 + 7.0F;
         localValue5 = localValue3 + 24.0F;

         for (ItemTargetType localValue21 : ItemTargetType.values()) {
            localValue4 = this.internalMethod00774(localValue1, localValue4, localValue5, localValue21.internalMethod00716(), this.internalField0012 == localValue21, () -> {
               if (this.internalField0012 != localValue21) {
                  this.internalField0648 = null;
                  this.internalField0012 = localValue21;
                  this.internalMethod00030();
               }
            });
         }
      }
   }

   private float internalMethod00774(UiRenderContext localValue1, float localValue2, float localValue3, String localValue4, boolean localValue5, Runnable localValue6) {
      String localValue7 = LanguageManager.internalMethod07214(localValue4);
      float localValue8 = Fonts.internalField1154.internalMethod05670(localValue7, 7.0F);
      float localValue9 = localValue8 + 8.0F;
      boolean localValue10 = this.internalMethod06393(localValue2, localValue3, localValue9, 13.0F);
      float localValue11 = localValue5 ? 1.0F : (localValue10 ? 0.8F : 0.4F);
      localValue1.drawRoundedRect(localValue2, localValue3, localValue9, 13.0F, CornerRadii.internalMethod03908(3.0F), ThemeColors.internalMethod08573().mulAlpha(localValue11));
      localValue1.drawText(
         Fonts.internalField1154.internalMethod01432(7.0F),
         localValue7,
         localValue2 + 4.0F,
         localValue3 + 4.0F,
         localValue5 ? ThemeColors.internalMethod08459() : ThemeColors.internalMethod08459().mulAlpha(0.75F)
      );
      if (localValue10 && this.internalField0277) {
         localValue6.run();
      }

      return localValue2 + localValue9 + 4.0F;
   }

   private List<EspFeature> internalMethod06948() {
      ArrayList localValue1 = new ArrayList();

      for (EspFeature localValue3 : EspManager.internalMethod06726().internalMethod02968()) {
         if (localValue3.internalMethod05593(this.internalField0027)
            && (this.internalField0027 != EntityTargetType.internalField0027 || localValue3.internalMethod05541(this.internalField0025))
            && (
               this.internalField0027 != EntityTargetType.internalField0964
                  || (
                     localValue3.internalMethod08691().isEmpty()
                        ? this.internalField0012 == ItemTargetType.internalField0013
                        : localValue3.internalMethod02299(this.internalField0012)
                  )
            )) {
            localValue1.add(localValue3);
         }
      }

      return localValue1;
   }

   private List<Setting> internalMethod02494(EspFeature localValue1) {
      ArrayList localValue2 = new ArrayList();
      if (this.internalField0027 == EntityTargetType.internalField0027) {
         localValue2.addAll(localValue1.internalMethod04288(this.internalField0025));
         localValue2.addAll(localValue1.internalMethod03913(this.internalField0027));
      } else if (this.internalField0027 == EntityTargetType.internalField0964 && !localValue1.internalMethod08691().isEmpty()) {
         localValue2.addAll(localValue1.internalMethod00082(this.internalField0012));
         localValue2.addAll(localValue1.internalMethod03913(this.internalField0027));
      } else {
         localValue2.addAll(localValue1.internalMethod06578(this.internalField0027));
         localValue2.addAll(localValue1.internalMethod03913(this.internalField0027));
      }

      return localValue2;
   }

   private BooleanSetting internalMethod06056(EspFeature localValue1) {
      if (this.internalField0027 == EntityTargetType.internalField0027) {
         return localValue1.internalMethod06156(this.internalField0025);
      } else {
         return this.internalField0027 == EntityTargetType.internalField0964 && !localValue1.internalMethod08691().isEmpty()
            ? localValue1.internalMethod06073(this.internalField0012)
            : localValue1.internalMethod02947(this.internalField0027);
      }
   }

   private KeybindSetting internalMethod06055(EspFeature localValue1) {
      if (this.internalField0027 == EntityTargetType.internalField0027) {
         return localValue1.internalMethod06155(this.internalField0025);
      } else {
         return this.internalField0027 == EntityTargetType.internalField0964 && !localValue1.internalMethod08691().isEmpty()
            ? localValue1.internalMethod06072(this.internalField0012)
            : localValue1.internalMethod02946(this.internalField0027);
      }
   }

   private void internalMethod04980(UiRenderContext localValue1, int localValue2, int localValue3, int localValue4, int localValue5, int localValue6, float localValue7) {
      this.internalField0808.internalMethod07059(1.0F);
      float localValue8 = this.internalField0808.internalMethod02881();
      float localValue9 = localValue4 * 1.5F;
      if (localValue8 < 1.0F && this.internalField0411 != null) {
         float localValue10 = this.internalField0276 ? -localValue9 * localValue8 : localValue9 * localValue8;
         this.internalMethod05462(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, this.internalField0411, localValue10);
      }

      Entity localValue12 = this.internalMethod07615();
      if (localValue12 != null) {
         float localValue11 = 0.0F;
         if (localValue8 < 1.0F) {
            localValue11 = this.internalField0276 ? localValue9 * (1.0F - localValue8) : -localValue9 * (1.0F - localValue8);
         }

         this.internalMethod05462(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue12, localValue11);
      }
   }

   private void internalMethod05462(UiRenderContext localValue1, int localValue2, int localValue3, int localValue4, int localValue5, int localValue6, float localValue7, Entity localValue8, float localValue9) {
      GlowEspFeature localValue10 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
      boolean localValue11;
      if (localValue10 == null) {
         localValue11 = false;
      } else if (this.internalField0027 == EntityTargetType.internalField0027) {
         localValue11 = localValue10.internalMethod06170(this.internalField0025);
      } else if (this.internalField0027 == EntityTargetType.internalField0964) {
         localValue11 = localValue10.internalMethod02927(this.internalField0012);
      } else {
         localValue11 = localValue10.internalMethod06206(this.internalField0027);
      }

      float localValue12 = localValue2 + localValue4 / 2.0F + localValue9;
      float localValue13 = localValue3 + localValue5 / 2.0F;
      float localValue14 = localValue6;
      Quaternionf localValue15 = new Quaternionf().rotateZ((float) Math.PI);
      localValue15.rotateX((float)Math.toRadians(this.internalField1461));
      internalField1456 = this.internalField1460;
      internalField1457 = this.internalField1461;
      internalField1458 = localValue14;
      internalField1459 = localValue7;
      if (localValue8 instanceof LivingEntity localValue16) {
         float[] localValue17 = this.internalMethod05777(localValue16);
         float localValue18 = 180.0F + this.internalField1460;
         this.internalMethod05778(localValue16, localValue18);
         if (this.internalField0027 == EntityTargetType.internalField0027) {
            GlowEspFeature.internalMethod00006(this.internalField0025);
         }

         if (this.internalField0027 == EntityTargetType.internalField0964) {
            GlowEspFeature.internalMethod05055(this.internalField0012);
         }

         GlowEspFeature.internalMethod00072(this.internalField0027);
         FriendMarkerEspFeature localValue19 = EspManager.internalMethod06726().internalMethod05464(FriendMarkerEspFeature.class);
         boolean localValue20 = this.internalField0027 == EntityTargetType.internalField0027
            && this.internalField0025 == PlayerTargetType.internalField0961
            && localValue19 != null
            && localValue19.internalMethod01517()
            && FriendMarkerEspFeature.internalMethod08145();
         if (localValue20) {
            FriendMarkerEspFeature.internalMethod02332(true);
         }

         this.internalMethod00266(rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getMatrices()), localValue12, localValue13, localValue14, localValue15, localValue7, 1.0F, 0.0F, localValue8);
         if (localValue20) {
            FriendMarkerEspFeature.internalMethod02332(false);
         }

         GlowEspFeature.internalMethod00072(null);
         GlowEspFeature.internalMethod00006(null);
         GlowEspFeature.internalMethod05055(null);
         if (localValue11 && localValue9 == 0.0F) {
            this.internalMethod07066(localValue1, localValue12, localValue13, localValue14, localValue15, localValue7, localValue8);
         }

         this.internalMethod05270(localValue16, localValue17);
      } else {
         Quaternionf localValue21 = new Quaternionf().rotateZ((float) Math.PI);
         localValue21.rotateY((float)Math.toRadians(this.internalField1462));
         this.internalMethod00266(rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getMatrices()), localValue12, localValue13, localValue14, localValue21, localValue7, 1.0F, 0.0F, localValue8);
      }
   }

   private float[] internalMethod05777(LivingEntity localValue1) {
      return new float[]{localValue1.bodyYaw, localValue1.lastBodyYaw, localValue1.getYaw(), localValue1.lastYaw, localValue1.getPitch(), localValue1.lastPitch, localValue1.lastHeadYaw, localValue1.headYaw};
   }

   private void internalMethod05778(LivingEntity localValue1, float localValue2) {
      localValue1.bodyYaw = localValue1.lastBodyYaw = localValue2;
      localValue1.setYaw(localValue2);
      localValue1.lastYaw = localValue2;
      localValue1.setPitch(0.0F);
      localValue1.lastPitch = 0.0F;
      localValue1.headYaw = localValue1.lastHeadYaw = localValue2;
   }

   private void internalMethod05270(LivingEntity localValue1, float[] localValue2) {
      localValue1.bodyYaw = localValue2[0];
      localValue1.lastBodyYaw = localValue2[1];
      localValue1.setYaw(localValue2[2]);
      localValue1.lastYaw = localValue2[3];
      localValue1.setPitch(localValue2[4]);
      localValue1.lastPitch = localValue2[5];
      localValue1.lastHeadYaw = localValue2[6];
      localValue1.headYaw = localValue2[7];
   }

   private void internalMethod07066(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, Quaternionf localValue5, float localValue6, Entity localValue7) {
      boolean localValue8 = GL11.glIsEnabled(3089);
      if (localValue8) {
         GL11.glDisable(3089);
      }

      this.internalField0769.internalMethod02227(true);
      if (localValue8) {
         GL11.glEnable(3089);
      }

      GlowEspFeature.internalField0277 = true;
      GlowEspFeature.internalField0410 = localValue7;
      if (this.internalField0027 == EntityTargetType.internalField0027) {
         GlowEspFeature.internalMethod00006(this.internalField0025);
      }

      if (this.internalField0027 == EntityTargetType.internalField0964) {
         GlowEspFeature.internalMethod05055(this.internalField0012);
      }

      GlowEspFeature.internalMethod00072(this.internalField0027);
      this.internalMethod00266(rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getMatrices()), localValue2, localValue3, localValue4, localValue5, localValue6, 1.0F, 0.0F, localValue7);
      GlowEspFeature.internalMethod00072(null);
      GlowEspFeature.internalMethod00006(null);
      GlowEspFeature.internalMethod05055(null);
      GlowEspFeature.internalField0410 = null;
      GlowEspFeature.internalField0277 = false;
      this.internalField0769.internalMethod03248();
      GlowEspFeature localValue9 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
      this.internalField0320.internalMethod06580((int)localValue9.internalMethod01315().internalMethod08576());
      this.internalField0320.internalMethod06579(7.0F);
      this.internalField0320.internalMethod08567(1.0F);
      this.internalField0320.internalMethod08555(1.0F);
      if (localValue8) {
         GL11.glDisable(3089);
      }

      this.internalField0320.internalMethod02623(this.internalField0769, -1, -1, -1, -1);
      if (localValue8) {
         GL11.glEnable(3089);
      }

      int localValue10 = this.internalField0320.internalMethod03092();
      if (localValue10 != 0) {
         RenderSystem.setShaderTexture(0, localValue10);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.contentAlpha);
         RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
         RenderPipeline.internalMethod01737(0.0F, -0.5F, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
         RenderPipeline.internalMethod01737(0.0F, -0.5F, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
         RenderSystem.depthMask(true);
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         RenderSystem.disableDepthTest();
      }
   }

   private void internalMethod03708(UiRenderContext localValue1, int localValue2, int localValue3, int localValue4, int localValue5) {
      Entity localValue6 = this.internalMethod07615();
      if (localValue6 != null) {
         float localValue7 = localValue2 + localValue4 / 2.0F;
         float localValue8 = localValue3 + localValue5 / 2.0F - 35.0F;

         for (EspFeature localValue10 : EspManager.internalMethod06726().internalMethod02968()) {
            if (localValue10.internalMethod05593(this.internalField0027)) {
               boolean localValue11;
               if (this.internalField0027 == EntityTargetType.internalField0027) {
                  localValue11 = localValue10.internalMethod06170(this.internalField0025);
               } else if (this.internalField0027 == EntityTargetType.internalField0964 && !localValue10.internalMethod08691().isEmpty()) {
                  localValue11 = localValue10.internalMethod02927(this.internalField0012);
               } else {
                  localValue11 = localValue10.internalMethod06206(this.internalField0027);
               }

               if (localValue11) {
                  localValue10.internalMethod06439(localValue1, localValue6, localValue7, localValue8, this.internalField0027, this.internalField0025);
               }
            }
         }
      }
   }

   private Entity internalMethod07615() {
      if (internalField0149.world == null) {
         return null;
      } else {
         return (Entity)(switch (this.internalField0027) {
            case internalField0027 -> this.internalMethod03934();
            case internalField0028 -> this.internalMethod06211();
            case internalField0963 -> this.internalMethod01955();
            case internalField0964 -> this.internalMethod01711();
         });
      }
   }

   private LivingEntity internalMethod03934() {
      if (this.internalField0505 == null && internalField0149.world != null) {
         GameProfile localValue1 = new GameProfile(UUID.randomUUID(), "Preview");
         OtherClientPlayerEntity localValue2 = new OtherClientPlayerEntity(internalField0149.world, localValue1);
         localValue2.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
         localValue2.equipStack(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
         localValue2.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
         this.internalField0505 = localValue2;
      }

      return this.internalField0505;
   }

   private ZombieEntity internalMethod06211() {
      if (this.internalField0585 == null && internalField0149.world != null) {
         this.internalField0585 = new ZombieEntity(EntityType.ZOMBIE, internalField0149.world);
      }

      return this.internalField0585;
   }

   private Entity internalMethod01955() {
      if (this.internalField0410 == null && internalField0149.world != null) {
         this.internalField0410 = new PigEntity(EntityType.PIG, internalField0149.world);
      }

      return this.internalField0410;
   }

   private ItemEntity internalMethod01711() {
      if (this.internalField0831 == null && internalField0149.world != null) {
         this.internalField0831 = new ItemEntity(EntityType.ITEM, internalField0149.world);
         this.internalField0831.setStack(new ItemStack(Items.NETHERITE_SWORD));
      }

      return this.internalField0831;
   }

   private void internalMethod00266(MatrixStack localValue1, float localValue2, float localValue3, float localValue4, Quaternionf localValue5, float localValue6, float localValue7, float localValue8, Entity localValue9) {
      localValue1.push();
      localValue1.translate(localValue2, localValue3, 50.0F);
      localValue1.scale(localValue4, localValue4, -localValue4);
      localValue1.multiply(localValue5);
      localValue1.translate(0.0F, -localValue9.getHeight() / 2.0F - localValue6 * localValue7, 0.0F);
      RenderSystem.disableDepthTest();
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.contentAlpha);
      EntityRenderManager localValue10 = internalField0149.getEntityRenderDispatcher();
      Immediate localValue11 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
      EntityRenderer localValue12 = localValue10.getRenderer(localValue9);

      try {
         rockstar.client.render.LegacyRenderCompat.renderEntity(localValue10, localValue9, localValue8, 0.0, 0.0, 0.0, localValue1, localValue11);
         localValue11.draw();
      } catch (Exception localValue14) {
      }

      RenderSystem.enableDepthTest();
      localValue1.pop();
   }

   private void internalMethod03505(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      localValue1.drawShadow(localValue2, localValue3, localValue4, localValue5, 25.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.5F));
      localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, 5.0F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1312);
      localValue1.drawSquircle(localValue2, localValue3, localValue4, localValue5, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1612);
      localValue1.drawSquircleBorder(localValue2, localValue3, localValue4, localValue5, 0.5F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1616);
   }

   public void close() {
      this.internalField0648 = null;
      super.close();
      MenuModule.internalMethod09723();
   }

   @Generated
   public static float internalMethod00029() {
      return internalField1456;
   }

   @Generated
   public static float internalMethod00034() {
      return internalField1457;
   }

   @Generated
   public static float internalMethod08401() {
      return internalField1458;
   }

   @Generated
   public static float internalMethod08412() {
      return internalField1459;
   }
}
