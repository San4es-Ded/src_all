package rockstar.client.internal.script;















import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import moscow.rockstar.mixin.accessors.ItemCooldownEntryAccessor;
import moscow.rockstar.mixin.accessors.ItemCooldownManagerAccessor;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import org.joml.Matrix4f;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

public class ItemBindsHud extends AbstractHudWidget {
   private static final ColorRGBA internalField0777 = new ColorRGBA(255.0F, 65.0F, 65.0F);
   private static final ColorRGBA internalField0776 = new ColorRGBA(255.0F, 120.0F, 95.0F);
   private static final float internalField0205 = 0.5F;
   private final Map<Integer, AnimatedValue> internalField0543 = new HashMap<>();
   private final Map<Integer, AnimatedValue> internalField0544 = new HashMap<>();
   private final Map<Integer, BlockPos> internalField1197 = new HashMap<>();
   private long internalField0229;
   private final SliderSetting internalField0383 = new SliderSetting(this, "hud.item_binds.per_row")
      .internalMethod05900(1.0F)
      .internalMethod02732(5.0F)
      .internalMethod08673(1.0F)
      .internalMethod08074(4.0F);
   private final EventListener<Render3DEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.currentScreen == null) {
         AssistModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
         if (localValue2 != null && localValue2.isEnabled()) {
            if (localValue2.internalMethod02374().internalMethod04496()) {
               this.internalMethod08466(localValue1);
               AssistSwapItem localValue3 = null;
               AssistSwapItem localValue4 = null;

               for (AssistSwapItem localValue6 : localValue2.internalMethod08262()) {
                  if (localValue6.internalMethod03236()
                     && (
                        localValue6 instanceof TrapkaSwapItem
                           || localValue6 instanceof PlastAssist
                           || localValue6 instanceof DezorentAssist
                           || localValue6 instanceof PilbAssist
                           || localValue6 instanceof AuraAssist
                           || localValue6 instanceof StanAssist
                           || localValue6 instanceof SnowAssist
                           || localValue6 instanceof BoomTrapAssist
                     )) {
                     if (localValue3 == null && localValue6.internalMethod03234() != -1 && this.internalMethod00491(localValue6.internalMethod03234())) {
                        localValue3 = localValue6;
                     } else if (localValue4 == null
                        && (
                           localValue6.internalMethod05467(internalField0149.player.getMainHandStack())
                              || localValue6.internalMethod05467(internalField0149.player.getOffHandStack())
                        )) {
                        localValue4 = localValue6;
                     }
                  }
               }

               AssistSwapItem localValue7 = localValue3 != null ? localValue3 : localValue4;
               if (localValue7 instanceof TrapkaSwapItem) {
                  this.internalMethod06246(localValue1, localValue7.internalMethod06489().getItem() == Items.POPPED_CHORUS_FRUIT ? "hw_trap_ender" : "ft_trap_default");
               } else if (localValue7 instanceof PlastAssist) {
                  this.internalMethod07846(localValue1);
               } else if (localValue7 instanceof DezorentAssist || localValue7 instanceof PilbAssist) {
                  this.internalMethod03993(localValue1, 10, 10.0);
               } else if (localValue7 instanceof AuraAssist) {
                  this.internalMethod08002(localValue1);
               } else if (localValue7 instanceof StanAssist) {
                  this.internalMethod00203(localValue1);
               } else if (localValue7 instanceof SnowAssist) {
                  if (!this.internalMethod03193()) {
                     return;
                  }

                  this.internalField0229 = System.currentTimeMillis() + 2500L;
                  this.internalMethod08649(localValue1);
               } else if (localValue7 instanceof BoomTrapAssist) {
                  this.internalMethod07441(localValue1);
               }
            }
         }
      }
   };
   private final Mutable internalField0232 = new Mutable();
   private static final float internalField0206 = 8.0F;
   private static final float internalField1048 = 2.0F;
   private static ColorRGBA internalMethod01555(ColorRGBA localValue0) {
      return new ColorRGBA(Math.min(255.0F, localValue0.getRed() + 55.0F), Math.min(255.0F, localValue0.getGreen() + 50.0F), Math.min(255.0F, localValue0.getBlue() + 40.0F));
   }

   public ItemBindsHud() {
      super("hud.item_binds", "hud/clock");
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   @Override
   public void renderComponent(UiRenderContext localValue1) {
      AssistModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      boolean localValue3 = internalField0149.currentScreen instanceof ChatScreen;
      if (localValue2 != null || localValue3) {
         List<ItemKeybindEntry> localValue4 = localValue2 == null ? new ArrayList<>() : this.internalMethod05076(localValue2);
         if (localValue4.isEmpty() && localValue3) {
            localValue4 = this.internalMethod02849();
         }

         if (!localValue4.isEmpty()) {
            this.internalMethod01316(localValue1, localValue4);
         }
      }
   }

   private void internalMethod06246(Render3DEvent localValue1, String localValue2) {
      BlockPos localValue3 = internalField0149.player.getBlockPos();
      Box localValue4 = new Box(localValue3.getX() - 1, localValue3.getY(), localValue3.getZ() - 1, localValue3.getX() + 2, localValue3.getY() + 3, localValue3.getZ() + 2);
      this.internalMethod05761(localValue1, localValue2, localValue3, this.internalMethod00538(localValue4, localValue1.getTickDelta()));
   }

   private void internalMethod05761(Render3DEvent localValue1, String localValue2, BlockPos localValue3, boolean localValue4) {
      SchematicRenderer.internalMethod04303(localValue2).internalMethod06730(localValue1, localValue3, localValue4 ? internalField0777 : ColorRGBA.WHITE, 0.5F);
   }

   private boolean internalMethod00538(Box localValue1, float localValue2) {
      for (AbstractClientPlayerEntity localValue4 : internalField0149.world.getPlayers()) {
         if (localValue4 != internalField0149.player
            && localValue4.isAlive()
            && !RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue4.getName().getString())) {
            Vec3d localValue5 = ScreenProjectionUtils.internalMethod02822(localValue4, localValue2);
            Box localValue6 = localValue4.getBoundingBox().offset(localValue5.subtract(localValue4.getEntityPos()));
            if (localValue1.intersects(localValue6)) {
               return true;
            }
         }
      }

      return false;
   }

   private void internalMethod00203(Render3DEvent localValue1) {
      BlockPos localValue2 = internalField0149.player.getBlockPos();
      Box localValue3 = new Box(localValue2.getX() - 15, localValue2.getY() - 15, localValue2.getZ() - 15, localValue2.getX() + 15, localValue2.getY() + 15, localValue2.getZ() + 15).expand(0.002);
      boolean localValue4 = this.internalMethod00538(localValue3, localValue1.getTickDelta());
      ColorRGBA localValue5 = ThemeColors.internalMethod02531();
      ColorRGBA localValue6 = localValue4 ? internalField0777 : localValue5;
      ColorRGBA localValue7 = localValue4 ? internalField0776 : internalMethod01555(localValue5);
      Vec3d localValue8 = internalField0149.gameRenderer.getCamera().getCameraPos();
      Box localValue9 = localValue3.offset(-localValue8.x, -localValue8.y, -localValue8.z);
      localValue1.getMatrices().push();
      this.internalMethod00492(false);
      BufferBuilder localValue10 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      Render3DUtils.internalMethod05375(localValue1.getMatrices(), localValue10, localValue9, localValue6.withAlpha(8.0F), localValue7.withAlpha(20.0F));
      this.internalMethod00167(localValue10);
      RenderSystem.lineWidth(2.5F);
      BufferBuilder localValue11 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      Render3DUtils.internalMethod05921(localValue1.getMatrices(), localValue11, localValue9, localValue6.withAlpha(95.0F), localValue7.withAlpha(170.0F));
      Render3DUtils.internalMethod08795(localValue1.getMatrices(), localValue11, localValue9.expand(-0.05), localValue7.withAlpha(40.0F));
      this.internalMethod00167(localValue11);
      RenderSystem.lineWidth(1.0F);
      this.internalMethod03191();
      localValue1.getMatrices().pop();
   }

   private void internalMethod07441(Render3DEvent localValue1) {
      if (this.internalMethod03192()) {
         this.internalMethod05761(
            localValue1, "explosion_trap_ground", internalField0149.player.getBlockPos().up(3), this.internalMethod01253(6.0, 1.0, localValue1.getTickDelta())
         );
      } else {
         this.internalMethod06246(localValue1, "explosion_trap_air");
      }
   }

   private boolean internalMethod03192() {
      if (internalField0149.player.isTouchingWater()) {
         return false;
      } else if (internalField0149.player.isOnGround()) {
         return true;
      } else {
         BlockPos localValue1 = internalField0149.player.getBlockPos();
         return this.internalMethod05135(localValue1.down()) || this.internalMethod05135(localValue1.down(2));
      }
   }

   private boolean internalMethod05135(BlockPos localValue1) {
      return !internalField0149.world.getBlockState(localValue1).getCollisionShape(internalField0149.world, localValue1).isEmpty();
   }

   private void internalMethod08649(Render3DEvent localValue1) {
      if (this.internalMethod03193()) {
         PredictionModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(PredictionModule.class);
         if (localValue2 != null) {
            Vec3d localValue3 = localValue2.internalMethod01554();
            if (localValue3 != null) {
               this.internalMethod05794(localValue1, BlockPos.ofFloored(localValue3), 7, 7.0, 1.0);
            }
         }
      }
   }

   private void internalMethod08466(Render3DEvent localValue1) {
      if (!this.internalMethod03193()) {
         this.internalField1197.clear();
      } else {
         PredictionModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(PredictionModule.class);
         if (localValue2 != null) {
            this.internalMethod05753(localValue2);

            for (BlockPos localValue4 : this.internalField1197.values()) {
               this.internalMethod05794(localValue1, localValue4, 7, 7.0, 1.0);
            }
         }
      }
   }

   private void internalMethod05753(PredictionModule localValue1) {
      HashSet localValue2 = new HashSet();
      long localValue3 = System.currentTimeMillis();

      for (Entity localValue6 : internalField0149.world.getEntities()) {
         if (localValue6 instanceof SnowballEntity localValue7 && !localValue7.isRemoved()) {
            int localValue8 = localValue7.getId();
            localValue2.add(localValue8);
            if (!this.internalField1197.containsKey(localValue8) && this.internalMethod05928(localValue7, localValue3)) {
               Vec3d localValue9 = localValue1.internalMethod07524(localValue7);
               if (localValue9 != null) {
                  this.internalField1197.put(localValue8, BlockPos.ofFloored(localValue9));
               }
            }
         }
      }

      this.internalField1197.keySet().removeIf(localValue1x -> !localValue2.contains(localValue1x));
   }

   private boolean internalMethod05928(SnowballEntity localValue1, long localValue2) {
      return localValue1.getOwner() == internalField0149.player ? true : localValue2 <= this.internalField0229 && localValue1.squaredDistanceTo(internalField0149.player) <= 64.0;
   }

   private boolean internalMethod03193() {
      return ServerUtils.internalMethod01786(KnownServer.internalField1566);
   }

   private void internalMethod08002(Render3DEvent localValue1) {
      BlockPos localValue2 = internalField0149.player.getBlockPos();
      Vec3d localValue3 = new Vec3d(localValue2.getX() + 0.5, localValue2.getY(), localValue2.getZ() + 0.5);
      boolean localValue4 = this.internalMethod04385(2.0, 2.0, localValue1.getTickDelta());
      ColorRGBA localValue5 = ThemeColors.internalMethod02531();
      ColorRGBA localValue6 = localValue4 ? internalField0777 : localValue5;
      ColorRGBA localValue7 = localValue4 ? internalField0776 : internalMethod01555(localValue5);
      Vec3d localValue8 = localValue3.subtract(internalField0149.gameRenderer.getCamera().getCameraPos());
      localValue1.getMatrices().push();
      this.internalMethod00492(false);
      BufferBuilder localValue9 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      this.internalMethod01805(localValue9, localValue1.getMatrices().peek().getPositionMatrix(), localValue8, 2.0, 2.0, localValue6.withAlpha(14.0F), localValue7.withAlpha(32.0F));
      this.internalMethod00167(localValue9);
      RenderSystem.lineWidth(2.5F);
      BufferBuilder localValue10 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      this.internalMethod00455(localValue10, localValue1.getMatrices().peek().getPositionMatrix(), localValue8, 2.0, 2.0, localValue6.withAlpha(120.0F), localValue7.withAlpha(200.0F));
      this.internalMethod00167(localValue10);
      RenderSystem.lineWidth(1.0F);
      this.internalMethod03191();
      localValue1.getMatrices().pop();
   }

   private boolean internalMethod04385(double localValue1, double localValue3, float localValue5) {
      BlockPos localValue6 = internalField0149.player.getBlockPos();
      Vec3d localValue7 = new Vec3d(localValue6.getX() + 0.5, localValue6.getY(), localValue6.getZ() + 0.5);
      double localValue8 = localValue1 * localValue1;

      for (AbstractClientPlayerEntity localValue11 : internalField0149.world.getPlayers()) {
         if (localValue11 != internalField0149.player
            && localValue11.isAlive()
            && RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue11.getName().getString())) {
            Vec3d localValue12 = ScreenProjectionUtils.internalMethod02822(localValue11, localValue5);
            Box localValue13 = localValue11.getBoundingBox().offset(localValue12.subtract(localValue11.getEntityPos()));
            double localValue14 = Math.clamp(localValue7.x, localValue13.minX, localValue13.maxX);
            double localValue16 = Math.clamp(localValue7.z, localValue13.minZ, localValue13.maxZ);
            boolean localValue18 = localValue13.maxY >= localValue7.y && localValue13.minY <= localValue7.y + localValue3;
            if (localValue18 && new Vec3d(localValue14, localValue7.y, localValue16).squaredDistanceTo(localValue7) <= localValue8) {
               return true;
            }
         }
      }

      return false;
   }

   private void internalMethod01805(BufferBuilder localValue1, Matrix4f localValue2, Vec3d localValue3, double localValue4, double localValue6, ColorRGBA localValue8, ColorRGBA localValue9) {
      for (int localValue10 = 0; localValue10 < 96; localValue10++) {
         double localValue11 = (Math.PI * 2) * localValue10 / 96.0;
         double localValue13 = (Math.PI * 2) * (localValue10 + 1) / 96.0;
         Vec3d localValue15 = localValue3.add(Math.cos(localValue11) * localValue4, 0.0, Math.sin(localValue11) * localValue4);
         Vec3d localValue16 = localValue3.add(Math.cos(localValue13) * localValue4, 0.0, Math.sin(localValue13) * localValue4);
         Vec3d localValue17 = localValue15.add(0.0, localValue6, 0.0);
         Vec3d localValue18 = localValue16.add(0.0, localValue6, 0.0);
         this.internalMethod02135(localValue2, localValue1, localValue15, localValue8);
         this.internalMethod02135(localValue2, localValue1, localValue16, localValue8);
         this.internalMethod02135(localValue2, localValue1, localValue18, localValue9);
         this.internalMethod02135(localValue2, localValue1, localValue17, localValue9);
      }
   }

   private void internalMethod00455(BufferBuilder localValue1, Matrix4f localValue2, Vec3d localValue3, double localValue4, double localValue6, ColorRGBA localValue8, ColorRGBA localValue9) {
      for (int localValue10 = 0; localValue10 < 96; localValue10++) {
         double localValue11 = (Math.PI * 2) * localValue10 / 96.0;
         double localValue13 = (Math.PI * 2) * (localValue10 + 1) / 96.0;
         Vec3d localValue15 = localValue3.add(Math.cos(localValue11) * localValue4, 0.0, Math.sin(localValue11) * localValue4);
         Vec3d localValue16 = localValue3.add(Math.cos(localValue13) * localValue4, 0.0, Math.sin(localValue13) * localValue4);
         Vec3d localValue17 = localValue15.add(0.0, localValue6, 0.0);
         Vec3d localValue18 = localValue16.add(0.0, localValue6, 0.0);
         this.internalMethod02135(localValue2, localValue1, localValue15, localValue8);
         this.internalMethod02135(localValue2, localValue1, localValue16, localValue8);
         this.internalMethod02135(localValue2, localValue1, localValue17, localValue9);
         this.internalMethod02135(localValue2, localValue1, localValue18, localValue9);
         if (localValue10 % 12 == 0) {
            this.internalMethod02135(localValue2, localValue1, localValue15, localValue8);
            this.internalMethod02135(localValue2, localValue1, localValue17, localValue9);
         }
      }
   }

   private void internalMethod02135(Matrix4f localValue1, BufferBuilder localValue2, Vec3d localValue3, ColorRGBA localValue4) {
      localValue2.vertex(localValue1, (float)localValue3.x, (float)localValue3.y, (float)localValue3.z)
         .color(localValue4.getRed() / 255.0F, localValue4.getGreen() / 255.0F, localValue4.getBlue() / 255.0F, localValue4.getAlpha() / 255.0F);
   }

   private void internalMethod03993(Render3DEvent localValue1, int localValue2, double localValue3) {
      this.internalMethod00062(localValue1, localValue2, localValue3, 10.0);
   }

   private void internalMethod00062(Render3DEvent localValue1, int localValue2, double localValue3, double localValue5) {
      this.internalMethod05794(localValue1, internalField0149.player.getBlockPos(), localValue2, localValue3, localValue5);
   }

   private void internalMethod05794(Render3DEvent localValue1, BlockPos localValue2, int localValue3, double localValue4, double localValue6) {
      List localValue8 = this.internalMethod05581(localValue2, localValue3);
      Vec3d localValue9 = new Vec3d(localValue2.getX() + 0.5, localValue2.getY(), localValue2.getZ() + 0.5);
      boolean localValue10 = this.internalMethod04542(localValue9, localValue4, localValue6, localValue1.getTickDelta());
      ColorRGBA localValue11 = ThemeColors.internalMethod02531();
      ColorRGBA localValue12 = localValue10 ? internalField0777 : localValue11;
      ColorRGBA localValue13 = localValue10 ? internalField0776 : internalMethod01555(localValue11);
      Vec3d localValue14 = internalField0149.gameRenderer.getCamera().getCameraPos();
      localValue1.getMatrices().push();
      this.internalMethod00492(this.internalMethod04623(localValue8));
      BufferBuilder localValue15 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

      for (Box localValue17 : (Iterable<Box>)(Iterable<?>)localValue8) {
         Render3DUtils.internalMethod05375(
            localValue1.getMatrices(), localValue15, localValue17.offset(-localValue14.x, -localValue14.y, -localValue14.z), localValue12.withAlpha(10.0F), localValue13.withAlpha(24.0F)
         );
      }

      this.internalMethod00167(localValue15);
      RenderSystem.lineWidth(2.5F);
      BufferBuilder localValue20 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

      for (Box localValue18 : (Iterable<Box>)(Iterable<?>)localValue8) {
         Box localValue19 = localValue18.offset(-localValue14.x, -localValue14.y, -localValue14.z);
         Render3DUtils.internalMethod05921(localValue1.getMatrices(), localValue20, localValue19, localValue12.withAlpha(105.0F), localValue13.withAlpha(180.0F));
      }

      this.internalMethod00167(localValue20);
      RenderSystem.lineWidth(1.0F);
      this.internalMethod03191();
      localValue1.getMatrices().pop();
   }

   private List<Box> internalMethod05581(BlockPos localValue1, int localValue2) {
      ArrayList localValue3 = new ArrayList();
      int localValue4 = Integer.MIN_VALUE;
      int localValue5 = Integer.MIN_VALUE;
      int localValue6 = Math.max(16, localValue2 * 8);

      for (int localValue7 = 0; localValue7 < localValue6; localValue7++) {
         double localValue8 = (Math.PI * 2) * localValue7 / localValue6;
         int localValue10 = (int)Math.round(Math.cos(localValue8) * localValue2);
         int localValue11 = (int)Math.round(Math.sin(localValue8) * localValue2);
         if (localValue10 != localValue4 || localValue11 != localValue5) {
            int localValue12 = localValue1.getX() + localValue10;
            int localValue13 = localValue1.getZ() + localValue11;
            int localValue14 = this.internalMethod04398(localValue12, localValue13, localValue1.getY());
            localValue3.add(new Box(localValue12, localValue14, localValue13, localValue12 + 1, localValue14 + 1, localValue13 + 1).expand(0.002));
            localValue4 = localValue10;
            localValue5 = localValue11;
         }
      }

      return localValue3;
   }

   private int internalMethod04398(int localValue1, int localValue2, int localValue3) {
      if (internalField0149.world == null) {
         return localValue3;
      } else {
         for (int localValue4 = 0; localValue4 <= 10; localValue4++) {
            if (this.internalMethod04399(localValue1, localValue3 - localValue4, localValue2)) {
               return localValue3 - localValue4;
            }

            if (localValue4 != 0 && this.internalMethod04399(localValue1, localValue3 + localValue4, localValue2)) {
               return localValue3 + localValue4;
            }
         }

         return localValue3;
      }
   }

   private boolean internalMethod04399(int localValue1, int localValue2, int localValue3) {
      this.internalField0232.set(localValue1, localValue2 - 1, localValue3);
      if (internalField0149.world.getBlockState(this.internalField0232).getCollisionShape(internalField0149.world, this.internalField0232).isEmpty()) {
         return false;
      } else {
         this.internalField0232.set(localValue1, localValue2, localValue3);
         return internalField0149.world.getBlockState(this.internalField0232).getCollisionShape(internalField0149.world, this.internalField0232).isEmpty();
      }
   }

   private void internalMethod07846(Render3DEvent localValue1) {
      List localValue2 = this.internalMethod00085(localValue1.getTickDelta());
      boolean localValue3 = this.internalMethod01542(localValue2, localValue1.getTickDelta());
      ColorRGBA localValue4 = ThemeColors.internalMethod02531();
      ColorRGBA localValue5 = localValue3 ? internalField0777 : localValue4;
      ColorRGBA localValue6 = localValue3 ? internalField0776 : internalMethod01555(localValue4);
      Vec3d localValue7 = internalField0149.gameRenderer.getCamera().getCameraPos();
      localValue1.getMatrices().push();
      this.internalMethod00492(this.internalMethod04623(localValue2));
      BufferBuilder localValue8 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

      for (Box localValue10 : (Iterable<Box>)(Iterable<?>)localValue2) {
         Render3DUtils.internalMethod05375(localValue1.getMatrices(), localValue8, localValue10.offset(-localValue7.x, -localValue7.y, -localValue7.z), localValue5.withAlpha(16.0F), localValue6.withAlpha(38.0F));
      }

      this.internalMethod00167(localValue8);
      RenderSystem.lineWidth(2.5F);
      BufferBuilder localValue13 = Tessellator.getInstance().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

      for (Box localValue11 : (Iterable<Box>)(Iterable<?>)localValue2) {
         Box localValue12 = localValue11.offset(-localValue7.x, -localValue7.y, -localValue7.z);
         Render3DUtils.internalMethod05921(localValue1.getMatrices(), localValue13, localValue12, localValue5.withAlpha(125.0F), localValue6.withAlpha(205.0F));
         Render3DUtils.internalMethod08795(localValue1.getMatrices(), localValue13, localValue12.expand(-0.03), localValue6.withAlpha(55.0F));
      }

      this.internalMethod00167(localValue13);
      RenderSystem.lineWidth(1.0F);
      this.internalMethod03191();
      localValue1.getMatrices().pop();
   }

   private List<Box> internalMethod00085(float localValue1) {
      BlockPos localValue2 = internalField0149.player.getBlockPos();
      float localValue3 = internalField0149.player.getPitch(localValue1);
      int localValue4 = Math.floorMod(Math.round(internalField0149.player.getYaw(localValue1) / 45.0F), 8);
      if (Math.abs(localValue3) >= 60.0F) {
         int localValue5 = localValue2.getY() + (localValue3 < 0.0F ? 3 : -2);
         return List.of(new Box(localValue2.getX() - 2, localValue5, localValue2.getZ() - 2, localValue2.getX() + 3, localValue5 + 2, localValue2.getZ() + 3).expand(0.002));
      } else {
         return localValue4 % 2 == 0 ? List.of(this.internalMethod00397(localValue2, localValue4).expand(0.002)) : this.internalMethod02072(localValue2, localValue4);
      }
   }

   private Box internalMethod00397(BlockPos localValue1, int localValue2) {
      return switch (localValue2) {
         case 0 -> new Box(localValue1.getX() - 2, localValue1.getY() - 1, localValue1.getZ() + 2, localValue1.getX() + 3, localValue1.getY() + 4, localValue1.getZ() + 4);
         default -> new Box(localValue1);
         case 2 -> new Box(localValue1.getX() - 4, localValue1.getY() - 1, localValue1.getZ() - 2, localValue1.getX() - 2, localValue1.getY() + 4, localValue1.getZ() + 3);
         case 4 -> new Box(localValue1.getX() - 2, localValue1.getY() - 1, localValue1.getZ() - 4, localValue1.getX() + 3, localValue1.getY() + 4, localValue1.getZ() - 2);
         case 6 -> new Box(localValue1.getX() + 2, localValue1.getY() - 1, localValue1.getZ() - 2, localValue1.getX() + 4, localValue1.getY() + 4, localValue1.getZ() + 3);
      };
   }

   private List<Box> internalMethod02072(BlockPos localValue1, int localValue2) {
      ArrayList localValue3 = new ArrayList();
      int[][] localValue4 = this.internalMethod04393(localValue2);

      for (int[] localValue8 : localValue4) {
         localValue3.add(
            new Box(localValue1.getX() + localValue8[0], localValue1.getY() - 1, localValue1.getZ() + localValue8[1], localValue1.getX() + localValue8[2], localValue1.getY() + 4, localValue1.getZ() + localValue8[3]).expand(0.002)
         );
      }

      return localValue3;
   }

   private int[][] internalMethod04393(int localValue1) {
      return switch (localValue1) {
         case 1 -> new int[][]{{-4, 0, -3, 1}, {-3, 1, -2, 2}, {-2, 2, -1, 3}, {-1, 3, 0, 4}, {0, 4, 1, 5}};
         default -> new int[0][0];
         case 3 -> new int[][]{{0, -4, 1, -3}, {-1, -3, 0, -2}, {-2, -2, -1, -1}, {-3, -1, -2, 0}, {-4, 0, -3, 1}};
         case 5 -> new int[][]{{4, 0, 5, 1}, {3, -1, 4, 0}, {2, -2, 3, -1}, {1, -3, 2, -2}, {0, -4, 1, -3}};
         case 7 -> new int[][]{{0, 4, 1, 5}, {1, 3, 2, 4}, {2, 2, 3, 3}, {3, 1, 4, 2}, {4, 0, 5, 1}};
      };
   }

   private boolean internalMethod01542(List<Box> localValue1, float localValue2) {
      for (AbstractClientPlayerEntity localValue4 : internalField0149.world.getPlayers()) {
         if (localValue4 != internalField0149.player
            && localValue4.isAlive()
            && !RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue4.getName().getString())) {
            Vec3d localValue5 = ScreenProjectionUtils.internalMethod02822(localValue4, localValue2);
            Box localValue6 = localValue4.getBoundingBox().offset(localValue5.subtract(localValue4.getEntityPos()));

            for (Box localValue8 : localValue1) {
               if (localValue8.intersects(localValue6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean internalMethod01253(double localValue1, double localValue3, float localValue5) {
      BlockPos localValue6 = internalField0149.player.getBlockPos();
      Vec3d localValue7 = new Vec3d(localValue6.getX() + 0.5, localValue6.getY(), localValue6.getZ() + 0.5);
      return this.internalMethod04542(localValue7, localValue1, localValue3, localValue5);
   }

   private boolean internalMethod04542(Vec3d localValue1, double localValue2, double localValue4, float localValue6) {
      double localValue7 = localValue2 * localValue2;

      for (AbstractClientPlayerEntity localValue10 : internalField0149.world.getPlayers()) {
         if (localValue10 != internalField0149.player
            && localValue10.isAlive()
            && !RockstarClient.getInstance().internalMethod03375().internalMethod00380(localValue10.getName().getString())) {
            Vec3d localValue11 = ScreenProjectionUtils.internalMethod02822(localValue10, localValue6);
            Box localValue12 = localValue10.getBoundingBox().offset(localValue11.subtract(localValue10.getEntityPos()));
            double localValue13 = Math.clamp(localValue1.x, localValue12.minX, localValue12.maxX);
            double localValue15 = Math.clamp(localValue1.z, localValue12.minZ, localValue12.maxZ);
            boolean localValue17 = localValue12.maxY >= localValue1.y && localValue12.minY <= localValue1.y + localValue4;
            if (localValue17 && new Vec3d(localValue13, localValue1.y, localValue15).squaredDistanceTo(localValue1) <= localValue7) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean internalMethod04623(List<Box> localValue1) {
      for (Box localValue3 : localValue1) {
         if (!internalField0149.world.getFluidState(BlockPos.ofFloored(localValue3.getCenter())).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod00492(boolean localValue1) {
      RenderSystem.enableBlend();
      if (!localValue1 && !internalField0149.player.isTouchingWater()) {
         RenderSystem.enableDepthTest();
      } else {
         RenderSystem.disableDepthTest();
      }

      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
   }

   private void internalMethod03191() {
      RenderSystem.defaultBlendFunc();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private void internalMethod00167(BufferBuilder localValue1) {
      BuiltBuffer localValue2 = localValue1.endNullable();
      if (localValue2 != null) {
         BufferRenderer.drawWithGlobalProgram(localValue2);
      }
   }

   private List<ItemKeybindEntry> internalMethod02849() {
      return List.of(
         ServerUtils.internalMethod01786(KnownServer.internalField1218) && !ServerUtils.internalMethod06501("holytime")
            ? new ItemKeybindEntry(Items.POPPED_CHORUS_FRUIT, -1)
            : new ItemKeybindEntry(Items.NETHERITE_SCRAP, -1),
         new ItemKeybindEntry(Items.ENDER_EYE, -1),
         new ItemKeybindEntry(Items.SUGAR, -1)
      );
   }

   private List<ItemKeybindEntry> internalMethod05076(AssistModule localValue1) {
      HashMap localValue2 = new HashMap();

      for (AssistSwapItem localValue4 : localValue1.internalMethod08262()) {
         if (localValue4.internalMethod03236()) {
            int localValue5 = localValue4.internalMethod03234();
            if (localValue5 != -1) {
               Item localValue6 = localValue4.internalMethod06489().getItem();
               localValue2.put(localValue5, new ItemKeybindEntry(localValue6, localValue5));
            }
         }
      }

      ArrayList localValue7 = new ArrayList(localValue2.values());
      HashSet localValue8 = new HashSet();

      for (ItemKeybindEntry localValue10 : (Iterable<ItemKeybindEntry>)(Iterable<?>)localValue7) {
         localValue8.add(localValue10.internalMethod05091());
      }

      this.internalMethod07423(localValue7, localValue8);
      return localValue7;
   }

   private void internalMethod07423(List<ItemKeybindEntry> localValue1, Set<Item> localValue2) {
      if (internalField0149.player != null) {
         ItemCooldownManagerAccessor localValue3 = (ItemCooldownManagerAccessor)(Object)internalField0149.player.getItemCooldownManager();

         for (Identifier localValue5 : localValue3.rockstar$getEntries().keySet()) {
            Item localValue6 = (Item)Registries.ITEM.get(localValue5);
            this.internalMethod05296(localValue1, localValue2, localValue6);
         }

         for (int localValue7 = 0; localValue7 < internalField0149.player.getInventory().size(); localValue7++) {
            this.internalMethod03806(localValue1, localValue2, internalField0149.player.getInventory().getStack(localValue7));
         }

         this.internalMethod03806(localValue1, localValue2, internalField0149.player.getOffHandStack());
      }
   }

   private void internalMethod03806(List<ItemKeybindEntry> localValue1, Set<Item> localValue2, ItemStack localValue3) {
      if (localValue3 != null && !localValue3.isEmpty()) {
         if (internalField0149.player.getItemCooldownManager().isCoolingDown(localValue3)) {
            this.internalMethod05296(localValue1, localValue2, localValue3.getItem());
         }
      }
   }

   private void internalMethod05296(List<ItemKeybindEntry> localValue1, Set<Item> localValue2, Item localValue3) {
      if (localValue3 != Items.AIR && !localValue2.contains(localValue3)) {
         if (!(this.internalMethod03761(localValue3) <= 0.0F)) {
            if (this.internalMethod03762(localValue3)) {
               localValue1.add(new ItemKeybindEntry(localValue3, -1));
               localValue2.add(localValue3);
            }
         }
      }
   }

   private boolean internalMethod03762(Item localValue1) {
      if (internalField0149.player == null) {
         return false;
      } else {
         for (int localValue2 = 0; localValue2 < internalField0149.player.getInventory().size(); localValue2++) {
            if (internalField0149.player.getInventory().getStack(localValue2).getItem() == localValue1) {
               return true;
            }
         }

         return internalField0149.player.getOffHandStack().getItem() == localValue1;
      }
   }

   private void internalMethod01316(UiRenderContext localValue1, List<ItemKeybindEntry> localValue2) {
      SizedFont localValue3 = Fonts.internalField0449.internalMethod01432(6.0F);
      byte localValue4 = 18;
      int localValue5 = Math.min((int)this.internalField0383.internalMethod08576(), localValue2.size());
      int localValue6 = (localValue2.size() + localValue5 - 1) / localValue5;
      this.width = localValue5 * 26 - 5;
      this.height = localValue6 * 32 - 4;
      ArrayList localValue7 = new ArrayList(localValue2.size());

      for (int localValue8 = 0; localValue8 < localValue2.size(); localValue8++) {
         int localValue9 = localValue8 % localValue5;
         int localValue10 = localValue8 / localValue5;
         float localValue11 = localValue9 * 26;
         float localValue12 = localValue10 * 32;
         float localValue13 = this.x + localValue11;
         float localValue14 = this.y + localValue12;
         float localValue15 = localValue13 + 3.0F;
         float localValue16 = localValue14 + 3.5F;
         ItemKeybindEntry localValue17 = (ItemKeybindEntry)localValue2.get(localValue8);
         localValue1.drawClientRect(
            localValue13,
            localValue14,
            22.0F,
            23.0F,
            255.0F * this.animation.internalMethod02881() * InterfaceModule.internalMethod07584(),
            this.dragAnim.internalMethod02881(),
            7.0F,
            4.0F
         );
         float localValue18 = this.internalMethod00490(localValue17.internalMethod04702());
         localValue7.add(new ItemBindsHud.InternalType0288(localValue17.internalMethod05091().getDefaultStack(), localValue15, localValue16, localValue18));
      }

      try (CustomDrawContext.InternalType0486 localValue25 = localValue1.beginItemBatch()) {
         for (ItemBindsHud.InternalType0288 localValue29 : (Iterable<ItemBindsHud.InternalType0288>)(Iterable<?>)localValue7) {
            HudRenderUtils.internalMethod08976(
               localValue1.getMatrices(), localValue29.internalMethod03597() + 8.0F, localValue29.internalMethod03600() + 8.0F, localValue29.internalMethod08483()
            );
            localValue1.drawBatchItem(localValue29.internalMethod05081(), localValue29.internalMethod03597(), localValue29.internalMethod03600(), 80);
            HudRenderUtils.internalMethod00012(localValue1.getMatrices());
         }
      }

      this.internalMethod02841(localValue1, localValue2, localValue5);
      ColoredQuadBatch localValue26 = new ColoredQuadBatch(VertexFormats.POSITION_COLOR, localValue1.getMatrices());
      this.internalMethod02120(localValue1, localValue2, localValue4, localValue3);
      localValue26.internalMethod09053();
      PlainGlyphBatch localValue28 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.internalField0449);
      this.internalMethod00610(localValue1, localValue2, localValue3);
      localValue28.internalMethod09053();

      for (int localValue30 = 0; localValue30 < localValue2.size(); localValue30++) {
         int localValue31 = localValue30 % localValue5;
         int localValue32 = localValue30 / localValue5;
         float localValue33 = localValue31 * 26;
         float localValue34 = localValue32 * 32;
         float localValue35 = this.x + localValue33 + 3.0F;
         float localValue36 = this.y + localValue34 + 3.5F;
         float localValue37 = 14.0F;
         ItemKeybindEntry localValue38 = (ItemKeybindEntry)localValue2.get(localValue30);
         if (internalField0149.player.getItemCooldownManager().isCoolingDown(localValue38.internalMethod05091().getDefaultStack())) {
            float localValue19 = internalField0149.player
               .getItemCooldownManager()
               .getCooldownProgress(localValue38.internalMethod05091().getDefaultStack(), internalField0149.getRenderTickCounter().getTickProgress(true));
            float localValue20 = localValue35 + 8.0F;
            float localValue21 = localValue36 + 8.0F;
            float localValue22 = localValue37 / 3.0F;
            localValue1.drawCircleProgress(localValue20, localValue21, localValue22, 1.35F, 0.999F, ThemeColors.internalField1616.mulAlpha(1.0F));
            localValue1.drawCircleProgress(localValue20, localValue21, localValue22, 1.35F, Math.clamp(localValue19, 0.0F, 1.0F), ThemeColors.internalMethod02531());
         }
      }
   }

   private void internalMethod02841(UiRenderContext localValue1, List<ItemKeybindEntry> localValue2, int localValue3) {
      if (internalField0149.player != null) {
         for (int localValue4 = 0; localValue4 < localValue2.size(); localValue4++) {
            ItemKeybindEntry localValue5 = (ItemKeybindEntry)localValue2.get(localValue4);
            if (internalField0149.player.getItemCooldownManager().isCoolingDown(localValue5.internalMethod05091().getDefaultStack())) {
               int localValue6 = localValue4 % localValue3;
               int localValue7 = localValue4 / localValue3;
               float localValue8 = this.x + localValue6 * 26 + 3.0F;
               float localValue9 = this.y + localValue7 * 32 + 3.5F;
               float localValue10 = 16.0F;
               float localValue11 = Math.clamp(
                  internalField0149.player
                     .getItemCooldownManager()
                     .getCooldownProgress(localValue5.internalMethod05091().getDefaultStack(), internalField0149.getRenderTickCounter().getTickProgress(true)),
                  0.0F,
                  1.0F
               );
               float localValue12 = localValue9 + localValue10 * (1.0F - localValue11);
               float localValue13 = localValue10 * localValue11;
               localValue1.drawRoundedRect(
                  localValue8, localValue12, localValue10, localValue13, CornerRadii.internalMethod03908(3.0F), ColorRGBA.BLACK.mulAlpha(0.48F * this.animation.internalMethod02881())
               );
            }
         }
      }
   }

   private void internalMethod00610(UiRenderContext localValue1, List<ItemKeybindEntry> localValue2, SizedFont localValue3) {
      byte localValue4 = 18;
      int localValue5 = Math.min((int)this.internalField0383.internalMethod08576(), localValue2.size());

      for (int localValue6 = 0; localValue6 < localValue2.size(); localValue6++) {
         int localValue7 = localValue6 % localValue5;
         int localValue8 = localValue6 / localValue5;
         float localValue9 = localValue7 * 26;
         float localValue10 = localValue8 * 32;
         float localValue11 = this.x + localValue9 + 3.5F;
         float localValue12 = this.y + localValue10 + 3.0F;
         ItemKeybindEntry localValue13 = (ItemKeybindEntry)localValue2.get(localValue6);
         float localValue14 = this.internalMethod03761(localValue13.internalMethod05091());
         boolean localValue15 = localValue14 > 0.0F;
         String localValue16;
         if (localValue15) {
            localValue16 = String.format("%.1f", localValue14);
         } else {
            if (localValue13.internalMethod04702() == -1) {
               continue;
            }

            localValue16 = this.internalMethod06964(localValue13.internalMethod04702());
            if (localValue16.length() > 3) {
               localValue16 = localValue16.substring(0, 3);
            }
         }

         float localValue17 = this.internalMethod04155(localValue11, localValue3.internalMethod00965(localValue16));
         float localValue18 = this.internalMethod04156(localValue12, localValue4);
         localValue1.drawText(localValue3, localValue16, localValue17 + 2.0F, localValue18 + Math.round((8.0F - localValue3.internalMethod04890()) / 2.0F), ThemeColors.internalMethod09808());
      }
   }

   private float internalMethod00489(float localValue1) {
      return Math.round(localValue1 / 2.0F) * 2.0F + 4.0F;
   }

   private float internalMethod04155(float localValue1, float localValue2) {
      return Math.round(localValue1 + 7.5F - this.internalMethod00489(localValue2) / 2.0F);
   }

   private float internalMethod04156(float localValue1, int localValue2) {
      return Math.round(localValue1 + localValue2 - 1.0F);
   }

   private void internalMethod02120(UiRenderContext localValue1, List<ItemKeybindEntry> localValue2, int localValue3, SizedFont localValue4) {
      int localValue5 = Math.min((int)this.internalField0383.internalMethod08576(), localValue2.size());

      for (int localValue6 = 0; localValue6 < localValue2.size(); localValue6++) {
         int localValue7 = localValue6 % localValue5;
         int localValue8 = localValue6 / localValue5;
         float localValue9 = localValue7 * 26;
         float localValue10 = localValue8 * 32;
         float localValue11 = this.x + localValue9 + 3.5F;
         float localValue12 = this.y + localValue10 + 3.0F;
         ItemKeybindEntry localValue13 = (ItemKeybindEntry)localValue2.get(localValue6);
         float localValue14 = this.internalMethod03761(localValue13.internalMethod05091());
         boolean localValue15 = localValue14 > 0.0F;
         String localValue16;
         if (localValue15) {
            localValue16 = String.format("%.1f", localValue14);
         } else {
            if (localValue13.internalMethod04702() == -1) {
               continue;
            }

            localValue16 = this.internalMethod06964(localValue13.internalMethod04702());
            if (localValue16.length() > 3) {
               localValue16 = localValue16.substring(0, 3);
            }
         }

         float localValue17 = localValue4.internalMethod00965(localValue16);
         localValue1.drawRoundedRect(
            this.internalMethod04155(localValue11, localValue17),
            this.internalMethod04156(localValue12, localValue3),
            this.internalMethod00489(localValue17),
            8.0F,
            CornerRadii.internalMethod03908(1.0F),
            ThemeColors.internalMethod02531()
         );
      }
   }

   private float internalMethod03761(Item localValue1) {
      if (internalField0149.player == null) {
         return 0.0F;
      } else {
         ItemCooldownManagerAccessor localValue2 = (ItemCooldownManagerAccessor)(Object)internalField0149.player.getItemCooldownManager();
         Identifier localValue3 = localValue2.rockstar$getGroup(localValue1.getDefaultStack());
         Object localValue4 = localValue2.rockstar$getEntries().get(localValue3);
         if (localValue4 == null) {
            return 0.0F;
         } else {
            int localValue5 = ((ItemCooldownEntryAccessor)(Object)localValue4).rockstar$getEndTick() - localValue2.rockstar$getTick();
            return Math.max(0.0F, localValue5 / 20.0F);
         }
      }
   }

   private String internalMethod06964(int localValue1) {
      if (KeybindUtils.internalMethod02026(localValue1)) {
         int localValue2 = KeybindUtils.internalMethod02025(localValue1) - 0 + 1;
         return KeybindUtils.internalMethod07434(KeybindUtils.internalMethod02857(localValue1)) + "M" + localValue2;
      } else {
         return TextUtils.internalMethod04982(localValue1);
      }
   }

   private float internalMethod00490(int localValue1) {
      if (localValue1 == -1) {
         return 1.0F;
      } else {
         boolean localValue2 = KeybindUtils.internalMethod02026(localValue1);
         boolean localValue4 = this.internalMethod00491(localValue1);
         AnimatedValue localValue3;
         if (localValue2) {
            localValue3 = this.internalField0544.computeIfAbsent(localValue1, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField0812));
         } else {
            localValue3 = this.internalField0543.computeIfAbsent(localValue1, localValue0 -> new AnimatedValue(200L, 0.0F, Easing.internalField0812));
         }

         localValue3.internalMethod07062(localValue4);
         return 1.0F - 0.15F * localValue3.internalMethod02881();
      }
   }

   private boolean internalMethod00491(int localValue1) {
      return KeybindUtils.internalMethod08521(localValue1);
   }

   static final class InternalType0288 {
      private final ItemStack internalField0878;
      private final float internalField0205;
      private final float internalField0206;
      private final float internalField1048;

      InternalType0288(ItemStack localValue1, float localValue2, float localValue3, float localValue4) {
         this.internalField0878 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0288[stack=" + this.internalField0878 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", scale=" + this.internalField1048 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         ItemBindsHud.InternalType0288 other = (ItemBindsHud.InternalType0288) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048);
      }

      public ItemStack internalMethod05081() {
         return this.internalField0878;
      }

      public float internalMethod03597() {
         return this.internalField0205;
      }

      public float internalMethod03600() {
         return this.internalField0206;
      }

      public float internalMethod08483() {
         return this.internalField1048;
      }
   }
}
