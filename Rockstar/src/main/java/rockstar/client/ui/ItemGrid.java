package rockstar.client.ui;





import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import pyrock.utility.render.ColorRGBA;

public class ItemGrid<T> extends UiNode {
   private final Supplier<List<T>> internalField0017;
   private final Function<T, ItemStack> internalField0571;
   private final Predicate<T> internalField0486;
   private final Consumer<T> internalField0922;
   private float internalField0205 = 20.0F;
   private float internalField0206 = 2.0F;
   private float internalField1048 = 0.9F;
   private float internalField1047 = 4.0F;
   private float internalField1049 = 2.0F;
   private float internalField1046 = -1.0F;
   private Consumer<T> internalField0921;
   private int internalField0227;
   private Function<ItemGrid<T>, ColorRGBA> internalField0570 = localValue0 -> ThemeColors.internalField1614;
   private Function<ItemGrid<T>, ColorRGBA> internalField1216 = localValue0 -> ThemeColors.internalField1310.mulAlpha(0.7F);
   private Function<ItemGrid<T>, ColorRGBA> internalField1213 = localValue0 -> ThemeColors.internalField1310;
   private final AnimatedFloat internalField0623 = new AnimatedFloat(0.0F, Motion.internalMethod01328(260L, Easing.internalField1828));
   private float internalField1456 = 0.0F;
   private float internalField1457 = 0.0F;
   private final Map<T, float[]> internalField0543 = new IdentityHashMap<>();

   public ItemGrid(Supplier<List<T>> localValue1, Function<T, ItemStack> localValue2, Predicate<T> localValue3, Consumer<T> localValue4) {
      this.internalField0017 = localValue1;
      this.internalField0571 = localValue2;
      this.internalField0486 = localValue3;
      this.internalField0922 = localValue4;
      this.cursor(CursorType.internalField0567);
   }

   public ItemGrid<T> internalMethod01914(Consumer<T> localValue1) {
      this.internalField0921 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod05719(int localValue1) {
      this.internalField0227 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod06059(float localValue1) {
      this.internalField0205 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod03897(float localValue1) {
      this.internalField0206 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod08580(float localValue1) {
      this.internalField1048 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod08956(float localValue1) {
      this.internalField1047 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod08852(float localValue1) {
      this.internalField1049 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod08417(float localValue1) {
      this.internalField1046 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod06518(ColorRGBA localValue1) {
      this.internalField0570 = localValue1x -> localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod07352(Function<ItemGrid<T>, ColorRGBA> localValue1) {
      this.internalField0570 = localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod03345(ColorRGBA localValue1) {
      this.internalField1216 = localValue1x -> localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod08286(ColorRGBA localValue1) {
      this.internalField1213 = localValue1x -> localValue1;
      return this;
   }

   public ItemGrid<T> internalMethod09528(float localValue1) {
      super.width(localValue1);
      return this;
   }

   public ItemGrid<T> internalMethod09696(float localValue1) {
      super.height(localValue1);
      return this;
   }

   public ItemGrid<T> internalMethod06817() {
      super.fillWidth();
      return this;
   }

   public ItemGrid<T> internalMethod02904() {
      super.fillHeight();
      return this;
   }

   private float internalMethod03130() {
      return this.internalField0205 - this.internalField0206 * 2.0F;
   }

   private int internalMethod06611(float localValue1) {
      return Math.max(1, (int)Math.floor((localValue1 - this.internalField1049 * 2.0F) / this.internalField0205));
   }

   private int internalMethod06988(float localValue1, float localValue2) {
      float localValue3 = this.x();
      float localValue4 = this.y();
      float localValue5 = this.w();
      float localValue6 = this.h();
      if (!(localValue1 < localValue3) && !(localValue1 > localValue3 + localValue5) && !(localValue2 < localValue4) && !(localValue2 > localValue4 + localValue6)) {
         List localValue7 = this.internalField0017.get();
         int localValue8 = this.internalMethod06611(localValue5);
         float localValue9 = localValue8 * this.internalField0205;
         float localValue10 = localValue3 + (localValue5 - localValue9) / 2.0F;
         float localValue11 = localValue1 - localValue10;
         float localValue12 = localValue2 - localValue4 - this.internalField1049 + this.internalField0623.internalMethod02046();
         if (!(localValue11 < 0.0F) && !(localValue12 < 0.0F)) {
            int localValue13 = (int)(localValue11 / this.internalField0205);
            int localValue14 = (int)(localValue12 / this.internalField0205);
            if (localValue13 >= 0 && localValue13 < localValue8) {
               int localValue15 = localValue14 * localValue8 + localValue13;
               return localValue15 >= 0 && localValue15 < localValue7.size() ? localValue15 : -1;
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   @Override
   protected void measure() {
      float localValue1 = this.w();
      float localValue2 = this.internalField1046 > 0.0F ? this.internalField1046 : localValue1;
      if (localValue1 < 4.0F) {
         if (!this.explicitH) {
            this.prefH = this.internalField1046 > 0.0F ? this.internalField1046 : 110.0F;
         }
      } else {
         int localValue3 = this.internalMethod06611(localValue1);
         int localValue4 = this.internalField0017.get().size();
         int localValue5 = Math.max(1, (int)Math.ceil((float)localValue4 / localValue3));
         this.internalField1457 = localValue5 * this.internalField0205 + this.internalField1049 * 2.0F;
         if (!this.explicitH) {
            this.prefH = Math.min(this.internalField1457, localValue2);
         }
      }
   }

   @Override
   protected void onTick(float localValue1, float localValue2, float localValue3) {
      float localValue4 = Math.max(0.0F, this.internalField1457 - this.h());
      this.internalField1456 = Math.max(0.0F, Math.min(this.internalField1456, localValue4));
      this.internalField0623.internalMethod03690(this.internalField1456);
      this.internalField0623.internalMethod08946(localValue1);
      int localValue5 = this.inFlow() && this.contains(localValue2, localValue3) ? this.internalMethod06988(localValue2, localValue3) : -1;
      List localValue6 = this.internalField0017.get();
      if (this.internalField0921 != null) {
         this.internalField0921.accept((T)(localValue5 >= 0 && localValue5 < localValue6.size() ? localValue6.get(localValue5) : null));
      }

      float localValue7 = Math.min(1.0F, localValue1 / 1000.0F * 12.0F);

      for (int localValue8 = 0; localValue8 < localValue6.size(); localValue8++) {
         Object localValue9 = localValue6.get(localValue8);
         float[] localValue10 = this.internalMethod02664((T)localValue9);
         float localValue11 = this.internalField0486.test((T)localValue9) ? 1.0F : 0.0F;
         float localValue12 = localValue8 == localValue5 ? 1.0F : 0.0F;
         localValue10[0] += (localValue11 - localValue10[0]) * localValue7;
         localValue10[1] += (localValue12 - localValue10[1]) * localValue7;
      }
   }

   private float[] internalMethod02664(T localValue1) {
      float[] localValue2 = this.internalField0543.get(localValue1);
      if (localValue2 == null) {
         localValue2 = new float[]{this.internalField0486.test((T)localValue1) ? 1.0F : 0.0F, 0.0F};
         this.internalField0543.put((T)localValue1, localValue2);
      }

      return localValue2;
   }

   @Override
   protected void drawSelf(UiRenderContext localValue1, float localValue2) {
      List localValue3 = this.internalField0017.get();
      int localValue4 = localValue3.size();
      float localValue5 = this.x();
      float localValue6 = this.y();
      float localValue7 = this.w();
      float localValue8 = this.h();
      int localValue9 = this.internalMethod06611(localValue7);
      float localValue10 = localValue9 * this.internalField0205;
      float localValue11 = localValue5 + (localValue7 - localValue10) / 2.0F;
      float localValue12 = this.internalField0623.internalMethod02046();
      float localValue13 = this.internalMethod03130();
      CornerRadii localValue14 = CornerRadii.internalMethod03908(this.internalField1047);
      org.joml.Matrix3x2fStack localValue15 = localValue1.getMatrices();
      ScissorStack.internalMethod06303(localValue15, localValue5, localValue6, localValue7, localValue8);
      ColorRGBA localValue16 = this.internalField0570.apply(this);
      ColorRGBA localValue17 = this.internalField1216.apply(this);

      for (int localValue18 = 0; localValue18 < localValue4; localValue18++) {
         float localValue19 = localValue11 + localValue18 % localValue9 * this.internalField0205;
         float localValue20 = localValue6 + this.internalField1049 + localValue18 / localValue9 * this.internalField0205 - localValue12;
         if (!(localValue20 + this.internalField0205 < localValue6) && !(localValue20 > localValue6 + localValue8)) {
            float[] localValue21 = this.internalMethod02664((T)localValue3.get(localValue18));
            ColorRGBA localValue22 = localValue16.mix(localValue17, localValue21[0]).mulAlpha(1.0F + 0.22F * localValue21[1]);
            localValue1.drawRoundedRect(localValue19 + this.internalField0206, localValue20 + this.internalField0206, localValue13, localValue13, localValue14, localValue22);
         }
      }

      if (this.internalField0227 != 0) {
         localValue15.pushMatrix();
         localValue15.translate(0.0F, 0.0F);
      }

      for (int localValue23 = 0; localValue23 < localValue4; localValue23++) {
         float localValue25 = localValue11 + localValue23 % localValue9 * this.internalField0205;
         float localValue27 = localValue6 + this.internalField1049 + localValue23 / localValue9 * this.internalField0205 - localValue12;
         if (!(localValue27 + this.internalField0205 < localValue6) && !(localValue27 > localValue6 + localValue8)) {
            float localValue29 = localValue25 + this.internalField0206 + localValue13 / 2.0F;
            float localValue31 = localValue27 + this.internalField0206 + localValue13 / 2.0F;
            HudRenderUtils.internalMethod08976(localValue15, localValue29, localValue31, this.internalField1048 * localValue2);
            localValue1.drawBatchItem(this.internalField0571.apply((T)localValue3.get(localValue23)), localValue29 - 8.0F, localValue31 - 8.0F);
            HudRenderUtils.internalMethod00012(localValue15);
            MinecraftClient.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
         }
      }

      if (this.internalField0227 != 0) {
         localValue15.popMatrix();
      }

      ColorRGBA localValue24 = this.internalField1213.apply(this);

      for (int localValue26 = 0; localValue26 < localValue4; localValue26++) {
         float[] localValue28 = this.internalMethod02664((T)localValue3.get(localValue26));
         if (!(localValue28[1] <= 0.01F)) {
            float localValue30 = localValue11 + localValue26 % localValue9 * this.internalField0205;
            float localValue32 = localValue6 + this.internalField1049 + localValue26 / localValue9 * this.internalField0205 - localValue12;
            if (!(localValue32 + this.internalField0205 < localValue6) && !(localValue32 > localValue6 + localValue8)) {
               localValue1.drawRoundedBorder(localValue30 + this.internalField0206, localValue32 + this.internalField0206, localValue13, localValue13, 1.2F, localValue14, localValue24.mulAlpha(0.6F * localValue28[1]));
            }
         }
      }

      ScissorStack.internalMethod07643();
   }

   @Override
   public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
      if (!this.inFlow() || !this.contains(localValue1, localValue2)) {
         return false;
      } else if (localValue3 != MouseButton.internalField0102) {
         return false;
      } else {
         int localValue4 = this.internalMethod06988(localValue1, localValue2);
         if (localValue4 < 0) {
            return false;
         } else {
            this.internalField0922.accept(this.internalField0017.get().get(localValue4));
            return true;
         }
      }
   }

   @Override
   public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
      if (this.inFlow() && this.contains(localValue1, localValue2)) {
         float localValue5 = Math.max(0.0F, this.internalField1457 - this.h());
         if (localValue5 <= 0.5F) {
            return false;
         } else {
            this.internalField1456 = Math.max(0.0F, Math.min(this.internalField1456 - localValue4 * 22.0F, localValue5));
            return true;
         }
      } else {
         return false;
      }
   }
}
