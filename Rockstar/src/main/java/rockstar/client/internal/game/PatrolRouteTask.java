package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import rockstar.client.internal.core.BotBehavior;
import rockstar.client.bot.BotTargetManager;

public class PatrolRouteTask
implements BotBehavior {
    private final List<Vec3d> internalField0416 = new ArrayList<Vec3d>();
    private int internalField0227;

    public void internalMethod05146(Vec3d vec3d) {
        if (vec3d != null) {
            this.internalField0416.add(vec3d);
        }
    }

    public void internalMethod05642() {
        this.internalField0416.clear();
        this.internalField0227 = 0;
    }

    public boolean internalMethod05643() {
        return this.internalField0416.isEmpty();
    }

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        if (typedValue055 == null || this.internalField0416.isEmpty()) {
            return;
        }
        Vec3d vec3d = this.internalField0416.get(this.internalField0227);
        if (typedValue055.internalMethod02497(vec3d) <= typedValue055.internalMethod06687().internalMethod09859()) {
            this.internalField0227 = (this.internalField0227 + 1) % this.internalField0416.size();
            vec3d = this.internalField0416.get(this.internalField0227);
        }
        typedValue055.internalMethod01196(vec3d, typedValue055.internalMethod06687().internalMethod09859());
    }

    @Override
    public String internalMethod06553() {
        return "Patrol " + this.internalField0416.size();
    }
}

