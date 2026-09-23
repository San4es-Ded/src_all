package pyrock.classes;


import rockstar.client.rotation.*;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import rockstar.client.RockstarClient;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.rotation.RotationPriority;
import rockstar.modules.combat.AuraModule;

public class PyRotations {
    private RotationManager handler() {
        return RockstarClient.getInstance().internalMethod02368();
    }

    private AuraModule aura() {
        return RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
    }

    private Rotation defaultRotation(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            return RotationUtils.internalMethod04766(livingEntity, this.aura());
        }
        if (entity == null) {
            return this.handler().internalMethod00024();
        }
        Vec3d vec3d = RotationUtils.internalMethod01412(entity);
        return RotationUtils.internalMethod05580(vec3d);
    }

    public List<Double> defaultAngles(Entity entity) {
        return this.angles(this.defaultRotation(entity));
    }

    public List<Double> current() {
        Rotation typedValue266 = this.handler().internalMethod00024();
        return this.angles(typedValue266);
    }

    public List<Double> player() {
        Rotation typedValue266 = this.handler().internalMethod07496();
        return this.angles(typedValue266);
    }

    public boolean idling() {
        return this.handler().internalMethod01525();
    }

    public List<Double> toPoint(double d, double d2, double d3) {
        Rotation typedValue266 = RotationUtils.internalMethod05580(new Vec3d(d, d2, d3));
        return this.angles(typedValue266);
    }

    public List<Double> to(Entity entity) {
        if (entity == null) {
            return this.current();
        }
        Vec3d vec3d = RotationUtils.internalMethod01412(entity);
        Rotation typedValue266 = RotationUtils.internalMethod05580(vec3d);
        return this.angles(typedValue266);
    }

    public List<Double> gcd(double d, double d2, double d3, double d4) {
        Rotation typedValue266 = RotationUtils.internalMethod02284(new Rotation(d, d2), new Rotation(d3, d4));
        return this.angles(typedValue266);
    }

    public List<Double> gcdStep(double d, double d2, double d3, double d4, double d5) {
        return this.gcdSteps(d, d2, d3, d4, d5, d5);
    }

    public List<Double> gcdSteps(double d, double d2, double d3, double d4, double d5, double d6) {
        Rotation typedValue266 = new Rotation(this.snapDelta((float)d, (float)d3, (float)d5, true), MathHelper.clamp((float)this.snapDelta((float)d2, (float)d4, (float)d6, false), (float)-90.0f, (float)90.0f));
        return this.angles(typedValue266);
    }

    public float vanillaStep() {
        return RotationUtils.internalMethod03158();
    }

    public void apply(double d, double d2) {
        this.apply(d, d2, "silent", "normal", 180.0, 180.0);
    }

    public void apply(double d, double d2, String string, String string2) {
        this.apply(d, d2, string, string2, 180.0, 180.0);
    }

    public void apply(double d, double d2, String string, String string2, double d3, double d4) {
        this.apply(d, d2, string, string2, d3, d4, 180.0, true);
    }

    public void apply(double d, double d2, String string, String string2, double d3, double d4, double d5, boolean bl) {
        this.handler().internalMethod01596(new Rotation(d, d2), PyRotations.parseCorrection(string), (float)d3, (float)d4, (float)d5, PyRotations.parsePriority(string2), bl);
    }

    public static RotationBehavior parseCorrection(String string) {
        if (string == null) {
            return RotationBehavior.internalField1003;
        }
        return switch (string.toLowerCase()) {
            case "none", "off" -> RotationBehavior.internalField0115;
            case "direct" -> RotationBehavior.internalField0114;
            case "strict" -> RotationBehavior.internalField1004;
            case "smooth", "smooth_silent" -> RotationBehavior.internalField1002;
            case "change_look", "changelook", "change-look" -> RotationBehavior.internalField1001;
            case "targeted" -> RotationBehavior.internalField1423;
            default -> RotationBehavior.internalField1003;
        };
    }

    public static RotationPriority parsePriority(String string) {
        if (string == null) {
            return RotationPriority.internalField0122;
        }
        return switch (string.toLowerCase()) {
            case "target", "to_target" -> RotationPriority.internalField1010;
            case "override" -> RotationPriority.internalField1011;
            case "use_item" -> RotationPriority.internalField1012;
            case "max" -> RotationPriority.internalField1009;
            case "low", "not_important" -> RotationPriority.internalField0121;
            default -> RotationPriority.internalField0122;
        };
    }

    private List<Double> angles(Rotation typedValue266) {
        return List.of(Double.valueOf(typedValue266.internalMethod00169()), Double.valueOf(typedValue266.internalMethod00171()));
    }

    private float snapDelta(float f, float f2, float f3, boolean bl) {
        float f4;
        float f5 = f4 = bl ? MathHelper.wrapDegrees((float)(f2 - f)) : f2 - f;
        if (f3 <= 0.0f || Float.isNaN(f3) || Float.isInfinite(f3)) {
            return f + f4;
        }
        return f + (float)Math.round(f4 / f3) * f3;
    }
}
