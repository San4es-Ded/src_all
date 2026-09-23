package haron.animation;

import haron.animation.EasingFunction;

public final class Easings {
    public static final double BACK_OVERSHOOT = 1.70158;
    public static final double BACK_OVERSHOOT_IN_OUT = 2.5949095;
    public static final double BACK_OVERSHOOT_PLUS_ONE = 2.70158;
    public static final double ELASTIC_PERIOD = 2.0943951023931953;
    public static final double ELASTIC_PERIOD_IN_OUT = 1.3962634015954636;
    public static final EasingFunction f = d -> {
        int n = 123;
        return d;
    };
    public static final EasingFunction g = Easings.a(2.0);
    public static final EasingFunction h = Easings.b(2.0);
    public static final EasingFunction i = Easings.c(2.0);
    public static final EasingFunction j = Easings.a(3.0);
    public static final EasingFunction k = Easings.b(3.0);
    public static final EasingFunction l = Easings.c(3.0);
    public static final EasingFunction m = Easings.a(4.0);
    public static final EasingFunction n = Easings.b(4.0);
    public static final EasingFunction o = Easings.c(4.0);
    public static final EasingFunction p = Easings.a(5.0);
    public static final EasingFunction q = Easings.b(5.0);
    public static final EasingFunction r = Easings.c(5.0);
    public static final EasingFunction s = d -> {
        return 1.0 - Math.cos(d * Math.PI / 2.0);
    };
    public static final EasingFunction t = d -> {
        return Math.sin(d * Math.PI / 2.0);
    };
    public static final EasingFunction u = d -> {
        return -(Math.cos(Math.PI * d) - 1.0) / 2.0;
    };
    public static final EasingFunction v = d -> {
        return 1.0 - Math.sqrt(1.0 - Math.pow(d, 2.0));
    };
    public static final EasingFunction w = d -> {
        return Math.sqrt(1.0 - Math.pow(d - 1.0, 2.0));
    };
    public static final EasingFunction x = d -> d < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * d, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * d + 2.0, 2.0)) + 1.0) / 2.0;
    public static final EasingFunction y = d -> {
        if (d == 0.0) {
            return 0.0;
        }
        return Math.pow(2.0, 10.0 * d - 10.0);
    };
    public static final EasingFunction z = d -> {
        if (d == 1.0) {
            return 1.0;
        }
        return 1.0 - Math.pow(2.0, -10.0 * d);
    };
    public static final EasingFunction A = d -> d == 0.0 || d == 1.0 ? d : (d < 0.5 ? Math.pow(2.0, 20.0 * d - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * d + 10.0)) / 2.0);
    public static final EasingFunction B = d -> 2.70158 * Math.pow(d, 3.0) - 1.70158 * Math.pow(d, 2.0);
    public static final EasingFunction C = d -> 1.0 + 2.70158 * Math.pow(d - 1.0, 3.0) + 1.70158 * Math.pow(d - 1.0, 2.0);
    public static final EasingFunction D = d -> d < 0.5 ? Math.pow(2.0 * d, 2.0) * (7.189819 * d - 2.5949095) / 2.0 : (Math.pow(2.0 * d - 2.0, 2.0) * (3.5949095 * (d * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
    public static final EasingFunction E = d -> d == 0.0 || d == 1.0 ? d : -Math.pow(2.0, 10.0 * d - 10.0) * Math.sin((d * 10.0 - 10.75) * 2.0943951023931953);
    public static final EasingFunction F = d -> d == 0.0 || d == 1.0 ? d : Math.pow(2.0, -10.0 * d) * Math.sin((d * 10.0 - 0.75) * 2.0943951023931953) + 1.0;
    public static final EasingFunction G = d -> d == 0.0 || d == 1.0 ? d : (d < 0.5 ? -(Math.pow(2.0, 20.0 * d - 10.0) * Math.sin((20.0 * d - 11.125) * 1.3962634015954636)) / 2.0 : Math.pow(2.0, -20.0 * d + 10.0) * Math.sin((20.0 * d - 11.125) * 1.3962634015954636) / 2.0 + 1.0);
    public static final EasingFunction H = d -> {
        if (d < 0.36363636363636365) {
            return 7.5625 * d * d;
        }
        if (d < 0.7272727272727273) {
            double d2 = d - 0.5454545454545454;
            return 7.5625 * d2 * d2 + 0.75;
        }
        if (d < 0.9090909090909091) {
            double d3 = d - 0.8181818181818182;
            return 7.5625 * d3 * d3 + 0.9375;
        }
        double d4 = d - 0.9545454545454546;
        return 7.5625 * d4 * d4 + 0.984375;
    };
    public static final EasingFunction I = d -> {
        return 1.0 - H.ease(1.0 - d);
    };
    public static final EasingFunction J = d -> d < 0.5 ? (1.0 - H.ease(1.0 - 2.0 * d)) / 2.0 : (1.0 + H.ease(2.0 * d - 1.0)) / 2.0;

    public static final EasingFunction LINEAR = f;
    public static final EasingFunction EASE_IN_QUAD = g;
    public static final EasingFunction EASE_OUT_QUAD = h;
    public static final EasingFunction EASE_IN_OUT_QUAD = i;
    public static final EasingFunction EASE_IN_CUBIC = j;
    public static final EasingFunction EASE_OUT_CUBIC = k;
    public static final EasingFunction EASE_IN_OUT_CUBIC = l;
    public static final EasingFunction EASE_IN_QUART = m;
    public static final EasingFunction EASE_OUT_QUART = n;
    public static final EasingFunction EASE_IN_OUT_QUART = o;
    public static final EasingFunction EASE_IN_QUINT = p;
    public static final EasingFunction EASE_OUT_QUINT = q;
    public static final EasingFunction EASE_IN_OUT_QUINT = r;
    public static final EasingFunction EASE_IN_SINE = s;
    public static final EasingFunction EASE_OUT_SINE = t;
    public static final EasingFunction EASE_IN_OUT_SINE = u;
    public static final EasingFunction EASE_IN_CIRC = v;
    public static final EasingFunction EASE_OUT_CIRC = w;
    public static final EasingFunction EASE_IN_OUT_CIRC = x;
    public static final EasingFunction EASE_IN_EXPO = y;
    public static final EasingFunction EASE_OUT_EXPO = z;
    public static final EasingFunction EASE_IN_OUT_EXPO = A;
    public static final EasingFunction EASE_IN_BACK = B;
    public static final EasingFunction EASE_OUT_BACK = C;
    public static final EasingFunction EASE_IN_OUT_BACK = D;
    public static final EasingFunction EASE_IN_ELASTIC = E;
    public static final EasingFunction EASE_OUT_ELASTIC = F;
    public static final EasingFunction EASE_IN_OUT_ELASTIC = G;
    public static final EasingFunction EASE_OUT_BOUNCE = H;
    public static final EasingFunction EASE_IN_BOUNCE = I;
    public static final EasingFunction EASE_IN_OUT_BOUNCE = J;

    private Easings() {
    }

    public static EasingFunction b(double d) {
        return d2 -> {
            return 1.0 - Math.pow(1.0 - d2, d);
        };
    }

    public static EasingFunction b(int n) {
        return Easings.b(n);
    }

    public static EasingFunction c(double d) {
        int n = 974;
        return d2 -> d2 < 0.5 ? Math.pow(2.0, d - 1.0) * Math.pow(d2, d) : 1.0 - Math.pow(-2.0 * d2 + 2.0, d) / 2.0;
    }

    public static EasingFunction a(double d) {
        return d2 -> {
            return Math.pow(d2, d);
        };
    }

    public static EasingFunction a(int n) {
        return Easings.a(n);
    }
}
