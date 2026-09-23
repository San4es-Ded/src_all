package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_5134;
import net.minecraft.class_638;
import net.minecraft.class_745;
import net.minecraft.class_1297.class_5529;

@ModuleRegister(
   a = "Fake Player",
   b = "Ставит рядом клиентского двойника игрока, чтобы проверять на нём бой",
   c = Category.Render
)
public class FakePlayer extends Module implements Interface {
   private static final String[] b = new String[]{
      "Steve", "Alex", "Herobrine", "Nagibator", "Vitalik", "Sanya", "Dimon", "Leha", "KolyaPRO", "Artem", "Nikita", "Timoha", "Zhenya", "MaksFX", "Vladik"
   };
   private final BooleanSetting c = new BooleanSetting("Ходьба", true);
   private final SliderSetting d2 = new SliderSetting("Радиус ходьбы", 2.0F, 0.5F, 6.0F, 0.5F).a(() -> this.c.c());
   private final SliderSetting e = new SliderSetting("Скорость ходьбы", 0.1F, 0.02F, 0.4F, 0.01F).a(() -> this.c.c());
   private final BooleanSetting f2 = new BooleanSetting("Поворот к игроку", true);
   private final BooleanSetting g2 = new BooleanSetting("Тотем", false);
   private final SliderSetting h2 = new SliderSetting("Ударов до тотема", 3.0F, 1.0F, 10.0F, 1.0F).a(() -> this.g2.c());
   private final BooleanSetting i2 = new BooleanSetting("Частицы урона", true);
   private FakePlayer.a j2;
   private class_243 k2 = class_243.field_1353;
   private class_243 l2 = class_243.field_1353;
   private float m2;
   private float n2;
   private int o2;

   public FakePlayer() {
      this.a(new Setting[]{this.c, this.d2, this.e, this.f2, this.g2, this.h2, this.i2});
   }

   @Override
   public void c() {
      this.F();
      super.c();
   }

   public static boolean a(class_1297 entity) {
      return entity instanceof FakePlayer.a;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (this.j2 == null || this.j2.method_31481() || this.j2.method_37908() != aM_.field_1687) {
            this.G();
         }

         this.H();
         this.I();
      } else {
         this.j2 = null;
      }
   }

   private void G() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      String name = b[random.nextInt(b.length)] + random.nextInt(10, 100);
      FakePlayer.a fake = new FakePlayer.a(aM_.field_1687, new GameProfile(UUID.randomUUID(), name));
      fake.method_5838(-random.nextInt(1000000, 2000000));
      this.k2 = aM_.field_1724.method_19538();
      this.m2 = aM_.field_1724.method_36454();
      float radians = this.m2 * (float) (Math.PI / 180.0);
      this.l2 = new class_243(-class_3532.method_15362(radians), 0.0, -class_3532.method_15374(radians));
      this.n2 = 0.0F;
      fake.method_5808(this.k2.field_1352, this.k2.field_1351, this.k2.field_1350, this.m2, 0.0F);
      fake.method_5847(this.m2);
      fake.method_5636(this.m2);
      aM_.field_1687.method_53875(fake);
      this.j2 = fake;
      this.o2 = 0;
      this.H();
   }

   private void I() {
      if (this.j2 != null) {
         class_243 position = this.k2;
         float yaw = this.m2;
         float pitch = 0.0F;
         if (this.c.c()) {
            this.n2 = this.n2 + this.e.c();
            position = this.k2.method_1019(this.l2.method_1021(class_3532.method_15374(this.n2) * this.d2.c()));
            class_243 direction = this.l2.method_1021(class_3532.method_15362(this.n2) >= 0.0F ? 1.0 : -1.0);
            yaw = (float)Math.toDegrees(class_3532.method_15349(-direction.field_1352, direction.field_1350));
         }

         if (this.f2.c()) {
            double dx = aM_.field_1724.method_23317() - this.j2.method_23317();
            double dy = aM_.field_1724.method_23320() - this.j2.method_23320();
            double dz = aM_.field_1724.method_23321() - this.j2.method_23321();
            yaw = (float)Math.toDegrees(class_3532.method_15349(dz, dx)) - 90.0F;
            pitch = (float)(-Math.toDegrees(class_3532.method_15349(dy, Math.hypot(dx, dz))));
         }

         this.j2.method_5808(position.field_1352, position.field_1351, position.field_1350, yaw, pitch);
         this.j2.method_5636(yaw);
         this.j2.method_5683(yaw, 3);
      }
   }

   private void H() {
      if (this.j2 != null) {
         this.j2.method_5673(class_1304.field_6171, this.g2.c() ? new class_1799(class_1802.field_8288) : class_1799.field_8037);
         if (!this.g2.c()) {
            this.o2 = 0;
            this.j2.method_6033(this.j2.method_6063());
         }
      }
   }

   private void F() {
      if (this.j2 != null) {
         if (this.j2.method_37908() instanceof class_638 world) {
            world.method_2945(this.j2.method_5628(), class_5529.field_26999);
         }

         this.j2 = null;
      }
   }

   @EventTarget
   public void a(AttackEvent event) {
      if (this.j2 != null && event.b() == this.j2 && aM_.field_1724 != null) {
         event.a(true);
         aM_.field_1724.method_7324(this.j2);
         if (this.i2.c()) {
            this.J();
         }

         if (this.g2.c()) {
            int limit = (int)this.h2.c().floatValue();
            this.o2++;
            if (this.o2 >= limit) {
               this.o2 = 0;
               this.j2.method_6033(1.0F);
               this.K();
               this.j2.method_6033(this.j2.method_6063());
            } else {
               float left = this.j2.method_6063() * (limit - this.o2) / limit;
               this.j2.method_6033(Math.max(1.0F, left));
            }
         }
      }
   }

   private void K() {
      this.j2.method_5673(class_1304.field_6171, new class_1799(class_1802.field_8288));
      aM_.field_1713.method_3051(this.j2, class_2398.field_11220, 30);
      aM_.field_1687
         .method_8486(this.j2.method_23317(), this.j2.method_23318(), this.j2.method_23321(), class_3417.field_14931, class_3419.field_15248, 1.0F, 1.0F, false);
   }

   private void J() {
      double damage = aM_.field_1724.method_45325(class_5134.field_23721);
      int count = Math.max(1, (int)(damage * 0.5));

      for (int index = 0; index < count; index++) {
         aM_.field_1687
            .method_8406(
               class_2398.field_11209,
               this.j2.method_23317(),
               this.j2.method_23323(0.5),
               this.j2.method_23321(),
               aM_.field_1687.field_9229.method_43059() * 0.1,
               0.0,
               aM_.field_1687.field_9229.method_43059() * 0.1
            );
      }
   }

   public static final class a extends class_745 {
      public a(class_638 world, GameProfile profile) {
         super(world, profile);
      }
   }
}
