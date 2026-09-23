package rockstar.client.internal.rotation;




import rockstar.client.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.internal.core.NewtonGlobalState;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.core.AuraAimService;

public final class SilentRotationHelper {
    private SilentRotationHelper() {
    }

    public static void internalMethod05978(Rotation typedValue266) {
        if (NewtonGlobalState.internalField1100) {
            RotationManager typedValue269 = NewtonCoreManager.internalMethod00114().internalMethod03241();
            Rotation typedValue267 = typedValue269.internalMethod01525() ? typedValue269.internalMethod07496() : typedValue269.internalMethod09074();
            float[] fArray = new float[2];
            if (AuraAimService.internalMethod00406(typedValue267.internalMethod00169(), typedValue267.internalMethod00171(), typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), fArray)) {
                Rotation typedValue268 = new Rotation(typedValue267.internalMethod00169() + fArray[0], Math.max(-90.0f, Math.min(90.0f, typedValue267.internalMethod00171() + fArray[1])));
                typedValue269.internalMethod00418(typedValue268, RotationBehavior.internalField0114, 180.0f, 180.0f, 30.0f, RotationPriority.internalField0122);
                return;
            }
        }
        SilentRotationHelper.internalMethod02485(typedValue266, 30.0f, 30.0f, 30.0f);
    }

    public static void internalMethod02485(Rotation typedValue266, float f, float f2, float f3) {
        NewtonCoreManager.internalMethod00114().internalMethod03241().internalMethod00418(typedValue266, RotationBehavior.internalField0114, f, f2, f3, RotationPriority.internalField0122);
    }

    public static void internalMethod06440() {
        NewtonCoreManager.internalMethod00114().internalMethod03241().internalMethod01526();
    }
}

