package rockstar.client.internal.core;



import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationAtlas {
   private static final Logger internalField0572 = LoggerFactory.getLogger(AnimationAtlas.class);
   private static final List<AnimationAtlas> internalField0416 = new ArrayList<>();
   private final Map<Identifier, AnimationAtlas.InternalType0109> internalField0543 = new HashMap<>();
   private final List<AnimationAtlas.InternalType0108> internalField0417 = new ArrayList<>();
   private Identifier internalField0354;
   private boolean internalField0277 = false;
   private final int internalField0227;
   private final int internalField0228;

   public static AnimationAtlas internalMethod02378(int localValue0, int localValue1) {
      for (AnimationAtlas localValue3 : internalField0416) {
         if (localValue3.internalField0227 == localValue0 && localValue3.internalField0228 == localValue1 && !localValue3.internalMethod00147()) {
            return localValue3;
         }
      }

      AnimationAtlas localValue4 = new AnimationAtlas(localValue0, localValue1);
      internalField0416.add(localValue4);
      return localValue4;
   }

   private AnimationAtlas(int localValue1, int localValue2) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
   }

   public void internalMethod05732(Identifier localValue1, GifAnimationData localValue2, List<NativeImage> localValue3) {
      if (this.internalField0277) {
         throw new RuntimeException(
            "\u0410\u0442\u043b\u0430\u0441 \u0443\u0436\u0435 \u0441\u043e\u0431\u0440\u0430\u043d! \u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 \u0434\u043e \u0432\u044b\u0437\u043e\u0432\u0430 buildAtlas()"
         );
      } else if (localValue3.isEmpty()) {
         internalField0572.warn("\u041f\u0443\u0441\u0442\u0430\u044f \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044f: {}", localValue1);
      } else {
         for (NativeImage localValue5 : localValue3) {
            if (localValue5.getWidth() != this.internalField0227 || localValue5.getHeight() != this.internalField0228) {
               throw new RuntimeException(
                  String.format(
                     "\u0420\u0430\u0437\u043c\u0435\u0440 \u043a\u0430\u0434\u0440\u043e\u0432 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 %s (%dx%d) \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u0435\u0442 \u0441 \u0440\u0430\u0437\u043c\u0435\u0440\u043e\u043c \u044d\u0442\u043e\u0433\u043e \u0430\u0442\u043b\u0430\u0441\u0430 (%dx%d)",
                     localValue1,
                     localValue5.getWidth(),
                     localValue5.getHeight(),
                     this.internalField0227,
                     this.internalField0228
                  )
               );
            }
         }

         int localValue6 = this.internalField0417.size();

         for (int localValue7 = 0; localValue7 < localValue3.size(); localValue7++) {
            this.internalField0417.add(new AnimationAtlas.InternalType0108(localValue1, localValue7, (NativeImage)localValue3.get(localValue7)));
         }

         AnimationAtlas.InternalType0109 localValue8 = new AnimationAtlas.InternalType0109(localValue1, localValue2, localValue6, localValue3.size(), null);
         this.internalField0543.put(localValue1, localValue8);
         internalField0572.info(
            "\u0417\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0430 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044f {} \u0441 {} \u043a\u0430\u0434\u0440\u0430\u043c\u0438 \u0432 \u0430\u0442\u043b\u0430\u0441\u0435 {}x{}",
            new Object[]{localValue1, localValue3.size(), this.internalField0227, this.internalField0228}
         );
      }
   }

   public void internalMethod03950(Identifier localValue1) {
      try {
         ResourceManager localValue2 = MinecraftClient.getInstance().getResourceManager();
         Optional localValue3 = localValue2.getResource(localValue1);
         if (localValue3.isEmpty()) {
            throw new RuntimeException("\u0424\u0430\u0439\u043b \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue1);
         } else {
            Resource localValue4 = (Resource)localValue3.get();
            GifAnimationData localValue5 = null;
            ArrayList localValue6 = new ArrayList();

            try (
               InputStream localValue7 = localValue4.getInputStream();
               ZipInputStream localValue8 = new ZipInputStream(localValue7);
            ) {
               TreeMap localValue9;
               ZipEntry localValue10;
               for (localValue9 = new TreeMap(); (localValue10 = localValue8.getNextEntry()) != null; localValue8.closeEntry()) {
                  String localValue11 = localValue10.getName();
                  if ("meta.json".equals(localValue11)) {
                     ByteArrayOutputStream localValue22 = new ByteArrayOutputStream();
                     byte[] localValue24 = new byte[1024];

                     int localValue26;
                     while ((localValue26 = localValue8.read(localValue24)) > 0) {
                        localValue22.write(localValue24, 0, localValue26);
                     }

                     String localValue15 = localValue22.toString(StandardCharsets.UTF_8);
                     localValue5 = GifAnimationData.internalMethod00892(localValue15);
                  } else if (localValue11.startsWith("frames/") && localValue11.endsWith(".png")) {
                     ByteArrayOutputStream localValue12 = new ByteArrayOutputStream();
                     byte[] localValue13 = new byte[1024];

                     int localValue14;
                     while ((localValue14 = localValue8.read(localValue13)) > 0) {
                        localValue12.write(localValue13, 0, localValue14);
                     }

                     localValue9.put(localValue11, localValue12.toByteArray());
                  }
               }

               for (byte[] localValue23 : (Iterable<byte[]>)(Iterable<?>)localValue9.values()) {
                  NativeImage localValue25 = NativeImage.read(new ByteArrayInputStream(localValue23));
                  localValue6.add(localValue25);
               }
            }

            if (localValue5 == null) {
               throw new RuntimeException("\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d meta.json \u0432 " + localValue1);
            } else if (localValue6.isEmpty()) {
               throw new RuntimeException(
                  "\u041d\u0435\u0442 \u043a\u0430\u0434\u0440\u043e\u0432 \u0434\u043b\u044f \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 " + localValue1
               );
            } else if (((NativeImage)localValue6.get(0)).getWidth() == this.internalField0227 && ((NativeImage)localValue6.get(0)).getHeight() == this.internalField0228) {
               this.internalMethod05732(localValue1, localValue5, localValue6);
            } else {
               throw new RuntimeException(
                  String.format(
                     "\u0420\u0430\u0437\u043c\u0435\u0440 \u043a\u0430\u0434\u0440\u043e\u0432 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 %s (%dx%d) \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u0435\u0442 \u0441 \u0440\u0430\u0437\u043c\u0435\u0440\u043e\u043c \u044d\u0442\u043e\u0433\u043e \u0430\u0442\u043b\u0430\u0441\u0430 (%dx%d)",
                     localValue1,
                     ((NativeImage)localValue6.get(0)).getWidth(),
                     ((NativeImage)localValue6.get(0)).getHeight(),
                     this.internalField0227,
                     this.internalField0228
                  )
               );
            }
         }
      } catch (Exception localValue20) {
         throw new RuntimeException(
            "\u041e\u0448\u0438\u0431\u043a\u0430 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0438 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 \u0438\u0437 "
               + localValue1,
            localValue20
         );
      }
   }

   public void internalMethod00146() {
      if (this.internalField0277) {
         internalField0572.warn("\u0410\u0442\u043b\u0430\u0441 \u0443\u0436\u0435 \u0441\u043e\u0431\u0440\u0430\u043d!");
      } else if (this.internalField0417.isEmpty()) {
         internalField0572.warn(
            "\u041d\u0435\u0442 \u043a\u0430\u0434\u0440\u043e\u0432 \u0434\u043b\u044f \u0441\u043e\u0437\u0434\u0430\u043d\u0438\u044f \u0430\u0442\u043b\u0430\u0441\u0430!"
         );
      } else {
         int localValue1 = this.internalField0417.size();
         int localValue2 = (int)Math.ceil(Math.sqrt(localValue1));
         int localValue3 = (int)Math.ceil((double)localValue1 / localValue2);
         int localValue4 = localValue2 * this.internalField0227;
         int localValue5 = localValue3 * this.internalField0228;
         NativeImage localValue6 = new NativeImage(localValue4, localValue5, false);

         for (int localValue7 = 0; localValue7 < localValue4; localValue7++) {
            for (int localValue8 = 0; localValue8 < localValue5; localValue8++) {
               localValue6.setColor(localValue7, localValue8, 0);
            }
         }

         for (int localValue20 = 0; localValue20 < localValue1; localValue20++) {
            int localValue22 = localValue20 % localValue2;
            int localValue9 = localValue20 / localValue2;
            int localValue10 = localValue22 * this.internalField0227;
            int localValue11 = localValue9 * this.internalField0228;
            NativeImage localValue12 = this.internalField0417.get(localValue20).internalField0771;

            for (int localValue13 = 0; localValue13 < this.internalField0227; localValue13++) {
               for (int localValue14 = 0; localValue14 < this.internalField0228; localValue14++) {
                  localValue6.setColor(localValue10 + localValue13, localValue11 + localValue14, localValue12.getColor(localValue13, localValue14));
               }
            }
         }

         this.internalField0354 = Identifier.of("rockstar", "global_animation_atlas_" + this.internalField0227 + "x" + this.internalField0228);
         NativeImageBackedTexture localValue21 = new NativeImageBackedTexture(() -> "Rockstar core image", localValue6);
         MinecraftClient.getInstance().getTextureManager().registerTexture(this.internalField0354, localValue21);

         for (AnimationAtlas.InternalType0109 localValue24 : this.internalField0543.values()) {
            localValue24.internalField0355 = this.internalField0354;
            ArrayList localValue25 = new ArrayList();

            for (int localValue26 = 0; localValue26 < localValue24.internalField0228; localValue26++) {
               int localValue27 = localValue24.internalField0227 + localValue26;
               int localValue28 = localValue27 % localValue2;
               int localValue29 = localValue27 / localValue2;
               float localValue15 = (float)localValue28 / localValue2;
               float localValue16 = (float)localValue29 / localValue3;
               float localValue17 = (float)(localValue28 + 1) / localValue2;
               float localValue18 = (float)(localValue29 + 1) / localValue3;
               TextureRegion localValue19 = new TextureRegion(this.internalField0354, localValue15, localValue16, localValue17, localValue18, this.internalField0227, this.internalField0228);
               localValue25.add(localValue19);
            }

            localValue24.internalField0416 = localValue25;
         }

         this.internalField0277 = true;
         internalField0572.info(
            "\u0410\u0442\u043b\u0430\u0441 {}x{} \u0441\u043e\u0431\u0440\u0430\u043d \u0441 {} \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u044f\u043c\u0438 \u0438 {} \u043a\u0430\u0434\u0440\u0430\u043c\u0438",
            new Object[]{this.internalField0227, this.internalField0228, this.internalField0543.size(), localValue1}
         );
      }
   }

   public static AnimationAtlas.InternalType0109 internalMethod07453(Identifier localValue0) {
      for (AnimationAtlas localValue2 : internalField0416) {
         AnimationAtlas.InternalType0109 localValue3 = localValue2.internalField0543.get(localValue0);
         if (localValue3 != null) {
            return localValue3;
         }
      }

      return null;
   }

   public Identifier internalMethod00249() {
      return this.internalField0354;
   }

   public boolean internalMethod00147() {
      return this.internalField0277;
   }

   public void internalMethod00150() {
      if (this.internalField0354 != null) {
         MinecraftClient.getInstance().getTextureManager().destroyTexture(this.internalField0354);
      }

      for (AnimationAtlas.InternalType0108 localValue2 : this.internalField0417) {
         try {
            localValue2.internalField0771.close();
         } catch (Exception localValue4) {
         }
      }

      this.internalField0543.clear();
      this.internalField0417.clear();
      this.internalField0277 = false;
   }

   public static void internalMethod08207() {
      for (AnimationAtlas localValue1 : internalField0416) {
         localValue1.internalMethod00150();
      }

      internalField0416.clear();
   }

   static class InternalType0108 {
      public final Identifier internalField0354;
      public final int internalField0227;
      public final NativeImage internalField0771;

      public InternalType0108(Identifier localValue1, int localValue2, NativeImage localValue3) {
         this.internalField0354 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0771 = localValue3;
      }
   }

   public static class InternalType0109 {
      public final Identifier internalField0354;
      public final GifAnimationData internalField0943;
      public final int internalField0227;
      public final int internalField0228;
      public Identifier internalField0355;
      public List<TextureRegion> internalField0416;

      public InternalType0109(Identifier localValue1, GifAnimationData localValue2, int localValue3, int localValue4, Identifier localValue5) {
         this.internalField0354 = localValue1;
         this.internalField0943 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0228 = localValue4;
         this.internalField0355 = localValue5;
      }

      public TextureRegion internalMethod00899(int localValue1) {
         return this.internalField0416 != null && localValue1 >= 0 && localValue1 < this.internalField0416.size() ? this.internalField0416.get(localValue1) : null;
      }
   }
}
