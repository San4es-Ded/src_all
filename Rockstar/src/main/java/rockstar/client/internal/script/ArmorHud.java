package rockstar.client.internal.script;









import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
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
import java.util.List;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public class ArmorHud extends AbstractHudWidget {
   private static final float internalField0205 = 16.0F;
   private static final float internalField0206 = 21.5F;
   private static final float internalField1048 = 6.0F;
   private final ModeSetting internalField0668 = new ModeSetting(this, "hud.targethud.armor");
   private final ModeSetting.InternalType0088 internalField0237 = new ModeSetting.InternalType0088(
         this.internalField0668, "hud.targethud.armor.number"
      )
      .select();
   private final ModeSetting.InternalType0088 internalField0238 = new ModeSetting.InternalType0088(
      this.internalField0668, "hud.targethud.armor.icon"
   );
   private final ModeSetting internalField0669 = new ModeSetting(this, "\u0424\u043e\u043d");
   private final ModeSetting.InternalType0088 internalField1066 = new ModeSetting.InternalType0088(
      this.internalField0669, "\u0421\u0442\u0430\u0440\u044b\u0439"
   );
   private final ModeSetting internalField1272 = new ModeSetting(this, "\u041f\u043e\u0437\u0438\u0446\u0438\u044f");
   private final ModeSetting.InternalType0088 internalField1068 = new ModeSetting.InternalType0088(
      this.internalField1272, "\u0413\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u044c\u043d\u043e"
   );
   private final ModeSetting.InternalType0088 internalField1065 = new ModeSetting.InternalType0088(
      this.internalField1272, "\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u043e"
   );
   private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   private final AnimatedValue[] internalField0556 = new AnimatedValue[4];
   private static final ItemStack[][] internalField0764 = new ItemStack[][]{
      {new ItemStack(Items.LEATHER_BOOTS), new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(Items.LEATHER_HELMET)},
      {
            new ItemStack(Items.CHAINMAIL_BOOTS),
            new ItemStack(Items.CHAINMAIL_LEGGINGS),
            new ItemStack(Items.CHAINMAIL_CHESTPLATE),
            new ItemStack(Items.CHAINMAIL_HELMET)
      },
      {new ItemStack(Items.IRON_BOOTS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Items.IRON_HELMET)},
      {new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Items.GOLDEN_HELMET)},
      {new ItemStack(Items.DIAMOND_BOOTS), new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_CHESTPLATE), new ItemStack(Items.DIAMOND_HELMET)},
      {
            new ItemStack(Items.NETHERITE_BOOTS),
            new ItemStack(Items.NETHERITE_LEGGINGS),
            new ItemStack(Items.NETHERITE_CHESTPLATE),
            new ItemStack(Items.NETHERITE_HELMET)
      }
   };

   public ArmorHud() {
      super("modules.settings.name_tags.elementsToDisplay.armor", "hud/armor");

      for (int localValue1 = 0; localValue1 < this.internalField0556.length; localValue1++) {
         this.internalField0556[localValue1] = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
      }
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      float localValue2 = RenderSystem.getShaderColor()[3];
      if (this.internalField1066.isSelected()) {
         this.internalMethod01725(localValue1, localValue2);
      } else {
         this.internalMethod03516(localValue1, localValue2);
      }
   }

   private void internalMethod01725(UiRenderContext localValue1, float localValue2) {
      boolean localValue3 = this.internalField0237.isSelected();
      SizedFont localValue4 = Fonts.internalField1157.internalMethod01432(6.0F);
      ColorRGBA localValue5 = ThemeColors.internalMethod07738()
         .withAlpha(
            255.0F
               * MathUtils.internalMethod02587(
                  ThemeColors.internalMethod02435().internalMethod08704(),
                  ThemeColors.internalMethod02435().internalMethod08705(),
                  InterfaceModule.internalMethod07584()
               )
         );
      this.internalField0809.internalMethod07062(this.animation.internalMethod02881() * this.visible.internalMethod02881() >= 1.0F);
      float localValue6 = 0.0F;
      int localValue7 = 0;
      List localValue8 = this.internalMethod00528();

      for (ArmorHud.InternalType0518 localValue10 : (Iterable<ArmorHud.InternalType0518>)(Iterable<?>)localValue8) {
         this.internalField0556[localValue7].internalMethod07062(localValue10.internalMethod06407());
         localValue6 += (localValue3 ? 23 : 20) * this.internalField0556[localValue7].internalMethod02881();
         localValue7++;
      }

      boolean localValue22 = this.internalMethod01726(localValue1, this.internalMethod07364(localValue3));
      this.width = this.internalMethod03510(localValue1);
      this.height = this.internalMethod01677(localValue1);
      this.internalField0808.internalMethod07059(localValue6);
      if (!(this.internalField0808.internalMethod02881() <= 0.001F) && !(this.internalField0809.internalMethod02881() <= 0.001F)) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         localValue1.drawItem(Items.DIAMOND_CHESTPLATE, -992.0F, 994.0F, 1.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2);
         float localValue23 = 0.0F;
         ArrayList localValue11 = new ArrayList();

         for (int localValue12 = 0; localValue12 < localValue8.size(); localValue12++) {
            int localValue13 = localValue22 ? localValue8.size() - 1 - localValue12 : localValue12;
            ArmorHud.InternalType0518 localValue14 = (ArmorHud.InternalType0518)localValue8.get(localValue13);
            AnimatedValue localValue15 = this.internalField0556[localValue13];
            float localValue16 = this.internalField0809.internalMethod02881() * localValue15.internalMethod02881();
            String localValue17 = TargetHud.internalMethod03047(localValue14.internalField0878);
            float localValue18 = localValue3 ? 23.0F : 20.0F;
            float localValue19 = localValue22 ? this.x : this.x + (this.width - this.internalField0808.internalMethod02881()) / 2.0F + localValue23;
            float localValue20 = localValue22 ? this.y + (this.height - this.internalField0808.internalMethod02881()) / 2.0F + localValue23 : this.y;
            if (localValue16 <= 0.001F) {
               localValue23 += localValue18 * localValue15.internalMethod02881();
            } else {
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2 * localValue16);
               localValue1.drawRoundedRect(
                  localValue19, localValue20, localValue3 ? 20.0F : 16.0F, localValue3 ? 9.0F : 16.0F, CornerRadii.internalMethod03908(1.5F), localValue5.withAlpha(localValue5.getAlpha() * localValue16)
               );
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2);
               ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue19, localValue20, 16.0F, localValue3 ? 8.5F : 16.0F);
               float localValue21 = localValue2 * localValue16 * (localValue3 ? 0.5F : 1.0F);
               if (!localValue14.internalField0277 || localValue14.internalField0205 < 1.0F) {
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue21 * (localValue14.internalField0277 ? 1.0F - localValue14.internalField0205 : 1.0F));
                  if (localValue3) {
                     this.internalMethod07314(localValue1, localValue14.internalField0878, localValue19 + 2.0F, localValue20 - 4.0F, 1.0F);
                  } else {
                     this.internalMethod07314(localValue1, localValue14.internalField0878, localValue19, localValue20, 1.0F);
                  }
               }

               if (localValue14.internalField0277 && localValue14.internalField0205 > 0.0F) {
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue21 * localValue14.internalField0205);
                  if (localValue3) {
                     this.internalMethod07314(localValue1, localValue14.internalField0879, localValue19 + 2.0F, localValue20 - 4.0F, 1.0F);
                  } else {
                     this.internalMethod07314(localValue1, localValue14.internalField0879, localValue19, localValue20, 1.0F);
                  }
               }

               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2);
               if (this.internalField0238.isSelected()) {
                  this.internalMethod08848(localValue1, localValue14.internalField0878, localValue19, localValue20, localValue16);
               }

               ScissorStack.internalMethod07643();
               if (localValue3) {
                  localValue11.add(new ArmorHud.InternalType0519(localValue17, localValue19 + 10.5F, localValue20 + 2.5F, 255.0F * localValue16));
               }

               localValue23 += localValue18 * localValue15.internalMethod02881();
            }
         }

         this.internalMethod07130(localValue1, localValue4, localValue11);
      }
   }

   private void internalMethod03516(UiRenderContext localValue1, float localValue2) {
      boolean localValue3 = this.internalField0237.isSelected();
      SizedFont localValue4 = Fonts.internalField1157.internalMethod01432(6.0F);
      this.internalField0809.internalMethod07062(this.animation.internalMethod02881() * this.visible.internalMethod02881() >= 1.0F);
      float localValue5 = 0.0F;
      int localValue6 = 0;
      List localValue7 = this.internalMethod00528();

      for (ArmorHud.InternalType0518 localValue9 : (Iterable<ArmorHud.InternalType0518>)(Iterable<?>)localValue7) {
         this.internalField0556[localValue6].internalMethod07062(localValue9.internalMethod06407());
         localValue5 += 21.5F * this.internalField0556[localValue6].internalMethod02881();
         localValue6++;
      }

      float localValue30 = localValue5 <= 0.0F ? 0.0F : localValue5 - 5.5F;
      boolean localValue31 = this.internalMethod01726(localValue1, this.internalMethod01698());
      this.width = this.internalMethod03510(localValue1);
      this.height = this.internalMethod01677(localValue1);
      if (!(localValue30 <= 0.001F)) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         localValue1.drawItem(Items.DIAMOND_CHESTPLATE, -992.0F, 994.0F, 1.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2);
         float localValue10 = localValue31 ? 27.0F : Math.max(12.0F, 12.0F + localValue30);
         float localValue11 = localValue31 ? Math.max(12.0F, 12.0F + localValue30) : 27.0F;
         float localValue12 = this.x + (this.width - localValue10) / 2.0F;
         float localValue13 = this.y + (this.height - localValue11) / 2.0F;
         localValue1.drawClientRect(localValue12, localValue13, localValue10, localValue11, 1.0F, 0.0F, 7.0F, 8.0F);
         float localValue14 = 0.0F;
         ArrayList localValue15 = new ArrayList();
         ArrayList localValue16 = new ArrayList();

         try (CustomDrawContext.InternalType0486 localValue17 = localValue1.beginItemBatch()) {
            for (int localValue18 = 0; localValue18 < localValue7.size(); localValue18++) {
               int localValue19 = localValue31 ? localValue7.size() - 1 - localValue18 : localValue18;
               ArmorHud.InternalType0518 localValue20 = (ArmorHud.InternalType0518)localValue7.get(localValue19);
               AnimatedValue localValue21 = this.internalField0556[localValue19];
               float localValue22 = this.internalField0809.internalMethod02881() * localValue21.internalMethod02881();
               String localValue23 = TargetHud.internalMethod03047(localValue20.internalField0878);
               float localValue24 = localValue31 ? localValue12 + 5.5F : localValue12 + 6.0F + localValue14;
               float localValue25 = localValue31 ? localValue13 + 6.0F + localValue14 : localValue13 + 5.5F;
               float localValue26 = 0.12F * localValue21.internalMethod02881();
               if (localValue22 <= 0.001F) {
                  localValue14 += 21.5F * localValue21.internalMethod02881();
               } else {
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2 * localValue22);
                  HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue24 + 8.0F, localValue25 + 8.0F, 1.0F + localValue26);
                  float localValue27 = localValue2 * localValue22;
                  if (!localValue20.internalField0277 || localValue20.internalField0205 < 1.0F) {
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue27 * (localValue20.internalField0277 ? 1.0F - localValue20.internalField0205 : 1.0F));
                     this.internalMethod07314(localValue1, localValue20.internalField0878, localValue24, localValue25, 1.0F);
                  }

                  if (localValue20.internalField0277 && localValue20.internalField0205 > 0.0F) {
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue27 * localValue20.internalField0205);
                     this.internalMethod07314(localValue1, localValue20.internalField0879, localValue24, localValue25, 1.0F);
                  }

                  HudRenderUtils.internalMethod00012(localValue1.getMatrices());
                  if (this.internalField0238.isSelected() && localValue20.internalField0878.isItemBarVisible()) {
                     localValue16.add(new ArmorHud.InternalType0229(localValue20.internalField0878, localValue24, localValue25, localValue22));
                  }

                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue2);
                  if (localValue3) {
                     localValue15.add(new ArmorHud.InternalType0519(localValue23, localValue24 + 8.0F + 1.5F, localValue25 + 14.0F, 255.0F * localValue22));
                  }

                  localValue14 += 21.5F * localValue21.internalMethod02881();
               }
            }
         }

         for (ArmorHud.InternalType0229 localValue33 : (Iterable<ArmorHud.InternalType0229>)(Iterable<?>)localValue16) {
            this.internalMethod04214(localValue1, localValue33.internalMethod01973(), localValue33.internalMethod02754(), localValue33.internalMethod02755(), localValue33.internalMethod08246());
         }

         this.internalMethod07130(localValue1, localValue4, localValue15);
      }
   }

   private void internalMethod07314(UiRenderContext localValue1, ItemStack localValue2, float localValue3, float localValue4, float localValue5) {
      localValue1.drawBatchItem(localValue2, localValue3, localValue4, localValue5, 80);
   }

   private void internalMethod07130(UiRenderContext localValue1, SizedFont localValue2, List<ArmorHud.InternalType0519> localValue3) {
      if (!localValue3.isEmpty()) {
         PlainGlyphBatch localValue4 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField1157);

         for (ArmorHud.InternalType0519 localValue6 : localValue3) {
            localValue1.drawCenteredText(
               localValue2,
               localValue6.internalMethod07360(),
               localValue6.internalMethod07231(),
               localValue6.internalMethod07256(),
               ThemeColors.internalMethod08459().withAlpha(localValue6.internalMethod08907())
            );
         }

         localValue4.internalMethod09053();
      }
   }

   private boolean internalMethod01726(UiRenderContext localValue1, float localValue2) {
      if (this.internalField1065.isSelected()) {
         return true;
      } else if (this.internalField1068.isSelected()) {
         return false;
      } else {
         float localValue3 = localValue1.getScaledWindowWidth();
         return this.x <= 8.0F || this.x + localValue2 >= localValue3 - 8.0F;
      }
   }

   @Override
   public void update(UiRenderContext localValue1) {
      this.width = this.internalMethod03510(localValue1);
      this.height = this.internalMethod01677(localValue1);
      super.update(localValue1);
   }

   private float internalMethod03510(UiRenderContext localValue1) {
      boolean localValue2 = this.internalField0237.isSelected();
      if (this.internalField1066.isSelected()) {
         return this.internalMethod01726(localValue1, this.internalMethod07364(localValue2)) ? (localValue2 ? 20 : 16) : this.internalMethod07364(localValue2);
      } else {
         return this.internalMethod01726(localValue1, this.internalMethod01698()) ? 27.0F : this.internalMethod01698();
      }
   }

   private float internalMethod01677(UiRenderContext localValue1) {
      boolean localValue2 = this.internalField0237.isSelected();
      if (this.internalField1066.isSelected()) {
         return this.internalMethod01726(localValue1, this.internalMethod07364(localValue2)) ? this.internalMethod07364(localValue2) : (localValue2 ? 9 : 16);
      } else {
         return this.internalMethod01726(localValue1, this.internalMethod01698()) ? this.internalMethod01698() : 27.0F;
      }
   }

   private float internalMethod07364(boolean localValue1) {
      return Math.max(12, (localValue1 ? 23 : 20) * this.internalField0556.length);
   }

   private float internalMethod01698() {
      return 28.0F + 21.5F * (this.internalField0556.length - 1);
   }

   private List<ArmorHud.InternalType0518> internalMethod00528() {
      ArrayList localValue1 = new ArrayList();
      rockstar.client.util.LegacyItemTypes.armorItems(internalField0149.player).forEach(localValue1::add);
      boolean localValue2 = localValue1.stream().allMatch(value -> ((ItemStack)value).isEmpty()) && internalField0149.currentScreen instanceof ChatScreen;
      if (!localValue2) {
         ArrayList localValue6 = new ArrayList();

         for (ItemStack localValue8 : (Iterable<ItemStack>)(Iterable<?>)localValue1) {
            localValue6.add(new ArmorHud.InternalType0518(localValue8, ItemStack.EMPTY, 0.0F, false));
         }

         return localValue6;
      } else {
         ArmorHud.InternalType0230 localValue3 = this.internalMethod04480();
         ArrayList localValue4 = new ArrayList();

         for (int localValue5 = 0; localValue5 < localValue3.internalMethod00587().length; localValue5++) {
            localValue4.add(new ArmorHud.InternalType0518(localValue3.internalMethod00587()[localValue5], localValue3.internalMethod05791()[localValue5], localValue3.internalMethod04556(), true));
         }

         return localValue4;
      }
   }

   private ArmorHud.InternalType0230 internalMethod04480() {
      float localValue1 = (float)(System.currentTimeMillis() % (2200L * internalField0764.length)) / (float)(2200L * internalField0764.length);
      float localValue2 = localValue1 * internalField0764.length;
      int localValue3 = (int)Math.floor(localValue2) % internalField0764.length;
      int localValue4 = (localValue3 + 1) % internalField0764.length;
      float localValue5 = MathHelper.clamp(localValue2 - (float)Math.floor(localValue2), 0.0F, 1.0F);
      float localValue6 = (float)(0.5 - 0.5 * Math.cos(Math.PI * localValue5));
      return new ArmorHud.InternalType0230(internalField0764[localValue3], internalField0764[localValue4], localValue6);
   }

   private void internalMethod04214(UiRenderContext localValue1, ItemStack localValue2, float localValue3, float localValue4, float localValue5) {
      if (localValue2.isItemBarVisible()) {
         float localValue6 = localValue3 + 2.0F;
         float localValue7 = localValue4 + 13.0F;
         localValue1.getMatrices().pushMatrix();
         localValue1.getMatrices().translate(0.0F, 0.0F);
         localValue1.drawRoundedRect(localValue6, localValue7, 13.0F, 1.5F, CornerRadii.internalMethod03908(0.25F), ColorRGBA.WHITE.mulAlpha(0.25F * localValue5));
         localValue1.drawRoundedRect(
            localValue6, localValue7, localValue2.getItemBarStep(), 1.5F, CornerRadii.internalMethod03908(0.25F), ThemeColors.internalMethod02531().mulAlpha(localValue5)
         );
         localValue1.getMatrices().popMatrix();
      }
   }

   private void internalMethod08848(UiRenderContext localValue1, ItemStack localValue2, float localValue3, float localValue4, float localValue5) {
      if (localValue2.isItemBarVisible()) {
         float localValue6 = localValue3 + 2.0F;
         float localValue7 = localValue4 + 13.0F;
         localValue1.getMatrices().pushMatrix();
         localValue1.getMatrices().translate(0.0F, 0.0F);
         localValue1.drawRect(localValue6, localValue7, 13.0F, 2.0F, ColorRGBA.BLACK.withAlpha(255.0F * localValue5));
         localValue1.drawRect(localValue6, localValue7, localValue2.getItemBarStep(), 1.0F, ColorRGBA.fromInt(0xFF000000 | localValue2.getItemBarColor()).withAlpha(255.0F * localValue5));
         localValue1.getMatrices().popMatrix();
      }
   }

   static final class InternalType0229 {
      private final ItemStack internalField0878;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;

      InternalType0229(ItemStack localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0878 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0229[stack=" + this.internalField0878 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", alpha=" + this.internalField1048 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ArmorHud.InternalType0229 other = (ArmorHud.InternalType0229) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048);
      }

      public ItemStack internalMethod01973() {
         return this.internalField0878;
      }

      public float internalMethod02754() {
         return this.internalField0205;
      }

      public float internalMethod02755() {
         return this.internalField0206;
      }

      public float internalMethod08246() {
         return this.internalField1048;
      }
   }

   static final class InternalType0230 {
      private final ItemStack[] internalField0299;
      private final ItemStack[] internalField0300;
      private final float internalField0205;

      InternalType0230(ItemStack[] localValue1, ItemStack[] localValue2, float localValue3) {
         this.internalField0299 = localValue1;
         this.internalField0300 = localValue2;
         this.internalField0205 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0230[current=" + this.internalField0299 + ", next=" + this.internalField0300 + ", blend=" + this.internalField0205 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0299);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0300);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ArmorHud.InternalType0230 other = (ArmorHud.InternalType0230) localValue1;
         return java.util.Objects.equals(this.internalField0299, other.internalField0299)
            && java.util.Objects.equals(this.internalField0300, other.internalField0300)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205);
      }

      public ItemStack[] internalMethod00587() {
         return this.internalField0299;
      }

      public ItemStack[] internalMethod05791() {
         return this.internalField0300;
      }

      public float internalMethod04556() {
         return this.internalField0205;
      }
   }

   static final class InternalType0518 {
      final ItemStack internalField0878;
      final ItemStack internalField0879;
      final float internalField0205;
      final boolean internalField0277;

      InternalType0518(ItemStack localValue1, ItemStack localValue2, float localValue3, boolean localValue4) {
         this.internalField0878 = localValue1;
         this.internalField0879 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0277 = localValue4;
      }

      boolean internalMethod06407() {
         return this.internalField0277 || !this.internalField0878.isEmpty();
      }

      @Override
      public final String toString() {
         return "InternalType0518[primary=" + this.internalField0878 + ", secondary=" + this.internalField0879 + ", blend=" + this.internalField0205 + ", placeholder=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0879);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ArmorHud.InternalType0518 other = (ArmorHud.InternalType0518) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0879, other.internalField0879)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public ItemStack internalMethod04845() {
         return this.internalField0878;
      }

      public ItemStack internalMethod02602() {
         return this.internalField0879;
      }

      public float internalMethod06404() {
         return this.internalField0205;
      }

      public boolean internalMethod06405() {
         return this.internalField0277;
      }
   }

   static final class InternalType0519 {
      private final String internalField0248;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;

      InternalType0519(String localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0248 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0519[text=" + this.internalField0248 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", alpha=" + this.internalField1048 + "]";
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
         ArmorHud.InternalType0519 other = (ArmorHud.InternalType0519) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048);
      }

      public String internalMethod07360() {
         return this.internalField0248;
      }

      public float internalMethod07231() {
         return this.internalField0205;
      }

      public float internalMethod07256() {
         return this.internalField0206;
      }

      public float internalMethod08907() {
         return this.internalField1048;
      }
   }
}
