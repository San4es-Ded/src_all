package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext.ShapeType;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.render.EventRender2D;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.font.Font;
import wtf.wyvern.core.font.Fonts;
import wtf.wyvern.core.performance.render.RenderFrameCache;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.wyvern.utility.game.player.RaytracingUtil;
import wtf.wyvern.utility.math.ProjectionUtil;
import wtf.wyvern.render.display.base.BorderRadius;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "Predictions",
   category = Category.RENDER,
   description = "Показывает куда упадет предмет"
)
public final class Predictions extends Module {
   private final List<Point> points = new ArrayList();
   private final List<Entity> projectiles = new ArrayList<>();
   private final List<Box> collisionBoxes = new ArrayList<>();
   private final java.util.Map<Integer, CachedTrajectory> trajectoryCache = new java.util.HashMap<>();
   public static Predictions INSTANCE = new Predictions();

   private Predictions() {
   }

   @FastNative
   @Override
   public void onDisable() {
      this.trajectoryCache.clear();
      super.onDisable();
   }

   @EventTarget
   public void onDraw(EventRender2D e) {
      Iterator var2 = this.points.iterator();

      while(var2.hasNext()) {
         Point point = (Point)var2.next();
         Vec3d vec3d = ProjectionUtil.worldSpaceToScreenSpace(point.pos);
         int ticks = point.ticks;
         if (ProjectionUtil.canSee(point.pos)) {
            Font font = Fonts.REGULAR.getFont(7.0F);
            String text = formatSeconds(ticks) + " сек";
            float textWidth = font.width(text);
            float padding = 2.0F;
            float centerX = (float)vec3d.getX();
            float centerY = (float)vec3d.getY();
            float totalHeight = font.height();
            float rectX = centerX - textWidth / 2.0F;
            float rectY = centerY - totalHeight / 2.0F;
            float textY = rectY + 5.0F;
            DrawUtil.drawRoundedRect(e.getContext().getMatrices(), rectX - 8.0F, textY - 2.0F, textWidth + 16.0F, totalHeight + 5.0F, BorderRadius.ZERO, new ColorRGBA(0, 0, 0, 120));
            e.getContext().getMatrices().push();
            e.getContext().getMatrices().translate(rectX - 6.0F, textY - 1.0F, 0.0F);
            e.getContext().getMatrices().scale(0.5F, 0.5F, 1.0F);
            e.getContext().drawItem(point.stack(), 0, 0);
            e.getContext().getMatrices().scale(1.0F, 1.0F, 1.0F);
            e.getContext().getMatrices().translate(-(rectX - 7.0F), -(textY - 1.0F), 0.0F);
            e.getContext().getMatrices().pop();
            e.getContext().drawText(font, text.replace(",", "."), rectX + 5.0F, textY + 0.5F, ColorRGBA.WHITE);
         }
      }

   }

   @EventTarget
   public void onWorldRender(EventRender3D e) {
      this.points.clear();
      this.collectProjectiles();
      if (this.projectiles.isEmpty()) {
         if (!this.trajectoryCache.isEmpty()) {
            this.trajectoryCache.clear();
         }
         return;
      }

      // Drop cached paths of projectiles that are no longer tracked.
      if (!this.trajectoryCache.isEmpty()) {
         this.trajectoryCache.keySet().removeIf(id -> {
            for (Entity entity : this.projectiles) {
               if (entity.getId() == id) {
                  return false;
               }
            }
            return true;
         });
      }

      // Position and velocity only change once per game tick, so the simulated path is
      // identical for every frame inside a tick. Simulate once per tick per projectile
      // and just redraw the cached segments each frame - the picture stays the same,
      // but hundreds of raycasts per frame are gone.
      boolean boxesBuilt = false;
      for (Entity entity : this.projectiles) {
         Vec3d pos = entity.getPos();
         Vec3d motion = entity.getVelocity();
         CachedTrajectory cached = this.trajectoryCache.get(entity.getId());
         if (cached == null || !cached.pos.equals(pos) || !cached.motion.equals(motion)) {
            if (!boxesBuilt) {
               this.collectCollisionBoxes();
               boxesBuilt = true;
            }
            cached = this.simulate(entity, pos, motion);
            this.trajectoryCache.put(entity.getId(), cached);
         }

         for (Segment segment : cached.segments) {
            int i = segment.index;
            Render3DUtil.drawLine(segment.from, segment.to, Wyvern.getInstance().getThemeManager().getClientColor(i).mulAlpha(MathHelper.clamp((float)i / 25.0F, 0.0F, 1.0F)).getRGB(), 2.0F, false);

            int glowColor = Wyvern.getInstance().getThemeManager().getClientColor(i).mulAlpha(MathHelper.clamp((float)i / 50.0F, 0.0F, 0.3F)).getRGB();
            Render3DUtil.drawLine(segment.from, segment.to, glowColor, 4.5F, false);
            Render3DUtil.drawLine(segment.from, segment.to, glowColor, 7.0F, false);
         }

         if (cached.point != null) {
            this.points.add(cached.point);
         }
      }
   }

   @FastNative
   private CachedTrajectory simulate(Entity entity, Vec3d startPos, Vec3d startMotion) {
      CachedTrajectory cached = new CachedTrajectory(startPos, startMotion);
      Vec3d motion = startMotion;
      Vec3d pos = startPos;
      int ticks = 0;

      for(int i = 0; i < 300; ++i) {
         Vec3d prevPos = pos;
         pos = pos.add(motion);
         motion = this.calculateMotion(entity, prevPos, motion);
         HitResult result = RaytracingUtil.raycast(prevPos, pos, ShapeType.COLLIDER, entity);
         if (!result.getType().equals(Type.MISS)) {
            pos = result.getPos();
         }

         cached.segments.add(new Segment(prevPos, pos, i));

         boolean inEntity = false;
         for (Box collisionBox : this.collisionBoxes) {
            if (collisionBox.intersects(prevPos, pos)) {
               inEntity = true;
               break;
            }
         }
         if (result.getType().equals(Type.BLOCK) || pos.y < -128.0D || inEntity || result.getType().equals(Type.ENTITY)) {
            cached.point = this.landingPoint(entity, pos, ticks);
            break;
         }

         ++ticks;
      }

      return cached;
   }

   @FastNative
   private void collectProjectiles() {
      this.projectiles.clear();
      for (Entity entity : RenderFrameCache.entities()) {
         if ((entity instanceof PersistentProjectileEntity || entity instanceof ThrownItemEntity || entity instanceof ItemEntity)
                 && !this.visible(entity)) {
            this.projectiles.add(entity);
         }
      }
   }

   @FastNative
   private void collectCollisionBoxes() {
      this.collisionBoxes.clear();
      for (Entity entity : RenderFrameCache.entities()) {
         if (entity instanceof LivingEntity living && living != mc.player && living.isAlive()) {
            this.collisionBoxes.add(living.getBoundingBox().expand(0.25D));
         }
      }
   }

   @FastNative
   public Vec3d calculateMotion(Entity entity, Vec3d prevPos, Vec3d motion) {
      boolean isInWater = ((ClientWorld)mc.world).getBlockState(BlockPos.ofFloored(prevPos)).getFluidState().isIn(FluidTags.WATER);

      float multiply;
      if (entity instanceof TridentEntity) {
         multiply = 0.99F;
      } else if (entity instanceof PersistentProjectileEntity) {
         multiply = isInWater ? 0.6F : 0.99F;
      } else {
         multiply = isInWater ? 0.8F : 0.99F;
      }

      return motion.multiply((double)multiply).add(0.0D, -entity.getFinalGravity(), 0.0D);
   }

   @FastNative
   private static String formatSeconds(int ticks) {
      int tenths = Math.max(0, ticks) / 2;
      return (tenths / 10) + "." + (tenths % 10);
   }

   @FastNative
   private Point landingPoint(Entity entity, Vec3d pos, int ticks) {
      if (entity instanceof ItemEntity item) {
         return new Point(item.getStack(), pos, ticks);
      } else if (entity instanceof ThrownItemEntity thrown) {
         return new Point(thrown.getStack(), pos, ticks);
      } else if (entity instanceof PersistentProjectileEntity persistent) {
         return new Point(persistent.getItemStack(), pos, ticks);
      }

      return null;
   }

   @FastNative
   private boolean visible(Entity entity) {
      boolean posChange = entity.getX() == entity.prevX && entity.getY() == entity.prevY && entity.getZ() == entity.prevZ;
      boolean itemEntityCheck = entity instanceof ItemEntity && (entity.isOnGround() || PlayerIntersectionUtil.isBoxInBlock(entity.getBoundingBox().expand(2.0D), Blocks.WATER));
      return posChange || itemEntityCheck;
   }

   private static record Segment(Vec3d from, Vec3d to, int index) {
   }

   private static final class CachedTrajectory {
      final Vec3d pos;
      final Vec3d motion;
      final List<Segment> segments = new ArrayList<>();
      Point point;

      CachedTrajectory(Vec3d pos, Vec3d motion) {
         this.pos = pos;
         this.motion = motion;
      }
   }

   private static record Point(ItemStack stack, Vec3d pos, int ticks) {
      private Point(ItemStack stack, Vec3d pos, int ticks) {
         this.stack = stack;
         this.pos = pos;
         this.ticks = ticks;
      }

      public ItemStack stack() {
         return this.stack;
      }

      public Vec3d pos() {
         return this.pos;
      }

      public int ticks() {
         return this.ticks;
      }
   }
}
