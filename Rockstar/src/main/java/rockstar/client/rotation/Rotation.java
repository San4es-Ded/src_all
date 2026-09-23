package rockstar.client.rotation;



import rockstar.client.util.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.util.math.Vec3d;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;

public class Rotation
implements MinecraftClientAccess {
    public static final Rotation internalField0118 = new Rotation(0.0f, 0.0f);
    private float internalField0205;
    private float internalField0206;

    public Rotation(double d, double d2) {
        this.internalField0205 = (float)d;
        this.internalField0206 = (float)d2;
    }

    public final Rotation internalMethod01178(Rotation typedValue266) {
        float f = MathUtils.internalMethod09441(this.internalField0205, typedValue266.internalField0205);
        float f2 = MathUtils.internalMethod09441(this.internalField0206, typedValue266.internalField0206);
        return new Rotation(f, f2);
    }

    public final float internalMethod00735(Rotation typedValue266) {
        float f = MathUtils.internalMethod09441(this.internalField0205, typedValue266.internalField0205);
        float f2 = MathUtils.internalMethod09441(this.internalField0206, typedValue266.internalField0206);
        return Math.abs(f) + Math.abs(f2);
    }

    public final Vec3d internalMethod06001() {
        return Rotation.internalField0149.player.getRotationVector(this.internalField0206, this.internalField0205);
    }

    public final Rotation internalMethod05809(float f, float f2) {
        return new Rotation(this.internalField0205 + f, this.internalField0206 + f2);
    }

    @Generated
    public float internalMethod00169() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod00171() {
        return this.internalField0206;
    }

    @Generated
    public void internalMethod03239(float f) {
        this.internalField0205 = f;
    }

    @Generated
    public void internalMethod03289(float f) {
        this.internalField0206 = f;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Rotation)) {
            return false;
        }
        Rotation typedValue266 = (Rotation)object;
        if (!typedValue266.internalMethod06016(this)) {
            return false;
        }
        if (Float.compare(this.internalMethod00169(), typedValue266.internalMethod00169()) != 0) {
            return false;
        }
        return Float.compare(this.internalMethod00171(), typedValue266.internalMethod00171()) == 0;
    }

    @Generated
    protected boolean internalMethod06016(Object object) {
        return object instanceof Rotation;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + Float.floatToIntBits(this.internalMethod00169());
        n2 = n2 * 59 + Float.floatToIntBits(this.internalMethod00171());
        return n2;
    }

    @Generated
    public String toString() {
        return "Rotation(yaw=" + this.internalMethod00169() + ", pitch=" + this.internalMethod00171() + ")";
    }

    @Generated
    public Rotation(float f, float f2) {
        this.internalField0205 = f;
        this.internalField0206 = f2;
    }
}
