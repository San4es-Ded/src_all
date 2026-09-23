package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.ModeSetting;
import rockstar.client.event.EventListener;
import rockstar.client.RockstarClient;
import rockstar.client.util.CombatUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.MathUtils;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.util.Stopwatch;
import rockstar.client.internal.rotation.AuraRotationMode;

public class SpookyTimeRotation
extends AuraRotationMode {
    private final Stopwatch internalField0519 = new Stopwatch();
    private final InternalType0261 internalField0717 = new InternalType0261();
    private long internalField0229;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (RockstarClient.getInstance().internalMethod04463().internalMethod01783() == null && !this.internalField0519.internalMethod02365(this.internalField0229) && this.aura().internalMethod01895().internalMethod07418() instanceof SpookyTimeRotation) {
            Rotation typedValue266 = RockstarClient.getInstance().internalMethod02368().internalMethod07496();
            typedValue266 = typedValue266.internalMethod05809(MathUtils.internalMethod07919(-8.0f, 5.0f), MathUtils.internalMethod07919(-5.0f, 8.0f));
            float f = MathUtils.internalMethod07919(65.0f, 95.0f);
            float f2 = MathUtils.internalMethod07919(30.0f, 45.0f);
            RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, f, f2, 90.0f, RotationPriority.internalField1010);
        }
    };

    public SpookyTimeRotation(ModeSetting typedValue170) {
        super(typedValue170, "SpookyTime");
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    @Override
    public void rotate(RotationManager typedValue269, float f, boolean bl, boolean bl2, RotationBehavior typedValue264, LivingEntity livingEntity) {
        if (livingEntity == null || SpookyTimeRotation.internalField0149.player == null) {
            return;
        }
        Rotation typedValue266 = this.internalField0717.internalMethod03165(livingEntity, typedValue269.internalMethod00024());
        typedValue269.internalMethod00418(typedValue266, typedValue264, 180.0f, 180.0f, 90.0f, RotationPriority.internalField1010);
        this.internalField0519.internalMethod00701();
        this.internalField0229 = (long)MathUtils.internalMethod07919(700.0f, 1000.0f);
    }

    @Override
    public void attack() {
        LivingEntity livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
        if (livingEntity != null) {
            this.internalField0717.internalMethod07146(livingEntity);
        }
    }

    @Override
    public void targetNull() {
        this.internalField0717.internalMethod05197();
    }

    @Override
    public boolean canAttack() {
        LivingEntity livingEntity = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
        if (livingEntity == null || SpookyTimeRotation.internalField0149.player == null) {
            return false;
        }
        Rotation typedValue266 = RockstarClient.getInstance().internalMethod02368().internalMethod00024();
        return this.internalField0717.internalMethod04516(livingEntity, this.aura().internalMethod05151().internalMethod08576(), 58.0f, typedValue266);
    }

    final class InternalType0261 {
        private static final String internalField0248 = "/assets/rockstar/ml/rotation/profile.json";
        private static final Gson internalField0931 = new GsonBuilder().create();
        private final InternalType0262 internalField0718 = new InternalType0262();
        private final InternalType0495 internalField0047 = new InternalType0495();
        private InternalType0494 internalField0046 = this.internalMethod05890();

        InternalType0261() {
        }

        Rotation internalMethod03165(LivingEntity livingEntity, Rotation typedValue266) {
            this.internalMethod05202();
            Vec3d vec3d = this.internalMethod00535(livingEntity);
            Vec3d vec3d2 = MinecraftClientAccess.internalField0149.player.getEyePos();
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            Vec3d vec3d4 = this.internalMethod04211(vec3d3, livingEntity);
            float f = (float)Math.toDegrees(Math.atan2(vec3d4.z, vec3d4.x)) - 90.0f;
            float f2 = (float)(-Math.toDegrees(Math.atan2(vec3d4.y, Math.hypot(vec3d4.x, vec3d4.z))));
            float f3 = MathHelper.wrapDegrees((float)(f - typedValue266.internalMethod00169()));
            float f4 = MathHelper.wrapDegrees((float)(f2 - typedValue266.internalMethod00171()));
            float f5 = this.internalMethod05201();
            float f6 = (float)(this.internalField0046.rotation.internalField0194 * (double)f5);
            float f7 = (float)(this.internalField0046.rotation.internalField0193 * (double)f5);
            float f8 = MathHelper.clamp((float)f3, (float)(-f6), (float)f6);
            float f9 = MathHelper.clamp((float)f4, (float)(-f7), (float)f7);
            float f10 = (float)this.internalField0046.rotation.internalField1045;
            f8 *= f10;
            f9 *= f10;
            float f11 = (float)(this.internalField0046.rotation.internalField1043 * (double)this.internalMethod08171());
            this.internalField0718.internalField0205 = f8 += this.internalMethod00870(-f11, f11);
            this.internalField0718.internalField0206 = f9 += this.internalMethod00870(-f11, f11);
            return new Rotation(typedValue266.internalMethod00169() + f8, MathHelper.clamp((float)(typedValue266.internalMethod00171() + f9), (float)-90.0f, (float)90.0f));
        }

        boolean internalMethod04516(LivingEntity livingEntity, float f, float f2, Rotation typedValue266) {
            this.internalMethod05202();
            long l = System.currentTimeMillis();
            if (this.internalField0718.internalField0505 != livingEntity) {
                if (l - this.internalField0718.internalField0229 < (long)this.internalField0046.attack_pattern.internalField1054) {
                    return false;
                }
                this.internalField0718.internalField0505 = livingEntity;
                this.internalField0718.internalField0229 = l;
            }
            if (MinecraftClientAccess.internalField0149.player.distanceTo((Entity)livingEntity) > f) {
                return false;
            }
            Vec3d vec3d = typedValue266.internalMethod06001().normalize();
            Vec3d vec3d2 = livingEntity.getBoundingBox().getCenter().subtract(MinecraftClientAccess.internalField0149.player.getEyePos());
            if (vec3d2.lengthSquared() > 1.0E-6 && vec3d.dotProduct(vec3d2.normalize()) < Math.cos(Math.toRadians(f2))) {
                return false;
            }
            return this.internalMethod00301(livingEntity, l);
        }

        private boolean internalMethod00301(LivingEntity livingEntity, long l) {
            long l2;
            float f = livingEntity.getHealth();
            float f2 = MinecraftClientAccess.internalField0149.player.getHealth();
            double d = MinecraftClientAccess.internalField0149.player.distanceTo((Entity)livingEntity);
            double d2 = 0.5;
            if (f < 6.0f) {
                d2 += 0.3;
            }
            if (f < 3.0f) {
                d2 += 0.4;
            }
            if (f2 < 8.0f) {
                d2 += 0.2;
            }
            if (f2 < 4.0f) {
                d2 += 0.3;
            }
            if (d < 2.0) {
                d2 += 0.2;
            }
            if (d > 4.0) {
                d2 -= 0.2;
            }
            if ((l2 = l - this.internalField0718.internalField1059) < 150L) {
                d2 -= 0.3;
            }
            if (this.internalField0718.internalField0277) {
                if (this.internalField0718.internalField0228 >= this.internalField0046.attack_pattern.internalField0227 || l > this.internalField0718.internalField0230) {
                    this.internalField0718.internalField0277 = false;
                    d2 -= 0.4;
                } else {
                    d2 += 0.3;
                }
            }
            if (ThreadLocalRandom.current().nextDouble() < this.internalField0046.human_mimic.internalField0193) {
                d2 -= 0.5;
            }
            if (this.internalField0046.human_mimic.internalField0277 && f <= 0.5f) {
                d2 -= 0.8;
            }
            return d2 > 0.6;
        }

        private float internalMethod05194() {
            float f = 1.0f;
            if (MinecraftClientAccess.internalField0149.player.getHealth() < 6.0f) {
                f *= 0.85f;
            }
            if (System.currentTimeMillis() - this.internalField0718.internalField1059 > 15000L) {
                f *= 1.15f;
            }
            return MathHelper.clamp((float)f, (float)0.7f, (float)1.4f);
        }

        private Vec3d internalMethod00535(LivingEntity livingEntity) {
            String string;
            Box box = CombatUtils.internalMethod04321((Entity)livingEntity, SpookyTimeRotation.this.aura().internalMethod06377().isSelected());
            double d = switch (string = this.internalField0046.attack_pattern.internalField0248) {
                case "head" -> 0.85;
                case "chest" -> 0.65;
                case "health" -> {
                    if (livingEntity.getHealth() < 8.0f) {
                        yield 0.85;
                    }
                    yield 0.55;
                }
                default -> 0.65;
            };
            d += ThreadLocalRandom.current().nextDouble(-0.07, 0.07);
            d = MathHelper.clamp((double)d, (double)0.05, (double)0.95);
            return new Vec3d(box.minX + (box.maxX - box.minX) * (0.3 + ThreadLocalRandom.current().nextDouble(0.4)), box.minY + (box.maxY - box.minY) * d, box.minZ + (box.maxZ - box.minZ) * (0.3 + ThreadLocalRandom.current().nextDouble(0.4)));
        }

        private Vec3d internalMethod04211(Vec3d vec3d, LivingEntity livingEntity) {
            Vec3d vec3d2 = livingEntity.getVelocity();
            float f = (float)(this.internalField0046.rotation.internalField1042 * 0.1);
            return new Vec3d(vec3d.x + vec3d2.x * (double)f, vec3d.y, vec3d.z + vec3d2.z * (double)f);
        }

        private float internalMethod05201() {
            float f = 1.0f;
            if (MinecraftClientAccess.internalField0149.player.getHealth() < 6.0f) {
                f *= 1.3f;
            }
            if (this.internalField0718.internalField0277) {
                f *= 1.2f;
            }
            return MathHelper.clamp((float)f, (float)0.6f, (float)1.5f);
        }

        private float internalMethod08171() {
            float f = 1.0f;
            if (MinecraftClientAccess.internalField0149.player.hurtTime > 0) {
                f *= 2.5f;
            }
            if (this.internalField0718.internalField0277) {
                f *= 1.3f;
            }
            return MathHelper.clamp((float)f, (float)0.5f, (float)3.0f);
        }

        void internalMethod07146(LivingEntity livingEntity) {
            long l;
            this.internalMethod05202();
            this.internalField0718.internalField1059 = l = System.currentTimeMillis();
            if (!this.internalField0718.internalField0277) {
                this.internalField0718.internalField0277 = true;
                this.internalField0718.internalField0228 = 0;
                int n = ThreadLocalRandom.current().nextInt(this.internalField0046.attack_pattern.internalField0228, this.internalField0046.attack_pattern.internalField1053);
                this.internalField0718.internalField0230 = l + (long)n;
            }
            ++this.internalField0718.internalField0228;
            this.internalField0047.internalField0416.add(l);
            this.internalField0047.internalField0416.removeIf(l2 -> l - l2 > 1000L);
            this.internalField0047.internalField0205 = this.internalField0047.internalField0416.size();
            this.internalMethod00013(livingEntity);
        }

        private void internalMethod00013(LivingEntity livingEntity) {
            float f;
            if (livingEntity instanceof PlayerEntity && (f = livingEntity.getHealth()) > 12.0f && this.internalField0718.internalField0615[this.internalField0718.internalField0227] < 5.0f) {
                this.internalField0046.attack_pattern.internalField0248 = "head";
            }
        }

        void internalMethod05197() {
            this.internalField0718.internalField0505 = null;
        }

        private void internalMethod05202() {
            if (this.internalField0046 == null || this.internalField0046.attack_pattern == null || this.internalField0046.human_mimic == null || this.internalField0046.rotation == null) {
                this.internalField0046 = this.internalMethod02723();
            }
        }

        /*
         * Enabled aggressive exception aggregation
         */
        private InternalType0494 internalMethod05890() {
            try (InputStream inputStream = SpookyTimeRotation.class.getResourceAsStream(internalField0248);){
                InternalType0494 nestedValue2062;
                if (inputStream == null) {
                    InternalType0494 nestedValue2063 = this.internalMethod02723();
                    return nestedValue2063;
                }
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));){
                    InternalType0494 nestedValue2064 = (InternalType0494)internalField0931.fromJson((Reader)bufferedReader, InternalType0494.class);
                    nestedValue2062 = nestedValue2064 == null ? this.internalMethod02723() : nestedValue2064;
                }
                return nestedValue2062;
            }
            catch (Exception exception) {
                return this.internalMethod02723();
            }
        }

        private InternalType0494 internalMethod02723() {
            InternalType0494 nestedValue2062 = new InternalType0494();
            nestedValue2062.name = "AggressivePlayer";
            nestedValue2062.version = 1.0;
            nestedValue2062.attack_pattern = new InternalType0494.InternalType0523();
            nestedValue2062.attack_pattern.internalField0227 = 5;
            nestedValue2062.attack_pattern.internalField0228 = 120;
            nestedValue2062.attack_pattern.internalField1053 = 280;
            nestedValue2062.attack_pattern.internalField0194 = 12.0;
            nestedValue2062.attack_pattern.internalField0193 = 18.5;
            nestedValue2062.attack_pattern.internalField1045 = 0.85;
            nestedValue2062.attack_pattern.internalField1055 = 32;
            nestedValue2062.attack_pattern.internalField1056 = 67;
            nestedValue2062.attack_pattern.internalField0248 = "head";
            nestedValue2062.attack_pattern.internalField1054 = 180;
            nestedValue2062.human_mimic = new InternalType0494.InternalType0524();
            nestedValue2062.human_mimic.internalField0227 = 55;
            nestedValue2062.human_mimic.internalField0228 = 130;
            nestedValue2062.human_mimic.internalField0194 = 0.21;
            nestedValue2062.human_mimic.internalField0193 = 0.07;
            nestedValue2062.human_mimic.internalField0277 = false;
            nestedValue2062.rotation = new InternalType0494.InternalType0522();
            nestedValue2062.rotation.internalField0194 = 110.0;
            nestedValue2062.rotation.internalField0193 = 58.0;
            nestedValue2062.rotation.internalField1045 = 0.68;
            nestedValue2062.rotation.internalField1043 = 0.35;
            nestedValue2062.rotation.internalField1042 = 0.52;
            return nestedValue2062;
        }

        private float internalMethod00870(float f, float f2) {
            return (float)ThreadLocalRandom.current().nextDouble(f, f2);
        }
    }

    static final class InternalType0494 {
        String name;
        double version;
        InternalType0523 attack_pattern;
        InternalType0524 human_mimic;
        InternalType0522 rotation;

        InternalType0494() {
        }

        static final class InternalType0522 {
            double internalField0194;
            double internalField0193;
            double internalField1045;
            double internalField1043;
            double internalField1042;

            InternalType0522() {
            }
        }

        static final class InternalType0524 {
            int internalField0227;
            int internalField0228;
            double internalField0194;
            double internalField0193;
            boolean internalField0277;

            InternalType0524() {
            }
        }

        static final class InternalType0523 {
            int internalField0227;
            int internalField0228;
            int internalField1053;
            double internalField0194;
            double internalField0193;
            double internalField1045;
            int internalField1055;
            int internalField1056;
            String internalField0248;
            int internalField1054;

            InternalType0523() {
            }
        }
    }

    static final class InternalType0495 {
        private long internalField0229;
        private boolean internalField0277;
        private int internalField0227;
        private long internalField0230;
        float internalField0205 = 10.0f;
        final List<Long> internalField0416 = new ArrayList<Long>();

        InternalType0495() {
        }
    }

    static final class InternalType0262 {
        float internalField0205;
        float internalField0206;
        final float[] internalField0615 = new float[20];
        int internalField0227;
        long internalField0229;
        private String internalField0248 = "health";
        boolean internalField0277;
        int internalField0228;
        long internalField0230;
        long internalField1059;
        LivingEntity internalField0505;
        InternalType0262() {
        }
    }
}
