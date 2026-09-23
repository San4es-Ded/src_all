package rockstar.client.internal.script;











import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import pyrock.utility.render.ColorRGBA;

public class SwingEditorPanel extends LegacyUiElement {
   private static final float internalField1049 = 32.0F;
   private static final float internalField1046 = 28.0F;
   private static final float[] internalField0615 = new float[]{0.0F, 0.0F, 0.0F};
   private SwingEditorPanel.InternalType0001 internalField0143;
   private SwingEditorPanel.InternalType0502 internalField0473;
   private float internalField1727;
   private float internalField1728;
   private float internalField1717;
   private float internalField1718;
   private float internalField1719;
   private boolean internalField0277;
   private final ItemStack internalField0878;
   private float[] internalField0616;
   private int internalField1055;
   private final AnimatedValue internalField0808;
   private final AnimatedValue[] internalField0556;
   private final AnimatedValue[] internalField0557;
   private final AnimatedValue[] internalField1204;
   private final AnimatedValue[] internalField1203;
   private float internalField1721;
   private float internalField1722;
   private float internalField1720;
   private boolean internalField0276;
   private final AnimatedValue internalField0809;
   private float[] internalField1238;
   private final float[] internalField1240;
   private final float[] internalField1239;
   private final float[] internalField1237;
   private boolean internalField1099;
   private float[] internalField1574;
   private float internalField1730;
   private float internalField1729;
   private float internalField1725;
   private float internalField1726;
   private boolean internalField1100;
   private boolean internalField1102;
   private int internalField1056;
   private double internalField0194;
   private double internalField0193;
   private float internalField1724;
   private float internalField1732;
   private Vec2f internalField0281;
   private final float[] internalField1573;
   private final float[] internalField1577;
   private final float[] internalField1575;
   private final Deque<float[]> internalField0796;
   private final Deque<float[]> internalField0797;
   private float[] internalField1576;
   private final Matrix4f internalField0788;
   private Vec2f internalField0280;
   private final Vec2f[] internalField0723;
   private final Vec2f[][] internalField0429;
   private final float[] internalField1578;
   private final float[] internalField1579;
   private float internalField1843;
   private final float[] internalField1580;
   private final float[] internalField1805;
   private float internalField1844;
   private float internalField1845;
   private final AnimatedValue internalField1321;
   private static final ColorRGBA[] internalField0748 = new ColorRGBA[]{ColorRGBA.RED, ColorRGBA.GREEN, ColorRGBA.BLUE};
   private static final float[] internalField1806 = new float[]{
      0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 0.0F, 1.0F, 2.0F
   };

   public SwingEditorPanel() {
      this.internalField0143 = SwingEditorPanel.InternalType0001.internalField0143;
      this.internalField0473 = SwingEditorPanel.InternalType0502.internalField0473;
      this.internalField1727 = 22.0F;
      this.internalField1728 = -12.0F;
      this.internalField1717 = 120.0F;
      this.internalField0277 = false;
      this.internalField0878 = new ItemStack(Items.NETHERITE_SWORD);
      this.internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
      this.internalField0556 = internalMethod04209(180L);
      this.internalField0557 = internalMethod04209(220L);
      this.internalField1204 = internalMethod04209(180L);
      this.internalField1203 = internalMethod04209(220L);
      this.internalField0276 = false;
      this.internalField0809 = new AnimatedValue(260L, Easing.internalField1626);
      this.internalField1240 = new float[3];
      this.internalField1239 = new float[3];
      this.internalField1237 = new float[2];
      this.internalField1099 = false;
      this.internalField1100 = false;
      this.internalField1102 = false;
      this.internalField1056 = -1;
      this.internalField0281 = Vec2f.ZERO;
      this.internalField1573 = new float[3];
      this.internalField1577 = new float[3];
      this.internalField1575 = new float[3];
      this.internalField0796 = new ArrayDeque<>();
      this.internalField0797 = new ArrayDeque<>();
      this.internalField0788 = new Matrix4f();
      this.internalField0280 = Vec2f.ZERO;
      this.internalField0723 = new Vec2f[]{Vec2f.ZERO, Vec2f.ZERO, Vec2f.ZERO};
      this.internalField0429 = new Vec2f[3][44];
      this.internalField1578 = new float[3];
      this.internalField1579 = new float[3];
      this.internalField1580 = new float[3];
      this.internalField1805 = new float[3];
      this.internalField1321 = new AnimatedValue(180L, Easing.internalField1626);
   }

   private static AnimatedValue[] internalMethod04209(long localValue0) {
      AnimatedValue[] localValue2 = new AnimatedValue[3];

      for (int localValue3 = 0; localValue3 < 3; localValue3++) {
         localValue2[localValue3] = new AnimatedValue(localValue0, Easing.internalField1626);
      }

      return localValue2;
   }

   private SwingTransformSettings internalMethod04482() {
      return this.internalField0143 == SwingEditorPanel.InternalType0001.internalField0144
         ? RockstarClient.getInstance().internalMethod00061().internalMethod05639()
         : RockstarClient.getInstance().internalMethod00061().internalMethod04274();
   }

   @Override
   public void internalMethod05619(UiRenderContext localValue1) {
      if (!this.internalField0276 && internalField0149.player != null) {
         this.internalMethod06140();
      }

      this.internalMethod09409();
      this.internalMethod07848();
      if (this.internalField1099) {
         this.internalField0809.internalMethod07062(true);
         float localValue2 = this.internalField0809.internalMethod02881();
         this.internalField1721 = this.internalField1240[0] + (this.internalField1239[0] - this.internalField1240[0]) * localValue2;
         this.internalField1722 = this.internalField1240[1] + (this.internalField1239[1] - this.internalField1240[1]) * localValue2;
         this.internalField1720 = this.internalField1240[2] + (this.internalField1239[2] - this.internalField1240[2]) * localValue2;
         this.internalField1718 = this.internalField1237[0] * (1.0F - localValue2);
         this.internalField1719 = this.internalField1237[1] * (1.0F - localValue2);
         if (localValue2 >= 1.0F) {
            this.internalField1099 = false;
            this.internalField1238 = null;
         }
      }

      if (this.internalField1102 && this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
         float[] localValue5 = this.internalMethod02024(this.internalMethod08180());
         this.internalField1721 = this.internalField1573[0] - localValue5[0];
         this.internalField1722 = this.internalField1573[1] - localValue5[1];
         this.internalField1720 = this.internalField1573[2] - localValue5[2];
      }

      this.internalField0808.internalMethod07062(true);
      float localValue6 = Math.min(1.0F, this.internalField0808.internalMethod02881());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue6);
      HudRenderUtils.internalMethod08976(
         localValue1.getMatrices(),
         this.internalField0205 + this.internalField1048 / 2.0F,
         this.internalField0206 + this.internalField1047 / 2.0F,
         0.5F + this.internalField0808.internalMethod02881() * 0.5F
      );
      this.internalMethod08897(localValue1);
      this.internalMethod08168(localValue1);
      this.internalField1730 = this.internalField0205;
      this.internalField1729 = this.internalField0206 + 32.0F;
      this.internalField1725 = this.internalField1048;
      this.internalField1726 = this.internalField1047 - 32.0F - 28.0F;
      float localValue3 = this.internalField1730 + this.internalField1725 / 2.0F;
      float localValue4 = this.internalField1729 + this.internalField1726 / 2.0F;
      ScissorStack.internalMethod06303(localValue1.getMatrices(), this.internalField1730, this.internalField1729, this.internalField1725, this.internalField1726);
      if (internalField0149.player != null) {
         this.internalMethod01461(localValue1, localValue3, localValue4);
         if (this.internalField0143 != SwingEditorPanel.InternalType0001.internalField1025) {
            this.internalMethod07813(localValue1);
         }
      }

      ScissorStack.internalMethod07643();
      this.internalMethod09731(localValue1);
      HudRenderUtils.internalMethod00012(localValue1.getMatrices());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void internalMethod01461(UiRenderContext localValue1, float localValue2, float localValue3) {
      float[] localValue4 = this.internalMethod08180();
      float localValue5 = localValue4[0];
      float localValue6 = localValue4[1];
      float localValue7 = localValue4[2];
      float localValue8 = localValue4[3];
      float localValue9 = localValue4[4];
      float localValue10 = localValue4[5];
      float localValue11 = localValue4[6];
      float localValue12 = localValue4[7];
      float localValue13 = localValue4[8];
      MatrixStack localValue14 = rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getMatrices());
      localValue14.push();
      localValue14.translate(localValue2 + this.internalField1718, localValue3 + this.internalField1719, 250.0F);
      localValue14.scale(this.internalField1717, -this.internalField1717, this.internalField1717);
      localValue14.multiply(new Quaternionf().rotateX((float)Math.toRadians(this.internalField1728)).rotateY((float)Math.toRadians(this.internalField1727)));
      localValue14.translate(this.internalField1721, this.internalField1722, this.internalField1720);
      this.internalField0788.set(localValue14.peek().getPositionMatrix());
      localValue14.translate(localValue5, localValue6, localValue7);
      localValue14.translate(localValue8, localValue9, localValue10);
      localValue14.multiply(new Quaternionf().rotationXYZ((float)Math.toRadians(localValue11), (float)Math.toRadians(localValue12), (float)Math.toRadians(localValue13)));
      localValue14.translate(-localValue5, -localValue6, -localValue7);
      localValue14.translate(0.56F, -0.52F, -0.72F);
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(true);
      RenderSystem.disableCull();
      GL11.glClear(256);
      HeldItemRenderer localValue15 = internalField0149.getEntityRenderDispatcher().getHeldItemRenderer();
      Immediate localValue16 = internalField0149.getBufferBuilders().getEntityVertexConsumers();

      try {
         rockstar.client.render.LegacyRenderCompat.renderItem(internalField0149.player, this.internalField0878, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, localValue14, localValue16, 15728880);
         localValue16.draw();
      } catch (Exception localValue18) {
      }

      RenderSystem.enableCull();
      localValue14.pop();
   }

   private void internalMethod07813(UiRenderContext localValue1) {
      float[] localValue2 = this.internalField0473 == SwingEditorPanel.InternalType0502.internalField1168
         ? this.internalMethod00708()
         : this.internalMethod02024(this.internalMethod08180());
      if (!this.internalField1102 && !this.internalField1100 && !this.internalField1099) {
         Vec2f localValue3 = this.internalMethod06892(localValue2[0], localValue2[1], localValue2[2]);
         if (localValue3.x < this.internalField1730
            || localValue3.x > this.internalField1730 + this.internalField1725
            || localValue3.y < this.internalField1729
            || localValue3.y > this.internalField1729 + this.internalField1726) {
            this.internalMethod07847();
         }
      }

      this.internalField0280 = this.internalMethod06892(localValue2[0], localValue2[1], localValue2[2]);
      if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
         for (int localValue6 = 0; localValue6 < 3; localValue6++) {
            this.internalField0429[localValue6] = this.internalMethod01812(localValue2[0], localValue2[1], localValue2[2], localValue6);
         }
      } else {
         this.internalField0723[0] = this.internalMethod06892(localValue2[0] + 0.45F, localValue2[1], localValue2[2]);
         this.internalField0723[1] = this.internalMethod06892(localValue2[0], localValue2[1] + 0.45F, localValue2[2]);
         this.internalField0723[2] = this.internalMethod06892(localValue2[0], localValue2[1], localValue2[2] + 0.45F);
      }

      int localValue7 = this.internalField1102 ? this.internalField1056 : this.internalMethod02516(localValue1.internalMethod05259(), localValue1.internalMethod05261());
      if (localValue7 >= 0 || this.internalField1102) {
         CursorManager.internalMethod06882(CursorType.internalField0567);
      }

      RenderSystem.disableDepthTest();
      UiBatchRenderer.internalMethod02576();
      if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
         for (int localValue4 = 0; localValue4 < 3; localValue4++) {
            this.internalMethod03992(localValue1, localValue2, localValue4, internalField0748[localValue4].mulAlpha(localValue4 == localValue7 ? 1.0F : 0.6F));
         }
      } else {
         for (int localValue8 = 0; localValue8 < 3; localValue8++) {
            this.internalMethod05331(localValue1, this.internalField0280, this.internalField0723[localValue8], internalField0748[localValue8].mulAlpha(localValue8 == localValue7 ? 1.0F : 0.7F));
         }
      }

      boolean localValue9 = this.internalField0473 == SwingEditorPanel.InternalType0502.internalField1168;
      float localValue5 = localValue9 ? 2.5F : 1.5F;
      localValue1.drawRoundedRect(
         this.internalField0280.x - localValue5,
         this.internalField0280.y - localValue5,
         localValue5 * 2.0F,
         localValue5 * 2.0F,
         CornerRadii.internalMethod03908(localValue5),
         (localValue9 ? ThemeColors.internalMethod02531() : ThemeColors.internalField1312).mulAlpha(0.9F)
      );
      RenderSystem.enableDepthTest();
   }

   private void internalMethod05331(UiRenderContext localValue1, Vec2f localValue2, Vec2f localValue3, ColorRGBA localValue4) {
      this.internalMethod04753(localValue1, localValue2, localValue3, localValue4);
      float localValue5 = (float)Math.atan2(localValue3.y - localValue2.y, localValue3.x - localValue2.x);
      float localValue6 = 4.0F;
      this.internalMethod04753(localValue1, localValue3, new Vec2f(localValue3.x - localValue6 * (float)Math.cos(localValue5 - 0.42F), localValue3.y - localValue6 * (float)Math.sin(localValue5 - 0.42F)), localValue4);
      this.internalMethod04753(localValue1, localValue3, new Vec2f(localValue3.x - localValue6 * (float)Math.cos(localValue5 + 0.42F), localValue3.y - localValue6 * (float)Math.sin(localValue5 + 0.42F)), localValue4);
   }

   private void internalMethod04753(UiRenderContext localValue1, Vec2f localValue2, Vec2f localValue3, ColorRGBA localValue4) {
      float localValue5 = (localValue3.x - localValue2.x) / 3.0F;
      float localValue6 = (localValue3.y - localValue2.y) / 3.0F;
      this.internalMethod05117(localValue1, localValue2, new Vec2f(localValue2.x + localValue5, localValue2.y + localValue6), new Vec2f(localValue2.x + 2.0F * localValue5, localValue2.y + 2.0F * localValue6), localValue3, localValue4);
   }

   private void internalMethod05117(UiRenderContext localValue1, Vec2f localValue2, Vec2f localValue3, Vec2f localValue4, Vec2f localValue5, ColorRGBA localValue6) {
      float localValue7 = 3.5F;
      float localValue8 = Math.min(Math.min(localValue2.x, localValue3.x), Math.min(localValue4.x, localValue5.x)) - localValue7;
      float localValue9 = Math.min(Math.min(localValue2.y, localValue3.y), Math.min(localValue4.y, localValue5.y)) - localValue7;
      float localValue10 = Math.max(Math.max(localValue2.x, localValue3.x), Math.max(localValue4.x, localValue5.x)) + localValue7;
      float localValue11 = Math.max(Math.max(localValue2.y, localValue3.y), Math.max(localValue4.y, localValue5.y)) + localValue7;
      localValue1.drawSmoothBezier(localValue8, localValue9, localValue10 - localValue8, localValue11 - localValue9, localValue2, localValue3, localValue4, localValue5, 1.5F, localValue6);
   }

   private void internalMethod03992(UiRenderContext localValue1, float[] localValue2, int localValue3, ColorRGBA localValue4) {
      Vector3f localValue5 = internalMethod05657(localValue3);
      Vector3f localValue6 = internalMethod02170(localValue3);
      Vector3f localValue7 = new Vector3f(localValue2[0], localValue2[1], localValue2[2]);
      float localValue8 = 0.2761424F;

      for (int localValue9 = 0; localValue9 < 4; localValue9++) {
         double localValue10 = localValue9 * Math.PI / 2.0;
         double localValue12 = localValue10 + (Math.PI / 2);
         Vector3f localValue14 = internalMethod01364(localValue7, localValue5, localValue6, localValue10);
         Vector3f localValue15 = internalMethod01364(localValue7, localValue5, localValue6, localValue12);
         Vector3f localValue16 = internalMethod06483(localValue5, localValue6, localValue10);
         Vector3f localValue17 = internalMethod06483(localValue5, localValue6, localValue12);
         this.internalMethod05117(
            localValue1,
            this.internalMethod06892(localValue14.x, localValue14.y, localValue14.z),
            this.internalMethod06892(localValue14.x + localValue16.x * localValue8, localValue14.y + localValue16.y * localValue8, localValue14.z + localValue16.z * localValue8),
            this.internalMethod06892(localValue15.x - localValue17.x * localValue8, localValue15.y - localValue17.y * localValue8, localValue15.z - localValue17.z * localValue8),
            this.internalMethod06892(localValue15.x, localValue15.y, localValue15.z),
            localValue4
         );
      }
   }

   private static Vector3f internalMethod05657(int localValue0) {
      return localValue0 == 0 ? new Vector3f(0.0F, 1.0F, 0.0F) : (localValue0 == 1 ? new Vector3f(0.0F, 0.0F, 1.0F) : new Vector3f(1.0F, 0.0F, 0.0F));
   }

   private static Vector3f internalMethod02170(int localValue0) {
      return localValue0 == 0 ? new Vector3f(0.0F, 0.0F, 1.0F) : (localValue0 == 1 ? new Vector3f(1.0F, 0.0F, 0.0F) : new Vector3f(0.0F, 1.0F, 0.0F));
   }

   private static Vector3f internalMethod01364(Vector3f localValue0, Vector3f localValue1, Vector3f localValue2, double localValue3) {
      float localValue5 = (float)Math.cos(localValue3) * 0.5F;
      float localValue6 = (float)Math.sin(localValue3) * 0.5F;
      return new Vector3f(localValue0.x + localValue1.x * localValue5 + localValue2.x * localValue6, localValue0.y + localValue1.y * localValue5 + localValue2.y * localValue6, localValue0.z + localValue1.z * localValue5 + localValue2.z * localValue6);
   }

   private static Vector3f internalMethod06483(Vector3f localValue0, Vector3f localValue1, double localValue2) {
      float localValue4 = (float)Math.cos(localValue2);
      float localValue5 = (float)Math.sin(localValue2);
      return new Vector3f(-localValue0.x * localValue5 + localValue1.x * localValue4, -localValue0.y * localValue5 + localValue1.y * localValue4, -localValue0.z * localValue5 + localValue1.z * localValue4);
   }

   private Vec2f[] internalMethod01812(float localValue1, float localValue2, float localValue3, int localValue4) {
      Vec2f[] localValue5 = new Vec2f[44];

      for (int localValue6 = 0; localValue6 < 44; localValue6++) {
         double localValue7 = (Math.PI * 2) * localValue6 / 44.0;
         float localValue9 = 0.5F * (float)Math.cos(localValue7);
         float localValue10 = 0.5F * (float)Math.sin(localValue7);
         float localValue11;
         float localValue12;
         float localValue13;
         switch (localValue4) {
            case 0:
               localValue11 = 0.0F;
               localValue12 = localValue9;
               localValue13 = localValue10;
               break;
            case 1:
               localValue11 = localValue10;
               localValue12 = 0.0F;
               localValue13 = localValue9;
               break;
            default:
               localValue11 = localValue9;
               localValue12 = localValue10;
               localValue13 = 0.0F;
         }

         localValue5[localValue6] = this.internalMethod06892(localValue1 + localValue11, localValue2 + localValue12, localValue3 + localValue13);
      }

      return localValue5;
   }

   private Vec2f internalMethod06892(float localValue1, float localValue2, float localValue3) {
      Vector4f localValue4 = new Vector4f(localValue1, localValue2, localValue3, 1.0F);
      this.internalField0788.transform(localValue4);
      return new Vec2f(localValue4.x, localValue4.y);
   }

   private float[] internalMethod00708() {
      float[] localValue1 = this.internalMethod08180();
      return new float[]{localValue1[0] + localValue1[3], localValue1[1] + localValue1[4], localValue1[2] + localValue1[5]};
   }

   private float[] internalMethod02024(float[] localValue1) {
      float[] localValue2 = this.internalMethod00767();
      Vector3f localValue3 = new Vector3f(0.56F + localValue2[0] - localValue1[0], -0.52F + localValue2[1] - localValue1[1], -0.72F + localValue2[2] - localValue1[2]);
      new Quaternionf().rotationXYZ((float)Math.toRadians(localValue1[6]), (float)Math.toRadians(localValue1[7]), (float)Math.toRadians(localValue1[8])).transform(localValue3);
      return new float[]{localValue1[0] + localValue1[3] + localValue3.x, localValue1[1] + localValue1[4] + localValue3.y, localValue1[2] + localValue1[5] + localValue3.z};
   }

   private float[] internalMethod00767() {
      if (this.internalField0616 != null) {
         return this.internalField0616;
      } else if (internalField0149.player == null) {
         return internalField0615;
      } else {
         SwingEditorPanel.InternalType0501 localValue1 = new SwingEditorPanel.InternalType0501();

         try {
            rockstar.client.render.LegacyRenderCompat.renderItem(
                  internalField0149.player,
                  this.internalField0878,
                  ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                  false,
                  new MatrixStack(),
                  localValue1x -> localValue1,
                  15728880
               );
         } catch (Exception localValue3) {
         }

         this.internalField0616 = localValue1.internalMethod01849();
         if (this.internalField0616 == null && ++this.internalField1055 > 20) {
            this.internalField0616 = internalField0615;
         }

         return this.internalField0616 != null ? this.internalField0616 : internalField0615;
      }
   }

   private float[] internalMethod07094(SwingTransformSettings localValue1) {
      float[] localValue2 = this.internalMethod03976(localValue1);
      float[] localValue3 = this.internalMethod02024(localValue2);
      return this.internalField0473 == SwingEditorPanel.InternalType0502.internalField1168
            && this.internalField0143 != SwingEditorPanel.InternalType0001.internalField1025
         ? new float[]{-(localValue3[0] + localValue2[0] + localValue2[3]) / 2.0F, -(localValue3[1] + localValue2[1] + localValue2[4]) / 2.0F, -(localValue3[2] + localValue2[2] + localValue2[5]) / 2.0F}
         : new float[]{-localValue3[0], -localValue3[1], -localValue3[2]};
   }

   private float[] internalMethod03976(SwingTransformSettings localValue1) {
      return new float[]{
         localValue1.internalMethod01003().internalMethod08576(),
         localValue1.internalMethod01734().internalMethod08576(),
         localValue1.internalMethod08438().internalMethod08576(),
         localValue1.internalMethod08568().internalMethod08576(),
         localValue1.internalMethod07873().internalMethod08576(),
         localValue1.internalMethod07987().internalMethod08576(),
         localValue1.internalMethod09766().internalMethod08576(),
         localValue1.internalMethod09853().internalMethod08576(),
         localValue1.internalMethod09483().internalMethod08576()
      };
   }

   private void internalMethod06140() {
      float[] localValue1 = this.internalMethod07094(this.internalMethod04482());
      this.internalField1721 = localValue1[0];
      this.internalField1722 = localValue1[1];
      this.internalField1720 = localValue1[2];
      this.internalField1718 = this.internalField1719 = 0.0F;
      this.internalField0276 = this.internalField0616 != null;
      this.internalField1099 = false;
   }

   private void internalMethod03879(SwingEditorPanel.InternalType0001 localValue1) {
      float[] localValue2 = this.internalMethod08180();
      this.internalField0143 = localValue1;
      this.internalMethod07847();
      this.internalField1238 = localValue2;
   }

   private void internalMethod07847() {
      this.internalField1240[0] = this.internalField1721;
      this.internalField1240[1] = this.internalField1722;
      this.internalField1240[2] = this.internalField1720;
      float[] localValue1 = this.internalMethod07094(this.internalMethod04482());
      this.internalField1239[0] = localValue1[0];
      this.internalField1239[1] = localValue1[1];
      this.internalField1239[2] = localValue1[2];
      this.internalField1237[0] = this.internalField1718;
      this.internalField1237[1] = this.internalField1719;
      this.internalField1238 = null;
      this.internalField0276 = true;
      this.internalField1099 = true;
      this.internalField0809.internalMethod07060(0.0F);
   }

   private void internalMethod07848() {
      float[] localValue1 = this.internalMethod03976(this.internalMethod04482());
      boolean localValue2 = this.internalField1574 != null && !Arrays.equals(localValue1, this.internalField1574);
      this.internalField1574 = localValue1;
      if (localValue2 && !this.internalField1102 && !this.internalField1099 && this.internalField0143 != SwingEditorPanel.InternalType0001.internalField1025) {
         this.internalMethod07847();
      }
   }

   private float[] internalMethod08180() {
      if (this.internalField0143 == SwingEditorPanel.InternalType0001.internalField1025) {
         SwingTransform localValue5 = RockstarClient.getInstance().internalMethod00061().internalMethod01686(this.internalMethod07856());
         return new float[]{
            localValue5.internalMethod01052(),
            localValue5.internalMethod01057(),
            localValue5.internalMethod08670(),
            localValue5.internalMethod08672(),
            localValue5.internalMethod08693(),
            localValue5.internalMethod08695(),
            localValue5.internalMethod09416(),
            localValue5.internalMethod09417(),
            localValue5.internalMethod09426()
         };
      } else {
         float[] localValue1 = this.internalMethod03976(this.internalMethod04482());
         if (this.internalField1099 && this.internalField1238 != null) {
            float localValue2 = this.internalField0809.internalMethod02881();
            float[] localValue3 = new float[9];

            for (int localValue4 = 0; localValue4 < 9; localValue4++) {
               localValue3[localValue4] = this.internalField1238[localValue4] + (localValue1[localValue4] - this.internalField1238[localValue4]) * localValue2;
            }

            return localValue3;
         } else {
            return localValue1;
         }
      }
   }

   private float internalMethod07856() {
      float localValue1 = RockstarClient.getInstance().internalMethod00061().internalMethod03324().internalMethod08576();
      long localValue2 = (long)Math.max(100.0F, 300.0F * localValue1);
      long localValue4 = 500L;
      long localValue6 = System.currentTimeMillis() % (localValue2 + localValue4);
      return localValue6 >= localValue2 ? 0.0F : (float)localValue6 / (float)localValue2;
   }

   private void internalMethod08897(UiRenderContext localValue1) {
      localValue1.drawShadow(
         this.internalField0205,
         this.internalField0206,
         this.internalField1048,
         this.internalField1047,
         25.0F,
         CornerRadii.internalMethod03908(11.0F),
         ThemeColors.internalField1309.mulAlpha(0.5F)
      );
      localValue1.drawBlurredRect(
         this.internalField0205,
         this.internalField0206,
         this.internalField1048,
         this.internalField1047,
         5.0F,
         3.0F,
         CornerRadii.internalMethod03908(11.0F),
         ThemeColors.internalField1312
      );
      localValue1.drawSquircle(
         this.internalField0205,
         this.internalField0206,
         this.internalField1048,
         this.internalField1047,
         3.0F,
         CornerRadii.internalMethod03908(11.0F),
         ThemeColors.internalField1612
      );
      localValue1.drawSquircleBorder(
         this.internalField0205,
         this.internalField0206,
         this.internalField1048,
         this.internalField1047,
         0.5F,
         3.0F,
         CornerRadii.internalMethod03908(11.0F),
         ThemeColors.internalField1616
      );
   }

   private void internalMethod08168(UiRenderContext localValue1) {
      localValue1.drawText(
         Fonts.internalField1157.internalMethod01432(7.0F),
         LanguageManager.internalMethod07214("swing.editor"),
         this.internalField0205 + 9.0F,
         this.internalField0206 + 8.5F,
         ThemeColors.internalMethod08459()
      );
      String[] localValue2 = new String[]{
         LanguageManager.internalMethod07214("swing.start"),
         LanguageManager.internalMethod07214("swing.end"),
         LanguageManager.internalMethod07214("swing.preview")
      };
      SwingEditorPanel.InternalType0001[] localValue3 = new SwingEditorPanel.InternalType0001[]{
         SwingEditorPanel.InternalType0001.internalField0143,
         SwingEditorPanel.InternalType0001.internalField0144,
         SwingEditorPanel.InternalType0001.internalField1025
      };
      float[] localValue4 = new float[3];
      float localValue5 = -8.0F;

      for (int localValue6 = 0; localValue6 < 3; localValue6++) {
         localValue4[localValue6] = Fonts.internalField1154.internalMethod05670(localValue2[localValue6], 7.0F);
         localValue5 += localValue4[localValue6] + 8.0F;
      }

      float localValue11 = this.internalField0205 + this.internalField1048 / 2.0F - localValue5 / 2.0F;
      this.internalField1843 = this.internalField0206 + 20.0F;

      for (int localValue7 = 0; localValue7 < 3; localValue7++) {
         boolean localValue8 = this.internalField0143 == localValue3[localValue7];
         boolean localValue9 = UiUtils.internalMethod05786(
            localValue11 - 4.0F, this.internalField1843, localValue4[localValue7] + 8.0F, 11.0, localValue1.internalMethod05259(), localValue1.internalMethod05261()
         );
         this.internalField0556[localValue7].internalMethod07062(localValue9);
         this.internalField0557[localValue7].internalMethod07062(localValue8);
         ColorRGBA localValue10 = ThemeColors.internalMethod08459()
            .mulAlpha(0.45F + 0.35F * this.internalField0556[localValue7].internalMethod02881())
            .mix(ThemeColors.internalMethod02531(), this.internalField0557[localValue7].internalMethod02881());
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(7.0F), localValue2[localValue7], localValue11, this.internalField1843, localValue10);
         if (localValue9) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
         }

         this.internalField1578[localValue7] = localValue11 - 4.0F;
         this.internalField1579[localValue7] = localValue4[localValue7] + 8.0F;
         localValue11 += localValue4[localValue7] + 8.0F;
      }
   }

   private void internalMethod09731(UiRenderContext localValue1) {
      float localValue2 = 6.0F;
      float localValue3 = 9.0F;
      this.internalField1844 = this.internalField0206 + this.internalField1047 - 28.0F + 2.0F;
      this.internalField1845 = this.internalField0205 + this.internalField1048 - 9.0F - 16.0F;
      boolean localValue4 = UiUtils.internalMethod05786(
         this.internalField1845, this.internalField1844, 16.0, 16.0, localValue1.internalMethod05259(), localValue1.internalMethod05261()
      );
      this.internalField1321.internalMethod07062(localValue4);
      ColorRGBA localValue5 = ThemeColors.internalMethod08459().mulAlpha(0.5F + 0.4F * this.internalField1321.internalMethod02881());
      if (IconFontRegistry.internalMethod06859("clean")) {
         localValue1.drawIcon("clean", this.internalField1845 + (16.0F - localValue3) / 2.0F, this.internalField1844 + (16.0F - localValue3) / 2.0F, localValue3, localValue5);
      } else {
         localValue1.drawText(Fonts.internalField1154.internalMethod01432(7.0F), "C", this.internalField1845 + 8.0F - 2.0F, this.internalField1844 + 8.0F - 3.0F, localValue5);
      }

      if (localValue4) {
         CursorManager.internalMethod06882(CursorType.internalField0567);
      }

      if (this.internalField0143 != SwingEditorPanel.InternalType0001.internalField1025) {
         String[] localValue6 = new String[]{"swing/position", "swing/rotation", "swing/anchor"};
         String[] localValue7 = new String[]{"M", "R", "P"};
         SwingEditorPanel.InternalType0502[] localValue8 = new SwingEditorPanel.InternalType0502[]{
            SwingEditorPanel.InternalType0502.internalField0473,
            SwingEditorPanel.InternalType0502.internalField0474,
            SwingEditorPanel.InternalType0502.internalField1168
         };
         float localValue9 = 48.0F + 2.0F * localValue2;
         float localValue10 = this.internalField0205 + this.internalField1048 / 2.0F - localValue9 / 2.0F;

         for (int localValue11 = 0; localValue11 < 3; localValue11++) {
            boolean localValue12 = this.internalField0473 == localValue8[localValue11];
            boolean localValue13 = UiUtils.internalMethod05786(localValue10, this.internalField1844, 16.0, 16.0, localValue1.internalMethod05259(), localValue1.internalMethod05261());
            this.internalField1204[localValue11].internalMethod07062(localValue13);
            this.internalField1203[localValue11].internalMethod07062(localValue12);
            ColorRGBA localValue14 = ThemeColors.internalMethod08459()
               .mulAlpha(0.5F + 0.4F * this.internalField1204[localValue11].internalMethod02881())
               .mix(ThemeColors.internalMethod02531(), this.internalField1203[localValue11].internalMethod02881());
            if (IconFontRegistry.internalMethod06859(localValue6[localValue11])) {
               localValue1.drawIcon(localValue6[localValue11], localValue10 + (16.0F - localValue3) / 2.0F, this.internalField1844 + (16.0F - localValue3) / 2.0F, localValue3, localValue14);
            } else {
               localValue1.drawText(
                  Fonts.internalField1154.internalMethod01432(7.0F), localValue7[localValue11], localValue10 + 8.0F - 2.0F, this.internalField1844 + 8.0F - 3.0F, localValue14
               );
            }

            if (localValue13) {
               CursorManager.internalMethod06882(CursorType.internalField0567);
            }

            this.internalField1580[localValue11] = localValue10;
            this.internalField1805[localValue11] = 16.0F;
            localValue10 += 16.0F + localValue2;
         }
      }
   }

   private int internalMethod02516(double localValue1, double localValue3) {
      int localValue5 = -1;
      float localValue6 = 6.0F;
      if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
         for (int localValue7 = 0; localValue7 < 3; localValue7++) {
            for (Vec2f localValue11 : this.internalField0429[localValue7]) {
               float localValue12 = internalMethod01607(localValue1, localValue3, localValue11.x, localValue11.y);
               if (localValue12 < localValue6) {
                  localValue6 = localValue12;
                  localValue5 = localValue7;
               }
            }
         }
      } else {
         for (int localValue13 = 0; localValue13 < 3; localValue13++) {
            float localValue14 = internalMethod02124((float)localValue1, (float)localValue3, this.internalField0280, this.internalField0723[localValue13]);
            if (localValue14 < localValue6) {
               localValue6 = localValue14;
               localValue5 = localValue13;
            }
         }
      }

      return localValue5;
   }

   private boolean internalMethod04245(double localValue1, double localValue3) {
      return UiUtils.internalMethod05785(this.internalField0205, this.internalField0206 + 32.0F, this.internalField1048, this.internalField1047 - 32.0F - 28.0F, localValue1, localValue3);
   }

   private boolean internalMethod06138() {
      return GLFW.glfwGetKey(internalField0149.getWindow().getHandle(), 32) == 1;
   }

   @Override
   public void internalMethod01643(double localValue1, double localValue3, MouseButton localValue5) {
      if ((localValue5 == MouseButton.internalField0990 || localValue5 == MouseButton.internalField0102 && this.internalMethod06138()) && this.internalMethod04245(localValue1, localValue3)) {
         this.internalField0277 = true;
      } else if (localValue5 == MouseButton.internalField0102) {
         SwingEditorPanel.InternalType0001[] localValue6 = new SwingEditorPanel.InternalType0001[]{
            SwingEditorPanel.InternalType0001.internalField0143,
            SwingEditorPanel.InternalType0001.internalField0144,
            SwingEditorPanel.InternalType0001.internalField1025
         };

         for (int localValue7 = 0; localValue7 < 3; localValue7++) {
            if (UiUtils.internalMethod05785(this.internalField1578[localValue7], this.internalField1843, this.internalField1579[localValue7], 11.0, localValue1, localValue3)) {
               if (localValue6[localValue7] != this.internalField0143) {
                  if (localValue6[localValue7] == SwingEditorPanel.InternalType0001.internalField1025) {
                     this.internalField0143 = SwingEditorPanel.InternalType0001.internalField1025;
                     this.internalMethod07847();
                  } else {
                     this.internalMethod03879(localValue6[localValue7]);
                  }
               }

               return;
            }
         }

         if (UiUtils.internalMethod05785(this.internalField1845, this.internalField1844, 16.0, 16.0, localValue1, localValue3)) {
            this.internalMethod07858();
         } else {
            if (this.internalField0143 != SwingEditorPanel.InternalType0001.internalField1025) {
               SwingEditorPanel.InternalType0502[] localValue10 = new SwingEditorPanel.InternalType0502[]{
                  SwingEditorPanel.InternalType0502.internalField0473,
                  SwingEditorPanel.InternalType0502.internalField0474,
                  SwingEditorPanel.InternalType0502.internalField1168
               };

               for (int localValue8 = 0; localValue8 < 3; localValue8++) {
                  if (UiUtils.internalMethod05785(this.internalField1580[localValue8], this.internalField1844, this.internalField1805[localValue8], 16.0, localValue1, localValue3)) {
                     this.internalMethod00481(localValue10[localValue8]);
                     return;
                  }
               }
            }

            if (this.internalMethod04245(localValue1, localValue3)) {
               int localValue11 = this.internalField0143 == SwingEditorPanel.InternalType0001.internalField1025 ? -1 : this.internalMethod02516(localValue1, localValue3);
               if (localValue11 >= 0) {
                  this.internalField1102 = true;
                  this.internalField1099 = false;
                  this.internalField1056 = localValue11;
                  this.internalField0194 = localValue1;
                  this.internalField0193 = localValue3;
                  this.internalField1724 = this.internalMethod00710(localValue11);
                  SwingTransformSettings localValue12 = this.internalMethod04482();
                  this.internalField1577[0] = localValue12.internalMethod01003().internalMethod08576();
                  this.internalField1577[1] = localValue12.internalMethod01734().internalMethod08576();
                  this.internalField1577[2] = localValue12.internalMethod08438().internalMethod08576();
                  this.internalField1575[0] = localValue12.internalMethod08568().internalMethod08576();
                  this.internalField1575[1] = localValue12.internalMethod07873().internalMethod08576();
                  this.internalField1575[2] = localValue12.internalMethod07987().internalMethod08576();
                  if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
                     this.internalField0281 = this.internalField0280;
                     this.internalField1732 = (float)Math.atan2(localValue3 - this.internalField0281.y, localValue1 - this.internalField0281.x);
                     float[] localValue9 = this.internalMethod02024(this.internalMethod08180());
                     this.internalField1573[0] = this.internalField1721 + localValue9[0];
                     this.internalField1573[1] = this.internalField1722 + localValue9[1];
                     this.internalField1573[2] = this.internalField1720 + localValue9[2];
                  }
               } else {
                  this.internalField1100 = true;
               }
            }
         }
      }
   }

   @Override
   public void internalMethod02863(double localValue1, double localValue3, MouseButton localValue5) {
      this.internalField1102 = false;
      this.internalField1100 = false;
      this.internalField0277 = false;
      this.internalField1056 = -1;
   }

   public void internalMethod00368(double localValue1, double localValue3, MouseButton localValue5, double localValue6, double localValue8) {
      if (this.internalField0277) {
         this.internalField1718 += (float)localValue6;
         this.internalField1719 += (float)localValue8;
      } else if (this.internalField1100) {
         this.internalField1727 += (float)localValue6 * 0.6F;
         this.internalField1728 = MathHelper.clamp(this.internalField1728 + (float)localValue8 * 0.6F, -89.0F, 89.0F);
      } else if (this.internalField1102 && this.internalField1056 >= 0) {
         if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0474) {
            float localValue10 = (float)Math.atan2(localValue3 - this.internalField0281.y, localValue1 - this.internalField0281.x);
            float localValue11 = (float)Math.toDegrees(internalMethod00709(localValue10 - this.internalField1732));
            this.internalMethod04246(this.internalField1056, this.internalField1724 + localValue11);
         } else {
            Vec2f localValue14 = new Vec2f(
               this.internalField0723[this.internalField1056].x - this.internalField0280.x, this.internalField0723[this.internalField1056].y - this.internalField0280.y
            );
            float localValue15 = (float)Math.sqrt(localValue14.x * localValue14.x + localValue14.y * localValue14.y);
            if (localValue15 < 0.001F) {
               return;
            }

            float localValue12 = (float)((localValue1 - this.internalField0194) * localValue14.x + (localValue3 - this.internalField0193) * localValue14.y) / localValue15;
            float localValue13 = localValue12 * (0.45F / localValue15);
            if (this.internalField0473 == SwingEditorPanel.InternalType0502.internalField0473) {
               this.internalMethod04246(this.internalField1056, this.internalField1724 + localValue13);
            } else {
               this.internalMethod02518(this.internalField1056, localValue13);
            }
         }

         this.internalMethod07857();
      }
   }

   private void internalMethod02518(int localValue1, float localValue2) {
      SwingTransformSettings localValue3 = this.internalMethod04482();
      Quaternionf localValue4 = new Quaternionf()
         .rotationXYZ(
            (float)Math.toRadians(localValue3.internalMethod09766().internalMethod08576()),
            (float)Math.toRadians(localValue3.internalMethod09853().internalMethod08576()),
            (float)Math.toRadians(localValue3.internalMethod09483().internalMethod08576())
         )
         .conjugate();
      Vector3f localValue5 = new Vector3f();
      if (localValue1 == 0) {
         localValue5.x = localValue2;
      } else if (localValue1 == 1) {
         localValue5.y = localValue2;
      } else {
         localValue5.z = localValue2;
      }

      Vector3f localValue6 = localValue4.transform(new Vector3f(localValue5));
      localValue3.internalMethod01003().internalMethod04736(this.internalField1577[0] + localValue6.x);
      localValue3.internalMethod01734().internalMethod04736(this.internalField1577[1] + localValue6.y);
      localValue3.internalMethod08438().internalMethod04736(this.internalField1577[2] + localValue6.z);
      localValue3.internalMethod08568().internalMethod04736(this.internalField1575[0] + (localValue5.x - localValue6.x));
      localValue3.internalMethod07873().internalMethod04736(this.internalField1575[1] + (localValue5.y - localValue6.y));
      localValue3.internalMethod07987().internalMethod04736(this.internalField1575[2] + (localValue5.z - localValue6.z));
   }

   private void internalMethod07857() {
      if (rockstar.client.compat.InputCompat.hasShiftDown() || rockstar.client.compat.InputCompat.hasControlDown()) {
         SwingTransformSettings localValue1 = this.internalMethod04482();
         SwingTransformSettings localValue2 = this.internalField0143 == SwingEditorPanel.InternalType0001.internalField0144
            ? RockstarClient.getInstance().internalMethod00061().internalMethod04274()
            : RockstarClient.getInstance().internalMethod00061().internalMethod05639();
         switch (this.internalField0473) {
            case internalField0473:
               localValue2.internalMethod08568().internalMethod04736(localValue1.internalMethod08568().internalMethod08576());
               localValue2.internalMethod07873().internalMethod04736(localValue1.internalMethod07873().internalMethod08576());
               localValue2.internalMethod07987().internalMethod04736(localValue1.internalMethod07987().internalMethod08576());
               break;
            case internalField0474:
               localValue2.internalMethod09766().internalMethod04736(localValue1.internalMethod09766().internalMethod08576());
               localValue2.internalMethod09853().internalMethod04736(localValue1.internalMethod09853().internalMethod08576());
               localValue2.internalMethod09483().internalMethod04736(localValue1.internalMethod09483().internalMethod08576());
               break;
            case internalField1168:
               localValue2.internalMethod01003().internalMethod04736(localValue1.internalMethod01003().internalMethod08576());
               localValue2.internalMethod01734().internalMethod04736(localValue1.internalMethod01734().internalMethod08576());
               localValue2.internalMethod08438().internalMethod04736(localValue1.internalMethod08438().internalMethod08576());
               localValue2.internalMethod08568().internalMethod04736(localValue1.internalMethod08568().internalMethod08576());
               localValue2.internalMethod07873().internalMethod04736(localValue1.internalMethod07873().internalMethod08576());
               localValue2.internalMethod07987().internalMethod04736(localValue1.internalMethod07987().internalMethod08576());
         }
      }
   }

   @Override
   public void internalMethod02890(double localValue1, double localValue3, double localValue5, double localValue7) {
      if (this.internalMethod04245(localValue1, localValue3)) {
         float localValue9 = MathHelper.clamp(this.internalField1717 * (1.0F + (float)localValue7 * 0.1F), 30.0F, 280.0F);
         float localValue10 = this.internalField1717 == 0.0F ? 1.0F : localValue9 / this.internalField1717;
         float localValue11 = this.internalField1730 + this.internalField1725 / 2.0F + this.internalField1718;
         float localValue12 = this.internalField1729 + this.internalField1726 / 2.0F + this.internalField1719;
         this.internalField1718 += ((float)localValue1 - localValue11) * (1.0F - localValue10);
         this.internalField1719 += ((float)localValue3 - localValue12) * (1.0F - localValue10);
         this.internalField1717 = localValue9;
      }
   }

   @Override
   public void internalMethod05727(int localValue1, int localValue2, int localValue3) {
      if (rockstar.client.compat.InputCompat.hasControlDown()) {
         if (localValue1 == 90) {
            this.internalMethod09411();
         } else if (localValue1 == 89) {
            this.internalMethod09422();
         }
      } else {
         switch (localValue1) {
            case 70:
               this.internalMethod06140();
               break;
            case 71:
               this.internalMethod00481(SwingEditorPanel.InternalType0502.internalField0473);
               break;
            case 82:
               this.internalMethod00481(SwingEditorPanel.InternalType0502.internalField0474);
               break;
            case 84:
               this.internalMethod00481(SwingEditorPanel.InternalType0502.internalField1168);
         }
      }
   }

   private void internalMethod00481(SwingEditorPanel.InternalType0502 localValue1) {
      if (this.internalField0473 != localValue1) {
         this.internalField0473 = localValue1;
         this.internalMethod07847();
      }
   }

   private SliderSetting internalMethod03691(int localValue1) {
      SwingTransformSettings localValue2 = this.internalMethod04482();

      return switch (this.internalField0473) {
         case internalField0473 -> localValue1 == 0 ? localValue2.internalMethod08568() : (localValue1 == 1 ? localValue2.internalMethod07873() : localValue2.internalMethod07987());
         case internalField0474 -> localValue1 == 0 ? localValue2.internalMethod09766() : (localValue1 == 1 ? localValue2.internalMethod09853() : localValue2.internalMethod09483());
         case internalField1168 -> localValue1 == 0 ? localValue2.internalMethod01003() : (localValue1 == 1 ? localValue2.internalMethod01734() : localValue2.internalMethod08438());
      };
   }

   private float internalMethod00710(int localValue1) {
      return this.internalMethod03691(localValue1).internalMethod08576();
   }

   private void internalMethod04246(int localValue1, float localValue2) {
      this.internalMethod03691(localValue1).internalMethod04736(localValue2);
   }

   private void internalMethod07858() {
      this.internalMethod02520(internalField1806);
   }

   private float[] internalMethod08197() {
      SwingAnimationManager localValue1 = RockstarClient.getInstance().internalMethod00061();
      SwingTransformSettings localValue2 = localValue1.internalMethod04274();
      SwingTransformSettings localValue3 = localValue1.internalMethod05639();
      Vec2f localValue4 = localValue1.internalMethod05663().internalMethod03179();
      Vec2f localValue5 = localValue1.internalMethod05663().internalMethod00322();
      return new float[]{
         localValue2.internalMethod01003().internalMethod08576(),
         localValue2.internalMethod01734().internalMethod08576(),
         localValue2.internalMethod08438().internalMethod08576(),
         localValue2.internalMethod08568().internalMethod08576(),
         localValue2.internalMethod07873().internalMethod08576(),
         localValue2.internalMethod07987().internalMethod08576(),
         localValue2.internalMethod09766().internalMethod08576(),
         localValue2.internalMethod09853().internalMethod08576(),
         localValue2.internalMethod09483().internalMethod08576(),
         localValue3.internalMethod01003().internalMethod08576(),
         localValue3.internalMethod01734().internalMethod08576(),
         localValue3.internalMethod08438().internalMethod08576(),
         localValue3.internalMethod08568().internalMethod08576(),
         localValue3.internalMethod07873().internalMethod08576(),
         localValue3.internalMethod07987().internalMethod08576(),
         localValue3.internalMethod09766().internalMethod08576(),
         localValue3.internalMethod09853().internalMethod08576(),
         localValue3.internalMethod09483().internalMethod08576(),
         localValue4.x,
         localValue4.y,
         localValue5.x,
         localValue5.y,
         localValue1.internalMethod05664().internalMethod04496() ? 1.0F : 0.0F,
         localValue1.internalMethod03324().internalMethod08576()
      };
   }

   private void internalMethod02520(float[] localValue1) {
      SwingAnimationManager localValue2 = RockstarClient.getInstance().internalMethod00061();
      SwingTransformSettings localValue3 = localValue2.internalMethod04274();
      SwingTransformSettings localValue4 = localValue2.internalMethod05639();
      localValue3.internalMethod01003().internalMethod04736(localValue1[0]);
      localValue3.internalMethod01734().internalMethod04736(localValue1[1]);
      localValue3.internalMethod08438().internalMethod04736(localValue1[2]);
      localValue3.internalMethod08568().internalMethod04736(localValue1[3]);
      localValue3.internalMethod07873().internalMethod04736(localValue1[4]);
      localValue3.internalMethod07987().internalMethod04736(localValue1[5]);
      localValue3.internalMethod09766().internalMethod04736(localValue1[6]);
      localValue3.internalMethod09853().internalMethod04736(localValue1[7]);
      localValue3.internalMethod09483().internalMethod04736(localValue1[8]);
      localValue4.internalMethod01003().internalMethod04736(localValue1[9]);
      localValue4.internalMethod01734().internalMethod04736(localValue1[10]);
      localValue4.internalMethod08438().internalMethod04736(localValue1[11]);
      localValue4.internalMethod08568().internalMethod04736(localValue1[12]);
      localValue4.internalMethod07873().internalMethod04736(localValue1[13]);
      localValue4.internalMethod07987().internalMethod04736(localValue1[14]);
      localValue4.internalMethod09766().internalMethod04736(localValue1[15]);
      localValue4.internalMethod09853().internalMethod04736(localValue1[16]);
      localValue4.internalMethod09483().internalMethod04736(localValue1[17]);
      localValue2.internalMethod05663().internalMethod07286(new Vec2f(localValue1[18], localValue1[19])).internalMethod01741(new Vec2f(localValue1[20], localValue1[21]));
      localValue2.internalMethod05664().internalMethod04836(localValue1[22] != 0.0F);
      localValue2.internalMethod03324().internalMethod04736(localValue1[23]);
   }

   private void internalMethod09409() {
      float[] localValue1 = this.internalMethod08197();
      if (this.internalField1576 == null) {
         this.internalField1576 = localValue1;
      } else if (!this.internalMethod06141()) {
         if (!Arrays.equals(localValue1, this.internalField1576)) {
            this.internalField0796.push(this.internalField1576);
            this.internalField0797.clear();
            this.internalField1576 = localValue1;
         }
      }
   }

   private boolean internalMethod06141() {
      long localValue1 = internalField0149.getWindow().getHandle();
      return GLFW.glfwGetMouseButton(localValue1, 0) == 1 || GLFW.glfwGetMouseButton(localValue1, 1) == 1 || GLFW.glfwGetMouseButton(localValue1, 2) == 1;
   }

   private void internalMethod09411() {
      if (!this.internalField0796.isEmpty()) {
         this.internalField0797.push(this.internalMethod08197());
         this.internalMethod02520(this.internalField0796.pop());
         this.internalField1576 = this.internalMethod08197();
      }
   }

   private void internalMethod09422() {
      if (!this.internalField0797.isEmpty()) {
         this.internalField0796.push(this.internalMethod08197());
         this.internalMethod02520(this.internalField0797.pop());
         this.internalField1576 = this.internalMethod08197();
      }
   }

   private static float internalMethod01607(double localValue0, double localValue2, float localValue4, float localValue5) {
      double localValue6 = localValue0 - localValue4;
      double localValue8 = localValue2 - localValue5;
      return (float)Math.sqrt(localValue6 * localValue6 + localValue8 * localValue8);
   }

   private static float internalMethod02124(float localValue0, float localValue1, Vec2f localValue2, Vec2f localValue3) {
      float localValue4 = localValue3.x - localValue2.x;
      float localValue5 = localValue3.y - localValue2.y;
      float localValue6 = localValue0 - localValue2.x;
      float localValue7 = localValue1 - localValue2.y;
      float localValue8 = localValue4 * localValue4 + localValue5 * localValue5;
      float localValue9 = localValue8 < 1.0E-5F ? 0.0F : MathHelper.clamp((localValue6 * localValue4 + localValue7 * localValue5) / localValue8, 0.0F, 1.0F);
      float localValue10 = localValue2.x + localValue9 * localValue4;
      float localValue11 = localValue2.y + localValue9 * localValue5;
      float localValue12 = localValue0 - localValue10;
      float localValue13 = localValue1 - localValue11;
      return (float)Math.sqrt(localValue12 * localValue12 + localValue13 * localValue13);
   }

   private static float internalMethod00709(float localValue0) {
      while (localValue0 > Math.PI) {
         localValue0 -= (float) (Math.PI * 2);
      }

      while (localValue0 < -Math.PI) {
         localValue0 += (float) (Math.PI * 2);
      }

      return localValue0;
   }

   static enum InternalType0001 {
      internalField0143,
      internalField0144,
      internalField1025;
   }

   static final class InternalType0501 implements VertexConsumer {
      private float internalField0205 = Float.MAX_VALUE;
      private float internalField0206 = Float.MAX_VALUE;
      private float internalField1048 = Float.MAX_VALUE;
      private float internalField1047 = -Float.MAX_VALUE;
      private float internalField1049 = -Float.MAX_VALUE;
      private float internalField1046 = -Float.MAX_VALUE;

      public VertexConsumer vertex(float x, float y, float z) {
         this.internalField0205 = Math.min(this.internalField0205, x);
         this.internalField0206 = Math.min(this.internalField0206, y);
         this.internalField1048 = Math.min(this.internalField1048, z);
         this.internalField1047 = Math.max(this.internalField1047, x);
         this.internalField1049 = Math.max(this.internalField1049, y);
         this.internalField1046 = Math.max(this.internalField1046, z);
         return this;
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         return this;
      }

      public VertexConsumer color(int argb) {
         return this;
      }

      public VertexConsumer texture(float u, float v) {
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         return this;
      }

      public VertexConsumer light(int u, int v) {
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         return this;
      }

      public VertexConsumer lineWidth(float width) {
         return this;
      }

      float[] internalMethod01849() {
         return this.internalField0205 > this.internalField1047
            ? null
            : new float[]{
               (this.internalField0205 + this.internalField1047) / 2.0F, (this.internalField0206 + this.internalField1049) / 2.0F, (this.internalField1048 + this.internalField1046) / 2.0F
            };
      }
   }

   static enum InternalType0502 {
      internalField0473,
      internalField0474,
      internalField1168;
   }
}
