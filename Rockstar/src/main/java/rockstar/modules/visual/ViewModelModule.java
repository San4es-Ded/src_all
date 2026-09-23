package rockstar.modules.visual;










import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.render.HandRenderEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.window.ChatClickEvent;
import pyrock.events.window.ChatReleaseEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "View Model",
   category = ModuleCategory.VISUALS,
   internalMethod09633 = "modules.descriptions.view_model"
)
public class ViewModelModule extends Module {
   public static boolean internalField0277;
   private static final int internalField0227 = 15728880;
   private static final float internalField0205 = 0.4F;
   static final float[] internalField0615 = new float[16];
   static final float[] internalField0616 = new float[16];
   VectorRangeSetting internalField0671;
   VectorRangeSetting internalField0670;
   SliderSetting internalField0383;
   SliderSetting internalField0382;
   private BooleanSetting internalField0650;
   private ButtonSetting internalField0663;
   private final ViewModelModule.InternalType0102 internalField0480 = new ViewModelModule.InternalType0102();
   private final ViewModelModule.InternalType0102 internalField0481 = new ViewModelModule.InternalType0102();
   private float internalField0206;
   private float internalField1048;
   Arm internalField0112;
   private float internalField1047;
   private float internalField1049;
   private float internalField1046;
   private float internalField1456;
   private boolean internalField0276;
   private final EventListener<HandRenderEvent> internalField0157 = new EventListener<HandRenderEvent>() {
      public void onEvent(HandRenderEvent localValue1) {
         MatrixStack localValue2 = localValue1.getMatrices();
         boolean localValue3 = localValue1.getArm() == Arm.RIGHT;
         float localValue4 = ViewModelModule.this.internalField0383.internalMethod08576() - 1.0F;
         float localValue5 = ViewModelModule.this.internalField0382.internalMethod08576() - 1.0F;
         if (localValue3) {
            localValue2.translate(
               ViewModelModule.this.internalField0671.internalMethod03695() - localValue5,
               -ViewModelModule.this.internalField0671.internalMethod03697() + localValue5 / 2.0F,
               localValue5
            );
         } else {
            localValue2.translate(
               ViewModelModule.this.internalField0670.internalMethod03695() + localValue4,
               -ViewModelModule.this.internalField0670.internalMethod03697() + localValue4 / 2.0F,
               localValue4
            );
         }

         if (ViewModelModule.this.internalMethod09270()) {
            ViewModelModule.this.internalMethod04769(localValue1);
         } else {
            ViewModelModule.this.internalField0112 = null;
         }
      }

      @Override
      public int internalMethod07175() {
         return 1;
      }
   };
   private final EventListener<ChatRenderEvent> internalField0158 = localValue1 -> {
      if (!this.internalMethod09270()) {
         this.internalField0112 = null;
      } else {
         Vector2f localValue2 = UiUtils.internalMethod03634();
         if (this.internalField0112 != null) {
         this.internalMethod01048(localValue2.x(), localValue2.y());
         }

         if (this.internalMethod01064() != null) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
         }
      }
   };
   private final EventListener<PreHudRenderEvent> internalField1028 = localValue1 -> {
      ViewModelModule.InternalType0102 localValue2 = this.internalMethod09270() ? this.internalMethod01064() : null;
      this.internalMethod07010(this.internalField0480, localValue2 == this.internalField0480);
      this.internalMethod07010(this.internalField0481, localValue2 == this.internalField0481);
   };
   private final EventListener<ChatClickEvent> internalField1029 = localValue1 -> {
      if (this.internalMethod09270() && localValue1.getButton() == 0 && !this.internalMethod01049(localValue1.getX(), localValue1.getY())) {
         ViewModelModule.InternalType0102 localValue2 = this.internalMethod04440(localValue1.getX(), localValue1.getY());
         if (localValue2 != null) {
            this.internalField0112 = localValue2 == this.internalField0480 ? Arm.RIGHT : Arm.LEFT;
            VectorRangeSetting localValue3 = this.internalMethod02601(this.internalField0112);
            this.internalField1047 = localValue1.getX();
            this.internalField1049 = localValue1.getY();
            this.internalField1046 = localValue3.internalMethod03695();
            this.internalField1456 = localValue3.internalMethod03697();
         }
      }
   };
   private final EventListener<ChatReleaseEvent> internalField1030 = localValue1 -> this.internalField0112 = null;

   public ViewModelModule() {
      this.internalMethod09267();
   }

   private void internalMethod09267() {
      this.internalField0670 = new VectorRangeSetting(this, "modules.settings.view_model.off_translate_x")
         .internalMethod08471(-2.0F)
         .internalMethod07903(2.0F)
         .internalMethod08506(-2.0F)
         .internalMethod07929(2.0F)
         .internalMethod07235(0.0F, 0.0F);
      this.internalField0671 = new VectorRangeSetting(this, "modules.settings.view_model.main_translate_x")
         .internalMethod08471(-2.0F)
         .internalMethod07903(2.0F)
         .internalMethod08506(-2.0F)
         .internalMethod07929(2.0F)
         .internalMethod07235(0.0F, 0.0F);
      this.internalField0383 = new SliderSetting(this, "modules.settings.view_model.size_left")
         .internalMethod05900(0.1F)
         .internalMethod02732(1.5F)
         .internalMethod08673(0.025F)
         .internalMethod08074(1.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.view_model.size_right")
         .internalMethod05900(0.1F)
         .internalMethod02732(1.5F)
         .internalMethod08673(0.025F)
         .internalMethod08074(1.0F);
      this.internalField0650 = new BooleanSetting(this, "modules.settings.view_model.chat_edit").internalMethod04836(true);
      this.internalField0663 = new ButtonSetting(this, "modules.settings.view_model.reset").internalMethod07149(this::internalMethod09269);
   }

   private void internalMethod00919(String localValue1, Exception localValue2) {
      if (!this.internalField0276) {
         this.internalField0276 = true;
         RockstarClient.internalField0572.error("[viewmodel] {}", localValue1, localValue2);
      }
   }

   private void internalMethod09269() {
      try (SettingHistoryManager.InternalType0070 localValue1 = SettingHistoryManager.internalMethod07567()) {
         this.internalField0671.internalMethod03891(0.0F, 0.0F);
         this.internalField0670.internalMethod03891(0.0F, 0.0F);
         this.internalField0382.internalMethod04736(1.0F);
         this.internalField0383.internalMethod04736(1.0F);
      }

      this.internalField0112 = null;
   }

   void internalMethod04769(HandRenderEvent localValue1) {
      Matrix4f localValue2 = this.internalMethod04807().mul(localValue1.getMatrices().peek().getPositionMatrix());
      float localValue3 = -0.72F;
      Vector2f localValue4 = this.internalMethod05125(localValue2, 0.0F, 0.0F, localValue3);
      Vector2f localValue5 = this.internalMethod05125(localValue2, 1.0F, 0.0F, localValue3);
      Vector2f localValue6 = this.internalMethod05125(localValue2, 0.0F, 1.0F, localValue3);
      if (localValue4 != null && localValue5 != null && localValue6 != null) {
      this.internalField0206 = Math.abs(localValue5.x() - localValue4.x());
      this.internalField1048 = Math.abs(localValue6.y() - localValue4.y());
      }
   }

   private Matrix4f internalMethod04807() {
      return new Matrix4f(RenderSystem.getProjectionMatrix()).mul(RenderSystem.getModelViewMatrix());
   }

   private Vector2f internalMethod05125(Matrix4f localValue1, float localValue2, float localValue3, float localValue4) {
      Vector4f localValue5 = new Vector4f(localValue2, localValue3, localValue4, 1.0F);
      localValue1.transform(localValue5);
      return localValue5.w <= 1.0E-4F
         ? null
         : new Vector2f(
            (localValue5.x / localValue5.w * 0.5F + 0.5F) * internalField0389.internalMethod03585(), (0.5F - localValue5.y / localValue5.w * 0.5F) * internalField0389.internalMethod03589()
         );
   }

   public void internalMethod04205(
      LivingEntity localValue2,
      ItemStack localValue3,
      ItemDisplayContext localValue4,
      boolean localValue5,
      MatrixStack localValue6,
      World localValue7,
      int localValue8,
      int localValue9,
      int localValue10
   ) {
      if (this.internalMethod09270() && localValue3 != null && !localValue3.isEmpty()) {
         if (localValue4 == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || localValue4 == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            ViewModelModule.InternalType0102 localValue11 = localValue4 == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ? this.internalField0480 : this.internalField0481;
            MatrixStack localValue12 = new MatrixStack();
            localValue12.multiplyPositionMatrix(localValue6.peek().getPositionMatrix());
            localValue11.internalMethod04782(this.internalMethod04807());

            try {
               LegacyRenderCompat.renderItem(localValue2, localValue3, localValue4, localValue5, localValue12, localValue11, localValue7, localValue8, localValue9, localValue10);
            } catch (Exception localValue14) {
               this.internalMethod00919("shape probe failed for " + localValue3.getItem(), localValue14);
            }

            localValue11.internalMethod00521();
            this.internalMethod00604(localValue11, localValue9x -> {
               MatrixStack localValue10x = new MatrixStack();
               localValue10x.multiplyPositionMatrix(localValue6.peek().getPositionMatrix());
               LegacyRenderCompat.renderItem(localValue2, localValue3, localValue4, localValue5, localValue10x, localValue9x, localValue7, 15728880, localValue9, localValue10);
            });
         }
      }
   }

   public void internalMethod04693(Arm localValue1, MatrixStack localValue2) {
      if (this.internalMethod09270()) {
         ClientPlayerEntity localValue3 = internalField0149.player;
         if (localValue3 != null) {
            if (internalField0149.getEntityRenderDispatcher().getRenderer(localValue3) instanceof PlayerEntityRenderer localValue4) {
               ViewModelModule.InternalType0102 localValue11 = localValue1 == Arm.RIGHT ? this.internalField0480 : this.internalField0481;
               MatrixStack localValue6 = new MatrixStack();
               localValue6.multiplyPositionMatrix(localValue2.peek().getPositionMatrix());
               Identifier localValue7 = localValue3.getSkin().body().texturePath();
               boolean localValue8 = localValue3.isModelPartVisible(localValue1 == Arm.LEFT ? PlayerModelPart.LEFT_SLEEVE : PlayerModelPart.RIGHT_SLEEVE);
               localValue11.internalMethod04782(this.internalMethod04807());

               try {
                  this.internalMethod01195(localValue4, localValue1, localValue6, localValue11, localValue7, localValue8);
               } catch (Exception localValue10) {
               }

               localValue11.internalMethod00521();
               this.internalMethod00604(localValue11, localValue6x -> {
                  MatrixStack localValue7x = new MatrixStack();
                  localValue7x.multiplyPositionMatrix(localValue2.peek().getPositionMatrix());
                  this.internalMethod01195(localValue4, localValue1, localValue7x, localValue6x, localValue7, localValue8);
               });
            }
         }
      }
   }

   private void internalMethod01195(PlayerEntityRenderer localValue1, Arm localValue2, MatrixStack localValue3, VertexConsumerProvider localValue4, Identifier localValue5, boolean localValue6) {
      if (localValue2 == Arm.LEFT) {
         LegacyRenderCompat.renderArm(localValue1, true, localValue3, localValue4, 15728880, localValue5, localValue6);
      } else {
         LegacyRenderCompat.renderArm(localValue1, false, localValue3, localValue4, 15728880, localValue5, localValue6);
      }
   }

   private void internalMethod00604(ViewModelModule.InternalType0102 localValue1, Consumer<Immediate> localValue2) {
      Immediate localValue3 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
      localValue3.draw();
      internalField0277 = true;
      localValue1.internalField0769.internalMethod02227(true);

      try {
         localValue2.accept(localValue3);
         localValue3.draw();
         localValue1.internalField0277 = true;
      } catch (Exception localValue5) {
         this.internalMethod00919("capture failed", localValue5);
      }

      localValue1.internalField0769.internalMethod03248();
      internalField0277 = false;
   }

   private ViewModelModule.InternalType0102 internalMethod01064() {
      if (this.internalField0112 != null) {
         return this.internalMethod05885(this.internalField0112);
      } else {
         Vector2f localValue1 = UiUtils.internalMethod03634();
         return this.internalMethod04440(localValue1.x(), localValue1.y());
      }
   }

   public boolean internalMethod05071(float localValue1, float localValue2, float localValue3) {
      if (this.internalMethod09270() && localValue3 != 0.0F) {
         Arm localValue4 = this.internalField0112;
         if (localValue4 == null) {
            ViewModelModule.InternalType0102 localValue5 = this.internalMethod04440(localValue1, localValue2);
            if (localValue5 == null || this.internalMethod01049(localValue1, localValue2)) {
               return false;
            }

            localValue4 = localValue5 == this.internalField0480 ? Arm.RIGHT : Arm.LEFT;
         }

         SliderSetting localValue6 = this.internalMethod00275(localValue4);
         localValue6.internalMethod04736(localValue6.internalMethod08576() + localValue3 * localValue6.internalMethod08575() * 2.0F);
         return true;
      } else {
         return false;
      }
   }

   private void internalMethod01048(float localValue1, float localValue2) {
      if (!(this.internalField0206 <= 0.001F) && !(this.internalField1048 <= 0.001F)) {
         this.internalMethod02601(this.internalField0112)
            .internalMethod03891(this.internalField1046 + (localValue1 - this.internalField1047) / this.internalField0206, this.internalField1456 + (localValue2 - this.internalField1049) / this.internalField1048);
      }
   }

   private void internalMethod07010(ViewModelModule.InternalType0102 localValue1, boolean localValue2) {
      float localValue3 = localValue1.internalField0808.internalMethod07059(localValue2 && localValue1.internalMethod00522() ? 1.0F : 0.0F);
      if (!(localValue3 <= 0.01F) && localValue1.internalField0277) {
         UiBatchRenderer.internalMethod02576();
         boolean localValue4 = GL11.glIsEnabled(2929);
         this.internalMethod02564(localValue1.internalField0769);
         RenderSystem.setShaderTexture(0, localValue1.internalField0769.getColorAttachmentView());
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.4F * localValue3);
         RenderPipeline.internalMethod01737(0.0F, 0.0F, internalField0389.internalMethod03585(), internalField0389.internalMethod03589());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         if (localValue4) {
            RenderSystem.enableDepthTest();
         }
      }
   }

   private void internalMethod02564(ManagedFramebuffer localValue1) {
      localValue1.beginWrite(true);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(772, 0, 0, 1);
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      float localValue2 = internalField0389.internalMethod03585();
      float localValue3 = internalField0389.internalMethod03589();
      int localValue4 = ColorRGBA.WHITE.getRGB();
      BufferBuilder localValue5 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      localValue5.vertex(0.0F, 0.0F, 0.0F).color(localValue4);
      localValue5.vertex(0.0F, localValue3, 0.0F).color(localValue4);
      localValue5.vertex(localValue2, localValue3, 0.0F).color(localValue4);
      localValue5.vertex(localValue2, 0.0F, 0.0F).color(localValue4);
      BufferRenderer.drawWithGlobalProgram(localValue5.end());
      RenderSystem.defaultBlendFunc();
      rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
   }

   private ViewModelModule.InternalType0102 internalMethod04440(double localValue1, double localValue3) {
      boolean localValue5 = this.internalField0480.internalMethod02710((float)localValue1, (float)localValue3);
      boolean localValue6 = this.internalField0481.internalMethod02710((float)localValue1, (float)localValue3);
      if (localValue5 && localValue6) {
         return this.internalField0480.internalMethod02709(localValue1, localValue3) <= this.internalField0481.internalMethod02709(localValue1, localValue3)
            ? this.internalField0480
            : this.internalField0481;
      } else if (localValue5) {
         return this.internalField0480;
      } else {
         return localValue6 ? this.internalField0481 : null;
      }
   }

   private boolean internalMethod01049(float localValue1, float localValue2) {
      if (localValue2 > internalField0389.internalMethod03589() - 16.0F) {
         return true;
      } else {
         HudManager localValue3 = RockstarClient.getInstance().internalMethod01271();
         if (localValue3 == null) {
            return false;
         } else {
            for (HudContextMenu localValue5 : localValue3.internalMethod09283()) {
               if (localValue5.internalMethod08805() && localValue5.internalMethod04933(localValue1, localValue2)) {
                  return true;
               }
            }

            for (AbstractHudWidget localValue7 : localValue3.internalMethod09520()) {
               if (localValue7.isDragging() || localValue7.isShowing() && localValue7.isHovered(localValue1, localValue2)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public boolean internalMethod09268() {
      return this.isEnabled() && this.internalField0650.internalMethod04496() && internalField0149.currentScreen instanceof ChatScreen;
   }

   boolean internalMethod09270() {
      return this.internalMethod09268();
   }

   private ViewModelModule.InternalType0102 internalMethod05885(Arm localValue1) {
      return localValue1 == Arm.RIGHT ? this.internalField0480 : this.internalField0481;
   }

   private VectorRangeSetting internalMethod02601(Arm localValue1) {
      return localValue1 == Arm.RIGHT ? this.internalField0671 : this.internalField0670;
   }

   private SliderSetting internalMethod00275(Arm localValue1) {
      return localValue1 == Arm.RIGHT ? this.internalField0382 : this.internalField0383;
   }

   static {
      for (int localValue0 = 0; localValue0 < 16; localValue0++) {
         double localValue1 = (Math.PI * 2) * localValue0 / 16.0;
         internalField0615[localValue0] = (float)Math.cos(localValue1);
         internalField0616[localValue0] = (float)Math.sin(localValue1);
      }
   }

   static final class InternalType0102 implements VertexConsumerProvider {
      private static final float internalField0205 = 3.0F;
      final AnimatedValue internalField0808 = new AnimatedValue(220L, 0.0F, Easing.internalField1626);
      private final float[] internalField0615 = new float[16];
      final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
      private final Map<RenderLayer, ViewModelModule.InternalType0103> internalField0543 = new IdentityHashMap<>();
      private Matrix4f internalField0788;
      private float internalField0206;
      private float internalField1048;
      private float internalField1047;
      private float internalField1049;
      private int internalField0227;
      private long internalField0229;
      boolean internalField0277;

      void internalMethod04782(Matrix4f localValue1) {
         this.internalField0788 = localValue1;
         this.internalField0227 = 0;
         this.internalField0277 = false;
         this.internalField0206 = Float.MAX_VALUE;
         this.internalField1048 = Float.MAX_VALUE;
         this.internalField1047 = -Float.MAX_VALUE;
         this.internalField1049 = -Float.MAX_VALUE;
         Arrays.fill(this.internalField0615, -Float.MAX_VALUE);
      }

      void internalMethod00521() {
         this.internalField0788 = null;
         if (this.internalField0227 >= 3) {
            this.internalField0229 = System.currentTimeMillis();
         }
      }

      boolean internalMethod00522() {
         return this.internalField0227 >= 3 && System.currentTimeMillis() - this.internalField0229 < 250L;
      }

      boolean internalMethod02710(float localValue1, float localValue2) {
         return this.internalMethod00522() && this.internalMethod01653(localValue1, localValue2, 0.0F);
      }

      private boolean internalMethod01653(float localValue1, float localValue2, float localValue3) {
         for (int localValue4 = 0; localValue4 < 16; localValue4++) {
            if (localValue1 * ViewModelModule.internalField0615[localValue4] + localValue2 * ViewModelModule.internalField0616[localValue4] > this.internalField0615[localValue4] + 3.0F + localValue3) {
               return false;
            }
         }

         return true;
      }

      double internalMethod02709(double localValue1, double localValue3) {
         double localValue5 = localValue1 - this.internalMethod00520();
         double localValue7 = localValue3 - (this.internalField1048 + this.internalField1049) / 2.0F;
         return localValue5 * localValue5 + localValue7 * localValue7;
      }

      private float internalMethod00520() {
         return (this.internalField0206 + this.internalField1047) / 2.0F;
      }

      public VertexConsumer getBuffer(RenderLayer localValue1) {
         return this.internalField0543.computeIfAbsent(localValue1, localValue1x -> new ViewModelModule.InternalType0103(this));
      }

      void internalMethod01652(float localValue1, float localValue2, float localValue3) {
         Matrix4f localValue4 = this.internalField0788;
         if (localValue4 != null) {
            float localValue5 = localValue4.m00() * localValue1 + localValue4.m10() * localValue2 + localValue4.m20() * localValue3 + localValue4.m30();
            float localValue6 = localValue4.m01() * localValue1 + localValue4.m11() * localValue2 + localValue4.m21() * localValue3 + localValue4.m31();
            float localValue7 = localValue4.m03() * localValue1 + localValue4.m13() * localValue2 + localValue4.m23() * localValue3 + localValue4.m33();
            if (!(localValue7 <= 1.0E-4F)) {
               float localValue8 = (localValue5 / localValue7 * 0.5F + 0.5F) * ScreenMetricsAccess.internalField0389.internalMethod03585();
               float localValue9 = (0.5F - localValue6 / localValue7 * 0.5F) * ScreenMetricsAccess.internalField0389.internalMethod03589();

               for (int localValue10 = 0; localValue10 < 16; localValue10++) {
                  float localValue11 = localValue8 * ViewModelModule.internalField0615[localValue10] + localValue9 * ViewModelModule.internalField0616[localValue10];
                  if (localValue11 > this.internalField0615[localValue10]) {
                     this.internalField0615[localValue10] = localValue11;
                  }
               }

               if (localValue8 < this.internalField0206) {
                  this.internalField0206 = localValue8;
               }

               if (localValue8 > this.internalField1047) {
                  this.internalField1047 = localValue8;
               }

               if (localValue9 < this.internalField1048) {
                  this.internalField1048 = localValue9;
               }

               if (localValue9 > this.internalField1049) {
                  this.internalField1049 = localValue9;
               }

               this.internalField0227++;
            }
         }
      }
   }

   static final class InternalType0103 implements VertexConsumer {
      private final ViewModelModule.InternalType0102 internalField0480;

      InternalType0103(ViewModelModule.InternalType0102 localValue1) {
         this.internalField0480 = localValue1;
      }

      public VertexConsumer vertex(float x, float y, float z) {
         this.internalField0480.internalMethod01652(x, y, z);
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

      @Override
      public final String toString() {
         return "InternalType0103[shape=" + this.internalField0480 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0480);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ViewModelModule.InternalType0103 other = (ViewModelModule.InternalType0103) localValue1;
         return java.util.Objects.equals(this.internalField0480, other.internalField0480);
      }

      public ViewModelModule.InternalType0102 internalMethod03972() {
         return this.internalField0480;
      }
   }
}
