package haron.render.world;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.client.MinecraftClientAccess;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class WorldRenderUtils
implements MinecraftClientAccess {
    public static void drawBillboardGlow(MatrixStack matrices, Vec3d position, float size, int color) {
        a(matrices, position, size, color);
    }

    public static void drawTexturedBillboard(MatrixStack matrices, Vec3d position, float size, int color,
                                              Identifier texture, float rotationDegrees, boolean visibleThroughWalls) {
        a(matrices, position, size, color, texture, rotationDegrees, visibleThroughWalls);
    }
    private static final Tessellator e = Tessellator.getInstance();
    private static final Identifier f = Identifier.of((String)"haron", (String)"textures/bloom.png");
    public static int a;
    public static boolean b;

    private WorldRenderUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void b(MatrixStack matrixStack, Box box, int n) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        double d = box.minX - vec3d.x;
        double d2 = box.minY - vec3d.y;
        double d3 = box.minZ - vec3d.z;
        double d4 = box.maxX - vec3d.x;
        double d5 = box.maxY - vec3d.y;
        double d6 = box.maxZ - vec3d.z;
        int n2 = n >> 16;
        float f = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n3 = n >> 8;
        float f2 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        float f3 = (float)((~n | 0xFF) - ~n) / 255.0f;
        int n4 = n >> 24;
        float f4 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        float f5 = f4 * 0.11f;
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f5);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f5);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)-1.0f, (float)-1.0f);
        RenderSystem.lineWidth((float)1.5f);
        BufferBuilder bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        BufferBuilder bufferBuilder3 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder3.end());
        BufferBuilder bufferBuilder4 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder4.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder4.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder4.end());
        GL11.glPolygonOffset((float)0.0f, (float)0.0f);
        GL11.glDisable((int)10754);
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void b(MatrixStack matrixStack, Iterable<BlockPos> iterable, int n) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.disableCull();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        int n2 = n >> 16;
        float f12 = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n3 = n >> 8;
        float f13 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        float f14 = (float)((~n | 0xFF) - ~n) / 255.0f;
        int n4 = n >> 24;
        float f15 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        if (f15 == 0.0f) {
            f15 = 1.0f;
        }
        float f16 = f15 * 0.2f;
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        int n5 = Integer.MAX_VALUE;
        int n6 = Integer.MAX_VALUE;
        int n7 = Integer.MAX_VALUE;
        int n8 = Integer.MIN_VALUE;
        int n9 = Integer.MIN_VALUE;
        int n10 = Integer.MIN_VALUE;
        for (BlockPos object2 : iterable) {
            hashSet.add(object2);
            n5 = Math.min(n5, object2.getX());
            n6 = Math.min(n6, object2.getY());
            n7 = Math.min(n7, object2.getZ());
            n8 = Math.max(n8, object2.getX());
            n9 = Math.max(n9, object2.getY());
            n10 = Math.max(n10, object2.getZ());
        }
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (BlockPos blockPos : hashSet) {
            float f17 = blockPos.getX() != n5 ? 0.0f : 0.01f;
            f11 = blockPos.getY() != n6 ? 0.0f : 0.01f;
            f10 = blockPos.getZ() != n7 ? 0.0f : 0.01f;
            float f18 = blockPos.getX() != n8 ? 0.0f : 0.01f;
            f9 = f18;
            float f19 = blockPos.getY() != n9 ? 0.0f : 0.01f;
            f8 = f19;
            f7 = blockPos.getZ() != n10 ? 0.0f : 0.01f;
            f6 = (float)blockPos.getX() - (float)vec3d.x + f17;
            f5 = (float)blockPos.getY() - (float)vec3d.y + f11;
            f4 = (float)blockPos.getZ() - (float)vec3d.z + f10;
            f3 = f6 + 1.0f - f17 - f9;
            f2 = f5 + 1.0f - f11 - f8;
            f = f4 + 1.0f - f10 - f7;
            if (!hashSet.contains(blockPos.down())) {
                bufferBuilder.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f16);
            }
            if (!hashSet.contains(blockPos.up())) {
                bufferBuilder.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f16);
            }
            if (!hashSet.contains(blockPos.north())) {
                bufferBuilder.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f16);
            }
            if (!hashSet.contains(blockPos.south())) {
                bufferBuilder.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f16);
            }
            if (!hashSet.contains(blockPos.west())) {
                bufferBuilder.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f16);
                bufferBuilder.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f16);
            }
            if (hashSet.contains(blockPos.east())) continue;
            bufferBuilder.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f16);
            bufferBuilder.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f16);
            bufferBuilder.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f16);
            bufferBuilder.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f16);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)-1.0f, (float)-1.0f);
        RenderSystem.lineWidth((float)1.5f);
        BufferBuilder bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (BlockPos blockPos : hashSet) {
            f11 = blockPos.getX() != n5 ? 0.0f : 0.01f;
            f10 = blockPos.getY() != n6 ? 0.0f : 0.01f;
            f9 = blockPos.getZ() != n7 ? 0.0f : 0.01f;
            f8 = blockPos.getX() != n8 ? 0.0f : 0.01f;
            f7 = blockPos.getY() != n9 ? 0.0f : 0.01f;
            float f20 = blockPos.getZ() != n10 ? 0.0f : 0.01f;
            f6 = (float)blockPos.getX() - (float)vec3d.x + f11;
            f5 = (float)blockPos.getY() - (float)vec3d.y + f10;
            f4 = (float)blockPos.getZ() - (float)vec3d.z + f9;
            f3 = f6 + 1.0f - f11 - f8;
            f2 = f5 + 1.0f - f10 - f7;
            f = f4 + 1.0f - f9 - f20;
            if (!hashSet.contains(blockPos.down())) {
                if (!hashSet.contains(blockPos.west())) {
                    bufferBuilder2.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.east())) {
                    bufferBuilder2.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.north())) {
                    bufferBuilder2.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.south())) {
                    bufferBuilder2.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f15);
                }
            }
            if (!hashSet.contains(blockPos.up())) {
                if (!hashSet.contains(blockPos.west())) {
                    bufferBuilder2.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.east())) {
                    bufferBuilder2.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.north())) {
                    bufferBuilder2.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f15);
                }
                if (!hashSet.contains(blockPos.south())) {
                    bufferBuilder2.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f15);
                    bufferBuilder2.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f15);
                }
            }
            if (!hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.north())) {
                bufferBuilder2.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.north())) {
                bufferBuilder2.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.south())) {
                bufferBuilder2.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.south())) {
                bufferBuilder2.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.west()) && hashSet.contains(blockPos.north()) && !hashSet.contains(blockPos.west().north())) {
                bufferBuilder2.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.north()) && hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.north().west())) {
                bufferBuilder2.vertex(matrix4f, f6, f5, f4).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f6, f2, f4).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.east())) {
                if (hashSet.contains(blockPos.north())) {
                    if (!hashSet.contains(blockPos.east().north())) {
                        bufferBuilder2.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f15);
                        bufferBuilder2.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f15);
                    }
                } else if (b) {
                    // empty if block
                }
            }
            if (!hashSet.contains(blockPos.north())) {
                if (hashSet.contains(blockPos.east())) {
                    if (!hashSet.contains(blockPos.north().east())) {
                        bufferBuilder2.vertex(matrix4f, f3, f5, f4).color(f12, f13, f14, f15);
                        bufferBuilder2.vertex(matrix4f, f3, f2, f4).color(f12, f13, f14, f15);
                    }
                } else if (b) {
                    // empty if block
                }
            }
            if (!hashSet.contains(blockPos.east()) && hashSet.contains(blockPos.south()) && !hashSet.contains(blockPos.east().south())) {
                bufferBuilder2.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.south()) && hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.south().east())) {
                bufferBuilder2.vertex(matrix4f, f3, f5, f).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f3, f2, f).color(f12, f13, f14, f15);
            }
            if (!hashSet.contains(blockPos.west()) && hashSet.contains(blockPos.south()) && !hashSet.contains(blockPos.west().south())) {
                bufferBuilder2.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f15);
                bufferBuilder2.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f15);
            }
            if (hashSet.contains(blockPos.south()) || !hashSet.contains(blockPos.west()) || hashSet.contains(blockPos.south().west())) continue;
            bufferBuilder2.vertex(matrix4f, f6, f5, f).color(f12, f13, f14, f15);
            bufferBuilder2.vertex(matrix4f, f6, f2, f).color(f12, f13, f14, f15);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        GL11.glPolygonOffset((float)0.0f, (float)0.0f);
        GL11.glDisable((int)10754);
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Vec3d vec3d, float f, int n, int n2, float f2) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)-1.0f, (float)-1.0f);
        RenderSystem.lineWidth((float)1.5f);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d2 = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        int n3 = n2 >> 16;
        float f3 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        int n4 = n2 >> 8;
        float f4 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        float f5 = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n5 = n2 >> 24;
        float f6 = (float)((~n5 | 0xFF) - ~n5) / 255.0f;
        if (f6 == 0.0f) {
            f6 = 1.0f;
        }
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        for (int i = 0; i <= n; ++i) {
            double d = Math.PI * 2 * (double)i / (double)n;
            bufferBuilder.vertex(matrix4f, (float)(vec3d.x + (double)f * Math.cos(d) - vec3d2.x), (float)(vec3d.y + (double)f2 + 1.0 - vec3d2.y), (float)(vec3d.z + (double)f * Math.sin(d) - vec3d2.z)).color(f3, f4, f5, f6);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        GL11.glPolygonOffset((float)0.0f, (float)0.0f);
        GL11.glDisable((int)10754);
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Box box, int n) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)-1.0f, (float)-1.0f);
        RenderSystem.lineWidth((float)1.5f);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        double d = box.minX - vec3d.x;
        double d2 = box.minY - vec3d.y;
        double d3 = box.minZ - vec3d.z;
        double d4 = box.maxX - vec3d.x;
        double d5 = box.maxY - vec3d.y;
        double d6 = box.maxZ - vec3d.z;
        int n2 = n >> 16;
        float f = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n3 = n >> 8;
        float f2 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        float f3 = (float)((~n | 0xFF) - ~n) / 255.0f;
        int n4 = n >> 24;
        float f4 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        BufferBuilder bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder2.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        BufferBuilder bufferBuilder3 = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d2, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d5, (float)d3).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d4, (float)d5, (float)d6).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d2, (float)d6).color(f, f2, f3, f4);
        bufferBuilder3.vertex(matrix4f, (float)d, (float)d5, (float)d6).color(f, f2, f3, f4);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder3.end());
        GL11.glPolygonOffset((float)0.0f, (float)0.0f);
        GL11.glDisable((int)10754);
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Vec3d vec3d, float f, int n) {
        RenderSystem.setShaderTexture((int)0, (Identifier)WorldRenderUtils.f);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        matrixStack.push();
        RenderSystem.disableCull();
        GL11.glDepthMask((boolean)false);
        RenderSystem.enableBlend();
        int n2 = n >> 16;
        int n3 = (~n2 | 0xFF) - ~n2;
        int n4 = n >> 8;
        int n5 = (~n4 | 0xFF) - ~n4;
        int n6 = (~n | 0xFF) - ~n;
        int n7 = n >> 24;
        int n8 = (~n7 | 0xFF) - ~n7;
        if ((float)((n3 ^ n5) + 2 * (n3 & n5) - ~n6 - 1) / 765.0f >= 0.2f) {
            RenderSystem.blendFunc((int)770, (int)1);
            Vec3d vec3d2 = WorldRenderUtils.c.getEntityRenderDispatcher().camera.getPos();
            matrixStack.translate(-vec3d2.x + vec3d.x, -vec3d2.y + vec3d.y, -vec3d2.z + vec3d.z);
            matrixStack.multiply(c.getEntityRenderDispatcher().getRotation());
            matrixStack.scale(f, f, f);
            BufferBuilder bufferBuilder = e.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float f2 = (float)n3 / 255.0f;
            float f3 = (float)n5 / 255.0f;
            float f4 = (float)n6 / 255.0f;
            float f5 = (float)n8 / 255.0f;
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, -0.5f, 0.0f).texture(0.0f, 0.0f).color(f2, f3, f4, f5);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, -0.5f, 0.0f).texture(1.0f, 0.0f).color(f2, f3, f4, f5);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, 0.5f, 0.0f).texture(1.0f, 1.0f).color(f2, f3, f4, f5);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, 0.5f, 0.0f).texture(0.0f, 1.0f).color(f2, f3, f4, f5);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            RenderSystem.blendFunc((int)770, (int)771);
        } else {
            RenderSystem.blendFunc((int)770, (int)771);
            Vec3d vec3d3 = WorldRenderUtils.c.getEntityRenderDispatcher().camera.getPos();
            matrixStack.translate(-vec3d3.x + vec3d.x, -vec3d3.y + vec3d.y, -vec3d3.z + vec3d.z);
            matrixStack.multiply(c.getEntityRenderDispatcher().getRotation());
            matrixStack.scale(f * 1.2f, f * 1.2f, f * 1.2f);
            BufferBuilder bufferBuilder = e.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float f6 = (float)n8 / 255.0f * 0.3f;
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, -0.5f, 0.0f).texture(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, f6);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, -0.5f, 0.0f).texture(1.0f, 0.0f).color(1.0f, 1.0f, 1.0f, f6);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, 0.5f, 0.0f).texture(1.0f, 1.0f).color(1.0f, 1.0f, 1.0f, f6);
            bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, 0.5f, 0.0f).texture(0.0f, 1.0f).color(1.0f, 1.0f, 1.0f, f6);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
            matrixStack.scale(0.8333333f, 0.8333333f, 0.8333333f);
            BufferBuilder bufferBuilder2 = e.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float f7 = (float)n3 / 255.0f;
            float f8 = (float)n5 / 255.0f;
            float f9 = (float)n6 / 255.0f;
            float f10 = (float)n8 / 255.0f;
            bufferBuilder2.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, -0.5f, 0.0f).texture(0.0f, 0.0f).color(f7, f8, f9, f10);
            bufferBuilder2.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, -0.5f, 0.0f).texture(1.0f, 0.0f).color(f7, f8, f9, f10);
            bufferBuilder2.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, 0.5f, 0.0f).texture(1.0f, 1.0f).color(f7, f8, f9, f10);
            bufferBuilder2.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, 0.5f, 0.0f).texture(0.0f, 1.0f).color(f7, f8, f9, f10);
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder2.end());
        }
        GL11.glDepthMask((boolean)true);
        RenderSystem.enableCull();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Vec3d vec3d, float f, int n, Identifier identifier, float f2, boolean bl) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
        matrixStack.push();
        RenderSystem.disableCull();
        if (bl) {
            RenderSystem.disableDepthTest();
            GL11.glDepthMask((boolean)false);
        } else {
            GL11.glDepthMask((boolean)false);
        }
        RenderSystem.enableBlend();
        int n2 = n >> 16;
        int n3 = (~n2 | 0xFF) - ~n2;
        int n4 = n >> 8;
        int n5 = (~n4 | 0xFF) - ~n4;
        int n6 = n >> 24;
        int n7 = (~n6 | 0xFF) - ~n6;
        RenderSystem.blendFunc((int)770, (int)771);
        Vec3d vec3d2 = WorldRenderUtils.c.getEntityRenderDispatcher().camera.getPos();
        matrixStack.translate(-vec3d2.x + vec3d.x, -vec3d2.y + vec3d.y, -vec3d2.z + vec3d.z);
        matrixStack.multiply(c.getEntityRenderDispatcher().getRotation());
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f2));
        matrixStack.scale(f, f, f);
        BufferBuilder bufferBuilder = e.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float f3 = (float)n3 / 255.0f;
        float f4 = (float)n5 / 255.0f;
        float f5 = (float)((~n | 0xFF) - ~n) / 255.0f;
        float f6 = (float)n7 / 255.0f;
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, -0.5f, 0.0f).texture(0.0f, 0.0f).color(f3, f4, f5, f6);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, -0.5f, 0.0f).texture(1.0f, 0.0f).color(f3, f4, f5, f6);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), 0.5f, 0.5f, 0.0f).texture(1.0f, 1.0f).color(f3, f4, f5, f6);
        bufferBuilder.vertex(matrixStack.peek().getPositionMatrix(), -0.5f, 0.5f, 0.0f).texture(0.0f, 1.0f).color(f3, f4, f5, f6);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        if (bl) {
            RenderSystem.enableDepthTest();
            GL11.glDepthMask((boolean)true);
        } else {
            GL11.glDepthMask((boolean)true);
        }
        RenderSystem.enableCull();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Vec3d vec3d, float f, int n, int n2) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d2 = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        int n3 = n2 >> 16;
        float f2 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        int n4 = n2 >> 8;
        float f3 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        float f4 = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n5 = n2 >> 24;
        float f5 = (float)((~n5 | 0xFF) - ~n5) / 255.0f * 0.3f;
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        bufferBuilder.vertex(matrix4f, (float)d, (float)d2, (float)d3).color(f2, f3, f4, f5);
        for (int i = 0; i <= n; ++i) {
            double d4 = Math.PI * 2 * (double)i / (double)n;
            bufferBuilder.vertex(matrix4f, (float)(d + (double)f * Math.cos(d4)), (float)d2, (float)(d3 + (double)f * Math.sin(d4))).color(f2, f3, f4, f5);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Iterable<BlockPos> iterable, int n) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)-1.0f, (float)-1.0f);
        RenderSystem.lineWidth((float)1.5f);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        int n2 = n >> 16;
        float f = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n3 = n >> 8;
        float f2 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        float f3 = (float)((~n | 0xFF) - ~n) / 255.0f;
        int n4 = n >> 24;
        float f4 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        if (f4 == 0.0f) {
            f4 = 1.0f;
        }
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        Iterator<BlockPos> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            hashSet.add(iterator.next());
        }
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (BlockPos blockPos : hashSet) {
            float f5 = (float)blockPos.getX() - (float)vec3d.x;
            float f6 = (float)blockPos.getY() - (float)vec3d.y;
            float f7 = (float)blockPos.getZ() - (float)vec3d.z;
            float f8 = f5 + 1.0f;
            float f9 = f6 + 1.0f;
            float f10 = f7 + 1.0f;
            if (!hashSet.contains(blockPos.down())) {
                if (!hashSet.contains(blockPos.west())) {
                    bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
                }
                if (!hashSet.contains(blockPos.east())) {
                    bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
                }
                if (!hashSet.contains(blockPos.north())) {
                    bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
                }
                if (!hashSet.contains(blockPos.south())) {
                    bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
                }
            }
            if (!hashSet.contains(blockPos.up())) {
                if (!hashSet.contains(blockPos.west())) {
                    bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
                }
                if (!hashSet.contains(blockPos.east())) {
                    bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
                }
                if (!hashSet.contains(blockPos.north())) {
                    bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
                } else if (b) {
                    // empty if block
                }
                if (!hashSet.contains(blockPos.south())) {
                    bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
                    bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
                }
            }
            if (!hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.north())) {
                bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.north())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.south())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.south())) {
                bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.west()) && hashSet.contains(blockPos.north()) && !hashSet.contains(blockPos.west().north())) {
                bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.north()) && hashSet.contains(blockPos.west()) && !hashSet.contains(blockPos.north().west())) {
                bufferBuilder.vertex(matrix4f, f5, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.east()) && hashSet.contains(blockPos.north()) && !hashSet.contains(blockPos.east().north())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.north()) && hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.north().east())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f7).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f7).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.east()) && hashSet.contains(blockPos.south()) && !hashSet.contains(blockPos.east().south())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.south()) && hashSet.contains(blockPos.east()) && !hashSet.contains(blockPos.south().east())) {
                bufferBuilder.vertex(matrix4f, f8, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f8, f9, f10).color(f, f2, f3, f4);
            }
            if (!hashSet.contains(blockPos.west()) && hashSet.contains(blockPos.south()) && !hashSet.contains(blockPos.west().south())) {
                bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
            }
            if (hashSet.contains(blockPos.south())) continue;
            if (hashSet.contains(blockPos.west())) {
                if (hashSet.contains(blockPos.south().west())) continue;
                bufferBuilder.vertex(matrix4f, f5, f6, f10).color(f, f2, f3, f4);
                bufferBuilder.vertex(matrix4f, f5, f9, f10).color(f, f2, f3, f4);
                continue;
            }
            if (!b) continue;
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        GL11.glPolygonOffset((float)0.0f, (float)0.0f);
        GL11.glDisable((int)10754);
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    public static void a(MatrixStack matrixStack, Vec3d vec3d, float f, float f2, int n, int n2) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.lineWidth((float)1.5f);
        WorldRenderUtils.a(matrixStack, vec3d, f, n, n2, 0.0f);
        WorldRenderUtils.a(matrixStack, vec3d, f, n, n2, f2);
        Tessellator tessellator = Tessellator.getInstance();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Vec3d vec3d2 = WorldRenderUtils.c.gameRenderer.getCamera().getPos();
        int n3 = n2 >> 16;
        float f3 = (float)((~n3 | 0xFF) - ~n3) / 255.0f;
        int n4 = n2 >> 8;
        float f4 = (float)((~n4 | 0xFF) - ~n4) / 255.0f;
        float f5 = (float)((~n2 | 0xFF) - ~n2) / 255.0f;
        int n5 = n2 >> 24;
        float f6 = (float)((~n5 | 0xFF) - ~n5) / 255.0f;
        if (f6 == 0.0f) {
            f6 = 1.0f;
        }
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < 8; ++i) {
            double d = Math.PI * 2 * (double)i / 8.0;
            double d2 = vec3d.x + (double)f * Math.cos(d) - vec3d2.x;
            double d3 = vec3d.z + (double)f * Math.sin(d) - vec3d2.z;
            double d4 = vec3d.y - vec3d2.y;
            bufferBuilder.vertex(matrix4f, (float)d2, (float)d4, (float)d3).color(f3, f4, f5, f6);
            bufferBuilder.vertex(matrix4f, (float)d2, (float)(d4 + (double)f2), (float)d3).color(f3, f4, f5, f6);
        }
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }
}
