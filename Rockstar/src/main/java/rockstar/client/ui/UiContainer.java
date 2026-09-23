package rockstar.client.ui;





import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.util.math.MatrixStack;
import pyrock.utility.render.ColorRGBA;

public class UiContainer extends UiNode {
   private final List<UiNode> internalField0416 = new ArrayList<>();
   private FlexDirection internalField0629;
   private float internalField0205;
   private Insets internalField0910;
   private int internalField0227;
   private boolean internalField0277;
   private TextAlignment internalField0622;
   private LayoutAlignment internalField0911;
   private boolean internalField0276;
   private boolean internalField1099;
   private float internalField0206;
   private float internalField1048;
   private float internalField1047;
   private float internalField1049;
   private Function<UiContainer, ColorRGBA> internalField0571;
   private CornerRadii internalField0098;
   private float internalField1046;
   private UiContainer.InternalType0354 internalField0340;
   private float internalField1456;
   private float internalField1457;
   private boolean internalField1100;
   private float internalField1458;
   private float internalField1459;
   private float internalField1460;
   private float internalField1461;
   private float internalField1462;
   private float internalField1455;
   private float internalField1723;
   private float internalField1731;
   private final ScrollController internalField0915;

   public UiContainer() {
      this.internalField0629 = FlexDirection.internalField0629;
      this.internalField0205 = 0.0F;
      this.internalField0910 = Insets.internalField0910;
      this.internalField0227 = 1;
      this.internalField0277 = false;
      this.internalField0622 = TextAlignment.internalField1243;
      this.internalField0911 = LayoutAlignment.internalField0911;
      this.internalField0276 = false;
      this.internalField1099 = false;
      this.internalField0206 = 0.0F;
      this.internalField1048 = 0.0F;
      this.internalField1047 = 0.0F;
      this.internalField1049 = 24.0F;
      this.internalField0098 = CornerRadii.internalField0098;
      this.internalField1046 = 0.0F;
      this.internalField1458 = 0.0F;
      this.internalField1459 = 0.0F;
      this.internalField1460 = 0.0F;
      this.internalField1461 = 0.0F;
      this.internalField0915 = new ScrollController(this);
   }

   public UiContainer internalMethod01192(FlexDirection localValue1) {
      this.internalField0629 = localValue1 == null ? FlexDirection.internalField0629 : localValue1;
      return this;
   }

   public UiContainer internalMethod01863() {
      return this.internalMethod01192(FlexDirection.internalField0629);
   }

   public UiContainer internalMethod05895() {
      return this.internalMethod01192(FlexDirection.internalField1246);
   }

   public UiContainer internalMethod03062(float localValue1) {
      this.internalField0205 = localValue1;
      return this;
   }

   public UiContainer internalMethod03514(Insets localValue1) {
      this.internalField0910 = localValue1 == null ? Insets.internalField0910 : localValue1;
      return this;
   }

   public UiContainer internalMethod07351(float localValue1) {
      this.internalField0910 = Insets.internalMethod00172(localValue1);
      return this;
   }

   public UiContainer internalMethod02146(float localValue1, float localValue2) {
      this.internalField0910 = Insets.internalMethod05266(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod02070(int localValue1) {
      this.internalField0227 = Math.max(1, localValue1);
      return this;
   }

   public UiContainer internalMethod07971() {
      this.internalField0277 = true;
      return this;
   }

   public UiContainer internalMethod01509(boolean localValue1) {
      this.internalField0277 = localValue1;
      return this;
   }

   public UiContainer internalMethod01855(TextAlignment localValue1) {
      this.internalField0622 = localValue1 == null ? TextAlignment.internalField1243 : localValue1;
      return this;
   }

   public UiContainer internalMethod07607(LayoutAlignment localValue1) {
      this.internalField0911 = localValue1 == null ? LayoutAlignment.internalField0911 : localValue1;
      return this;
   }

   public UiContainer internalMethod08755() {
      this.internalField0276 = true;
      return this;
   }

   public UiContainer internalMethod05747(boolean localValue1) {
      this.internalField0276 = localValue1;
      return this;
   }

   public UiContainer internalMethod08881(float localValue1) {
      this.internalField1049 = localValue1;
      return this;
   }

   public UiContainer internalMethod08791() {
      this.internalField1099 = true;
      return this;
   }

   public UiContainer internalMethod08556(boolean localValue1) {
      this.internalField1099 = localValue1;
      return this;
   }

   public UiContainer internalMethod08163(float localValue1) {
      this.internalField1047 = Math.max(0.0F, localValue1);
      return this;
   }

   public UiContainer internalMethod01416(ScrollPolicy localValue1) {
      this.internalField0915.internalMethod02353(localValue1);
      return this;
   }

   public UiContainer internalMethod05391(Consumer<ScrollController> localValue1) {
      if (localValue1 != null) {
         localValue1.accept(this.internalField0915);
      }

      return this;
   }

   public ScrollController internalMethod05715() {
      return this.internalField0915;
   }

   public UiContainer internalMethod06812(Function<UiContainer, ColorRGBA> localValue1) {
      this.internalField0571 = localValue1;
      return this;
   }

   public UiContainer internalMethod02303(ColorRGBA localValue1) {
      this.internalField0571 = localValue1x -> localValue1;
      return this;
   }

   public UiContainer internalMethod09018(float localValue1) {
      this.internalField0098 = CornerRadii.internalMethod03908(localValue1);
      return this;
   }

   public UiContainer internalMethod06083(CornerRadii localValue1) {
      this.internalField0098 = localValue1 == null ? CornerRadii.internalField0098 : localValue1;
      return this;
   }

   public UiContainer internalMethod08487(float localValue1) {
      this.internalField1046 = localValue1;
      return this;
   }

   @Override
   protected float backdropRadius() {
      return this.internalField0098.internalMethod05337();
   }

   @Override
   protected CornerRadii shapeRadius() {
      return this.internalField0098;
   }

   @Override
   protected float shapeSquircle() {
      return this.internalField1046;
   }

   public UiContainer internalMethod09736(float localValue1) {
      super.blur(localValue1);
      return this;
   }

   public UiContainer internalMethod03817(float localValue1, ColorRGBA localValue2) {
      super.blur(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod08011() {
      super.glass();
      return this;
   }

   public UiContainer internalMethod00480(float localValue1, boolean localValue2) {
      super.glass(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod07178(UiContainer.InternalType0354 localValue1) {
      this.internalField0340 = localValue1;
      return this;
   }

   public List<UiNode> internalMethod01401() {
      return this.internalField0416;
   }

   public UiContainer internalMethod03754(Motion localValue1) {
      super.motion(localValue1);
      return this;
   }

   public UiContainer internalMethod09339(float localValue1) {
      super.width(localValue1);
      return this;
   }

   public UiContainer internalMethod09266(float localValue1) {
      super.height(localValue1);
      return this;
   }

   public UiContainer internalMethod03995(float localValue1, float localValue2) {
      super.size(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod08043(float localValue1, float localValue2) {
      super.minSize(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod08392(float localValue1, float localValue2) {
      super.maxSize(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod09554(float localValue1) {
      super.minWidth(localValue1);
      return this;
   }

   public UiContainer internalMethod09890(float localValue1) {
      super.minHeight(localValue1);
      return this;
   }

   public UiContainer internalMethod09609() {
      super.fillWidth();
      return this;
   }

   public UiContainer internalMethod09186() {
      super.fillHeight();
      return this;
   }

   public UiContainer internalMethod09213() {
      super.fill();
      return this;
   }

   public UiContainer internalMethod08296(float localValue1, float localValue2) {
      super.at(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod01619(UiTransition localValue1) {
      super.enter(localValue1);
      return this;
   }

   public UiContainer internalMethod02991(UiTransition localValue1) {
      super.exit(localValue1);
      return this;
   }

   public UiContainer internalMethod07914(UiTransition localValue1) {
      super.transition(localValue1);
      return this;
   }

   public UiContainer internalMethod05305(Motion localValue1) {
      super.lifeMotion(localValue1);
      return this;
   }

   public UiContainer internalMethod05690(Runnable localValue1) {
      super.onClick(localValue1);
      return this;
   }

   public UiContainer internalMethod02525(Consumer<MouseButton> localValue1) {
      super.onClick(localValue1);
      return this;
   }

   public UiContainer internalMethod05382(UiNode.InternalType0352 localValue1) {
      super.onClick(localValue1);
      return this;
   }

   public UiContainer internalMethod07853(boolean localValue1) {
      super.interactive(localValue1);
      return this;
   }

   public UiContainer internalMethod09625() {
      super.modal();
      return this;
   }

   public UiContainer internalMethod03715(DragConstraint localValue1) {
      super.draggable(localValue1);
      return this;
   }

   public UiContainer internalMethod08928(boolean localValue1) {
      super.draggable(localValue1);
      return this;
   }

   public UiContainer internalMethod04332(CursorType localValue1) {
      super.cursor(localValue1);
      return this;
   }

   public UiContainer internalMethod08336(Motion localValue1) {
      super.hoverMotion(localValue1);
      return this;
   }

   public UiContainer internalMethod09784() {
      super.center();
      return this;
   }

   public UiContainer internalMethod09378() {
      super.centerX();
      return this;
   }

   public UiContainer internalMethod09408() {
      super.centerY();
      return this;
   }

   public UiContainer internalMethod03855(BooleanSupplier localValue1) {
      super.visibleWhen(localValue1);
      return this;
   }

   public UiContainer internalMethod05899(BooleanSupplier localValue1, Motion localValue2) {
      super.visibleWhen(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod06712(BooleanSupplier localValue1, Easing localValue2, long localValue3) {
      super.visibleWhen(localValue1, localValue2, localValue3);
      return this;
   }

   public UiContainer internalMethod01850(BooleanSupplier localValue1) {
      super.snapPosition(localValue1);
      return this;
   }

   public UiContainer internalMethod09801() {
      super.snapPosition();
      return this;
   }

   @Override
   public void snapSubtree() {
      super.snapSubtree();

      for (UiNode localValue2 : this.internalField0416) {
         localValue2.snapSubtree();
      }
   }

   public UiContainer internalMethod10026() {
      super.animatePosition();
      return this;
   }

   public UiContainer internalMethod08226(BooleanSupplier localValue1) {
      super.sticky(localValue1);
      return this;
   }

   public UiContainer internalMethod09932() {
      super.sticky();
      return this;
   }

   public UiContainer internalMethod09936() {
      super.collapse();
      return this;
   }

   public UiContainer internalMethod08193(boolean localValue1) {
      super.collapse(localValue1);
      return this;
   }

   public UiContainer internalMethod06451(String localValue1, BooleanSupplier localValue2) {
      super.bind(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod04310(String localValue1, UiNode.InternalType0353 localValue2) {
      super.bind(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod01258(String localValue1, BooleanSupplier localValue2, Motion localValue3) {
      super.bind(localValue1, localValue2, localValue3);
      return this;
   }

   public UiContainer internalMethod01647(String localValue1, UiNode.InternalType0353 localValue2, Motion localValue3) {
      super.bind(localValue1, localValue2, localValue3);
      return this;
   }

   public UiContainer internalMethod01658(String localValue1, BooleanSupplier localValue2, long localValue3) {
      super.bind(localValue1, localValue2, localValue3);
      return this;
   }

   public UiContainer internalMethod07452(String localValue1, UiNode.InternalType0353 localValue2, long localValue3) {
      super.bind(localValue1, localValue2, localValue3);
      return this;
   }

   public UiContainer internalMethod05214(String localValue1, Motion localValue2) {
      super.signalMotion(localValue1, localValue2);
      return this;
   }

   public UiContainer internalMethod03907(UiNode localValue1) {
      if (localValue1 != null && localValue1 != this) {
         localValue1.parent = this;
         localValue1.beginEnter(0.0F);
         this.internalField0416.add(localValue1);
         return this;
      } else {
         return this;
      }
   }

   public UiContainer internalMethod07108(List<? extends UiNode> localValue1) {
      for (UiNode localValue3 : localValue1) {
         this.internalMethod03907(localValue3);
      }

      return this;
   }

   public UiContainer internalMethod06568(UiNode localValue1) {
      if (localValue1 != null && this.internalField0416.contains(localValue1)) {
         localValue1.beginExit(0.0F);
      }

      return this;
   }

   public void internalMethod03628() {
      for (UiNode localValue2 : this.internalField0416) {
         localValue2.beginExit(0.0F);
      }
   }

   public UiContainer internalMethod06213(List<? extends UiNode> localValue1) {
      for (UiNode localValue3 : this.internalField0416) {
         localValue3.parent = null;
      }

      this.internalField0416.clear();
      this.internalField0206 = this.internalField1048 = 0.0F;
      if (localValue1 != null) {
         this.internalMethod07108(localValue1);
      }

      return this;
   }

   public UiContainer internalMethod07849(List<? extends UiNode> localValue1) {
      IdentityHashMap localValue2 = new IdentityHashMap();

      for (UiNode localValue4 : localValue1) {
         localValue2.put(localValue4, Boolean.TRUE);
      }

      ArrayList localValue10 = new ArrayList();
      int localValue11 = 0;

      for (UiNode localValue6 : this.internalField0416) {
         if (localValue6.phase() != UiNode.InternalType0146.internalField1090 && !localValue2.containsKey(localValue6)) {
            localValue11++;
         }
      }

      int localValue12 = 0;

      for (UiNode localValue7 : this.internalField0416) {
         if (localValue7.phase() != UiNode.InternalType0146.internalField1090 && !localValue2.containsKey(localValue7)) {
            localValue7.beginExit(this.internalMethod02537(localValue12++, localValue11));
            localValue10.add(localValue7);
         }
      }

      IdentityHashMap localValue14 = new IdentityHashMap();

      for (UiNode localValue8 : this.internalField0416) {
         localValue14.put(localValue8, Boolean.TRUE);
      }

      int localValue16 = localValue1.size();

      for (int localValue17 = 0; localValue17 < localValue16; localValue17++) {
         UiNode localValue9 = (UiNode)localValue1.get(localValue17);
         if (localValue14.containsKey(localValue9)) {
            if (localValue9.phase() == UiNode.InternalType0146.internalField1090 || localValue9.phase() == UiNode.InternalType0146.internalField1089) {
               localValue9.parent = this;
               localValue9.beginEnter(this.internalMethod02537(localValue17, localValue16));
            } else if (localValue9.phase() == UiNode.InternalType0146.internalField1091) {
               localValue9.beginEnter(this.internalMethod02537(localValue17, localValue16));
            }

            localValue10.add(localValue9);
         } else {
            localValue9.parent = this;
            localValue9.beginEnter(this.internalMethod02537(localValue17, localValue16));
            localValue10.add(localValue9);
         }
      }

      this.internalField0416.clear();
      this.internalField0416.addAll(localValue10);
      this.internalField1048 = 0.0F;
      return this;
   }

   private float internalMethod02537(int localValue1, int localValue2) {
      return !(this.internalField1047 <= 0.0F) && localValue2 > 1 ? this.internalField1047 * ((float)localValue1 / (localValue2 - 1)) : 0.0F;
   }

   @Override
   public float desiredW() {
      return this.explicitW ? this.clampW(this.prefW) : this.clampW(this.internalField1458 >= 0.0F ? this.internalMethod09246() : this.prefW);
   }

   @Override
   protected float rawDesiredH() {
      return this.explicitH ? this.clampH(this.prefH) : this.clampH(this.internalMethod09678());
   }

   private float internalMethod09246() {
      return (this.internalField0629.internalMethod05114() ? this.internalField1459 : this.internalField1458) + this.internalField0910.internalMethod05308();
   }

   private float internalMethod09678() {
      return (this.internalField0629.internalMethod05114() ? this.internalField1458 : this.internalField1459) + this.internalField0910.internalMethod05311();
   }

   @Override
   protected void measure() {
      if (this.internalField1099) {
         this.internalMethod08139();
      } else {
         boolean localValue1 = this.internalField0629.internalMethod05114();
         int localValue2 = Math.max(1, this.internalField0227);
         ArrayList localValue3 = new ArrayList(this.internalField0416.size());

         for (UiNode localValue5 : this.internalField0416) {
            if (localValue5.phase() != UiNode.InternalType0146.internalField1089) {
               localValue5.measure();
               if (localValue5.inFlow()) {
                  localValue3.add(localValue5);
               }
            }
         }

         int localValue13 = localValue3.size();
         if (localValue13 == 0) {
            this.internalField1458 = 0.0F;
            this.internalField1459 = 0.0F;
         } else if (this.internalField0277) {
            float localValue15 = localValue1
               ? this.h.internalMethod02046() - this.internalField0910.internalMethod05311()
               : this.w.internalMethod02046() - this.internalField0910.internalMethod05308();
            if (localValue15 <= 0.0F) {
               localValue15 = Float.MAX_VALUE;
            }

            this.internalMethod03518(localValue3, localValue1, localValue15, false, 0.0F, 0.0F);
         } else {
            int localValue14 = (localValue13 + localValue2 - 1) / localValue2;
            float[] localValue6 = new float[localValue14];
            float localValue7 = 0.0F;

            for (int localValue8 = 0; localValue8 < localValue13; localValue8++) {
               UiNode localValue9 = (UiNode)localValue3.get(localValue8);
               int localValue10 = localValue8 / localValue2;
               float localValue11 = localValue1 ? localValue9.desiredH() : localValue9.desiredW();
               float localValue12 = localValue1 ? localValue9.desiredW() : localValue9.desiredH();
               localValue6[localValue10] = Math.max(localValue6[localValue10], localValue11);
               localValue7 = Math.max(localValue7, localValue12);
            }

            float localValue16 = (localValue14 - 1) * this.internalField0205;

            for (float localValue20 : localValue6) {
               localValue16 += localValue20;
            }

            this.internalField1458 = localValue16;
            this.internalField1459 = localValue2 * localValue7 + (localValue2 - 1) * this.internalField0205;
         }
      }
   }

   @Override
   protected void onTick(float localValue1, float localValue2, float localValue3) {
      this.internalField0416.removeIf(localValue0 -> localValue0.phase() == UiNode.InternalType0146.internalField1090);
      if (this.phase == UiNode.InternalType0146.internalField1089) {
         this.internalField1100 = false;
      } else {
         if (this.internalField1100) {
            float localValue4 = this.x.internalMethod02046() - this.internalField1456;
            float localValue5 = this.y.internalMethod02046() - this.internalField1457;
            if (localValue4 != 0.0F || localValue5 != 0.0F) {
               for (UiNode localValue7 : this.internalField0416) {
                  localValue7.rideWith(localValue4, localValue5);
               }
            }
         }

         this.internalField1456 = this.x.internalMethod02046();
         this.internalField1457 = this.y.internalMethod02046();
         this.internalField1100 = true;
         if (this.inFlow()) {
            this.internalMethod08155();
         }

         boolean localValue13 = this.epochSnap();
         if (!this.explicitW && !this.fillW) {
            float localValue14 = this.clampW(this.internalMethod09246());
            if (localValue13) {
               this.w.internalMethod03759(localValue14);
            } else {
               this.w.internalMethod03690(localValue14);
            }
         }

         if (!this.explicitH && !this.fillH) {
            float localValue15 = this.clampH(this.internalMethod09678());
            if (localValue13) {
               this.h.internalMethod03759(localValue15);
            } else {
               this.h.internalMethod03690(localValue15);
            }
         }

         if (this.internalField0276) {
            float localValue16 = Math.max(0.0F, this.internalField1458 - this.internalField1460);
            this.internalField1048 = Math.max(0.0F, Math.min(this.internalField1048, localValue16));
            if (localValue13) {
               this.internalField0206 = this.internalField1048;
            } else {
               this.internalField0206 = this.internalField0206 + (this.internalField1048 - this.internalField0206) * Math.min(1.0F, localValue1 * 0.02F);
            }

            if (Math.abs(this.internalField1048 - this.internalField0206) < 0.05F) {
               this.internalField0206 = this.internalField1048;
            }
         } else {
            this.internalField0206 = this.internalField1048 = 0.0F;
         }

         boolean localValue17 = this.internalField0629.internalMethod05114();
         float localValue18 = 0.0F;

         for (UiNode localValue8 : this.internalField0416) {
            if (localValue8.inFlow() && localValue8.isSticky()) {
               localValue18 += localValue17 ? localValue8.h() : localValue8.w();
            }
         }

         this.internalField1461 = localValue18;
         float localValue20 = localValue2;
         float localValue21 = localValue3;
         if (localValue17) {
            localValue21 = localValue3 + this.internalField0206;
         } else {
            localValue20 = localValue2 + this.internalField0206;
         }

         boolean localValue9 = this.hoverable && this.inFlow();
         int localValue10 = this.internalMethod03627();

         for (int localValue11 = 0; localValue11 < this.internalField0416.size(); localValue11++) {
            UiNode localValue12 = this.internalField0416.get(localValue11);
            localValue12.hoverable = localValue9 && localValue11 >= localValue10 && this.internalMethod03304(localValue12, localValue2, localValue3);
            if (localValue12.isSticky()) {
               localValue12.tick(localValue1, localValue2, localValue3);
            } else {
               localValue12.tick(localValue1, localValue20, localValue21);
            }
         }

         this.internalField0915.internalMethod04465(localValue1, localValue2, localValue3);
      }
   }

   private int internalMethod03627() {
      for (int localValue1 = this.internalField0416.size() - 1; localValue1 >= 0; localValue1--) {
         UiNode localValue2 = this.internalField0416.get(localValue1);
         if (localValue2.isModal() && localValue2.inFlow()) {
            return localValue1;
         }
      }

      return 0;
   }

   FlexDirection internalMethod01862() {
      return this.internalField0629;
   }

   boolean internalMethod03629() {
      return this.internalField0276 && this.internalField1458 > this.internalField1460 + 0.5F;
   }

   float internalMethod03626() {
      return this.internalField1462;
   }

   float internalMethod03630() {
      return this.internalField1455;
   }

   float internalMethod08624() {
      return this.internalField1723;
   }

   float internalMethod08625() {
      return this.internalField1731;
   }

   float internalMethod08638() {
      return this.internalField1460;
   }

   float internalMethod08639() {
      return this.internalField1461;
   }

   float internalMethod09418() {
      return this.internalField1458;
   }

   float internalMethod09419() {
      return this.internalField0206;
   }

   float internalMethod09428() {
      return Math.max(0.0F, this.internalField1458 - this.internalField1460);
   }

   void internalMethod04955(float localValue1) {
      float localValue2 = this.internalMethod09428();
      this.internalField0206 = Math.max(0.0F, Math.min(localValue1, localValue2));
      this.internalField1048 = this.internalField0206;
   }

   public float internalMethod09429() {
      return this.internalField0206;
   }

   public void internalMethod03631() {
      this.internalField1048 = 0.0F;
      this.internalField0915.internalMethod05171();
   }

   public void internalMethod05009(float localValue1) {
      if (this.internalField0276) {
         this.internalField0206 = this.internalField1048 = Math.max(0.0F, localValue1);
         this.internalField0915.internalMethod05171();
      }
   }

   public void internalMethod01272(UiNode localValue1, float localValue2) {
      if (this.internalField0276 && localValue1 != null) {
         boolean localValue3 = this.internalField0629.internalMethod05114();
         float localValue4 = localValue3 ? this.y.internalMethod02046() + this.internalField0910.internalField0205 : this.x.internalMethod02046() + this.internalField0910.internalField1047;
         float localValue5 = (localValue3 ? localValue1.y() : localValue1.x()) - localValue4;
         this.internalField1048 = Math.max(0.0F, Math.min(localValue5 - this.internalField1461 - localValue2, this.internalMethod09428()));
         this.internalField0915.internalMethod05171();
      }
   }

   private boolean internalMethod03304(UiNode localValue1, float localValue2, float localValue3) {
      if (!this.internalField0276) {
         return true;
      } else {
         boolean localValue4 = this.internalField0629.internalMethod05114();
         float localValue5 = localValue1.isSticky() ? 0.0F : this.internalField1461;
         float localValue6 = this.internalField1462 + (localValue4 ? 0.0F : localValue5);
         float localValue7 = this.internalField1455 + (localValue4 ? localValue5 : 0.0F);
         float localValue8 = this.internalField1723 - (localValue4 ? 0.0F : localValue5);
         float localValue9 = this.internalField1731 - (localValue4 ? localValue5 : 0.0F);
         return localValue2 >= localValue6 && localValue2 <= localValue6 + localValue8 && localValue3 >= localValue7 && localValue3 <= localValue7 + localValue9;
      }
   }

   private void internalMethod08139() {
      float localValue1 = 0.0F;
      float localValue2 = 0.0F;

      for (UiNode localValue4 : this.internalField0416) {
         if (localValue4.phase() != UiNode.InternalType0146.internalField1089) {
            localValue4.measure();
            if (localValue4.inFlow()) {
               localValue1 = Math.max(localValue1, localValue4.desiredH());
               localValue2 = Math.max(localValue2, localValue4.desiredW());
            }
         }
      }

      this.internalField1458 = localValue1;
      this.internalField1459 = localValue2;
   }

   private void internalMethod08141() {
      float localValue1 = this.x.internalMethod02046() + this.internalField0910.internalField1047;
      float localValue2 = this.y.internalMethod02046() + this.internalField0910.internalField0205;
      float localValue3 = Math.max(0.0F, this.w.internalMethod02046() - this.internalField0910.internalMethod05308());
      float localValue4 = Math.max(0.0F, this.h.internalMethod02046() - this.internalField0910.internalMethod05311());
      this.internalField1460 = localValue4;
      this.internalField1462 = localValue1;
      this.internalField1455 = localValue2;
      this.internalField1723 = localValue3;
      this.internalField1731 = localValue4;
      boolean localValue5 = this.internalMethod04956(true);
      boolean localValue6 = this.internalMethod04956(false);
      float localValue7 = 0.0F;
      float localValue8 = 0.0F;

      for (UiNode localValue10 : this.internalField0416) {
         if (localValue10.inFlow()) {
            float localValue11 = !localValue10.fillW && this.internalField0622 != TextAlignment.internalField1243
               ? (localValue5 ? localValue10.desiredW() : Math.min(localValue3, localValue10.desiredW()))
               : localValue3;
            float localValue12 = localValue10.fillH ? localValue4 : (localValue6 ? localValue10.desiredH() : Math.min(localValue4, localValue10.desiredH()));
            if (this.snappedThisFrame()) {
               localValue10.forceSnap();
            }

            localValue10.setSlot(localValue1, localValue2, localValue11, localValue12);
            localValue7 = Math.max(localValue7, localValue12);
            localValue8 = Math.max(localValue8, localValue11);
         }
      }

      this.internalField1458 = localValue7;
      this.internalField1459 = localValue8;
   }

   private boolean internalMethod04956(boolean localValue1) {
      return localValue1 ? !this.explicitW && !this.fillW && this.maxW == Float.MAX_VALUE : !this.explicitH && !this.fillH && this.maxH == Float.MAX_VALUE;
   }

   private boolean internalMethod05010(boolean localValue1) {
      return localValue1 ? !this.explicitH && !this.fillH && this.maxH == Float.MAX_VALUE : !this.explicitW && !this.fillW && this.maxW == Float.MAX_VALUE;
   }

   private void internalMethod08155() {
      if (this.internalField1099) {
         this.internalMethod08141();
      } else {
         boolean localValue1 = this.internalField0629.internalMethod05114();
         float localValue2 = this.x.internalMethod02046() + this.internalField0910.internalField1047;
         float localValue3 = this.y.internalMethod02046() + this.internalField0910.internalField0205;
         float localValue4 = Math.max(0.0F, this.w.internalMethod02046() - this.internalField0910.internalMethod05308());
         float localValue5 = Math.max(0.0F, this.h.internalMethod02046() - this.internalField0910.internalMethod05311());
         float localValue6 = localValue1 ? localValue5 : localValue4;
         float localValue7 = localValue1 ? localValue4 : localValue5;
         this.internalField1460 = localValue6;
         this.internalField1462 = localValue2;
         this.internalField1455 = localValue3;
         this.internalField1723 = localValue4;
         this.internalField1731 = localValue5;
         int localValue8 = Math.max(1, this.internalField0227);
         float localValue9 = (localValue7 - (localValue8 - 1) * this.internalField0205) / localValue8;
         if (localValue9 < 0.0F) {
            localValue9 = 0.0F;
         }

         ArrayList localValue10 = new ArrayList(this.internalField0416.size());

         for (UiNode localValue12 : this.internalField0416) {
            if (localValue12.inFlow()) {
               localValue10.add(localValue12);
            }
         }

         int localValue43 = localValue10.size();
         if (localValue43 == 0) {
            this.internalField1458 = 0.0F;
            this.internalField1459 = 0.0F;
         } else if (this.internalField0277) {
            this.internalMethod03518(localValue10, localValue1, localValue6, true, localValue2, localValue3);
         } else {
            int localValue44 = (localValue43 + localValue8 - 1) / localValue8;
            float[] localValue13 = new float[localValue44];
            float localValue14 = 0.0F;

            for (int localValue15 = 0; localValue15 < localValue43; localValue15++) {
               UiNode localValue16 = (UiNode)localValue10.get(localValue15);
               int localValue17 = localValue15 / localValue8;
               float localValue18 = localValue1 ? localValue16.desiredH() : localValue16.desiredW();
               float localValue19 = localValue1 ? localValue16.desiredW() : localValue16.desiredH();
               localValue13[localValue17] = Math.max(localValue13[localValue17], localValue18);
               localValue14 = Math.max(localValue14, localValue19);
            }

            float localValue45 = (localValue44 - 1) * this.internalField0205;

            for (float localValue53 : localValue13) {
               localValue45 += localValue53;
            }

            this.internalField1458 = localValue45;
            this.internalField1459 = localValue8 * (this.internalField0622 == TextAlignment.internalField1243 ? localValue9 : localValue14) + (localValue8 - 1) * this.internalField0205;
            if (!this.explicitW && !this.fillW && !this.explicitH && !this.fillH) {
               this.internalField1459 = localValue8 * localValue14 + (localValue8 - 1) * this.internalField0205;
            }

            float localValue47 = Math.max(0.0F, localValue6 - localValue45);
            int localValue49 = 0;
            if (localValue8 == 1) {
               for (UiNode localValue54 : (Iterable<UiNode>)(Iterable<?>)localValue10) {
                  if (localValue1 ? localValue54.fillH : localValue54.fillW) {
                     localValue49++;
                  }
               }
            }

            float localValue52 = localValue49 > 0 && localValue47 > 0.0F ? localValue47 / localValue49 : 0.0F;
            if (localValue52 > 0.0F) {
               localValue47 = 0.0F;
            }

            float localValue55 = 1.0F;
            if (localValue8 == 1 && localValue49 > 0 && localValue45 > localValue6 && !this.internalMethod05010(localValue1)) {
               float localValue20 = 0.0F;

               for (UiNode localValue22 : (Iterable<UiNode>)(Iterable<?>)localValue10) {
                  if (localValue1 ? localValue22.fillH : localValue22.fillW) {
                     localValue20 += localValue1 ? localValue22.desiredH() : localValue22.desiredW();
                  }
               }

               float localValue57 = localValue45 - localValue6;
               if (localValue20 > 0.0F) {
                  localValue55 = Math.max(0.0F, (localValue20 - localValue57) / localValue20);
                  this.internalField1458 = localValue45 - Math.min(localValue57, localValue20);
               }
            }

            float localValue56 = 0.0F;
            float localValue58 = 0.0F;
            switch (this.internalField0911) {
               case internalField0912:
                  localValue56 = localValue47 / 2.0F;
                  break;
               case internalField1380:
                  localValue56 = localValue47;
                  break;
               case internalField1377:
                  localValue58 = localValue44 > 1 ? localValue47 / (localValue44 - 1) : 0.0F;
                  break;
               case internalField1378:
                  localValue58 = localValue47 / localValue44;
                  localValue56 = localValue58 / 2.0F;
                  break;
               case internalField1379:
                  localValue58 = localValue47 / (localValue44 + 1);
                  localValue56 = localValue58;
            }

            boolean localValue59 = this.internalMethod04956(localValue1);
            float localValue23 = localValue56;

            for (int localValue24 = 0; localValue24 < localValue43; localValue24++) {
               UiNode localValue25 = (UiNode)localValue10.get(localValue24);
               int localValue26 = localValue24 / localValue8;
               int localValue27 = localValue24 % localValue8;
               boolean localValue28 = localValue1 ? localValue25.fillW : localValue25.fillH;
               boolean localValue29 = localValue1 ? localValue25.fillH : localValue25.fillW;
               float localValue30 = localValue1 ? localValue25.desiredW() : localValue25.desiredH();
               float localValue31 = localValue27 * (localValue9 + this.internalField0205);
               float localValue32 = !localValue28 && this.internalField0622 != TextAlignment.internalField1243 ? (localValue59 ? localValue30 : Math.min(localValue9, localValue30)) : localValue9;

               float localValue33 = switch (this.internalField0622) {
                  case internalField0621 -> (localValue9 - localValue32) / 2.0F;
                  case internalField1242 -> localValue9 - localValue32;
                  default -> 0.0F;
               };
               float localValue34 = localValue31 + localValue33;
               float localValue35 = localValue29 ? localValue52 : 0.0F;
               float localValue36 = localValue29 ? localValue13[localValue26] * localValue55 : localValue13[localValue26];
               float localValue37 = localValue29 ? localValue36 + localValue35 : (localValue1 ? localValue25.desiredH() : localValue25.desiredW());
               float localValue38 = localValue23;
               if (this.internalField0629.internalMethod09096()) {
                  localValue38 = localValue6 - localValue23 - localValue37;
               }

               float localValue39;
               float localValue40;
               float localValue41;
               float localValue42;
               if (localValue1) {
                  localValue39 = localValue2 + localValue34;
                  localValue40 = localValue3 + localValue38;
                  localValue41 = localValue32;
                  localValue42 = localValue37;
               } else {
                  localValue39 = localValue2 + localValue38;
                  localValue40 = localValue3 + localValue34;
                  localValue41 = localValue37;
                  localValue42 = localValue32;
               }

               if (this.snappedThisFrame()) {
                  localValue25.forceSnap();
               }

               localValue25.setSlot(localValue39, localValue40, localValue41, localValue42);
               if (localValue27 == localValue8 - 1 || localValue24 == localValue43 - 1) {
                  localValue23 += localValue36 + localValue35 + this.internalField0205 + localValue58;
               }
            }
         }
      }
   }

   private void internalMethod03518(List<UiNode> localValue1, boolean localValue2, float localValue3, boolean localValue4, float localValue5, float localValue6) {
      float localValue7 = 0.0F;
      float localValue8 = 0.0F;
      float localValue9 = 0.0F;
      float localValue10 = 0.0F;
      boolean localValue11 = true;

      for (UiNode localValue13 : localValue1) {
         float localValue14 = localValue2 ? localValue13.desiredH() : localValue13.desiredW();
         float localValue15 = localValue2 ? localValue13.desiredW() : localValue13.desiredH();
         float localValue16 = localValue11 ? 0.0F : localValue7 + this.internalField0205;
         if (!localValue11 && localValue16 + localValue14 > localValue3) {
            localValue8 += localValue9 + this.internalField0205;
            localValue9 = 0.0F;
            localValue16 = 0.0F;
            localValue11 = true;
         }

         if (localValue4) {
            float localValue17;
            float localValue18;
            float localValue19;
            float localValue20;
            if (localValue2) {
               localValue17 = localValue5 + localValue8;
               localValue18 = localValue6 + localValue16;
               localValue19 = localValue15;
               localValue20 = localValue14;
            } else {
               localValue17 = localValue5 + localValue16;
               localValue18 = localValue6 + localValue8;
               localValue19 = localValue14;
               localValue20 = localValue15;
            }

            if (this.snappedThisFrame()) {
               localValue13.forceSnap();
            }

            localValue13.setSlot(localValue17, localValue18, localValue19, localValue20);
         }

         localValue7 = localValue16 + localValue14;
         localValue9 = Math.max(localValue9, localValue15);
         localValue10 = Math.max(localValue10, localValue7);
         localValue11 = false;
      }

      this.internalField1458 = localValue10;
      this.internalField1459 = localValue8 + localValue9;
   }

   @Override
   protected void drawSelf(UiRenderContext localValue1, float localValue2) {
      if (this.internalField0571 != null) {
         ColorRGBA localValue3 = this.internalField0571.apply(this);
         if (localValue3 != null && localValue3.getAlpha() > 0.0F) {
            if (this.internalField1046 > 0.0F) {
               localValue1.drawSquircle(
                  this.x.internalMethod02046(),
                  this.y.internalMethod02046(),
                  this.w.internalMethod02046(),
                  this.h.internalMethod02046(),
                  this.internalField1046,
                  this.internalField0098,
                  localValue3
               );
            } else {
               localValue1.drawRoundedRect(
                  this.x.internalMethod02046(), this.y.internalMethod02046(), this.w.internalMethod02046(), this.h.internalMethod02046(), this.internalField0098, localValue3
               );
            }
         }
      }

      if (this.internalField0340 != null) {
         this.internalField0340.paint(localValue1, this);
      }
   }

   @Override
   protected void drawChildren(UiRenderContext localValue1, float localValue2) {
      boolean localValue3 = this.internalField0629.internalMethod05114();
      float localValue4 = this.x.internalMethod02046() + this.internalField0910.internalField1047;
      float localValue5 = this.y.internalMethod02046() + this.internalField0910.internalField0205;
      float localValue6 = Math.max(0.0F, this.w.internalMethod02046() - this.internalField0910.internalMethod05308());
      float localValue7 = Math.max(0.0F, this.h.internalMethod02046() - this.internalField0910.internalMethod05311());
      float localValue8 = 0.0F;
      boolean localValue9 = false;

      for (UiNode localValue11 : this.internalField0416) {
         if (localValue11.inFlow() && localValue11.isSticky()) {
            localValue9 = true;
            localValue8 += localValue3 ? localValue11.h() : localValue11.w();
         }
      }

      boolean localValue23 = this.internalField0276;
      org.joml.Matrix3x2fStack localValue24 = localValue1.getMatrices();
      boolean localValue25 = this.internalField0206 != 0.0F;
      float localValue26 = this.internalField0206 - this.internalField1049;
      float localValue27 = this.internalField0206 + this.internalField1460 + 24.0F;
      boolean localValue28 = this.internalField0276 && localValue3;
      boolean localValue16 = DRAW_CLIP;
      float localValue17 = DRAW_CLIP_MIN;
      float localValue18 = DRAW_CLIP_MAX;
      boolean contentScissorPushed = false;
      boolean contentMatrixPushed = false;

      try {
         if (localValue23) {
            float localValue12 = localValue4 + (localValue3 ? 0.0F : localValue8);
            float localValue13 = localValue5 + (localValue3 ? localValue8 : 0.0F);
            float localValue14 = Math.max(0.0F, localValue6 - (localValue3 ? 0.0F : localValue8));
            float localValue15 = Math.max(0.0F, localValue7 - (localValue3 ? localValue8 : 0.0F));
            ScissorStack.internalMethod06303(localValue24, localValue12, localValue13, localValue14, localValue15);
            contentScissorPushed = true;
         }

         if (localValue25) {
            localValue24.pushMatrix();
            contentMatrixPushed = true;
            localValue24.translate(localValue3 ? 0.0F : -this.internalField0206, localValue3 ? -this.internalField0206 : 0.0F);
         }

         if (localValue28) {
            DRAW_CLIP = true;
            DRAW_CLIP_MIN = localValue5 + this.internalField0206 - this.internalField1049;
            DRAW_CLIP_MAX = localValue5 + this.internalField0206 + this.internalField1460 + 24.0F;
         }

         for (UiNode localValue20 : this.internalField0416) {
            if (!localValue20.isSticky()) {
               float localValue21 = localValue3 ? localValue20.y() - localValue5 : localValue20.x() - localValue4;
               float localValue22 = localValue3 ? localValue20.h() : localValue20.w();
               if (!this.internalField0276 || !(localValue21 + localValue22 < localValue26) && !(localValue21 > localValue27)) {
                  localValue20.draw(localValue1, localValue2);
               }
            }
         }
      } finally {
         DRAW_CLIP = localValue16;
         DRAW_CLIP_MIN = localValue17;
         DRAW_CLIP_MAX = localValue18;
         if (contentMatrixPushed) {
            localValue24.popMatrix();
         }
         if (contentScissorPushed) {
            ScissorStack.internalMethod07643();
         }
      }

      if (localValue9) {
         boolean stickyScissorPushed = false;
         try {
            if (localValue23) {
               ScissorStack.internalMethod06303(localValue24, localValue4, localValue5, localValue6, localValue7);
               stickyScissorPushed = true;
            }

            for (UiNode localValue30 : this.internalField0416) {
               if (localValue30.isSticky()) {
                  localValue30.draw(localValue1, localValue2);
               }
            }
         } finally {
            if (stickyScissorPushed) {
               ScissorStack.internalMethod07643();
            }
         }
      }

      this.internalField0915.internalMethod06945(localValue1, localValue2);
   }

   @Override
   public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
      if (!this.inFlow()) {
         return false;
      } else if (this.internalField0915.internalMethod04466(localValue1, localValue2, localValue3 == MouseButton.internalField0102)) {
         return true;
      } else if (!this.contains(localValue1, localValue2)) {
         return false;
      } else {
         int localValue4 = this.internalMethod03627();

         for (int localValue5 = this.internalField0416.size() - 1; localValue5 >= localValue4; localValue5--) {
            UiNode localValue6 = this.internalField0416.get(localValue5);
            if (localValue6.inFlow() && localValue6.isSticky() && this.internalMethod03304(localValue6, localValue1, localValue2) && localValue6.mouseClicked(localValue1, localValue2, localValue3)) {
               return true;
            }
         }

         float localValue9 = localValue2;
         float localValue10 = localValue1;
         if (this.internalField0629.internalMethod05114()) {
            localValue9 = localValue2 + this.internalField0206;
         } else {
            localValue10 = localValue1 + this.internalField0206;
         }

         for (int localValue7 = this.internalField0416.size() - 1; localValue7 >= localValue4; localValue7--) {
            UiNode localValue8 = this.internalField0416.get(localValue7);
            if (localValue8.inFlow() && !localValue8.isSticky() && this.internalMethod03304(localValue8, localValue1, localValue2) && localValue8.mouseClicked(localValue10, localValue9, localValue3)) {
               return true;
            }
         }

         return super.mouseClicked(localValue1, localValue2, localValue3);
      }
   }

   @Override
   public void mouseReleased(float localValue1, float localValue2, MouseButton localValue3) {
      super.mouseReleased(localValue1, localValue2, localValue3);
      this.internalField0915.internalMethod05174();
      float localValue4 = localValue2;
      float localValue5 = localValue1;
      if (this.internalField0629.internalMethod05114()) {
         localValue4 = localValue2 + this.internalField0206;
      } else {
         localValue5 = localValue1 + this.internalField0206;
      }

      for (UiNode localValue7 : this.internalField0416) {
         if (localValue7.isSticky()) {
            localValue7.mouseReleased(localValue1, localValue2, localValue3);
         } else {
            localValue7.mouseReleased(localValue5, localValue4, localValue3);
         }
      }
   }

   @Override
   public boolean keyPressed(int localValue1, int localValue2, int localValue3) {
      if (!this.inFlow()) {
         return false;
      } else {
         int localValue4 = this.internalField0416.size() - 1;

         for (int localValue5 = this.internalMethod03627(); localValue4 >= localValue5; localValue4--) {
            if (this.internalField0416.get(localValue4).keyPressed(localValue1, localValue2, localValue3)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean keyReleased(int localValue1, int localValue2, int localValue3) {
      if (!this.inFlow()) {
         return false;
      } else {
         int localValue4 = this.internalField0416.size() - 1;

         for (int localValue5 = this.internalMethod03627(); localValue4 >= localValue5; localValue4--) {
            if (this.internalField0416.get(localValue4).keyReleased(localValue1, localValue2, localValue3)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean charTyped(char localValue1, int localValue2) {
      if (!this.inFlow()) {
         return false;
      } else {
         int localValue3 = this.internalField0416.size() - 1;

         for (int localValue4 = this.internalMethod03627(); localValue3 >= localValue4; localValue3--) {
            if (this.internalField0416.get(localValue3).charTyped(localValue1, localValue2)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
      if (this.inFlow() && this.contains(localValue1, localValue2)) {
         float localValue5 = localValue2;
         float localValue6 = localValue1;
         if (this.internalField0629.internalMethod05114()) {
            localValue5 = localValue2 + this.internalField0206;
         } else {
            localValue6 = localValue1 + this.internalField0206;
         }

         int localValue7 = this.internalMethod03627();

         for (int localValue8 = this.internalField0416.size() - 1; localValue8 >= localValue7; localValue8--) {
            UiNode localValue9 = this.internalField0416.get(localValue8);
            float localValue10 = localValue9.isSticky() ? localValue1 : localValue6;
            float localValue11 = localValue9.isSticky() ? localValue2 : localValue5;
            if (localValue9.mouseScrolled(localValue10, localValue11, localValue3, localValue4)) {
               return true;
            }
         }

         if (localValue7 > 0) {
            return true;
         } else if (this.internalField0276 && this.internalField1458 > this.internalField1460 + 0.5F) {
            float localValue12 = this.internalField0629.internalMethod05114() ? localValue4 : localValue3;
            this.internalField1048 -= localValue12 * 22.0F;
            this.internalField0915.internalMethod05171();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public interface InternalType0354 {
      void paint(UiRenderContext localValue1, UiContainer localValue2);
   }
}
