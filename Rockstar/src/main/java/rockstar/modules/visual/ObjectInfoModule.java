package rockstar.modules.visual;











import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Object Info",
   category = ModuleCategory.VISUALS,
   internalMethod09633 = "modules.descriptions.object_info"
)
public class ObjectInfoModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private final Map<BlockPos, ObjectInfoModule.InternalType0320> internalField0543 = new HashMap<>();
   private final List<ObjectInfoModule.InternalType0319> internalField0416 = new ArrayList<>();
   static final Random internalField0362 = new Random();
   private final Stopwatch internalField0519 = new Stopwatch();
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (localValue1.getPacket() instanceof PlaySoundS2CPacket localValue2) {
         String localValue5 = localValue2.getSound().getIdAsString();
         if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
            if (localValue5.contains("minecraft:block.anvil.place")
               && (localValue2.getVolume() == 0.5F || localValue2.getVolume() == 0.7F)
               && (localValue2.getPitch() == 1.1F || localValue2.getPitch() == 0.5F)) {
               BlockPos localValue4 = new BlockPos((int)localValue2.getX(), (int)localValue2.getY(), (int)localValue2.getZ());
               this.internalField0543
                  .put(localValue4, new ObjectInfoModule.InternalType0320(localValue4.up().add(0, 0, 0), ObjectInfoModule.InternalType0231.internalField1314));
            }

            if ((localValue5.contains("minecraft:entity.wither.break_block") || localValue5.contains("minecraft:block.anvil.place")) && localValue2.getVolume() == 0.7F
               || localValue2.getVolume() == 0.2F && localValue2.getPitch() == 1.0F) {
               BlockPos localValue6 = new BlockPos((int)localValue2.getX(), (int)localValue2.getY(), (int)localValue2.getZ());
               this.internalField0543
                  .put(localValue6, new ObjectInfoModule.InternalType0320(localValue6.up().add(0, 0, 0), ObjectInfoModule.InternalType0231.internalField0784));
            }
         } else if (ServerUtils.internalMethod08700()) {
            if (localValue5.contains("minecraft:entity.generic.explode") && (localValue2.getVolume() == 1.0F || localValue2.getPitch() == 1.0F)) {
               BlockPos localValue7 = new BlockPos((int)localValue2.getX(), (int)localValue2.getY(), (int)localValue2.getZ());
               this.internalField0543.put(localValue7, new ObjectInfoModule.InternalType0320(localValue7, ObjectInfoModule.InternalType0231.internalField1316));
               this.internalField0543.put(localValue7.up(), new ObjectInfoModule.InternalType0320(localValue7.up(), ObjectInfoModule.InternalType0231.internalField1315));
            }

            if (localValue5.contains("minecraft:block.beacon.deactivate") && (localValue2.getVolume() == 1.5 || localValue2.getPitch() == 1.0F)) {
               BlockPos localValue8 = new BlockPos((int)localValue2.getX(), (int)localValue2.getY(), (int)localValue2.getZ());
               this.internalField0543.put(localValue8, new ObjectInfoModule.InternalType0320(localValue8, ObjectInfoModule.InternalType0231.internalField1316));
            }
         }
      }
   };
   private final EventListener<PreHudRenderEvent> internalField0158 = localValue1 -> {
      try {
         for (ObjectInfoModule.InternalType0320 localValue3 : this.internalField0543.values()) {
            localValue3.internalMethod00234(localValue1);
         }
      } catch (ConcurrentModificationException localValue4) {
      }
   };
   private final EventListener<Render3DEvent> internalField1028 = localValue1 -> {
      MatrixStack localValue2 = localValue1.getMatrices();
      localValue2.push();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
      Identifier localValue3 = RockstarClient.id("textures/bloom.png");
      RenderSystem.setShaderTexture(0, localValue3);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder localValue4 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      BlockPos localValue5 = null;

      try {
         for (Entry localValue7 : this.internalField0543.entrySet()) {
            ObjectInfoModule.InternalType0320 localValue8 = (ObjectInfoModule.InternalType0320)localValue7.getValue();
            localValue2.push();
            localValue8.internalMethod06672(localValue1, localValue4);
            localValue2.pop();
            if (localValue8.internalField0519.internalMethod02365(localValue8.internalMethod03212().internalMethod04734())) {
               this.internalMethod05936(localValue8);
               localValue5 = (BlockPos)localValue7.getKey();
            }
         }
      } catch (ConcurrentModificationException localValue9) {
      }

      Iterator localValue10 = this.internalField0416.iterator();

      while (localValue10.hasNext()) {
         ObjectInfoModule.InternalType0319 localValue11 = (ObjectInfoModule.InternalType0319)localValue10.next();
         if (localValue11.internalMethod01548()) {
            localValue10.remove();
         } else {
            localValue11.internalMethod01547();
            localValue11.internalMethod00014(localValue1, localValue4);
         }
      }

      if (localValue5 != null) {
         this.internalField0543.remove(localValue5);
      }

      HudRenderUtils.internalMethod05816(localValue4);
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
      localValue2.pop();
   };
   private final EventListener<WorldChangeEvent> internalField1029 = localValue1 -> {
      this.internalField0543.clear();
      this.internalField0416.clear();
   };

   public ObjectInfoModule() {
      this.internalMethod09351();
   }

   private void internalMethod09351() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.object_info.particleMode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.object_info.particleMode.gravity");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.object_info.particleMode.scatter");
   }

   private void internalMethod05936(ObjectInfoModule.InternalType0320 localValue1) {
      if (localValue1.internalField0784 == ObjectInfoModule.InternalType0231.internalField1316) {
         Vec3d localValue2 = localValue1.internalField0352.toCenterPos();
         float[][] localValue3 = new float[][]{
            {15.0F, -15.0F, 15.0F, 0.0F, 1.0F, 0.0F},
            {-15.0F, -15.0F, 15.0F, 0.0F, 1.0F, 0.0F},
            {15.0F, -15.0F, -15.0F, 0.0F, 1.0F, 0.0F},
            {-15.0F, -15.0F, -15.0F, 0.0F, 1.0F, 0.0F},
            {-15.0F, 15.0F, 15.0F, 1.0F, 0.0F, 0.0F},
            {-15.0F, -15.0F, 15.0F, 1.0F, 0.0F, 0.0F},
            {-15.0F, 15.0F, -15.0F, 1.0F, 0.0F, 0.0F},
            {-15.0F, -15.0F, -15.0F, 1.0F, 0.0F, 0.0F},
            {15.0F, 15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
            {-15.0F, 15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
            {15.0F, -15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
            {-15.0F, -15.0F, -15.0F, 0.0F, 0.0F, 1.0F}
         };

         for (float[] localValue7 : localValue3) {
            for (float localValue8 = 0.0F; localValue8 < 30.0F; localValue8 += 0.2F) {
               float localValue9 = localValue7[0] + localValue8 * localValue7[3];
               float localValue10 = localValue7[1] + localValue8 * localValue7[4];
               float localValue11 = localValue7[2] + localValue8 * localValue7[5];
               float localValue12 = 0.005F;
               float localValue13 = (internalField0362.nextFloat() - 0.5F) * 2.0F * localValue12;
               float localValue14 = (internalField0362.nextFloat() - 0.5F) * 2.0F * localValue12;
               float localValue15 = (internalField0362.nextFloat() - 0.5F) * 2.0F * localValue12;
               this.internalField0416
                  .add(
                     new ObjectInfoModule.InternalType0319(
                        localValue2, localValue9, localValue10, localValue11, localValue13, localValue14, localValue15, this.internalField0668.internalMethod06103(this.internalField0237)
                     )
                  );
            }
         }
      }
   }

   static enum InternalType0231 {
      internalField0784("object_info.trap", Items.NETHERITE_SCRAP, 15000L),
      internalField0783("object_info.dragon", Items.NETHERITE_SCRAP, 30000L),
      internalField1317("object_info.dragon", Items.NETHERITE_SCRAP, 60000L),
      internalField1315("object_info.boom_trap", Items.PRISMARINE_SHARD, 11000L),
      internalField1316("object_info.stan", Items.NETHER_STAR, 15000L),
      internalField1314("object_info.plast", Items.DRIED_KELP, 20000L);

      final String internalField0248;
      final Item internalField0152;
      final long internalField0229;

      public String internalMethod01635() {
         return LanguageManager.internalMethod07214(this.internalField0248);
      }

      @Generated
      public String internalMethod06214() {
         return this.internalField0248;
      }

      @Generated
      public Item internalMethod02393() {
         return this.internalField0152;
      }

      @Generated
      public long internalMethod04734() {
         return this.internalField0229;
      }

      @Generated
      private InternalType0231(String localValue3, Item localValue4, long localValue5) {
         this.internalField0248 = localValue3;
         this.internalField0152 = localValue4;
         this.internalField0229 = localValue5;
      }
   }

   class InternalType0319 {
      double internalField0194;
      double internalField0193;
      double internalField1045;
      float internalField0205;
      float internalField0206;
      float internalField1048;
      long internalField0229;
      float internalField1047 = 1.0F;
      float internalField1049 = 2.5F;
      float internalField1046;
      long internalField0230;
      boolean internalField0277;
      float internalField1456;
      static final float internalField1457 = 0.4F;

      InternalType0319(Vec3d localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, boolean localValue9) {
         this.internalField0194 = localValue2.x + localValue3;
         this.internalField0193 = localValue2.y + localValue4;
         this.internalField1045 = localValue2.z + localValue5;
         this.internalField0205 = localValue6;
         this.internalField0206 = localValue7;
         this.internalField1048 = localValue8;
         this.internalField0229 = System.currentTimeMillis();
         this.internalField0277 = localValue9;
         this.internalField1046 = 2.0E-4F + ObjectInfoModule.internalField0362.nextFloat() * 4.0E-4F;
         this.internalField0230 = 2000 + ObjectInfoModule.internalField0362.nextInt(2000);
         this.internalField1456 = localValue9 ? 0.9999F : 0.999F;
      }

      void internalMethod01547() {
         if (this.internalField0277) {
            this.internalField0206 = this.internalField0206 - this.internalField1046;
         }

         this.internalField0205 = this.internalField0205 * this.internalField1456;
         this.internalField0206 = this.internalField0206 * this.internalField1456;
         this.internalField1048 = this.internalField1048 * this.internalField1456;
         double localValue1 = this.internalField0194 + this.internalField0205;
         double localValue3 = this.internalField0193 + this.internalField0206;
         double localValue5 = this.internalField1045 + this.internalField1048;
         if (this.internalField0277) {
            BlockPos localValue7 = BlockPos.ofFloored(this.internalField0194, localValue3 - 0.5, this.internalField1045);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue7).isAir()) {
               this.internalField0206 = -this.internalField0206 * 0.4F;
               localValue3 = this.internalField0193;
            }

            BlockPos localValue8 = BlockPos.ofFloored(localValue1, this.internalField0193, this.internalField1045);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue8).isAir()) {
               this.internalField0205 = -this.internalField0205 * 0.4F;
               localValue1 = this.internalField0194;
            }

            BlockPos localValue9 = BlockPos.ofFloored(this.internalField0194, this.internalField0193, localValue5);
            if (!MinecraftClientAccess.internalField0149.world.getBlockState(localValue9).isAir()) {
               this.internalField1048 = -this.internalField1048 * 0.4F;
               localValue5 = this.internalField1045;
            }
         }

         this.internalField0194 = localValue1;
         this.internalField0193 = localValue3;
         this.internalField1045 = localValue5;
      }

      float internalMethod01546() {
         return MathHelper.clamp((float)(System.currentTimeMillis() - this.internalField0229) / (float)this.internalField0230, 0.0F, 1.0F);
      }

      float internalMethod01550() {
         return 1.0F - this.internalMethod01546();
      }

      boolean internalMethod01548() {
         return System.currentTimeMillis() - this.internalField0229 > this.internalField0230;
      }

      void internalMethod00014(Render3DEvent localValue1, BufferBuilder localValue2) {
         MatrixStack localValue3 = localValue1.getMatrices();
         Camera localValue4 = MinecraftClientAccess.internalField0149.gameRenderer.getCamera();
         float localValue5 = this.internalMethod01550();
         localValue3.push();
         HudRenderUtils.internalMethod03474(localValue3, new Vec3d(this.internalField0194, this.internalField0193, this.internalField1045));
         localValue3.multiply(localValue4.getRotation());
         RenderPipeline.internalMethod05007(
            localValue3,
            localValue2,
            -this.internalField1047 / 2.0F,
            -this.internalField1047 / 2.0F,
            0.0,
            this.internalField1047,
            this.internalField1047,
            ThemeColors.internalMethod02531().mulAlpha(0.9F * localValue5)
         );
         RenderPipeline.internalMethod05007(
            localValue3,
            localValue2,
            -this.internalField1049 / 2.0F,
            -this.internalField1049 / 2.0F,
            0.0,
            this.internalField1049,
            this.internalField1049,
            ThemeColors.internalMethod02531().mulAlpha(0.1F * localValue5)
         );
         localValue3.pop();
      }
   }

   static class InternalType0320 {
      final BlockPos internalField0352;
      final ObjectInfoModule.InternalType0231 internalField0784;
      Stopwatch internalField0519 = new Stopwatch();

      void internalMethod00234(PreHudRenderEvent localValue1) {
         int localValue2 = (int)((float)(this.internalField0784.internalMethod04734() - this.internalField0519.internalMethod00700()) / 1000.0F);
         org.joml.Matrix3x2fStack localValue3 = localValue1.getContext().getMatrices();
         BlockPos localValue4 = this.internalField0352;
         Vec3d localValue5 = localValue4.add(0, 1, 0).toCenterPos();
         Vec2f localValue6 = ScreenProjectionUtils.internalMethod00612(localValue5);
         if (localValue6 != null) {
            float localValue7 = (float)MinecraftClientAccess.internalField0149.player.getEntityPos().distanceTo(Vec3d.of(localValue4));
            float localValue8 = MathHelper.clamp(1.0F - localValue7 / 20.0F, 0.5F, 1.0F) * 0.5F;
            float localValue9 = 1.0F - (float)this.internalField0519.internalMethod00700() / (float)this.internalField0784.internalMethod04734();
            String localValue10 = "0:" + (localValue2 < 10 ? "0" + localValue2 : localValue2);
            float localValue11 = 150.0F;
            float localValue12 = 150.0F;
            localValue3.pushMatrix();
            localValue3.translate(localValue6.x - localValue11 / 2.0F, localValue6.y - localValue12 / 2.0F);
            HudRenderUtils.internalMethod08976(localValue3, localValue11 / 2.0F, localValue12 / 2.0F, localValue8);
            localValue1.getContext()
               .drawBlurredRect(0.0F, 0.0F, localValue11, localValue12, 45.0F, 5.0F, CornerRadii.internalMethod03908(26.0F), ThemeColors.internalField1312);
            localValue1.getContext()
               .drawSquircle(0.0F, 0.0F, localValue11, localValue12, 5.0F, CornerRadii.internalMethod03908(26.0F), new ColorRGBA(9.0F, 9.0F, 11.0F).mulAlpha(0.5F));
            localValue1.getContext().drawCircleProgress(localValue11 / 2.0F, localValue12 / 2.0F, 48.0F, 6.0F, localValue9, ThemeColors.internalMethod02531());
            localValue1.getContext().drawItem(this.internalField0784.internalMethod02393(), 60.0F, 50.0F, 1.875F);
            localValue1.getContext()
               .drawCenteredText(Fonts.internalField1156.internalMethod01432(20.0F), localValue10, localValue11 / 2.0F, 86.0F, ThemeColors.internalMethod08459());
            HudRenderUtils.internalMethod00012(localValue3);
            localValue3.popMatrix();
         }
      }

      void internalMethod06672(Render3DEvent localValue1, BufferBuilder localValue2) {
         if (this.internalField0784 == ObjectInfoModule.InternalType0231.internalField1316) {
            float localValue3 = 1.0F;
            float localValue4 = 2.5F;
            MatrixStack localValue5 = localValue1.getMatrices();
            Camera localValue6 = MinecraftClientAccess.internalField0149.gameRenderer.getCamera();
            HudRenderUtils.internalMethod03474(localValue5, this.internalField0352.toCenterPos());
            float[][] localValue7 = new float[][]{
               {15.0F, -15.0F, 15.0F, 0.0F, 1.0F, 0.0F},
               {-15.0F, -15.0F, 15.0F, 0.0F, 1.0F, 0.0F},
               {15.0F, -15.0F, -15.0F, 0.0F, 1.0F, 0.0F},
               {-15.0F, -15.0F, -15.0F, 0.0F, 1.0F, 0.0F},
               {-15.0F, 15.0F, 15.0F, 1.0F, 0.0F, 0.0F},
               {-15.0F, -15.0F, 15.0F, 1.0F, 0.0F, 0.0F},
               {-15.0F, 15.0F, -15.0F, 1.0F, 0.0F, 0.0F},
               {-15.0F, -15.0F, -15.0F, 1.0F, 0.0F, 0.0F},
               {15.0F, 15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
               {-15.0F, 15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
               {15.0F, -15.0F, -15.0F, 0.0F, 0.0F, 1.0F},
               {-15.0F, -15.0F, -15.0F, 0.0F, 0.0F, 1.0F}
            };

            for (float[] localValue11 : localValue7) {
               for (float localValue12 = 0.0F; localValue12 < 30.0F; localValue12 += 0.2F) {
                  localValue5.push();
                  localValue5.translate(localValue11[0] + localValue12 * localValue11[3], localValue11[1] + localValue12 * localValue11[4], localValue11[2] + localValue12 * localValue11[5]);
                  localValue5.multiply(localValue6.getRotation());
                  RenderPipeline.internalMethod05007(
                     localValue5, localValue2, -localValue3 / 2.0F, -localValue3 / 2.0F, 0.0, localValue3, localValue3, ThemeColors.internalMethod02531().mulAlpha(0.9F)
                  );
                  RenderPipeline.internalMethod05007(
                     localValue5, localValue2, -localValue4 / 2.0F, -localValue4 / 2.0F, 0.0, localValue4, localValue4, ThemeColors.internalMethod02531().mulAlpha(0.1F)
                  );
                  localValue5.pop();
               }
            }
         }
      }

      @Generated
      public BlockPos internalMethod00523() {
         return this.internalField0352;
      }

      @Generated
      public ObjectInfoModule.InternalType0231 internalMethod03212() {
         return this.internalField0784;
      }

      @Generated
      public Stopwatch internalMethod03598() {
         return this.internalField0519;
      }

      @Generated
      public InternalType0320(BlockPos localValue1, ObjectInfoModule.InternalType0231 localValue2) {
         this.internalField0352 = localValue1;
         this.internalField0784 = localValue2;
      }
   }
}
