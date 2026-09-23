package rockstar.client.rotation;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.RotationResetMode;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationSmoother;

public class RotationRequest {
    private final Rotation internalField0118;
    private final RotationBehavior internalField0115;
    private final float internalField0205;
    private final float internalField0206;
    private float internalField1048;
    private final int internalField0227;
    private final boolean internalField0277;
    private RotationResetMode internalField0117 = RotationResetMode.internalField0116;
    private RotationSmoother internalField0123;

    public RotationRequest(Rotation typedValue266, RotationBehavior typedValue264, float f, float f2, float f3, int n) {
        this(typedValue266, typedValue264, f, f2, f3, n, true);
    }

    public RotationRequest(Rotation typedValue266, RotationBehavior typedValue264, float f, float f2, float f3, int n, boolean bl) {
        this.internalField0118 = typedValue266;
        this.internalField0115 = typedValue264;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField0227 = n;
        this.internalField1048 = f3;
        this.internalField0277 = bl;
    }

    public RotationRequest(Rotation typedValue266, float f, float f2, long l, int n) {
        this(typedValue266, RotationBehavior.internalField0115, f, f2, l, n);
    }

    @Generated
    public Rotation internalMethod01388() {
        return this.internalField0118;
    }

    @Generated
    public RotationBehavior internalMethod01386() {
        return this.internalField0115;
    }

    @Generated
    public float internalMethod00376() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod00840() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod07915() {
        return this.internalField1048;
    }

    @Generated
    public int internalMethod00377() {
        return this.internalField0227;
    }

    @Generated
    public boolean internalMethod00378() {
        return this.internalField0277;
    }

    @Generated
    public RotationResetMode internalMethod01387() {
        return this.internalField0117;
    }

    @Generated
    public RotationSmoother internalMethod01390() {
        return this.internalField0123;
    }

    @Generated
    public void internalMethod01156(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod03988(RotationResetMode typedValue265) {
        this.internalField0117 = typedValue265;
    }

    @Generated
    public void internalMethod01624(RotationSmoother typedValue271) {
        this.internalField0123 = typedValue271;
    }
}

