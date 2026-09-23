package rockstar.client.esp;







import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import globals.client.Information;
import globals.shared.proto.Packets;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class BoxEspFeature extends EspFeature {
   private static final Identifier internalField0354 = RockstarClient.id("textures/bloom.png");
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (this.internalMethod06968()) {
         MatrixStack localValue2 = localValue1.getMatrices();
         Camera localValue3 = internalField0149.gameRenderer.getCamera();
         float localValue4 = localValue1.getTickDelta();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.depthMask(false);
         RenderSystem.setShaderTexture(0, internalField0354);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder localValue5 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

         for (Entity localValue7 : internalField0149.world.getEntities()) {
            if (this.internalMethod03051(localValue7)) {
               Vec3d localValue8 = ScreenProjectionUtils.internalMethod02822(localValue7, localValue4);
               float localValue9 = (localValue7.getWidth() + localValue7.getHeight()) * 1.2F;
               ColorRGBA localValue10 = this.internalMethod06722(localValue7);
               if (this.internalMethod06467(localValue7, "esp.boxes.mode.fill")) {
                  localValue2.push();
                  HudRenderUtils.internalMethod03474(localValue2, localValue8);
                  localValue2.translate(0.0F, localValue7.getHeight() / 4.0F, 0.0F);
                  localValue2.multiply(localValue3.getRotation());
                  RenderPipeline.internalMethod05007(localValue2, localValue5, -localValue9 / 2.0F, -localValue9 / 2.0F, 0.0, localValue9, localValue9, localValue10.mulAlpha(0.5F));
                  localValue2.pop();
               } else {
                  localValue2.push();
                  HudRenderUtils.internalMethod03474(localValue2, localValue8);
                  localValue2.translate(0.0, localValue7.getHeight() / 3.0, 0.0);
                  localValue2.multiply(localValue3.getRotation());
                  RenderPipeline.internalMethod05007(localValue2, localValue5, -localValue9 / 2.0F, -localValue9 / 2.0F, 0.0, localValue9, localValue9, localValue10.mulAlpha(0.5F));
                  localValue2.pop();
                  localValue2.push();
                  HudRenderUtils.internalMethod03474(localValue2, localValue8);
                  localValue2.translate(0.0, localValue7.getHeight() / 1.5, 0.0);
                  localValue2.multiply(localValue3.getRotation());
                  RenderPipeline.internalMethod05007(localValue2, localValue5, -localValue9 / 2.0F, -localValue9 / 2.0F, 0.0, localValue9, localValue9, localValue10.mulAlpha(0.5F));
                  localValue2.pop();
               }
            }
         }

         HudRenderUtils.internalMethod05816(localValue5);
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue14 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         for (Entity localValue17 : internalField0149.world.getEntities()) {
            if (this.internalMethod03051(localValue17)) {
               MultiSelectSetting localValue19 = this.internalMethod04629(localValue17);
               float localValue21 = localValue17.getWidth() / 2.0F;
               Box localValue11 = new Box(-localValue21, 0.0, -localValue21, localValue21, localValue17.getHeight(), localValue21);
               localValue2.push();
               HudRenderUtils.internalMethod03474(localValue2, ScreenProjectionUtils.internalMethod02822(localValue17, localValue4));
               boolean localValue12 = localValue19 != null && this.internalMethod00109(localValue19, "esp.boxes.mode.fill");
               ColorRGBA localValue13 = this.internalMethod06722(localValue17).mulAlpha(localValue12 && !this.internalMethod00109(localValue19, "esp.boxes.mode.outline") ? 0.35F : 0.1F);
               if (localValue12) {
                  Render3DUtils.internalMethod05375(localValue2, localValue14, localValue11, localValue13, localValue13.mulAlpha(0.0F));
               } else {
                  Render3DUtils.internalMethod02535(localValue2, localValue14, localValue11, localValue13);
               }

               localValue2.pop();
            }
         }

         HudRenderUtils.internalMethod05816(localValue14);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue16 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (Entity localValue20 : internalField0149.world.getEntities()) {
            if (this.internalMethod03051(localValue20)) {
               MultiSelectSetting localValue22 = this.internalMethod04629(localValue20);
               float localValue23 = localValue20.getWidth() / 2.0F;
               Box localValue24 = new Box(-localValue23, 0.0, -localValue23, localValue23, localValue20.getHeight(), localValue23);
               localValue2.push();
               HudRenderUtils.internalMethod03474(localValue2, ScreenProjectionUtils.internalMethod02822(localValue20, localValue4));
               ColorRGBA localValue25 = this.internalMethod06722(localValue20);
               if (localValue22 != null && this.internalMethod00109(localValue22, "esp.boxes.mode.outline")) {
                  Render3DUtils.internalMethod05921(localValue2, localValue16, localValue24, localValue25, localValue25.mulAlpha(0.0F));
               } else {
                  Render3DUtils.internalMethod08795(localValue2, localValue16, localValue24, localValue25);
               }

               localValue2.pop();
            }
         }

         HudRenderUtils.internalMethod05816(localValue16);
         RenderSystem.depthMask(true);
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   };

   public BoxEspFeature() {
      super(
         "boxes",
         EntityTargetType.internalField0027,
         EntityTargetType.internalField0028,
         EntityTargetType.internalField0963,
         EntityTargetType.internalField0964
      );
   }

   @Override
   public void internalMethod06439(UiRenderContext localValue1, Entity localValue2, float localValue3, float localValue4, EntityTargetType localValue5, PlayerTargetType localValue6) {
      float localValue7 = localValue2.getHeight();
      float localValue8 = localValue2.getWidth() / 2.0F;
      MatrixStack localValue9 = rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getMatrices());
      ColorRGBA localValue10 = this.internalMethod01120(localValue5, localValue6);
      float localValue11 = EspEditorScreen.internalMethod08401();
      float localValue12 = EspEditorScreen.internalMethod08412();
      float localValue13 = localValue2 instanceof LivingEntity ? EspEditorScreen.internalMethod00034() : 0.0F;
      float localValue14 = 45.0F;
      Quaternionf localValue15 = new Quaternionf().rotateZ((float) Math.PI).rotateX((float)Math.toRadians(localValue13));
      float localValue16 = (localValue8 + localValue7) * 1.4F;
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.depthMask(false);
      localValue9.push();
      localValue9.translate(localValue3, localValue4 + 36.0F, 50.0F);
      localValue9.scale(localValue11, localValue11, -localValue11);
      localValue9.multiply(localValue15);
      localValue9.translate(0.0F, -localValue12, 0.0F);
      RenderSystem.setShaderTexture(0, internalField0354);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue17 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      if (this.internalMethod06467(localValue2, "esp.boxes.mode.fill")) {
         RenderPipeline.internalMethod05007(localValue9, localValue17, -localValue16 / 2.0F, -localValue7 / 5.0F - localValue16 / 2.0F, 0.0, localValue16, localValue16, localValue10.mulAlpha(0.5F));
      } else {
         RenderPipeline.internalMethod05007(localValue9, localValue17, -localValue16 / 2.0F, localValue7 / 6.0F - localValue16 / 2.0F, 0.0, localValue16, localValue16, localValue10.mulAlpha(0.5F));
         RenderPipeline.internalMethod05007(localValue9, localValue17, -localValue16 / 2.0F, -localValue7 / 6.0F - localValue16 / 2.0F, 0.0, localValue16, localValue16, localValue10.mulAlpha(0.5F));
      }

      HudRenderUtils.internalMethod05816(localValue17);
      RenderSystem.setShaderTexture(0, 0);
      localValue9.pop();
      localValue9.push();
      localValue9.translate(localValue3, localValue4 + 36.0F, 50.0F);
      localValue9.scale(localValue11, localValue11, -localValue11);
      localValue9.multiply(localValue15);
      localValue9.translate(0.0F, -localValue7 / 2.0F - localValue12, 0.0F);
      localValue9.translate(0.0F, localValue7 / 2.0F, 0.0F);
      localValue9.multiply(new Quaternionf().rotateY((float)Math.toRadians(localValue14)));
      localValue9.translate(0.0F, -localValue7 / 2.0F, 0.0F);
      Box localValue18 = new Box(-localValue8, 0.0, -localValue8, localValue8, localValue7, localValue8);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder localValue19 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      MultiSelectSetting localValue20 = this.internalMethod04629(localValue2);
      boolean localValue21 = localValue20 != null && this.internalMethod00109(localValue20, "esp.boxes.mode.fill");
      ColorRGBA localValue22 = this.internalMethod06722(localValue2).mulAlpha(localValue21 && !this.internalMethod00109(localValue20, "esp.boxes.mode.outline") ? 0.35F : 0.1F);
      if (localValue21) {
         Render3DUtils.internalMethod05375(localValue9, localValue19, localValue18, localValue22, localValue22.mulAlpha(0.0F));
      } else {
         Render3DUtils.internalMethod02535(localValue9, localValue19, localValue18, localValue22);
      }

      HudRenderUtils.internalMethod05816(localValue19);
      BufferBuilder localValue23 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      ColorRGBA localValue24 = this.internalMethod06722(localValue2);
      if (localValue20 != null && this.internalMethod00109(localValue20, "esp.boxes.mode.outline")) {
         Render3DUtils.internalMethod05921(localValue9, localValue23, localValue18, localValue24, localValue24.mulAlpha(0.0F));
      } else {
         Render3DUtils.internalMethod08795(localValue9, localValue23, localValue18, localValue24);
      }

      HudRenderUtils.internalMethod05816(localValue23);
      localValue9.pop();
      RenderSystem.depthMask(true);
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   private ColorRGBA internalMethod01120(EntityTargetType localValue1, PlayerTargetType localValue2) {
      BooleanSetting localValue3;
      ColorSetting localValue4;
      if (localValue1 == EntityTargetType.internalField0027) {
         localValue3 = this.internalMethod02672("theme.sync", localValue2);
         localValue4 = this.internalMethod02672("esp.boxes.color", localValue2);
      } else {
         localValue3 = this.internalMethod05940("theme.sync", localValue1);
         localValue4 = this.internalMethod05940("esp.boxes.color", localValue1);
      }

      return localValue3 != null && localValue3.internalMethod04496()
         ? ThemeColors.internalMethod02531()
         : (localValue4 != null ? localValue4.internalMethod05620() : ThemeColors.internalMethod02531());
   }

   private ColorRGBA internalMethod06722(Entity localValue1) {
      BooleanSetting localValue2;
      ColorSetting localValue3;
      if (localValue1 instanceof AbstractClientPlayerEntity localValue4) {
         PlayerTargetType localValue5 = this.internalMethod04462(localValue4);
         localValue2 = this.internalMethod02672("theme.sync", localValue5);
         localValue3 = this.internalMethod02672("esp.boxes.color", localValue5);
      } else if (localValue1 instanceof ItemEntity) {
         localValue2 = this.internalMethod05940("theme.sync", EntityTargetType.internalField0964);
         localValue3 = this.internalMethod05940("esp.boxes.color", EntityTargetType.internalField0964);
      } else if (localValue1 instanceof HostileEntity) {
         localValue2 = this.internalMethod05940("theme.sync", EntityTargetType.internalField0028);
         localValue3 = this.internalMethod05940("esp.boxes.color", EntityTargetType.internalField0028);
      } else {
         if (!(localValue1 instanceof AnimalEntity)) {
            return ThemeColors.internalMethod02531();
         }

         localValue2 = this.internalMethod05940("theme.sync", EntityTargetType.internalField0963);
         localValue3 = this.internalMethod05940("esp.boxes.color", EntityTargetType.internalField0963);
      }

      return localValue2 != null && localValue2.internalMethod04496()
         ? ThemeColors.internalMethod02531()
         : (localValue3 != null ? localValue3.internalMethod05620() : ThemeColors.internalMethod02531());
   }

   private MultiSelectSetting internalMethod04629(Entity localValue1) {
      if (localValue1 instanceof AbstractClientPlayerEntity localValue2) {
         return this.internalMethod02672("esp.boxes.mode", this.internalMethod04462(localValue2));
      } else if (localValue1 instanceof ItemEntity) {
         return this.internalMethod05940("esp.boxes.mode", EntityTargetType.internalField0964);
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod05940("esp.boxes.mode", EntityTargetType.internalField0028);
      } else {
         return localValue1 instanceof AnimalEntity ? this.internalMethod05940("esp.boxes.mode", EntityTargetType.internalField0963) : null;
      }
   }

   private boolean internalMethod06467(Entity localValue1, String localValue2) {
      MultiSelectSetting localValue3 = this.internalMethod04629(localValue1);
      return localValue3 != null && this.internalMethod00109(localValue3, localValue2);
   }

   private boolean internalMethod00109(MultiSelectSetting localValue1, String localValue2) {
      return localValue1.internalMethod07492().stream().anyMatch(localValue1x -> localValue1x.getName().equals(localValue2));
   }

   private boolean internalMethod03051(Entity localValue1) {
      if (localValue1 instanceof AbstractClientPlayerEntity localValue2) {
         return localValue2 == internalField0149.player && internalField0149.options.getPerspective().isFirstPerson()
            ? false
            : this.internalMethod06170(this.internalMethod04462(localValue2));
      } else if (localValue1 instanceof ItemEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0964);
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0028);
      } else {
         return localValue1 instanceof AnimalEntity ? this.internalMethod06206(EntityTargetType.internalField0963) : false;
      }
   }

   private PlayerTargetType internalMethod04462(AbstractClientPlayerEntity localValue1) {
      if (localValue1 == internalField0149.player) {
         return PlayerTargetType.internalField0026;
      } else {
         FriendListManager localValue2 = RockstarClient.getInstance().internalMethod03375();
         if (localValue2.internalMethod00380(localValue1.getName().getString())) {
            return PlayerTargetType.internalField0961;
         } else {
            for (Packets.InternalType0018 localValue4 : Information.getVisiblePlayers()) {
               if (localValue4.gameInfo() != null && localValue4.gameInfo().nickname() != null && localValue4.gameInfo().nickname().equals(localValue1.getName().getString())) {
                  return PlayerTargetType.internalField0962;
               }
            }

            return PlayerTargetType.internalField0025;
         }
      }
   }
}
