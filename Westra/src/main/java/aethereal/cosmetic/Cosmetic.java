package aethereal.cosmetic;

import aethereal.core.Interface;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

public class Cosmetic implements GeoAnimatable {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private final GeoObjectRenderer<Cosmetic> renderer;
   private final String name;
   private final UUID uuid;
   private final CosmeticsType type;
   private final CosmeticsCategory category;
   private final Vector3f offset;
   private final float scale;
   private final BakedGeoModel bakedModel;
   private final BakedAnimations bakedAnimations;
   private final class_2960 image;

   @Generated
   public AnimatableInstanceCache getCache() {
      return this.cache;
   }

   @Generated
   public GeoObjectRenderer<Cosmetic> getRenderer() {
      return this.renderer;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public CosmeticsType getType() {
      return this.type;
   }

   @Generated
   public CosmeticsCategory getCategory() {
      return this.category;
   }

   @Generated
   public Vector3f getOffset() {
      return this.offset;
   }

   @Generated
   public float getScale() {
      return this.scale;
   }

   @Generated
   public BakedGeoModel getBakedModel() {
      return this.bakedModel;
   }

   @Generated
   public BakedAnimations getBakedAnimations() {
      return this.bakedAnimations;
   }

   @Generated
   public class_2960 getImage() {
      return this.image;
   }

   public Cosmetic(
      String name,
      UUID uuid,
      CosmeticsCategory category,
      float scale,
      Vector3f offset,
      BakedGeoModel bakedModel,
      BakedAnimations bakedAnimations,
      class_1011 image
   ) {
      this.name = name;
      this.uuid = uuid;
      this.type = CosmeticsType.COSMETIC;
      this.category = category;
      this.offset = offset;
      this.scale = scale;
      this.bakedModel = bakedModel;
      this.bakedAnimations = bakedAnimations;
      this.image = class_2960.method_60655("westra", "cosmetics/" + name);
      this.renderer = new GeoObjectRenderer(new CosmeticsGeoModel(this));
      Interface.aM_.method_1531().method_4616(this.image, new class_1043(image));
   }

   public Cosmetic(String name, UUID uuid, BakedAnimations bakedAnimations) {
      this.name = name;
      this.uuid = uuid;
      this.type = CosmeticsType.EMOTION;
      this.category = null;
      this.offset = new Vector3f(0.0F, 0.0F, 0.0F);
      this.scale = 1.0F;
      this.bakedModel = null;
      this.bakedAnimations = bakedAnimations;
      this.image = null;
      this.renderer = null;
   }

   public void registerControllers(ControllerRegistrar controllers) {
      if (this.bakedAnimations != null && !this.bakedAnimations.animations().isEmpty()) {
         controllers.add(
            new AnimationController(
               this,
               "main",
               0,
               state -> state.setAndContinue(RawAnimation.begin().thenLoop((String)this.bakedAnimations.animations().keySet().iterator().next()))
            )
         );
      }
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   public double getTick(Object entity) {
      return RenderUtil.getCurrentTick();
   }
}
