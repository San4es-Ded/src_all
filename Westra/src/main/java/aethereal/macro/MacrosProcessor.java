package aethereal.macro;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.core.EventTarget;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.event.KeyEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.util.KeyUtil;
import java.util.ArrayList;
import java.util.List;

public class MacrosProcessor extends ConfigProcessor<MacrosConstructor> {
   @Compile
   @Override
   protected List<MacrosConstructor> a(String json) throws Exception {
      JSONArray jSONArray = new JSONArray(json);
      ArrayList arrayList = new ArrayList();

      for (int i = 0; i < jSONArray.a(); i++) {
         JSONObject jSONObjectJ = jSONArray.j(i);
         arrayList.add(new MacrosConstructor(jSONObjectJ.l("key"), jSONObjectJ.l("command")));
      }

      return arrayList;
   }

   @Compile
   @Override
   protected String a(List<MacrosConstructor> data) throws Exception {
      JSONArray jSONArray = new JSONArray();

      for (MacrosConstructor macrosConstructor : data) {
         JSONObject jSONObject = new JSONObject();
         if (!(macrosConstructor instanceof MacrosConstructor)) {
            throw new ClassCastException();
         }

         jSONObject.c("key", macrosConstructor.a());
         jSONObject.c("command", macrosConstructor.b());
         jSONArray.a(jSONObject);
      }

      return jSONArray.E(2);
   }

   @Override
   protected String b() {
      return "macros.westra";
   }

   @EventTarget
   public void a(KeyEvent event) {
      if (event.d() == 1 && aM_.field_1755 == null) {
         for (MacrosConstructor constructor : Westra.h().d().d().e()) {
            if (KeyUtil.a(event.b()) == KeyUtil.a(constructor.a())) {
               aM_.field_1724.field_3944.method_45729(constructor.b());
            }
         }
      }
   }

   public List<MacrosConstructor> a() {
      return new ArrayList<>(this.d);
   }

   public void a(String str, String str2) {
      this.d.add(new MacrosConstructor(str, str2));
   }

   public void b(String str) {
      this.d.removeIf(macro -> macro.a().equals(str));
   }

   public void f() {
      this.d.clear();
   }

   static {
      NativeMethodLookup.lookup(MacrosProcessor.class, 32);
   }
}
