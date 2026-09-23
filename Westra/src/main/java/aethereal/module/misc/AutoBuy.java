package aethereal.module.misc;

import aethereal.autobuy.AnLogic;
import aethereal.autobuy.AutoBuyEntry;
import aethereal.autobuy.BuyConfig;
import aethereal.autobuy.ItemAuthenticity;
import aethereal.command.AHCommand;
import aethereal.core.Category;
import aethereal.core.Client;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.ContainerEvent;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.handler.RotationProcessor;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ButtonSetting;
import aethereal.setting.Setting;
import aethereal.telegram.TelegramBot;
import aethereal.ui.screen.StationScreen;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import aethereal.util.ServerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_10185;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2649;
import net.minecraft.class_2767;
import net.minecraft.class_2813;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_3532;
import net.minecraft.class_3944;
import net.minecraft.class_408;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_7439;
import net.minecraft.class_7923;
import net.minecraft.class_9288;
import net.minecraft.class_9334;
import net.minecraft.class_1792.class_9635;
import org.lwjgl.glfw.GLFW;
import platform.inject.accessors.HandledScreenAccessor;

@ModuleRegister(
   a = "Auto Buy",
   b = "Автопокупка + парсер цен",
   c = Category.Misc
)
public class AutoBuy extends Module implements Interface {
   private final BooleanSetting reissue = new BooleanSetting("Авто-перевыставление вещей", false);
   private final BooleanSetting debug = new BooleanSetting("Дебаг", false);
   private final List<class_1799> buyHistory = new ArrayList<>();
   private static final List<Integer> swapAnarchies = new ArrayList<>();
   private static int swapIndex;
   private long nextRefreshAt;
   private final CounterUtil reissueTimer = new CounterUtil();
   private int tickCounter;
   private int containerAge;
   private class_1799 lastBoughtItem;
   private boolean buyActive;
   private boolean parserActive;
   private boolean ahReopen;
   private int lastSyncId = -1;
   private final List<AutoBuyEntry> parseQueue = new ArrayList<>();
   private AutoBuyEntry parseCurrentItem = null;
   private final JsonObject parsedPrices = new JsonObject();
   private int parseStage = 0;
   private long parseDelayTimer = 0L;
   private boolean chatPending;
   private long chatSendAt;
   private String pendingChatCommand;
   private int parseAttempts;
   private static final int MAX_PARSE_ATTEMPTS = 3;
   private int refreshCount = 0;
   private long lastMinPrice = -1L;
   private final Pattern pricePattern = Pattern.compile("(?:Цена|\\$):?\\s*([\\d .,]+)");
   private class_243 humanTarget;
   private long humanTargetTime;
   private long humanPauseUntil;
   private long nextClickAt;
   private int moveDir;
   private long moveUntil;
   private long nextJumpAt;
   private float inFwd;
   private float inStrafe;
   private boolean inJump;
   private BooleanSetting microSteps;
   private long lastAhAttempt;
   private long lastWindowUpdateAt;
   private long lastSwapAt;

   public static void addSwapAnarchy(int anarchy) {
      if (!swapAnarchies.contains(anarchy)) {
         swapAnarchies.add(anarchy);
         saveSwapAnarchies();
      }
   }

   public static void delSwapAnarchy(int anarchy) {
      if (swapAnarchies.remove(Integer.valueOf(anarchy))) {
         saveSwapAnarchies();
      }
   }

   public static void clearSwapAnarchies() {
      swapAnarchies.clear();
      saveSwapAnarchies();
   }

   public static List<Integer> getSwapAnarchies() {
      return new ArrayList<>(swapAnarchies);
   }

   private static int nextSwapAnarchy() {
      if (!swapAnarchies.isEmpty()) {
         int anarchy = swapAnarchies.get(swapIndex % swapAnarchies.size());
         swapIndex = (swapIndex + 1) % swapAnarchies.size();
         return anarchy;
      } else {
         return (int)(MathUtil.a(0.0F, 100.0F) <= 50.0F ? MathUtil.a(205.0F, 231.0F) : MathUtil.a(305.0F, 325.0F));
      }
   }

   private static void loadSwapAnarchies() {
      try {
         File file = new File("DeltaClient" + File.separator + "autobuy" + File.separator + "anarchies.json");
         if (!file.exists()) {
            return;
         }

         JsonArray arr = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonArray();
         swapAnarchies.clear();

         for (JsonElement el : arr) {
            swapAnarchies.add(el.getAsInt());
         }
      } catch (Exception var4) {
      }
   }

   private static void saveSwapAnarchies() {
      try {
         File dir = new File("DeltaClient" + File.separator + "autobuy");
         if (!dir.exists()) {
            dir.mkdirs();
         }

         JsonArray arr = new JsonArray();

         for (int anarchy : swapAnarchies) {
            arr.add(anarchy);
         }

         Files.writeString(new File(dir, "anarchies.json").toPath(), arr.toString());
      } catch (Exception var4) {
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (aM_.field_1724 != null && this.microSteps != null && this.microSteps.c()) {
         if (this.parserActive || this.buyActive) {
            event.a(this.inFwd);
            event.b(this.inStrafe);
            event.b(this.inJump);
            if (this.inFwd == 0.0F && this.inStrafe == 0.0F) {
               aM_.field_1724.field_3913.field_54155 = class_10185.field_54098;
            }
         }
      }
   }

   public AutoBuy() {
      ButtonSetting openEditor = new ButtonSetting("Открыть редактор", () -> aM_.method_1507(new StationScreen(class_2561.method_43470(""), 1)));
      this.microSteps = new BooleanSetting("Микро-шажки", true);
      this.a(new Setting[]{openEditor, this.reissue, this.debug, this.microSteps});
   }

   @Generated
   public boolean q() {
      return this.buyActive;
   }

   @Generated
   public boolean r() {
      return this.ahReopen;
   }

   @Generated
   public void d(boolean status) {
      this.buyActive = status;
   }

   @Generated
   public boolean s() {
      return this.parserActive;
   }

   @Generated
   public void f(boolean status) {
      this.parserActive = status;
   }

   @Generated
   public void e(boolean ah) {
      this.ahReopen = ah;
   }

   @Override
   public void b() {
      super.b();
      this.lastAhAttempt = System.currentTimeMillis();
      if (!AnLogic.isEnabled()) {
         AnLogic.toggle();
      }
   }

   @Override
   public void c() {
      if (AnLogic.isEnabled()) {
         AnLogic.toggle();
      }

      this.resetParseState();
      if (aM_.field_1724 != null) {
         aM_.field_1724.field_3913.field_54155 = class_10185.field_54098;
         aM_.field_1724.field_3913.field_3905 = 0.0F;
         aM_.field_1724.field_3913.field_3907 = 0.0F;
      }

      this.moveDir = 0;
      this.moveUntil = 0L;
      super.c();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null) {
         AnLogic.update();
         if (this.parserActive && this.parseStage == 0) {
            this.startParsing();
         }

         if (!this.parserActive && this.parseStage != 0) {
            this.resetParseState();
         }

         if (this.parseStage == 1 && System.currentTimeMillis() >= this.parseDelayTimer) {
            this.handleParseSearch();
         }

         if (this.parseStage == 2 && System.currentTimeMillis() >= this.parseDelayTimer) {
            this.handleParseScan();
         }

         if (this.chatPending && System.currentTimeMillis() >= this.chatSendAt) {
            if (aM_.field_1755 instanceof class_408 chat) {
               chat.method_44056(this.pendingChatCommand, true);
               aM_.method_1507(null);
            }

            this.chatPending = false;
         }

         if (this.parserActive || this.buyActive) {
            this.humanizeMovement();
         }

         if (aM_.field_1724.field_6012 >= 220
            && this.ahReopen
            && !(aM_.field_1755 instanceof class_476)
            && this.buyActive
            && aM_.field_1724.field_6012 % 20 == 0) {
            aM_.field_1724.field_3944.method_45731("ah");
            if (this.debug.c()) {
               this.debugMsg("/ah");
            }

            this.ahReopen = false;
         }

         if (this.buyActive && !(aM_.field_1755 instanceof class_476) && aM_.field_1755 == null && System.currentTimeMillis() - this.lastAhAttempt >= 2000L) {
            this.lastAhAttempt = System.currentTimeMillis();
            aM_.field_1724.field_3944.method_45731("ah");
            if (this.debug.c()) {
               this.debugMsg("Инвентарь закрыт, переоткрываю /ah");
            }
         }

         this.tickCounter++;
         this.containerAge++;
         if (aM_.field_1755 instanceof class_476 screen) {
            try {
               if (this.buyActive) {
                  class_1703 handler = screen.method_17577();
                  String title = screen.method_25440().getString().replaceAll("§.", "").toLowerCase().trim();
                  boolean buy = this.canClick();
                  boolean reissue = this.reissue.c() && this.reissueTimer.a(10000L);
                  if (!title.contains("аукцион")) {
                     if ((title.contains("подтверждение покупки") || title.contains("подозрительная цена!") || title.contains("подозрительная цена: ")) && buy) {
                        this.clickSlot(handler, 1, class_1713.field_7794);
                     }
                  } else {
                     boolean found = false;
                     int bestSlot = -1;
                     long bestPrice = Long.MAX_VALUE;
                     class_1799 bestStack = null;

                     for (class_1735 slot : handler.field_7761.subList(0, Math.min(45, handler.field_7761.size()))) {
                        class_1799 stack = slot.method_7677();
                        if (stack != null && !stack.method_7960()) {
                           class_9288 shulker = (class_9288)stack.method_57824(class_9334.field_49622);

                           for (AutoBuyEntry item : Westra.h().d().q().e()) {
                              if (item.l()) {
                                 boolean match = false;
                                 if (item.a(stack)) {
                                    match = true;
                                 } else if (shulker != null && shulker.method_57489().anyMatch(item::a)) {
                                    match = true;
                                 }

                                 if (match) {
                                    long lotPrice = ServerUtil.a.a(stack);
                                    if (lotPrice > 0L) {
                                       double priceLimit = item.k();
                                       if ((!(priceLimit > 1.0) || !(lotPrice > priceLimit))
                                          && lotPrice * Math.max(stack.method_7947(), 1) <= ServerUtil.a.e()
                                          && lotPrice < bestPrice) {
                                          bestPrice = lotPrice;
                                          bestSlot = slot.field_7874;
                                          bestStack = stack;
                                       }
                                    }
                                 }
                                 break;
                              }
                           }
                        }
                     }

                     if (bestSlot != -1 && buy) {
                        found = true;
                        this.lastBoughtItem = bestStack.method_7972();
                        this.clickSlot(handler, bestSlot, class_1713.field_7794);
                        if (this.debug.c()) {
                           this.debugMsg("Покупаю самый дешёвый лот (" + bestPrice + "$, слот " + bestSlot + ")");
                        }
                     }

                     long nowMs = System.currentTimeMillis();
                     if (this.lastWindowUpdateAt != 0L && nowMs - this.lastSwapAt >= 20000L && nowMs - this.lastWindowUpdateAt >= 5000L) {
                        int anarchy = nextSwapAnarchy();
                        aM_.field_1724.field_3944.method_45730("an" + anarchy);
                        if (this.debug.c()) {
                           this.debugMsg("Аукцион не отвечает " + (nowMs - this.lastWindowUpdateAt) / 1000L + " сек -> /an" + anarchy);
                        }

                        ChatUtil.sendMessage("Обнаружили замедление аукциона, переходим на " + anarchy + " анархию");
                        TelegramBot.notifyAutoBuy(
                           "Замедление аукциона, свап на "
                              + anarchy
                              + " анархию | ник: "
                              + aM_.method_1548().method_1676()
                              + " | была анархия: "
                              + ServerUtil.a.d()
                        );
                        this.lastSwapAt = nowMs;
                        this.lastWindowUpdateAt = nowMs;
                        this.ahReopen = true;
                     }

                     if (!found && !reissue && System.currentTimeMillis() >= this.nextRefreshAt) {
                        int refreshSlot = this.findRefreshSlot((class_1707)handler);
                        if (refreshSlot == -1) {
                           refreshSlot = 49;
                        }

                        this.clickRefresh((class_1707)handler, refreshSlot);
                        this.nextRefreshAt = System.currentTimeMillis() + 100L + (long)(Math.random() * 100.0);
                        if (this.debug.c()) {
                           this.debugMsg("Обновляю аукцион (слот " + refreshSlot + ")");
                        }
                     }
                  }

                  if (this.reissue.c() && reissue) {
                     class_465<?> handledScreen = (class_465<?>)aM_.field_1755;
                     if (handledScreen instanceof class_465 && handledScreen instanceof class_476 && !MoveUtil.a()) {
                        if (title.matches(".*а.*у.*к.*ц.*и.*о.*н.*")) {
                           if (aM_.field_1724.field_6012 % 10 == 0) {
                              this.clickSlot(handledScreen.method_17577(), 46, class_1713.field_7790);
                              this.containerAge = 0;
                           }
                        } else if (title.matches(".*х.*р.*а.*н.*и.*л.*и.*щ.*е.*")) {
                           if (this.containerAge % 20 == 10) {
                              this.clickSlot(handledScreen.method_17577(), 52, class_1713.field_7790);
                           } else if (this.containerAge % 20 == 0 && this.containerAge > 0) {
                              this.clickSlot(handledScreen.method_17577(), 46, class_1713.field_7790);
                              this.reissueTimer.b();
                           }
                        }

                        this.tickCounter = 0;
                     }
                  }
               }
            } catch (Exception var23) {
               if (this.debug.c()) {
                  StackTraceElement[] st = var23.getStackTrace();
                  String at = st.length > 0
                     ? " @ " + st[0].getClassName().substring(st[0].getClassName().lastIndexOf(46) + 1) + ":" + st[0].getLineNumber()
                     : "";
                  this.debugMsg("&cОшибка тика автобая: " + var23 + at);
               }
            }
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.debug.c() && !event.b()) {
         String pkt = event.d().getClass().getSimpleName();
         if (pkt.contains("GameMessage") || pkt.contains("Inventory") || pkt.contains("OpenScreen")) {
            this.debugMsg("&8<- " + pkt);
         }
      }

      if (event.c() && this.buyActive) {
         if (event.d() instanceof class_2649 inventoryPacket && inventoryPacket.method_11441().size() == 90) {
            this.lastWindowUpdateAt = System.currentTimeMillis();
         }

         if (event.d() instanceof class_7439 messagePacket) {
            if (this.lastBoughtItem != null && messagePacket.comp_763().getString().contains("Вы успешно купили")) {
               if (this.buyHistory.isEmpty() || !class_1799.method_7973(this.buyHistory.getFirst(), this.lastBoughtItem)) {
                  Client clientF = Westra.h().f();
                  Object[] objArr = new Object[]{"message", null};
                  Object[] objArr2 = new Object[]{
                     this.lastBoughtItem.method_7964().getString() + (this.lastBoughtItem.method_7947() > 1 ? " ×" + this.lastBoughtItem.method_7947() : ""),
                     String.format(Locale.US, "%,d", ServerUtil.a.a(this.lastBoughtItem)),
                     String.format(Locale.US, "%,d", ServerUtil.a.e())
                  };
                  objArr[1] = "\ud83d\uded2 AutoBuy — Успешная покупкк!\n\n\ud83d\udce6 Предмет: %s\n\ud83d\udcb0 Цена: %s $\n\ud83d\udcb3 Баланс: %s $\n"
                     .formatted(objArr2);
                  clientF.a(false, "telegram", objArr);
                  ChatUtil.sendMessage(
                     "Успешно куплен предмет &c" + this.lastBoughtItem.method_7964().getString() + " &7за &c" + ServerUtil.a.a(this.lastBoughtItem)
                  );
                  TelegramBot.notifyAutoBuy(
                     "Купил "
                        + this.lastBoughtItem.method_7964().getString()
                        + " за "
                        + ServerUtil.a.a(this.lastBoughtItem)
                        + "$ | анархия: "
                        + ServerUtil.a.d()
                        + " | ник: "
                        + aM_.method_1548().method_1676()
                        + " | баланс: "
                        + ServerUtil.a.e()
                        + "$"
                  );
                  this.buyHistory.addFirst(this.lastBoughtItem);
               }

               this.lastBoughtItem = null;
            }

            String banText = messagePacket.comp_763().getString();
            if (banText.contains("[ВАС ЗАБАНИЛИ]")) {
               TelegramBot.notifyAutoBuy("ВАС ЗАБАНИЛИ! ник: " + aM_.method_1548().method_1676() + " | анархия: " + ServerUtil.a.d());
               ChatUtil.sendMessage("&c[AutoBuy] Забанены, уведомление отправлено в ТГ");
            }
         }

         if (event.d() instanceof class_3944 openScreenPacket) {
            if (!(aM_.field_1755 instanceof class_476)) {
               this.tickCounter = 0;
            }

            this.lastSyncId = openScreenPacket.method_17592();
            this.lastWindowUpdateAt = System.currentTimeMillis();
         }

         if (event.d() instanceof class_2767 soundPacket
            && ((class_3414)soundPacket.method_11894().comp_349()).comp_3319().method_12832().equals("block.note_block.basedrum")) {
            this.lastSyncId = aM_.field_1724.field_7512.field_7763;
            event.a(true);
         }
      }
   }

   @EventTarget
   public void a(ContainerEvent event) {
      if (event.h() == ContainerEvent.Phase.POST) {
         String title = event.b().method_25440().getString().replaceAll("§.", "").toLowerCase().trim();
         if (title.contains("аукцион")) {
            HandledScreenAccessor accessor = (HandledScreenAccessor)event.b();
            class_332 context = event.d();
            int count = accessor.getBackgroundHeight() / 18;
            int x = accessor.getX() - 22;
            int y = accessor.getY() + 3;
            int bottom = y + count * 18;
            int[][] edges = new int[][]{
               {x - 2, y, x + 20, bottom, -3750202},
               {x, y - 2, x + 18, bottom + 2, -3750202},
               {x - 1, y - 1, x + 19, y, -3750202},
               {x - 1, bottom, x + 19, bottom + 1, -3750202},
               {x, y - 2, x + 18, y - 1, -1},
               {x - 1, y - 1, x, y, -1},
               {x - 2, y, x - 1, bottom, -1},
               {x, bottom + 1, x + 18, bottom + 2, -11184811},
               {x + 18, bottom, x + 19, bottom + 1, -11184811},
               {x + 19, y, x + 20, bottom, -11184811}
            };

            for (int[] edge : edges) {
               context.method_25294(edge[0], edge[1], edge[2], edge[3], edge[4]);
            }

            for (int i = 0; i < count; i++) {
               int slotY = y + i * 18;
               context.method_52706(class_1921::method_62277, class_2960.method_60656("container/slot"), x, slotY, 18, 18);
               if (i < this.buyHistory.size()) {
                  class_1799 stack = this.buyHistory.get(i);
                  Westra.h().d().j().a(context, stack, x + 1, slotY + 1, 0, 1.0F, 1.0F, true);
                  if (MathUtil.a(event.f(), event.g(), x + 1, slotY + 1, 16.0F, 16.0F)) {
                     context.method_51740(class_1921.method_51785(), x + 1, slotY + 1, x + 17, slotY + 17, -2130706433, -2130706433, 0);
                     context.method_51446(aM_.field_1772, stack, event.f(), event.g());
                  }
               }
            }
         }
      }
   }

   private void clickSlot(class_1703 handler, int slot, class_1713 action) {
      aM_.field_1724
         .field_3944
         .method_52787(
            new class_2813(handler.field_7763, handler.method_37421(), slot, 0, action, handler.method_34255().method_7972(), Int2ObjectMaps.emptyMap())
         );
      this.tickCounter = 0;
      this.nextClickAt = System.currentTimeMillis() + this.nextClickDelay();
   }

   private void clickRefresh(class_1707 handler, int slot) {
      aM_.field_1724
         .field_3944
         .method_52787(
            new class_2813(
               handler.field_7763, handler.method_37421(), slot, 0, class_1713.field_7794, handler.method_34255().method_7972(), Int2ObjectMaps.emptyMap()
            )
         );
      this.tickCounter = 0;
   }

   private boolean canClick() {
      return System.currentTimeMillis() >= this.nextClickAt;
   }

   private long nextClickDelay() {
      long base = 160L + (long)(Math.random() * 260.0);
      if (Math.random() < 0.15) {
         base += 350L + (long)(Math.random() * 600.0);
      }

      return base;
   }

   private void handleParseSearch() {
      if (this.parseQueue.isEmpty()) {
         this.saveParsedJson();
         if (this.debug.c()) {
            this.debugMsg("Parser finished: " + this.parsedPrices.size() + " items saved to parse.json");
         }

         this.parserActive = false;
         this.resetParseState();
      } else if (aM_.field_1755 != null) {
         aM_.method_1507(null);
      } else {
         this.parseCurrentItem = this.parseQueue.remove(0);
         this.parseAttempts = 1;
         String searchQuery = AHCommand.cleanSearchQuery(this.parseCurrentItem.b());
         this.openChatWithCommand("ah search " + searchQuery);
         if (this.debug.c()) {
            this.debugMsg("/ah search " + searchQuery + " (" + this.parseQueue.size() + " remaining)");
         }

         this.parseDelayTimer = System.currentTimeMillis() + 1200L;
         this.parseStage = 2;
      }
   }

   private void openChatWithCommand(String command) {
      this.pendingChatCommand = "/" + command;
      this.chatPending = true;
      this.chatSendAt = System.currentTimeMillis() + 450L + (long)(Math.random() * 400.0);
      aM_.method_1507(new class_408(this.pendingChatCommand));
   }

   private void handleParseScan() {
      if (aM_.field_1755 instanceof class_465<?> screen && screen.method_17577() instanceof class_1707 container) {
         long currentInvMin = this.scanForMinPrice(container);
         if (currentInvMin != -1L && (this.lastMinPrice == -1L || currentInvMin < this.lastMinPrice)) {
            this.lastMinPrice = currentInvMin;
         }

         if (this.refreshCount < 2) {
            int refreshSlotId = this.findRefreshSlot(container);
            if (refreshSlotId != -1) {
               this.clickParsedSlot(screen, refreshSlotId);
               this.refreshCount++;
               if (this.debug.c()) {
                  this.debugMsg("Refresh #" + this.refreshCount + " clicked (slot " + refreshSlotId + ")");
               }

               this.parseDelayTimer = System.currentTimeMillis() + 350L;
            } else {
               this.proceedToNextParseProduct();
            }
         } else {
            this.proceedToNextParseProduct();
         }
      } else if (aM_.field_1755 == null) {
         if (this.parseCurrentItem != null && this.parseAttempts < 3) {
            this.parseAttempts++;
            if (this.debug.c()) {
               this.debugMsg("Аукцион не открылся, попытка #" + this.parseAttempts + " из 3");
            }

            String searchQuery = AHCommand.cleanSearchQuery(this.parseCurrentItem.b());
            this.openChatWithCommand("ah search " + searchQuery);
            this.parseDelayTimer = System.currentTimeMillis() + 1500L;
         } else {
            if (this.debug.c() && this.parseCurrentItem != null) {
               this.debugMsg(this.parseCurrentItem.name() + " -> поиск не открылся за 3 попытки, пропускаю");
            }

            this.proceedToNextParseProduct();
         }
      } else {
         this.proceedToNextParseProduct();
      }
   }

   private void proceedToNextParseProduct() {
      if (this.lastMinPrice != -1L) {
         double multiplier = (100.0 - BuyConfig.getDiscountPercentage()) / 100.0;
         long discountedPrice = Math.round(this.lastMinPrice * multiplier);
         this.parsedPrices.addProperty(this.parseCurrentItem.name(), discountedPrice);
         this.parseCurrentItem.a(discountedPrice);
         BuyConfig.setPrice(this.parseCurrentItem.name(), discountedPrice);
         if (this.debug.c()) {
            this.debugMsg(this.parseCurrentItem.name() + " -> min: " + this.lastMinPrice + ", порог покупки: " + discountedPrice);
         }
      } else {
         this.parsedPrices.addProperty(this.parseCurrentItem.name(), -1);
         if (this.debug.c()) {
            this.debugMsg(this.parseCurrentItem.name() + " -> not found");
         }
      }

      if (aM_.field_1724 != null && aM_.field_1755 != null) {
         aM_.method_1507(null);
      }

      this.resetParseTracking();
      this.parseStage = 1;
      this.parseDelayTimer = System.currentTimeMillis() + 150L;
   }

   private long scanForMinPrice(class_1707 container) {
      if (this.parseCurrentItem == null) {
         return -1L;
      } else {
         long min = -1L;
         String normalizedTarget = ItemAuthenticity.normalizeName(this.parseCurrentItem.b());
         class_9635 context = aM_.field_1687 != null ? class_9635.method_59530(aM_.field_1687.method_30349()) : class_9635.field_51353;

         for (int i = 0; i < 54 && i < container.field_7761.size(); i++) {
            class_1735 slot = container.method_7611(i);
            if (slot != null && slot.method_7681()) {
               class_1799 stack = slot.method_7677();
               if (this.parseCurrentItem.d() == stack.method_7909()) {
                  String registryPath = class_7923.field_41178.method_10221(stack.method_7909()).method_12832();
                  if (!registryPath.contains("shulker_box")) {
                     String name = stack.method_7964().getString();
                     if (!name.contains("Обновить")
                        && !name.contains("Сложить")
                        && !name.contains("Взять")
                        && !name.contains("Выбросить")
                        && !name.contains("Назад")
                        && !name.contains("Вперед")) {
                        String normalizedName = ItemAuthenticity.normalizeName(name);
                        if (normalizedName.contains(normalizedTarget) || normalizedTarget.contains(normalizedName)) {
                           int serverPrice = ServerUtil.a.a(stack);
                           if (serverPrice > 0) {
                              long perUnitPrice = Math.round((double)serverPrice);
                              if (min == -1L || perUnitPrice < min) {
                                 min = perUnitPrice;
                              }

                              if (this.debug.c()) {
                                 this.debugMsg("Found price: " + stack.method_7964().getString() + " -> " + perUnitPrice);
                              }
                           } else {
                              for (class_2561 text : stack.method_7950(context, aM_.field_1724, class_1836.field_41070)) {
                                 String line = text.getString();
                                 long totalPrice = this.parsePriceFromString(line);
                                 if (totalPrice != -1L) {
                                    int stackCount = Math.max(1, stack.method_7947());
                                    long perUnit = Math.round((double)totalPrice / stackCount);
                                    if (min == -1L || perUnit < min) {
                                       min = perUnit;
                                    }

                                    if (this.debug.c()) {
                                       this.debugMsg("Fallback price: " + line + " -> " + perUnit);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return min;
      }
   }

   private int findRefreshSlot(class_1707 container) {
      for (int i = Math.max(0, 45); i < Math.min(54, container.field_7761.size()); i++) {
         class_1735 slot = container.method_7611(i);
         if (slot != null && slot.method_7681()) {
            String normalized = ItemAuthenticity.normalizeName(slot.method_7677().method_7964().getString());
            if (normalized.contains("обнов") || normalized.contains("refresh") || normalized.contains("рефреш")) {
               return i;
            }
         }
      }

      for (int ix = 0; ix < Math.min(54, container.field_7761.size()); ix++) {
         class_1735 slot = container.method_7611(ix);
         if (slot != null && slot.method_7681() && slot.method_7677().method_7909() == class_1802.field_8137) {
            return ix;
         }
      }

      return -1;
   }

   private long parsePriceFromString(String text) {
      if (text.contains("$") || text.toLowerCase(Locale.ROOT).contains("цен")) {
         StringBuilder digits = new StringBuilder();

         for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
               digits.append(c);
            }
         }

         if (digits.length() > 0) {
            try {
               return Long.parseLong(digits.toString());
            } catch (NumberFormatException var7) {
            }
         }
      }

      return -1L;
   }

   private void debugMsg(String msg) {
      if (aM_.field_1724 != null) {
         class_2561 text = class_2561.method_43470("§7[§9AutoBuy§7] §f" + msg);
         aM_.field_1724.method_7353(text, false);
         aM_.field_1724.method_7353(text, true);
      }
   }

   private void clickParsedSlot(class_465<?> screen, int slotId) {
      if (aM_.field_1761 != null && aM_.field_1724 != null) {
         aM_.field_1761.method_2906(screen.method_17577().field_7763, slotId, 0, class_1713.field_7790, aM_.field_1724);
      }
   }

   private void resetParseTracking() {
      this.parseCurrentItem = null;
      this.refreshCount = 0;
      this.lastMinPrice = -1L;
      this.parseAttempts = 0;
   }

   private void resetParseState() {
      this.resetParseTracking();
      this.parseQueue.clear();
      this.parsedPrices.entrySet().clear();
      this.parseStage = 0;
      this.chatPending = false;
   }

   public void startParsing() {
      this.resetParseState();

      for (AutoBuyEntry entry : AutoBuyEntry.values()) {
         if (entry.l()) {
            this.parseQueue.add(entry);
         }
      }

      if (this.parseQueue.isEmpty()) {
         if (this.debug.c()) {
            this.debugMsg("Parser: нет включённых предметов в AutoBuy — парсить нечего");
         }
      } else {
         if (this.debug.c()) {
            this.debugMsg("Parser started: " + this.parseQueue.size() + " items queued");
         }

         this.parseStage = 1;
      }
   }

   private void saveParsedJson() {
      File dir = new File("DeltaClient" + File.separator + "autobuy");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      File file = new File(dir, "parse.json");
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      try (FileWriter writer = new FileWriter(file)) {
         gson.toJson(this.parsedPrices, writer);
      } catch (IOException var9) {
      }
   }

   private boolean isBindPressed(BindSetting setting) {
      int key = setting.c();
      if (key == -1) {
         return false;
      } else if (aM_.method_22683() == null) {
         return false;
      } else {
         long handle = aM_.method_22683().method_4490();
         return GLFW.glfwGetKey(handle, key) == 1;
      }
   }

   private void humanizeMovement() {
      if (aM_.field_1724 != null) {
         long now = System.currentTimeMillis();
         if (now >= this.moveUntil) {
            int roll = (int)(Math.random() * 100.0);
            if (roll < 65) {
               this.moveDir = 0;
               this.moveUntil = now + 700L + (long)(Math.random() * 2000.0);
            } else {
               this.moveDir = 1 + (int)(Math.random() * 4.0);
               this.moveUntil = now + 200L + (long)(Math.random() * 300.0);
            }
         }

         boolean moving = this.moveDir != 0 && this.microSteps != null && this.microSteps.c();
         float speed = 0.07F + (float)Math.random() * 0.08F;
         boolean fwd = moving && this.moveDir == 1;
         boolean back = moving && this.moveDir == 2;
         boolean left = moving && this.moveDir == 3;
         boolean right = moving && this.moveDir == 4;
         this.inFwd = fwd ? speed : (back ? -speed : 0.0F);
         this.inStrafe = right ? speed : (left ? -speed : 0.0F);
         this.inJump = false;
         if (now >= this.humanPauseUntil) {
            float currentYaw = aM_.field_1724.method_36454();
            float currentPitch = aM_.field_1724.method_36455();
            if (this.humanTarget == null || now > this.humanTargetTime) {
               boolean bigLook = MathUtil.a(0.0F, 100.0F) < 15.0F;
               float yawOffset = bigLook ? (float)(Math.random() * 120.0 - 60.0) : (float)(Math.random() * 24.0 - 12.0);
               float pitchOffset = bigLook ? (float)(Math.random() * 20.0 - 12.0) : (float)(Math.random() * 8.0 - 4.0);
               this.humanTarget = new class_243(
                  class_3532.method_15393(currentYaw + yawOffset), class_3532.method_15363(currentPitch + pitchOffset, -60.0F, 60.0F), 0.0
               );
               this.humanTargetTime = now + (long)(1800.0 + Math.random() * 3200.0);
               if (MathUtil.a(0.0F, 100.0F) < 20.0F) {
                  this.humanPauseUntil = now + (long)(600.0 + Math.random() * 1800.0);
               }
            }

            float targetYaw = (float)this.humanTarget.field_1352;
            float targetPitch = (float)this.humanTarget.field_1351;
            float yawDiff = class_3532.method_15393(targetYaw - currentYaw);
            float pitchDiff = targetPitch - currentPitch;
            if (!(Math.abs(yawDiff) < 0.4F) || !(Math.abs(pitchDiff) < 0.4F)) {
               float step = 0.1F + (float)Math.random() * 0.06F;
               float newYaw = currentYaw + yawDiff * step;
               float newPitch = currentPitch + pitchDiff * step;
               float t = aM_.field_1724.field_6012 * 0.11F;
               newYaw += (float)(Math.sin(t) * 0.12);
               newPitch += (float)(Math.sin(t * 1.7F + 1.3F) * 0.07);
               newYaw = RotationProcessor.a(currentYaw, newYaw);
               newPitch = RotationProcessor.a(currentPitch, class_3532.method_15363(newPitch, -89.9F, 89.9F));
               aM_.field_1724.method_36456(newYaw);
               aM_.field_1724.method_36457(newPitch);
            }
         }
      }
   }

   static {
      loadSwapAnarchies();
   }
}
