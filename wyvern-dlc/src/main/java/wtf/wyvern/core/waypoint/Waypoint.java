package wtf.wyvern.core.waypoint;

import lombok.Generated;
import wtf.astroguard.J2C.FastNative;

@FastNative
public class Waypoint {
   private final String name;
   private final double x;
   private final double y;
   private final double z;
   private final String dimension;

   public Waypoint(String name, double x, double y, double z, String dimension) {
      this.name = name;
      this.x = x;
      this.y = y;
      this.z = z;
      this.dimension = dimension;
   }

   public Waypoint(double x, double z) {
      this("GPS", x, 0.0D, z, "");
   }

   public Waypoint(String name, double x, double z) {
      this(name, x, 0.0D, z, "");
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public double getX() {
      return this.x;
   }

   @Generated
   public double getZ() {
      return this.z;
   }

   @Generated
   public double getY() {
      return this.y;
   }

   @Generated
   public String getDimension() {
      return this.dimension;
   }
}
