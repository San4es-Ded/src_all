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

public class ParkourMoveNode
extends AbstractPathStep {
    private final int internalField0228;
    private boolean internalField0277;
    private boolean internalField0276;
    private int internalField1053;
    private double internalField1043 = Double.NaN;

    public ParkourMoveNode(PathNode typedValue296, PathNode typedValue297) {
        super(typedValue296, typedValue297);
        this.internalField0228 = (int)Math.round(this.internalMethod08660()) - 1;
    }

    @Override
    public double internalMethod01349() {
        return this.internalField0228 <= 1 ? 2.5 : 4.0;
    }

    @Override
    public boolean internalMethod01348() {
        return false;
    }

    @Override
    public boolean internalMethod01351() {
        return this.internalField0277;
    }

    @Override
    public boolean internalMethod04946(PathfindBlockView typedValue292) {
        int n;
        int n2;
        int n3;
        if (this.internalField0924.internalMethod02949() != this.internalField0923.internalMethod02949()) {
            return false;
        }
        int n4 = this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945();
        int n5 = this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945();
        if (n4 != 0 && n5 != 0) {
            return false;
        }
        int n6 = Math.abs(n4) + Math.abs(n5);
        if (n6 < 2 || n6 > 3) {
            return false;
        }
        double d = typedValue292.internalMethod05956(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945());
        double d2 = typedValue292.internalMethod05956(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        if (Double.isNaN(d) || Double.isNaN(d2)) {
            return false;
        }
        if (Math.abs(d2 - d) > 0.3) {
            return false;
        }
        int n7 = Integer.signum(n4);
        int n8 = Integer.signum(n5);
        for (n3 = 1; n3 < n6; ++n3) {
            n2 = this.internalField0923.internalMethod02945() + n7 * n3;
            n = this.internalField0923.internalMethod07945() + n8 * n3;
            if (typedValue292.internalMethod09396(n2, this.internalField0923.internalMethod02949(), n)) {
                return false;
            }
            if (!typedValue292.internalMethod05958(n2, this.internalField0923.internalMethod02949(), n)) {
                return false;
            }
            if (!typedValue292.internalMethod05958(n2, this.internalField0923.internalMethod02949() + 1, n)) {
                return false;
            }
            for (int i = this.internalField0923.internalMethod02949() - 1; i >= this.internalField0923.internalMethod02949() - 4; --i) {
                if (!typedValue292.internalMethod08739(n2, i, n)) continue;
                return false;
            }
        }
        if (!typedValue292.internalMethod03056(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod07945(), this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod07945(), Math.max(d, d2) + 0.05, d + 2.1)) {
            return false;
        }
        n3 = this.internalField0924.internalMethod02945() + n7;
        n2 = this.internalField0924.internalMethod07945() + n8;
        int n9 = n = typedValue292.internalMethod09396(n3, this.internalField0924.internalMethod02949(), n2) && Math.abs(typedValue292.internalMethod05956(n3, this.internalField0924.internalMethod02949(), n2) - d2) <= 0.62 ? 1 : 0;
        if (this.internalField0228 >= 2 && n == 0) {
            return false;
        }
        this.internalField0277 = this.internalField0228 >= 2;
        this.internalMethod01515(d, d2);
        return true;
    }

    @Override
    public AbstractPathStep.InternalType0058 internalMethod04014() {
        double d;
        ClientPlayerEntity clientPlayerEntity = ParkourMoveNode.internalMethod03778();
        if (clientPlayerEntity == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        double d2 = clientPlayerEntity.getY();
        if (d2 < Math.min(this.internalField0193, this.internalField1045) - 1.5 && !clientPlayerEntity.isTouchingWater()) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        double d3 = this.internalMethod08660();
        double d4 = this.internalMethod01442(clientPlayerEntity.getX(), clientPlayerEntity.getZ());
        if (clientPlayerEntity.isOnGround() && this.internalField0276 && d4 > d3 - 0.75 && Math.abs(d2 - this.internalField1045) < 0.7) {
            return AbstractPathStep.InternalType0058.internalField0454;
        }
        this.internalMethod01443((double)this.internalField0924.internalMethod02945() + 0.5, (double)this.internalField0924.internalMethod07945() + 0.5);
        MovementInputController typedValue290 = ParkourMoveNode.internalMethod00577();
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod08033(false);
        typedValue290.internalMethod08045(false);
        typedValue290.internalMethod08371(false);
        if (!clientPlayerEntity.isOnGround()) {
            this.internalField1043 = Double.NaN;
            double d5 = d3 - d4;
            typedValue290.internalMethod08359(false);
            if (this.internalField0277) {
                boolean bl = d5 > 0.4;
                typedValue290.internalMethod03508(bl);
                typedValue290.internalMethod03557(false);
                typedValue290.internalMethod09358(bl);
            } else {
                typedValue290.internalMethod09358(false);
                if (d5 > 1.0) {
                    typedValue290.internalMethod03508(true);
                    typedValue290.internalMethod03557(false);
                } else if (d5 > 0.35) {
                    typedValue290.internalMethod03508(false);
                    typedValue290.internalMethod03557(false);
                } else {
                    typedValue290.internalMethod03508(false);
                    typedValue290.internalMethod03557(true);
                }
            }
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        double d6 = this.internalMethod01513(clientPlayerEntity.getX(), clientPlayerEntity.getZ());
        double d7 = Double.isNaN(this.internalField1043) ? this.internalMethod08658() : d4 - this.internalField1043;
        this.internalField1043 = d4;
        if (d6 > 0.45 || d4 < -0.4) {
            this.internalMethod01443((double)this.internalField0923.internalMethod02945() + 0.5, (double)this.internalField0923.internalMethod07945() + 0.5);
            typedValue290.internalMethod03508(true);
            typedValue290.internalMethod09358(false);
            typedValue290.internalMethod08359(false);
            if (++this.internalField1053 >= 40) {
                return AbstractPathStep.InternalType0058.internalField1160;
            }
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        double d8 = this.internalField0277 ? 0.18 : 0.11;
        double d9 = this.internalField0277 ? 10.0 : 0.24;
        double d10 = d = this.internalField0277 ? 0.25 : 0.1;
        if (d7 > d9 && d4 > -0.2) {
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField1158);
            typedValue290.internalMethod08359(false);
            if (++this.internalField1053 >= 40) {
                return AbstractPathStep.InternalType0058.internalField1160;
            }
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        if (d7 >= d8 && d7 <= d9 && d4 + d7 >= d && d4 >= d - 0.35) {
            typedValue290.internalMethod03508(true);
            typedValue290.internalMethod09358(this.internalField0277);
            typedValue290.internalMethod08359(true);
            this.internalField0276 = true;
            this.internalField1053 = 0;
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        if (d4 > d + 0.1 && d7 < d8) {
            this.internalMethod06359(typedValue290, AbstractPathStep.InternalType0057.internalField1159);
            typedValue290.internalMethod08359(false);
            if (++this.internalField1053 >= 40) {
                return AbstractPathStep.InternalType0058.internalField1160;
            }
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        typedValue290.internalMethod03508(true);
        typedValue290.internalMethod09358(this.internalField0277);
        typedValue290.internalMethod08359(false);
        this.internalField1053 = 0;
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    @Override
    public void internalMethod01347() {
        MovementInputController typedValue290 = ParkourMoveNode.internalMethod00577();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod08359(false);
        typedValue290.internalMethod09358(false);
        this.internalField0276 = false;
        this.internalField1053 = 0;
    }
}

