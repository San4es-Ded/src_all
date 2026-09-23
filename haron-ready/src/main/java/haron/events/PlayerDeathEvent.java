package haron.events;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerDeathEvent {
    private final PlayerEntity player;
    private final double x;
    private final double y;
    private final double z;

    public PlayerEntity player() {
        return this.player;
    }

    public PlayerDeathEvent(PlayerEntity playerEntity, double d, double d2, double d3) {
        this.player = playerEntity;
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public double b() {
        int n = 563;
        return this.x;
    }

    public double x() {
        return this.x;
    }

    public double c() {
        return this.y;
    }

    public double d() {
        return this.z;
    }

    public PlayerEntity a() {
        return this.player;
    }

    public double z() {
        return this.z;
    }

    public double y() {
        return this.y;
    }
}

