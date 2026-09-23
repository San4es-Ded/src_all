package rockstar.client.ui;


import rockstar.client.*;
import rockstar.client.ui.UiNode;

public interface UiTransition {
    public static final UiTransition internalField0918 = (f, typedValue004, nestedValue2009) -> {};
    public static final UiTransition internalField0919 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = 0.0f;
    };
    public static final UiTransition internalField1389 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
    };
    public static final UiTransition internalField1386 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField1048 = (1.0f - f) * 14.0f;
    };
    public static final UiTransition internalField1388 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField1048 = (1.0f - f) * 16.0f;
    };
    public static final UiTransition internalField1387 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField1048 = -(1.0f - f) * 16.0f;
    };
    public static final UiTransition internalField1658 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField0206 = (1.0f - f) * 16.0f;
    };
    public static final UiTransition internalField1660 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField0206 = -(1.0f - f) * 16.0f;
    };
    public static final UiTransition internalField1661 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField1047 = 0.92f + 0.08f * f;
    };
    public static final UiTransition internalField1659 = (f, typedValue004, nestedValue2009) -> {
        nestedValue2009.internalField0205 = f;
        nestedValue2009.internalField1047 = 0.85f + 0.15f * f;
        nestedValue2009.internalField1048 = (1.0f - f) * 8.0f;
    };

    public void apply(float localValue1, UiNode localValue2, InternalType0040 localValue3);

    public static UiTransition internalMethod02229(float f) {
        return (f2, typedValue004, nestedValue2009) -> {
            nestedValue2009.internalField0205 = f2;
            nestedValue2009.internalField1048 = (1.0f - f2) * f;
        };
    }

    public static UiTransition internalMethod04083(float f) {
        return (f2, typedValue004, nestedValue2009) -> {
            nestedValue2009.internalField0205 = f2;
            nestedValue2009.internalField0206 = (1.0f - f2) * f;
        };
    }

    public static UiTransition internalMethod07845(float f) {
        return (f2, typedValue004, nestedValue2009) -> {
            nestedValue2009.internalField0205 = f2;
            nestedValue2009.internalField1048 = (1.0f - f2) * typedValue004.h() * f;
        };
    }

    public static final class InternalType0040 {
        public float internalField0205 = 1.0f;
        public float internalField0206 = 0.0f;
        public float internalField1048 = 0.0f;
        public float internalField1047 = 1.0f;

        public void internalMethod02215() {
            this.internalField0205 = 1.0f;
            this.internalField0206 = 0.0f;
            this.internalField1048 = 0.0f;
            this.internalField1047 = 1.0f;
        }
    }
}

