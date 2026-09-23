package rockstar.client.internal.rotation;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import rockstar.client.setting.ModeSetting;
import rockstar.client.util.ClientMessages;
import rockstar.client.server.ServerUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.internal.rotation.AuraRotationMode;
import rockstar.client.internal.script.AuraRotationPredictor;
import rockstar.client.internal.script.AuraDatasetRecorder;
import rockstar.client.internal.config.NeuroModelConfig;

public class NeuralAuraRotation
extends AuraRotationMode {
    private final AuraRotationPredictor internalField0325 = new AuraRotationPredictor();
    private final AuraRotationPredictor internalField0324 = new AuraRotationPredictor();
    private final float[] internalField0615 = new float[2];
    private final AuraDatasetRecorder internalField0326 = new AuraDatasetRecorder();
    private int internalField1055 = -1;
    private int internalField1056;
    private boolean internalField0277;
    private boolean internalField0276;

    public NeuralAuraRotation(ModeSetting typedValue170) {
        super(typedValue170, "\u041d\u0435\u0439\u0440\u043e");
    }

    @Override
    public void rotate(RotationManager typedValue269, float f, boolean bl, boolean bl2, RotationBehavior typedValue264, LivingEntity livingEntity) {
        if (NeuralAuraRotation.internalField0149.player == null || livingEntity == null) {
            return;
        }
        NeuroModelConfig typedValue121 = NeuroModelConfig.internalMethod03469();
        if (typedValue121 == null) {
            this.internalMethod02802();
            typedValue269.internalMethod00418(RotationUtils.internalMethod05580(livingEntity.getBoundingBox().getCenter()), typedValue264, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1010);
            return;
        }
        Box box = livingEntity.getBoundingBox();
        Vec3d vec3d = NeuralAuraRotation.internalField0149.player.getEyePos();
        Vec3d vec3d2 = box.getCenter().subtract(vec3d);
        double d = Math.max(Math.hypot(vec3d2.x, vec3d2.z), 0.05);
        float f2 = (float)Math.toDegrees(Math.atan2(vec3d2.z, vec3d2.x)) - 90.0f;
        float f3 = (float)(-Math.toDegrees(Math.atan2(vec3d2.y, d)));
        float f4 = Math.max((float)Math.toDegrees(Math.atan2(box.getLengthX() / 2.0, d)), 0.5f);
        float f5 = Math.max((float)Math.toDegrees(Math.atan2(box.getLengthY() / 2.0, d)), 0.5f);
        double d2 = NeuralAuraRotation.internalMethod02615(vec3d, box);
        Rotation typedValue266 = typedValue269.internalMethod00024();
        if (livingEntity.getId() != this.internalField1055 || !this.internalField0325.internalMethod02348()) {
            this.internalField0325.internalMethod06942(typedValue121, typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), f2, f3);
            this.internalField1055 = livingEntity.getId();
        }
        this.internalField1056 = 0;
        float f6 = 0.45f * typedValue121.internalMethod04221(ThreadLocalRandom.current().nextFloat());
        if (!this.internalField0325.internalMethod02367(typedValue121, typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), f2, f3, f4, f5, d2, f6, 12, 0.7f, 4, this.internalMethod02813(), this.internalField0615)) {
            return;
        }
        typedValue269.internalMethod00418(new Rotation(typedValue266.internalMethod00169() + this.internalField0615[0], MathHelper.clamp((float)(typedValue266.internalMethod00171() + this.internalField0615[1]), (float)-90.0f, (float)90.0f)), typedValue264, 180.0f, 180.0f, 180.0f, RotationPriority.internalField1010);
        if (this.internalField0277) {
            this.internalField0277 = false;
            this.internalField0325.internalMethod02351();
        } else {
            this.internalField0325.internalMethod08616();
        }
    }

    public Rotation internalMethod05104(Rotation typedValue266, Rotation typedValue267) {
        NeuroModelConfig typedValue121 = NeuroModelConfig.internalMethod03469();
        if (typedValue121 == null || NeuralAuraRotation.internalField0149.player == null) {
            return null;
        }
        float f = Math.max(3.0f, RotationUtils.internalMethod03158());
        if (Math.abs(MathHelper.wrapDegrees((float)(typedValue267.internalMethod00169() - typedValue266.internalMethod00169()))) <= f && Math.abs(typedValue267.internalMethod00171() - typedValue266.internalMethod00171()) <= f) {
            this.internalField0324.internalMethod02347();
            this.internalField1056 = 0;
            return null;
        }
        if (++this.internalField1056 > 30) {
            this.internalField0324.internalMethod02347();
            return null;
        }
        if (!this.internalField0324.internalMethod02348()) {
            this.internalField0324.internalMethod06942(typedValue121, typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), typedValue267.internalMethod00169(), typedValue267.internalMethod00171());
        }
        if (!this.internalField0324.internalMethod02367(typedValue121, typedValue266.internalMethod00169(), typedValue266.internalMethod00171(), typedValue267.internalMethod00169(), typedValue267.internalMethod00171(), 5.0f, 15.0f, 3.0, 0.0f, 12, 0.7f, 4, 1.1f, this.internalField0615)) {
            return null;
        }
        this.internalField0324.internalMethod08616();
        return new Rotation(typedValue266.internalMethod00169() + this.internalField0615[0], MathHelper.clamp((float)(typedValue266.internalMethod00171() + this.internalField0615[1]), (float)-90.0f, (float)90.0f));
    }

    private float internalMethod02813() {
        return NeuralAuraRotation.internalField0149.player != null && NeuralAuraRotation.internalField0149.player.isSubmergedInWater() && ServerUtils.internalMethod08700() ? 0.8f : 1.1f;
    }

    @Override
    public void attack() {
        this.internalField0277 = true;
    }

    @Override
    public void enabled() {
        NeuroModelConfig.internalMethod00495();
        this.internalField0325.internalMethod02347();
        this.internalField0324.internalMethod02347();
        this.internalField1055 = -1;
        this.internalField0276 = false;
    }

    @Override
    public void targetNull() {
        this.internalField0325.internalMethod02347();
        this.internalField1055 = -1;
        this.internalField0277 = false;
    }

    public boolean internalMethod02803() {
        return NeuroModelConfig.internalMethod03469() != null;
    }

    private void internalMethod02802() {
        if (this.internalField0276) {
            return;
        }
        this.internalField0276 = true;
        ClientMessages.internalMethod09025((Text)Text.literal((String)("\u041c\u043e\u0434\u0435\u043b\u044c " + NeuroModelConfig.internalMethod07182() + " \u043d\u0435 \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u043b\u0430\u0441\u044c \u2014 \u043e\u0431\u0443\u0447\u0438 \u0447\u0435\u0440\u0435\u0437 .neuro train \u0438\u043b\u0438 \u0432\u044b\u0431\u0435\u0440\u0438 \u0434\u0440\u0443\u0433\u0443\u044e: .neuro list")));
    }

    public static float internalMethod02801() {
        return 0.7f;
    }

    public static double internalMethod02615(Vec3d vec3d, Box box) {
        double d = Math.max(Math.max(box.minX - vec3d.x, 0.0), vec3d.x - box.maxX);
        double d2 = Math.max(Math.max(box.minY - vec3d.y, 0.0), vec3d.y - box.maxY);
        double d3 = Math.max(Math.max(box.minZ - vec3d.z, 0.0), vec3d.z - box.maxZ);
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }

    @Generated
    public AuraDatasetRecorder internalMethod03449() {
        return this.internalField0326;
    }
}

