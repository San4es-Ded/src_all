package aethereal.ui.screen;

import aethereal.build.BuildData;
import aethereal.build.BuildManager;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScaleUtil;
import aethereal.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_4608;
import net.minecraft.class_7833;
import net.minecraft.class_827;
import net.minecraft.class_4597.class_4598;
import org.lwjgl.opengl.GL11;

public class BuildViewerScreen extends class_437 implements Interface {
   private static final String[] FILTERS = new String[]{"ALL", "BUILTIN", "USER"};
   private static final String[] FILTER_NAMES = new String[]{"Все", "Встр.", "Мои"};
   private static final float ROW = 22.0F;
   private final AnimationUtil open = new AnimationUtil();
   private List<BuildData> builds;
   private BuildData selected;
   private String filter = "ALL";
   private float scroll;
   private float pitch = 25.0F;
   private float yaw = 45.0F;
   private float zoom = 1.0F;
   private boolean dragging;
   private float screenW = 480.0F;
   private float screenH = 270.0F;

   public BuildViewerScreen() {
      this(null);
   }

   public BuildViewerScreen(BuildData build) {
      super(class_2561.method_43470(""));
      this.builds = BuildManager.get().all();
      this.selected = build != null ? build : (this.builds.isEmpty() ? null : this.builds.getFirst());
   }

   private List<BuildData> filtered() {
      String var1 = this.filter;

      return switch (var1) {
         case "BUILTIN" -> this.builds.stream().filter(build -> build.source() == BuildData.Source.BUILTIN).toList();
         case "USER" -> this.builds.stream().filter(build -> build.source() == BuildData.Source.USER).toList();
         default -> this.builds;
      };
   }

   private void select(BuildData build) {
      this.selected = build;
      this.pitch = 25.0F;
      this.yaw = 45.0F;
      this.zoom = 1.0F;
   }

   private float panelWidth() {
      return Math.min(490.0F, this.screenW * 0.92F);
   }

   private float panelHeight() {
      return Math.min(310.0F, this.screenH * 0.92F);
   }

   private float panelX() {
      return (this.screenW - this.panelWidth()) / 2.0F;
   }

   private float panelY() {
      return (this.screenH - this.panelHeight()) / 2.0F;
   }

   private float sideX() {
      return this.panelX() + 8.0F;
   }

   private float sideY() {
      return this.panelY() + 38.0F;
   }

   private float sideWidth() {
      return 118.0F;
   }

   private float sideHeight() {
      return this.panelHeight() - 46.0F;
   }

   private float viewX() {
      return this.sideX() + this.sideWidth() + 6.0F;
   }

   private float viewWidth() {
      return this.panelX() + this.panelWidth() - 8.0F - this.viewX();
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      double mx = MathUtil.scale(mouseX, 2);
      double my = MathUtil.scale(mouseY, 2);
      ScaleUtil.a(context, 2);
      this.screenW = aM_.method_22683().method_4486();
      this.screenH = aM_.method_22683().method_4502();
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      this.open.a(0.0F, 1.0F, 0.3F, EasingList.g, delta);
      this.open.a(aM_.field_1755 == this);
      float alpha = this.open.c();
      float panelX = this.panelX();
      float panelY = this.panelY();
      float panelW = this.panelWidth();
      float panelH = this.panelHeight();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      draw.b(matrices, panelX, panelY, panelW, panelH, 8.0F, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), alpha));
      draw.a(matrices, panelX, panelY, panelW, panelH, 8.0F, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), alpha));
      draw.a(matrices, panelX, panelY, panelW, panelH, 8.0F, 0.5F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), alpha));
      this.header(matrices, draw, theme, alpha);
      draw.a(matrices, panelX + 8.0F, panelY + 34.0F, panelW - 16.0F, 0.5F, 0.0F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), alpha));
      this.sidebar(matrices, draw, theme, mx, my, alpha, accent);
      this.viewport(context, matrices, draw, theme, alpha);
      ScaleUtil.a(context);
   }

   private void header(class_4587 matrices, Draw2DProcessor draw, ThemeProcessor theme, float alpha) {
      float x = this.panelX() + 10.0F;
      float y = this.panelY();
      Fonts.d.a(matrices, "Build Viewer", x, y + 9.0F, 9.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), alpha));
      Fonts.c.a(matrices, "ЛКМ — вращение  |  Колесо — зум", x, y + 22.0F, 6.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), alpha));
      if (this.selected != null) {
         String size = this.selected.solidCount() + " блоков  |  " + this.selected.sizeX() + "x" + this.selected.sizeY() + "x" + this.selected.sizeZ();
         float right = this.panelX() + this.panelWidth() - 10.0F;
         Fonts.d.a(matrices, size, right - Fonts.d.a(size, 7.0F), y + 9.0F, 7.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), alpha));
         String author = "Автор: " + this.selected.author() + "  |  " + (this.selected.source() == BuildData.Source.BUILTIN ? "Встроенная" : "Своя");
         Fonts.c.a(matrices, author, right - Fonts.c.a(author, 6.0F), y + 22.0F, 6.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), alpha));
      }
   }

   private void sidebar(class_4587 matrices, Draw2DProcessor draw, ThemeProcessor theme, double mx, double my, float alpha, int accent) {
      float x = this.sideX();
      float y = this.sideY();
      float width = this.sideWidth();
      float height = this.sideHeight();
      draw.a(matrices, x, y, width, height, 6.0F, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), 0.35F * alpha));
      float chipX = x + 5.0F;
      float chipY = y + 5.0F;

      for (int index = 0; index < FILTERS.length; index++) {
         float chipWidth = Fonts.c.a(FILTER_NAMES[index], 6.5F) + 12.0F;
         boolean active = this.filter.equals(FILTERS[index]);
         boolean hovered = MathUtil.a(mx, my, chipX, chipY, chipWidth, 13.0F);
         int background = active ? ColorUtil.a(accent, 0.23F * alpha) : ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), (hovered ? 0.08F : 0.023529412F) * alpha);
         draw.a(matrices, chipX, chipY, chipWidth, 13.0F, 6.5F, background);
         if (active) {
            draw.a(matrices, chipX, chipY, chipWidth, 13.0F, 6.5F, 0.5F, ColorUtil.a(accent, 0.5F * alpha));
         }

         int color = active ? theme.a(ThemeInfo.TEXT).a() : theme.a(ThemeInfo.TEXT_DISABLED).a();
         Fonts.c.a(matrices, FILTER_NAMES[index], chipX + 6.0F, Fonts.c.a(FILTER_NAMES[index], 6.5F, chipY + 6.5F), 6.5F, ColorUtil.a(color, alpha));
         chipX += chipWidth + 4.0F;
      }

      List<BuildData> list = this.filtered();
      float listY = y + 23.0F;
      float listHeight = height - 28.0F;
      this.scroll = Math.max(0.0F, Math.min(this.scroll, Math.max(0.0F, list.size() * 22.0F - listHeight)));

      for (int index = 0; index < list.size(); index++) {
         float rowY = listY + index * 22.0F - this.scroll;
         if (!(rowY + 22.0F < listY) && !(rowY > listY + listHeight)) {
            BuildData build = list.get(index);
            boolean active = build == this.selected;
            boolean hovered = MathUtil.a(mx, my, x + 4.0F, rowY, width - 8.0F, 20.0F) && my >= listY && my <= listY + listHeight;
            int background = active ? ColorUtil.a(accent, 0.18F * alpha) : ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), (hovered ? 0.08F : 0.023529412F) * alpha);
            draw.a(matrices, x + 4.0F, rowY, width - 8.0F, 20.0F, 5.0F, background);
            if (active) {
               draw.a(matrices, x + 4.0F, rowY, width - 8.0F, 20.0F, 5.0F, 0.5F, ColorUtil.a(accent, 0.55F * alpha));
            }

            int dot = build.source() == BuildData.Source.BUILTIN ? accent : theme.a(ThemeInfo.TEXT_DISABLED).a();
            draw.a(matrices, x + 10.0F, rowY + 6.0F, 3.0F, 3.0F, 1.5F, ColorUtil.a(dot, alpha));
            int nameColor = active ? theme.a(ThemeInfo.TEXT).a() : theme.a(ThemeInfo.TEXT_DISABLED).a();
            Fonts.d.a(matrices, build.name(), x + 17.0F, rowY + 8.0F, 7.0F, ColorUtil.a(nameColor, alpha));
            String info = build.solidCount() + " бл.  |  " + build.sizeX() + "×" + build.sizeY() + "×" + build.sizeZ();
            Fonts.c.a(matrices, info, x + 17.0F, rowY + 16.0F, 6.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), 0.8F * alpha));
         }
      }

      if (list.isEmpty()) {
         String empty = "Пусто";
         Fonts.c
            .a(
               matrices,
               empty,
               x + (width - Fonts.c.a(empty, 6.5F)) / 2.0F,
               listY + listHeight / 2.0F,
               6.5F,
               ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), alpha)
            );
      }
   }

   private void viewport(class_332 context, class_4587 matrices, Draw2DProcessor draw, ThemeProcessor theme, float alpha) {
      float x = this.viewX();
      float y = this.sideY();
      float width = this.viewWidth();
      float height = this.sideHeight();
      draw.a(matrices, x, y, width, height, 6.0F, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), 0.35F * alpha));
      if (this.selected != null && !this.selected.blocks().isEmpty()) {
         this.model(context, x + 6.0F, y + 6.0F, width - 12.0F, height - 12.0F);
      } else {
         String empty = "Нет построек";
         Fonts.c
            .a(matrices, empty, x + (width - Fonts.c.a(empty, 6.5F)) / 2.0F, y + height / 2.0F, 6.5F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), alpha));
      }
   }

   private void model(class_332 context, float x, float y, float width, float height) {
      class_4598 consumers = aM_.method_22940().method_23000();
      class_4587 matrices = context.method_51448();
      matrices.method_22903();
      matrices.method_46416(x + width / 2.0F, y + height / 2.0F, 500.0F);
      float largest = Math.max(this.selected.sizeX(), Math.max(this.selected.sizeY(), this.selected.sizeZ()));
      float scale = Math.min(width, height) / Math.max(largest + 2.0F, 8.0F) * this.zoom;
      matrices.method_22905(scale, -scale, scale);
      matrices.method_22907(class_7833.field_40714.rotationDegrees(this.pitch));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(this.yaw));
      matrices.method_46416(-this.selected.centerX(), -this.selected.centerY(), -this.selected.centerZ());
      RenderSystem.enableDepthTest();
      GL11.glClear(256);
      class_308.method_24211();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int light = 15728880;

      for (BuildData.Block block : this.selected.blocks()) {
         if (!block.state().method_26215()) {
            matrices.method_22903();
            matrices.method_46416(block.x(), block.y(), block.z());
            boolean rendered = false;
            if (block.nbt() != null && aM_.field_1687 != null) {
               try {
                  class_2586 entity = class_2586.method_11005(
                     new class_2338(block.x(), block.y(), block.z()), block.state(), block.nbt(), aM_.field_1687.method_30349()
                  );
                  if (entity != null) {
                     class_827<class_2586> renderer = aM_.method_31975().method_3550(entity);
                     if (renderer != null) {
                        renderer.method_3569(entity, 0.0F, matrices, consumers, light, class_4608.field_21444);
                        rendered = true;
                     }
                  }
               } catch (Throwable var16) {
                  rendered = false;
               }
            }

            if (!rendered) {
               aM_.method_1541().method_3353(block.state(), matrices, consumers, light, class_4608.field_21444);
            }

            matrices.method_22909();
         }
      }

      consumers.method_22993();
      class_308.method_24210();
      matrices.method_22909();
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      double mx = MathUtil.scale(mouseX, 2);
      double my = MathUtil.scale(mouseY, 2);
      if (button == 0) {
         float chipX = this.sideX() + 5.0F;
         float chipY = this.sideY() + 5.0F;

         for (int index = 0; index < FILTERS.length; index++) {
            float chipWidth = Fonts.c.a(FILTER_NAMES[index], 6.5F) + 12.0F;
            if (MathUtil.a(mx, my, chipX, chipY, chipWidth, 13.0F)) {
               this.filter = FILTERS[index];
               this.scroll = 0.0F;
               return true;
            }

            chipX += chipWidth + 4.0F;
         }

         float listY = this.sideY() + 23.0F;
         float listHeight = this.sideHeight() - 28.0F;
         if (MathUtil.a(mx, my, this.sideX() + 4.0F, listY, this.sideWidth() - 8.0F, listHeight)) {
            List<BuildData> list = this.filtered();

            for (int index = 0; index < list.size(); index++) {
               float rowY = listY + index * 22.0F - this.scroll;
               if (my >= rowY && my <= rowY + 22.0F - 2.0F) {
                  this.select(list.get(index));
                  return true;
               }
            }

            return true;
         }

         if (MathUtil.a(mx, my, this.viewX(), this.sideY(), this.viewWidth(), this.sideHeight())) {
            this.dragging = true;
            return true;
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (button == 0) {
         this.dragging = false;
      }

      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.dragging) {
         this.yaw += (float)deltaX;
         this.pitch = Math.max(-90.0F, Math.min(90.0F, this.pitch + (float)deltaY));
         return true;
      } else {
         return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontal, double vertical) {
      double mx = MathUtil.scale(mouseX, 2);
      double my = MathUtil.scale(mouseY, 2);
      if (MathUtil.a(mx, my, this.viewX(), this.sideY(), this.viewWidth(), this.sideHeight())) {
         this.zoom = Math.max(0.2F, Math.min(6.0F, this.zoom + (float)vertical * 0.1F * this.zoom));
         return true;
      } else if (MathUtil.a(mx, my, this.sideX(), this.sideY(), this.sideWidth(), this.sideHeight())) {
         this.scroll -= (float)vertical * 12.0F;
         return true;
      } else {
         return super.method_25401(mouseX, mouseY, horizontal, vertical);
      }
   }

   public boolean method_25421() {
      return false;
   }
}
