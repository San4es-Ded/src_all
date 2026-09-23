package su.sacura.util.impl.lua.api;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.lwjgl.glfw.GLFW;
import su.sacura.util.impl.lua.render.LuaFontManager;
import su.sacura.util.impl.lua.render.LuaFontRenderer;
import su.sacura.util.impl.render.RenderUtils;

public class RenderApi implements LuaApi {
    public static DrawContext currentContext;

    private static final Map<String, Vec2> dragPositions = new HashMap<>();
    private static String draggingId = null;
    private static float dragOffsetX;
    private static float dragOffsetY;
    private static boolean wasMouseDown = false;

    private int getColor(LuaValue val) {
        if (val.istable()) {
            int r = val.get(1).checkint();
            int g = val.get(2).checkint();
            int b = val.get(3).checkint();
            int a = val.get(4).optint(255);
            return a << 24 | r << 16 | g << 8 | b;
        }
        return (int)val.checklong();
    }

    public void register(Globals globals) {
        LuaTable luaTable = LuaValue.tableOf();

        luaTable.set("rect", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                if (RenderApi.currentContext == null)
                    return LuaValue.NIL;
                float x = (float)args.checkdouble(1);
                float y = (float)args.checkdouble(2);
                float w = (float)args.checkdouble(3);
                float h = (float)args.checkdouble(4);

                int color = RenderApi.this.getColor(args.arg(5));

                float a = (color >> 24 & 0xFF) / 255.0F;
                float r = (color >> 16 & 0xFF) / 255.0F;
                float g = (color >> 8 & 0xFF) / 255.0F;
                float b = (color & 0xFF) / 255.0F;

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableDepthTest();
                // Исправлено: field_53876 -> ShaderProgramKeys.POSITION_COLOR
                RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

                Tessellator tessellator = Tessellator.getInstance();
                // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
                BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

                Matrix4f matrix = RenderApi.currentContext.getMatrices().peek().getPositionMatrix();
                float x2 = x + w;
                float y2 = y + h;

                buffer.vertex(matrix, x, y2, 0.0F).color(r, g, b, a);
                buffer.vertex(matrix, x2, y2, 0.0F).color(r, g, b, a);
                buffer.vertex(matrix, x2, y, 0.0F).color(r, g, b, a);
                buffer.vertex(matrix, x, y, 0.0F).color(r, g, b, a);

                BufferRenderer.drawWithGlobalProgram(buffer.end());

                return LuaValue.NIL;
            }
        });

        luaTable.set("text", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                if (RenderApi.currentContext == null)
                    return LuaValue.NIL;
                String text = args.checkjstring(1);
                float x = (float)args.checkdouble(2);
                float y = (float)args.checkdouble(3);

                int color = RenderApi.this.getColor(args.arg(4));
                String fontName = args.optjstring(5, null);
                float scale = (float)args.optdouble(6, 1.0D);

                if (fontName != null) {
                    LuaFontRenderer font = LuaFontManager.getFont(fontName);
                    if (font != null) {
                        font.drawString(RenderApi.currentContext.getMatrices().peek().getPositionMatrix(), text, x, y, color, scale);
                        return LuaValue.NIL;
                    }
                }
                RenderApi.currentContext.drawText((MinecraftClient.getInstance()).textRenderer, text, (int)x, (int)y, color, true);
                return LuaValue.NIL;
            }
        });

        // Исправлено: new VarArgFunction(this) -> new VarArgFunction()
        luaTable.set("getWidth", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                String text = args.checkjstring(1);
                String fontName = args.optjstring(2, null);
                float scale = (float)args.optdouble(3, 1.0D);

                if (fontName != null) {
                    LuaFontRenderer font = LuaFontManager.getFont(fontName);
                    if (font != null)
                        return LuaValue.valueOf(font.getWidth(text, scale));
                }
                return LuaValue.valueOf((MinecraftClient.getInstance()).textRenderer.getWidth(text));
            }
        });

        // Исправлено: new VarArgFunction(this) -> new VarArgFunction()
        luaTable.set("getResolution", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
                int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(width), LuaValue.valueOf(height) });
            }
        });

        luaTable.set("roundRect", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                if (RenderApi.currentContext == null)
                    return LuaValue.NIL;
                float x = (float)args.checkdouble(1);
                float y = (float)args.checkdouble(2);
                float w = (float)args.checkdouble(3);
                float h = (float)args.checkdouble(4);
                float rad = (float)args.checkdouble(5);
                int color = RenderApi.this.getColor(args.arg(6));

                RenderUtils.rect(RenderApi.currentContext.getMatrices().peek().getPositionMatrix(), x, y, w, h, rad, color);

                return LuaValue.NIL;
            }
        });

        luaTable.set("gradient", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                if (RenderApi.currentContext == null)
                    return LuaValue.NIL;
                float x = (float)args.checkdouble(1);
                float y = (float)args.checkdouble(2);
                float w = (float)args.checkdouble(3);
                float h = (float)args.checkdouble(4);
                int c1 = RenderApi.this.getColor(args.arg(5));
                int c2 = RenderApi.this.getColor(args.arg(6));
                boolean vertical = args.optboolean(7, false);

                float a1 = (c1 >> 24 & 0xFF) / 255.0F;
                float r1 = (c1 >> 16 & 0xFF) / 255.0F;
                float g1 = (c1 >> 8 & 0xFF) / 255.0F;
                float b1 = (c1 & 0xFF) / 255.0F;

                float a2 = (c2 >> 24 & 0xFF) / 255.0F;
                float r2 = (c2 >> 16 & 0xFF) / 255.0F;
                float g2 = (c2 >> 8 & 0xFF) / 255.0F;
                float b2 = (c2 & 0xFF) / 255.0F;

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableDepthTest();
                // Исправлено: field_53876 -> ShaderProgramKeys.POSITION_COLOR
                RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

                Tessellator tessellator = Tessellator.getInstance();
                // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
                BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

                Matrix4f matrix = RenderApi.currentContext.getMatrices().peek().getPositionMatrix();
                float x2 = x + w;
                float y2 = y + h;

                if (vertical) {
                    buffer.vertex(matrix, x, y2, 0.0F).color(r2, g2, b2, a2);
                    buffer.vertex(matrix, x2, y2, 0.0F).color(r2, g2, b2, a2);
                    buffer.vertex(matrix, x2, y, 0.0F).color(r1, g1, b1, a1);
                    buffer.vertex(matrix, x, y, 0.0F).color(r1, g1, b1, a1);
                } else {
                    buffer.vertex(matrix, x, y2, 0.0F).color(r1, g1, b1, a1);
                    buffer.vertex(matrix, x2, y2, 0.0F).color(r2, g2, b2, a2);
                    buffer.vertex(matrix, x2, y, 0.0F).color(r2, g2, b2, a2);
                    buffer.vertex(matrix, x, y, 0.0F).color(r1, g1, b1, a1);
                }

                BufferRenderer.drawWithGlobalProgram(buffer.end());

                return LuaValue.NIL;
            }
        });

        luaTable.set("border", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                if (RenderApi.currentContext == null)
                    return LuaValue.NIL;
                float x = (float)args.checkdouble(1);
                float y = (float)args.checkdouble(2);
                float w = (float)args.checkdouble(3);
                float h = (float)args.checkdouble(4);
                float thickness = (float)args.checkdouble(5);
                int color = RenderApi.this.getColor(args.arg(6));
                float radius = (float)args.optdouble(7, 0.0D);

                RenderUtils.border(RenderApi.currentContext.getMatrices().peek().getPositionMatrix(), x, y, w, h, radius, thickness, 1.0F, 1.0F, color);

                return LuaValue.NIL;
            }
        });

        // Исправлено: new VarArgFunction(this) -> new VarArgFunction()
        luaTable.set("drag", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                String id = args.checkjstring(1);
                float defaultX = (float)args.checkdouble(2);
                float defaultY = (float)args.checkdouble(3);
                float w = (float)args.checkdouble(4);
                float h = (float)args.checkdouble(5);

                MinecraftClient mc = MinecraftClient.getInstance();
                double mouseX = mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth();
                double mouseY = mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight();
                boolean isMouseDown = (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1);

                Vec2 pos = RenderApi.dragPositions.computeIfAbsent(id, k -> new Vec2(defaultX, defaultY));

                if (mc.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen) {
                    if (id.equals(RenderApi.draggingId)) {
                        pos.setX((float)(mouseX - RenderApi.dragOffsetX));
                        pos.setY((float)(mouseY - RenderApi.dragOffsetY));
                        if (!isMouseDown)
                            RenderApi.draggingId = null;
                    } else if (RenderApi.draggingId == null &&
                            isMouseDown && !RenderApi.wasMouseDown &&
                            mouseX >= pos.getX() && mouseX <= (pos.getX() + w) && mouseY >= pos.getY() && mouseY <= (pos.getY() + h)) {
                        RenderApi.draggingId = id;
                        RenderApi.dragOffsetX = (float)(mouseX - pos.getX());
                        RenderApi.dragOffsetY = (float)(mouseY - pos.getY());
                    }
                } else if (id.equals(RenderApi.draggingId)) {
                    RenderApi.draggingId = null;
                }

                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(pos.getX()), LuaValue.valueOf(pos.getY()) });
            }
        });

        globals.set("render", luaTable);
    }

    public static void updateInput() {
        MinecraftClient mc = MinecraftClient.getInstance();
        wasMouseDown = (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1);
    }
}