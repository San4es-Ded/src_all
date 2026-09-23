package rockstar.client.esp;








import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.block.BlockState;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class GlowEspFeature extends EspFeature implements WindowAccess {
   public static boolean internalField0277;
   public static boolean internalField0276;
   public static Entity internalField0410;
   private static PlayerTargetType internalField0025;
   private static ItemTargetType internalField0012;
   private static EntityTargetType internalField0027;
   private static final ColorRGBA internalField0777 = new ColorRGBA(255.0F, 255.0F, 255.0F, 255.0F);
   private final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
   private final ManagedFramebuffer internalField0770 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
   private final ManagedFramebuffer internalField1308 = new ManagedFramebuffer(true).internalMethod06013().internalMethod03472(1.0F);
   private final GlowBloomRenderer internalField0320 = GlowBloomRenderer.internalMethod06776();
   private final GlowBloomRenderer internalField0321 = GlowBloomRenderer.internalMethod06776();
   private final Stopwatch internalField0519 = new Stopwatch();
   private boolean internalField1099 = false;
   private boolean internalField1100 = false;
   private boolean internalField1102 = false;
   private boolean internalField1101 = false;
   private int internalField1053 = -1;
   private int internalField1055 = -1;
   private int internalField1056 = -1;
   private int internalField1054 = -1;
   private float internalField0205;
   private float internalField0206;
   private float internalField1048;
   private float internalField1047;
   private boolean internalField1516;
   private final BooleanSetting internalField0650 = this.internalMethod02236("esp.glow");
   private final SliderSetting internalField0383 = new SliderSetting(this, "esp.glow.strength")
      .internalMethod05900(1.0F)
      .internalMethod02732(5.0F)
      .internalMethod08673(1.0F)
      .internalMethod08074(3.0F);
   private final SliderSetting internalField0382 = new SliderSetting(this, "esp.glow.strength_items")
      .internalMethod05900(1.0F)
      .internalMethod02732(5.0F)
      .internalMethod08673(1.0F)
      .internalMethod08074(3.0F);
   private final BooleanSetting internalField0651 = this.internalMethod06933((localValue0, localValue1, localValue2) -> {
      String localValue3 = localValue2 == EntityTargetType.internalField0964 ? "esp.glow.item_color" : "esp.glow.entity_color";
      return new BooleanSetting(localValue0, localValue3, () -> !localValue1.internalMethod04496()).internalMethod06630();
   });
   private final BooleanSetting internalField1261 = this.internalMethod04340(
      "esp.glow.entity_color",
      (localValue0, localValue1, localValue2) -> new BooleanSetting(localValue0, "theme.sync", () -> !localValue1.internalMethod04496() || localValue2.internalMethod04496()).internalMethod06630()
   );
   private final BooleanSetting internalField1263 = this.internalMethod06698(
      "esp.glow.entity_color",
      "theme.sync",
      (localValue0, localValue1, localValue2, localValue3) -> new BooleanSetting(
         localValue0, "esp.glow.gradient", () -> !localValue1.internalMethod04496() || localValue2.internalMethod04496() || localValue3.internalMethod04496()
      )
   );
   private final ColorSetting internalField0665 = this.internalMethod01887(
      "esp.glow.entity_color",
      "theme.sync",
      "esp.glow.gradient",
      (localValue0, localValue1, localValue2, localValue3, localValue4) -> new ColorSetting(
            localValue0, "esp.glow.color", () -> !localValue1.internalMethod04496() || localValue2.internalMethod04496() || localValue3.internalMethod04496() || localValue4.internalMethod04496()
         )
         .internalMethod04886(ThemeColors.internalMethod02531())
   );
   private final List<GradientColorSetting> internalField0416 = new ArrayList<>();
   private boolean internalField1517;
   private final GradientColorSetting internalField0666 = this.internalMethod01887(
      "esp.glow.entity_color",
      "theme.sync",
      "esp.glow.gradient",
      (localValue1, localValue2, localValue3, localValue4, localValue5) -> {
         GlowEspFeature.InternalType0168 localValue6 = new GlowEspFeature.InternalType0168(
            localValue1,
            "esp.glow.gradient_color",
            () -> !localValue2.internalMethod04496() || localValue3.internalMethod04496() || localValue4.internalMethod04496() || !localValue5.internalMethod04496(),
            this::internalMethod07350
         );
         localValue6.internalMethod00046(new ColorRGBA(255.0F, 80.0F, 200.0F, 255.0F), new ColorRGBA(80.0F, 160.0F, 255.0F, 255.0F));
         this.internalField0416.add(localValue6);
         return localValue6;
      }
   );
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      this.internalField1102 = false;
      if (this.internalMethod02927(ItemTargetType.internalField0012)) {
         this.internalField0770.internalMethod02227(true);
         this.internalField0770.internalMethod03248();
      }

      if (this.internalMethod06968() && this.internalField0519.internalMethod02365(10L)) {
         boolean localValue2 = this.internalMethod02927(ItemTargetType.internalField0013);
         this.internalField0769.internalMethod02227(true);
         internalField0277 = true;
         this.internalField1100 = false;
         this.internalField0205 = this.internalField0206 = Float.POSITIVE_INFINITY;
         this.internalField1048 = this.internalField1047 = Float.NEGATIVE_INFINITY;
         this.internalField1516 = false;

         for (Entity localValue4 : internalField0149.world.getEntities()) {
            if (!(localValue4 instanceof ItemEntity) && this.internalMethod06209(localValue4)) {
               this.internalMethod04116(localValue4, localValue1);
            }
         }

         internalField0277 = false;
         this.internalField0769.internalMethod03248();
         if (this.internalField1100 && this.internalMethod09230()) {
            this.internalMethod03896(this.internalField0769, internalField0777);
         }

         this.internalField1101 = false;
         if (localValue2) {
            this.internalField1308.internalMethod02227(true);
            internalField0276 = true;

            for (Entity localValue6 : internalField0149.world.getEntities()) {
               if (localValue6 instanceof ItemEntity && this.internalMethod06209(localValue6)) {
                  this.internalMethod02729(localValue6, localValue1);
               }
            }

            internalField0276 = false;
            this.internalField1308.internalMethod03248();
         }

         if (this.internalField1100) {
            this.internalMethod04776();
         } else {
            this.internalField1053 = this.internalField1055 = this.internalField1056 = this.internalField1054 = -1;
         }

         this.internalField1099 = true;
         this.internalField0519.internalMethod00701();
      }
   };
   private final EventListener<PreHudRenderEvent> internalField0158 = localValue1 -> {
      boolean localValue2 = this.internalMethod06968();
      if (localValue2) {
         if (this.internalField1099) {
            if (this.internalField1100) {
               RenderPipeline.internalField0320.internalMethod06580((int)this.internalField0383.internalMethod08576());
               RenderPipeline.internalField0320.internalMethod06579(7.0F);
               RenderPipeline.internalField0320.internalMethod08567(1.0F);
               RenderPipeline.internalField0320.internalMethod08555(1.0F);
               RenderPipeline.internalField0320.internalMethod02623(this.internalField0769, -1, -1, -1, -1);
            }

            this.internalField1099 = false;
         }

         if (this.internalField1100) {
            int localValue3 = GlowBloomRenderer.internalMethod08936();
            if (localValue3 != 0) {
               this.internalMethod03895(localValue3, -0.5F, this.internalMethod09230());
            }
         }
      }

      if (this.internalMethod02927(ItemTargetType.internalField0012) && this.internalField1102) {
         boolean localValue5 = this.internalMethod07667(ItemTargetType.internalField0012);
         if (localValue5) {
            this.internalMethod03896(this.internalField0770, internalField0777);
         } else {
            ColorRGBA localValue4 = this.internalMethod01164();
            if (localValue4 != null) {
               this.internalMethod03896(this.internalField0770, localValue4);
            }
         }

         this.internalField0320.internalMethod06580((int)this.internalField0382.internalMethod08576());
         this.internalField0320.internalMethod06579(7.0F);
         this.internalField0320.internalMethod08555(1.0F);
         this.internalField0320.internalMethod08567(1.0F);
         this.internalField0320.internalMethod08914(-8.0F);
         this.internalField0320.internalMethod02623(this.internalField0770, -1, -1, -1, -1);
         EspGlowCaptureRenderer.internalMethod04936(this.internalField0320.internalMethod05596());
         int localValue7 = this.internalField0320.internalMethod03092();
         if (localValue7 != 0) {
            this.internalMethod03895(localValue7, -0.5F, localValue5);
         }
      }

      if (this.internalMethod02927(ItemTargetType.internalField0013) && this.internalField1101) {
         boolean localValue6 = this.internalMethod07667(ItemTargetType.internalField0013);
         if (localValue6) {
            this.internalMethod03896(this.internalField1308, internalField0777);
         } else {
            ColorRGBA localValue8 = this.internalMethod05513();
            if (localValue8 != null) {
               this.internalMethod03896(this.internalField1308, localValue8);
            }
         }

         this.internalField0321.internalMethod06580((int)this.internalField0382.internalMethod08576());
         this.internalField0321.internalMethod06579(7.0F);
         this.internalField0321.internalMethod08555(1.0F);
         this.internalField0321.internalMethod08567(1.0F);
         this.internalField0321.internalMethod02623(this.internalField1308, -1, -1, -1, -1);
         int localValue9 = this.internalField0321.internalMethod03092();
         if (localValue9 != 0) {
            this.internalMethod03895(localValue9, -0.5F, localValue6);
         }
      }
   };
   private void internalMethod07350(GradientColorSetting localValue1) {
      if (!this.internalField1517) {
         this.internalField1517 = true;

         try {
            ColorRGBA localValue2 = localValue1.internalMethod05319();
            ColorRGBA localValue3 = localValue1.internalMethod01482();

            for (GradientColorSetting localValue5 : this.internalField0416) {
               if (localValue5 != localValue1) {
                  if (localValue2 != null) {
                     localValue5.internalMethod01227(localValue2);
                  }

                  if (localValue3 != null) {
                     localValue5.internalMethod04884(localValue3);
                  }
               }
            }
         } finally {
            this.internalField1517 = false;
         }
      }
   }

   private boolean internalMethod09230() {
      for (EntityTargetType localValue4 : new EntityTargetType[]{EntityTargetType.internalField0028, EntityTargetType.internalField0963}) {
         if (this.internalMethod08343(localValue4)) {
            return true;
         }
      }

      for (PlayerTargetType localValue8 : PlayerTargetType.values()) {
         if (this.internalMethod08335(localValue8)) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod08343(EntityTargetType localValue1) {
      if (!this.internalMethod06206(localValue1)) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalMethod05940("esp.glow.entity_color", localValue1);
         if (localValue2 != null && localValue2.internalMethod04496()) {
            return false;
         } else {
            BooleanSetting localValue3 = this.internalMethod05940("theme.sync", localValue1);
            if (localValue3 != null && localValue3.internalMethod04496()) {
               return false;
            } else {
               BooleanSetting localValue4 = this.internalMethod05940("esp.glow.gradient", localValue1);
               return localValue4 != null && localValue4.internalMethod04496();
            }
         }
      }
   }

   private boolean internalMethod08335(PlayerTargetType localValue1) {
      if (!this.internalMethod06170(localValue1)) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalMethod02672("esp.glow.entity_color", localValue1);
         if (localValue2 != null && localValue2.internalMethod04496()) {
            return false;
         } else {
            BooleanSetting localValue3 = this.internalMethod02672("theme.sync", localValue1);
            if (localValue3 != null && localValue3.internalMethod04496()) {
               return false;
            } else {
               BooleanSetting localValue4 = this.internalMethod02672("esp.glow.gradient", localValue1);
               return localValue4 != null && localValue4.internalMethod04496();
            }
         }
      }
   }

   private boolean internalMethod07667(ItemTargetType localValue1) {
      if (!this.internalMethod02927(localValue1)) {
         return false;
      } else {
         BooleanSetting localValue2 = this.internalMethod02616("esp.glow.entity_color", localValue1);
         if (localValue2 != null && localValue2.internalMethod04496()) {
            return false;
         } else {
            BooleanSetting localValue3 = this.internalMethod02616("theme.sync", localValue1);
            if (localValue3 != null && localValue3.internalMethod04496()) {
               return false;
            } else {
               BooleanSetting localValue4 = this.internalMethod02616("esp.glow.gradient", localValue1);
               return localValue4 != null && localValue4.internalMethod04496();
            }
         }
      }
   }

   public GlowEspFeature() {
      super(
         "glow",
         new ItemTargetType[]{ItemTargetType.internalField0012, ItemTargetType.internalField0013},
         EntityTargetType.internalField0027,
         EntityTargetType.internalField0028,
         EntityTargetType.internalField0963,
         EntityTargetType.internalField0964
      );
      this.internalField0320.internalMethod03093();
      this.internalField0321.internalMethod03093();
      this.internalMethod00430(
         this.internalField0383,
         new EntityTargetType[]{EntityTargetType.internalField0027, EntityTargetType.internalField0028, EntityTargetType.internalField0963}
      );
      this.internalMethod00430(this.internalField0382, new EntityTargetType[]{EntityTargetType.internalField0964});
   }

   public boolean internalMethod06209(Entity localValue1) {
      if (localValue1 instanceof PlayerEntity localValue2) {
         if (internalField0025 != null) {
            return this.internalMethod06170(internalField0025);
         } else if (localValue2 == internalField0149.player) {
            return this.internalMethod06170(PlayerTargetType.internalField0026);
         } else if (internalMethod00242(localValue2)) {
            return this.internalMethod06170(PlayerTargetType.internalField0962);
         } else {
            return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue2.getName().getString())
               ? this.internalMethod06170(PlayerTargetType.internalField0961)
               : this.internalMethod06170(PlayerTargetType.internalField0025);
         }
      } else if (localValue1 instanceof HostileEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0028);
      } else if (localValue1 instanceof AnimalEntity) {
         return this.internalMethod06206(EntityTargetType.internalField0963);
      } else if (localValue1 instanceof ItemEntity) {
         return internalField0012 != null ? this.internalMethod02927(internalField0012) : this.internalMethod02927(ItemTargetType.internalField0013);
      } else {
         return false;
      }
   }

   public ColorRGBA internalMethod03008(Entity localValue1) {
      BooleanSetting localValue2;
      BooleanSetting localValue3;
      ColorSetting localValue4;
      if (localValue1 instanceof PlayerEntity localValue5) {
         PlayerTargetType localValue6 = internalField0025 != null ? internalField0025 : internalMethod05946(localValue5);
         localValue2 = this.internalMethod02672("esp.glow.entity_color", localValue6);
         localValue3 = this.internalMethod02672("theme.sync", localValue6);
         localValue4 = this.internalMethod02672("esp.glow.color", localValue6);
      } else if (localValue1 instanceof HostileEntity) {
         EntityTargetType localValue7 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0028;
         localValue2 = this.internalMethod05940("esp.glow.entity_color", localValue7);
         localValue3 = this.internalMethod05940("theme.sync", localValue7);
         localValue4 = this.internalMethod05940("esp.glow.color", localValue7);
      } else {
         if (!(localValue1 instanceof AnimalEntity)) {
            return null;
         }

         EntityTargetType localValue8 = internalField0027 != null ? internalField0027 : EntityTargetType.internalField0963;
         localValue2 = this.internalMethod05940("esp.glow.entity_color", localValue8);
         localValue3 = this.internalMethod05940("theme.sync", localValue8);
         localValue4 = this.internalMethod05940("esp.glow.color", localValue8);
      }

      if (localValue2 == null || localValue2.internalMethod04496()) {
         return null;
      } else if (localValue3 != null && localValue3.internalMethod04496()) {
         return ThemeColors.internalMethod02531();
      } else {
         return localValue4 != null ? localValue4.internalMethod05620() : ThemeColors.internalMethod02531();
      }
   }

   public ColorRGBA internalMethod01164() {
      return this.internalMethod04081(ItemTargetType.internalField0012);
   }

   public ColorRGBA internalMethod05513() {
      return this.internalMethod04081(ItemTargetType.internalField0013);
   }

   private ColorRGBA internalMethod04081(ItemTargetType localValue1) {
      BooleanSetting localValue2 = this.internalMethod02616("esp.glow.entity_color", localValue1);
      BooleanSetting localValue3 = this.internalMethod02616("theme.sync", localValue1);
      ColorSetting localValue4 = this.internalMethod02616("esp.glow.color", localValue1);
      if (localValue2 == null || localValue2.internalMethod04496()) {
         return null;
      } else if (localValue3 != null && localValue3.internalMethod04496()) {
         return ThemeColors.internalMethod02531();
      } else {
         return localValue4 != null ? localValue4.internalMethod05620() : ThemeColors.internalMethod02531();
      }
   }

   public void internalMethod07050(
      HeldItemRenderer localValue1, AbstractClientPlayerEntity localValue2, ItemStack localValue3, ItemDisplayContext localValue4, boolean localValue5, MatrixStack localValue6, int localValue7
   ) {
      if (this.internalMethod02927(ItemTargetType.internalField0012)) {
         if (localValue3 != null && !localValue3.isEmpty()) {
            internalField0276 = true;
            this.internalField0770.internalMethod02227(false);
            Immediate localValue8 = internalField0149.getBufferBuilders().getEntityVertexConsumers();

            try {
               rockstar.client.render.LegacyRenderCompat.renderItem(localValue2, localValue3, localValue4, localValue5, localValue6, localValue8, 15728880);
               localValue8.draw();
               this.internalField1102 = true;
            } catch (Exception localValue10) {
            }

            this.internalField0770.internalMethod03248();
            internalField0276 = false;
         }
      }
   }

   public void internalMethod05860(BlockRenderManager localValue1, BlockState localValue2, MatrixStack localValue3, int localValue4) {
      if (this.internalMethod02927(ItemTargetType.internalField0012)) {
         if (localValue2 != null) {
            internalField0276 = true;
            this.internalField0770.internalMethod02227(false);
            Immediate localValue5 = internalField0149.getBufferBuilders().getEntityVertexConsumers();

            try {
               localValue1.renderBlockAsEntity(localValue2, localValue3, localValue5, 15728880, localValue4);
               localValue5.draw();
               this.internalField1102 = true;
            } catch (Exception localValue7) {
            }

            this.internalField0770.internalMethod03248();
            internalField0276 = false;
         }
      }
   }

   private void internalMethod02729(Entity localValue1, Render3DEvent localValue2) {
      if (localValue1 != null && localValue1.isAlive()) {
         MatrixStack localValue3 = localValue2.getMatrices();
         Camera localValue4 = internalField0149.gameRenderer.getCamera();
         Vec3d localValue5 = ScreenProjectionUtils.internalMethod02822(localValue1, localValue2.getTickDelta());
         Vec3d localValue6 = localValue4.getCameraPos();
         localValue3.push();
         localValue3.translate(localValue5.x - localValue6.x, localValue5.y - localValue6.y, localValue5.z - localValue6.z);
         RenderSystem.disableDepthTest();
         RenderSystem.enableBlend();
         EntityRenderManager localValue7 = internalField0149.getEntityRenderDispatcher();
         Immediate localValue8 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
         internalField0410 = localValue1;

         try {
            rockstar.client.render.LegacyRenderCompat.renderEntity(localValue7, localValue1, localValue2.getTickDelta(), 0.0, 0.0, 0.0, localValue3, localValue8);
            localValue8.draw();
            this.internalField1101 = true;
         } catch (Exception localValue10) {
         }

         internalField0410 = null;
         RenderSystem.enableDepthTest();
         localValue3.pop();
      }
   }

   private void internalMethod04116(Entity localValue1, Render3DEvent localValue2) {
      if (localValue1 != null && localValue1.isAlive()) {
         if (localValue1 != internalField0149.player || !internalField0149.options.getPerspective().isFirstPerson()) {
            MatrixStack localValue3 = localValue2.getMatrices();
            Camera localValue4 = internalField0149.gameRenderer.getCamera();
            Vec3d localValue5 = ScreenProjectionUtils.internalMethod02822(localValue1, localValue2.getTickDelta());
            Vec3d localValue6 = localValue4.getCameraPos();
            localValue3.push();
            localValue3.translate(localValue5.x - localValue6.x, localValue5.y - localValue6.y, localValue5.z - localValue6.z);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            EntityRenderManager localValue7 = internalField0149.getEntityRenderDispatcher();
            Immediate localValue8 = internalField0149.getBufferBuilders().getEntityVertexConsumers();
            internalField0410 = localValue1;

            try {
               rockstar.client.render.LegacyRenderCompat.renderEntity(localValue7, localValue1, localValue2.getTickDelta(), 0.0, 0.0, 0.0, localValue3, localValue8);
               localValue8.draw();
               this.internalMethod05604(localValue1, localValue6);
               this.internalField1100 = true;
            } catch (Exception localValue10) {
            }

            internalField0410 = null;
            RenderSystem.enableDepthTest();
            localValue3.pop();
         }
      }
   }

   private void internalMethod03895(int localValue1, float localValue2, boolean localValue3) {
      ColorRGBA localValue4 = this.internalField0666.internalMethod05319();
      ColorRGBA localValue5 = this.internalField0666.internalMethod01482();
      if (localValue3 && localValue4 != null && localValue5 != null) {
         this.internalMethod03991(localValue1, localValue2, internalMethod02515(localValue4), internalMethod02515(localValue5));
      } else {
         this.internalMethod00755(localValue1, localValue2);
      }
   }

   private void internalMethod00755(int localValue1, float localValue2) {
      RenderSystem.setShaderTexture(0, localValue1);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
      RenderPipeline.internalMethod01737(0.0F, localValue2, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
      RenderPipeline.internalMethod01737(0.0F, localValue2, internalField0267.getScaledWidth(), internalField0267.getScaledHeight());
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
   }

   private void internalMethod03896(ManagedFramebuffer localValue1, ColorRGBA localValue2) {
      localValue1.beginWrite(true);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(772, 0, 0, 1);
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int localValue3 = internalMethod03396(localValue2);
      float localValue4 = internalField0267.getScaledWidth();
      float localValue5 = internalField0267.getScaledHeight();
      BufferBuilder localValue6 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      localValue6.vertex(0.0F, 0.0F, 0.0F).color(localValue3);
      localValue6.vertex(0.0F, localValue5, 0.0F).color(localValue3);
      localValue6.vertex(localValue4, localValue5, 0.0F).color(localValue3);
      localValue6.vertex(localValue4, 0.0F, 0.0F).color(localValue3);
      BufferRenderer.drawWithGlobalProgram(localValue6.end());
      RenderSystem.defaultBlendFunc();
      rockstar.client.render.FramebufferCompat.beginWrite(internalField0149.getFramebuffer(), true);
   }

   private void internalMethod03991(int localValue1, float localValue2, int localValue3, int localValue4) {
      RenderSystem.setShaderTexture(0, localValue1);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
      float localValue5 = internalField0267.getScaledWidth();
      float localValue6 = internalField0267.getScaledHeight();
      float localValue7 = localValue6 / 4.0F;
      float localValue8 = 2.0F * localValue7;
      float localValue9 = (float)(System.currentTimeMillis() % 1000000L) / 1000.0F;
      float localValue10 = localValue9 * 80.0F % localValue8;
      byte localValue11 = 2;

      for (int localValue12 = 0; localValue12 < 2; localValue12++) {
         BufferBuilder localValue13 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

         for (int localValue14 = -localValue11; localValue14 < 4 + localValue11; localValue14++) {
            boolean localValue15 = Math.floorMod(localValue14, 2) == 0;
            int localValue16 = localValue15 ? localValue3 : localValue4;
            int localValue17 = localValue15 ? localValue4 : localValue3;
            float localValue18 = localValue2 + localValue14 * localValue7 - localValue10;
            float localValue19 = localValue18 + localValue7;
            float localValue20 = 1.0F - (localValue18 - localValue2) / localValue6;
            float localValue21 = 1.0F - (localValue19 - localValue2) / localValue6;
            localValue13.vertex(0.0F, localValue18, 0.0F).texture(0.0F, localValue20).color(localValue16);
            localValue13.vertex(0.0F, localValue19, 0.0F).texture(0.0F, localValue21).color(localValue17);
            localValue13.vertex(localValue5, localValue19, 0.0F).texture(1.0F, localValue21).color(localValue17);
            localValue13.vertex(localValue5, localValue18, 0.0F).texture(1.0F, localValue20).color(localValue16);
         }

         BufferRenderer.drawWithGlobalProgram(localValue13.end());
      }

      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.disableDepthTest();
   }

   private static int internalMethod02515(ColorRGBA localValue0) {
      int localValue1 = Math.round(localValue0.getRed()) & 0xFF;
      int localValue2 = Math.round(localValue0.getGreen()) & 0xFF;
      int localValue3 = Math.round(localValue0.getBlue()) & 0xFF;
      return 0xFF000000 | localValue1 << 16 | localValue2 << 8 | localValue3;
   }

   private static int internalMethod03396(ColorRGBA localValue0) {
      float localValue1 = Math.max(0.0F, Math.min(1.0F, localValue0.getAlpha() / 255.0F));
      int localValue2 = Math.round(localValue0.getRed() * localValue1) & 0xFF;
      int localValue3 = Math.round(localValue0.getGreen() * localValue1) & 0xFF;
      int localValue4 = Math.round(localValue0.getBlue() * localValue1) & 0xFF;
      return 0xFF000000 | localValue2 << 16 | localValue3 << 8 | localValue4;
   }

   private void internalMethod05604(Entity localValue1, Vec3d localValue2) {
      if (!this.internalField1516) {
         Box localValue3 = localValue1.getBoundingBox();
         Matrix4f localValue4 = new Matrix4f(RenderSystem.getProjectionMatrix()).mul(RenderSystem.getModelViewMatrix());
         double[] localValue5 = new double[]{localValue3.minX, localValue3.maxX};
         double[] localValue6 = new double[]{localValue3.minY, localValue3.maxY};
         double[] localValue7 = new double[]{localValue3.minZ, localValue3.maxZ};
         Vector4f localValue8 = new Vector4f();

         for (double localValue12 : localValue5) {
            for (double localValue17 : localValue6) {
               for (double localValue22 : localValue7) {
                  localValue8.set((float)(localValue12 - localValue2.x), (float)(localValue17 - localValue2.y), (float)(localValue22 - localValue2.z), 1.0F);
                  localValue8.mul(localValue4);
                  if (localValue8.w <= 1.0E-4F) {
                     this.internalField1516 = true;
                     return;
                  }

                  float localValue24 = localValue8.x / localValue8.w;
                  float localValue25 = localValue8.y / localValue8.w;
                  if (localValue24 < this.internalField0205) {
                     this.internalField0205 = localValue24;
                  }

                  if (localValue24 > this.internalField1048) {
                     this.internalField1048 = localValue24;
                  }

                  if (localValue25 < this.internalField0206) {
                     this.internalField0206 = localValue25;
                  }

                  if (localValue25 > this.internalField1047) {
                     this.internalField1047 = localValue25;
                  }
               }
            }
         }
      }
   }

   private void internalMethod04776() {
      int localValue1 = internalField0149.getWindow().getFramebufferWidth();
      int localValue2 = internalField0149.getWindow().getFramebufferHeight();
      if (!this.internalField1516 && !(this.internalField0205 > this.internalField1048)) {
         byte localValue3 = 96;
         float localValue4 = (this.internalField0205 * 0.5F + 0.5F) * localValue1;
         float localValue5 = (this.internalField1048 * 0.5F + 0.5F) * localValue1;
         float localValue6 = (this.internalField0206 * 0.5F + 0.5F) * localValue2;
         float localValue7 = (this.internalField1047 * 0.5F + 0.5F) * localValue2;
         int localValue8 = (int)Math.floor(localValue4) - localValue3;
         int localValue9 = (int)Math.floor(localValue6) - localValue3;
         int localValue10 = (int)Math.ceil(localValue5 - localValue4) + localValue3 * 2;
         int localValue11 = (int)Math.ceil(localValue7 - localValue6) + localValue3 * 2;
         if (localValue8 < 0) {
            localValue10 += localValue8;
            localValue8 = 0;
         }

         if (localValue9 < 0) {
            localValue11 += localValue9;
            localValue9 = 0;
         }

         if (localValue8 < localValue1 && localValue9 < localValue2) {
            if (localValue8 + localValue10 > localValue1) {
               localValue10 = localValue1 - localValue8;
            }

            if (localValue9 + localValue11 > localValue2) {
               localValue11 = localValue2 - localValue9;
            }

            this.internalField1053 = localValue8;
            this.internalField1055 = localValue9;
            this.internalField1056 = localValue10;
            this.internalField1054 = localValue11;
         } else {
            this.internalField1053 = this.internalField1055 = this.internalField1056 = this.internalField1054 = -1;
         }
      } else {
         this.internalField1053 = 0;
         this.internalField1055 = 0;
         this.internalField1056 = localValue1;
         this.internalField1054 = localValue2;
      }
   }

   private static PlayerTargetType internalMethod05946(PlayerEntity localValue0) {
      if (localValue0 == internalField0149.player) {
         return PlayerTargetType.internalField0026;
      } else if (RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue0.getName().getString())) {
         return PlayerTargetType.internalField0961;
      } else {
         return internalMethod00242(localValue0) ? PlayerTargetType.internalField0962 : PlayerTargetType.internalField0025;
      }
   }

   private static boolean internalMethod00242(PlayerEntity localValue0) {
      String localValue1 = localValue0.getName().getString();

      for (Packets.InternalType0018 localValue3 : Information.getVisiblePlayers()) {
         if (localValue3.gameInfo() != null && localValue1.equals(localValue3.gameInfo().nickname())) {
            return true;
         }
      }

      return false;
   }

   @Generated
   public ManagedFramebuffer internalMethod04551() {
      return this.internalField0769;
   }

   @Generated
   public ManagedFramebuffer internalMethod05285() {
      return this.internalField0770;
   }

   @Generated
   public ManagedFramebuffer internalMethod08044() {
      return this.internalField1308;
   }

   @Generated
   public GlowBloomRenderer internalMethod01970() {
      return this.internalField0320;
   }

   @Generated
   public GlowBloomRenderer internalMethod01640() {
      return this.internalField0321;
   }

   @Generated
   public Stopwatch internalMethod00868() {
      return this.internalField0519;
   }

   @Generated
   public boolean internalMethod04777() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod08731() {
      return this.internalField1100;
   }

   @Generated
   public boolean internalMethod08734() {
      return this.internalField1102;
   }

   @Generated
   public boolean internalMethod08741() {
      return this.internalField1101;
   }

   @Generated
   public int internalMethod04773() {
      return this.internalField1053;
   }

   @Generated
   public int internalMethod04775() {
      return this.internalField1055;
   }

   @Generated
   public int internalMethod08730() {
      return this.internalField1056;
   }

   @Generated
   public int internalMethod08733() {
      return this.internalField1054;
   }

   @Generated
   public float internalMethod04772() {
      return this.internalField0205;
   }

   @Generated
   public float internalMethod04774() {
      return this.internalField0206;
   }

   @Generated
   public float internalMethod08729() {
      return this.internalField1048;
   }

   @Generated
   public float internalMethod08732() {
      return this.internalField1047;
   }

   @Generated
   public boolean internalMethod08742() {
      return this.internalField1516;
   }

   @Generated
   public BooleanSetting internalMethod01921() {
      return this.internalField0650;
   }

   @Generated
   public SliderSetting internalMethod01315() {
      return this.internalField0383;
   }

   @Generated
   public SliderSetting internalMethod00354() {
      return this.internalField0382;
   }

   @Generated
   public BooleanSetting internalMethod02598() {
      return this.internalField0651;
   }

   @Generated
   public BooleanSetting internalMethod08686() {
      return this.internalField1261;
   }

   @Generated
   public BooleanSetting internalMethod08829() {
      return this.internalField1263;
   }

   @Generated
   public ColorSetting internalMethod01978() {
      return this.internalField0665;
   }

   @Generated
   public List<GradientColorSetting> internalMethod06326() {
      return this.internalField0416;
   }

   @Generated
   public boolean internalMethod09228() {
      return this.internalField1517;
   }

   @Generated
   public GradientColorSetting internalMethod01979() {
      return this.internalField0666;
   }

   @Generated
   public EventListener<Render3DEvent> internalMethod00909() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<PreHudRenderEvent> internalMethod00898() {
      return this.internalField0158;
   }

   @Generated
   public static void internalMethod00006(PlayerTargetType localValue0) {
      internalField0025 = localValue0;
   }

   @Generated
   public static PlayerTargetType internalMethod06287() {
      return internalField0025;
   }

   @Generated
   public static void internalMethod05055(ItemTargetType localValue0) {
      internalField0012 = localValue0;
   }

   @Generated
   public static ItemTargetType internalMethod06229() {
      return internalField0012;
   }

   @Generated
   public static void internalMethod00072(EntityTargetType localValue0) {
      internalField0027 = localValue0;
   }

   @Generated
   public static EntityTargetType internalMethod06288() {
      return internalField0027;
   }

   static class InternalType0168 extends GradientColorSetting {
      private final Consumer<GradientColorSetting> internalField0922;

      InternalType0168(SettingOwner localValue1, String localValue2, BooleanSupplier localValue3, Consumer<GradientColorSetting> localValue4) {
         super(localValue1, localValue2, localValue3);
         this.internalField0922 = localValue4;
      }

      @Override
      public GradientColorSetting internalMethod01227(ColorRGBA localValue1) {
         super.internalMethod01227(localValue1);
         if (this.internalField0922 != null) {
            this.internalField0922.accept(this);
         }

         return this;
      }

      @Override
      public GradientColorSetting internalMethod04884(ColorRGBA localValue1) {
         super.internalMethod04884(localValue1);
         if (this.internalField0922 != null) {
            this.internalField0922.accept(this);
         }

         return this;
      }
   }
}
