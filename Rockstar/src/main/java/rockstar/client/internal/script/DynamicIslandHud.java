package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.screen.ChatScreen;
import org.joml.Matrix4f;
import pyrock.utility.render.ColorRGBA;

public class DynamicIslandHud extends AbstractHudWidget implements MinecraftClientAccess, ScreenMetricsAccess {
   private static final float internalField0205 = 7.0F;
   private static final float internalField0206 = 125.0F;
   private static final int internalField0227 = 5;
   private static final int[] internalField0618 = new int[]{450, 300, 150, 75};
   private static final float internalField1047 = 15.0F;
   static final UiTransition internalField0918 = (localValue0, localValue1, localValue2) -> {
      localValue2.internalField0205 = localValue0;
      localValue2.internalField1047 = 0.9F + 0.1F * localValue0;
   };
   private final MultiSelectSetting internalField0675 = new MultiSelectSetting(this, "hud.dynamic_island.statuses").internalMethod05559();
   private final BooleanSetting internalField0650 = new BooleanSetting(this, "hud.dynamic_island.song_lyrics", () -> true).internalMethod04836(true);
   private final TextSetting internalField0384 = new TextSetting(this, "hud.dynamic_island.custom_text")
      .internalMethod01004(20)
      .internalMethod00011("");
   private final MutableVector3 internalField0211 = new MutableVector3(48.0F, 15.0F, 7.0F);
   private final AnimatedValue internalField0808 = new AnimatedValue(200L, 0.0F, Easing.internalField1627);
   private final AnimatedValue internalField0809 = new AnimatedValue(500L, 0.0F, Easing.internalField1327);
   private final AnimatedValue internalField1321 = new AnimatedValue(500L, 0.0F, Easing.internalField0812);
   private final AnimatedValue internalField1322 = new AnimatedValue(260L, 0.0F, Easing.internalField1626);
   private final AnimatedValue internalField1323 = new AnimatedValue(500L, 0.0F, Easing.internalField0812);
   private final AnimatedColor internalField0814 = new AnimatedColor(300L, new ColorRGBA(0.0F, 0.0F, 0.0F), Easing.internalField1626);
   private final AdaptiveUiBatchRenderer internalField0203 = new AdaptiveUiBatchRenderer()
      .internalMethod02287(0.1F)
      .internalMethod07251(4.0F)
      .internalMethod04668(0.3F, 0.44F)
      .internalMethod00514(-1, -15856114);
   private DynamicIslandContainer internalField0210;
   private UiContainer internalField0634;
   private DynamicIslandStatus internalField0212;
   private DynamicIslandStatus internalField0213;
   private DynamicIslandHud.InternalType0324 internalField0477;
   private boolean internalField0277;
   private float internalField1456;
   private float internalField1457;
   private float internalField1458;
   private MusicLyricsHud internalField0363;
   private boolean internalField0276;
   private long internalField1059;
   private long internalField1058;
   private boolean internalField1099;
   private int internalField0228;
   private long internalField1060;

   public DynamicIslandHud() {
      super("hud.dynamic_island", "hud/island");
      this.showing = true;
      IslandStatusRegistry.internalMethod07184(this.internalField0675);
      RockstarClient.getInstance().internalMethod03371().internalMethod06625("client");
      this.internalMethod04724();
   }

   @Override
   public UiContainer build() {
      this.internalField0210 = new DynamicIslandContainer(this);
      this.internalField0210
         .internalMethod08791()
         .internalMethod03754(Motion.internalMethod01328(500L, Easing.internalField1327))
         .internalMethod03995(this.internalField0211.internalField0205, this.internalField0211.internalField0206);
      this.internalField0634 = new UiContainer();
      this.internalField0634.internalMethod08791().internalMethod09213().internalMethod09801().snapSize().interactive(false);
      this.internalField0210.internalMethod03907(this.internalField0634);
      return this.internalField0210;
   }

   @Override
   public boolean anchorsRightEdge() {
      return false;
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      this.internalMethod04731();
      List localValue2 = this.internalMethod07881();
      DynamicIslandStatus localValue3 = this.internalMethod00094(localValue2);
      this.internalMethod07180(this.internalMethod05743(localValue3, localValue2));
      this.internalMethod08536();
      this.internalField0212 = localValue3;
      if (localValue3 != null) {
         this.internalMethod00751(localValue3);
         this.internalMethod05740(localValue3);
         this.internalMethod08861(localValue3);
         float localValue4 = this.internalMethod07404(localValue1);
         this.internalMethod08518();
         this.internalMethod02773(localValue1, localValue4);
         this.internalMethod08267(localValue3);
      }
   }

   public <T extends DynamicIslandStatus> T internalMethod03346(T localValue1) {
      return (T)localValue1;
   }

   public boolean internalMethod00752(DynamicIslandStatus localValue1) {
      return this.internalMethod03637(localValue1, true);
   }

   public boolean internalMethod05741(DynamicIslandStatus localValue1) {
      return this.internalMethod03637(localValue1, false);
   }

   private boolean internalMethod03637(DynamicIslandStatus localValue1, boolean localValue2) {
      if (localValue1 == null) {
         return false;
      } else {
         boolean localValue3 = this.internalField0675.internalMethod01792().remove(localValue1);
         this.internalField0675.internalMethod07492().remove(localValue1);
         if (localValue3) {
            this.internalField1099 = false;
            if (localValue1 == this.internalField0363) {
               this.internalMethod08538();
            }

            if (localValue1 == this.internalField0213) {
               this.internalField0213 = null;
               this.internalField0212 = null;
            }

            if (this.internalField0634 != null) {
               this.internalField0634.internalMethod03628();
            }

            if (localValue2) {
               RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            }
         }

         return localValue3;
      }
   }

   public boolean internalMethod01363(String localValue1) {
      DynamicIslandStatus localValue2 = this.internalMethod06681(localValue1);
      return localValue2 != null && this.internalMethod00752(localValue2);
   }

   public String internalMethod07542() {
      String localValue1 = this.internalField0384.internalMethod08926();
      return localValue1 != null && !localValue1.isBlank() ? TextUtils.internalMethod06864(localValue1.trim()) : "NoName";
   }

   public DynamicIslandStatus internalMethod06681(String localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         for (DynamicIslandStatus localValue3 : this.internalMethod05407()) {
            if (localValue3.getName().equalsIgnoreCase(localValue1)) {
               return localValue3;
            }
         }

         return null;
      }
   }

   public List<DynamicIslandStatus> internalMethod05407() {
      ArrayList localValue1 = new ArrayList();

      for (MultiSelectSetting.InternalType0091 localValue3 : this.internalField0675.internalMethod01792()) {
         if (localValue3 instanceof DynamicIslandStatus localValue4) {
            localValue1.add(localValue4);
         }
      }

      return localValue1;
   }

   public DynamicIslandStatus internalMethod07528() {
      return this.internalMethod00094(this.internalMethod07881());
   }

   private DynamicIslandStatus internalMethod00094(List<DynamicIslandStatus> localValue1) {
      if (this.internalField0363 != null) {
         if (localValue1.contains(this.internalField0363)) {
            return this.internalField0363;
         }

         this.internalMethod08538();
         this.internalField1099 = false;
      }

      return localValue1.isEmpty() ? null : (DynamicIslandStatus)localValue1.getLast();
   }

   public List<DynamicIslandStatus> internalMethod05688() {
      return this.internalMethod07881();
   }

   private List<DynamicIslandStatus> internalMethod07881() {
      return this.internalField0675
         .internalMethod01792()
         .stream()
         .filter(localValue0 -> localValue0 instanceof DynamicIslandStatus)
         .map(localValue0 -> (DynamicIslandStatus)localValue0)
         .filter(localValue0 -> localValue0.canShow() && localValue0.isSelected())
         .toList()
         .reversed();
   }

   private void internalMethod04724() {
      ModuleStatusHud localValue1 = null;
      DynamicIslandDefaultStatus localValue2 = null;
      SilentAlertHud localValue3 = null;

      for (DynamicIslandStatus localValue5 : this.internalMethod05407()) {
         if (localValue5 instanceof ModuleStatusHud localValue6) {
            localValue1 = localValue6;
         }

         if (localValue5 instanceof DynamicIslandDefaultStatus localValue11) {
            localValue2 = localValue11;
         }

         if (localValue5 instanceof SilentAlertHud localValue12) {
            localValue3 = localValue12;
         }
      }

      if (localValue1 != null && localValue2 != null) {
         List localValue9 = this.internalField0675.internalMethod01792();
         int localValue10 = localValue9.indexOf(localValue1);
         int localValue13 = localValue9.indexOf(localValue2);
         if (localValue10 >= 0 && localValue13 >= 0 && localValue10 >= localValue13) {
            localValue9.remove(localValue1);
            int localValue7 = localValue3 == null ? -1 : localValue9.indexOf(localValue3);
            int localValue8 = localValue7 >= 0 ? localValue7 + 1 : 0;
            localValue9.add(Math.min(localValue8, localValue9.size()), localValue1);
            this.internalField0675.internalMethod05668(localValue1);
         }
      }
   }

   private void internalMethod04731() {
      if (this.flow == null) {
         this.flow = this.build();
      }
   }

   private void internalMethod00751(DynamicIslandStatus localValue1) {
      if (localValue1 == this.internalField0363 && this.internalField0276 && System.currentTimeMillis() - this.internalField1059 >= 210L) {
         this.internalField0276 = false;
         this.internalField1099 = true;
      }

      if (!localValue1.isExpandable() || !this.internalMethod08519() && localValue1 != this.internalField0363) {
         this.internalField1099 = false;
      }

      this.internalField0808.internalMethod07062(this.internalField1099);
   }

   private boolean internalMethod08519() {
      return (internalField0149.currentScreen instanceof ChatScreen || internalField0149.player == null) && !this.select;
   }

   private void internalMethod05740(DynamicIslandStatus localValue1) {
      for (MultiSelectSetting.InternalType0091 localValue3 : this.internalField0675.internalMethod01792()) {
         if (localValue3 instanceof DynamicIslandStatus localValue4) {
            localValue4.getAnimation().internalMethod07059(localValue4 == localValue1 ? 1.0F : 0.0F);
         }
      }
   }

   private void internalMethod08861(DynamicIslandStatus localValue1) {
      localValue1.prepare(this);
      UiNode localValue2 = localValue1.element(this);
      localValue2.prepareRoot();
      MutableVector3 localValue3 = localValue1.getSize();
      this.internalField0814.internalMethod03893(localValue1.getColor());
      this.internalField0809.internalMethod07059(localValue1.radius(this));
      if (this.width <= 0.0F) {
         this.width = localValue3.internalField0205;
      }

      if (this.height <= 0.0F) {
         this.height = localValue3.internalField0206;
      }

      this.x = this.internalMethod04377(this.width);
      this.y = this.internalMethod08535();
      this.internalField0210.internalMethod03995(localValue3.internalField0205, localValue3.internalField0206);
      if (this.internalField0213 != localValue1 || localValue2.parent() != this.internalField0634) {
         this.internalField0634.internalMethod07849(List.of(localValue2));
         this.internalField0213 = localValue1;
      }
   }

   private float internalMethod07404(UiRenderContext localValue1) {
      long localValue2 = System.currentTimeMillis();
      float localValue4 = this.internalField1058 == 0L ? 16.0F : Math.min(64.0F, (float)(localValue2 - this.internalField1058));
      this.internalField1058 = localValue2;
      this.flow.prepareRoot();
      this.internalMethod08515();
      this.flow.tick(localValue4, localValue1.internalMethod05259(), localValue1.internalMethod05261());
      this.internalMethod08515();
      this.flow.tick(0.0F, localValue1.internalMethod05259(), localValue1.internalMethod05261());
      this.flow.draw(localValue1, this.internalMethod08517());
      return localValue4;
   }

   private void internalMethod08515() {
      float localValue1 = this.flow.w() > 0.0F ? this.flow.w() : this.width;
      this.flow.snapAt(this.internalMethod04377(localValue1), this.internalMethod08535());
   }

   private void internalMethod08518() {
      this.width = this.flow.w();
      this.height = this.flow.h();
      this.x = this.flow.x();
      this.y = this.flow.y();
      this.internalField0211.internalMethod03811(this.width, this.height, this.internalField0809.internalMethod02881());
   }

   private void internalMethod08267(DynamicIslandStatus localValue1) {
      if (!this.internalField1099
         && localValue1.isExpandable()
         && UiUtils.internalMethod05785(
            this.x, this.y, this.width, this.height, UiUtils.internalMethod03634().x(), UiUtils.internalMethod03634().y()
         )) {
         CursorManager.internalMethod06882(CursorType.internalField0567);
      }
   }

   private float internalMethod08517() {
      return Math.min(1.0F, this.animation.internalMethod02881() * this.visible.internalMethod02881());
   }

   private float internalMethod04377(float localValue1) {
      float localValue2 = 9.5F * this.internalField1322.internalMethod02881();
      return internalField0389.internalMethod03585() / 2.0F - localValue1 / 2.0F - localValue2;
   }

   private float internalMethod08535() {
      return internalField0149.currentScreen instanceof IslandGameScreen ? internalField0389.internalMethod03589() / 2.0F - 125.0F : 7.0F;
   }

   public void internalMethod04864(UiRenderContext localValue1, DynamicIslandContainer localValue2, float localValue3) {
      float localValue4 = localValue2.x();
      float localValue5 = localValue2.y();
      float localValue6 = localValue2.w();
      float localValue7 = localValue2.h();
      this.internalMethod01380(localValue1, localValue4, localValue5, localValue6);
      if (this.internalField0277) {
         this.internalMethod02030(localValue1, localValue4, localValue5, localValue6, localValue7, localValue3);
      } else if (this.internalField0212 == null || !this.internalField0212.drawsOwnBackground()) {
         this.internalMethod01392(localValue1, localValue4, localValue5, localValue6, localValue7, localValue3);
      }
   }

   private void internalMethod01392(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      CornerRadii localValue7 = CornerRadii.internalMethod03908(this.internalField0809.internalMethod02881() + 11.0F * this.internalField0808.internalMethod02881());
      localValue1.drawSquircle(
         localValue2 - 1.0F,
         localValue3 - 1.0F,
         localValue4 + 2.0F,
         localValue5 + 2.0F,
         2.0F + 5.0F * this.internalField0808.internalMethod02881(),
         localValue7,
         ThemeColors.internalField1312.withAlpha(25.5F * localValue6)
      );
      localValue1.drawBlurredRect(
         localValue2, localValue3, localValue4, localValue5, 45.0F, 2.0F + 2.0F * this.internalField0808.internalMethod02881(), localValue7, ColorRGBA.WHITE.withAlpha(255.0F * localValue6)
      );
      localValue1.drawSquircle(
         localValue2,
         localValue3,
         localValue4,
         localValue5,
         2.0F + 5.0F * this.internalField0808.internalMethod02881(),
         localValue7,
         this.internalField0814.internalMethod04159().withAlpha(216.75F * localValue6)
      );
   }

   private void internalMethod01380(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4) {
      if (internalField0149.player != null && !(internalField0149.currentScreen instanceof IslandGameScreen)) {
         float localValue5 = 1.0F - this.internalField0808.internalMethod02881();
         Matrix4f localValue6 = rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue1.getMatrices());
         SizedFont localValue7 = Fonts.internalField0449.internalMethod01432(7.0F);
         String localValue8 = TextUtils.internalMethod04048();
         this.internalField0203
            .internalMethod00921(
               localValue7.internalMethod01335(),
               localValue8,
               localValue7.internalMethod07850(),
               localValue6,
               localValue2 - Fonts.internalField0449.internalMethod01432(9.0F).internalMethod00965(localValue8) - 4.0F,
               localValue3 + 5.0F,
               localValue5
            );
         float localValue9 = 19.0F * this.internalField1323.internalMethod02881();
         if (!internalField0149.isInSingleplayer() && internalField0149.player.networkHandler.getPlayerListEntry(internalField0149.player.getUuid()) != null) {
            this.internalMethod02373(localValue1, localValue6, localValue2 + localValue4 + localValue9, localValue3, localValue5);
         } else {
            this.internalField0203.internalMethod02846("plane", localValue6, localValue2 + localValue4 + localValue9 + 8.0F, localValue3 + 3.5F, 8.0F, localValue5);
         }

         this.internalField0203.internalMethod00733();
      }
   }

   private MusicLyricsHud internalMethod05743(DynamicIslandStatus localValue1, List<DynamicIslandStatus> localValue2) {
      if (!(localValue1 instanceof ModuleStatusHud)) {
         return null;
      } else {
         for (DynamicIslandStatus localValue4 : localValue2) {
            if (localValue4 instanceof MusicLyricsHud localValue5) {
               return localValue5;
            }
         }

         return null;
      }
   }

   private void internalMethod07180(MusicLyricsHud localValue1) {
      boolean localValue2 = localValue1 != null;
      this.internalField1322.internalMethod07062(localValue2);
      if (!localValue2) {
         if (this.internalField0477 != null
            && (
               this.internalField0477.phase() == UiNode.InternalType0146.internalField0259
                  || this.internalField0477.phase() == UiNode.InternalType0146.internalField0260
            )) {
            this.internalField0477.beginExit(0.0F);
         }
      } else {
         if (this.internalField0477 == null) {
            this.internalField0477 = new DynamicIslandHud.InternalType0324();
            this.internalField0477.beginEnter(0.0F);
         } else if (this.internalField0477.phase() == UiNode.InternalType0146.internalField1091
            || this.internalField0477.phase() == UiNode.InternalType0146.internalField1090
            || this.internalField0477.phase() == UiNode.InternalType0146.internalField1089) {
            this.internalField0477.beginEnter(0.0F);
         }

         this.internalField0477.internalMethod03400(localValue1);
      }
   }

   private void internalMethod08536() {
      boolean localValue1 = this.internalField0477 != null
         && (
            this.internalField0477.phase() == UiNode.InternalType0146.internalField0259
               || this.internalField0477.phase() == UiNode.InternalType0146.internalField0260
         );
      this.internalField1323.internalMethod07062(localValue1);
      this.internalField0277 = this.internalField1323.internalMethod02881() > 0.004F
         || this.internalField0477 != null && this.internalField0477.phase() != UiNode.InternalType0146.internalField1090;
   }

   private void internalMethod02030(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      float localValue7 = this.internalField1323.internalMethod02881();
      float localValue8 = 7.5F;
      float localValue9 = localValue3 + localValue5 / 2.0F;
      float localValue10 = localValue2 + localValue4 - localValue8;
      float localValue11 = localValue2 + localValue4 + 4.0F + localValue8;
      float localValue12 = localValue10 + (localValue11 - localValue10) * localValue7;
      this.internalField1456 = localValue12;
      this.internalField1457 = localValue9;
      this.internalField1458 = localValue8;
      float localValue13 = 5.0F;
      float localValue14 = localValue2 - localValue13;
      float localValue15 = localValue3 - localValue13;
      float localValue16 = localValue12 + localValue8 + localValue13;
      float localValue17 = localValue3 + localValue5 + localValue13;
      float localValue18 = localValue16 - localValue14;
      float localValue19 = localValue17 - localValue15;
      float localValue20 = localValue12 - localValue8 - (localValue2 + localValue4);
      float localValue21 = Math.max(0.0F, 8.0F - 2.0F * Math.abs(localValue20));
      this.internalMethod06241(localValue1, localValue2, localValue3, localValue4, localValue5, this.internalField0809.internalMethod02881(), localValue6);
      this.internalMethod06241(localValue1, localValue12 - localValue8, localValue9 - localValue8, localValue8 * 2.0F, localValue8 * 2.0F, localValue8, localValue6);
      ColorRGBA localValue22 = this.internalField0814.internalMethod04159().withAlpha(216.75F);
      ColorRGBA localValue23 = ThemeColors.internalField1312.withAlpha(25.5F);
      RenderPipeline.internalMethod05386(
         localValue1.getMatrices(),
         localValue14,
         localValue15,
         localValue18,
         localValue19,
         localValue2 + localValue4 / 2.0F - localValue14,
         localValue9 - localValue15,
         localValue4 / 2.0F,
         localValue5 / 2.0F,
         this.internalField0809.internalMethod02881(),
         localValue12 - localValue14,
         localValue9 - localValue15,
         localValue8,
         localValue21,
         1.2F,
         localValue22,
         localValue23,
         localValue6
      );
   }

   private void internalMethod06241(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, 45.0F, 2.0F, CornerRadii.internalMethod03908(localValue6), ColorRGBA.WHITE.withAlpha(255.0F * localValue7));
   }

   private void internalMethod02773(UiRenderContext localValue1, float localValue2) {
      if (this.internalField0477 != null && this.internalField0477.phase() != UiNode.InternalType0146.internalField1090) {
         this.internalField0477.snapAt(this.internalField1456 - 7.5F, this.internalField1457 - 7.5F);
         this.internalField0477.prepareRoot();
         this.internalField0477.tick(localValue2, localValue1.internalMethod05259(), localValue1.internalMethod05261());
         this.internalField0477.draw(localValue1, this.internalMethod08517());
      }
   }

   private void internalMethod02373(UiRenderContext localValue1, Matrix4f localValue2, float localValue3, float localValue4, float localValue5) {
      this.internalField1321
         .internalMethod07062(UiUtils.internalMethod06450(localValue3 + 4.0F + 4.0F * this.internalField1321.internalMethod02881(), localValue4 + 5.0F, 12.8F, 7.0, localValue1));
      this.internalField0203.internalMethod04015(localValue2, localValue3 + 10.0F, localValue4 + 8.0F);
      int localValue6 = internalField0149.player.networkHandler.getPlayerListEntry(internalField0149.player.getUuid()).getLatency();
      SizedFont localValue7 = Fonts.internalField0449.internalMethod01432(7.0F);
      this.internalField0203
         .internalMethod00921(
            localValue7.internalMethod01335(),
            localValue6 + " ms",
            localValue7.internalMethod07850(),
            localValue2,
            localValue3 + 4.0F + 4.0F * this.internalField1321.internalMethod02881(),
            localValue4 + 5.0F,
            localValue5 * this.internalField1321.internalMethod02881()
         );
      float localValue8 = 1.0F - this.internalField1321.internalMethod02881();
      if (localValue8 > 0.0F) {
         float localValue9 = localValue5 * 0.2F * localValue8;
         float localValue10 = localValue5 * localValue8;
         float localValue11 = localValue10 + localValue9 * (1.0F - localValue10);

         for (int localValue12 = 0; localValue12 < internalField0618.length; localValue12++) {
            float localValue13 = localValue6 < internalField0618[localValue12] ? localValue11 : localValue9;
            this.internalField0203
               .internalMethod02384(
                  localValue2, localValue3 + 9.0F + localValue12 * 2.7F + 4.0F * this.internalField1321.internalMethod02881(), localValue4 + 8.0F - localValue12, 2.0F, 3 + localValue12, localValue13
               );
         }
      }

      this.internalField0203.internalMethod00656();
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (localValue5 != MouseButton.internalField0102 || !this.internalMethod02749((float)localValue1, (float)localValue3)) {
         if (localValue5 == MouseButton.internalField0101) {
            super.onMouseClicked(localValue1, localValue3, localValue5);
         }

         this.internalMethod02613((float)localValue1, (float)localValue3, localValue5.internalMethod02957());
      }
   }

   public boolean internalMethod02613(float localValue1, float localValue2, int localValue3) {
      boolean localValue4 = UiUtils.internalMethod05785(this.x, this.y, this.width, this.height, localValue1, localValue2);
      if (localValue4 && localValue3 == 0 && this.internalMethod08537()) {
         return true;
      } else {
         DynamicIslandStatus localValue5 = this.internalMethod07528();
         if (localValue5 == null) {
            return false;
         } else if (this.internalField1099) {
            if (!localValue4) {
               this.internalField1099 = false;
               this.internalMethod08538();
            } else {
               this.internalMethod00299(localValue5, localValue1, localValue2, localValue3);
            }

            return true;
         } else if (!localValue4) {
            return false;
         } else {
            if (localValue5.isExpandable()) {
               this.internalField1099 = true;
               if (localValue5 == this.internalField0363) {
                  this.internalField0276 = false;
               }
            } else {
               this.internalMethod00299(localValue5, localValue1, localValue2, localValue3);
            }

            return true;
         }
      }
   }

   private boolean internalMethod02749(float localValue1, float localValue2) {
      if (this.internalField0477 != null
         && (
            this.internalField0477.phase() == UiNode.InternalType0146.internalField0259
               || this.internalField0477.phase() == UiNode.InternalType0146.internalField0260
         )
         && this.internalField0477.contains(localValue1, localValue2)) {
         MusicLyricsHud localValue3 = this.internalField0477.internalMethod03154();
         if (localValue3 == null) {
            return false;
         } else {
            this.internalField0363 = localValue3;
            this.internalField0276 = true;
            this.internalField1059 = System.currentTimeMillis();
            this.internalField1099 = false;
            return true;
         }
      } else {
         return false;
      }
   }

   private void internalMethod08538() {
      this.internalField0363 = null;
      this.internalField0276 = false;
      this.internalField1059 = 0L;
   }

   private void internalMethod00299(DynamicIslandStatus localValue1, float localValue2, float localValue3, int localValue4) {
      UiNode localValue5 = localValue1.element(this);
      if (!localValue5.mouseClicked(localValue2, localValue3, MouseButton.internalMethod01669(localValue4))) {
         localValue1.click(localValue2, localValue3, localValue4);
      }
   }

   private boolean internalMethod08537() {
      long localValue1 = System.currentTimeMillis();
      if (localValue1 - this.internalField1060 > 900L) {
         this.internalField0228 = 0;
      }

      this.internalField1060 = localValue1;
      this.internalField0228++;
      if (this.internalField0228 >= 5 && !this.internalMethod07881().stream().noneMatch(localValue0 -> localValue0 instanceof DynamicIslandDefaultStatus)) {
         this.internalField0228 = 0;
         this.internalField1099 = false;
         if (!InterfaceModule.internalMethod09917()) {
            internalField0149.setScreen(new IslandGameScreen());
         }

         return true;
      } else {
         return false;
      }
   }

   @Generated
   public MultiSelectSetting internalMethod03091() {
      return this.internalField0675;
   }

   @Generated
   public BooleanSetting internalMethod03026() {
      return this.internalField0650;
   }

   @Generated
   public TextSetting internalMethod00849() {
      return this.internalField0384;
   }

   @Generated
   public MutableVector3 internalMethod07527() {
      return this.internalField0211;
   }

   @Generated
   public AnimatedValue internalMethod05767() {
      return this.internalField0808;
   }

   @Generated
   public AnimatedValue internalMethod06413() {
      return this.internalField0809;
   }

   @Generated
   public AnimatedValue internalMethod08419() {
      return this.internalField1321;
   }

   @Generated
   public AnimatedValue internalMethod08549() {
      return this.internalField1322;
   }

   @Generated
   public AnimatedValue internalMethod07840() {
      return this.internalField1323;
   }

   @Generated
   public AnimatedColor internalMethod05769() {
      return this.internalField0814;
   }

   @Generated
   public AdaptiveUiBatchRenderer internalMethod07485() {
      return this.internalField0203;
   }

   @Generated
   public DynamicIslandContainer internalMethod07526() {
      return this.internalField0210;
   }

   @Generated
   public UiContainer internalMethod06521() {
      return this.internalField0634;
   }

   @Generated
   public DynamicIslandStatus internalMethod00065() {
      return this.internalField0212;
   }

   @Generated
   public DynamicIslandStatus internalMethod08784() {
      return this.internalField0213;
   }

   @Generated
   public DynamicIslandHud.InternalType0324 internalMethod06829() {
      return this.internalField0477;
   }

   @Generated
   public boolean internalMethod04725() {
      return this.internalField0277;
   }

   @Generated
   public float internalMethod04721() {
      return this.internalField1456;
   }

   @Generated
   public float internalMethod04729() {
      return this.internalField1457;
   }

   @Generated
   public float internalMethod08513() {
      return this.internalField1458;
   }

   @Generated
   public MusicLyricsHud internalMethod00729() {
      return this.internalField0363;
   }

   @Generated
   public boolean internalMethod04732() {
      return this.internalField0276;
   }

   @Generated
   public long internalMethod04723() {
      return this.internalField1059;
   }

   @Generated
   public long internalMethod04730() {
      return this.internalField1058;
   }

   @Generated
   public boolean internalMethod08516() {
      return this.internalField1099;
   }

   @Generated
   public int internalMethod04722() {
      return this.internalField0228;
   }

   @Generated
   public long internalMethod08514() {
      return this.internalField1060;
   }

   final class InternalType0324 extends UiNode {
      private static final int internalField0227 = 4;
      private MusicLyricsHud internalField0363;

      InternalType0324() {
         this.size(15.0F, 15.0F);
         this.motion(Motion.internalMethod01328(260L, Easing.internalField1626));
         this.lifeMotion(Motion.internalMethod01328(260L, Easing.internalField1626));
         this.transition(DynamicIslandHud.internalField0918);
         this.interactive(false);
      }

      void internalMethod03400(MusicLyricsHud localValue1) {
         this.internalField0363 = localValue1;
      }

      MusicLyricsHud internalMethod03154() {
         return this.internalField0363;
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         if (this.internalField0363 != null) {
            this.internalMethod03434(localValue1, localValue2);
         }
      }

      private void internalMethod03434(UiRenderContext localValue1, float localValue2) {
         float localValue3 = 1.0F;
         float localValue4 = 0.9F;
         float localValue5 = 4.0F * localValue3 + 3.0F * localValue4;
         float localValue6 = this.x() + (this.w() - localValue5) / 2.0F;
         float[] localValue7 = this.internalField0363.internalMethod03122();
         ColorRGBA localValue8 = this.internalField0363.internalMethod06918().withAlpha(255.0F * localValue2);

         for (int localValue9 = 0; localValue9 < 4; localValue9++) {
            float localValue10 = Math.clamp(1.5F + localValue7[localValue9] * 0.75F, 2.5F, 9.5F);
            float localValue11 = localValue6 + localValue9 * (localValue3 + localValue4);
            localValue1.drawRoundedRect(localValue11, this.y() + (this.h() - localValue10) / 2.0F, localValue3, localValue10, CornerRadii.internalMethod03908(localValue3 / 2.0F), localValue8);
         }
      }
   }
}
