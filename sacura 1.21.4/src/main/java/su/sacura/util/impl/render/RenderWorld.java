package su.sacura.util.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;
import su.sacura.events.render.WorldRenderEvent;
import su.sacura.util.impl.render.other.Projection;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.type.MinecraftWrapper;

public final class RenderWorld implements MinecraftWrapper {
    private static final Map<VoxelShape, Pair<List<Box>, List<Line>>> SHAPE_OUTLINES = new HashMap<>();
    private static final Map<VoxelShape, List<Box>> SHAPE_BOXES = new HashMap<>();
    public static final List<Texture> TEXTURE_DEPTH = new ArrayList<>();
    public static final List<Texture> TEXTURE = new ArrayList<>();
    public static final List<Line> LINE_DEPTH = new ArrayList<>();
    public static final List<Line> LINE = new ArrayList<>();
    public static final List<Quad> QUAD_DEPTH = new ArrayList<>();
    public static final List<Quad> QUAD = new ArrayList<>();
    public static Matrix4f lastProjMat = new Matrix4f();
    public static MatrixStack.Entry lastWorldSpaceMatrix = new MatrixStack().peek();
    private static final Identifier captureId = Identifier.of("textures/capture1.png");
    private static final Identifier bloom = Identifier.of("textures/features/particles/bloom.png");
    public static final List<Crystal> crystalList = new ArrayList<>();
    private static float prevCubeSize = 0.0f;
    private static final Random random = new Random();
    private static final List<Vec3d> particles = new ArrayList<>();
    private static float espValue = 1.0f;
    private static float espSpeed = 1.0f;
    private static float prevEspValue;
    private static float circleStep;
    private static boolean flipSpeed;

    private RenderWorld() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void setLastProjMat(Matrix4f lastProjMat) {
        RenderWorld.lastProjMat = lastProjMat;
    }

    public static void setLastWorldSpaceMatrix(MatrixStack.Entry lastWorldSpaceMatrix) {
        RenderWorld.lastWorldSpaceMatrix = lastWorldSpaceMatrix;
    }

    public static void drawEntity(Entity entity, Vec3d pos, float yaw, int alpha, MatrixStack matrices, float tickDelta) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        matrices.push();
        matrices.translate(pos.x, pos.y, pos.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrices.scale(1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) alpha / 255.0f);
        EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer(entity);
        if (renderer != null) {
            int light = renderer.getLight(livingEntity, tickDelta);
            VertexConsumerProvider.Immediate vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();
            EntityRenderState renderState = renderer.getAndUpdateRenderState(livingEntity, tickDelta);
            if (renderState != null) {
                renderer.render(renderState, matrices, vertexConsumers, light);
            }
            vertexConsumers.draw();
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        matrices.pop();
    }

    public static void onWorldRender(WorldRenderEvent e) {
        if (!TEXTURE.isEmpty()) {
            Set<Identifier> identifiers = TEXTURE.stream().map(Texture::id).collect(Collectors.toCollection(LinkedHashSet::new));
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            identifiers.forEach(id -> {
                RenderSystem.setShaderTexture(0, id);
                RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
                BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                TEXTURE.stream().filter(texture -> texture.id().equals(id))
                        .forEach(tex -> RenderWorld.quadTexture(tex.entry(), buffer, tex.x(), tex.y(), tex.width(), tex.height(), tex.color()));
                BufferRenderer.drawWithGlobalProgram(buffer.end());
            });
            RenderSystem.disableBlend();
            TEXTURE.clear();
        }
        if (!TEXTURE_DEPTH.isEmpty()) {
            Set<Identifier> identifiers = TEXTURE_DEPTH.stream().map(Texture::id).collect(Collectors.toCollection(LinkedHashSet::new));
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            identifiers.forEach(id -> {
                RenderSystem.setShaderTexture(0, id);
                RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
                BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                TEXTURE_DEPTH.stream().filter(texture -> texture.id().equals(id))
                        .forEach(tex -> RenderWorld.quadTexture(tex.entry(), buffer, tex.x(), tex.y(), tex.width(), tex.height(), tex.color()));
                BufferRenderer.drawWithGlobalProgram(buffer.end());
            });
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            TEXTURE_DEPTH.clear();
        }
        if (!LINE.isEmpty()) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            Set<Float> widths = LINE.stream().map(Line::width).collect(Collectors.toCollection(LinkedHashSet::new));
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
            widths.forEach(width -> {
                RenderSystem.lineWidth(width);
                BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
                LINE.stream().filter(line -> line.width() == width)
                        .forEach(line -> RenderWorld.vertexLine(line.entry(), buffer, line.start().toVector3f(), line.end().toVector3f(), line.colorStart(), line.colorEnd()));
                BufferRenderer.drawWithGlobalProgram(buffer.end());
            });
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            LINE.clear();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
        if (!QUAD.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            QUAD.forEach(quad -> RenderWorld.vertexQuad(quad.entry(), buffer, quad.x(), quad.y(), quad.w(), quad.z(), quad.color()));
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            QUAD.clear();
        }
        if (!LINE_DEPTH.isEmpty()) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            Set<Float> widths = LINE_DEPTH.stream().map(Line::width).collect(Collectors.toCollection(LinkedHashSet::new));
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
            widths.forEach(width -> {
                RenderSystem.lineWidth(width);
                BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
                LINE_DEPTH.stream().filter(line -> line.width() == width)
                        .forEach(line -> RenderWorld.vertexLine(line.entry(), buffer, line.start().toVector3f(), line.end().toVector3f(), line.colorStart(), line.colorEnd()));
                BufferRenderer.drawWithGlobalProgram(buffer.end());
            });
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            LINE_DEPTH.clear();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
        if (!QUAD_DEPTH.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            QUAD_DEPTH.forEach(quad -> RenderWorld.vertexQuad(quad.entry(), buffer, quad.x(), quad.y(), quad.w(), quad.z(), quad.color()));
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            QUAD_DEPTH.clear();
        }
    }

    public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width) {
        RenderWorld.drawShape(blockPos, voxelShape, color, width, true, false);
    }

    public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
        if (SHAPE_BOXES.containsKey(voxelShape)) {
            SHAPE_BOXES.get(voxelShape).forEach(box -> {
                Box offsetBox = box.offset(blockPos);
                if (Projection.canSee(offsetBox)) {
                    RenderWorld.drawBox(offsetBox, color, width, true, fill, depth);
                }
            });
            return;
        }
        SHAPE_BOXES.put(voxelShape, voxelShape.getBoundingBoxes());
    }

    public static void drawShapeAlternative(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
        Vec3d vec3d = Vec3d.of(blockPos);
        if (Projection.canSee(new Box(blockPos))) {
            if (SHAPE_OUTLINES.containsKey(voxelShape)) {
                Pair<List<Box>, List<Line>> pair = SHAPE_OUTLINES.get(voxelShape);
                if (fill) {
                    pair.getLeft().forEach(box -> RenderWorld.drawBox(box.offset(vec3d), color, width, false, true, depth));
                }
                pair.getRight().forEach(line -> RenderWorld.drawLine(line.start().add(vec3d), line.end().add(vec3d), color, width, depth));
                return;
            }
            List<Line> lines = new ArrayList<>();
            voxelShape.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) ->
                    lines.add(new Line(null, new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), 0, 0, 0.0f)));
            SHAPE_OUTLINES.put(voxelShape, new Pair<>(voxelShape.getBoundingBoxes(), lines));
        }
    }

    public static void drawBox(Box box, int color, float width) {
        RenderWorld.drawBox(box, color, width, true, true, false);
    }

    public static void drawBox(Box box, int color, float width, boolean line, boolean fill, boolean depth) {
        RenderWorld.drawBox(null, box, color, width, line, fill, depth);
    }

    public static void drawBox(MatrixStack.Entry entry, Box box, int color, float width, boolean line, boolean fill, boolean depth) {
        box = box.expand(0.001);
        double x1 = box.minX, y1 = box.minY, z1 = box.minZ;
        double x2 = box.maxX, y2 = box.maxY, z2 = box.maxZ;
        if (fill) {
            int fillColor = ColorProvider.multAlpha(color, 0.1f);
            RenderWorld.drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), fillColor, depth);
            RenderWorld.drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y1, z1), fillColor, depth);
            RenderWorld.drawQuad(entry, new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), new Vec3d(x2, y1, z2), fillColor, depth);
            RenderWorld.drawQuad(entry, new Vec3d(x1, y1, z2), new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), fillColor, depth);
            RenderWorld.drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), fillColor, depth);
            RenderWorld.drawQuad(entry, new Vec3d(x1, y2, z1), new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z2), new Vec3d(x2, y2, z1), fillColor, depth);
        }
        if (line) {
            RenderWorld.drawLine(entry, x1, y1, z1, x2, y1, z1, color, width, depth);
            RenderWorld.drawLine(entry, x2, y1, z1, x2, y1, z2, color, width, depth);
            RenderWorld.drawLine(entry, x2, y1, z2, x1, y1, z2, color, width, depth);
            RenderWorld.drawLine(entry, x1, y1, z2, x1, y1, z1, color, width, depth);
            RenderWorld.drawLine(entry, x1, y1, z2, x1, y2, z2, color, width, depth);
            RenderWorld.drawLine(entry, x1, y1, z1, x1, y2, z1, color, width, depth);
            RenderWorld.drawLine(entry, x2, y1, z2, x2, y2, z2, color, width, depth);
            RenderWorld.drawLine(entry, x2, y1, z1, x2, y2, z1, color, width, depth);
            RenderWorld.drawLine(entry, x1, y2, z1, x2, y2, z1, color, width, depth);
            RenderWorld.drawLine(entry, x2, y2, z1, x2, y2, z2, color, width, depth);
            RenderWorld.drawLine(entry, x2, y2, z2, x1, y2, z2, color, width, depth);
            RenderWorld.drawLine(entry, x1, y2, z2, x1, y2, z1, color, width, depth);
        }
    }

    public static void vertexLine(MatrixStack matrices, VertexConsumer buffer, Vec3d start, Vec3d end, int startColor, int endColor) {
        RenderWorld.vertexLine(matrices.peek(), buffer, start.toVector3f(), end.toVector3f(), startColor, endColor);
    }

    public static void vertexLine(MatrixStack.Entry entry, VertexConsumer buffer, Vector3f start, Vector3f end, int startColor, int endColor) {
        if (entry == null) {
            entry = lastWorldSpaceMatrix;
        }
        Vector3f vec = RenderWorld.getNormal(start, end);
        buffer.vertex(entry, start).color(startColor).normal(entry, vec);
        buffer.vertex(entry, end).color(endColor).normal(entry, vec);
    }

    public static void vertexQuad(MatrixStack.Entry entry, VertexConsumer buffer, Vec3d vec1, Vec3d vec2, Vec3d vec3, Vec3d vec4, int color) {
        RenderWorld.vertexQuad(entry, buffer, vec1.toVector3f(), vec2.toVector3f(), vec3.toVector3f(), vec4.toVector3f(), color);
    }

    public static void vertexQuad(MatrixStack.Entry entry, VertexConsumer buffer, Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, int color) {
        if (entry == null) {
            entry = lastWorldSpaceMatrix;
        }
        buffer.vertex(entry, vec1).color(color);
        buffer.vertex(entry, vec2).color(color);
        buffer.vertex(entry, vec3).color(color);
        buffer.vertex(entry, vec4).color(color);
    }

    public static void quadTexture(MatrixStack.Entry entry, BufferBuilder buffer, float x, float y, float width, float height, Vector4i color) {
        buffer.vertex(entry, x, y + height, 0.0f).texture(0.0f, 0.0f).color(color.x);
        buffer.vertex(entry, x + width, y + height, 0.0f).texture(0.0f, 1.0f).color(color.y);
        buffer.vertex(entry, x + width, y, 0.0f).texture(1.0f, 1.0f).color(color.w);
        buffer.vertex(entry, x, y, 0.0f).texture(1.0f, 0.0f).color(color.z);
    }

    public static Vector3f getNormal(Vector3f start, Vector3f end) {
        Vector3f normal = new Vector3f(start).sub(end);
        float sqrt = MathHelper.sqrt(normal.lengthSquared());
        return normal.div(sqrt);
    }

    public static void updateTargetEsp() {
        prevEspValue = espValue;
        espValue += espSpeed;
        if (espSpeed > 25.0f) {
            flipSpeed = true;
        }
        if (espSpeed < -25.0f) {
            flipSpeed = false;
        }
        espSpeed = flipSpeed ? espSpeed - 0.5f : espSpeed + 0.5f;
        circleStep += 0.15f;
    }

    public static void drawLine(MatrixStack.Entry entry, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color, float width, boolean depth) {
        RenderWorld.drawLine(entry, new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), color, color, width, depth);
    }

    public static void drawLine(Vec3d start, Vec3d end, int color, float width, boolean depth) {
        RenderWorld.drawLine(null, start, end, color, color, width, depth);
    }

    public static void drawLine(MatrixStack.Entry entry, Vec3d start, Vec3d end, int colorStart, int colorEnd, float width, boolean depth) {
        Line line = new Line(entry, start, end, colorStart, colorEnd, width);
        if (depth) {
            LINE_DEPTH.add(line);
        } else {
            LINE.add(line);
        }
    }

    public static void drawCircleQuad(BufferBuilder buffer, MatrixStack.Entry entry, Vec3d center, double radius, int color, int segments) {
        buffer.vertex(entry, (float) center.x, (float) center.y, (float) center.z).color(color);
        for (int i = 0; i <= segments; ++i) {
            double angle = Math.PI * 2 * i / segments;
            double dx = Math.cos(angle) * radius;
            double dz = Math.sin(angle) * radius;
            buffer.vertex(entry, (float) (center.x + dx), (float) center.y, (float) (center.z + dz)).color(color);
        }
    }

    public static void drawQuad(Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color, boolean depth) {
        RenderWorld.drawQuad(null, x, y, w, z, color, depth);
    }

    public static void drawQuad(MatrixStack.Entry entry, Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color, boolean depth) {
        Quad quad = new Quad(entry, x, y, w, z, color);
        if (depth) {
            QUAD_DEPTH.add(quad);
        } else {
            QUAD.add(quad);
        }
    }

    public static void drawTexture(MatrixStack.Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color, boolean depth) {
        Texture texture = new Texture(entry, id, x, y, width, height, color);
        if (depth) {
            TEXTURE_DEPTH.add(texture);
        } else {
            TEXTURE.add(texture);
        }
    }

    public record Line(MatrixStack.Entry entry, Vec3d start, Vec3d end, int colorStart, int colorEnd, float width) {}
    public record Quad(MatrixStack.Entry entry, Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color) {}
    public record Texture(MatrixStack.Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color) {}

    private static class Crystal {
        private final Entity entity;
        private final Vec3d position;
        private final Vec3d rotation;
        private final float size;
        private final float rotationSpeed;

        public Crystal(Entity entity, Vec3d position, Vec3d rotation) {
            this.entity = entity;
            this.position = position;
            this.rotation = rotation;
            this.size = 0.09f;
            this.rotationSpeed = 0.5f + (float) (Math.random() * 1.5);
        }

        public void render(MatrixStack ms) {
            ms.push();
            ms.translate(this.position.x, this.position.y, this.position.z);
            float pulsation = 1.0f + (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.1f);
            ms.scale(pulsation, pulsation, pulsation);
            float selfRotation = (float) (System.currentTimeMillis() % 36000L) / 100.0f * this.rotationSpeed;
            ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) this.rotation.x));
            ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) this.rotation.y + selfRotation));
            ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) this.rotation.z));
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            int baseColor = ColorProvider.fade(90);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
            this.drawCrystal(ms, baseColor, 0.2f, true);
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            this.drawCrystal(ms, baseColor, 0.3f, true);
            this.drawCrystal(ms, baseColor, 0.8f, false);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            ms.pop();
        }

        private void drawCrystal(MatrixStack ms, int baseColor, float alpha, boolean filled) {
            // ⚠️ Проверь два имени DrawMode ниже командой в конце ответа (field_27379 / field_29344).
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(
                    filled ? VertexFormat.DrawMode.TRIANGLES : VertexFormat.DrawMode.LINES,
                    VertexFormats.POSITION_COLOR);
            float s = this.size;
            float hPrism = this.size * 1.0f;
            float hPyramid = this.size * 1.5f;
            int numSides = 8;
            List<Vec3d> topVertices = new ArrayList<>();
            List<Vec3d> bottomVertices = new ArrayList<>();
            for (int i = 0; i < numSides; ++i) {
                float angle = (float) (Math.PI * 2 * i / numSides);
                float x = (float) (s * Math.cos(angle));
                float z = (float) (s * Math.sin(angle));
                topVertices.add(new Vec3d(x, hPrism / 2.0f, z));
                bottomVertices.add(new Vec3d(x, -hPrism / 2.0f, z));
            }
            Vec3d vTop = new Vec3d(0.0, hPrism / 2.0f + hPyramid, 0.0);
            Vec3d vBottom = new Vec3d(0.0, -hPrism / 2.0f - hPyramid, 0.0);
            int finalColor = ColorProvider.setAlpha(baseColor, (int) (alpha * 255.0f));
            for (int i = 0; i < numSides; ++i) {
                Vec3d v1 = bottomVertices.get(i);
                Vec3d v2 = bottomVertices.get((i + 1) % numSides);
                Vec3d v3 = topVertices.get((i + 1) % numSides);
                Vec3d v4 = topVertices.get(i);
                this.drawQuad(ms, bufferBuilder, v1, v2, v3, v4, finalColor, filled);
            }
            for (int i = 0; i < numSides; ++i) {
                this.drawTriangle(ms, bufferBuilder, vTop, topVertices.get(i), topVertices.get((i + 1) % numSides), finalColor, filled);
            }
            for (int i = 0; i < numSides; ++i) {
                this.drawTriangle(ms, bufferBuilder, vBottom, bottomVertices.get((i + 1) % numSides), bottomVertices.get(i), finalColor, filled);
            }
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }

        private void drawTriangle(MatrixStack ms, BufferBuilder bb, Vec3d v1, Vec3d v2, Vec3d v3, int color, boolean filled) {
            Matrix4f m = ms.peek().getPositionMatrix();
            if (filled) {
                bb.vertex(m, (float) v1.x, (float) v1.y, (float) v1.z).color(color);
                bb.vertex(m, (float) v2.x, (float) v2.y, (float) v2.z).color(color);
                bb.vertex(m, (float) v3.x, (float) v3.y, (float) v3.z).color(color);
            } else {
                bb.vertex(m, (float) v1.x, (float) v1.y, (float) v1.z).color(color);
                bb.vertex(m, (float) v2.x, (float) v2.y, (float) v2.z).color(color);
                bb.vertex(m, (float) v2.x, (float) v2.y, (float) v2.z).color(color);
                bb.vertex(m, (float) v3.x, (float) v3.y, (float) v3.z).color(color);
                bb.vertex(m, (float) v3.x, (float) v3.y, (float) v3.z).color(color);
                bb.vertex(m, (float) v1.x, (float) v1.y, (float) v1.z).color(color);
            }
        }

        private void drawQuad(MatrixStack ms, BufferBuilder bb, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, int color, boolean filled) {
            if (filled) {
                this.drawTriangle(ms, bb, v1, v2, v3, color, true);
                this.drawTriangle(ms, bb, v1, v3, v4, color, true);
            } else {
                Matrix4f m = ms.peek().getPositionMatrix();
                bb.vertex(m, (float) v1.x, (float) v1.y, (float) v1.z).color(color);
                bb.vertex(m, (float) v2.x, (float) v2.y, (float) v2.z).color(color);
                bb.vertex(m, (float) v2.x, (float) v2.y, (float) v2.z).color(color);
                bb.vertex(m, (float) v3.x, (float) v3.y, (float) v3.z).color(color);
                bb.vertex(m, (float) v3.x, (float) v3.y, (float) v3.z).color(color);
                bb.vertex(m, (float) v4.x, (float) v4.y, (float) v4.z).color(color);
                bb.vertex(m, (float) v4.x, (float) v4.y, (float) v4.z).color(color);
                bb.vertex(m, (float) v1.x, (float) v1.y, (float) v1.z).color(color);
            }
        }
    }
}