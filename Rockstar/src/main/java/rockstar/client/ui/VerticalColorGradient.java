package rockstar.client.ui;


import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.QuadColorGradient;

public class VerticalColorGradient
extends QuadColorGradient {
    public VerticalColorGradient(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        super(colorRGBA, colorRGBA, colorRGBA2, colorRGBA2);
    }

    @Override
    public VerticalColorGradient internalMethod01936() {
        return new VerticalColorGradient(this.internalField1312, this.internalField0777);
    }
}

