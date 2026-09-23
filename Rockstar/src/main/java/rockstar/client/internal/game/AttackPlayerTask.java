package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import rockstar.client.internal.core.BotBehavior;
import rockstar.client.bot.BotTargetManager;

public class AttackPlayerTask
implements BotBehavior {
    private final String internalField0248;
    private final double internalField0194;
    private long internalField0229;

    public AttackPlayerTask(String string, double d) {
        this.internalField0248 = string;
        this.internalField0194 = d;
    }

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        PlayerEntity playerEntity = this.internalMethod04870();
        if (typedValue055 == null || playerEntity == null || playerEntity.isRemoved()) {
            typedValue055.internalMethod09733();
            return;
        }
        Vec3d vec3d = playerEntity.getEntityPos();
        typedValue055.internalMethod05389(vec3d.x, vec3d.y + (double)playerEntity.getStandingEyeHeight(), vec3d.z);
        if (typedValue055.internalMethod01366(vec3d) > this.internalField0194) {
            typedValue055.internalMethod01196(vec3d, this.internalField0194 * 0.8);
            return;
        }
        typedValue055.internalMethod09733();
        long l = System.currentTimeMillis();
        if (l - this.internalField0229 >= typedValue055.internalMethod06687().internalMethod04129()) {
            typedValue055.internalMethod05689((Entity)playerEntity);
            this.internalField0229 = l;
        }
    }

    private PlayerEntity internalMethod04870() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.world == null || this.internalField0248 == null || this.internalField0248.isBlank()) {
            return null;
        }
        for (PlayerEntity playerEntity : minecraftClient.world.getPlayers()) {
            if (!playerEntity.getName().getString().equalsIgnoreCase(this.internalField0248)) continue;
            return playerEntity;
        }
        return null;
    }

    @Override
    public String internalMethod06553() {
        return "Attack " + this.internalField0248;
    }

    @Generated
    public String internalMethod03029() {
        return this.internalField0248;
    }

    @Generated
    public double internalMethod06098() {
        return this.internalField0194;
    }

    @Generated
    public long internalMethod06099() {
        return this.internalField0229;
    }
}

