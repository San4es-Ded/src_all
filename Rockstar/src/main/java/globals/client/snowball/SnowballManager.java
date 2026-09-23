package globals.client.snowball;




import rockstar.client.util.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import globals.client.WorldKey;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.other.GlobalsMenuModule;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.render.RenderPipeline;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.util.Stopwatch;

public class SnowballManager implements MinecraftClientAccess {
   private static SnowballManager instance;
   private final List<GlowSnowball> snowballs = new CopyOnWriteArrayList<>();
   private final List<SnowParticle> particles = new CopyOnWriteArrayList<>();
   private static final Identifier BLOOM_TEXTURE = RockstarClient.id("textures/bloom.png");
   private static final double[] SPHERE_X = new double[50];
   private static final double[] SPHERE_Y = new double[50];
   private static final double[] SPHERE_Z = new double[50];
   private static final Stopwatch timer = new Stopwatch();
   private int targetFrozenTicks = 0;
   public final EventListener<KeyPressEvent> onKeyPress = localValue1 -> {
      if (localValue1.getAction() == 1) {
         if (internalField0149.currentScreen == null) {
            GlobalsMenuModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class);
            if (localValue2.internalMethod06378().internalMethod02165(localValue1.getKey())) {
               this.throwSnowball();
            }
         }
      }
   };
   private final EventListener<MouseEvent> onMouseEvent = localValue1 -> {
      if (internalField0149.currentScreen == null) {
         GlobalsMenuModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class);
         if (localValue2.internalMethod06378().internalMethod02165(localValue1.getButton())) {
            this.throwSnowball();
         }
      }
   };
   public final EventListener<ClientPlayerTickEvent> onTick = localValue1 -> this.tick();
   public final EventListener<Render3DEvent> onRender3D = localValue1 -> this.render3D(localValue1.getMatrices(), localValue1.getTickDelta());

   public static SnowballManager getInstance() {
      if (instance == null) {
         instance = new SnowballManager();
      }

      return instance;
   }

   public void register() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public void throwSnowball() {
      if (internalField0149.player != null && timer.internalMethod02365(500L)) {
         Vec3d localValue1 = internalField0149.player.getEyePos();
         Vec3d localValue2 = internalField0149.player.getRotationVec(1.0F);
         Packets.InternalType0031 localValue3 = RockstarClient.getInstance().internalMethod06050().getGameInfo();
         if (localValue3 != null) {
            RockstarClient.getInstance()
               .internalMethod06050()
               .send(
                  new Packets.InternalType0393(
                     new Packets.InternalType0068(localValue1.x, localValue1.y, localValue1.z), new Packets.InternalType0068(localValue2.x, localValue2.y, localValue2.z), "", localValue3.server(), localValue3.hash()
                  )
               );
            timer.internalMethod00701();
         }
      }
   }

   public void onSnowballReceived(Packets.InternalType0041 localValue1) {
      if (internalField0149.player != null) {
         if (localValue1.author().gameInfo() != null) {
            String localValue2 = localValue1.author().gameInfo().nickname();
            if (localValue2 != null && !localValue2.isEmpty()) {
               if (WorldKey.sameWorld(localValue1.author().gameInfo())) {
                  Vec3d localValue3 = new Vec3d(localValue1.position().x(), localValue1.position().y(), localValue1.position().z());
                  Vec3d localValue4 = new Vec3d(localValue1.direction().x(), localValue1.direction().y(), localValue1.direction().z());
                  this.snowballs.add(new GlowSnowball(localValue3, localValue4, localValue2));
               }
            }
         }
      }
   }

   private void spawnDebrisParticles(GlowSnowball localValue1) {
      Vec3d localValue2 = localValue1.getPosition();
      Vec3d localValue3 = localValue1.getVelocity();

      for (int localValue4 = 0; localValue4 < 150; localValue4++) {
         double localValue5 = Math.acos(1.0 - 2.0 * (localValue4 + 0.5) / 150.0);
         double localValue7 = Math.PI * (1.0 + Math.sqrt(5.0)) * localValue4;
         double localValue9 = 0.1F * MathUtils.internalMethod04857(localValue5) * MathUtils.internalMethod04929(localValue7);
         double localValue11 = 0.1F * MathUtils.internalMethod04857(localValue5) * MathUtils.internalMethod04857(localValue7);
         double localValue13 = 0.1F * MathUtils.internalMethod04929(localValue5);
         Vec3d localValue15 = localValue2.add(localValue9, localValue11, localValue13);
         this.particles.add(new SnowParticle(localValue15, localValue3, 0.1F));
      }
   }

   public void tick() {
      ArrayList localValue1 = new ArrayList();

      for (GlowSnowball localValue3 : this.snowballs) {
         localValue3.tick();
         if (localValue3.didHitPlayer()) {
            this.spawnDebrisParticles(localValue3);
            if (localValue3.didHitLocalPlayer() && internalField0149.player != null) {
               this.targetFrozenTicks = 139;
            }
         }

         if (localValue3.didHitBlock()) {
            this.spawnDebrisParticles(localValue3);
         }

         if (localValue3.shouldRemove()) {
            localValue1.add(localValue3);
         }
      }

      this.snowballs.removeAll(localValue1);
      ArrayList localValue5 = new ArrayList();

      for (SnowParticle localValue4 : this.particles) {
         localValue4.tick();
         if (localValue4.shouldRemove()) {
            localValue5.add(localValue4);
         }
      }

      this.particles.removeAll(localValue5);
      if (internalField0149.player instanceof FakeFrozenTicksAccess localValue7) {
         int localValue9 = localValue7.rockstar$getFakeFrozenTicks();
         if (this.targetFrozenTicks > 0) {
            if (localValue9 < this.targetFrozenTicks) {
               localValue7.rockstar$setFakeFrozenTicks(Math.min(localValue9 + 20, this.targetFrozenTicks));
            } else {
               this.targetFrozenTicks = 0;
            }
         } else if (localValue9 > 0) {
            localValue7.rockstar$setFakeFrozenTicks(localValue9 - 2);
         }
      }
   }

   public void render3D(MatrixStack localValue1, float localValue2) {
      if (!this.snowballs.isEmpty() || !this.particles.isEmpty()) {
         Camera localValue3 = internalField0149.gameRenderer.getCamera();
         localValue1.push();
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.depthMask(false);
         RenderSystem.setShaderTexture(0, BLOOM_TEXTURE);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         BufferBuilder localValue4 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         ColorRGBA localValue5 = new ColorRGBA(180.0F, 220.0F, 255.0F, 255.0F);

         for (GlowSnowball localValue7 : this.snowballs) {
            if (!localValue7.isInsideAuthor()) {
               Vec3d localValue8 = localValue7.getRenderPos(localValue2);
               float localValue9 = localValue7.getAge() * 0.1F;
               float localValue10 = localValue7.getAlpha();
               double localValue11 = MathUtils.internalMethod04929(localValue9);
               double localValue13 = MathUtils.internalMethod04857(localValue9);

               for (int localValue15 = 0; localValue15 < 50; localValue15++) {
                  double localValue16 = SPHERE_X[localValue15] * localValue11 - SPHERE_Z[localValue15] * localValue13;
                  double localValue18 = SPHERE_X[localValue15] * localValue13 + SPHERE_Z[localValue15] * localValue11;
                  Vec3d localValue20 = localValue8.add(localValue16, SPHERE_Y[localValue15], localValue18);
                  localValue1.push();
                  HudRenderUtils.internalMethod03474(localValue1, localValue20);
                  localValue1.multiply(localValue3.getRotation());
                  RenderPipeline.internalMethod05007(localValue1, localValue4, -0.125, -0.125, 0.0, 0.25, 0.25, localValue5.mulAlpha(0.9F * localValue10));
                  RenderPipeline.internalMethod05007(localValue1, localValue4, -0.25, -0.25, 0.0, 0.5, 0.5, localValue5.mulAlpha(0.15F * localValue10));
                  localValue1.pop();
               }
            }
         }

         for (SnowParticle localValue22 : this.particles) {
            Vec3d localValue23 = localValue22.getRenderPos(localValue2);
            float localValue24 = localValue22.getAlpha();
            localValue1.push();
            HudRenderUtils.internalMethod03474(localValue1, localValue23);
            localValue1.multiply(localValue3.getRotation());
            RenderPipeline.internalMethod05007(localValue1, localValue4, -0.075F, -0.075F, 0.0, 0.15F, 0.15F, localValue5.mulAlpha(0.9F * localValue24));
            RenderPipeline.internalMethod05007(localValue1, localValue4, -0.15F, -0.15F, 0.0, 0.3F, 0.3F, localValue5.mulAlpha(0.15F * localValue24));
            localValue1.pop();
         }

         HudRenderUtils.internalMethod05816(localValue4);
         RenderSystem.depthMask(true);
         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         RenderSystem.disableDepthTest();
         localValue1.pop();
      }
   }

   static {
      for (int localValue0 = 0; localValue0 < 50; localValue0++) {
         double localValue1 = Math.acos(1.0 - 2.0 * (localValue0 + 0.5) / 50.0);
         double localValue3 = Math.PI * (1.0 + Math.sqrt(5.0)) * localValue0;
         SPHERE_X[localValue0] = 0.1F * MathUtils.internalMethod04857(localValue1) * MathUtils.internalMethod04929(localValue3);
         SPHERE_Y[localValue0] = 0.1F * MathUtils.internalMethod04857(localValue1) * MathUtils.internalMethod04857(localValue3);
         SPHERE_Z[localValue0] = 0.1F * MathUtils.internalMethod04929(localValue1);
      }
   }
}
