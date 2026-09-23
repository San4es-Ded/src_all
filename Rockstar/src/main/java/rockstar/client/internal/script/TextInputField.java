package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;

public class TextInputField extends UiNode {
   private final SizedFont internalField0447;
   private final CustomTextField internalField0936;
   private final Consumer<String> internalField0922;
   private String internalField0248;
   private Supplier<String> internalField0017 = () -> "";
   private Function<TextInputField, ColorRGBA> internalField0571;
   private Function<TextInputField, ColorRGBA> internalField0570;
   private float internalField0205 = 0.0F;
   private Function<TextInputField, ColorRGBA> internalField1216 = localValue0 -> ThemeColors.internalField1613;
   private float internalField0206 = 4.0F;
   private float internalField1048 = 0.0F;
   private final AnimatedFloat internalField0623 = new AnimatedFloat(0.0F, Motion.internalMethod01328(200L, Easing.internalField1822));

   public TextInputField(SizedFont localValue1, String localValue2, Consumer<String> localValue3) {
      this.internalField0447 = localValue1;
      this.internalField0922 = localValue3;
      this.internalField0936 = new CustomTextField(localValue1);
      this.internalField0936.internalMethod00484(localValue2 == null ? "" : localValue2);
      this.internalField0248 = this.internalField0936.internalMethod06202();
      this.internalMethod06928(120.0F, 15.0F);
   }

   public String internalMethod04805() {
      return this.internalField0936.internalMethod06202();
   }

   public TextInputField internalMethod06885(String localValue1) {
      String localValue2 = localValue1 == null ? "" : localValue1;
      if (localValue2.equals(this.internalField0936.internalMethod06202())) {
         return this;
      } else {
         this.internalField0936.internalMethod00484(localValue2);
         this.internalField0248 = localValue2;
         return this;
      }
   }

   public TextInputField internalMethod07533(Supplier<String> localValue1) {
      if (localValue1 != null) {
         this.internalField0017 = localValue1;
      }

      return this;
   }

   public TextInputField internalMethod01789(String localValue1) {
      this.internalField0017 = () -> localValue1;
      return this;
   }

   public TextInputField internalMethod04564(boolean localValue1) {
      this.internalField0936.internalMethod07563(localValue1);
      return this;
   }

   public TextInputField internalMethod06323(int localValue1) {
      this.internalField0936.internalMethod07506(localValue1);
      return this;
   }

   public TextInputField internalMethod04945() {
      this.internalField0936.internalMethod01541();
      return this;
   }

   public TextInputField internalMethod02926(ColorRGBA localValue1) {
      this.internalField0571 = localValue1x -> localValue1;
      return this;
   }

   public TextInputField internalMethod06836(Function<TextInputField, ColorRGBA> localValue1) {
      this.internalField0571 = localValue1;
      return this;
   }

   public TextInputField internalMethod01600(float localValue1, ColorRGBA localValue2) {
      this.internalField0205 = localValue1;
      this.internalField0570 = localValue1x -> localValue2;
      return this;
   }

   public TextInputField internalMethod06069(float localValue1, Function<TextInputField, ColorRGBA> localValue2) {
      this.internalField0205 = localValue1;
      this.internalField0570 = localValue2;
      return this;
   }

   public TextInputField internalMethod01901(float localValue1) {
      this.internalField0206 = localValue1;
      return this;
   }

   public TextInputField internalMethod03704(ColorRGBA localValue1) {
      this.internalField1216 = localValue1x -> localValue1;
      return this;
   }

   public TextInputField internalMethod00015(Function<TextInputField, ColorRGBA> localValue1) {
      this.internalField1216 = localValue1;
      return this;
   }

   public TextInputField internalMethod03161(float localValue1) {
      this.internalField1048 = localValue1;
      return this;
   }

   public TextInputField internalMethod02459(int localValue1) {
      this.internalField0936.internalMethod07506(localValue1);
      return this;
   }

   public boolean internalMethod06703() {
      return this.internalField0936.internalMethod00342();
   }

   public float internalMethod06701() {
      return this.internalField0623.internalMethod02046();
   }

   public TextInputField internalMethod07676(float localValue1) {
      super.width(localValue1);
      return this;
   }

   public TextInputField internalMethod07996(float localValue1) {
      super.height(localValue1);
      return this;
   }

   public TextInputField internalMethod06928(float localValue1, float localValue2) {
      super.size(localValue1, localValue2);
      return this;
   }

   public TextInputField internalMethod06744() {
      super.fillWidth();
      return this;
   }

   public TextInputField internalMethod08467() {
      super.fillHeight();
      return this;
   }

   private void internalMethod06702() {
      this.internalField0936.internalMethod05191(this.x() + this.internalField1048, this.y(), this.w() - this.internalField1048 * 2.0F, this.h());
   }

   @Override
   public boolean mouseClicked(float localValue1, float localValue2, MouseButton localValue3) {
      if (!this.inFlow()) {
         return false;
      } else {
         this.internalMethod06702();
         this.internalField0936.internalMethod01643(localValue1, localValue2, localValue3);
         return this.contains(localValue1, localValue2);
      }
   }

   @Override
   public void mouseReleased(float localValue1, float localValue2, MouseButton localValue3) {
      this.internalField0936.internalMethod02863(localValue1, localValue2, localValue3);
      super.mouseReleased(localValue1, localValue2, localValue3);
   }

   @Override
   public boolean keyPressed(int localValue1, int localValue2, int localValue3) {
      if (!this.internalField0936.internalMethod00342()) {
         return false;
      } else {
         this.internalField0936.internalMethod05727(localValue1, localValue2, localValue3);
         return true;
      }
   }

   @Override
   public boolean charTyped(char localValue1, int localValue2) {
      return !this.internalField0936.internalMethod00342() ? false : this.internalField0936.internalMethod05413(localValue1, localValue2);
   }

   @Override
   public void onTick(float localValue1, float localValue2, float localValue3) {
      this.internalField0623.internalMethod03690(this.internalField0936.internalMethod00342() ? 1.0F : 0.0F);
      this.internalField0623.internalMethod08946(localValue1);
   }

   @Override
   public void drawSelf(UiRenderContext localValue1, float localValue2) {
      if (this.internalField0571 != null) {
         ColorRGBA localValue3 = this.internalField0571.apply(this);
         if (localValue3 != null && localValue3.getAlpha() > 0.0F) {
            localValue1.drawRoundedRect(this.x(), this.y(), this.w(), this.h(), CornerRadii.internalMethod03908(this.internalField0206), localValue3);
         }
      }

      if (this.internalField0570 != null && this.internalField0205 > 0.0F) {
         ColorRGBA localValue4 = this.internalField0570.apply(this);
         if (localValue4 != null && localValue4.getAlpha() > 0.0F) {
            localValue1.drawRoundedBorder(this.x(), this.y(), this.w(), this.h(), this.internalField0205, CornerRadii.internalMethod03908(this.internalField0206), localValue4);
         }
      }

      this.internalMethod06702();
      this.internalField0936.internalMethod08627(1.0F);
      this.internalField0936.internalMethod09000(this.internalField0017.get());
      this.internalField0936.internalMethod00143(this.internalField1216.apply(this));
      this.internalField0936.internalMethod03398(localValue1);
      String localValue5 = this.internalField0936.internalMethod06202();
      if (!localValue5.equals(this.internalField0248)) {
         this.internalField0248 = localValue5;
         this.internalField0922.accept(localValue5);
      }
   }
}
