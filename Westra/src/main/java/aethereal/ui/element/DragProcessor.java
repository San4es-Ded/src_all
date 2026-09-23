package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.config.ConverterUtil;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.event.ClickEvent;
import aethereal.event.DrawEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.setting.Setting;
import aethereal.ui.widget.HudScale;
import aethereal.util.CursorUtil;
import aethereal.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_408;

public class DragProcessor extends ConfigProcessor<DragInfo> implements Interface {
   private final DragProcessor.b e = new DragProcessor.b();
   private final DragProcessor.b f = new DragProcessor.b();
   private DragInfo g = null;

   @Compile
   @Override
   protected List<DragInfo> a(String json) throws Exception {
      JSONArray jSONArray = new JSONArray(json);

      for (int i = 0; i < jSONArray.a(); i++) {
         JSONObject jSONObjectJ = jSONArray.j(i);
         String strL = jSONObjectJ.l("name");

         for (DragInfo dragInfo : this.e()) {
            if (!(dragInfo instanceof DragInfo)) {
               throw new ClassCastException();
            }

            if (dragInfo.j().equals(strL)) {
               dragInfo.a(jSONObjectJ.f("x"));
               dragInfo.b(jSONObjectJ.f("y"));
               dragInfo.c(jSONObjectJ.f("width"));
               dragInfo.d(jSONObjectJ.f("height"));
               if (jSONObjectJ.m("settings")) {
                  dragInfo.e();
                  JSONObject jSONObjectJ2 = jSONObjectJ.j("settings");

                  for (Setting<?> setting : dragInfo.e().b()) {
                     if (!(setting instanceof Setting)) {
                        throw new ClassCastException();
                     }

                     if (jSONObjectJ2.m(setting.i())) {
                        ConverterUtil.a(setting, jSONObjectJ2.a(setting.i()));
                     }
                  }
               }
            }
         }
      }

      return new ArrayList<>(this.e());
   }

   @Compile
   @Override
   protected String a(List<DragInfo> data) throws Exception {
      JSONArray jSONArray = new JSONArray();

      for (DragInfo dragInfo : data) {
         JSONObject jSONObject = new JSONObject();
         if (!(dragInfo instanceof DragInfo)) {
            throw new ClassCastException();
         }

         jSONObject.c("name", dragInfo.j());
         jSONObject.b("x", dragInfo.c());
         jSONObject.b("y", dragInfo.d());
         jSONObject.b("width", dragInfo.f());
         jSONObject.b("height", dragInfo.g());
         dragInfo.e();
         JSONObject jSONObject2 = new JSONObject();

         for (Setting<?> setting : dragInfo.e().b()) {
            if (!(setting instanceof Setting)) {
               throw new ClassCastException();
            }

            jSONObject2.c(setting.i(), ConverterUtil.a(setting));
         }

         jSONObject.c("settings", jSONObject2);
         jSONArray.a(jSONObject);
      }

      return jSONArray.E(2);
   }

   @Generated
   public DragProcessor.b a() {
      return this.e;
   }

   @Generated
   public DragProcessor.b f() {
      return this.f;
   }

   @Generated
   public DragInfo g() {
      return this.g;
   }

   @Override
   protected String b() {
      return "drag.westra";
   }

   @EventTarget
   public void a(ClickEvent event) {
      if (aM_.field_1755 instanceof class_408) {
         float hudScale = HudScale.a();
         double mouseX = event.f() / hudScale;
         double mouseY = event.g() / hudScale;
         if (event.b() && event.h() == 0) {
            for (DragInfo dragInfo : this.e()) {
               if (dragInfo.k() != 2 && MathUtil.a(mouseX, mouseY, dragInfo.a(), dragInfo.b(), dragInfo.f(), dragInfo.g())) {
                  CursorUtil.a(CursorUtil.a.HAND);
                  this.g = dragInfo;
                  this.g.a(mouseX - dragInfo.a());
                  this.g.b(mouseY - dragInfo.b());
                  break;
               }
            }
         } else if (event.c() && event.h() == 0) {
            this.h();
         } else if (event.d() && this.g != null && event.h() == 0) {
            this.a((float)(mouseX - this.g.h()), (float)(mouseY - this.g.i()), this.g);
         }

         for (DragInfo dragInfo2 : this.e()) {
            if (event.h() == 1 && event.b()) {
               if (MathUtil.a(mouseX, mouseY, dragInfo2.a(), dragInfo2.b(), dragInfo2.f(), dragInfo2.g())) {
                  dragInfo2.e().a(!dragInfo2.e().g());
               }
            } else if (event.h() == 0 && event.c() && dragInfo2.e().g()) {
               for (Element_2<?> element : dragInfo2.e().c()) {
                  element.a(mouseX, mouseY, event.h());
               }
            }
         }
      }
   }

   @EventTarget(
      a = 4
   )
   public void a(DrawEvent event) {
      if (event.b()) {
         if (aM_.field_1755 instanceof class_408) {
            if (this.g == null && !this.e.a() && !this.f.a()) {
               this.h();
            }

            this.e.a(this.g != null, event.g());
            this.f.a(this.g != null, event.g());
            if (!this.e.a() && !this.f.a()) {
               return;
            }

            this.b(event);
            return;
         }

         for (DragInfo dragInfo : this.e()) {
            dragInfo.e().a(false);
         }

         if (this.g != null) {
            this.h();
         }
      }
   }

   private void a(float x, float y, DragInfo dragInfo) {
      int status = dragInfo.k();
      if (status == 2) {
         this.e.a(null);
         this.f.a(null);
      } else {
         boolean onlyY = status == 1;
         if (onlyY) {
            x = dragInfo.a();
         }

         float x2 = MathUtil.b(x, 0.0F, HudScale.c() - dragInfo.f());
         float y2 = MathUtil.b(y, 0.0F, HudScale.d() - dragInfo.g());
         if (!onlyY) {
            x2 = this.a(DragProcessor.a.X, x2, dragInfo);
         } else {
            this.e.a(null);
         }

         float y3 = this.a(DragProcessor.a.Y, y2, dragInfo);
         dragInfo.a(MathUtil.b(x2, 0.0F, HudScale.c() - dragInfo.f()));
         dragInfo.b(MathUtil.b(y3, 0.0F, HudScale.d() - dragInfo.g()));
      }
   }

   private float a(DragProcessor.a axis, float pos, DragInfo dragInfo) {
      float size = axis.b(dragInfo);
      float[] points = new float[]{pos, pos + size / 2.0F, pos + size};
      DragProcessor.b guide = axis == DragProcessor.a.X ? this.e : this.f;
      float bestDistance = 25.0F;
      Float bestGuide = null;
      float snappedPos = pos;

      for (float guidePos : this.a(axis, dragInfo)) {
         for (int i = 0; i < points.length; i++) {
            float distance = Math.abs(points[i] - guidePos);
            if (distance < bestDistance) {
               bestDistance = distance;
               bestGuide = guidePos;
               float f;
               if (i == 0) {
                  f = 0.0F;
               } else {
                  f = i == 1 ? size / 2.0F : size;
               }

               snappedPos = guidePos - f;
            }
         }
      }

      if (bestGuide != null && bestDistance < 5.0F) {
         pos = snappedPos;
         guide.a(bestGuide);
      } else {
         guide.a(null);
      }

      return pos;
   }

   private List<Float> a(DragProcessor.a axis, DragInfo currentElement) {
      List<Float> guides = new ArrayList<>();
      guides.add(0.0F);
      guides.add(axis.a() / 2.0F);
      guides.add(axis.a());

      for (DragInfo other : this.e()) {
         if (other != currentElement && (other.f() != 0.0F || other.g() != 0.0F)) {
            float pos = axis.a(other);
            float size = axis.b(other);
            guides.add(pos);
            guides.add(pos + size / 2.0F);
            guides.add(pos + size);
         }
      }

      return guides;
   }

   private void b(DrawEvent event) {
      if (this.e.a()) {
         event.d()
            .a(
               event.i(),
               this.e.c() * HudScale.a() - 0.5F,
               0.0F,
               0.5F,
               aM_.method_22683().method_4506() / aM_.method_22683().method_4476(2, aM_.method_1573()),
               ColorUtil.a(255, 255, 255, (int)(this.e.b().c() * 200.0F))
            );
      }

      if (this.f.a()) {
         event.d()
            .a(
               event.i(),
               0.0F,
               this.f.c() * HudScale.a() - 0.5F,
               aM_.method_22683().method_4489() / aM_.method_22683().method_4476(2, aM_.method_1573()),
               0.5F,
               ColorUtil.a(255, 255, 255, (int)(this.f.b().c() * 200.0F))
            );
      }
   }

   private void h() {
      CursorUtil.a(CursorUtil.a.DEFAULT);
      this.g = null;
      this.f.a(null);
      this.e.a(null);
   }

   static {
      NativeMethodLookup.lookup(DragProcessor.class, 26);
   }

   static enum a {
      X,
      Y;

      float a() {
         return this == X ? HudScale.c() : HudScale.d();
      }

      float a(DragInfo info) {
         return this == X ? info.a() : info.b();
      }

      float b(DragInfo info) {
         return this == X ? info.f() : info.g();
      }
   }

   static class b {
      private final AnimationUtil a = new AnimationUtil();
      private Float b = null;
      private boolean c = false;

      @Generated
      public AnimationUtil b() {
         return this.a;
      }

      @Generated
      public Float c() {
         return this.b;
      }

      @Generated
      public boolean d() {
         return this.c;
      }

      void a(Float newPosition) {
         if (newPosition == null) {
            this.c = false;
         } else if (!newPosition.equals(this.b)) {
            this.b = newPosition;
            this.c = true;
         }
      }

      void a(boolean active, float tickDelta) {
         if (this.b != null) {
            boolean should = active && this.c;
            this.a.a(active && this.c);
            this.a.a(0.0F, 1.0F, 0.3F, EasingList.i, tickDelta);
            if (!should && this.a.c() <= 0.0F) {
               this.b = null;
            }
         }
      }

      boolean a() {
         return this.b != null && this.a.c() > 0.0F;
      }
   }
}
