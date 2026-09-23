package rockstar.client.internal.core;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.client.network.ClientPlayerEntity;
import rockstar.client.internal.script.MovementInputController;
import rockstar.client.internal.game.PathfindBlockView;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.rotation.AbstractPathStep;

public class FallMoveNode
extends AbstractPathStep {
    public static final int internalField0227 = 3;
    public static final int internalField0228 = 12;
    private boolean internalField0277;

    public FallMoveNode(PathNode typedValue296, PathNode typedValue297) {
        super(typedValue296, typedValue297);
    }

    @Override
    public double internalMethod01349() {
        int n = this.internalField0923.internalMethod02949() - this.internalField0924.internalMethod02949();
        return 1.2 + (double)n * 0.15;
    }

    @Override
    public boolean internalMethod01348() {
        return false;
    }

    @Override
    public boolean internalMethod01351() {
        return false;
    }

    @Override
    public boolean internalMethod04946(PathfindBlockView typedValue292) {
        int n = this.internalField0923.internalMethod02949() - this.internalField0924.internalMethod02949();
        if (n < 2 || n > 12) {
            return false;
        }
        if (Math.abs(this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945()) + Math.abs(this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945()) != 1) {
            return false;
        }
        double d = typedValue292.internalMethod05956(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945());
        if (Double.isNaN(d)) {
            d = this.internalField0923.internalMethod02949();
        }
        if (!typedValue292.internalMethod03056(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod07945(), this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod07945(), d + 0.05, d + 1.8)) {
            return false;
        }
        for (int i = this.internalField0923.internalMethod02949() - 1; i > this.internalField0924.internalMethod02949(); --i) {
            if (typedValue292.internalMethod09927(this.internalField0924.internalMethod02945(), i, this.internalField0924.internalMethod07945())) continue;
            return false;
        }
        this.internalField0277 = typedValue292.internalMethod07797(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        if (this.internalField0277) {
            if (typedValue292.internalMethod08739(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945())) {
                return false;
            }
            this.internalMethod01515(d, this.internalField0924.internalMethod02949());
            return true;
        }
        if (n > 3) {
            return false;
        }
        double d2 = typedValue292.internalMethod05956(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        if (Double.isNaN(d2)) {
            return false;
        }
        this.internalMethod01515(d, d2);
        return true;
    }

    @Override
    public AbstractPathStep.InternalType0058 internalMethod04014() {
        double d;
        double d2;
        boolean bl;
        ClientPlayerEntity clientPlayerEntity = FallMoveNode.internalMethod03778();
        if (clientPlayerEntity == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        double d3 = clientPlayerEntity.getY();
        double d4 = clientPlayerEntity.getX() - ((double)this.internalField0924.internalMethod02945() + 0.5);
        double d5 = clientPlayerEntity.getZ() - ((double)this.internalField0924.internalMethod07945() + 0.5);
        double d6 = Math.hypot(d4, d5);
        boolean bl2 = bl = clientPlayerEntity.isOnGround() || clientPlayerEntity.isTouchingWater();
        if (bl && d3 <= this.internalField1045 + 1.0 && d6 < 0.6) {
            return AbstractPathStep.InternalType0058.internalField0454;
        }
        if (d3 < this.internalField1045 - 1.5 && !clientPlayerEntity.isTouchingWater()) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        MovementInputController typedValue290 = FallMoveNode.internalMethod00577();
        typedValue290.internalMethod08359(false);
        typedValue290.internalMethod08371(false);
        typedValue290.internalMethod08033(false);
        typedValue290.internalMethod08045(false);
        typedValue290.internalMethod09358(false);
        if (clientPlayerEntity.isOnGround() && d3 > this.internalField1045 + 1.5) {
            this.internalMethod01443((double)this.internalField0924.internalMethod02945() + 0.5, (double)this.internalField0924.internalMethod07945() + 0.5);
            typedValue290.internalMethod03557(false);
            typedValue290.internalMethod03508(true);
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        float f = this.internalMethod01350();
        if (!Float.isNaN(f)) {
            FallMoveNode.internalMethod01516(f, 0.0f);
        }
        if ((d2 = this.internalMethod01442(clientPlayerEntity.getX(), clientPlayerEntity.getZ())) < (d = this.internalMethod08660()) - 0.25) {
            typedValue290.internalMethod03508(true);
            typedValue290.internalMethod03557(false);
        } else if (d2 > d + 0.3) {
            typedValue290.internalMethod03508(false);
            typedValue290.internalMethod03557(true);
        } else {
            typedValue290.internalMethod03508(false);
            typedValue290.internalMethod03557(false);
        }
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    @Override
    public void internalMethod01347() {
        MovementInputController typedValue290 = FallMoveNode.internalMethod00577();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod09358(false);
    }
}

