package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class BlockScanEffectRenderer implements MinecraftClientAccess {
   public static BlockScanEffectRenderer internalField0186 = new BlockScanEffectRenderer();
   private final List<BlockScanEffectRenderer.InternalType0313> internalField0416 = new CopyOnWriteArrayList<>();
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (internalField0149.world != null && internalField0149.player != null && !this.internalField0416.isEmpty()) {
         MatrixStack localValue2 = localValue1.getMatrices();
         Vec3d localValue3 = internalField0149.gameRenderer.getCamera().getCameraPos();
         localValue2.push();
         localValue2.translate(-localValue3.x, -localValue3.y, -localValue3.z);
         RenderSystem.enableBlend();
         RenderSystem.enableDepthTest();
         RenderSystem.depthFunc(515);
         RenderSystem.depthMask(false);
         RenderSystem.disableCull();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue4 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         for (BlockScanEffectRenderer.InternalType0313 localValue6 : this.internalField0416) {
            float localValue7 = localValue6.internalMethod06024();
            float localValue8 = localValue6.internalMethod06027();
            float localValue9 = localValue6.internalField0206;
            float localValue10 = localValue7 - localValue9;
            float localValue11 = localValue7 + localValue9;
            ColorRGBA localValue12 = localValue6.internalField0777;

            for (BlockScanEffectRenderer.InternalType0312 localValue14 : localValue6.internalField0416) {
               if (localValue14.internalField0194 >= localValue10 && localValue14.internalField0194 <= localValue11) {
                  float localValue15 = (float)Math.pow(Math.max(0.0F, Math.min(1.0F, 1.0F - Math.abs((float)(localValue14.internalField0194 - localValue7)) / localValue9)), 0.5);
                  float localValue16 = localValue8 * localValue15 * 0.15F;
                  if (localValue16 > 0.005F) {
                     Render3DUtils.internalMethod02535(localValue2, localValue4, localValue14.internalField0681, localValue12.withAlpha(localValue16 * 255.0F));
                  }
               }
            }
         }

         BuiltBuffer localValue19 = localValue4.endNullable();
         if (localValue19 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue19);
         }

         BufferBuilder localValue20 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (BlockScanEffectRenderer.InternalType0313 localValue23 : this.internalField0416) {
            float localValue24 = localValue23.internalMethod06024();
            float localValue25 = localValue23.internalMethod06027();
            float localValue26 = localValue23.internalField0206;
            float localValue27 = localValue24 - localValue26;
            float localValue28 = localValue24 + localValue26;
            ColorRGBA localValue29 = localValue23.internalField0777;

            for (BlockScanEffectRenderer.InternalType0312 localValue31 : localValue23.internalField0416) {
               if (localValue31.internalField0194 >= localValue27 && localValue31.internalField0194 <= localValue28) {
                  float localValue17 = (float)Math.pow(Math.max(0.0F, Math.min(1.0F, 1.0F - Math.abs((float)(localValue31.internalField0194 - localValue24)) / localValue26)), 0.5);
                  float localValue18 = localValue25 * localValue17 * 0.5F;
                  if (localValue18 > 0.005F) {
                     Render3DUtils.internalMethod08795(localValue2, localValue20, localValue31.internalField0681, localValue29.withAlpha(localValue18 * 255.0F));
                  }
               }
            }
         }

         BuiltBuffer localValue22 = localValue20.endNullable();
         if (localValue22 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue22);
         }

         RenderSystem.defaultBlendFunc();
         RenderSystem.depthMask(true);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         localValue2.pop();
         this.internalField0416.removeIf(BlockScanEffectRenderer.InternalType0313::internalMethod06025);
      }
   };
   private final EventListener<WorldChangeEvent> internalField0158 = localValue1 -> this.internalField0416.clear();

   private BlockScanEffectRenderer() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public void internalMethod07398(Vec3d localValue1, float localValue2, float localValue3, float localValue4) {
      this.internalMethod04515(localValue1, localValue2, localValue3, localValue4, ThemeColors.internalMethod02531());
   }

   public void internalMethod04515(Vec3d localValue1, float localValue2, float localValue3, float localValue4, ColorRGBA localValue5) {
      if (internalField0149.world != null) {
         BlockPos localValue6 = BlockPos.ofFloored(localValue1);
         int localValue7 = (int)Math.ceil(localValue2);
         HashSet localValue8 = new HashSet(localValue7 * localValue7 * localValue7 / 2);

         for (int localValue9 = -localValue7; localValue9 <= localValue7; localValue9++) {
            for (int localValue10 = -localValue7; localValue10 <= localValue7; localValue10++) {
               for (int localValue11 = -localValue7; localValue11 <= localValue7; localValue11++) {
                  BlockPos localValue12 = localValue6.add(localValue9, localValue10, localValue11);
                  if (internalField0149.world.getBlockState(localValue12).getBlock() != Blocks.AIR
                     && !(internalField0149.world.getBlockState(localValue12).getBlock() instanceof FlowerBlock)
                     && internalField0149.world.getBlockState(localValue12).getBlock() != Blocks.SHORT_GRASS
                     && internalField0149.world.getBlockState(localValue12).getBlock() != Blocks.TALL_GRASS) {
                     localValue8.add(localValue12);
                  }
               }
            }
         }

         ArrayList localValue17 = new ArrayList(localValue8.size() / 3);

         for (BlockPos localValue20 : (Iterable<BlockPos>)(Iterable<?>)localValue8) {
            boolean localValue22 = false;

            for (Direction localValue16 : Direction.values()) {
               if (!localValue8.contains(localValue20.offset(localValue16))) {
                  localValue22 = true;
                  break;
               }
            }

            if (localValue22) {
               double localValue24 = localValue1.distanceTo(localValue20.toCenterPos());
               localValue17.add(new BlockScanEffectRenderer.InternalType0312(new Box(localValue20).expand(0.002), localValue24));
            }
         }

         if (localValue17.isEmpty()) {
            for (BlockPos localValue21 : (Iterable<BlockPos>)(Iterable<?>)localValue8) {
               double localValue23 = localValue1.distanceTo(localValue21.toCenterPos());
               localValue17.add(new BlockScanEffectRenderer.InternalType0312(new Box(localValue21).expand(0.002), localValue23));
            }
         }

         this.internalField0416.add(new BlockScanEffectRenderer.InternalType0313(localValue17, (long)(localValue3 * 1000.0F), localValue2, localValue4, localValue5));
      }
   }

   @Generated
   public List<BlockScanEffectRenderer.InternalType0313> internalMethod07120() {
      return this.internalField0416;
   }

   @Generated
   public EventListener<Render3DEvent> internalMethod07522() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<WorldChangeEvent> internalMethod00766() {
      return this.internalField0158;
   }

   static final class InternalType0312 {
      final Box internalField0681;
      final double internalField0194;

      InternalType0312(Box localValue1, double localValue2) {
         this.internalField0681 = localValue1;
         this.internalField0194 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0312[box=" + this.internalField0681 + ", distance=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0681);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BlockScanEffectRenderer.InternalType0312 other = (BlockScanEffectRenderer.InternalType0312) localValue1;
         return java.util.Objects.equals(this.internalField0681, other.internalField0681)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public Box internalMethod03790() {
         return this.internalField0681;
      }

      public double internalMethod04319() {
         return this.internalField0194;
      }
   }

   static class InternalType0313 {
      final List<BlockScanEffectRenderer.InternalType0312> internalField0416;
      private final long internalField0229;
      private final float internalField0205;
      final float internalField0206;
      final ColorRGBA internalField0777;
      private final Stopwatch internalField0519;

      public InternalType0313(List<BlockScanEffectRenderer.InternalType0312> localValue1, long localValue2, float localValue4, float localValue5, ColorRGBA localValue6) {
         this.internalField0416 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0205 = localValue4;
         this.internalField0206 = localValue5;
         this.internalField0777 = localValue6;
         this.internalField0519 = new Stopwatch();
      }

      public boolean internalMethod06025() {
         return this.internalField0519.internalMethod00700() >= this.internalField0229;
      }

      public float internalMethod06024() {
         float localValue1 = Math.min(1.0F, (float)this.internalField0519.internalMethod00700() / (float)this.internalField0229);
         return localValue1 * this.internalField0205;
      }

      public float internalMethod06027() {
         float localValue1 = Math.min(1.0F, (float)this.internalField0519.internalMethod00700() / (float)this.internalField0229);
         return (float)Math.pow(1.0F - localValue1, 2.5);
      }
   }
}
