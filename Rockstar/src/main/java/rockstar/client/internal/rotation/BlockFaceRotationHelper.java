package rockstar.client.internal.rotation;



import rockstar.client.rotation.*;
import rockstar.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.internal.rotation.NewtonCoreManager;

public final class BlockFaceRotationHelper {
    private Rotation internalField0118;
    private int internalField0228;

    public boolean internalMethod05481(BlockPos blockPos, Direction direction) {
        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos).add((double)direction.getOffsetX() * 0.5, (double)direction.getOffsetY() * 0.5, (double)direction.getOffsetZ() * 0.5);
        Rotation typedValue266 = RotationUtils.internalMethod05580(vec3d);
        if (this.internalField0118 == null || this.internalField0118.internalMethod00735(typedValue266) > 0.5f) {
            this.internalField0118 = typedValue266;
            this.internalField0228 = 0;
        }
        RotationManager typedValue269 = NewtonCoreManager.internalMethod00114().internalMethod03241();
        typedValue269.internalMethod00418(typedValue266, RotationBehavior.internalField1003, 180.0f, 180.0f, 180.0f, RotationPriority.internalField0122);
        if (typedValue269.internalMethod08209().internalMethod00735(typedValue266) <= 1.5f) {
            ++this.internalField0228;
            return this.internalField0228 >= 2;
        }
        return false;
    }

    public void internalMethod06793() {
        this.internalField0118 = null;
        this.internalField0228 = 0;
    }
}

