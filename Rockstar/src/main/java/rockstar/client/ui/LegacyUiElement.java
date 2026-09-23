package rockstar.client.ui;


import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.MouseButton;
import rockstar.client.ui.UiUtils;
import rockstar.client.MinecraftClientAccess;

public abstract class LegacyUiElement
implements MinecraftClientAccess {
    protected float internalField0205;
    protected float internalField0206;
    protected float internalField1048;
    protected float internalField1047;

    protected LegacyUiElement() {
        this(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void internalMethod03398(UiRenderContext iII) {
        this.internalMethod08744(iII);
        this.internalMethod05619(iII);
    }

    protected abstract void internalMethod05619(UiRenderContext localValue1);

    public void internalMethod02325() {
    }

    public void internalMethod08744(UiRenderContext iII) {
    }

    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
    }

    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
    }

    public void internalMethod05727(int n, int n2, int n3) {
    }

    public boolean internalMethod05413(char c, int n) {
        return false;
    }

    public void internalMethod02890(double d, double d2, double d3, double d4) {
    }

    public void internalMethod04932(float f, float f2) {
        this.internalField0205 = f;
        this.internalField0206 = f2;
    }

    public void internalMethod05191(float f, float f2, float f3, float f4) {
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField1048 = f3;
        this.internalField1047 = f4;
    }

    public void internalMethod02101(Rect rect) {
        this.internalField0205 = rect.getX();
        this.internalField0206 = rect.getY();
        this.internalField1048 = rect.getWidth();
        this.internalField1047 = rect.getHeight();
    }

    public boolean internalMethod04933(float f, float f2) {
        return UiUtils.internalMethod05785(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, f, f2);
    }

    public boolean internalMethod04931(double d, double d2) {
        return UiUtils.internalMethod05785(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, d, d2);
    }

    public boolean internalMethod03399(UiRenderContext iII) {
        return this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261());
    }

    @Generated
    public float internalMethod02045() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod02048() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08827() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod07809() {
        return this.internalField1047;
    }

    @Generated
    public void internalMethod03623(float f) {
        this.internalField0205 = f;
    }

    @Generated
    public void internalMethod03701(float f) {
        this.internalField0206 = f;
    }

    @Generated
    public void internalMethod08630(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod08642(float f) {
        this.internalField1047 = f;
    }

    @Generated
    protected LegacyUiElement(float f, float f2, float f3, float f4) {
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField1048 = f3;
        this.internalField1047 = f4;
    }
}

