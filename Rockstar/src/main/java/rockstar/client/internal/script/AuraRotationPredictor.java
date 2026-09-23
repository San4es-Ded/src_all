package rockstar.client.internal.script;




import rockstar.client.rotation.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.math.MathHelper;
import rockstar.client.rotation.RotationUtils;
import rockstar.client.internal.config.NeuroModelConfig;

public final class AuraRotationPredictor {
    private float[] internalField0615;
    private float internalField0206 = 20.0f;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private float internalField1456;
    private float internalField1457;
    private float internalField1458;
    private float internalField1459;
    private float internalField1460;
    private float internalField1461;
    private float internalField1462;
    private float internalField1455 = 5.0f;
    private float internalField1723 = 15.0f;
    private double internalField0194 = 3.0;
    private boolean internalField0277;

    public boolean internalMethod02348() {
        return this.internalField0615 != null;
    }

    public void internalMethod06942(NeuroModelConfig typedValue121, float f, float f2, float f3, float f4) {
        this.internalField0615 = typedValue121.internalMethod04220();
        this.internalField1047 = f3;
        this.internalField1049 = f4;
        this.internalField1048 = 0.0f;
        this.internalField1462 = 0.0f;
        this.internalField1461 = 0.0f;
        this.internalField1460 = 0.0f;
        this.internalField1459 = 0.0f;
        this.internalField1046 = this.internalField1457 = MathHelper.wrapDegrees((float)(f3 - f));
        this.internalField1456 = this.internalField1458 = f4 - f2;
    }

    public void internalMethod02347() {
        this.internalField0615 = null;
        this.internalField1048 = 0.0f;
    }

    public void internalMethod02351() {
        this.internalField0206 = 0.0f;
    }

    public void internalMethod08616() {
        this.internalField0206 = Math.min(this.internalField0206 + 1.0f, 20.0f);
    }

    public boolean internalMethod02352() {
        return this.internalField0277;
    }

    public boolean internalMethod02367(NeuroModelConfig typedValue121, float f, float f2, float f3, float f4, float f5, float f6, double d, float f7, int n, float f8, int n2, float f9, float[] fArray) {
        float f10;
        boolean bl;
        if (typedValue121 == null) {
            return false;
        }
        if (this.internalField0615 == null) {
            this.internalMethod06942(typedValue121, f, f2, f3, f4);
        }
        this.internalField1455 = f5;
        this.internalField1723 = f6;
        this.internalField0194 = d;
        float f11 = RotationUtils.internalMethod03158();
        float f12 = MathHelper.wrapDegrees((float)(f3 - this.internalField1047));
        float f13 = f4 - this.internalField1049;
        this.internalField1047 = f3;
        this.internalField1049 = f4;
        float f14 = MathHelper.wrapDegrees((float)(f3 - f));
        float f15 = f4 - f2;
        float[] fArray2 = typedValue121.internalMethod00149(this.internalMethod04826(typedValue121, f12, f13), this.internalField0615);
        float f16 = 0.0f;
        float f17 = 0.0f;
        boolean bl2 = bl = this.internalField1048 >= (float)Math.min(n, typedValue121.internalMethod00494());
        if (bl || ThreadLocalRandom.current().nextFloat() >= NeuroModelConfig.internalMethod04277(fArray2[0])) {
            f10 = Float.MAX_VALUE;
            for (int i = n2; i > 0; --i) {
                int n3 = 1 + 6 * typedValue121.internalMethod04897(fArray2, ThreadLocalRandom.current().nextFloat());
                float f18 = NeuroModelConfig.internalMethod08867(fArray2[n3 + 5]);
                float f19 = (float)Math.sqrt(Math.max(0.0f, 1.0f - f18 * f18));
                float f20 = AuraRotationPredictor.internalMethod02346();
                float f21 = f18 * f20 + f19 * AuraRotationPredictor.internalMethod02346();
                float f22 = AuraRotationPredictor.internalMethod01697(f9 * typedValue121.internalMethod05587(fArray2[n3 + 1], fArray2[n3 + 3], true, f20, f8), f11);
                float f23 = AuraRotationPredictor.internalMethod01697(f9 * typedValue121.internalMethod05587(fArray2[n3 + 2], fArray2[n3 + 4], false, f21, f8), f11);
                float f24 = Math.abs((float)Math.hypot(MathHelper.wrapDegrees((float)(f14 - f22)) / this.internalField1455, (f15 - f23) / this.internalField1723) - f7);
                if (!(f24 < f10)) continue;
                f10 = f24;
                f16 = f22;
                f17 = f23;
            }
        }
        if (bl && f16 == 0.0f && f17 == 0.0f) {
            if (Math.abs(f14) >= Math.abs(f15)) {
                f16 = Math.copySign(f11, f14);
            } else {
                f17 = Math.copySign(f11, f15);
            }
        }
        this.internalField1048 = f16 == 0.0f && f17 == 0.0f ? this.internalField1048 + 1.0f : 0.0f;
        f10 = MathHelper.clamp((float)(f2 + f17), (float)-90.0f, (float)90.0f);
        f17 = f10 - f2;
        this.internalField1461 = this.internalField1459;
        this.internalField1462 = this.internalField1460;
        this.internalField1459 = f16;
        this.internalField1460 = f17;
        this.internalField1457 = this.internalField1046;
        this.internalField1458 = this.internalField1456;
        this.internalField1046 = MathHelper.wrapDegrees((float)(f3 - (f + f16)));
        this.internalField1456 = f4 - f10;
        this.internalField0277 = Math.abs(this.internalField1046) <= this.internalField1455 && Math.abs(this.internalField1456) <= this.internalField1723;
        fArray[0] = f16;
        fArray[1] = f17;
        return true;
    }

    private float[] internalMethod04826(NeuroModelConfig typedValue121, float f, float f2) {
        return new float[]{AuraRotationPredictor.internalMethod04928(this.internalField1046), AuraRotationPredictor.internalMethod04928(this.internalField1456), AuraRotationPredictor.internalMethod04928(MathHelper.wrapDegrees((float)(this.internalField1046 - this.internalField1457))), AuraRotationPredictor.internalMethod04928(this.internalField1456 - this.internalField1458), AuraRotationPredictor.internalMethod04928(f), AuraRotationPredictor.internalMethod04928(f2), AuraRotationPredictor.internalMethod04928(this.internalField1459), AuraRotationPredictor.internalMethod04928(this.internalField1460), AuraRotationPredictor.internalMethod04928(this.internalField1461), AuraRotationPredictor.internalMethod04928(this.internalField1462), AuraRotationPredictor.internalMethod04928(this.internalField1046 / this.internalField1455), AuraRotationPredictor.internalMethod04928(this.internalField1456 / this.internalField1723), (float)Math.log(Math.max(this.internalField0194, 0.05) + 0.5) / 2.0f, (float)Math.log(this.internalField1455) / 3.0f, this.internalField0277 ? 1.0f : 0.0f, this.internalField0206 / 20.0f, this.internalField1048 / (float)typedValue121.internalMethod00494()};
    }

    private static float internalMethod04928(float f) {
        return (float)(Math.log((double)f + Math.sqrt((double)(f * f) + 1.0)) / 3.0);
    }

    private static float internalMethod02346() {
        return (float)ThreadLocalRandom.current().nextGaussian();
    }

    private static float internalMethod01697(float f, float f2) {
        return (float)Math.round(f / f2) * f2;
    }
}

