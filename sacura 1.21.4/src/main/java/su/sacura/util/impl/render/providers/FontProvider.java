 package su.sacura.util.impl.render.providers;
 
 import su.sacura.util.impl.render.msdf.api.MsdfFont;
 import su.sacura.util.impl.render.msdf.render.FontRender;
 
 public final class FontProvider {
   public static final FontRender sfmedium = create("SF Medium", "sf_medium");
   
   public static final FontRender regular = create("Sans Regular", "productsans_regular");
   
   public static final FontRender logo = create("Client Logo", "logo");
   
   public static final FontRender icons = create("Icons Font", "icons");
   
   public static final FontRender monoton = create("Monoton Font", "monoton");
   
   public static final FontRender arrows = create("Arrows Icon", "arrow");
   
   public static final FontRender category = create("Category Icon", "category");
   
   private static FontRender create(String name, String fileName) {
     return new FontRender(MsdfFont.builder().name(name).data(fileName).atlas(fileName).build());
   }
 }


