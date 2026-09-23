package rockstar.modules.other;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.window.KeyPressEvent;

@ModuleInfo(
   name = "Inventory Builder",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.inventory_builder"
)
public class InventoryBuilderModule extends Module {
   public static final int internalField0227 = 41;
   public static final int internalField0228 = 36;
   public static final int internalField1053 = 40;
   private static final List<String> internalField0416 = List.of("gear", "spheres", "runes", "explosives", "potions", "backpacks", "other");
   private static final List<InventoryBuilderModule.InternalType0024> internalField0417 = new ArrayList<>();
   private static final File internalField0148 = new File(new File(ConfigManager.internalField0148, "presets"), "invbuilder");
   private static final String internalField0248 = ".rock";
   private static final File internalField0147 = new File(ConfigManager.internalField0148, "loadouts.json");
   private static final Pattern internalField0293 = Pattern.compile("(?i)(con|prn|aux|nul|com\\d|lpt\\d)");
   private static boolean internalField0277;
   private ButtonSetting internalField0663;
   private ButtonSetting internalField0662;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private BooleanSetting internalField0650;
   private static final Pattern internalField0294 = Pattern.compile("\u0443\u0432\u0435\u043b\u0438\u0447\u0438\u0442\u044c\\D+(\\d+)");
   private static final Pattern internalField1112 = Pattern.compile("\u0443\u043c\u0435\u043d\u044c\u0448\u0438\u0442\u044c\\D+(\\d+)");
   private static final Pattern internalField1111 = Pattern.compile("\\((\\d{1,2}):(\\d{2})\\)");
   private static final Pattern internalField1113 = Pattern.compile("\\((\\d+)\\s*/\\s*(\\d+)\\)");
   private static final long internalField0229 = 50L;
   private long internalField0230;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final Deque<int[]> internalField0796 = new ArrayDeque<>();
   private boolean internalField0276;
   private int internalField1055;
   private int internalField1056;
   private boolean internalField1099;
   private int internalField1054;
   private int internalField1464;
   private InventoryBuilderModule.InternalType0065 internalField0141;
   private InventoryBuilderModule.InternalType0024 internalField0192;
   private int internalField1470;
   private int internalField1465;
   private int internalField1463;
   private int internalField1466;
   private int internalField1467;
   private long internalField1059;
   private int internalField1740;
   private int internalField1741;
   private int internalField1736;
   private int internalField1735;
   private int internalField1748;
   private String internalField0247;
   private int internalField1733;
   private final Set<Integer> internalField0546;
   private int internalField1738;
   private final Set<String> internalField0545;
   private String internalField1077;
   private volatile boolean internalField1100;
   private volatile String internalField1076;
   private int internalField1739;
   private final EventListener<KeyPressEvent> internalField0157;
   private final EventListener<ReceivePacketEvent> internalField0158;
   private int internalField1742;
   private static final List<InventoryBuilderModule.InternalType0023> internalField1145 = new ArrayList<>();

   public static String internalMethod04599(String localValue0) {
      return TextUtils.internalMethod06864(localValue0);
   }

   public static int internalMethod01691(String localValue0) {
      int localValue1 = localValue0 == null ? -1 : internalField0416.indexOf(localValue0);
      return localValue1 < 0 ? internalField0416.size() : localValue1;
   }

   public InventoryBuilderModule() {
      this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0141;
      this.internalField1466 = -1;
      this.internalField1467 = -1;
      this.internalField1740 = -1;
      this.internalField1741 = -1;
      this.internalField1736 = 8;
      this.internalField1748 = -1;
      this.internalField0247 = "";
      this.internalField0546 = new HashSet<>();
      this.internalField0545 = new HashSet<>();
      this.internalField1077 = "";
      this.internalField1076 = "";
      this.internalField0157 = localValue1 -> {
         if (this.internalMethod09168() && internalField0149.currentScreen != null) {
            if (localValue1.getKey() == 256 && localValue1.getAction() == 1) {
               this.internalMethod01693("\u043e\u0442\u043c\u0435\u043d\u0430 \u043f\u043e Esc");
            }
         }
      };
      this.internalField0158 = localValue1 -> {
         if (this.internalField0141 != InventoryBuilderModule.InternalType0065.internalField0141) {
            if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2) {
               String localValue5 = localValue2.content().getString().toLowerCase(Locale.ROOT);
               if (this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1022) {
                  if (localValue5.contains("\u043d\u0435 \u0431\u044b\u043b\u043e \u043d\u0430\u0439\u0434\u0435\u043d\u043e")
                     || localValue5.contains("\u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e")) {
                     this.internalField0276 = true;
                  }
               } else if (this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1024
                  || this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1021
                  || this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1023) {
                  if (!localValue5.contains("\u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e")
                        && !localValue5.contains("\u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442")
                     || !localValue5.contains("\u0441\u0440\u0435\u0434\u0441\u0442\u0432")
                        && !localValue5.contains("\u0434\u0435\u043d\u0435\u0433")
                        && !localValue5.contains("\u043c\u043e\u043d\u0435\u0442")) {
                     boolean localValue4 = localValue5.contains("\u0443\u0436\u0435 \u043a\u0443\u043f")
                        || localValue5.contains("\u0443\u0436\u0435 \u043f\u0440\u043e\u0434\u0430")
                        || localValue5.contains("\u0443\u0441\u043f\u0435\u043b")
                        || localValue5.contains("\u043f\u0435\u0440\u0435\u043a\u0443\u043f")
                        || localValue5.contains("\u043b\u043e\u0442")
                           && (
                              localValue5.contains("\u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d")
                                 || localValue5.contains("\u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442")
                                 || localValue5.contains("\u0441\u043d\u044f\u0442")
                                 || localValue5.contains("\u0438\u0441\u0442\u0451\u043a")
                                 || localValue5.contains("\u0438\u0441\u0442\u0435\u043a")
                           );
                     if (localValue4) {
                        this.internalField1100 = true;
                        this.internalField1076 = "\u041b\u043e\u0442 \u043f\u0435\u0440\u0435\u0445\u0432\u0430\u0442\u0438\u043b\u0438";
                     }
                  } else {
                     this.internalField1739++;
                     this.internalField1100 = true;
                     this.internalField1076 = "\u041d\u0435 \u0445\u0432\u0430\u0442\u0438\u043b\u043e \u0434\u0435\u043d\u0435\u0433 \u043d\u0430 \u043b\u043e\u0442";
                  }
               }
            }
         }
      };
      this.internalMethod09324();
   }

   private void internalMethod09324() {
      this.internalField0663 = new ButtonSetting(this, "modules.settings.inventory_builder.open")
         .internalMethod07149(() -> internalField0149.setScreen(new InventoryBuilderScreen()));
      this.internalField0662 = new ButtonSetting(this, "modules.settings.inventory_builder.stop", () -> !this.internalMethod09168())
         .internalMethod07149(
            () -> this.internalMethod01693("\u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e \u0432\u0440\u0443\u0447\u043d\u0443\u044e")
         );
      this.internalField0383 = new SliderSetting(this, "modules.settings.inventory_builder.delay")
         .internalMethod05900(100.0F)
         .internalMethod02732(1500.0F)
         .internalMethod08673(50.0F)
         .internalMethod08074(300.0F)
         .internalMethod06240("ms");
      this.internalField0382 = new SliderSetting(this, "modules.settings.inventory_builder.attempts")
         .internalMethod05900(1.0F)
         .internalMethod02732(15.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(6.0F);
      this.internalField1142 = new SliderSetting(this, "modules.settings.inventory_builder.pages")
         .internalMethod05900(1.0F)
         .internalMethod02732(30.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(6.0F);
      this.internalField0650 = new BooleanSetting(this, "modules.settings.inventory_builder.sort");
   }

   public boolean internalMethod09168() {
      return this.internalField0141 != InventoryBuilderModule.InternalType0065.internalField0141;
   }

   public void internalMethod01627(InventoryBuilderModule.InternalType0024 localValue1) {
      if (internalField0149.player != null && localValue1 != null) {
         if (localValue1.internalMethod01393() == 0) {
            ClientMessages.internalMethod03058(Text.of("\u041f\u0440\u0435\u0441\u0435\u0442 \u043f\u0443\u0441\u0442\u043e\u0439"));
         } else {
            this.internalField0192 = localValue1;
            this.internalField1470 = 0;
            this.internalField1465 = 0;
            this.internalField1738 = 0;
            this.internalField1059 = 0L;
            this.internalField0796.clear();
            this.internalField0546.clear();
            this.internalField0545.clear();
            this.internalField1077 = "";
            this.internalField1100 = false;
            this.internalField1739 = 0;
            this.internalField1733 = 0;
            this.internalField1748 = -1;
            this.internalField1735 = 0;
            this.internalField0276 = false;
            this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
            this.internalField0518.internalMethod00701();
            this.internalField0519.internalMethod00701();
            if (!this.isEnabled()) {
               this.enable();
            }

            ClientMessages.internalMethod01809(
               Text.of(
                  "\u0421\u0431\u043e\u0440\u043a\u0430 \u00ab"
                     + localValue1.internalField0248
                     + "\u00bb \u0437\u0430\u043f\u0443\u0449\u0435\u043d\u0430: "
                     + localValue1.internalMethod01393()
                     + " "
                     + internalMethod06614(localValue1.internalMethod01393())
                     + ". \u041e\u0442\u043c\u0435\u043d\u0430 \u2014 Esc"
               )
            );
         }
      }
   }

   public void internalMethod01693(String localValue1) {
      if (this.internalField0141 != InventoryBuilderModule.InternalType0065.internalField0141) {
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0141;
         this.internalField0192 = null;
         this.internalField0796.clear();
         ClientMessages.internalMethod01809(
            Text.of(
               "\u0421\u0431\u043e\u0440\u043a\u0430 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0430: "
                  + localValue1
                  + " (\u043a\u0443\u043f\u043b\u0435\u043d\u043e "
                  + this.internalField1738
                  + ")"
            )
         );
      }
   }

   private void internalMethod09327() {
      if (this.internalField0141 != InventoryBuilderModule.InternalType0065.internalField0141) {
         String localValue1 = this.internalField0192 == null ? "" : this.internalField0192.internalField0248;
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0141;
         this.internalField0192 = null;
         this.internalField0796.clear();
         ClientMessages.internalMethod01809(
            Text.of(
               "\u0421\u0431\u043e\u0440\u043a\u0430 \u00ab"
                  + localValue1
                  + "\u00bb \u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u0430: \u043a\u0443\u043f\u043b\u0435\u043d\u043e "
                  + this.internalField1738
                  + " "
                  + internalMethod05152(this.internalField1738)
            )
         );
      }
   }

   private static String internalMethod06614(int localValue0) {
      int localValue1 = localValue0 % 100;
      int localValue2 = localValue0 % 10;
      if (localValue1 >= 11 && localValue1 <= 14) {
         return "\u0441\u043b\u043e\u0442\u043e\u0432";
      } else {
         return localValue2 == 1 ? "\u0441\u043b\u043e\u0442" : (localValue2 >= 2 && localValue2 <= 4 ? "\u0441\u043b\u043e\u0442\u0430" : "\u0441\u043b\u043e\u0442\u043e\u0432");
      }
   }

   private static String internalMethod05152(int localValue0) {
      int localValue1 = localValue0 % 100;
      int localValue2 = localValue0 % 10;
      if (localValue1 >= 11 && localValue1 <= 14) {
         return "\u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432";
      } else {
         return localValue2 == 1
            ? "\u043f\u0440\u0435\u0434\u043c\u0435\u0442"
            : (localValue2 >= 2 && localValue2 <= 4 ? "\u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430" : "\u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432");
      }
   }

   @Override
   public void internalMethod08229() {
      if (this.internalField0141 != InventoryBuilderModule.InternalType0065.internalField0141) {
         if (internalField0149.player != null && internalField0149.world != null) {
            if (this.internalField0519.internalMethod02365(Math.max(50L, (long)this.internalField0383.internalMethod08576() + this.internalField0230))) {
               this.internalField0519.internalMethod00701();
               this.internalField0230 = ThreadLocalRandom.current().nextLong(-50L, 51L);
               if (this.internalField1100) {
                  this.internalField1100 = false;
                  if (this.internalField1739 >= 3) {
                     this.internalMethod01693("\u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u0434\u0435\u043d\u0435\u0433");
                     return;
                  }

                  if (this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1024
                     || this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1021
                     || this.internalField0141 == InventoryBuilderModule.InternalType0065.internalField1023) {
                     this.internalMethod08055(
                        this.internalField1076.isEmpty() ? "\u041b\u043e\u0442 \u043d\u0435 \u043a\u0443\u043f\u043b\u0435\u043d" : this.internalField1076
                     );
                     return;
                  }
               }

               switch (this.internalField0141) {
                  case internalField0142:
                     this.internalMethod09330();
                     break;
                  case internalField1022:
                     this.internalMethod09333();
                     break;
                  case internalField1024:
                     this.internalMethod09991();
                     break;
                  case internalField1021:
                     this.internalMethod09993();
                     break;
                  case internalField1023:
                     this.internalMethod09995();
                     break;
                  case internalField1432:
                     this.internalMethod10054();
               }
            }
         } else {
            this.internalMethod01693("\u043d\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430");
         }
      }
   }

   private void internalMethod09330() {
      while (this.internalField1470 < 41 && (this.internalField0192.internalField0430[this.internalField1470] == null || this.internalMethod06364(this.internalField1470))) {
         this.internalField1470++;
      }

      if (this.internalField1470 >= 41) {
         if (this.internalField0650.internalMethod04496()) {
            this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1432;
            this.internalField0796.clear();
            this.internalField1467 = -1;
            this.internalField1464 = 0;
            this.internalField0518.internalMethod00701();
            this.internalMethod10055();
         } else {
            this.internalMethod09327();
         }
      } else {
         InventoryBuilderModule.InternalType0023 localValue1 = this.internalField0192.internalField0430[this.internalField1470];
         int localValue2 = this.internalMethod01622(localValue1) - this.internalMethod07478(localValue1);
         if (localValue2 <= 0) {
            this.internalMethod10052();
         } else if (this.internalField1465 >= (int)this.internalField0382.internalMethod08576()) {
            ClientMessages.internalMethod03058(
               Text.of(
                  "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043a\u0443\u043f\u0438\u0442\u044c: "
                     + localValue1.internalMethod01243()
                     + ", \u043d\u0435 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 "
                     + localValue2
               )
            );
            this.internalMethod10052();
         } else {
            this.internalMethod10055();
            this.internalField1054 = 0;
            this.internalField1055 = 0;
            this.internalField1056 = 0;
            this.internalField1099 = false;
            internalField0149.player.networkHandler.sendChatCommand("ah search " + this.internalMethod06802(localValue1));
            this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1022;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod09333() {
      if (this.internalField0276) {
         this.internalField0276 = false;
         InventoryBuilderModule.InternalType0023 localValue18 = this.internalField0192.internalField0430[this.internalField1470];
         if (localValue18 != null) {
            ClientMessages.internalMethod03058(
               Text.of(
                  "\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043d\u0430 \u0430\u0443\u043a\u0446\u0438\u043e\u043d\u0435: "
                     + localValue18.internalMethod01243()
               )
            );
         }

         this.internalMethod10052();
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
      } else if (this.internalField0518.internalMethod02365(6000L)) {
         this.internalField1465++;
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
      } else {
         ScreenHandler localValue1 = this.internalMethod06707();
         if (localValue1 != null) {
            if (this.internalMethod08196().contains("\u043f\u043e\u043a\u0443\u043f\u043a")) {
               this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1024;
               this.internalField0518.internalMethod00701();
            } else {
               int[] localValue2 = this.internalMethod06362();
               if (this.internalField1055 > 0) {
                  boolean localValue3 = localValue2[1] > 0;
                  if (localValue3 && localValue2[0] != this.internalField1055) {
                     return;
                  }

                  if (!localValue3 && !this.internalField0518.internalMethod02365(700L)) {
                     return;
                  }

                  this.internalField1055 = 0;
                  this.internalField1099 = false;
                  this.internalField0518.internalMethod00701();
               }

               InventoryBuilderModule.InternalType0023 localValue19 = this.internalField0192.internalField0430[this.internalField1470];
               int localValue4 = this.internalMethod01622(localValue19) - this.internalMethod07478(localValue19);
               if (localValue4 <= 0) {
                  this.internalMethod10055();
                  this.internalMethod10052();
                  this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
               } else {
                  int localValue5 = localValue1.slots.size() - 36;
                  if (!this.internalField1099) {
                     boolean localValue6 = true;
                     int localValue7 = 0;

                     while (true) {
                        if (localValue7 < localValue5) {
                           if (localValue1.getSlot(localValue7).getStack().isEmpty()) {
                              localValue7++;
                              continue;
                           }

                           localValue6 = false;
                        }

                        if (localValue6 || !this.internalField0518.internalMethod02365(Math.max(400L, (long)this.internalField0383.internalMethod08576()))) {
                           return;
                        }

                        this.internalField1099 = true;
                        break;
                     }
                  }

                  if (localValue19.internalField0228 > 1 && this.internalField1056 < 8) {
                     int localValue20 = this.internalMethod00582(localValue1, localValue5);
                     if (localValue20 >= 0 && !this.internalMethod04535(localValue1.getSlot(localValue20).getStack())) {
                        this.internalField1056++;
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue20, 0, SlotActionType.PICKUP, internalField0149.player);
                        this.internalField1099 = false;
                        this.internalField0518.internalMethod00701();
                        return;
                     }

                     if (localValue20 >= 0) {
                        this.internalField1056 = 0;
                     }
                  }

                  int localValue21 = -1;
                  int localValue22 = 0;
                  long localValue8 = Long.MAX_VALUE;
                  String localValue10 = "";

                  for (int localValue11 = 0; localValue11 < localValue5; localValue11++) {
                     ItemStack localValue12 = localValue1.getSlot(localValue11).getStack();
                     if (this.internalMethod07186(localValue12, localValue19)) {
                        List localValue13 = this.internalMethod00191(localValue12);
                        if (this.internalMethod04080(localValue12, localValue13, localValue19)) {
                           int localValue14 = Math.max(1, localValue12.getCount());
                           long localValue15 = this.internalMethod02028(localValue13, localValue14);
                           if (localValue15 >= 0L && (localValue19.internalField0229 <= 0L || localValue15 <= localValue19.internalField0229)) {
                              String localValue17 = this.internalMethod03255(localValue12, localValue13, localValue15);
                              if (!this.internalField0545.contains(localValue17) && localValue15 < localValue8) {
                                 localValue8 = localValue15;
                                 localValue21 = localValue11;
                                 localValue22 = localValue14;
                                 localValue10 = localValue17;
                              }
                           }
                        }
                     }
                  }

                  if (localValue21 >= 0) {
                     this.internalField1463 = this.internalMethod07478(localValue19);
                     this.internalField1466 = -1;
                     this.internalField1742 = 0;
                     this.internalField1059 = localValue8;
                     this.internalField1077 = localValue10;
                     this.internalField1100 = false;
                     this.internalField0796.clear();
                     if (localValue22 <= localValue4) {
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue21, 0, SlotActionType.PICKUP, internalField0149.player);
                     } else {
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue21, 1, SlotActionType.PICKUP, internalField0149.player);
                        this.internalField1742 = localValue4;
                     }

                     this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1024;
                     this.internalField0518.internalMethod00701();
                  } else {
                     int localValue23 = this.internalMethod05329(localValue1, localValue5);
                     if (localValue23 < 0 || this.internalField1054 + 1 >= (int)this.internalField1142.internalMethod08576() || localValue2[1] > 0 && localValue2[0] >= localValue2[1]) {
                        ClientMessages.internalMethod03058(
                           Text.of(
                              (
                                    this.internalField0545.isEmpty()
                                       ? "\u041d\u0435\u0442 \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0438\u0445 \u043b\u043e\u0442\u043e\u0432: "
                                       : "\u0412\u0441\u0435 \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0438\u0435 \u043b\u043e\u0442\u044b \u0440\u0430\u0437\u043e\u0431\u0440\u0430\u043b\u0438: "
                                 )
                                 + localValue19.internalMethod01243()
                           )
                        );
                        this.internalMethod10055();
                        this.internalMethod10052();
                        this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
                     } else {
                        this.internalField1054++;
                        this.internalField1055 = localValue2[0] + 1;
                        internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue23, 0, SlotActionType.PICKUP, internalField0149.player);
                        this.internalField0518.internalMethod00701();
                     }
                  }
               }
            }
         }
      }
   }

   private void internalMethod09991() {
      if (this.internalField0518.internalMethod02365(5000L)) {
         this.internalMethod08055("\u041b\u043e\u0442 \u043f\u0435\u0440\u0435\u0445\u0432\u0430\u0442\u0438\u043b\u0438");
      } else {
         ScreenHandler localValue1 = this.internalMethod06707();
         if (localValue1 != null && this.internalMethod08196().contains("\u043f\u043e\u043a\u0443\u043f\u043a")) {
            int localValue2 = localValue1.slots.size() - 36;
            int localValue3 = -1;
            int localValue4 = -1;
            int localValue5 = -1;
            int localValue6 = -1;
            int localValue7 = Integer.MAX_VALUE;
            int localValue8 = -1;
            boolean[] localValue9 = new boolean[localValue2];

            for (int localValue10 = 0; localValue10 < localValue2; localValue10++) {
               ItemStack localValue11 = localValue1.getSlot(localValue10).getStack();
               if (!localValue11.isEmpty()) {
                  String localValue12 = localValue11.getName().getString().toLowerCase(Locale.ROOT);
                  Matcher localValue13 = internalField0294.matcher(localValue12);
                  Matcher localValue14 = internalField1112.matcher(localValue12);
                  boolean localValue15 = false;
                  if (localValue13.find()) {
                     int localValue16 = Integer.parseInt(localValue13.group(1));
                     if (localValue16 == 1) {
                        localValue3 = localValue10;
                     } else if (localValue16 == 10) {
                        localValue4 = localValue10;
                     }

                     localValue15 = true;
                  } else if (localValue14.find()) {
                     if (Integer.parseInt(localValue14.group(1)) == 1) {
                        localValue5 = localValue10;
                     }

                     localValue15 = true;
                  } else if (localValue12.contains("\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0434")
                     || localValue12.contains("\u043a\u0443\u043f\u0438\u0442\u044c")
                     || localValue12.contains("\u043f\u0440\u0438\u043e\u0431\u0440\u0435\u0441\u0442")) {
                     localValue6 = localValue10;
                  }

                  if (localValue15) {
                     localValue9[localValue10] = true;
                     localValue7 = Math.min(localValue7, localValue10);
                     localValue8 = Math.max(localValue8, localValue10);
                  }
               }
            }

            if (localValue6 == -1 && localValue8 != -1) {
               for (int localValue17 = localValue7 + 1; localValue17 < localValue8; localValue17++) {
                  if (!localValue9[localValue17] && !localValue1.getSlot(localValue17).getStack().isEmpty()) {
                     localValue6 = localValue17;
                     break;
                  }
               }
            }

            if (localValue6 == -1) {
               this.internalMethod05013(
                  localValue1,
                  localValue2,
                  "\u043d\u0435 \u043d\u0430\u0448\u0451\u043b \u043a\u043d\u043e\u043f\u043a\u0443 \u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u044f"
               );
            } else if (this.internalField1742 > 1 && localValue3 == -1 && localValue4 == -1) {
               this.internalMethod05013(
                  localValue1,
                  localValue2,
                  "\u043d\u0435 \u043d\u0430\u0448\u0451\u043b \u043a\u043d\u043e\u043f\u043a\u0438 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u0430, \u043d\u0435 \u0431\u0443\u0434\u0443 \u0431\u0440\u0430\u0442\u044c \u0432\u0435\u0441\u044c \u043b\u043e\u0442"
               );
            } else {
               this.internalField1466 = localValue6;
               if (this.internalField1742 > 1) {
                  this.internalMethod04112(this.internalField1742 - 1, localValue3, localValue4, localValue5);
                  this.internalField1742 = 0;
                  this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1021;
                  this.internalField0518.internalMethod00701();
               } else {
                  this.internalField1742 = 0;
                  internalField0149.interactionManager.clickSlot(localValue1.syncId, this.internalField1466, 0, SlotActionType.PICKUP, internalField0149.player);
                  this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1023;
                  this.internalField0518.internalMethod00701();
               }
            }
         }
      }
   }

   private void internalMethod05013(ScreenHandler localValue1, int localValue2, String localValue3) {
      StringBuilder localValue4 = new StringBuilder();

      for (int localValue5 = 0; localValue5 < localValue2; localValue5++) {
         ItemStack localValue6 = localValue1.getSlot(localValue5).getStack();
         if (!localValue6.isEmpty()) {
            localValue4.append(localValue5).append('=').append(localValue6.getName().getString()).append("; ");
         }
      }

      RockstarClient.internalField0572
         .warn("[InventoryBuilder] \u043c\u0435\u043d\u044e \u043f\u043e\u043a\u0443\u043f\u043a\u0438: {} | \u0441\u043b\u043e\u0442\u044b: {}", localValue3, localValue4);
      ClientMessages.internalMethod03058(
         Text.of(
            "\u041c\u0435\u043d\u044e \u043f\u043e\u043a\u0443\u043f\u043a\u0438: "
               + localValue3
               + " \u2014 \u043b\u043e\u0442 \u043f\u0440\u043e\u043f\u0443\u0449\u0435\u043d"
         )
      );
      this.internalField1465++;
      this.internalField1742 = 0;
      this.internalField0796.clear();
      this.internalMethod10055();
      this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
      this.internalField0518.internalMethod00701();
   }

   private void internalMethod04112(int localValue1, int localValue2, int localValue3, int localValue4) {
      if (localValue1 > 0) {
         if (localValue3 == -1) {
            for (int localValue12 = 0; localValue12 < localValue1 && localValue2 != -1; localValue12++) {
               this.internalField0796.add(new int[]{localValue2, 0});
            }
         } else {
            int localValue5 = localValue1 / 10;
            int localValue6 = localValue1 % 10;
            boolean localValue7 = localValue6 > 0 && localValue4 != -1;
            boolean localValue8 = localValue6 == 0 || localValue2 != -1;
            int localValue9 = localValue7 ? localValue5 + 1 + (10 - localValue6) : Integer.MAX_VALUE;
            int localValue10 = localValue8 ? localValue5 + localValue6 : Integer.MAX_VALUE;
            if (localValue9 <= localValue10) {
               for (int localValue14 = 0; localValue14 <= localValue5; localValue14++) {
                  this.internalField0796.add(new int[]{localValue3, 0});
               }

               for (int localValue15 = 0; localValue15 < 10 - localValue6; localValue15++) {
                  this.internalField0796.add(new int[]{localValue4, 0});
               }
            } else {
               for (int localValue11 = 0; localValue11 < localValue5; localValue11++) {
                  this.internalField0796.add(new int[]{localValue3, 0});
               }

               for (int localValue13 = 0; localValue13 < localValue6 && localValue2 != -1; localValue13++) {
                  this.internalField0796.add(new int[]{localValue2, 0});
               }
            }
         }
      }
   }

   private void internalMethod09993() {
      ScreenHandler localValue1 = this.internalMethod06707();
      if (localValue1 != null && this.internalMethod08196().contains("\u043f\u043e\u043a\u0443\u043f\u043a")) {
         int[] localValue2 = this.internalField0796.poll();
         if (localValue2 != null) {
            this.internalMethod06926(localValue1, localValue2);
         } else {
            internalField0149.interactionManager.clickSlot(localValue1.syncId, this.internalField1466, 0, SlotActionType.PICKUP, internalField0149.player);
            this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1023;
            this.internalField0518.internalMethod00701();
         }
      } else {
         this.internalField0796.clear();
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1023;
         this.internalField0518.internalMethod00701();
      }
   }

   private void internalMethod09995() {
      if (this.internalField0518.internalMethod02365(1200L)) {
         InventoryBuilderModule.InternalType0023 localValue1 = this.internalField0192.internalField0430[this.internalField1470];
         int localValue2 = this.internalMethod07478(localValue1);
         if (localValue2 <= this.internalField1463) {
            if (this.internalField0518.internalMethod02365(2500L)) {
               this.internalMethod08055("\u041b\u043e\u0442 \u043f\u0435\u0440\u0435\u0445\u0432\u0430\u0442\u0438\u043b\u0438");
            }
         } else {
            this.internalField1465 = 0;
            this.internalField1739 = 0;
            this.internalField1738 = this.internalField1738 + (localValue2 - this.internalField1463);
            ClientMessages.internalMethod01809(
               Text.of(
                  "\u041a\u0443\u043f\u043b\u0435\u043d\u043e "
                     + (localValue2 - this.internalField1463)
                     + "\u00d7 "
                     + localValue1.internalMethod01243()
                     + (this.internalField1059 > 0L ? " \u043f\u043e " + MathUtils.internalMethod00005(this.internalField1059) : "")
               )
            );
            this.internalField1059 = 0L;
            this.internalField1077 = "";
            if (this.internalMethod01622(localValue1) - localValue2 > 0
               && this.internalMethod06707() != null
               && !this.internalMethod08196().contains("\u043f\u043e\u043a\u0443\u043f\u043a")) {
               this.internalMethod09997();
            } else {
               this.internalMethod10055();
               this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod08055(String localValue1) {
      this.internalField1465++;
      this.internalField1742 = 0;
      this.internalField0796.clear();
      if (!this.internalField1077.isEmpty()) {
         this.internalField0545.add(this.internalField1077);
      }

      this.internalField1077 = "";
      this.internalField1059 = 0L;
      InventoryBuilderModule.InternalType0023 localValue2 = this.internalField0192 != null && this.internalField1470 >= 0 && this.internalField1470 < 41
         ? this.internalField0192.internalField0430[this.internalField1470]
         : null;
      String localValue3 = localValue2 == null ? "" : ": " + localValue2.internalMethod01243();
      if (this.internalField1465 >= (int)this.internalField0382.internalMethod08576()) {
         ClientMessages.internalMethod03058(
            Text.of(localValue1 + localValue3 + " \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u044e \u043f\u0440\u0435\u0434\u043c\u0435\u0442")
         );
         this.internalMethod10055();
         this.internalMethod10052();
         this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
         this.internalField0518.internalMethod00701();
      } else {
         ClientMessages.internalMethod03058(Text.of(localValue1 + localValue3 + " \u2014 \u0431\u0435\u0440\u0443 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439"));
         if (this.internalMethod06707() != null && !this.internalMethod08196().contains("\u043f\u043e\u043a\u0443\u043f\u043a")) {
            this.internalMethod09997();
         } else {
            this.internalMethod10055();
            this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0142;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod09997() {
      this.internalField1054 = 0;
      this.internalField1055 = 0;
      this.internalField1099 = false;
      this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField1022;
      this.internalField0518.internalMethod00701();
   }

   private void internalMethod10052() {
      this.internalField1470++;
      this.internalField1465 = 0;
      this.internalField0545.clear();
      this.internalField1077 = "";
   }

   private String internalMethod03255(ItemStack localValue1, List<Text> localValue2, long localValue3) {
      StringBuilder localValue5 = new StringBuilder(ItemNbtUtils.internalMethod02181(localValue1).trim().toLowerCase(Locale.ROOT));
      localValue5.append('|').append(localValue1.getCount()).append('|').append(localValue3);

      for (Text localValue7 : localValue2) {
         String localValue8 = localValue7.getString();
         String localValue9 = localValue8.toLowerCase(Locale.ROOT);
         if (localValue9.contains("\u043f\u0440\u043e\u0434\u0430\u0432") || localValue9.contains("\u0432\u043b\u0430\u0434\u0435\u043b")) {
            localValue5.append('|').append(localValue8.trim());
            break;
         }
      }

      return localValue5.toString();
   }

   private void internalMethod10054() {
      if (!this.internalField0518.internalMethod02365(20000L) && this.internalField1464 <= 400) {
         if (this.internalMethod06707() != null) {
            this.internalMethod10055();
         } else {
            PlayerScreenHandler localValue1 = internalField0149.player.playerScreenHandler;
            if (this.internalField1735 > 0) {
               this.internalMethod01455(localValue1);
            } else if (!localValue1.getCursorStack().isEmpty()) {
               this.internalMethod00531(localValue1);
            } else {
               for (int localValue2 = 0; localValue2 < 41; localValue2++) {
                  InventoryBuilderModule.InternalType0023 localValue3 = this.internalField0192.internalField0430[localValue2];
                  if (localValue3 != null) {
                     int localValue4 = this.internalMethod06363(localValue2);
                     if (!this.internalField0546.contains(localValue4)) {
                        ItemStack localValue5 = localValue1.getSlot(localValue4).getStack();
                        boolean localValue6 = this.internalMethod07186(localValue5, localValue3);
                        int localValue7 = localValue6 ? localValue5.getCount() : 0;
                        if (!localValue6 || localValue7 != localValue3.internalField0228) {
                           if (!localValue6) {
                              int localValue8 = this.internalMethod01297(localValue1, localValue3, localValue4, 0, localValue5);
                              if (localValue8 >= 0) {
                                 this.internalMethod06924(localValue1, localValue8, localValue4);
                                 return;
                              }
                           } else if (this.internalMethod02911(localValue1, localValue3, localValue4, localValue5, localValue7)) {
                              return;
                           }
                        }
                     }
                  }
               }

               this.internalMethod09327();
            }
         }
      } else {
         this.internalMethod01693(
            this.internalField1464 > 400
               ? "\u0440\u0430\u0441\u043a\u043b\u0430\u0434\u043a\u0430 \u043d\u0435 \u0441\u0445\u043e\u0434\u0438\u0442\u0441\u044f"
               : "\u0440\u0430\u0441\u043a\u043b\u0430\u0434\u043a\u0430 \u0437\u0430\u0432\u0438\u0441\u043b\u0430"
         );
      }
   }

   private void internalMethod06924(ScreenHandler localValue1, int localValue2, int localValue3) {
      this.internalField1464++;
      this.internalMethod00583(localValue1, localValue3);
      if (localValue3 >= 36 && localValue3 <= 44) {
         this.internalMethod06313(localValue1, localValue2, localValue3 - 36);
      } else if (localValue2 >= 36 && localValue2 <= 44) {
         this.internalMethod06313(localValue1, localValue3, localValue2 - 36);
      } else {
         this.internalField1740 = localValue2;
         this.internalField1741 = localValue3;
         this.internalField1736 = this.internalMethod06923(localValue1, localValue2, localValue3);
         this.internalField1735 = 1;
         this.internalMethod06313(localValue1, this.internalField1740, this.internalField1736);
      }
   }

   private void internalMethod01455(ScreenHandler localValue1) {
      this.internalField1464++;
      if (this.internalField1735 == 1) {
         this.internalField1735 = 2;
         this.internalMethod06313(localValue1, this.internalField1741, this.internalField1736);
      } else {
         this.internalField1735 = 0;
         this.internalMethod06313(localValue1, this.internalField1740, this.internalField1736);
      }
   }

   private void internalMethod06313(ScreenHandler localValue1, int localValue2, int localValue3) {
      internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, localValue3, SlotActionType.SWAP, internalField0149.player);
      this.internalField0518.internalMethod00701();
   }

   private int internalMethod06923(ScreenHandler localValue1, int localValue2, int localValue3) {
      for (int localValue4 = 44; localValue4 >= 36; localValue4--) {
         if (localValue4 != localValue2 && localValue4 != localValue3 && !this.internalMethod06421(localValue4) && localValue1.getSlot(localValue4).getStack().isEmpty()) {
            return localValue4 - 36;
         }
      }

      for (int localValue5 = 44; localValue5 >= 36; localValue5--) {
         if (localValue5 != localValue2 && localValue5 != localValue3 && !this.internalMethod06421(localValue5)) {
            return localValue5 - 36;
         }
      }

      return 8;
   }

   private boolean internalMethod02911(ScreenHandler localValue1, InventoryBuilderModule.InternalType0023 localValue2, int localValue3, ItemStack localValue4, int localValue5) {
      if (localValue5 > localValue2.internalField0228) {
         this.internalField1464++;
         this.internalMethod00583(localValue1, localValue3);
         this.internalMethod09136(localValue1, localValue3, 0);
         return true;
      } else {
         int localValue6 = this.internalMethod01297(localValue1, localValue2, localValue3, localValue5, localValue4);
         if (localValue6 < 0) {
            return false;
         } else {
            this.internalField1464++;
            this.internalMethod00583(localValue1, localValue3);
            this.internalMethod09136(localValue1, localValue6, 0);
            return true;
         }
      }
   }

   private void internalMethod00531(ScreenHandler localValue1) {
      ItemStack localValue2 = localValue1.getCursorStack();

      for (int localValue3 = 0; localValue3 < 41; localValue3++) {
         InventoryBuilderModule.InternalType0023 localValue4 = this.internalField0192.internalField0430[localValue3];
         if (localValue4 != null) {
            int localValue5 = this.internalMethod06363(localValue3);
            if (!this.internalField0546.contains(localValue5) && this.internalMethod07186(localValue2, localValue4)) {
               ItemStack localValue6 = localValue1.getSlot(localValue5).getStack();
               int localValue7 = this.internalMethod07186(localValue6, localValue4) ? localValue6.getCount() : 0;
               if (localValue7 < localValue4.internalField0228 && (localValue6.isEmpty() || localValue7 != 0) && (localValue7 <= 0 || ItemStack.areItemsAndComponentsEqual(localValue6, localValue2))) {
                  boolean localValue8 = localValue7 + localValue2.getCount() <= localValue4.internalField0228;
                  this.internalMethod09136(localValue1, localValue5, localValue8 ? 0 : 1);
                  return;
               }
            }
         }
      }

      int localValue9 = this.internalMethod01454(localValue1);
      if (localValue9 < 0) {
         this.internalMethod01693("\u043d\u0435\u0442 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u0441\u043b\u043e\u0442\u0430");
      } else {
         this.internalMethod09136(localValue1, localValue9, 0);
      }
   }

   private int internalMethod01297(ScreenHandler localValue1, InventoryBuilderModule.InternalType0023 localValue2, int localValue3, int localValue4, ItemStack localValue5) {
      for (int localValue6 = 5; localValue6 <= 45; localValue6++) {
         if (localValue6 != localValue3) {
            ItemStack localValue7 = localValue1.getSlot(localValue6).getStack();
            if (this.internalMethod07186(localValue7, localValue2) && !this.internalMethod00584(localValue1, localValue6) && (localValue4 <= 0 || ItemStack.areItemsAndComponentsEqual(localValue5, localValue7))) {
               return localValue6;
            }
         }
      }

      return -1;
   }

   private void internalMethod09136(ScreenHandler localValue1, int localValue2, int localValue3) {
      if (this.internalField1748 != localValue2 || !this.internalMethod06870(localValue1, localValue2).equals(this.internalField0247)) {
         this.internalField1733 = 0;
      } else if (++this.internalField1733 >= 3) {
         ClientMessages.internalMethod03058(
            Text.of(
               "\u0421\u043b\u043e\u0442 "
                  + localValue2
                  + " \u043d\u0435 \u043e\u0442\u0432\u0435\u0447\u0430\u0435\u0442, \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u044e"
            )
         );
         this.internalField0546.add(localValue2);
         this.internalField1733 = 0;
      }

      this.internalField1748 = localValue2;
      this.internalField0247 = this.internalMethod06870(localValue1, localValue2);
      internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, localValue3, SlotActionType.PICKUP, internalField0149.player);
      this.internalField0518.internalMethod00701();
   }

   private String internalMethod06870(ScreenHandler localValue1, int localValue2) {
      ItemStack localValue3 = localValue1.getSlot(localValue2).getStack();
      return localValue3.isEmpty() ? "-" : Registries.ITEM.getId(localValue3.getItem()) + "x" + localValue3.getCount();
   }

   private void internalMethod00583(ScreenHandler localValue1, int localValue2) {
      this.internalField1748 = localValue2;
      this.internalField0247 = this.internalMethod06870(localValue1, localValue2);
   }

   private void internalMethod06926(ScreenHandler localValue1, int[] localValue2) {
      SlotActionType localValue3 = localValue2.length > 2 && localValue2[2] == 1 ? SlotActionType.SWAP : SlotActionType.PICKUP;
      internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2[0], localValue2[1], localValue3, internalField0149.player);
   }

   private int internalMethod06363(int localValue1) {
      if (localValue1 < 27) {
         return 9 + localValue1;
      } else if (localValue1 < 36) {
         return 36 + (localValue1 - 27);
      } else {
         return localValue1 < 40 ? 5 + (localValue1 - 36) : 45;
      }
   }

   private boolean internalMethod00584(ScreenHandler localValue1, int localValue2) {
      for (int localValue3 = 0; localValue3 < 41; localValue3++) {
         InventoryBuilderModule.InternalType0023 localValue4 = this.internalField0192.internalField0430[localValue3];
         if (localValue4 != null && this.internalMethod06363(localValue3) == localValue2) {
            ItemStack localValue5 = localValue1.getSlot(localValue2).getStack();
            if (this.internalMethod07186(localValue5, localValue4) && localValue5.getCount() == localValue4.internalField0228) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean internalMethod05776(InventoryBuilderModule.InternalType0023 localValue1, InventoryBuilderModule.InternalType0023 localValue2) {
      return localValue1.internalField0354.equals(localValue2.internalField0354) && localValue1.internalMethod04718().equalsIgnoreCase(localValue2.internalMethod04718());
   }

   private int internalMethod01622(InventoryBuilderModule.InternalType0023 localValue1) {
      int localValue2 = 0;

      for (InventoryBuilderModule.InternalType0023 localValue6 : this.internalField0192.internalField0430) {
         if (localValue6 != null && this.internalMethod05776(localValue6, localValue1)) {
            localValue2 += localValue6.internalField0228;
         }
      }

      return localValue2;
   }

   private boolean internalMethod06364(int localValue1) {
      InventoryBuilderModule.InternalType0023 localValue2 = this.internalField0192.internalField0430[localValue1];

      for (int localValue3 = 0; localValue3 < localValue1; localValue3++) {
         InventoryBuilderModule.InternalType0023 localValue4 = this.internalField0192.internalField0430[localValue3];
         if (localValue4 != null && this.internalMethod05776(localValue4, localValue2)) {
            return true;
         }
      }

      return false;
   }

   private int internalMethod01454(ScreenHandler localValue1) {
      for (int localValue2 = 9; localValue2 <= 44; localValue2++) {
         if (localValue1.getSlot(localValue2).getStack().isEmpty() && !this.internalMethod06421(localValue2)) {
            return localValue2;
         }
      }

      for (int localValue3 = 9; localValue3 <= 44; localValue3++) {
         if (localValue1.getSlot(localValue3).getStack().isEmpty()) {
            return localValue3;
         }
      }

      return -1;
   }

   private boolean internalMethod06421(int localValue1) {
      for (int localValue2 = 0; localValue2 < 41; localValue2++) {
         if (this.internalField0192.internalField0430[localValue2] != null && this.internalMethod06363(localValue2) == localValue1) {
            return true;
         }
      }

      return false;
   }

   private int internalMethod00582(ScreenHandler localValue1, int localValue2) {
      for (int localValue3 = 0; localValue3 < localValue2; localValue3++) {
         ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
         if (!localValue4.isEmpty() && localValue4.getName().getString().toLowerCase(Locale.ROOT).contains("\u0441\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u043a\u0430")) {
            return localValue3;
         }
      }

      return -1;
   }

   private boolean internalMethod04535(ItemStack localValue1) {
      for (Text localValue3 : this.internalMethod00191(localValue1)) {
         String localValue4 = localValue3.getString().trim();
         String localValue5 = localValue4.toLowerCase(Locale.ROOT);
         if (localValue5.contains("\u0434\u0435\u0448\u0435\u0432") && localValue5.contains("\u0435\u0434")) {
            return localValue4.startsWith("\u2713") || localValue4.startsWith("\u2714");
         }
      }

      return false;
   }

   private int[] internalMethod06362() {
      Matcher localValue1 = internalField1113.matcher(this.internalMethod08196());
      return localValue1.find() ? new int[]{Integer.parseInt(localValue1.group(1)), Integer.parseInt(localValue1.group(2))} : new int[]{1, 0};
   }

   private int internalMethod05329(ScreenHandler localValue1, int localValue2) {
      for (int localValue3 = 0; localValue3 < localValue2; localValue3++) {
         ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
         if (!localValue4.isEmpty()
            && localValue4.getName()
               .getString()
               .toLowerCase(Locale.ROOT)
               .contains("\u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0430")) {
            return localValue3;
         }
      }

      return -1;
   }

   private String internalMethod06802(InventoryBuilderModule.InternalType0023 localValue1) {
      if (!localValue1.internalField1078.isBlank()) {
         return localValue1.internalField1078;
      } else {
         if (!localValue1.internalField1079.isBlank()) {
            Identifier localValue2 = Identifier.tryParse(localValue1.internalField1079);
            Potion localValue3 = localValue2 == null ? null : (Potion)Registries.POTION.get(localValue2);
            if (localValue3 != null && !localValue3.getEffects().isEmpty()) {
               String localValue4 = ((StatusEffect)((StatusEffectInstance)localValue3.getEffects().getFirst()).getEffectType().value()).getName().getString();
               if (!localValue4.isBlank()) {
                  return "\u0417\u0435\u043b\u044c\u0435 " + localValue4.toLowerCase(Locale.ROOT);
               }
            }
         }

         String localValue5 = localValue1.internalMethod01243().trim();
         return !localValue5.isBlank() ? localValue5 : Text.translatable(((Item)Registries.ITEM.get(localValue1.internalField0354)).getTranslationKey()).getString();
      }
   }

   private int internalMethod07478(InventoryBuilderModule.InternalType0023 localValue1) {
      int localValue2 = 0;

      for (int localValue3 = 0; localValue3 < internalField0149.player.getInventory().size(); localValue3++) {
         ItemStack localValue4 = internalField0149.player.getInventory().getStack(localValue3);
         if (this.internalMethod07186(localValue4, localValue1) && this.internalMethod04080(localValue4, this.internalMethod00191(localValue4), localValue1)) {
            localValue2 += localValue4.getCount();
         }
      }

      return localValue2;
   }

   private boolean internalMethod07186(ItemStack localValue1, InventoryBuilderModule.InternalType0023 localValue2) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         if (localValue1.getItem() != Registries.ITEM.get(localValue2.internalField0354)) {
            return false;
         } else {
            String localValue3 = ItemNbtUtils.internalMethod02181(localValue1).trim().toLowerCase(Locale.ROOT);
            if (localValue2.internalField0276) {
               return localValue3.equals(localValue2.internalMethod04718().trim().toLowerCase(Locale.ROOT));
            } else {
               String localValue4 = localValue2.internalMethod08505();
               return localValue4.isBlank() || localValue3.contains(localValue4.toLowerCase(Locale.ROOT));
            }
         }
      } else {
         return false;
      }
   }

   private int internalMethod03666(List<Text> localValue1, String localValue2) {
      String localValue3 = localValue2.toLowerCase(Locale.ROOT);

      for (Text localValue5 : localValue1) {
         String localValue6 = localValue5.getString().trim();
         int localValue7 = localValue6.toLowerCase(Locale.ROOT).indexOf(localValue3);
         if (localValue7 >= 0) {
            String localValue8 = localValue6.substring(localValue7 + localValue2.length()).trim();
            int localValue9 = localValue8.indexOf(32);
            if (localValue9 > 0) {
               localValue8 = localValue8.substring(0, localValue9);
            }

            localValue8 = localValue8.replaceAll("[^IVX]", "");
            return localValue8.isEmpty() ? 0 : this.internalMethod00091(localValue8);
         }
      }

      return -1;
   }

   private int internalMethod00091(String localValue1) {
      int localValue2 = 0;
      int localValue3 = 0;

      for (int localValue4 = localValue1.length() - 1; localValue4 >= 0; localValue4--) {
         byte localValue5 = switch (localValue1.charAt(localValue4)) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            default -> 0;
         };
         localValue2 += localValue5 < localValue3 ? -localValue5 : localValue5;
         localValue3 = Math.max(localValue3, localValue5);
      }

      return localValue2;
   }

   private int internalMethod07472(List<Text> localValue1) {
      int localValue2 = 0;
      Matcher localValue3 = internalField1111.matcher("");

      for (Text localValue5 : localValue1) {
         localValue3.reset(localValue5.getString());

         while (localValue3.find()) {
            localValue2 = Math.max(localValue2, Integer.parseInt(localValue3.group(1)) * 60 + Integer.parseInt(localValue3.group(2)));
         }
      }

      return localValue2;
   }

   private boolean internalMethod04080(ItemStack localValue1, List<Text> localValue2, InventoryBuilderModule.InternalType0023 localValue3) {
      if (localValue3.internalField1053 > 0 && localValue1.isDamageable() && localValue1.getMaxDamage() > 0) {
         float localValue4 = (localValue1.getMaxDamage() - localValue1.getDamage()) * 100.0F / localValue1.getMaxDamage();
         if (localValue4 < localValue3.internalField1053) {
            return false;
         }
      }

      for (Entry localValue5 : localValue3.internalField0543.entrySet()) {
         int localValue6 = this.internalMethod03666(localValue2, (String)localValue5.getKey());
         if (localValue6 < 0 || localValue6 < (Integer)localValue5.getValue()) {
            return false;
         }
      }

      return localValue3.internalField1055 <= 0 || this.internalMethod07472(localValue2) >= localValue3.internalField1055;
   }

   private long internalMethod02028(List<Text> localValue1, int localValue2) {
      long localValue3 = -1L;
      long localValue5 = -1L;

      for (Text localValue8 : localValue1) {
         String localValue9 = localValue8.getString();
         String localValue10 = localValue9.toLowerCase(Locale.ROOT);
         if (localValue10.contains("\u0446\u0435\u043d\u0430") || localValue10.contains("\u0441\u0442\u043e\u0438\u043c\u043e\u0441\u0442\u044c")) {
            int localValue11 = localValue9.indexOf(58);
            long localValue12 = this.internalMethod01692(localValue11 < 0 ? localValue9 : localValue9.substring(localValue11 + 1));
            if (localValue12 >= 0L) {
               if (localValue10.contains("\u0435\u0434")) {
                  localValue3 = localValue12;
               } else {
                  localValue5 = localValue12;
               }
            }
         }
      }

      if (localValue3 >= 0L) {
         return localValue3;
      } else if (localValue5 >= 0L) {
         return localValue2 > 1 ? localValue5 / localValue2 : localValue5;
      } else {
         return -1L;
      }
   }

   private long internalMethod01692(String localValue1) {
      StringBuilder localValue2 = new StringBuilder();

      for (char localValue6 : localValue1.toCharArray()) {
         if (Character.isDigit(localValue6)) {
            localValue2.append(localValue6);
         }
      }

      if (localValue2.isEmpty()) {
         return -1L;
      } else {
         try {
            return Long.parseLong(localValue2.toString());
         } catch (NumberFormatException localValue7) {
            return -1L;
         }
      }
   }

   private List<Text> internalMethod00191(ItemStack localValue1) {
      return localValue1.getTooltip(TooltipContext.create(internalField0149.world), internalField0149.player, TooltipType.BASIC);
   }

   private String internalMethod08196() {
      return internalField0149.currentScreen == null ? "" : internalField0149.currentScreen.getTitle().getString().toLowerCase(Locale.ROOT);
   }

   private ScreenHandler internalMethod06707() {
      if (!(internalField0149.currentScreen instanceof HandledScreen)) {
         return null;
      } else {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         return localValue1 != null && localValue1 != internalField0149.player.playerScreenHandler ? localValue1 : null;
      }
   }

   private void internalMethod10055() {
      ScreenHandler localValue1 = this.internalMethod06707();
      if (localValue1 != null) {
         internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(localValue1.syncId));
         internalField0149.player.closeHandledScreen();
      }
   }

   @Override
   public void onDisable() {
      this.internalField0141 = InventoryBuilderModule.InternalType0065.internalField0141;
      this.internalField0192 = null;
      this.internalField0796.clear();
      super.onDisable();
   }

   public static void internalMethod09167() {
      if (internalField1145.isEmpty()) {
         try {
            try (InputStream localValue0 = InventoryBuilderModule.class.getResourceAsStream("/assets/rockstar/auction_items.json")) {
               if (localValue0 == null) {
                  return;
               }

               JsonObject localValue1 = (JsonObject)ConfigManager.internalField0931.fromJson(new InputStreamReader(localValue0, StandardCharsets.UTF_8), JsonObject.class);
               if (localValue1 != null && localValue1.has("items")) {
                  for (JsonElement localValue3 : localValue1.getAsJsonArray("items")) {
                     JsonObject localValue4 = localValue3.getAsJsonObject();
                     Identifier localValue5 = Identifier.tryParse(localValue4.get("id").getAsString());
                     if (localValue5 != null) {
                        InventoryBuilderModule.InternalType0023 localValue6 = new InventoryBuilderModule.InternalType0023(localValue5, localValue4.get("query").getAsString());
                        localValue6.internalField0248 = localValue4.has("category") ? localValue4.get("category").getAsString() : "";
                        localValue6.internalField1077 = localValue4.has("match") ? localValue4.get("match").getAsString() : "";
                        localValue6.internalField1076 = localValue4.has("texture") ? localValue4.get("texture").getAsString() : "";
                        localValue6.internalField0227 = localValue4.has("color") ? localValue4.get("color").getAsInt() : -1;
                        localValue6.internalField1056 = localValue4.has("duration") ? localValue4.get("duration").getAsInt() : 0;
                        localValue6.internalField0276 = !localValue4.has("exact") || localValue4.get("exact").getAsBoolean();
                        localValue6.internalField1054 = localValue4.has("budget") ? localValue4.get("budget").getAsInt() : 0;
                        localValue6.internalField1464 = localValue4.has("stack") ? localValue4.get("stack").getAsInt() : 0;
                        localValue6.internalField1078 = localValue4.has("search") ? localValue4.get("search").getAsString() : "";
                        localValue6.internalField0277 = true;
                        if (localValue4.has("traits")) {
                           for (JsonElement localValue8 : localValue4.getAsJsonArray("traits")) {
                              JsonObject localValue9 = localValue8.getAsJsonObject();
                              localValue6.internalField0416.add(new InventoryBuilderModule.InternalType0066(localValue9.get("name").getAsString(), localValue9.get("max").getAsInt()));
                           }
                        }

                        if (localValue4.has("variants")) {
                           for (JsonElement localValue16 : localValue4.getAsJsonArray("variants")) {
                              LinkedHashMap localValue17 = new LinkedHashMap();

                              for (Entry localValue11 : localValue16.getAsJsonObject().entrySet()) {
                                 localValue17.put((String)localValue11.getKey(), ((JsonElement)localValue11.getValue()).getAsInt());
                              }

                              if (!localValue17.isEmpty()) {
                                 localValue6.internalField0417.add(localValue17);
                              }
                           }
                        }

                        internalMethod01623(localValue6);
                        internalField1145.add(localValue6);
                     }
                  }

                  return;
               }
            }
         } catch (Exception localValue14) {
            System.err.println("Error reading auction catalog: " + localValue14.getMessage());
         }
      }
   }

   private static void internalMethod01623(InventoryBuilderModule.InternalType0023 localValue0) {
      if (localValue0.internalField0417.size() == 1) {
         localValue0.internalField0543.putAll(localValue0.internalField0417.getFirst());
      }
   }

   private static void internalMethod07479(InventoryBuilderModule.InternalType0023 localValue0) {
      for (InventoryBuilderModule.InternalType0023 localValue2 : internalField1145) {
         if (localValue2.internalField0354.equals(localValue0.internalField0354) && localValue2.internalMethod04718().equalsIgnoreCase(localValue0.internalMethod04718())) {
            localValue0.internalField0247 = localValue2.internalField0247;
            localValue0.internalField1077 = localValue2.internalField1077;
            localValue0.internalField0277 = localValue2.internalField0277;
            localValue0.internalField0276 = localValue2.internalField0276;
            localValue0.internalField1076 = localValue2.internalField1076;
            localValue0.internalField1079 = localValue2.internalField1079;
            localValue0.internalField0227 = localValue2.internalField0227;
            localValue0.internalField1056 = localValue2.internalField1056;
            localValue0.internalField1054 = localValue2.internalField1054;
            localValue0.internalField1464 = localValue2.internalField1464;
            localValue0.internalField1078 = localValue2.internalField1078;
            localValue0.internalField0416.clear();
            localValue0.internalField0416.addAll(localValue2.internalField0416);
            localValue0.internalField0417.clear();

            for (Map localValue4 : localValue2.internalField0417) {
               localValue0.internalField0417.add(new LinkedHashMap<>(localValue4));
            }

            localValue0.internalField0543.keySet().removeIf(localValue1 -> localValue2.internalField0416.stream().noneMatch(localValue1x -> localValue1x.internalMethod03562().equals(localValue1)));
            internalMethod01623(localValue0);
            return;
         }
      }
   }

   public static void internalMethod09170() {
      internalMethod09167();
      if (!internalField0277) {
         internalField0277 = true;
         internalField0417.clear();
         File[] localValue0 = internalField0148.listFiles((localValue0x, localValue1x) -> localValue1x.toLowerCase(Locale.ROOT).endsWith(".rock"));
         if (localValue0 != null && localValue0.length != 0) {
            LinkedHashMap<String, Integer> localValue1 = new LinkedHashMap<>();
            ArrayList<InventoryBuilderModule.InternalType0024> localValue2 = new ArrayList<>();

            for (File localValue6 : localValue0) {
               String localValue7 = localValue6.getName();

               try (FileReader localValue8 = new FileReader(localValue6, StandardCharsets.UTF_8)) {
                  JsonObject localValue9 = (JsonObject)ConfigManager.internalField0931.fromJson(localValue8, JsonObject.class);
                  InventoryBuilderModule.InternalType0024 localValue10 = internalMethod06735(localValue9, localValue7.substring(0, localValue7.length() - ".rock".length()));
                  if (localValue10 != null) {
                     localValue10.internalField0247 = localValue7;
                     localValue1.put(localValue7, localValue9.has("order") ? localValue9.get("order").getAsInt() : Integer.MAX_VALUE);
                     localValue2.add(localValue10);
                  }
               } catch (Exception localValue13) {
                  RockstarClient.internalField0572
                     .warn(
                        "[InventoryBuilder] \u043f\u0440\u0435\u0441\u0435\u0442 {} \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f: {}",
                        localValue7,
                        localValue13.getMessage()
                     );
               }
            }

            localValue2.sort(
               Comparator.<InventoryBuilderModule.InternalType0024>comparingInt(localValue1x -> localValue1.getOrDefault(localValue1x.internalField0247, Integer.MAX_VALUE))
                  .thenComparing(localValue0x -> localValue0x.internalField0247, String.CASE_INSENSITIVE_ORDER)
            );
            internalField0417.addAll(localValue2);
         } else {
            internalMethod10056();
         }
      }
   }

   private static void internalMethod10056() {
      if (internalField0147.exists()) {
         ArrayList localValue0 = new ArrayList();

         try (FileReader localValue1 = new FileReader(internalField0147, StandardCharsets.UTF_8)) {
            JsonObject localValue2 = (JsonObject)ConfigManager.internalField0931.fromJson(localValue1, JsonObject.class);
            if (localValue2 != null && localValue2.has("presets")) {
               for (JsonElement localValue4 : localValue2.getAsJsonArray("presets")) {
                  InventoryBuilderModule.InternalType0024 localValue5 = internalMethod06735(localValue4.getAsJsonObject(), "\u041f\u0440\u0435\u0441\u0435\u0442");
                  if (localValue5 != null) {
                     localValue0.add(localValue5);
                  }
               }
            }
         } catch (Exception localValue8) {
            RockstarClient.internalField0572
               .warn(
                  "[InventoryBuilder] \u0441\u0442\u0430\u0440\u044b\u0435 \u043f\u0440\u0435\u0441\u0435\u0442\u044b \u043d\u0435 \u0447\u0438\u0442\u0430\u044e\u0442\u0441\u044f: {}",
                  localValue8.getMessage()
               );
            return;
         }

         internalField0417.addAll(localValue0);
         if (internalMethod09171()) {
            File[] localValue9 = internalField0148.listFiles((localValue0x, localValue1x) -> localValue1x.toLowerCase(Locale.ROOT).endsWith(".rock"));
            if (localValue9 != null && localValue9.length >= localValue0.size()) {
               if (!internalField0147.delete()) {
                  internalField0147.deleteOnExit();
               }

               RockstarClient.internalField0572
                  .info(
                     "[InventoryBuilder] \u043f\u0440\u0435\u0441\u0435\u0442\u044b \u043f\u0435\u0440\u0435\u043d\u0435\u0441\u0435\u043d\u044b \u0432 {} ({} \u0448\u0442.)",
                     internalField0148,
                     localValue0.size()
                  );
            } else {
               RockstarClient.internalField0572
                  .warn(
                     "[InventoryBuilder] \u043f\u0435\u0440\u0435\u043d\u043e\u0441 \u043f\u0440\u0435\u0441\u0435\u0442\u043e\u0432 \u043d\u0435 \u0443\u0434\u0430\u043b\u0441\u044f, \u0441\u0442\u0430\u0440\u044b\u0439 \u0444\u0430\u0439\u043b \u043e\u0441\u0442\u0430\u0432\u043b\u0435\u043d"
                  );
            }
         }
      }
   }

   public static boolean internalMethod09171() {
      if (!internalField0277) {
         return false;
      } else {
         try {
            Files.createDirectories(internalField0148.toPath());
            HashSet localValue0 = new HashSet();

            for (int localValue1 = 0; localValue1 < internalField0417.size(); localValue1++) {
               InventoryBuilderModule.InternalType0024 localValue2 = internalField0417.get(localValue1);
               String localValue3 = internalMethod04154(localValue2, localValue0);
               localValue0.add(localValue3.toLowerCase(Locale.ROOT));
               if (!localValue2.internalField0247.isEmpty() && !localValue2.internalField0247.equalsIgnoreCase(localValue3)) {
                  new File(internalField0148, localValue2.internalField0247).delete();
               }

               localValue2.internalField0247 = localValue3;
               JsonObject localValue4 = internalMethod01215(localValue2);
               localValue4.addProperty("order", localValue1);
               ConfigManager.internalMethod01467(new File(internalField0148, localValue3), localValue4);
            }

            File[] localValue7 = internalField0148.listFiles((localValue0x, localValue1x) -> localValue1x.toLowerCase(Locale.ROOT).endsWith(".rock"));
            if (localValue7 != null) {
               for (File localValue5 : localValue7) {
                  if (!localValue0.contains(localValue5.getName().toLowerCase(Locale.ROOT))) {
                     localValue5.delete();
                  }
               }
            }

            return true;
         } catch (Exception localValue6) {
            RockstarClient.internalField0572
               .warn(
                  "[InventoryBuilder] \u043f\u0440\u0435\u0441\u0435\u0442\u044b \u043d\u0435 \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u043b\u0438\u0441\u044c: {}",
                  localValue6.getMessage()
               );
            return false;
         }
      }
   }

   public static String internalMethod03299(InventoryBuilderModule.InternalType0024 localValue0) {
      return internalMethod01215(localValue0).toString();
   }

   public static boolean internalMethod06969(String localValue0, String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         try {
            internalMethod09170();
            String localValue2 = localValue0 != null && !localValue0.isBlank() ? localValue0.trim() : "\u041f\u0440\u0435\u0441\u0435\u0442";
            InventoryBuilderModule.InternalType0024 localValue3 = internalMethod06735((JsonObject)ConfigManager.internalField0931.fromJson(localValue1, JsonObject.class), localValue2);
            if (localValue3 != null && localValue3.internalMethod01393() != 0) {
               localValue3.internalField0248 = internalMethod07976(localValue2);
               localValue3.internalField0247 = "";
               internalField0417.add(localValue3);
               return internalMethod09171();
            } else {
               return false;
            }
         } catch (Exception localValue4) {
            RockstarClient.internalField0572
               .warn(
                  "[InventoryBuilder] \u0447\u0443\u0436\u043e\u0439 \u043f\u0440\u0435\u0441\u0435\u0442 \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f: {}",
                  localValue4.getMessage()
               );
            return false;
         }
      } else {
         return false;
      }
   }

   private static String internalMethod07976(String localValue0) {
      String localValue1 = localValue0;

      for (int localValue2 = 2; internalMethod01694(localValue1) && localValue2 < 100; localValue2++) {
         localValue1 = localValue0 + " " + localValue2;
      }

      return localValue1;
   }

   private static boolean internalMethod01694(String localValue0) {
      for (InventoryBuilderModule.InternalType0024 localValue2 : internalField0417) {
         if (localValue2.internalField0248.equalsIgnoreCase(localValue0)) {
            return true;
         }
      }

      return false;
   }

   private static InventoryBuilderModule.InternalType0024 internalMethod06735(JsonObject localValue0, String localValue1) {
      if (localValue0 != null && localValue0.has("slots")) {
         String localValue2 = localValue0.has("name") ? localValue0.get("name").getAsString() : "";
         InventoryBuilderModule.InternalType0024 localValue3 = new InventoryBuilderModule.InternalType0024(localValue2.isBlank() ? localValue1 : localValue2);

         for (JsonElement localValue5 : localValue0.getAsJsonArray("slots")) {
            JsonObject localValue6 = localValue5.getAsJsonObject();
            int localValue7 = localValue6.get("slot").getAsInt();
            if (localValue7 >= 0 && localValue7 < 41) {
               InventoryBuilderModule.InternalType0023 localValue8 = new InventoryBuilderModule.InternalType0023();
               Identifier localValue9 = Identifier.tryParse(localValue6.get("item").getAsString());
               if (localValue9 != null) {
                  localValue8.internalField0354 = localValue9;
                  localValue8.internalField0247 = localValue6.has("query") ? localValue6.get("query").getAsString() : "";
                  localValue8.internalField1077 = localValue6.has("match") ? localValue6.get("match").getAsString() : "";
                  localValue8.internalField0277 = localValue6.has("custom") && localValue6.get("custom").getAsBoolean();
                  localValue8.internalField0228 = localValue6.has("amount") ? localValue6.get("amount").getAsInt() : 1;
                  localValue8.internalField1053 = localValue6.has("durability") ? localValue6.get("durability").getAsInt() : 0;
                  localValue8.internalField1055 = localValue6.has("minDuration") ? localValue6.get("minDuration").getAsInt() : 0;
                  localValue8.internalField0229 = localValue6.has("price") ? localValue6.get("price").getAsLong() : 0L;
                  localValue8.internalField1076 = localValue6.has("texture") ? localValue6.get("texture").getAsString() : "";
                  localValue8.internalField0227 = localValue6.has("color") ? localValue6.get("color").getAsInt() : -1;
                  localValue8.internalField1079 = localValue6.has("potion") ? localValue6.get("potion").getAsString() : "";
                  localValue8.internalField0276 = localValue6.has("exact") && localValue6.get("exact").getAsBoolean();
                  localValue8.internalField1056 = localValue6.has("duration") ? localValue6.get("duration").getAsInt() : 0;
                  localValue8.internalField1054 = localValue6.has("budget") ? localValue6.get("budget").getAsInt() : 0;
                  localValue8.internalField1464 = localValue6.has("stack") ? localValue6.get("stack").getAsInt() : 0;
                  localValue8.internalField1078 = localValue6.has("search") ? localValue6.get("search").getAsString() : "";
                  if (localValue6.has("variants")) {
                     for (JsonElement localValue11 : localValue6.getAsJsonArray("variants")) {
                        LinkedHashMap localValue12 = new LinkedHashMap();

                        for (Entry localValue14 : localValue11.getAsJsonObject().entrySet()) {
                           localValue12.put((String)localValue14.getKey(), ((JsonElement)localValue14.getValue()).getAsInt());
                        }

                        if (!localValue12.isEmpty()) {
                           localValue8.internalField0417.add(localValue12);
                        }
                     }
                  }

                  if (localValue6.has("required")) {
                     for (Entry localValue17 : localValue6.getAsJsonObject("required").entrySet()) {
                        localValue8.internalField0543.put((String)localValue17.getKey(), ((JsonElement)localValue17.getValue()).getAsInt());
                     }
                  }

                  if (localValue6.has("options")) {
                     for (JsonElement localValue18 : localValue6.getAsJsonArray("options")) {
                        JsonObject localValue19 = localValue18.getAsJsonObject();
                        localValue8.internalField0416.add(new InventoryBuilderModule.InternalType0066(localValue19.get("name").getAsString(), localValue19.get("max").getAsInt()));
                     }
                  }

                  internalMethod07479(localValue8);
                  localValue8.internalField0228 = Math.max(1, Math.min(localValue8.internalField0228, localValue8.internalMethod00510()));
                  localValue3.internalField0430[localValue7] = localValue8;
               }
            }
         }

         return localValue3;
      } else {
         return null;
      }
   }

   private static JsonObject internalMethod01215(InventoryBuilderModule.InternalType0024 localValue0) {
      JsonObject localValue1 = new JsonObject();
      localValue1.addProperty("name", localValue0.internalField0248);
      JsonArray localValue2 = new JsonArray();

      for (int localValue3 = 0; localValue3 < 41; localValue3++) {
         InventoryBuilderModule.InternalType0023 localValue4 = localValue0.internalField0430[localValue3];
         if (localValue4 != null) {
            JsonObject localValue5 = new JsonObject();
            localValue5.addProperty("slot", localValue3);
            localValue5.addProperty("item", localValue4.internalField0354.toString());
            localValue5.addProperty("query", localValue4.internalField0247);
            localValue5.addProperty("match", localValue4.internalField1077);
            localValue5.addProperty("custom", localValue4.internalField0277);
            localValue5.addProperty("amount", localValue4.internalField0228);
            localValue5.addProperty("durability", localValue4.internalField1053);
            localValue5.addProperty("minDuration", localValue4.internalField1055);
            localValue5.addProperty("price", localValue4.internalField0229);
            localValue5.addProperty("texture", localValue4.internalField1076);
            localValue5.addProperty("color", localValue4.internalField0227);
            localValue5.addProperty("potion", localValue4.internalField1079);
            localValue5.addProperty("exact", localValue4.internalField0276);
            localValue5.addProperty("duration", localValue4.internalField1056);
            localValue5.addProperty("budget", localValue4.internalField1054);
            localValue5.addProperty("stack", localValue4.internalField1464);
            localValue5.addProperty("search", localValue4.internalField1078);
            JsonObject localValue6 = new JsonObject();
            localValue4.internalField0543.forEach(localValue6::addProperty);
            localValue5.add("required", localValue6);
            JsonArray localValue7 = new JsonArray();

            for (Map<String, Integer> localValue9 : localValue4.internalField0417) {
               JsonObject localValue10 = new JsonObject();
               localValue9.forEach(localValue10::addProperty);
               localValue7.add(localValue10);
            }

            localValue5.add("variants", localValue7);
            JsonArray localValue12 = new JsonArray();

            for (InventoryBuilderModule.InternalType0066 localValue14 : localValue4.internalField0416) {
               JsonObject localValue11 = new JsonObject();
               localValue11.addProperty("name", localValue14.internalMethod03562());
               localValue11.addProperty("max", localValue14.internalMethod04555());
               localValue12.add(localValue11);
            }

            localValue5.add("options", localValue12);
            localValue2.add(localValue5);
         }
      }

      localValue1.add("slots", localValue2);
      return localValue1;
   }

   private static String internalMethod04154(InventoryBuilderModule.InternalType0024 localValue0, Set<String> localValue1) {
      String localValue2 = internalMethod08173(localValue0.internalField0248);
      String localValue3 = localValue2 + ".rock";

      for (int localValue4 = 2; localValue1.contains(localValue3.toLowerCase(Locale.ROOT)); localValue4++) {
         localValue3 = localValue2 + "-" + localValue4 + ".rock";
      }

      return localValue3;
   }

   private static String internalMethod08173(String localValue0) {
      StringBuilder localValue1 = new StringBuilder();

      for (char localValue5 : (localValue0 == null ? "" : localValue0).trim().toCharArray()) {
         boolean localValue6 = localValue5 == ' ';
         if (!localValue6 || !localValue1.isEmpty() && localValue1.charAt(localValue1.length() - 1) != ' ') {
            boolean localValue7 = localValue6 || localValue5 == '-' || localValue5 == '_' || Character.isLetterOrDigit(localValue5);
            localValue1.append(localValue7 ? localValue5 : '_');
            if (localValue1.length() >= 48) {
               break;
            }
         }
      }

      String localValue8 = localValue1.toString().trim();
      if (localValue8.isEmpty()) {
         localValue8 = "preset";
      }

      return internalField0293.matcher(localValue8).matches() ? localValue8 + "_" : localValue8;
   }

   @Generated
   public ButtonSetting internalMethod04055() {
      return this.internalField0663;
   }

   @Generated
   public ButtonSetting internalMethod04811() {
      return this.internalField0662;
   }

   @Generated
   public SliderSetting internalMethod01790() {
      return this.internalField0383;
   }

   @Generated
   public SliderSetting internalMethod02448() {
      return this.internalField0382;
   }

   @Generated
   public SliderSetting internalMethod08294() {
      return this.internalField1142;
   }

   @Generated
   public BooleanSetting internalMethod04001() {
      return this.internalField0650;
   }

   @Generated
   public long internalMethod06046() {
      return this.internalField0230;
   }

   @Generated
   public Stopwatch internalMethod02908() {
      return this.internalField0519;
   }

   @Generated
   public Stopwatch internalMethod03621() {
      return this.internalField0518;
   }

   @Generated
   public Deque<int[]> internalMethod00067() {
      return this.internalField0796;
   }

   @Generated
   public boolean internalMethod09325() {
      return this.internalField0276;
   }

   @Generated
   public int internalMethod08915() {
      return this.internalField1055;
   }

   @Generated
   public int internalMethod08916() {
      return this.internalField1056;
   }

   @Generated
   public boolean internalMethod09328() {
      return this.internalField1099;
   }

   @Generated
   public int internalMethod08924() {
      return this.internalField1054;
   }

   @Generated
   public int internalMethod08925() {
      return this.internalField1464;
   }

   @Generated
   public InventoryBuilderModule.InternalType0065 internalMethod06172() {
      return this.internalField0141;
   }

   @Generated
   public InventoryBuilderModule.InternalType0024 internalMethod04222() {
      return this.internalField0192;
   }

   @Generated
   public int internalMethod09161() {
      return this.internalField1470;
   }

   @Generated
   public int internalMethod09162() {
      return this.internalField1465;
   }

   @Generated
   public int internalMethod09166() {
      return this.internalField1463;
   }

   @Generated
   public int internalMethod09169() {
      return this.internalField1466;
   }

   @Generated
   public int internalMethod09323() {
      return this.internalField1467;
   }

   @Generated
   public long internalMethod06048() {
      return this.internalField1059;
   }

   @Generated
   public int internalMethod09326() {
      return this.internalField1740;
   }

   @Generated
   public int internalMethod09329() {
      return this.internalField1741;
   }

   @Generated
   public int internalMethod09332() {
      return this.internalField1736;
   }

   @Generated
   public int internalMethod09990() {
      return this.internalField1735;
   }

   @Generated
   public int internalMethod09992() {
      return this.internalField1748;
   }

   @Generated
   public String internalMethod08577() {
      return this.internalField0247;
   }

   @Generated
   public int internalMethod09994() {
      return this.internalField1733;
   }

   @Generated
   public Set<Integer> internalMethod00263() {
      return this.internalField0546;
   }

   @Generated
   public int internalMethod09996() {
      return this.internalField1738;
   }

   @Generated
   public Set<String> internalMethod04981() {
      return this.internalField0545;
   }

   @Generated
   public String internalMethod07939() {
      return this.internalField1077;
   }

   @Generated
   public boolean internalMethod09331() {
      return this.internalField1100;
   }

   @Generated
   public String internalMethod08309() {
      return this.internalField1076;
   }

   @Generated
   public int internalMethod10051() {
      return this.internalField1739;
   }

   @Generated
   public EventListener<KeyPressEvent> internalMethod04974() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod01240() {
      return this.internalField0158;
   }

   @Generated
   public int internalMethod10053() {
      return this.internalField1742;
   }

   @Generated
   public static List<InventoryBuilderModule.InternalType0024> internalMethod05853() {
      return internalField0417;
   }

   @Generated
   public static List<InventoryBuilderModule.InternalType0023> internalMethod03453() {
      return internalField1145;
   }

   public static class InternalType0023 {
      public Identifier internalField0354 = Identifier.ofVanilla("stone");
      public String internalField0248 = "";
      public String internalField0247 = "";
      public String internalField1077 = "";
      public String internalField1076 = "";
      public String internalField1079 = "";
      public int internalField0227 = -1;
      public boolean internalField0277;
      public boolean internalField0276;
      public int internalField0228 = 1;
      public int internalField1053 = 75;
      public int internalField1055;
      public long internalField0229;
      public final Map<String, Integer> internalField0543 = new LinkedHashMap<>();
      public final List<InventoryBuilderModule.InternalType0066> internalField0416 = new ArrayList<>();
      public int internalField1056;
      public int internalField1054;
      public final List<Map<String, Integer>> internalField0417 = new ArrayList<>();
      public int internalField1464;
      public String internalField1078 = "";

      public InternalType0023(Identifier localValue1, String localValue2) {
         this.internalField0354 = localValue1;
         this.internalField0247 = localValue2;
      }

      public InternalType0023() {
      }

      public String internalMethod04718() {
         return this.internalField1077.isBlank() ? this.internalField0247 : this.internalField0247 + " " + this.internalField1077;
      }

      public String internalMethod01243() {
         return InventoryBuilderModule.internalMethod04599(this.internalMethod04718());
      }

      public String internalMethod08505() {
         if (!this.internalField1077.isBlank()) {
            return this.internalField1077;
         } else {
            return this.internalField0277 ? this.internalField0247 : "";
         }
      }

      public ItemStack internalMethod06731() {
         Item localValue1 = (Item)Registries.ITEM.get(this.internalField0354);
         ItemStack localValue2 = localValue1.getDefaultStack();
         localValue2.setCount(Math.max(1, Math.min(this.internalField0228, localValue1.getMaxCount())));
         if (!this.internalField1076.isBlank()) {
            PropertyMap localValue3 = new PropertyMap(com.google.common.collect.ImmutableMultimap.of("textures", new Property("textures", this.internalField1076)));
            localValue2.set(
               DataComponentTypes.PROFILE,
               ProfileComponent.ofStatic(new com.mojang.authlib.GameProfile(UUID.nameUUIDFromBytes(this.internalField1076.getBytes(StandardCharsets.UTF_8)), "", localValue3))
            );
         }

         if (!this.internalField1079.isBlank()) {
            Identifier localValue5 = Identifier.tryParse(this.internalField1079);
            Potion localValue4 = localValue5 == null ? null : (Potion)Registries.POTION.get(localValue5);
            if (localValue4 != null) {
               localValue2.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Registries.POTION.getEntry(localValue4)));
            }
         } else if (this.internalField0227 >= 0) {
            localValue2.set(
               DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.of(this.internalField0227), List.of(), Optional.empty())
            );
         }

         return localValue2;
      }

      public boolean internalMethod00511() {
         return ((Item)Registries.ITEM.get(this.internalField0354)).getDefaultStack().isDamageable();
      }

      public int internalMethod00510() {
         if (this.internalField1464 > 0) {
            return this.internalField1464;
         } else {
            ItemStack localValue1 = ((Item)Registries.ITEM.get(this.internalField0354)).getDefaultStack();
            return localValue1.isOf(Items.POTION) ? 64 : localValue1.getMaxCount();
         }
      }

      public InventoryBuilderModule.InternalType0023 internalMethod06720() {
         InventoryBuilderModule.InternalType0023 localValue1 = new InventoryBuilderModule.InternalType0023(this.internalField0354, this.internalField0247);
         localValue1.internalField0248 = this.internalField0248;
         localValue1.internalField1077 = this.internalField1077;
         localValue1.internalField1076 = this.internalField1076;
         localValue1.internalField1079 = this.internalField1079;
         localValue1.internalField0227 = this.internalField0227;
         localValue1.internalField0277 = this.internalField0277;
         localValue1.internalField0276 = this.internalField0276;
         localValue1.internalField0228 = this.internalField0228;
         localValue1.internalField1053 = this.internalField1053;
         localValue1.internalField1055 = this.internalField1055;
         localValue1.internalField0229 = this.internalField0229;
         localValue1.internalField1056 = this.internalField1056;
         localValue1.internalField1054 = this.internalField1054;
         localValue1.internalField1464 = this.internalField1464;
         localValue1.internalField1078 = this.internalField1078;
         localValue1.internalField0543.putAll(this.internalField0543);
         localValue1.internalField0416.addAll(this.internalField0416);

         for (Map localValue3 : this.internalField0417) {
            localValue1.internalField0417.add(new LinkedHashMap<>(localValue3));
         }

         return localValue1;
      }
   }

   public static class InternalType0024 {
      public String internalField0248;
      public final InventoryBuilderModule.InternalType0023[] internalField0430 = new InventoryBuilderModule.InternalType0023[41];
      public String internalField0247 = "";

      public InternalType0024(String localValue1) {
         this.internalField0248 = localValue1;
      }

      public int internalMethod01393() {
         int localValue1 = 0;

         for (InventoryBuilderModule.InternalType0023 localValue5 : this.internalField0430) {
            if (localValue5 != null) {
               localValue1++;
            }
         }

         return localValue1;
      }

      public InventoryBuilderModule.InternalType0024 internalMethod07222(String localValue1) {
         InventoryBuilderModule.InternalType0024 localValue2 = new InventoryBuilderModule.InternalType0024(localValue1);

         for (int localValue3 = 0; localValue3 < 41; localValue3++) {
            if (this.internalField0430[localValue3] != null) {
               localValue2.internalField0430[localValue3] = this.internalField0430[localValue3].internalMethod06720();
            }
         }

         return localValue2;
      }

      public void internalMethod01394() {
         for (int localValue1 = 0; localValue1 < 41; localValue1++) {
            this.internalField0430[localValue1] = null;
         }
      }
   }

   static enum InternalType0065 {
      internalField0141,
      internalField0142,
      internalField1022,
      internalField1024,
      internalField1021,
      internalField1023,
      internalField1432;
   }

   public static final class InternalType0066 {
      private final String internalField0248;
      private final int internalField0227;

      public InternalType0066(String localValue1, int localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0066[name=" + this.internalField0248 + ", max=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         InventoryBuilderModule.InternalType0066 other = (InventoryBuilderModule.InternalType0066) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public String internalMethod03562() {
         return this.internalField0248;
      }

      public int internalMethod04555() {
         return this.internalField0227;
      }
   }
}
