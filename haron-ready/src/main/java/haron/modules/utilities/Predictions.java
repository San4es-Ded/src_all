package haron.modules.utilities;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.utilities.ProjectilePhysicsProfile;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;

@ModuleInfo(a="Predictions", b="Predicts projectile trajectories.", c=ModuleCategory.UTILITIES)
public class Predictions
extends HaronModule {
    private static final int MAX_STEPS = 220;
    private static final int MARKER_SEGMENTS = 48;
    private final ColorSetting gradientColor;
    private final BooleanSetting fadeOut;
    private final NumberSetting fadeStart;
    private final SettingGroup markerGroup;
    private final BooleanSetting impactMarker;
    private final NumberSetting markerSize;
    private final BooleanSetting markerHaron;
    private final NumberSetting markerHaronSpeed;
    private final ColorSetting hitColor;
    private Vec3d impactPoint;
    private boolean hitEntity;
    private final List<Vec3d> path = new ArrayList<Vec3d>();
    private final SettingGroup projectileGroup = new SettingGroup("Снаряды");
    private final BooleanSetting arrows = new BooleanSetting("Стрелы", true);
    private final BooleanSetting enderPearls = new BooleanSetting("Эндер-жемчуг", true);
    private final BooleanSetting tridents = new BooleanSetting("Трезубцы", true);
    private final BooleanSetting potions = new BooleanSetting("Зелья", true);
    private final SettingGroup lineGroup = new SettingGroup("Линия");
    private final NumberSetting lineWidth = new NumberSetting("Толщина линии", 2.0f, 1.0f, 6.0f, 0.25f);
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting lineColor = new ColorSetting("Цвет линии", new Color(120, 80, 255)).a(() -> {
        return !this.useClientColor.a();
    });
    private final BooleanSetting gradient = new BooleanSetting("Градиент", true);

    private void render(WorldRenderPostEvent kvprd92) {
        MatrixStack matrixStack = kvprd92.a();
        Vec3d vec3d = Predictions.c.gameRenderer.getCamera().getPos();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.lineWidth((float)this.lineWidth.a());
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 1; i < this.path.size(); ++i) {
            Vec3d vec3d2 = this.path.get(i);
            float f = this.path.size() <= 1 ? 0.0f : (float)i / (float)(this.path.size() - 1);
            Predictions.vertex(bufferBuilder, matrix4f, vec3d2.subtract(vec3d), this.pathColor(f), this.alpha(f));
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
        if (this.impactMarker.a() && this.impactPoint != null) {
            this.renderImpactMarker(matrix4f, vec3d);
        }
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private ItemStack currentProjectileStack() {
        ItemStack itemStack = Predictions.c.player.getMainHandStack();
        if (ProjectilePhysicsProfile.from(itemStack) != null) {
            return itemStack;
        }
        ItemStack itemStack2 = Predictions.c.player.getOffHandStack();
        return ProjectilePhysicsProfile.from(itemStack2) != null ? itemStack2 : ItemStack.EMPTY;
    }

    private static int lerp(int n, int n2, float f) {
        return Math.round((float)n + (float)(n2 - n) * Math.max(0.0f, Math.min(1.0f, f)));
    }

    private boolean isReady(ItemStack itemStack, ProjectilePhysicsProfile ncpsno2) {
        Item item = itemStack.getItem();
        if (item == Items.BOW || item == Items.TRIDENT) {
            return true;
        }
        if (item != Items.CROSSBOW) {
            return ncpsno2 == ProjectilePhysicsProfile.ENDER_PEARL || ncpsno2 == ProjectilePhysicsProfile.POTION;
        }
        ChargedProjectilesComponent chargedProjectilesComponent = (ChargedProjectilesComponent)itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
        return chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty();
    }

    private void simulate(float f, ItemStack itemStack, ProjectilePhysicsProfile ncpsno2) {
        this.path.clear();
        this.impactPoint = null;
        this.hitEntity = false;
        Vec3d vec3d = Predictions.c.player.getCameraPosVec(f);
        Vec3d vec3d2 = this.initialVelocity(itemStack, ncpsno2);
        this.path.add(vec3d);
        for (int i = 0; i < 220; ++i) {
            EntityHitResult entityHitResult;
            Vec3d vec3d3 = vec3d.add(vec3d2);
            BlockHitResult blockHitResult = Predictions.c.world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)Predictions.c.player));
            boolean bl = false;
            if (blockHitResult.getType() != HitResult.Type.MISS) {
                vec3d3 = blockHitResult.getPos();
                bl = true;
            }
            if ((entityHitResult = this.findEntityHit(vec3d, vec3d3)) != null) {
                vec3d3 = entityHitResult.getPos();
                this.hitEntity = true;
                bl = true;
            }
            this.path.add(vec3d3);
            if (bl) {
                this.impactPoint = vec3d3;
                return;
            }
            if (vec3d3.y < (double)Predictions.c.world.getBottomY() - 16.0) {
                return;
            }
            vec3d = vec3d3;
            vec3d2 = vec3d2.multiply(ncpsno2.drag).subtract(0.0, ncpsno2.gravity, 0.0);
        }
    }

    private Color pathColor(float f) {
        Color color;
        Color color2 = color = this.useClientColor.a() ? ModuleManager.CLIENT_COLOR.n() : this.lineColor.a();
        if (!this.gradient.a()) {
            return color;
        }
        Color color3 = this.gradientColor.a();
        return new Color(Predictions.lerp(color.getRed(), color3.getRed(), f), Predictions.lerp(color.getGreen(), color3.getGreen(), f), Predictions.lerp(color.getBlue(), color3.getBlue(), f));
    }

    private static void vertex(BufferBuilder bufferBuilder, Matrix4f matrix4f, Vec3d vec3d, Color color, float f) {
        int n = Math.max(0, Math.min(255, (int)(f * 255.0f)));
        bufferBuilder.vertex(matrix4f, (float)vec3d.x, (float)vec3d.y, (float)vec3d.z).color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    private Vec3d lookDirection() {
        double d = (double)Predictions.c.player.getYaw() * (Math.PI / 180);
        double d2 = (double)Predictions.c.player.getPitch() * (Math.PI / 180);
        double d3 = Math.cos(d2);
        return new Vec3d(-Math.sin(d) * d3, -Math.sin(d2), Math.cos(d) * d3).normalize();
    }

    private void renderImpactMarker(Matrix4f matrix4f, Vec3d vec3d) {
        Color color = this.hitEntity ? this.hitColor.a() : this.pathColor(1.0f);
        float f = this.markerSize.a() * (this.markerHaron.a() ? 1.0f + 0.18f * (float)Math.sin((double)System.currentTimeMillis() * 0.006 * (double)this.markerHaronSpeed.a()) : 1.0f);
        Vec3d vec3d2 = this.impactPoint.subtract(vec3d);
        RenderSystem.lineWidth((float)2.0f);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 48; ++i) {
            double d = Math.PI * 2 * (double)i / 48.0;
            double d2 = Math.PI * 2 * (double)(i + 1) / 48.0;
            Predictions.vertex(bufferBuilder, matrix4f, vec3d2.add(Math.cos(d) * (double)f, 0.01, Math.sin(d) * (double)f), color, 0.9f);
            Predictions.vertex(bufferBuilder, matrix4f, vec3d2.add(Math.cos(d2) * (double)f, 0.01, Math.sin(d2) * (double)f), color, 0.9f);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
    }

    private Vec3d initialVelocity(ItemStack itemStack, ProjectilePhysicsProfile ncpsno2) {
        Vec3d vec3d = this.lookDirection();
        double d = ncpsno2.baseSpeed;
        Item item = itemStack.getItem();
        if (item == Items.BOW) {
            int n = Predictions.c.player.isUsingItem() ? itemStack.getMaxUseTime((LivingEntity)Predictions.c.player) - Predictions.c.player.getItemUseTimeLeft() : itemStack.getMaxUseTime((LivingEntity)Predictions.c.player);
            d = 3.0 * (double)Math.min(1.0f, Math.max(0.05f, ((float)n / 20.0f * (float)n / 20.0f + (float)n / 20.0f * 2.0f) / 3.0f));
        } else if (item == Items.TRIDENT) {
            int n = Predictions.c.player.isUsingItem() ? itemStack.getMaxUseTime((LivingEntity)Predictions.c.player) - Predictions.c.player.getItemUseTimeLeft() : itemStack.getMaxUseTime((LivingEntity)Predictions.c.player);
            d = 2.5 * (double)Math.min(1.0f, Math.max(0.1f, (float)n / 10.0f));
        }
        return vec3d.multiply(d).add(Predictions.c.player.getVelocity().multiply(0.35));
    }

    private void clearPrediction() {
        int n = 921;
        this.path.clear();
        this.impactPoint = null;
        this.hitEntity = false;
    }

    private EntityHitResult findEntityHit(Vec3d vec3d, Vec3d vec3d2) {
        Entity entity = null;
        Vec3d vec3d3 = null;
        double d = Double.MAX_VALUE;
        for (Entity entity2 : Predictions.c.world.getOtherEntities((Entity)Predictions.c.player, new Box(vec3d, vec3d2).expand(1.0))) {
            double d2;
            Optional optional;
            if (entity2 == Predictions.c.player || entity2.isSpectator() || !entity2.isAlive() || !(entity2 instanceof LivingEntity) || !(optional = entity2.getBoundingBox().expand(0.3).raycast(vec3d, vec3d2)).isPresent() || !((d2 = vec3d.squaredDistanceTo((Vec3d)optional.get())) < d)) continue;
            d = d2;
            entity = entity2;
            vec3d3 = (Vec3d)optional.get();
        }
        if (entity == null) {
            return null;
        }
        return new EntityHitResult(entity, vec3d3);
    }

    public Predictions() {
        ColorSetting f40tf12 = new ColorSetting("Цвет градиента", new Color(80, 180, 255));
        BooleanSetting xcv91t2 = this.gradient;
        Objects.requireNonNull(xcv91t2);
        this.gradientColor = f40tf12.a(xcv91t2::a);
        this.fadeOut = new BooleanSetting("Затухание", true);
        NumberSetting by6erl2 = new NumberSetting("Начало затухания", 0.65f, 0.1f, 0.95f, 0.05f);
        BooleanSetting xcv91t3 = this.fadeOut;
        Objects.requireNonNull(xcv91t3);
        this.fadeStart = by6erl2.a(xcv91t3::a);
        this.markerGroup = new SettingGroup("Маркер попадания");
        this.impactMarker = new BooleanSetting("Маркер попадания", true);
        NumberSetting by6erl3 = new NumberSetting("Размер маркера", 0.35f, 0.1f, 1.2f, 0.05f);
        BooleanSetting xcv91t4 = this.impactMarker;
        Objects.requireNonNull(xcv91t4);
        this.markerSize = by6erl3.a(xcv91t4::a);
        BooleanSetting xcv91t5 = new BooleanSetting("Харон", true);
        BooleanSetting xcv91t6 = this.impactMarker;
        Objects.requireNonNull(xcv91t6);
        this.markerHaron = xcv91t5.a(xcv91t6::a);
        this.markerHaronSpeed = new NumberSetting("Скорость Харона", 1.0f, 0.2f, 3.0f, 0.1f).a(() -> this.impactMarker.a() && this.markerHaron.a());
        ColorSetting f40tf13 = new ColorSetting("Цвет попадания", new Color(255, 85, 110));
        BooleanSetting xcv91t7 = this.impactMarker;
        Objects.requireNonNull(xcv91t7);
        this.hitColor = f40tf13.a(xcv91t7::a);
    }

    @EventHandler
    public void a(WorldRenderPostEvent kvprd92) {
        if (Predictions.c.player == null || Predictions.c.world == null || Predictions.c.gameRenderer == null) {
            this.clearPrediction();
            return;
        }
        ItemStack itemStack = this.currentProjectileStack();
        ProjectilePhysicsProfile ncpsno2 = ProjectilePhysicsProfile.from(itemStack);
        if (ncpsno2 == null || !this.isEnabled(ncpsno2) || !this.isReady(itemStack, ncpsno2)) {
            this.clearPrediction();
            return;
        }
        this.simulate(kvprd92.b(), itemStack, ncpsno2);
        if (this.path.size() > 1) {
            this.render(kvprd92);
        }
    }

    private boolean isEnabled(ProjectilePhysicsProfile ncpsno2) {
        switch (ncpsno2.ordinal()) {
            case 0: {
                return this.enderPearls.a();
            }
            case 1: {
                return this.tridents.a();
            }
            case 2: {
                return this.arrows.a();
            }
            case 3: {
                return this.potions.a();
            }
        }
        throw new MatchException(null, null);
    }

    private float alpha(float f) {
        if (!this.fadeOut.a() || f <= this.fadeStart.a()) {
            return 1.0f;
        }
        return Math.max(0.0f, 1.0f - (f - this.fadeStart.a()) / (1.0f - this.fadeStart.a()));
    }
}

