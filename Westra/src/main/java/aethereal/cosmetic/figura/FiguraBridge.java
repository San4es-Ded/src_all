package aethereal.cosmetic.figura;

import aethereal.core.Interface;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;

public final class FiguraBridge {
   private static Method load;
   private static Method clear;

   private FiguraBridge() {
   }

   public static boolean available() {
      return FabricLoader.getInstance().isModLoaded("figura");
   }

   public static boolean apply(Path avatar) {
      if (!available()) {
         System.err.println("[Westra] Figura is not loaded");
         return false;
      } else if (avatar == null) {
         return false;
      } else {
         try {
            clear();
            if (load == null) {
               load = Class.forName("org.figuramc.figura.avatar.AvatarManager").getMethod("loadLocalAvatar", Path.class);
            }

            load.invoke(null, avatar);
            return true;
         } catch (ReflectiveOperationException var2) {
            System.err.println("[Westra] Figura load failed for " + avatar + ": " + root(var2));
            return false;
         }
      }
   }

   public static boolean clear() {
      if (!available()) {
         return false;
      } else {
         try {
            UUID id = Interface.aM_.field_1724 != null ? Interface.aM_.field_1724.method_5667() : Interface.aM_.method_1548().method_44717();
            if (id == null) {
               return false;
            } else {
               if (clear == null) {
                  clear = Class.forName("org.figuramc.figura.avatar.AvatarManager").getMethod("clearAvatars", UUID.class);
               }

               clear.invoke(null, id);
               return true;
            }
         } catch (Throwable var1) {
            System.err.println("[Westra] Figura clear failed: " + root(var1));
            return false;
         }
      }
   }

   private static Throwable root(Throwable e) {
      return e.getCause() == null ? e : e.getCause();
   }
}
