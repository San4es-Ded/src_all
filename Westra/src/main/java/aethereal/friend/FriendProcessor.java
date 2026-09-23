package aethereal.friend;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.core.EventTarget;
import aethereal.core.NativeMethodLookup;
import aethereal.event.BackendEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FriendProcessor extends ConfigProcessor<FriendConstructor> {
   @Compile
   @Override
   protected List<FriendConstructor> a(String json) throws Exception {
      JSONArray jSONArray = new JSONArray(json);
      ArrayList arrayList = new ArrayList();

      for (int i = 0; i < jSONArray.a(); i++) {
         arrayList.add(new FriendConstructor(jSONArray.j(i).l("name")));
      }

      return arrayList;
   }

   @Compile
   @Override
   protected String a(List<FriendConstructor> data) throws Exception {
      JSONArray jSONArray = new JSONArray();

      for (FriendConstructor friendConstructor : data) {
         JSONObject jSONObject = new JSONObject();
         jSONObject.c("name", friendConstructor.a());
         jSONArray.a(jSONObject);
      }

      return jSONArray.E(2);
   }

   @Override
   protected String b() {
      return "friends.westra";
   }

   @EventTarget
   public void a(BackendEvent event) {
      if (event.b() && "friend".equals(event.d().b())) {
         String payload = event.d().c();
         String minecraft;
         if ("rename".equals(event.d().a().a(payload, "type")) && (minecraft = event.d().a().a(payload, "minecraft")) != null) {
            this.b(minecraft);
            this.unSetup();
         }
      }
   }

   public List<FriendConstructor> a() {
      return new ArrayList<>(this.d);
   }

   public void b(String str) {
      if (!this.d(str)) {
         this.d.add(new FriendConstructor(str));
      }
   }

   public void c(String str) {
      this.d.removeIf(friend -> friend.a().equalsIgnoreCase(str));
   }

   public boolean d(String name) {
      return this.d.stream().anyMatch(friend -> friend.a().equalsIgnoreCase(name));
   }

   public void f() {
      this.d.clear();
   }

   static {
      NativeMethodLookup.lookup(FriendProcessor.class, 30);
   }
}
