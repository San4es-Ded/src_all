package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.setting.StringSetting;
import net.minecraft.class_332;
import net.minecraft.class_5611;

public class StringElement extends Element_2<StringSetting> {
   private TextField d;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      TextField textFieldG = this.g();
      if (textFieldG != null) {
         textFieldG.a(mouseX, mouseY, button);
         TextField textFieldG2 = this.g();
         if (textFieldG2 != null) {
            return textFieldG2.j();
         }
      }

      throw new NullPointerException();
   }

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.d == null) {
         return false;
      } else {
         this.d.b(mouseX, mouseY, button);
         return this.d.j();
      }
   }

   @Compile
   @Override
   public boolean a(int keyCode, int scanCode, int modifiers) {
      if (this.d != null && this.d.j()) {
         this.d.a(keyCode, scanCode, modifiers);
         return true;
      } else {
         return false;
      }
   }

   @Compile
   @Override
   public boolean a(char chr, int modifiers) {
      if (this.d == null) {
         return false;
      } else {
         TextField textField = this.d;
         if (textField == null) {
            throw new NullPointerException();
         } else if (!textField.j()) {
            return false;
         } else {
            TextField textField2 = this.d;
            if (textField2 == null) {
               throw new NullPointerException();
            } else {
               textField2.a(chr, modifiers);
               return true;
            }
         }
      }
   }

   public StringElement(StringSetting setting) {
      super(setting);
      this.a.w = 12.0F;
   }

   private TextField g() {
      if (this.d == null) {
         this.d = new TextField(TextField.a.GUI_SETTING, this.b.k());
         this.d.a(this.b.i());
         this.d.g().append(this.b.c());
      }

      return this.d;
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      TextField field = this.g();
      field.b(new class_5611(this.a.z, this.a.w));
      field.a(new class_5611(this.a.x, this.a.y));
      field.a(context, mouseX, mouseY, delta, extend);
      this.b.a(field.g().toString());
   }

   static {
      NativeMethodLookup.lookup(StringElement.class, 14);
   }
}
