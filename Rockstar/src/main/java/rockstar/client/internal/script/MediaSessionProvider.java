package rockstar.client.internal.script;



import rockstar.client.internal.network.*;
import rockstar.client.*;
import dev.redstones.mediaplayerinfo.IMediaSession;
import dev.redstones.mediaplayerinfo.MediaInfo;
import dev.redstones.mediaplayerinfo.MediaPlayerInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pyrock.utility.render.ColorRGBA;

public class MediaSessionProvider implements MinecraftClientAccess {
   private static final Logger internalField0572 = LoggerFactory.getLogger("rockstar-lyrics");
   private final Thread internalField0380;
   private final AtomicReference<IMediaSession> internalField0746 = new AtomicReference<>();
   private volatile ColorRGBA internalField0777 = ColorRGBA.WHITE;
   private static final AtomicInteger internalField0678 = new AtomicInteger();
   private volatile Identifier internalField0354;
   private String internalField0248 = "";
   private int internalField0227;
   private int internalField0228;
   private static final long internalField0229 = 1500L;
   private volatile long internalField0230;
   private static final Executor internalField0252 = internalMethod01882("rockstar-track-lyrics");
   private static final Executor internalField0253 = internalMethod01882("rockstar-track-metadata");
   private final Map<String, LyricsTrack> internalField0543 = new ConcurrentHashMap<>();
   private final Map<String, Float> internalField0544 = new ConcurrentHashMap<>();
   private volatile long internalField1058;
   private volatile int internalField1056;
   private volatile boolean internalField0277;
   private final Object internalField0290 = new Object();
   private volatile LyricsTrack internalField0517 = LyricsTrack.internalMethod02903();
   private volatile String internalField0247 = "";
   private volatile float internalField0205;
   private volatile long internalField1060 = -1L;
   private volatile long internalField1057;
   private volatile boolean internalField0276;

   private static Executor internalMethod01882(String localValue0) {
      return Executors.newSingleThreadExecutor(localValue1 -> {
         Thread localValue2 = new Thread(localValue1, localValue0);
         localValue2.setDaemon(true);
         localValue2.setPriority(1);
         return localValue2;
      });
   }

   public MediaSessionProvider() {
      this.internalField0380 = new Thread(() -> {
         while (true) {
            try {
               long localValue1 = System.currentTimeMillis();
               this.internalMethod07173();
               long localValue3 = this.internalField0746.get() == null ? 1000L : 100L;
               Thread.sleep(Math.max(localValue3, System.currentTimeMillis() - localValue1));
            } catch (InterruptedException localValue5) {
               Thread.currentThread().interrupt();
            }
         }
      }, "rockstar-media-poll");
      this.internalField0380.setDaemon(true);
      this.internalField0380.setPriority(1);
      this.internalField0380.start();
   }

   private void internalMethod07173() {
      try {
         List<IMediaSession> localValue1 = MediaPlayerInfo.INSTANCE.getMediaSessions();
         IMediaSession localValue2 = localValue1.stream()
            .filter(localValue0 -> !localValue0.getMedia().getArtist().isEmpty() && !localValue0.getMedia().getTitle().isEmpty())
            .findFirst()
            .orElse(null);
         if (localValue2 == null && System.currentTimeMillis() - this.internalField0230 < 1500L) {
            return;
         }

         if (localValue2 != null) {
            this.internalField0230 = System.currentTimeMillis();
         }

         this.internalField0746.set(localValue2);
         if (localValue2 != null) {
            MediaInfo localValue3 = localValue2.getMedia();
            String localValue4 = localValue3.getArtist();
            String localValue5 = localValue3.getTitle();
            String localValue6 = localValue4 + " - " + localValue5;
            this.internalField0276 = localValue3.isPlaying();
            long localValue7 = localValue3.getPosition();
            if (localValue7 != this.internalField1060) {
               this.internalField1060 = localValue7;
               this.internalField1057 = System.currentTimeMillis();
            }

            boolean localValue9;
            synchronized (this.internalField0290) {
               localValue9 = !localValue6.equals(this.internalField0247);
               if (localValue9) {
                  this.internalField0247 = localValue6;
                  this.internalField0517 = LyricsTrack.internalMethod02903();
                  this.internalField0205 = 0.0F;
               }
            }

            if (!localValue9
               && this.internalField0517.internalMethod00819()
               && !this.internalField0277
               && this.internalField1056 < 3
               && System.currentTimeMillis() >= this.internalField1058) {
               this.internalMethod01723(localValue6, localValue4, localValue5, localValue3.getDuration());
            }

            if (localValue9) {
               this.internalField1056 = 0;
               this.internalField1058 = 0L;
               if (!this.internalField0277) {
                  this.internalMethod01723(localValue6, localValue4, localValue5, localValue3.getDuration());
               }

               internalField0253.execute(() -> {
                  if (localValue6.equals(this.internalField0247)) {
                     Float localValue5x = this.internalField0544.get(localValue6);
                     if (localValue5x == null) {
                        localValue5x = DeezerBpmService.internalMethod05651(localValue4, localValue5, localValue3.getDuration());
                        if (this.internalField0544.size() > 64) {
                           this.internalField0544.clear();
                        }

                        this.internalField0544.put(localValue6, localValue5x);
                     }

                     if (localValue5x > 0.0F) {
                        synchronized (this.internalField0290) {
                           if (localValue6.equals(this.internalField0247)) {
                              this.internalField0205 = localValue5x;
                           }
                        }
                     }
                  }
               });
            }

            this.internalMethod00230(localValue6, localValue3.getArtworkPng(), localValue2.getOwner() != null && localValue2.getOwner().toLowerCase(Locale.ROOT).contains("spotify"));
         } else {
            this.internalField0276 = false;
            this.internalMethod00230("", null, false);
         }
      } catch (Exception localValue13) {
      }
   }

   private void internalMethod01723(String localValue1, String localValue2, String localValue3, long localValue4) {
      int localValue6 = ++this.internalField1056;
      internalField0572.info(
         "[lyrics] \u0441\u0442\u0430\u0432\u043b\u044e \u0432 \u043e\u0447\u0435\u0440\u0435\u0434\u044c \u00ab{}\u00bb, \u043f\u043e\u043f\u044b\u0442\u043a\u0430 {}/{}",
         new Object[]{localValue1, localValue6, 3}
      );
      this.internalField1058 = System.currentTimeMillis() + 15000L;
      this.internalField0277 = true;
      internalField0252.execute(
         () -> {
            try {
               if (!localValue1.equals(this.internalField0247)) {
                  internalField0572.info(
                     "[lyrics] \u00ab{}\u00bb \u043e\u0442\u043c\u0435\u043d\u0451\u043d: \u0438\u0433\u0440\u0430\u0435\u0442 \u0443\u0436\u0435 \u00ab{}\u00bb",
                     localValue1,
                     this.internalField0247
                  );
                  return;
               }

               LyricsTrack localValue7 = this.internalField0543.get(localValue1);
               if (localValue7 != null) {
                  internalField0572.info(
                     "[lyrics] \u00ab{}\u00bb \u0443\u0436\u0435 \u0432 \u043a\u044d\u0448\u0435: {} \u0441\u0442\u0440\u043e\u043a",
                     localValue1,
                     localValue7.internalMethod00129().size()
                  );
               }

               LyricsTrack localValue8 = localValue7 != null ? localValue7 : LyricsAggregatorService.internalMethod02041(localValue2, localValue3, localValue4);
               if (localValue8 == null) {
                  localValue8 = LyricsTrack.internalMethod02903();
               }

               if (localValue8.internalMethod00819()) {
                  if (localValue6 >= 3) {
                     this.internalMethod01265(localValue1, localValue8);
                  }

                  return;
               }

               this.internalMethod01265(localValue1, localValue8);
               synchronized (this.internalField0290) {
                  if (localValue1.equals(this.internalField0247)) {
                     this.internalField0517 = localValue8;
                  }
               }
            } finally {
               this.internalField0277 = false;
            }
         }
      );
   }

   private void internalMethod01265(String localValue1, LyricsTrack localValue2) {
      if (this.internalField0543.size() > 64) {
         this.internalField0543.clear();
      }

      this.internalField0543.put(localValue1, localValue2);
   }

   public Identifier internalMethod00930() {
      return this.internalField0354;
   }

   private void internalMethod00230(String localValue1, byte[] localValue2, boolean localValue3) {
      int localValue4 = localValue2 != null && localValue2.length != 0 ? Arrays.hashCode(localValue2) : 0;
      if (localValue4 != 0 || !localValue1.equals(this.internalField0248)) {
         if (localValue4 == this.internalField0227) {
            this.internalField0248 = localValue1;
         } else if (localValue4 != this.internalField0228) {
            this.internalField0228 = localValue4;
         } else {
            this.internalField0248 = localValue1;
            this.internalField0227 = localValue4;
            if (localValue4 == 0) {
               this.internalMethod02787(null, ColorRGBA.WHITE);
            } else {
               try {
                  NativeImage localValue5 = NativeImage.read(localValue2);
                  if (localValue3) {
                     localValue5 = this.internalMethod00515(localValue5);
                  }

                  ColorRGBA localValue6 = this.internalMethod04331(localValue5, 16);
                  this.internalMethod02787(this.internalMethod06453(localValue5, 128), localValue6);
               } catch (RuntimeException | IOException localValue7) {
                  this.internalMethod02787(null, ColorRGBA.WHITE);
               }
            }
         }
      }
   }

   private NativeImage internalMethod06453(NativeImage localValue1, int localValue2) {
      int localValue3 = localValue1.getWidth();
      int localValue4 = localValue1.getHeight();
      if (Math.max(localValue3, localValue4) <= localValue2) {
         return localValue1;
      } else {
         float localValue5 = (float)localValue2 / Math.max(localValue3, localValue4);
         NativeImage localValue6 = new NativeImage(localValue1.getFormat(), Math.max(1, Math.round(localValue3 * localValue5)), Math.max(1, Math.round(localValue4 * localValue5)), false);
         localValue1.resizeSubRectTo(0, 0, localValue3, localValue4, localValue6);
         localValue1.close();
         return localValue6;
      }
   }

   private NativeImage internalMethod00515(NativeImage localValue1) {
      int localValue2 = localValue1.getWidth();
      int localValue3 = localValue1.getHeight();
      int localValue4 = (int)(localValue2 * 0.11);
      int localValue5 = localValue2 - localValue4 * 2;
      int localValue6 = localValue3 - (int)(localValue3 * 0.22);
      if (localValue5 > 0 && localValue6 > 0) {
         NativeImage localValue7 = new NativeImage(localValue1.getFormat(), localValue5, localValue6, false);

         for (int localValue8 = 0; localValue8 < localValue6; localValue8++) {
            for (int localValue9 = 0; localValue9 < localValue5; localValue9++) {
               localValue7.setColorArgb(localValue9, localValue8, localValue1.getColorArgb(localValue9 + localValue4, localValue8));
            }
         }

         localValue1.close();
         return localValue7;
      } else {
         return localValue1;
      }
   }

   private void internalMethod02787(NativeImage localValue1, ColorRGBA localValue2) {
      Identifier localValue3 = localValue1 == null ? null : RockstarClient.id("temp/artwork_" + internalField0678.incrementAndGet());
      internalField0149.execute(() -> {
         Identifier localValue4 = this.internalField0354;
         if (localValue1 != null) {
            internalField0149.getTextureManager().registerTexture(localValue3, new NativeImageBackedTexture(() -> "Rockstar script texture", localValue1));
         }

         this.internalField0354 = localValue3;
         this.internalField0777 = localValue2;
         if (localValue4 != null) {
            internalField0149.getTextureManager().destroyTexture(localValue4);
         }
      });
   }

   public ColorRGBA internalMethod04331(NativeImage localValue1, int localValue2) {
      int localValue3 = localValue1.getWidth();
      int localValue4 = localValue1.getHeight();
      long localValue5 = 0L;
      long localValue7 = 0L;
      long localValue9 = 0L;
      long localValue11 = 0L;
      int localValue13 = 0;
      int localValue14 = 0;

      while (localValue14 < localValue4) {
         for (int localValue15 = 0; localValue15 < localValue3; localValue15 += localValue2) {
            int localValue16 = localValue1.getColorArgb(localValue15, localValue14);
            int localValue17 = localValue16 >> 24 & 0xFF;
            if (localValue17 != 0) {
               localValue5 += localValue17;
               localValue7 += localValue16 >> 16 & 0xFF;
               localValue9 += localValue16 >> 8 & 0xFF;
               localValue11 += localValue16 & 0xFF;
               localValue13++;
            }
         }

         localValue14 += localValue2;
      }

      if (localValue13 == 0) {
         return ColorRGBA.WHITE;
      } else {
         float localValue18 = 50.0F;
         return new ColorRGBA((float)localValue7 / localValue13 + localValue18, (float)localValue9 / localValue13 + localValue18, (float)localValue11 / localValue13 + localValue18);
      }
   }

   public double internalMethod07171() {
      if (this.internalField1060 < 0L) {
         return 0.0;
      } else if (!this.internalField0276) {
         return this.internalField1060;
      } else {
         long localValue1 = System.currentTimeMillis() - this.internalField1057;
         return this.internalField1060 + Math.min(localValue1, 1000L) / 1000.0;
      }
   }

   public MediaSessionProvider.InternalType0314 internalMethod03535() {
      IMediaSession localValue1 = this.internalField0746.get();
      if (localValue1 == null) {
         return null;
      } else {
         try {
            MediaInfo localValue2 = localValue1.getMedia();
            if (localValue2 == null) {
               return null;
            } else {
               String localValue3 = Objects.requireNonNullElse(localValue2.getTitle(), "");
               String localValue4 = Objects.requireNonNullElse(localValue2.getArtist(), "");
               String localValue5 = localValue4 + " - " + localValue3;
               String localValue6 = Objects.requireNonNullElse(localValue1.getOwner(), "");
               long localValue7 = Math.max(0L, localValue2.getDuration());
               synchronized (this.internalField0290) {
                  boolean localValue10 = localValue5.equals(this.internalField0247);
                  double localValue11 = localValue10 ? this.internalMethod07171() : localValue2.getPosition();
                  localValue11 = Math.max(0.0, localValue7 > 0L ? Math.min(localValue11, (double)localValue7) : localValue11);
                  return new MediaSessionProvider.InternalType0314(
                     localValue3,
                     localValue4,
                     localValue6,
                     localValue2.isPlaying(),
                     localValue11,
                     localValue7,
                     localValue10 ? this.internalField0205 : 0.0F,
                     this.internalField0777,
                     this.internalField0354,
                     localValue10 ? this.internalField0517 : LyricsTrack.internalMethod02903()
                  );
               }
            }
         } catch (Exception localValue15) {
            return null;
         }
      }
   }

   public boolean internalMethod07174() {
      return this.internalField0746.get() != null;
   }

   public IMediaSession internalMethod04514() {
      return this.internalField0746.get();
   }

   @Override
   public boolean equals(Object localValue1) {
      if (localValue1 != null && this.getClass() == localValue1.getClass()) {
         MediaSessionProvider localValue2 = (MediaSessionProvider)localValue1;
         return Objects.equals(this.internalField0380, localValue2.internalField0380)
            && Objects.equals(this.internalMethod04514(), localValue2.internalMethod04514())
            && Objects.equals(this.internalField0777, localValue2.internalField0777)
            && Objects.equals(this.internalField0517, localValue2.internalField0517)
            && Objects.equals(this.internalField0247, localValue2.internalField0247);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.internalField0380, this.internalMethod04514(), this.internalField0777, this.internalField0517, this.internalField0247);
   }

   @Generated
   public Thread internalMethod00003() {
      return this.internalField0380;
   }

   @Generated
   public ColorRGBA internalMethod02369() {
      return this.internalField0777;
   }

   @Generated
   public Object internalMethod07549() {
      return this.internalField0290;
   }

   @Generated
   public LyricsTrack internalMethod02718() {
      return this.internalField0517;
   }

   @Generated
   public String internalMethod07127() {
      return this.internalField0247;
   }

   @Generated
   public float internalMethod07172() {
      return this.internalField0205;
   }

   public static final class InternalType0314 {
      private final String internalField0248;
      private final String internalField0247;
      private final String internalField1077;
      private final boolean internalField0277;
      private final double internalField0194;
      private final long internalField0229;
      private final float internalField0205;
      private final ColorRGBA internalField0777;
      private final Identifier internalField0354;
      private final LyricsTrack internalField0517;

      public InternalType0314(
         String localValue1, String localValue2, String localValue3, boolean localValue4, double localValue5, long localValue7, float localValue9, ColorRGBA localValue10, Identifier localValue11, LyricsTrack localValue12
      ) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField1077 = localValue3;
         this.internalField0277 = localValue4;
         this.internalField0194 = localValue5;
         this.internalField0229 = localValue7;
         this.internalField0205 = localValue9;
         this.internalField0777 = localValue10;
         this.internalField0354 = localValue11;
         this.internalField0517 = localValue12;
      }

      @Override
      public final String toString() {
         return "InternalType0314[title=" + this.internalField0248 + ", artist=" + this.internalField0247 + ", owner=" + this.internalField1077 + ", playing=" + this.internalField0277 + ", positionSeconds=" + this.internalField0194 + ", durationSeconds=" + this.internalField0229 + ", bpm=" + this.internalField0205 + ", color=" + this.internalField0777 + ", artwork=" + this.internalField0354 + ", lyrics=" + this.internalField0517 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1077);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0777);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0354);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0517);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MediaSessionProvider.InternalType0314 other = (MediaSessionProvider.InternalType0314) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField1077, other.internalField1077)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0777, other.internalField0777)
            && java.util.Objects.equals(this.internalField0354, other.internalField0354)
            && java.util.Objects.equals(this.internalField0517, other.internalField0517);
      }

      public String internalMethod01219() {
         return this.internalField0248;
      }

      public String internalMethod05846() {
         return this.internalField0247;
      }

      public String internalMethod08078() {
         return this.internalField1077;
      }

      public boolean internalMethod00392() {
         return this.internalField0277;
      }

      public double internalMethod00389() {
         return this.internalField0194;
      }

      public long internalMethod00391() {
         return this.internalField0229;
      }

      public float internalMethod00390() {
         return this.internalField0205;
      }

      public ColorRGBA internalMethod02127() {
         return this.internalField0777;
      }

      public Identifier internalMethod03282() {
         return this.internalField0354;
      }

      public LyricsTrack internalMethod01450() {
         return this.internalField0517;
      }
   }
}
