 package su.sacura.util.impl.render.other;
 
 import net.minecraft.client.render.Frustum;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.math.Box;
 import net.minecraft.util.math.Vec3d;
 import org.jetbrains.annotations.NotNull;
 import org.joml.Matrix4f;
 import org.joml.Matrix4fc;
 import org.joml.Vector3f;
 import org.joml.Vector4d;
 import org.joml.Vector4f;
 import org.lwjgl.opengl.GL11;
 import su.sacura.mixin.accessors.WorldRendererAccessor;
 import su.sacura.util.impl.render.RenderWorld;
 import su.sacura.util.type.MinecraftWrapper;
 
 public final class Projection implements MinecraftWrapper {
   private Projection() {
     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
   
   @NotNull
   public static Vec3d worldSpaceToScreenSpace(Vec3d pos) {
     Vector3f delta = pos.toVector3f();
     int[] viewport = new int[4];
     GL11.glGetIntegerv(2978, viewport);
     Vector3f target = new Vector3f();
     Vector4f transformedCoordinates = (new Vector4f(delta.x, delta.y, delta.z, 1.0F)).mul((Matrix4fc)RenderWorld.lastWorldSpaceMatrix.getPositionMatrix());
     Matrix4f matrixProj = new Matrix4f((Matrix4fc)RenderWorld.lastProjMat);
     matrixProj.project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
     return new Vec3d(target.x / mc.getWindow().getScaleFactor(), (mc.getWindow().getHeight() - target.y) / mc.getWindow().getScaleFactor(), target.z);
   }
   
   public static double getDistanceToGround() {
     for (double y = mc.player.getY(); y > 0.0D; y -= 0.1D) {
       if (!mc.world.getBlockState(mc.player.getBlockPos().down((int)(mc.player.getY() - y + 1.0D))).isAir())
         return mc.player.getY() - y;
     } 
     return 256.0D;
   }
   
   @NotNull
   public static Vec3d[] getVec3ds(Entity ent, Vec3d pos) {
     Box axisAlignedBB2 = ent.getBoundingBox();
     Box axisAlignedBB = new Box(axisAlignedBB2.minX - ent.getX() + pos.x - 0.10000000149011612D, axisAlignedBB2.minY - ent.getY() + pos.y - 0.10000000149011612D, axisAlignedBB2.minZ - ent.getZ() + pos.z - 0.10000000149011612D, axisAlignedBB2.maxX - ent.getX() + pos.x + 0.10000000149011612D, axisAlignedBB2.maxY - ent.getY() + pos.y + 0.10000000149011612D, axisAlignedBB2.maxZ - ent.getZ() + pos.z + 0.10000000149011612D);
     return new Vec3d[] { new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) };
   }
   
   public static boolean canSee(Box box) {
     if (box == null || mc.worldRenderer == null)
       return false; 
     Frustum frustum = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
     return (frustum != null && frustum.isVisible(box));
   }
   
   public static boolean cantSee(Vector4d vec) {
     return (vec == null || (vec.x < 0.0D && vec.z < 1.0D) || (vec.y < 0.0D && vec.w < 1.0D));
   }
   
   public static double centerX(Vector4d vec) {
     return vec.x + (vec.z - vec.x) / 2.0D;
   }
 }


