package rockstar.client.setting;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import pyrock.utility.render.ColorRGBA;

public class SectionSetting extends AbstractSetting {
   private int internalField0227 = 1;
   private boolean internalField0277;
   private boolean internalField0276;

   public SectionSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3, @NotNull BooleanSupplier localValue4) {
      super(localValue1, localValue2, localValue4);
   }

   public SectionSetting(@NotNull SettingOwner localValue1, String localValue2, @NotNull BooleanSupplier localValue3) {
      super(localValue1, localValue2, localValue3);
   }

   public SectionSetting(@NotNull SettingOwner localValue1, String localValue2, String localValue3) {
      super(localValue1, localValue2);
   }

   public SectionSetting(@NotNull SettingOwner localValue1, String localValue2) {
      super(localValue1, localValue2);
   }

   public SectionSetting internalMethod04286(int localValue1) {
      this.internalField0227 = localValue1;
      return this;
   }

   public SectionSetting internalMethod00288() {
      this.internalField0277 = true;
      return this;
   }

   public SectionSetting internalMethod00983() {
      this.internalField0276 = true;
      return this;
   }

   @Override
   public final JsonElement toJson() {
      return new JsonPrimitive("\u043a\u043e\u0441\u0442\u044b\u043b\u044c");
   }

   @Override
   public final void fromJson(JsonElement localValue1) {
   }

   @Override
   public UiContainer createComponent() {
      SizedFont localValue1 = (this.internalField0277 ? Fonts.internalField1154 : Fonts.internalField1157).internalMethod01432(8 + this.internalField0227);
      Supplier localValue2 = () -> LanguageManager.internalMethod07214(this.internalField0248);
      Object localValue3 = this.internalField0277
         ? new WrappedTextNode(localValue1, localValue2)
            .internalMethod04223(this.internalField0276)
            .internalMethod04268(localValue0 -> ThemeColors.internalField1613.mulAlpha(0.9F))
            .internalMethod04302()
         : new UiElement()
            .fillWidth()
            .height(localValue1.internalMethod04890())
            .text(localValue1, localValue2, localValue0 -> ThemeColors.internalField1613.mulAlpha(0.9F))
            .textAlign(this.internalField0276 ? TextAlignment.internalField0621 : TextAlignment.internalField0622)
            .fade(!this.internalField0276)
            .textShadow(this.internalField0276 ? ColorRGBA.BLACK.withAlpha(100.0F) : null, 0.0F, 1.0F, 0.0F)
            .interactive(false);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod03907(
            new UiContainer()
               .internalMethod01863()
               .internalMethod09609()
               .internalMethod03514(Insets.internalMethod00105(10.0F, 0.0F, 5.0F, 0.0F))
               .internalMethod03907((UiNode)localValue3)
         );
   }

   @Generated
   public int internalMethod00687() {
      return this.internalField0227;
   }

   @Generated
   public boolean internalMethod04496() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod08083() {
      return this.internalField0276;
   }

   @Generated
   public void internalMethod02054(int localValue1) {
      this.internalField0227 = localValue1;
   }

   @Generated
   public void internalMethod02055(boolean localValue1) {
      this.internalField0277 = localValue1;
   }

   @Generated
   public void internalMethod02116(boolean localValue1) {
      this.internalField0276 = localValue1;
   }
}
