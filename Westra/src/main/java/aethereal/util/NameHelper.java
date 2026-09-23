package aethereal.util;

import aethereal.lib.reflections.ReflectionsException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

public interface NameHelper {
   List<String> l = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
   List<Class<?>> m = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);
   List<String> n = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");

   default String c(AnnotatedElement element) {
      if (element.getClass().equals(Class.class)) {
         return this.g((Class<?>)element);
      } else if (element.getClass().equals(Constructor.class)) {
         return this.a((Constructor<?>)element);
      } else if (element.getClass().equals(Method.class)) {
         return this.a((Method)element);
      } else {
         return element.getClass().equals(Field.class) ? this.a((Field)element) : null;
      }
   }

   default String g(Class<?> type) {
      int dim = 0;

      while (type.isArray()) {
         dim++;
         type = type.getComponentType();
      }

      return type.getName() + String.join("", Collections.nCopies(dim, "[]"));
   }

   default String a(Constructor<?> constructor) {
      return String.format("%s.<init>(%s)", constructor.getName(), String.join(", ", this.b(constructor.getParameterTypes())));
   }

   default String a(Method method) {
      return String.format("%s.%s(%s)", method.getDeclaringClass().getName(), method.getName(), String.join(", ", this.b(method.getParameterTypes())));
   }

   default String a(Field field) {
      return String.format("%s.%s", field.getDeclaringClass().getName(), field.getName());
   }

   default Collection<String> a(Collection<? extends AnnotatedElement> elements) {
      return elements.stream().map(this::c).filter(v0 -> Objects.nonNull(v0)).collect(Collectors.toList());
   }

   default Collection<String> b(AnnotatedElement... elements) {
      return this.a(Arrays.asList(elements));
   }

   default <T> T a(String str, Class<T> cls, ClassLoader... classLoaderArr) {
      if (cls.equals(Class.class)) {
         return (T)this.a(str, classLoaderArr);
      } else if (cls.equals(Constructor.class)) {
         return (T)this.d(str, classLoaderArr);
      } else if (cls.equals(Method.class)) {
         return (T)this.c(str, classLoaderArr);
      } else if (cls.equals(Field.class)) {
         return (T)this.e(str, classLoaderArr);
      } else {
         return (T)(cls.equals(Member.class) ? this.b(str, classLoaderArr) : null);
      }
   }

   default Class<?> a(String typeName, ClassLoader... loaders) {
      if (l.contains(typeName)) {
         return m.get(l.indexOf(typeName));
      } else {
         String type;
         if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            String type3 = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");
            String type2;
            if (l.contains(type3)) {
               type2 = n.get(l.indexOf(type3));
            } else {
               type2 = "L" + type3 + ";";
            }

            type = array + type2;
         } else {
            type = typeName;
         }

         for (ClassLoader classLoader : ClasspathHelper.a(loaders)) {
            if (type.contains("[")) {
               try {
                  return Class.forName(type, false, classLoader);
               } catch (Throwable var9) {
               }
            }

            try {
               return classLoader.loadClass(type);
            } catch (Throwable var8) {
            }
         }

         return null;
      }
   }

   default Member b(String descriptor, ClassLoader... loaders) throws ReflectionsException {
      int p0 = descriptor.lastIndexOf(40);
      String memberKey = p0 != -1 ? descriptor.substring(0, p0) : descriptor;
      String methodParameters = p0 != -1 ? descriptor.substring(p0 + 1, descriptor.lastIndexOf(41)) : "";
      int p1 = Math.max(memberKey.lastIndexOf(46), memberKey.lastIndexOf("$"));
      String className = memberKey.substring(0, p1);
      String memberName = memberKey.substring(p1 + 1);
      Class<?>[] parameterTypes = null;
      if (!methodParameters.isEmpty()) {
         String[] parameterNames = methodParameters.split(",");
         parameterTypes = Arrays.stream(parameterNames).map(name -> this.a(name.trim(), loaders)).toArray(Class[]::new);
      }

      try {
         for (Class<?> aClass = this.a(className, loaders); aClass != null; aClass = aClass.getSuperclass()) {
            try {
               if (descriptor.contains("(")) {
                  if (!descriptor.contains("init>")) {
                     return aClass.isInterface() ? aClass.getMethod(memberName, parameterTypes) : aClass.getDeclaredMethod(memberName, parameterTypes);
                  }

                  return aClass.isInterface() ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredConstructor(parameterTypes);
               }

               return aClass.isInterface() ? aClass.getField(memberName) : aClass.getDeclaredField(memberName);
            } catch (Exception var12) {
            }
         }

         return null;
      } catch (Exception var13) {
         return null;
      }
   }

   @Nullable
   default <T extends AnnotatedElement> T b(String descriptor, Class<T> resultType, ClassLoader[] loaders) {
      Member member = this.b(descriptor, loaders);
      return (T)(member != null && member.getClass().equals(resultType) ? member : null);
   }

   @Nullable
   default Method c(String descriptor, ClassLoader... loaders) throws ReflectionsException {
      return this.b(descriptor, Method.class, loaders);
   }

   default Constructor<?> d(String descriptor, ClassLoader... loaders) throws ReflectionsException {
      return this.b(descriptor, Constructor.class, loaders);
   }

   @Nullable
   default Field e(String descriptor, ClassLoader... loaders) {
      return this.b(descriptor, Field.class, loaders);
   }

   default <T> Collection<T> a(Collection<String> names, Class<T> resultType, ClassLoader... loaders) {
      return names.stream().map(name -> this.a(name, resultType, loaders)).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
   }

   default Collection<Class<?>> a(Collection<String> names, ClassLoader... loaders) {
      return this.a(names, Class.class, loaders);
   }
}
