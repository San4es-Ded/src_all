package rockstar.client.internal.script;



import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.NonNull;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;

public class AnimatedColor {
    private static final Easing internalField0812 = Easing.internalField1814;
    private final long internalField0229;
    private final AnimatedValue internalField0808;
    private final AnimatedValue internalField0809;
    private final AnimatedValue internalField1321;
    private final AnimatedValue internalField1322;

    public AnimatedColor(long l, Easing typedValue214) {
        this.internalField0229 = l;
        this.internalField0808 = new AnimatedValue(l, typedValue214);
        this.internalField0809 = new AnimatedValue(l, typedValue214);
        this.internalField1321 = new AnimatedValue(l, typedValue214);
        this.internalField1322 = new AnimatedValue(l, typedValue214);
    }

    public AnimatedColor(long l) {
        this(l, internalField0812);
    }

    public AnimatedColor(long l, ColorRGBA colorRGBA, Easing typedValue214) {
        this.internalField0229 = l;
        this.internalField0808 = new AnimatedValue(l, colorRGBA.getRed(), typedValue214);
        this.internalField0809 = new AnimatedValue(l, colorRGBA.getGreen(), typedValue214);
        this.internalField1321 = new AnimatedValue(l, colorRGBA.getBlue(), typedValue214);
        this.internalField1322 = new AnimatedValue(l, colorRGBA.getAlpha(), typedValue214);
    }

    public AnimatedColor(long l, ColorRGBA colorRGBA) {
        this(l, colorRGBA, internalField0812);
    }

    public void internalMethod03893(@NonNull ColorRGBA colorRGBA) {
        if (colorRGBA == null) {
            throw new NullPointerException("targetColor is marked non-null but is null");
        }
        this.internalField0808.internalMethod07059(colorRGBA.getRed());
        this.internalField0809.internalMethod07059(colorRGBA.getGreen());
        this.internalField1321.internalMethod07059(colorRGBA.getBlue());
        this.internalField1322.internalMethod07059(colorRGBA.getAlpha());
    }

    public ColorRGBA internalMethod04159() {
        return new ColorRGBA((int)this.internalField0808.internalMethod02881(), (int)this.internalField0809.internalMethod02881(), (int)this.internalField1321.internalMethod02881(), (int)this.internalField1322.internalMethod02881());
    }

    public void internalMethod00236(Easing typedValue214) {
        this.internalField0808.internalMethod06645(typedValue214);
        this.internalField0809.internalMethod06645(typedValue214);
        this.internalField1321.internalMethod06645(typedValue214);
        this.internalField1322.internalMethod06645(typedValue214);
    }

    public void internalMethod03370(long l) {
        this.internalField0808.internalMethod07061(l);
        this.internalField0809.internalMethod07061(l);
        this.internalField1321.internalMethod07061(l);
        this.internalField1322.internalMethod07061(l);
    }

    public void internalMethod03487(@NonNull ColorRGBA colorRGBA) {
        if (colorRGBA == null) {
            throw new NullPointerException("color is marked non-null but is null");
        }
        this.internalField0808.internalMethod07060(colorRGBA.getRed());
        this.internalField0809.internalMethod07060(colorRGBA.getGreen());
        this.internalField1321.internalMethod07060(colorRGBA.getBlue());
        this.internalField1322.internalMethod07060(colorRGBA.getAlpha());
    }
}

