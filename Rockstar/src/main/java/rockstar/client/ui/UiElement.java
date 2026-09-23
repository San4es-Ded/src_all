package rockstar.client.ui;





import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.util.Identifier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.Insets;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.TextAlignment;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.ui.UiTransition;
import rockstar.client.animation.Easing;
import rockstar.client.core.CursorType;
import rockstar.client.ui.DragConstraint;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class UiElement
extends UiNode {
    private Function<UiElement, ColorRGBA> bg;
    private CornerRadii radius = CornerRadii.internalField0098;
    private float squircle = 0.0f;
    private Insets padding = Insets.internalField0910;
    private float borderWidth = 0.0f;
    private Function<UiElement, ColorRGBA> border;
    private Supplier<String> text;
    private SizedFont font;
    private Function<UiElement, ColorRGBA> textColor;
    private TextAlignment textAlign = TextAlignment.internalField0622;
    private float textInset = 0.0f;
    private boolean textFade = false;
    private float marqueeOffset = 0.0f;
    private boolean marqueeForward = true;
    private float marqueeHold = 0.0f;
    private ColorRGBA shadowColor;
    private float shadowOffX;
    private float shadowOffY;
    private float shadowBlur;
    private String iconName;
    private float iconSize = 0.0f;
    private Function<UiElement, ColorRGBA> iconColor;
    private Identifier image;
    private float imageSize = 0.0f;
    private float imageRadius = 0.0f;
    private Function<UiElement, ColorRGBA> imageColor;
    private InternalType0268 painter;

    public UiElement background(Function<UiElement, ColorRGBA> function) {
        this.bg = function;
        return this;
    }

    public UiElement background(ColorRGBA colorRGBA) {
        this.bg = typedParameter1002 -> colorRGBA;
        return this;
    }

    public UiElement radius(float f) {
        this.radius = CornerRadii.internalMethod03908(f);
        return this;
    }

    public UiElement radius(CornerRadii typedParameter1014) {
        this.radius = typedParameter1014 == null ? CornerRadii.internalField0098 : typedParameter1014;
        return this;
    }

    public UiElement squircle(float f) {
        this.squircle = f;
        return this;
    }

    @Override
    protected float backdropRadius() {
        return this.radius.internalMethod05337();
    }

    @Override
    protected CornerRadii shapeRadius() {
        return this.radius;
    }

    @Override
    protected float shapeSquircle() {
        return this.squircle;
    }

    @Override
    public UiElement blur(float f) {
        super.blur(f);
        return this;
    }

    @Override
    public UiElement blur(float f, ColorRGBA colorRGBA) {
        super.blur(f, colorRGBA);
        return this;
    }

    @Override
    public UiElement glass() {
        super.glass();
        return this;
    }

    @Override
    public UiElement glass(float f, boolean bl) {
        super.glass(f, bl);
        return this;
    }

    public UiElement border(float f, Function<UiElement, ColorRGBA> function) {
        this.borderWidth = f;
        this.border = function;
        return this;
    }

    public UiElement border(float f, ColorRGBA colorRGBA) {
        this.borderWidth = f;
        this.border = typedParameter1002 -> colorRGBA;
        return this;
    }

    public UiElement padding(Insets iIII) {
        this.padding = iIII == null ? Insets.internalField0910 : iIII;
        return this;
    }

    public UiElement padding(float f) {
        this.padding = Insets.internalMethod00172(f);
        return this;
    }

    public UiElement padding(float f, float f2) {
        this.padding = Insets.internalMethod05266(f, f2);
        return this;
    }

    public UiElement text(SizedFont typedValue020, String string, Function<UiElement, ColorRGBA> function) {
        this.font = typedValue020;
        this.text = () -> string;
        this.textColor = function;
        return this;
    }

    public UiElement text(SizedFont typedValue020, Supplier<String> supplier, Function<UiElement, ColorRGBA> function) {
        this.font = typedValue020;
        this.text = supplier;
        this.textColor = function;
        return this;
    }

    public UiElement text(SizedFont typedValue020, String string, ColorRGBA colorRGBA) {
        return this.text(typedValue020, string, (UiElement typedParameter1002) -> colorRGBA);
    }

    public UiElement textAlign(TextAlignment iRockstarClient) {
        this.textAlign = iRockstarClient;
        return this;
    }

    public UiElement textInset(float f) {
        this.textInset = f;
        return this;
    }

    public UiElement fade() {
        this.textFade = true;
        return this;
    }

    public UiElement fade(boolean bl) {
        this.textFade = bl;
        return this;
    }

    public UiElement textShadow(ColorRGBA colorRGBA, float f, float f2, float f3) {
        this.shadowColor = colorRGBA;
        this.shadowOffX = f;
        this.shadowOffY = f2;
        this.shadowBlur = f3;
        return this;
    }

    public UiElement icon(String string, float f, Function<UiElement, ColorRGBA> function) {
        this.iconName = string;
        this.iconSize = f;
        this.iconColor = function;
        return this;
    }

    public UiElement icon(String string, float f, ColorRGBA colorRGBA) {
        return this.icon(string, f, (UiElement typedParameter1002) -> colorRGBA);
    }

    public UiElement image(Identifier identifier, float f, float f2, Function<UiElement, ColorRGBA> function) {
        this.image = identifier;
        this.imageSize = f;
        this.imageRadius = f2;
        this.imageColor = function;
        return this;
    }

    public UiElement image(Identifier identifier, float f, float f2, ColorRGBA colorRGBA) {
        return this.image(identifier, f, f2, (UiElement typedParameter1002) -> colorRGBA);
    }

    public UiElement paint(InternalType0268 nestedValue2034) {
        this.painter = nestedValue2034;
        return this;
    }

    @Override
    public UiElement motion(Motion typedParameter1004) {
        super.motion(typedParameter1004);
        return this;
    }

    @Override
    public UiElement width(float f) {
        super.width(f);
        return this;
    }

    @Override
    public UiElement height(float f) {
        super.height(f);
        return this;
    }

    @Override
    public UiElement size(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    @Override
    public UiElement minSize(float f, float f2) {
        super.minSize(f, f2);
        return this;
    }

    @Override
    public UiElement maxSize(float f, float f2) {
        super.maxSize(f, f2);
        return this;
    }

    @Override
    public UiElement minWidth(float f) {
        super.minWidth(f);
        return this;
    }

    @Override
    public UiElement minHeight(float f) {
        super.minHeight(f);
        return this;
    }

    @Override
    public UiElement fillWidth() {
        super.fillWidth();
        return this;
    }

    @Override
    public UiElement fillHeight() {
        super.fillHeight();
        return this;
    }

    @Override
    public UiElement fill() {
        super.fill();
        return this;
    }

    @Override
    public UiElement at(float f, float f2) {
        super.at(f, f2);
        return this;
    }

    @Override
    public UiElement enter(UiTransition typedParameter1007) {
        super.enter(typedParameter1007);
        return this;
    }

    @Override
    public UiElement exit(UiTransition typedParameter1007) {
        super.exit(typedParameter1007);
        return this;
    }

    @Override
    public UiElement transition(UiTransition typedParameter1007) {
        super.transition(typedParameter1007);
        return this;
    }

    @Override
    public UiElement lifeMotion(Motion typedParameter1004) {
        super.lifeMotion(typedParameter1004);
        return this;
    }

    @Override
    public UiElement onClick(Runnable runnable) {
        super.onClick(runnable);
        return this;
    }

    @Override
    public UiElement onClick(Consumer<MouseButton> consumer) {
        super.onClick(consumer);
        return this;
    }

    @Override
    public UiElement onClick(UiNode.InternalType0352 nestedValue2051) {
        super.onClick(nestedValue2051);
        return this;
    }

    @Override
    public UiElement interactive(boolean bl) {
        super.interactive(bl);
        return this;
    }

    @Override
    public UiElement draggable(DragConstraint typedValue003) {
        super.draggable(typedValue003);
        return this;
    }

    @Override
    public UiElement draggable(boolean bl) {
        super.draggable(bl);
        return this;
    }

    @Override
    public UiElement cursor(CursorType internalValue0001) {
        super.cursor(internalValue0001);
        return this;
    }

    @Override
    public UiElement hoverMotion(Motion typedParameter1004) {
        super.hoverMotion(typedParameter1004);
        return this;
    }

    @Override
    public UiElement center() {
        super.center();
        return this;
    }

    @Override
    public UiElement centerX() {
        super.centerX();
        return this;
    }

    @Override
    public UiElement centerY() {
        super.centerY();
        return this;
    }

    @Override
    public UiElement visibleWhen(BooleanSupplier booleanSupplier) {
        super.visibleWhen(booleanSupplier);
        return this;
    }

    @Override
    public UiElement visibleWhen(BooleanSupplier booleanSupplier, Motion typedParameter1004) {
        super.visibleWhen(booleanSupplier, typedParameter1004);
        return this;
    }

    @Override
    public UiElement visibleWhen(BooleanSupplier booleanSupplier, Easing typedValue214, long l) {
        super.visibleWhen(booleanSupplier, typedValue214, l);
        return this;
    }

    @Override
    public UiElement snapPosition(BooleanSupplier booleanSupplier) {
        super.snapPosition(booleanSupplier);
        return this;
    }

    @Override
    public UiElement snapPosition() {
        super.snapPosition();
        return this;
    }

    @Override
    public UiElement animatePosition() {
        super.animatePosition();
        return this;
    }

    @Override
    public UiElement sticky(BooleanSupplier booleanSupplier) {
        super.sticky(booleanSupplier);
        return this;
    }

    @Override
    public UiElement sticky() {
        super.sticky();
        return this;
    }

    @Override
    public UiElement collapse() {
        super.collapse();
        return this;
    }

    @Override
    public UiElement collapse(boolean bl) {
        super.collapse(bl);
        return this;
    }

    @Override
    public UiElement bind(String string, BooleanSupplier booleanSupplier) {
        super.bind(string, booleanSupplier);
        return this;
    }

    @Override
    public UiElement bind(String string, UiNode.InternalType0353 nestedValue2052) {
        super.bind(string, nestedValue2052);
        return this;
    }

    @Override
    public UiElement bind(String string, BooleanSupplier booleanSupplier, Motion typedParameter1004) {
        super.bind(string, booleanSupplier, typedParameter1004);
        return this;
    }

    @Override
    public UiElement bind(String string, UiNode.InternalType0353 nestedValue2052, Motion typedParameter1004) {
        super.bind(string, nestedValue2052, typedParameter1004);
        return this;
    }

    @Override
    public UiElement bind(String string, BooleanSupplier booleanSupplier, long l) {
        super.bind(string, booleanSupplier, l);
        return this;
    }

    @Override
    public UiElement bind(String string, UiNode.InternalType0353 nestedValue2052, long l) {
        super.bind(string, nestedValue2052, l);
        return this;
    }

    @Override
    public UiElement signalMotion(String string, Motion typedParameter1004) {
        super.signalMotion(string, typedParameter1004);
        return this;
    }

    @Override
    protected void onTick(float f, float f2, float f3) {
        if (!this.textFade || this.font == null || this.text == null) {
            return;
        }
        String string = this.text.get();
        if (string == null || string.isEmpty()) {
            this.marqueeOffset = 0.0f;
            this.marqueeForward = true;
            this.marqueeHold = 0.0f;
            return;
        }
        float f4 = Math.max(1.0f, this.w.internalMethod02046() - this.padding.internalMethod05308() - this.textInset * 2.0f);
        float f5 = Math.max(0.0f, this.font.internalMethod00965(string) - f4);
        if (f5 <= 0.0f) {
            this.marqueeOffset = 0.0f;
            this.marqueeForward = true;
            this.marqueeHold = 0.0f;
            return;
        }
        if (this.contains(f2, f3) && !ANY_DRAGGING) {
            this.marqueeOffset = Math.min(this.marqueeOffset, f5);
            if (this.marqueeHold > 0.0f) {
                this.marqueeHold -= f;
            } else {
                float f6 = f / 1000.0f * 35.0f;
                if (this.marqueeForward) {
                    this.marqueeOffset = Math.min(this.marqueeOffset + f6, f5);
                    if (this.marqueeOffset >= f5) {
                        this.marqueeForward = false;
                        this.marqueeHold = 600.0f;
                    }
                } else {
                    this.marqueeOffset = Math.max(this.marqueeOffset - f6, 0.0f);
                    if (this.marqueeOffset <= 0.0f) {
                        this.marqueeForward = true;
                        this.marqueeHold = 600.0f;
                    }
                }
            }
        } else if (this.marqueeOffset > 0.0f) {
            this.marqueeOffset = Math.max(0.0f, this.marqueeOffset - f / 1000.0f * 35.0f);
        }
    }

    @Override
    protected void measure() {
        float f;
        if (this.image != null && this.imageSize > 0.0f) {
            if (!this.explicitW) {
                this.prefW = this.imageSize + this.padding.internalMethod05308();
            }
            if (!this.explicitH) {
                this.prefH = this.imageSize + this.padding.internalMethod05311();
            }
        }
        if (this.text == null || this.font == null) {
            return;
        }
        String string = this.text.get();
        float f2 = f = string == null || string.isEmpty() ? 0.0f : this.font.internalMethod00965(string);
        if (!this.explicitW) {
            this.prefW = (this.textFade ? 0.0f : f) + this.textInset * 2.0f + this.padding.internalMethod05308();
        }
        if (!this.explicitH) {
            this.prefH = this.font.internalMethod04890() + this.padding.internalMethod05311();
        }
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        float f2;
        Object object;
        ColorRGBA colorRGBA;
        float f3 = this.x.internalMethod02046();
        float f4 = this.y.internalMethod02046();
        float f5 = this.w.internalMethod02046();
        float f6 = this.h.internalMethod02046();
        if (this.bg != null && (colorRGBA = this.bg.apply(this)) != null && colorRGBA.getAlpha() > 0.0f) {
            if (this.squircle > 0.0f) {
                iII.drawSquircle(f3, f4, f5, f6, this.squircle, this.radius, colorRGBA);
            } else {
                iII.drawRoundedRect(f3, f4, f5, f6, this.radius, colorRGBA);
            }
        }
        if (this.border != null && this.borderWidth > 0.0f && (colorRGBA = this.border.apply(this)) != null && colorRGBA.getAlpha() > 0.0f) {
            if (this.squircle > 0.0f) {
                iII.drawSquircleBorder(f3, f4, f5, f6, this.borderWidth, this.squircle, this.radius, colorRGBA);
            } else {
                iII.drawRoundedBorder(f3, f4, f5, f6, this.borderWidth, this.radius, colorRGBA);
            }
        }
        float f7 = f3 + this.padding.internalField1047;
        float f8 = f4 + this.padding.internalField0205;
        float f9 = f5 - this.padding.internalMethod05308();
        float f10 = f6 - this.padding.internalMethod05311();
        if (this.iconName != null && this.iconSize > 0.0f) {
            object = this.iconColor != null ? this.iconColor.apply(this) : ColorRGBA.WHITE;
            iII.drawIcon(this.iconName, f7 + this.textInset, f8 + f10 / 2.0f - this.iconSize / 2.0f, this.iconSize, (ColorRGBA)object);
        }
        if (this.image != null && this.imageSize > 0.0f) {
            Object object2 = object = this.imageColor != null ? this.imageColor.apply(this) : ColorRGBA.WHITE;
            if (object == null) {
                object = ColorRGBA.WHITE;
            }
            float f11 = f7 + f9 / 2.0f - this.imageSize / 2.0f;
            f2 = f8 + f10 / 2.0f - this.imageSize / 2.0f;
            if (this.imageRadius > 0.0f) {
                iII.drawRoundedTexture(this.image, f11, f2, this.imageSize, this.imageSize, CornerRadii.internalMethod03908(this.imageRadius), (ColorRGBA)object);
            } else {
                iII.drawTexture(this.image, f11, f2, this.imageSize, this.imageSize, (ColorRGBA)object);
            }
        }
        if (this.text != null && this.font != null && (object = this.text.get()) != null && !((String)object).isEmpty()) {
            ColorRGBA colorRGBA2 = this.textColor != null ? this.textColor.apply(this) : ColorRGBA.WHITE;
            f2 = f8 + f10 / 2.0f - this.font.internalMethod04890() / 2.0f;
            if (this.textFade) {
                boolean bl;
                float f12 = Math.max(1.0f, f9 - this.textInset * 2.0f);
                boolean bl2 = bl = this.marqueeOffset > 0.01f;
                if (bl) {
                    float f13 = Math.max(f6, this.font.internalMethod04890() + 4.0f);
                    ScissorStack.internalMethod06303(iII.getMatrices(), f7 + this.textInset - 3.0f, f4 - 3.0f, f12 + 6.0f, f13 + 6.0f);
                    iII.pushMatrix();
                    iII.getMatrices().translate(-this.marqueeOffset, 0.0f);
                }
                iII.drawFadeoutText(this.font, (String)object, f7 + this.textInset, f2, colorRGBA2, 0.95f, 1.0f, f12);
                if (bl) {
                    iII.popMatrix();
                    ScissorStack.internalMethod07643();
                }
                if (this.painter != null) {
                    this.painter.paint(iII, this);
                }
                return;
            }
            boolean bl = this.shadowColor != null && this.shadowColor.getAlpha() > 0.0f;
            switch (this.textAlign) {
                case internalField0621: {
                    if (bl) {
                        iII.drawCenteredTextWithShadow(this.font, (String)object, f7 + f9 / 2.0f, f2, colorRGBA2, this.shadowColor, this.shadowOffX, this.shadowOffY, this.shadowBlur);
                        break;
                    }
                    iII.drawCenteredText(this.font, (String)object, f7 + f9 / 2.0f, f2, colorRGBA2);
                    break;
                }
                case internalField1242: {
                    iII.drawRightText(this.font, (String)object, f7 + f9 - this.textInset, f2, colorRGBA2);
                    break;
                }
                default: {
                    if (bl) {
                        iII.drawTextWithShadow(this.font, (String)object, f7 + this.textInset, f2, colorRGBA2, this.shadowColor, this.shadowOffX, this.shadowOffY, this.shadowBlur);
                        break;
                    }
                    iII.drawText(this.font, (String)object, f7 + this.textInset, f2, colorRGBA2);
                }
            }
        }
        if (this.painter != null) {
            this.painter.paint(iII, this);
        }
    }

    public static interface InternalType0268 {
        public void paint(UiRenderContext localValue1, UiElement localValue2);
    }
}

