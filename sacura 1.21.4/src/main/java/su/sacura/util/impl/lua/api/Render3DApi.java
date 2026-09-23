package su.sacura.util.impl.lua.api;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class Render3DApi implements LuaApi {
    public void register(Globals globals) {
        LuaTable luaTable = LuaValue.tableOf();

        luaTable.set("box", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                double x = args.checkdouble(1);
                double y = args.checkdouble(2);
                double z = args.checkdouble(3);
                double w = args.checkdouble(4);
                double h = args.checkdouble(5);
                double d = args.checkdouble(6);
                int color = args.checkint(7);
                boolean filled = args.optboolean(8, true);

                Render3DApi.this.drawBox(x, y, z, w, h, d, color, filled);
                return LuaValue.NIL;
            }
        });

        luaTable.set("line", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                double x1 = args.checkdouble(1);
                double y1 = args.checkdouble(2);
                double z1 = args.checkdouble(3);
                double x2 = args.checkdouble(4);
                double y2 = args.checkdouble(5);
                double z2 = args.checkdouble(6);
                int color = args.checkint(7);
                float width = (float)args.optdouble(8, 1.0D);

                Render3DApi.this.drawLine(x1, y1, z1, x2, y2, z2, color, width);
                return LuaValue.NIL;
            }
        });

        globals.set("render3d", luaTable);
    }

    private void drawBox(double x, double y, double z, double w, double h, double d, int color, boolean filled) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();

        double minX = x - camPos.x;
        double minY = y - camPos.y;
        double minZ = z - camPos.z;
        double maxX = minX + w;
        double maxY = minY + h;
        double maxZ = minZ + d;

        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        // Исправлено: field_53876 -> ShaderProgramKeys.POSITION_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.disableDepthTest();

        if (filled) {
            // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            buffer.vertex((float)minX, (float)minY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)minY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)minY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)minY, (float)maxZ).color(r, g, b, a);

            buffer.vertex((float)minX, (float)maxY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)maxY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)minZ).color(r, g, b, a);

            buffer.vertex((float)minX, (float)minY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)maxY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)minY, (float)minZ).color(r, g, b, a);

            buffer.vertex((float)minX, (float)minY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)minY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)maxY, (float)maxZ).color(r, g, b, a);

            buffer.vertex((float)minX, (float)minY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)minY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)maxY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)minX, (float)maxY, (float)minZ).color(r, g, b, a);

            buffer.vertex((float)maxX, (float)minY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)minZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a);
            buffer.vertex((float)maxX, (float)minY, (float)maxZ).color(r, g, b, a);

            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, int color, float width) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();

        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        // Исправлено: field_53876 -> ShaderProgramKeys.POSITION_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.lineWidth(width);

        // Исправлено: field_29344 -> VertexFormat.DrawMode.LINES
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);

        buffer.vertex((float)(x1 - camPos.x), (float)(y1 - camPos.y), (float)(z1 - camPos.z)).color(r, g, b, a);
        buffer.vertex((float)(x2 - camPos.x), (float)(y2 - camPos.y), (float)(z2 - camPos.z)).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.enableDepthTest();
    }
}