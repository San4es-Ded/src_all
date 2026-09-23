package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.util.math.Vec3d;
import rockstar.client.internal.core.BotBehavior;
import rockstar.client.internal.core.VelocityDecayBehavior;
import rockstar.client.bot.BotTargetManager;

public class GotoPositionTask
implements BotBehavior {
    private final Vec3d internalField0283;
    private final double internalField0194;

    public GotoPositionTask(Vec3d vec3d, double d) {
        this.internalField0283 = vec3d;
        this.internalField0194 = d;
    }

    public GotoPositionTask(double d, double d2, double d3) {
        this(new Vec3d(d + 0.5, d2, d3 + 0.5), 0.35);
    }

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        if (typedValue055 == null || this.internalField0283 == null) {
            return;
        }
        if (typedValue055.internalMethod02497(this.internalField0283) <= this.internalField0194) {
            typedValue055.internalMethod05456(new VelocityDecayBehavior());
            return;
        }
        typedValue055.internalMethod01196(this.internalField0283, this.internalField0194);
    }

    @Override
    public String internalMethod06553() {
        return "Goto " + Math.round(this.internalField0283.x) + " " + Math.round(this.internalField0283.y) + " " + Math.round(this.internalField0283.z);
    }

    @Generated
    public Vec3d internalMethod04861() {
        return this.internalField0283;
    }

    @Generated
    public double internalMethod05518() {
        return this.internalField0194;
    }
}

