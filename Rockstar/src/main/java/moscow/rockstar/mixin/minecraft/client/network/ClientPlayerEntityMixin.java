package moscow.rockstar.mixin.minecraft.client.network;




import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import globals.client.snowball.FakeFrozenTicksAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.CloseScreenEvent;
import pyrock.events.player.ClientPlayerTickEndEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.EventMotion;
import pyrock.events.player.EventUpdatePostTick;
import pyrock.events.player.SlowDownEvent;
import rockstar.modules.player.InventoryUtilsModule;
import rockstar.modules.player.NoPushModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.SprintResetManager;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.PlayerSprintAccess;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.modules.combat.AuraModule;
import rockstar.modules.combat.TriggerBotModule;
import rockstar.modules.movement.AutoSprintModule;
import rockstar.modules.movement.SpeedModule;

@Mixin(value={ClientPlayerEntity.class})
public class ClientPlayerEntityMixin
implements FakeFrozenTicksAccess,
MinecraftClientAccess,
PlayerSprintAccess {
    @Unique
    private int groundTicks;
    @Unique
    private int rockstar$fakeFrozenTicks;
    @Unique
    private EventMotion rockstar$motionEvent;
    @Unique
    private AuraModule aura;

    @Shadow
    private PlayerInput lastPlayerInput;

    @Shadow
    private void sendSprintingPacket() {
    }

    @Unique
    private AuraModule rockstar$aura() {
        if (this.aura == null) {
            this.aura = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
        }
        return this.aura;
    }

    @Redirect(
        method={"getCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;"},
        at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;")
    )
    private static Vec3d rockstar$useServerRotationForEntityRaytrace(Entity entity, float tickProgress) {
        if (entity != MinecraftClient.getInstance().player) {
            return entity.getRotationVec(tickProgress);
        }
        RotationManager rotationManager = RockstarClient.getInstance().internalMethod02368();
        return rotationManager.internalMethod01525()
            ? entity.getRotationVec(tickProgress)
            : rotationManager.internalMethod09074().internalMethod06001();
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    private void rockstar$keepBounceSprint(ClientPlayerEntity clientPlayerEntity, boolean bl) {
        if (!bl && SpeedModule.internalMethod06513(clientPlayerEntity)) {
            return;
        }
        clientPlayerEntity.setSprinting(bl);
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require=0)
    private boolean onIsUsingItemRedirect(ClientPlayerEntity clientPlayerEntity) {
        SlowDownEvent slowDownEvent = new SlowDownEvent();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(slowDownEvent);
        return clientPlayerEntity.isUsingItem() && clientPlayerEntity.getVehicle() == null && !slowDownEvent.isCancelled();
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/PlayerInput;sprint()Z")})
    public boolean unpressSprintKey(boolean bl) {
        if (this.shouldPreventAuraSprint((ClientPlayerEntity)(Object)this)) {
            return false;
        }
        return bl;
    }

    @Inject(method={"canSprint(Z)Z"}, at={@At(value="RETURN")}, cancellable=true)
    private void disallowSprinting(boolean allowTouchingWater, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (this.shouldPreventAuraSprint((ClientPlayerEntity)(Object)this)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"canStartSprinting"}, at={@At(value="HEAD")}, cancellable=true)
    private void preventSprintStart(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (SprintResetManager.internalMethod06977((Entity)((ClientPlayerEntity)(Object)this))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"sendSprintingPacket"}, at={@At(value="HEAD")})
    private void resetSprintBeforePacket(CallbackInfo callbackInfo) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)(Object)this;
        if (SprintResetManager.internalMethod06977((Entity)clientPlayerEntity)) {
            ClientPlayerEntityMixin.internalField0149.options.sprintKey.setPressed(false);
            clientPlayerEntity.setSprinting(false);
            if (this.rockstar$motionEvent != null) {
                this.rockstar$motionEvent.setSprinting(false);
            }
        }
    }

    @Inject(method={"sendSprintingPacket"}, at={@At(value="TAIL")})
    private void markSprintResetSynced(CallbackInfo callbackInfo) {
        SprintResetManager.internalMethod06976((Entity)((ClientPlayerEntity)(Object)this));
    }

    @ModifyExpressionValue(method={"canSprint(Z)Z"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;canSprintOrFly()Z")})
    private boolean ignoreHungerForAutoSprint(boolean vanillaCanSprint) {
        AutoSprintModule typedValue122 = RockstarClient.getInstance().getModuleManager().getModule(AutoSprintModule.class);
        if (typedValue122.isEnabled() && typedValue122.internalMethod02831().internalMethod04496()) {
            return true;
        }
        return vanillaCanSprint;
    }

    @WrapWithCondition(method={"closeScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")})
    private boolean preventCloseScreen(MinecraftClient minecraftClient, Screen screen) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new CloseScreenEvent(screen));
        return true;
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    public void removePushOutFromBlocks(double d, double d2, CallbackInfo callbackInfo) {
        NoPushModule typedValue306 = RockstarClient.getInstance().getModuleManager().getModule(NoPushModule.class);
        if (typedValue306.isEnabled() && typedValue306.internalMethod08017().isSelected()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void triggerTickEvent(CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ClientPlayerTickEvent());
    }

    @Inject(method={"tick"}, at={@At(value="RETURN")})
    public void triggerTickEndEvent(CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ClientPlayerTickEndEvent());
        this.rockstar$finishMotionEvent(false);
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift=At.Shift.AFTER)})
    public void triggerUpdatePostTickEvent(CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new EventUpdatePostTick());
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift=At.Shift.AFTER)})
    private void rockstar$createMotionEventBeforeActionPackets(CallbackInfo callbackInfo) {
        this.rockstar$getMotionEvent((ClientPlayerEntity)(Object)this);
    }

    @Inject(method={"tickMovement"}, at={@At(value="HEAD")})
    public void updateOnGroundTicks(CallbackInfo callbackInfo) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)(Object)this;
        if (SprintResetManager.internalMethod06977((Entity)clientPlayerEntity)) {
            ClientPlayerEntityMixin.internalField0149.options.sprintKey.setPressed(false);
            clientPlayerEntity.setSprinting(false);
            if (SprintResetManager.internalMethod04866((Entity)clientPlayerEntity)) {
                this.sendSprintingPacket();
            }
        }
        this.groundTicks = ClientPlayerEntityMixin.internalField0149.player != null && ClientPlayerEntityMixin.internalField0149.player.isOnGround() ? ++this.groundTicks : 0;
    }

    @ModifyExpressionValue(
        method={"tick"},
        at={@At(value="FIELD", target="Lnet/minecraft/client/input/Input;playerInput:Lnet/minecraft/util/PlayerInput;")}
    )
    private PlayerInput rockstar$replaceActionInput(PlayerInput playerInput) {
        EventMotion eventMotion = this.rockstar$getMotionEvent((ClientPlayerEntity)(Object)this);
        if (eventMotion.isCancelled()) {
            return this.lastPlayerInput;
        }
        return new PlayerInput(
            playerInput.forward(),
            playerInput.backward(),
            playerInput.left(),
            playerInput.right(),
            playerInput.jump(),
            eventMotion.isSneaking(),
            eventMotion.isSprinting()
        );
    }

    @Inject(method={"sendSprintingPacket"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$cancelSprintingPacket(CallbackInfo callbackInfo) {
        if (this.rockstar$motionEvent != null && this.rockstar$motionEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method={"sendSprintingPacket"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isSprinting()Z"))
    private boolean rockstar$replaceSprintingState(ClientPlayerEntity clientPlayerEntity) {
        return this.rockstar$motionEvent != null ? this.rockstar$motionEvent.isSprinting() : clientPlayerEntity.isSprinting();
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$cancelMovementPackets(CallbackInfo callbackInfo) {
        EventMotion eventMotion = this.rockstar$getMotionEvent((ClientPlayerEntity)(Object)this);
        if (eventMotion.isCancelled()) {
            this.rockstar$finishMotionEvent(false);
            callbackInfo.cancel();
        }
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getX()D"))
    public double replaceMovePacketX(ClientPlayerEntity clientPlayerEntity) {
        return this.rockstar$getMotionEvent(clientPlayerEntity).getX();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getY()D"))
    public double replaceMovePacketY(ClientPlayerEntity clientPlayerEntity) {
        return this.rockstar$getMotionEvent(clientPlayerEntity).getY();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getZ()D"))
    public double replaceMovePacketZ(ClientPlayerEntity clientPlayerEntity) {
        return this.rockstar$getMotionEvent(clientPlayerEntity).getZ();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    public float replaceMovePacketYaw(ClientPlayerEntity clientPlayerEntity) {
        float f = this.rockstar$getMotionEvent(clientPlayerEntity).getYaw();
        RockstarClient.getInstance().internalMethod02368().internalMethod08209().internalMethod03239(f);
        return f;
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    public float replaceMovePacketPitch(ClientPlayerEntity clientPlayerEntity) {
        float f = this.rockstar$getMotionEvent(clientPlayerEntity).getPitch();
        RockstarClient.getInstance().internalMethod02368().internalMethod08209().internalMethod03289(f);
        return f;
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isOnGround()Z"))
    public boolean replaceMovePacketGround(ClientPlayerEntity clientPlayerEntity) {
        return this.rockstar$getMotionEvent(clientPlayerEntity).isOnGround();
    }

    @Unique
    private EventMotion rockstar$getMotionEvent(ClientPlayerEntity clientPlayerEntity) {
        if (this.rockstar$motionEvent != null) {
            return this.rockstar$motionEvent;
        }
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        Rotation typedValue266 = typedValue269.internalMethod09639();
        float f2 = typedValue266 != null ? typedValue266.internalMethod00169() : (typedValue269.internalMethod01525() ? clientPlayerEntity.getYaw() : typedValue269.internalMethod09074().internalMethod00169());
        float f3 = typedValue266 != null ? typedValue266.internalMethod00171() : (typedValue269.internalMethod01525() ? clientPlayerEntity.getPitch() : typedValue269.internalMethod09074().internalMethod00171());
        this.rockstar$motionEvent = new EventMotion(clientPlayerEntity.getX(), clientPlayerEntity.getY(), clientPlayerEntity.getZ(), f2, f3, clientPlayerEntity.isOnGround(), clientPlayerEntity.isSneaking(), clientPlayerEntity.isSprinting());
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(this.rockstar$motionEvent);
        return this.rockstar$motionEvent;
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="TAIL")})
    private void rockstar$clearInteractItemRotation(CallbackInfo callbackInfo) {
        this.rockstar$finishMotionEvent(true);
    }

    @Unique
    private void rockstar$finishMotionEvent(boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = this.rockstar$motionEvent != null;
        if (this.rockstar$motionEvent != null) {
            if (bl && !this.rockstar$motionEvent.isCancelled()) {
                this.rockstar$motionEvent.markSent();
            }
            this.rockstar$motionEvent = null;
        }
        if (bl2 || bl) {
            RockstarClient.getInstance().internalMethod02368().internalMethod08198(null);
        }
    }

    @Inject(method={"dropSelectedItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDropSelectedItem(boolean bl, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        InventoryUtilsModule typedValue278 = RockstarClient.getInstance().getModuleManager().getModule(InventoryUtilsModule.class);
        if (typedValue278.isEnabled() && typedValue278.internalMethod03702().isSelected() && typedValue278.internalMethod06820(ClientPlayerEntityMixin.internalField0149.player.getInventory().getSelectedSlot())) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
        }
    }

    @Override
    public int rockstar$getOnGroundTicks() {
        return this.groundTicks;
    }

    @Override
    public void rockstar$syncSprinting() {
        this.sendSprintingPacket();
    }

    @Override
    public int rockstar$getFakeFrozenTicks() {
        return this.rockstar$fakeFrozenTicks;
    }

    @Override
    public void rockstar$setFakeFrozenTicks(int n) {
        this.rockstar$fakeFrozenTicks = Math.max(0, n);
    }

    @Unique
    private boolean shouldPreventAuraSprint(ClientPlayerEntity clientPlayerEntity) {
        TriggerBotModule typedValue119 = RockstarClient.getInstance().getModuleManager().getModule(TriggerBotModule.class);
        AuraModule internalValue0004 = this.rockstar$aura();
        return SprintResetManager.internalMethod06977((Entity)clientPlayerEntity) || internalValue0004 != null && internalValue0004.isEnabled() && internalValue0004.internalMethod09757() || typedValue119.isEnabled() && typedValue119.internalMethod09517();
    }
}
