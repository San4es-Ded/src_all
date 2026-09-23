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

public class FollowEntityTask
implements BotBehavior {
    private final String internalField0248;
    private final Entity internalField0410;
    private final double internalField0194;

    public FollowEntityTask(String string, double d) {
        this.internalField0248 = string;
        this.internalField0410 = null;
        this.internalField0194 = d;
    }

    public FollowEntityTask(Entity entity, double d) {
        this.internalField0248 = entity == null ? null : entity.getName().getString();
        this.internalField0410 = entity;
        this.internalField0194 = d;
    }

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        Entity entity = this.internalMethod05246();
        if (typedValue055 == null || entity == null || entity.isRemoved()) {
            typedValue055.internalMethod09733();
            return;
        }
        Vec3d vec3d = entity.getEntityPos();
        typedValue055.internalMethod05389(vec3d.x, vec3d.y + (double)entity.getStandingEyeHeight(), vec3d.z);
        if (typedValue055.internalMethod01366(vec3d) > this.internalField0194) {
            typedValue055.internalMethod01196(vec3d, this.internalField0194);
        } else {
            typedValue055.internalMethod09733();
        }
    }

    private Entity internalMethod05246() {
        if (this.internalField0410 != null && !this.internalField0410.isRemoved()) {
            return this.internalField0410;
        }
        if (this.internalField0248 == null || this.internalField0248.isBlank()) {
            return null;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.world == null) {
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
        return "Follow " + (this.internalField0248 == null ? "target" : this.internalField0248);
    }

    @Generated
    public String internalMethod02898() {
        return this.internalField0248;
    }

    @Generated
    public Entity internalMethod05049() {
        return this.internalField0410;
    }

    @Generated
    public double internalMethod04167() {
        return this.internalField0194;
    }
}

