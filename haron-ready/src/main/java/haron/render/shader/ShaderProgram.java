package haron.render.shader;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import ru.haron.Haron;

public class ShaderProgram {
    private static final int a = 0;
    private static final int b = 1;
    private static int e = -1;
    private static int f = -1;
    private static final FloatBuffer g = BufferUtils.createFloatBuffer((int)16);
    private boolean d = true;
    private final Map<String, Integer> h = Maps.newLinkedHashMap();
    private final int c = GL30.glCreateProgram();

    public void e() {
        GL30.glUseProgram((int)0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public void b(String string, int n) {
        GL30.glUniform1i((int)this.a(string), (int)n);
    }

    public boolean b() {
        return this.d;
    }

    public void b(String string, FloatBuffer floatBuffer) {
        RenderSystem.glUniform1((int)this.a(string), (FloatBuffer)floatBuffer);
    }

    public void c(String string, FloatBuffer floatBuffer) {
        GL30.glUniformMatrix4fv((int)this.a(string), (boolean)false, (FloatBuffer)floatBuffer);
    }

    public void c() {
        GL30.glBindAttribLocation((int)this.c, (int)0, (CharSequence)"Position");
        GL30.glBindAttribLocation((int)this.c, (int)1, (CharSequence)"UV0");
        GL30.glLinkProgram((int)this.c);
        if (GL30.glGetProgrami((int)this.c, (int)35714) == 0) {
            Haron.getLOGGER().error("ShaderProgram {} link error: \n{}", (Object)this.c, (Object)GL30.glGetProgramInfoLog((int)this.c));
            this.d = false;
        }
    }

    private static void f() {
        int n = GL30.glGetInteger((int)34229);
        e = GL30.glGenVertexArrays();
        f = GL30.glGenBuffers();
        GL30.glBindVertexArray((int)e);
        GL30.glBindBuffer((int)34962, (int)f);
        GL30.glEnableVertexAttribArray((int)0);
        GL30.glVertexAttribPointer((int)0, (int)3, (int)5126, (boolean)false, (int)20, (long)0L);
        GL30.glEnableVertexAttribArray((int)1);
        GL30.glVertexAttribPointer((int)1, (int)2, (int)5126, (boolean)false, (int)20, (long)12L);
        GL30.glBindVertexArray((int)n);
    }

    public void d() {
        if (this.d) {
            GL30.glUseProgram((int)this.c);
            Matrix4f matrix4f = RenderSystem.getProjectionMatrix();
            Matrix4f matrix4f2 = RenderSystem.getModelViewMatrix();
            g.clear();
            matrix4f.get(g);
            g.rewind();
            int n = this.a("ProjMat");
            if (n != -1) {
                GL30.glUniformMatrix4fv((int)n, (boolean)false, (FloatBuffer)g);
            }
            g.clear();
            matrix4f2.get(g);
            g.rewind();
            int n2 = this.a("ModelViewMat");
            if (n2 != -1) {
                GL30.glUniformMatrix4fv((int)n2, (boolean)false, (FloatBuffer)g);
            }
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            this.a("resolution", minecraftClient.getWindow().getScaledWidth(), minecraftClient.getWindow().getScaledHeight());
        }
    }

    public void a(String string, int n, int n2) {
        GL30.glUniform2i((int)this.a(string), (int)n, (int)n2);
    }

    public void a(String string, FloatBuffer floatBuffer) {
        int n = 532;
        GL30.glUniform1fv((int)this.a(string), (FloatBuffer)floatBuffer);
    }

    public void a(String string, float f, float f2, float f3) {
        GL30.glUniform3f((int)this.a(string), (float)f, (float)f2, (float)f3);
    }

    public void a(String string, int n, int n2, int n3) {
        int n4 = 960;
        GL30.glUniform3i((int)this.a(string), (int)n, (int)n2, (int)n3);
    }

    public void a(String string, int n, int n2, int n3, int n4) {
        GL30.glUniform4i((int)this.a(string), (int)n, (int)n2, (int)n3, (int)n4);
    }

    public void a(String string, float[] fArray) {
        GL30.glUniformMatrix4fv((int)this.a(string), (boolean)false, (float[])fArray);
    }

    public void a(String string, float f) {
        GL30.glUniform1f((int)this.a(string), (float)f);
    }

    public int a(String string) {
        if (!this.h.containsKey(string)) {
            this.h.put(string, GL30.glGetUniformLocation((int)this.c, (CharSequence)string));
        }
        return this.h.get(string);
    }

    public static void a(float[] fArray, int n) {
        if (e == -1) {
            ShaderProgram.f();
        }
        int n2 = GL30.glGetInteger((int)34229);
        GL30.glBindVertexArray((int)e);
        GL30.glBindBuffer((int)34962, (int)f);
        GL30.glBufferData((int)34962, (float[])fArray, (int)35048);
        GL30.glDrawArrays((int)4, (int)0, (int)n);
        GL30.glBindVertexArray((int)n2);
    }

    public static void a(float f, float f2, float f3, float f4) {
        if (e == -1) {
            ShaderProgram.f();
        }
        int n = GL30.glGetInteger((int)34229);
        GL30.glBindVertexArray((int)e);
        GL30.glBindBuffer((int)34962, (int)ShaderProgram.f);
        GL30.glBufferData((int)34962, (float[])new float[]{f, f2 + f4, 0.0f, 0.0f, 1.0f, f + f3, f2 + f4, 0.0f, 1.0f, 1.0f, f + f3, f2, 0.0f, 1.0f, 0.0f, f, f2 + f4, 0.0f, 0.0f, 1.0f, f + f3, f2, 0.0f, 1.0f, 0.0f, f, f2, 0.0f, 0.0f, 0.0f}, (int)35048);
        GL30.glDrawArrays((int)4, (int)0, (int)6);
        GL30.glBindVertexArray((int)n);
    }

    public void a(String string, int n) {
        int n2 = GL30.glCreateShader((int)n);
        GL30.glShaderSource((int)n2, (CharSequence)string);
        GL30.glCompileShader((int)n2);
        if (GL30.glGetShaderi((int)n2, (int)35713) != 0) {
            GL30.glAttachShader((int)this.c, (int)n2);
        } else {
            Haron.getLOGGER().error("Shader {} compile error: \n{}", (Object)n2, (Object)GL30.glGetShaderInfoLog((int)n2));
            this.d = false;
        }
    }

    public void a(String string, Color color) {
        GL30.glUniform4f((int)this.a(string), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public void a(String string, float f, float f2, float f3, float f4) {
        GL30.glUniform4f((int)this.a(string), (float)f, (float)f2, (float)f3, (float)f4);
    }

    public int a() {
        return this.c;
    }

    public void a(String string, float f, float f2) {
        GL30.glUniform2f((int)this.a(string), (float)f, (float)f2);
    }
}

