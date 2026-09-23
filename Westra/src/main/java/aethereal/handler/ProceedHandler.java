package aethereal.handler;

import aethereal.lib.javassist.ASTList;
import aethereal.lib.javassist.Bytecode_2;
import aethereal.lib.javassist.CompileError;
import aethereal.lib.javassist.JvstCodeGen;
import aethereal.lib.javassist.JvstTypeChecker;

public interface ProceedHandler {
   void a(JvstCodeGen var1, Bytecode_2 var2, ASTList var3) throws CompileError;

   void a(JvstTypeChecker var1, ASTList var2) throws CompileError;
}
