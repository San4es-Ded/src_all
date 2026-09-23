package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.internal.framework.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import globals.client.WorldKey;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.profile.Profile;

public class MainMenuScreen extends RockstarScreen implements MinecraftClientAccess {
   private static boolean internalField0277;
   private static final List<MenuTile> internalField0416 = new ArrayList<>();
   private static WallpaperManager internalField0374;
   private static TopBarWidget internalField0373;
   private boolean internalField0276;
   private long internalField0230;
   private double internalField0194;
   private double internalField0193;
    private float internalField0205;
    private final AnimatedValue internalField0808 = new AnimatedValue(800L, 1.0F, Easing.internalField1626);
   private final long internalField1059 = this.internalField0808.internalMethod02882();
   private final Stopwatch internalField0519 = new Stopwatch();
   private float internalField0206;
   private float internalField1048;
   private long internalField1058 = System.currentTimeMillis();
   private final Stopwatch internalField0518 = new Stopwatch();
   private boolean internalField1100;
   private final AnimatedColor internalField0814 = new AnimatedColor(300L, new ColorRGBA(255.0F, 255.0F, 255.0F), Easing.internalField1627);

   public void init() {
      String localValue1 = "image/mainmenu/icons/";
      internalMethod07141();
      internalMethod07140();
      if (!internalField0277) {
         internalField0416.clear();
         internalField0416.add(new MenuTile(localValue1 + "single.png", 12.0F, () -> internalField0149.setScreen(new SelectWorldScreen(this))));
         internalField0416.add(new MenuTile(localValue1 + "multi.png", 12.0F, () -> internalField0149.setScreen(new MultiplayerScreen(this))));
         internalField0416.add(new MenuTile(localValue1 + "alt.png", 12.0F, () -> internalField0149.setScreen(new AltManagerScreen())));
         internalField0416.add(
            new MenuTile(localValue1 + "settings.png", 12.0F, () -> internalField0149.setScreen(new OptionsScreen(this, internalField0149.options)))
         );
         internalField0416.add(new MenuTile(localValue1 + "quit.png", 14.0F, () -> {
            if (RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).internalMethod08121().isSelected()) {
               RandomSoundHelper.internalMethod08455();
            }

            internalField0149.stop();
         }));
         internalField0277 = true;

         try {
            if (RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).isEnabled()
               && RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).internalMethod08081().isSelected()) {
               RandomSoundHelper.internalMethod07538();
            }
         } catch (Throwable localValue3) {
            RockstarClient.internalField0572
               .warn(
                  "\u0433\u043e\u043b\u043e\u0441 \u043f\u0440\u0438\u0432\u0435\u0442\u0441\u0442\u0432\u0438\u044f \u043d\u0435 \u043f\u0440\u043e\u0438\u0433\u0440\u0430\u043b\u0441\u044f: {}",
                  localValue3.toString()
               );
         }
      }

      RockstarClient.getInstance()
         .internalMethod06050()
         .info(internalField0149.getSession().getUsername(), "", "", "", WorldKey.local().world(), "all", Profile.getUsername());
      RockstarClient.getInstance().internalMethod06050().update("main_menu");
      super.init();
   }

   static WallpaperManager internalMethod07141() {
      if (internalField0374 == null) {
         int localValue0 = LocalTime.now().getHour();
         boolean localValue1 = localValue0 >= 6 && localValue0 < 19;
         int localValue2 = WallpaperManager.internalMethod00991();
         int localValue3 = WallpaperManager.internalMethod00997() - localValue2;
         int localValue4 = localValue1 ? ThreadLocalRandom.current().nextInt(localValue2) : localValue2 + ThreadLocalRandom.current().nextInt(Math.max(1, localValue3));
         internalField0374 = new WallpaperManager(localValue4);
      }

      return internalField0374;
   }

   private static TopBarWidget internalMethod07140() {
      if (internalField0373 == null) {
         internalField0373 = new TopBarWidget(() -> internalMethod07141().internalMethod00992());
      }

      return internalField0373;
   }

   @Override
   public void render(UiRenderContext localValue1) {
      SizedFont localValue2 = Fonts.internalField1156.internalMethod01432(65.0F);
      SizedFont localValue3 = Fonts.internalField0449.internalMethod01432(16.0F);
      SizedFont localValue4 = Fonts.internalField1154.internalMethod01432(10.0F);
      float localValue5 = 1.0F - internalMethod07141().internalMethod00990();
      float localValue6 = 80.0F;
      float localValue7 = 205.0F * (1.0F - this.internalField0808.internalMethod02881()) * localValue5;
      float localValue8 = MathUtils.internalMethod02587(localValue6, -120.0, this.internalField0808.internalMethod02881());
      this.internalField0808.internalMethod07062(true);
      localValue1.drawRoundedRect(0.0F, 0.0F, this.width, this.height, CornerRadii.internalField0098, ColorRGBA.BLACK);
      long localValue9 = System.currentTimeMillis();
      float localValue11 = Math.min(0.1F, (float)(localValue9 - this.internalField1058) / 1000.0F);
      this.internalField1058 = localValue9;
      this.internalMethod07546(localValue9, localValue11);
      float localValue12 = this.width / 2.0F;
      float localValue13 = this.height / 2.0F;
      float localValue14 = (10.0F + 6.0F * this.internalField0808.internalMethod02881()) * localValue5;
      float localValue15 = MathHelper.clamp((localValue1.internalMethod05259() - localValue12) / Math.max(1.0F, localValue12), -1.0F, 1.0F) * localValue14;
      float localValue16 = MathHelper.clamp((localValue1.internalMethod05261() - localValue13) / Math.max(1.0F, localValue13), -1.0F, 1.0F) * localValue14;
      float localValue17 = 1.0F - (float)Math.pow(0.0025F, localValue11);
      this.internalField0206 = this.internalField0206 + (localValue15 - this.internalField0206) * localValue17;
      this.internalField1048 = this.internalField1048 + (localValue16 - this.internalField1048) * localValue17;
      internalMethod07141()
         .internalMethod01889(localValue1, this.width, this.height, this.internalField0206, this.internalField1048, this.internalField0808.internalMethod02881(), this.internalField0205);
      boolean localValue18 = internalField0149.getOverlay() != null;
      float localValue19 = localValue18 ? 0.5F : 1.4F;
      float localValue20 = (localValue18 ? 1.0F : 1.0F - this.internalField0808.internalMethod02881()) * localValue5;
      if (localValue20 > 0.01F) {
         RenderPipeline.internalField0314.internalMethod02201(localValue19);
         localValue1.drawBlurredRect(0.0F, 0.0F, this.width, this.height, 15.0F, CornerRadii.internalField0098, ColorRGBA.WHITE.withAlpha(255.0F * localValue20));
      }

      float localValue21 = Math.min(130.0F, this.height * 0.38F);
      localValue1.drawRoundedRect(
         0.0F,
         this.height - localValue21,
         this.width,
         localValue21,
         CornerRadii.internalField0098,
         new AlternatingColorGradient(new ColorRGBA(0.0F, 0.0F, 0.0F, 0.0F), new ColorRGBA(0.0F, 0.0F, 0.0F, (int)(90.0F * localValue5)))
      );
      if (this.internalField0518.internalMethod02365(250L)) {
         double localValue22 = internalField0149.getWindow().getScaleFactor();
         float localValue24 = localValue6 + localValue2.internalMethod04890() * 0.5F;
         ColorRGBA localValue25 = ColorRGBA.fromPixel(this.width / 2.0F * (float)localValue22, internalField0149.getWindow().getHeight() - localValue24 * (float)localValue22);
         this.internalField1100 = (localValue25.getRed() + localValue25.getGreen() + localValue25.getBlue()) / 3.0F > 120.0F;
         this.internalField0518.internalMethod00701();
      }

      this.internalField0814.internalMethod03893(this.internalField1100 ? new ColorRGBA(28.0F, 28.0F, 30.0F) : ColorRGBA.WHITE);
      ColorRGBA localValue40 = this.internalField0814.internalMethod04159().withAlpha(localValue7);
      String localValue23 = TextUtils.internalMethod08270();
      localValue1.drawCenteredText(localValue3, localValue23, this.width / 2.0F, localValue8 - 23.0F, localValue40);
      String localValue41 = TextUtils.internalMethod04048();
      String[] localValue42 = localValue41.split(":");
      String localValue26 = localValue42.length > 0 ? localValue42[0] : localValue41;
      String localValue27 = localValue42.length > 1 ? localValue42[1] : "";
      float localValue28 = localValue2.internalMethod00965(localValue26);
      float localValue29 = localValue2.internalMethod00965(":");
      float localValue30 = localValue2.internalMethod00965(localValue27);
      float localValue31 = localValue28 + localValue29 + localValue30;
      float localValue32 = this.width / 2.0F - localValue31 / 2.0F;
      float localValue33 = 1.0F + 0.008F * (float)Math.sin((float)localValue9 * 0.00185F);
      HudRenderUtils.internalMethod08976(localValue1.getMatrices(), this.width / 2.0F, localValue8 + localValue2.internalMethod04890() / 2.0F, localValue33);
      float localValue34 = localValue8 - localValue2.internalMethod04890() * 0.1F;
      localValue1.drawText(localValue2, localValue26, localValue32, localValue8, localValue40);
      localValue1.drawText(localValue2, ":", localValue32 + localValue28, localValue34, localValue40);
      localValue1.drawText(localValue2, localValue27, localValue32 + localValue28 + localValue29, localValue8, localValue40);
      HudRenderUtils.internalMethod00012(localValue1.getMatrices());
      float localValue43 = 0.0F;

      for (MenuTile localValue45 : internalField0416) {
         localValue45.internalMethod03820()
            .internalMethod07062(
               internalField0416.size() - internalField0416.indexOf(localValue45) > (1.0F - this.internalField0808.internalMethod02881()) * internalField0416.size() + 0.5F
            );
         localValue45.set(
            this.width / 2.0F - 87.0F + localValue43,
            (this.height > 500 ? this.height / 2.0F + 20.0F : this.height / 1.25F) - 5.0F - 10.0F * localValue45.internalMethod03820().internalMethod02881(),
            30.0F,
            30.0F
         );
         localValue43 += localValue45.getWidth() + 6.0F;
         localValue45.internalMethod05203(localValue1, localValue5);
      }

      if (this.internalMethod06085()) {
         RockstarClient.getInstance().internalMethod01271().internalMethod01259().render(localValue1, localValue5);
      }

      internalMethod07141().internalMethod00055(localValue1, this.width, this.height);
      internalMethod07140().internalMethod01573(localValue1, this.width, this.height, localValue5, this.internalField0808.internalMethod02881());
   }

   private void internalMethod07546(long localValue1, float localValue3) {
      if (this.internalField0276 && !internalMethod07141().internalMethod00999() && localValue1 - this.internalField0230 >= 1500L) {
             this.internalField0276 = false;
          internalMethod07141().internalMethod00992();
      }

      float localValue4 = this.internalField0276 && !internalMethod07141().internalMethod00999() ? MathHelper.clamp((float)(localValue1 - this.internalField0230) / 1500.0F, 0.0F, 1.0F) : 0.0F;
      this.internalField0205 = this.internalField0205 + (localValue4 - this.internalField0205) * (1.0F - (float)Math.pow(6.0E-4F, localValue3));
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (internalMethod07141().internalMethod00999()) {
         internalMethod07141().internalMethod05038(localValue1, localValue3, localValue5.internalMethod02957());
      } else if (!internalMethod07140().internalMethod00660(localValue1, localValue3, localValue5.internalMethod02957())) {
         if (!this.internalMethod06085()
            || !RockstarClient.getInstance().internalMethod01271().internalMethod01259().internalMethod02613((float)localValue1, (float)localValue3, localValue5.internalMethod02957())) {
            for (MenuTile localValue7 : internalField0416) {
               if (localValue7.hovered(localValue1, localValue3) && localValue7.internalMethod03820().internalMethod02881() == 1.0F) {
                  localValue7.internalMethod07255(localValue1, localValue3, localValue5.internalMethod02957());
                  return;
               }
            }

            if (localValue5.internalMethod02957() == 0) {
               this.internalField0276 = true;
               this.internalField0230 = System.currentTimeMillis();
               this.internalField0194 = localValue1;
               this.internalField0193 = localValue3;
            }

            super.onMouseClicked(localValue1, localValue3, localValue5);
         }
      }
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      if (internalMethod07141().internalMethod00999()) {
         internalMethod07141().internalMethod08174(localValue1, localValue3, localValue5.internalMethod02957());
      } else {
         if (this.internalField0276 && localValue5.internalMethod02957() == 0) {
            this.internalField0276 = false;
             if (System.currentTimeMillis() - this.internalField0230 < 1500L && this.internalField0519.internalMethod02365(this.internalField1059)) {
                this.internalField0519.internalMethod00701();
             }
         }
      }
   }

   @Override
   public void onMouseDragged(double localValue1, double localValue3, MouseButton localValue5, double localValue6, double localValue8) {
      if (internalMethod07141().internalMethod00999()) {
         internalMethod07141().internalMethod01847(localValue1, localValue3, localValue5.internalMethod02957());
      } else {
         if (this.internalField0276 && (Math.abs(localValue1 - this.internalField0194) > 6.0 || Math.abs(localValue3 - this.internalField0193) > 6.0)) {
            this.internalField0276 = false;
         }
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (internalMethod07141().internalMethod00999()) {
         internalMethod07141().internalMethod03743(verticalAmount);
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (internalMethod07141().internalMethod00999()) {
         if (keyCode == 256) {
            internalMethod07141().internalMethod00998();
         }

         if (keyCode == 263) {
            internalMethod07141().internalMethod03744(-1);
         }

         if (keyCode == 262) {
            internalMethod07141().internalMethod03744(1);
         }

         return true;
      } else if (internalMethod07140().internalMethod00668(keyCode, scanCode, modifiers)) {
         return true;
      } else {
          if (keyCode == 69) {
            RockstarClient.getInstance().internalMethod04467().internalMethod05591();
         }

         if (keyCode == 82) {
            MinecraftClient.getInstance().setScreen(new MultiplayerScreen(this));
         }

         if (keyCode == 84) {
            MinecraftClient.getInstance().setScreen(new SelectWorldScreen(this));
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   private boolean internalMethod06085() {
      return RockstarClient.getInstance().internalMethod05636().internalMethod07174()
         && RockstarClient.getInstance()
            .internalMethod01271()
            .internalMethod01259()
            .internalMethod05688()
            .stream()
            .anyMatch(localValue0 -> localValue0 instanceof MusicLyricsHud);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}
