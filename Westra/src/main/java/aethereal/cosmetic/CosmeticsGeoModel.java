package aethereal.cosmetic;

import net.minecraft.class_2960;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CosmeticsGeoModel extends GeoModel<Cosmetic> {
   private final Cosmetic cosmetic;

   public CosmeticsGeoModel(Cosmetic cosmetic) {
      this.cosmetic = cosmetic;
   }

   public class_2960 getModelResource(Cosmetic cosmetic, GeoRenderer<Cosmetic> renderer) {
      return null;
   }

   public class_2960 getTextureResource(Cosmetic cosmetic, GeoRenderer<Cosmetic> renderer) {
      return cosmetic.getImage();
   }

   public class_2960 getAnimationResource(Cosmetic cosmetic) {
      return null;
   }

   public BakedGeoModel getBakedModel(class_2960 resource) {
      BakedGeoModel model = this.cosmetic.getBakedModel();
      this.getAnimationProcessor().setActiveModel(model);
      return model;
   }

   public Animation getAnimation(Cosmetic animatable, String name) {
      return this.cosmetic.getBakedAnimations() == null ? null : this.cosmetic.getBakedAnimations().getAnimation(name);
   }
}
