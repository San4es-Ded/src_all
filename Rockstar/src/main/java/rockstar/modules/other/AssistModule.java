package rockstar.modules.other;

















import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import rockstar.modules.player.FreeCameraModule;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import lombok.Generated;
import moscow.rockstar.mixin.accessors.EntityS2CPacketAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler.ConfirmServerResourcePackScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import pyrock.events.game.AttackEvent;
import pyrock.events.game.EventSetCooldown;
import pyrock.events.game.FinishEatEvent;
import pyrock.events.game.SendMessageEvent;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.InputEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Assist",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.assist"
)
public class AssistModule extends Module {
   private final BooleanSupplier internalField0424 = () -> this.internalMethod09942()
      && !ServerUtils.internalMethod01786(KnownServer.internalField1571);
   private final BooleanSupplier internalField0425 = () -> this.internalMethod09942()
      && !ServerUtils.internalMethod01786(KnownServer.internalField1566);
   private final BooleanSupplier internalField1150 = () -> this.internalMethod09942()
      && !ServerUtils.internalMethod01786(KnownServer.internalField1569);
   private ButtonSetting internalField0663;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private BooleanSetting internalField1262;
   private BooleanSetting internalField1264;
   private BooleanSetting internalField1587;
   private BooleanSetting internalField1590;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private BooleanSetting internalField1594;
   private BooleanSetting internalField1588;
   private BooleanSetting internalField1589;
   private BooleanSetting internalField1591;
   private BooleanSetting internalField1592;
   private BooleanSetting internalField1593;
   private BooleanSetting internalField1811;
   private BooleanSetting internalField1809;
   private BooleanSetting internalField1810;
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private final List<AssistSwapItem> internalField0416 = AssistItemCatalog.internalMethod02335();
   private final List<AssistSwapItem> internalField0417 = new ArrayList<>();
   private final Set<String> internalField0546 = new HashSet<>(
      Arrays.asList(
         "\u0430\u043a\u0440\u0438\u0435\u043d(\u0430|\u0443|\u043e\u043c|\u0435|\u0447\u0438\u043a)?",
         "\u0440\u0438\u0447(\u0430|\u0443|\u043e\u043c|\u0435\u0439|\u0435)?",
         "\u043d\u044c\u044e\u043a\u043e\u0434(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u044d\u043a\u0441\u043f\u0435\u043d\u0441\u0438\u0432(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0435)?",
         "\u0438\u043c\u043f\u0430\u043a\u0442(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u044d\u043a\u0441\u0435\u043b\u043b\u0435\u043d\u0442(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u044d\u043a\u0441\u0435\u043b\u0435\u043d\u0442(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a)?",
         "\u043a\u0430\u0442\u043b\u0430\u0432\u0430\u043d(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0447\u0438\u043a)?",
         "\u043a\u0430\u0442\u043b\u043e\u0432\u0430\u043d(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0447\u0438\u043a)?",
         "\u0446\u0435\u043b\u0435\u0441\u0442\u0438\u0430\u043b(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0435)?",
         "\u0446\u0435\u043b\u043a(\u043e\u0439|\u0430|\u0443|\u0430\u043c\u0438|\u043e\u0447\u043a\u0430|\u0435)?",
         "\u043c\u0430\u0442\u0438\u043a\u0441(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0435)?",
         "\u0438\u043d\u0435\u0440\u0442\u0438(\u044f|\u0435\u0439|\u044e|\u044f\u043c\u0438|\u0435)?",
         "\u044d\u043a\u0441\u043f(\u0430|\u043e\u0439|\u043e\u044e|\u0443|\u0443\u043b\u0438\u0447\u043a\u0430|\u0435)?",
         "\u0444\u043b\u044e\u0433\u0435\u0440(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438)?",
         "\u0440\u0438\u043a\u0435\u0440(\u0430|\u0443|\u043e\u043c|\u043e\u0447\u0435\u043a)?",
         "\u0444\u0430\u043d\u043f\u0435(\u0439|\u044e|\u044f|\u0435\u043c|\u0435|\u0439\u0447\u0438\u043a)?",
         "\u0432\u0435\u043a\u0441\u0430\u0439\u0434(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u043d\u0443\u0440\u0441\u0443\u043b\u0442\u0430\u043d(\u0430|\u0443|\u0435|\u043e\u043c|\u0447\u0438\u043a)?",
         "\u043d\u0443\u0440\u0438\u043a(\u0430|\u0443|\u043e\u043c|\u0435)?",
         "\u043d\u0443\u0440\u043b\u0430\u043d(\u0430|\u0443|\u043e\u043c|\u0447\u0438\u043a|\u0435)?",
         "\u0432\u0435\u043a\u0441(\u043e\u043c|\u0443|\u0430|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u0440\u0435\u043b\u0435\u0439\u043a(\u043e\u043c|\u0443|\u0430|\u0430\u043c\u0438|\u0435)?",
         "\u0430\u0440\u0431\u0443\u0437(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u0432\u0438\u043b\u0434(\u043e\u043c|\u0443|\u0430|\u0430\u043c\u0438|\u0438\u043a|\u0435)?",
         "\u0444\u0430\u043d\u0442\u0430\u0439\u043c(\u0435|\u0430|\u0443)?",
         "\u0445\u043e\u043b\u0438\u043a(\u0435|\u0430|\u0443)?",
         "\u0445\u043e\u043b\u0438\u0432\u043e\u0440\u043b\u0434(\u0430|\u0443|\u0435)?",
         "\u0440\u043e\u043a\u0441\u0442\u0430\u0440(\u043e\u043c|\u0430|\u0443|\u0430\u043c\u0438|\u0447\u0438\u043a|\u0435)?",
         "\u0440\u043e\u0433\u0430\u043b\u0438\u043a(\u0430|\u0443|\u043e\u043c|\u0435)?",
         "\u0442\u0430\u043d\u0434\u0435\u0440\u0445\u0430\u043a(\u043e\u043c|\u0443|\u0438|\u0430\u043c\u0438|\u0430|\u0435)?",
         "\u043b\u0438\u043a\u0432\u0438\u0434\u0431\u0430\u0443\u043d\u0441(\u0430|\u0443|\u0430\u043c\u0438|\u0435)?",
         "expensive",
         "celestial",
         "newcode",
         "arbuz",
         "akrien",
         "nursultan",
         "relake",
         "wild",
         "wurst",
         "catlovan",
         "excellent",
         "rockstar",
         "catlavan",
         "impact",
         "matix",
         "inertia",
         "wex",
         "wexside",
         "nurik",
         "nurlan",
         "rich",
         "funpay",
         "fluger",
         "riker",
         "funtime",
         "holyworld",
         "wwe",
         "hvh",
         "rogalik",
         "thunderhack",
         "liquidbounce"
      )
   );
   private final List<Pattern> internalField1145 = new ArrayList<>();
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private final Stopwatch internalField1189;
   private final Stopwatch internalField1186;
   private final Stopwatch internalField1188;
   private final Stopwatch internalField1187;
   private final AutoCaptchaSolver internalField0011;
   private boolean internalField0277;
   private boolean internalField0276;
   private boolean internalField1099;
   private boolean internalField1100;
   private int internalField0227;
   private boolean internalField1102;
   private boolean internalField1101;
   private int internalField0228;
   private HotbarSlot internalField0226;
   private boolean internalField1516;
   private boolean internalField1517;
   private final Set<Integer> internalField0545;
   private final EventListener<MouseEvent> internalField0157;
   private final EventListener<KeyPressEvent> internalField0158;
   private final EventListener<EventSetCooldown> internalField1028;
   private final EventListener<ReceivePacketEvent> internalField1029;
   private final EventListener<SendMessageEvent> internalField1030;
   private final EventListener<AttackEvent> internalField1027;
   private final EventListener<FinishEatEvent> internalField1436;
   private final EventListener<FinishEatEvent> internalField1435;
   private final EventListener<InputEvent> internalField1434;
   private InventorySlot internalField0022;
   private boolean internalField1512;
   private boolean internalField1515;
   private final Stopwatch internalField1550;
   private final EventListener<HudRenderEvent> internalField1433;
   private final EventListener<FinishEatEvent> internalField1437;

   public AssistModule() {
      for (String localValue2 : this.internalField0546) {
         try {
            this.internalField1145.add(Pattern.compile("\\b" + localValue2 + "\\b", 322));
         } catch (Exception localValue4) {
            this.internalField1145.add(Pattern.compile(Pattern.quote(localValue2), 258));
         }
      }

      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField1189 = new Stopwatch();
      this.internalField1186 = new Stopwatch();
      this.internalField1188 = new Stopwatch();
      this.internalField1187 = new Stopwatch();
      this.internalField0011 = new AutoCaptchaSolver();
      this.internalField0276 = true;
      this.internalField1099 = true;
      this.internalField0228 = -1;
      this.internalField0545 = new HashSet<>();
      this.internalField0157 = localValue1 -> {
         if (localValue1.getAction() == 1) {
            if (internalField0149.currentScreen == null) {
               this.internalMethod08202(localValue1.getButton());
            }
         }
      };
      this.internalField0158 = localValue1 -> {
         if (localValue1.getAction() == 1) {
            if (internalField0149.currentScreen == null) {
               this.internalMethod08202(localValue1.getKey());
            }
         }
      };
      this.internalField1028 = localValue1 -> {
         if (this.internalField1264.internalMethod04496()) {
            Item localValue2x = (Item)Registries.ITEM.get(localValue1.getCooldownGroup());
            if (localValue2x == Items.ENCHANTED_GOLDEN_APPLE || localValue2x == Items.GOLDEN_APPLE || localValue2x == Items.POTION || localValue2x == Items.CHORUS_FRUIT) {
               localValue1.setCooldown(localValue1.getCooldown() - 32);
            }
         }
      };
      this.internalField1029 = localValue1 -> {
         if (this.internalField1594.internalMethod04496() && localValue1.getPacket() instanceof GameMessageS2CPacket localValue2x) {
            String localValue9 = localValue2x.content().getString().toLowerCase();
            if (localValue9.contains("10,000 \u0431\u044b\u043b\u043e \u043d\u0430\u0447\u0438\u0441\u043b\u0435\u043d\u043e \u0432\u0430\u043c")
               || localValue9.contains("\u043f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442 \u0435\u0449\u0435 \u0440\u0430\u0437")) {
               this.internalField1099 = false;
               this.internalField0519.internalMethod00701();
            }
         }

         if (this.internalField1588.internalMethod04496() && localValue1.getPacket() instanceof GameMessageS2CPacket localValue4x) {
            this.internalField0011.internalMethod05324(localValue4x.content().getString());
         }

         if (this.internalField1591.internalMethod04496() && localValue1.getPacket() instanceof GameMessageS2CPacket localValue5) {
            String localValue12 = localValue5.content().getString();
            if (localValue12.contains(
               "\u0412\u044b \u0443\u0436\u0435 \u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043b\u0438 \u044d\u0442\u043e\u0442 \u043f\u0440\u043e\u043c\u043e\u043a\u043e\u0434"
            )) {
               this.internalField0276 = false;
               this.internalField0519.internalMethod00701();
            } else if (localValue12.contains(
               "\u041f\u0440\u044f\u043c\u043e \u0441\u0435\u0439\u0447\u0430\u0441 \u0438\u0434\u0435\u0442 \u043d\u0430\u0431\u043e\u0440"
            )) {
               this.internalField0276 = true;
            }
         }

         if (this.internalField1592.internalMethod04496() && localValue1.getPacket() instanceof GameMessageS2CPacket localValue6) {
            String localValue14 = localValue6.content().getString();
            if (localValue14.contains("\u041d\u0435 \u0434\u0432\u0438\u0433\u0430\u0439\u0442\u0435\u0441\u044c")) {
               this.internalField0277 = true;
               this.internalField0518.internalMethod00701();
               System.out.println("stop");
            }
         }

         if (this.internalField1261.internalMethod04496() && localValue1.getPacket() instanceof OpenScreenS2CPacket localValue7) {
            String localValue16 = localValue7.getName().getString();
            if (localValue16.contains("\u041c\u0435\u043d\u044e") || localValue16.contains("\ua201\ua000\ua202\ua301\ua202\ua001")) {
               internalField0149.player.closeScreen();
               localValue1.cancel();
            }
         }

         if (this.internalField1589.internalMethod04496() && this.internalField1102 && localValue1.getPacket() instanceof EntityS2CPacket localValue8) {
            LivingEntity localValue18 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
            if (localValue18 != null && ((EntityS2CPacketAccessor)(Object)localValue8).getId() == localValue18.getId()) {
               this.internalField1188.internalMethod00701();
            }
         }
      };
      this.internalField1030 = localValue1 -> {
         if (this.internalMethod06330(localValue1.getMessage())) {
            localValue1.cancel();
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("assist.ct_command_blocked")));
         } else if (this.internalField1262.internalMethod04496()) {
            for (Pattern localValue3 : this.internalField1145) {
               if (localValue3.matcher(localValue1.getMessage()).find()) {
                  localValue1.cancel();
                  ClientMessages.internalMethod09025(
                     Text.of(
                        "\u0421\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043d\u0435 \u0431\u044b\u043b\u043e \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e \u0442.\u043a. \u0432 \u043d\u0435\u043c \u043f\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u0443\u0435\u0442 \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043d\u043e\u0435 \u0441\u043b\u043e\u0432\u043e"
                     )
                  );
                  return;
               }
            }
         }
      };
      this.internalField1027 = localValue1 -> {
         if (this.internalField1589.internalMethod04496() && this.internalField1102) {
            localValue1.cancel();
         }
      };
      this.internalField1436 = localValue1 -> {
         if (this.internalField1589.internalMethod04496()) {
            if (this.internalField1102) {
               if (localValue1.getUser() == internalField0149.player) {
                  if (localValue1.getStack().getItem() == Items.CHORUS_FRUIT) {
                     this.internalMethod10106();
                  }
               }
            }
         }
      };
      this.internalField1435 = localValue1 -> {
         if (this.internalField1809.internalMethod04496()
            && ServerUtils.internalMethod01786(KnownServer.internalField1218)
            && !ServerUtils.internalField0277) {
            if (localValue1.getUser() == internalField0149.player) {
               if (localValue1.getStack().isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
                  CommandRegistry localValue2x = RockstarClient.getInstance().internalMethod05348();
                  if (localValue2x != null) {
                     localValue2x.internalMethod04610(localValue2x.internalMethod03606() + "rct");
                  }
               }
            }
         }
      };
      this.internalField1434 = localValue1 -> {
         if (this.internalField1592.internalMethod04496() && this.internalField0277 && !this.internalField0518.internalMethod02365(4000L)) {
            localValue1.setForward(0.0F);
            localValue1.setJump(false);
            localValue1.setStrafe(0.0F);
            localValue1.setSprint(false);
         }
      };
      this.internalField0022 = null;
      this.internalField1512 = false;
      this.internalField1515 = false;
      this.internalField1550 = new Stopwatch();
      this.internalField1433 = localValue1 -> {
         if (ServerUtils.internalField0277 && this.internalField1593.internalMethod04496()) {
            CustomDrawContext localValue2x = localValue1.getContext();
            if (!internalField0149.player.hasStatusEffect(StatusEffects.STRENGTH)) {
               HudRenderUtils.internalMethod02251(
                  localValue2x,
                  localValue0 -> localValue0.getItem() == Items.POTION && PotionItemUtils.internalMethod05184(localValue0, StatusEffects.STRENGTH),
                  ThemeColors.internalMethod02531().mulAlpha(0.85F)
               );
            }

            if (!(internalField0149.player.getHealth() + internalField0149.player.getAbsorptionAmount() > 19.0F)) {
               if (this.internalField1550.internalMethod02365(ServerUtils.internalMethod08700() ? 10000L : 20000L)) {
                  HudRenderUtils.internalMethod02251(
                     localValue2x,
                     localValue0 -> localValue0.getItem() == Items.POTION && PotionItemUtils.internalMethod05184(localValue0, StatusEffects.INSTANT_HEALTH),
                     ThemeColors.internalMethod02531().mulAlpha(0.85F)
                  );
               } else if (internalField0149.player.getHungerManager().getFoodLevel() < 20) {
                  HudRenderUtils.internalMethod04554(localValue2x, Items.GOLDEN_CARROT, ThemeColors.internalMethod02531().mulAlpha(0.85F));
               } else if (!internalField0149.player.getItemCooldownManager().isCoolingDown(Items.GOLDEN_APPLE.getDefaultStack())) {
                  HudRenderUtils.internalMethod04554(localValue2x, Items.GOLDEN_APPLE, ThemeColors.internalMethod02531().mulAlpha(0.85F));
               } else {
                  HudRenderUtils.internalMethod04554(localValue2x, Items.ENCHANTED_GOLDEN_APPLE, ThemeColors.internalMethod02531().mulAlpha(0.85F));
               }
            }
         }
      };
      this.internalField1437 = localValue1 -> {
         if (localValue1.getUser() == internalField0149.player) {
            if (PotionItemUtils.internalMethod05184(localValue1.getStack(), StatusEffects.INSTANT_HEALTH)) {
               this.internalField1550.internalMethod00701();
            }
         }
      };
      this.internalMethod09438();
   }

   private void internalMethod09438() {
      this.internalField0663 = new ButtonSetting(this, "\u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043c\u0430\u043a\u0440\u043e\u0441\u044b")
         .internalMethod07149(() -> internalField0149.setScreen(new AssistScreen()));
      this.internalField0650 = new BooleanSetting(this, "modules.settings.assist.show_radius").internalMethod06630();
      this.internalField0651 = new BooleanSetting(
         this, "modules.settings.assist.spoof_rp", "modules.settings.assist.spoof_rp.description", this.internalField0424
      );
      this.internalField1261 = new BooleanSetting(
            this, "modules.settings.assist.close_menu", "modules.settings.assist.close_menu.description", this.internalField0424
         )
         .internalMethod06630();
      this.internalField1263 = new BooleanSetting(this, "modules.settings.assist.auto_fix", this.internalField0424);
      this.internalField1262 = new BooleanSetting(this, "modules.settings.assist.chat_filter", this.internalField0424);
      this.internalField1264 = new BooleanSetting(this, "modules.settings.assist.reduce_cooldown");
      this.internalField1587 = new BooleanSetting(this, "modules.settings.assist.warn_armor", "modules.settings.assist.warn_armor.description");
      this.internalField1590 = new BooleanSetting(this, "modules.settings.assist.dragon_fly", "modules.settings.assist.dragon_fly.description")
         .internalMethod06630();
      this.internalField0383 = new SliderSetting(this, "modules.settings.assist.fly_speed_xz", () -> !this.internalField1590.internalMethod04496())
         .internalMethod08074(1.0F)
         .internalMethod02732(5.0F)
         .internalMethod05900(1.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(5.0F);
      this.internalField0382 = new SliderSetting(this, "modules.settings.assist.fly_speed_y", () -> !this.internalField1590.internalMethod04496())
         .internalMethod08074(1.0F)
         .internalMethod02732(5.0F)
         .internalMethod05900(1.0F)
         .internalMethod08673(0.5F)
         .internalMethod08074(5.0F);
      this.internalField1594 = new BooleanSetting(
            this, "modules.settings.assist.auto_piona", "modules.settings.assist.auto_piona.description", this.internalField0425
         )
         .internalMethod06630();
      this.internalField1588 = new BooleanSetting(
         this, "modules.settings.assist.auto_captcha", "modules.settings.assist.auto_captcha.description", this.internalField0425
      );
      this.internalField1589 = new BooleanSetting(this, "modules.settings.assist.auto_chorus");
      this.internalField1591 = new BooleanSetting(this, "modules.settings.assist.auto_zako", this.internalField1150);
      this.internalField1592 = new BooleanSetting(this, "modules.settings.assist.auto_stop", this.internalField1150);
      this.internalField1593 = new BooleanSetting(this, "modules.settings.assist.heal_helper");
      this.internalField1811 = new BooleanSetting(this, "modules.settings.assist.ct_commands", "modules.settings.assist.ct_commands.description")
         .internalMethod06630();
      this.internalField1809 = new BooleanSetting(this, "modules.settings.assist.auto_rct", this.internalField1150);
      this.internalField1810 = new BooleanSetting(this, "modules.settings.assist.buffs_loop");
      this.internalField0668 = new ModeSetting(
         this, "modules.settings.assist.hand_mode", () -> ServerUtils.internalMethod01786(KnownServer.internalField0578)
      );
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.assist.hand_mode.left");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.assist.hand_mode.right");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.assist.hand_mode.packet");
   }

   private void internalMethod08202(int localValue1) {
      for (AssistSwapItem localValue3 : this.internalField0417) {
         if (localValue3.internalMethod03236() && !localValue3.internalMethod04619() && KeybindUtils.internalMethod04328(localValue3.internalMethod03234(), localValue1)) {
            if (this.internalMethod06130(localValue3)) {
               this.internalField0545.add(localValue1);
               return;
            }

            this.internalMethod06129(localValue3);
            return;
         }
      }
   }

   private void internalMethod09590() {
      Iterator localValue1 = this.internalField0545.iterator();

      while (localValue1.hasNext()) {
         int localValue2 = (Integer)localValue1.next();
         if (!this.internalMethod02748(localValue2)) {
            localValue1.remove();
            if (internalField0149.currentScreen == null) {
               for (AssistSwapItem localValue4 : this.internalField0417) {
                  if (this.internalMethod06130(localValue4)
                     && localValue4.internalMethod03236()
                     && !localValue4.internalMethod04619()
                     && KeybindUtils.internalMethod02025(localValue4.internalMethod03234()) == localValue2) {
                     this.internalMethod06129(localValue4);
                     break;
                  }
               }
            }
         }
      }
   }

   private boolean internalMethod06130(AssistSwapItem localValue1) {
      return localValue1 instanceof TrapkaSwapItem
         || localValue1 instanceof PlastAssist
         || localValue1 instanceof DezorentAssist
         || localValue1 instanceof PilbAssist
         || localValue1 instanceof AuraAssist
         || localValue1 instanceof StanAssist
         || localValue1 instanceof SnowAssist
         || localValue1 instanceof BoomTrapAssist;
   }

   private void internalMethod06129(AssistSwapItem localValue1) {
      localValue1.internalMethod03235();
      if (!localValue1.internalMethod07961()) {
         if (localValue1 instanceof BackpackSwapItem localValue2) {
            localValue2.internalMethod08913();
         } else {
            ItemCooldownHolder.internalField0006.internalMethod04839(localValue1.internalMethod06489().getItem(), localValue1::internalMethod05467, localValue1.internalMethod00556());
         }
      }
   }

   private boolean internalMethod02748(int localValue1) {
      return KeybindUtils.internalMethod08521(localValue1);
   }

   public final void internalMethod03137(List<AssistSwapItem> localValue1) {
      this.internalField0417.clear();
      this.internalField0417.addAll(localValue1);
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
         this.internalMethod09590();

         for (AssistSwapItem localValue2 : this.internalField0417) {
            localValue2.internalMethod04618();
         }

         this.internalMethod09604();
         if (this.internalField1588.internalMethod04496()) {
            this.internalField0011.internalMethod03753();
         } else {
            this.internalField0011.internalMethod03755();
         }

         if (this.internalField1589.internalMethod04496() && RockstarClient.getInstance().internalMethod04463().internalMethod04526() != null) {
            LivingEntity localValue6 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
            if (internalField0149.player.distanceTo(localValue6) < 5.0F && !localValue6.getActiveItem().isOf(Items.CHORUS_FRUIT)) {
               this.internalField1186.internalMethod00701();
            }
         }

         if (this.internalField1810.internalMethod04496()) {
            if (this.internalField0227 == 0) {
               internalField0149.player.networkHandler.sendChatMessage("\u041a\u0442\u043e \u0445\u043e\u0447\u0435\u0442 \u0431\u0430\u0444\u044b?");
               this.internalField1189.internalMethod00701();
               this.internalField0227 = 1;
            } else if (this.internalField0227 == 1 && this.internalField1189.internalMethod02365(1000L)) {
               internalField0149.player.networkHandler.sendChatMessage("\u041a\u0442\u043e \u0441\u043e\u0441\u0430\u043b?");
               this.internalField1189.internalMethod00701();
               this.internalField0227 = 2;
            } else if (this.internalField0227 == 2 && this.internalField1189.internalMethod02365(10000L)) {
               this.internalField0227 = 0;
            }
         } else {
            this.internalField0227 = 0;
         }

         if (this.internalField1587.internalMethod04496()) {
            float localValue7 = 1.0F;

            for (ItemStack localValue3 : rockstar.client.util.LegacyItemTypes.armorItems(internalField0149.player)) {
               if (!localValue3.isEmpty()) {
                  float localValue4 = localValue3.getMaxDamage();
                  float localValue5 = localValue4 - localValue3.getDamage();
                  localValue7 = localValue5 / localValue4;
               }
            }

            if (localValue7 < 0.36) {
               if (this.internalField1100) {
                  RockstarClient.getInstance()
                     .internalMethod02503()
                     .internalMethod00599(
                        NotificationType.internalField1280,
                        LanguageManager.internalMethod07214("assist.break"),
                        LanguageManager.internalMethod07214("assist.armor_almost_broken")
                     );
                  this.internalField1100 = false;
               }
            } else {
               this.internalField1100 = true;
            }
         }

         if (this.internalField1590.internalMethod04496()
            && internalField0149.player.getAbilities().allowFlying
            && internalField0149.player.getAbilities().flying
            && !RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class).isEnabled()) {
            if (!internalField0149.player.isSneaking() && internalField0149.options.jumpKey.isPressed()) {
               internalField0149.player
                  .setVelocity(internalField0149.player.getVelocity().x, this.internalField0382.internalMethod08576(), internalField0149.player.getVelocity().z);
            } else if (internalField0149.options.sneakKey.isPressed()) {
               internalField0149.player
                  .setVelocity(internalField0149.player.getVelocity().x, -this.internalField0382.internalMethod08576(), internalField0149.player.getVelocity().z);
            }

            GameUtils.internalMethod05868(this.internalField0383.internalMethod08576(), false);
         }

         if (this.internalField1263.internalMethod04496()) {
            PlayerInventory localValue8 = internalField0149.player.getInventory();
            boolean localValue10 = this.internalMethod06605(localValue8);
            if (ServerUtils.internalField0277 && localValue10) {
               this.internalField1517 = true;
            }

            if (!ServerUtils.internalField0277 && localValue10) {
               if (this.internalField1517) {
                  this.internalMethod09592();
                  this.internalField1517 = false;
                  this.internalField1187.internalMethod00701();
               }
            } else if (!localValue10) {
               this.internalField1517 = false;
            }
         } else {
            this.internalField1517 = false;
         }

         if (this.internalField0651.internalMethod04496()
            && this.internalField0651.isVisible()
            && internalField0149.player.age > 20
            && internalField0149.currentScreen instanceof ConfirmServerResourcePackScreen) {
            internalField0149.player.networkHandler.sendPacket(new ResourcePackStatusC2SPacket(internalField0149.player.getUuid(), Status.ACCEPTED));
            internalField0149.player.networkHandler.sendPacket(new ResourcePackStatusC2SPacket(internalField0149.player.getUuid(), Status.SUCCESSFULLY_LOADED));
            internalField0149.player.closeScreen();
         }

         if (this.internalField1594.internalMethod04496()) {
            if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler
               && internalField0149.currentScreen.getTitle().getString().contains("\u0412\u0430\u043c \u043f\u043e\u0434\u0430\u0440\u043e\u043a")) {
               internalField0149.interactionManager
                  .clickSlot(internalField0149.player.currentScreenHandler.syncId, 13, 0, SlotActionType.PICKUP, internalField0149.player);
            }

            if (this.internalField0519.internalMethod02365(1000L) && !this.internalField1099) {
               this.internalField1099 = true;
               internalField0149.player.networkHandler.sendChatCommand("piona");
            }
         }

         if (this.internalField1591.internalMethod04496() && this.internalField0519.internalMethod02365(500L) && this.internalField0276) {
            this.internalField0276 = false;
            internalField0149.player.networkHandler.sendChatCommand("zako");
         }

         super.internalMethod08229();
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.internalField0227 = 0;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.internalField0227 = 0;
      this.internalField0545.clear();
      this.internalMethod10106();
      this.internalField0011.internalMethod03755();
   }

   private void internalMethod09592() {
      internalField0149.player.networkHandler.sendChatCommand("fix all");
   }

   private boolean internalMethod06605(PlayerInventory localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1.size(); localValue2++) {
         ItemStack localValue3 = localValue1.getStack(localValue2);
         if (!localValue3.isEmpty() && localValue3.isDamageable()) {
            float localValue4 = localValue3.getMaxDamage();
            float localValue5 = localValue4 - localValue3.getDamage();
            if (localValue5 / localValue4 > 0.5F) {
               return true;
            }
         }
      }

      return false;
   }

   private void internalMethod09604() {
      if (!this.internalField1589.internalMethod04496()) {
         if (this.internalField1102) {
            this.internalMethod10106();
         }

         this.internalField1186.internalMethod00701();
      } else if (internalField0149.player != null && internalField0149.world != null) {
         LivingEntity localValue1 = RockstarClient.getInstance().internalMethod04463().internalMethod01783();
         if (localValue1 == null) {
            this.internalField1186.internalMethod00701();
            if (this.internalField1102 && !internalField0149.player.isUsingItem()) {
               this.internalMethod10106();
            }
         } else if (!localValue1.isAlive()) {
            this.internalField1186.internalMethod00701();
         } else {
            double localValue2 = internalField0149.player.squaredDistanceTo(localValue1);
            boolean localValue4 = localValue1.isUsingItem() && localValue1.getActiveItem().getItem() == Items.CHORUS_FRUIT;
            if (!localValue4 || localValue2 > 25.0) {
               this.internalField1186.internalMethod00701();
            } else if (!this.internalField1102
               && internalField0149.currentScreen == null
               && !internalField0149.player.isUsingItem()
               && this.internalField1186.internalMethod02365(200L)) {
               this.internalMethod09606();
            }

            if (this.internalField1102) {
               if (internalField0149.player.isUsingItem()) {
                  this.internalField1516 = true;
               } else if (this.internalField1516) {
                  this.internalMethod10106();
                  return;
               }

               if (this.internalField1188.internalMethod02365(2500L)) {
                  this.internalMethod10106();
               }
            }
         }
      } else {
         if (this.internalField1102) {
            this.internalMethod10106();
         }

         this.internalField1186.internalMethod00701();
      }
   }

   private void internalMethod09606() {
      if (internalField0149.player != null && internalField0149.interactionManager != null && internalField0149.options != null) {
         SlotCollection localValue1 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558());
         InventorySlot localValue2 = localValue1.internalMethod02510(Items.CHORUS_FRUIT);
         if (localValue2 != null) {
            HotbarSlot localValue3 = InventoryUtils.internalMethod06160();
            this.internalField0226 = localValue3;
            this.internalField1101 = false;
            this.internalField0228 = -1;
            if (localValue2 instanceof HotbarSlot localValue4) {
               if (localValue4.internalMethod08745() != localValue3.internalMethod08745()) {
                  InventoryUtils.internalMethod01980(localValue4);
               }
            } else {
               this.internalField1101 = true;
               this.internalField0228 = localValue2.internalMethod06662();
               InventoryUtils.internalMethod08821(this.internalField0228, localValue3.internalMethod08745());
            }

            internalField0149.options.useKey.setPressed(true);
            this.internalField1102 = true;
            this.internalField1188.internalMethod00701();
            this.internalField1516 = false;
         }
      }
   }

   private void internalMethod10106() {
      if (!this.internalField1102) {
         this.internalField1101 = false;
         this.internalField0226 = null;
         this.internalField0228 = -1;
         this.internalField1516 = false;
      } else {
         if (internalField0149.options != null) {
            internalField0149.options.useKey.setPressed(false);
         }

         if (internalField0149.player != null && internalField0149.interactionManager != null) {
            if (this.internalField1101 && this.internalField0226 != null && this.internalField0228 != -1) {
               InventoryUtils.internalMethod08821(this.internalField0228, this.internalField0226.internalMethod08745());
            }

            if (this.internalField0226 != null) {
               InventoryUtils.internalMethod01980(this.internalField0226);
            }
         }

         this.internalField1102 = false;
         this.internalField1101 = false;
         this.internalField0226 = null;
         this.internalField0228 = -1;
         this.internalField1516 = false;
         this.internalField1186.internalMethod00701();
         this.internalField1188.internalMethod00701();
      }
   }

   public void internalMethod09436() {
      if (this.internalField1512) {
         MinecraftClient.getInstance().options.useKey.setPressed(false);
         this.internalField1512 = false;
      }

      if (this.internalField0022 != null && this.internalField0022 instanceof HotbarSlot localValue1) {
         InventoryUtils.internalMethod01980(localValue1);
         this.internalField0022 = null;
      }

      this.internalField1515 = false;
   }

   private boolean internalMethod09942() {
      return ServerUtils.internalMethod01786(KnownServer.internalField1571)
         || ServerUtils.internalMethod01786(KnownServer.internalField1566)
         || ServerUtils.internalMethod01786(KnownServer.internalField1569);
   }

   private boolean internalMethod06330(String localValue1) {
      if (this.internalField1811.internalMethod04496() && ServerUtils.internalField0277) {
         String localValue2 = this.internalMethod01638(localValue1);
         if (localValue2 == null) {
            return false;
         } else {
            String[] localValue3 = new String[]{"hub", "an", "grief", "leave", "limbo", "lobby"};

            for (String localValue7 : localValue3) {
               if (localValue2.equals(localValue7.toLowerCase(Locale.ROOT))) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private String internalMethod01638(String localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         String localValue2 = localValue1.trim();
         if (!localValue2.startsWith("/")) {
            return null;
         } else {
            localValue2 = localValue2.substring(1).trim();
            if (localValue2.isEmpty()) {
               return null;
            } else {
               int localValue3 = localValue2.indexOf(32);
               if (localValue3 != -1) {
                  localValue2 = localValue2.substring(0, localValue3);
               }

               return localValue2.toLowerCase(Locale.ROOT);
            }
         }
      }
   }

   public boolean internalMethod09437() {
      return this.internalField0668.internalMethod06103(this.internalField0237);
   }

   public boolean internalMethod09439() {
      return this.internalField0668.internalMethod06103(this.internalField1066);
   }

   @Generated
   public BooleanSupplier internalMethod06178() {
      return this.internalField0424;
   }

   @Generated
   public BooleanSupplier internalMethod00982() {
      return this.internalField0425;
   }

   @Generated
   public BooleanSupplier internalMethod08767() {
      return this.internalField1150;
   }

   @Generated
   public ButtonSetting internalMethod02417() {
      return this.internalField0663;
   }

   @Generated
   public BooleanSetting internalMethod02374() {
      return this.internalField0650;
   }

   @Generated
   public BooleanSetting internalMethod02990() {
      return this.internalField0651;
   }

   @Generated
   public BooleanSetting internalMethod08254() {
      return this.internalField1261;
   }

   @Generated
   public BooleanSetting internalMethod08388() {
      return this.internalField1263;
   }

   @Generated
   public BooleanSetting internalMethod08393() {
      return this.internalField1262;
   }

   @Generated
   public BooleanSetting internalMethod07778() {
      return this.internalField1264;
   }

   @Generated
   public BooleanSetting internalMethod09619() {
      return this.internalField1587;
   }

   @Generated
   public BooleanSetting internalMethod09680() {
      return this.internalField1590;
   }

   @Generated
   public SliderSetting internalMethod00066() {
      return this.internalField0383;
   }

   @Generated
   public SliderSetting internalMethod00802() {
      return this.internalField0382;
   }

   @Generated
   public BooleanSetting internalMethod09303() {
      return this.internalField1594;
   }

   @Generated
   public BooleanSetting internalMethod09373() {
      return this.internalField1588;
   }

   @Generated
   public BooleanSetting internalMethod09635() {
      return this.internalField1589;
   }

   @Generated
   public BooleanSetting internalMethod09698() {
      return this.internalField1591;
   }

   @Generated
   public BooleanSetting internalMethod09310() {
      return this.internalField1592;
   }

   @Generated
   public BooleanSetting internalMethod09388() {
      return this.internalField1593;
   }

   @Generated
   public BooleanSetting internalMethod09981() {
      return this.internalField1811;
   }

   @Generated
   public BooleanSetting internalMethod10004() {
      return this.internalField1809;
   }

   @Generated
   public BooleanSetting internalMethod09970() {
      return this.internalField1810;
   }

   @Generated
   public ModeSetting internalMethod02419() {
      return this.internalField0668;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod02899() {
      return this.internalField0237;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod03089() {
      return this.internalField0238;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod07909() {
      return this.internalField1066;
   }

   @Generated
   public Set<String> internalMethod02625() {
      return this.internalField0546;
   }

   @Generated
   public List<Pattern> internalMethod05764() {
      return this.internalField1145;
   }

   @Generated
   public Stopwatch internalMethod01368() {
      return this.internalField0519;
   }

   @Generated
   public Stopwatch internalMethod02009() {
      return this.internalField0518;
   }

   @Generated
   public Stopwatch internalMethod08058() {
      return this.internalField1189;
   }

   @Generated
   public Stopwatch internalMethod08182() {
      return this.internalField1186;
   }

   @Generated
   public Stopwatch internalMethod09010() {
      return this.internalField1188;
   }

   @Generated
   public Stopwatch internalMethod09137() {
      return this.internalField1187;
   }

   @Generated
   public AutoCaptchaSolver internalMethod00228() {
      return this.internalField0011;
   }

   @Generated
   public boolean internalMethod09591() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod09593() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod09605() {
      return this.internalField1099;
   }

   @Generated
   public boolean internalMethod09607() {
      return this.internalField1100;
   }

   @Generated
   public int internalMethod08228() {
      return this.internalField0227;
   }

   @Generated
   public boolean internalMethod10107() {
      return this.internalField1102;
   }

   @Generated
   public boolean internalMethod10108() {
      return this.internalField1101;
   }

   @Generated
   public int internalMethod08230() {
      return this.internalField0228;
   }

   @Generated
   public HotbarSlot internalMethod06869() {
      return this.internalField0226;
   }

   @Generated
   public boolean internalMethod10112() {
      return this.internalField1516;
   }

   @Generated
   public boolean internalMethod10113() {
      return this.internalField1517;
   }

   @Generated
   public Set<Integer> internalMethod07245() {
      return this.internalField0545;
   }

   @Generated
   public EventListener<MouseEvent> internalMethod01715() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<KeyPressEvent> internalMethod02935() {
      return this.internalField0158;
   }

   @Generated
   public EventListener<EventSetCooldown> internalMethod08266() {
      return this.internalField1028;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod08504() {
      return this.internalField1029;
   }

   @Generated
   public EventListener<SendMessageEvent> internalMethod08397() {
      return this.internalField1030;
   }

   @Generated
   public EventListener<AttackEvent> internalMethod08648() {
      return this.internalField1027;
   }

   @Generated
   public EventListener<FinishEatEvent> internalMethod09508() {
      return this.internalField1436;
   }

   @Generated
   public EventListener<FinishEatEvent> internalMethod09634() {
      return this.internalField1435;
   }

   @Generated
   public EventListener<InputEvent> internalMethod09582() {
      return this.internalField1434;
   }

   @Generated
   public InventorySlot internalMethod05217() {
      return this.internalField0022;
   }

   @Generated
   public boolean internalMethod09939() {
      return this.internalField1512;
   }

   @Generated
   public boolean internalMethod09940() {
      return this.internalField1515;
   }

   @Generated
   public Stopwatch internalMethod09524() {
      return this.internalField1550;
   }

   @Generated
   public EventListener<HudRenderEvent> internalMethod09697() {
      return this.internalField1433;
   }

   @Generated
   public EventListener<FinishEatEvent> internalMethod09182() {
      return this.internalField1437;
   }

   @Generated
   public List<AssistSwapItem> internalMethod03313() {
      return this.internalField0416;
   }

   @Generated
   public List<AssistSwapItem> internalMethod08262() {
      return this.internalField0417;
   }
}
