package ru.prism.cosmetic.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ru.prism.cosmetic.geo.GeoBone;
import ru.prism.cosmetic.geo.GeoCube;
import ru.prism.cosmetic.geo.GeoModel;
import ru.prism.cosmetic.geo.GeoModelParser;
import ru.prism.cosmetic.geo.GeoQuad;
import ru.prism.cosmetic.geo.GeoVertex;
import ru.prism.cosmetic.model.CosmeticModel;
import ru.prism.cosmetic.model.ModelPosition;

public class CosmeticRenderer {
   private static final float RAD_TO_DEG = 180.0F / (float)Math.PI;
   private static final Vector3f POSITIVE_X = new Vector3f(1.0F, 0.0F, 0.0F);
   private static final Vector3f POSITIVE_Y = new Vector3f(0.0F, 1.0F, 0.0F);
   private static final Vector3f POSITIVE_Z = new Vector3f(0.0F, 0.0F, 1.0F);
   private final Map<Integer, GeoModel> modelCache = new ConcurrentHashMap<>();
   private final Map<Integer, CosmeticRenderer.CosmeticAnimationData> animationCache = new ConcurrentHashMap<>();
   private final Set<Integer> noAnimationSet = ConcurrentHashMap.newKeySet();
   private final Map<Integer, Long> animationStartTime = new ConcurrentHashMap<>();
   private final Map<Integer, Map<String, float[]>> initialBoneTransforms = new ConcurrentHashMap<>();
   private final Map<Integer, float[]> previewBounds = new ConcurrentHashMap<>();

   public void render(CosmeticModel cosmetic, MatrixStack matrices, VertexConsumer consumer, int light, PlayerEntityModel playerModel) {
      if (cosmetic == null || cosmetic.getTextureId() == null || consumer == null) {
         return;
      }

      matrices.push();
      float yOffset = this.transformToPosition(cosmetic, playerModel, matrices);
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      matrices.translate(cosmetic.getX(), cosmetic.getY() + yOffset, cosmetic.getZ());
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cosmetic.getYaw()));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cosmetic.getPitch()));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(cosmetic.getRoll()));
      float scale = cosmetic.getScale();
      matrices.scale(scale, scale, scale);

      GeoModel model = this.getOrParseModel(cosmetic);
      if (model != null) {
         CosmeticRenderer.CosmeticAnimationData animation = this.getOrParseAnimation(cosmetic);
         if (animation != null) {
            this.applyAnimations(model, animation, cosmetic.getId());
         }

         for (GeoBone bone : model.topLevelBones) {
            this.renderBone(bone, matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
         }
      }

      matrices.pop();
   }

   public void renderPreview(CosmeticModel cosmetic, MatrixStack matrices, VertexConsumer consumer, int light, float alpha) {
      if (cosmetic == null || cosmetic.getTextureId() == null || consumer == null) {
         return;
      }

      matrices.push();
      this.applyPreviewTransform(cosmetic, matrices);
      GeoModel model = this.getOrParseModel(cosmetic);
      if (model != null) {
         CosmeticRenderer.CosmeticAnimationData animation = this.getOrParseAnimation(cosmetic);
         if (animation != null) {
            this.applyAnimations(model, animation, cosmetic.getId());
         }

         for (GeoBone bone : model.topLevelBones) {
            this.renderBone(bone, matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);
         }
      }

      matrices.pop();
   }

   public GeoModel getPreviewModel(CosmeticModel cosmetic) {
      return cosmetic == null ? null : this.getOrParseModel(cosmetic);
   }

   public float[] getPreviewBounds(CosmeticModel cosmetic) {
      if (cosmetic == null) {
         return null;
      }

      int id = cosmetic.getId();
      float[] cached = this.previewBounds.get(id);
      if (cached != null) {
         return cached;
      }

      GeoModel model = this.getOrParseModel(cosmetic);
      if (model == null) {
         return null;
      }

      float[] bounds = new float[]{
         Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE
      };
      MatrixStack matrices = new MatrixStack();
      matrices.push();
      this.applyPreviewTransform(cosmetic, matrices);
      for (GeoBone bone : model.topLevelBones) {
         this.expandBoneBounds(bone, matrices, bounds);
      }

      matrices.pop();
      if (bounds[0] > bounds[3] || bounds[1] > bounds[4] || bounds[2] > bounds[5]) {
         return null;
      }

      this.previewBounds.put(id, bounds);
      return bounds;
   }

   private void applyPreviewTransform(CosmeticModel cosmetic, MatrixStack matrices) {
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      matrices.translate(cosmetic.getX(), cosmetic.getY(), cosmetic.getZ());
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cosmetic.getYaw()));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cosmetic.getPitch()));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(cosmetic.getRoll()));
      float scale = cosmetic.getScale();
      matrices.scale(scale, scale, scale);
   }

   private void expandBoneBounds(GeoBone bone, MatrixStack matrices, float[] bounds) {
      if (bone.isHidden) {
         return;
      }

      matrices.push();
      this.translate(bone, matrices);
      this.moveToPivot(bone, matrices);
      this.rotate(bone, matrices);
      this.scale(bone, matrices);
      this.moveBackFromPivot(bone, matrices);

      for (GeoCube cube : bone.childCubes) {
         matrices.push();
         this.moveToPivot(cube, matrices);
         this.rotate(cube, matrices);
         this.moveBackFromPivot(cube, matrices);
         Matrix4f matrix = matrices.peek().getPositionMatrix();

         for (GeoQuad quad : cube.quads) {
            if (quad == null) {
               continue;
            }

            for (GeoVertex vertex : quad.vertices) {
               Vector4f point = new Vector4f(vertex.position.getX(), vertex.position.getY(), vertex.position.getZ(), 1.0F);
               point.mul(matrix);
               bounds[0] = Math.min(bounds[0], point.x());
               bounds[1] = Math.min(bounds[1], point.y());
               bounds[2] = Math.min(bounds[2], point.z());
               bounds[3] = Math.max(bounds[3], point.x());
               bounds[4] = Math.max(bounds[4], point.y());
               bounds[5] = Math.max(bounds[5], point.z());
            }
         }

         matrices.pop();
      }

      for (GeoBone child : bone.childBones) {
         this.expandBoneBounds(child, matrices, bounds);
      }

      matrices.pop();
   }

   public void clearCaches() {
      this.modelCache.clear();
      this.animationCache.clear();
      this.noAnimationSet.clear();
      this.animationStartTime.clear();
      this.initialBoneTransforms.clear();
      this.previewBounds.clear();
   }

   private float transformToPosition(CosmeticModel cosmetic, PlayerEntityModel playerModel, MatrixStack matrices) {
      float offset = 0.0F;
      ModelPosition position = cosmetic.getPosition();
      if (playerModel == null) {
         return offset;
      }

      switch (position) {
         case HEAD:
            this.transformToModelPart(playerModel.head, matrices);
            offset = 0.5F;
            break;
         case ABOVE_HEAD:
            offset = 0.75F;
            break;
         case BODY:
            this.transformToModelPart(playerModel.body, matrices);
            offset = -0.3F;
            break;
         case RIGHT_ARM:
            this.transformToModelPart(playerModel.rightArm, matrices);
            offset = -0.25F;
            break;
         case LEFT_ARM:
            this.transformToModelPart(playerModel.leftArm, matrices);
            offset = -0.25F;
            break;
         case RIGHT_LEG:
            this.transformToModelPart(playerModel.rightLeg, matrices);
            offset = -0.35F;
            break;
         case LEFT_LEG:
            this.transformToModelPart(playerModel.leftLeg, matrices);
            offset = -0.35F;
         case FREE:
      }

      return offset;
   }

   private void transformToModelPart(ModelPart part, MatrixStack matrices) {
      matrices.translate(part.originX * 0.0625F, part.originY * 0.0625F, part.originZ * 0.0625F);
      this.rotateDegrees(matrices, part.pitch * RAD_TO_DEG, part.yaw * RAD_TO_DEG, part.roll * RAD_TO_DEG);
   }

   private void rotateDegrees(MatrixStack matrices, float x, float y, float z) {
      if (z != 0.0F) {
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(z));
      }

      if (y != 0.0F) {
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(y));
      }

      if (x != 0.0F) {
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(x));
      }
   }

   private GeoModel getOrParseModel(CosmeticModel cosmetic) {
      int id = cosmetic.getId();
      if (this.modelCache.containsKey(id)) {
         return this.modelCache.get(id);
      }

      GeoModel model = GeoModelParser.parse(cosmetic.getRawModelJson());
      if (model != null) {
         this.modelCache.put(id, model);
         this.saveInitialBoneTransforms(id, model);
      }

      return model;
   }

   private void saveInitialBoneTransforms(int id, GeoModel model) {
      HashMap<String, float[]> transforms = new HashMap<>();

      for (GeoBone bone : model.topLevelBones) {
         this.saveBonesRecursive(bone, transforms);
      }

      this.initialBoneTransforms.put(id, transforms);
   }

   private void saveBonesRecursive(GeoBone bone, Map<String, float[]> transforms) {
      transforms.put(
         bone.name,
         new float[]{
            bone.getRotationX(),
            bone.getRotationY(),
            bone.getRotationZ(),
            bone.getPositionX(),
            bone.getPositionY(),
            bone.getPositionZ(),
            bone.getScaleX(),
            bone.getScaleY(),
            bone.getScaleZ()
         }
      );

      for (GeoBone child : bone.childBones) {
         this.saveBonesRecursive(child, transforms);
      }
   }

   private void renderBone(GeoBone bone, MatrixStack matrices, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
      if (!bone.isHidden) {
         matrices.push();
         this.translate(bone, matrices);
         this.moveToPivot(bone, matrices);
         this.rotate(bone, matrices);
         this.scale(bone, matrices);
         this.moveBackFromPivot(bone, matrices);

         for (GeoCube cube : bone.childCubes) {
            this.renderCube(cube, matrices, consumer, light, overlay, r, g, b, a);
         }

         for (GeoBone child : bone.childBones) {
            this.renderBone(child, matrices, consumer, light, overlay, r, g, b, a);
         }

         matrices.pop();
      }
   }

   private void renderCube(GeoCube cube, MatrixStack matrices, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
      matrices.push();
      this.moveToPivot(cube, matrices);
      this.rotate(cube, matrices);
      this.moveBackFromPivot(cube, matrices);
      Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
      Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

      for (GeoQuad quad : cube.quads) {
         if (quad != null) {
            Vector3f normal = new Vector3f(quad.normal.getX(), quad.normal.getY(), quad.normal.getZ());
            normalMatrix.transform(normal);
            float nx = normal.x();
            float ny = normal.y();
            float nz = normal.z();
            if ((cube.size.getY() == 0.0F || cube.size.getZ() == 0.0F) && nx < 0.0F) {
               nx = -nx;
            }

            if ((cube.size.getX() == 0.0F || cube.size.getZ() == 0.0F) && ny < 0.0F) {
               ny = -ny;
            }

            if ((cube.size.getX() == 0.0F || cube.size.getY() == 0.0F) && nz < 0.0F) {
               nz = -nz;
            }

            for (GeoVertex vertex : quad.vertices) {
               consumer.vertex(positionMatrix, vertex.position.getX(), vertex.position.getY(), vertex.position.getZ())
                  .color(r, g, b, a)
                  .texture(vertex.textureU, vertex.textureV)
                  .overlay(overlay)
                  .light(light)
                  .normal(nx, ny, nz);
            }
         }
      }

      matrices.pop();
   }

   private void translate(GeoBone bone, MatrixStack matrices) {
      matrices.translate(-bone.getPositionX() / 16.0F, bone.getPositionY() / 16.0F, bone.getPositionZ() / 16.0F);
   }

   private void moveToPivot(GeoBone bone, MatrixStack matrices) {
      matrices.translate(bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F, bone.getPivotZ() / 16.0F);
   }

   private void moveBackFromPivot(GeoBone bone, MatrixStack matrices) {
      matrices.translate(-bone.getPivotX() / 16.0F, -bone.getPivotY() / 16.0F, -bone.getPivotZ() / 16.0F);
   }

   private void rotate(GeoBone bone, MatrixStack matrices) {
      if (bone.getRotationZ() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_Z, bone.getRotationZ()));
      }

      if (bone.getRotationY() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_Y, bone.getRotationY()));
      }

      if (bone.getRotationX() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_X, bone.getRotationX()));
      }
   }

   private void scale(GeoBone bone, MatrixStack matrices) {
      matrices.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
   }

   private void moveToPivot(GeoCube cube, MatrixStack matrices) {
      matrices.translate(cube.pivot.getX() / 16.0F, cube.pivot.getY() / 16.0F, cube.pivot.getZ() / 16.0F);
   }

   private void moveBackFromPivot(GeoCube cube, MatrixStack matrices) {
      matrices.translate(-cube.pivot.getX() / 16.0F, -cube.pivot.getY() / 16.0F, -cube.pivot.getZ() / 16.0F);
   }

   private void rotate(GeoCube cube, MatrixStack matrices) {
      if (cube.rotation.getZ() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_Z, cube.rotation.getZ()));
      }

      if (cube.rotation.getY() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_Y, cube.rotation.getY()));
      }

      if (cube.rotation.getX() != 0.0F) {
         matrices.multiply(getRadialQuaternion(POSITIVE_X, cube.rotation.getX()));
      }
   }

   private static Quaternionf getRadialQuaternion(Vector3f axis, float angle) {
      float sin = (float)Math.sin(angle / 2.0F);
      float x = axis.x() * sin;
      float y = axis.y() * sin;
      float z = axis.z() * sin;
      float w = (float)Math.cos(angle / 2.0F);
      return new Quaternionf(x, y, z, w);
   }

   private CosmeticRenderer.CosmeticAnimationData getOrParseAnimation(CosmeticModel cosmetic) {
      int id = cosmetic.getId();
      if (this.animationCache.containsKey(id)) {
         return this.animationCache.get(id);
      }

      if (this.noAnimationSet.contains(id)) {
         return null;
      }

      JsonObject animationJson = cosmetic.getAnimationJson();
      if (animationJson == null) {
         this.noAnimationSet.add(id);
         return null;
      }

      try {
         CosmeticRenderer.CosmeticAnimationData data = this.parseAnimationData(animationJson);
         if (data != null) {
            this.animationCache.put(id, data);
         } else {
            this.noAnimationSet.add(id);
         }

         return data;
      } catch (Exception exception) {
         this.noAnimationSet.add(id);
         return null;
      }
   }

   private CosmeticRenderer.CosmeticAnimationData parseAnimationData(JsonObject animationJson) {
      CosmeticRenderer.CosmeticAnimationData data = new CosmeticRenderer.CosmeticAnimationData();
      if (!animationJson.has("animations")) {
         return null;
      }

      JsonObject animations = animationJson.getAsJsonObject("animations");
      Iterator<Map.Entry<String, JsonElement>> iterator = animations.entrySet().iterator();
      if (iterator.hasNext()) {
         Map.Entry<String, JsonElement> entry = iterator.next();
         JsonObject animation = entry.getValue().getAsJsonObject();
         data.animationName = entry.getKey();
         data.loop = animation.has("loop") && animation.get("loop").getAsBoolean();
         data.length = animation.has("animation_length") ? animation.get("animation_length").getAsFloat() : 1.0F;
         if (animation.has("bones")) {
            JsonObject bones = animation.getAsJsonObject("bones");

            for (Map.Entry<String, JsonElement> boneEntry : bones.entrySet()) {
               String boneName = boneEntry.getKey();
               JsonObject bone = boneEntry.getValue().getAsJsonObject();
               CosmeticRenderer.BoneAnimationData boneData = new CosmeticRenderer.BoneAnimationData();
               if (bone.has("rotation")) {
                  boneData.rotationKeyframes = this.parseKeyframes(bone.get("rotation"));
               }

               if (bone.has("position")) {
                  boneData.positionKeyframes = this.parseKeyframes(bone.get("position"));
               }

               if (bone.has("scale")) {
                  boneData.scaleKeyframes = this.parseKeyframes(bone.get("scale"));
               }

               data.boneAnimations.put(boneName, boneData);
            }
         }
      }

      return data;
   }

   private Map<Float, float[]> parseKeyframes(JsonElement keyframeElement) {
      HashMap<Float, float[]> keyframes = new HashMap<>();
      if (keyframeElement.isJsonObject()) {
         JsonObject keyframeObject = keyframeElement.getAsJsonObject();

         for (Map.Entry<String, JsonElement> entry : keyframeObject.entrySet()) {
            try {
               float time = Float.parseFloat(entry.getKey());
               JsonElement value = entry.getValue();
               float[] vector = new float[3];
               if (value.isJsonObject()) {
                  JsonObject valueObject = value.getAsJsonObject();
                  if (valueObject.has("vector")) {
                     JsonArray array = valueObject.getAsJsonArray("vector");
                     vector[0] = array.get(0).getAsFloat();
                     vector[1] = array.get(1).getAsFloat();
                     vector[2] = array.get(2).getAsFloat();
                  }
               } else if (value.isJsonArray()) {
                  JsonArray array = value.getAsJsonArray();
                  vector[0] = array.get(0).getAsFloat();
                  vector[1] = array.get(1).getAsFloat();
                  vector[2] = array.get(2).getAsFloat();
               }

               keyframes.put(time, vector);
            } catch (NumberFormatException exception) {
            }
         }
      }

      return keyframes;
   }

   private void applyAnimations(GeoModel model, CosmeticRenderer.CosmeticAnimationData animation, int id) {
      Map<String, float[]> initialTransforms = this.initialBoneTransforms.get(id);
      if (initialTransforms != null) {
         long startTime = this.animationStartTime.computeIfAbsent(id, key -> System.currentTimeMillis());
         float elapsed = (float)(System.currentTimeMillis() - startTime) / 1000.0F;
         float time;
         if (animation.loop && animation.length > 0.0F) {
            time = elapsed % animation.length;
         } else {
            time = Math.min(elapsed, animation.length);
         }

         for (GeoBone bone : model.topLevelBones) {
            this.resetBoneRecursive(bone, initialTransforms);
         }

         for (Map.Entry<String, CosmeticRenderer.BoneAnimationData> entry : animation.boneAnimations.entrySet()) {
            String boneName = entry.getKey();
            CosmeticRenderer.BoneAnimationData boneData = entry.getValue();
            GeoBone bone = this.findBone(model, boneName);
            if (bone != null) {
               float[] initial = initialTransforms.get(boneName);
               if (initial == null) {
                  initial = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F};
               }

               if (!boneData.rotationKeyframes.isEmpty()) {
                  float[] rotation = this.interpolateKeyframes(boneData.rotationKeyframes, time);
                  bone.setRotationX(initial[0] + (float)Math.toRadians(-rotation[0]));
                  bone.setRotationY(initial[1] + (float)Math.toRadians(-rotation[1]));
                  bone.setRotationZ(initial[2] + (float)Math.toRadians(rotation[2]));
               }

               if (!boneData.positionKeyframes.isEmpty()) {
                  float[] position = this.interpolateKeyframes(boneData.positionKeyframes, time);
                  bone.setPositionX(initial[3] + position[0]);
                  bone.setPositionY(initial[4] + position[1]);
                  bone.setPositionZ(initial[5] + position[2]);
               }

               if (!boneData.scaleKeyframes.isEmpty()) {
                  float[] scale = this.interpolateKeyframes(boneData.scaleKeyframes, time);
                  bone.setScaleX(initial[6] * scale[0]);
                  bone.setScaleY(initial[7] * scale[1]);
                  bone.setScaleZ(initial[8] * scale[2]);
               }
            }
         }
      }
   }

   private void resetBoneRecursive(GeoBone bone, Map<String, float[]> initialTransforms) {
      float[] initial = initialTransforms.get(bone.name);
      if (initial != null) {
         bone.setRotationX(initial[0]);
         bone.setRotationY(initial[1]);
         bone.setRotationZ(initial[2]);
         bone.setPositionX(initial[3]);
         bone.setPositionY(initial[4]);
         bone.setPositionZ(initial[5]);
         bone.setScaleX(initial[6]);
         bone.setScaleY(initial[7]);
         bone.setScaleZ(initial[8]);
      }

      for (GeoBone child : bone.childBones) {
         this.resetBoneRecursive(child, initialTransforms);
      }
   }

   private GeoBone findBone(GeoModel model, String name) {
      for (GeoBone bone : model.topLevelBones) {
         GeoBone found = this.findBoneRecursive(bone, name);
         if (found != null) {
            return found;
         }
      }

      return null;
   }

   private GeoBone findBoneRecursive(GeoBone bone, String name) {
      if (bone.name.equals(name)) {
         return bone;
      }

      for (GeoBone child : bone.childBones) {
         GeoBone found = this.findBoneRecursive(child, name);
         if (found != null) {
            return found;
         }
      }

      return null;
   }

   private float[] interpolateKeyframes(Map<Float, float[]> keyframes, float time) {
      if (keyframes.isEmpty()) {
         return new float[]{0.0F, 0.0F, 0.0F};
      }

      Float previousTime = null;
      Float nextTime = null;
      float[] previousValue = null;
      float[] nextValue = null;

      for (Map.Entry<Float, float[]> entry : keyframes.entrySet()) {
         float frameTime = entry.getKey();
         if (frameTime <= time && (previousTime == null || frameTime > previousTime)) {
            previousTime = frameTime;
            previousValue = entry.getValue();
         }

         if (frameTime >= time && (nextTime == null || frameTime < nextTime)) {
            nextTime = frameTime;
            nextValue = entry.getValue();
         }
      }

      if (previousValue == null && nextValue == null) {
         return new float[]{0.0F, 0.0F, 0.0F};
      }

      if (previousValue == null) {
         return nextValue;
      }

      if (nextValue == null) {
         return previousValue;
      }

      if (previousTime.equals(nextTime)) {
         return previousValue;
      }

      float progress = (time - previousTime) / (nextTime - previousTime);
      return new float[]{
         previousValue[0] + progress * (nextValue[0] - previousValue[0]),
         previousValue[1] + progress * (nextValue[1] - previousValue[1]),
         previousValue[2] + progress * (nextValue[2] - previousValue[2])
      };
   }

   private static class BoneAnimationData {
      Map<Float, float[]> rotationKeyframes = new HashMap<>();
      Map<Float, float[]> positionKeyframes = new HashMap<>();
      Map<Float, float[]> scaleKeyframes = new HashMap<>();
   }

   private static class CosmeticAnimationData {
      String animationName;
      boolean loop;
      float length;
      Map<String, CosmeticRenderer.BoneAnimationData> boneAnimations = new HashMap<>();
   }
}
