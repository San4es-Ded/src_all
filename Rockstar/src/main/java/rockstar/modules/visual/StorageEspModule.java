package rockstar.modules.visual;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Storage ESP",
   category = ModuleCategory.VISUALS
)
public class StorageEspModule extends Module {
   private static final Box internalField0681 = new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   private static final Box internalField0682 = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   private final Stopwatch internalField0519 = new Stopwatch();
   private volatile List<StorageEspModule.InternalType0513> internalField0416 = new ArrayList<>();
   private volatile List<StorageEspModule.InternalType0514> internalField0417 = new ArrayList<>();
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private MultiSelectSetting.InternalType0091 internalField1072;
   private MultiSelectSetting.InternalType0091 internalField1491;
   private MultiSelectSetting.InternalType0091 internalField1488;
   private MultiSelectSetting.InternalType0091 internalField1490;
   private MultiSelectSetting.InternalType0091 internalField1489;
   private MultiSelectSetting internalField0674;
   private MultiSelectSetting.InternalType0091 internalField1493;
   private MultiSelectSetting.InternalType0091 internalField1487;
   private MultiSelectSetting.InternalType0091 internalField1486;
   private MultiSelectSetting.InternalType0091 internalField1492;
   private SliderSetting internalField0383;
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (internalField0149.world != null && internalField0149.player != null) {
         MatrixStack localValue2 = localValue1.getMatrices();
         Camera localValue3 = internalField0149.gameRenderer.getCamera();
         Vec3d localValue4 = localValue3.getCameraPos();
         List localValue5 = this.internalField0416;
         List localValue6 = this.internalField0417;
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         for (StorageEspModule.InternalType0513 localValue9 : (Iterable<StorageEspModule.InternalType0513>)(Iterable<?>)localValue5) {
            for (Box localValue11 : localValue9.internalField0416) {
               if (this.internalField1493.isSelected()) {
                  Render3DUtils.internalMethod02535(
                     localValue2, localValue7, localValue11.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue9.internalField0777.withAlpha(50.0F)
                  );
               }
            }
         }

         for (StorageEspModule.InternalType0514 localValue15 : (Iterable<StorageEspModule.InternalType0514>)(Iterable<?>)localValue6) {
            if (this.internalField1493.isSelected()) {
               Render3DUtils.internalMethod02535(
                  localValue2, localValue7, localValue15.internalField0681.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue15.internalField0777.withAlpha(50.0F)
               );
            }
         }

         HudRenderUtils.internalMethod05816(localValue7);
         BufferBuilder localValue14 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (StorageEspModule.InternalType0513 localValue18 : (Iterable<StorageEspModule.InternalType0513>)(Iterable<?>)localValue5) {
            for (Box localValue12 : localValue18.internalField0416) {
               if (this.internalField1486.isSelected()) {
                  Render3DUtils.internalMethod09146(
                     localValue2, localValue14, localValue12.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue18.internalField0777.withAlpha(100.0F)
                  );
               }

               if (this.internalField1487.isSelected()) {
                  Render3DUtils.internalMethod08795(
                     localValue2, localValue14, localValue12.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue18.internalField0777.withAlpha(100.0F)
                  );
               }

               if (this.internalField1492.isSelected()) {
                  Render3DUtils.internalMethod01606(localValue2, localValue14, localValue18.internalField0283, localValue18.internalField0777);
               }
            }
         }

         for (StorageEspModule.InternalType0514 localValue19 : (Iterable<StorageEspModule.InternalType0514>)(Iterable<?>)localValue6) {
            if (this.internalField1486.isSelected()) {
               Render3DUtils.internalMethod09146(
                  localValue2, localValue14, localValue19.internalField0681.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue19.internalField0777.withAlpha(100.0F)
               );
            }

            if (this.internalField1487.isSelected()) {
               Render3DUtils.internalMethod08795(
                  localValue2, localValue14, localValue19.internalField0681.offset(-localValue4.getX(), -localValue4.getY(), -localValue4.getZ()), localValue19.internalField0777.withAlpha(100.0F)
               );
            }

            if (this.internalField1492.isSelected()) {
               Render3DUtils.internalMethod01606(localValue2, localValue14, localValue19.internalField0283, localValue19.internalField0777);
            }
         }

         HudRenderUtils.internalMethod05816(localValue14);
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   };
   private final EventListener<WorldChangeEvent> internalField0158 = localValue1 -> {
      BlockEntityCache.internalMethod04034();
      this.internalField0416 = new ArrayList<>();
      this.internalField0417 = new ArrayList<>();
   };
   private final EventListener<ClientPlayerTickEvent> internalField1028 = localValue1 -> {
      if (internalField0149.world != null && internalField0149.player != null) {
         if (this.internalField0519.internalMethod02365(1000L)) {
            ArrayList localValue2 = new ArrayList();

            for (BlockEntity localValue4 : BlockEntityCache.internalMethod06956()) {
               if (this.internalMethod05067(localValue4)) {
                  List localValue5 = this.internalMethod07215(localValue4);
                  ColorRGBA localValue6 = this.internalMethod06459(localValue4);
                  Vec3d localValue7 = localValue4.getPos().toCenterPos();
                  localValue2.add(new StorageEspModule.InternalType0513(localValue5, localValue6, localValue7));
               }
            }

            ArrayList localValue9 = new ArrayList();

            for (Entity localValue11 : internalField0149.world.getEntities()) {
               if (this.internalMethod01684(localValue11)) {
                  Box localValue12 = localValue11.getBoundingBox();
                  ColorRGBA localValue13 = this.internalMethod06361(localValue11);
                  Vec3d localValue8 = localValue11.getEntityPos();
                  localValue9.add(new StorageEspModule.InternalType0514(localValue12, localValue13, localValue8));
               }
            }

            this.internalField0416 = localValue2;
            this.internalField0417 = localValue9;
            this.internalField0519.internalMethod00701();
         }
      }
   };

   public StorageEspModule() {
      this.internalMethod09209();
   }

   private void internalMethod09209() {
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.storage_esp.blocks");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.chests").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.ender_chests").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.trapped_chests");
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.furnaces");
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.barrels").select();
      this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.minecart").select();
      this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.shulkers").select();
      this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.droppers");
      this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.dispensers");
      this.internalField1489 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.storage_esp.blocks.hoppers");
      this.internalField0674 = new MultiSelectSetting(this, "modules.settings.storage_esp.render");
      this.internalField1493 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.storage_esp.render.fill").select();
      this.internalField1487 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.storage_esp.render.outline").select();
      this.internalField1486 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.storage_esp.render.diagonals").select();
      this.internalField1492 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.storage_esp.render.lines");
      this.internalField0383 = new SliderSetting(this, "modules.settings.storage_esp.max_distance", "modules.settings.storage_esp.max_distance.description")
         .internalMethod05900(5.0F)
         .internalMethod02732(128.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(128.0F);
   }

   private List<Box> internalMethod07215(BlockEntity localValue1) {
      if (internalField0149.world == null) {
         return List.of(internalField0682);
      } else {
         BlockPos localValue2 = localValue1.getPos();
         BlockState localValue3 = internalField0149.world.getBlockState(localValue2);
         VoxelShape localValue4 = localValue3.getOutlineShape(internalField0149.world, localValue2);
         return localValue4.isEmpty() ? List.of(internalField0681.offset(localValue2)) : localValue4.getBoundingBoxes().stream().map(localValue1x -> localValue1x.offset(localValue2)).toList();
      }
   }

   private boolean internalMethod05067(BlockEntity localValue1) {
      double localValue2 = this.internalField0383.internalMethod08576() * this.internalField0383.internalMethod08576();
      if (internalField0149.player == null || internalField0149.player.squaredDistanceTo(localValue1.getPos().toCenterPos()) > localValue2) {
         return false;
      } else if (localValue1 instanceof ChestBlockEntity && this.internalField0245.isSelected()) {
         return true;
      } else if (localValue1 instanceof EnderChestBlockEntity && this.internalField0244.isSelected()) {
         return true;
      } else if (localValue1 instanceof TrappedChestBlockEntity && this.internalField1075.isSelected()) {
         return true;
      } else if (localValue1 instanceof FurnaceBlockEntity && this.internalField1074.isSelected()) {
         return true;
      } else if (localValue1 instanceof BarrelBlockEntity && this.internalField1073.isSelected()) {
         return true;
      } else if (localValue1 instanceof ShulkerBoxBlockEntity && this.internalField1491.isSelected()) {
         return true;
      } else if (localValue1 instanceof DropperBlockEntity && this.internalField1488.isSelected()) {
         return true;
      } else {
         return localValue1 instanceof DispenserBlockEntity && this.internalField1490.isSelected()
            ? true
            : localValue1 instanceof HopperBlockEntity && this.internalField1489.isSelected();
      }
   }

   private ColorRGBA internalMethod06459(BlockEntity localValue1) {
      if (localValue1 instanceof ChestBlockEntity) {
         return new ColorRGBA(255.0F, 131.0F, 54.0F);
      } else if (localValue1 instanceof EnderChestBlockEntity) {
         return new ColorRGBA(121.0F, 54.0F, 255.0F);
      } else if (localValue1 instanceof TrappedChestBlockEntity) {
         return new ColorRGBA(255.0F, 101.0F, 54.0F);
      } else if (localValue1 instanceof FurnaceBlockEntity) {
         return new ColorRGBA(126.0F, 126.0F, 126.0F);
      } else if (localValue1 instanceof BarrelBlockEntity) {
         return new ColorRGBA(255.0F, 185.0F, 54.0F);
      } else if (localValue1 instanceof ShulkerBoxBlockEntity) {
         if (localValue1.getCachedState().getBlock() instanceof ShulkerBoxBlock localValue2) {
            DyeColor localValue4 = localValue2.getColor();
            if (localValue4 != null) {
               return ColorRGBA.fromInt(localValue4.getEntityColor());
            }
         }

         return new ColorRGBA(181.0F, 54.0F, 255.0F);
      } else if (localValue1 instanceof DropperBlockEntity) {
         return new ColorRGBA(100.0F, 100.0F, 100.0F);
      } else if (localValue1 instanceof DispenserBlockEntity) {
         return new ColorRGBA(100.0F, 100.0F, 100.0F);
      } else {
         return localValue1 instanceof HopperBlockEntity ? new ColorRGBA(100.0F, 100.0F, 100.0F) : ThemeColors.internalField1312;
      }
   }

   private ColorRGBA internalMethod06361(Entity localValue1) {
      return localValue1 instanceof ChestMinecartEntity ? new ColorRGBA(255.0F, 200.0F, 100.0F) : ThemeColors.internalField1312;
   }

   private boolean internalMethod01684(Entity localValue1) {
      double localValue2 = this.internalField0383.internalMethod08576() * this.internalField0383.internalMethod08576();
      return internalField0149.player != null && !(internalField0149.player.squaredDistanceTo(localValue1.getEntityPos()) > localValue2)
         ? localValue1 instanceof ChestMinecartEntity && this.internalField1072.isSelected()
         : false;
   }

   static final class InternalType0513 {
      final List<Box> internalField0416;
      final ColorRGBA internalField0777;
      final Vec3d internalField0283;

      InternalType0513(List<Box> localValue1, ColorRGBA localValue2, Vec3d localValue3) {
         this.internalField0416 = localValue1;
         this.internalField0777 = localValue2;
         this.internalField0283 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0513[boundingBoxes=" + this.internalField0416 + ", color=" + this.internalField0777 + ", centerPos=" + this.internalField0283 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0777);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         StorageEspModule.InternalType0513 other = (StorageEspModule.InternalType0513) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0777, other.internalField0777)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283);
      }

      public List<Box> internalMethod05378() {
         return this.internalField0416;
      }

      public ColorRGBA internalMethod07450() {
         return this.internalField0777;
      }

      public Vec3d internalMethod01964() {
         return this.internalField0283;
      }
   }

   static final class InternalType0514 {
      final Box internalField0681;
      final ColorRGBA internalField0777;
      final Vec3d internalField0283;

      InternalType0514(Box localValue1, ColorRGBA localValue2, Vec3d localValue3) {
         this.internalField0681 = localValue1;
         this.internalField0777 = localValue2;
         this.internalField0283 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0514[boundingBox=" + this.internalField0681 + ", color=" + this.internalField0777 + ", pos=" + this.internalField0283 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0681);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0777);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         StorageEspModule.InternalType0514 other = (StorageEspModule.InternalType0514) localValue1;
         return java.util.Objects.equals(this.internalField0681, other.internalField0681)
            && java.util.Objects.equals(this.internalField0777, other.internalField0777)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283);
      }

      public Box internalMethod01463() {
         return this.internalField0681;
      }

      public ColorRGBA internalMethod00697() {
         return this.internalField0777;
      }

      public Vec3d internalMethod06230() {
         return this.internalField0283;
      }
   }
}
