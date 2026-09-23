package wtf.wyvern.core.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.base.color.ColorUtil;

public class ThemeManager {
   private static final int WHITE = new Color(255, 255, 255, 255).getRGB();
   private Theme currentTheme;
   private final List<Theme> themes = new ArrayList();
   private final Theme defaultTheme = theme("Cyber Blue", 66, 112, 255);

   public ThemeManager() {
      this.initThemes();
   }

   private void initThemes() {
      if (this.currentTheme == null) {
         this.currentTheme = this.defaultTheme;
      }

      this.themes.addAll(List.of(new Theme[]{
         this.defaultTheme,
         theme("Olive", 166, 170, 72),
         theme("Violet Void", 155, 72, 255),
         theme("Sunset", 255, 104, 112),
         theme("Neon Green", 75, 235, 105),
         theme("Abyss Blue", 36, 86, 205),
         theme("Sakura", 255, 132, 180),
         theme("Cyber Dragon", 235, 62, 112),
         theme("Aurora", 72, 218, 166),
         theme("Matrix", 70, 205, 92),
         theme("Hellfire", 255, 67, 52),
         theme("Frozen Soul", 105, 185, 255),
         theme("Gold Dust", 241, 196, 62),
         theme("Neon Wave", 240, 72, 210),
         theme("Desert Rose", 226, 126, 134),
         theme("Ghost", 156, 166, 190),
         theme("Ruby Red", 225, 55, 91),
         theme("Oceanic", 34, 145, 226),
         theme("Forest", 48, 166, 94),
         theme("Cyberpunk", 220, 64, 255),
         theme("Solar", 255, 151, 48),
         theme("Deep Sea", 31, 92, 190),
         theme("Autumn", 213, 119, 61),
         theme("Polar", 116, 202, 239),
         theme("Volcanic", 232, 72, 48),
         theme("Royal", 111, 88, 242),
         theme("Pastel Pink", 246, 143, 194),
         theme("Grape", 125, 72, 218),
         theme("Steel", 98, 139, 180),
         theme("Ocean", 36, 119, 214),
         theme("Fire", 246, 76, 65),
         theme("Night", 75, 91, 171),
         theme("Rose Gold", 211, 117, 135),
         theme("Deep Purple", 112, 64, 191),
         theme("Toxic", 151, 224, 62),
         theme("Ice", 105, 210, 244),
         theme("Electric", 76, 108, 255),
         theme("Candy", 255, 104, 201),
         theme("Starlight", 102, 119, 214),
         theme("Emerald Dream", 45, 187, 117),
         theme("Synthwave", 224, 62, 185),
         theme("Bloodlust", 177, 43, 69),
         theme("Thunder", 129, 119, 214),
         theme("Mocha", 185, 126, 103),
         theme("Amethyst", 177, 91, 235),
         theme("Arctic", 62, 190, 218),
         theme("Shadow", 77, 80, 103),
         theme("Moonlight", 116, 139, 229),
         theme("Celestial", 92, 155, 255),
         theme("Obsidian Rose", 205, 72, 135),
         theme("Mint Aurora", 65, 211, 171),
         theme("Solar Flare", 255, 177, 58),
         theme("Blue Hour", 70, 105, 229),
         theme("Vampire", 157, 51, 112),
         theme("Azure", 55, 151, 255),
         theme("Cobalt", 47, 86, 226),
         theme("Indigo", 91, 72, 230),
         theme("Lavender", 167, 137, 245),
         theme("Orchid", 213, 91, 230),
         theme("Magenta", 245, 75, 190),
         theme("Coral", 255, 106, 91),
         theme("Amber", 255, 181, 55),
         theme("Lemon", 221, 218, 72),
         theme("Lime", 143, 215, 68),
         theme("Jade", 52, 182, 139),
         theme("Turquoise", 47, 203, 193),
         theme("Seafoam", 78, 209, 170),
         theme("Sky", 91, 185, 250),
         theme("Frost", 125, 205, 255),
         theme("Graphite", 99, 108, 132),
         theme("Silver", 164, 172, 194),
         theme("Copper", 205, 117, 78),
         theme("Peach", 248, 147, 112),
         theme("Cherry", 223, 60, 108),
         theme("Raspberry", 205, 57, 131),
         theme("Cosmic", 117, 74, 224),
         theme("Dream", 139, 111, 245),
         theme("Twilight", 92, 101, 205)
      }));
   }

   private static Theme theme(String name, int red, int green, int blue) {
      return new Theme(name, new Color(red, green, blue, 255).getRGB(), WHITE);
   }

   public ColorRGBA getClientColor(int index) {
      return this.currentTheme == null ? new ColorRGBA(255, 255, 255, 255) : ColorUtil.gradient(3, index, this.currentTheme.getColor(), this.currentTheme.getSecondColor());
   }

   @Generated
   public Theme getCurrentTheme() {
      return this.currentTheme;
   }

   @Generated
   public List<Theme> getThemes() {
      return this.themes;
   }

   @Generated
   public Theme getDefaultTheme() {
      return this.defaultTheme;
   }

   @Generated
   public void setCurrentTheme(Theme currentTheme) {
      this.currentTheme = currentTheme;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ThemeManager)) {
         return false;
      } else {
         ThemeManager other = (ThemeManager)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$currentTheme = this.getCurrentTheme();
               Object other$currentTheme = other.getCurrentTheme();
               if (this$currentTheme == null) {
                  if (other$currentTheme == null) {
                     break label47;
                  }
               } else if (this$currentTheme.equals(other$currentTheme)) {
                  break label47;
               }

               return false;
            }

            Object this$themes = this.getThemes();
            Object other$themes = other.getThemes();
            if (this$themes == null) {
               if (other$themes != null) {
                  return false;
               }
            } else if (!this$themes.equals(other$themes)) {
               return false;
            }

            Object this$defaultTheme = this.getDefaultTheme();
            Object other$defaultTheme = other.getDefaultTheme();
            if (this$defaultTheme == null) {
               if (other$defaultTheme != null) {
                  return false;
               }
            } else if (!this$defaultTheme.equals(other$defaultTheme)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ThemeManager;
   }

   @Generated
   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $currentTheme = this.getCurrentTheme();
      result = result * 59 + ($currentTheme == null ? 43 : $currentTheme.hashCode());
      Object $themes = this.getThemes();
      result = result * 59 + ($themes == null ? 43 : $themes.hashCode());
      Object $defaultTheme = this.getDefaultTheme();
      result = result * 59 + ($defaultTheme == null ? 43 : $defaultTheme.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getCurrentTheme());
      return "ThemeManager(currentTheme=" + var10000 + ", themes=" + String.valueOf(this.getThemes()) + ", defaultTheme=" + String.valueOf(this.getDefaultTheme()) + ")";
   }
}
