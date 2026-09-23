package rockstar.modules.combat;









import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.GameRendererEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Aim Bot",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.aim_bot"
)
public class AimBotModule extends Module {
   private static final int internalField0227 = 80;
   private static final double internalField0194 = 12.0;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private MultiSelectSetting.InternalType0091 internalField1072;
   private MultiSelectSetting.InternalType0091 internalField1491;
   private MultiSelectSetting.InternalType0091 internalField1488;
   private MultiSelectSetting.InternalType0091 internalField1490;
   private MultiSelectSetting.InternalType0091 internalField1489;
   private boolean internalField0277 = false;
   private int internalField1056 = 0;
   private Rotation internalField0118 = new Rotation(0.0F, 0.0F);
   private Rotation internalField0119 = null;
   private long internalField0229 = 0L;
   private int internalField1054 = -1;
   private int internalField1464 = -1;
   private final Deque<Vec3d> internalField0796 = new ArrayDeque<>();
   private Box internalField0681 = null;
   private boolean internalField0276 = false;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (!this.internalMethod09543()) {
            this.internalField0277 = false;
            this.internalField1056 = 0;
            this.internalField0119 = null;
            this.internalField0681 = null;
            this.internalField1464 = -1;
         } else {
            boolean localValue2 = internalField0149.player.isUsingItem();
            if (localValue2 && !this.internalField0277) {
               this.internalField1056 = 1;
               this.internalField0118 = new Rotation(internalField0149.player.getYaw(), internalField0149.player.getPitch());
            }

            this.internalField0277 = localValue2;
            if (this.internalField1056 > 0) {
               this.internalField1056--;
               this.internalMethod00358(this.internalField0118);
               this.internalField0681 = null;
            } else {
               EntityTargetFilter localValue3 = new EntityTargetFilter.InternalType0309()
                  .internalMethod00547(this.internalField1074.isSelected())
                  .internalMethod06455(this.internalField1073.isSelected())
                  .internalMethod08543(this.internalField1072.isSelected())
                  .internalMethod07990(this.internalField1491.isSelected())
                  .internalMethod09114(this.internalField1488.isSelected())
                  .internalMethod09525(this.internalField1490.isSelected())
                  .internalMethod08126(this.internalField1489.isSelected())
                  .internalMethod03468(this.internalField0383.internalMethod08576())
                  .internalMethod03528();
               AimBotModule.InternalType0053 localValue4 = this.internalMethod03408(localValue3);
               if (localValue4 == null) {
                  this.internalField0119 = null;
                  this.internalField0681 = null;
               } else {
                  this.internalMethod00358(localValue4.internalField0118);
               }
            }
         }
      }
   };
   private final EventListener<GameRendererEvent> internalField0158 = localValue1 -> {
      if (internalField0149.player != null) {
         if (!this.internalField1261.internalMethod04496()) {
            if (this.internalField0119 == null) {
               this.internalField0229 = 0L;
            } else {
               long localValue2 = System.nanoTime();
               float localValue4;
               if (this.internalField0229 == 0L) {
                  localValue4 = 0.016666668F;
               } else {
                  localValue4 = MathHelper.clamp((float)(localValue2 - this.internalField0229) / 1.0E9F, 0.004166667F, 0.1F);
               }

               this.internalField0229 = localValue2;
               float localValue5 = 20.0F;
               float localValue6 = MathHelper.clamp(1.0F - (float)Math.exp(-localValue5 * localValue4), 0.01F, 0.95F);
               float localValue7 = MathHelper.wrapDegrees(this.internalField0119.internalMethod00169() - internalField0149.player.getYaw());
               float localValue8 = MathHelper.clamp(this.internalField0119.internalMethod00171(), -89.9F, 89.9F) - internalField0149.player.getPitch();
               float localValue9 = internalField0149.player.getYaw() + localValue7 * localValue6;
               float localValue10 = MathHelper.clamp(internalField0149.player.getPitch() + localValue8 * localValue6, -90.0F, 90.0F);
               internalField0149.player.setYaw(localValue9);
               internalField0149.player.setPitch(localValue10);
               internalField0149.player.setHeadYaw(localValue9);
               internalField0149.player.setBodyYaw(localValue9);
            }
         }
      }
   };
   private final EventListener<Render3DEvent> internalField1028 = localValue1 -> {
      if (this.internalField0651.internalMethod04496() && this.internalField0650.internalMethod04496()) {
         if (this.internalField0681 != null && internalField0149.player != null) {
            ColorRGBA localValue2 = ThemeColors.internalMethod02531();
            MatrixStack localValue3 = localValue1.getMatrices();
            localValue3.push();
            HudRenderUtils.internalMethod02691(true);
            HudRenderUtils.internalMethod01900(localValue3);
            RenderSystem.enableDepthTest();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            Camera localValue4 = internalField0149.gameRenderer.getCamera();
            Vec3d localValue5 = localValue4.getCameraPos();
            Box localValue6 = this.internalField0681.offset(-localValue5.getX(), -localValue5.getY(), -localValue5.getZ());
            localValue3.push();
            localValue3.translate(localValue5.getX(), localValue5.getY(), localValue5.getZ());
            BufferBuilder localValue7 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            Render3DUtils.internalMethod02535(localValue3, localValue7, localValue6, localValue2.mulAlpha(0.25F));
            HudRenderUtils.internalMethod05816(localValue7);
            BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            Render3DUtils.internalMethod08795(localValue3, localValue8, localValue6, localValue2);
            HudRenderUtils.internalMethod05816(localValue8);
            localValue3.pop();
            HudRenderUtils.internalMethod04670();
            localValue3.pop();
         }
      }
   };

   public AimBotModule() {
      this.internalMethod09542();
   }

   private void internalMethod09542() {
      MultiSelectSetting localValue1 = new MultiSelectSetting(this, "modules.settings.aim_bot.items");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(localValue1, "modules.settings.aim_bot.bow").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(localValue1, "modules.settings.aim_bot.crossbow").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(localValue1, "modules.settings.aim_bot.trident").select();
      this.internalField0650 = new BooleanSetting(this, "modules.settings.aim_bot.predict").internalMethod06630();
      this.internalField0651 = new BooleanSetting(this, "modules.settings.aim_bot.draw_predicted_box", () -> !this.internalField0650.internalMethod04496())
         .internalMethod06630();
      this.internalField1261 = new BooleanSetting(this, "modules.settings.aim_bot.silent_aim");
      this.internalField0383 = new SliderSetting(this, "modules.settings.aim_bot.distance")
         .internalMethod05900(0.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(30.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.aim_bot.fov")
         .internalMethod05900(1.0F)
         .internalMethod02732(180.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(90.0F);
      MultiSelectSetting localValue2 = new MultiSelectSetting(this, "targets");
      this.internalField1074 = new MultiSelectSetting.InternalType0091(localValue2, "players").select();
      this.internalField1073 = new MultiSelectSetting.InternalType0091(localValue2, "animals").select();
      this.internalField1072 = new MultiSelectSetting.InternalType0091(localValue2, "mobs").select();
      this.internalField1491 = new MultiSelectSetting.InternalType0091(localValue2, "invisibles").select();
      this.internalField1488 = new MultiSelectSetting.InternalType0091(localValue2, "nakedPlayers").select();
      this.internalField1489 = new MultiSelectSetting.InternalType0091(localValue2, "rockUsers");
      this.internalField1490 = new MultiSelectSetting.InternalType0091(localValue2, "friends");
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.internalField0119 = null;
      this.internalField0681 = null;
      this.internalField0796.clear();
      this.internalField1054 = -1;
      this.internalField1464 = -1;
      this.internalField0229 = 0L;
   }

   private AimBotModule.InternalType0053 internalMethod03408(EntityTargetFilter localValue1) {
      float localValue2 = this.internalMethod02241();
      if (localValue2 < 0.5F) {
         this.internalField0681 = null;
         return null;
      } else {
         List localValue3 = this.internalMethod00287(localValue1);
         if (localValue3.isEmpty()) {
            this.internalField1464 = -1;
            this.internalField0681 = null;
            return null;
         } else {
            AimBotModule.InternalType0053 localValue4 = null;
            int localValue5 = Math.min(localValue3.size(), 3);

            for (int localValue6 = 0; localValue6 < localValue5; localValue6++) {
               LivingEntity localValue7 = (LivingEntity)localValue3.get(localValue6);
               boolean localValue8 = this.internalField1464 != -1 ? localValue7.getId() == this.internalField1464 : localValue6 == 0;
               AimBotModule.InternalType0053 localValue9 = this.internalMethod03075(localValue7, localValue2, localValue8);
               if (localValue9 != null) {
                  if (localValue9.internalMethod03128()) {
                     this.internalField1464 = localValue9.internalMethod03127();
                     this.internalField0681 = localValue9.internalMethod07073();
                     return localValue9;
                  }

                  if (localValue4 == null) {
                     localValue4 = localValue9;
                  }
               }
            }

            if (localValue4 == null) {
               this.internalField0681 = null;
               return null;
            } else {
               this.internalField1464 = localValue4.internalMethod03127();
               this.internalField0681 = localValue4.internalMethod07073();
               return localValue4;
            }
         }
      }
   }

   private List<LivingEntity> internalMethod00287(EntityTargetFilter localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      Vec3d localValue3 = internalField0149.player.getRotationVec(1.0F).normalize();
      double localValue4 = MathHelper.clamp(this.internalField0382.internalMethod08576(), 1.0F, 180.0F) * 0.5;
      TargetSelectionManager localValue6 = RockstarClient.getInstance().internalMethod04463();
      ArrayList localValue7 = new ArrayList();

      for (Entity localValue9 : internalField0149.world.getEntities()) {
         if (localValue9 instanceof LivingEntity localValue10 && localValue1.internalMethod05417(localValue9)) {
            boolean localValue11 = localValue10.getId() == this.internalField1464;
            double localValue12 = this.internalMethod07408(localValue2, localValue3, localValue10);
            if (!(localValue12 > (localValue11 ? localValue4 + 12.0 : localValue4))) {
               double localValue14 = localValue12 + localValue2.distanceTo(localValue10.getBoundingBox().getCenter()) * 0.05;
               if (localValue11) {
                  localValue14 -= 8.0;
               }

               if (localValue6 != null && localValue6.internalMethod04582(localValue10.getName().getString())) {
                  localValue14 -= 1000.0;
               }

               localValue7.add(new AimBotModule.InternalType0054(localValue10, localValue14));
            }
         }
      }

      localValue7.sort(Comparator.comparingDouble(AimBotModule.InternalType0054::internalMethod04977));
      ArrayList localValue16 = new ArrayList(localValue7.size());

      for (AimBotModule.InternalType0054 localValue18 : (Iterable<AimBotModule.InternalType0054>)(Iterable<?>)localValue7) {
         localValue16.add(localValue18.internalMethod04957());
      }

      return localValue16;
   }

   private double internalMethod07408(Vec3d localValue1, Vec3d localValue2, LivingEntity localValue3) {
      Vec3d localValue4 = localValue3.getBoundingBox().getCenter().subtract(localValue1);
      double localValue5 = localValue4.length();
      if (localValue5 < 1.0E-4) {
         return 0.0;
      } else {
         double localValue7 = MathHelper.clamp(localValue2.dotProduct(localValue4.multiply(1.0 / localValue5)), -1.0, 1.0);
         double localValue9 = Math.toDegrees(Math.acos(localValue7));
         double localValue11 = Math.toDegrees(Math.atan2(Math.max(localValue3.getWidth() * 0.5, localValue3.getHeight() * 0.35), localValue5));
         return Math.max(0.0, localValue9 - localValue11);
      }
   }

   private AimBotModule.InternalType0053 internalMethod03075(LivingEntity localValue1, float localValue2, boolean localValue3) {
      Vec3d localValue4 = internalField0149.player.getEyePos();
      Vec3d localValue5 = this.internalField0650.internalMethod04496() ? this.internalMethod03275(localValue1, localValue4, localValue2, localValue3).subtract(localValue1.getEntityPos()) : Vec3d.ZERO;
      Box localValue6 = localValue1.getBoundingBox().offset(localValue5.x, localValue5.y, localValue5.z);
      Rotation localValue7 = null;
      Rotation localValue8 = null;
      double localValue9 = Double.MAX_VALUE;
      double localValue11 = Double.MAX_VALUE;
      List localValue13 = this.internalMethod06636(localValue1, localValue5);

      for (int localValue14 = 0; localValue14 < localValue13.size(); localValue14++) {
         Rotation localValue15 = this.internalMethod05861(localValue4, (Vec3d)localValue13.get(localValue14), localValue2);
         if (localValue15 != null) {
            double localValue16 = this.internalMethod00357(localValue15) + localValue14 * 0.75;
            if (localValue16 < localValue11) {
               localValue11 = localValue16;
               localValue8 = localValue15;
            }

            if (localValue16 < localValue9 && this.internalMethod00326(localValue4, localValue15, localValue2, localValue1, localValue5)) {
               localValue9 = localValue16;
               localValue7 = localValue15;
            }
         }
      }

      if (localValue7 != null) {
         return new AimBotModule.InternalType0053(localValue7, localValue6, true, localValue1.getId());
      } else {
         return localValue8 != null ? new AimBotModule.InternalType0053(localValue8, localValue6, false, localValue1.getId()) : null;
      }
   }

   private Vec3d internalMethod03275(LivingEntity localValue1, Vec3d localValue2, float localValue3, boolean localValue4) {
      Vec3d localValue5 = localValue4 ? this.internalMethod02059(localValue1) : new Vec3d(localValue1.getX() - localValue1.lastX, localValue1.getY() - localValue1.lastY, localValue1.getZ() - localValue1.lastZ);
      Vec3d localValue6 = localValue1.getEntityPos();
      boolean localValue7 = !localValue1.isOnGround() && !localValue1.isClimbing() && !localValue1.isTouchingWater() && !localValue1.hasNoGravity();
      double localValue8 = localValue4 ? this.internalMethod02240() : 0.9;
      Vec3d localValue10 = new Vec3d(localValue5.x * localValue8, 0.0, localValue5.z * localValue8);
      double localValue11 = localValue5.y;
      double localValue13 = localValue2.distanceTo(localValue6) / localValue3;
      Vec3d localValue15 = localValue6;

      for (int localValue16 = 0; localValue16 < 6; localValue16++) {
         Vec3d localValue17 = this.internalMethod02231(localValue1, localValue6, localValue10, localValue13);
         double localValue18 = this.internalMethod06641(localValue11, localValue13, localValue7);
         localValue15 = localValue6.add(localValue17.x, localValue18, localValue17.z);
         double localValue20 = this.internalMethod02239(localValue2, localValue15, localValue3);
         if (!Double.isNaN(localValue20) && !(localValue20 < 0.0)) {
            if (Math.abs(localValue20 - localValue13) < 0.05) {
               break;
            }

            localValue13 = localValue20;
         } else {
            localValue13 = localValue2.distanceTo(localValue15) / localValue3;
         }
      }

      return localValue15;
   }

   private double internalMethod02240() {
      if (this.internalField0276) {
         return 0.9;
      } else {
         int localValue1 = this.internalField0796.size();
         if (localValue1 < 3) {
            return 0.9;
         } else {
            Vec3d[] localValue2 = this.internalField0796.toArray(new Vec3d[0]);
            double localValue3 = 0.0;

            for (int localValue5 = 1; localValue5 < localValue1; localValue5++) {
               double localValue6 = localValue2[localValue5].x - localValue2[localValue5 - 1].x;
               double localValue8 = localValue2[localValue5].z - localValue2[localValue5 - 1].z;
               localValue3 += Math.sqrt(localValue6 * localValue6 + localValue8 * localValue8);
            }

            double localValue11 = localValue2[localValue1 - 1].x - localValue2[0].x;
            double localValue7 = localValue2[localValue1 - 1].z - localValue2[0].z;
            double localValue9 = Math.sqrt(localValue11 * localValue11 + localValue7 * localValue7);
            return localValue3 < 0.05 ? 1.0 : MathHelper.clamp(localValue9 / localValue3, 0.2, 1.0);
         }
      }
   }

   private double internalMethod06641(double localValue1, double localValue3, boolean localValue5) {
      if (!localValue5) {
         return localValue1 * localValue3;
      } else {
         int localValue6 = (int)Math.floor(localValue3);
         double localValue7 = localValue3 - localValue6;
         double localValue9 = localValue1;
         double localValue11 = 0.0;

         for (int localValue13 = 0; localValue13 < localValue6; localValue13++) {
            localValue9 -= 0.08F;
            localValue11 += localValue9;
            localValue9 *= 0.98F;
         }

         if (localValue7 > 0.001) {
            double localValue16 = localValue9 - 0.08F;
            localValue11 += localValue16 * localValue7;
         }

         return localValue11;
      }
   }

   private Vec3d internalMethod02231(LivingEntity localValue1, Vec3d localValue2, Vec3d localValue3, double localValue4) {
      double localValue6 = Math.sqrt(localValue3.x * localValue3.x + localValue3.z * localValue3.z) * localValue4;
      if (localValue6 < 0.05) {
         return new Vec3d(localValue3.x * localValue4, 0.0, localValue3.z * localValue4);
      } else {
         Vec3d localValue8 = localValue2.add(0.0, localValue1.getHeight() * 0.5, 0.0);
         Vec3d localValue9 = localValue8.add(localValue3.x * localValue4, 0.0, localValue3.z * localValue4);
         BlockHitResult localValue10 = internalField0149.world.raycast(new RaycastContext(localValue8, localValue9, ShapeType.COLLIDER, FluidHandling.NONE, localValue1));
         if (localValue10.getType() == Type.BLOCK) {
            double localValue11 = localValue10.getPos().distanceTo(localValue8) - 0.3;
            if (localValue11 <= 0.0) {
               return Vec3d.ZERO;
            } else {
               double localValue13 = localValue11 / localValue6;
               return new Vec3d(localValue3.x * localValue4 * localValue13, 0.0, localValue3.z * localValue4 * localValue13);
            }
         } else {
            return new Vec3d(localValue3.x * localValue4, 0.0, localValue3.z * localValue4);
         }
      }
   }

   private Vec3d internalMethod02059(LivingEntity localValue1) {
      if (this.internalField1054 != localValue1.getId()) {
         this.internalField0796.clear();
         this.internalField0276 = false;
         this.internalField1054 = localValue1.getId();
      }

      this.internalField0796.addLast(localValue1.getEntityPos());

      while (this.internalField0796.size() > 6) {
         this.internalField0796.removeFirst();
      }

      if (this.internalField0796.size() < 2) {
         this.internalField0276 = false;
         return new Vec3d(localValue1.getX() - localValue1.lastX, localValue1.getY() - localValue1.lastY, localValue1.getZ() - localValue1.lastZ);
      } else {
         Vec3d[] localValue2 = this.internalField0796.toArray(new Vec3d[0]);
         int localValue3 = localValue2.length;
         Vec3d localValue4 = this.internalMethod00878(localValue2);
         Vec3d localValue5 = localValue2[localValue3 - 1].subtract(localValue2[localValue3 - 2]);
         double localValue6 = localValue5.x * localValue5.x + localValue5.z * localValue5.z;
         double localValue8 = localValue4.x * localValue4.x + localValue4.z * localValue4.z;
         if (localValue6 > 0.005 && localValue8 > 0.0025) {
            double localValue10 = (localValue5.x * localValue4.x + localValue5.z * localValue4.z) / (Math.sqrt(localValue6) * Math.sqrt(localValue8));
            if (localValue10 < 0.3) {
               this.internalField0276 = true;
               return localValue5;
            }
         }

         this.internalField0276 = false;
         return localValue4;
      }
   }

   private Vec3d internalMethod00878(Vec3d[] localValue1) {
      int localValue2 = localValue1.length;
      double localValue3 = 0.0;
      double localValue5 = 0.0;
      double localValue7 = 0.0;
      double localValue9 = 0.0;

      for (int localValue11 = 1; localValue11 < localValue2; localValue11++) {
         double localValue12 = localValue11;
         localValue3 += (localValue1[localValue11].x - localValue1[localValue11 - 1].x) * localValue12;
         localValue5 += (localValue1[localValue11].y - localValue1[localValue11 - 1].y) * localValue12;
         localValue7 += (localValue1[localValue11].z - localValue1[localValue11 - 1].z) * localValue12;
         localValue9 += localValue12;
      }

      if (localValue9 < 1.0E-6) {
         return Vec3d.ZERO;
      } else {
         double localValue14 = 1.0 / localValue9;
         return new Vec3d(localValue3 * localValue14, localValue5 * localValue14, localValue7 * localValue14);
      }
   }

   private double internalMethod02239(Vec3d localValue1, Vec3d localValue2, double localValue3) {
      double localValue5 = localValue2.x - localValue1.x;
      double localValue7 = localValue2.z - localValue1.z;
      double localValue9 = localValue2.y - localValue1.y;
      double localValue11 = Math.sqrt(localValue5 * localValue5 + localValue7 * localValue7);
      if (localValue11 < 1.0E-4) {
         return Double.NaN;
      } else {
         double localValue13 = localValue3 * localValue3;
         double localValue15 = 0.05F;
         double localValue17 = localValue13 * localValue13 - localValue15 * (localValue15 * localValue11 * localValue11 + 2.0 * localValue9 * localValue13);
         if (localValue17 < 0.0) {
            return localValue11 / localValue3;
         } else {
            double localValue19 = (localValue13 - Math.sqrt(localValue17)) / (localValue15 * localValue11);
            double localValue21 = 1.0 / Math.sqrt(1.0 + localValue19 * localValue19);
            double localValue23 = localValue3 * localValue21;
            if (localValue23 < 1.0E-4) {
               return localValue11 / localValue3;
            } else {
               double localValue25 = 0.00999999F;
               double localValue27 = localValue11 * localValue25 / localValue23;
               return localValue27 >= 0.999 ? localValue11 / localValue23 * 1.5 : Math.log(1.0 - localValue27) / Math.log(0.99F);
            }
         }
      }
   }

   private Rotation internalMethod05861(Vec3d localValue1, Vec3d localValue2, float localValue3) {
      double localValue4 = localValue2.x - localValue1.x;
      double localValue6 = localValue2.z - localValue1.z;
      double localValue8 = localValue2.y - localValue1.y;
      double localValue10 = Math.sqrt(localValue4 * localValue4 + localValue6 * localValue6);
      if (localValue10 < 1.0E-4) {
         return null;
      } else {
         double localValue12 = localValue3 * localValue3;
         double localValue14 = 0.05F;
         double localValue16 = localValue12 * localValue12 - localValue14 * (localValue14 * localValue10 * localValue10 + 2.0 * localValue8 * localValue12);
         if (localValue16 < 0.0) {
            return null;
         } else {
            double localValue18 = (localValue12 - Math.sqrt(localValue16)) / (localValue14 * localValue10);
            float localValue20 = (float)(Math.toDegrees(Math.atan2(localValue6, localValue4)) - 90.0);
            float localValue21 = (float)(-Math.toDegrees(Math.atan(localValue18)));
            localValue21 = MathHelper.clamp(localValue21, -89.9F, 89.9F);
            return new Rotation(localValue20, localValue21);
         }
      }
   }

   private boolean internalMethod00326(Vec3d localValue1, Rotation localValue2, float localValue3, LivingEntity localValue4, Vec3d localValue5) {
      double localValue6 = Math.toRadians(localValue2.internalMethod00169());
      double localValue8 = Math.toRadians(localValue2.internalMethod00171());
      double localValue10 = Math.cos(localValue8);
      Vec3d localValue12 = new Vec3d(-Math.sin(localValue6) * localValue10, -Math.sin(localValue8), Math.cos(localValue6) * localValue10).multiply(localValue3);
      Vec3d localValue13 = localValue1;
      Box localValue14 = localValue4.getBoundingBox().offset(localValue5.x, localValue5.y, localValue5.z).expand(0.1);
      int localValue15 = MathHelper.clamp((int)(localValue1.distanceTo(localValue14.getCenter()) / localValue3 * 3.0) + 10, 20, 80);
      double localValue16 = localValue14.minY - 5.0;

      for (int localValue18 = 0; localValue18 < localValue15; localValue18++) {
         Vec3d localValue19 = localValue13.add(localValue12);
         if (localValue19.y < localValue16 && localValue12.y < 0.0) {
            return false;
         }

         Optional localValue20 = localValue14.raycast(localValue13, localValue19);
         BlockHitResult localValue21 = internalField0149.world
            .raycast(new RaycastContext(localValue13, localValue19, ShapeType.COLLIDER, FluidHandling.NONE, internalField0149.player));
         if (localValue20.isPresent()) {
            if (localValue21.getType() == Type.BLOCK) {
               double localValue22 = localValue21.getPos().squaredDistanceTo(localValue13);
               double localValue24 = ((Vec3d)localValue20.get()).squaredDistanceTo(localValue13);
               if (localValue22 < localValue24) {
                  return false;
               }
            }

            return true;
         }

         if (localValue21.getType() == Type.BLOCK) {
            return false;
         }

         localValue13 = localValue19;
         localValue12 = localValue12.multiply(0.99F);
         localValue12 = new Vec3d(localValue12.x, localValue12.y - 0.05F, localValue12.z);
      }

      return false;
   }

   private List<Vec3d> internalMethod06636(LivingEntity localValue1, Vec3d localValue2) {
      Box localValue3 = localValue1.getBoundingBox().offset(localValue2.x, localValue2.y, localValue2.z);
      Vec3d localValue4 = localValue3.getCenter();
      double localValue5 = localValue1.getY() + localValue2.y;
      double localValue7 = localValue1.getHeight();
      ArrayList localValue9 = new ArrayList(8);
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.85, localValue4.z));
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.65, localValue4.z));
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.5, localValue4.z));
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.3, localValue4.z));
      localValue9.add(new Vec3d(localValue3.minX + 0.1, localValue5 + localValue7 * 0.55, localValue4.z));
      localValue9.add(new Vec3d(localValue3.maxX - 0.1, localValue5 + localValue7 * 0.55, localValue4.z));
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.55, localValue3.minZ + 0.1));
      localValue9.add(new Vec3d(localValue4.x, localValue5 + localValue7 * 0.55, localValue3.maxZ - 0.1));
      return localValue9;
   }

   private float internalMethod00357(Rotation localValue1) {
      double localValue2 = Math.toRadians(localValue1.internalMethod00169());
      double localValue4 = Math.toRadians(localValue1.internalMethod00171());
      double localValue6 = Math.cos(localValue4);
      Vec3d localValue8 = new Vec3d(-Math.sin(localValue2) * localValue6, -Math.sin(localValue4), Math.cos(localValue2) * localValue6);
      Vec3d localValue9 = internalField0149.player.getRotationVec(1.0F).normalize();
      double localValue10 = MathHelper.clamp(localValue9.dotProduct(localValue8), -1.0, 1.0);
      return (float)Math.toDegrees(Math.acos(localValue10));
   }

   private float internalMethod02241() {
      Item localValue1 = internalField0149.player.getMainHandStack().getItem();
      if (localValue1 == Items.BOW) {
         return 3.0F;
      } else if (localValue1 == Items.CROSSBOW) {
         return 3.15F;
      } else {
         return localValue1 == Items.TRIDENT ? 2.5F : 3.0F;
      }
   }

   private void internalMethod00358(Rotation localValue1) {
      float localValue2 = MathHelper.clamp(localValue1.internalMethod00171(), -89.9F, 89.9F);
      if (this.internalField1261.internalMethod04496()) {
         float localValue3 = RockstarClient.getInstance().internalMethod02368().internalMethod09074().internalMethod00169();
         float localValue4 = localValue3 + MathHelper.wrapDegrees(localValue1.internalMethod00169() - localValue3);
         RockstarClient.getInstance()
            .internalMethod02368()
            .internalMethod00418(
               new Rotation(localValue4, localValue2), RotationBehavior.internalField0114, 120.0F, 120.0F, 120.0F, RotationPriority.internalField1010
            );
         this.internalField0119 = null;
      } else {
         this.internalField0119 = new Rotation(localValue1.internalMethod00169(), localValue2);
      }
   }

   private boolean internalMethod09543() {
      ItemStack localValue1 = internalField0149.player.getMainHandStack();
      Item localValue2 = localValue1.getItem();
      if (localValue2 == Items.BOW && this.internalField0245.isSelected()) {
         return internalField0149.player.isUsingItem();
      } else if (localValue2 == Items.CROSSBOW && this.internalField0244.isSelected()) {
         return internalField0149.player.isUsingItem() || CrossbowItem.isCharged(localValue1);
      } else {
         return localValue2 == Items.TRIDENT && this.internalField1075.isSelected() ? internalField0149.player.isUsingItem() : false;
      }
   }

   static final class InternalType0053 {
      final Rotation internalField0118;
      private final Box internalField0681;
      private final boolean internalField0277;
      private final int internalField0227;

      InternalType0053(Rotation localValue1, Box localValue2, boolean localValue3, int localValue4) {
         this.internalField0118 = localValue1;
         this.internalField0681 = localValue2;
         this.internalField0277 = localValue3;
         this.internalField0227 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0053[rotation=" + this.internalField0118 + ", box=" + this.internalField0681 + ", hits=" + this.internalField0277 + ", targetId=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0118);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0681);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AimBotModule.InternalType0053 other = (AimBotModule.InternalType0053) localValue1;
         return java.util.Objects.equals(this.internalField0118, other.internalField0118)
            && java.util.Objects.equals(this.internalField0681, other.internalField0681)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Rotation internalMethod00185() {
         return this.internalField0118;
      }

      public Box internalMethod07073() {
         return this.internalField0681;
      }

      public boolean internalMethod03128() {
         return this.internalField0277;
      }

      public int internalMethod03127() {
         return this.internalField0227;
      }
   }

   static final class InternalType0054 {
      private final LivingEntity internalField0505;
      private final double internalField0194;

      InternalType0054(LivingEntity localValue1, double localValue2) {
         this.internalField0505 = localValue1;
         this.internalField0194 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0054[entity=" + this.internalField0505 + ", score=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0505);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AimBotModule.InternalType0054 other = (AimBotModule.InternalType0054) localValue1;
         return java.util.Objects.equals(this.internalField0505, other.internalField0505)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public LivingEntity internalMethod04957() {
         return this.internalField0505;
      }

      public double internalMethod04977() {
         return this.internalField0194;
      }
   }
}
