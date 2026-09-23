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

public class DescendMoveNode
extends AbstractPathStep {
    public DescendMoveNode(PathNode typedValue296, PathNode typedValue297) {
        super(typedValue296, typedValue297);
    }

    @Override
    public double internalMethod01349() {
        return 1.0;
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
        double d;
        int n = this.internalField0924.internalMethod02949() - this.internalField0923.internalMethod02949();
        if (n > 0 || n < -1) {
            return false;
        }
        if (Math.abs(this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945()) + Math.abs(this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945()) != 1) {
            return false;
        }
        double d2 = typedValue292.internalMethod05956(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945());
        double d3 = typedValue292.internalMethod05956(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        if (Double.isNaN(d3)) {
            return false;
        }
        if (Double.isNaN(d2)) {
            d2 = this.internalField0923.internalMethod02949();
        }
        if ((d = d2 - d3) <= 0.62 || d > 1.3) {
            return false;
        }
        this.internalMethod01515(d2, d3);
        return typedValue292.internalMethod03056(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod07945(), this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod07945(), d2 + 0.05, d2 + 1.8);
    }

    @Override
    public AbstractPathStep.InternalType0058 internalMethod04014() {
        double d;
        double d2;
        ClientPlayerEntity clientPlayerEntity = DescendMoveNode.internalMethod03778();
        if (clientPlayerEntity == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        double d3 = clientPlayerEntity.getY();
        if (this.internalMethod05161(0.45) && d3 <= this.internalField1045 + 0.15 && (clientPlayerEntity.isOnGround() || clientPlayerEntity.isTouchingWater())) {
            return AbstractPathStep.InternalType0058.internalField0454;
        }
        if (d3 < this.internalField1045 - 1.2 && !clientPlayerEntity.isTouchingWater() && !clientPlayerEntity.isClimbing()) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        MovementInputController typedValue290 = DescendMoveNode.internalMethod00577();
        typedValue290.internalMethod08359(false);
        if (clientPlayerEntity.isOnGround() && d3 > this.internalField1045 + 0.5) {
            this.internalMethod01443((double)this.internalField0924.internalMethod02945() + 0.5, (double)this.internalField0924.internalMethod07945() + 0.5);
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField0452);
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        float f = this.internalMethod01350();
        if (!Float.isNaN(f)) {
            DescendMoveNode.internalMethod01516(f, 0.0f);
        }
        if ((d2 = this.internalMethod01442(clientPlayerEntity.getX(), clientPlayerEntity.getZ())) < (d = this.internalMethod08660()) - 0.25) {
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField0452);
        } else if (d2 > d + 0.25) {
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField1159);
        } else {
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField1158);
        }
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    @Override
    public void internalMethod01347() {
        MovementInputController typedValue290 = DescendMoveNode.internalMethod00577();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod09358(false);
    }
}

