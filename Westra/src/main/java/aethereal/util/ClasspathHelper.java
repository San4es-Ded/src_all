package aethereal.util;

import java.util.Arrays;
import java.util.List;

public final class ClasspathHelper {
   private ClasspathHelper() {
   }

   public static Iterable<ClassLoader> a(ClassLoader... loaders) {
      return loaders != null && loaders.length != 0 ? Arrays.asList(loaders) : List.of(ClassLoader.getSystemClassLoader());
   }
}
