package rockstar.client.internal.script;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.auth.AltManager;
import rockstar.client.notification.NotificationType;
import rockstar.client.render.CornerRadii;
import rockstar.client.render.Fonts;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.MouseButton;
import rockstar.client.ui.RockstarScreen;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiRenderContext;

public class AltManagerScreen extends RockstarScreen implements MinecraftClientAccess {
   private static final float PANEL_W = 250.0F;
   private static final float PANEL_H = 222.0F;

   private static final float SIDE_PAD = 13.0F;
   private static final float ROW_H = 24.0F;
   private static final float ROW_STRIDE = 27.0F;
   private static final int MAX_ROWS = 5;
   private static final long APPLY_MS = 180L;

   private final AnimatedValue openAnim = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
   private final CustomTextField field = new CustomTextField(Fonts.internalField1157.internalMethod01432(7.0F));
   private final List<AnimatedValue> rowAnims = new ArrayList<>();

   private boolean closing;
   private int applying = -1;
   private long applyStart;
   private boolean applied;
   private int selectedAlt = -1;

   private float bgOffX;
   private float bgOffY;
   private long bgLast;

   private final AnimatedValue backAnim = new AnimatedValue(200L, 0.0F, Easing.internalField1626);
   private boolean backToMenu;

   public AltManagerScreen() {
      this.field.internalMethod09000("Никнейм");
      this.field.internalMethod07508(true);
   }

   @Override
   public void render(UiRenderContext ctx) {
      float mx = ctx.internalMethod05259();
      float my = ctx.internalMethod05261();

      this.openAnim.internalMethod07062(!this.closing);
      float anim = this.openAnim.internalMethod02881();

      if (this.applying >= 0 && !this.applied && System.currentTimeMillis() - this.applyStart >= APPLY_MS) {
         List<String> alts = AltManager.getAlts();
         if (this.applying < alts.size()) {
            String name = alts.get(this.applying);
            AltManager.login(name);
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(
                    NotificationType.internalField0704,
                    "Alt Manager",
                    "Вы вошли как " + name
            );
         }
         this.selectedAlt = this.applying;
         this.applying = -1;
         this.applied = true;
      }

      float px = this.width / 2.0F - PANEL_W / 2.0F;
      float py = this.height / 2.0F - PANEL_H / 2.0F;

      this.renderWallpaper(ctx, anim);

      ctx.drawRoundedRect(0.0F, 0.0F, (float)this.width, (float)this.height, CornerRadii.internalField0098, ColorRGBA.BLACK.withAlpha(52.0F * anim));

      HudRenderUtils.internalMethod08976(
              ctx.getMatrices(),
              px + PANEL_W / 2.0F,
              py + PANEL_H / 2.0F,
              0.96F + 0.04F * anim
      );

      CornerRadii panel = CornerRadii.internalMethod03908(11.0F);

      // Main surface.
      ctx.drawShadow(
              px, py, PANEL_W, PANEL_H,
              22.0F,
              panel,
              ThemeColors.internalField1309.mulAlpha(0.58F * anim)
      );

      if (!this.closing) {
         ctx.drawBlurredRect(
                 px, py, PANEL_W, PANEL_H,
                 5.0F, 3.0F,
                 panel,
                 ThemeColors.internalField1312.mulAlpha(anim)
         );
      }

      ctx.drawSquircle(
              px, py, PANEL_W, PANEL_H,
              3.0F,
              panel,
              ThemeColors.internalMethod07738().mulAlpha(anim)
      );

      ctx.drawSquircleBorder(
              px, py, PANEL_W, PANEL_H,
              0.65F, 3.0F,
              panel,
              ThemeColors.internalField1616.mulAlpha(anim)
      );

      // Header.
      ctx.drawCenteredText(
              Fonts.internalField1156.internalMethod01432(9.0F),
              "Alt Manager",
              px + PANEL_W / 2.0F,
              py + 9.0F,
              ThemeColors.internalMethod08459().mulAlpha(anim)
      );

      ctx.drawCenteredText(
              Fonts.internalField1157.internalMethod01432(5.5F),
              "Управление сохранёнными аккаунтами",
              px + PANEL_W / 2.0F,
              py + 23.0F,
              ThemeColors.internalMethod08459().mulAlpha(0.38F * anim)
      );

      List<String> alts = AltManager.getAlts();
      int count = Math.min(alts.size(), MAX_ROWS);

      while (this.rowAnims.size() < count) {
         this.rowAnims.add(new AnimatedValue(220L, 0.0F, Easing.internalField1626));
      }

      // Add-account field.
      float fieldY = py + 36.0F;
      float fieldW = PANEL_W - SIDE_PAD * 2.0F - 68.0F;
      float fieldX = px + SIDE_PAD;
      this.field.internalMethod05191(fieldX, fieldY, fieldW, 20.0F);
      this.field.internalMethod00143(ThemeColors.internalMethod08459());
      this.field.internalMethod08627(anim);

      // Visible input surface must be drawn before the text field itself.
      CornerRadii fieldRadii = CornerRadii.internalMethod03908(5.0F);
      ctx.drawRoundedRect(
              fieldX, fieldY, fieldW, 20.0F,
              fieldRadii,
              ThemeColors.internalMethod08573().mulAlpha(0.90F * anim)
      );
      ctx.drawRoundedBorder(
              fieldX, fieldY, fieldW, 20.0F, 0.55F,
              fieldRadii,
              ThemeColors.internalField1616.mulAlpha(0.90F * anim)
      );

      this.field.internalMethod03398(ctx);

      float addX = px + SIDE_PAD + fieldW + 6.0F;
      float addW = 68.0F;
      boolean addHover = inside(mx, my, addX, fieldY, addW, 20.0F);

      ctx.drawRoundedRect(
              addX, fieldY, addW, 20.0F,
              CornerRadii.internalMethod03908(5.0F),
              (addHover
                      ? ThemeColors.internalMethod02531()
                      : ThemeColors.internalMethod08573()).mulAlpha(anim)
      );

      if (!addHover) {
         ctx.drawRoundedBorder(
                 addX, fieldY, addW, 20.0F, 0.55F,
                 CornerRadii.internalMethod03908(5.0F),
                 ThemeColors.internalField1616.mulAlpha(anim)
         );
      }

      SizedFont addFont = Fonts.internalField1157.internalMethod01432(7.0F);
      ctx.drawCenteredText(
               addFont,
               "Добавить",
               addX + addW / 2.0F,
               fieldY + (20.0F - addFont.internalMethod04890()) / 2.0F,
               ThemeColors.internalMethod08459().mulAlpha(anim)
      );

      // List section label.
      float listTop = py + 68.0F;
      ctx.drawCenteredText(
              Fonts.internalField1157.internalMethod01432(5.5F),
              "СОХРАНЁННЫЕ АККАУНТЫ",
              px + PANEL_W / 2.0F,
              listTop - 7.0F,
              ThemeColors.internalMethod08459().mulAlpha(0.38F * anim)
      );

      if (count == 0) {
         CornerRadii emptyRadii = CornerRadii.internalMethod03908(9.0F);
         float emptyY = listTop + 2.0F;
         ctx.drawRoundedRect(
                 px + SIDE_PAD,
                 emptyY,
                 PANEL_W - SIDE_PAD * 2.0F,
                 ROW_H * 1.45F,
                 emptyRadii,
                 ThemeColors.internalMethod08573().mulAlpha(anim)
         );
         ctx.drawRoundedBorder(
                 px + SIDE_PAD,
                 emptyY,
                 PANEL_W - SIDE_PAD * 2.0F,
                 ROW_H * 1.45F,
                 0.5F,
                 emptyRadii,
                 ThemeColors.internalField1616.mulAlpha(anim)
         );

          SizedFont emptyFont = Fonts.internalField1157.internalMethod01432(6.0F);
          float emptyH = ROW_H * 1.45F;
          ctx.drawCenteredText(
                  emptyFont,
                  "Список пуст — добавьте первый никнейм",
                  px + PANEL_W / 2.0F,
                  emptyY + (emptyH - emptyFont.internalMethod04890()) / 2.0F,
                  ThemeColors.internalMethod08459().mulAlpha(anim)
          );
      }

      for (int i = 0; i < count; i++) {
         AnimatedValue rowAnim = this.rowAnims.get(i);

         if (!this.closing && anim > (float)(i + 1) * 0.10F) {
            rowAnim.internalMethod07062(true);
         } else if (this.closing) {
            rowAnim.internalMethod07062(false);
         }

         float ra = rowAnim.internalMethod02881();

         float rx = px + SIDE_PAD + 8.0F * (1.0F - ra);
         float ry = listTop + (float)i * ROW_STRIDE + 4.0F * (1.0F - ra);
         float rowW = PANEL_W - SIDE_PAD * 2.0F;

         CornerRadii rowRadii = CornerRadii.internalMethod03908(7.0F);
         ctx.drawRoundedRect(
                 rx, ry, rowW, ROW_H,
                 rowRadii,
                 ThemeColors.internalMethod08573().mulAlpha(ra * anim)
         );

         ctx.drawRoundedBorder(
                 rx, ry, rowW, ROW_H,
                 0.45F,
                 rowRadii,
                 ThemeColors.internalField1616.mulAlpha(ra * anim)
         );

         // Small avatar/initial badge. It uses only the existing theme colors.
         float badgeX = rx + 7.0F;
         float badgeY = ry + 5.0F;
         float badgeS = 16.0F;

         ctx.drawRoundedRect(
                 badgeX, badgeY, badgeS, badgeS,
                 CornerRadii.internalMethod03908(5.0F),
                 ThemeColors.internalMethod07738().mulAlpha(ra * anim)
         );

          String name = alts.get(i);
          String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();

          SizedFont badgeFont = Fonts.internalField1157.internalMethod01432(6.0F);
          ctx.drawCenteredText(
                  badgeFont,
                  initial,
                  badgeX + badgeS / 2.0F,
                  badgeY + (badgeS - badgeFont.internalMethod04890()) / 2.0F + 1.0F,
                  ThemeColors.internalMethod08459().mulAlpha(ra * anim)
          );

          SizedFont nameFont = Fonts.internalField1157.internalMethod01432(7.0F);
          SizedFont subFont = Fonts.internalField1157.internalMethod01432(5.0F);
          float nameH = nameFont.internalMethod04890();
          float subH = subFont.internalMethod04890();
          float textBlockH = nameH + 1.0F + subH;
          float textY = ry + (ROW_H - textBlockH) / 2.0F;

          ctx.drawText(
                  nameFont,
                  name,
                  rx + 30.0F,
                  textY,
                  ThemeColors.internalMethod08459().mulAlpha(ra * anim)
          );

          ctx.drawText(
                  subFont,
                  "Сохранённый аккаунт",
                  rx + 30.0F,
                  textY + nameH + 1.0F,
                  ThemeColors.internalMethod08459().mulAlpha(0.30F * ra * anim)
          );

         float loginW = 50.0F;
         float loginX = rx + rowW - loginW - 21.0F;
         float loginY = ry + 3.0F;
         boolean loginHover = inside(mx, my, loginX, loginY, loginW, 20.0F);

         if (loginHover) {
            ctx.drawRoundedRect(
                    loginX, loginY, loginW, 20.0F,
                    CornerRadii.internalMethod03908(5.0F),
                    ThemeColors.internalMethod02531().mulAlpha(ra * anim)
            );
         } else {
            ctx.drawRoundedRect(
                    loginX, loginY, loginW, 20.0F,
                    CornerRadii.internalMethod03908(5.0F),
                    ThemeColors.internalMethod07738().mulAlpha(ra * anim)
            );
            ctx.drawRoundedBorder(
                    loginX, loginY, loginW, 20.0F, 0.45F,
                    CornerRadii.internalMethod03908(5.0F),
                    ThemeColors.internalField1616.mulAlpha(ra * anim)
            );
         }

          if (this.applying == i && !this.applied) {
             float progress = Math.min(
                     1.0F,
                     Math.max(
                             0.0F,
                             (float)(System.currentTimeMillis() - this.applyStart) / (float)APPLY_MS
                     )
             );

             ctx.drawCircleProgress(
                     loginX + loginW / 2.0F,
                     loginY + 10.0F,
                     6.0F, 1.5F,
                     progress,
                     ColorRGBA.WHITE.mulAlpha(ra * anim)
             );
          } else {
             SizedFont loginFont = Fonts.internalField1157.internalMethod01432(7.0F);
             ctx.drawCenteredText(
                     loginFont,
                     this.selectedAlt == i ? "Выбрано" : "Войти",
                     loginX + loginW / 2.0F,
                     loginY + (20.0F - loginFont.internalMethod04890()) / 2.0F,
                     ThemeColors.internalMethod08459().mulAlpha(ra * anim)
             );
          }

          float trashS = 9.0F;
          float trashX = rx + rowW - 16.0F;
          boolean trashHover = inside(mx, my, trashX, loginY, 16.0F, 20.0F);
          ColorRGBA trashColor = trashHover
                  ? ThemeColors.internalField0777
                  : ThemeColors.internalMethod08459();

          ctx.drawIcon(
                  "trash",
                  trashX + (16.0F - trashS) / 2.0F,
                  loginY + (20.0F - trashS) / 2.0F,
                  trashS,
                  trashColor.mulAlpha(ra * anim)
          );
      }

      // Footer / back button.
      this.backAnim.internalMethod07062(!this.closing && anim > 0.72F);
      float ba = this.backAnim.internalMethod02881();

      float backW = PANEL_W - SIDE_PAD * 2.0F;
      float backX = px + SIDE_PAD + 8.0F * (1.0F - ba);
      float backY = py + PANEL_H - 28.0F + 5.0F * (1.0F - ba);
      boolean backHover = inside(mx, my, backX, backY, backW, 22.0F);

      if (backHover) {
         ctx.drawRoundedRect(
                 backX, backY, backW, 22.0F,
                 CornerRadii.internalMethod03908(6.0F),
                 ThemeColors.internalMethod02531().mulAlpha(ba * anim)
         );
      } else {
         ctx.drawRoundedRect(
                 backX, backY, backW, 22.0F,
                 CornerRadii.internalMethod03908(6.0F),
                 ThemeColors.internalMethod08573().mulAlpha(ba * anim)
         );
         ctx.drawRoundedBorder(
                 backX, backY, backW, 22.0F, 0.5F,
                 CornerRadii.internalMethod03908(6.0F),
                 ThemeColors.internalField1616.mulAlpha(ba * anim)
         );
      }

      float backH = 22.0F;
      float backIconS = 10.0F;
      ctx.drawIcon(
               "back",
               backX + 12.0F,
               backY + (backH - backIconS) / 2.0F,
               backIconS,
               ThemeColors.internalMethod08459().mulAlpha(ba * anim)
      );

      SizedFont backFont = Fonts.internalField1157.internalMethod01432(7.0F);
      ctx.drawCenteredText(
               backFont,
               "В главное меню",
               backX + backW / 2.0F,
               backY + (backH - backFont.internalMethod04890()) / 2.0F,
               ThemeColors.internalMethod08459().mulAlpha(ba * anim)
      );

      HudRenderUtils.internalMethod00012(ctx.getMatrices());

      if (this.closing && this.openAnim.internalMethod02884() && anim <= 0.01F) {
         if (this.backToMenu) {
            internalField0149.setScreen(new MainMenuScreen());
         } else {
            this.close();
         }
      }
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      this.field.internalMethod01643(mouseX, mouseY, button);

      if (button.internalMethod02957() != 0) {
         return;
      }

      float mx = (float)mouseX;
      float my = (float)mouseY;

      float px = this.width / 2.0F - PANEL_W / 2.0F;
      float py = this.height / 2.0F - PANEL_H / 2.0F;

      float fieldY = py + 36.0F;
      float fieldW = PANEL_W - SIDE_PAD * 2.0F - 68.0F;
      float addX = px + SIDE_PAD + fieldW + 6.0F;

      if (inside(mx, my, addX, fieldY, 68.0F, 20.0F)) {
         this.submit();
         return;
      }

      List<String> alts = AltManager.getAlts();
      int count = Math.min(alts.size(), MAX_ROWS);
      float listTop = py + 68.0F;
      float rowW = PANEL_W - SIDE_PAD * 2.0F;

      for (int i = 0; i < count; i++) {
         float rx = px + SIDE_PAD;
         float ry = listTop + (float)i * ROW_STRIDE;
         float loginX = rx + rowW - 50.0F - 21.0F;

          if (inside(mx, my, loginX, ry + 3.0F, 50.0F, 20.0F)) {
            if (this.selectedAlt != i && this.applying < 0) {
               this.applying = i;
               this.applyStart = System.currentTimeMillis();
               this.applied = false;
            }
            return;
         }

          if (inside(mx, my, rx + rowW - 16.0F, ry + 3.0F, 16.0F, 20.0F)) {
            AltManager.removeAlt(alts.get(i));
            if (this.selectedAlt == i) {
               this.selectedAlt = -1;
            } else if (this.selectedAlt > i) {
               this.selectedAlt--;
            }
            return;
         }
      }

      float backX = px + SIDE_PAD;
      float backY = py + PANEL_H - 28.0F;

      if (inside(mx, my, backX, backY, PANEL_W - SIDE_PAD * 2.0F, 22.0F)) {
         this.backToMenu = true;
         this.closing = true;
      }
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.field.internalMethod02863(mouseX, mouseY, button);
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.field.internalMethod00342()) {
         if (keyCode == 256) {
            this.field.internalMethod07508(false);
            return true;
         }

         this.field.internalMethod05727(keyCode, scanCode, modifiers);

         if (keyCode == 257) {
            this.submit();
         }

         return true;
      }

      if (keyCode == 256) {
         this.closing = true;
         return true;
      }

      if (keyCode == 257) {
         this.submit();
         return true;
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      if (this.field.internalMethod00342() && this.field.internalMethod05413(chr, modifiers)) {
         return true;
      }

      return super.charTyped(chr, modifiers);
   }

   private void renderWallpaper(UiRenderContext ctx, float anim) {
      ctx.drawRoundedRect(
              0.0F, 0.0F,
              (float)this.width, (float)this.height,
              CornerRadii.internalField0098,
              ColorRGBA.BLACK
      );

      long now = System.currentTimeMillis();
      float dt = Math.min(0.1F, (float)(now - this.bgLast) / 1000.0F);
      this.bgLast = now;

      float amp = 8.0F + 4.0F * anim;

      float targetX = MathHelper.clamp(
              (ctx.internalMethod05259() - this.width / 2.0F) / Math.max(1.0F, this.width / 2.0F),
              -1.0F, 1.0F
      ) * amp;

      float targetY = MathHelper.clamp(
              (ctx.internalMethod05261() - this.height / 2.0F) / Math.max(1.0F, this.height / 2.0F),
              -1.0F, 1.0F
      ) * amp;

      float k = 1.0F - (float)Math.pow(0.0025F, dt);
      this.bgOffX += (targetX - this.bgOffX) * k;
      this.bgOffY += (targetY - this.bgOffY) * k;

      MainMenuScreen.internalMethod07141().internalMethod01889(
              ctx,
              this.width,
              this.height,
              this.bgOffX,
              this.bgOffY,
              1.0F,
              0.0F
      );
   }

   private void submit() {
      String name = this.field.internalMethod06202().trim();

      if (!name.isEmpty()) {
         AltManager.addAlt(name);
         this.field.internalMethod00484("");
      }
   }

   private static boolean inside(float px, float py, float x, float y, float w, float h) {
      return px >= x && px <= x + w && py >= y && py <= y + h;
   }
}
