 package su.sacura.util.impl.math.impl;
 
 import su.sacura.util.impl.math.api.Animation;
 
 public class EaseInOutQuad extends Animation {
   public EaseInOutQuad(int ms, double endPoint) {
     super(ms, endPoint);
   }
   
   protected double getEquation(double x1) {
     double x = x1 / this.duration;
     return (x < 0.5D) ? (2.0D * Math.pow(x, 2.0D)) : (1.0D - Math.pow(-2.0D * x + 2.0D, 2.0D) / 2.0D);
   }
 }


