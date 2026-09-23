 package su.sacura.util.impl.lua.render;
 
 import java.awt.Font;
 import java.io.File;
 import java.util.HashMap;
 import java.util.Map;
 import net.minecraft.client.MinecraftClient;
 
 public class LuaFontManager {
   private static final Map<String, LuaFontRenderer> fonts = new HashMap<>();
   
   public static void init() {
     File folder = new File((MinecraftClient.getInstance()).runDirectory, "sacura/lua/fonts");
     if (!folder.exists())
       folder.mkdirs(); 
     File[] files = folder.listFiles((dir, name) -> name.endsWith(".ttf"));
     if (files == null)
       return; 
     for (File file : files) {
       try {
         Font font = Font.createFont(0, file).deriveFont(18.0F);
         String name = file.getName().replace(".ttf", "");
         fonts.put(name, new LuaFontRenderer(font));
         System.out.println("Loaded custom font: " + name);
       } catch (Exception e) {
         e.printStackTrace();
       } 
     } 
   }
   
   public static LuaFontRenderer getFont(String name) {
     return fonts.get(name);
   }
 }


