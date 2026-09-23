package aethereal.render;

public class EasingList {
   public static final double a = 1.7015791506357025;
   public static final double b = 2.5949079670432127;
   public static final double c = 2.701578217685146;
   public static final double d = 2.094396299644887;
   public static final double e = 1.3962634699317127;
   public static final EasingList.a f = value -> (float)(1.0 - Math.cos(value * 3.1415928959671753 / 2.0));
   public static final EasingList.a g = value -> (float)Math.sin(value * 3.1415928959671753 / 2.0);
   public static final EasingList.a h = value -> (float)(-(Math.cos(3.1415928959671753 * value) - 1.0) / 2.0);
   public static final EasingList.a i = value -> (float)(1.0 - Math.sqrt(1.0 - Math.pow(value, 2.0)));
   public static final EasingList.a j = value -> (float)Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
   public static final EasingList.a k = value -> (float)(
      value < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * value, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * value + 2.0, 2.0)) + 1.0) / 2.0
   );
   public static final EasingList.a l = value -> value != 0.0 && value != 1.0
      ? (float)(Math.pow(-2.0, 10.0 * value - 10.0) * Math.sin((value * 10.0 - 10.75) * 2.094396299644887))
      : value;
   public static final EasingList.a m = value -> value != 0.0 && value != 1.0
      ? (float)(Math.pow(2.0, -10.0 * value) * Math.sin((value * 10.0 - 0.75) * 2.094396299644887) + 1.0)
      : value;
   public static final EasingList.a n = value -> value != 0.0 && value != 1.0
      ? (float)(
         value < 0.5
            ? -(Math.pow(2.0, 20.0 * value - 10.0) * Math.sin((20.0 * value - 11.125) * 1.3962634699317127)) / 2.0
            : Math.pow(2.0, -20.0 * value + 10.0) * Math.sin((20.0 * value - 11.125) * 1.3962634699317127) / 2.0 + 1.0
      )
      : value;
   public static final EasingList.a o = value -> value != 0.0 ? (float)Math.pow(2.0, 10.0 * value - 10.0) : value;
   public static final EasingList.a p = value -> value != 1.0 ? (float)(1.0 - Math.pow(2.0, -10.0 * value)) : value;
   public static final EasingList.a q = value -> value != 0.0 && value != 1.0
      ? (float)(value < 0.5 ? Math.pow(2.0, 20.0 * value - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * value + 10.0)) / 2.0)
      : value;
   public static final EasingList.a r = value -> (float)(2.701578217685146 * Math.pow(value, 3.0) - 1.7015791506357025 * Math.pow(value, 2.0));
   public static final EasingList.a s = value -> (float)(1.0 + 2.701578217685146 * Math.pow(value - 1.0, 3.0) + 1.7015791506357025 * Math.pow(value - 1.0, 2.0));
   public static final EasingList.a t = value -> value;
   public static final EasingList.a u = value -> (float)(
      value < 0.5
         ? Math.pow(2.0 * value, 2.0) * (7.189816336825345 * value - 2.5949079670432127) / 2.0
         : (Math.pow(2.0 * value - 2.0, 2.0) * (3.594908086491756 * (value * 2.0 - 2.0) + 2.5949079670432127) + 2.0) / 2.0
   );
   public static final EasingList.a v = value -> {
      if (value < 0.36363636363636365) {
         return (float)(7.5625 * Math.pow(value, 2.0));
      } else {
         return value < 0.7272727272727273
            ? (float)(7.5625 * Math.pow(value - 0.5454545454545454, 2.0) + 0.75)
            : (float)(
               value < 0.9090909090909091
                  ? 7.5625 * Math.pow(value - 0.8181818181818182, 2.0) + 0.9375
                  : 7.5625 * Math.pow(value - 0.9545454545454546, 2.0) + 0.984375
            );
      }
   };
   public static final EasingList.a w = value -> (float)(1.0 - v.ease((float)(1.0 - value)));
   public static final EasingList.a x = value -> (float)(
      value < 0.5 ? (1.0 - v.ease((float)(1.0 - 2.0 * value))) / 2.0 : (1.0 + v.ease((float)(2.0 * value - 1.0))) / 2.0
   );
   public static final EasingList.a y = x2 -> x2 < 0.5 ? 16.0F * x2 * x2 * x2 * x2 * x2 : (float)(1.0 - Math.pow(-2.0F * x2 + 2.0F, 5.0) / 2.0);

   @FunctionalInterface
   public interface a {
      float ease(float var1);
   }
}
