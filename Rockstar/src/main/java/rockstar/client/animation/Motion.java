package rockstar.client.animation;


import rockstar.client.*;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;

public abstract class Motion {
    public static final Motion internalField0913 = Motion.internalMethod01328(300L, Easing.internalField0812);
    public static final Motion internalField0914 = Motion.internalMethod01328(450L, Easing.internalField0811);
    public static final Motion internalField1384 = Motion.internalMethod01328(250L, Easing.internalField1626);
    public static final Motion internalField1383 = Motion.internalMethod01328(150L, Easing.internalField1828);
    public static final Motion internalField1381 = Motion.internalMethod01328(200L, Easing.internalField1828);
    public static final Motion internalField1382 = Motion.internalMethod07185(220.0f, 24.0f);
    public static final Motion internalField1657 = Motion.internalMethod07185(380.0f, 30.0f);

    public abstract void internalMethod07028(AnimatedFloat localValue1);

    public abstract void internalMethod05226(AnimatedFloat localValue1, float localValue2);

    public boolean internalMethod00265(AnimatedFloat typedParameter002) {
        return Math.abs(typedParameter002.internalField0205 - typedParameter002.internalField0206) < 0.01f && Math.abs(typedParameter002.internalField1048) < 0.01f;
    }

    public static Motion internalMethod01328(long l, Easing typedValue214) {
        return new InternalType0035(l, typedValue214);
    }

    public static Motion internalMethod01870(long l) {
        return Motion.internalMethod01328(l, Easing.internalField1627);
    }

    public static Motion internalMethod07185(float f, float f2) {
        return new InternalType0034(f, f2);
    }

    static final class InternalType0035
    extends Motion {
        private final long internalField0229;
        private final Easing internalField0812;

        InternalType0035(long l, Easing typedValue214) {
            this.internalField0229 = Math.max(1L, l);
            this.internalField0812 = typedValue214;
        }

        @Override
        public void internalMethod07028(AnimatedFloat typedParameter002) {
            typedParameter002.internalField1047 = typedParameter002.internalField0205;
            typedParameter002.internalField1049 = 0.0f;
        }

        @Override
        public void internalMethod05226(AnimatedFloat typedParameter002, float f) {
            if (typedParameter002.internalField0205 == typedParameter002.internalField0206) {
                return;
            }
            typedParameter002.internalField1049 += f;
            float f2 = Math.min(1.0f, typedParameter002.internalField1049 / (float)this.internalField0229);
            float f3 = this.internalField0812.ease(f2, 0.0f, 1.0f, 1.0f);
            typedParameter002.internalField0205 = typedParameter002.internalField1047 + (typedParameter002.internalField0206 - typedParameter002.internalField1047) * f3;
            if (f2 >= 1.0f) {
                typedParameter002.internalField0205 = typedParameter002.internalField0206;
                typedParameter002.internalField1048 = 0.0f;
            }
        }

        @Override
        public boolean internalMethod00265(AnimatedFloat typedParameter002) {
            return typedParameter002.internalField0205 == typedParameter002.internalField0206;
        }
    }

    static final class InternalType0034
    extends Motion {
        private final float internalField0205;
        private final float internalField0206;

        InternalType0034(float f, float f2) {
            this.internalField0205 = f;
            this.internalField0206 = f2;
        }

        @Override
        public void internalMethod07028(AnimatedFloat typedParameter002) {
        }

        @Override
        public void internalMethod05226(AnimatedFloat typedParameter002, float f) {
            float f2 = Math.min(0.05f, f / 1000.0f);
            if (f2 <= 0.0f) {
                return;
            }
            float f3 = typedParameter002.internalField0205 - typedParameter002.internalField0206;
            float f4 = -this.internalField0205 * f3 - this.internalField0206 * typedParameter002.internalField1048;
            typedParameter002.internalField1048 += f4 * f2;
            typedParameter002.internalField0205 += typedParameter002.internalField1048 * f2;
            if (Math.abs(f3) < 0.05f && Math.abs(typedParameter002.internalField1048) < 0.05f) {
                typedParameter002.internalField0205 = typedParameter002.internalField0206;
                typedParameter002.internalField1048 = 0.0f;
            }
        }
    }
}

