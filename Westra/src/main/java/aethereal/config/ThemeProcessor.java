package aethereal.config;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.lib.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class ThemeProcessor extends ConfigProcessor<ThemeConstructor> {
   private ThemeType e = ThemeType.DARK;

   @Compile
   @Override
   protected List<ThemeConstructor> a(String json) throws Exception {
      if (json != null && !json.isBlank() && !json.trim().startsWith("[")) {
         JSONObject jSONObject = new JSONObject(json);
         this.e = ThemeType.valueOf(jSONObject.a("type", ThemeType.DARK.name()));
         List<ThemeConstructor> listE = this.e();
         if (listE == null) {
            throw new NullPointerException();
         } else {
            listE.clear();
            ThemeInfo[] themeInfoArrValues = ThemeInfo.values();
            if (themeInfoArrValues == null) {
               throw new NullPointerException();
            } else {
               for (ThemeInfo themeInfo : themeInfoArrValues) {
                  if (themeInfo == null) {
                     throw new NullPointerException();
                  }

                  ThemeConstructor themeConstructorA = themeInfo.a(this.e);
                  List<ThemeConstructor> listE2 = this.e();
                  if (themeConstructorA == null) {
                     throw new NullPointerException();
                  }

                  ThemeConstructor themeConstructor = new ThemeConstructor(
                     themeConstructorA.c(), themeConstructorA.d(), themeConstructorA.e(), themeConstructorA.f(), themeConstructorA.g()
                  );
                  if (listE2 == null) {
                     throw new NullPointerException();
                  }

                  listE2.add(themeConstructor);
               }

               if (jSONObject.m("primary")) {
                  this.a(ThemeInfo.PRIMARY).a(jSONObject.h("primary"));
               }

               if (jSONObject.m("colors")) {
                  JSONObject colors = jSONObject.j("colors");

                  for (ThemeInfo info : ThemeInfo.values()) {
                     if (colors.m(info.name())) {
                        this.a(info).a(colors.h(info.name()));
                     }
                  }
               }

               return null;
            }
         }
      } else {
         return this.createDefaultThemes();
      }
   }

   @Compile
   @Override
   protected String a(List<ThemeConstructor> data) throws Exception {
      JSONObject jSONObject = new JSONObject();
      jSONObject.c("type", this.e.name());
      jSONObject.b("primary", this.a(ThemeInfo.PRIMARY).a());
      JSONObject colors = new JSONObject();

      for (ThemeInfo info : ThemeInfo.values()) {
         colors.b(info.name(), this.a(info).a());
      }

      jSONObject.c("colors", colors);
      return jSONObject.a(2);
   }

   @Generated
   public ThemeType a() {
      return this.e;
   }

   @Override
   protected String b() {
      return "theme.westra";
   }

   public ThemeConstructor a(ThemeInfo type) {
      return this.d.stream().filter(constructor -> constructor.c().equalsIgnoreCase(type.a().c())).findFirst().orElse(type.a(this.e));
   }

   public JSONObject f() {
      JSONObject object = new JSONObject();
      object.c("type", this.e.name());
      JSONObject colors = new JSONObject();

      for (ThemeInfo info : ThemeInfo.values()) {
         colors.b(info.name(), this.a(info).a());
      }

      object.c("colors", colors);
      return object;
   }

   public void a(JSONObject object) {
      if (object != null) {
         ThemeType type = ThemeType.valueOf(object.a("type", ThemeType.DARK.name()));
         this.e = type;
         this.b(type);
         if (object.m("colors")) {
            JSONObject colors = object.j("colors");

            for (ThemeInfo info : ThemeInfo.values()) {
               if (colors.m(info.name())) {
                  this.a(info).a(colors.h(info.name()));
               }
            }
         }
      }
   }

   public void a(ThemeType type) {
      if (this.e != type) {
         this.e = type;
         this.b(type);
      }
   }

   public void b(ThemeType type) {
      for (ThemeInfo info : ThemeInfo.values()) {
         ThemeConstructor defaults = info.a(type);
         ThemeConstructor current = this.a(info);
         current.b(defaults.d());
         current.c(defaults.e());
         current.d(defaults.f());
         current.e(defaults.g());
      }
   }

   private List<ThemeConstructor> createDefaultThemes() {
      List<ThemeConstructor> themes = new ArrayList<>();

      for (ThemeInfo themeInfo : ThemeInfo.values()) {
         ThemeConstructor defaults = themeInfo.a(this.e);
         themes.add(new ThemeConstructor(defaults.c(), defaults.d(), defaults.e(), defaults.f(), defaults.g()));
      }

      return themes;
   }

   static {
      NativeMethodLookup.lookup(ThemeProcessor.class, 38);
   }
}
