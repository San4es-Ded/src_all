package aethereal.handler;

import java.lang.reflect.Method;
import net.minecraft.class_2338;

public final class BaritoneBridge {
   private static Object provider;
   private static boolean checked;
   private static Method goalAndPath;
   private static Method customGoalProcess;
   private static Method pathingBehavior;
   private static Method isPathing;
   private static Method cancelEverything;
   private static Method hasPath;
   private static Method mineProcess;
   private static Method mineByName;
   private static Method mineActive;
   private static Method mineCancel;

   private BaritoneBridge() {
   }

   public static boolean a() {
      if (!checked) {
         checked = true;

         try {
            Class<?> api = Class.forName("baritone.api.BaritoneAPI");
            Object found = api.getMethod("getProvider").invoke(null);
            provider = found.getClass().getMethod("getPrimaryBaritone").invoke(found);
         } catch (Throwable var2) {
            provider = null;
         }
      }

      return provider != null;
   }

   public static void a(class_2338 target) {
      if (a() && target != null) {
         try {
            Class<?> goalBlock = Class.forName("baritone.api.pathing.goals.GoalBlock");
            Object goal = goalBlock.getConstructor(int.class, int.class, int.class)
               .newInstance(target.method_10263(), target.method_10264(), target.method_10260());
            b(goal);
         } catch (Throwable var3) {
         }
      }
   }

   public static void a(int x, int z) {
      if (a()) {
         try {
            Class<?> goalXZ = Class.forName("baritone.api.pathing.goals.GoalXZ");
            b(goalXZ.getConstructor(int.class, int.class).newInstance(x, z));
         } catch (Throwable var3) {
         }
      }
   }

   private static void b(Object goal) throws Exception {
      if (customGoalProcess == null) {
         customGoalProcess = provider.getClass().getMethod("getCustomGoalProcess");
      }

      Object process = customGoalProcess.invoke(provider);
      if (goalAndPath == null) {
         Class<?> goalType = Class.forName("baritone.api.pathing.goals.Goal");
         goalAndPath = process.getClass().getMethod("setGoalAndPath", goalType);
      }

      goalAndPath.invoke(process, goal);
   }

   public static boolean c() {
      if (!a()) {
         return false;
      } else {
         try {
            if (pathingBehavior == null) {
               pathingBehavior = provider.getClass().getMethod("getPathingBehavior");
            }

            Object behavior = pathingBehavior.invoke(provider);
            if (isPathing == null) {
               isPathing = behavior.getClass().getMethod("isPathing");
            }

            return Boolean.TRUE.equals(isPathing.invoke(behavior));
         } catch (Throwable var1) {
            return false;
         }
      }
   }

   public static void d() {
      if (a()) {
         try {
            Object behavior = f();
            if (cancelEverything == null) {
               cancelEverything = behavior.getClass().getMethod("cancelEverything");
            }

            cancelEverything.invoke(behavior);
         } catch (Throwable var1) {
         }
      }
   }

   private static Object f() throws Exception {
      if (pathingBehavior == null) {
         pathingBehavior = provider.getClass().getMethod("getPathingBehavior");
      }

      return pathingBehavior.invoke(provider);
   }

   private static Object g() throws Exception {
      if (mineProcess == null) {
         mineProcess = provider.getClass().getMethod("getMineProcess");
      }

      return mineProcess.invoke(provider);
   }

   public static boolean e() {
      if (!a()) {
         return false;
      } else {
         try {
            Object behavior = f();
            if (hasPath == null) {
               hasPath = behavior.getClass().getMethod("hasPath");
            }

            return Boolean.TRUE.equals(hasPath.invoke(behavior));
         } catch (Throwable var1) {
            return false;
         }
      }
   }

   public static void a(double distance, class_2338... away) {
      if (a() && away.length != 0) {
         try {
            Class<?> goalRunAway = Class.forName("baritone.api.pathing.goals.GoalRunAway");
            b(goalRunAway.getConstructor(double.class, class_2338[].class).newInstance(distance, away));
         } catch (Throwable var4) {
         }
      }
   }

   public static void a(String... blocks) {
      if (a() && blocks.length != 0) {
         try {
            Object process = g();
            if (mineByName == null) {
               mineByName = process.getClass().getMethod("mineByName", String[].class);
            }

            mineByName.invoke(process, blocks);
         } catch (Throwable var2) {
         }
      }
   }

   public static boolean b() {
      if (!a()) {
         return false;
      } else {
         try {
            Object process = g();
            if (mineActive == null) {
               mineActive = process.getClass().getMethod("isActive");
            }

            return Boolean.TRUE.equals(mineActive.invoke(process));
         } catch (Throwable var1) {
            return false;
         }
      }
   }

   public static void a(String name, Object value) {
      if (a()) {
         try {
            Class<?> api = Class.forName("baritone.api.BaritoneAPI");
            Object settings = api.getMethod("getSettings").invoke(null);
            Object setting = settings.getClass().getField(name).get(settings);
            setting.getClass().getField("value").set(setting, value);
         } catch (Throwable var5) {
         }
      }
   }

   public static void f2() {
      if (a()) {
         try {
            Object process = g();
            if (mineCancel == null) {
               mineCancel = process.getClass().getMethod("cancel");
            }

            mineCancel.invoke(process);
         } catch (Throwable var1) {
         }
      }
   }
}
