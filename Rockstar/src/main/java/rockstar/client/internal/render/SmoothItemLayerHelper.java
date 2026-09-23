package rockstar.client.internal.render;


import rockstar.client.*;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.UnaryOperator;
import moscow.rockstar.mixin.accessors.SpriteAtlasTextureAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.SmoothItemLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.MathHelper;
import rockstar.client.render.LegacyRenderCompat;

public final class SmoothItemLayerHelper {
   private static final Map<BakedQuad, BakedQuad> internalField0543 = new WeakHashMap<>();
   private static SpriteAtlasTexture internalField0515;
   private static boolean internalField0277;
   static boolean internalField0276;

   private SmoothItemLayerHelper() {
   }

   public static void internalMethod00865(ItemRenderState localValue0, MatrixStack localValue1, VertexConsumerProvider localValue2, int localValue3, int localValue4) {
      internalMethod03538(localValue0, localValue1, localValue2, localValue3, localValue4, UnaryOperator.identity());
   }

   public static void internalMethod03538(
      ItemRenderState localValue0, MatrixStack localValue1, VertexConsumerProvider localValue2, int localValue3, int localValue4, UnaryOperator<RenderLayer> localValue5
   ) {
      if (internalField0276) {
         LegacyRenderCompat.renderItemState(localValue0, localValue1, localValue2, localValue3, localValue4, 0);
      } else {
         boolean localValue6 = internalField0277;
         internalField0277 = true;

         try {
            LegacyRenderCompat.renderItemState(
               localValue0,
               localValue1,
               localValue2x -> localValue2.getBuffer(internalMethod02228(localValue5.apply(localValue2x))),
               localValue3,
               localValue4,
               0
            );
         } finally {
            internalField0277 = localValue6;
         }
      }
   }

   public static SmoothItemLayerHelper.InternalType0310 internalMethod04678() {
      return new SmoothItemLayerHelper.InternalType0310();
   }

   public static RenderLayer internalMethod02228(RenderLayer localValue0) {
      if (!SmoothItemLayers.ready()) {
         return localValue0;
      } else if (localValue0 == TexturedRenderLayers.getItemTranslucentCull()) {
         return SmoothItemLayers.translucent(SpriteAtlasTexture.ITEMS_ATLAS_TEXTURE);
      } else if (localValue0 == TexturedRenderLayers.getBlockTranslucentCull()) {
         internalMethod04532();
         return SmoothItemLayers.translucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
      } else if (localValue0 == TexturedRenderLayers.getEntityCutout()) {
         internalMethod04532();
         return SmoothItemLayers.cutout(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
      } else {
         return localValue0;
      }
   }

   public static BakedQuad internalMethod04269(BakedQuad localValue0) {
      if (internalField0277 && SmoothItemLayers.ready()) {
         BakedQuad localValue1 = internalField0543.get(localValue0);
         if (localValue1 == null) {
            localValue1 = internalMethod01912(localValue0);
            internalField0543.put(localValue0, localValue1);
         }

         return localValue1;
      } else {
         return localValue0;
      }
   }

   private static BakedQuad internalMethod01912(BakedQuad localValue0) {
      Sprite localValue1 = localValue0.sprite();
      long[] localValue2 = new long[]{localValue0.packedUV0(), localValue0.packedUV1(), localValue0.packedUV2(), localValue0.packedUV3()};
      int localValue3 = 4;
      float localValue4;
      float localValue5;
      float localValue6;
      float localValue7;
      if (localValue1 != null) {
         float localValue8 = (localValue1.getMaxU() - localValue1.getMinU()) / (2.0F * Math.max(1, localValue1.getContents().getWidth()));
         float localValue9 = (localValue1.getMaxV() - localValue1.getMinV()) / (2.0F * Math.max(1, localValue1.getContents().getHeight()));
         localValue4 = localValue1.getMinU() + localValue8;
         localValue5 = localValue1.getMaxU() - localValue8;
         localValue6 = localValue1.getMinV() + localValue9;
         localValue7 = localValue1.getMaxV() - localValue9;
      } else {
         if (internalField0515 == null) {
            return localValue0;
         }

         float localValue17 = 0.5F / Math.max(1, ((SpriteAtlasTextureAccessor)(Object)internalField0515).rockstar$getWidth());
         float localValue19 = 0.5F / Math.max(1, ((SpriteAtlasTextureAccessor)(Object)internalField0515).rockstar$getHeight());
         localValue4 = Float.MAX_VALUE;
         localValue5 = -Float.MAX_VALUE;
         localValue6 = Float.MAX_VALUE;
         localValue7 = -Float.MAX_VALUE;

         for (int localValue10 = 0; localValue10 < localValue3; localValue10++) {
            float localValue11 = Vector2f.getX(localValue2[localValue10]);
            float localValue12 = Vector2f.getY(localValue2[localValue10]);
            localValue4 = Math.min(localValue4, localValue11);
            localValue5 = Math.max(localValue5, localValue11);
            localValue6 = Math.min(localValue6, localValue12);
            localValue7 = Math.max(localValue7, localValue12);
         }

         localValue4 += localValue17;
         localValue5 -= localValue17;
         localValue6 += localValue19;
         localValue7 -= localValue19;
      }

      if (localValue4 > localValue5) {
         localValue4 = localValue5 = (localValue4 + localValue5) * 0.5F;
      }

      if (localValue6 > localValue7) {
         localValue6 = localValue7 = (localValue6 + localValue7) * 0.5F;
      }

      for (int localValue18 = 0; localValue18 < localValue3; localValue18++) {
         float localValue20 = MathHelper.clamp(Vector2f.getX(localValue2[localValue18]), localValue4, localValue5);
         float localValue21 = MathHelper.clamp(Vector2f.getY(localValue2[localValue18]), localValue6, localValue7);
         localValue2[localValue18] = Vector2f.toLong(localValue20, localValue21);
      }

      return new BakedQuad(
         localValue0.position0(), localValue0.position1(), localValue0.position2(), localValue0.position3(),
         localValue2[0], localValue2[1], localValue2[2], localValue2[3],
         localValue0.tintIndex(), localValue0.face(), localValue1, localValue0.shade(), localValue0.lightEmission()
      );
   }

   private static void internalMethod04532() {
      SpriteAtlasTexture localValue0 = (SpriteAtlasTexture)MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
      if (localValue0 != internalField0515) {
         internalField0515 = localValue0;
         internalField0543.clear();
      }
   }

   public static final class InternalType0310 implements AutoCloseable {
      private final boolean internalField0277 = SmoothItemLayerHelper.internalField0276;

      InternalType0310() {
         SmoothItemLayerHelper.internalField0276 = true;
      }

      @Override
      public void close() {
         SmoothItemLayerHelper.internalField0276 = this.internalField0277;
      }
   }
}
