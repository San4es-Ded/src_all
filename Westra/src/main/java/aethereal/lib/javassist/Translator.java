package aethereal.lib.javassist;

public interface Translator extends javassist.Translator {
   default void a(ClassPool pool) throws NotFoundException, CannotCompileException {
      try {
         this.start(pool);
      } catch (javassist.NotFoundException var3) {
         throw new NotFoundException(var3.getMessage());
      } catch (javassist.CannotCompileException var4) {
         throw new CannotCompileException(var4.getMessage(), var4);
      }
   }

   default void a(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
   }
}
