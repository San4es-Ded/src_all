package rockstar.client.internal.game;


import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class FakePlayerEntity
extends OtherClientPlayerEntity {
    public static final String internalField0248 = "13371337-1337-abcd-ef00-deadbeef1337";

    public FakePlayerEntity(ClientWorld clientWorld, GameProfile gameProfile) {
        super(clientWorld, gameProfile);
    }

    public void internalMethod02207() {
        this.unsetRemoved();
        ((ClientWorld)this.getEntityWorld()).addEntity(this);
    }

    public void internalMethod02208() {
        ((ClientWorld)this.getEntityWorld()).removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
        this.onRemoved();
    }

    public void takeKnockback(double strength, double x, double z) {
    }
}
