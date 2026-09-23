 package su.sacura.util.impl.math.impl;
 
 import net.minecraft.util.math.Direction;
 import su.sacura.util.impl.math.api.Animation;
 
 public class EaseBackIn extends Animation {
   private final float easeAmount;
   
   public EaseBackIn(int ms, double endPoint, float easeAmount) {
     super(ms, endPoint);
     this.easeAmount = easeAmount;
   }
   
   public EaseBackIn(int ms, double endPoint, float easeAmount, Direction.AxisDirection direction) {
     super(ms, endPoint, direction);
     this.easeAmount = easeAmount;
   }
   
   protected boolean correctOutput() {
     return true;
   }
   
   protected double getEquation(double x) {
     double x1 = x / this.duration;
     float shrink = this.easeAmount + 1.0F;
     return Math.max(0.0D, 1.0D + shrink * Math.pow(x1 - 1.0D, 3.0D) + this.easeAmount * Math.pow(x1 - 1.0D, 2.0D));
   }
 }


