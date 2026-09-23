package rockstar.client.internal.core;



import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.util.math.Vec2f;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.core.SwingTransform;

public final class SwingPreset {
    private final String internalField0248;
    private final Vec2f internalField0281;
    private final Vec2f internalField0280;
    private final boolean internalField0277;
    private final float internalField0205;
    private final SwingTransform internalField0735;
    private final SwingTransform internalField0736;
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1626);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1626);

    @Generated
    public SwingPreset(String string, Vec2f vec2f, Vec2f vec2f2, boolean bl, float f, SwingTransform typedValue112, SwingTransform typedValue113) {
        this.internalField0248 = string;
        this.internalField0281 = vec2f;
        this.internalField0280 = vec2f2;
        this.internalField0277 = bl;
        this.internalField0205 = f;
        this.internalField0735 = typedValue112;
        this.internalField0736 = typedValue113;
    }

    @Generated
    public String internalMethod02665() {
        return this.internalField0248;
    }

    @Generated
    public Vec2f internalMethod00271() {
        return this.internalField0281;
    }

    @Generated
    public Vec2f internalMethod05536() {
        return this.internalField0280;
    }

    @Generated
    public boolean internalMethod05999() {
        return this.internalField0277;
    }

    @Generated
    public float internalMethod05998() {
        return this.internalField0205;
    }

    @Generated
    public SwingTransform internalMethod02271() {
        return this.internalField0735;
    }

    @Generated
    public SwingTransform internalMethod03556() {
        return this.internalField0736;
    }

    @Generated
    public AnimatedValue internalMethod03126() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod03867() {
        return this.internalField0809;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SwingPreset)) {
            return false;
        }
        SwingPreset typedValue116 = (SwingPreset)object;
        if (this.internalMethod05999() != typedValue116.internalMethod05999()) {
            return false;
        }
        if (Float.compare(this.internalMethod05998(), typedValue116.internalMethod05998()) != 0) {
            return false;
        }
        String string = this.internalMethod02665();
        String string2 = typedValue116.internalMethod02665();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        Vec2f vec2f = this.internalMethod00271();
        Vec2f vec2f2 = typedValue116.internalMethod00271();
        if (vec2f == null ? vec2f2 != null : !vec2f.equals(vec2f2)) {
            return false;
        }
        Vec2f vec2f3 = this.internalMethod05536();
        Vec2f vec2f4 = typedValue116.internalMethod05536();
        if (vec2f3 == null ? vec2f4 != null : !vec2f3.equals(vec2f4)) {
            return false;
        }
        SwingTransform typedValue112 = this.internalMethod02271();
        SwingTransform typedValue113 = typedValue116.internalMethod02271();
        if (typedValue112 == null ? typedValue113 != null : !((Object)typedValue112).equals(typedValue113)) {
            return false;
        }
        SwingTransform typedValue114 = this.internalMethod03556();
        SwingTransform typedValue115 = typedValue116.internalMethod03556();
        if (typedValue114 == null ? typedValue115 != null : !((Object)typedValue114).equals(typedValue115)) {
            return false;
        }
        AnimatedValue typedValue210 = this.internalMethod03126();
        AnimatedValue typedValue211 = typedValue116.internalMethod03126();
        if (typedValue210 == null ? typedValue211 != null : !typedValue210.equals(typedValue211)) {
            return false;
        }
        AnimatedValue typedValue212 = this.internalMethod03867();
        AnimatedValue typedValue213 = typedValue116.internalMethod03867();
        return !(typedValue212 == null ? typedValue213 != null : !typedValue212.equals(typedValue213));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        n2 = n2 * 59 + (this.internalMethod05999() ? 79 : 97);
        n2 = n2 * 59 + Float.floatToIntBits(this.internalMethod05998());
        String string = this.internalMethod02665();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        Vec2f vec2f = this.internalMethod00271();
        n2 = n2 * 59 + (vec2f == null ? 43 : vec2f.hashCode());
        Vec2f vec2f2 = this.internalMethod05536();
        n2 = n2 * 59 + (vec2f2 == null ? 43 : vec2f2.hashCode());
        SwingTransform typedValue112 = this.internalMethod02271();
        n2 = n2 * 59 + (typedValue112 == null ? 43 : ((Object)typedValue112).hashCode());
        SwingTransform typedValue113 = this.internalMethod03556();
        n2 = n2 * 59 + (typedValue113 == null ? 43 : ((Object)typedValue113).hashCode());
        AnimatedValue typedValue210 = this.internalMethod03126();
        n2 = n2 * 59 + (typedValue210 == null ? 43 : typedValue210.hashCode());
        AnimatedValue typedValue211 = this.internalMethod03867();
        n2 = n2 * 59 + (typedValue211 == null ? 43 : typedValue211.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "SwingPreset(name=" + this.internalMethod02665() + ", bezierStart=" + String.valueOf(this.internalMethod00271()) + ", bezierEnd=" + String.valueOf(this.internalMethod05536()) + ", swingBack=" + this.internalMethod05999() + ", speed=" + this.internalMethod05998() + ", from=" + String.valueOf(this.internalMethod02271()) + ", to=" + String.valueOf(this.internalMethod03556()) + ", hoverAnimation=" + String.valueOf(this.internalMethod03126()) + ", activeAnimation=" + String.valueOf(this.internalMethod03867()) + ")";
    }
}

