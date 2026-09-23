package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.EmptyStackException;

public class DoubleStack {
    private double[] internalField0612;
    private int internalField0227;

    public DoubleStack() {
        this(5);
    }

    public DoubleStack(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Stack's capacity must be positive");
        }
        this.internalField0612 = new double[n];
        this.internalField0227 = -1;
    }

    public void internalMethod06182(double d) {
        if (this.internalField0227 + 1 == this.internalField0612.length) {
            double[] dArray = new double[(int)((double)this.internalField0612.length * 1.2) + 1];
            System.arraycopy(this.internalField0612, 0, dArray, 0, this.internalField0612.length);
            this.internalField0612 = dArray;
        }
        this.internalField0612[++this.internalField0227] = d;
    }

    public double internalMethod03376() {
        if (this.internalField0227 == -1) {
            throw new EmptyStackException();
        }
        return this.internalField0612[this.internalField0227];
    }

    public double internalMethod03379() {
        if (this.internalField0227 == -1) {
            throw new EmptyStackException();
        }
        return this.internalField0612[this.internalField0227--];
    }

    public boolean internalMethod03378() {
        return this.internalField0227 == -1;
    }

    public int internalMethod03377() {
        return this.internalField0227 + 1;
    }
}

