package rockstar.client.ui;


import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.QuadColorGradient;

public class AlternatingColorGradient
extends QuadColorGradient {
    public AlternatingColorGradient(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        super(colorRGBA, colorRGBA2, colorRGBA, colorRGBA2);
    }

    @Override
    public AlternatingColorGradient internalMethod01936() {
        return new AlternatingColorGradient(this.internalField1312, this.internalField0777);
    }
}

