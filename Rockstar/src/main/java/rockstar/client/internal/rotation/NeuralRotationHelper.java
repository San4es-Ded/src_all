package rockstar.client.internal.rotation;





import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import rockstar.client.internal.rotation.NeuralAuraRotation;
import rockstar.client.internal.script.AuraRotationPredictor;
import rockstar.client.internal.config.NeuroModelConfig;

public final class NeuralRotationHelper {
    private final AuraRotationPredictor internalField0325 = new AuraRotationPredictor();
    private final float[] internalField0615 = new float[2];
    private int internalField1053 = -1;
    private boolean internalField0277;

    public boolean internalMethod00618() {
        return NeuroModelConfig.internalMethod03469() != null;
    }

    public void internalMethod00617() {
        this.internalField0277 = true;
    }

    public void internalMethod00621() {
        this.internalField0325.internalMethod02347();
        this.internalField1053 = -1;
        this.internalField0277 = false;
    }

    public boolean internalMethod04053(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, LivingEntity livingEntity, float f, float[] fArray) {
        NeuroModelConfig typedValue121 = NeuroModelConfig.internalMethod03469();
        if (typedValue121 == null || clientPlayerEntity == null || clientWorld == null || livingEntity == null) {
            return false;
        }
        Box box = livingEntity.getBoundingBox();
        Vec3d vec3d = clientPlayerEntity.getEyePos();
        Vec3d vec3d2 = box.getCenter().subtract(vec3d);
        double d = Math.max(Math.hypot(vec3d2.x, vec3d2.z), 0.05);
        float f2 = (float)Math.toDegrees(Math.atan2(vec3d2.z, vec3d2.x)) - 90.0f;
        float f3 = (float)(-Math.toDegrees(Math.atan2(vec3d2.y, d)));
        float f4 = Math.max((float)Math.toDegrees(Math.atan2(box.getLengthX() / 2.0, d)), 0.5f);
        float f5 = Math.max((float)Math.toDegrees(Math.atan2(box.getLengthY() / 2.0, d)), 0.5f);
        if (livingEntity.getId() != this.internalField1053 || !this.internalField0325.internalMethod02348()) {
            this.internalField0325.internalMethod06942(typedValue121, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch(), f2, f3);
            this.internalField1053 = livingEntity.getId();
        }
        float f6 = 0.45f * typedValue121.internalMethod04221(ThreadLocalRandom.current().nextFloat());
        if (!this.internalField0325.internalMethod02367(typedValue121, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch(), f2, f3, f4, f5, NeuralAuraRotation.internalMethod02615(vec3d, box), f6, 12, f, 4, 1.0f, this.internalField0615)) {
            return false;
        }
        if (this.internalField0277) {
            this.internalField0277 = false;
            this.internalField0325.internalMethod02351();
        } else {
            this.internalField0325.internalMethod08616();
        }
        fArray[0] = this.internalField0615[0];
        fArray[1] = this.internalField0615[1];
        return true;
    }
}

