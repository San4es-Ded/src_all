package rockstar.client.internal.script;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.*;
import java.util.function.Supplier;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.Insets;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public final class UiElementFactory {
    private UiElementFactory() {
    }

    public static UiNode internalMethod03257(SizedFont typedValue020, Supplier<String> supplier, Supplier<ColorRGBA> supplier2) {
        return UiElementFactory.internalMethod04195(typedValue020, supplier, supplier2, () -> 0.0f);
    }

    public static UiNode internalMethod04195(SizedFont typedValue020, Supplier<String> supplier, Supplier<ColorRGBA> supplier2, UiNode.InternalType0353 nestedValue2052) {
        return new InternalType0200(typedValue020, supplier, supplier2, nestedValue2052);
    }

    public static UiNode internalMethod03849(final Identifier identifier, final float f, final float f2, final Supplier<ColorRGBA> supplier, final UiNode.InternalType0353 nestedValue2052) {
        return new UiNode(){
            {
                this.size(f, f2);
                this.interactive(false);
            }

            @Override
            protected void drawSelf(UiRenderContext iII, float f3) {
                ColorRGBA colorRGBA;
                ColorRGBA colorRGBA2 = colorRGBA = supplier == null ? ColorRGBA.WHITE : (ColorRGBA)supplier.get();
                if (colorRGBA != null) {
                    iII.drawTexture(identifier, this.x() + UiElementFactory.internalMethod00792(nestedValue2052), this.y(), this.w(), this.h(), colorRGBA);
                }
            }
        };
    }

    public static UiNode internalMethod02496(final String string, final float f, final Supplier<ColorRGBA> supplier, final UiNode.InternalType0353 nestedValue2052) {
        return new UiNode(){
            {
                this.size(f, f);
                this.interactive(false);
            }

            @Override
            protected void drawSelf(UiRenderContext iII, float f2) {
                ColorRGBA colorRGBA;
                ColorRGBA colorRGBA2 = colorRGBA = supplier == null ? ColorRGBA.WHITE : (ColorRGBA)supplier.get();
                if (colorRGBA != null) {
                    iII.drawIcon(string, this.x() + UiElementFactory.internalMethod00792(nestedValue2052), this.y(), Math.min(this.w(), this.h()), colorRGBA);
                }
            }
        };
    }

    public static UiNode internalMethod00122(final float f, final float f2, final InternalType0199 nestedValue2028) {
        return new UiNode(){
            {
                this.size(f, f2);
                this.interactive(false);
            }

            @Override
            protected void drawSelf(UiRenderContext iII, float f3) {
                if (nestedValue2028 != null) {
                    nestedValue2028.paint(iII, this, f3);
                }
            }
        };
    }

    public static UiNode internalMethod04181(final UiNode.InternalType0353 nestedValue2052, final UiNode.InternalType0353 nestedValue2053) {
        return new UiNode(){
            {
                this.interactive(false);
            }

            @Override
            protected void measure() {
                this.prefW = Math.max(0.0f, UiElementFactory.internalMethod00792(nestedValue2052));
                this.prefH = Math.max(0.0f, UiElementFactory.internalMethod00792(nestedValue2053));
            }
        };
    }

    public static UiContainer internalMethod04233(UiNode.InternalType0353 nestedValue2052) {
        return new InternalType0397(nestedValue2052);
    }

    public static UiContainer internalMethod01039(InternalType0199 nestedValue2028) {
        return new InternalType0398(nestedValue2028);
    }

    public static UiContainer internalMethod01948(float f, Insets iIII, float f2) {
        return new UiContainer().internalMethod05895().internalMethod09266(f).internalMethod03514(iIII).internalMethod03062(f2).internalMethod07853(false);
    }

    public static float internalMethod00792(UiNode.InternalType0353 nestedValue2052) {
        return nestedValue2052 == null ? 0.0f : nestedValue2052.get();
    }

    static final class InternalType0200
    extends UiNode {
        private final SizedFont internalField0447;
        private final Supplier<String> internalField0017;
        private final Supplier<ColorRGBA> internalField0018;
        private final UiNode.InternalType0353 internalField0334;

        InternalType0200(SizedFont typedValue020, Supplier<String> supplier, Supplier<ColorRGBA> supplier2, UiNode.InternalType0353 nestedValue2052) {
            this.internalField0447 = typedValue020;
            this.internalField0017 = supplier;
            this.internalField0018 = supplier2;
            this.internalField0334 = nestedValue2052;
            this.interactive(false);
        }

        @Override
        protected void measure() {
            String string = this.internalMethod06260();
            if (!this.explicitW) {
                float f = this.prefW = string.isEmpty() ? 0.0f : this.internalField0447.internalMethod00965(string);
            }
            if (!this.explicitH) {
                this.prefH = this.internalField0447.internalMethod04890();
            }
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            ColorRGBA colorRGBA;
            String string = this.internalMethod06260();
            if (string.isEmpty()) {
                return;
            }
            ColorRGBA colorRGBA2 = colorRGBA = this.internalField0018 == null ? ColorRGBA.WHITE : this.internalField0018.get();
            if (colorRGBA != null) {
                iII.drawText(this.internalField0447, string, this.x() + UiElementFactory.internalMethod00792(this.internalField0334), this.y(), colorRGBA);
            }
        }

        private String internalMethod06260() {
            String string = this.internalField0017 == null ? "" : this.internalField0017.get();
            return string == null ? "" : string;
        }
    }

    public static interface InternalType0199 {
        public void paint(UiRenderContext localValue1, UiNode localValue2, float localValue3);
    }

    static final class InternalType0397
    extends UiContainer {
        private final UiNode.InternalType0353 internalField0334;

        InternalType0397(UiNode.InternalType0353 nestedValue2052) {
            this.internalField0334 = nestedValue2052;
            this.internalMethod07853(false);
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            iII.getMatrices().pushMatrix();
            iII.getMatrices().translate(UiElementFactory.internalMethod00792(this.internalField0334), 0.0f);
            super.drawSelf(iII, f);
        }

        @Override
        protected void drawChildren(UiRenderContext iII, float f) {
            super.drawChildren(iII, f);
            iII.getMatrices().popMatrix();
        }
    }

    static final class InternalType0398
    extends UiContainer {
        private final InternalType0199 internalField0007;

        InternalType0398(InternalType0199 nestedValue2028) {
            this.internalField0007 = nestedValue2028;
            this.internalMethod07853(false);
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            if (this.internalField0007 != null) {
                this.internalField0007.paint(iII, this, f);
            }
        }

        @Override
        protected void drawChildren(UiRenderContext iII, float f) {
        }
    }
}

