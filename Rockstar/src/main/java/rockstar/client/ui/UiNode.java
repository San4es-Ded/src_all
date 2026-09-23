package rockstar.client.ui;





import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.compat.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import pyrock.utility.render.ColorRGBA;

public abstract class UiNode {
   protected Motion motion = Motion.internalField0913;
   final AnimatedFloat x = new AnimatedFloat(this.motion);
   final AnimatedFloat y = new AnimatedFloat(this.motion);
   final AnimatedFloat w = new AnimatedFloat(this.motion);
   final AnimatedFloat h = new AnimatedFloat(this.motion);
   protected float prefW;
   protected float prefH;
   protected float minW = 0.0F;
   protected float minH = 0.0F;
   protected float maxW = Float.MAX_VALUE;
   protected float maxH = Float.MAX_VALUE;
   protected boolean explicitW;
   protected boolean explicitH;
   protected boolean fillW;
   protected boolean fillH;
   protected UiNode parent;
   private final AnimatedFloat hover = new AnimatedFloat(0.0F, Motion.internalMethod01328(300L, Easing.internalField1626));
   private final AnimatedFloat press = new AnimatedFloat(0.0F, Motion.internalField1381);
   private Map<String, AnimatedFloat> signals;
   private Map<String, UiNode.InternalType0353> bindings;
   protected boolean interactive = true;
   private boolean modal;
   private boolean pressed;
   private MouseButton pressedButton;
   private boolean hovered;
   boolean hoverable = true;
   protected UiNode.InternalType0146 phase = UiNode.InternalType0146.internalField0260;
   final AnimatedFloat appear = new AnimatedFloat(1.0F, Motion.internalMethod01328(240L, Easing.internalField1822));
   private UiTransition enterT = UiTransition.internalField1389;
   private UiTransition exitT = UiTransition.internalField1389;
   private float appearDelay;
   private float pendingTarget = 1.0F;
   private boolean lifeStarted = true;
   private final UiTransition.InternalType0040 tweak = new UiTransition.InternalType0040();
   private Runnable leftClick;
   private Consumer<MouseButton> buttonClick;
   private UiNode.InternalType0352 posClick;
   public static boolean ANY_DRAGGING = false;
   static boolean DRAW_CLIP = false;
   static float DRAW_CLIP_MIN = 0.0F;
   static float DRAW_CLIP_MAX = 0.0F;
   private DragConstraint dragMode = DragConstraint.internalField0630;
   private boolean dragging;
   private DragConstraint dragAxis = DragConstraint.internalField0631;
   private float dragStartMx;
   private float dragStartMy;
   private float dragStartX;
   private float dragStartY;
   private boolean manuallyPositioned;
   private CursorType cursor;
   private boolean centerX;
   private boolean centerY;
   private BooleanSupplier visibleWhen;
   private boolean visInit;
   private BooleanSupplier snapPosition;
   private BooleanSupplier snapSize;
   private boolean forcedSnap;
   private boolean snappedThisFrame;
   private boolean ignoreSnap;
   private BooleanSupplier stickyWhen;
   private boolean collapse;
   private static int layoutEpoch;
   private static UiNode spotlight;
   private AnimatedFloat spotlightAnim;
   private boolean onSpotlightPath;
   private int seenEpoch;
   private float shakeLeft;
   private float shakeElapsed;
   private float shakeAmp = 1.5F;
   private float shakeDur = 450.0F;
   private float blurAmount;
   private ColorRGBA blurTint;
   private float glassAlpha = -1.0F;
   private boolean glassOutline;

   public UiNode blur(float localValue1) {
      this.blurAmount = Math.max(0.0F, localValue1);
      return this;
   }

   public UiNode blur(float localValue1, ColorRGBA localValue2) {
      this.blurTint = localValue2;
      return this.blur(localValue1);
   }

   public UiNode glass() {
      return this.glass(1.0F, true);
   }

   public UiNode glass(float localValue1, boolean localValue2) {
      this.glassAlpha = Math.max(0.0F, localValue1);
      this.glassOutline = localValue2;
      return this;
   }

   protected CornerRadii shapeRadius() {
      return CornerRadii.internalField0098;
   }

   protected float shapeSquircle() {
      return 0.0F;
   }

   public UiNode motion(Motion localValue1) {
      this.motion = localValue1;
      this.x.internalMethod00216(localValue1);
      this.y.internalMethod00216(localValue1);
      this.w.internalMethod00216(localValue1);
      this.h.internalMethod00216(localValue1);
      return this;
   }

   public UiNode width(float localValue1) {
      this.prefW = localValue1;
      this.explicitW = true;
      this.w.internalMethod03690(this.clampW(localValue1));
      return this;
   }

   public UiNode height(float localValue1) {
      this.prefH = localValue1;
      this.explicitH = true;
      this.h.internalMethod03690(this.clampH(localValue1));
      return this;
   }

   public UiNode size(float localValue1, float localValue2) {
      this.width(localValue1);
      this.height(localValue2);
      return this;
   }

   public UiNode minSize(float localValue1, float localValue2) {
      this.minW = localValue1;
      this.minH = localValue2;
      return this;
   }

   public UiNode maxSize(float localValue1, float localValue2) {
      this.maxW = localValue1;
      this.maxH = localValue2;
      return this;
   }

   public UiNode minWidth(float localValue1) {
      this.minW = localValue1;
      return this;
   }

   public UiNode minHeight(float localValue1) {
      this.minH = localValue1;
      return this;
   }

   public UiNode fillWidth() {
      this.fillW = true;
      return this;
   }

   public UiNode fillHeight() {
      this.fillH = true;
      return this;
   }

   public UiNode fill() {
      this.fillW = this.fillH = true;
      return this;
   }

   public UiNode at(float localValue1, float localValue2) {
      this.x.internalMethod03690(localValue1);
      this.y.internalMethod03690(localValue2);
      return this;
   }

   public UiNode snapAt(float localValue1, float localValue2) {
      this.x.internalMethod03759(localValue1);
      this.y.internalMethod03759(localValue2);
      return this;
   }

   public UiNode snapToSize(float localValue1, float localValue2) {
      this.prefW = localValue1;
      this.explicitW = true;
      this.w.internalMethod03759(this.clampW(localValue1));
      this.prefH = localValue2;
      this.explicitH = true;
      this.h.internalMethod03759(this.clampH(localValue2));
      return this;
   }

   final void rideWith(float localValue1, float localValue2) {
      if (!this.ignoreSnap) {
         this.x.internalMethod08930(localValue1);
         this.y.internalMethod08930(localValue2);
      }
   }

   public UiNode enter(UiTransition localValue1) {
      this.enterT = localValue1;
      return this;
   }

   public UiNode exit(UiTransition localValue1) {
      this.exitT = localValue1;
      return this;
   }

   public UiNode transition(UiTransition localValue1) {
      this.enterT = localValue1;
      this.exitT = localValue1;
      return this;
   }

   public UiNode lifeMotion(Motion localValue1) {
      this.appear.internalMethod00216(localValue1);
      return this;
   }

   public UiNode onClick(Runnable localValue1) {
      this.leftClick = localValue1;
      return this;
   }

   public UiNode onClick(Consumer<MouseButton> localValue1) {
      this.buttonClick = localValue1;
      return this;
   }

   public UiNode onClick(UiNode.InternalType0352 localValue1) {
      this.posClick = localValue1;
      return this;
   }

   public UiNode interactive(boolean localValue1) {
      this.interactive = localValue1;
      return this;
   }

   public UiNode modal() {
      this.modal = true;
      return this;
   }

   boolean isModal() {
      return this.modal;
   }

   public UiNode draggable(DragConstraint localValue1) {
      this.dragMode = localValue1 == null ? DragConstraint.internalField0630 : localValue1;
      return this;
   }

   public UiNode draggable(boolean localValue1) {
      this.dragMode = localValue1 ? DragConstraint.internalField0631 : DragConstraint.internalField0630;
      return this;
   }

   public UiNode cursor(CursorType localValue1) {
      this.cursor = localValue1;
      return this;
   }

   public UiNode hoverMotion(Motion localValue1) {
      if (localValue1 != null) {
         this.hover.internalMethod00216(localValue1);
      }

      return this;
   }

   public UiNode center() {
      this.centerX = this.centerY = true;
      return this;
   }

   public UiNode centerX() {
      this.centerX = true;
      return this;
   }

   public UiNode centerY() {
      this.centerY = true;
      return this;
   }

   public UiNode visibleWhen(BooleanSupplier localValue1) {
      this.visibleWhen = localValue1;
      return this;
   }

   public UiNode visibleWhen(BooleanSupplier localValue1, Motion localValue2) {
      this.visibleWhen = localValue1;
      if (localValue2 != null) {
         this.appear.internalMethod00216(localValue2);
      }

      return this;
   }

   public UiNode visibleWhen(BooleanSupplier localValue1, Easing localValue2, long localValue3) {
      return this.visibleWhen(localValue1, Motion.internalMethod01328(localValue3, localValue2));
   }

   public UiNode snapPosition(BooleanSupplier localValue1) {
      this.snapPosition = localValue1;
      return this;
   }

   public UiNode snapPosition() {
      this.snapPosition = () -> true;
      return this;
   }

   public UiNode animatePosition() {
      this.ignoreSnap = true;
      return this;
   }

   public UiNode snapSize(BooleanSupplier localValue1) {
      this.snapSize = localValue1;
      return this;
   }

   public UiNode snapSize() {
      this.snapSize = () -> true;
      return this;
   }

   public UiNode sticky(BooleanSupplier localValue1) {
      this.stickyWhen = localValue1;
      return this;
   }

   public UiNode sticky() {
      this.stickyWhen = () -> true;
      return this;
   }

   public UiNode collapse() {
      this.collapse = true;
      return this;
   }

   public UiNode collapse(boolean localValue1) {
      this.collapse = localValue1;
      return this;
   }

   public UiNode shakeConfig(float localValue1, float localValue2) {
      this.shakeAmp = localValue1;
      this.shakeDur = Math.max(1.0F, localValue2);
      return this;
   }

   public void shake() {
      this.shakeLeft = this.shakeDur;
      this.shakeElapsed = 0.0F;
   }

   public boolean shaking() {
      return this.shakeLeft > 0.0F;
   }

   public float shakeAmount() {
      return this.shakeLeft > 0.0F ? this.shakeLeft / this.shakeDur : 0.0F;
   }

   public UiNode bind(String localValue1, BooleanSupplier localValue2) {
      return this.bind(localValue1, () -> localValue2.getAsBoolean() ? 1.0F : 0.0F);
   }

   public UiNode bind(String localValue1, UiNode.InternalType0353 localValue2) {
      if (this.bindings == null) {
         this.bindings = new HashMap<>();
      }

      this.bindings.put(localValue1, localValue2);
      this.signal(localValue1);
      return this;
   }

   public UiNode bind(String localValue1, BooleanSupplier localValue2, Motion localValue3) {
      return this.bind(localValue1, () -> localValue2.getAsBoolean() ? 1.0F : 0.0F, localValue3);
   }

   public UiNode bind(String localValue1, UiNode.InternalType0353 localValue2, Motion localValue3) {
      this.bind(localValue1, localValue2);
      this.signalMotion(localValue1, localValue3);
      return this;
   }

   public UiNode bind(String localValue1, BooleanSupplier localValue2, long localValue3) {
      return this.bind(localValue1, () -> localValue2.getAsBoolean() ? 1.0F : 0.0F, localValue3);
   }

   public UiNode bind(String localValue1, UiNode.InternalType0353 localValue2, long localValue3) {
      this.bind(localValue1, localValue2);
      this.signalMotion(localValue1, Motion.internalMethod01870(localValue3));
      return this;
   }

   public UiNode signalMotion(String localValue1, Motion localValue2) {
      if (localValue2 != null) {
         this.signal(localValue1).internalMethod00216(localValue2);
      }

      return this;
   }

   public float hover() {
      return this.hover.internalMethod02046();
   }

   public boolean hovered() {
      return this.hovered;
   }

   public float press() {
      return this.press.internalMethod02046();
   }

   public float appear() {
      return this.appear.internalMethod02046();
   }

   public boolean dragging() {
      return this.dragging;
   }

   public boolean pressed() {
      return this.pressed;
   }

   public float sig(String localValue1) {
      AnimatedFloat localValue2 = this.signals == null ? null : this.signals.get(localValue1);
      return localValue2 == null ? 0.0F : localValue2.internalMethod02046();
   }

   public float sig(String localValue1, Easing localValue2) {
      float localValue3 = this.sig(localValue1);
      return localValue2 == null ? localValue3 : localValue2.ease(localValue3, 0.0F, 1.0F, 1.0F);
   }

   public AnimatedFloat signal(String localValue1) {
      if (this.signals == null) {
         this.signals = new HashMap<>();
      }

      return this.signals.computeIfAbsent(localValue1, localValue0 -> new AnimatedFloat(0.0F, Motion.internalField1381));
   }

   public float x() {
      return this.x.internalMethod02046();
   }

   public float y() {
      return this.y.internalMethod02046();
   }

   public float w() {
      return this.w.internalMethod02046();
   }

   public float h() {
      return this.h.internalMethod02046();
   }

   public UiNode parent() {
      return this.parent;
   }

   public UiNode.InternalType0146 phase() {
      return this.phase;
   }

   public boolean alive() {
      return this.phase != UiNode.InternalType0146.internalField1090;
   }

   public void close() {
      this.pressed = false;
      this.pressedButton = null;
      if (this.dragging) {
         this.dragging = false;
         ANY_DRAGGING = false;
      }

      this.beginExit(0.0F);
   }

   public void discard() {
      this.phase = UiNode.InternalType0146.internalField1090;
      this.appear.internalMethod03759(0.0F);
      this.pressed = false;
      this.pressedButton = null;
      this.hovered = false;
      this.spotlightAnim = null;
      this.onSpotlightPath = false;
      if (spotlight == this) {
         spotlight = null;
      }

      if (this.dragging) {
         this.dragging = false;
         ANY_DRAGGING = false;
      }
   }

   public float life() {
      float localValue1 = this.appear.internalMethod02046();
      return localValue1 < 0.0F ? 0.0F : (localValue1 > 1.0F ? 1.0F : localValue1);
   }

   public boolean inFlow() {
      if (this.phase == UiNode.InternalType0146.internalField1090 || this.phase == UiNode.InternalType0146.internalField1089) {
         return false;
      } else {
         return this.phase == UiNode.InternalType0146.internalField1091 ? this.collapse : true;
      }
   }

   protected float clampW(float localValue1) {
      return Math.max(this.minW, Math.min(this.maxW, localValue1));
   }

   protected float clampH(float localValue1) {
      return Math.max(this.minH, Math.min(this.maxH, localValue1));
   }

   protected float collapseScale() {
      if (!this.collapse) {
         return 1.0F;
      } else {
         float localValue1 = this.appear.internalMethod02046();
         return localValue1 < 0.0F ? 0.0F : (localValue1 > 1.0F ? 1.0F : localValue1);
      }
   }

   protected float rawDesiredH() {
      return this.clampH(this.prefH);
   }

   public float desiredW() {
      return this.clampW(this.prefW);
   }

   public float desiredH() {
      return this.rawDesiredH() * this.collapseScale();
   }

   public void setSlot(float localValue1, float localValue2, float localValue3, float localValue4) {
      boolean localValue5 = this.epochSnap();
      boolean localValue6 = localValue5 || !this.ignoreSnap && (this.forcedSnap || this.snapPosition != null && this.snapPosition.getAsBoolean());
      this.forcedSnap = false;
      this.snappedThisFrame = localValue6;
      if (localValue6) {
         this.x.internalMethod03759(localValue1);
         this.y.internalMethod03759(localValue2);
      } else {
         this.x.internalMethod03690(localValue1);
         this.y.internalMethod03690(localValue2);
      }

      boolean localValue7 = localValue5 || this.snapSize != null && this.snapSize.getAsBoolean();
      if (localValue7) {
         this.w.internalMethod03759(this.clampW(localValue3));
      } else {
         this.w.internalMethod03690(this.clampW(localValue3));
      }

      if (!this.collapse || this.phase != UiNode.InternalType0146.internalField0259 && this.phase != UiNode.InternalType0146.internalField1091) {
         if (localValue7) {
            this.h.internalMethod03759(this.clampH(localValue4));
         } else {
            this.h.internalMethod03690(this.clampH(localValue4));
         }
      } else {
         this.h.internalMethod03759(this.rawDesiredH());
      }
   }

   boolean snappedThisFrame() {
      return this.snappedThisFrame;
   }

   void forceSnap() {
      this.forcedSnap = true;
   }

   public void snapSubtree() {
      this.forceSnap();
   }

   public static void invalidateLayout() {
      layoutEpoch++;
   }

   public static void spotlight(UiNode localValue0) {
      if (spotlight != localValue0) {
         UiNode localValue1 = spotlight;
         spotlight = localValue0;
         if (localValue1 != null) {
            localValue1.spotlightAnim().internalMethod03690(0.0F);
         }

         if (localValue0 != null) {
            localValue0.spotlightAnim().internalMethod03690(1.0F);

            for (UiNode localValue2 = localValue0; localValue2 != null; localValue2 = localValue2.parent) {
               localValue2.onSpotlightPath = true;
            }
         }
      }
   }

   public static UiNode spotlight() {
      return spotlight;
   }

   private AnimatedFloat spotlightAnim() {
      if (this.spotlightAnim == null) {
         this.spotlightAnim = new AnimatedFloat(0.0F, Motion.internalMethod01328(200L, Easing.internalField1828));
      }

      return this.spotlightAnim;
   }

   public float spotlightAmount() {
      if (this.spotlightAnim == null) {
         return 0.0F;
      } else {
         float localValue1 = this.spotlightAnim.internalMethod02046();
         return localValue1 < 0.0F ? 0.0F : (localValue1 > 1.0F ? 1.0F : localValue1);
      }
   }

   private void stepSpotlight(float localValue1) {
      if (this.spotlightAnim != null) {
         this.spotlightAnim.internalMethod08946(localValue1);
         if (this != spotlight && !(this.spotlightAnim.internalMethod02046() > 0.002F)) {
            this.spotlightAnim = null;

            for (UiNode localValue2 = this; localValue2 != null; localValue2 = localValue2.parent) {
               localValue2.onSpotlightPath = false;
            }

            for (UiNode localValue3 = spotlight; localValue3 != null; localValue3 = localValue3.parent) {
               localValue3.onSpotlightPath = true;
            }
         }
      }
   }

   protected final boolean epochSnap() {
      return this.seenEpoch != layoutEpoch;
   }

   public boolean isSticky() {
      return this.stickyWhen != null && this.stickyWhen.getAsBoolean();
   }

   protected void measure() {
   }

   final void primeSize() {
      if (!this.w.internalField0277) {
         this.w.internalMethod03759(this.clampW(this.desiredW()));
      }

      if (!this.h.internalField0277) {
         this.h.internalMethod03759(this.clampH(this.desiredH()));
      }
   }

   public final void prepareRoot() {
      this.measure();
      this.primeSize();
   }

   final void centerWithin(float localValue1, float localValue2) {
      if (!this.manuallyPositioned) {
         boolean localValue3 = this.epochSnap();
         if (this.centerX) {
            float localValue4 = (localValue1 - this.desiredW()) / 2.0F;
            if (localValue3) {
               this.x.internalMethod03759(localValue4);
            } else {
               this.x.internalMethod03690(localValue4);
            }
         }

         if (this.centerY) {
            float localValue5 = (localValue2 - this.desiredH()) / 2.0F;
            if (localValue3) {
               this.y.internalMethod03759(localValue5);
            } else {
               this.y.internalMethod03690(localValue5);
            }
         }
      }
   }

   public void beginEnter(float localValue1) {
      this.phase = UiNode.InternalType0146.internalField0259;
      this.appear.internalMethod03759(0.0F);
      this.appearDelay = Math.max(0.0F, localValue1);
      this.pendingTarget = 1.0F;
      this.lifeStarted = localValue1 <= 0.0F;
      if (this.lifeStarted) {
         this.appear.internalMethod03690(1.0F);
      }

      this.forceSnap();
   }

   public void beginExit(float localValue1) {
      this.phase = UiNode.InternalType0146.internalField1091;
      this.appearDelay = Math.max(0.0F, localValue1);
      this.pendingTarget = 0.0F;
      this.lifeStarted = localValue1 <= 0.0F;
      if (this.lifeStarted) {
         this.appear.internalMethod03690(0.0F);
      }
   }

   private void advanceLife(float localValue1) {
      if (!this.lifeStarted) {
         this.appearDelay -= localValue1;
         if (this.appearDelay <= 0.0F) {
            this.lifeStarted = true;
            this.appear.internalMethod03690(this.pendingTarget);
         }
      }

      if (this.phase == UiNode.InternalType0146.internalField0259 && this.appear.internalMethod02046() >= 0.999F) {
         this.phase = UiNode.InternalType0146.internalField0260;
      }

      if (this.phase == UiNode.InternalType0146.internalField1091 && this.lifeStarted && this.appear.internalMethod02046() <= 0.001F) {
         this.phase = this.visibleWhen != null ? UiNode.InternalType0146.internalField1089 : UiNode.InternalType0146.internalField1090;
      }
   }

   private void applyVisibility() {
      boolean localValue1 = this.visibleWhen.getAsBoolean();
      if (!this.visInit) {
         this.visInit = true;
         if (!localValue1) {
            this.phase = UiNode.InternalType0146.internalField1089;
            this.appear.internalMethod03759(0.0F);
         }
      } else {
         boolean localValue2 = this.phase == UiNode.InternalType0146.internalField0259 || this.phase == UiNode.InternalType0146.internalField0260;
         if (localValue1 && !localValue2) {
            this.beginEnter(0.0F);
         } else if (!localValue1 && localValue2) {
            this.beginExit(0.0F);
         }
      }
   }

   public final void tick(float localValue1, float localValue2, float localValue3) {
      this.dropLostPress(localValue2, localValue3);
      if (this.visibleWhen != null) {
         this.applyVisibility();
      }

      if (this.dragging) {
         this.applyDrag(localValue2, localValue3);
      }

      this.hovered = this.hoverable && this.interactive && this.inFlow() && this.contains(localValue2, localValue3);
      this.hover.internalMethod03690(this.hovered ? 1.0F : 0.0F);
      this.press.internalMethod03690(this.pressed && this.hovered ? 1.0F : 0.0F);
      if (this.hovered && this.cursor != null) {
         CursorManager.internalMethod06882(this.cursor);
      }

      if (this.bindings != null) {
         for (Entry localValue5 : this.bindings.entrySet()) {
            this.signal((String)localValue5.getKey()).internalMethod03690(((UiNode.InternalType0353)localValue5.getValue()).get());
         }
      }

      this.advanceLife(localValue1);
      this.x.internalMethod08946(localValue1);
      this.y.internalMethod08946(localValue1);
      this.w.internalMethod08946(localValue1);
      this.h.internalMethod08946(localValue1);
      this.hover.internalMethod08946(localValue1);
      this.press.internalMethod08946(localValue1);
      this.appear.internalMethod08946(localValue1);
      this.stepSpotlight(localValue1);
      if (this.signals != null) {
         for (AnimatedFloat localValue7 : this.signals.values()) {
            localValue7.internalMethod08946(localValue1);
         }
      }

      if (this.shakeLeft > 0.0F) {
         this.shakeLeft -= localValue1;
         this.shakeElapsed += localValue1;
      }

      this.onTick(localValue1, localValue2, localValue3);
      this.seenEpoch = layoutEpoch;
      this.hoverable = true;
   }

   protected void onTick(float localValue1, float localValue2, float localValue3) {
   }

   public final void draw(UiRenderContext localValue1, float localValue2) {
      this.tweak.internalMethod02215();
      UiTransition localValue3 = this.phase == UiNode.InternalType0146.internalField1091 ? this.exitT : this.enterT;
      if (localValue3 != null) {
         localValue3.apply(this.appear.internalMethod02046(), this, this.tweak);
      }

      float localValue4 = this.spotlightAmount();
      float localValue5 = (localValue2 + (1.0F - localValue2) * localValue4) * this.tweak.internalField0205;
      if (!(localValue5 <= 0.003F) || this.onSpotlightPath) {
         if (DRAW_CLIP) {
            float localValue6 = this.y.internalMethod02046();
            if (localValue6 + this.h.internalMethod02046() < DRAW_CLIP_MIN || localValue6 > DRAW_CLIP_MAX) {
               return;
            }
         }

         org.joml.Matrix3x2fStack localValue13 = localValue1.getMatrices();
         boolean localValue7 = this.collapse && this.appear.internalMethod02046() < 0.999F;
         if (localValue7) {
            ScissorStack.internalMethod06303(
               localValue13, this.x.internalMethod02046(), this.y.internalMethod02046(), this.w.internalMethod02046(), Math.max(0.0F, this.desiredH())
            );
         }

         float localValue8 = localValue7 ? (1.0F - this.collapseScale()) * this.h.internalMethod02046() : 0.0F;
         float localValue9 = 0.0F;
         if (this.shakeLeft > 0.0F) {
            localValue9 = (float)Math.sin(this.shakeElapsed * 0.05F) * this.shakeAmp * (this.shakeLeft / this.shakeDur);
         }

         boolean localValue10 = this.tweak.internalField0206 != 0.0F || this.tweak.internalField1048 != 0.0F || this.tweak.internalField1047 != 1.0F || localValue8 != 0.0F || localValue9 != 0.0F;
         float[] previousShaderColor = RenderSystem.getShaderColor().clone();
         boolean matrixPushed = false;

         try {
            if (localValue10) {
               localValue13.pushMatrix();
               matrixPushed = true;
               if (localValue9 != 0.0F) {
                  localValue13.translate(localValue9, 0.0F);
               }

               if (this.tweak.internalField0206 != 0.0F || this.tweak.internalField1048 != 0.0F) {
                  localValue13.translate(this.tweak.internalField0206, this.tweak.internalField1048);
               }

               if (localValue8 != 0.0F) {
                  localValue13.translate(0.0F, -localValue8);
               }

               if (this.tweak.internalField1047 != 1.0F) {
                  float localValue11 = this.x.internalMethod02046() + this.w.internalMethod02046() / 2.0F;
                  float localValue12 = this.y.internalMethod02046() + this.h.internalMethod02046() / 2.0F;
                  localValue13.translate(localValue11, localValue12);
                  localValue13.scale(this.tweak.internalField1047, this.tweak.internalField1047);
                  localValue13.translate(-localValue11, -localValue12);
               }
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue5);
            if (localValue4 > 0.0F) {
               this.drawSpotlightBackdrop(localValue1, (1.0F - localValue2) * localValue4);
            }

            if (this.blurAmount > 0.0F || this.glassAlpha >= 0.0F) {
               this.drawBackdrop(localValue1);
            }

            this.drawSelf(localValue1, localValue5);
            this.drawChildren(localValue1, localValue5);
         } finally {
            try {
               RenderSystem.setShaderColor(previousShaderColor[0], previousShaderColor[1], previousShaderColor[2], previousShaderColor[3]);
               if (matrixPushed) {
                  localValue13.popMatrix();
               }
            } finally {
               if (localValue7) {
                  ScissorStack.internalMethod07643();
               }
            }
         }
      }
   }

   private void drawBackdrop(UiRenderContext localValue1) {
      float localValue2 = this.x.internalMethod02046();
      float localValue3 = this.y.internalMethod02046();
      float localValue4 = this.w.internalMethod02046();
      float localValue5 = this.h.internalMethod02046();
      if (!(localValue4 <= 0.0F) && !(localValue5 <= 0.0F)) {
         CornerRadii localValue6 = this.shapeRadius();
         float localValue7 = this.shapeSquircle();
         if (this.glassAlpha >= 0.0F) {
            localValue1.drawClientRect(localValue2, localValue3, localValue4, localValue5, this.glassAlpha, 0.0F, localValue7, localValue6.internalMethod05337(), this.glassOutline);
         } else {
            ColorRGBA localValue8 = this.blurTint == null ? ColorRGBA.WHITE : this.blurTint;
            if (localValue7 > 0.0F) {
               localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, this.blurAmount, localValue7, localValue6, localValue8);
            } else {
               localValue1.drawBlurredRect(localValue2, localValue3, localValue4, localValue5, this.blurAmount, localValue6, localValue8);
            }
         }
      }
   }

   protected float backdropRadius() {
      return 0.0F;
   }

   private void drawSpotlightBackdrop(UiRenderContext localValue1, float localValue2) {
      if (!(localValue2 <= 0.01F)) {
         float localValue3 = this.x.internalMethod02046();
         float localValue4 = this.y.internalMethod02046();
         float localValue5 = this.w.internalMethod02046();
         float localValue6 = this.h.internalMethod02046();
         if (!(localValue5 <= 0.0F) && !(localValue6 <= 0.0F)) {
            float localValue7 = this.backdropRadius();
            float localValue8 = Math.min(localValue7 > 0.0F ? localValue7 : 7.0F, Math.min(localValue5, localValue6) / 2.0F);
            localValue1.drawSquircle(
               localValue3, localValue4, localValue5, localValue6, 3.0F, CornerRadii.internalMethod03908(localValue8), ThemeColors.internalMethod07738().mulAlpha(0.85F * localValue2)
            );
         }
      }
   }

   protected void drawSelf(UiRenderContext localValue1, float localValue2) {
   }

   protected void drawChildren(UiRenderContext localValue1, float localValue2) {
   }

   public boolean contains(float localValue1, float localValue2) {
      float localValue3 = this.x.internalMethod02046();
      float localValue4 = this.y.internalMethod02046();
      float localValue5 = this.h.internalMethod02046();
      if (this.collapse && this.appear.internalMethod02046() < 0.999F) {
         localValue5 = Math.max(0.0F, this.desiredH());
      }

      return localValue1 >= localValue3 && localValue1 <= localValue3 + this.w.internalMethod02046() && localValue2 >= localValue4 && localValue2 <= localValue4 + localValue5;
   }

   private UiNode dragRoot() {
      UiNode localValue1 = this;

      while (localValue1.parent != null) {
         localValue1 = localValue1.parent;
      }

      return localValue1;
   }

   private void applyDrag(float localValue1, float localValue2) {
      if (this.dragAxis == DragConstraint.internalField0631 || this.dragAxis == DragConstraint.internalField1248) {
         this.x.internalMethod03759(this.dragStartX + localValue1 - this.dragStartMx);
      }

      if (this.dragAxis == DragConstraint.internalField0631 || this.dragAxis == DragConstraint.internalField1249) {
         this.y.internalMethod03759(this.dragStartY + localValue2 - this.dragStartMy);
      }
   }

   public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
      if (this.interactive && this.inFlow() && this.contains(localValue1, localValue2)) {
         NumberInputField.internalMethod05283(this);
         boolean localValue4 = this.dragMode != DragConstraint.internalField0630 && localValue3 == MouseButton.internalField0102;
         if (localValue4) {
            UiNode localValue5 = this.dragRoot();
            localValue5.dragging = true;
            localValue5.dragAxis = this.dragMode;
            localValue5.dragStartMx = localValue1;
            localValue5.dragStartMy = localValue2;
            localValue5.dragStartX = localValue5.x.internalMethod02046();
            localValue5.dragStartY = localValue5.y.internalMethod02046();
            localValue5.manuallyPositioned = true;
            ANY_DRAGGING = true;
         }

         this.pressed = true;
         this.pressedButton = localValue3;

         try {
            if (localValue3 == MouseButton.internalField0102 && this.leftClick != null) {
               this.leftClick.run();
            }

            if (this.buttonClick != null) {
               this.buttonClick.accept(localValue3);
            }

            if (this.posClick != null) {
               this.posClick.onClick(localValue3, localValue1, localValue2);
            }
         } catch (Throwable localValue6) {
            RockstarClient.internalField0572.error("[ui] click handler failed on {}", this.getClass().getSimpleName(), localValue6);
         }

         return localValue4 || this.leftClick != null || this.buttonClick != null || this.posClick != null;
      } else {
         return false;
      }
   }

   public void mouseReleased(float localValue1, float localValue2, MouseButton localValue3) {
      this.pressed = false;
      this.pressedButton = null;
      if (this.dragging) {
         this.dragging = false;
         ANY_DRAGGING = false;
      }
   }

   private void dropLostPress(float localValue1, float localValue2) {
      MouseButton localValue3 = this.pressed ? this.pressedButton : (this.dragging ? MouseButton.internalField0102 : null);
      if (localValue3 != null) {
         long localValue4 = MinecraftClient.getInstance().getWindow().getHandle();
         if (GLFW.glfwGetMouseButton(localValue4, localValue3.internalMethod02957()) != 1) {
            this.mouseReleased(localValue1, localValue2, localValue3);
         }
      }
   }

   public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
      return false;
   }

   public boolean keyPressed(int localValue1, int localValue2, int localValue3) {
      return false;
   }

   public boolean keyReleased(int localValue1, int localValue2, int localValue3) {
      return false;
   }

   public boolean charTyped(char localValue1, int localValue2) {
      return false;
   }

   public static enum InternalType0146 {
      internalField0259,
      internalField0260,
      internalField1091,
      internalField1090,
      internalField1089;
   }

   public interface InternalType0352 {
      void onClick(MouseButton localValue1, float localValue2, float localValue3);
   }

   public interface InternalType0353 {
      float get();
   }
}
