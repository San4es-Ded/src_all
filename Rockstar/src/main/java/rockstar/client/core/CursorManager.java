package rockstar.client.core;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.core.CursorType;

public final class CursorManager {
    private static CursorType internalField0566 = CursorType.internalField0566;
    private static CursorType internalField0567 = CursorType.internalField0567;

    public static void internalMethod06882(CursorType internalValue0001) {
        internalField0566 = internalValue0001;
    }

    @Generated
    private CursorManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static CursorType internalMethod02850() {
        return internalField0566;
    }

    @Generated
    public static CursorType internalMethod03537() {
        return internalField0567;
    }

    @Generated
    public static void internalMethod03769(CursorType internalValue0001) {
        internalField0567 = internalValue0001;
    }
}

