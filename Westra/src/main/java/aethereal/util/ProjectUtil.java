package aethereal.util;

import aethereal.core.Interface;
import aethereal.core.Westra;
import lombok.Generated;
import net.minecraft.class_238;
import net.minecraft.class_4184;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import platform.inject.invokers.GameRendererInvoker;

public class ProjectUtil implements Interface {
   @Generated
   private ProjectUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Vector2f a(double x, double y, double z) {
      class_4184 camera = aM_.method_1561().field_4686;
      Vector3f result3f = new Vector3f(
         (float)(x - camera.method_19326().field_1352), (float)(y - camera.method_19326().field_1351), (float)(z - camera.method_19326().field_1350)
      );
      Quaternionf invCamRot = new Quaternionf(camera.method_23767()).conjugate();
      result3f.rotate(invCamRot);
      return a(result3f, ((GameRendererInvoker)aM_.field_1773).invokeGetFov(camera, aM_.method_61966().method_60637(false), true));
   }

   private static Vector2f a(Vector3f result3f, double fov) {
      if (result3f.z >= 0.0F) {
         return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
      } else {
         float realAspect = (float)aM_.method_22683().method_4489() / aM_.method_22683().method_4506();
         float modifiedAspect = Westra.h().d().t().aB().m() ? Westra.h().d().t().aB().q() : realAspect;
         double halfHeightAtDepth = -result3f.z * Math.tan(Math.toRadians(fov / 2.0));
         double halfWidthAtDepth = halfHeightAtDepth * modifiedAspect;
         double ndcX = result3f.x / halfWidthAtDepth;
         double ndcY = result3f.y / halfHeightAtDepth;
         float screenX = (float)(aM_.method_22683().method_4486() / 2.0F + ndcX * (aM_.method_22683().method_4486() / 2.0F));
         float screenY = (float)(aM_.method_22683().method_4502() / 2.0F - ndcY * (aM_.method_22683().method_4502() / 2.0F));
         return new Vector2f(screenX, screenY);
      }
   }

   public static float[] a(class_238 box) {
      float minX = Float.MAX_VALUE;
      float minY = Float.MAX_VALUE;
      float maxX = -Float.MAX_VALUE;
      float maxY = -Float.MAX_VALUE;

      for (int corner = 0; corner < 8; corner++) {
         double x = (corner & 1) == 0 ? box.field_1323 : box.field_1320;
         double y = (corner & 2) == 0 ? box.field_1322 : box.field_1325;
         double z = (corner & 4) == 0 ? box.field_1321 : box.field_1324;
         Vector2f screen = a(x, y, z);
         if (screen.x() != Float.MAX_VALUE) {
            minX = Math.min(minX, screen.x());
            minY = Math.min(minY, screen.y());
            maxX = Math.max(maxX, screen.x());
            maxY = Math.max(maxY, screen.y());
         }
      }

      return !(maxX <= minX) && !(maxY <= minY) ? new float[]{minX, minY, maxX, maxY} : null;
   }

   public static boolean a(Vector2f screen) {
      return screen.x() != Float.MAX_VALUE
         && screen.y() != Float.MAX_VALUE
         && screen.x() >= 0.0F
         && screen.y() >= 0.0F
         && screen.x() <= aM_.method_22683().method_4486()
         && screen.y() <= aM_.method_22683().method_4502();
   }
}
