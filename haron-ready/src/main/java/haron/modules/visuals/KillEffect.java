package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.AttackTargetEvent;
import haron.events.WorldRenderPostEvent;
import haron.events.ClientTickEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.visuals.killeffect.KillSoundScheduler;
import haron.modules.visuals.killeffect.KillTracker;
import haron.modules.visuals.killeffect.KillParticleBurstEmitter;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(a="Kill Effect", b="Эффекты при убийстве", c=ModuleCategory.VISUALS)
public class KillEffect
extends HaronModule {
    private final BooleanSetting particles = new BooleanSetting("Частицы", true);
    private final NumberSetting particleCount = new NumberSetting("Кол-во частиц", 20.0f, 5.0f, 50.0f, 1.0f);
    private final BooleanSetting scanEffect = new BooleanSetting("Волна", true);
    private final NumberSetting scanSpeed = new NumberSetting("Скорость волны", 1.5f, 0.5f, 4.0f, 0.1f);
    private final BooleanSetting soundEnabled = new BooleanSetting("Звук", true);
    private final NumberSetting soundVolume = new NumberSetting("Громкость", 1.0f, 0.1f, 2.0f, 0.1f);
    private final KillTracker tracker = new KillTracker(2500L, this::onKill);
    private final KillParticleBurstEmitter particles2 = new KillParticleBurstEmitter();
    private final KillSoundScheduler sounds = new KillSoundScheduler();
    private float scanAlpha = 0.0f;
    private float scanRadius = 0.0f;
    private Vec3d scanPos = null;

    @EventHandler
    public void onTick(ClientTickEvent q8krcw2) {
        this.tracker.tick();
        this.sounds.tick();
        if (this.scanAlpha > 0.0f) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            this.scanRadius += this.scanSpeed.a() * 0.15f;
            this.scanAlpha -= 0.015f;
            if (this.scanAlpha <= 0.0f) {
                this.scanAlpha = 0.0f;
                this.scanPos = null;
            }
        }
    }

    @EventHandler
    public void onAttack(AttackTargetEvent dt813s2) {
        Entity entity = dt813s2.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            this.tracker.remember(livingEntity, false);
        }
    }

    private void onKill(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null || minecraftClient.world == null) {
            return;
        }
        float f = minecraftClient.getRenderTickCounter().getTickDelta(false);
        Vec3d vec3d = livingEntity.getLerpedPos(f).add(0.0, (double)livingEntity.getHeight() / 2.0, 0.0);
        Color color = this.getAccentColor();
        if (this.particles.a()) {
            this.particles2.spawnBurst(vec3d, (int)this.particleCount.a(), 0, 0.8f, (int)this.particleCount.a(), 0.3f);
        }
        if (this.scanEffect.a()) {
            this.scanPos = vec3d;
            this.scanAlpha = 1.0f;
            this.scanRadius = 0.0f;
        }
        if (this.soundEnabled.a()) {
            this.sounds.schedule(null, this.soundVolume.a(), 0L);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void renderScan(WorldRenderPostEvent kvprd92, Vec3d vec3d, float f, float f2) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Vec3d vec3d2 = minecraftClient.gameRenderer.getCamera().getPos();
        Color color = this.getAccentColor();
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        int n4 = (int)(f * 180.0f);
        MatrixStack matrixStack = kvprd92.a();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        RenderSystem.lineWidth((float)2.5f);
        try {
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 360; i += 3) {
                double d = Math.toRadians(i);
                float f3 = (float)(vec3d.x + Math.cos(d) * (double)f2 - vec3d2.x);
                float f4 = (float)(vec3d.z + Math.sin(d) * (double)f2 - vec3d2.z);
                float f5 = (float)(vec3d.y - vec3d2.y);
                bufferBuilder.vertex(matrix4f, f3, f5, f4).color(n, n2, n3, n4);
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
        catch (Exception exception) {
        }
        finally {
            GL11.glDisable((int)2848);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private Color getAccentColor() {
        return ModuleManager.CLIENT_COLOR.n();
    }

    @EventHandler
    public void onWorldRender(WorldRenderPostEvent kvprd92) {
        if (this.scanAlpha > 0.0f && this.scanPos != null) {
            this.renderScan(kvprd92, this.scanPos, this.scanAlpha, this.scanRadius);
        }
    }

    @Override
    public void f() {
        super.f();
        this.tracker.clear();
        this.particles2.clear();
        this.sounds.clear();
        this.scanAlpha = 0.0f;
        this.scanPos = null;
    }
}

