package aethereal.setting;

import aethereal.ui.element.Element_2;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;

public abstract class Setting<Value> {
   private final Value c;
   private Value d;
   private final String e;
   private Supplier<Boolean> a = () -> true;
   private Consumer<Value> b = value -> {};
   private boolean f = true;

   public abstract Element_2<?> d();

   @Generated
   public Supplier<Boolean> e() {
      return this.a;
   }

   @Generated
   public Consumer<Value> f() {
      return this.b;
   }

   @Generated
   public Value g() {
      return this.c;
   }

   @Generated
   public Value h() {
      return this.d;
   }

   @Generated
   public String i() {
      return this.e;
   }

   @Generated
   public boolean j() {
      return this.f;
   }

   public Setting(String name, Value defaultValue) {
      this.e = name;
      this.c = defaultValue;
      this.d = defaultValue;
   }

   public <T extends Setting<Value>> T a() {
      this.f = false;
      return (T)this;
   }

   public Setting<Value> a(Value newValue) {
      if (Objects.equals(this.d, newValue)) {
         return this;
      } else {
         this.d = newValue;
         this.b.accept(newValue);
         return this;
      }
   }

   public <T extends Setting<Value>> T a(Consumer<Value> onChange) {
      this.b = onChange;
      return (T)this;
   }

   public void b() {
      this.d = this.c;
   }

   public <T extends Setting<Value>> T a(Supplier<Boolean> bool) {
      this.a = bool;
      return (T)this;
   }

   public Value c() {
      return this.d;
   }
}
