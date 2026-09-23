package aethereal.config;

public enum ThemeInfo {
   PRIMARY(new ThemeConstructor("primary", 255, 111, 181, 75), new ThemeConstructor("primary", 255, 111, 181, 75)),
   BACKGROUND_HUD(new ThemeConstructor("background_hud", 19, 11, 17, 170), new ThemeConstructor("background_hud", 19, 11, 17, 170)),
   BACKGROUND_GUI(new ThemeConstructor("background_gui", 13, 8, 11, 255), new ThemeConstructor("background_gui", 255, 251, 253, 255)),
   OUTLINE_SMALL(new ThemeConstructor("outline_small", 255, 255, 255, 5), new ThemeConstructor("outline_small", 24, 17, 21, 5)),
   OUTLINE_MEDIUM(new ThemeConstructor("outline_medium", 255, 255, 255, 10), new ThemeConstructor("outline_medium", 24, 17, 21, 5)),
   TEXT(new ThemeConstructor("typography_text", 255, 255, 255, 255), new ThemeConstructor("typography_text", 24, 17, 21, 255)),
   TEXT_DISABLED(new ThemeConstructor("typography_disabled", 92, 68, 82, 255), new ThemeConstructor("typography_disabled", 192, 166, 180, 255));

   private final ThemeConstructor h;
   private final ThemeConstructor i;

   private ThemeInfo(ThemeConstructor dark, ThemeConstructor light) {
      this.h = dark;
      this.i = light;
   }

   public ThemeConstructor a(ThemeType theme) {
      return theme == ThemeType.LIGHT ? this.i : this.h;
   }

   public ThemeConstructor a() {
      return this.i;
   }
}
