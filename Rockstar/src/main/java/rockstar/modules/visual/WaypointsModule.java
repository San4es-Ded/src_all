package rockstar.modules.visual;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import globals.client.Information;
import globals.client.api.RockNetClient;
import globals.shared.proto.Packets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Waypoints",
   category = ModuleCategory.VISUALS
)
public class WaypointsModule extends Module {
   private KeybindSetting internalField0648;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private BooleanSetting internalField0650;
   private ColorSetting internalField0665;
   private static final String internalField0248 = "%self%";
   private final Map<String, WaypointsModule.InternalType0105> internalField0543 = new HashMap<>();
   private final EventListener<KeyPressEvent> internalField0157 = localValue1 -> {
      if (this.internalField0648.internalMethod02165(localValue1.getKey()) && localValue1.getAction() == 1 && internalField0149.currentScreen == null) {
         this.internalMethod09682();
      }
   };
   private final EventListener<MouseEvent> internalField0158 = localValue1 -> {
      if (this.internalField0648.internalMethod02165(localValue1.getButton()) && internalField0149.currentScreen == null) {
         this.internalMethod09682();
      }
   };
   private final EventListener<Render3DEvent> internalField1028 = localValue1 -> {
      MatrixStack localValue2 = localValue1.getMatrices();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
      Camera localValue3 = internalField0149.gameRenderer.getCamera();
      Identifier localValue4 = RockstarClient.id("textures/bloom.png");
      RenderSystem.setShaderTexture(0, localValue4);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue5 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (WaypointsModule.InternalType0105 localValue7 : this.internalField0543.values()) {
         Entity localValue8 = null;
         ColorRGBA localValue9 = this.internalMethod06970(localValue7);

         for (Entity localValue11 : internalField0149.world.getEntities()) {
            if (localValue11.getName().getString().equals(localValue7.internalMethod05795())) {
               localValue8 = localValue11;
            }
         }

         if (localValue8 != internalField0149.player) {
            localValue2.push();
            HudRenderUtils.internalMethod03474(localValue2, localValue7.internalField0283);
            boolean localValue16 = internalField0149.player.getEntityPos().distanceTo(localValue7.internalField0283) > 60.0;
            int localValue17 = localValue16 ? 150 : 10;

            for (int localValue12 = 0; localValue12 < localValue17; localValue12++) {
               float localValue13 = 0.6F;
               if (localValue16) {
                  localValue13 *= 2.0F;
               }

               float localValue14 = localValue13 * 5.0F;
               localValue2.push();
               localValue2.translate(0.0F, localValue12 / 5.0F, 0.0F);
               localValue2.multiply(localValue3.getRotation());
               RenderPipeline.internalMethod05007(
                  localValue2,
                  localValue5,
                  -localValue14 / 2.0F,
                  -localValue14 / 2.0F,
                  -localValue13 / 2.0F,
                  localValue14,
                  localValue14,
                  localValue9.mulAlpha((0.4F + localValue7.internalField0808.internalMethod02881() * 0.7F) * localValue7.internalField0809.internalMethod02881() / 5.0F)
               );
               RenderPipeline.internalMethod05007(
                  localValue2,
                  localValue5,
                  -localValue13 / 2.0F,
                  -localValue13 / 2.0F,
                  -localValue13 / 2.0F,
                  localValue13,
                  localValue13,
                  localValue9.mulAlpha((0.4F + localValue7.internalField0808.internalMethod02881() * 0.7F) * localValue7.internalField0809.internalMethod02881())
               );
               localValue2.pop();
            }

            localValue2.pop();
         }
      }

      BuiltBuffer localValue15 = localValue5.endNullable();
      if (localValue15 != null) {
         BufferRenderer.drawWithGlobalProgram(localValue15);
      }

      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
   };
   private final EventListener<PreHudRenderEvent> internalField1029 = localValue1 -> {
      org.joml.Matrix3x2fStack localValue2 = localValue1.getContext().getMatrices();
      this.internalField0543
         .values()
         .removeIf(localValue0 -> localValue0.internalField0519.internalMethod02365(localValue0.internalField0229) && localValue0.internalField0809.internalMethod02881() <= 0.01F);

      for (WaypointsModule.InternalType0105 localValue4 : this.internalField0543.values()) {
         if (localValue4 instanceof WaypointsModule.InternalType0104 localValue5) {
            Entity localValue6 = null;

            for (Entity localValue8 : internalField0149.world.getEntities()) {
               if (localValue8.getName().getString().equals(localValue5.internalMethod05795())) {
                  localValue6 = localValue8;
               }
            }

            if (localValue6 != null) {
               localValue5.internalField0283 = ScreenProjectionUtils.internalMethod02822(localValue6, internalField0149.getRenderTickCounter().getTickProgress(true));
            }
         }

         Vec2f localValue9 = ScreenProjectionUtils.internalMethod00612(localValue4.internalField0283.add(0.0, 0.5, 0.0));
         if (localValue9 != null) {
            float localValue10 = (float)internalField0149.player.getEntityPos().distanceTo(localValue4.internalField0283);
            float localValue11 = MathHelper.clamp(1.0F - localValue10 / 20.0F, 0.5F, 1.0F);
            float localValue12 = (0.4F + localValue4.internalField0808.internalMethod02881() * 0.7F) * localValue4.internalField0809.internalMethod02881();
            localValue2.pushMatrix();
            localValue2.translate(localValue9.x, localValue9.y);
            localValue2.scale(localValue11, localValue11);
            WaypointLabelRenderer.internalMethod03782(localValue1.getContext(), localValue4.internalMethod05795(), localValue10, this.internalMethod06970(localValue4), localValue12);
            localValue2.popMatrix();
         }

         this.internalMethod00140(localValue4);
      }
   };

   public WaypointsModule() {
      this.internalMethod09681();
   }

   private void internalMethod09681() {
      this.internalField0648 = new KeybindSetting(this, "modules.settings.waypoints.key").internalMethod01713(86);
      this.internalField0383 = new SliderSetting(this, "modules.settings.waypoints.player_time")
         .internalMethod08673(1.0F)
         .internalMethod05900(5.0F)
         .internalMethod02732(100.0F)
         .internalMethod08074(20.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.waypoints.block_time")
         .internalMethod08673(1.0F)
         .internalMethod05900(5.0F)
         .internalMethod02732(20.0F)
         .internalMethod08074(10.0F);
      this.internalField0650 = new BooleanSetting(this, "theme.sync").internalMethod06630();
      this.internalField0665 = new ColorSetting(this, "modules.settings.waypoints.color", this.internalField0650::internalMethod04496)
         .internalMethod04886(ThemeColors.internalMethod02531())
         .internalMethod05166(false);
   }

   private void internalMethod09682() {
      if (internalField0149.player != null && internalField0149.world != null) {
         ColorRGBA localValue1 = this.internalMethod01299();
         Vec3d localValue2 = internalField0149.player.getRotationVec(internalField0149.getRenderTickCounter().getTickProgress(true));
         Vec3d localValue3 = internalField0149.player.getEyePos().add(localValue2.multiply(200.0));
         Entity localValue4 = null;

         for (Entity localValue6 : internalField0149.world.getEntities()) {
            Vec3d localValue7 = (Vec3d)localValue6.getBoundingBox().expand(0.3).raycast(internalField0149.player.getEyePos(), localValue3).orElse(null);
            if (localValue7 != null) {
               double localValue8 = internalField0149.player.getEyePos().distanceTo(localValue7);
               if (localValue8 <= 200.0) {
                  localValue4 = localValue6;
               }
            }
         }

         Object localValue10;
         if (localValue4 != null) {
            localValue10 = new WaypointsModule.InternalType0104(
               localValue1, localValue4.getName().getString(), localValue4.getEntityPos(), (long)this.internalField0383.internalMethod08576() * 1000L
            );
         } else {
            HitResult localValue11 = internalField0149.player.raycast(200.0, internalField0149.getRenderTickCounter().getTickProgress(true), false);
            if (localValue11.getType() != Type.BLOCK || !(localValue11 instanceof BlockHitResult localValue13)) {
               return;
            }

            localValue10 = new WaypointsModule.InternalType0105(localValue1, localValue13.getPos(), (long)this.internalField0382.internalMethod08576() * 1000L);
         }

         this.internalField0543.put("%self%", (WaypointsModule.InternalType0105)localValue10);
         RockNetClient localValue12 = RockstarClient.getInstance().internalMethod06050();
         if (Information.getUser() != null && localValue12.getGameInfo() != null) {
            localValue12.send(
               new Packets.InternalType0064(
                  ((WaypointsModule.InternalType0105)localValue10).internalMethod05795(),
                  new Packets.InternalType0068(
                     ((WaypointsModule.InternalType0105)localValue10).internalField0283.x,
                     ((WaypointsModule.InternalType0105)localValue10).internalField0283.y,
                     ((WaypointsModule.InternalType0105)localValue10).internalField0283.z
                  ),
                  "",
                  localValue12.getGameInfo().server(),
                  localValue12.getGameInfo().hash(),
                  ((WaypointsModule.InternalType0105)localValue10).internalMethod06571(),
                  localValue1.getRGB()
               )
            );
         }
      }
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null && !this.internalField0543.isEmpty()) {
         if (internalField0149.player.age % 10 == 0) {
            RockNetClient localValue1 = RockstarClient.getInstance().internalMethod06050();
            if (Information.getUser() == null || localValue1.getGameInfo() == null) {
               return;
            }

            for (Entry localValue3 : this.internalField0543.entrySet()) {
               if (!((WaypointsModule.InternalType0105)localValue3.getValue()).internalMethod05795().contains(" ")) {
                  Entity localValue4 = null;

                  for (Entity localValue6 : internalField0149.world.getEntities()) {
                     if (localValue6.getName().getString().equals(((WaypointsModule.InternalType0105)localValue3.getValue()).internalMethod05795())) {
                        localValue4 = localValue6;
                     }
                  }

                  if (localValue4 != null) {
                     WaypointsModule.InternalType0105 localValue7 = (WaypointsModule.InternalType0105)localValue3.getValue();
                     localValue1.send(
                        new Packets.InternalType0134(
                           localValue7.internalMethod05795(),
                           new Packets.InternalType0068(localValue4.getEntityPos().x, localValue4.getEntityPos().y, localValue4.getEntityPos().z),
                           "",
                           localValue1.getGameInfo().server(),
                           localValue1.getGameInfo().hash()
                        )
                     );
                  }
               }
            }
         }
      }
   }

   private void internalMethod00140(WaypointsModule.InternalType0105 localValue1) {
      if (localValue1.internalField0809.internalMethod02881() == 1.0F) {
         localValue1.internalField0808.internalMethod02887();
         localValue1.internalField0808.internalMethod07061(Math.max((localValue1.internalField0229 - localValue1.internalField0519.internalMethod00700()) / 4L, 450L));
      } else {
         localValue1.internalField0808.internalMethod07061(0L);
         localValue1.internalField0808.internalMethod07059(1.0F);
      }

      localValue1.internalField0808.internalMethod07061(Math.max((localValue1.internalField0229 - localValue1.internalField0519.internalMethod00700()) / 4L, 450L));
      if (localValue1.internalField0519.internalMethod00700() >= localValue1.internalField0229) {
         localValue1.internalField0809.internalMethod07059(0.0F);
      } else {
         localValue1.internalField0809.internalMethod07059(1.0F);
      }
   }

   private ColorRGBA internalMethod01299() {
      return this.internalField0650.internalMethod04496() ? ThemeColors.internalMethod02531() : this.internalField0665.internalMethod05620();
   }

   private ColorRGBA internalMethod06970(WaypointsModule.InternalType0105 localValue1) {
      return this.internalField0650.internalMethod04496() ? ThemeColors.internalMethod02531() : localValue1.internalField0777;
   }

   public void internalMethod02656(Packets.InternalType0115 localValue1) {
      if (!this.internalMethod02002(localValue1.author())) {
         Packets.InternalType0068 localValue2 = localValue1.vector();
         if (localValue1.name().contains(" ")) {
            WaypointsModule.InternalType0105 localValue3 = new WaypointsModule.InternalType0105(
               ColorRGBA.fromInt(localValue1.color()), new Vec3d(localValue2.x(), localValue2.y(), localValue2.z()), localValue1.livingTime()
            );
            this.internalField0543.put(localValue1.author().username(), localValue3);
         } else {
            WaypointsModule.InternalType0104 localValue4 = new WaypointsModule.InternalType0104(
               ColorRGBA.fromInt(localValue1.color()), localValue1.name(), new Vec3d(localValue2.x(), localValue2.y(), localValue2.z()), localValue1.livingTime()
            );
            this.internalField0543.put(localValue1.author().username(), localValue4);
         }
      }
   }

   public void internalMethod06096(Packets.InternalType0338 localValue1) {
      if (!this.internalMethod02002(localValue1.author())) {
         Entity localValue2 = null;

         for (Entity localValue4 : internalField0149.world.getEntities()) {
            if (localValue4.getName().getString().equals(localValue1.name())) {
               localValue2 = localValue4;
            }
         }

         if (localValue2 == null) {
            for (WaypointsModule.InternalType0105 localValue7 : this.internalField0543.values()) {
               if (localValue7.internalMethod05795().equals(localValue1.name())) {
                  Packets.InternalType0068 localValue5 = localValue1.vector();
                  localValue7.internalMethod07252(new Vec3d(localValue5.x(), localValue5.y(), localValue5.z()));
               }
            }
         }
      }
   }

   private boolean internalMethod02002(Packets.InternalType0018 localValue1) {
      if (localValue1 == null) {
         return false;
      } else {
         return Information.getUser() != null && Information.getUser().username().equalsIgnoreCase(localValue1.username())
            ? true
            : localValue1.gameInfo() != null && internalField0149.getSession().getUsername().equals(localValue1.gameInfo().nickname());
      }
   }

   static class InternalType0104 extends WaypointsModule.InternalType0105 {
      private final String internalField0248;

      public InternalType0104(ColorRGBA localValue1, String localValue2, Vec3d localValue3, long localValue4) {
         super(localValue1, localValue3, localValue4);
         this.internalField0248 = localValue2;
      }

      @Override
      public String internalMethod05795() {
         return this.internalField0248;
      }
   }

   static class InternalType0105 {
      final ColorRGBA internalField0777;
      public Vec3d internalField0283;
      final Stopwatch internalField0519 = new Stopwatch();
      final long internalField0229;
      final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1626);
      final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1626);

      public String internalMethod05795() {
         return String.format("%s %s %s", Math.round(this.internalField0283.x), Math.round(this.internalField0283.y), Math.round(this.internalField0283.z));
      }

      @Generated
      public InternalType0105(ColorRGBA localValue1, Vec3d localValue2, long localValue3) {
         this.internalField0777 = localValue1;
         this.internalField0283 = localValue2;
         this.internalField0229 = localValue3;
      }

      @Generated
      public void internalMethod07252(Vec3d localValue1) {
         this.internalField0283 = localValue1;
      }

      @Generated
      public long internalMethod06571() {
         return this.internalField0229;
      }
   }
}
