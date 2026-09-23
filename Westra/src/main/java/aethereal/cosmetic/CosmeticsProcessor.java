package aethereal.cosmetic;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.core.NativeMethodLookup;
import aethereal.event.BackendEvent;
import aethereal.network.PacketSecurity;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Generated;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.minecraft.class_1011;
import net.minecraft.class_1299;
import net.minecraft.class_922;
import net.minecraft.class_5617.class_5618;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.loading.object.BakedModelFactory;
import software.bernie.geckolib.loading.object.GeometryTree;

public class CosmeticsProcessor extends BaseProcessor {
   private final List<Cosmetic> cosmetics = new CopyOnWriteArrayList<>();
   private final ScheduledExecutorService bootstrapper = Executors.newSingleThreadScheduledExecutor();

   @Compile
   @Override
   public void setup() {
      LivingEntityFeatureRendererRegistrationCallback.EVENT.register(new LivingEntityFeatureRendererRegistrationCallback() {
         public void registerRenderers(class_1299 class_1299Var, class_922 class_922Var, RegistrationHelper registrationHelper, class_5618 class_5618Var) {
            CosmeticsProcessor.lambda$setup$0(class_1299Var, class_922Var, registrationHelper, class_5618Var);
         }
      });
   }

   @Generated
   public List<Cosmetic> getCosmetics() {
      return this.cosmetics;
   }

   @Generated
   public ScheduledExecutorService getBootstrapper() {
      return this.bootstrapper;
   }

   public static void lambda$setup$0(class_1299 type, class_922 renderer, RegistrationHelper helper, class_5618 context) {
      if (type == class_1299.field_6097) {
         helper.register(new CosmeticsRenderer(renderer));
      }
   }

   @EventTarget
   public void onBackend(BackendEvent event) {
      if (event.b() && "cosmetics".equals(event.d().b())) {
         PacketSecurity security = event.d().a();
         String payload = event.d().c();
         String action = security.a(payload, "action");
         String type = security.a(payload, "type");
         String uuid = security.a(payload, "uuid");
         String cosmetic = security.a(payload, "cosmetic");
         if (cosmetic != null && uuid != null) {
            if ("WEAR".equals(action)) {
               if (CosmeticsType.COSMETIC.name().equals(type)) {
                  this.bootstrapper.execute(() -> this.registerCosmetic(security, UUID.fromString(uuid), cosmetic));
               }
            } else if ("UNWEAR".equals(action) && CosmeticsType.COSMETIC.name().equals(type)) {
               this.bootstrapper.execute(() -> this.unregisterCosmetic(UUID.fromString(uuid), CosmeticsCategory.valueOf(security.a(cosmetic, "category"))));
            }
         }
      }

      if (event.c()) {
         this.cosmetics.clear();
      }
   }

   private void registerCosmetic(PacketSecurity security, UUID uuid, String cosmetic) {
      CosmeticsCategory category = CosmeticsCategory.valueOf(security.a(cosmetic, "category"));
      String name = security.a(cosmetic, "name");
      String geometry = security.a(cosmetic, "geometry");
      String animations = security.a(cosmetic, "animation");
      byte[] texture = Base64.getDecoder().decode(security.a(cosmetic, "texture"));
      float scale = Float.parseFloat(security.a(cosmetic, "scale"));
      Vector3f offset = new Vector3f(
         Float.parseFloat(security.a(cosmetic, "x")), Float.parseFloat(security.a(cosmetic, "y")), Float.parseFloat(security.a(cosmetic, "z"))
      );
      BakedGeoModel bakedModel = BakedModelFactory.getForNamespace("westra")
         .constructGeoModel(GeometryTree.fromModel((Model)KeyFramesAdapter.GEO_GSON.fromJson(geometry, Model.class)));
      BakedAnimations bakedAnimations = animations == null
         ? null
         : (BakedAnimations)KeyFramesAdapter.GEO_GSON
            .fromJson(JsonParser.parseString(animations).getAsJsonObject().getAsJsonObject("animations"), BakedAnimations.class);

      class_1011 image;
      try {
         image = class_1011.method_4309(new ByteArrayInputStream(texture));
      } catch (IOException var15) {
         return;
      }

      aM_.execute(() -> this.register(new Cosmetic(name, uuid, category, scale, offset, bakedModel, bakedAnimations, image)));
   }

   public Cosmetic register(Cosmetic cosmetic) {
      this.cosmetics
         .removeIf(
            existing -> existing.getType() == cosmetic.getType()
               && existing.getCategory() == cosmetic.getCategory()
               && Objects.equals(existing.getUuid(), cosmetic.getUuid())
         );
      this.cosmetics.add(cosmetic);
      return cosmetic;
   }

   public void unregisterCosmetic(UUID uuid, CosmeticsCategory category) {
      this.cosmetics
         .removeIf(existing -> existing.getType() == CosmeticsType.COSMETIC && existing.getCategory() == category && Objects.equals(existing.getUuid(), uuid));
   }

   @Override
   public void unSetup() {
      this.cosmetics.clear();
   }

   static {
      NativeMethodLookup.lookup(CosmeticsProcessor.class, 24);
   }
}
