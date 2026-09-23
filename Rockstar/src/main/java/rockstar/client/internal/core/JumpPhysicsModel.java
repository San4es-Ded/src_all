package rockstar.client.internal.core;


import rockstar.client.*;
public final class JumpPhysicsModel {
    public static final double internalField0194 = 0.08;
    public static final double internalField0193 = 0.42;
    public static final double internalField1045 = 0.2;
    public static final double internalField1043 = 0.6;

    private JumpPhysicsModel() {
    }

    public static boolean internalMethod07469(InternalType0401 nestedValue0149, double d, double d2, double d3, double d4, int n) {
        double d5;
        boolean bl;
        boolean bl2;
        boolean bl3 = nestedValue0149.internalField0276 || !nestedValue0149.internalField0277;
        boolean bl4 = bl2 = bl3 && !nestedValue0149.internalField1099;
        if (bl2 && nestedValue0149.internalField0276) {
            nestedValue0149.internalField1043 = Math.max(0.42, nestedValue0149.internalField1043);
            nestedValue0149.internalField1045 += 0.2;
        }
        if (bl2 && !nestedValue0149.internalField0276 && !nestedValue0149.internalField0277) {
            nestedValue0149.internalField0277 = true;
        }
        if (nestedValue0149.internalField0277) {
            JumpPhysicsModel.internalMethod01967(nestedValue0149, d);
        } else if (nestedValue0149.internalField0276) {
            nestedValue0149.internalField1045 = (nestedValue0149.internalField1045 + 0.13) * 0.546;
            nestedValue0149.internalField1043 = (nestedValue0149.internalField1043 - 0.08) * 0.98;
        } else {
            nestedValue0149.internalField1045 = (nestedValue0149.internalField1045 + 0.026) * 0.91;
            nestedValue0149.internalField1043 = (nestedValue0149.internalField1043 - 0.08) * 0.98;
        }
        if (nestedValue0149.internalField0227 >= 0 && --nestedValue0149.internalField0227 < 0) {
            nestedValue0149.internalField0277 = false;
        }
        double d6 = nestedValue0149.internalField0193 + nestedValue0149.internalField1043;
        boolean bl5 = bl = d4 > d2 + 0.6 && d4 > Math.max(nestedValue0149.internalField0193, d6);
        if (bl) {
            nestedValue0149.internalField1045 = 0.0;
        } else {
            nestedValue0149.internalField0194 += nestedValue0149.internalField1045;
        }
        double d7 = d5 = bl ? d2 : d3;
        if (d6 <= d5) {
            nestedValue0149.internalField0193 = d5;
            nestedValue0149.internalField1043 = 0.0;
            nestedValue0149.internalField0276 = true;
            if (nestedValue0149.internalField0227 < 0 && nestedValue0149.internalField0277) {
                nestedValue0149.internalField0227 = n;
            }
        } else {
            nestedValue0149.internalField0193 = d6;
            nestedValue0149.internalField0276 = false;
        }
        nestedValue0149.internalField1099 = bl2;
        return bl;
    }

    private static void internalMethod01967(InternalType0401 nestedValue0149, double d) {
        double d2;
        double d3 = Math.toRadians(d);
        double d4 = Math.cos(d3);
        double d5 = d4 * d4;
        double d6 = Math.abs(nestedValue0149.internalField1045);
        nestedValue0149.internalField1043 += 0.08 * (-1.0 + d5 * 0.75);
        if (nestedValue0149.internalField1043 < 0.0 && d4 > 0.0) {
            d2 = nestedValue0149.internalField1043 * -0.1 * d5;
            nestedValue0149.internalField1045 += d2;
            nestedValue0149.internalField1043 += d2;
        }
        if (d3 < 0.0 && d4 > 0.0) {
            d2 = d6 * -Math.sin(d3) * 0.04;
            nestedValue0149.internalField1045 -= d2;
            nestedValue0149.internalField1043 += d2 * 3.2;
        }
        if (d4 > 0.0) {
            nestedValue0149.internalField1045 += (d6 - nestedValue0149.internalField1045) * 0.1;
        }
        nestedValue0149.internalField1045 *= 0.99;
        nestedValue0149.internalField1043 *= 0.98;
    }

    public static final class InternalType0401 {
        public double internalField0194;
        public double internalField0193;
        public double internalField1045;
        public double internalField1043;
        public boolean internalField0277;
        public boolean internalField0276;
        public boolean internalField1099;
        public int internalField0227;

        public void internalMethod00806(InternalType0401 nestedValue0149) {
            this.internalField0194 = nestedValue0149.internalField0194;
            this.internalField0193 = nestedValue0149.internalField0193;
            this.internalField1045 = nestedValue0149.internalField1045;
            this.internalField1043 = nestedValue0149.internalField1043;
            this.internalField0277 = nestedValue0149.internalField0277;
            this.internalField0276 = nestedValue0149.internalField0276;
            this.internalField1099 = nestedValue0149.internalField1099;
            this.internalField0227 = nestedValue0149.internalField0227;
        }
    }
}

