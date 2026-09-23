package rockstar.client.internal.network;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pyrock.events.render.PreHudRenderEvent;

public class WaypointCommandHandler implements MinecraftClientAccess, ScreenMetricsAccess {
   private final EventListener<PreHudRenderEvent> internalField0157 = new EventListener<PreHudRenderEvent>() {
      public void onEvent(PreHudRenderEvent localValue1) {
         MatrixStack localValue2 = rockstar.client.render.GuiMatrixCompat.toLegacyStack(localValue1.getContext().getMatrices());
         WaypointCommandHandler.this.internalMethod03808(localValue1, localValue2);
      }

      @Override
      public int internalMethod07175() {
         return 1;
      }
   };

   public WaypointCommandHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public final CommandNode internalMethod06075() {
      return CommandBuilder.internalMethod00593("waypoint")
         .internalMethod05325("way", "gps")
         .internalMethod06148("commands.way.description")
         .internalMethod01539("action", localValue0 -> {
            localValue0.internalMethod04818("add", "remove", "del", "clear", "list");
            localValue0.internalMethod07138("add", "remove", "clear", "list");
         })
         .internalMethod01539("name", localValue0 -> localValue0.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116))
         .internalMethod01539("x", localValue1 -> localValue1.internalMethod06921().internalMethod00776(this::internalMethod01920))
         .internalMethod01539("y", localValue1 -> localValue1.internalMethod06921().internalMethod00776(this::internalMethod01920))
         .internalMethod01539("z", localValue1 -> localValue1.internalMethod06921().internalMethod00776(this::internalMethod01920))
         .internalMethod00262(this::internalMethod04699)
         .internalMethod04146();
   }

   private OperationResult internalMethod01920(String localValue1) {
      try {
         Integer.parseInt(localValue1);
         return OperationResult.internalMethod00116(localValue1);
      } catch (NumberFormatException localValue3) {
         return OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.way.error_number"));
      }
   }

   private void internalMethod04699(ParsedCommand localValue1) {
      String localValue2 = this.internalMethod07165(localValue1, 0);
      String localValue3 = this.internalMethod07165(localValue1, 1);
      String localValue4 = this.internalMethod07165(localValue1, 2);
      String localValue5 = this.internalMethod07165(localValue1, 3);
      String localValue6 = this.internalMethod07165(localValue1, 4);
      WaypointStore localValue7 = RockstarClient.getInstance().internalMethod06121();
      if (localValue2 == null) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.way.help")));
      } else {
         String localValue8 = localValue2.toLowerCase();
         switch (localValue8) {
            case "add":
               this.internalMethod02791(localValue7, localValue3, localValue4, localValue5, localValue6);
               break;
            case "remove":
            case "del":
               String localValue10 = this.internalMethod02313(localValue3, localValue4, localValue5, localValue6);
               String localValue11 = this.internalMethod04375(localValue10);
               if (localValue11 == null || localValue11.isBlank()) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.way.error_name")));
                  return;
               }

               localValue7.internalMethod06386(localValue11);
               break;
            case "clear":
               localValue7.internalMethod02210();
               break;
            case "list":
               if (localValue7.internalMethod00276().isEmpty()) {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.way.list_empty")));
                  return;
               }

               ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.way.list_header")));
               localValue7.internalMethod00276().forEach(localValue1x -> ClientMessages.internalMethod07664(this.internalMethod07329(localValue1x.getKey(), localValue1x.getValue())));
         }
      }
   }

   private String internalMethod02313(String localValue1, String localValue2, String localValue3, String localValue4) {
      if (localValue1 == null) {
         return null;
      } else if (localValue1.startsWith("b64:")) {
         return localValue1;
      } else if (localValue2 == null && localValue3 == null && localValue4 == null) {
         return localValue1;
      } else {
         StringBuilder localValue5 = new StringBuilder();
         localValue5.append(localValue1);
         if (localValue2 != null) {
            localValue5.append(" ").append(localValue2);
         }

         if (localValue3 != null) {
            localValue5.append(" ").append(localValue3);
         }

         if (localValue4 != null) {
            localValue5.append(" ").append(localValue4);
         }

         return localValue5.toString().trim();
      }
   }

   private void internalMethod02791(WaypointStore localValue1, String localValue2, String localValue3, String localValue4, String localValue5) {
      WaypointCommandHandler.InternalType0356 localValue6 = this.internalMethod01984(localValue1, localValue2, localValue3, localValue4, localValue5);
      if (localValue6 != null) {
         localValue1.internalMethod05412(localValue6.internalMethod03096(), localValue6.internalMethod01527(), localValue6.internalMethod01529(), localValue6.internalMethod08237());
      }
   }

   private WaypointCommandHandler.InternalType0356 internalMethod01984(WaypointStore localValue1, String localValue2, String localValue3, String localValue4, String localValue5) {
      int localValue6 = internalField0149.player.getBlockX();
      int localValue7 = internalField0149.player.getBlockY();
      int localValue8 = internalField0149.player.getBlockZ();
      WaypointCommandHandler.InternalType0084 localValue9 = this.internalMethod01719(localValue1, localValue2, localValue3, localValue4, localValue5);
      WaypointCommandHandler.InternalType0357 localValue10 = this.internalMethod01934(localValue9.internalMethod01536(), localValue6, localValue7, localValue8);
      return localValue10 == null
         ? null
         : new WaypointCommandHandler.InternalType0356(localValue9.internalMethod02867(), localValue10.internalMethod03159(), localValue10.internalMethod03162(), localValue10.internalMethod08040());
   }

   private WaypointCommandHandler.InternalType0084 internalMethod01719(WaypointStore localValue1, String localValue2, String localValue3, String localValue4, String localValue5) {
      if (this.internalMethod00772(localValue2, localValue3, localValue4, localValue5)) {
         return new WaypointCommandHandler.InternalType0084(this.internalMethod05634(localValue1), List.of());
      } else {
         return this.internalMethod00754(localValue2, localValue3)
            ? new WaypointCommandHandler.InternalType0084(this.internalMethod05634(localValue1), this.internalMethod06174(localValue2, localValue3, localValue4, localValue5))
            : new WaypointCommandHandler.InternalType0084(this.internalMethod01999(localValue1, localValue2), this.internalMethod03118(localValue3, localValue4, localValue5));
      }
   }

   private boolean internalMethod00772(String localValue1, String localValue2, String localValue3, String localValue4) {
      return localValue1 == null && localValue2 == null && localValue3 == null && localValue4 == null;
   }

   private boolean internalMethod00754(String localValue1, String localValue2) {
      return this.internalMethod01858(localValue1) && this.internalMethod01858(localValue2);
   }

   private String internalMethod01999(WaypointStore localValue1, String localValue2) {
      return localValue2 != null && !localValue2.isBlank() ? localValue2 : this.internalMethod05634(localValue1);
   }

   private List<String> internalMethod06174(String localValue1, String localValue2, String localValue3, String localValue4) {
      ArrayList localValue5 = new ArrayList(4);
      this.internalMethod05315(localValue5, localValue1);
      this.internalMethod05315(localValue5, localValue2);
      this.internalMethod05315(localValue5, localValue3);
      this.internalMethod05315(localValue5, localValue4);
      return localValue5;
   }

   private List<String> internalMethod03118(String localValue1, String localValue2, String localValue3) {
      ArrayList localValue4 = new ArrayList(3);
      this.internalMethod05315(localValue4, localValue1);
      this.internalMethod05315(localValue4, localValue2);
      this.internalMethod05315(localValue4, localValue3);
      return localValue4;
   }

   private void internalMethod05315(List<String> localValue1, String localValue2) {
      if (localValue2 != null) {
         localValue1.add(localValue2);
      }
   }

   private WaypointCommandHandler.InternalType0357 internalMethod01934(List<String> localValue1, int localValue2, int localValue3, int localValue4) {
      try {
         return switch (localValue1.size()) {
            case 0 -> new WaypointCommandHandler.InternalType0357(localValue2, localValue3, localValue4);
            default -> {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.way.help")));
               yield null;
            }
            case 2 -> new WaypointCommandHandler.InternalType0357(Integer.parseInt((String)localValue1.get(0)), localValue3, Integer.parseInt((String)localValue1.get(1)));
            case 3 -> new WaypointCommandHandler.InternalType0357(
               Integer.parseInt((String)localValue1.get(0)), Integer.parseInt((String)localValue1.get(1)), Integer.parseInt((String)localValue1.get(2))
            );
         };
      } catch (NumberFormatException localValue6) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.way.error_numbers")));
         return null;
      }
   }

   private boolean internalMethod01858(String localValue1) {
      if (localValue1 == null) {
         return false;
      } else {
         try {
            Integer.parseInt(localValue1);
            return true;
         } catch (NumberFormatException localValue3) {
            return false;
         }
      }
   }

   private String internalMethod05634(WaypointStore localValue1) {
      int localValue2 = 1;

      while (localValue1.internalMethod06387("\u041c\u0435\u0442\u043a\u0430 " + localValue2)) {
         localValue2++;
      }

      return "\u041c\u0435\u0442\u043a\u0430 " + localValue2;
   }

   private String internalMethod03284(String localValue1) {
      return localValue1 == null ? "" : "b64:" + Base64.getUrlEncoder().withoutPadding().encodeToString(localValue1.getBytes(StandardCharsets.UTF_8));
   }

   private String internalMethod04375(String localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         if (localValue1.startsWith("b64:")) {
            try {
               byte[] localValue2 = Base64.getUrlDecoder().decode(localValue1.substring("b64:".length()));
               return new String(localValue2, StandardCharsets.UTF_8);
            } catch (IllegalArgumentException localValue3) {
            }
         }

         return localValue1;
      }
   }

   private String internalMethod07165(ParsedCommand localValue1, int localValue2) {
      if (localValue1 != null && localValue1.internalMethod02266() != null) {
         if (localValue1.internalMethod02266().size() <= localValue2) {
            return null;
         } else {
            Object localValue3 = localValue1.internalMethod02266().get(localValue2);
            return localValue3 instanceof String ? (String)localValue3 : null;
         }
      } else {
         return null;
      }
   }

   private MutableText internalMethod07329(String localValue1, Vec3d localValue2) {
      int localValue3 = (int)Math.round(localValue2.x);
      int localValue4 = (int)Math.round(localValue2.y);
      int localValue5 = (int)Math.round(localValue2.z);
      String localValue6 = ".waypoint remove " + this.internalMethod03284(localValue1);
      MutableText localValue7 = Text.literal(localValue1)
         .setStyle(
            Style.EMPTY
               .withColor(TextColor.fromFormatting(Formatting.AQUA))
               .withClickEvent(new ClickEvent.RunCommand(localValue6))
               .withHoverEvent(new HoverEvent.ShowText(Text.of(LanguageManager.internalMethod07214("commands.way.list_hover"))))
         );
      MutableText localValue8 = Text.literal(LanguageManager.internalMethod00160("commands.way.list_coordinates", localValue3, localValue4, localValue5))
         .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)));
      return Text.literal("- ").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY))).append(localValue7).append(" ").append(localValue8);
   }

   public void internalMethod03808(PreHudRenderEvent localValue1, MatrixStack localValue2) {
      if (internalField0149.player != null && internalField0149.world != null) {
         for (Entry localValue4 : RockstarClient.getInstance().internalMethod06121().internalMethod00276()) {
            String localValue5 = (String)localValue4.getKey();
            Vec3d localValue6 = (Vec3d)localValue4.getValue();
            Vec3d localValue7 = localValue6.add(0.0, 0.5, 0.0);
            Vec2f localValue8 = ScreenProjectionUtils.internalMethod00612(localValue7);
            if (localValue8 != null) {
               float localValue9 = (float)internalField0149.player.getEntityPos().distanceTo(localValue6.add(0.5, 0.5, 0.5));
               float localValue10 = MathHelper.clamp(1.1F - localValue9 / 100.0F, 0.6F, 1.1F);
               localValue2.push();
               localValue2.translate(localValue8.x, localValue8.y, 0.0F);
               localValue2.scale(localValue10, localValue10, 1.0F);
               WaypointLabelRenderer.internalMethod03782(localValue1.getContext(), localValue5, localValue9, ThemeColors.internalMethod02531(), 1.0F);
               localValue2.pop();
            }
         }
      }
   }

   static final class InternalType0084 {
      private final String internalField0248;
      private final List<String> internalField0416;

      InternalType0084(String localValue1, List<String> localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0416 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0084[name=" + this.internalField0248 + ", cords=" + this.internalField0416 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WaypointCommandHandler.InternalType0084 other = (WaypointCommandHandler.InternalType0084) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416);
      }

      public String internalMethod02867() {
         return this.internalField0248;
      }

      public List<String> internalMethod01536() {
         return this.internalField0416;
      }
   }

   static final class InternalType0356 {
      private final String internalField0248;
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;

      InternalType0356(String localValue1, int localValue2, int localValue3, int localValue4) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0228 = localValue3;
         this.internalField1053 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0356[name=" + this.internalField0248 + ", x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WaypointCommandHandler.InternalType0356 other = (WaypointCommandHandler.InternalType0356) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053);
      }

      public String internalMethod03096() {
         return this.internalField0248;
      }

      public int internalMethod01527() {
         return this.internalField0227;
      }

      public int internalMethod01529() {
         return this.internalField0228;
      }

      public int internalMethod08237() {
         return this.internalField1053;
      }
   }

   static final class InternalType0357 {
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;

      InternalType0357(int localValue1, int localValue2, int localValue3) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField1053 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0357[x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WaypointCommandHandler.InternalType0357 other = (WaypointCommandHandler.InternalType0357) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053);
      }

      public int internalMethod03159() {
         return this.internalField0227;
      }

      public int internalMethod03162() {
         return this.internalField0228;
      }

      public int internalMethod08040() {
         return this.internalField1053;
      }
   }
}
