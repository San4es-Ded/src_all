package moscow.rockstar.mixin.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.AfterAttackEvent;
import pyrock.events.game.BlockBreakEvent;
import pyrock.events.game.BlockPlaceEvent;
import pyrock.events.game.InternalAttackEvent;
import pyrock.events.game.StartBreakBlockEvent;
import rockstar.modules.player.NoInteractModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin
implements MinecraftClientAccess {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method={"attackEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$critPre(PlayerEntity playerEntity, Entity entity, CallbackInfo callbackInfo) {
        InternalAttackEvent internalAttackEvent = new InternalAttackEvent(entity);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(internalAttackEvent);
        if (internalAttackEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"attackEntity"}, at={@At(value="RETURN")})
    private void rockstar$critPost(PlayerEntity playerEntity, Entity entity, CallbackInfo callbackInfo) {
        AfterAttackEvent afterAttackEvent = new AfterAttackEvent(entity);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(afterAttackEvent);
    }

    @Inject(method={"breakBlock"}, at={@At(value="RETURN")}, cancellable=true)
    public void breakBlockHook(BlockPos blockPos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(blockPos);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(blockBreakEvent);
        if (blockBreakEvent.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"attackBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        StartBreakBlockEvent startBreakBlockEvent = new StartBreakBlockEvent(blockPos);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(startBreakBlockEvent);
        if (startBreakBlockEvent.isCancelled()) {
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method={"interactBlock"}, at={@At(value="HEAD")}, cancellable=true)
    public void preventInteraction(ClientPlayerEntity clientPlayerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        ItemStack itemStack;
        if (this.client.world == null) {
            return;
        }
        NoInteractModule typedValue305 = RockstarClient.getInstance().getModuleManager().getModule(NoInteractModule.class);
        if (!typedValue305.isEnabled()) {
            return;
        }
        Block block = this.client.world.getBlockState(blockHitResult.getBlockPos()).getBlock();
        if (typedValue305.internalMethod00044(block, itemStack = clientPlayerEntity.getStackInHand(hand))) {
            callbackInfoReturnable.setReturnValue(ActionResult.PASS);
        }
        if (typedValue305.internalMethod01109(itemStack)) {
            callbackInfoReturnable.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method={"interactEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void preventEntityInteraction(PlayerEntity playerEntity, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        if (this.client.world == null || !(entity instanceof ArmorStandEntity) && !(entity instanceof AbstractMinecartEntity)) {
            return;
        }
        NoInteractModule typedValue305 = RockstarClient.getInstance().getModuleManager().getModule(NoInteractModule.class);
        if (!typedValue305.isEnabled()) {
            return;
        }
        if (typedValue305.internalMethod00145(entity, playerEntity.getStackInHand(hand))) {
            callbackInfoReturnable.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(method={"interactBlock"}, at={@At(value="RETURN")})
    private void onInteractBlock(ClientPlayerEntity clientPlayerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        if (this.client.world == null) {
            return;
        }
        ActionResult actionResult = (ActionResult)callbackInfoReturnable.getReturnValue();
        if (actionResult == null || !actionResult.isAccepted()) {
            return;
        }
        ItemStack itemStack = clientPlayerEntity.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return;
        }
        if (!(itemStack.getItem() instanceof BlockItem) && !itemStack.isOf(Items.END_CRYSTAL)) {
            return;
        }
        BlockPos blockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new BlockPlaceEvent(blockPos, blockHitResult.getBlockPos(), blockHitResult.getSide(), hand, itemStack));
    }

    @Inject(method={"interactEntityAtLocation"}, at={@At(value="HEAD")}, cancellable=true)
    private void preventEntityInteractionAtLocation(PlayerEntity playerEntity, Entity entity, EntityHitResult entityHitResult, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        if (this.client.world == null || !(entity instanceof ArmorStandEntity) && !(entity instanceof AbstractMinecartEntity)) {
            return;
        }
        NoInteractModule typedValue305 = RockstarClient.getInstance().getModuleManager().getModule(NoInteractModule.class);
        if (!typedValue305.isEnabled()) {
            return;
        }
        if (typedValue305.internalMethod00145(entity, playerEntity.getStackInHand(hand))) {
            callbackInfoReturnable.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(method={"clickSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void onClickSlot(int n, int n2, int n3, SlotActionType slotActionType, PlayerEntity playerEntity, CallbackInfo callbackInfo) {
    }
}
