package rockstar.client.internal.script;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.framework.*;
import rockstar.client.*;
import dev.redstones.mediaplayerinfo.IMediaSession;
import dev.redstones.mediaplayerinfo.MediaInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Identifier;
import pyrock.events.window.MouseScrollEvent;
import pyrock.utility.render.ColorRGBA;

public class MusicLyricsHud extends IslandGroupStatus implements MinecraftClientAccess {
   private static final float internalField0205 = 0.5F;
   private static final float internalField0206 = 164.0F;
   private static final float internalField1048 = 80.0F;
   private static final float internalField1047 = 125.0F;
   private static final float internalField1049 = 15.0F;
   private static final float internalField1046 = 144.0F;
   private static final float internalField1456 = 32.0F;
   private static final float internalField1457 = 0.7F;
   private static final float internalField1458 = 16.0F;
   private static final float internalField1459 = 8.0F;
   private static final float internalField1460 = 3.0F;
   private static final String internalField0248 = "lyrics-expansion";
   private static final float internalField1461 = 95.0F;
   private static final long internalField0229 = 3000L;
   private static final float[] internalField0615 = new float[]{1.0F, 2.0F, 0.5F, 1.5F};
   private static final float[] internalField0616 = new float[]{3.0F, 1.5F, 2.5F, 4.0F};
   private static final float[] internalField1238 = new float[]{0.0F, 1.9F, 4.2F, 2.7F};
   final float[] internalField1240 = new float[4];
   private long internalField0230 = System.currentTimeMillis();
   final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField1327);
   final AnimatedValue internalField0809 = new AnimatedValue(600L, 0.0F, Easing.internalField1626);
   float internalField1455;
   int internalField0227 = 0;
   float internalField1723 = -1.0F;
   long internalField1059;
   float internalField1731;
   private UiContainer internalField0634;
   private final AnimatedValue internalField1321 = new AnimatedValue(260L, 0.0F, Easing.internalField1626);
   int internalField0228 = -1;
   String internalField0247 = "";
   float internalField1727;
   private final EventListener<MouseScrollEvent> internalField0157 = localValue1x -> {
      if (this.internalMethod04973()) {
         DynamicIslandHud localValue2 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
         if (localValue2 != null && localValue2.internalMethod07528() == this && localValue2.internalMethod08516()) {
            Vector2f localValue3 = UiUtils.internalMethod03634();
      if (UiUtils.internalMethod05785(localValue2.getX(), localValue2.getY(), localValue2.getWidth(), localValue2.getHeight(), localValue3.x(), localValue3.y())) {
               LyricsTrack localValue4 = this.internalMethod03658().internalMethod02718();
               if (!localValue4.internalMethod00823()) {
                  if (localValue1x.getVerticalAmount() < 0.0) {
                     this.internalField0227++;
                  } else {
                     if (!(localValue1x.getVerticalAmount() > 0.0)) {
                        return;
                     }

                     this.internalField0227--;
                  }

                  int localValue5 = Math.max(0, localValue4.internalMethod00129().size() - 6);
                  this.internalField0227 = Math.clamp((long)this.internalField0227, 0, localValue5);
               }
            }
         }
      }
   };

   public MusicLyricsHud(MultiSelectSetting localValue1) {
      super(localValue1, "music");
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   @Override
   public void prepare(DynamicIslandHud localValue1) {
      this.internalMethod04968();
   }

   public UiContainer internalMethod01155(DynamicIslandHud localValue1) {
      if (this.internalField0634 == null) {
         this.internalField0634 = new MusicLyricsHud.InternalType0406(localValue1);
      }

      return this.internalField0634;
   }

   public Identifier internalMethod00023() {
      Identifier localValue1 = this.internalMethod03658().internalMethod00930();
      return localValue1 != null ? localValue1 : RockstarClient.id("icons/music/no_image.png");
   }

   public String internalMethod00841() {
      float localValue1 = this.internalMethod03658().internalMethod07172();
      return localValue1 > 0.0F ? String.valueOf(Math.round(localValue1)) : "--";
   }

   public ColorRGBA internalMethod06918() {
      return this.internalMethod03658().internalMethod02369();
   }

   public float[] internalMethod03122() {
      this.internalMethod04972();
      return this.internalField1240;
   }

   MusicLyricsHud.InternalType0405 internalMethod01872(BooleanSupplier localValue1, Runnable localValue2, MusicLyricsHud.InternalType0118 localValue3) {
      return this.internalMethod02074(16.0F, localValue1, localValue2, localValue3);
   }

   MusicLyricsHud.InternalType0405 internalMethod02074(float localValue1, BooleanSupplier localValue2, Runnable localValue3, MusicLyricsHud.InternalType0118 localValue4) {
      MusicLyricsHud.InternalType0405 localValue5 = new MusicLyricsHud.InternalType0405(localValue2);
      localValue5.size(localValue1, localValue1);
      localValue5.onClick(localValue3);
      localValue5.paint((localValue2x, localValue3x) -> localValue4.paint(localValue2x, localValue5));
      return localValue5;
   }

   @Override
   public boolean canShow() {
      IMediaSession localValue1 = this.internalMethod05622();
      if (localValue1 == null) {
         return false;
      } else {
         String localValue2 = localValue1.getOwner();
         return localValue2 == null || !localValue2.toLowerCase(Locale.ROOT).contains("gram");
      }
   }

   @Override
   public ColorRGBA getColor() {
      return ThemeColors.internalMethod02435().internalMethod09722() == 1.0F
         ? super.getColor().mix(this.internalMethod03658().internalMethod02369(), 0.2F)
         : super.getColor();
   }

   public MediaSessionProvider internalMethod03658() {
      return RockstarClient.getInstance().internalMethod05636();
   }

   public IMediaSession internalMethod05622() {
      MediaSessionProvider localValue1 = this.internalMethod03658();
      return !localValue1.internalMethod07174() ? null : localValue1.internalMethod04514();
   }

   public MediaInfo internalMethod01181() {
      IMediaSession localValue1 = this.internalMethod05622();
      return localValue1 == null ? null : localValue1.getMedia();
   }

   public String internalMethod04727(Function<MediaInfo, String> localValue1) {
      MediaInfo localValue2 = this.internalMethod01181();
      if (localValue2 == null) {
         return "";
      } else {
         String localValue3 = (String)localValue1.apply(localValue2);
         return localValue3 == null ? "" : localValue3;
      }
   }

   public float internalMethod06470(MediaInfo localValue1) {
      float localValue2 = this.internalField0228 >= 0 ? 127.0F : 144.0F;
      return Math.max(48.0F, Math.min(this.internalMethod01058(localValue1), localValue2));
   }

   private float internalMethod01058(MediaInfo localValue1) {
      if (this.internalField0228 >= 0) {
         return 32.0F + Fonts.internalField0449.internalMethod01432(7.0F).internalMethod00965(this.internalField0247) + 6.0F;
      } else {
         String localValue2 = localValue1.getTitle();
         return 32.0F + Fonts.internalField0449.internalMethod01432(7.0F).internalMethod00965(localValue2 == null ? "" : localValue2);
      }
   }

   private void internalMethod04968() {
      MediaInfo localValue1 = this.internalMethod01181();
      LyricsTrack localValue2 = this.internalMethod03658().internalMethod02718();
      int localValue3 = -1;
      if (localValue1 != null && localValue1.isPlaying() && localValue2.internalMethod00823() && this.internalMethod04973()) {
         double localValue4 = this.internalMethod03658().internalMethod07171() * 1000.0 + 150.0;
         long localValue6 = localValue1.getDuration() * 1000L;
         localValue3 = localValue2.internalMethod05414(localValue4, localValue6, 3000L);
         if (localValue3 >= 0) {
            this.internalField0247 = localValue2.internalMethod00129().get(localValue3).internalMethod00756();
            this.internalField1727 = localValue2.internalMethod02234(localValue3, localValue4, localValue6);
         }
      }

      this.internalField0228 = localValue3;
      this.internalField1321.internalMethod07062(localValue3 >= 0);
   }

   public float internalMethod04967() {
      return this.internalField1321.internalMethod02881() * (1.0F - this.internalMethod08304());
   }

   public float internalMethod04971() {
      return this.internalMethod08305() ? 125.0F : 80.0F;
   }

   public boolean internalMethod04969() {
      return !this.internalMethod03658().internalMethod02718().internalMethod00819();
   }

   public boolean internalMethod04973() {
      DynamicIslandHud localValue1 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
      return localValue1 == null || localValue1.internalMethod03026().internalMethod04496();
   }

   public void internalMethod03124(boolean localValue1) {
      DynamicIslandHud localValue2 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
      if (localValue2 != null) {
         localValue2.internalMethod03026().internalMethod02034(localValue1);
         if (localValue1) {
            this.internalField0227 = 0;
         }

         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      }
   }

   public boolean internalMethod08305() {
      return this.internalMethod04973() && this.internalMethod04969();
   }

   public float internalMethod08304() {
      DynamicIslandHud localValue1 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
      return localValue1 == null ? 0.0F : localValue1.internalMethod05767().internalMethod02881();
   }

   public float internalMethod08306() {
      return this.internalField1731;
   }

   public String internalMethod05549() {
      IMediaSession localValue1 = this.internalMethod05622();
      if (localValue1 != null && localValue1.getOwner() != null) {
         String localValue2 = localValue1.getOwner().toLowerCase(Locale.ROOT);
         if (localValue2.contains("yandex") || localValue2.contains("\u044f\u043d\u0434\u0435\u043a\u0441")) {
            return "yandex_music";
         } else if (localValue2.contains("edge")) {
            return "edge";
         } else {
            return localValue2.contains("spotify") ? "spotify" : null;
         }
      } else {
         return null;
      }
   }

   public String internalMethod09033() {
      return Math.round(this.internalField1455 * this.internalField0809.internalMethod02881()) + " BPM";
   }

   public float internalMethod08314() {
      MediaInfo localValue1 = this.internalMethod01181();
      if (localValue1 != null && localValue1.isPlaying()) {
         float localValue2 = this.internalMethod03658().internalMethod07172();
         float localValue3 = localValue2 > 0.0F ? localValue2 : 120.0F;
         double localValue4 = this.internalMethod03658().internalMethod07171() * localValue3 / 60.0;
         return (float)Math.pow(1.0 - (localValue4 - Math.floor(localValue4)), 4.0);
      } else {
         return 0.0F;
      }
   }

   public void internalMethod04972() {
      MediaInfo localValue1 = this.internalMethod01181();
      if (localValue1 != null) {
         float localValue2 = this.internalMethod03658().internalMethod07172();
         float localValue3 = localValue2 > 0.0F ? localValue2 : 120.0F;
         double localValue4 = this.internalMethod03658().internalMethod07171() * localValue3 / 60.0;
         long localValue6 = System.currentTimeMillis();
         float localValue8 = Math.min((float)(localValue6 - this.internalField0230) / 1000.0F, 0.05F);
         this.internalField0230 = localValue6;

         for (int localValue9 = 0; localValue9 < this.internalField1240.length; localValue9++) {
            float localValue10;
            if (localValue1.isPlaying()) {
               double localValue11 = localValue4 - localValue9 * 0.05;
               float localValue13 = (float)(localValue11 - Math.floor(localValue11));
               float localValue14 = (float)Math.pow(1.0F - localValue13, 6.0) * 4.0F;
               float localValue15 = localValue13 >= 0.5F ? (float)Math.pow(1.0F - (localValue13 - 0.5F) * 2.0F, 6.0) * 1.6F : 0.0F;
               float localValue16 = (float)(
                  Math.abs(MathUtils.internalMethod04857(localValue11 * Math.PI * internalField0615[localValue9] + internalField1238[localValue9])) * 0.65
                     + Math.abs(MathUtils.internalMethod04857(localValue11 * Math.PI * internalField0616[localValue9] + internalField1238[localValue9] * 1.7)) * 0.35
               );
               float localValue17 = 0.5F + 0.5F * (float)MathUtils.internalMethod04857(localValue11 * Math.PI * 0.25 + localValue9 * 2.1);
               localValue10 = Math.min(1.2F + localValue16 * (2.2F + 2.6F * localValue17) + (localValue14 + localValue15) * (0.7F + 0.3F * localValue17), 10.0F);
            } else {
               localValue10 = 3.0F;
            }

            float localValue18 = localValue10 > this.internalField1240[localValue9] ? 45.0F : 9.0F;
            this.internalField1240[localValue9] = this.internalField1240[localValue9] + (localValue10 - this.internalField1240[localValue9]) * Math.min(1.0F, localValue8 * localValue18);
         }
      }
   }

   public static float internalMethod00335(float localValue0, float localValue1, float localValue2) {
      if (localValue2 <= localValue1) {
         return localValue0 >= localValue2 ? 1.0F : 0.0F;
      } else {
         float localValue3 = internalMethod03123((localValue0 - localValue1) / (localValue2 - localValue1));
         return Easing.internalField1822.ease(localValue3, 0.0F, 1.0F, 1.0F);
      }
   }

   public static float internalMethod03123(float localValue0) {
      return Math.max(0.0F, Math.min(1.0F, localValue0));
   }

   public static float internalMethod05364(float localValue0, float localValue1, float localValue2) {
      return localValue0 + (localValue1 - localValue0) * internalMethod03123(localValue2);
   }

   public static String internalMethod06397(long localValue0) {
      long localValue2 = localValue0 / 60L;
      long localValue4 = localValue0 % 60L;
      return String.format("%d:%02d", localValue2, localValue4);
   }

   interface InternalType0118 {
      void paint(UiRenderContext localValue1, MusicLyricsHud.InternalType0405 localValue2);
   }

   final class InternalType0119 extends UiNode {
      private static final float internalField0205 = 240.0F;
      private static final float internalField0206 = 16.0F;
      private static final float internalField1048 = 320.0F;
      private static final float internalField1047 = 0.13F;
      private static final float internalField1049 = 3.0F;
      private static final float internalField1046 = 0.42F;
      private static final float internalField1456 = 9.0F;
      private static final float internalField1457 = 5.0F;
      private static final float internalField1458 = 180.0F;
      private static final float internalField1459 = 220.0F;
      private static final float internalField1460 = 6.0F;
      private static final float internalField1461 = 90.0F;
      private final SizedFont internalField0447 = Fonts.internalField0449.internalMethod01432(7.0F);
      private MusicLyricsHud.InternalType0121 internalField0952;
      private MusicLyricsHud.InternalType0121 internalField0953;
      private int internalField0227 = -1;
      private String internalField0248 = "";
      private long internalField0229 = System.currentTimeMillis();

      InternalType0119() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         long localValue3 = System.currentTimeMillis();
         float localValue5 = Math.min(0.05F, Math.max(0.0F, (float)(localValue3 - this.internalField0229) / 1000.0F));
         this.internalField0229 = localValue3;
         this.internalMethod03140(localValue3);
         float localValue6 = MusicLyricsHud.this.internalMethod04967();
         if (!(localValue6 <= 0.003F)) {
            float localValue7 = Math.max(1.0F, this.w());
            if (this.internalField0953 != null) {
               if ((float)(localValue3 - this.internalField0953.internalField0230) >= 400.0F) {
                  this.internalField0953 = null;
               } else {
                  this.internalMethod00805(localValue1, this.internalField0953, -1, localValue6, localValue7, localValue3, true);
               }
            }

            if (this.internalField0952 != null) {
               int localValue8 = this.internalMethod01885(this.internalField0952);
               this.internalMethod04548(this.internalField0952, localValue8, localValue5);
               this.internalMethod01886(this.internalField0952);
               this.internalMethod00104(this.internalField0952, localValue7, localValue8, localValue5);
               this.internalField0952.internalField1049 = this.internalMethod01884(this.internalField0952);
               this.internalMethod00805(localValue1, this.internalField0952, localValue8, localValue6, localValue7, localValue3, false);
            }
         }
      }

      private void internalMethod00805(UiRenderContext localValue1, MusicLyricsHud.InternalType0121 localValue2, int localValue3, float localValue4, float localValue5, long localValue6, boolean localValue8) {
         if (localValue2.internalMethod00161() != 0) {
            float localValue9 = Math.min(16.0F, (localValue8 ? 220.0F : 320.0F) / localValue2.internalField0359.length);
            float localValue10 = (float)(localValue6 - (localValue8 ? localValue2.internalField0230 : localValue2.internalField0229));
            if (!(localValue10 <= 0.0F)) {
               ScissorStack.internalMethod06303(
                  localValue1.getMatrices(),
                  this.x() - 1.0F,
                  this.y() - 1.5F,
                  localValue5 + 2.0F,
                  this.internalField0447.internalMethod04890() + this.internalField0447.internalMethod07836() + 3.0F
               );

               for (int localValue11 = 0; localValue11 < localValue2.internalMethod00161(); localValue11++) {
                  this.internalMethod05443(localValue1, localValue2, localValue11, localValue3, localValue4, localValue10, localValue9, localValue5, localValue8);
               }

               ScissorStack.internalMethod07643();
            }
         }
      }

      private void internalMethod05443(
         UiRenderContext localValue1, MusicLyricsHud.InternalType0121 localValue2, int localValue3, int localValue4, float localValue5, float localValue6, float localValue7, float localValue8, boolean localValue9
      ) {
         float localValue10 = localValue2.internalField1240[localValue3];
         float localValue11 = localValue2.internalField1239[localValue3];
         float localValue12 = (localValue10 - 1.0F) * 3.0F;
         boolean localValue13 = Math.abs(localValue10 - 1.0F) > 0.002F;
         if (localValue13) {
            HudRenderUtils.internalMethod08976(
               localValue1.getMatrices(),
               this.x() - localValue2.internalField1048 + localValue11 + (localValue2.internalField0616[localValue3] + localValue2.internalField1238[localValue3]) / 2.0F,
               this.y() + this.internalField0447.internalMethod04890() / 2.0F,
               localValue10
            );
         }

         ColorRGBA localValue14 = ThemeColors.internalMethod08459();
         boolean localValue15 = localValue2.internalField1049 <= localValue2.internalField0616[localValue3] || localValue2.internalField1049 >= localValue2.internalField1238[localValue3];
         boolean localValue16 = !localValue9 && localValue6 - localValue2.internalField0617[localValue3] * localValue7 >= 240.0F;
         if (localValue15 && localValue16 && localValue2.internalField0205 <= localValue8) {
            float localValue25 = localValue2.internalField1049 >= localValue2.internalField1238[localValue3] ? 1.0F : 0.42F;
            localValue1.drawText(
               this.internalField0447,
               localValue2.internalField0358[localValue3],
               this.x() - localValue2.internalField1048 + localValue11 + localValue2.internalField0616[localValue3],
               this.y() - localValue12,
               localValue14.withAlpha(255.0F * localValue5 * localValue25)
            );
            if (localValue13) {
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }
         } else {
            for (int localValue17 = localValue2.internalField0618[localValue3]; localValue17 <= localValue2.internalField0617[localValue3]; localValue17++) {
               float localValue18 = localValue9
                  ? 1.0F - MusicLyricsHud.internalMethod03123((localValue6 - localValue17 * localValue7) / 180.0F)
                  : MusicLyricsHud.internalMethod03123((localValue6 - localValue17 * localValue7) / 240.0F);
               if (!(localValue18 <= 0.001F)) {
                  float localValue19 = Easing.internalField1822.ease(localValue18, 0.0F, 1.0F, 1.0F);
                  float localValue20 = this.x() - localValue2.internalField1048 + localValue11 + localValue2.internalField0615[localValue17];
                  float localValue21 = this.internalMethod04547(localValue2, localValue20, localValue8);
                  if (!(localValue21 <= 0.001F)) {
                     float localValue22 = MusicLyricsHud.internalMethod03123(
                        (localValue2.internalField1049 - localValue2.internalField0615[localValue17]) / Math.max(0.5F, localValue2.internalMethod03013(localValue17) * 0.55F)
                     );
                     float localValue23 = 0.42F + 0.58000004F * localValue22;
                     float localValue24 = (1.0F - localValue19) * (localValue9 ? -6.0F : 6.0F);
                     localValue1.drawText(
                        this.internalField0447,
                        localValue2.internalField0359[localValue17],
                        localValue20,
                        this.y() - localValue12 + localValue24,
                        localValue14.withAlpha(255.0F * localValue5 * localValue23 * localValue19 * localValue21)
                     );
                  }
               }
            }

            if (localValue13) {
               HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            }
         }
      }

      private float internalMethod01884(MusicLyricsHud.InternalType0121 localValue1) {
         float localValue2 = MusicLyricsHud.internalMethod03123(MusicLyricsHud.this.internalField1727) * localValue1.internalField0359.length;
         if (localValue2 >= localValue1.internalField0359.length) {
            return localValue1.internalField0205;
         } else {
            int localValue3 = (int)localValue2;
            return localValue1.internalField0615[localValue3] + localValue1.internalMethod03013(localValue3) * (localValue2 - localValue3);
         }
      }

      private void internalMethod03140(long localValue1) {
         if (MusicLyricsHud.this.internalField0228 >= 0
            && (MusicLyricsHud.this.internalField0228 != this.internalField0227 || !MusicLyricsHud.this.internalField0247.equals(this.internalField0248))) {
            this.internalField0227 = MusicLyricsHud.this.internalField0228;
            this.internalField0248 = MusicLyricsHud.this.internalField0247;
            long localValue3 = localValue1;
            if (this.internalField0952 != null) {
               this.internalField0952.internalField0230 = localValue1;
               this.internalField0953 = this.internalField0952;
               localValue3 = localValue1 + 90L;
            }

            this.internalField0952 = new MusicLyricsHud.InternalType0121(
               this.internalField0447, TextUtils.internalMethod06864(MusicLyricsHud.this.internalField0247), localValue3
            );
         }
      }

      private int internalMethod01885(MusicLyricsHud.InternalType0121 localValue1) {
         for (int localValue2 = localValue1.internalMethod00161() - 1; localValue2 > 0; localValue2--) {
            if (MusicLyricsHud.this.internalField1727 * localValue1.internalField0359.length >= localValue1.internalField0618[localValue2]) {
               return localValue2;
            }
         }

         return 0;
      }

      private void internalMethod04548(MusicLyricsHud.InternalType0121 localValue1, int localValue2, float localValue3) {
         float localValue4 = Math.min(1.0F, localValue3 * 11.0F);

         for (int localValue5 = 0; localValue5 < localValue1.internalMethod00161(); localValue5++) {
            float localValue6 = localValue5 == localValue2 ? 1.13F : 1.0F;
            localValue1.internalField1240[localValue5] = localValue1.internalField1240[localValue5] + (localValue6 - localValue1.internalField1240[localValue5]) * localValue4;
         }
      }

      private void internalMethod01886(MusicLyricsHud.InternalType0121 localValue1) {
         float localValue2 = 0.0F;

         for (int localValue3 = 0; localValue3 < localValue1.internalMethod00161(); localValue3++) {
            float localValue4 = (localValue1.internalField1240[localValue3] - 1.0F) * (localValue1.internalField1238[localValue3] - localValue1.internalField0616[localValue3] + 5.0F);
            localValue1.internalField1239[localValue3] = localValue2 + localValue4 / 2.0F;
            localValue2 += localValue4;
         }

         localValue1.internalField0206 = localValue2;
      }

      private void internalMethod00104(MusicLyricsHud.InternalType0121 localValue1, float localValue2, int localValue3, float localValue4) {
         float localValue5 = Math.max(0.0F, localValue1.internalField0205 - localValue2);
         localValue1.internalField1047 = localValue5;
         if (localValue5 <= 0.5F) {
            localValue1.internalField1048 = localValue1.internalField1048 - localValue1.internalField1048 * Math.min(1.0F, localValue4 * 9.0F);
         } else {
            float localValue6 = Math.max(localValue5, localValue1.internalField0205 + localValue1.internalField0206 - localValue2);
            float localValue7 = Math.clamp(localValue1.internalField1238[localValue3] + localValue1.internalField1239[localValue3] - localValue2 * 0.6F, 0.0F, localValue6);
            if (!(localValue7 <= localValue1.internalField1048)) {
               localValue1.internalField1048 = localValue1.internalField1048 + (localValue7 - localValue1.internalField1048) * (1.0F - (float)Math.exp(-9.0F * localValue4));
            }
         }
      }

      private float internalMethod04547(MusicLyricsHud.InternalType0121 localValue1, float localValue2, float localValue3) {
         if (localValue1.internalField0205 <= localValue3) {
            return 1.0F;
         } else {
            float localValue4 = localValue2 - this.x();
            float localValue5 = 1.0F - (1.0F - MusicLyricsHud.internalMethod03123(localValue4 / 9.0F)) * MusicLyricsHud.internalMethod03123(localValue1.internalField1048 / 9.0F);
            float localValue6 = 1.0F
               - (1.0F - MusicLyricsHud.internalMethod03123((localValue3 - localValue4) / 9.0F))
                  * MusicLyricsHud.internalMethod03123((localValue1.internalField1047 - localValue1.internalField1048) / 9.0F);
            return Math.min(localValue5, localValue6);
         }
      }

      float internalMethod03277() {
         return this.internalField0447.internalMethod04890();
      }
   }

   final class InternalType0120 extends UiNode {
      private final SizedFont internalField0447;
      private final Supplier<String> internalField0017;
      private final Supplier<ColorRGBA> internalField0018;
      private final float internalField0205;
      private final float internalField0206;

      InternalType0120(SizedFont localValue2, Supplier<String> localValue3, Supplier<ColorRGBA> localValue4, float localValue5, float localValue6) {
         this.internalField0447 = localValue2;
         this.internalField0017 = localValue3;
         this.internalField0018 = localValue4;
         this.internalField0205 = localValue5;
         this.internalField0206 = localValue6;
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         String localValue3 = this.internalMethod01843();
         if (!localValue3.isEmpty()) {
            ColorRGBA localValue4 = this.internalField0018.get();
            if (localValue4 != null) {
               float localValue5 = Math.max(0.0F, this.w());
               if (!(localValue5 <= 0.0F)) {
                  if (this.internalField0447.internalMethod00965(localValue3) <= localValue5) {
                     localValue1.drawText(this.internalField0447, localValue3, this.x(), this.y(), localValue4);
                  } else {
                     ScissorStack.internalMethod06303(
                        localValue1.getMatrices(), this.x() - 1.0F, this.y() - 1.0F, localValue5 + 1.0F, this.internalField0447.internalMethod04890() + 2.0F
                     );

                     try {
                        localValue1.drawFadeoutText(
                           this.internalField0447, localValue3, this.x(), this.y(), localValue4, Math.max(this.internalField0205, 0.84F), Math.max(this.internalField0206, 0.96F), localValue5
                        );
                     } finally {
                        ScissorStack.internalMethod07643();
                     }
                  }
               }
            }
         }
      }

      private String internalMethod01843() {
         String localValue1 = this.internalField0017.get();
         return localValue1 == null ? "" : localValue1;
      }

      float internalMethod06576() {
         return this.internalField0447.internalMethod04890();
      }
   }

   static final class InternalType0121 {
      final String[] internalField0359;
      final float[] internalField0615;
      final String[] internalField0358;
      final int[] internalField0618;
      final int[] internalField0617;
      final float[] internalField0616;
      final float[] internalField1238;
      final float[] internalField1240;
      final float[] internalField1239;
      final float internalField0205;
      final long internalField0229;
      float internalField0206;
      float internalField1048;
      float internalField1047;
      float internalField1049;
      long internalField0230;

      InternalType0121(SizedFont localValue1, String localValue2, long localValue3) {
         this.internalField0229 = localValue3;
         int localValue5 = localValue2.length();
         this.internalField0359 = new String[localValue5];
         this.internalField0615 = new float[localValue5];
         int localValue6 = 0;
         boolean localValue7 = false;

         for (int localValue8 = 0; localValue8 < localValue5; localValue8++) {
            boolean localValue9 = Character.isWhitespace(localValue2.charAt(localValue8));
            if (!localValue9 && !localValue7) {
               localValue6++;
            }

            localValue7 = !localValue9;
         }

         this.internalField0358 = new String[localValue6];
         this.internalField0618 = new int[localValue6];
         this.internalField0617 = new int[localValue6];
         this.internalField0616 = new float[localValue6];
         this.internalField1238 = new float[localValue6];
         this.internalField1240 = new float[localValue6];
         this.internalField1239 = new float[localValue6];
         Arrays.fill(this.internalField1240, 1.0F);
         int localValue13 = -1;
         localValue7 = false;

         for (int localValue14 = 0; localValue14 < localValue5; localValue14++) {
            char localValue10 = localValue2.charAt(localValue14);
            this.internalField0359[localValue14] = String.valueOf(localValue10);
            this.internalField0615[localValue14] = localValue1.internalMethod00965(localValue2.substring(0, localValue14));
            boolean localValue11 = Character.isWhitespace(localValue10);
            if (!localValue11) {
               if (!localValue7) {
                  this.internalField0618[++localValue13] = localValue14;
                  this.internalField0616[localValue13] = this.internalField0615[localValue14];
               }

               this.internalField0617[localValue13] = localValue14;
               this.internalField1238[localValue13] = this.internalField0615[localValue14] + localValue1.internalMethod00667(localValue10);
            }

            localValue7 = !localValue11;
         }

         for (int localValue15 = 0; localValue15 < localValue6; localValue15++) {
            this.internalField0358[localValue15] = localValue2.substring(this.internalField0618[localValue15], this.internalField0617[localValue15] + 1);
         }

         this.internalField0205 = localValue1.internalMethod00965(localValue2);
      }

      int internalMethod00161() {
         return this.internalField0358.length;
      }

      float internalMethod03013(int localValue1) {
         return (localValue1 + 1 < this.internalField0359.length ? this.internalField0615[localValue1 + 1] : this.internalField0205) - this.internalField0615[localValue1];
      }
   }

   final class InternalType0404 extends UiNode {
      private static final int internalField0227 = 6;
      private static final float internalField0205 = 8.0F;
      private final AnimatedValue internalField0808 = new AnimatedValue(360L, 0.0F, Easing.internalField1626);
      private final AnimatedValue internalField0809 = new AnimatedValue(280L, -1.0F, Easing.internalField1626);
      private LyricsTrack internalField0517;
      private int internalField0228 = -1;
      private String internalField0248 = "";
      private float internalField0206;
      private long internalField0229 = System.currentTimeMillis();

      InternalType0404() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         LyricsTrack localValue3 = MusicLyricsHud.this.internalMethod03658().internalMethod02718();
         MediaInfo localValue4 = MusicLyricsHud.this.internalMethod01181();
         float localValue5 = MusicLyricsHud.this.internalMethod08306();
         if (MusicLyricsHud.this.internalMethod04973() && !localValue3.internalMethod00819() && localValue4 != null && !(localValue5 <= 0.001F)) {
            List localValue6 = localValue3.internalMethod00129();
            int localValue7 = localValue6.size();
            double localValue8 = MusicLyricsHud.this.internalMethod03658().internalMethod07171() * 1000.0 + 150.0;
            int localValue10 = localValue3.internalMethod00823() ? localValue3.internalMethod06143((long)localValue8) : -1;
            int localValue11 = Math.max(0, localValue7 - 6);
            int localValue12 = localValue3.internalMethod00823() ? Math.clamp((long)(localValue10 - 2), 0, localValue11) : Math.clamp((long)MusicLyricsHud.this.internalField0227, 0, localValue11);
            MusicLyricsHud.this.internalField0227 = localValue12;
            this.internalMethod01232(localValue3, localValue12, localValue10);
            float localValue13 = this.internalField0808.internalMethod02881();
            float localValue14 = this.internalField0809.internalMethod02881();
            int localValue15 = Math.max(0, (int)Math.floor(localValue13) - 1);
            int localValue16 = Math.min(localValue7 - 1, (int)Math.ceil(localValue13) + 6 + 1);
            int localValue17 = localValue3.internalMethod00823() ? localValue10 : Math.clamp((long)Math.round(localValue13 + 2.0F), 0, localValue7 - 1);

            for (int localValue18 = localValue15; localValue18 <= localValue16; localValue18++) {
               float localValue19 = localValue18 - localValue13;
               float localValue20 = Math.min(MusicLyricsHud.internalMethod03123(localValue19 + 1.0F), MusicLyricsHud.internalMethod03123(6.0F - localValue19));
               if (!(localValue20 <= 0.001F)) {
                  float localValue21 = localValue3.internalMethod00823() ? MusicLyricsHud.internalMethod03123(1.0F - Math.abs(localValue18 - localValue14)) : 0.0F;
                  float localValue22 = localValue3.internalMethod00823() ? Math.max(0.0F, MusicLyricsHud.internalMethod03123(1.5F - Math.abs(localValue18 - localValue14)) - localValue21) : 0.0F;
                  float localValue23 = (localValue3.internalMethod00823() ? 50.0F + 205.0F * localValue21 + 100.0F * localValue22 : 200.0F) * localValue5 * localValue20;
                  SizedFont localValue24 = localValue21 > 0.1F
                     ? Fonts.internalField0449.internalMethod01432(MusicLyricsHud.internalMethod05364(5.0F, 6.0F, localValue21))
                     : Fonts.internalField1154.internalMethod01432(5.0F);
                  float localValue25 = this.y() + localValue19 * 8.0F;
                  float localValue26 = localValue18 == localValue10 ? localValue3.internalMethod05423(localValue18, localValue8, (long)(localValue4.getDuration() * 1000.0)) : -1.0F;
                  ColorRGBA localValue27 = ThemeColors.internalMethod08459().withAlpha(localValue26 >= 0.0F ? localValue23 * 0.38F : localValue23);
                  this.internalMethod00285(
                     localValue1,
                     localValue24,
                     ((LyricsTrack.InternalType0316)localValue6.get(localValue18)).internalMethod00756(),
                     localValue18,
                     localValue18 == localValue17,
                     this.x(),
                     localValue25,
                     Math.max(1.0F, this.w()),
                     localValue27,
                     ColorRGBA.WHITE.withAlpha(localValue23),
                     localValue26
                  );
               }
            }
         }
      }

      private void internalMethod01232(LyricsTrack localValue1, int localValue2, int localValue3) {
         if (this.internalField0517 != localValue1) {
            this.internalField0517 = localValue1;
            this.internalField0808.internalMethod07060(localValue2);
            this.internalField0809.internalMethod07060(localValue3);
            this.internalMethod01041();
         } else {
            this.internalField0808.internalMethod07059(localValue2);
            this.internalField0809.internalMethod07059(localValue3);
         }
      }

      private void internalMethod00285(
         UiRenderContext localValue1, SizedFont localValue2, String localValue3, int localValue4, boolean localValue5, float localValue6, float localValue7, float localValue8, ColorRGBA localValue9, ColorRGBA localValue10, float localValue11
      ) {
         float localValue12 = localValue2.internalMethod00965(localValue3);
         float localValue13 = this.internalMethod02252(localValue2, localValue3, localValue11);
         if (localValue5 && !(localValue12 <= localValue8)) {
            this.internalMethod03519(localValue4, localValue3, localValue12, localValue8, localValue13, localValue11, System.currentTimeMillis());
            float localValue14 = Math.max(0.0F, localValue12 - localValue8);
            this.internalField0206 = Math.min(this.internalField0206, localValue14);
            ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue6 - 2.5F, localValue7 - 3.0F, localValue8 + 5.0F, localValue2.internalMethod04892() + 6.0F);
            localValue1.pushMatrix();
            localValue1.getMatrices().translate(-this.internalField0206, 0.0F);
            localValue1.drawText(localValue2, localValue3, localValue6, localValue7, localValue9);
            if (localValue11 >= 0.0F) {
               this.internalMethod02506(localValue1, localValue2, localValue3, localValue6, localValue7, localValue13, localValue12, localValue10, localValue5x -> localValue1.drawText(localValue2, localValue3, localValue6, localValue7, localValue5x));
            }

            localValue1.popMatrix();
            ScissorStack.internalMethod07643();
         } else {
            localValue1.drawFadeoutText(localValue2, localValue3, localValue6, localValue7, localValue9, 0.88F, 1.0F, localValue8);
            if (localValue11 >= 0.0F) {
               this.internalMethod02506(
                  localValue1, localValue2, localValue3, localValue6, localValue7, localValue13, localValue8, localValue10, localValue6x -> localValue1.drawFadeoutText(localValue2, localValue3, localValue6, localValue7, localValue6x, 0.88F, 1.0F, localValue8)
               );
            }
         }
      }

      private void internalMethod02506(
         UiRenderContext localValue1, SizedFont localValue2, String localValue3, float localValue4, float localValue5, float localValue6, float localValue7, ColorRGBA localValue8, Consumer<ColorRGBA> localValue9
      ) {
         if (!(localValue6 <= 0.0F) && !localValue3.isEmpty()) {
            this.internalMethod04273(localValue1, localValue4, localValue5, Math.min(localValue7, localValue6 + 1.2F), localValue2.internalMethod04892(), localValue8.mulAlpha(0.18F), localValue9);
            this.internalMethod04273(localValue1, localValue4, localValue5, Math.min(localValue7, localValue6 + 0.55F), localValue2.internalMethod04892(), localValue8.mulAlpha(0.38F), localValue9);
            this.internalMethod04273(localValue1, localValue4, localValue5, Math.min(localValue7, localValue6), localValue2.internalMethod04892(), localValue8, localValue9);
         }
      }

      private void internalMethod04273(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6, Consumer<ColorRGBA> localValue7) {
         if (!(localValue4 <= 0.0F) && !(localValue6.getAlpha() <= 0.0F)) {
            ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue2 - 2.0F, localValue3 - 3.0F, localValue4 + 2.0F, localValue5 + 6.0F);
            localValue7.accept(localValue6);
            ScissorStack.internalMethod07643();
         }
      }

      private float internalMethod02252(SizedFont localValue1, String localValue2, float localValue3) {
         int localValue4 = localValue2.codePointCount(0, localValue2.length());
         if (localValue4 == 0) {
            return 0.0F;
         } else {
            float localValue5 = MusicLyricsHud.internalMethod03123(localValue3) * localValue4;
            int localValue6 = Math.min(localValue4, (int)localValue5);
            int localValue7 = localValue2.offsetByCodePoints(0, localValue6);
            float localValue8 = localValue1.internalMethod00965(localValue2.substring(0, localValue7));
            if (localValue6 == localValue4) {
               return localValue8;
            } else {
               int localValue9 = localValue2.offsetByCodePoints(localValue7, 1);
               float localValue10 = localValue1.internalMethod00965(localValue2.substring(0, localValue9));
               return localValue8 + (localValue10 - localValue8) * (localValue5 - localValue6);
            }
         }
      }

      private void internalMethod03519(int localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, long localValue7) {
         if (this.internalField0228 == localValue1 && this.internalField0248.equals(localValue2)) {
            float localValue9 = Math.max(0.0F, localValue3 - localValue4);
            float localValue10 = Math.min(0.05F, Math.max(0.0F, (float)(localValue7 - this.internalField0229) / 1000.0F));
            this.internalField0229 = localValue7;
            this.internalField0206 = Math.min(this.internalField0206, localValue9);
            if (!(localValue9 <= 0.0F)) {
               if (localValue6 < 0.0F) {
                  this.internalField0206 = Math.min(localValue9, this.internalField0206 + localValue10 * 24.0F);
               } else {
                  float localValue11 = Math.clamp(localValue5 - localValue4 * 0.62F, 0.0F, localValue9);
                  if (!(localValue11 <= this.internalField0206)) {
                     float localValue12 = 1.0F - (float)Math.exp(-14.0F * localValue10);
                     this.internalField0206 = Math.min(localValue11, this.internalField0206 + (localValue11 - this.internalField0206) * localValue12);
                  }
               }
            }
         } else {
            this.internalField0228 = localValue1;
            this.internalField0248 = localValue2;
            this.internalField0206 = 0.0F;
            this.internalField0229 = localValue7;
         }
      }

      private void internalMethod01041() {
         this.internalField0228 = -1;
         this.internalField0248 = "";
         this.internalField0206 = 0.0F;
         this.internalField0229 = System.currentTimeMillis();
      }
   }

   final class InternalType0405 extends UiElement {
      private final BooleanSupplier internalField0424;

      InternalType0405(BooleanSupplier localValue2) {
         this.internalField0424 = localValue2;
         this.snapPosition();
         this.snapSize();
         this.cursor(CursorType.internalField0567);
      }

      @Override
      public boolean contains(float localValue1, float localValue2) {
         return this.internalField0424.getAsBoolean() && super.contains(localValue1, localValue2);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         if (this.internalField0424.getAsBoolean()) {
            super.drawSelf(localValue1, localValue2);
         }
      }
   }

   final class InternalType0406 extends UiContainer {
      private final DynamicIslandHud internalField0209;
      private final MusicLyricsHud.InternalType0508 internalField0492 = MusicLyricsHud.this.new InternalType0508();
      private final MusicLyricsHud.InternalType0120 internalField0951 = MusicLyricsHud.this.new InternalType0120(
         Fonts.internalField0449.internalMethod01432(7.0F),
         () -> MusicLyricsHud.this.internalMethod04727(MediaInfo::getTitle),
         () -> ThemeColors.internalMethod08459().withAlpha(255.0F * (1.0F - MusicLyricsHud.this.internalMethod04967())),
         0.3F,
         0.7F
      );
      private final MusicLyricsHud.InternalType0119 internalField0949 = MusicLyricsHud.this.new InternalType0119();
      private final MusicLyricsHud.InternalType0120 internalField0950 = MusicLyricsHud.this.new InternalType0120(
         Fonts.internalField1154.internalMethod01432(7.0F),
         () -> MusicLyricsHud.this.internalMethod04727(MediaInfo::getArtist),
         () -> ThemeColors.internalMethod08459().withAlpha(178.5F * this.internalMethod09246()),
         0.3F,
         0.7F
      );
      private final MusicLyricsHud.InternalType0408 internalField0822 = MusicLyricsHud.this.new InternalType0408(
         Fonts.internalField1154.internalMethod01432(5.0F),
         () -> MusicLyricsHud.internalMethod06397(
            MusicLyricsHud.this.internalMethod01181() == null ? 0L : MusicLyricsHud.this.internalMethod01181().getPosition()
         ),
         () -> ThemeColors.internalMethod08459().withAlpha(255.0F)
      );
      private final MusicLyricsHud.InternalType0408 internalField0821 = MusicLyricsHud.this.new InternalType0408(
         Fonts.internalField1154.internalMethod01432(5.0F),
         () -> MusicLyricsHud.internalMethod06397(
            MusicLyricsHud.this.internalMethod01181() == null ? 0L : MusicLyricsHud.this.internalMethod01181().getDuration()
         ),
         () -> ThemeColors.internalMethod08459().withAlpha(255.0F)
      );
      private final MusicLyricsHud.InternalType0409 internalField0823 = MusicLyricsHud.this.new InternalType0409();
      private final MusicLyricsHud.InternalType0405 internalField0818 = MusicLyricsHud.this.internalMethod01872(
         this::internalMethod08134,
         () -> {
            IMediaSession localValue1x = MusicLyricsHud.this.internalMethod05622();
            if (localValue1x != null) {
               localValue1x.previous();
            }
         },
         (localValue0, localValue1x) -> localValue0.drawIcon(
            "music/previous", localValue1x.x(), localValue1x.y(), localValue1x.w(), ThemeColors.internalMethod08459().withAlpha(255.0F - 100.0F * localValue1x.hover())
         )
      );
      private final MusicLyricsHud.InternalType0405 internalField0819 = MusicLyricsHud.this.internalMethod01872(this::internalMethod08134, () -> {
         IMediaSession localValue1x = MusicLyricsHud.this.internalMethod05622();
         if (localValue1x != null) {
            MediaInfo localValue2x = localValue1x.getMedia();
            float localValue3 = localValue2x != null && localValue2x.isPlaying() ? 0.0F : 1.0F;
            localValue1x.playPause();
            MusicLyricsHud.this.internalField1723 = localValue3;
            MusicLyricsHud.this.internalField1059 = System.currentTimeMillis() + 1000L;
            MusicLyricsHud.this.internalField0808.internalMethod07061(600L);
            MusicLyricsHud.this.internalField0808.internalMethod07059(localValue3);
         }
      }, this::internalMethod06165);
      private final MusicLyricsHud.InternalType0405 internalField1334 = MusicLyricsHud.this.internalMethod01872(
         this::internalMethod08134,
         () -> {
            IMediaSession localValue1x = MusicLyricsHud.this.internalMethod05622();
            if (localValue1x != null) {
               localValue1x.next();
            }
         },
         (localValue0, localValue1x) -> localValue0.drawIcon(
            "music/next", localValue1x.x(), localValue1x.y(), localValue1x.w(), ThemeColors.internalMethod08459().withAlpha(255.0F - 100.0F * localValue1x.hover())
         )
      );
      private final MusicLyricsHud.InternalType0407 internalField0820 = MusicLyricsHud.this.new InternalType0407();
      private final MusicLyricsHud.InternalType0509 internalField0493 = MusicLyricsHud.this.new InternalType0509();
      private final MusicLyricsHud.InternalType0405 internalField1333 = MusicLyricsHud.this.internalMethod02074(
         8.0F,
         this::internalMethod08151,
         () -> {
            if (MusicLyricsHud.this.internalMethod04969()) {
               MusicLyricsHud.this.internalMethod03124(!MusicLyricsHud.this.internalMethod04973());
            }
         },
         (localValue0, localValue1x) -> localValue0.drawIcon(
            "music/text", localValue1x.x(), localValue1x.y(), localValue1x.w(), ThemeColors.internalMethod08459().withAlpha(255.0F - 100.0F * localValue1x.hover())
         )
      );
      private final MusicLyricsHud.InternalType0404 internalField0817 = MusicLyricsHud.this.new InternalType0404();
      private final MusicLyricsHud.InternalType0410 internalField0824 = MusicLyricsHud.this.new InternalType0410();

      InternalType0406(DynamicIslandHud localValue2) {
         this.internalField0209 = localValue2;
         this.internalMethod03995(48.0F, 15.0F);
         this.internalMethod07853(false);
         this.internalMethod01258("lyrics-expansion", MusicLyricsHud.this::internalMethod08305, Motion.internalMethod07185(300.0F, 30.0F));
         this.internalMethod09801();
         this.snapSize();
         this.internalMethod05497(this.internalField0492);
         this.internalMethod05497(this.internalField0951);
         this.internalMethod05497(this.internalField0949);
         this.internalMethod05497(this.internalField0950);
         this.internalMethod05497(this.internalField0822);
         this.internalMethod05497(this.internalField0821);
         this.internalMethod05497(this.internalField0823);
         this.internalMethod05497(this.internalField0818);
         this.internalMethod05497(this.internalField0819);
         this.internalMethod05497(this.internalField1334);
         this.internalMethod05497(this.internalField0820);
         this.internalMethod05497(this.internalField0493);
         this.internalMethod05497(this.internalField1333);
         this.internalMethod05497(this.internalField0817);
         this.internalMethod05497(this.internalField0824);
      }

      @Override
      protected void measure() {
         MediaInfo localValue1 = MusicLyricsHud.this.internalMethod01181();
         this.prefW = this.internalMethod01864(localValue1);
         this.prefH = this.internalMethod09678();
      }

      @Override
      protected void onTick(float localValue1, float localValue2, float localValue3) {
         this.internalMethod01401().removeIf(localValue0 -> localValue0.phase() == UiNode.InternalType0146.internalField1090);
         this.internalMethod08141();
         this.internalMethod08139();

         for (UiNode localValue5 : this.internalMethod01401()) {
            localValue5.tick(localValue1, localValue2, localValue3);
         }
      }

      @Override
      protected void drawChildren(UiRenderContext localValue1, float localValue2) {
         this.internalMethod08155();
         MusicLyricsHud.this.internalMethod04972();
         this.internalMethod08139();

         for (UiNode localValue4 : this.internalMethod01401()) {
            if (localValue4.inFlow()) {
               localValue4.draw(localValue1, localValue2);
            }
         }
      }

      private void internalMethod08139() {
         MediaInfo localValue1 = MusicLyricsHud.this.internalMethod01181();
         float localValue2 = this.internalMethod09246();
         boolean localValue3 = this.internalMethod04656();
         float localValue4 = localValue1 == null ? 48.0F : MusicLyricsHud.this.internalMethod06470(localValue1);
         if (!localValue3 && this.w() > 1.0F) {
            localValue4 = this.w();
         }

         float localValue5 = localValue3 ? ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0F - 82.0F : this.x();
         float localValue6 = localValue3 ? ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0F : localValue5 + localValue4 / 2.0F;
         float localValue7 = localValue3 ? localValue5 + 164.0F : localValue5 + localValue4;
         float localValue8 = MusicLyricsHud.this.internalMethod04971();
         float localValue9 = 4.0F + 6.0F * localValue2;
         float localValue10 = 7.0F + 19.0F * localValue2;
         float localValue11 = this.y() + (this.internalField0209.internalMethod08516() ? localValue9 : (this.internalField0209.internalMethod07527().internalField0206 - localValue10) / 2.0F);
         this.internalField0492.setSlot(localValue5 + localValue9 - 10.0F + 10.0F * MusicLyricsHud.this.animation.internalMethod02881(), localValue11, localValue10, localValue10);
         float localValue12 = localValue3 ? localValue7 - 22.0F : localValue5 + localValue4 - 12.0F;
         float localValue13 = localValue5 - 5.0F + 20.0F * MusicLyricsHud.this.animation.internalMethod02881() + 29.0F * localValue2;
         float localValue14 = Math.max(0.0F, localValue12 - 3.0F - localValue13);
         this.internalField0951.setSlot(localValue13, this.y() + 5.0F + 11.0F * localValue2, localValue14, this.internalField0951.internalMethod06576());
         this.internalField0949.setSlot(localValue13, this.y() + 5.0F, localValue14, this.internalField0949.internalMethod03277());
         this.internalField0950.setSlot(localValue5 + 20.0F + 24.0F * localValue2, this.y() + 5.0F + 19.0F * localValue2, localValue14, this.internalField0950.internalMethod06576());
         String localValue15 = localValue1 == null ? "0:00" : MusicLyricsHud.internalMethod06397(localValue1.getDuration());
         float localValue16 = Fonts.internalField1154.internalMethod01432(5.0F).internalMethod00965(localValue15);
         this.internalField0822
            .setSlot(
               localValue6 - 82.0F + 11.0F * localValue2,
               this.y() + 43.0F * localValue2,
               Fonts.internalField1154.internalMethod01432(5.0F).internalMethod00965(this.internalField0822.internalMethod02471()),
               this.internalField0822.internalMethod07628()
            );
         this.internalField0821.setSlot(localValue6 + 82.0F - (9.5F + localValue16 * localValue2), this.y() + 43.0F * localValue2, localValue16, this.internalField0821.internalMethod07628());
         float localValue17 = 115.0F
            + Fonts.internalField1154.internalMethod05670("0:00", 5.0F)
            - Fonts.internalField1154.internalMethod05670(localValue15, 5.0F);
         this.internalField0823
            .setSlot(localValue6 - localValue17 / 2.0F, this.y() + localValue8 - (MusicLyricsHud.this.internalMethod08305() ? 45.0F : 0.0F) - 36.5F * localValue2, localValue17, 3.0F);
         float localValue18 = this.y() + localValue8 - 25.0F * localValue2;
         this.internalField0818.setSlot(localValue6 - 40.0F, localValue18, 16.0F, 16.0F);
         this.internalField0819.setSlot(localValue6 - 8.0F, localValue18, 16.0F, 16.0F);
         this.internalField1334.setSlot(localValue6 + 24.0F, localValue18, 16.0F, 16.0F);
         this.internalField0820.setSlot(localValue7 - 22.0F, this.y() + localValue8 - 21.0F, 8.0F, 8.0F);
         String localValue19 = MusicLyricsHud.this.internalMethod09033();
         SizedFont localValue20 = Fonts.internalField0449.internalMethod01432(4.5F);
         float localValue21 = localValue20.internalMethod00965(localValue19);
         this.internalField0493.setSlot(localValue7 - 18.0F - localValue21, this.y() + 25.0F, localValue21, localValue20.internalMethod04890());
         this.internalField1333.setSlot(localValue5 + 14.0F, this.y() + localValue8 - 21.0F, 8.0F, 8.0F);
         this.internalField0817.setSlot(localValue5 + 10.0F, this.y() + 55.0F, 144.0F, 45.0F);
         this.internalField0824.setSlot(localValue12, this.y() + MathUtils.internalMethod02587(4.25, 14.0, localValue2), 14.0F, 10.0F);
      }

      private boolean internalMethod04656() {
         return this.internalField0209.internalMethod08516() || this.internalMethod09246() > 0.001F;
      }

      private void internalMethod08141() {
         float localValue1 = MusicLyricsHud.internalMethod03123((this.h() - 80.0F) / 45.0F);
         MusicLyricsHud.this.internalField1731 = MusicLyricsHud.internalMethod00335(
            Math.min(MusicLyricsHud.internalMethod03123(this.sig("lyrics-expansion")), localValue1), 0.1F, 0.7F
         );
      }

      private boolean internalMethod08132() {
         return this.internalMethod09246() != 0.0F;
      }

      private boolean internalMethod08134() {
         return this.internalMethod09246() > 0.7F && MusicLyricsHud.this.internalMethod05622() != null;
      }

      private boolean internalMethod08151() {
         return this.internalMethod08132() && MusicLyricsHud.this.internalMethod04969();
      }

      private void internalMethod05497(UiNode localValue1) {
         localValue1.lifeMotion(Motion.internalMethod01870(1L));
         this.internalMethod03907(localValue1);
      }

      private float internalMethod09246() {
         return this.internalField0209.internalMethod05767().internalMethod02881();
      }

      private float internalMethod01864(MediaInfo localValue1) {
         if (localValue1 == null) {
            return 48.0F;
         } else {
            return this.internalField0209.internalMethod08516() ? 164.0F : MusicLyricsHud.this.internalMethod06470(localValue1);
         }
      }

      private float internalMethod09678() {
         return this.internalField0209.internalMethod08516() ? MusicLyricsHud.this.internalMethod04971() : 15.0F;
      }

      private void internalMethod08155() {
         MediaInfo localValue1 = MusicLyricsHud.this.internalMethod01181();
         if (localValue1 != null && this.internalMethod08132()) {
            float localValue2 = localValue1.isPlaying() ? 1.0F : 0.0F;
            long localValue3 = System.currentTimeMillis();
            if (MusicLyricsHud.this.internalField1723 >= 0.0F) {
               if (localValue2 != MusicLyricsHud.this.internalField1723 && localValue3 < MusicLyricsHud.this.internalField1059) {
                  localValue2 = MusicLyricsHud.this.internalField1723;
               } else {
                  MusicLyricsHud.this.internalField1723 = -1.0F;
               }
            }

            MusicLyricsHud.this.internalField0808.internalMethod07061(600L);
            MusicLyricsHud.this.internalField0808.internalMethod07059(localValue2);
         }

         MediaSessionProvider localValue5 = MusicLyricsHud.this.internalMethod03658();
         float localValue6 = localValue5.internalMethod07172();
         boolean localValue4 = localValue6 > 0.0F;
         if (localValue4) {
            MusicLyricsHud.this.internalField1455 = localValue6;
         }

         MusicLyricsHud.this.internalField0809.internalMethod07059(localValue4 ? 1.0F : 0.0F);
      }

      private void internalMethod06165(UiRenderContext localValue1, MusicLyricsHud.InternalType0405 localValue2) {
         float localValue3 = MusicLyricsHud.this.internalField0808.internalMethod02881();
         float localValue4 = localValue2.x() + localValue2.w() / 2.0F;
         float localValue5 = localValue2.y() + localValue2.h() / 2.0F;
         float localValue6 = 255.0F * (1.0F - localValue3) - 100.0F * localValue2.hover();
         float localValue7 = 255.0F * localValue3 - 100.0F * localValue2.hover();
         if (localValue6 > 0.5F) {
            HudRenderUtils.internalMethod02865(localValue1.getMatrices(), localValue4, localValue5, 90.0F * localValue3);
            HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue4, localValue5, 1.0F - localValue3);
            localValue1.drawIcon("music/play", localValue2.x(), localValue2.y(), localValue2.w(), ThemeColors.internalMethod08459().withAlpha(localValue6));
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }

         if (localValue7 > 0.5F) {
            HudRenderUtils.internalMethod02865(localValue1.getMatrices(), localValue4, localValue5, -90.0F + 90.0F * localValue3);
            HudRenderUtils.internalMethod08976(localValue1.getMatrices(), localValue4, localValue5, localValue3);
            localValue1.drawIcon("music/pause", localValue2.x(), localValue2.y(), localValue2.w(), ThemeColors.internalMethod08459().withAlpha(localValue7));
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }
      }
   }

   final class InternalType0407 extends UiNode {
      InternalType0407() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         if (MusicLyricsHud.this.internalMethod08304() != 0.0F) {
            String localValue3 = MusicLyricsHud.this.internalMethod05549();
            if (localValue3 != null) {
               localValue1.drawTexture(RockstarClient.id("icons/media/" + localValue3 + ".png"), this.x(), this.y(), this.w(), this.h(), ColorRGBA.WHITE);
            }
         }
      }
   }

   final class InternalType0408 extends UiNode {
      private final SizedFont internalField0447;
      private final Supplier<String> internalField0017;
      private final Supplier<ColorRGBA> internalField0018;

      InternalType0408(SizedFont localValue2, Supplier<String> localValue3, Supplier<ColorRGBA> localValue4) {
         this.internalField0447 = localValue2;
         this.internalField0017 = localValue3;
         this.internalField0018 = localValue4;
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         if (MusicLyricsHud.this.internalMethod08304() != 0.0F) {
            String localValue3 = this.internalMethod02471();
            if (!localValue3.isEmpty()) {
               localValue1.drawText(this.internalField0447, localValue3, this.x(), this.y(), this.internalField0018.get());
            }
         }
      }

      String internalMethod02471() {
         String localValue1 = this.internalField0017.get();
         return localValue1 == null ? "" : localValue1;
      }

      float internalMethod07628() {
         return this.internalField0447.internalMethod04890();
      }
   }

   final class InternalType0409 extends UiNode {
      InternalType0409() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         MediaInfo localValue3 = MusicLyricsHud.this.internalMethod01181();
         if (localValue3 != null && MusicLyricsHud.this.internalMethod08304() != 0.0F) {
            ColorRGBA localValue4 = ThemeColors.internalMethod08459();
            localValue1.drawRoundedRect(this.x(), this.y(), this.w(), this.h(), CornerRadii.internalMethod03908(0.5F), localValue4.withAlpha(63.75F));
            float localValue5 = (float)localValue3.getDuration();
            float localValue6 = localValue5 <= 0.0F ? 0.0F : this.w() * Math.min(1.0F, (float)localValue3.getPosition() / localValue5);
            localValue1.drawRoundedRect(this.x(), this.y(), localValue6, this.h(), CornerRadii.internalMethod03908(0.5F), localValue4.withAlpha(150.0F));
         }
      }
   }

   final class InternalType0410 extends UiNode {
      InternalType0410() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         MediaSessionProvider localValue3 = MusicLyricsHud.this.internalMethod03658();
         float localValue4 = MusicLyricsHud.this.internalMethod08304();

         for (int localValue5 = 0; localValue5 < MusicLyricsHud.this.internalField1240.length; localValue5++) {
            localValue1.drawRoundedRect(
               this.x() + localValue5 * (2.0F + localValue4),
               this.y() + (7.0F - MusicLyricsHud.this.internalField1240[localValue5]) / 2.0F,
               1.0F + localValue4,
               MusicLyricsHud.this.internalField1240[localValue5],
               CornerRadii.internalMethod03908(0.5F),
               localValue3.internalMethod02369()
            );
         }
      }
   }

   final class InternalType0508 extends UiNode {
      InternalType0508() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         Identifier localValue3 = MusicLyricsHud.this.internalMethod00023();
         float localValue4 = MusicLyricsHud.this.internalMethod08304();
         localValue1.drawRoundedTexture(localValue3, this.x(), this.y(), this.w(), this.h(), CornerRadii.internalMethod03908(1.0F + 5.0F * localValue4));
      }
   }

   final class InternalType0509 extends UiNode {
      InternalType0509() {
         this.snapPosition();
         this.snapSize();
         this.interactive(false);
      }

      @Override
      protected void drawSelf(UiRenderContext localValue1, float localValue2) {
         if (MusicLyricsHud.this.internalMethod08304() != 0.0F
            && !(MusicLyricsHud.this.internalField0809.internalMethod02881() <= 0.02F)
            && !(MusicLyricsHud.this.internalField1455 <= 0.0F)) {
            String localValue3 = MusicLyricsHud.this.internalMethod09033();
            float localValue4 = MusicLyricsHud.this.internalMethod08314();
            float localValue5 = (140.0F + 90.0F * localValue4) * MusicLyricsHud.this.internalMethod08304() * MusicLyricsHud.this.internalField0809.internalMethod02881();
            SizedFont localValue6 = Fonts.internalField0449.internalMethod01432(4.5F);
            HudRenderUtils.internalMethod08976(localValue1.getMatrices(), this.x() + this.w() / 2.0F, this.y() + 2.5F, 1.0F + 0.1F * localValue4);
            localValue1.drawText(localValue6, localValue3, this.x(), this.y(), MusicLyricsHud.this.internalMethod03658().internalMethod02369().withAlpha(localValue5));
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }
      }
   }
}
