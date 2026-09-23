package rockstar.client.internal.script;





import rockstar.client.rotation.*;
import rockstar.client.animation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BlockGhostLayers;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.SchematicRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.Nullable;
import pyrock.events.render.Render3DEvent;

public final class GhostBlockRenderer implements MinecraftClientAccess {
   private static final float internalField0205 = 0.25F;
   private static final long internalField0229 = 220L;
   private static final int internalField0227 = 256;
   private static final Long2ObjectOpenHashMap<GhostBlockRenderer.InternalType0275> internalField0461 = new Long2ObjectOpenHashMap();
   private static volatile LongOpenHashSet internalField0933 = new LongOpenHashSet();
   private static volatile int internalField1053 = 15728880;
   private static volatile int internalField1055 = 15;
   private static volatile int internalField1056 = 15;
   private static final GhostBlockRenderer.InternalType0056 internalField0302 = new GhostBlockRenderer.InternalType0056();
   private static final GhostBlockRenderer.InternalType0276 internalField0409 = new GhostBlockRenderer.InternalType0276();

   private GhostBlockRenderer() {
   }

   public static boolean internalMethod05216() {
      return !internalField0933.isEmpty();
   }

   public static boolean internalMethod01361(int localValue0, int localValue1, int localValue2) {
      LongOpenHashSet localValue3 = internalField0933;
      return !localValue3.isEmpty() && localValue3.contains(BlockPos.asLong(localValue0, localValue1, localValue2));
   }

   public static boolean internalMethod00945(BlockPos localValue0) {
      return internalMethod01361(localValue0.getX(), localValue0.getY(), localValue0.getZ());
   }

   public static int internalMethod01806(BlockPos localValue0, int localValue1) {
      return internalMethod00945(localValue0) ? Math.max(localValue1, internalField1053) : localValue1;
   }

   public static int internalMethod05834(LightType localValue0, int localValue1) {
      return Math.max(localValue1, localValue0 == LightType.SKY ? internalField1056 : internalField1055);
   }

   public static void internalMethod05215() {
      internalField0461.clear();
      if (!internalField0933.isEmpty()) {
         internalMethod06318(new LongOpenHashSet());
      }
   }

   public static void internalMethod04614(float localValue0, boolean localValue1) {
      if (internalField0149.player != null && internalField0149.world != null) {
         boolean localValue2 = localValue1 && !internalField0149.options.getPerspective().isFirstPerson();
         LongOpenHashSet localValue3 = localValue2 ? internalMethod01645(localValue0) : new LongOpenHashSet();
         LongIterator localValue4 = localValue3.iterator();

         while (localValue4.hasNext()) {
            long localValue5 = (Long)localValue4.next();
            internalField0461.computeIfAbsent(localValue5, localValue0x -> new GhostBlockRenderer.InternalType0275());
         }

         LongOpenHashSet localValue9 = new LongOpenHashSet();
         ObjectIterator localValue10 = internalField0461.long2ObjectEntrySet().fastIterator();

         while (localValue10.hasNext()) {
            Entry localValue6 = (Entry)localValue10.next();
            GhostBlockRenderer.InternalType0275 localValue7 = (GhostBlockRenderer.InternalType0275)localValue6.getValue();
            boolean localValue8 = localValue3.contains(localValue6.getLongKey());
            if (localValue7.internalField0277) {
               if (!localValue8) {
                  if (--localValue7.internalField0227 <= 0) {
                     localValue10.remove();
                  }
                  continue;
               }

               localValue7.internalField0277 = false;
               localValue7.internalField0227 = 3;
            }

            if (localValue7.internalField0227 > 0) {
               localValue7.internalField0227--;
            } else if (localValue8) {
               localValue7.internalField0808.internalMethod07062(true);
            } else if (localValue7.internalField0808.internalMethod07059(0.0F) <= 0.001F) {
               localValue7.internalField0277 = true;
               localValue7.internalField0227 = 3;
               continue;
            }

            localValue9.add(localValue6.getLongKey());
         }

         if (!localValue9.equals(internalField0933)) {
            internalMethod06318(localValue9);
         }
      } else {
         internalMethod05215();
      }
   }

   private static LongOpenHashSet internalMethod01645(float localValue0) {
      Vec3d localValue1 = internalField0149.gameRenderer.getCamera().getCameraPos();
      Vec3d localValue2 = ScreenProjectionUtils.internalMethod02822(internalField0149.player, localValue0);
      Box localValue3 = internalField0149.player.getBoundingBox().offset(localValue2.subtract(internalField0149.player.getEntityPos())).expand(0.05);
      BlockPos localValue4 = BlockPos.ofFloored(localValue2.add(0.0, internalField0149.player.getStandingEyeHeight(), 0.0));
      BlockPos localValue5 = BlockPos.ofFloored(localValue1);
      internalField1055 = Math.max(internalField0149.world.getLightLevel(LightType.BLOCK, localValue4), internalField0149.world.getLightLevel(LightType.BLOCK, localValue5));
      internalField1056 = Math.max(internalField0149.world.getLightLevel(LightType.SKY, localValue4), internalField0149.world.getLightLevel(LightType.SKY, localValue5));
      internalField1053 = LightmapTextureManager.pack(internalField1055, internalField1056);
      List localValue6 = internalMethod01062(localValue3);
      if (localValue6.isEmpty()) {
         return new LongOpenHashSet();
      } else {
         LongOpenHashSet localValue7 = new LongOpenHashSet();
         Mutable localValue8 = new Mutable();
         int localValue9 = (int)Math.ceil(2.4) + 1;
         int localValue10 = (int)Math.floor(Math.min(localValue1.x, localValue3.minX)) - localValue9;
         int localValue11 = (int)Math.floor(Math.min(localValue1.y, localValue3.minY)) - localValue9;
         int localValue12 = (int)Math.floor(Math.min(localValue1.z, localValue3.minZ)) - localValue9;
         int localValue13 = (int)Math.floor(Math.max(localValue1.x, localValue3.maxX)) + localValue9;
         int localValue14 = (int)Math.floor(Math.max(localValue1.y, localValue3.maxY)) + localValue9;
         int localValue15 = (int)Math.floor(Math.max(localValue1.z, localValue3.maxZ)) + localValue9;
         boolean localValue16 = localValue1.y > localValue3.minY;

         for (int localValue17 = localValue10; localValue17 <= localValue13 && localValue7.size() < 256; localValue17++) {
            for (int localValue18 = localValue11; localValue18 <= localValue14 && localValue7.size() < 256; localValue18++) {
               if (!localValue16 || !(localValue18 + 1 <= localValue3.minY + 0.02)) {
                  for (int localValue19 = localValue12; localValue19 <= localValue15 && localValue7.size() < 256; localValue19++) {
                     if (internalMethod01614(localValue17, localValue18, localValue19, localValue1, localValue6)) {
                        localValue8.set(localValue17, localValue18, localValue19);
                        if (internalMethod00253(internalField0149.world.getBlockState(localValue8))) {
                           localValue7.add(localValue8.asLong());
                        }
                     }
                  }
               }
            }
         }

         return localValue7;
      }
   }

   private static boolean internalMethod00253(BlockState localValue0) {
      return localValue0.getRenderType() == BlockRenderType.MODEL;
   }

   private static List<Vec3d> internalMethod01062(Box localValue0) {
      Vec3d localValue1 = localValue0.getCenter();
      return List.of(new Vec3d(localValue1.x, localValue0.minY + 0.3, localValue1.z), localValue1, new Vec3d(localValue1.x, localValue0.maxY, localValue1.z));
   }

   private static boolean internalMethod01614(int localValue0, int localValue1, int localValue2, Vec3d localValue3, List<Vec3d> localValue4) {
      double localValue5 = localValue0 + 0.5;
      double localValue7 = localValue1 + 0.5;
      double localValue9 = localValue2 + 0.5;

      for (Vec3d localValue12 : localValue4) {
         double localValue13 = localValue12.x - localValue3.x;
         double localValue15 = localValue12.y - localValue3.y;
         double localValue17 = localValue12.z - localValue3.z;
         double localValue19 = localValue13 * localValue13 + localValue15 * localValue15 + localValue17 * localValue17;
         if (!(localValue19 < 0.01)) {
            double localValue21 = Math.clamp(((localValue5 - localValue3.x) * localValue13 + (localValue7 - localValue3.y) * localValue15 + (localValue9 - localValue3.z) * localValue17) / localValue19, 0.0, 1.0);
            double localValue23 = localValue3.x + localValue13 * localValue21 - localValue5;
            double localValue25 = localValue3.y + localValue15 * localValue21 - localValue7;
            double localValue27 = localValue3.z + localValue17 * localValue21 - localValue9;
            double localValue29 = 2.4 + -1.7999999999999998 * localValue21;
            if (localValue23 * localValue23 + localValue25 * localValue25 + localValue27 * localValue27 <= localValue29 * localValue29) {
               return true;
            }
         }
      }

      return false;
   }

   private static void internalMethod06318(LongOpenHashSet localValue0) {
      LongOpenHashSet localValue1 = internalField0933;
      internalField0933 = localValue0;
      if (internalField0149.worldRenderer != null && internalField0149.world != null) {
         for (LongOpenHashSet localValue3 : List.of(localValue1, localValue0)) {
            LongIterator localValue4 = localValue3.iterator();

            while (localValue4.hasNext()) {
               long localValue5 = (Long)localValue4.next();
               if (!localValue1.contains(localValue5) || !localValue0.contains(localValue5)) {
                  BlockPos localValue7 = BlockPos.fromLong(localValue5);
                  BlockState localValue8 = internalField0149.world.getBlockState(localValue7);
                  internalField0149.worldRenderer.updateBlock(internalField0149.world, localValue7, localValue8, localValue8, 3);
               }
            }
         }
      }
   }

   public static void internalMethod04333(Render3DEvent localValue0) {
      if (!internalField0461.isEmpty() && internalField0149.world != null) {
         Vec3d localValue1 = internalField0149.gameRenderer.getCamera().getCameraPos();
         Immediate localValue2 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
         RenderLayer localValue3 = BlockGhostLayers.ready() ? BlockGhostLayers.ghost() : SchematicRenderLayers.visible();
         internalField0409.internalField0261 = localValue2.getBuffer(localValue3);
         MatrixStack localValue4 = localValue0.getMatrices();
         Random localValue5 = Random.create();
         ObjectIterator localValue6 = internalField0461.long2ObjectEntrySet().fastIterator();

         while (localValue6.hasNext()) {
            Entry localValue7 = (Entry)localValue6.next();
            BlockPos localValue8 = BlockPos.fromLong(localValue7.getLongKey());
            BlockState localValue9 = internalField0149.world.getBlockState(localValue8);
            if (localValue9.getRenderType() == BlockRenderType.MODEL) {
               double localValue10 = localValue8.getX() + 0.5 - localValue1.x;
               double localValue12 = localValue8.getY() + 0.5 - localValue1.y;
               double localValue14 = localValue8.getZ() + 0.5 - localValue1.z;
               double localValue16 = Math.sqrt(localValue10 * localValue10 + localValue12 * localValue12 + localValue14 * localValue14);
               float localValue18 = (float)Math.clamp((localValue16 - 1.5) / 1.0, 0.0, 1.0);
               float localValue19 = 1.0F - (1.0F - 0.25F * localValue18) * ((GhostBlockRenderer.InternalType0275)localValue7.getValue()).internalField0808.internalMethod02881();
               if (!(localValue19 <= 0.004F)) {
                  internalField0409.internalField0205 = localValue19;
                  internalField0302.internalField0229 = localValue7.getLongKey();
                  localValue4.push();
                  localValue4.translate(localValue8.getX() - localValue1.x, localValue8.getY() - localValue1.y, localValue8.getZ() - localValue1.z);
                  BlockStateModel localValue20 = internalField0149.getBlockRenderManager().getModel(localValue9);
                  ArrayList<BlockModelPart> localValue21 = new ArrayList<>();
                  localValue5.setSeed(localValue9.getRenderingSeed(localValue8));
                  localValue20.addParts(localValue5, localValue21);
                  internalField0149.getBlockRenderManager().renderBlock(localValue9, localValue8, internalField0302, localValue4, internalField0409, true, localValue21);
                  localValue4.pop();
               }
            }
         }

         internalField0409.internalField0261 = null;
         localValue2.draw(localValue3);
      }
   }

   static final class InternalType0056 implements BlockRenderView {
      long internalField0229;

      public BlockState getBlockState(BlockPos pos) {
         return pos.asLong() != this.internalField0229 && GhostBlockRenderer.internalMethod00945(pos)
            ? Blocks.AIR.getDefaultState()
            : MinecraftClientAccess.internalField0149.world.getBlockState(pos);
      }

      public FluidState getFluidState(BlockPos pos) {
         return this.getBlockState(pos).getFluidState();
      }

      @Nullable
      public BlockEntity getBlockEntity(BlockPos pos) {
         return MinecraftClientAccess.internalField0149.world.getBlockEntity(pos);
      }

      public float getBrightness(Direction direction, boolean shaded) {
         return MinecraftClientAccess.internalField0149.world.getBrightness(direction, shaded);
      }

      public LightingProvider getLightingProvider() {
         return MinecraftClientAccess.internalField0149.world.getLightingProvider();
      }

      public int getColor(BlockPos pos, ColorResolver colorResolver) {
         return MinecraftClientAccess.internalField0149.world.getColor(pos, colorResolver);
      }

      public int getHeight() {
         return MinecraftClientAccess.internalField0149.world.getHeight();
      }

      public int getBottomY() {
         return MinecraftClientAccess.internalField0149.world.getBottomY();
      }
   }

   static final class InternalType0275 {
      final AnimatedValue internalField0808 = new AnimatedValue(220L, 0.0F, Easing.internalField1627);
      int internalField0227 = 3;
      boolean internalField0277;
   }

   static final class InternalType0276 implements VertexConsumer {
      VertexConsumer internalField0261;
      float internalField0205 = 1.0F;

      public VertexConsumer vertex(float x, float y, float z) {
         return this.internalField0261.vertex(x, y, z);
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         return this.internalField0261.color(red, green, blue, (int)(alpha * this.internalField0205));
      }

      public VertexConsumer color(int argb) {
         return this.internalField0261.color(ColorHelper.withAlpha((int)(ColorHelper.getAlpha(argb) * this.internalField0205), argb));
      }

      public VertexConsumer texture(float u, float v) {
         return this.internalField0261.texture(u, v);
      }

      public VertexConsumer overlay(int u, int v) {
         return this.internalField0261.overlay(u, v);
      }

      public VertexConsumer light(int u, int v) {
         return this.internalField0261.light(u, v);
      }

      public VertexConsumer normal(float x, float y, float z) {
         return this.internalField0261.normal(x, y, z);
      }

      public VertexConsumer lineWidth(float width) {
         return this.internalField0261.lineWidth(width);
      }

      public void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
         int localValue12 = ColorHelper.withAlpha((int)(ColorHelper.getAlpha(color) * this.internalField0205), color);
         this.internalField0261.vertex(x, y, z, localValue12, u, v, overlay, light, normalX, normalY, normalZ);
      }
   }
}
