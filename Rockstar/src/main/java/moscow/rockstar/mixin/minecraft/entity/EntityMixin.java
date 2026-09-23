package moscow.rockstar.mixin.minecraft.entity;



import rockstar.client.rotation.*;
import rockstar.client.internal.core.*;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.RotateCameraEvent;
import pyrock.events.player.EventOnMovePost;
import pyrock.events.player.TraceEvent;
import rockstar.modules.player.NoPushModule;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.BackTrackDataAccess;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationRequest;
import rockstar.modules.combat.BackTrackModule;
import rockstar.modules.combat.HitboxesModule;

@Mixin({Entity.class})
public abstract class EntityMixin implements MinecraftClientAccess, BackTrackDataAccess {
   @Shadow
   private Box boundingBox;
   @Unique
   private List<BackTrackModule.InternalType0403> backTracks;

   @Shadow
   public abstract Vec3d getCameraPosVec(float localValue1);

   @Shadow
   public abstract Vec3d getRotationVec(float localValue1);

   @Shadow
   public abstract float getYaw();

   @Shadow
   public abstract float getPitch();

   @Inject(
      method = {"raycast"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRaycast(double localValue1, float localValue3, boolean localValue4, CallbackInfoReturnable<HitResult> localValue5) {
      TraceEvent localValue6 = new TraceEvent(this.getYaw(), this.getPitch());
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue6);
      if (localValue6.isCancelled()) {
         Vec3d localValue7 = this.getCameraPosVec(localValue3);
         Vec3d localValue8 = this.getCustomLook(localValue6.getPitch(), localValue6.getYaw());
         Vec3d localValue9 = localValue7.add(localValue8.multiply(localValue1));
         BlockHitResult localValue10 = ((Entity)(Object)this)
            .getEntityWorld()
            .raycast(new RaycastContext(localValue7, localValue9, ShapeType.OUTLINE, localValue4 ? FluidHandling.ANY : FluidHandling.NONE, (Entity)(Object)this));
         localValue5.setReturnValue(localValue10);
      }
   }

   private Vec3d getCustomLook(float localValue1, float localValue2) {
      float localValue3 = localValue1 * (float) (Math.PI / 180.0);
      float localValue4 = -localValue2 * (float) (Math.PI / 180.0);
      float localValue5 = (float)Math.cos(localValue4);
      float localValue6 = (float)Math.sin(localValue4);
      float localValue7 = (float)Math.cos(localValue3);
      float localValue8 = (float)Math.sin(localValue3);
      return new Vec3d(localValue6 * localValue7, -localValue8, localValue5 * localValue7);
   }

   @WrapOperation(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"
      )}
   )
   private void rockstar$fallDistanceOnly(Entity localValue1, double localValue2, boolean localValue4, BlockState localValue5, BlockPos localValue6, Operation<Void> localValue7) {
      if (localValue1 != internalField0149.player) {
         localValue7.call(new Object[]{localValue1, localValue2, localValue4, localValue5, localValue6});
      } else {
         if (localValue4) {
            localValue1.fallDistance = 0.0F;
         } else if (localValue2 < 0.0) {
            localValue1.fallDistance -= (float)localValue2;
         }
      }
   }

   @Inject(
      method = {"isGlowing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isGlowing(CallbackInfoReturnable<Boolean> localValue1) {
      RemovalsModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
      if (localValue2.isEnabled() && localValue2.internalMethod09969().isSelected()) {
         localValue1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"onBubbleColumnCollision", "onBubbleColumnSurfaceCollision"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removePushFromBubbleColumns(boolean localValue1, CallbackInfo localValue2) {
      NoPushModule localValue3 = RockstarClient.getInstance().getModuleManager().getModule(NoPushModule.class);
      if ((Object)this == internalField0149.player && localValue3.isEnabled() && localValue3.internalMethod07982().isSelected()) {
         localValue2.cancel();
      }
   }

   @Inject(
      method = {"getBoundingBox"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public final void getBoundingBox(CallbackInfoReturnable<Box> localValue1) {
      HitboxesModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(HitboxesModule.class);
      Entity localValue3 = (Entity)(Object)this;
      if (localValue3 instanceof LivingEntity localValue4 && localValue2.isEnabled() && localValue2.internalMethod01528(localValue4) && localValue3.getId() != internalField0149.player.getId()) {
         localValue1.setReturnValue(
            new Box(
               this.boundingBox.minX - localValue2.internalMethod03348().internalMethod08576(),
               this.boundingBox.minY,
               this.boundingBox.minZ - localValue2.internalMethod03348().internalMethod08576(),
               this.boundingBox.maxX + localValue2.internalMethod03348().internalMethod08576(),
               this.boundingBox.maxY,
               this.boundingBox.maxZ + localValue2.internalMethod03348().internalMethod08576()
            )
         );
      }
   }

   @Redirect(
      method = {"updateVelocity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;getYaw()F"
      )
   )
   public float movementCorrection(Entity localValue1) {
      RotationManager localValue2 = RockstarClient.internalField0240.internalMethod02368();
      RotationRequest localValue3 = localValue2.internalMethod07551();
      return localValue3 != null && localValue3.internalMethod01386().internalMethod03280() && localValue1 instanceof ClientPlayerEntity
         ? localValue2.internalMethod09074().internalMethod00169()
         : localValue1.getYaw();
   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("TAIL")}
   )
   private void rockstar$onMovePost(float localValue1, Vec3d localValue2, CallbackInfo localValue3) {
      Entity localValue4 = (Entity)(Object)this;
      if (internalField0149.player != null && localValue4.getId() == internalField0149.player.getId()) {
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(new EventOnMovePost(localValue1, localValue2));
      }
   }

   @Override
   public List<BackTrackModule.InternalType0403> rockstar2_0$getBackTracks() {
      if (this.backTracks == null) {
         this.backTracks = new ArrayList<>();
      }

      return this.backTracks;
   }

   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChangeLookDirection(double localValue1, double localValue3, CallbackInfo localValue5) {
      if ((Object)this == internalField0149.player) {
         RotateCameraEvent localValue6 = new RotateCameraEvent((float)(localValue1 * 0.15F), (float)(localValue3 * 0.15F));
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue6);
         if (localValue6.isCancelled()) {
            localValue5.cancel();
         }
      }
   }
}
