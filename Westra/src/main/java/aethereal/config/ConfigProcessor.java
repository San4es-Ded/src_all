package aethereal.config;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public abstract class ConfigProcessor<T> extends BaseProcessor {
   public static final String EXTENSION = ".westra";
   protected final File b = new File(aM_.field_1697, "configs");
   protected final File c = new File(aM_.field_1697, "configs\\general");
   protected final List<T> d = new ArrayList<>();

   protected abstract String b();

   @Compile
   @Override
   public void setup() {
      try {
         List<T> list = this.d;
         if (this.b() != null) {
            File fileD = this.d();
            if (!fileD.exists()) {
               fileD.mkdirs();
            }

            File file = new File(fileD, this.b());
            File legacy = new File(fileD, this.b().replace(".westra", ".json"));
            File source = file.exists() ? file : legacy;
            String string = source.exists() ? Files.readString(source.toPath()) : "";
            List<T> listA = this.a(string.length() == 0 ? "[]" : string);
            if (listA != null) {
               list.clear();
               list.addAll(listA);
            }

            if (string.length() == 0) {
               Files.writeString(file.toPath(), this.a(list));
            }
         }
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }

   @Compile
   protected abstract List<T> a(String var1) throws Exception;

   @Compile
   protected abstract String a(List<T> var1) throws Exception;

   @Generated
   public File c() {
      return this.b;
   }

   @Generated
   public File d() {
      return this.c;
   }

   @Generated
   public List<T> e() {
      return this.d;
   }

   @Override
   public void unSetup() {
      if (this.b() != null) {
         try {
            File file = new File(this.d(), this.b());
            Files.writeString(file.toPath(), this.a(this.d));
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   static {
      NativeMethodLookup.lookup(ConfigProcessor.class, 23);
   }
}
