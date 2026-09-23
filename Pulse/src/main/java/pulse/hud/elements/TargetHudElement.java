package pulse.hud.elements;

import java.awt.Color;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.hit.HitResult.Type;
import org.joml.Matrix3x2fStack;
import pulse.animation.AnimationState;
import pulse.animation.Easing;
import pulse.gui.core.PulseClickGuiScreen;
import pulse.hud.core.HudElement;
import pulse.hud.core.HudServiceRegistry;
import pulse.module.ModuleRegistry;
import pulse.render.RenderSystemHelper;
import pulse.render.Renderer2D;
import pulse.render.font.FontManager;
import pulse.render.font.FontRenderer;

public class TargetHudElement extends HudElement {
   private static final float INFO_WIDTH = 134.0F;
   private static final float INFO_HEIGHT = 38.0F;
   private static final float GEAR_WIDTH = 134.0F;
   private static final float GEAR_HEIGHT = 22.0F;
   private static final float GEAR_GAP = 4.0F;
   private static final float AVATAR_SIZE = 23.0F;
   private static final float CARD_RADIUS = 7.0F;
   private static final float AVATAR_RADIUS = 4.0F;
   private static final float HP_BAR_HEIGHT = 2.4F;
   private static final float ITEM_SIZE = 14.0F;
   private static final float DURABILITY_HEIGHT = 1.4F;
   private static final Color SHADOW = new Color(0, 0, 0, 96);
   private static final Color CARD_TOP = new Color(15, 15, 26, 232);
   private static final Color CARD_BOTTOM = new Color(9, 9, 18, 236);
   private static final Color GEAR_CARD = new Color(12, 12, 22, 228);
   private static final Color AVATAR_BACK_TOP = new Color(30, 28, 42, 245);
   private static final Color AVATAR_BACK_BOTTOM = new Color(18, 17, 28, 245);
   private static final Color BAR_BACK = new Color(22, 20, 34, 230);
   private static final Color BAR_GLOW = new Color(125, 82, 255, 150);
   private static final Color TEXT_SECONDARY = new Color(230, 230, 238, 238);
   private static final Color BADGE_TEXT = new Color(185, 185, 198, 220);
   private final AnimationState visibilityAnimation = new AnimationState();
   private final AnimationState healthAnimation = new AnimationState();
   private final AnimationState switchAnimation = new AnimationState();
   private LivingEntity target;
   private String targetName = "Target";
   private float targetHealth;
   private float targetMaxHealth = 20.0F;
   private Identifier skinTexture;
   private Identifier lastTexture;
   private long textureChangedAtMs;
   private boolean visible;
   private boolean settingsBound;
   private long lastTargetTime;

   public TargetHudElement(float f, float f2) {
      super(f, f2);
      this.visibilityAnimation.d(0.0);
      this.healthAnimation.d(1.0);
      this.switchAnimation.d(1.0);
      this.d = 134.0F;
      this.e = 64.0F;
   }

   @Override
   protected void a() {
      this.bindSettings();
      float fG = this.g();
      this.d = 130.0F * fG;
      this.e = 40.0F * fG;
   }

   @Override
   public void a(Matrix3x2fStack MatrixStackVar, Renderer2D renderer2D, float f, float f2) {
      if (this.a.player != null) {
         this.a();
         this.updateTarget();
         float fJ = (float)this.visibilityAnimation.j();
         if (!(fJ < 0.01F)) {
            float fG = this.g();
            float f3 = this.b;
            float f4 = this.c;
            renderer2D.a(f3, f4, this.d, this.e, 6.0F * fG, withAlpha(new Color(0, 0, 0, 70), fJ), MatrixStackVar);
            Color bgColor = new Color(15, 15, 18, 255);
            renderer2D.a(f3, f4, this.d, this.e, 6.0F * fG, withAlpha(bgColor, fJ), MatrixStackVar);
            float headSize = 22.0F * fG;
            float headX = f3 + 7.0F * fG;
            float headY = f4 + 5.0F * fG;
            Identifier headTex = this.skinTexture != null && !this.skinTexture.getPath().isEmpty() ? this.skinTexture : DefaultSkinHelper.getTexture();
            Color colorWithAlpha = withAlpha(Color.WHITE, fJ);
            RenderSystemHelper.setShaderTexture(0, headTex);
            renderer2D.a(headTex, headX, headY, headSize, headSize, 4.0F * fG, 0.125F, 0.125F, 0.125F, 0.125F, colorWithAlpha, MatrixStackVar);
            renderer2D.a(headTex, headX, headY, headSize, headSize, 4.0F * fG, 0.625F, 0.125F, 0.125F, 0.125F, colorWithAlpha, MatrixStackVar);
            if (this.target != null && this.target.hurtTime > 0) {
               renderer2D.a(
                  headX,
                  headY,
                  headSize,
                  headSize,
                  3.0F * fG,
                  new Color(255, 70, 70, (int)(this.target.hurtTime / 10.0F * 0.5F * fJ * 155.0F)),
                  MatrixStackVar
               );
            }

            float nameX = headX + headSize + 7.0F * fG;
            MatrixStackVar.pushMatrix();
            MatrixStackVar.translate(nameX, headY);
            MatrixStackVar.scale(0.5F, 0.5F);
            FontRenderer nameFont = FontManager.b[Math.max(6, Math.min(64, Math.round(32.0F * fG)))];
            nameFont.a(this.targetName != null && !this.targetName.isBlank() ? this.targetName : "Target", 0.0, 0.0, withAlpha(Color.WHITE, fJ), MatrixStackVar);
            MatrixStackVar.popMatrix();
            MatrixStackVar.pushMatrix();
            MatrixStackVar.translate(nameX, headY + 12.0F * fG);
            MatrixStackVar.scale(0.5F, 0.5F);
            FontRenderer hpFont = FontManager.b[Math.max(6, Math.min(64, Math.round(27.0F * fG)))];
            hpFont.a(
               "HP / " + String.format(Locale.ROOT, "%.1f", Math.max(0.0F, this.targetHealth)).replace('.', ','),
               0.0,
               0.0,
               withAlpha(new Color(190, 190, 205, 240), fJ),
               MatrixStackVar
            );
            MatrixStackVar.popMatrix();
            float barX = headX;
            float barY = f4 + this.e - 9.0F * fG;
            float barW = this.d - 14.0F * fG;
            float barH = 1.3F * fG;
            float barRadius = barH / 2.0F;
            renderer2D.a(barX, barY, barW, barH, barRadius, withAlpha(new Color(14, 12, 22, 220), fJ), MatrixStackVar);
            float fMax = Math.max(0.0F, Math.min(1.0F, (float)this.healthAnimation.j()));
            if (fMax > 0.0F) {
               float filledW = Math.max(barH, barW * fMax);
               Color glowColor = new Color(95, 65, 240, 60);
               renderer2D.a(
                  barX - 1.5F * fG,
                  barY - 1.5F * fG,
                  filledW + 3.0F * fG,
                  barH + 3.0F * fG,
                  (barH + 3.0F * fG) / 2.0F,
                  withAlpha(glowColor, fJ),
                  MatrixStackVar
               );
               Color barColor = new Color(95, 65, 240);
               renderer2D.a(barX, barY, filledW, barH, barRadius, withAlpha(barColor, fJ), MatrixStackVar);
            }
         }
      }
   }

   private void bindSettings() {
      if (!this.settingsBound && ModuleRegistry.TARGET_HUD != null) {
         this.f().a(ModuleRegistry.TARGET_HUD);
         this.settingsBound = true;
      }
   }

   private void updateTarget() {
      LivingEntity LivingEntityVarCurrentTarget = HudServiceRegistry.TARGETS.currentTarget();
      if (LivingEntityVarCurrentTarget == null) {
         LivingEntityVarCurrentTarget = this.targetFromCrosshair();
      }

      if (LivingEntityVarCurrentTarget == null
         && this.a.player != null
         && (this.a.currentScreen instanceof ChatScreen || this.a.currentScreen instanceof PulseClickGuiScreen)) {
         LivingEntityVarCurrentTarget = this.a.player;
      }

      if (LivingEntityVarCurrentTarget != null) {
         this.lastTargetTime = System.currentTimeMillis();
      }

      boolean z = this.visible;
      long lifetimeMs = (long)(ModuleRegistry.TARGET_HUD.targetLifeTime().get() * 1000.0F);
      this.visible = this.target != null && System.currentTimeMillis() - this.lastTargetTime <= lifetimeMs;
      if (LivingEntityVarCurrentTarget != null) {
         if (LivingEntityVarCurrentTarget != this.target) {
            this.target = LivingEntityVarCurrentTarget;
            this.switchAnimation.d(0.0);
            this.switchAnimation.a(1.0, 0.2, Easing.h);
            this.targetName = LivingEntityVarCurrentTarget.getName().getString();
            this.skinTexture = LivingEntityVarCurrentTarget instanceof PlayerEntity
               ? this.skinFor((PlayerEntity)LivingEntityVarCurrentTarget)
               : Identifier.of("minecraft", "textures/entity/steve.png");
         }

         this.targetHealth = Math.max(0.0F, LivingEntityVarCurrentTarget.getHealth());
         this.targetMaxHealth = Math.max(1.0F, LivingEntityVarCurrentTarget.getMaxHealth());
         if (LivingEntityVarCurrentTarget instanceof PlayerEntity PlayerEntityVar) {
            this.targetName = PlayerEntityVar.getName().getString();
            this.skinTexture = this.skinFor(PlayerEntityVar);
         }
      } else {
         this.target = null;
      }

      if (this.visible && !z) {
         this.visibilityAnimation.a(1.0, 0.2, Easing.h);
      } else if (!this.visible && z) {
         this.visibilityAnimation.a(0.0, 0.2, Easing.h);
      } else if (this.visible && this.visibilityAnimation.i() < 1.0) {
         this.visibilityAnimation.a(1.0, 0.2, Easing.h);
      }

      float fMax = Math.max(0.0F, Math.min(1.0F, this.targetHealth / this.targetMaxHealth));
      if (Math.abs(this.healthAnimation.i() - fMax) > 0.01F) {
         this.healthAnimation.a(fMax, 0.15, Easing.h);
      }

      this.visibilityAnimation.a();
      this.healthAnimation.a();
      this.switchAnimation.a();
   }

   private Identifier skinFor(PlayerEntity PlayerEntityVar) {
      if (PlayerEntityVar == null) {
         return Identifier.of("minecraft", "textures/entity/steve.png");
      }

      try {
         if (PlayerEntityVar instanceof AbstractClientPlayerEntity) {
            Identifier id = ((AbstractClientPlayerEntity)PlayerEntityVar).getSkin().body().id();
            if (id != null && !id.getPath().isEmpty()) {
               return id;
            }
         }
      } catch (Throwable var3) {
      }

      return Identifier.of("minecraft", "textures/entity/steve.png");
   }

   private LivingEntity targetFromCrosshair() {
      if (this.a.player != null && this.a.world != null) {
         HitResult hitResult = this.a.crosshairTarget;
         if (hitResult != null && hitResult.getType() == Type.ENTITY && hitResult instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult)hitResult).getEntity();
            if (entity instanceof LivingEntity && entity != this.a.player && ((LivingEntity)entity).isAlive() && !entity.isRemoved()) {
               return (LivingEntity)entity;
            }
         }

         double maxDistance = 30.0;
         Vec3d start = this.a.player.getCameraPosVec(1.0F);
         Vec3d direction = this.a.player.getRotationVec(1.0F);
         Vec3d end = start.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
         Box box = this.a.player.getBoundingBox().stretch(direction.multiply(maxDistance)).expand(1.0, 1.0, 1.0);
         Entity closestEntity = null;
         double closestDistanceSq = maxDistance * maxDistance;

         for (Entity entity : this.a.world.getOtherEntities(this.a.player, box, e -> e instanceof LivingEntity && e.isAlive() && !e.isRemoved())) {
            Box entityBox = entity.getBoundingBox().expand(entity.getTargetingMargin());
            Optional<Vec3d> hit = entityBox.raycast(start, end);
            if (hit.isPresent()) {
               double distSq = start.squaredDistanceTo(hit.get());
               if (distSq < closestDistanceSq) {
                  closestEntity = entity;
                  closestDistanceSq = distSq;
               }
            }
         }

         return closestEntity instanceof LivingEntity ? (LivingEntity)closestEntity : null;
      } else {
         return null;
      }
   }

   private static Color withAlpha(Color color, float f) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)(color.getAlpha() * f))));
   }
}
