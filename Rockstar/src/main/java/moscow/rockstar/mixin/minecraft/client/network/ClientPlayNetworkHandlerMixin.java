package moscow.rockstar.mixin.minecraft.client.network;



import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.game.PickupEvent;
import pyrock.events.game.WorldChangeEvent;
import rockstar.modules.visual.XRayModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.internal.game.BlockEntityCache;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.rotation.Rotation;
import rockstar.modules.movement.AirStuckModule;

@Mixin(value={ClientPlayNetworkHandler.class})
public abstract class ClientPlayNetworkHandlerMixin
extends ClientCommonNetworkHandler
implements MinecraftClientAccess {
    @Unique
    private Rotation oldRotation;

    protected ClientPlayNetworkHandlerMixin(MinecraftClient minecraftClient, ClientConnection clientConnection, ClientConnectionState clientConnectionState) {
        super(minecraftClient, clientConnection, clientConnectionState);
    }

    @Inject(method={"onItemPickupAnimation"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;getEntityById(I)Lnet/minecraft/entity/Entity;", ordinal=0)})
    private void onItemPickupAnimation(ItemPickupAnimationS2CPacket itemPickupAnimationS2CPacket, CallbackInfo callbackInfo) {
        Entity entity = this.client.world.getEntityById(itemPickupAnimationS2CPacket.getEntityId());
        Entity entity2 = this.client.world.getEntityById(itemPickupAnimationS2CPacket.getCollectorEntityId());
        if (entity instanceof ItemEntity) {
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new PickupEvent(entity2, ((ItemEntity)entity).getStack(), itemPickupAnimationS2CPacket.getStackAmount()));
        }
    }

    @Inject(method={"onBlockEntityUpdate"}, at={@At(value="TAIL")})
    private void onBlockEntityUpdate(BlockEntityUpdateS2CPacket blockEntityUpdateS2CPacket, CallbackInfo callbackInfo) {
        if (ClientPlayNetworkHandlerMixin.internalField0149.world == null) {
            return;
        }
        BlockPos blockPos = blockEntityUpdateS2CPacket.getPos();
        BlockEntityCache.internalMethod04676(ClientPlayNetworkHandlerMixin.internalField0149.world.getBlockEntity(blockPos));
    }

    @Inject(method={"onChunkData"}, at={@At(value="TAIL")})
    private void onChunkData(ChunkDataS2CPacket chunkDataS2CPacket, CallbackInfo callbackInfo) {
        if (ClientPlayNetworkHandlerMixin.internalField0149.world == null) {
            return;
        }
        WorldChunk worldChunk = ClientPlayNetworkHandlerMixin.internalField0149.world.getChunk(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ());
        worldChunk.getBlockEntities().values().forEach(BlockEntityCache::internalMethod04676);
        XRayModule iModuleManager = RockstarClient.getInstance().getModuleManager().getModule(XRayModule.class);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (iModuleManager == null || !iModuleManager.isEnabled() || minecraftClient.world == null) {
            return;
        }
        new Thread(() -> iModuleManager.internalMethod00622(worldChunk)).start();
    }

    @Inject(method={"onGameJoin"}, at={@At(value="TAIL")})
    private void onGameJoin(GameJoinS2CPacket gameJoinS2CPacket, CallbackInfo callbackInfo) {
        BlockEntityCache.internalMethod04034();
        EntityPositionTracker.internalMethod07263();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new WorldChangeEvent());
    }

    @Inject(method={"onPlayerRespawn"}, at={@At(value="TAIL")})
    private void rockstar$onPlayerRespawn(PlayerRespawnS2CPacket playerRespawnS2CPacket, CallbackInfo callbackInfo) {
        BlockEntityCache.internalMethod04034();
        EntityPositionTracker.internalMethod07263();
        AirStuckModule internalValue0006 = RockstarClient.getInstance().getModuleManager().getModule(AirStuckModule.class);
        if (internalValue0006 != null) {
            internalValue0006.disable();
        }
    }

    @Inject(method={"onPlayerPositionLook"}, at={@At(value="HEAD")})
    public void savePlayerRotation(PlayerPositionLookS2CPacket playerPositionLookS2CPacket, CallbackInfo callbackInfo) {
        if (ClientPlayNetworkHandlerMixin.internalField0149.player == null) {
            return;
        }
        this.oldRotation = new Rotation(ClientPlayNetworkHandlerMixin.internalField0149.player.getYaw(), ClientPlayNetworkHandlerMixin.internalField0149.player.getPitch());
    }

    @Inject(method={"onPlayerPositionLook"}, at={@At(value="RETURN")})
    public void modifyPlayerRotation(PlayerPositionLookS2CPacket playerPositionLookS2CPacket, CallbackInfo callbackInfo) {
        if (ClientPlayNetworkHandlerMixin.internalField0149.player == null) {
            return;
        }
        Rotation typedValue266 = new Rotation(playerPositionLookS2CPacket.change().yaw(), playerPositionLookS2CPacket.change().pitch());
    }
}
