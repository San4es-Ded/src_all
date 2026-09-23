package rockstar.modules.combat;







import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import rockstar.client.compat.RenderSystem;
import java.util.List;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Back Track",
   internalMethod09633 = "modules.descriptions.back_track",
   category = ModuleCategory.COMBAT
)
public class BackTrackModule extends Module {
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (internalField0149.world != null && internalField0149.player != null) {
         long localValue2 = System.currentTimeMillis();
         boolean localValue4 = this.internalMethod09464();

         for (Entity localValue6 : internalField0149.world.getEntities()) {
            if (localValue6 instanceof BackTrackDataAccess localValue7) {
               List localValue8 = localValue7.rockstar2_0$getBackTracks();
               if (localValue4) {
                  this.internalMethod06335(localValue6, localValue8, localValue2);
               } else if (!this.internalMethod01795(localValue6)) {
                  localValue8.clear();
               } else {
                  this.internalMethod06335(localValue6, localValue8, localValue2);
               }
            }
         }
      }
   };
   private final EventListener<Render3DEvent> internalField0158 = localValue1 -> {
      if (this.internalField0650.internalMethod04496()) {
         if (internalField0149.world != null && internalField0149.player != null) {
            if (!this.internalMethod09464()) {
               MatrixStack localValue2 = localValue1.getMatrices();
               Vec3d localValue3 = internalField0149.gameRenderer.getCamera().getCameraPos();

               for (PlayerEntity localValue5 : internalField0149.world.getPlayers()) {
                  if (localValue5 != internalField0149.player
                     && !RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue5.getName().getString())
                     && localValue5 instanceof BackTrackDataAccess localValue6) {
                     List localValue7 = localValue6.rockstar2_0$getBackTracks();
                     if (!localValue7.isEmpty()) {
                        long localValue8 = System.currentTimeMillis();
                        this.internalMethod06335(localValue5, localValue7, localValue8);
                        if (!localValue7.isEmpty()) {
                           Vec3d localValue10 = this.internalMethod05573(localValue5, localValue7);
                           if (localValue10 != null) {
                              BufferBuilder localValue11 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
                              localValue2.push();
                              RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
                              RenderSystem.disableCull();
                              RenderSystem.enableBlend();
                              RenderSystem.defaultBlendFunc();
                              Render3DUtils.internalMethod08795(
                                 localValue2,
                                 localValue11,
                                 localValue5.getBoundingBox().offset(localValue10.subtract(localValue5.getEntityPos())).offset(-localValue3.x, -localValue3.y, -localValue3.z),
                                 ColorRGBA.WHITE.withAlpha(180.0F)
                              );
                              BuiltBuffer localValue12 = localValue11.endNullable();
                              if (localValue12 != null) {
                                 BufferRenderer.drawWithGlobalProgram(localValue12);
                              }

                              RenderSystem.enableCull();
                              RenderSystem.disableBlend();
                              localValue2.pop();
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   };

   public BackTrackModule() {
      this.internalMethod09463();
   }

   private void internalMethod09463() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.backtrack.visual");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.backtrack.autoreset");
      this.internalField1261 = new BooleanSetting(this, "Ping Based");
      this.internalField1263 = new BooleanSetting(this, "TPS Based");
      this.internalField0383 = new SliderSetting(this, "Delay")
         .internalMethod06240("ms")
         .internalMethod05900(50.0F)
         .internalMethod02732(1200.0F)
         .internalMethod08673(25.0F)
         .internalMethod08074(150.0F);
      this.internalField0382 = new SliderSetting(this, "Ping Multiplier", () -> !this.internalField1261.internalMethod04496())
         .internalMethod06240("x")
         .internalMethod05900(0.6F)
         .internalMethod02732(2.0F)
         .internalMethod08673(0.1F)
         .internalMethod08074(1.1F);
      this.internalField1142 = new SliderSetting(this, "Min TPS", () -> !this.internalField1263.internalMethod04496())
         .internalMethod06240("")
         .internalMethod05900(14.0F)
         .internalMethod02732(20.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(17.0F);
   }

   private int internalMethod08072() {
      if (internalField0149.getNetworkHandler() != null && internalField0149.player != null) {
         PlayerListEntry localValue1 = internalField0149.getNetworkHandler().getPlayerListEntry(internalField0149.player.getUuid());
         return localValue1 != null ? localValue1.getLatency() : 0;
      } else {
         return 0;
      }
   }

   private int internalMethod05478(PlayerEntity localValue1) {
      if (internalField0149.getNetworkHandler() == null) {
         return 0;
      } else {
         PlayerListEntry localValue2 = internalField0149.getNetworkHandler().getPlayerListEntry(localValue1.getUuid());
         return localValue2 != null ? localValue2.getLatency() : 0;
      }
   }

   private float internalMethod01772() {
      ServerTpsTracker localValue1 = RockstarClient.getInstance().internalMethod06191();
      return localValue1 != null ? localValue1.internalMethod00956() : 20.0F;
   }

   private boolean internalMethod09464() {
      return this.internalField1263.internalMethod04496() && this.internalMethod01772() < this.internalField1142.internalMethod08576();
   }

   private long internalMethod01794(Entity localValue1) {
      long localValue2 = (long)this.internalField0383.internalMethod08576();
      if (this.internalField1261.internalMethod04496() && localValue1 instanceof PlayerEntity localValue4) {
         int localValue5 = this.internalMethod08072() + this.internalMethod05478(localValue4);
         long localValue6 = (long)(localValue5 * this.internalField0382.internalMethod08576());
         localValue2 = Math.clamp(localValue6, (long)this.internalField0383.internalMethod05288(), (long)this.internalField0383.internalMethod05291());
      }

      if (this.internalField1263.internalMethod04496()) {
         float localValue8 = this.internalMethod01772();
         if (localValue8 > 0.0F) {
            float localValue9 = MathHelper.clamp(
               20.0F / localValue8, 1.0F, this.internalField0383.internalMethod05291() / Math.max(this.internalField0383.internalMethod05288(), (float)localValue2)
            );
            localValue2 = (long)((float)localValue2 * localValue9);
         }
      }

      return Math.clamp(localValue2, (long)this.internalField0383.internalMethod05288(), (long)this.internalField0383.internalMethod05291());
   }

   private Vec3d internalMethod05573(PlayerEntity localValue1, List<BackTrackModule.InternalType0403> localValue2) {
      if (localValue2.isEmpty()) {
         return null;
      } else {
         Vec3d localValue3 = ((BackTrackModule.InternalType0403)localValue2.getLast()).internalMethod00937();
         if (!this.internalField0651.internalMethod04496()) {
            return localValue3;
         } else {
            Vec3d localValue4 = internalField0149.player.getEyePos();
            Vec3d localValue5 = localValue1.getEntityPos();
            Box localValue6 = localValue1.getBoundingBox();
            double localValue7 = this.internalMethod01478(localValue4, localValue6);

            for (int localValue9 = localValue2.size() - 1; localValue9 >= 0; localValue9--) {
               BackTrackModule.InternalType0403 localValue10 = (BackTrackModule.InternalType0403)localValue2.get(localValue9);
               Box localValue11 = localValue6.offset(localValue10.internalMethod00937().subtract(localValue5));
               double localValue12 = this.internalMethod01478(localValue4, localValue11);
               if (localValue12 < localValue7) {
                  return localValue10.internalMethod00937();
               }
            }

            return null;
         }
      }
   }

   private double internalMethod01478(Vec3d localValue1, Box localValue2) {
      double localValue3 = this.internalMethod03386(localValue1, localValue2);
      double localValue5 = this.internalMethod07910(localValue1, localValue2);
      double localValue7 = Math.min(localValue3 / 6.0, 1.0);
      double localValue9 = Math.min(localValue5 / 180.0, 1.0);
      return localValue7 * 0.6 + localValue9 * 0.4;
   }

   private double internalMethod03386(Vec3d localValue1, Box localValue2) {
      double localValue3 = MathHelper.clamp(localValue1.x, localValue2.minX, localValue2.maxX);
      double localValue5 = MathHelper.clamp(localValue1.y, localValue2.minY, localValue2.maxY);
      double localValue7 = MathHelper.clamp(localValue1.z, localValue2.minZ, localValue2.maxZ);
      return localValue1.distanceTo(new Vec3d(localValue3, localValue5, localValue7));
   }

   private double internalMethod07910(Vec3d localValue1, Box localValue2) {
      float localValue3 = internalField0149.player.getYaw();
      float localValue4 = internalField0149.player.getPitch();
      Vec3d localValue5 = this.internalMethod04071(localValue4, localValue3);
      Vec3d localValue6 = this.internalMethod02955(localValue1, localValue2);
      Vec3d localValue7 = localValue6.subtract(localValue1).normalize();
      double localValue8 = localValue5.dotProduct(localValue7);
      localValue8 = MathHelper.clamp(localValue8, -1.0, 1.0);
      return Math.toDegrees(Math.acos(localValue8));
   }

   private Vec3d internalMethod02955(Vec3d localValue1, Box localValue2) {
      double localValue3 = MathHelper.clamp(localValue1.x, localValue2.minX, localValue2.maxX);
      double localValue5 = MathHelper.clamp(localValue1.y, localValue2.minY, localValue2.maxY);
      double localValue7 = MathHelper.clamp(localValue1.z, localValue2.minZ, localValue2.maxZ);
      return new Vec3d(localValue3, localValue5, localValue7);
   }

   private Vec3d internalMethod04071(float localValue1, float localValue2) {
      float localValue3 = (float)Math.toRadians(localValue1);
      float localValue4 = (float)Math.toRadians(localValue2);
      float localValue5 = MathHelper.cos(-localValue4 - (float) Math.PI);
      float localValue6 = MathHelper.sin(-localValue4 - (float) Math.PI);
      float localValue7 = MathHelper.cos(-localValue3);
      float localValue8 = MathHelper.sin(-localValue3);
      return new Vec3d(localValue6 * localValue7, localValue8, localValue5 * localValue7);
   }

   public Vec3d internalMethod03167(Entity localValue1) {
      if (!this.isEnabled()) {
         return null;
      } else if (internalField0149.player == null) {
         return null;
      } else if (this.internalMethod09464()) {
         return null;
      } else if (localValue1 instanceof BackTrackDataAccess localValue2) {
         if (localValue1 instanceof PlayerEntity localValue3) {
            if (!this.internalMethod01795(localValue1)) {
               return null;
            } else {
               List localValue4 = localValue2.rockstar2_0$getBackTracks();
               if (localValue4.isEmpty()) {
                  return null;
               } else {
                  long localValue5 = System.currentTimeMillis();
                  this.internalMethod06335(localValue1, localValue4, localValue5);
                  return localValue4.isEmpty() ? null : this.internalMethod05573(localValue3, localValue4);
               }
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Override
   public void onDisable() {
      this.internalMethod09465();
   }

   public boolean internalMethod01795(Entity localValue1) {
      if (!this.isEnabled()) {
         return false;
      } else if (internalField0149.player == null) {
         return false;
      } else if (localValue1 instanceof PlayerEntity localValue2) {
         if (localValue2 == internalField0149.player) {
            return false;
         } else {
            return RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue2.getName().getString()) ? false : !this.internalMethod09464();
         }
      } else {
         return false;
      }
   }

   public void internalMethod01533(Entity localValue1, Vec3d localValue2, long localValue3) {
      if (this.internalMethod01795(localValue1)) {
         if (localValue1 instanceof BackTrackDataAccess localValue5) {
            List localValue6 = localValue5.rockstar2_0$getBackTracks();
            this.internalMethod06335(localValue1, localValue6, localValue3);
            localValue6.add(new BackTrackModule.InternalType0403(localValue2, localValue3));
         }
      }
   }

   public void internalMethod07322(Entity localValue1, Vec3d localValue2, long localValue3) {
      if (localValue1 instanceof BackTrackDataAccess localValue5) {
         List localValue6 = localValue5.rockstar2_0$getBackTracks();
         localValue6.clear();
         if (this.internalMethod01795(localValue1)) {
            localValue6.add(new BackTrackModule.InternalType0403(localValue2, localValue3));
         }
      }
   }

   private void internalMethod06335(Entity localValue1, List<BackTrackModule.InternalType0403> localValue2, long localValue3) {
      long localValue5 = this.internalMethod01794(localValue1);
      localValue2.removeIf(localValue4 -> localValue3 - localValue4.internalMethod02302() > localValue5);
   }

   private void internalMethod09465() {
      if (internalField0149.world != null) {
         for (Entity localValue2 : internalField0149.world.getEntities()) {
            if (localValue2 instanceof BackTrackDataAccess localValue3) {
               localValue3.rockstar2_0$getBackTracks().clear();
            }
         }
      }
   }

   public static final class InternalType0403 {
      private final Vec3d internalField0283;
      private final long internalField0229;

      public InternalType0403(Vec3d localValue1, long localValue2) {
         this.internalField0283 = localValue1;
         this.internalField0229 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0403[pos=" + this.internalField0283 + ", time=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         BackTrackModule.InternalType0403 other = (BackTrackModule.InternalType0403) localValue1;
         return java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public Vec3d internalMethod00937() {
         return this.internalField0283;
      }

      public long internalMethod02302() {
         return this.internalField0229;
      }
   }
}
