package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.render.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import pyrock.utility.render.ColorRGBA;

public class TargetHud extends AbstractHudWidget {
   private final BooleanSetting internalField0650 = new BooleanSetting(this, "hud.targethud.look");
   private final ModeSetting internalField0668 = new ModeSetting(this, "hud.targethud.armor");
   private final ModeSetting.InternalType0088 internalField0237 = new ModeSetting.InternalType0088(
      this.internalField0668, "hud.targethud.armor.none"
   );
   private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(
         this.internalField0668, "hud.targethud.armor.number"
      )
      .select();
   private final ModeSetting.InternalType0088 internalField1066 = new ModeSetting.InternalType0088(
      this.internalField0668, "hud.targethud.armor.icon"
   );
   private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1321 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1322 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1323 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1324 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1623 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1618 = new AnimatedValue(300L, 0.0F, Easing.internalField1626);
   private final AnimatedValue internalField1621 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1622 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1619 = new AnimatedValue(500L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1617 = new AnimatedValue(150L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1620 = new AnimatedValue(50L, 0.0F, Easing.internalField1818);
   private final AnimatedValue[] internalField0556 = new AnimatedValue[4];
   private LivingEntity internalField0505;
   private final Stopwatch internalField0519 = new Stopwatch();
   private boolean internalField0277;

   public TargetHud() {
      super("hud.targethud", "hud/target");

      for (int localValue1 = 0; localValue1 < this.internalField0556.length; localValue1++) {
         this.internalField0556[localValue1] = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
      }
   }

   @Override
   public void update(UiRenderContext localValue1) {
      super.update(localValue1);
      this.width = 91.0F;
      this.height = 27.0F;
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      LivingEntity localValue2 = this.internalMethod01309();
      if (localValue2 != null) {
         this.internalField0505 = localValue2;
      }

      if (this.internalField0505 != null) {
         SizedFont localValue3 = Fonts.internalField1154.internalMethod01432(7.0F);
         SizedFont localValue4 = Fonts.internalField1157.internalMethod01432(6.0F);
         SizedFont localValue5 = Fonts.internalField1157.internalMethod01432(7.0F);
         boolean localValue6 = RockstarClient.getInstance().internalMethod04467().internalMethod05065() == ThemeVariant.internalField0395;
         ColorRGBA localValue7 = ThemeColors.internalMethod07738()
            .withAlpha(
               255.0F
                  * MathUtils.internalMethod02587(
                     ThemeColors.internalMethod02435().internalMethod08704(),
                     ThemeColors.internalMethod02435().internalMethod08705(),
                     InterfaceModule.internalMethod07584()
                  )
            );
         boolean localValue8 = UiUtils.internalMethod06450(
            this.x + 26.0F,
            this.y + 2.0F + 6.0F * this.internalField0809.internalMethod02881(),
            Math.min(30.0F, Fonts.internalField1154.internalMethod01432(7.0F).internalMethod00965(this.internalField0505.getName().getString())),
            6.0,
            localValue1
         );
         if (!localValue8 || this.internalField0519.internalMethod02365(1000L)) {
            this.internalField0277 = false;
         }

         boolean localValue9 = this.internalField0505.isUsingItem() && this.internalField0505.getActiveItem().contains(DataComponentTypes.FOOD);
         this.internalField1617.internalMethod07062(localValue9);
         if (localValue9) {
            float localValue10 = (float)Math.sin(System.currentTimeMillis() / 100.0) * 0.5F + 0.5F;
            this.internalField1620.internalMethod07060(localValue10);
         }

         this.internalField1622.internalMethod07062(localValue8);
         this.internalField1619.internalMethod07062(this.internalField0277);
         this.internalField0808.internalMethod07062(this.animation.internalMethod02881() * this.visible.internalMethod02881() >= 1.0F);
         this.internalField0809.internalMethod07062(this.internalField0808.internalMethod02881() >= 0.7F);
         this.internalField1321.internalMethod07062(this.internalField0809.internalMethod02881() >= 0.7F);
         this.internalField1322.internalMethod07062(this.internalField1321.internalMethod02881() >= 0.7F);
         this.internalField1323.internalMethod07062(this.internalField0808.internalMethod02881() >= 0.7F);
         this.internalField1324
            .internalMethod07059(
               (this.internalField0505 instanceof PlayerEntity localValue29 ? GameUtils.internalMethod02919(localValue29) : this.internalField0505.getHealth())
                  / this.internalField0505.getMaxHealth()
            );
         this.internalField1623.internalMethod07059(this.internalField0505.getAbsorptionAmount() / 20.0F);
         float localValue30 = this.internalField0505 instanceof PlayerEntity localValue31 ? GameUtils.internalMethod02919(localValue31) : this.internalField0505.getHealth();
         this.internalField1618.internalMethod07059(localValue30);
         if (this.animation.internalMethod02881() != 0.0F) {
            if (!this.internalField0237.isSelected()) {
               float localValue32 = RenderSystem.getShaderColor()[3];
               ItemStack[] localValue34 = new ItemStack[]{this.internalField0505.getMainHandStack(), this.internalField0505.getOffHandStack()};
               boolean localValue13 = Arrays.stream(localValue34).anyMatch(localValue0 -> !localValue0.isEmpty());

               for (ItemStack localValue15 : rockstar.client.util.LegacyItemTypes.armorItems(this.internalField0505)) {
                  if (!localValue15.isEmpty()) {
                     localValue13 = true;
                     break;
                  }
               }

               if (localValue13) {
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                  localValue1.drawItem(Items.DIAMOND_CHESTPLATE, -992.0F, 994.0F, 1.0F);
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32);
               }

               float localValue37 = 0.0F;
               int localValue39 = 0;

               for (ItemStack localValue17 : rockstar.client.util.LegacyItemTypes.armorItems(this.internalField0505)) {
                  if (!localValue17.isEmpty()) {
                     localValue37 += (this.internalField1066.isSelected() ? 11.0F : 5.0F + localValue4.internalMethod00965(internalMethod03047(localValue17))) + 2.0F;
                  }
               }

               float localValue42 = 11.0F;
               if (this.internalField1066.isSelected()) {
                  for (ItemStack localValue20 : localValue34) {
                     if (!localValue20.isEmpty()) {
                        localValue37 += 13.0F;
                     }
                  }
               }

               this.internalField1621.internalMethod07059(localValue37 - 2.0F);
               float localValue46 = -this.internalField1621.internalMethod02881() / 2.0F;
               ArrayList localValue48 = new ArrayList();

               for (ItemStack localValue54 : rockstar.client.util.LegacyItemTypes.armorItems(this.internalField0505)) {
                  this.internalField0556[localValue39].internalMethod07062(!localValue54.isEmpty());
                  float localValue21 = this.internalField1323.internalMethod02881() * this.internalField0556[localValue39].internalMethod02881();
                  if (localValue21 <= 0.001F) {
                     localValue39++;
                  } else {
                     String localValue22 = internalMethod03047(localValue54);
                     boolean localValue23 = this.internalField1066.isSelected();
                     float localValue24 = localValue23 ? 11.0F : 5.0F + localValue4.internalMethod00965(localValue22);
                     float localValue25 = localValue23 ? 11.0F : 9.0F;
                     float localValue26 = this.x + this.width / 2.0F + localValue46;
                     float localValue27 = this.y + this.height - 4.0F + 6.0F * localValue21;
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32 * localValue21);
                     localValue1.drawBlurredRect(
                        localValue26,
                        localValue27,
                        localValue24,
                        localValue25,
                        5.0F,
                        CornerRadii.internalMethod03908(1.5F),
                        ColorRGBA.WHITE.withAlpha(255.0F * this.animation.internalMethod02881())
                     );
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32);
                     localValue1.drawRoundedRect(localValue26, localValue27, localValue24, localValue25, CornerRadii.internalMethod03908(1.5F), localValue7.withAlpha(localValue7.getAlpha() * localValue21));
                     ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue26, localValue27, localValue24, localValue25);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32 * localValue21 * 0.5F);
                     if (this.internalField0238.isSelected()) {
                        localValue1.drawItem(localValue54, localValue26 - 11.0F + localValue24 / 2.0F + 2.0F, localValue27 - 4.0F, 1.0F);
                     } else {
                        localValue1.drawItem(localValue54, localValue26 - 11.0F + localValue24 / 2.0F + 5.5F, localValue27, 0.7F);
                     }

                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32);
                     ScissorStack.internalMethod07643();
                     if (this.internalField0238.isSelected()) {
                        localValue48.add(new TargetHud.InternalType0299(localValue22, localValue26 + 3.0F, localValue27 + 2.5F, 255.0F * localValue21));
                     }

                     localValue46 += (localValue24 + 2.0F) * localValue21;
                     localValue39++;
                  }
               }

               if (!localValue48.isEmpty()) {
                  PlainGlyphBatch localValue51 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField1157);

                  for (TargetHud.InternalType0299 localValue58 : (Iterable<TargetHud.InternalType0299>)(Iterable<?>)localValue48) {
                     localValue1.drawText(
                        localValue4,
                        localValue58.internalMethod05597(),
                        localValue58.internalMethod06464(),
                        localValue58.internalMethod06496(),
                        ThemeColors.internalMethod08459().withAlpha(localValue58.internalMethod08623())
                     );
                  }

                  localValue51.internalMethod09053();
               }

               float localValue52 = Arrays.stream(localValue34).mapToInt(localValue1x -> localValue1x.isEmpty() ? 0 : (int)(localValue42 + 2.0F)).sum() - 2;
               float localValue56 = this.internalField0238.isSelected() ? -localValue52 / 2.0F : localValue46;
               boolean localValue59 = false;

               for (ItemStack localValue64 : rockstar.client.util.LegacyItemTypes.armorItems(this.internalField0505)) {
                  if (!localValue64.isEmpty()) {
                     localValue59 = true;
                     break;
                  }
               }

               for (ItemStack localValue68 : localValue34) {
                  if (!localValue68.isEmpty()) {
                     float localValue69 = this.x + this.width / 2.0F + localValue56;
                     float localValue70 = this.y
                        + this.height
                        - 4.0F
                        + 6.0F * this.internalField1323.internalMethod02881()
                        + (this.internalField0238.isSelected() && localValue59 ? 12 : 0);
                     float localValue28 = this.internalField1323.internalMethod02881()
                        * (localValue9 && this.internalField0505.getActiveItem() == localValue68 ? 0.5F + 0.7F * this.internalField1620.internalMethod02881() : 1.0F);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32 * localValue28);
                     localValue1.drawBlurredRect(
                        localValue69,
                        localValue70,
                        localValue42,
                        localValue42,
                        5.0F,
                        CornerRadii.internalMethod03908(1.5F),
                        ColorRGBA.WHITE.withAlpha(255.0F * this.animation.internalMethod02881())
                     );
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32);
                     localValue1.drawRoundedRect(localValue69, localValue70, localValue42, localValue42, CornerRadii.internalMethod03908(1.5F), localValue7.withAlpha(localValue7.getAlpha() * localValue28));
                     ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue69, localValue70, localValue42, localValue42);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32 * localValue28);
                     localValue1.drawItem(localValue68, localValue69 - 11.0F + localValue42 / 2.0F + 5.5F, localValue70, 0.7F);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue32);
                     ScissorStack.internalMethod07643();
                     localValue56 += localValue42 + 2.0F;
                     if (this.internalField1066.isSelected()) {
                        localValue46 += (localValue42 + 2.0F) * this.internalField1323.internalMethod02881();
                     }
                  }
               }
            }

            if (this.dragAnim.internalMethod02881() > 0.0F) {
               localValue1.drawShadow(
                  this.x - 5.0F,
                  this.y - 5.0F,
                  this.width + 10.0F,
                  this.height + 10.0F,
                  15.0F,
                  CornerRadii.internalMethod03908(ThemeColors.internalMethod02435().internalMethod01359()),
                  ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.internalMethod02881())
               );
            }

            localValue1.drawClientRect(this.x, this.y, this.width, this.height, this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 7.0F);
            float localValue33 = 255.0F * this.internalField0808.internalMethod02881();
            float localValue35 = 255.0F * this.internalField0809.internalMethod02881();
            float localValue36 = 255.0F * this.internalField1322.internalMethod02881();
            float localValue38 = 255.0F * this.internalField1321.internalMethod02881();
            ScissorStack.internalMethod06303(localValue1.getMatrices(), this.x, this.y, this.width, this.height);
            if (this.internalField0505 instanceof AbstractClientPlayerEntity localValue40) {
               localValue1.drawHead(
                  localValue40,
                  this.x + 5.0F * this.internalField0808.internalMethod02881(),
                  this.y + 5.0F,
                  17.0F,
                  CornerRadii.internalMethod03908(4.0F),
                  ThemeColors.internalField1312.withAlpha(localValue33)
               );
            } else {
               localValue1.drawRoundedTexture(
                  RockstarClient.id(
                     InterfaceModule.internalMethod09717()
                        ? "icons/hud/whoglass.png"
                        : (
                           RockstarClient.getInstance().internalMethod04467().internalMethod05065() == ThemeVariant.internalField0395
                              ? "icons/hud/whodark.png"
                              : "icons/hud/who.png"
                        )
                  ),
                  this.x + 5.0F * this.internalField0808.internalMethod02881(),
                  this.y + 5.0F,
                  17.0F,
                  17.0F,
                  CornerRadii.internalMethod03908(4.0F),
                  ThemeColors.internalField1312.withAlpha(localValue33)
               );
            }

            NameProtectModule localValue41 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
            String localValue44 = this.internalField0505.getName().getString();
            if (localValue41.isEnabled()) {
               localValue44 = localValue41.internalMethod08287(localValue44);
            }

            String localValue47 = localValue30 == 1000.0F ? "?" : TextUtils.internalMethod07254(this.internalField1618.internalMethod02881()).replace(",", ".");
            localValue1.drawFadeoutText(
               localValue3,
               localValue44,
               this.x + 26.0F + 8.0F * this.internalField1622.internalMethod02881(),
               this.y + 2.0F + 6.0F * this.internalField0809.internalMethod02881(),
               ThemeColors.internalMethod08459().withAlpha(localValue35),
               0.7F,
               1.0F,
               this.width - 37.0F - 8.0F * this.internalField1622.internalMethod02881() - localValue5.internalMethod00965(localValue47)
            );
            float localValue49 = localValue35 * this.internalField1622.internalMethod02881() * (1.0F - this.internalField1619.internalMethod02881());
            if (localValue49 > 0.5F) {
               HudRenderUtils.internalMethod02865(
                  localValue1.getMatrices(),
                  this.x + 24.0F + 5.0F * this.internalField1622.internalMethod02881(),
                  this.y + 5.0F + 6.0F * this.internalField0809.internalMethod02881(),
                  90.0F * this.internalField1619.internalMethod02881()
               );
               localValue1.drawIcon(
                  "copy",
                  this.x + 21.0F + 5.0F * this.internalField1622.internalMethod02881(),
                  this.y + 2.0F + 6.0F * this.internalField0809.internalMethod02881(),
                  6.0F,
                  ThemeColors.internalMethod08459().withAlpha(localValue49)
               );
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }

            float localValue53 = localValue35 * this.internalField1622.internalMethod02881() * this.internalField1619.internalMethod02881();
            if (localValue53 > 0.5F) {
               HudRenderUtils.internalMethod02865(
                  localValue1.getMatrices(),
                  this.x + 24.0F + 5.0F * this.internalField1622.internalMethod02881(),
                  this.y + 5.0F + 6.0F * this.internalField0809.internalMethod02881(),
                  -90.0F + 90.0F * this.internalField1619.internalMethod02881()
               );
               localValue1.drawIcon(
                  "check",
                  this.x + 21.0F + 5.0F * this.internalField1622.internalMethod02881(),
                  this.y + 2.0F + 6.0F * this.internalField0809.internalMethod02881(),
                  6.0F,
                  ThemeColors.internalField0776.withAlpha(localValue53)
               );
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }

            float localValue57 = this.y + this.height - 5.0F - 6.0F * this.internalField1321.internalMethod02881();
            float localValue60 = 57.0F * Math.clamp(this.internalField1324.internalMethod02881(), 0.0F, 1.0F);
            float localValue63 = 57.0F * Math.clamp(this.internalField1623.internalMethod02881(), 0.0F, 1.0F);
            TextBatchRenderer localValue66 = new TextBatchRenderer(Fonts.internalField1157, 1.5F);
            localValue66.internalMethod00978(
               rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue1.getMatrices()),
               localValue47,
               localValue4.internalMethod07850(),
               this.x + this.width - 7.0F - localValue4.internalMethod00965(localValue47),
               this.y + 2.0F + 6.0F * this.internalField1322.internalMethod02881(),
               0.0F,
               ThemeColors.internalMethod02531().withAlpha(localValue36).getRGB()
            );
            localValue66.internalMethod04362(
               rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue1.getMatrices()),
               this.x + 26.0F,
               localValue57,
               57.0F,
               3.0F,
               ThemeColors.internalMethod08573().withAlpha(localValue38 * (1.0F - 0.7F * InterfaceModule.internalMethod07584()))
            );
            localValue66.internalMethod04362(
               rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue1.getMatrices()), this.x + 26.0F, localValue57, localValue60, 3.0F, ThemeColors.internalMethod02531().withAlpha(localValue38)
            );
            if (!ServerUtils.internalMethod01786(KnownServer.internalField0578) && localValue63 > 0.0F) {
               localValue66.internalMethod04362(
                  rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue1.getMatrices()), this.x + 83.0F - localValue63, localValue57, localValue63, 3.0F, new ColorRGBA(255.0F, 220.0F, 81.0F, localValue38)
               );
            }

            localValue66.internalMethod03841();
            ScissorStack.internalMethod07643();
         }
      }
   }

   public static String internalMethod03047(ItemStack localValue0) {
      if (!localValue0.isEmpty() && localValue0.isDamageable()) {
         int localValue1 = localValue0.getMaxDamage();
         int localValue2 = localValue0.getDamage();
         if (localValue2 >= localValue1) {
            return "0%";
         } else {
            double localValue3 = 100.0 - (double)localValue2 / localValue1 * 100.0;
            return String.format("%.0f%%", localValue3);
         }
      } else {
         return "100%";
      }
   }

   private LivingEntity internalMethod01309() {
      LivingEntity localValue2 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue3 ? localValue3 : null;
      if (localValue2 != null) {
         return localValue2;
      } else if (this.internalField0650.internalMethod04496() && internalField0149.targetedEntity instanceof LivingEntity localValue5) {
         return localValue5;
      } else {
         return internalField0149.currentScreen instanceof ChatScreen ? internalField0149.player : null;
      }
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.internalField0505 != null
         && UiUtils.internalMethod05785(
            this.x + 26.0F,
            this.y + 2.0F + 6.0F * this.internalField0809.internalMethod02881(),
            Math.min(30.0F, Fonts.internalField1154.internalMethod01432(7.0F).internalMethod00965(this.internalField0505.getName().getString())),
            6.0,
            localValue1,
            localValue3
         )) {
         TextUtils.internalMethod05864(internalField0149.player.getName().getString());
         this.internalField0519.internalMethod00701();
         this.internalField0277 = true;
      } else {
         super.onMouseClicked(localValue1, localValue3, localValue5);
      }
   }

   @Override
   public boolean show() {
      return this.internalMethod01309() != null;
   }

   static final class InternalType0299 {
      private final String internalField0248;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;

      InternalType0299(String localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0248 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0299[text=" + this.internalField0248 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", alpha=" + this.internalField1048 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TargetHud.InternalType0299 other = (TargetHud.InternalType0299) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048);
      }

      public String internalMethod05597() {
         return this.internalField0248;
      }

      public float internalMethod06464() {
         return this.internalField0205;
      }

      public float internalMethod06496() {
         return this.internalField0206;
      }

      public float internalMethod08623() {
         return this.internalField1048;
      }
   }
}
