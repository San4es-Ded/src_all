package pyrock.ui;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.core.*;
import java.util.function.BooleanSupplier;
import jep.python.PyCallable;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.ui.LayeredRockstarScreen;
import rockstar.client.ui.TextAlignment;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.core.ScrollPolicy;
import rockstar.client.RockstarClient;
import rockstar.client.ui.UiTransition;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.FlexDirection;
import rockstar.client.ui.DragConstraint;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class Node {
    private static final ColorRGBA TRANSPARENT = new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
    private final UiNode el;
    private static final long VIS_THROTTLE_MS = 50L;
    private static long visStagger = 0L;

    Node(UiNode typedValue004) {
        this.el = typedValue004;
    }

    public UiNode element() {
        return this.el;
    }

    public Node add(Node node) {
        UiNode typedValue004;
        if (node != null && (typedValue004 = this.el) instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod03907(node.el);
        }
        return this;
    }

    public Node clear() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod03628();
        }
        return this;
    }

    public Node width(float f) {
        this.el.width(f);
        return this;
    }

    public Node height(float f) {
        this.el.height(f);
        return this;
    }

    public Node size(float f, float f2) {
        this.el.size(f, f2);
        return this;
    }

    public Node minSize(float f, float f2) {
        this.el.minSize(f, f2);
        return this;
    }

    public Node maxSize(float f, float f2) {
        this.el.maxSize(f, f2);
        return this;
    }

    public Node minWidth(float f) {
        this.el.minWidth(f);
        return this;
    }

    public Node minHeight(float f) {
        this.el.minHeight(f);
        return this;
    }

    public Node fill() {
        this.el.fill();
        return this;
    }

    public Node fillWidth() {
        this.el.fillWidth();
        return this;
    }

    public Node fillHeight() {
        this.el.fillHeight();
        return this;
    }

    public Node at(float f, float f2) {
        this.el.at(f, f2);
        return this;
    }

    public Node center() {
        this.el.center();
        return this;
    }

    public Node centerX() {
        this.el.centerX();
        return this;
    }

    public Node centerY() {
        this.el.centerY();
        return this;
    }

    public Node animatePosition() {
        this.el.animatePosition();
        return this;
    }

    public Node snapPosition() {
        this.el.snapPosition();
        return this;
    }

    public Node direction(String string) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod01192(Node.parseDir(string));
        }
        return this;
    }

    public Node column() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod01863();
        }
        return this;
    }

    public Node row() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod05895();
        }
        return this;
    }

    public Node gap(float f) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod03062(f);
        }
        return this;
    }

    public Node columns(int n) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod02070(n);
        }
        return this;
    }

    public Node wrap() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod07971();
        }
        return this;
    }

    public Node align(String string) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod01855(Node.parseAlign(string));
        }
        return this;
    }

    public Node justify(String string) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod07607(Node.parseJustify(string));
        }
        return this;
    }

    public Node scrollable() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod08755();
        }
        return this;
    }

    public Node stack() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod08791();
        }
        return this;
    }

    public Node scrollbar(String string) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod01416(Node.parseScroll(string));
        }
        return this;
    }

    public Node stagger(float f) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod08163(f);
        }
        return this;
    }

    public Node pad(float f) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod07351(f);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiElement) {
                UiElement typedParameter003 = (UiElement)typedValue004;
                typedParameter003.padding(f);
            }
        }
        return this;
    }

    public Node pad(float f, float f2) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod02146(f, f2);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiElement) {
                UiElement typedParameter003 = (UiElement)typedValue004;
                typedParameter003.padding(f, f2);
            }
        }
        return this;
    }

    public Node color(ColorRGBA colorRGBA) {
        if (colorRGBA == null) {
            return this;
        }
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.background(colorRGBA);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiContainer) {
                UiContainer typedValue006 = (UiContainer)typedValue004;
                typedValue006.internalMethod02303(colorRGBA);
            }
        }
        return this;
    }

    public Node colorFn(PyCallable pyCallable) {
        if (pyCallable == null) {
            return this;
        }
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedValue002 = (UiElement)typedValue004;
            typedValue002.background(typedParameter003 -> Node.safeColor(pyCallable));
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiContainer) {
                UiContainer typedValue007 = (UiContainer)typedValue004;
                typedValue007.internalMethod06812(typedValue006 -> Node.safeColor(pyCallable));
            }
        }
        return this;
    }

    public Node radius(float f) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.radius(f);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiContainer) {
                UiContainer typedValue006 = (UiContainer)typedValue004;
                typedValue006.internalMethod09018(f);
            }
        }
        return this;
    }

    public Node squircle(float f) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.squircle(f);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiContainer) {
                UiContainer typedValue006 = (UiContainer)typedValue004;
                typedValue006.internalMethod08487(f);
            }
        }
        return this;
    }

    public Node radiusBottom(float f) {
        CornerRadii typedParameter015 = CornerRadii.internalMethod05385(f, f);
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.radius(typedParameter015);
        } else {
            typedValue004 = this.el;
            if (typedValue004 instanceof UiContainer) {
                UiContainer typedValue006 = (UiContainer)typedValue004;
                typedValue006.internalMethod06083(typedParameter015);
            }
        }
        return this;
    }

    public Node blur(float f) {
        this.el.blur(f);
        return this;
    }

    public Node blur(float f, ColorRGBA colorRGBA) {
        this.el.blur(f, colorRGBA);
        return this;
    }

    public Node glass() {
        this.el.glass();
        return this;
    }

    public Node glass(float f, boolean bl) {
        this.el.glass(f, bl);
        return this;
    }

    public Node border(float f, ColorRGBA colorRGBA) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.border(f, colorRGBA);
        }
        return this;
    }

    public Node textAlign(String string) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.textAlign(Node.parseAlign(string));
        }
        return this;
    }

    public Node textShadow(ColorRGBA colorRGBA, float f, float f2, float f3) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.textShadow(colorRGBA, f, f2, f3);
        }
        return this;
    }

    public Node icon(String string, float f, ColorRGBA colorRGBA) {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.icon(string, f, colorRGBA != null ? colorRGBA : ThemeColors.internalField1613);
        }
        return this;
    }

    public Node fade() {
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiElement) {
            UiElement typedParameter003 = (UiElement)typedValue004;
            typedParameter003.fade();
        }
        return this;
    }

    public Node onClick(PyCallable pyCallable) {
        if (pyCallable != null) {
            this.el.onClick(() -> {
                try {
                    pyCallable.call(new Object[0]);
                }
                catch (Exception exception) {
                    RockstarClient.internalField0572.error("[PyUi] onClick", (Throwable)exception);
                }
            });
        }
        return this;
    }

    public Node onClickPos(PyCallable pyCallable) {
        if (pyCallable != null) {
            this.el.onClick((MouseButton typedParameter016, float f, float f2) -> {
                try {
                    pyCallable.call(new Object[]{Float.valueOf(f), Float.valueOf(f2), typedParameter016.name().toLowerCase()});
                }
                catch (Exception exception) {
                    RockstarClient.internalField0572.error("[PyUi] onClick", (Throwable)exception);
                }
            });
        }
        return this;
    }

    public Node interactive(boolean bl) {
        this.el.interactive(bl);
        return this;
    }

    public Node cursor(String string) {
        this.el.cursor(Node.parseCursor(string));
        return this;
    }

    public Node draggable() {
        this.el.draggable(DragConstraint.internalField0631);
        return this;
    }

    public Node draggable(String string) {
        this.el.draggable(Node.parseDrag(string));
        return this;
    }

    public Node visibleWhen(PyCallable pyCallable) {
        if (pyCallable != null) {
            this.el.visibleWhen(Node.throttledBool(pyCallable));
        }
        return this;
    }

    public Node enter(String string) {
        this.el.enter(Node.parseTransition(string));
        return this;
    }

    public Node exit(String string) {
        this.el.exit(Node.parseTransition(string));
        return this;
    }

    public Node enterSlide(float f) {
        this.el.enter(UiTransition.internalMethod02229(f));
        return this;
    }

    public Node exitSlide(float f) {
        this.el.exit(UiTransition.internalMethod02229(f));
        return this;
    }

    public Node motion(String string) {
        this.el.motion(Node.parseMotion(string));
        return this;
    }

    public Node sticky() {
        this.el.sticky();
        return this;
    }

    public Node collapse() {
        this.el.collapse();
        return this;
    }

    public Node bind(String string, PyCallable pyCallable) {
        if (pyCallable != null) {
            this.el.bind(string, Node.boolSupplier(pyCallable));
        }
        return this;
    }

    public Node window(float f, float f2, String string) {
        this.el.size(f, f2).center().draggable(DragConstraint.internalField0631);
        UiNode typedValue004 = this.el;
        if (typedValue004 instanceof UiContainer) {
            UiContainer typedValue006 = (UiContainer)typedValue004;
            typedValue006.internalMethod07351(10.0f).internalMethod03062(8.0f).internalMethod09018(10.0f).internalMethod08487(6.0f).internalMethod02303(ThemeColors.internalMethod07738().withAlpha(235.0f));
        }
        return this;
    }

    public float w() {
        return this.el.w();
    }

    public float h() {
        return this.el.h();
    }

    public boolean hovered() {
        return this.el.hovered();
    }

    private static BooleanSupplier boolSupplier(PyCallable pyCallable) {
        return () -> {
            try {
                return Boolean.TRUE.equals(pyCallable.call(new Object[0]));
            }
            catch (Exception exception) {
                return false;
            }
        };
    }

    private static BooleanSupplier throttledBool(final PyCallable pyCallable) {
        final long l = visStagger++ * 7L % 50L;
        return new BooleanSupplier(){
            boolean cached;
            boolean inited;
            long nextEval;

            @Override
            public boolean getAsBoolean() {
                long l2 = LayeredRockstarScreen.renderClock();
                if (!this.inited) {
                    this.inited = true;
                    this.cached = this.eval();
                    this.nextEval = l2 + l;
                } else if (l2 >= this.nextEval) {
                    this.cached = this.eval();
                    this.nextEval = l2 + 50L;
                }
                return this.cached;
            }

            private boolean eval() {
                try {
                    return Boolean.TRUE.equals(pyCallable.call(new Object[0]));
                }
                catch (Exception exception) {
                    return false;
                }
            }
        };
    }

    private static ColorRGBA safeColor(PyCallable pyCallable) {
        try {
            ColorRGBA colorRGBA;
            Object object = pyCallable.call(new Object[0]);
            return object instanceof ColorRGBA ? (colorRGBA = (ColorRGBA)object) : TRANSPARENT;
        }
        catch (Exception exception) {
            return TRANSPARENT;
        }
    }

    private static TextAlignment parseAlign(String string) {
        if (string == null) {
            return TextAlignment.internalField0622;
        }
        return switch (string.toLowerCase()) {
            case "center" -> TextAlignment.internalField0621;
            case "end" -> TextAlignment.internalField1242;
            case "stretch" -> TextAlignment.internalField1243;
            default -> TextAlignment.internalField0622;
        };
    }

    private static LayoutAlignment parseJustify(String string) {
        if (string == null) {
            return LayoutAlignment.internalField0911;
        }
        return switch (string.toLowerCase()) {
            case "center" -> LayoutAlignment.internalField0912;
            case "end" -> LayoutAlignment.internalField1380;
            case "between", "space_between" -> LayoutAlignment.internalField1377;
            case "around", "space_around" -> LayoutAlignment.internalField1378;
            case "evenly", "space_evenly" -> LayoutAlignment.internalField1379;
            default -> LayoutAlignment.internalField0911;
        };
    }

    private static FlexDirection parseDir(String string) {
        if (string == null) {
            return FlexDirection.internalField0629;
        }
        return switch (string.toLowerCase()) {
            case "up" -> FlexDirection.internalField0628;
            case "right", "row", "horizontal" -> FlexDirection.internalField1246;
            case "left" -> FlexDirection.internalField1247;
            default -> FlexDirection.internalField0629;
        };
    }

    private static ScrollPolicy parseScroll(String string) {
        if (string == null) {
            return ScrollPolicy.internalField0916;
        }
        return switch (string.toLowerCase()) {
            case "always" -> ScrollPolicy.internalField0917;
            case "never" -> ScrollPolicy.internalField1385;
            default -> ScrollPolicy.internalField0916;
        };
    }

    private static DragConstraint parseDrag(String string) {
        if (string == null) {
            return DragConstraint.internalField0631;
        }
        return switch (string.toLowerCase()) {
            case "x", "horizontal" -> DragConstraint.internalField1248;
            case "y", "vertical" -> DragConstraint.internalField1249;
            case "none" -> DragConstraint.internalField0630;
            default -> DragConstraint.internalField0631;
        };
    }

    private static CursorType parseCursor(String string) {
        if (string == null) {
            return CursorType.internalField0566;
        }
        return switch (string.toLowerCase()) {
            case "hand", "pointer" -> CursorType.internalField0567;
            case "text", "ibeam" -> CursorType.internalField1206;
            case "crosshair" -> CursorType.internalField1207;
            case "hresize", "horizontal" -> CursorType.internalField1208;
            case "vresize", "vertical" -> CursorType.internalField1205;
            case "block", "notallowed" -> CursorType.internalField1562;
            case "resize", "resizeall" -> CursorType.internalField1563;
            default -> CursorType.internalField0566;
        };
    }

    private static Motion parseMotion(String string) {
        if (string == null) {
            return Motion.internalField0913;
        }
        return switch (string.toLowerCase()) {
            case "fast" -> Motion.internalField1383;
            case "smooth" -> Motion.internalField1384;
            case "signal" -> Motion.internalField1381;
            case "spring" -> Motion.internalField1382;
            case "spring_snap" -> Motion.internalField1657;
            case "soft", "bakek_soft" -> Motion.internalField0914;
            default -> Motion.internalField0913;
        };
    }

    private static UiTransition parseTransition(String string) {
        if (string == null) {
            return UiTransition.internalField1389;
        }
        return switch (string.toLowerCase()) {
            case "none" -> UiTransition.internalField0918;
            case "vanish" -> UiTransition.internalField0919;
            case "fade_slide" -> UiTransition.internalField1386;
            case "up", "slide_up" -> UiTransition.internalField1388;
            case "down", "slide_down" -> UiTransition.internalField1387;
            case "left", "slide_left" -> UiTransition.internalField1658;
            case "right", "slide_right" -> UiTransition.internalField1660;
            case "pop" -> UiTransition.internalField1661;
            case "fade_pop" -> UiTransition.internalField1659;
            default -> UiTransition.internalField1389;
        };
    }
}

