package pulse.modules.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.joml.Matrix4f;
import pulse.events.WorldRenderEvent;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.module.ModuleRegistry;
import pulse.render.system.ClientPipelines;
import pulse.settings.BooleanSetting;
import pulse.settings.ColorSetting;
import pulse.settings.SettingGroup;
import pulse.settings.SliderSetting;

@ModuleInfo(a = "Predictions", b = "Показывает траекторию полёта снарядов (жемчуг, стрелы, трезубец, зелья и др.)", c = ModuleCategory.UTILITIES)
public class Predictions extends ClientModule {
   private static final int MAX_STEPS = 220;
   private static final int MARKER_SEGMENTS = 32;
   private final SettingGroup projectileGroup = new SettingGroup("Снаряды");
   private final BooleanSetting arrows = new BooleanSetting("Стрелы", true);
   private final BooleanSetting enderPearls = new BooleanSetting("Эндер-жемчуг", true);
   private final BooleanSetting tridents = new BooleanSetting("Трезубцы", true);
   private final BooleanSetting potions = new BooleanSetting("Зелья и снежки", true);
   private final SettingGroup lineGroup = new SettingGroup("Линия");
   private final SliderSetting lineWidth = new SliderSetting("Толщина линии", 2.0F, 1.0F, 6.0F, 0.25F);
   private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
   private final ColorSetting lineColor = new ColorSetting("Цвет линии", new Color(120, 80, 255)).a(() -> !this.useClientColor.a());
   private final BooleanSetting gradient = new BooleanSetting("Градиент", true);
   private final ColorSetting gradientColor;
   private final BooleanSetting fadeOut;
   private final SliderSetting fadeStart;
   private final SettingGroup markerGroup;
   private final BooleanSetting impactMarker;
   private final SliderSetting markerSize;
   private final BooleanSetting markerPulse;
   private final SliderSetting markerPulseSpeed;
   private final ColorSetting hitColor;
   private final List<Predictions.Trajectory> trajectories = new ArrayList<>();
   private boolean anyEntityHit = false;

   public Predictions() {
      ColorSetting colorSetting = new ColorSetting("Цвет градиента", new Color(80, 180, 255));
      BooleanSetting booleanSetting = this.gradient;
      this.gradientColor = colorSetting.a(booleanSetting::a);
      this.fadeOut = new BooleanSetting("Затухание", true);
      SliderSetting sliderSetting = new SliderSetting("Начало затухания", 0.65F, 0.1F, 0.95F, 0.05F);
      BooleanSetting booleanSetting2 = this.fadeOut;
      this.fadeStart = sliderSetting.a(booleanSetting2::a);
      this.markerGroup = new SettingGroup("Метка падения");
      this.impactMarker = new BooleanSetting("Метка падения", true);
      SliderSetting sliderSetting2 = new SliderSetting("Размер метки", 0.4F, 0.1F, 1.2F, 0.05F);
      BooleanSetting booleanSetting3 = this.impactMarker;
      this.markerSize = sliderSetting2.a(booleanSetting3::a);
      BooleanSetting booleanSetting4 = new BooleanSetting("Пульсация", true);
      BooleanSetting booleanSetting5 = this.impactMarker;
      this.markerPulse = booleanSetting4.a(booleanSetting5::a);
      this.markerPulseSpeed = new SliderSetting("Скорость пульсации", 1.0F, 0.2F, 3.0F, 0.1F).a(() -> this.impactMarker.a() && this.markerPulse.a());
      ColorSetting colorSetting2 = new ColorSetting("Цвет при попадании в цель", new Color(255, 60, 60));
      BooleanSetting booleanSetting6 = this.impactMarker;
      this.hitColor = colorSetting2.a(booleanSetting6::a);
   }

   @EventHandler
   public void a(WorldRenderEvent worldRenderEvent) {
      if (c.player != null && c.world != null && c.gameRenderer != null) {
         ItemStack stack = this.currentProjectileStack();
         Predictions.ProjectileType type = Predictions.ProjectileType.from(stack);
         if (type != null && this.isEnabled(type) && this.isReady(stack, type)) {
            this.simulateAll(stack, type);
            if (!this.trajectories.isEmpty()) {
               this.render(worldRenderEvent);
            }
         } else {
            this.clearPrediction();
         }
      } else {
         this.clearPrediction();
      }
   }

   private ItemStack currentProjectileStack() {
      ItemStack main = c.player.getMainHandStack();
      if (Predictions.ProjectileType.from(main) != null) {
         return main;
      }

      ItemStack off = c.player.getOffHandStack();
      return Predictions.ProjectileType.from(off) != null ? off : ItemStack.EMPTY;
   }

   private boolean isEnabled(Predictions.ProjectileType type) {
      if (type == Predictions.ProjectileType.ENDER_PEARL) {
         return this.enderPearls.a();
      } else if (type == Predictions.ProjectileType.TRIDENT) {
         return this.tridents.a();
      } else if (type == Predictions.ProjectileType.ARROW) {
         return this.arrows.a();
      } else {
         return type != Predictions.ProjectileType.POTION
               && type != Predictions.ProjectileType.SNOWBALL
               && type != Predictions.ProjectileType.EGG
               && type != Predictions.ProjectileType.EXPERIENCE_BOTTLE
               && type != Predictions.ProjectileType.WIND_CHARGE
            ? true
            : this.potions.a();
      }
   }

   private boolean isReady(ItemStack stack, Predictions.ProjectileType type) {
      Item item = stack.getItem();
      if (item == Items.BOW || item == Items.TRIDENT) {
         return true;
      }

      if (item != Items.CROSSBOW) {
         return true;
      }

      ChargedProjectilesComponent comp = (ChargedProjectilesComponent)stack.get(DataComponentTypes.CHARGED_PROJECTILES);
      return comp != null && !comp.isEmpty();
   }

   private boolean hasMultishot(ItemStack stack) {
      try {
         for (RegistryEntry<Enchantment> entry : stack.getEnchantments().getEnchantments()) {
            if (entry.getKey().isPresent() && ((RegistryKey)entry.getKey().get()).getValue().getPath().contains("multishot")) {
               return true;
            }
         }
      } catch (Exception var4) {
      }

      return false;
   }

   private void simulateAll(ItemStack stack, Predictions.ProjectileType type) {
      this.trajectories.clear();
      this.anyEntityHit = false;
      if (stack.getItem() == Items.CROSSBOW && this.hasMultishot(stack)) {
         this.simulateSingle(stack, type, -10.0F);
         this.simulateSingle(stack, type, 0.0F);
         this.simulateSingle(stack, type, 10.0F);
      } else {
         this.simulateSingle(stack, type, 0.0F);
      }
   }

   private void simulateSingle(ItemStack stack, Predictions.ProjectileType type, float yawOffset) {
      Predictions.Trajectory t = new Predictions.Trajectory();
      Vec3d pos = c.player.getEyePos();
      Vec3d vel = this.initialVelocity(stack, type, yawOffset);
      t.path.add(pos);

      for (int i = 0; i < 220; i++) {
         Vec3d nextPos = pos.add(vel);
         BlockHitResult blockHit = c.world.raycast(new RaycastContext(pos, nextPos, ShapeType.COLLIDER, FluidHandling.NONE, c.player));
         boolean hit = false;
         if (blockHit.getType() != Type.MISS) {
            nextPos = blockHit.getPos();
            hit = true;
         }

         EntityHitResult entityHit = this.findEntityHit(pos, nextPos);
         if (entityHit != null) {
            nextPos = entityHit.getPos();
            t.hitEntity = true;
            this.anyEntityHit = true;
            hit = true;
         }

         t.path.add(nextPos);
         if (hit) {
            t.impactPoint = nextPos;
            break;
         }

         if (nextPos.y < c.world.getBottomY() - 16.0) {
            break;
         }

         pos = nextPos;
         vel = vel.multiply(type.drag).subtract(0.0, type.gravity, 0.0);
      }

      this.trajectories.add(t);
   }

   private Vec3d initialVelocity(ItemStack stack, Predictions.ProjectileType type, float yawOffset) {
      float yaw = c.player.getYaw() + yawOffset;
      float pitch = c.player.getPitch();
      float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
      float g = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));
      float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
      Vec3d dir = new Vec3d(f, g, h).normalize();
      double speed = type.baseSpeed;
      Item item = stack.getItem();
      if (item == Items.BOW) {
         float pull = 1.0F;
         if (c.player.isUsingItem()) {
            int useTime = stack.getMaxUseTime(c.player) - c.player.getItemUseTimeLeft();
            pull = useTime / 20.0F;
            pull = (pull * pull + pull * 2.0F) / 3.0F;
            if (pull > 1.0F) {
               pull = 1.0F;
            }

            if (pull < 0.1F) {
               pull = 1.0F;
            }
         }

         speed = 3.0 * pull;
      } else if (item == Items.CROSSBOW) {
         speed = 3.15;
      } else if (item == Items.TRIDENT) {
         speed = 2.5;
      }

      return dir.multiply(speed).add(c.player.getVelocity().multiply(0.35));
   }

   private EntityHitResult findEntityHit(Vec3d start, Vec3d end) {
      Entity target = null;
      Vec3d hitPos = null;
      double minDist = Double.MAX_VALUE;

      for (Entity ent : c.world.getOtherEntities(c.player, new Box(start, end).expand(1.0))) {
         if (ent != c.player && !ent.isSpectator() && ent.isAlive() && ent instanceof LivingEntity) {
            Optional<Vec3d> hit = ent.getBoundingBox().expand(0.3).raycast(start, end);
            if (hit.isPresent()) {
               double dist = start.squaredDistanceTo(hit.get());
               if (dist < minDist) {
                  minDist = dist;
                  target = ent;
                  hitPos = hit.get();
               }
            }
         }
      }

      return target == null ? null : new EntityHitResult(target, hitPos);
   }

   private void render(WorldRenderEvent event) {
      Immediate bufferSource = event.bufferSource();
      if (bufferSource != null) {
         MatrixStack stack = event.matrices();
         Vec3d camPos = c.gameRenderer.getCamera().getCameraPos();
         Matrix4f matrix = stack.peek().getPositionMatrix();
         VertexConsumer lines = bufferSource.getBuffer(ClientPipelines.OUTLINE_NO);

         for (Predictions.Trajectory t : this.trajectories) {
            for (int i = 0; i + 1 < t.path.size(); i++) {
               Vec3d p1 = t.path.get(i).subtract(camPos);
               Vec3d p2 = t.path.get(i + 1).subtract(camPos);
               float size1 = (float)i / Math.max(1, t.path.size() - 1);
               float size2 = (float)(i + 1) / Math.max(1, t.path.size() - 1);
               Color c1 = this.pathColor(size1, t.hitEntity);
               Color c2 = this.pathColor(size2, t.hitEntity);
               int argb1 = (int)(this.alpha(size1) * 255.0F) << 24 | c1.getRed() << 16 | c1.getGreen() << 8 | c1.getBlue();
               int argb2 = (int)(this.alpha(size2) * 255.0F) << 24 | c2.getRed() << 16 | c2.getGreen() << 8 | c2.getBlue();
               lines.vertex(matrix, (float)p1.x, (float)p1.y, (float)p1.z).color(argb1);
               lines.vertex(matrix, (float)p2.x, (float)p2.y, (float)p2.z).color(argb2);
               if (i > 0 && i % 3 == 0) {
                  this.renderPathParticle(lines, matrix, p1, c1, size1);
               }
            }

            if (this.impactMarker.a() && t.impactPoint != null) {
               this.renderImpactSphere(lines, matrix, camPos, t.impactPoint, t.hitEntity);
            }
         }

         bufferSource.draw();
      }
   }

   private void renderPathParticle(VertexConsumer lines, Matrix4f matrix, Vec3d pos, Color color, float progress) {
      int argb = (int)(this.alpha(progress) * 230.0F) << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
      float s = 0.05F;
      lines.vertex(matrix, (float)pos.x - s, (float)pos.y, (float)pos.z).color(argb);
      lines.vertex(matrix, (float)pos.x + s, (float)pos.y, (float)pos.z).color(argb);
      lines.vertex(matrix, (float)pos.x, (float)pos.y - s, (float)pos.z).color(argb);
      lines.vertex(matrix, (float)pos.x, (float)pos.y + s, (float)pos.z).color(argb);
      lines.vertex(matrix, (float)pos.x, (float)pos.y, (float)pos.z - s).color(argb);
      lines.vertex(matrix, (float)pos.x, (float)pos.y, (float)pos.z + s).color(argb);
   }

   private void renderImpactSphere(VertexConsumer lines, Matrix4f matrix, Vec3d camPos, Vec3d point, boolean hitEnt) {
      Color colorSphere = hitEnt ? this.hitColor.a() : this.pathColor(1.0F, false);
      Color colorCross = hitEnt ? this.hitColor.a() : new Color(80, 220, 100);
      float radius = this.markerSize.a()
         * (this.markerPulse.a() ? 1.0F + 0.15F * (float)Math.sin(System.currentTimeMillis() * 0.006 * this.markerPulseSpeed.a()) : 1.0F);
      Vec3d center = point.subtract(camPos);
      int argbSphere = -268435456 | colorSphere.getRed() << 16 | colorSphere.getGreen() << 8 | colorSphere.getBlue();
      int argbCross = 0xFF000000 | colorCross.getRed() << 16 | colorCross.getGreen() << 8 | colorCross.getBlue();

      for (int i = 0; i < 32; i++) {
         double angle1 = (Math.PI * 2) * i / 32.0;
         double angle2 = (Math.PI * 2) * (i + 1) / 32.0;
         Vec3d h1 = center.add(Math.cos(angle1) * radius, 0.0, Math.sin(angle1) * radius);
         Vec3d h2 = center.add(Math.cos(angle2) * radius, 0.0, Math.sin(angle2) * radius);
         lines.vertex(matrix, (float)h1.x, (float)h1.y, (float)h1.z).color(argbSphere);
         lines.vertex(matrix, (float)h2.x, (float)h2.y, (float)h2.z).color(argbSphere);
         Vec3d v1x = center.add(Math.cos(angle1) * radius, Math.sin(angle1) * radius, 0.0);
         Vec3d v2x = center.add(Math.cos(angle2) * radius, Math.sin(angle2) * radius, 0.0);
         lines.vertex(matrix, (float)v1x.x, (float)v1x.y, (float)v1x.z).color(argbSphere);
         lines.vertex(matrix, (float)v2x.x, (float)v2x.y, (float)v2x.z).color(argbSphere);
         Vec3d v1z = center.add(0.0, Math.sin(angle1) * radius, Math.cos(angle1) * radius);
         Vec3d v2z = center.add(0.0, Math.sin(angle2) * radius, Math.cos(angle2) * radius);
         lines.vertex(matrix, (float)v1z.x, (float)v1z.y, (float)v1z.z).color(argbSphere);
         lines.vertex(matrix, (float)v2z.x, (float)v2z.y, (float)v2z.z).color(argbSphere);
      }

      float cr = radius * 0.45F;
      lines.vertex(matrix, (float)center.x - cr, (float)center.y, (float)center.z).color(argbCross);
      lines.vertex(matrix, (float)center.x + cr, (float)center.y, (float)center.z).color(argbCross);
      lines.vertex(matrix, (float)center.x, (float)center.y - cr, (float)center.z).color(argbCross);
      lines.vertex(matrix, (float)center.x, (float)center.y + cr, (float)center.z).color(argbCross);
      lines.vertex(matrix, (float)center.x, (float)center.y, (float)center.z - cr).color(argbCross);
      lines.vertex(matrix, (float)center.x, (float)center.y, (float)center.z + cr).color(argbCross);
   }

   private Color pathColor(float f, boolean hitEnt) {
      if (hitEnt) {
         return this.hitColor.a();
      }

      Color colorN = this.useClientColor.a() ? ModuleRegistry.CLIENT_COLOR.n() : this.lineColor.a();
      if (!this.gradient.a()) {
         return colorN;
      }

      Color colorA = this.gradientColor.a();
      return new Color(lerp(colorN.getRed(), colorA.getRed(), f), lerp(colorN.getGreen(), colorA.getGreen(), f), lerp(colorN.getBlue(), colorA.getBlue(), f));
   }

   private float alpha(float f) {
      return this.fadeOut.a() && !(f <= this.fadeStart.a()) ? Math.max(0.0F, 1.0F - (f - this.fadeStart.a()) / (1.0F - this.fadeStart.a())) : 1.0F;
   }

   private void clearPrediction() {
      this.trajectories.clear();
      this.anyEntityHit = false;
   }

   private static int lerp(int i, int i2, float f) {
      return Math.round(i + (i2 - i) * Math.max(0.0F, Math.min(1.0F, f)));
   }

   private enum ProjectileType {
      ENDER_PEARL(1.5, 0.99, 0.03),
      TRIDENT(2.5, 0.99, 0.05),
      ARROW(3.0, 0.99, 0.05),
      POTION(0.5, 0.99, 0.05),
      SNOWBALL(1.5, 0.99, 0.03),
      EGG(1.5, 0.99, 0.03),
      EXPERIENCE_BOTTLE(0.7, 0.99, 0.07),
      WIND_CHARGE(1.5, 1.0, 0.0);

      private final double baseSpeed;
      private final double drag;
      private final double gravity;

      ProjectileType(double d, double d2, double d3) {
         this.baseSpeed = d;
         this.drag = d2;
         this.gravity = d3;
      }

      private static Predictions.ProjectileType from(ItemStack ItemStackVar) {
         if (ItemStackVar != null && !ItemStackVar.isEmpty()) {
            Item item = ItemStackVar.getItem();
            if (item == Items.ENDER_PEARL) {
               return ENDER_PEARL;
            } else if (item == Items.TRIDENT) {
               return TRIDENT;
            } else if (item == Items.BOW || item == Items.CROSSBOW) {
               return ARROW;
            } else if (item == Items.SPLASH_POTION || item == Items.LINGERING_POTION) {
               return POTION;
            } else if (item == Items.SNOWBALL) {
               return SNOWBALL;
            } else if (item == Items.EGG) {
               return EGG;
            } else if (item == Items.EXPERIENCE_BOTTLE) {
               return EXPERIENCE_BOTTLE;
            } else {
               return item == Items.WIND_CHARGE ? WIND_CHARGE : null;
            }
         } else {
            return null;
         }
      }
   }

   private static class Trajectory {
      final List<Vec3d> path = new ArrayList<>();
      Vec3d impactPoint = null;
      boolean hitEntity = false;
   }
}
