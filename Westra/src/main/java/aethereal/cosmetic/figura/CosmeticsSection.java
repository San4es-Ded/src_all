package aethereal.cosmetic.figura;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScissorUtil;
import aethereal.ui.element.Section;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import java.util.List;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class CosmeticsSection extends Section {
   private static final String[] TAB_LABELS = new String[]{"Костюмы", "Аксессуары", "Мечи"};
   private static final CosmeticCategory[] TAB_CATEGORIES = new CosmeticCategory[]{CosmeticCategory.MODELS, CosmeticCategory.HEAD, CosmeticCategory.WEAPONS};
   private final AnimationUtil b = new AnimationUtil();
   private final Vector4f c = new Vector4f();
   private final Vector4f d = new Vector4f();
   private final Vector4f[] e = new Vector4f[]{new Vector4f(), new Vector4f(), new Vector4f()};
   private CosmeticEntry f;
   private CosmeticEntry g;
   private float h;
   private CosmeticCategory i = CosmeticCategory.MODELS;

   public CosmeticsSection() {
      super("P", "cosmetics section");
   }

   private FiguraCosmeticsProcessor processor() {
      return Westra.h().d().w();
   }

   private boolean isWorn(CosmeticEntry entry) {
      return entry.id().equals(this.processor().selected(entry.category()));
   }

   private String categoryName(CosmeticCategory category) {
      return switch (category) {
         case HEAD -> "Аксессуар";
         case WEAPONS -> "Меч";
         default -> "Костюм";
      };
   }

   private List<CosmeticEntry> items() {
      return this.processor().getCatalog().stream().filter(entry -> entry.category() == this.i).toList();
   }

   @Override
   public void a(class_332 context, Vector4f frame, int mouseX, int mouseY, float scroll, float delta) {
      float startX = frame.x + 8.0F;
      float top = frame.y + 24.0F;
      float settingsX = startX + 240.0F - 4.0F + 8.0F;
      this.a(context, frame, startX, frame.y, mouseX, mouseY, delta);
      this.a(context, frame, 44.0F, 6.0F, 4, top, scroll, mouseX, mouseY, delta);
      this.b(context, frame, settingsX, top, mouseX, mouseY, delta);
   }

   private void a(class_332 context, Vector4f frame, float startX, float top, int mouseX, int mouseY, float delta) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float tabW = 52.0F;
      float tabGap = 4.0F;
      float tabH = 16.0F;
      float tabX = startX;

      for (int n = 0; n < TAB_LABELS.length; n++) {
         boolean active = this.i == TAB_CATEGORIES[n];
         boolean hover = MathUtil.a(mouseX, mouseY, tabX, top, tabW, tabH);
         this.e[n].set(tabX, top, tabW, tabH);
         if (active) {
            int tabLeft = ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), 0.3F);
            int tabRight = ColorUtil.a(RecodeKit.accentShade(), 0.12F);
            draw.a(matrices, tabX, top, tabW, tabH, tabH / 2.0F, tabLeft, tabRight, tabLeft, tabRight);
            draw.a(matrices, tabX, top, tabW, tabH, tabH / 2.0F, 0.5F, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), 0.45F));
         } else {
            draw.a(matrices, tabX, top, tabW, tabH, tabH / 2.0F, ColorUtil.a(-1, hover ? 0.06F : 0.03F));
            draw.a(matrices, tabX, top, tabW, tabH, tabH / 2.0F, 0.5F, ColorUtil.a(-1, 0.06F));
         }

         Fonts.d
            .b(
               matrices,
               TAB_LABELS[n],
               tabX + tabW / 2.0F,
               Fonts.d.a(TAB_LABELS[n], 6.5F, top + tabH / 2.0F),
               6.5F,
               active ? -1 : ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), -1, hover ? 0.5F : 0.0F)
            );
         tabX += tabW + tabGap;
      }
   }

   private void a(class_332 context, Vector4f frame, float slot, float gap, int columns, float top, float scroll, int mouseX, int mouseY, float delta) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float startX = frame.x + 8.0F;
      float bottom = frame.y + frame.w;
      List<CosmeticEntry> items = this.items();
      this.h = 0.0F;
      this.g = null;
      if (this.f == null && !items.isEmpty()) {
         this.f = items.getFirst();
      }

      ScissorUtil.a(matrices, frame.x, top, frame.z, bottom - top);

      for (int n = 0; n < items.size(); n++) {
         CosmeticEntry item = items.get(n);
         float slotX = startX + n % columns * (slot + gap);
         float slotY = top + n / columns * (slot + gap) + scroll;
         this.h = Math.max(this.h, (n / columns + 1) * (slot + gap) - gap);
         if (slotY + slot >= top && slotY <= bottom) {
            if (MathUtil.a(mouseX, mouseY, slotX, slotY, slot, slot)) {
               this.g = item;
            }

            boolean selected = item == this.f;
            boolean worn = this.isWorn(item);
            if (selected) {
               float var10000 = 1.0F;
            } else {
               float var35 = this.g == item ? 0.5F : 0.0F;
            }

            int accent = theme.a(ThemeInfo.PRIMARY).a();
            draw.a(matrices, slotX, slotY, slot, slot, 8.0F, ColorUtil.a(-1, 0.03F + 0.035F * (this.g == item ? 1.0F : 0.0F)));
            if (selected) {
               int slotTop = ColorUtil.a(accent, 0.22F);
               int slotBottom = ColorUtil.a(RecodeKit.accentShade(), 0.06F);
               draw.a(matrices, slotX, slotY, slot, slot, 8.0F, slotTop, slotTop, slotBottom, slotBottom);
            }

            draw.a(matrices, slotX, slotY, slot, slot, 8.0F, 0.5F, selected ? ColorUtil.a(accent, 0.6F) : ColorUtil.a(-1, 0.06F));
            class_2960 tex = CosmeticTextureCache.get(item.preview());
            if (tex != null) {
               int[] size = CosmeticTextureCache.size(item.preview());
               float scale = Math.min(slot / size[0], slot / size[1]) * 0.8F;
               float w = size[0] * scale;
               float h2 = size[1] * scale;
               draw.a(matrices, tex, slotX + (slot - w) / 2.0F, slotY + (slot - h2) / 2.0F, w, h2, 4.0F, -1);
            } else {
               Fonts.a.a(matrices, "P", slotX + (slot - 16.0F) / 2.0F, slotY + (slot - 16.0F) / 2.0F, 16.0F, theme.a(ThemeInfo.TEXT_DISABLED).a());
            }

            if (worn) {
               float badge = 9.0F;
               float badgeX = slotX + slot - badge - 3.0F;
               float badgeY = slotY + slot - badge - 3.0F;
               draw.a(matrices, badgeX, badgeY, badge, badge, badge / 2.0F, theme.a(ThemeInfo.PRIMARY).a());
               Fonts.a.a(matrices, "m", badgeX + (badge - Fonts.a.b("m", 6.0F)) / 2.0F, Fonts.a.a("m", 6.0F, badgeY + badge / 2.0F), 6.0F, -1);
            }
         }
      }

      ScissorUtil.a(matrices);
   }

   private void b(class_332 context, Vector4f frame, float settingsX, float y, int mouseX, int mouseY, float delta) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float width = frame.x + frame.z - 8.0F - settingsX;
      if (this.f != null) {
         float rowX = settingsX + 8.0F;
         float rowW = width - 16.0F;
         float nameY = y - 18.0F;
         draw.a(matrices, settingsX, nameY - 8.0F, width, frame.y + frame.w - (nameY - 8.0F) - 6.0F, 9.0F, ColorUtil.a(-1, 0.025F));
         draw.a(matrices, settingsX, nameY - 8.0F, width, frame.y + frame.w - (nameY - 8.0F) - 6.0F, 9.0F, 0.5F, ColorUtil.a(-1, 0.06F));
         Fonts.d.c(matrices, this.f.name(), rowX, nameY, 7.5F, -1, rowW);
         float pv = 44.0F;
         class_2960 tex = CosmeticTextureCache.get(this.f.preview());
         if (tex != null) {
            int[] size = CosmeticTextureCache.size(this.f.preview());
            float scale = Math.min(pv / size[0], pv / size[1]);
            float w = size[0] * scale;
            float h2 = size[1] * scale;
            draw.a(matrices, tex, rowX + (rowW - w) / 2.0F, y + 2.0F, w, h2, 6.0F, -1);
         }

         Fonts.c.b(matrices, this.categoryName(this.f.category()), rowX + rowW / 2.0F, y + 2.0F + pv + 8.0F, 6.75F, theme.a(ThemeInfo.TEXT_DISABLED).a());
         boolean worn = this.isWorn(this.f);
         float btnY = frame.y + frame.w - 26.0F;
         float btnH = 16.0F;
         this.c.set(rowX, btnY, rowW, btnH);
         boolean hover = MathUtil.a(mouseX, mouseY, rowX, btnY, rowW, btnH);
         this.b.a(hover);
         this.b.a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
         float on = this.b.c();
         int tone = worn ? ColorUtil.a(255, 90, 110, 255) : theme.a(ThemeInfo.PRIMARY).a();
         int toneEnd = worn ? ColorUtil.a(200, 60, 120, 255) : RecodeKit.accentShade();
         int btnLeft = ColorUtil.a(tone, 0.55F + 0.35F * on);
         int btnRight = ColorUtil.a(toneEnd, 0.45F + 0.35F * on);
         draw.a(matrices, rowX, btnY, rowW, btnH, btnH / 2.0F, btnLeft, btnRight, btnLeft, btnRight);
         draw.a(matrices, rowX, btnY, rowW, btnH, btnH / 2.0F, 0.5F, ColorUtil.a(-1, 0.12F + 0.1F * on));
         String action = worn ? "Снять" : "Надеть";
         Fonts.d.b(matrices, action, rowX + rowW / 2.0F, Fonts.d.a(action, 7.0F, btnY + btnH / 2.0F), 7.0F, -1);
      }
   }

   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return false;
      } else {
         for (int n = 0; n < this.e.length; n++) {
            if (MathUtil.a(mouseX, mouseY, this.e[n].x, this.e[n].y, this.e[n].z, this.e[n].w)) {
               this.i = TAB_CATEGORIES[n];
               this.f = null;
               return true;
            }
         }

         if (MathUtil.a(mouseX, mouseY, this.c.x, this.c.y, this.c.z, this.c.w)) {
            if (this.f != null) {
               if (this.isWorn(this.f)) {
                  this.processor().unequip(this.f.category());
               } else {
                  this.processor().equip(this.f);
               }
            }

            return true;
         } else if (this.g != null && this.g != this.f) {
            this.f = this.g;
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public float a(Vector4f frame) {
      return this.h;
   }
}
