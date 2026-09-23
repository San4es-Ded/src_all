package rockstar.client.render;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.text.Text;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.render.FontFamily;

public class SizedFont {
    private FontFamily internalField0450;
    private float internalField0205;

    public float internalMethod04890() {
        return this.internalField0450.internalMethod04986(this.internalField0205);
    }

    public float internalMethod04892() {
        return this.internalField0450.internalMethod08067(this.internalField0205);
    }

    public float internalMethod07835() {
        return this.internalField0450.internalMethod01616() * this.internalField0205;
    }

    public float internalMethod07836() {
        return -this.internalField0450.internalMethod08619() * this.internalField0205;
    }

    public float internalMethod00965(String string) {
        return this.internalField0450.internalMethod05670(PostProcessRenderer.internalMethod03546(string), this.internalField0205);
    }

    public float internalMethod06412(Text text) {
        return this.internalField0450.internalMethod04675(text, this.internalField0205);
    }

    public float internalMethod00667(char c) {
        return this.internalField0450.internalMethod01957(c, this.internalField0205);
    }

    @Generated
    public FontFamily internalMethod01335() {
        return this.internalField0450;
    }

    @Generated
    public float internalMethod07850() {
        return this.internalField0205;
    }

    @Generated
    public SizedFont(FontFamily typedValue022, float f) {
        this.internalField0450 = typedValue022;
        this.internalField0205 = f;
    }
}

