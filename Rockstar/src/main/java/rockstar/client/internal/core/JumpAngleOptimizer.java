package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.JumpPhysicsModel;
import rockstar.client.internal.core.TerrainHeightSampler;

public final class JumpAngleOptimizer {
    private static final float[] internalField0615 = new float[]{-60.0f, -35.0f, -15.0f, 0.0f, 15.0f, 40.0f, 65.0f, 90.0f};
    private static final double internalField0194 = 50.0;
    private static final double internalField1045 = Double.NEGATIVE_INFINITY;
    private final JumpPhysicsModel.InternalType0401[] internalField0488 = JumpAngleOptimizer.internalMethod00166(internalField0615.length);
    private final JumpPhysicsModel.InternalType0401[] internalField0489 = JumpAngleOptimizer.internalMethod00166(internalField0615.length);
    private final double[] internalField0612 = new double[internalField0615.length];
    private final double[] internalField0613 = new double[internalField0615.length];
    private final float[] internalField0616 = new float[internalField0615.length];
    private final float[] internalField1238 = new float[internalField0615.length];
    private final float[] internalField1240 = new float[internalField0615.length];
    private final int internalField0227;
    private final JumpPhysicsModel.InternalType0401 internalField0842 = new JumpPhysicsModel.InternalType0401();

    public JumpAngleOptimizer(int n) {
        this.internalField0227 = Math.max(2, n);
    }

    private static JumpPhysicsModel.InternalType0401[] internalMethod00166(int n) {
        JumpPhysicsModel.InternalType0401[] nestedValue0150 = new JumpPhysicsModel.InternalType0401[n];
        for (int i = 0; i < n; ++i) {
            nestedValue0150[i] = new JumpPhysicsModel.InternalType0401();
        }
        return nestedValue0150;
    }

    public float internalMethod01373(JumpPhysicsModel.InternalType0401 nestedValue0149, float f, float f2, TerrainHeightSampler typedValue124, int n) {
        float f3 = f;
        double d = Double.NEGATIVE_INFINITY;
        int n2 = 0;
        for (float f4 : internalField0615) {
            float f5 = JumpAngleOptimizer.internalMethod02057(f, f4, f2);
            boolean bl = false;
            for (int i = 0; i < n2; ++i) {
                if (this.internalField1240[i] != f5) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            this.internalField1240[n2++] = f5;
            double d2 = this.internalMethod01372(nestedValue0149, f5, f2, typedValue124, n);
            if (!(d2 > d)) continue;
            d = d2;
            f3 = f5;
        }
        return f3;
    }

    private double internalMethod01372(JumpPhysicsModel.InternalType0401 nestedValue0149, float f, float f2, TerrainHeightSampler typedValue124, int n) {
        int n2;
        for (n2 = 0; n2 < internalField0615.length; ++n2) {
            this.internalField0612[n2] = Double.NEGATIVE_INFINITY;
        }
        this.internalField0488[0].internalMethod00806(nestedValue0149);
        this.internalField0612[0] = JumpAngleOptimizer.internalMethod02079(this.internalField0488[0], f, typedValue124, n);
        this.internalField0616[0] = f;
        for (n2 = 1; n2 < this.internalField0227; ++n2) {
            int n3;
            for (n3 = 0; n3 < internalField0615.length; ++n3) {
                this.internalField0613[n3] = Double.NEGATIVE_INFINITY;
            }
            for (n3 = 0; n3 < internalField0615.length; ++n3) {
                if (this.internalField0612[n3] == Double.NEGATIVE_INFINITY) continue;
                for (int i = 0; i < internalField0615.length; ++i) {
                    float f3 = JumpAngleOptimizer.internalMethod02057(this.internalField0616[n3], internalField0615[i], f2);
                    this.internalField0842.internalMethod00806(this.internalField0488[n3]);
                    double d = this.internalField0612[n3] + JumpAngleOptimizer.internalMethod02079(this.internalField0842, f3, typedValue124, n);
                    if (!(d > this.internalField0613[i])) continue;
                    this.internalField0613[i] = d;
                    this.internalField1238[i] = f3;
                    this.internalField0489[i].internalMethod00806(this.internalField0842);
                }
            }
            for (n3 = 0; n3 < internalField0615.length; ++n3) {
                this.internalField0612[n3] = this.internalField0613[n3];
                this.internalField0616[n3] = this.internalField1238[n3];
                if (this.internalField0613[n3] == Double.NEGATIVE_INFINITY) continue;
                this.internalField0488[n3].internalMethod00806(this.internalField0489[n3]);
            }
        }
        double d = Double.NEGATIVE_INFINITY;
        for (double d2 : this.internalField0612) {
            d = Math.max(d, d2);
        }
        return d;
    }

    private static double internalMethod02079(JumpPhysicsModel.InternalType0401 nestedValue0149, float f, TerrainHeightSampler typedValue124, int n) {
        double d = nestedValue0149.internalField0194;
        double d2 = d + nestedValue0149.internalField1045;
        double d3 = nestedValue0149.internalField1045;
        boolean bl = JumpPhysicsModel.internalMethod07469(nestedValue0149, f, typedValue124.internalMethod00952(d), typedValue124.internalMethod00952(d2), typedValue124.internalMethod00994(d2), n);
        double d4 = bl ? 50.0 + 100.0 * Math.max(0.0, d3) : 0.0;
        return nestedValue0149.internalField0194 - d - d4;
    }

    private static float internalMethod02057(float f, float f2, float f3) {
        float f4 = f2 - f;
        if (f4 > f3) {
            return f + f3;
        }
        if (f4 < -f3) {
            return f - f3;
        }
        return f2;
    }
}

