package rockstar.client.internal.game;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.MinecraftClientAccess;

public class SmoothScrollHandler
implements MinecraftClientAccess {
    private double internalField0194;
    private double internalField0193 = 0.0;
    private double internalField1045 = 0.0;
    private double internalField1043 = 0.95;
    private double internalField1042 = 0.08;
    private double internalField1044 = 0.7;

    public void internalMethod02322() {
        this.internalField0193 += this.internalField1045;
        this.internalField1045 *= this.internalField1043;
        if (Math.abs(this.internalField1045) < 0.01) {
            this.internalField1045 = 0.0;
        }
        if (this.internalField0194 >= 0.0) {
            this.internalField0193 = 0.0;
            this.internalField1045 = 0.0;
            return;
        }
        if (this.internalField0193 > 0.0) {
            double d = -this.internalField0193 * this.internalField1042;
            this.internalField1045 += d;
            this.internalField1045 *= this.internalField1044;
        } else if (this.internalField0193 < this.internalField0194) {
            double d = (this.internalField0194 - this.internalField0193) * this.internalField1042;
            this.internalField1045 += d;
            this.internalField1045 *= this.internalField1044;
        }
    }

    public double internalMethod02321() {
        return -this.internalField0193;
    }

    public void internalMethod02328() {
        this.internalField0193 = 0.0;
        this.internalField1045 = 0.0;
    }

    public void internalMethod04249(double d) {
        this.internalField1045 += d;
    }

    public boolean internalMethod02323() {
        if (this.internalField1045 > 0.5) {
            return true;
        }
        return this.internalField0193 > 0.0 || this.internalField0193 < this.internalField0194;
    }

    public void internalMethod04250(int n) {
        if (n == 265) {
            this.internalMethod04249(1.0);
        } else if (n == 264) {
            this.internalMethod04249(-1.0);
        }
    }

    @Generated
    public double internalMethod02327() {
        return this.internalField0194;
    }

    @Generated
    public double internalMethod08484() {
        return this.internalField1045;
    }

    @Generated
    public double internalMethod08485() {
        return this.internalField1043;
    }

    @Generated
    public double internalMethod08493() {
        return this.internalField1042;
    }

    @Generated
    public double internalMethod08494() {
        return this.internalField1044;
    }

    @Generated
    public void internalMethod04308(double d) {
        this.internalField0194 = d;
    }

    @Generated
    public void internalMethod09052(double d) {
        this.internalField0193 = d;
    }

    @Generated
    public void internalMethod09059(double d) {
        this.internalField1045 = d;
    }

    @Generated
    public void internalMethod07841(double d) {
        this.internalField1043 = d;
    }

    @Generated
    public void internalMethod07854(double d) {
        this.internalField1042 = d;
    }

    @Generated
    public void internalMethod09693(double d) {
        this.internalField1044 = d;
    }
}

