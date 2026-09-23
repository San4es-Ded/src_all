package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1299;
import net.minecraft.class_1686;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_742;
import net.minecraft.class_7439;
import net.minecraft.class_8038;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Auto Dodge",
   b = "Автоматически уклоняется от выбранных целей",
   c = Category.Movement
)
public class AutoDodge extends Module {
   private final Map<Integer, AutoDodge.b> c = new HashMap<>();
   int b = 0;

   @Override
   public void c() {
      this.c.clear();
      super.c();
   }

   @EventTarget
   public void a(TickEvent tickEvent) {
      Iterator<Entry<Integer, AutoDodge.b>> iterator = this.c.entrySet().iterator();

      while (iterator.hasNext()) {
         if (aM_.field_1687.method_8469(iterator.next().getKey()) == null) {
            iterator.remove();
         }
      }

      class_238 playerHitboxExpanded = aM_.field_1724.method_5829().method_1014(2.0);

      for (class_1686 potionEntity : aM_.field_1687
         .method_8390(class_1686.class, aM_.field_1724.method_5829().method_1014((Integer)aM_.field_1690.method_42503().method_41753() * 16), p -> true)) {
         AutoDodge.b matched = this.c.get(potionEntity.method_5628());
         if (matched != null) {
            boolean trace = a(potionEntity, playerHitboxExpanded);
            int rgba = 0xFF000000 | matched.a & 16777215;
            if (!trace || Westra.h().d().e().d(matched.b)) {
               return;
            }

            if (rgba != -13447886 && rgba != -16776961 || !(aM_.field_1724.method_5739(potionEntity) > 2.300000381469741) || this.b < 0) {
               return;
            }

            class_1799 kelp = class_1802.field_8551.method_7854();
            if (!aM_.field_1724.method_7357().method_7904(kelp) && InventoryUtil.b(class_1802.field_8551) != -1) {
               if (Westra.h().d().v().b().a().isEmpty()) {
                  if (Rotation.b().a(new Rotation(aM_.field_1724.method_36454(), aM_.field_1724.method_36455())) < 20.0) {
                     this.b++;
                  }

                  if (this.b >= 2) {
                     this.b = 5;
                     Westra.h().d().v().b().a(class_1802.field_8551.method_7854());
                  }
               }

               Rotation aimRotation = Rotation.a(aM_.field_1724.method_33571(), potionEntity.method_33571());
               Westra.h().d().k().a(new Rotation(aimRotation.c() + MathUtil.a(-3.0F, 3.0F), aimRotation.d() + MathUtil.a(-3.0F, 3.0F)), 180.0F, 1, 1);
               break;
            }
         }
      }

      this.b++;
   }

   @EventTarget
   public void a(PacketEvent packetEvent) {
      if (packetEvent.c() && aM_.field_1724 != null && aM_.field_1687 != null) {
         this.a(packetEvent.d());
         if (packetEvent.c()
            && packetEvent.d() instanceof class_7439 class_7439VarD
            && class_7439VarD.comp_763().getString().equals("На этой анархии этот предмет не работает")) {
            this.b = -50;
         }
      }
   }

   private void a(class_2596<?> packet) {
      if (packet instanceof class_8038<?> bundlePacket) {
         for (class_2596<?> innerPacket : bundlePacket.method_48324()) {
            this.a(innerPacket);
         }
      } else {
         if (packet instanceof class_2604 spawnPacket) {
            if (spawnPacket.method_11169() != class_1299.field_6045) {
               return;
            }

            Map<String, AutoDodge.a> holders = this.q();
            class_243 spawnPosition = new class_243(spawnPacket.method_11175(), spawnPacket.method_11174(), spawnPacket.method_11176());
            class_243 spawnVelocity = new class_243(spawnPacket.method_11170(), spawnPacket.method_11172(), spawnPacket.method_11173());
            double bestDistance = 1.7976922776554304E308;
            String matchedNick = null;
            int matchedRgb = 0;

            for (Entry<String, AutoDodge.a> holderEntry : holders.entrySet()) {
               AutoDodge.a holder = holderEntry.getValue();
               double distance = spawnPosition.method_1022(holder.b);
               if (distance <= 25.0) {
                  if (holder.b.field_1351 - spawnPosition.field_1351 > 2.0) {
                     if (new class_243(spawnPosition.field_1352 - holder.b.field_1352, 0.0, spawnPosition.field_1350 - holder.b.field_1350).method_1033()
                           < 15.0
                        && distance < bestDistance) {
                        bestDistance = distance;
                        matchedRgb = holder.a;
                        matchedNick = holderEntry.getKey();
                     }
                  } else if ((
                        spawnVelocity.method_1027() <= 9.99999773128142E-7
                           || spawnVelocity.method_1029().method_1026(holder.c.method_1029()) > 0.10000000396251493
                     )
                     && distance < bestDistance) {
                     bestDistance = distance;
                     matchedRgb = holder.a;
                     matchedNick = holderEntry.getKey();
                  }
               }
            }

            if (matchedNick != null) {
               this.b = 0;
               this.c.put(spawnPacket.method_11167(), new AutoDodge.b(matchedRgb, matchedNick));
            }
         }
      }
   }

   private Map<String, AutoDodge.a> q() {
      HashMap<String, AutoDodge.a> result = new HashMap<>();

      for (class_742 class_746Var : aM_.field_1687.method_18456()) {
         class_1799 mainHand = class_746Var.method_6047();
         class_1799 offHand = class_746Var.method_6079();
         int splashColor = mainHand.method_7909() == class_1802.field_8436 ? a(mainHand) : (offHand.method_7909() == class_1802.field_8436 ? a(offHand) : -1);
         if (splashColor >= 0 && class_746Var != aM_.field_1724 && aM_.field_1724.method_5858(class_746Var) <= 400.0) {
            result.put(class_746Var.method_5477().getString(), new AutoDodge.a(splashColor, class_746Var.method_19538(), class_746Var.method_5828(1.0F)));
         }
      }

      return result;
   }

   private static boolean a(class_1686 potionEntity, class_238 expandedPlayer) {
      class_243 velocity = potionEntity.method_18798();
      class_243 position = potionEntity.method_19538();

      for (int step = 0;
         step < 70
            && velocity.method_1027() >= 9.99999773128142E-7
            && position.field_1351 >= aM_.field_1687.method_31607()
            && position.field_1351 <= aM_.field_1687.method_31607() + aM_.field_1687.method_31605();
         step++
      ) {
         double drag = aM_.field_1687.method_8316(class_2338.method_49638(position)).method_15767(class_3486.field_15517)
            ? 0.8000000016738433
            : 0.9900000205305426;
         velocity = new class_243(velocity.field_1352 * drag, (velocity.field_1351 - 0.0500000024232657) * drag, velocity.field_1350 * drag);
         class_243 nextPosition = position.method_1019(velocity);
         class_3959 ctx = new class_3959(position, nextPosition, class_3960.field_17558, class_242.field_1348, potionEntity);
         class_3965 blockHit = aM_.field_1687.method_17742(ctx);
         if (blockHit.method_17783() == class_240.field_1332) {
            return a(expandedPlayer, position, blockHit.method_17784());
         }

         if (a(expandedPlayer, position, nextPosition)) {
            return true;
         }

         position = nextPosition;
      }

      return false;
   }

   private static boolean a(class_238 box, class_243 first, class_243 second) {
      return new class_238(
            Math.min(first.field_1352, second.field_1352),
            Math.min(first.field_1351, second.field_1351),
            Math.min(first.field_1350, second.field_1350),
            Math.max(first.field_1352, second.field_1352),
            Math.max(first.field_1351, second.field_1351),
            Math.max(first.field_1350, second.field_1350)
         )
         .method_1014(0.11999995180429479)
         .method_994(box);
   }

   private static int a(class_1799 itemStack) {
      class_1844 contents = (class_1844)itemStack.method_57824(class_9334.field_49651);
      return contents != null ? contents.method_8064() & 16777215 : 0;
   }

   static final class a {
      final int a;
      final class_243 b;
      final class_243 c;

      a(int color, class_243 position, class_243 lookDirection) {
         this.a = color;
         this.b = position;
         this.c = lookDirection;
      }

      public int a() {
         return this.a;
      }

      public class_243 b() {
         return this.b;
      }

      public class_243 c() {
         return this.c;
      }
   }

   static final class b {
      final int a;
      final String b;

      b(int color, String nick) {
         this.a = color;
         this.b = nick;
      }

      public int a() {
         return this.a;
      }

      public String b() {
         return this.b;
      }
   }
}
