package pulse.gui.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.joml.Matrix3x2fStack;
import pulse.render.Renderer2D;
import pulse.render.Renderer2DImpl;
import pulse.render.font.FontManager;
import pulse.render.font.FontRenderer;
import pulse.render.icons.IconTextureRegistry;
import ru.pulse.Pulse;

public class PulseKeybindManagerScreen extends Screen {
   public static final List<PulseKeybindManagerScreen.BindItem> BINDS = new ArrayList<>();
   private static final String[] MODULE_SUGGESTIONS = new String[]{
      "Fast Exp",
      "Sprint",
      "Free Look",
      "Auto Eat",
      "Elytra Swap",
      "Full Bright",
      "Target Hud",
      "Watermark",
      "Target Esp",
      "Crosshair",
      "Shulker Preview",
      "Auto Potion",
      "Auto Respawn",
      "Auto Reconnect",
      "Item Scroller",
      "Sound Controller",
      "Streamer Mode",
      "Auto Leave",
      "Item Swap",
      "Auto Invest",
      "Block Overlay"
   };
   private final Screen parent;
   private String selectedKeyName = null;
   private float popoverX = 0.0F;
   private float popoverY = 0.0F;
   private int popoverStep = 0;
   private int actionTab = 0;
   private String commandInput = "/";
   private String functionInput = "";
   private boolean isQuickBindActive = false;
   private int quickBindStep = 0;
   private String quickBindKeyLabel = null;
   private static final Color VIOLET_ACCENT = new Color(110, 70, 255);
   private static final Color KEY_HOVER = new Color(50, 50, 75, 180);
   private static final Color KEY_BG = new Color(28, 28, 40, 250);
   private static final Color TEXT_MUTED = new Color(150, 155, 180);

   public PulseKeybindManagerScreen(Screen parent) {
      super(Text.literal("Keybind Manager"));
      this.parent = parent;
   }

   private boolean isBound(String keyName) {
      for (PulseKeybindManagerScreen.BindItem b : BINDS) {
         if (b.keyName.equalsIgnoreCase(keyName)) {
            return true;
         }
      }

      return false;
   }

   private String getBestSuggestion(String input) {
      if (input != null && !input.trim().isEmpty()) {
         String clean = input.trim().toLowerCase();

         for (String m : MODULE_SUGGESTIONS) {
            if (m.toLowerCase().startsWith(clean)) {
               return m;
            }
         }

         return "";
      } else {
         return "";
      }
   }

   private void openKeyPopover(String label, float px, float py) {
      if (this.isQuickBindActive && this.quickBindStep == 1) {
         this.quickBindKeyLabel = "Key " + label;
         this.selectedKeyName = label;
         this.quickBindStep = 2;
      } else {
         this.selectedKeyName = label;
         this.popoverX = px;
         this.popoverY = py;
         this.popoverStep = 1;
         this.isQuickBindActive = false;
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      int w = this.width;
      int h = this.height;
      context.fill(0, 0, w, h, -183825644);
      if (Pulse.getInstance().getRender() instanceof Renderer2DImpl impl) {
         impl.setDrawContext(context);
      }

      Renderer2D r = Pulse.getInstance().getRender();
      Matrix3x2fStack m = context.getMatrices();
      float layoutW = 850.0F;
      float layoutH = 360.0F;
      float layoutX = (w - layoutW) / 2.0F;
      float layoutY = (h - layoutH) / 2.0F;
      Identifier bgTex = IconTextureRegistry.get("keybind_bg");
      if (bgTex != null) {
         Renderer2DImpl.setTextureFilter(bgTex, true);
         r.a(bgTex, layoutX, layoutY, layoutW, layoutH, Color.WHITE, m);
      }

      float kbX = layoutX + 218.0F;
      float kbY = layoutY + 68.0F;
      this.renderKeyboardHighlights(r, m, kbX, kbY, mouseX, mouseY);
      float listX = layoutX + 20.0F;
      float listY = layoutY + 90.0F;
      FontRenderer bTitleF = FontManager.b[13];
      FontRenderer bSubF = FontManager.a[10];
      FontRenderer badgeF = FontManager.b[11];

      for (int i = 0; i < BINDS.size(); i++) {
         PulseKeybindManagerScreen.BindItem item = BINDS.get(i);
         float cardY = listY + i * 42.0F;
         if (cardY + 38.0F > layoutY + 340.0F) {
            break;
         }

         r.a(listX, cardY, 155.0F, 38.0F, 6.0F, new Color(26, 26, 38, 240), m);
         bTitleF.a(item.actionText, listX + 8.0F, cardY + 6.0F, Color.WHITE, m);
         bSubF.a(item.actionType, listX + 8.0F, cardY + 21.0F, TEXT_MUTED, m);
         float bgW = badgeF.a(item.keyName) + 14.0F;
         r.a(listX + 155.0F - bgW - 6.0F, cardY + 8.0F, bgW, 22.0F, 4.0F, VIOLET_ACCENT, m);
         badgeF.a(item.keyName, listX + 155.0F - bgW, cardY + 12.0F, Color.WHITE, m);
      }

      float pillX = layoutX + 665.0F;
      float headerY = layoutY + 10.0F;
      if (this.isQuickBindActive) {
         this.renderQuickBindPopovers(r, m, pillX - 75.0F, headerY + 30.0F, mouseX, mouseY);
      } else if (this.popoverStep > 0 && this.selectedKeyName != null) {
         this.renderPopovers(r, m, mouseX, mouseY);
      }

      super.render(context, mouseX, mouseY, delta);
   }

   private void renderKeyboardHighlights(Renderer2D r, Object m, float kx, float ky, int mx, int my) {
      FontRenderer kf = FontManager.b[12];
      String[][] keyRows = new String[][]{
         {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "BACK"},
         {"TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"},
         {"CAPS", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "ENTER"},
         {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "SHIFT"},
         {"CTRL", "FN", "ALT", "SPACE", "ALT", "FN", "MENU", "CTRL"}
      };
      float keyH = 26.0F;
      float gap = 4.0F;

      for (int row = 0; row < keyRows.length; row++) {
         float ry = ky + row * (keyH + gap);
         float rx = kx;

         for (int col = 0; col < keyRows[row].length; col++) {
            String label = keyRows[row][col];
            float kw = this.getKeyWidth(label);
            boolean hov = this.isHovered(rx, ry, kw, keyH, mx, my);
            boolean bound = this.isBound(label);
            boolean isSelected = label.equalsIgnoreCase(this.selectedKeyName) && (this.popoverStep > 0 || this.isQuickBindActive);
            if (!bound && !isSelected) {
               if (hov) {
                  r.a(rx, ry, kw, keyH, 4.0F, KEY_HOVER, m);
               }
            } else {
               Color bg = bound ? VIOLET_ACCENT : new Color(80, 60, 150);
               r.a(rx, ry, kw, keyH, 4.0F, bg, m);
               float txtW = kf.a(label);
               kf.a(label, rx + (kw - txtW) / 2.0F, ry + 6.0F, Color.WHITE, m);
            }

            rx += kw + gap;
         }
      }
   }

   private float getKeyWidth(String key) {
      return switch (key) {
         case "BACK" -> 44.0F;
         case "TAB" -> 36.0F;
         case "\\" -> 24.0F;
         case "CAPS" -> 40.0F;
         case "ENTER" -> 44.0F;
         case "SHIFT" -> 50.0F;
         case "SPACE" -> 132.0F;
         case "CTRL", "ALT", "FN" -> 28.0F;
         case "MENU" -> 30.0F;
         default -> 23.0F;
      };
   }

   private void renderQuickBindPopovers(Renderer2D r, Object m, float qx, float qy, int mx, int my) {
      FontRenderer bf = FontManager.b[15];
      FontRenderer sf = FontManager.a[11];
      float q1W = 240.0F;
      float q1H = 95.0F;
      r.a(qx - 4.0F, qy + 10.0F, 4.0F, 8.0F, 1.0F, new Color(22, 22, 34, 252), m);
      r.a(qx, qy, q1W, q1H, 8.0F, new Color(22, 22, 34, 252), m);
      r.a(qx, qy, q1W, q1H, 8.0F, 1.2F, new Color(55, 50, 85, 220), m);
      bf.a("Quick bind", qx + 14.0F, qy + 11.0F, Color.WHITE, m);
      sf.a("Press a key or mouse button", qx + 12.0F, qy + 31.0F, TEXT_MUTED, m);
      sf.a("to choose a bind target", qx + 12.0F, qy + 43.0F, TEXT_MUTED, m);
      float btnY = qy + 58.0F;
      if (this.quickBindStep >= 2 && this.quickBindKeyLabel != null) {
         r.a(qx + 12.0F, btnY, 216.0F, 28.0F, 6.0F, VIOLET_ACCENT, m);
         FontRenderer kF = FontManager.b[13];
         float txtW = kF.a(this.quickBindKeyLabel);
         kF.a(this.quickBindKeyLabel, qx + (216.0F - txtW) / 2.0F, btnY + 7.0F, Color.WHITE, m);
      } else {
         r.a(qx + 12.0F, btnY, 216.0F, 28.0F, 6.0F, new Color(26, 26, 38), m);
         FontRenderer plF = FontManager.b[12];
         plF.a("KEY / BUTTON", qx + 55.0F, btnY + 7.0F, new Color(110, 110, 135), m);
      }

      if (this.quickBindStep >= 2) {
         float my2 = qy + q1H + 8.0F;
         this.renderActionModal(r, m, qx, my2, mx, my);
      }
   }

   private void renderPopovers(Renderer2D r, Object m, int mx, int my) {
      FontRenderer bf = FontManager.b[13];
      float p1W = 230.0F;
      float p1H = 36.0F;
      float px1 = this.popoverX;
      float py1 = this.popoverY;
      r.a(px1 + 24.0F, py1 - 4.0F, 8.0F, 4.0F, 1.0F, new Color(22, 22, 34, 252), m);
      r.a(px1, py1, p1W, p1H, 6.0F, new Color(22, 22, 34, 252), m);
      r.a(px1, py1, p1W, p1H, 6.0F, 1.2F, new Color(55, 50, 85, 220), m);
      FontRenderer badgeF = FontManager.b[12];
      float kw = badgeF.a(this.selectedKeyName) + 14.0F;
      r.a(px1 + 8.0F, py1 + 6.0F, kw, 24.0F, 4.0F, new Color(34, 34, 48), m);
      badgeF.a(this.selectedKeyName, px1 + 15.0F, py1 + 10.0F, Color.WHITE, m);
      bf.a("Key", px1 + 15.0F + kw + 8.0F, py1 + 10.0F, Color.WHITE, m);
      float addBtnX = px1 + p1W - 84.0F;
      float addBtnY = py1 + 6.0F;
      boolean addH = this.isHovered(addBtnX, addBtnY, 76.0F, 24.0F, mx, my);
      r.a(addBtnX, addBtnY, 76.0F, 24.0F, 5.0F, addH ? VIOLET_ACCENT : new Color(36, 30, 56), m);
      FontRenderer addF = FontManager.b[12];
      addF.a("+ Add", addBtnX + 18.0F, addBtnY + 5.0F, addH ? Color.WHITE : new Color(160, 150, 220), m);
      if (this.popoverStep >= 2) {
         float my2 = py1 + 42.0F;
         this.renderActionModal(r, m, px1, my2, mx, my);
      }
   }

   private void renderActionModal(Renderer2D r, Object m, float mx2, float my2, int mx, int my) {
      FontRenderer sf = FontManager.a[11];
      float m2W = 245.0F;
      float m2H = 155.0F;
      r.a(mx2 + 24.0F, my2 - 4.0F, 8.0F, 4.0F, 1.0F, new Color(22, 22, 34, 252), m);
      r.a(mx2, my2, m2W, m2H, 8.0F, new Color(22, 22, 34, 252), m);
      r.a(mx2, my2, m2W, m2H, 8.0F, 1.2F, new Color(60, 55, 95, 230), m);
      r.a(mx2 + 10.0F, my2 + 10.0F, 16.0F, 16.0F, 4.0F, VIOLET_ACCENT, m);
      FontRenderer iconF = FontManager.b[12];
      iconF.a("+", mx2 + 15.0F, my2 + 10.0F, Color.WHITE, m);
      FontRenderer mTitleF = FontManager.b[15];
      mTitleF.a("Add action", mx2 + 32.0F, my2 + 9.0F, Color.WHITE, m);
      sf.a("Choose what should run", mx2 + 10.0F, my2 + 29.0F, TEXT_MUTED, m);
      sf.a("when this bind is pressed.", mx2 + 10.0F, my2 + 41.0F, TEXT_MUTED, m);
      float tabY = my2 + 58.0F;
      float tabW = 108.0F;
      float tabH = 26.0F;
      boolean t0H = this.isHovered(mx2 + 10.0F, tabY, tabW, tabH, mx, my);
      Color t0Bg = this.actionTab == 0 ? VIOLET_ACCENT : (t0H ? KEY_HOVER : KEY_BG);
      r.a(mx2 + 10.0F, tabY, tabW, tabH, 5.0F, t0Bg, m);
      FontRenderer tabF = FontManager.b[12];
      tabF.a("/A Command", mx2 + 18.0F, tabY + 6.0F, this.actionTab != 0 && !t0H ? TEXT_MUTED : Color.WHITE, m);
      boolean t1H = this.isHovered(mx2 + 125.0F, tabY, tabW, tabH, mx, my);
      Color t1Bg = this.actionTab == 1 ? VIOLET_ACCENT : (t1H ? KEY_HOVER : KEY_BG);
      r.a(mx2 + 125.0F, tabY, tabW, tabH, 5.0F, t1Bg, m);
      tabF.a(">- Function", mx2 + 133.0F, tabY + 6.0F, this.actionTab != 1 && !t1H ? TEXT_MUTED : Color.WHITE, m);
      float inY = tabY + 32.0F;
      r.a(mx2 + 10.0F, inY, 225.0F, 24.0F, 4.0F, new Color(16, 16, 26, 255), m);
      r.a(mx2 + 10.0F, inY, 225.0F, 24.0F, 4.0F, 1.0F, new Color(70, 60, 110, 200), m);
      String cursor = System.currentTimeMillis() % 1000L > 500L ? "|" : "";
      FontRenderer inF = FontManager.b[13];
      if (this.actionTab == 0) {
         inF.a(this.commandInput + cursor, mx2 + 16.0F, inY + 5.0F, Color.WHITE, m);
      } else {
         String curText = this.functionInput;
         String best = this.getBestSuggestion(curText);
         if (!curText.isEmpty()) {
            inF.a(curText + cursor, mx2 + 16.0F, inY + 5.0F, Color.WHITE, m);
            if (!best.isEmpty() && best.toLowerCase().startsWith(curText.toLowerCase())) {
               String ghost = best.substring(curText.length());
               float typedW = inF.a(curText + cursor);
               inF.a(ghost, mx2 + 16.0F + typedW, inY + 5.0F, new Color(110, 110, 135), m);
            }
         } else {
            inF.a("Fast Exp" + cursor, mx2 + 16.0F, inY + 5.0F, new Color(110, 110, 135), m);
         }
      }

      float saveY = inY + 30.0F;
      String curVal = this.actionTab == 0
         ? this.commandInput.trim()
         : (this.getBestSuggestion(this.functionInput).isEmpty() ? this.functionInput.trim() : this.getBestSuggestion(this.functionInput));
      boolean canSave = this.actionTab == 0 ? curVal.length() > 1 : !curVal.isEmpty();
      boolean saveH = canSave && this.isHovered(mx2 + 10.0F, saveY, 225.0F, 22.0F, mx, my);
      r.a(mx2 + 10.0F, saveY, 225.0F, 22.0F, 5.0F, saveH ? VIOLET_ACCENT : new Color(32, 28, 50), m);
      FontRenderer sF = FontManager.b[12];
      sF.a("+ Add", mx2 + 100.0F, saveY + 4.0F, saveH ? Color.WHITE : (canSave ? new Color(160, 150, 220) : TEXT_MUTED), m);
   }

   private void saveCurrentBind() {
      if (this.selectedKeyName != null) {
         String typeStr = this.actionTab == 0 ? "Command" : "Function";
         String textVal;
         if (this.actionTab == 0) {
            textVal = this.commandInput.trim();
         } else {
            String sugg = this.getBestSuggestion(this.functionInput);
            textVal = sugg.isEmpty() ? (this.functionInput.trim().isEmpty() ? "Fast Exp" : this.functionInput.trim()) : sugg;
         }

         if (!textVal.isEmpty()) {
            BINDS.add(new PulseKeybindManagerScreen.BindItem(this.selectedKeyName, typeStr, textVal));
         }

         this.popoverStep = 0;
         this.isQuickBindActive = false;
         this.quickBindStep = 0;
         this.selectedKeyName = null;
      }
   }

   public boolean mouseClicked(Click click, boolean bl) {
      int mx = (int)click.x();
      int my = (int)click.y();
      int w = this.width;
      int h = this.height;
      float layoutW = 850.0F;
      float layoutH = 360.0F;
      float layoutX = (w - layoutW) / 2.0F;
      float layoutY = (h - layoutH) / 2.0F;
      float pillW = 125.0F;
      float pillH = 26.0F;
      float pillX = layoutX + 665.0F;
      float headerY = layoutY + 10.0F;
      if (this.isHovered(pillX, headerY - 2.0F, pillW, pillH, mx, my)) {
         this.isQuickBindActive = true;
         this.quickBindStep = 1;
         this.quickBindKeyLabel = null;
         this.selectedKeyName = null;
         this.popoverStep = 0;
         return true;
      }

      if (this.isQuickBindActive && this.quickBindStep >= 2) {
         float qx = pillX - 75.0F;
         float qy = headerY + 30.0F;
         float my2 = qy + 95.0F + 8.0F;
         float tabY = my2 + 58.0F;
         if (this.isHovered(qx + 10.0F, tabY, 108.0F, 26.0F, mx, my)) {
            this.actionTab = 0;
            return true;
         }

         if (this.isHovered(qx + 125.0F, tabY, 108.0F, 26.0F, mx, my)) {
            this.actionTab = 1;
            return true;
         }

         float saveY = tabY + 62.0F;
         if (this.isHovered(qx + 10.0F, saveY, 225.0F, 22.0F, mx, my)) {
            this.saveCurrentBind();
            return true;
         }
      }

      if (this.popoverStep > 0 && this.selectedKeyName != null) {
         float px1 = this.popoverX;
         float py1 = this.popoverY;
         if (this.isHovered(px1 + 146.0F, py1 + 6.0F, 76.0F, 24.0F, mx, my)) {
            this.popoverStep = 2;
            this.actionTab = 0;
            this.commandInput = "/";
            return true;
         }

         if (this.popoverStep >= 2) {
            float mx2 = px1;
            float my2 = py1 + 42.0F;
            float tabY = my2 + 58.0F;
            if (this.isHovered(mx2 + 10.0F, tabY, 108.0F, 26.0F, mx, my)) {
               this.actionTab = 0;
               return true;
            }

            if (this.isHovered(mx2 + 125.0F, tabY, 108.0F, 26.0F, mx, my)) {
               this.actionTab = 1;
               return true;
            }

            float saveY = tabY + 62.0F;
            if (this.isHovered(mx2 + 10.0F, saveY, 225.0F, 22.0F, mx, my)) {
               this.saveCurrentBind();
               return true;
            }
         }
      }

      if (this.isHovered(layoutX + 20.0F, layoutY + 10.0F, 75.0F, 24.0F, mx, my)) {
         if (this.client != null) {
            this.client.setScreen(this.parent);
         }

         return true;
      } else {
         float kbX = layoutX + 218.0F;
         float kbY = layoutY + 68.0F;
         String[][] keyRows = new String[][]{
            {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "BACK"},
            {"TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"},
            {"CAPS", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "ENTER"},
            {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "SHIFT"},
            {"CTRL", "FN", "ALT", "SPACE", "ALT", "FN", "MENU", "CTRL"}
         };
         float keyH = 26.0F;
         float gap = 4.0F;

         for (int row = 0; row < keyRows.length; row++) {
            float ry = kbY + row * (keyH + gap);
            float rx = kbX;

            for (int col = 0; col < keyRows[row].length; col++) {
               String label = keyRows[row][col];
               float kw = this.getKeyWidth(label);
               if (this.isHovered(rx, ry, kw, keyH, mx, my)) {
                  this.openKeyPopover(label, rx, ry + keyH + 6.0F);
                  return true;
               }

               rx += kw + gap;
            }
         }

         float mcx = layoutX + 758.0F;
         float mcy = layoutY + 200.0F;
         if (this.isHovered(mcx - 30.0F, mcy - 65.0F, 20.0F, 32.0F, mx, my)) {
            this.openKeyPopover("M1", mcx - 30.0F, mcy - 30.0F);
            return true;
         }

         if (this.isHovered(mcx + 10.0F, mcy - 65.0F, 20.0F, 32.0F, mx, my)) {
            this.openKeyPopover("M2", mcx + 10.0F, mcy - 30.0F);
            return true;
         }

         if (this.isHovered(mcx - 7.0F, mcy - 58.0F, 14.0F, 24.0F, mx, my)) {
            this.openKeyPopover("M3", mcx - 10.0F, mcy - 30.0F);
            return true;
         }

         if (this.isHovered(mcx - 39.0F, mcy - 14.0F, 9.0F, 20.0F, mx, my)) {
            this.openKeyPopover("M4", mcx - 40.0F, mcy - 10.0F);
            return true;
         }

         if (this.isHovered(mcx - 39.0F, mcy + 14.0F, 9.0F, 20.0F, mx, my)) {
            this.openKeyPopover("M5", mcx - 40.0F, mcy + 20.0F);
            return true;
         }

         if (this.isHovered(mcx + 30.0F, mcy + 14.0F, 9.0F, 20.0F, mx, my)) {
            this.openKeyPopover("M6", mcx + 30.0F, mcy + 20.0F);
            return true;
         }

         if (this.isHovered(mcx + 30.0F, mcy - 14.0F, 9.0F, 20.0F, mx, my)) {
            this.openKeyPopover("M7", mcx + 30.0F, mcy - 10.0F);
            return true;
         }

         if (this.isHovered(kbX, layoutY + 310.0F, 120.0F, 20.0F, mx, my)) {
            BINDS.clear();
            this.popoverStep = 0;
            this.isQuickBindActive = false;
            this.selectedKeyName = null;
            return true;
         }

         if (!this.isQuickBindActive) {
            this.popoverStep = 0;
            this.selectedKeyName = null;
         }

         return super.mouseClicked(click, bl);
      }
   }

   public boolean charTyped(CharInput charInput) {
      if (this.popoverStep >= 2 || this.isQuickBindActive && this.quickBindStep >= 2) {
         char chr = (char)charInput.codepoint();
         if (this.actionTab == 0) {
            if (chr >= ' ' && chr != 127 && this.commandInput.length() < 30) {
               this.commandInput = this.commandInput + chr;
               return true;
            }
         } else if (chr >= ' ' && chr != 127 && this.functionInput.length() < 30) {
            this.functionInput = this.functionInput + chr;
            return true;
         }

         return true;
      } else {
         return super.charTyped(charInput);
      }
   }

   public boolean keyPressed(KeyInput keyInput) {
      int key = keyInput.key();
      if (this.isQuickBindActive && this.quickBindStep == 1) {
         if (key == 256) {
            this.isQuickBindActive = false;
            this.quickBindStep = 0;
            return true;
         } else {
            String kName = this.getKeyNameFromCode(key);
            this.quickBindKeyLabel = "Key " + kName;
            this.selectedKeyName = kName;
            this.quickBindStep = 2;
            return true;
         }
      } else {
         if (this.popoverStep >= 2 || this.isQuickBindActive && this.quickBindStep >= 2) {
            if (key == 256) {
               this.popoverStep = 0;
               this.isQuickBindActive = false;
               this.quickBindStep = 0;
               this.selectedKeyName = null;
               return true;
            }

            if (key == 257 || key == 335 || key == 258) {
               if (this.actionTab == 1) {
                  String best = this.getBestSuggestion(this.functionInput);
                  if (!best.isEmpty()) {
                     this.functionInput = best;
                  }
               }

               this.saveCurrentBind();
               return true;
            }

            if (key == 259) {
               if (this.actionTab == 0) {
                  if (this.commandInput.length() > 1) {
                     this.commandInput = this.commandInput.substring(0, this.commandInput.length() - 1);
                  }
               } else if (!this.functionInput.isEmpty()) {
                  this.functionInput = this.functionInput.substring(0, this.functionInput.length() - 1);
               }

               return true;
            }
         }

         if (key == 256) {
            if (this.client != null) {
               this.client.setScreen(this.parent);
            }

            return true;
         } else {
            return super.keyPressed(keyInput);
         }
      }
   }

   private String getKeyNameFromCode(int keyCode) {
      return switch (keyCode) {
         case 32 -> "SPACE";
         case 67 -> "C";
         case 69 -> "E";
         case 70 -> "F";
         case 71 -> "G";
         case 81 -> "Q";
         case 82 -> "R";
         case 86 -> "V";
         case 88 -> "X";
         case 90 -> "Z";
         case 258 -> "TAB";
         case 340, 344 -> "SHIFT";
         case 341, 345 -> "CTRL";
         case 342, 346 -> "ALT";
         default -> keyCode >= 65 && keyCode <= 90
            ? String.valueOf((char)keyCode)
            : (keyCode >= 48 && keyCode <= 57 ? String.valueOf((char)keyCode) : "KEY_" + keyCode);
      };
   }

   private boolean isHovered(float x, float y, float w, float h, int mx, int my) {
      return mx >= x && mx <= x + w && my >= y && my <= y + h;
   }

   public static class BindItem {
      public String keyName;
      public String actionType;
      public String actionText;

      public BindItem(String keyName, String actionType, String actionText) {
         this.keyName = keyName;
         this.actionType = actionType;
         this.actionText = actionText;
      }
   }
}
