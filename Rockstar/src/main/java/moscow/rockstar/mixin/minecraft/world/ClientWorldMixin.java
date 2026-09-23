package moscow.rockstar.mixin.minecraft.world;


import rockstar.client.internal.game.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.game.EntityDeathEvent;
import rockstar.modules.visual.XRayModule;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.internal.game.FakePlayerEntity;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={ClientWorld.class})
public abstract class ClientWorldMixin
extends World
implements MinecraftClientAccess {
    protected ClientWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, DynamicRegistryManager dynamicRegistryManager, RegistryEntry<DimensionType> registryEntry, boolean bl, boolean bl2, long l, int n) {
        super(mutableWorldProperties, registryKey, dynamicRegistryManager, registryEntry, bl, bl2, l, n);
    }

    @Inject(method={"handleBlockUpdate"}, at={@At(value="HEAD")})
    private void onHandleBlockUpdate(BlockPos blockPos, BlockState blockState, int n, CallbackInfo callbackInfo) {
        XRayModule iModuleManager = RockstarClient.getInstance().getModuleManager().getModule(XRayModule.class);
        if (iModuleManager == null || !iModuleManager.isEnabled()) {
            return;
        }
        Block block = blockState.getBlock();
        BlockPos blockPos2 = blockPos.toImmutable();
        if (iModuleManager.internalMethod03929().internalMethod05586(block)) {
            iModuleManager.internalMethod01204().add(blockPos2);
        } else {
            iModuleManager.internalMethod01204().remove(blockPos2);
        }
    }

    @Inject(method={"removeEntity"}, at={@At(value="HEAD")})
    private void removeEntityEvent(int n, Entity.RemovalReason removalReason, CallbackInfo callbackInfo) {
        LivingEntity livingEntity;
        EntityPositionTracker.internalMethod03945(n);
        if (ClientWorldMixin.internalField0149.player == null || ClientWorldMixin.internalField0149.player.isRemoved()) {
            return;
        }
        if (ClientWorldMixin.internalField0149.player.getId() == n) {
            return;
        }
        Entity entity = this.getEntityById(n);
        if (entity instanceof FakePlayerEntity) {
            return;
        }
        if (entity instanceof LivingEntity && ClientWorldMixin.internalField0149.player.distanceTo((Entity)(livingEntity = (LivingEntity)entity)) < 6.0f && !ClientWorldMixin.internalField0149.player.isDead() && EntityPositionTracker.internalMethod06807(entity)) {
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new EntityDeathEvent(livingEntity));
        }
    }

    @Inject(method={"addBlockBreakParticles"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$removeBlockBreakParticles(BlockPos blockPos, BlockState blockState, CallbackInfo callbackInfo) {
        RemovalsModule removals = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (removals.isEnabled() && removals.internalMethod09705().isSelected()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"spawnBlockBreakingParticle"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$removeBlockBreakingParticle(BlockPos blockPos, Direction direction, CallbackInfo callbackInfo) {
        RemovalsModule removals = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (removals.isEnabled() && removals.internalMethod09705().isSelected()) {
            callbackInfo.cancel();
        }
    }

}
