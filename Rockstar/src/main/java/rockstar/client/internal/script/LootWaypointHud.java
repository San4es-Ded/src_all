package rockstar.client.internal.script;





import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.event.*;
import rockstar.client.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.game.WorldChangeEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.utility.render.ColorRGBA;

public class LootWaypointHud extends DynamicIslandLabelStatus {
   private final List<LootWaypointHud.InternalType0097> internalField0416 = new CopyOnWriteArrayList<>();
   private String internalField0248;
   private int internalField0227 = -1;
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1x -> {
      if (localValue1x.getPacket() instanceof GameMessageS2CPacket localValue2) {
         String localValue14 = localValue2.content().getString().replaceAll("\\n", " ").replaceAll("[^\\p{L}\\p{N}\\s\\[\\]:.-]", "").replaceAll("\\s{2,}", " ").trim();
         if (localValue2.content().getString().contains("\u041f\u043e\u044f\u0432\u0438\u043b\u0441\u044f")) {
            Matcher localValue4 = Pattern.compile("\\[([^\\]]+)\\]").matcher(localValue14);
            Matcher localValue5 = Pattern.compile("\u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430\u0445\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)")
               .matcher(localValue14);
            Matcher localValue6 = Pattern.compile("\u0423\u0440\u043e\u0432\u0435\u043d\u044c \u043b\u0443\u0442\u0430:\\s*(\\S+)").matcher(localValue14);
            if (localValue4.find() && localValue5.find()) {
               String localValue7 = localValue4.group(1);
               String localValue8 = localValue6.find() ? localValue6.group(1) : null;

               for (LootWaypointHud.InternalType0098 localValue12 : LootWaypointHud.InternalType0098.values()) {
                  if (localValue7.toLowerCase().contains(localValue12.internalMethod00988().toLowerCase())) {
                     this.internalField0416.removeIf(localValue1xx -> localValue1xx.internalField0331 == localValue12);
                     if (localValue8 != null && !localValue8.isEmpty()) {
                        String localValue13 = localValue8 + " " + localValue12.internalMethod00988();
                        this.internalField0416.add(new LootWaypointHud.InternalType0097(localValue13, System.currentTimeMillis() + localValue12.internalMethod01446(), localValue12));
                        this.internalField0227 = ServerUtils.internalField0228;
                        if (this.internalField0227 == ServerUtils.internalField0228) {
                           RockstarClient.getInstance()
                              .internalMethod06121()
                              .internalMethod05412(localValue13, Integer.parseInt(localValue5.group(1)), Integer.parseInt(localValue5.group(2)), Integer.parseInt(localValue5.group(3)));
                        }
                     }
                     break;
                  }
               }
            }
         } else {
            for (LootWaypointHud.InternalType0098 localValue21 : LootWaypointHud.InternalType0098.values()) {
               if (localValue14.equalsIgnoreCase(localValue21.internalMethod00988())) {
                  this.internalField0248 = localValue21.internalMethod00988();
                  break;
               }
            }

            if (localValue14.toLowerCase().startsWith("\u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b")) {
               Matcher localValue16 = Pattern.compile("\u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b:?\\s*(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)", 2)
                  .matcher(localValue14);
               if (localValue16.find() && this.internalField0248 != null) {
                  for (LootWaypointHud.InternalType0098 localValue23 : LootWaypointHud.InternalType0098.values()) {
                     if (this.internalField0248.equalsIgnoreCase(localValue23.internalMethod00988())) {
                        this.internalField0416.removeIf(localValue1xx -> localValue1xx.internalField0331 == localValue23);
                        this.internalField0416
                           .add(
                              new LootWaypointHud.InternalType0097(localValue23.internalMethod00988(), System.currentTimeMillis() + localValue23.internalMethod01446(), localValue23)
                           );
                        this.internalField0227 = ServerUtils.internalField0228;
                        RockstarClient.getInstance()
                           .internalMethod06121()
                           .internalMethod05412(
                              localValue23.internalMethod00988(), Integer.parseInt(localValue16.group(1)), Integer.parseInt(localValue16.group(2)), Integer.parseInt(localValue16.group(3))
                           );
                        break;
                     }
                  }

                  this.internalField0248 = null;
               }
            }
         }
      }
   };
   private final EventListener<WorldChangeEvent> internalField0158 = localValue1x -> {
      if (ServerUtils.internalField0228 != this.internalField0227) {
         this.internalField0416.forEach(localValue0 -> RockstarClient.getInstance().internalMethod06121().internalMethod06386(localValue0.internalMethod06860()));
         this.internalField0416.clear();
         this.internalField0248 = null;
      }
   };

   public LootWaypointHud(MultiSelectSetting localValue1) {
      super(localValue1, "events");
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   @Override
   public void prepare(DynamicIslandHud localValue1) {
      long localValue2 = System.currentTimeMillis();
      this.internalMethod04869(localValue2);
      if (!this.internalField0416.isEmpty()) {
         LootWaypointHud.InternalType0097 localValue4 = this.internalField0416.getFirst();
         long localValue5 = localValue4.internalMethod01566() - localValue2;
         if (localValue5 > 0L) {
            int localValue7 = (int)(localValue5 / 1000L);
            int localValue8 = localValue7 / 60;
            int localValue9 = localValue7 % 60;
            String localValue10 = String.format("%d:%02d", localValue8, localValue9);
            ColorRGBA localValue11 = this.internalMethod05029(localValue4.internalField0331);
            this.internalMethod01781(
               Integer.parseInt(localValue10.split(":")[0]) + ":", "", Integer.parseInt(localValue10.split(":")[1]), localValue4.internalField0331.internalField0248, localValue11
            );
         }
      }

      super.prepare(localValue1);
   }

   private ColorRGBA internalMethod05029(LootWaypointHud.InternalType0098 localValue1) {
      return switch (localValue1) {
         case internalField0331 -> new ColorRGBA(138.0F, 43.0F, 226.0F);
         case internalField0330 -> new ColorRGBA(255.0F, 69.0F, 0.0F);
         case internalField1124 -> new ColorRGBA(255.0F, 140.0F, 0.0F);
         case internalField1123 -> new ColorRGBA(70.0F, 130.0F, 180.0F);
         case internalField1122 -> new ColorRGBA(243.0F, 196.0F, 82.0F);
         case internalField1121 -> new ColorRGBA(139.0F, 222.0F, 221.0F);
         case internalField1528 -> new ColorRGBA(141.0F, 99.0F, 184.0F);
         case internalField1527 -> new ColorRGBA(41.0F, 253.0F, 5.0F);
         case internalField1526 -> new ColorRGBA(90.0F, 158.0F, 152.0F);
      };
   }

   @Override
   public boolean canShow() {
      this.internalMethod04869(System.currentTimeMillis());
      return !this.internalField0416.isEmpty();
   }

   private void internalMethod04869(long localValue1) {
      this.internalField0416.removeIf(localValue2 -> {
         if (localValue1 < localValue2.internalMethod01566()) {
            return false;
         } else {
            RockstarClient.getInstance().internalMethod06121().internalMethod06386(localValue2.internalMethod06860());
            return true;
         }
      });
   }

   static final class InternalType0097 {
      private final String internalField0248;
      private final long internalField0229;
      final LootWaypointHud.InternalType0098 internalField0331;

      InternalType0097(String localValue1, long localValue2, LootWaypointHud.InternalType0098 localValue4) {
         this.internalField0248 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0331 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0097[waypointName=" + this.internalField0248 + ", expiresAt=" + this.internalField0229 + ", type=" + this.internalField0331 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0331);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         LootWaypointHud.InternalType0097 other = (LootWaypointHud.InternalType0097) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0331, other.internalField0331);
      }

      public String internalMethod06860() {
         return this.internalField0248;
      }

      public long internalMethod01566() {
         return this.internalField0229;
      }

      public LootWaypointHud.InternalType0098 internalMethod00669() {
         return this.internalField0331;
      }
   }

   static enum InternalType0098 {
      internalField0331("\u041c\u0438\u0441\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0410\u043b\u0442\u0430\u0440\u044c", 360000L),
      internalField0330("\u041c\u0430\u044f\u043a \u0423\u0431\u0438\u0439\u0446\u0430", 360000L),
      internalField1124("\u0412\u0443\u043b\u043a\u0430\u043d", 300000L),
      internalField1123("\u041c\u0435\u0442\u0435\u043e\u0440\u0438\u0442\u043d\u044b\u0439 \u0434\u043e\u0436\u0434\u044c", 180000L),
      internalField1122("\u041f\u043e\u0441\u044b\u043b\u043a\u0430", 180000L),
      internalField1121("\u0411\u043e\u0441\u0441", 180000L),
      internalField1528("\u041a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440", 180000L),
      internalField1527("\u0413\u0440\u0443\u0437", 180000L),
      internalField1526("\u0422\u0430\u0438\u043d\u0441\u0442\u0432\u0435\u043d\u043d\u044b\u0439 \u043a\u043e\u0440\u0430\u0431\u043b\u044c", 300000L);

      final String internalField0248;
      final long internalField0229;

      @Generated
      public String internalMethod00988() {
         return this.internalField0248;
      }

      @Generated
      public long internalMethod01446() {
         return this.internalField0229;
      }

      @Generated
      private InternalType0098(String localValue3, long localValue4) {
         this.internalField0248 = localValue3;
         this.internalField0229 = localValue4;
      }
   }
}
