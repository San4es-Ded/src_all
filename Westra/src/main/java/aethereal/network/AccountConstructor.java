package aethereal.network;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_1068;
import net.minecraft.class_2960;

public class AccountConstructor {
   private String a;
   private boolean b;
   private boolean c;

   @Generated
   public void a(String name) {
      this.a = name;
   }

   @Generated
   public void a(boolean selected) {
      this.b = selected;
   }

   @Generated
   public void b(boolean favorited) {
      this.c = favorited;
   }

   @Generated
   public AccountConstructor() {
   }

   @Generated
   public AccountConstructor(String name, boolean selected, boolean favorited) {
      this.a = name;
      this.b = selected;
      this.c = favorited;
   }

   @Generated
   public String b() {
      return this.a;
   }

   @Generated
   public boolean c() {
      return this.b;
   }

   @Generated
   public boolean d() {
      return this.c;
   }

   public AccountConstructor(String name) {
      this.a = name;
      this.b = true;
   }

   public class_2960 a() {
      UUID uuid = this.a != null && !this.a.isEmpty() ? UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.a).getBytes(StandardCharsets.UTF_8)) : new UUID(0L, 0L);
      return class_1068.method_4648(uuid).comp_1626();
   }
}
