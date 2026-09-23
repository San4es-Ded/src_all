package rockstar.client.internal.script;



import rockstar.client.util.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.Rect;
import rockstar.client.util.Stopwatch;

public class InertialRect
extends Rect {
    private Rect internalField0136 = Rect.EMPTY;
    private Rect internalField0135 = Rect.EMPTY;
    private final Stopwatch internalField0519 = new Stopwatch();
    private float internalField0205 = 0.0f;
    private float internalField0206 = 0.0f;
    private float internalField1048 = 0.0f;
    private float internalField1047 = 0.0f;
    private float internalField1049 = 0.0f;
    private float internalField1046 = 0.0f;
    private float internalField1456 = 0.0f;
    private float internalField1459 = 1.0f;
    private float internalField1460 = 1.0f;
    private float internalField1461 = 1.0f;
    private float internalField1462 = 1.0f;
    private boolean internalField0277 = false;
    private boolean internalField0276 = false;

    public void internalMethod04595(float f, float f2, float f3, float f4) {
        this.internalMethod05929(new Rect(f, f2, f3, f4), false);
    }

    public void internalMethod00826(float f, float f2, float f3, float f4, boolean bl) {
        this.internalMethod05929(new Rect(f, f2, f3, f4), bl);
    }

    public void internalMethod04500(Rect rect) {
        this.internalMethod05929(rect, false);
    }

    public void internalMethod05929(Rect rect, boolean bl) {
        float f;
        float f2;
        float f3;
        float f4;
        boolean bl2;
        float f5 = 0.3f;
        float f6 = 0.1f;
        float f7 = 15.0f;
        float f8 = 0.3f;
        float f9 = Math.min((float)this.internalField0519.internalMethod00700() / 1000.0f, 0.033f);
        this.internalField0519.internalMethod00701();
        if (f9 < 0.001f) {
            return;
        }
        this.internalField0136 = rect;
        this.internalField1048 = this.internalField0205;
        this.internalField1047 = this.internalField0206;
        float f10 = rect.getX() - this.internalField0135.getX();
        float f11 = rect.getY() - this.internalField0135.getY();
        float f12 = (float)Math.sqrt(f10 * f10 + f11 * f11);
        this.internalField1049 = bl ? (this.internalField1049 += f12) : 0.0f;
        this.internalField0205 = f10 / f9;
        this.internalField0206 = f11 / f9;
        this.internalField0205 = (float)((double)this.internalField0205 * Math.pow(f6, f9 * 60.0f));
        this.internalField0206 = (float)((double)this.internalField0206 * Math.pow(f6, f9 * 60.0f));
        float f13 = Math.abs(this.internalField0205);
        float f14 = Math.abs(this.internalField0206);
        float f15 = Math.abs(this.internalField1048);
        float f16 = Math.abs(this.internalField1047);
        boolean bl3 = bl2 = !bl || this.internalField1049 > 5.0f;
        if (!bl && bl2) {
            f4 = Math.abs(this.internalField0205 - this.internalField1048);
            f3 = Math.abs(this.internalField0206 - this.internalField1047);
            if (f15 > 100.0f && f4 > 80.0f) {
                f2 = Math.signum(this.internalField1048);
                this.internalField1046 = f2 * f15 * 0.8f * 0.01f;
            }
            if (f16 > 100.0f && f3 > 80.0f) {
                f2 = Math.signum(this.internalField1047);
                this.internalField1456 = f2 * f16 * 0.8f * 0.01f;
            }
        }
        this.internalField1046 += (0.0f - this.internalField1046) * f9 * 8.0f;
        this.internalField1456 += (0.0f - this.internalField1456) * f9 * 8.0f;
        if (Math.abs(this.internalField1046) < 0.1f) {
            this.internalField1046 = 0.0f;
        }
        if (Math.abs(this.internalField1456) < 0.1f) {
            this.internalField1456 = 0.0f;
        }
        if (f15 > 50.0f && f13 < 20.0f && bl2) {
            this.internalField0277 = true;
        }
        if (f16 > 50.0f && f14 < 20.0f && bl2) {
            this.internalField0276 = true;
        }
        if ((f13 > 5.0f || f14 > 5.0f) && bl2) {
            this.internalField0277 = false;
            this.internalField0276 = false;
            if (f13 > f14) {
                this.internalField1461 = 1.0f + f13 * f5 * 8.0E-4f;
                this.internalField1462 = Math.max(0.6f, 1.0f - f13 * f5 * 0.002f);
            } else {
                this.internalField1461 = Math.max(0.6f, 1.0f - f14 * f5 * 0.002f);
                this.internalField1462 = 1.0f + f14 * f5 * 8.0E-4f;
            }
        } else if ((this.internalField0277 || this.internalField0276) && bl2) {
            if (this.internalField0277) {
                this.internalField1461 = 1.0f - f8 * 0.3f;
                this.internalField1462 = 1.0f + f8 * 0.5f;
                if (Math.abs(this.internalField1459 - this.internalField1461) < 0.05f && Math.abs(this.internalField1460 - this.internalField1462) < 0.05f) {
                    this.internalField0277 = false;
                }
            }
            if (this.internalField0276) {
                this.internalField1461 = 1.0f + f8 * 0.5f;
                this.internalField1462 = 1.0f - f8 * 0.3f;
                if (Math.abs(this.internalField1459 - this.internalField1461) < 0.05f && Math.abs(this.internalField1460 - this.internalField1462) < 0.05f) {
                    this.internalField0276 = false;
                }
            }
        } else {
            this.internalField1461 = 1.0f;
            this.internalField1462 = 1.0f;
        }
        this.internalField1459 += (this.internalField1461 - this.internalField1459) * f9 * f7;
        this.internalField1460 += (this.internalField1462 - this.internalField1460) * f9 * f7;
        f4 = rect.getWidth() * this.internalField1459;
        f3 = rect.getHeight() * this.internalField1460;
        if (f13 > f14 && f13 > 5.0f && bl2) {
            f2 = this.internalField0205 > 0.0f ? rect.getWidth() - f4 : 0.0f;
            f = (rect.getHeight() - f3) * 0.5f;
        } else if (f14 > 5.0f && bl2) {
            f2 = (rect.getWidth() - f4) * 0.5f;
            f = this.internalField0206 > 0.0f ? rect.getHeight() - f3 : 0.0f;
        } else {
            f2 = (rect.getWidth() - f4) * 0.5f;
            f = (rect.getHeight() - f3) * 0.5f;
        }
        this.set(rect.getX() + f2 + this.internalField1046, rect.getY() + f + this.internalField1456, f4, f3);
        this.internalField0135 = new Rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void internalMethod06406() {
        this.internalField1459 = 1.0f;
        this.internalField1460 = 1.0f;
        this.internalField1461 = 1.0f;
        this.internalField1462 = 1.0f;
        this.internalField0205 = 0.0f;
        this.internalField0206 = 0.0f;
        this.internalField1048 = 0.0f;
        this.internalField1047 = 0.0f;
        this.internalField0277 = false;
        this.internalField0276 = false;
        this.internalField1049 = 0.0f;
        this.internalField1046 = 0.0f;
        this.internalField1456 = 0.0f;
        this.internalField0519.internalMethod00701();
    }

    @Generated
    public Rect internalMethod02804() {
        return this.internalField0136;
    }
}

