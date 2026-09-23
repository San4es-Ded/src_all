package rockstar.client.internal.script;







import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import globals.client.api.RockNetClient;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import pyrock.events.render.HudRenderEvent;

public class AutoFarmWebBridge {
   private final Queue<String> internalField0877 = new ConcurrentLinkedQueue<>();
   private final EventListener<HudRenderEvent> internalField0157 = localValue1 -> {
      String localValue2 = this.internalField0877.poll();
      if (localValue2 != null) {
         internalMethod01472(() -> this.internalMethod05455(localValue2, true));
      }
   };

   public AutoFarmWebBridge() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public void internalMethod03603(Packets.InternalType0391 localValue1) {
      if (localValue1 != null && localValue1.reqId() != null && !localValue1.reqId().isBlank()) {
         String localValue2 = localValue1.action();
         switch (localValue2) {
            case "state":
               internalMethod05537(() -> this.internalMethod05089(localValue1.reqId()));
               break;
            case "set":
               internalMethod05537(() -> {
                  this.internalMethod05379(localValue1);
                  this.internalMethod05089(localValue1.reqId());
               });
               break;
            case "inventory":
               internalMethod05537(() -> this.internalMethod06549(localValue1.reqId()));
         }
      }
   }

   private static void internalMethod05537(Runnable localValue0) {
      MinecraftClient.getInstance().execute(() -> internalMethod01472(localValue0));
   }

   private static void internalMethod01472(Runnable localValue0) {
      try {
         localValue0.run();
      } catch (Throwable localValue2) {
         RockstarClient.internalField0572
            .error(
               "[AutoFarm] \u0437\u0430\u043f\u0440\u043e\u0441 \u0441 \u0441\u0430\u0439\u0442\u0430 \u043d\u0435 \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d",
               localValue2
            );
      }
   }

   private void internalMethod06549(String localValue1) {
      MinecraftClient localValue2 = MinecraftClient.getInstance();
      if (localValue2.player != null && !localValue2.options.hudHidden) {
         while (this.internalField0877.size() >= 4) {
            this.internalField0877.poll();
         }

         this.internalField0877.add(localValue1);
      } else {
         this.internalMethod05455(localValue1, false);
      }
   }

   private void internalMethod05089(String localValue1) {
      JsonObject localValue2 = new JsonObject();
      localValue2.addProperty("reqId", localValue1);
      MinecraftClient localValue3 = MinecraftClient.getInstance();
      localValue2.addProperty("inGame", localValue3.player != null && localValue3.world != null);
      AutoFarmModule localValue4 = internalMethod05659();
      boolean localValue5 = localValue4 != null && localValue4.isEnabled();
      localValue2.addProperty("enabled", localValue5);
      ModeSetting localValue6 = localValue4 == null ? null : internalMethod07465(localValue4);
      ModeSetting.InternalType0088 localValue7 = localValue6 == null ? null : localValue6.internalMethod07418();
      localValue2.addProperty("mode", localValue7 == null ? "" : LanguageManager.internalMethod07214(localValue7.getName()));
      JsonArray localValue8 = new JsonArray();
      if (localValue4 != null && localValue5) {
         for (Setting localValue10 : List.copyOf(localValue4.getSettings())) {
            if (localValue10 != localValue6 && localValue10.isVisible()) {
               JsonObject localValue11 = internalMethod00858(localValue10);
               if (localValue11 != null) {
                  localValue8.add(localValue11);
               }
            }
         }
      }

      localValue2.add("settings", localValue8);
      internalMethod03247(new Packets.InternalType0465(localValue2));
   }

   private static JsonObject internalMethod00858(Setting localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("key", localValue0.getName());
      localValue1.addProperty("label", LanguageManager.internalMethod07214(localValue0.getName()));
      if (localValue0 instanceof BooleanSetting localValue10) {
         localValue1.addProperty("type", "boolean");
         localValue1.addProperty("value", localValue10.internalMethod04496());
         return localValue1;
      } else if (localValue0 instanceof SliderSetting localValue9) {
         localValue1.addProperty("type", "slider");
         localValue1.addProperty("value", localValue9.internalMethod08576());
         localValue1.addProperty("min", localValue9.internalMethod05288());
         localValue1.addProperty("max", localValue9.internalMethod05291());
         localValue1.addProperty("step", localValue9.internalMethod08575());
         localValue1.addProperty("suffix", localValue9.internalMethod08885().trim());
         return localValue1;
      } else if (localValue0 instanceof TextSetting localValue8) {
         localValue1.addProperty("type", "string");
         localValue1.addProperty("value", localValue8.internalMethod08926() == null ? "" : localValue8.internalMethod08926());
         localValue1.addProperty("numberOnly", localValue8.internalMethod04496());
         return localValue1;
      } else if (localValue0 instanceof MultiSelectSetting localValue7) {
         localValue1.addProperty("type", "select");
         localValue1.addProperty("min", localValue7.internalMethod00564());
         JsonArray localValue11 = new JsonArray();

         for (MultiSelectSetting.InternalType0091 localValue13 : List.copyOf(localValue7.internalMethod01792())) {
            if (!localValue13.isHidden()) {
               localValue11.add(internalMethod01767(localValue13.getName(), localValue7.internalMethod07492().contains(localValue13)));
            }
         }

         localValue1.add("options", localValue11);
         return localValue1;
      } else if (localValue0 instanceof ModeSetting localValue2) {
         localValue1.addProperty("type", "mode");
         ModeSetting.InternalType0088 localValue3 = localValue2.internalMethod07418();
         localValue1.addProperty("value", localValue3 == null ? "" : LanguageManager.internalMethod07214(localValue3.getName()));
         JsonArray localValue4 = new JsonArray();

         for (ModeSetting.InternalType0088 localValue6 : List.copyOf(localValue2.internalMethod06723())) {
            if (!localValue6.isHidden()) {
               localValue4.add(internalMethod01767(localValue6.getName(), localValue6 == localValue3));
            }
         }

         localValue1.add("options", localValue4);
         return localValue1;
      } else {
         return null;
      }
   }

   private static JsonObject internalMethod01767(String localValue0, boolean localValue1) {
      JsonObject localValue2 = new JsonObject();
      localValue2.addProperty("name", localValue0);
      localValue2.addProperty("label", LanguageManager.internalMethod07214(localValue0));
      localValue2.addProperty("on", localValue1);
      return localValue2;
   }

   private void internalMethod05379(Packets.InternalType0391 localValue1) {
      AutoFarmModule localValue2 = internalMethod05659();
      if (localValue2 != null && localValue2.isEnabled()) {
         ModeSetting localValue3 = internalMethod07465(localValue2);
         Setting localValue4 = null;

         for (Setting localValue6 : List.copyOf(localValue2.getSettings())) {
            if (localValue6 != localValue3 && localValue6.isVisible() && localValue6.getName().equals(localValue1.key())) {
               localValue4 = localValue6;
               break;
            }
         }

         if (localValue4 != null) {
            JsonElement localValue13 = localValue1.value();
            if (localValue4 instanceof BooleanSetting localValue7) {
               localValue7.internalMethod02034(internalMethod06841(localValue13, !localValue7.internalMethod04496()));
            } else if (localValue4 instanceof SliderSetting localValue8) {
               localValue8.internalMethod04736((float)internalMethod06840(localValue13, localValue8.internalMethod08576()));
            } else if (localValue4 instanceof TextSetting localValue9) {
               localValue9.internalMethod03337(internalMethod05844(localValue13, localValue9.internalMethod08926()));
            } else if (localValue4 instanceof MultiSelectSetting localValue10) {
               MultiSelectSetting.InternalType0091 localValue11 = internalMethod00409(localValue10, localValue1.option());
               if (localValue11 != null && localValue10.internalMethod07492().contains(localValue11) != internalMethod06841(localValue13, false)) {
                  localValue11.toggle();
               }
            } else {
               if (!(localValue4 instanceof ModeSetting localValue14)) {
                  return;
               }

               for (ModeSetting.InternalType0088 localValue12 : List.copyOf(localValue14.internalMethod06723())) {
                  if (localValue12.getName().equals(localValue1.option())) {
                     localValue12.select();
                     break;
                  }
               }
            }

            RockstarClient.getInstance().internalMethod02152().internalMethod07804();
         }
      }
   }

   private static MultiSelectSetting.InternalType0091 internalMethod00409(MultiSelectSetting localValue0, String localValue1) {
      for (MultiSelectSetting.InternalType0091 localValue3 : List.copyOf(localValue0.internalMethod01792())) {
         if (localValue3.getName().equals(localValue1)) {
            return localValue3;
         }
      }

      return null;
   }

   private void internalMethod05455(String localValue1, boolean localValue2) {
      JsonObject localValue3 = new JsonObject();
      localValue3.addProperty("reqId", localValue1);
      ClientPlayerEntity localValue4 = MinecraftClient.getInstance().player;
      if (localValue4 == null) {
         localValue3.addProperty("ok", false);
         localValue3.addProperty("error", "NO_PLAYER");
         internalMethod03247(new Packets.InternalType0032(localValue3));
      } else {
         localValue3.addProperty("ok", true);
         localValue3.addProperty("nickname", localValue4.getName().getString());
         if (localValue2) {
            try {
               localValue3.addProperty("png", Base64.getEncoder().encodeToString(InventoryScreenshotRenderer.internalMethod05830(localValue4)));
            } catch (Throwable localValue7) {
               RockstarClient.internalField0572
                  .error(
                     "[AutoFarm] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u0440\u0438\u0441\u043e\u0432\u0430\u0442\u044c \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c",
                     localValue7
                  );
               localValue3.addProperty("imageError", "RENDER_FAILED");
            }
         } else {
            localValue3.addProperty("imageError", "HUD_HIDDEN");
         }

         int localValue5 = 0;

         for (int localValue6 = 0; localValue6 < localValue4.getInventory().getMainStacks().size(); localValue6++) {
            if (!((ItemStack)localValue4.getInventory().getMainStacks().get(localValue6)).isEmpty()) {
               localValue5++;
            }
         }

         localValue3.addProperty("used", localValue5);
         localValue3.addProperty("total", localValue4.getInventory().getMainStacks().size());
         localValue3.add("items", internalMethod04658(localValue4));
         internalMethod03247(new Packets.InternalType0032(localValue3));
      }
   }

   private static JsonArray internalMethod04658(ClientPlayerEntity localValue0) {
      LinkedHashMap localValue1 = new LinkedHashMap();
      ArrayList localValue2 = new ArrayList(localValue0.getInventory().getMainStacks());
      localValue2.addAll(rockstar.client.util.LegacyItemTypes.armorItems(localValue0));
      localValue2.add(localValue0.getOffHandStack());

      for (ItemStack localValue4 : (Iterable<ItemStack>)(Iterable<?>)localValue2) {
         if (localValue4 != null && !localValue4.isEmpty()) {
            localValue1.merge(localValue4.getName().getString(), localValue4.getCount(), (left, right) -> (Integer)left + (Integer)right);
         }
      }

      JsonArray localValue5 = new JsonArray();
      localValue1.entrySet().stream().sorted(Entry.comparingByValue().reversed()).forEach(localValue1x -> {
         JsonObject localValue2x = new JsonObject();
         localValue2x.addProperty("name", (String)((java.util.Map.Entry)localValue1x).getKey());
         localValue2x.addProperty("count", (Number)((java.util.Map.Entry)localValue1x).getValue());
         localValue5.add(localValue2x);
      });
      return localValue5;
   }

   private static AutoFarmModule internalMethod05659() {
      return RockstarClient.getInstance().getModuleManager().getModule(AutoFarmModule.class);
   }

   private static ModeSetting internalMethod07465(AutoFarmModule localValue0) {
      for (Setting localValue2 : List.copyOf(localValue0.getSettings())) {
         if (localValue2 instanceof ModeSetting localValue3) {
            for (ModeSetting.InternalType0088 localValue5 : List.copyOf(localValue3.internalMethod06723())) {
               if (localValue5 instanceof AutoFarmModeState) {
                  return localValue3;
               }
            }
         }
      }

      return null;
   }

   private static boolean internalMethod06841(JsonElement localValue0, boolean localValue1) {
      try {
         return localValue0 != null && localValue0.isJsonPrimitive() ? localValue0.getAsBoolean() : localValue1;
      } catch (RuntimeException localValue3) {
         return localValue1;
      }
   }

   private static double internalMethod06840(JsonElement localValue0, double localValue1) {
      try {
         return localValue0 != null && localValue0.isJsonPrimitive() ? localValue0.getAsDouble() : localValue1;
      } catch (RuntimeException localValue4) {
         return localValue1;
      }
   }

   private static String internalMethod05844(JsonElement localValue0, String localValue1) {
      try {
         return localValue0 != null && localValue0.isJsonPrimitive() ? localValue0.getAsString() : localValue1;
      } catch (RuntimeException localValue3) {
         return localValue1;
      }
   }

   private static void internalMethod03247(Packet localValue0) {
      RockNetClient localValue1 = RockstarClient.getInstance().internalMethod06050();
      if (localValue1 != null) {
         localValue1.send(localValue0);
      }
   }
}
