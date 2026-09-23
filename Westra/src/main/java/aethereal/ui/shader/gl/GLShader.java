package aethereal.ui.shader.gl;

import aethereal.core.Interface;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public class GLShader {
   @Generated
   private static final Logger_2 a = LoggerFactory.a(GLShader.class);
   private static final FloatBuffer b = BufferUtils.createFloatBuffer(16);
   private static FloatBuffer c = BufferUtils.createFloatBuffer(1024);
   private static int d = -1;
   private static int e = -1;
   private final int f = GL30.glCreateProgram();
   private final Map<String, Integer> g = new HashMap<>();
   private boolean h = true;

   @Generated
   public int a() {
      return this.f;
   }

   @Generated
   public boolean b() {
      return this.h;
   }

   public void a(String source, int stage) {
      int shader = GL30.glCreateShader(stage);
      GL30.glShaderSource(shader, source);
      GL30.glCompileShader(shader);
      if (GL30.glGetShaderi(shader, 35713) != 0) {
         GL30.glAttachShader(this.f, shader);
         GL30.glDeleteShader(shader);
      } else {
         a.f("Ошибка компиляции шейдера {}:\n{}", new Object[]{shader, GL30.glGetShaderInfoLog(shader)});
         GL30.glDeleteShader(shader);
         this.h = false;
      }
   }

   public void c() {
      GL30.glBindAttribLocation(this.f, 0, "Position");
      GL30.glBindAttribLocation(this.f, 1, "UV0");
      GL30.glBindAttribLocation(this.f, 2, "Color");
      GL30.glLinkProgram(this.f);
      if (GL30.glGetProgrami(this.f, 35714) == 0) {
         a.f("Ошибка линковки программы {}:\n{}", new Object[]{this.f, GL30.glGetProgramInfoLog(this.f)});
         this.h = false;
      }
   }

   public void d() {
      this.d(new Matrix4f());
   }

   public void d(Matrix4f modelView) {
      if (this.h) {
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         GL30.glUseProgram(this.f);
         int projection = this.a("ProjMat");
         if (projection != -1) {
            RenderSystem.getProjectionMatrix().get(b);
            b.rewind();
            GL30.glUniformMatrix4fv(projection, false, b);
         }

         int model = this.a("ModelViewMat");
         if (model != -1) {
            modelView.get(b);
            b.rewind();
            GL30.glUniformMatrix4fv(model, false, b);
         }

         this.a("resolution", Interface.aM_.method_22683().method_4486(), Interface.aM_.method_22683().method_4502());
      }
   }

   public void e() {
      GL30.glUseProgram(0);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
   }

   public static void a(float x, float y, float width, float height) {
      a(new float[]{x, y, 0.0F, 0.0F, 0.0F, x, y + height, 0.0F, 0.0F, 1.0F, x + width, y + height, 0.0F, 1.0F, 1.0F, x + width, y, 0.0F, 1.0F, 0.0F}, 6);
   }

   public static void a(float[] vertices, int mode) {
      if (d == -1) {
         f();
      }

      if (c.capacity() < vertices.length) {
         c = BufferUtils.createFloatBuffer(vertices.length * 2);
      }

      int previousArray = GL30.glGetInteger(34229);
      int previousBuffer = GL30.glGetInteger(34964);
      GL30.glBindVertexArray(d);
      GL30.glBindBuffer(34962, e);
      c.clear();
      c.put(vertices);
      c.flip();
      GL30.glBufferData(34962, c, 35048);
      GL30.glEnableVertexAttribArray(0);
      GL30.glVertexAttribPointer(0, 3, 5126, false, 20, 0L);
      GL30.glEnableVertexAttribArray(1);
      GL30.glVertexAttribPointer(1, 2, 5126, false, 20, 12L);
      GL30.glDrawArrays(mode, 0, vertices.length / 5);
      GL30.glBindBuffer(34962, previousBuffer);
      GL30.glBindVertexArray(previousArray);
   }

   private static void f() {
      int previousArray = GL30.glGetInteger(34229);
      int previousBuffer = GL30.glGetInteger(34964);
      d = GL30.glGenVertexArrays();
      e = GL30.glGenBuffers();
      GL30.glBindVertexArray(d);
      GL30.glBindBuffer(34962, e);
      GL30.glEnableVertexAttribArray(0);
      GL30.glVertexAttribPointer(0, 3, 5126, false, 20, 0L);
      GL30.glEnableVertexAttribArray(1);
      GL30.glVertexAttribPointer(1, 2, 5126, false, 20, 12L);
      GL30.glBindBuffer(34962, previousBuffer);
      GL30.glBindVertexArray(previousArray);
   }

   public int a(String name) {
      Integer cached = this.g.get(name);
      if (cached == null) {
         cached = GL30.glGetUniformLocation(this.f, name);
         this.g.put(name, cached);
      }

      return cached;
   }

   public void a(String name, float value) {
      int location = this.a(name);
      if (location != -1) {
         GL30.glUniform1f(location, value);
      }
   }

   public void b(String name, int value) {
      int location = this.a(name);
      if (location != -1) {
         GL30.glUniform1i(location, value);
      }
   }

   public void a(String name, float x, float y) {
      int location = this.a(name);
      if (location != -1) {
         GL30.glUniform2f(location, x, y);
      }
   }

   public void a(String name, float x, float y, float z) {
      int location = this.a(name);
      if (location != -1) {
         GL30.glUniform3f(location, x, y, z);
      }
   }

   public void a(String name, float x, float y, float z, float w) {
      int location = this.a(name);
      if (location != -1) {
         GL30.glUniform4f(location, x, y, z, w);
      }
   }

   public void a(String name, Color color) {
      if (color != null) {
         this.a(name, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
      }
   }

   public void c(String name, int argb) {
      this.a(name, (argb >> 16 & 0xFF) / 255.0F, (argb >> 8 & 0xFF) / 255.0F, (argb & 0xFF) / 255.0F, (argb >>> 24 & 0xFF) / 255.0F);
   }
}
