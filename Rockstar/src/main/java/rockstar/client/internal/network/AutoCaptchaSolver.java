package rockstar.client.internal.network;





import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.minecraft.block.MapColor;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoCaptchaSolver implements MinecraftClientAccess {
   private static final double internalField0194 = 16.0;
   private static final HttpClient internalField0791 = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();
   private final Stopwatch internalField0519 = new Stopwatch();
   private String internalField0248;
   private String internalField0247;
   private boolean internalField0277;
   private boolean internalField0276;
   private int internalField1056;

   public void internalMethod05324(String localValue1) {
      String localValue2 = localValue1.toLowerCase();
      if (localValue2.contains("\u043a\u0430\u0440\u0442\u0438\u043d\u043a") || localValue2.contains("\u043a\u0430\u043f\u0447") || localValue2.contains("captcha")) {
         this.internalField0276 = true;
         this.internalField1056 = 0;
         this.internalField0519.internalMethod00701();
      }
   }

   public void internalMethod03753() {
      if (internalField0149.player != null && internalField0149.world != null && this.internalField0276 && !this.internalField0277) {
         if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
            if (this.internalField1056 < 3 && this.internalField0519.internalMethod02365(this.internalField1056 == 0 ? 500L : 3000L)) {
               List localValue1 = this.internalMethod00785(this.internalMethod05189());
               if (!localValue1.isEmpty()) {
                  String localValue2 = this.internalMethod07246(localValue1);
                  if (localValue2 != null && !localValue2.equals(this.internalField0247)) {
                     BufferedImage localValue3 = this.internalMethod01339(localValue1);
                     if (localValue3 != null) {
                        this.internalField1056++;
                        this.internalField0277 = true;
                        this.internalField0248 = localValue2;
                        this.internalField0519.internalMethod00701();
                        this.internalMethod02950(localValue3, localValue2);
                     }
                  }
               }
            }
         }
      }
   }

   private List<ItemFrameEntity> internalMethod05189() {
      ArrayList localValue1 = new ArrayList();

      for (Entity localValue3 : internalField0149.world.getEntities()) {
         if (localValue3 instanceof ItemFrameEntity localValue4 && localValue4.containsMap() && !(localValue4.squaredDistanceTo(internalField0149.player) > 256.0)) {
            localValue1.add(localValue4);
         }
      }

      return localValue1;
   }

   private List<ItemFrameEntity> internalMethod00785(List<ItemFrameEntity> localValue1) {
      HashMap<String, Map<Long, ItemFrameEntity>> localValue2 = new HashMap<>();

      for (ItemFrameEntity localValue4 : localValue1) {
         Direction localValue5 = localValue4.getHorizontalFacing();
         if (!localValue5.getAxis().isVertical()) {
            BlockPos localValue6 = localValue4.getBlockPos();
            Vec3i localValue7 = localValue5.getVector();
            String localValue8 = localValue5.asString() + ":" + (localValue6.getX() * localValue7.getX() + localValue6.getZ() * localValue7.getZ());
            localValue2.computeIfAbsent(localValue8, localValue0 -> new HashMap<>()).put(this.internalMethod06914(this.internalMethod01169(localValue6, localValue5), localValue6.getY()), localValue4);
         }
      }

      Vec3d localValue20 = internalField0149.player.getEyePos();
      Vec3d localValue21 = internalField0149.player.getRotationVec(1.0F);
      double localValue22 = Math.cos(Math.toRadians(50.0));
      List localValue23 = List.of();
      double localValue24 = localValue22;
      boolean localValue10 = false;

      for (Map localValue12 : (Iterable<Map>)(Iterable<?>)localValue2.values()) {
         boolean localValue13 = localValue12.size() == 12;

         for (ItemFrameEntity localValue15 : (Iterable<ItemFrameEntity>)(Iterable<?>)localValue12.values()) {
            List localValue16 = this.internalMethod00753(localValue12, localValue15);
            if (localValue16 != null) {
               Vec3d localValue17 = Vec3d.ZERO;

               for (ItemFrameEntity localValue19 : (Iterable<ItemFrameEntity>)(Iterable<?>)localValue16) {
                  localValue17 = localValue17.add(localValue19.getEntityPos());
               }

               double localValue25 = localValue21.dotProduct(localValue17.multiply(1.0 / localValue16.size()).subtract(localValue20).normalize());
               if (!(localValue25 < localValue22) && (localValue13 && !localValue10 || localValue13 == localValue10 && localValue25 > localValue24)) {
                  localValue24 = localValue25;
                  localValue10 = localValue13;
                  localValue23 = localValue16;
               }
            }
         }
      }

      return localValue23;
   }

   private List<ItemFrameEntity> internalMethod00753(Map<Long, ItemFrameEntity> localValue1, ItemFrameEntity localValue2) {
      Direction localValue3 = localValue2.getHorizontalFacing();
      int localValue4 = this.internalMethod01169(localValue2.getBlockPos(), localValue3);
      int localValue5 = localValue2.getBlockPos().getY();
      ArrayList localValue6 = new ArrayList(12);

      for (int localValue7 = 0; localValue7 < 3; localValue7++) {
         for (int localValue8 = 0; localValue8 < 4; localValue8++) {
            ItemFrameEntity localValue9 = (ItemFrameEntity)localValue1.get(this.internalMethod06914(localValue4 + localValue8, localValue5 - localValue7));
            if (localValue9 == null) {
               return null;
            }

            localValue6.add(localValue9);
         }
      }

      return localValue6;
   }

   private long internalMethod06914(int localValue1, int localValue2) {
      return (long)localValue1 << 32 | localValue2 & 4294967295L;
   }

   private int internalMethod01169(BlockPos localValue1, Direction localValue2) {
      Vec3i localValue3 = localValue2.rotateYCounterclockwise().getVector();
      return localValue1.getX() * localValue3.getX() + localValue1.getZ() * localValue3.getZ();
   }

   private BufferedImage internalMethod01339(List<ItemFrameEntity> localValue1) {
      Direction localValue2 = ((ItemFrameEntity)localValue1.getFirst()).getHorizontalFacing();
      int localValue3 = Integer.MAX_VALUE;
      int localValue4 = Integer.MIN_VALUE;
      int localValue5 = Integer.MAX_VALUE;
      int localValue6 = Integer.MIN_VALUE;

      for (ItemFrameEntity localValue8 : localValue1) {
         int localValue9 = this.internalMethod01169(localValue8.getBlockPos(), localValue2);
         int localValue10 = localValue8.getBlockPos().getY();
         localValue3 = Math.min(localValue3, localValue9);
         localValue4 = Math.max(localValue4, localValue9);
         localValue5 = Math.min(localValue5, localValue10);
         localValue6 = Math.max(localValue6, localValue10);
      }

      BufferedImage localValue15 = new BufferedImage((localValue4 - localValue3 + 1) * 128, (localValue6 - localValue5 + 1) * 128, 1);
      Graphics2D localValue16 = localValue15.createGraphics();
      localValue16.setColor(Color.WHITE);
      localValue16.fillRect(0, 0, localValue15.getWidth(), localValue15.getHeight());

      for (ItemFrameEntity localValue18 : localValue1) {
         MapIdComponent localValue11 = localValue18.getMapId(localValue18.getHeldItemStack());
         MapState localValue12 = localValue11 == null ? null : internalField0149.world.getMapState(localValue11);
         if (localValue12 == null) {
            localValue16.dispose();
            return null;
         }

         int localValue13 = (this.internalMethod01169(localValue18.getBlockPos(), localValue2) - localValue3) * 128;
         int localValue14 = (localValue6 - localValue18.getBlockPos().getY()) * 128;
         localValue16.drawImage(this.internalMethod04787(localValue12.colors, localValue18.getRotation() & 3), localValue13, localValue14, null);
      }

      localValue16.dispose();
      return localValue15;
   }

   private String internalMethod07246(List<ItemFrameEntity> localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (ItemFrameEntity localValue4 : localValue1) {
         MapIdComponent localValue5 = localValue4.getMapId(localValue4.getHeldItemStack());
         if (localValue5 == null || internalField0149.world.getMapState(localValue5) == null) {
            return null;
         }

         localValue2.add(localValue5.id());
      }

      Collections.sort(localValue2);
      return localValue2.toString();
   }

   private BufferedImage internalMethod04787(byte[] localValue1, int localValue2) {
      BufferedImage localValue3 = new BufferedImage(128, 128, 1);

      for (int localValue4 = 0; localValue4 < 128; localValue4++) {
         for (int localValue5 = 0; localValue5 < 128; localValue5++) {
            int localValue6 = localValue5;
            int localValue7 = localValue4;

            for (int localValue8 = 0; localValue8 < localValue2; localValue8++) {
               int localValue10 = 127 - localValue6;
               localValue6 = localValue7;
               localValue7 = localValue10;
            }

            localValue3.setRGB(localValue5, localValue4, MapColor.getRenderColor(localValue1[localValue6 + localValue7 * 128] & 255));
         }
      }

      return localValue3;
   }

   private void internalMethod02950(BufferedImage localValue1, String localValue2) {
      this.internalMethod01770(localValue1, localValue2x -> {
         AutoCaptchaSolver.InternalType0270 localValue3 = this.internalMethod04750(localValue2x);
         this.internalMethod02092(localValue3 == null ? null : localValue3.internalMethod03383(), localValue3 == null ? -1.0 : localValue3.internalMethod06399(), localValue2);
      });
   }

   private void internalMethod01770(BufferedImage localValue1, Consumer<String> localValue2) {
      byte[] localValue3;
      try (ByteArrayOutputStream localValue4 = new ByteArrayOutputStream()) {
         ImageIO.write(localValue1, "png", localValue4);
         localValue3 = localValue4.toByteArray();
      } catch (Exception localValue9) {
         this.internalField0277 = false;
         RockstarClient.internalField0572
            .warn("[AutoCaptcha] \u043d\u0435 \u0441\u043c\u043e\u0433 \u0441\u043e\u0431\u0440\u0430\u0442\u044c png: {}", localValue9.toString());
         return;
      }

      JsonObject localValue10 = new JsonObject();
      localValue10.addProperty("base64", Base64.getEncoder().encodeToString(localValue3));
      HttpRequest localValue5 = HttpRequest.newBuilder(URI.create("https://ftapi.rockstar.pub/v1/captcha"))
         .header("Content-Type", "application/json")
         .header("User-Agent", "NoName")
         .timeout(Duration.ofSeconds(15L))
         .POST(BodyPublishers.ofString(localValue10.toString(), StandardCharsets.UTF_8))
         .build();
      internalField0791.sendAsync(localValue5, BodyHandlers.ofString(StandardCharsets.UTF_8))
         .thenAccept(localValue1x -> internalField0149.execute(() -> localValue2.accept(localValue1x.body())))
         .exceptionally(
            localValue1x -> {
               internalField0149.execute(
                  () -> {
                     this.internalField0277 = false;
                     RockstarClient.internalField0572
                        .warn("[AutoCaptcha] \u0437\u0430\u043f\u0440\u043e\u0441 \u043d\u0435 \u0434\u043e\u0448\u0451\u043b: {}", localValue1x.toString());
                  }
               );
               return null;
            }
         );
   }

   private AutoCaptchaSolver.InternalType0270 internalMethod04750(String localValue1) {
      try {
         JsonObject localValue2 = JsonParser.parseString(localValue1).getAsJsonObject();
         JsonObject localValue3 = localValue2.has("data") && localValue2.get("data").isJsonObject() ? localValue2.getAsJsonObject("data") : localValue2;
         String localValue4 = null;
         if (localValue3.has("solved") && localValue3.get("solved").getAsBoolean() && localValue3.has("text") && !localValue3.get("text").isJsonNull()) {
            localValue4 = localValue3.get("text").getAsString();
         }

         double localValue5 = -1.0;
         if (localValue3.has("overall_percent") && !localValue3.get("overall_percent").isJsonNull()) {
            localValue5 = localValue3.get("overall_percent").getAsDouble();
         }

         return new AutoCaptchaSolver.InternalType0270(localValue4, localValue5);
      } catch (Exception localValue7) {
         RockstarClient.internalField0572.warn("[AutoCaptcha] \u043d\u0435\u043f\u043e\u043d\u044f\u0442\u043d\u044b\u0439 \u043e\u0442\u0432\u0435\u0442: {}", localValue1);
         return null;
      }
   }

   private void internalMethod02092(String localValue1, double localValue2, String localValue4) {
      this.internalField0277 = false;
      if (internalField0149.player != null && localValue4.equals(this.internalField0248) && !localValue4.equals(this.internalField0247)) {
         if (localValue1 != null && !localValue1.isBlank()) {
            if (localValue2 >= 0.0 && localValue2 < 50.0) {
               if (this.internalField1056 >= 3) {
                  ClientMessages.internalMethod03058(Text.of(LanguageManager.internalMethod00160("auto_captcha.low_confidence", localValue1, (int)localValue2)));
               }
            } else {
               this.internalField0247 = localValue4;
               this.internalField0276 = false;
               internalField0149.player.networkHandler.sendChatMessage(localValue1);
               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("auto_captcha.solved", localValue1)));
            }
         } else {
            if (this.internalField1056 >= 3) {
               ClientMessages.internalMethod03058(Text.of(LanguageManager.internalMethod07214("auto_captcha.failed")));
            }
         }
      }
   }

   public void internalMethod03755() {
      this.internalField0248 = null;
      this.internalField0247 = null;
      this.internalField1056 = 0;
      this.internalField0276 = false;
   }

   static final class InternalType0270 {
      private final String internalField0248;
      private final double internalField0194;

      InternalType0270(String localValue1, double localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0194 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0270[text=" + this.internalField0248 + ", percent=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AutoCaptchaSolver.InternalType0270 other = (AutoCaptchaSolver.InternalType0270) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public String internalMethod03383() {
         return this.internalField0248;
      }

      public double internalMethod06399() {
         return this.internalField0194;
      }
   }
}
