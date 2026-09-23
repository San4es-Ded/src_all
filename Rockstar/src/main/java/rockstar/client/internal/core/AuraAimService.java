package rockstar.client.internal.core;




import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.script.AuraRotationPredictor;
import rockstar.client.internal.config.NeuroModelConfig;

public final class AuraAimService {
    private static final AuraAimService internalField0775 = new AuraAimService();
    private final AuraRotationPredictor internalField0325 = new AuraRotationPredictor();
    private final float[] internalField0615 = new float[2];
    private int internalField1053 = Integer.MIN_VALUE;

    private AuraAimService() {
    }

    public static boolean internalMethod05078() {
        return NeuroModelConfig.internalMethod03469() != null;
    }

    @Nullable
    public static String internalMethod06837() {
        return NeuroModelConfig.internalMethod03469() == null ? null : NeuroModelConfig.internalMethod07182();
    }

    public static boolean internalMethod00406(float f, float f2, float f3, float f4, float[] fArray) {
        return internalField0775.internalMethod04806(f, f2, f3, f4, fArray);
    }

    private boolean internalMethod04806(float f, float f2, float f3, float f4, float[] fArray) {
        NeuroModelConfig typedValue121 = NeuroModelConfig.internalMethod03469();
        if (typedValue121 == null) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        if (clientPlayerEntity == null || minecraftClient.world == null) {
            return false;
        }
        if (Math.abs(MathHelper.wrapDegrees((float)(f3 - f))) > 90.0f || Math.abs(f4) > 30.0f) {
            this.internalField0325.internalMethod02347();
            return false;
        }
        if (clientPlayerEntity.age - this.internalField1053 > 3) {
            this.internalField0325.internalMethod02347();
        }
        this.internalField1053 = clientPlayerEntity.age;
        double d = 3.5 * Math.max(Math.cos(Math.toRadians(f4)), 0.05);
        float f5 = (float)Math.max(Math.toDegrees(Math.atan2(0.3f, d)), 0.5);
        float f6 = (float)Math.max(Math.toDegrees(Math.atan2(0.9f, d)), 0.5);
        if (!this.internalField0325.internalMethod02348()) {
            this.internalField0325.internalMethod06942(typedValue121, f, f2, f3, f4);
        }
        if (!this.internalField0325.internalMethod02367(typedValue121, f, f2, f3, f4, f5, f6, 3.5, 0.0f, 12, 0.3f, 4, 1.0f, this.internalField0615)) {
            return false;
        }
        this.internalField0325.internalMethod08616();
        float f7 = MathHelper.clamp((float)this.internalField0615[0], (float)-25.0f, (float)25.0f);
        float f8 = MathHelper.clamp((float)this.internalField0615[1], (float)-25.0f, (float)25.0f);
        if (Float.isNaN(f7) || Float.isNaN(f8)) {
            this.internalField0325.internalMethod02347();
            return false;
        }
        fArray[0] = f7;
        fArray[1] = f8;
        return true;
    }
}

