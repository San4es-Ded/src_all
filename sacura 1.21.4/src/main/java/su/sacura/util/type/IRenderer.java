 package su.sacura.util.type;
 
 import org.joml.Matrix4f;
 
 public interface IRenderer {
   public static final Matrix4f DEFAULT_MATRIX = new Matrix4f();
   
   default void render(double x, double y) {
     render((float)x, (float)y);
   }
   
   default void render(float x, float y) {
     render(DEFAULT_MATRIX, x, y);
   }
   
   default void render(Matrix4f matrix, double x, double y) {
     render(matrix, (float)x, (float)y);
   }
   
   default void render(Matrix4f matrix, float x, float y) {
     render(matrix, x, y, 0.0F);
   }
   
   default void render(double x, double y, double z) {
     render((float)x, (float)y, (float)z);
   }
   
   default void render(float x, float y, float z) {
     render(DEFAULT_MATRIX, x, y, z);
   }
   
   default void render(Matrix4f matrix, double x, double y, double z) {
     render(matrix, (float)x, (float)y, (float)z);
   }
   
   void render(Matrix4f paramMatrix4f, float paramFloat1, float paramFloat2, float paramFloat3);
 }


