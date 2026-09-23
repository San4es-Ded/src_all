package aethereal.util;

import aethereal.core.Interface;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_2596;

public class NetworkUtil implements Interface {
   private static final int a = 256;
   private static final Map<class_2596<?>, Boolean> b = new IdentityHashMap<>();
   private static final Deque<class_2596<?>> c = new ArrayDeque<>();

   @Generated
   private NetworkUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void a(class_2596<?> packet) {
      if (packet != null && aM_.method_1562() != null) {
         synchronized (b) {
            if (b.put(packet, Boolean.TRUE) == null) {
               c.addLast(packet);
            }

            while (c.size() > 256) {
               b.remove(c.removeFirst());
            }
         }

         try {
            aM_.method_1562().method_48296().method_10743(packet);
         } finally {
            synchronized (b) {
               if (b.remove(packet) != null) {
                  c.remove(packet);
               }
            }
         }
      }
   }

   public static boolean b(class_2596<?> packet) {
      synchronized (b) {
         return b.containsKey(packet);
      }
   }

   public static void a() {
      synchronized (b) {
         b.clear();
         c.clear();
      }
   }
}
