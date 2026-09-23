package ru.prism.cosmetic.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.prism.cosmetic.model.CosmeticModel;
import ru.prism.cosmetic.model.ModelPosition;

public class CosmeticLoader {
   public static final String NAMESPACE = "client";
   private static CosmeticLoader instance;
   private final Map<Integer, CosmeticModel> loadedCosmetics = new ConcurrentHashMap<>();
   private final Set<Integer> failed = ConcurrentHashMap.newKeySet();
   private final Map<Integer, Identifier> capeTextures = new ConcurrentHashMap<>();
   private final Map<Integer, Boolean> capeSkinLayout = new ConcurrentHashMap<>();
   private volatile boolean loading;

   public static CosmeticLoader getInstance() {
      if (instance == null) {
         instance = new CosmeticLoader();
      }

      return instance;
   }

   public boolean isLoading() {
      return this.loading;
   }

   public void loadAll(ResourceManager manager, int count) {
      if (this.loading) {
         return;
      }

      this.loading = true;
      Thread thread = new Thread(() -> {
         for (int i = 0; i < count; i++) {
            if (this.loadedCosmetics.containsKey(i) || this.failed.contains(i)) {
               continue;
            }

            try {
               Identifier resourceId = Identifier.of(NAMESPACE, "cosmetics/models/cosmetic_" + i + ".json");
               InputStream stream = manager.open(resourceId);
               String text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
               JsonObject root = JsonParser.parseString(text).getAsJsonObject();
               this.loadFromJson(i, root);
            } catch (Exception exception) {
               this.failed.add(i);
            }
         }

         this.loading = false;
      }, "prism-cosmetics");
      thread.setDaemon(true);
      thread.start();
   }

   private void loadFromJson(int index, JsonObject root) {
      if (root == null || !root.has("name")) {
         this.failed.add(index);
         return;
      }

      String name = root.get("name").getAsString();
      int category = root.has("category") ? root.get("category").getAsInt() : 1;
      boolean hasModel = root.has("model");
      CosmeticModel model = new CosmeticModel(name, index, category);

      if (hasModel) {
         model.setRawModelJson(root.getAsJsonObject("model").toString());
         this.parseModelPosition(root, model);

         if (root.has("height")) {
            model.setHeight(root.get("height").getAsFloat());
         }

         if (root.has("previewScale")) {
            model.setPreviewScale(root.get("previewScale").getAsFloat());
         }

         if (root.has("previewY")) {
            model.setPreviewY(root.get("previewY").getAsFloat());
         }

         if (root.has("animation")) {
            model.setAnimationJson(root.getAsJsonObject("animation"));
         }
      }

      NativeImage image = null;
      if (root.has("texture")) {
         try {
            byte[] bytes = Base64.getDecoder().decode(root.get("texture").getAsString().replace(" ", ""));
            image = NativeImage.read(new java.io.ByteArrayInputStream(bytes));
         } catch (Exception exception) {
            image = null;
         }
      }

      if (image == null) {
         this.failed.add(index);
         return;
      }

      String key = name.replace(" ", "").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_./-]", "") + "_" + index;
      NativeImage finalImage = image;
      boolean finalHasModel = hasModel;
      MinecraftClient.getInstance().execute(() -> {
         Identifier textureId = Identifier.of(NAMESPACE, "cosmetic/" + key);
         NativeImageBackedTexture texture = new NativeImageBackedTexture(() -> "prism cosmetic", finalImage);
         MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, texture);

         if (finalHasModel) {
            model.setTextureId(textureId);
            this.loadedCosmetics.put(index, model);
         } else {
            this.capeTextures.put(index, textureId);
            this.capeSkinLayout.put(index, finalImage.getWidth() == finalImage.getHeight() * 2);
         }
      });
   }

   private void parseModelPosition(JsonObject root, CosmeticModel model) {
      if (root.has("pos")) {
         model.setPosition(ModelPosition.getById(root.get("pos").getAsInt()));
      }

      if (root.has("scale")) {
         model.setScale(root.get("scale").getAsFloat());
      }

      if (root.has("x")) {
         model.setX(root.get("x").getAsFloat());
      }

      if (root.has("y")) {
         model.setY(root.get("y").getAsFloat());
      }

      if (root.has("z")) {
         model.setZ(root.get("z").getAsFloat());
      }

      if (root.has("yaw")) {
         model.setYaw(root.get("yaw").getAsFloat());
      }

      if (root.has("pitch")) {
         model.setPitch(root.get("pitch").getAsFloat());
      }

      if (root.has("roll")) {
         model.setRoll(root.get("roll").getAsFloat());
      }
   }

   public CosmeticModel getCosmetic(int index) {
      return this.loadedCosmetics.get(index);
   }

   public Map<Integer, CosmeticModel> getAllCosmetics() {
      return this.loadedCosmetics;
   }

   public Identifier getCapeTexture(int index) {
      return this.capeTextures.get(index);
   }

   public boolean isCapeSkinLayout(int index) {
      return this.capeSkinLayout.getOrDefault(index, false);
   }

   public void reset() {
      this.loadedCosmetics.clear();
      this.capeTextures.clear();
      this.capeSkinLayout.clear();
      this.failed.clear();
   }
}
