package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.colors.ColorUtil;

@ModuleInfo(
        name = "Nimb",
        desc = "Рисует нимб над головой, чтобы все видели, кто тут святой.",
        category = Category.VISUALS
)
public class Nimb extends Module {

    private static final int SEGMENTS = 72;
    private static final double MAX_RENDER_DISTANCE_SQ = 16384.0;
    private static final double[] COS = new double[SEGMENTS + 1];
    private static final double[] SIN = new double[SEGMENTS + 1];

    public final SliderSetting lineWidth = new SliderSetting(this, "Толщина линии", 1.5F, 1.0F, 3.0F, 0.5F);
    public final SliderSetting radius = new SliderSetting(this, "Радиус", 0.28F, 0.18F, 0.55F, 0.02F);
    public final SliderSetting yOffset = new SliderSetting(this, "Смещение по Y", 0.08F, -0.3F, 0.25F, 0.02F);
    public final BooleanSetting clientColor = new BooleanSetting(this, "Цвет клиента", true);
    public final ColorSetting customColor = new ColorSetting(this, "Кастомный цвет", ColorUtil.WHITE)
            .setVisible(() -> !clientColor.getValue());

    private final BufferAllocator bufferAllocator = new BufferAllocator(1 << 16);

    private static final RenderPipeline NIMB_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation(Identifier.of("prism", "nimb"))
                    .withVertexShader("core/position_color")
                    .withFragmentShader("core/position_color")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withCull(false)
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .build()
    );

    private static final RenderLayer NIMB_LAYER = RenderLayer.of(
            "prism_nimb",
            RenderSetup.builder(NIMB_PIPELINE)
                    .translucent()
                    .expectedBufferSize(1 << 16)
                    .build()
    );

    @EventHandler
    public void onRender(EventRender3D e) {
        if (mc.player == null || mc.world == null || mc.gameRenderer == null) return;

        MatrixStack stack = e.getMatrixStack();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vec3d camera = mc.gameRenderer.getCamera().getCameraPos();
        float tickDelta = e.getTickDelta();
        boolean firstPerson = mc.options.getPerspective().isFirstPerson();
        float baseRadius = radius.getValue();
        float yOff = yOffset.getValue();
        int color = clientColor.getValue() ? ColorUtil.getClientColor1(1) : customColor.getValue();
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        float rInner = baseRadius * 0.75F;
        float rOuter = baseRadius * 1.2F;
        float halfHeight = 0.04F;

        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(bufferAllocator);
        VertexConsumer consumer = immediate.getBuffer(NIMB_LAYER);

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player.isInvisible() || (player == mc.player && firstPerson)) continue;

            double dx = player.getX() - mc.player.getX();
            double dy = player.getY() - mc.player.getY();
            double dz = player.getZ() - mc.player.getZ();
            if (dx * dx + dy * dy + dz * dz > MAX_RENDER_DISTANCE_SQ) continue;

            double cx = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX()) - camera.x;
            double cy = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY()) - camera.y
                    + player.getEyeHeight(player.getPose()) + 0.22 + yOff;
            double cz = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ()) - camera.z;
            float yTop = (float) cy + halfHeight;
            float yBot = (float) cy - halfHeight;

            for (int i = 0; i < SEGMENTS; i++) {
                double cos1 = COS[i];
                double sin1 = SIN[i];
                double cos2 = COS[i + 1];
                double sin2 = SIN[i + 1];
                float x1In = (float) (cx + cos1 * rInner);
                float z1In = (float) (cz + sin1 * rInner);
                float x1Out = (float) (cx + cos1 * rOuter);
                float z1Out = (float) (cz + sin1 * rOuter);
                float x2In = (float) (cx + cos2 * rInner);
                float z2In = (float) (cz + sin2 * rInner);
                float x2Out = (float) (cx + cos2 * rOuter);
                float z2Out = (float) (cz + sin2 * rOuter);

                consumer.vertex(matrix, x1Out, yTop, z1Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x1In, yTop, z1In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2In, yTop, z2In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2Out, yTop, z2Out).color(red, green, blue, 0xE6);

                consumer.vertex(matrix, x1Out, yBot, z1Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2Out, yBot, z2Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2In, yBot, z2In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x1In, yBot, z1In).color(red, green, blue, 0xE6);

                consumer.vertex(matrix, x2Out, yBot, z2Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x1Out, yBot, z1Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x1Out, yTop, z1Out).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2Out, yTop, z2Out).color(red, green, blue, 0xE6);

                consumer.vertex(matrix, x1In, yBot, z1In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2In, yBot, z2In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x2In, yTop, z2In).color(red, green, blue, 0xE6);
                consumer.vertex(matrix, x1In, yTop, z1In).color(red, green, blue, 0xE6);
            }
        }

        immediate.draw();
    }

    static {
        for (int i = 0; i <= SEGMENTS; i++) {
            double angle = i / (double) SEGMENTS * Math.PI * 2.0;
            COS[i] = Math.cos(angle);
            SIN[i] = Math.sin(angle);
        }
    }
}
