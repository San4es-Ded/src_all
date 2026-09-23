package rockstar.client.internal.core;


import rockstar.client.*;
public final class TerrainHeightSampler {
    public static final double internalField0194 = -4096.0;
    private final double[] internalField0612;
    private final double internalField0193;
    private int internalField0227;

    public TerrainHeightSampler(int n, double d) {
        this.internalField0612 = new double[n];
        this.internalField0193 = d;
    }

    public void internalMethod01747() {
        this.internalField0227 = 0;
    }

    public void internalMethod00954(double d) {
        if (this.internalField0227 < this.internalField0612.length) {
            this.internalField0612[this.internalField0227++] = d;
        }
    }

    public int internalMethod01746() {
        return this.internalField0612.length;
    }

    public double internalMethod01745() {
        return this.internalField0193;
    }

    public double internalMethod00952(double d) {
        if (this.internalField0227 == 0) {
            return -4096.0;
        }
        int n = this.internalMethod00953(d);
        if (n < 0) {
            return this.internalField0612[0];
        }
        if (n >= this.internalField0227 - 1) {
            return this.internalField0612[this.internalField0227 - 1];
        }
        return Math.min(this.internalField0612[n], this.internalField0612[n + 1]);
    }

    public double internalMethod00994(double d) {
        if (this.internalField0227 == 0) {
            return -4096.0;
        }
        int n = this.internalMethod00953(d);
        if (n < 0) {
            return this.internalField0612[0];
        }
        if (n >= this.internalField0227 - 1) {
            return this.internalField0612[this.internalField0227 - 1];
        }
        return Math.max(this.internalField0612[n], this.internalField0612[n + 1]);
    }

    private int internalMethod00953(double d) {
        double d2 = d / this.internalField0193;
        return d2 <= 0.0 ? -1 : (int)d2;
    }
}

