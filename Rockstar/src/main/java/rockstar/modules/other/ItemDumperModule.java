package rockstar.modules.other;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

@ModuleInfo(
   name = "Item Dumper",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.item_dumper"
)
public class ItemDumperModule extends Module {
   private static final File internalField0148 = new File(ConfigManager.internalField0148, "item_dump.json");
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private final Map<String, JsonObject> internalField0543 = new LinkedHashMap<>();
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private boolean internalField0277;

   public ItemDumperModule() {
      this.internalMethod09404();
   }

   private void internalMethod09404() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.item_dumper.only_custom");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.item_dumper.alerts");
   }

   @Override
   public void onEnable() {
      this.internalField0543.clear();
      this.internalField0277 = false;
      ClientMessages.internalMethod01809(
         Text.of(
            "\u0414\u0430\u043c\u043f \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u043f\u0438\u0448\u0435\u0442\u0441\u044f \u0432 "
               + internalField0148.getName()
               + ", \u043f\u043e\u0445\u043e\u0434\u0438 \u043f\u043e \u0430\u0443\u043a\u0446\u0438\u043e\u043d\u0443"
         )
      );
      super.onEnable();
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (this.internalField0519.internalMethod02365(200L)) {
            this.internalField0519.internalMethod00701();
            if (internalField0149.currentScreen instanceof HandledScreen) {
               ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
               if (localValue1 != null && localValue1 != internalField0149.player.playerScreenHandler) {
                  int localValue2 = Math.max(0, localValue1.slots.size() - 36);

                  for (int localValue3 = 0; localValue3 < localValue2; localValue3++) {
                     this.internalMethod05683(localValue1.getSlot(localValue3).getStack());
                  }
               }
            }

            if (this.internalField0277 && this.internalField0518.internalMethod02365(2000L)) {
               this.internalMethod09406();
               this.internalField0277 = false;
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod05683(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         List localValue2 = localValue1.getTooltip(TooltipContext.create(internalField0149.world), internalField0149.player, TooltipType.BASIC);
         NbtCompound localValue3 = ItemNbtUtils.internalMethod05003(localValue1);
         CustomItemUtils.InternalType0254 localValue4 = CustomItemUtils.internalMethod03238(localValue1);
         boolean localValue5 = localValue4 != null || localValue3 != null && !localValue3.isEmpty() || localValue1.hasEnchantments() || localValue2.size() > 1;
         if (!this.internalField0650.internalMethod04496() || localValue5) {
            String localValue6 = Registries.ITEM.getId(localValue1.getItem()).toString();
            String localValue7 = localValue1.getName().getString();
            String localValue8 = ItemNbtUtils.internalMethod02181(localValue1);
            ArrayList localValue9 = new ArrayList();

            for (int localValue10 = 1; localValue10 < localValue2.size(); localValue10++) {
               String localValue11 = ((Text)localValue2.get(localValue10)).getString();
               if (!localValue11.isBlank()) {
                  localValue9.add(localValue11);
               }
            }

            String localValue16 = localValue6 + "|" + localValue7 + "|" + String.join("\u0001", localValue9);
            JsonObject localValue17 = this.internalField0543.get(localValue16);
            if (localValue17 != null) {
               localValue17.addProperty("seen", localValue17.get("seen").getAsInt() + 1);
            } else {
               JsonObject localValue12 = new JsonObject();
               localValue12.addProperty("id", localValue6);
               localValue12.addProperty("name", localValue7);
               localValue12.addProperty("clean", localValue8);
               localValue12.addProperty("count", localValue1.getCount());
               localValue12.addProperty("seen", 1);
               localValue12.addProperty("damageable", localValue1.isDamageable());
               if (localValue1.isDamageable()) {
                  localValue12.addProperty("damage", localValue1.getDamage());
                  localValue12.addProperty("maxDamage", localValue1.getMaxDamage());
               }

               JsonArray localValue13 = new JsonArray();

               for (String localValue15 : (Iterable<String>)(Iterable<?>)localValue9) {
                  localValue13.add(localValue15);
               }

               localValue12.add("lore", localValue13);
               if (localValue3 != null && !localValue3.isEmpty()) {
                  localValue12.addProperty("nbt", localValue3.toString());
               }

               if (localValue4 != null) {
                  JsonObject localValue18 = new JsonObject();
                  localValue18.addProperty("category", localValue4.internalMethod03470().name());
                  localValue18.addProperty("display", localValue4.internalMethod00671(localValue1));
                  if (localValue4.internalMethod01319() != null) {
                     localValue18.addProperty("type", localValue4.internalMethod01319());
                  }

                  if (localValue4.internalMethod07219() != null) {
                     localValue18.addProperty("rank", localValue4.internalMethod07219().name());
                  }

                  localValue18.addProperty("server", localValue4.internalMethod07218().name());
                  localValue12.add("detected", localValue18);
               }

               this.internalField0543.put(localValue16, localValue12);
               this.internalField0277 = true;
               if (this.internalField0651.internalMethod04496()) {
                  RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(localValue8, localValue1.getItem()));
               }
            }
         }
      }
   }

   private void internalMethod09406() {
      try {
         JsonArray localValue1 = new JsonArray();

         for (JsonObject localValue3 : this.internalField0543.values()) {
            localValue1.add(localValue3);
         }

         JsonObject localValue5 = new JsonObject();
         localValue5.addProperty("total", this.internalField0543.size());
         localValue5.add("items", localValue1);
         ConfigManager.internalMethod01467(internalField0148, localValue5);
      } catch (Exception localValue4) {
         System.err.println("Error saving item dump: " + localValue4.getMessage());
      }
   }

   @Override
   public void onDisable() {
      if (!this.internalField0543.isEmpty()) {
         this.internalMethod09406();
         ClientMessages.internalMethod01809(
            Text.of(
               "\u0421\u043e\u0431\u0440\u0430\u043d\u043e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432: "
                  + this.internalField0543.size()
                  + " -> "
                  + internalField0148.getPath()
            )
         );
      }

      super.onDisable();
   }

   @Generated
   public BooleanSetting internalMethod06461() {
      return this.internalField0650;
   }

   @Generated
   public BooleanSetting internalMethod07142() {
      return this.internalField0651;
   }

   @Generated
   public Map<String, JsonObject> internalMethod03548() {
      return this.internalField0543;
   }

   @Generated
   public Stopwatch internalMethod05482() {
      return this.internalField0519;
   }

   @Generated
   public Stopwatch internalMethod06114() {
      return this.internalField0518;
   }

   @Generated
   public boolean internalMethod09405() {
      return this.internalField0277;
   }
}
