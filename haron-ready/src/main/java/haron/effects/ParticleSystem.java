package haron.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.effects.ParticleRenderData;
import haron.effects.Particle;
import haron.events.WorldRenderPostEvent;
import haron.events.RenderTickEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;

@HudServiceInfo(enabledByDefault=true)
public class ParticleSystem
extends HudService
implements MinecraftClientAccess {
    private final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
    private long lastTick = -1L;

    public int count() {
        return this.particles.size();
    }

    private void renderParticles(MatrixStack matrixStack, Frustum frustum) {
        if (this.particles.isEmpty()) {
            return;
        }
        Map<Identifier, List<ParticleRenderData>> hashMap = new HashMap<>();
        for (Particle object : this.particles) {
            ParticleRenderData mchc6i3 = object.buildRenderData(frustum);
            if (mchc6i3 == null) continue;
            hashMap.computeIfAbsent(object.texture, identifier -> {
                return new ArrayList();
            }).add(mchc6i3);
        }
        Vec3d vec3d = ParticleSystem.c.gameRenderer.getCamera().getPos();
        for (Map.Entry<Identifier, List<ParticleRenderData>> entry : hashMap.entrySet()) {
            Identifier identifier2 = entry.getKey();
            List<ParticleRenderData> list = entry.getValue();
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier2);
            RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc((int)515);
            RenderSystem.disableCull();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.enableBlend();
            boolean bl = list.stream().anyMatch(mchc6i2 -> {
                return BooleanCoercion.from(mchc6i2.averageBrightness >= 0.2f ? 0 : 1);
            });
            boolean bl2 = list.stream().anyMatch(mchc6i2 -> {
                return BooleanCoercion.from(mchc6i2.averageBrightness < 0.2f ? 0 : 1);
            });
            if (bl && bl2) {
                this.drawBatch(matrixStack, (ParticleRenderData[])list.stream().filter(mchc6i2 -> {
                    int n = mchc6i2.averageBrightness >= 0.2f ? 0 : 1;
                    return BooleanCoercion.from(n);
                }).toArray(n -> {
                    return new ParticleRenderData[n];
                }), vec3d, 770, 771);
                this.drawBatch(matrixStack, (ParticleRenderData[])list.stream().filter(mchc6i2 -> {
                    int n = 718;
                    return BooleanCoercion.from(mchc6i2.averageBrightness < 0.2f ? 0 : 1);
                }).toArray(n -> {
                    return new ParticleRenderData[n];
                }), vec3d, 770, 1);
            } else if (bl) {
                RenderSystem.blendFunc((int)770, (int)771);
                this.drawBatch(matrixStack, list.toArray(new ParticleRenderData[0]), vec3d, 770, 771);
            } else {
                RenderSystem.blendFunc((int)770, (int)1);
                this.drawBatch(matrixStack, list.toArray(new ParticleRenderData[0]), vec3d, 770, 1);
            }
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.depthFunc((int)515);
            RenderSystem.enableCull();
        }
    }

    private void drawBatch(MatrixStack matrixStack, ParticleRenderData[] mchc6iArray, Vec3d vec3d, int n, int n2) {
        if (mchc6iArray.length != 0) {
            RenderSystem.blendFunc((int)n, (int)n2);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (ParticleRenderData mchc6i2 : mchc6iArray) {
                matrixStack.push();
                matrixStack.translate(-vec3d.x + mchc6i2.position.x, -vec3d.y + mchc6i2.position.y, -vec3d.z + mchc6i2.position.z);
                matrixStack.multiply(ParticleSystem.c.gameRenderer.getCamera().getRotation());
                matrixStack.multiply(new Quaternionf().rotationZ((float)Math.toRadians(mchc6i2.rotationDegrees)));
                matrixStack.scale(mchc6i2.size, mchc6i2.size, mchc6i2.size);
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                bufferBuilder.vertex(matrix4f, -0.5f, -0.5f, 0.0f).texture(0.0f, 0.0f).color(mchc6i2.red, mchc6i2.green, mchc6i2.blue, mchc6i2.alpha);
                bufferBuilder.vertex(matrix4f, 0.5f, -0.5f, 0.0f).texture(1.0f, 0.0f).color(mchc6i2.red, mchc6i2.green, mchc6i2.blue, mchc6i2.alpha);
                bufferBuilder.vertex(matrix4f, 0.5f, 0.5f, 0.0f).texture(1.0f, 1.0f).color(mchc6i2.red, mchc6i2.green, mchc6i2.blue, mchc6i2.alpha);
                bufferBuilder.vertex(matrix4f, -0.5f, 0.5f, 0.0f).texture(0.0f, 1.0f).color(mchc6i2.red, mchc6i2.green, mchc6i2.blue, mchc6i2.alpha);
                matrixStack.pop();
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
    }

    public void spawn(Vec3d position, Vec3d velocity, int lifetimeTicks, float size, Identifier texture, int color, double gravity, String physicsMode) {
        this.particles.add(new Particle(position, velocity, lifetimeTicks, size, texture, this.particles, color, gravity, physicsMode));
    }

    public void spawn(Vec3d position, Vec3d velocity, int lifetimeTicks, float size, Identifier texture, int color, String physicsMode) {
        this.spawn(position, velocity, lifetimeTicks, size, texture, color, 0.02, physicsMode);
    }

    public void spawn(Vec3d position, Vec3d velocity, int lifetimeTicks, float size, Identifier texture, int color) {
        this.spawn(position, velocity, lifetimeTicks, size, texture, color, 0.02, "Реалистичная");
    }

    public void spawn(Vec3d position, Vec3d velocity, int lifetimeTicks, float size, Identifier texture, int color, double gravity) {
        this.spawn(position, velocity, lifetimeTicks, size, texture, color, gravity, "Реалистичная");
    }

    @EventHandler
    public void onRenderTick(RenderTickEvent event) {
        long l;
        if (ParticleSystem.c.world != null && (l = ParticleSystem.c.world.getTime()) != this.lastTick) {
            this.particles.forEach(r4lbah2 -> {
                r4lbah2.tick();
            });
            this.lastTick = l;
        }
    }

    @EventHandler
    public void onWorldRender(WorldRenderPostEvent event) {
        if (ParticleSystem.c.gameRenderer == null || ParticleSystem.c.gameRenderer.getCamera() == null) {
            return;
        }
        Frustum frustum = new Frustum(new Matrix4f((Matrix4fc)event.a().peek().getPositionMatrix()), new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix()));
        frustum.setPosition(ParticleSystem.c.gameRenderer.getCamera().getPos().x, ParticleSystem.c.gameRenderer.getCamera().getPos().y, ParticleSystem.c.gameRenderer.getCamera().getPos().z);
        this.renderParticles(event.a(), frustum);
    }
}
