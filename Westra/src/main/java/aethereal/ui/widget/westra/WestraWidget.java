package aethereal.ui.widget.westra;

import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;

public abstract class WestraWidget extends Widget implements Interface {
   protected static final float k = 12.0F;
   protected static final float l = 7.0F;
   protected static final float m = 5.0F;
   protected static final float n = 1.75F;
   protected static final float o = 5.5F;
   protected static final float p = 6.25F;
   protected static final float A = 1.75F;
   protected static final float B = 3.0F;
   protected static final float C = 2.5F;
   protected static final float D = 6.5F;

   protected WestraWidget(String name) {
      super(new DragInfo(name, 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   protected void q(DrawEvent event, float x, float y, float width, float height, float animation, boolean rail) {
      WestraStyle.a(event, x, y, width, height, animation, rail);
   }

   protected float r(DrawEvent event, float x, float y, float height, String label, String value, int valueColor, float animation) {
      return WestraStyle.a(event, x, y, height, label, value, valueColor, animation);
   }

   protected static float s(String label, String value) {
      return WestraStyle.a(label, value);
   }

   protected void t(DrawEvent event, float x, float y, float height, float animation) {
      WestraStyle.b(event, x, y, height, animation);
   }
}
