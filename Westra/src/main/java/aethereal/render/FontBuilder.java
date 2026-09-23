package aethereal.render;

import aethereal.core.Interface;
import com.google.gson.Gson;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.class_1044;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import platform.client.processors.draw.fonts.FontData;

public class FontBuilder implements Interface {
   private final Gson b = new Gson();
   private class_2960 c;
   private class_2960 d;
   private String e;

   public FontBuilder a(String fontName) {
      this.e = fontName;
      this.c = class_2960.method_60655("westra", "fonts/" + fontName + ".json");
      this.d = class_2960.method_60655("westra", "fonts/" + fontName + ".png");
      return this;
   }

   public Font a() {
      FontData data = this.b();
      class_1044 texture = this.c();
      Map<Integer, MsdfGlyph> glyphs = this.a(data);
      Map<Integer, Map<Integer, Float>> kernings = this.b(data);
      return new Font(this.e, texture, data.atlas(), data.metrics(), glyphs, kernings);
   }

   private FontData b() {
      FontData data = (FontData)this.b.fromJson(this.a(this.c), FontData.class);
      if (data == null) {
         throw new RuntimeException("Failed to read font data file: " + this.c + ". Are you sure this is a valid JSON file? Check its syntax.");
      } else {
         return data;
      }
   }

   private class_1044 c() {
      class_1044 texture = class_310.method_1551().method_1531().method_4619(this.d);
      RenderSystem.recordRenderCall(() -> texture.method_4527(true, false));
      return texture;
   }

   private Map<Integer, MsdfGlyph> a(FontData data) {
      float atlasWidth = data.atlas().width();
      float atlasHeight = data.atlas().height();
      return data.glyphs().stream().collect(Collectors.toMap(v0 -> v0.unicode(), glyphData -> new MsdfGlyph(glyphData, atlasWidth, atlasHeight)));
   }

   private Map<Integer, Map<Integer, Float>> b(FontData data) {
      Map<Integer, Map<Integer, Float>> kernings = new HashMap<>();
      data.kernings().forEach(kerning -> {
         Map<Integer, Float> kerningMap = kernings.computeIfAbsent(kerning.leftChar(), k -> new HashMap<>());
         kerningMap.put(kerning.rightChar(), kerning.advance());
      });
      return kernings;
   }

   private String a(class_2960 identifier) {
      try {
         InputStream inputStream = aM_.method_1478().open(identifier);

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
               String str = reader.lines().collect(Collectors.joining("\n"));
               reader.close();
               if (inputStream != null) {
                  inputStream.close();
               }

               return str;
            } catch (Throwable var8) {
               try {
                  reader.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }
         } catch (Throwable var9) {
            if (inputStream != null) {
               try {
                  inputStream.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }
      } catch (IOException var10) {
         throw new RuntimeException("Failed to read resource: " + identifier, var10);
      }
   }
}
