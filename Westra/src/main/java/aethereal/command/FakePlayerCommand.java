package aethereal.command;

import aethereal.util.ChatUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_2172;
import net.minecraft.class_745;

@Command(
   a = "fakeplayer"
)
public class FakePlayerCommand extends BaseCommand {
   private static final List<Integer> c = new ArrayList<>();
   private static int d = 900000000;

   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(this.a("clear").executes(context -> {
         q();
         ChatUtil.a("Клоны убраны.");
         return 1;
      }))).then(this.a("off").executes(context2 -> {
         q();
         ChatUtil.a("Клоны убраны.");
         return 1;
      }))).executes(context3 -> {
         if (aM_.field_1687 != null && aM_.field_1724 != null) {
            GameProfile source = aM_.field_1724.method_7334();
            GameProfile profile = new GameProfile(UUID.randomUUID(), source.getName());
            profile.getProperties().putAll(source.getProperties());
            class_745 fake = new class_745(aM_.field_1687, profile);
            fake.method_5838(d);
            c.add(d);
            d++;
            double yaw = Math.toRadians(aM_.field_1724.method_36454());
            double x = aM_.field_1724.method_23317() - Math.sin(yaw) * 2.0;
            double z = aM_.field_1724.method_23321() + Math.cos(yaw) * 2.0;
            fake.method_5808(x, aM_.field_1724.method_23318(), z, aM_.field_1724.method_36454() + 180.0F, 0.0F);
            fake.field_6014 = x;
            fake.field_6036 = aM_.field_1724.method_23318();
            fake.field_5969 = z;
            fake.field_6038 = x;
            fake.field_5971 = aM_.field_1724.method_23318();
            fake.field_5989 = z;
            fake.method_6033(aM_.field_1724.method_6063());

            for (class_1304 slot : class_1304.values()) {
               fake.method_5673(slot, aM_.field_1724.method_6118(slot).method_7972());
            }

            aM_.field_1687.method_53875(fake);
            ChatUtil.a("Клон создан. Всего: &c" + c.size() + "&7. Убрать — .fakeplayer clear");
            return 1;
         } else {
            return 1;
         }
      });
   }

   private static void q() {
      if (aM_.field_1687 == null) {
         c.clear();
      } else {
         for (Integer id : c) {
            class_1297 entity = aM_.field_1687.method_8469(id);
            if (entity != null) {
               entity.method_31472();
            }
         }

         c.clear();
      }
   }
}
