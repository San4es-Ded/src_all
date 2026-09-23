package rockstar.client.internal.rotation;





import rockstar.client.rotation.*;
import rockstar.client.bot.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import rockstar.client.internal.inventory.BotPlayerState;
import rockstar.client.bot.BotTargetManager;

public class BotFakePlayer
extends OtherClientPlayerEntity {
    public BotFakePlayer(ClientWorld clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
    }

    public void internalMethod07330() {
        this.unsetRemoved();
        ((ClientWorld)this.getEntityWorld()).addEntity(this);
    }

    public void internalMethod07331() {
        ((ClientWorld)this.getEntityWorld()).removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
        this.onRemoved();
    }

    public void internalMethod05011(BotTargetManager typedValue055) {
        BotPlayerState typedParameter1027 = typedValue055.internalMethod06688();
        this.refreshPositionAndAngles(typedParameter1027.internalMethod02807(), typedParameter1027.internalMethod02816(), typedParameter1027.internalMethod08590(), typedParameter1027.internalMethod02808(), typedParameter1027.internalMethod02817());
        this.setYaw(typedParameter1027.internalMethod02808());
        this.setPitch(typedParameter1027.internalMethod02817());
        this.setHeadYaw(typedParameter1027.internalMethod02808());
        this.setBodyYaw(typedParameter1027.internalMethod02808());
        this.setOnGround(typedParameter1027.internalMethod09849());
    }

    public boolean shouldRender(double distance) {
        return false;
    }
}
