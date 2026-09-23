package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.setting.Setting;
import rockstar.client.internal.script.HudContextMenu;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.ScissorStack;

public abstract class SettingComponent<T extends Setting>
extends LegacyUiElement {
    private final LegacyUiElement internalField0241;
    protected final T internalField0644;
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1325);
    protected final AnimatedValue internalField0808 = new AnimatedValue(300L, Easing.internalField1626);
    private float internalField1456;
    private boolean internalField0277 = true;
    private long internalField0229 = System.currentTimeMillis();
    private long internalField0230;
    protected float internalField1049;
    protected float internalField1046;

    public SettingComponent(T t, LegacyUiElement typedValue001) {
        this.internalField0241 = typedValue001;
        this.internalField0644 = t;
    }

    @Override
    public void internalMethod08744(UiRenderContext iII) {
        String string = LanguageManager.internalMethod00095(this.internalField0644.getDescriptionKey());
        if (this.internalField0241 instanceof HudContextMenu && this.internalMethod03399(iII)) {
            RockstarClient.getInstance().internalMethod01271().internalMethod00688(LanguageManager.internalMethod07214(string));
        }
        super.internalMethod08744(iII);
    }

    @Override
    public void internalMethod02325() {
        super.internalMethod02325();
    }

    public float internalMethod08498() {
        return this.internalField0809.internalMethod02881();
    }

    public void internalMethod08256(UiRenderContext iII) {
    }

    public void internalMethod07807(UiRenderContext iII) {
    }

    protected void internalMethod00985(UiRenderContext iII, SizedFont typedValue020, String string, float f, float f2, float f3, ColorRGBA colorRGBA, float f4, float f5) {
        float f6;
        boolean bl;
        float f7 = Math.max(1.0f, f3);
        float f8 = typedValue020.internalMethod00965(string);
        long l = System.currentTimeMillis();
        boolean bl2 = this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261());
        float f9 = Math.max(0.0f, f8 - f7);
        float f10 = (float)(l - this.internalField0229) / 1000.0f;
        this.internalField0229 = l;
        boolean bl3 = bl = bl2 && f9 > 0.0f;
        if (f9 <= 0.0f) {
            this.internalField1456 = 0.0f;
        } else if (bl) {
            this.internalField1456 = Math.min(this.internalField1456, f9);
            if (l >= this.internalField0230) {
                f6 = f10 * 35.0f;
                if (this.internalField0277) {
                    this.internalField1456 = Math.min(this.internalField1456 + f6, f9);
                    if (this.internalField1456 >= f9) {
                        this.internalField0277 = false;
                        this.internalField0230 = l + 600L;
                    }
                } else {
                    this.internalField1456 = Math.max(this.internalField1456 - f6, 0.0f);
                    if (this.internalField1456 <= 0.0f) {
                        this.internalField0277 = true;
                        this.internalField0230 = l + 600L;
                    }
                }
            }
        } else if (this.internalField1456 > 0.0f) {
            this.internalField1456 = Math.max(0.0f, this.internalField1456 - f10 * 35.0f);
            if (this.internalField1456 == 0.0f) {
                this.internalField0277 = true;
                this.internalField0230 = l;
            }
        }
        f6 = Math.max(this.internalField1047, typedValue020.internalMethod04890() + 4.0f);
        ScissorStack.internalMethod06303(iII.getMatrices(), f - 3.0f, this.internalField0206 - 3.0f, f7 + 6.0f, f6 + 6.0f);
        iII.pushMatrix();
        iII.getMatrices().translate(-this.internalField1456, 0.0f);
        iII.drawFadeoutText(typedValue020, string, f, f2, colorRGBA, 0.95f, f5, f7);
        iII.popMatrix();
        ScissorStack.internalMethod07643();
    }

    @Generated
    public LegacyUiElement internalMethod05803() {
        return this.internalField0241;
    }

    @Generated
    public T internalMethod05697() {
        return this.internalField0644;
    }

    @Generated
    public AnimatedValue internalMethod00182() {
        return this.internalField0809;
    }

    @Generated
    public AnimatedValue internalMethod00903() {
        return this.internalField0808;
    }

    @Generated
    public float internalMethod08502() {
        return this.internalField1456;
    }

    @Generated
    public boolean internalMethod02326() {
        return this.internalField0277;
    }

    @Generated
    public long internalMethod02324() {
        return this.internalField0229;
    }

    @Generated
    public long internalMethod02329() {
        return this.internalField0230;
    }

    @Generated
    public float internalMethod09904() {
        return this.internalField1049;
    }

    @Generated
    public float internalMethod09905() {
        return this.internalField1046;
    }

    @Generated
    public void internalMethod07890(float f) {
        this.internalField1049 = f;
    }

    @Generated
    public void internalMethod07901(float f) {
        this.internalField1046 = f;
    }
}

