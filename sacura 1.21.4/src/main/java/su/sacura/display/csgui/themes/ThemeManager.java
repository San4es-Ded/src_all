 package su.sacura.display.csgui.themes;
 
 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonParser;
 import java.awt.Color;
 import java.io.File;
 import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.Reader;
 import java.io.Writer;
 import java.util.ArrayList;
 import java.util.List;
 import su.sacura.display.csgui.helper.ThemeStorage;
 
 public class ThemeManager {
   public static final List<Theme> themes = new ArrayList<>();
   
   private static final File THEME_DIR = new File("run/sacura/themes");
   
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   
   public static void init() {
     if (!THEME_DIR.exists())
       THEME_DIR.mkdirs(); 
     themes.add(new Theme("Темная", "System", new Color(17, 16, 19, 255), new Color(113, 106, 223, 255), new Color(255, 255, 255, 255), new Color(60, 60, 60, 255)));
     themes.add(new Theme("Красная", "System", new Color(20, 10, 10, 255), new Color(225, 60, 60, 255), new Color(255, 255, 255, 255), new Color(100, 40, 40, 255)));
     themes.add(new Theme("Фиолетовая", "System", new Color(20, 10, 20, 255), new Color(150, 60, 225, 255), new Color(255, 255, 255, 255), new Color(80, 40, 100, 255)));
     themes.add(new Theme("Матрица", "System", new Color(10, 20, 10, 255), new Color(50, 255, 50, 255), new Color(200, 255, 200, 255), new Color(20, 100, 20, 255)));
     themes.add(new Theme("Океан", "System", new Color(10, 20, 30, 255), new Color(0, 150, 255, 255), new Color(220, 240, 255, 255), new Color(0, 60, 100, 255)));
     themes.add(new Theme("Закат", "System", new Color(30, 15, 20, 255), new Color(255, 100, 50, 255), new Color(255, 220, 200, 255), new Color(100, 40, 20, 255)));
     loadThemes();
   }
   
   public static void loadThemes() {
     File[] files = THEME_DIR.listFiles((dir, name) -> name.endsWith(".json"));
     if (files == null)
       return; 
     for (File file : files) {
       try {
         Reader reader = new FileReader(file);
         try {
           JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
           String name = json.get("name").getAsString();
           String author = json.get("author").getAsString();
           if (themes.stream().anyMatch(t -> t.name.equalsIgnoreCase(name))) {
             reader.close();
           } else {
             JsonObject colors = json.getAsJsonObject("colors");
             Color bg = new Color(colors.get("background").getAsInt(), true);
             Color accent = new Color(colors.get("accent").getAsInt(), true);
             Color text = new Color(colors.get("text").getAsInt(), true);
             Color border = new Color(colors.get("border").getAsInt(), true);
             themes.add(new Theme(name, author, bg, accent, text, border));
             reader.close();
           } 
         } catch (Throwable throwable) {
           try {
             reader.close();
           } catch (Throwable throwable1) {
             throwable.addSuppressed(throwable1);
           } 
           throw throwable;
         } 
       } catch (Exception e) {
         e.printStackTrace();
       } 
     } 
   }
   
   public static void saveTheme(Theme theme) {
     if (theme.author.equalsIgnoreCase("System"))
       return; 
     JsonObject json = new JsonObject();
     json.addProperty("name", theme.name);
     json.addProperty("author", theme.author);
     JsonObject colors = new JsonObject();
     colors.addProperty("background", (Number)theme.backgroundColor.get());
     colors.addProperty("accent", (Number)theme.accentColor.get());
     colors.addProperty("text", (Number)theme.textColor.get());
     colors.addProperty("border", (Number)theme.borderColor.get());
     json.add("colors", (JsonElement)colors);
     try {
       Writer writer = new FileWriter(new File(THEME_DIR, theme.name + ".json"));
       try {
         GSON.toJson((JsonElement)json, writer);
         writer.close();
       } catch (Throwable throwable) {
         try {
           writer.close();
         } catch (Throwable throwable1) {
           throwable.addSuppressed(throwable1);
         } 
         throw throwable;
       } 
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public static void deleteTheme(Theme theme) {
     if (theme.author.equalsIgnoreCase("System"))
       return; 
     themes.remove(theme);
     (new File(THEME_DIR, theme.name + ".json")).delete();
   }
   
   public static Theme createNewTheme(String name, String author) {
     Theme newTheme = new Theme(name, author, new Color(20, 20, 20), new Color(100, 100, 255), new Color(255, 255, 255), new Color(50, 50, 50));
     themes.add(newTheme);
     saveTheme(newTheme);
     return newTheme;
   }
   
   public static void setTheme(Theme theme) {
     ThemeStorage.backgroundColor = new Color(((Integer)theme.backgroundColor.get()).intValue(), true);
     ThemeStorage.accentColor = new Color(((Integer)theme.accentColor.get()).intValue(), true);
     ThemeStorage.textColor = new Color(((Integer)theme.textColor.get()).intValue(), true);
     ThemeStorage.borderColor = new Color(((Integer)theme.borderColor.get()).intValue(), true);
   }
 }


