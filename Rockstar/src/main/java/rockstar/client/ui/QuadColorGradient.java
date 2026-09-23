package rockstar.client.ui;


import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;

public class QuadColorGradient {
    protected final ColorRGBA internalField0777;
    protected final ColorRGBA internalField0776;
    protected final ColorRGBA internalField1311;
    protected final ColorRGBA internalField1312;

    public QuadColorGradient(ColorRGBA colorRGBA, ColorRGBA colorRGBA2, ColorRGBA colorRGBA3, ColorRGBA colorRGBA4) {
        this.internalField0777 = colorRGBA;
        this.internalField0776 = colorRGBA2;
        this.internalField1311 = colorRGBA3;
        this.internalField1312 = colorRGBA4;
    }

    public static QuadColorGradient internalMethod05983(ColorRGBA colorRGBA, ColorRGBA colorRGBA2, ColorRGBA colorRGBA3, ColorRGBA colorRGBA4) {
        return new QuadColorGradient(colorRGBA, colorRGBA2, colorRGBA3, colorRGBA4);
    }

    public QuadColorGradient internalMethod01936() {
        return this;
    }

    @Generated
    public ColorRGBA internalMethod03279() {
        return this.internalField0777;
    }

    @Generated
    public ColorRGBA internalMethod07639() {
        return this.internalField0776;
    }

    @Generated
    public ColorRGBA internalMethod08085() {
        return this.internalField1311;
    }

    @Generated
    public ColorRGBA internalMethod08912() {
        return this.internalField1312;
    }
}

