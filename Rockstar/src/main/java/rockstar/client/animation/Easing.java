package rockstar.client.animation;



import rockstar.client.util.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.util.MathUtils;

public interface Easing {
    public static final Easing internalField0812 = Easing.internalMethod05127(0.45f, 1.45f, 0.49f, 1.15f);
    public static final Easing internalField0811 = Easing.internalMethod05127(0.45f, 1.45f, 0.43f, 0.91f);
    public static final Easing internalField1325 = Easing.internalMethod05127(0.1f, 1.07f, 0.34f, 1.04f);
    public static final Easing internalField1327 = Easing.internalMethod05127(0.27f, 1.09f, 0.49f, 1.06f);
    public static final Easing internalField1328 = Easing.internalMethod05127(0.62, -0.16, 0.8, 0.37);
    public static final Easing internalField1326 = Easing.internalMethod05127(0.25, 1.07, 0.11, 1.1);
    public static final Easing internalField1626 = Easing.internalMethod05127(0.42, 0.0, 0.58, 1.0);
    public static final Easing internalField1624 = (f, f2, f3, f4) -> {
        float f5 = f3 * f / f4 + f2;
        return (float)(-2.0 * Math.pow(f5, 3.0) + 3.0 * Math.pow(f5, 2.0));
    };
    public static final Easing internalField1631 = (f, f2, f3, f4) -> {
        float f5 = f3 * f / f4 + f2;
        return (double)f5 < 0.5 ? 4.0f * f5 * f5 * f5 : (float)(1.0 - Math.pow(-2.0f * f5 + 2.0f, 3.0) / 2.0);
    };
    public static final Easing internalField1627 = (f, f2, f3, f4) -> f3 * f / f4 + f2;
    public static final Easing internalField1628 = (f, f2, f3, f4) -> f3 * (f /= f4) * f + f2;
    public static final Easing internalField1630 = (f, f2, f3, f4) -> -f3 * (f /= f4) * (f - 2.0f) + f2;
    public static final Easing internalField1625 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return f3 / 2.0f * f * f + f2;
        }
        return -f3 / 2.0f * ((f -= 1.0f) * (f - 2.0f) - 1.0f) + f2;
    };
    public static final Easing internalField1629 = (f, f2, f3, f4) -> f3 * (f /= f4) * f * f + f2;
    public static final Easing internalField1822 = (f, f2, f3, f4) -> {
        f = f / f4 - 1.0f;
        return f3 * (f * f * f + 1.0f) + f2;
    };
    public static final Easing internalField1814 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return f3 / 2.0f * f * f * f + f2;
        }
        return f3 / 2.0f * ((f -= 2.0f) * f * f + 2.0f) + f2;
    };
    public static final Easing internalField1829 = (f, f2, f3, f4) -> f3 * (f /= f4) * f * f * f + f2;
    public static final Easing internalField1828 = (f, f2, f3, f4) -> {
        f = f / f4 - 1.0f;
        return -f3 * (f * f * f * f - 1.0f) + f2;
    };
    public static final Easing internalField1827 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return f3 / 2.0f * f * f * f * f + f2;
        }
        return -f3 / 2.0f * ((f -= 2.0f) * f * f * f - 2.0f) + f2;
    };
    public static final Easing internalField1826 = (f, f2, f3, f4) -> f3 * (f /= f4) * f * f * f * f + f2;
    public static final Easing internalField1825 = (f, f2, f3, f4) -> {
        f = f / f4 - 1.0f;
        return f3 * (f * f * f * f * f + 1.0f) + f2;
    };
    public static final Easing internalField1823 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return f3 / 2.0f * f * f * f * f * f + f2;
        }
        return f3 / 2.0f * ((f -= 2.0f) * f * f * f * f + 2.0f) + f2;
    };
    public static final Easing internalField1820 = (f, f2, f3, f4) -> -f3 * (float)MathUtils.internalMethod04929((double)(f / f4) * 1.5707963267948966) + f3 + f2;
    public static final Easing internalField1819 = (f, f2, f3, f4) -> f3 * (float)MathUtils.internalMethod04857((double)(f / f4) * 1.5707963267948966) + f2;
    public static final Easing internalField1818 = (f, f2, f3, f4) -> -f3 / 2.0f * ((float)MathUtils.internalMethod04929(Math.PI * (double)f / (double)f4) - 1.0f) + f2;
    public static final Easing internalField1817 = (f, f2, f3, f4) -> f == 0.0f ? f2 : f3 * (float)Math.pow(2.0, 10.0f * (f / f4 - 1.0f)) + f2;
    public static final Easing internalField1821 = (f, f2, f3, f4) -> f == f4 ? f2 + f3 : f3 * (-((float)Math.pow(2.0, -10.0f * f / f4)) + 1.0f) + f2;
    public static final Easing internalField1824 = (f, f2, f3, f4) -> {
        float f5;
        if (f == 0.0f) {
            return f2;
        }
        if (f == f4) {
            return f2 + f3;
        }
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return f3 / 2.0f * (float)Math.pow(2.0, 10.0f * (f - 1.0f)) + f2;
        }
        return f3 / 2.0f * (-((float)Math.pow(2.0, -10.0f * (f -= 1.0f))) + 2.0f) + f2;
    };
    public static final Easing internalField1816 = (f, f2, f3, f4) -> -f3 * ((float)Math.sqrt(1.0f - (f /= f4) * f) - 1.0f) + f2;
    public static final Easing internalField1815 = (f, f2, f3, f4) -> {
        f = f / f4 - 1.0f;
        return f3 * (float)Math.sqrt(1.0f - f * f) + f2;
    };
    public static final Easing internalField1927 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4 / 2.0f;
        if (f < 1.0f) {
            return -f3 / 2.0f * ((float)Math.sqrt(1.0f - f * f) - 1.0f) + f2;
        }
        return f3 / 2.0f * ((float)Math.sqrt(1.0f - (f -= 2.0f) * f) + 1.0f) + f2;
    };
    public static final InternalType0328 internalField0056 = new InternalType0329();
    public static final InternalType0328 internalField0057 = new InternalType0480();
    public static final InternalType0328 internalField0970 = new InternalType0479();
    public static final InternalType0051 internalField0868 = new InternalType0052();
    public static final InternalType0051 internalField0869 = new InternalType0327();
    public static final InternalType0051 internalField1364 = new InternalType0326();
    public static final Easing internalField1924 = (f, f2, f3, f4) -> {
        float f5;
        f /= f4;
        if (f < 0.36363637f) {
            return f3 * (7.5625f * f * f) + f2;
        }
        if (f < 0.72727275f) {
            return f3 * (7.5625f * (f -= 0.54545456f) * f + 0.75f) + f2;
        }
        if (f < 0.90909094f) {
            return f3 * (7.5625f * (f -= 0.8181818f) * f + 0.9375f) + f2;
        }
        return f3 * (7.5625f * (f -= 0.95454544f) * f + 0.984375f) + f2;
    };
    public static final Easing internalField1925 = (f, f2, f3, f4) -> f3 - internalField1924.ease(f4 - f, 0.0f, f3, f4) + f2;
    public static final Easing internalField1926 = (f, f2, f3, f4) -> {
        if (f < f4 / 2.0f) {
            return internalField1925.ease(f * 2.0f, 0.0f, f3, f4) * 0.5f + f2;
        }
        return internalField1924.ease(f * 2.0f - f4, 0.0f, f3, f4) * 0.5f + f3 * 0.5f + f2;
    };

    public static Easing internalMethod05127(final double d, final double d2, final double d3, final double d4) {
        return new Easing(){

            @Override
            public float ease(float f, float f2, float f3, float f4) {
                if (f4 <= 0.0f || f <= 0.0f) {
                    return f2;
                }
                if (f >= f4) {
                    return f2 + f3;
                }
                float f5 = f / f4;
                float f6 = this.internalMethod00254((float)d, (float)d3, f5);
                float f7 = this.internalMethod09139(f6, (float)d2, (float)d4);
                return f2 + f3 * f7;
            }

            private float internalMethod00254(float f, float f2, float f3) {
                float f4 = f3;
                int n = 8;
                float f5 = 1.0E-5f;
                for (int i = 0; i < 8; ++i) {
                    float f6 = this.internalMethod05314(f4, f, f2);
                    float f7 = this.internalMethod08188(f4, f, f2);
                    if (Math.abs(f6 - f3) < 1.0E-5f || Math.abs(f7) < 1.0E-6f) break;
                    f4 -= (f6 - f3) / f7;
                    f4 = Math.max(0.0f, Math.min(1.0f, f4));
                }
                return f4;
            }

            private float internalMethod05314(float f, float f2, float f3) {
                return 3.0f * (1.0f - f) * (1.0f - f) * f * f2 + 3.0f * (1.0f - f) * f * f * f3 + f * f * f;
            }

            private float internalMethod08188(float f, float f2, float f3) {
                return 3.0f * ((1.0f - f) * (1.0f - 3.0f * f) * f2 + (2.0f * f - 3.0f * f * f) * f3) + 3.0f * f * f;
            }

            private float internalMethod09139(float f, float f2, float f3) {
                return 3.0f * (1.0f - f) * (1.0f - f) * f * f2 + 3.0f * (1.0f - f) * f * f * f3 + f * f * f;
            }
        };
    }

    public float ease(float localValue1, float localValue2, float localValue3, float localValue4);

    public static class InternalType0329
    extends InternalType0328 {
        public InternalType0329(float f, float f2) {
            super(f, f2);
        }

        public InternalType0329() {
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5 = this.internalMethod00679();
            float f6 = this.internalMethod00684();
            if (f == 0.0f) {
                return f2;
            }
            if ((f /= f4) == 1.0f) {
                return f2 + f3;
            }
            if (f6 == 0.0f) {
                f6 = f4 * 0.3f;
            }
            float f7 = 0.0f;
            if (f5 < Math.abs(f3)) {
                f5 = f3;
                f7 = f6 / 4.0f;
            } else {
                f7 = f6 / ((float)Math.PI * 2) * (float)Math.asin(f3 / f5);
            }
            return -(f5 * (float)Math.pow(2.0, 10.0f * (f -= 1.0f)) * (float)MathUtils.internalMethod04857((double)(f * f4 - f7) * (Math.PI * 2) / (double)f6)) + f2;
        }
    }

    public static abstract class InternalType0328
    implements Easing {
        private float internalField0205;
        private float internalField0206;

        public InternalType0328(float f, float f2) {
            this.internalField0205 = f;
            this.internalField0206 = f2;
        }

        public InternalType0328() {
            this(-1.0f, 0.0f);
        }

        @Generated
        public void internalMethod01951(float f) {
            this.internalField0205 = f;
        }

        @Generated
        public void internalMethod02001(float f) {
            this.internalField0206 = f;
        }

        @Generated
        public float internalMethod00679() {
            return this.internalField0205;
        }

        @Generated
        public float internalMethod00684() {
            return this.internalField0206;
        }
    }

    public static class InternalType0480
    extends InternalType0328 {
        public InternalType0480(float f, float f2) {
            super(f, f2);
        }

        public InternalType0480() {
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5 = this.internalMethod00679();
            float f6 = this.internalMethod00684();
            if (f == 0.0f) {
                return f2;
            }
            if ((f /= f4) == 1.0f) {
                return f2 + f3;
            }
            if (f6 == 0.0f) {
                f6 = f4 * 0.3f;
            }
            float f7 = 0.0f;
            if (f5 < Math.abs(f3)) {
                f5 = f3;
                f7 = f6 / 4.0f;
            } else {
                f7 = f6 / ((float)Math.PI * 2) * (float)Math.asin(f3 / f5);
            }
            return f5 * (float)Math.pow(2.0, -10.0f * f) * (float)MathUtils.internalMethod04857((double)(f * f4 - f7) * (Math.PI * 2) / (double)f6) + f3 + f2;
        }
    }

    public static class InternalType0479
    extends InternalType0328 {
        public InternalType0479(float f, float f2) {
            super(f, f2);
        }

        public InternalType0479() {
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5 = this.internalMethod00679();
            float f6 = this.internalMethod00684();
            if (f == 0.0f) {
                return f2;
            }
            if ((f /= f4 / 2.0f) == 2.0f) {
                return f2 + f3;
            }
            if (f6 == 0.0f) {
                f6 = f4 * 0.45000002f;
            }
            float f7 = 0.0f;
            if (f5 < Math.abs(f3)) {
                f5 = f3;
                f7 = f6 / 4.0f;
            } else {
                f7 = f6 / ((float)Math.PI * 2) * (float)Math.asin(f3 / f5);
            }
            if (f < 1.0f) {
                return -0.5f * (f5 * (float)Math.pow(2.0, 10.0f * (f -= 1.0f)) * (float)MathUtils.internalMethod04857((double)(f * f4 - f7) * (Math.PI * 2) / (double)f6)) + f2;
            }
            return f5 * (float)Math.pow(2.0, -10.0f * (f -= 1.0f)) * (float)MathUtils.internalMethod04857((double)(f * f4 - f7) * (Math.PI * 2) / (double)f6) * 0.5f + f3 + f2;
        }
    }

    public static class InternalType0052
    extends InternalType0051 {
        public InternalType0052() {
        }

        public InternalType0052(float f) {
            super(f);
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5 = this.internalMethod05693();
            return f3 * (f /= f4) * f * ((f5 + 1.0f) * f - f5) + f2;
        }
    }

    public static abstract class InternalType0051
    implements Easing {
        public static final float internalField0205 = 1.70158f;
        private float internalField0206;

        public InternalType0051() {
            this(1.70158f);
        }

        public InternalType0051(float f) {
            this.internalField0206 = f;
        }

        @Generated
        public void internalMethod02404(float f) {
            this.internalField0206 = f;
        }

        @Generated
        public float internalMethod05693() {
            return this.internalField0206;
        }
    }

    public static class InternalType0327
    extends InternalType0051 {
        public InternalType0327() {
        }

        public InternalType0327(float f) {
            super(f);
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5 = this.internalMethod05693();
            f = f / f4 - 1.0f;
            return f3 * (f * f * ((f5 + 1.0f) * f + f5) + 1.0f) + f2;
        }
    }

    public static class InternalType0326
    extends InternalType0051 {
        public InternalType0326() {
        }

        public InternalType0326(float f) {
            super(f);
        }

        @Override
        public float ease(float f, float f2, float f3, float f4) {
            float f5;
            float f6 = this.internalMethod05693();
            f /= f4 / 2.0f;
            if (f < 1.0f) {
                return f3 / 2.0f * (f * f * (((f6 *= 1.525f) + 1.0f) * f - f6)) + f2;
            }
            return f3 / 2.0f * ((f -= 2.0f) * f * (((f6 *= 1.525f) + 1.0f) * f + f6) + 2.0f) + f2;
        }
    }
}
