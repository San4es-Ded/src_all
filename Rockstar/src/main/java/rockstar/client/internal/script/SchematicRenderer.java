package rockstar.client.internal.script;


import rockstar.client.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.SchematicRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3fc;
import net.minecraft.world.EmptyBlockView;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public final class SchematicRenderer implements MinecraftClientAccess {
   private static final Map<String, SchematicRenderer> internalField0543 = new HashMap<>();
   private static final Direction[] internalField0470 = Direction.values();
   private static final float internalField0205 = 0.35F;
   private static final SchematicRenderer internalField0185 = new SchematicRenderer(new SchematicRenderer.InternalType0301[0], Set.of(), Vec3i.ZERO);
   private final SchematicRenderer.InternalType0301[] internalField0600;
   private final Set<Integer> internalField0546;
   private final Vec3i internalField0284;

   private SchematicRenderer(SchematicRenderer.InternalType0301[] localValue1, Set<Integer> localValue2, Vec3i localValue3) {
      this.internalField0600 = localValue1;
      this.internalField0546 = localValue2;
      this.internalField0284 = localValue3;
   }

   public static SchematicRenderer internalMethod04303(String localValue0) {
      return internalField0543.computeIfAbsent(localValue0, SchematicRenderer::internalMethod06109);
   }

   private static SchematicRenderer internalMethod06109(String localValue0) {
      try {
         SchematicRenderer localValue28;
         try (InputStream localValue1 = SchematicRenderer.class.getResourceAsStream("/assets/rockstar/schematics/" + localValue0 + ".schem")) {
            if (localValue1 == null) {
               return internalField0185;
            }

            NbtCompound localValue2 = NbtIo.readCompressed(localValue1, NbtSizeTracker.ofUnlimitedBytes()).getCompound("Schematic").orElseGet(net.minecraft.nbt.NbtCompound::new);
            int localValue3 = localValue2.getShort("Width").orElse((short)0) & '\uffff';
            int localValue4 = localValue2.getShort("Length").orElse((short)0) & '\uffff';
            int[] localValue5 = localValue2.getIntArray("Offset").orElseGet(() -> new int[0]);
            NbtCompound localValue6 = localValue2.getCompound("Blocks").orElseGet(net.minecraft.nbt.NbtCompound::new);
            NbtCompound localValue7 = localValue6.getCompound("Palette").orElseGet(net.minecraft.nbt.NbtCompound::new);
            BlockState[] localValue8 = new BlockState[localValue7.getKeys().size()];

            for (String localValue10 : localValue7.getKeys()) {
               localValue8[localValue7.getInt(localValue10).orElse(0)] = BlockArgumentParser.block(Registries.BLOCK, localValue10, false).blockState();
            }

            byte[] localValue25 = localValue6.getByteArray("Data").orElseGet(() -> new byte[0]);
            ArrayList<SchematicRenderer.InternalType0301> localValue26 = new ArrayList<>();
            HashSet localValue11 = new HashSet();
            int localValue12 = 0;

            for (int localValue13 = 0; localValue13 < localValue25.length; localValue12++) {
               int localValue14 = 0;
               byte localValue15 = 0;

               byte localValue16;
               do {
                  localValue16 = localValue25[localValue13++];
                  localValue14 |= (localValue16 & 127) << localValue15;
                  localValue15 += 7;
               } while ((localValue16 & 128) != 0);

               BlockState localValue17 = localValue14 < localValue8.length ? localValue8[localValue14] : null;
               if (localValue17 != null && !localValue17.isAir()) {
                  int localValue18 = localValue12 % localValue3;
                  int localValue19 = localValue12 / (localValue3 * localValue4);
                  int localValue20 = localValue12 / localValue3 % localValue4;
                  localValue26.add(new SchematicRenderer.InternalType0301(localValue18, localValue19, localValue20, localValue17));
                  if (Block.isShapeFullCube(localValue17.getCollisionShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN))) {
                     localValue11.add(internalMethod04141(localValue18, localValue19, localValue20));
                  }
               }
            }

            SchematicRenderer.InternalType0301[] localValue27 = localValue26.toArray(new SchematicRenderer.InternalType0301[0]);
            localValue28 = localValue5.length == 3
               ? new SchematicRenderer(localValue27, localValue11, new Vec3i(localValue5[0], localValue5[1], localValue5[2]))
               : new SchematicRenderer(localValue27, localValue11, Vec3i.ZERO);
         }

         return localValue28;
      } catch (Exception localValue23) {
         return internalField0185;
      }
   }

   public void internalMethod06730(Render3DEvent localValue1, BlockPos localValue2, ColorRGBA localValue3, float localValue4) {
      if (this.internalField0600.length != 0 && internalField0149.world != null) {
         Vec3d localValue5 = internalField0149.gameRenderer.getCamera().getCameraPos();
         int localValue6 = localValue2.getX() + this.internalField0284.getX();
         int localValue7 = localValue2.getY() + this.internalField0284.getY();
         int localValue8 = localValue2.getZ() + this.internalField0284.getZ();
         double localValue9 = localValue6 - localValue5.x;
         double localValue11 = localValue7 - localValue5.y;
         double localValue13 = localValue8 - localValue5.z;
         Arrays.sort(this.internalField0600, Comparator.comparingDouble(localValue6x -> {
            double localValue7x = localValue9 + localValue6x.internalMethod06023() + 0.5;
            double localValue9x = localValue11 + localValue6x.internalMethod06061() + 0.5;
            double localValue11x = localValue13 + localValue6x.internalMethod08450() + 0.5;
            return localValue7x * localValue7x + localValue9x * localValue9x + localValue11x * localValue11x;
         }));
         Immediate localValue15 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
         RenderLayer localValue16 = SchematicRenderLayers.hidden();
         this.internalMethod05806(localValue1, new SchematicRenderer.InternalType0302(localValue15.getBuffer(localValue16), localValue3, 0.35F), true, localValue6, localValue7, localValue8, localValue9, localValue11, localValue13);
         localValue15.draw(localValue16);
         RenderLayer localValue17 = SchematicRenderLayers.visible();
         this.internalMethod05806(localValue1, new SchematicRenderer.InternalType0302(localValue15.getBuffer(localValue17), localValue3, localValue4), false, localValue6, localValue7, localValue8, localValue9, localValue11, localValue13);
         localValue15.draw(localValue17);
      }
   }

   private void internalMethod05806(Render3DEvent localValue1, VertexConsumer localValue2, boolean localValue3, int localValue4, int localValue5, int localValue6, double localValue7, double localValue9, double localValue11) {
      MatrixStack localValue13 = localValue1.getMatrices();
      Mutable localValue14 = new Mutable();
      Random localValue15 = Random.create();

      for (SchematicRenderer.InternalType0301 localValue19 : this.internalField0600) {
         localValue14.set(localValue4 + localValue19.internalMethod06023(), localValue5 + localValue19.internalMethod06061(), localValue6 + localValue19.internalMethod08450());
         localValue13.push();
         localValue13.translate(localValue7 + localValue19.internalMethod06023(), localValue9 + localValue19.internalMethod06061(), localValue11 + localValue19.internalMethod08450());
         this.internalMethod05138(
            localValue19,
            localValue13.peek(),
            localValue2,
            localValue14,
            localValue15,
            localValue3,
            localValue7 + localValue19.internalMethod06023() + 0.5,
            localValue9 + localValue19.internalMethod06061() + 0.5,
            localValue11 + localValue19.internalMethod08450() + 0.5
         );
         localValue13.pop();
      }
   }

   private void internalMethod05138(
      SchematicRenderer.InternalType0301 localValue1,
      Entry localValue2,
      VertexConsumer localValue3,
      Mutable localValue4,
      Random localValue5,
      boolean localValue6,
      double localValue7,
      double localValue9,
      double localValue11
   ) {
      BlockState localValue13 = localValue1.internalMethod03922();
      if (localValue13.getRenderType() == BlockRenderType.MODEL) {
         BlockStateModel localValue14 = internalField0149.getBlockRenderManager().getModel(localValue13);
         localValue5.setSeed(42L);
         List<BlockModelPart> localValue20 = localValue14.getParts(localValue5);
         int localValue15 = internalField0149.getBlockColors().getColor(localValue13, internalField0149.world, localValue4, 0);
         float localValue16 = (localValue15 >> 16 & 0xFF) / 255.0F;
         float localValue17 = (localValue15 >> 8 & 0xFF) / 255.0F;
         float localValue18 = (localValue15 & 0xFF) / 255.0F;

         for (Direction localValue22 : internalField0470) {
            if (!this.internalMethod06292(localValue1, localValue22, localValue4, localValue6, localValue7, localValue9, localValue11)) {
               for (BlockModelPart localValue21 : localValue20) {
                  this.internalMethod04625(localValue2, localValue3, localValue21.getQuads(localValue22), localValue16, localValue17, localValue18, false, localValue7, localValue9, localValue11);
               }
            }
         }

         for (BlockModelPart localValue21 : localValue20) {
            this.internalMethod04625(localValue2, localValue3, localValue21.getQuads(null), localValue16, localValue17, localValue18, true, localValue7, localValue9, localValue11);
         }
      }
   }

   private boolean internalMethod06292(SchematicRenderer.InternalType0301 localValue1, Direction localValue2, Mutable localValue3, boolean localValue4, double localValue5, double localValue7, double localValue9) {
      Vec3i localValue11 = localValue2.getVector();
      if (localValue11.getX() * (localValue5 + localValue11.getX() * 0.5) + localValue11.getY() * (localValue7 + localValue11.getY() * 0.5) + localValue11.getZ() * (localValue9 + localValue11.getZ() * 0.5) >= 0.0) {
         return true;
      } else if (this.internalField0546
         .contains(internalMethod04141(localValue1.internalMethod06023() + localValue11.getX(), localValue1.internalMethod06061() + localValue11.getY(), localValue1.internalMethod08450() + localValue11.getZ()))
         )
       {
         return true;
      } else if (localValue4) {
         return false;
      } else {
         localValue3.move(localValue2);
         boolean localValue12 = internalField0149.world.getBlockState(localValue3).isOpaqueFullCube();
         localValue3.move(localValue2.getOpposite());
         return localValue12;
      }
   }

   private void internalMethod04625(
      Entry localValue1, VertexConsumer localValue2, List<BakedQuad> localValue3, float localValue4, float localValue5, float localValue6, boolean localValue7, double localValue8, double localValue10, double localValue12
   ) {
      for (BakedQuad localValue15 : localValue3) {
         if (!localValue7 || !this.internalMethod04000(localValue15, localValue8, localValue10, localValue12)) {
            boolean localValue16 = localValue15.hasTint();
            localValue2.quad(localValue1, localValue15, localValue16 ? localValue4 : 1.0F, localValue16 ? localValue5 : 1.0F, localValue16 ? localValue6 : 1.0F, 1.0F, 15728880, OverlayTexture.DEFAULT_UV);
         }
      }
   }

   private boolean internalMethod04000(BakedQuad localValue1, double localValue2, double localValue4, double localValue6) {
      Vector3fc localValue8 = localValue1.position0();
      Vector3fc localValue9 = localValue1.position1();
      Vector3fc localValue13x = localValue1.position2();
      Vector3fc localValue16x = localValue1.position3();
      float localValue10 = localValue8.x();
      float localValue11 = localValue8.y();
      float localValue12 = localValue8.z();
      float localValue13 = localValue9.x();
      float localValue14 = localValue9.y();
      float localValue15 = localValue9.z();
      float localValue16 = localValue13x.x();
      float localValue17 = localValue13x.y();
      float localValue18 = localValue13x.z();
      float localValue19 = localValue16x.x();
      float localValue20 = localValue16x.y();
      float localValue21 = localValue16x.z();
      float localValue22 = localValue13 - localValue10;
      float localValue23 = localValue14 - localValue11;
      float localValue24 = localValue15 - localValue12;
      float localValue25 = localValue16 - localValue10;
      float localValue26 = localValue17 - localValue11;
      float localValue27 = localValue18 - localValue12;
      double localValue28 = localValue23 * localValue27 - localValue24 * localValue26;
      double localValue30 = localValue24 * localValue25 - localValue22 * localValue27;
      double localValue32 = localValue22 * localValue26 - localValue23 * localValue25;
      return localValue28 == 0.0 && localValue30 == 0.0 && localValue32 == 0.0
         ? false
         : localValue28 * (localValue2 - 0.5 + (localValue10 + localValue13 + localValue16 + localValue19) / 4.0)
               + localValue30 * (localValue4 - 0.5 + (localValue11 + localValue14 + localValue17 + localValue20) / 4.0)
               + localValue32 * (localValue6 - 0.5 + (localValue12 + localValue15 + localValue18 + localValue21) / 4.0)
            >= 0.0;
   }

   private static int internalMethod04141(int localValue0, int localValue1, int localValue2) {
      return ((localValue0 + 1) * 512 + localValue1 + 1) * 512 + localValue2 + 1;
   }

   static final class InternalType0301 {
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;
      private final BlockState internalField0934;

      InternalType0301(int localValue1, int localValue2, int localValue3, BlockState localValue4) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField1053 = localValue3;
         this.internalField0934 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0301[x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + ", state=" + this.internalField0934 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0934);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         SchematicRenderer.InternalType0301 other = (SchematicRenderer.InternalType0301) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField0934, other.internalField0934);
      }

      public int internalMethod06023() {
         return this.internalField0227;
      }

      public int internalMethod06061() {
         return this.internalField0228;
      }

      public int internalMethod08450() {
         return this.internalField1053;
      }

      public BlockState internalMethod03922() {
         return this.internalField0934;
      }
   }

   static final class InternalType0302 implements VertexConsumer {
      private final VertexConsumer internalField0261;
      private final ColorRGBA internalField0777;
      private final float internalField0205;

      InternalType0302(VertexConsumer localValue1, ColorRGBA localValue2, float localValue3) {
         this.internalField0261 = localValue1;
         this.internalField0777 = localValue2;
         this.internalField0205 = localValue3;
      }

      public VertexConsumer vertex(float x, float y, float z) {
         this.internalField0261.vertex(x, y, z);
         return this;
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         this.internalField0261
            .color(
               (int)(red * this.internalField0777.getRed() / 255.0F),
               (int)(green * this.internalField0777.getGreen() / 255.0F),
               (int)(blue * this.internalField0777.getBlue() / 255.0F),
               (int)(alpha * this.internalField0205)
            );
         return this;
      }

      public VertexConsumer color(int argb) {
         return this.color(argb >> 16 & 255, argb >> 8 & 255, argb & 255, argb >>> 24);
      }

      public VertexConsumer texture(float u, float v) {
         this.internalField0261.texture(u, v);
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         this.internalField0261.overlay(u, v);
         return this;
      }

      public VertexConsumer light(int u, int v) {
         this.internalField0261.light(u, v);
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         this.internalField0261.normal(x, y, z);
         return this;
      }

      public VertexConsumer lineWidth(float width) {
         this.internalField0261.lineWidth(width);
         return this;
      }

      @Override
      public final String toString() {
         return "InternalType0302[delegate=" + this.internalField0261 + ", tint=" + this.internalField0777 + ", alpha=" + this.internalField0205 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0261);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0777);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         SchematicRenderer.InternalType0302 other = (SchematicRenderer.InternalType0302) localValue1;
         return java.util.Objects.equals(this.internalField0261, other.internalField0261)
            && java.util.Objects.equals(this.internalField0777, other.internalField0777)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205);
      }

      public VertexConsumer internalMethod03208() {
         return this.internalField0261;
      }

      public ColorRGBA internalMethod01503() {
         return this.internalField0777;
      }

      public float internalMethod01742() {
         return this.internalField0205;
      }
   }
}
