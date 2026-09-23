package aethereal.lib.javassist;

import javassist.CtClass;

public class ClassPool extends javassist.ClassPool {
   public ClassPool() {
   }

   public ClassPool(boolean makeDefault) {
      super(makeDefault);
   }

   public CtClass f(String name) throws javassist.NotFoundException {
      return this.get(name);
   }
}
