package aethereal.lib.javassist;

import javassist.bytecode.CodeAttribute;

public class CodeIterator extends javassist.bytecode.CodeIterator {
   public CodeIterator(CodeAttribute ca) {
      super(ca);
   }

   public int d(int pos) {
      return this.byteAt(pos);
   }

   public int g(int pos) {
      return this.u16bitAt(pos);
   }

   public int h(int pos) {
      return this.s32bitAt(pos);
   }
}
