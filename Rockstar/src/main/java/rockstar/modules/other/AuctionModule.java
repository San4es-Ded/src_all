package rockstar.modules.other;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import moscow.rockstar.mixin.accessors.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.ScreenRenderEvent;
import pyrock.events.window.ContainerClickEvent;
import pyrock.events.window.ContainerReleaseEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;

@ModuleInfo(
   name = "Auction",
   category = ModuleCategory.OTHER
)
public class AuctionModule extends Module {
   private final List<AuctionModule.InternalType0467> internalField0416 = new ArrayList<>();
   private double internalField0194 = 0.0;
   private double internalField0193 = Double.MAX_VALUE;
   private static final long internalField0229 = 100L;
   private static final Pattern internalField0293 = Pattern.compile("\\b(\\d+)\\s*/\\s*(\\d+)\\b");
   private String internalField0247 = "";
   private int internalField0227 = -1;
   private double internalField1043 = -1.0;
   private long internalField1058 = -1L;
   private long internalField1060 = -1L;
   private ColorSetting internalField0665;
   private ColorSetting internalField0664;
   private BooleanSetting internalField0650;
   private BooleanSetting internalField0651;
   private KeybindSetting internalField0648;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private TextSetting internalField0384;
   private KeybindSetting internalField0647;
   private ModeSetting internalField0669;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private SliderSetting internalField0383;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting internalField0674;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private MultiSelectSetting.InternalType0091 internalField1072;
   private MultiSelectSetting.InternalType0091 internalField1491;
   private MultiSelectSetting.InternalType0091 internalField1488;
   private MultiSelectSetting.InternalType0091 internalField1490;
   private MultiSelectSetting.InternalType0091 internalField1489;
   private MultiSelectSetting internalField1276;
   private MultiSelectSetting.InternalType0091 internalField1493;
   private MultiSelectSetting.InternalType0091 internalField1487;
   private MultiSelectSetting.InternalType0091 internalField1486;
   private boolean internalField0277 = false;
   private boolean internalField0276 = false;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final Stopwatch internalField1189 = new Stopwatch();
   private final List<AuctionModule.InternalType0045> internalField0417 = new ArrayList<>();
   private AuctionModule.InternalType0043 internalField0538;
   private Item internalField0152;
   private ItemStack internalField0878;
   private String internalField1077;
   private int internalField0228;
   private int internalField1053;
   private int internalField1055;
   private static final Item[] internalField0838 = new Item[]{
      Items.TRIDENT,
      Items.DIAMOND_SWORD,
      Items.NETHERITE_AXE,
      Items.GOLDEN_APPLE,
      Items.TOTEM_OF_UNDYING,
      Items.ENDER_PEARL,
      Items.ELYTRA,
      Items.SHIELD,
      Items.BOW,
      Items.CROSSBOW,
      Items.FIREWORK_ROCKET,
      Items.ENCHANTED_GOLDEN_APPLE
   };
   private final HudContextMenu internalField0574;
   private boolean internalField1099;
   private AuctionModule.InternalType0466 internalField0371;
   private final EventListener<KeyPressEvent> internalField0157;
   private final EventListener<ReceivePacketEvent> internalField0158;
   private final EventListener<HudRenderEvent> internalField1028;
   private final EventListener<ScreenRenderEvent> internalField1029;
   private final EventListener<ContainerClickEvent> internalField1030;
   private final EventListener<ContainerReleaseEvent> internalField1027;

   public AuctionModule() {
      this.internalField0538 = AuctionModule.InternalType0043.internalField0538;
      this.internalField0878 = ItemStack.EMPTY;
      this.internalField1077 = "";
      this.internalField0228 = 1;
      this.internalField1055 = -1;
      this.internalField0574 = new HudContextMenu(0.0F, 0.0F);
      this.internalField0371 = AuctionModule.InternalType0466.internalField0371;
      this.internalField0157 = localValue1 -> {
         int localValue2 = localValue1.getKey();
         int localValue3 = localValue1.getAction();
         this.internalField0277 = localValue2 == 340 && (localValue3 == 2 || localValue3 == 1 || localValue3 == 0);
         if (this.internalField0647.internalMethod02165(localValue2) && localValue3 == 1 && internalField0149.currentScreen == null) {
            this.internalMethod09216();
         } else {
            if (this.internalField0648.internalMethod02165(localValue2) && localValue3 == 1 && internalField0149.currentScreen == null) {
               ItemStack localValue4 = internalField0149.player.getMainHandStack();
               if (localValue4.isEmpty()) {
                  RockstarClient.getInstance()
                     .internalMethod02503()
                     .internalMethod02784(
                        new ItemNotification(
                           "\u0412\u0430\u043c \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0438\u043c\u0435\u0442\u044c \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u043f\u0440\u0430\u0432\u043e\u0439 \u0440\u0443\u043a\u0435!",
                           internalField0838[MathUtils.internalField0858.nextInt(internalField0838.length)]
                        )
                     );
                  return;
               }

               CustomItemUtils.InternalType0254 localValue5 = CustomItemUtils.internalMethod03238(localValue4);
               String localValue6 = localValue5 != null ? localValue5.internalMethod00671(localValue4) : ItemNbtUtils.internalMethod02181(localValue4);
               internalField0149.player.networkHandler.sendChatCommand("ah search " + localValue6);
            }
         }
      };
      this.internalField0158 = localValue1 -> {
         if (this.internalField1261.internalMethod04496() && internalField0149.player != null) {
            if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2) {
               if (this.internalMethod04506(localValue2.content()).contains("/ah search auto confirm")) {
                  if (this.internalField0519.internalMethod02365(3000L)) {
                     this.internalField0519.internalMethod00701();
                     internalField0149.player.networkHandler.sendChatCommand("/ah search auto confirm".substring(1));
                  }
               }
            }
         }
      };
      this.internalField1028 = localValue1 -> {
         if (internalField0149.currentScreen == null) {
            this.internalField0247 = "";
            this.internalField0574.internalMethod05781(false);
            if (this.internalField0574.internalMethod06960().internalMethod02881() > 0.0F) {
               this.internalMethod01981(localValue1.getContext());
            }
         }
      };
      this.internalField1029 = localValue1 -> {
         if (internalField0149.currentScreen instanceof HandledScreen localValue2) {
            if (this.internalMethod06532(localValue2.getTitle().getString())) {
               String localValue14 = this.internalField0247.toLowerCase(Locale.ROOT);
               boolean localValue4 = this.internalMethod02833(localValue14, "\u0438\u043d\u0432\u0438\u0437", "\u043d\u0435\u0432\u0438\u0434", "invis");
               boolean localValue5 = !localValue4 && this.internalMethod09219();
               this.internalField0574.internalMethod05781(localValue5);
               HandledScreenAccessor localValue6 = (HandledScreenAccessor)(Object)localValue2;

               try {
                  for (AuctionModule.InternalType0467 localValue8 : this.internalField0416) {
                     if (this.internalField0650.internalMethod04496() || !(localValue8.internalField0194 > this.internalField0194)) {
                        Slot localValue9 = localValue2.getScreenHandler().getSlot(localValue8.internalField0227);
                        if (localValue9 != null) {
                           int localValue10 = localValue6.getX() + localValue9.x;
                           int localValue11 = localValue6.getY() + localValue9.y;
                           ColorRGBA localValue12 = this.internalMethod04361(localValue8.internalField0194);
                           localValue1.getContext()
                              .drawRoundedRect(
                                 (float)localValue10,
                                 (float)localValue11,
                                 16.0F,
                                 16.0F,
                                 CornerRadii.internalMethod03908(1.0F),
                                 new AlternatingColorGradient(localValue12.withAlpha(0.0F), localValue12.withAlpha(0.8F * localValue12.getAlpha()))
                              );
                        }
                     }
                  }

                  if (this.internalMethod05075(this.internalField0247) && this.internalField0227 != -1 && this.internalField1058 != -1L && this.internalField1058 >= this.internalField1043
                     )
                   {
                     Slot localValue15 = localValue2.getScreenHandler().getSlot(this.internalField0227);
                     if (localValue15 != null) {
                        int localValue16 = localValue6.getX() + localValue15.x;
                        int localValue17 = localValue6.getY() + localValue15.y;
                        ColorRGBA localValue18 = new ColorRGBA(60.0F, 255.0F, 120.0F, 220.0F);
                        localValue1.getContext()
                           .drawRoundedRect(
                              (float)localValue16,
                              (float)localValue17,
                              16.0F,
                              16.0F,
                              CornerRadii.internalMethod03908(1.0F),
                              new AlternatingColorGradient(localValue18.withAlpha(0.0F), localValue18.withAlpha(0.85F * localValue18.getAlpha()))
                           );
                     }
                  }
               } catch (Exception localValue13) {
                  this.internalMethod10115();
               }

               if (localValue5 || this.internalField0574.internalMethod06960().internalMethod02881() > 0.0F) {
                  this.internalMethod01981(localValue1.getContext());
               }
            }
         }
      };
      this.internalField1030 = localValue1 -> {
         this.internalField0574.internalMethod01643(localValue1.getX(), localValue1.getY(), MouseButton.internalMethod01669(localValue1.getButton()));
         if (this.internalField0651.internalMethod04496()
            && this.internalField0277
            && internalField0149.currentScreen instanceof HandledScreen localValue2
            && this.internalMethod06532(this.internalField0247)) {
            HandledScreenAccessor localValue8 = (HandledScreenAccessor)(Object)localValue2;
            Slot localValue4 = localValue8.getFocusedSlot();
            if (localValue4 != null && localValue4.hasStack()) {
               AuctionModule.InternalType0467 localValue5 = this.internalField0416.stream().filter(localValue1x -> localValue1x.internalField0227 == localValue4.id).findFirst().orElse(null);
               if (localValue5 == null) {
                  return;
               }

               double localValue6 = this.internalField0193 * 2.0;
               if (localValue5.internalField0194 > localValue6) {
                  RockstarClient.getInstance()
                     .internalMethod02503()
                     .internalMethod02784(
                        new ItemNotification(
                           "\u0426\u0435\u043d\u0430 \u0431\u043e\u043b\u044c\u0448\u0435 \u043c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u043e\u0439 \u043d\u0430 100%+",
                           internalField0838[MathUtils.internalField0858.nextInt(internalField0838.length)]
                        )
                     );
                  return;
               }

               this.internalField0276 = true;
            }
         }
      };
      this.internalField1027 = localValue1 -> this.internalField0574.internalMethod02863(localValue1.getX(), localValue1.getY(), MouseButton.internalMethod01669(localValue1.getButton()));
      this.internalMethod09878();
   }

   private void internalMethod09878() {
      this.internalField0665 = new ColorSetting(this, "modules.settings.auction.low_price_color")
         .internalMethod04886(new ColorRGBA(60.0F, 255.0F, 60.0F, 250.0F));
      this.internalField0664 = new ColorSetting(this, "modules.settings.auction.high_price_color")
         .internalMethod04886(new ColorRGBA(255.0F, 255.0F, 60.0F, 250.0F));
      this.internalField0650 = new BooleanSetting(this, "modules.settings.auction.show_yellow");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.auction.fast_buy");
      this.internalField0648 = new KeybindSetting(this, "modules.settings.auction.search");
      this.internalField1261 = new BooleanSetting(this, "modules.settings.auction.auto_confirm");
      this.internalField1263 = new BooleanSetting(this, "modules.settings.auction.auto_sell");
      this.internalField0668 = new ModeSetting(this, "modules.settings.auction.auto_sell.mode", () -> !this.internalField1263.internalMethod04496());
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auction.auto_sell.mode.funtime").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auction.auto_sell.mode.holyworld");
      this.internalField0384 = new TextSetting(this, "modules.settings.auction.auto_sell.pages", () -> !this.internalField1263.internalMethod04496())
         .internalMethod00011("1")
         .internalMethod07009(true);
      this.internalField0647 = new KeybindSetting(this, "modules.settings.auction.auto_sell.bind", () -> !this.internalField1263.internalMethod04496());
      this.internalField0669 = new ModeSetting(this, "modules.settings.auction.price_mode");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auction.price_mode.per_unit");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.auction.price_mode.total");
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.auction.armor", () -> !this.internalMethod09223());
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auction.armor.no_spike").select();
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auction.armor.no_prot5").select();
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auction.armor.no_durability");
      this.internalField0383 = new SliderSetting(
            this, "modules.settings.auction.armor.min_durability", () -> !this.internalField1075.isSelected() || !this.internalMethod09223()
         )
         .internalMethod05900(1.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(100.0F)
         .internalMethod06240("%");
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.auction.armor.no_repair");
      this.internalField0674 = new MultiSelectSetting(this, "modules.settings.auction.pickaxe", () -> !this.internalMethod09225());
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.pickaxe.silk_touch");
      this.internalField1072 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.pickaxe.noMega");
      this.internalField1491 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.pickaxe.noMiner");
      this.internalField1488 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.armor.no_repair");
      this.internalField1490 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.pickaxe.noMagnit");
      this.internalField1489 = new MultiSelectSetting.InternalType0091(this.internalField0674, "modules.settings.auction.pickaxe.noExp");
      this.internalField1276 = new MultiSelectSetting(this, "modules.settings.auction.potions", () -> !this.internalMethod10110());
      this.internalField1493 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.auction.potions.no_level3");
      this.internalField1487 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.auction.potions.no_combined");
      this.internalField1486 = new MultiSelectSetting.InternalType0091(this.internalField1276, "modules.settings.auction.potions.no_under_6min");
   }

   private void internalMethod09880() {
      if (!this.internalField1099) {
         this.internalField1099 = true;

         for (Setting localValue2 : this.getSettings()) {
            if (localValue2 instanceof MultiSelectSetting localValue3) {
               this.internalField0574.internalMethod05995(localValue3);
            }
         }
      }
   }

   @Override
   public void internalMethod08229() {
      if (this.internalField0538 != AuctionModule.InternalType0043.internalField0538) {
         this.internalMethod09218();
      } else if (internalField0149.currentScreen instanceof HandledScreen localValue1) {
         String localValue5 = localValue1.getTitle().getString();
         this.internalField0247 = localValue5;
         if (this.internalField0276 && this.internalMethod07000(localValue1)) {
            if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue3) {
               internalField0149.interactionManager.clickSlot(localValue3.syncId, 11, 0, SlotActionType.PICKUP, internalField0149.player);
               this.internalField0276 = false;
            }
         } else if (!this.internalMethod06532(localValue5)) {
            this.internalMethod10115();
         } else {
            this.internalMethod06531(localValue5);
            this.internalMethod06999(localValue1);
            super.internalMethod08229();
         }
      } else {
         this.internalMethod10115();
         this.internalField0276 = false;
      }
   }

   private String internalMethod04506(Text localValue1) {
      return localValue1.getString().replaceAll("(?i)\u00a7[0-9a-fk-or]", "").replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
   }

   private boolean internalMethod09219() {
      for (Setting localValue2 : this.getSettings()) {
         if (localValue2 instanceof MultiSelectSetting localValue3 && localValue3.isVisible()) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod01981(CustomDrawContext localValue1) {
      this.internalMethod09880();
      UiRenderContext localValue2 = UiRenderContext.internalMethod02316(
         localValue1,
         internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().x(),
         internalField0149.currentScreen == null ? -1 : (int)UiUtils.internalMethod03634().y(),
         MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false)
      );
      this.internalField0574.internalMethod08630(120.0F);
      this.internalField0574.internalMethod04932(10.0F, internalField0389.internalMethod03589() / 2.0F - this.internalField0574.internalMethod07809() / 2.0F);
      this.internalField0574.internalMethod03398(localValue2);
   }

   private ColorRGBA internalMethod04361(double localValue1) {
      double localValue3 = this.internalField0194 - this.internalField0193;
      float localValue5 = localValue3 > 0.0 ? (float)((localValue1 - this.internalField0193) / localValue3) : 0.0F;
      localValue5 = Math.max(0.0F, Math.min(1.0F, localValue5));
      ColorRGBA localValue6 = this.internalField0665.internalMethod05620();
      ColorRGBA localValue7 = this.internalField0664.internalMethod05620();
      ColorRGBA localValue8 = localValue6.mix(localValue7, localValue5);
      float localValue9 = 1.0F - localValue5 * 0.6F;
      return localValue8.mulAlpha(localValue9);
   }

   private boolean internalMethod07000(HandledScreen<?> localValue1) {
      String localValue2 = AuctionTooltipParser.internalMethod03300(localValue1.getTitle().getString());
      return !localValue2.contains("\u043f\u043e\u043a\u0443\u043f\u043a") && !localValue2.contains("\u043f\u043e\u0434\u0442\u0432\u0435\u0440")
         ? !AuctionSearchTracker.internalMethod05980(localValue1) && !this.internalMethod06532(localValue1.getTitle().getString())
         : true;
   }

   private boolean internalMethod06532(String localValue1) {
      return AuctionTooltipParser.internalMethod08274(localValue1)
         ? true
         : this.internalMethod02833(
            AuctionTooltipParser.internalMethod03300(localValue1),
            "\u043d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a",
            "\u0448\u043b\u0435\u043c",
            "\u043f\u043e\u043d\u043e\u0436",
            "\u0431\u043e\u0442\u0438\u043d",
            "\u043a\u0438\u0440\u043a",
            "\u0437\u0435\u043b\u044c"
         );
   }

   private boolean internalMethod05075(String localValue1) {
      return AuctionTooltipParser.internalMethod07991(localValue1);
   }

   @Override
   public void onDisable() {
      this.internalMethod10114();
      this.internalMethod10115();
      super.onDisable();
   }

   private void internalMethod09216() {
      if (this.internalField1263.internalMethod04496() && internalField0149.player != null && internalField0149.world != null) {
         ItemStack localValue1 = internalField0149.player.getMainHandStack();
         if (localValue1.isEmpty()) {
            RockstarClient.getInstance()
               .internalMethod02503()
               .internalMethod02784(
                  new ItemNotification(
                     "\u0412\u043e\u0437\u044c\u043c\u0438\u0442\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0432 \u0440\u0443\u043a\u0443",
                     internalField0838[MathUtils.internalField0858.nextInt(internalField0838.length)]
                  )
               );
         } else {
            this.internalField0152 = localValue1.getItem();
            this.internalField0878 = localValue1.copy();
            this.internalField1077 = ItemNbtUtils.internalMethod02181(localValue1).trim();
            if (this.internalField1077.isEmpty()) {
               RockstarClient.getInstance()
                  .internalMethod02503()
                  .internalMethod02784(
                     new ItemNotification(
                        "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430",
                        internalField0838[MathUtils.internalField0858.nextInt(internalField0838.length)]
                     )
                  );
            } else {
               this.internalField0228 = this.internalMethod09876();
               this.internalField1053 = 0;
               this.internalField1055 = -1;
               this.internalField0417.clear();
               this.internalField0538 = AuctionModule.InternalType0043.internalField0537;
               this.internalField0518.internalMethod00701();
               this.internalField1189.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09218() {
      if (internalField0149.player != null && internalField0149.world != null) {
         switch (this.internalField0538) {
            case internalField0537:
               this.internalMethod09222();
               break;
            case internalField1194:
               this.internalMethod09224();
               break;
            case internalField1193:
               this.internalMethod10109();
         }
      } else {
         this.internalMethod10114();
      }
   }

   private void internalMethod09222() {
      if (this.internalField0518.internalMethod02365(100L)) {
         this.internalMethod09941();
         internalField0149.player.networkHandler.sendChatCommand("ah search " + this.internalField1077);
         this.internalMethod02158(AuctionModule.InternalType0043.internalField1194);
      }
   }

   private void internalMethod09224() {
      ScreenHandler localValue1 = this.internalMethod06493();
      if (localValue1 == null) {
         if (this.internalField1189.internalMethod02365(5000L)) {
            ClientMessages.internalMethod03058(
               Text.literal(
                  "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u043a\u0440\u044b\u0442\u044c \u0430\u0443\u043a\u0446\u0438\u043e\u043d"
               )
            );
            this.internalMethod10114();
         }
      } else if (this.internalField0518.internalMethod02365(100L)) {
         this.internalField1053++;
         this.internalField0417.addAll(this.internalMethod05131(localValue1));
         if (this.internalField1053 >= this.internalField0228) {
            this.internalMethod10111();
         } else {
            AuctionModule.InternalType0044 localValue2 = this.internalMethod03153(localValue1);
            if (localValue2 != null && localValue2.internalField0227 >= localValue2.internalField0228) {
               this.internalMethod10111();
            } else {
               int localValue3 = this.internalMethod03906(localValue1);
               if (localValue3 == -1) {
                  this.internalMethod10111();
               } else {
                  this.internalField1055 = localValue2 == null ? -1 : localValue2.internalField0227 + 1;
                  internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue3, 0, SlotActionType.PICKUP, internalField0149.player);
                  this.internalMethod02158(AuctionModule.InternalType0043.internalField1193);
               }
            }
         }
      }
   }

   private void internalMethod10109() {
      ScreenHandler localValue1 = this.internalMethod06493();
      if (localValue1 == null) {
         this.internalMethod10111();
      } else {
         AuctionModule.InternalType0044 localValue2 = this.internalMethod03153(localValue1);
         if (localValue2 != null && this.internalField1055 != -1 && localValue2.internalField0227 >= this.internalField1055) {
            this.internalMethod02158(AuctionModule.InternalType0043.internalField1194);
         } else if (this.internalField1055 == -1 && this.internalField0518.internalMethod02365(100L)) {
            this.internalMethod02158(AuctionModule.InternalType0043.internalField1194);
         } else {
            if (this.internalField1189.internalMethod02365(5000L)) {
               this.internalMethod10111();
            }
         }
      }
   }

   private void internalMethod10111() {
      AuctionModule.InternalType0046 localValue1 = this.internalMethod03745(this.internalField0417);
      if (localValue1.internalField0228 > 0) {
         long localValue2 = Math.round(localValue1.internalMethod04921());
         int localValue4 = Math.max(1, this.internalField0878.getCount());
         long localValue5 = localValue2 * localValue4;
         this.internalMethod09941();
         internalField0149.player.networkHandler.sendChatCommand("ah sell " + localValue5);
         ClientMessages.internalMethod01809(
            Text.literal(
               "\u0412\u044b\u0441\u0442\u0430\u0432\u0438\u043b "
                  + this.internalField0152.getName().getString()
                  + " x"
                  + localValue4
                  + " \u0437\u0430 $"
                  + this.internalMethod02614(localValue5)
                  + " ("
                  + this.internalMethod02614(localValue2)
                  + "$ \u0437\u0430 1 \u0448\u0442.)"
            )
         );
      } else {
         ClientMessages.internalMethod03058(
            Text.literal(
               "\u0426\u0435\u043d\u044b \u0434\u043b\u044f \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b"
            )
         );
         this.internalMethod09941();
      }

      this.internalMethod10114();
   }

   private void internalMethod02158(AuctionModule.InternalType0043 localValue1) {
      this.internalField0538 = localValue1;
      this.internalField0518.internalMethod00701();
      this.internalField1189.internalMethod00701();
   }

   private void internalMethod10114() {
      this.internalField0538 = AuctionModule.InternalType0043.internalField0538;
      this.internalField0152 = null;
      this.internalField0878 = ItemStack.EMPTY;
      this.internalField1077 = "";
      this.internalField0228 = 1;
      this.internalField1053 = 0;
      this.internalField1055 = -1;
      this.internalField0417.clear();
   }

   private boolean internalMethod02163(ItemStack localValue1) {
      EquippableComponent localValue2 = (EquippableComponent)localValue1.get(DataComponentTypes.EQUIPPABLE);
      if (localValue2 != null && localValue1.getMaxDamage() > 0) {
         return switch (localValue2.slot()) {
            case HEAD, CHEST, LEGS, FEET -> true;
            default -> false;
         };
      } else {
         return false;
      }
   }

   private boolean internalMethod07438(ItemStack localValue1, List<Text> localValue2) {
      Item localValue3 = localValue1.getItem();
      if (this.internalField0371 == AuctionModule.InternalType0466.internalField0370 && this.internalMethod02163(localValue1)) {
         if (this.internalField0245.isSelected() && EnchantmentUtils.internalMethod01710(localValue1, Enchantments.THORNS)) {
            return true;
         }

         if (this.internalField0244.isSelected() && EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PROTECTION) < 5) {
            return true;
         }

         if (this.internalField1075.isSelected() && localValue1.getMaxDamage() > 0) {
            double localValue4 = (double)(localValue1.getMaxDamage() - localValue1.getDamage()) / localValue1.getMaxDamage() * 100.0;
            if (localValue4 < this.internalField0383.internalMethod08576()) {
               return true;
            }
         }

         if (this.internalField1074.isSelected() && !EnchantmentUtils.internalMethod01710(localValue1, Enchantments.MENDING)) {
            return true;
         }
      }

      if (this.internalField0371 == AuctionModule.InternalType0466.internalField1136 && localValue3.getTranslationKey().contains("pickaxe")) {
         if (this.internalField1073.isSelected() && !EnchantmentUtils.internalMethod01710(localValue1, Enchantments.SILK_TOUCH)) {
            return true;
         }

         if (this.internalField1488.isSelected() && !EnchantmentUtils.internalMethod01710(localValue1, Enchantments.MENDING)) {
            return true;
         }

         if (this.internalField1490.isSelected() && !EnchantmentUtils.internalMethod03136(localValue2, "\u043c\u0430\u0433\u043d\u0438\u0442")) {
            return true;
         }

         if (this.internalField1072.isSelected()
            && !EnchantmentUtils.internalMethod03136(localValue2, "\u043c\u0435\u0433\u0430-\u0431\u0443\u043b\u044c\u0434\u043e\u0437\u0435\u0440")) {
            return true;
         }

         if (this.internalField1489.isSelected() && !EnchantmentUtils.internalMethod06801(localValue2, "\u043e\u043f\u044b\u0442\u043d\u044b\u0439", 3)) {
            return true;
         }

         if (this.internalField1491.isSelected() && !EnchantmentUtils.internalMethod06801(localValue2, "\u0431\u0443\u043b\u044c\u0434\u043e\u0437\u0435\u0440", 2)) {
            return true;
         }
      }

      if (this.internalField0371 == AuctionModule.InternalType0466.internalField1135
         && (localValue3 instanceof PotionItem || localValue3 instanceof SplashPotionItem || localValue3 instanceof LingeringPotionItem)) {
         List localValue7 = localValue2.stream().map(localValue0 -> localValue0.getString().toLowerCase(Locale.ROOT)).toList();
         boolean localValue5 = this.internalMethod07781(localValue7);
         if (!localValue5) {
            boolean localValue6 = this.internalMethod07266(localValue7);
            if (this.internalField1493.isSelected() && localValue6 && !this.internalMethod06890(localValue7)) {
               return true;
            }

            if (this.internalField1487.isSelected() && localValue6 && !this.internalMethod07663(localValue7)) {
               return true;
            }

            return this.internalField1486.isSelected() && this.internalMethod08677(localValue7);
         }
      }

      return false;
   }

   private boolean internalMethod07266(List<String> localValue1) {
      for (String localValue3 : localValue1) {
         String localValue4 = localValue3.toLowerCase(Locale.ROOT);
         if ((!localValue4.contains("\u0441\u0438\u043b\u0430") || localValue4.contains("\u0443\u0440\u043e\u043d"))
            && (!localValue4.contains("\u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c") || localValue4.contains("\u0430\u0442\u0430\u043a\u0438"))) {
            if (!localValue4.contains("strength") && (!localValue4.contains("speed") || localValue4.contains("attack"))) {
               continue;
            }

            return true;
         }

         return true;
      }

      return false;
   }

   private boolean internalMethod09223() {
      return this.internalField0371 == AuctionModule.InternalType0466.internalField0370
         || internalField0149.currentScreen == RockstarClient.getInstance().internalMethod04334();
   }

   private boolean internalMethod09225() {
      return this.internalField0371 == AuctionModule.InternalType0466.internalField1136
         || internalField0149.currentScreen == RockstarClient.getInstance().internalMethod04334();
   }

   private boolean internalMethod10110() {
      if (this.internalField0371 == AuctionModule.InternalType0466.internalField1135) {
         String localValue1 = this.internalField0247.toLowerCase(Locale.ROOT);
         return !this.internalMethod02833(localValue1, "\u0438\u043d\u0432\u0438\u0437", "\u043d\u0435\u0432\u0438\u0434", "invis");
      } else {
         return internalField0149.currentScreen == RockstarClient.getInstance().internalMethod04334();
      }
   }

   private boolean internalMethod02833(String localValue1, String... localValue2) {
      for (String localValue6 : localValue2) {
         if (localValue1.contains(localValue6)) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod06531(String localValue1) {
      if (!this.internalMethod08075(AuctionTooltipParser.internalMethod03300(localValue1))) {
         if (!this.internalMethod08075(AuctionSearchTracker.internalMethod04907().toLowerCase(Locale.ROOT))) {
            this.internalField0371 = AuctionModule.InternalType0466.internalField0371;
         }
      }
   }

   private boolean internalMethod08075(String localValue1) {
      if (localValue1.isEmpty()) {
         return false;
      } else if (this.internalMethod02833(
         localValue1,
         "\u0437\u0435\u043b\u044c",
         "\u0431\u0430\u0444",
         "\u0431\u0430\u0444\u0444",
         "\u0441\u0438\u043b\u0430",
         "\u0441\u0438\u043b\u044b",
         "\u0441\u0438\u043b\u043a\u0430",
         "\u0441\u0438\u043b\u044c\u043a\u0430",
         "\u0441\u043a\u043e\u0440\u043e\u0441\u0442",
         "\u0438\u043d\u0432\u0438\u0437",
         "\u0438\u043d\u0432\u0438\u0437\u043a",
         "\u043d\u0435\u0432\u0438\u0434"
      )) {
         this.internalField0371 = AuctionModule.InternalType0466.internalField1135;
         return true;
      } else if (this.internalMethod02833(
         localValue1,
         "\u0448\u043b\u0435\u043c",
         "\u043d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a",
         "\u043f\u043e\u043d\u043e\u0436",
         "\u0431\u043e\u0442\u0438\u043d",
         "\u0431\u0440\u043e\u043d"
      )) {
         this.internalField0371 = AuctionModule.InternalType0466.internalField0370;
         return true;
      } else if (this.internalMethod02833(
         localValue1,
         "\u043a\u0438\u0440\u043a",
         "\u043a\u0438\u0440\u043a\u0430",
         "\u043a\u0438\u0440\u043a\u0438",
         "\u043a\u0438\u0440\u043e\u043a",
         "\u0448\u0430\u0445\u0442",
         "\u0431\u0443\u0440"
      )) {
         this.internalField0371 = AuctionModule.InternalType0466.internalField1136;
         return true;
      } else {
         return false;
      }
   }

   private boolean internalMethod06890(List<String> localValue1) {
      int localValue2 = 0;

      for (String localValue4 : localValue1) {
         localValue4 = localValue4.toLowerCase(Locale.ROOT);
         if (++localValue2 <= 2) {
            if ((localValue4.contains("\u0441\u0438\u043b") || localValue4.contains("\u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c"))
               && !localValue4.contains("iii")
               && !localValue4.contains("\u0443\u0441\u0438\u043b\u0435\u043d\u043d")) {
               continue;
            }

            return true;
         }
         break;
      }

      return false;
   }

   private boolean internalMethod07781(List<String> localValue1) {
      for (String localValue3 : localValue1) {
         if (localValue3.contains("\u0438\u043d\u0432\u0438\u0437") || localValue3.contains("\u043d\u0435\u0432\u0438\u0434")) {
            return true;
         }
      }

      return false;
   }

   private boolean internalMethod07663(List<String> localValue1) {
      boolean localValue2 = localValue1.stream().anyMatch(localValue0 -> localValue0.contains("\u0441\u0438\u043b\u0430"));
      boolean localValue3 = localValue1.stream().anyMatch(localValue0 -> localValue0.contains("\u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c"));
      return localValue2 && localValue3;
   }

   private boolean internalMethod08677(List<String> localValue1) {
      for (String localValue3 : localValue1) {
         if (localValue3.contains("\u0441\u0438\u043b\u0430")) {
            int localValue4 = this.internalMethod06529(localValue3);
            if (localValue4 != -1 && localValue4 < 6) {
               return true;
            }
         }
      }

      return false;
   }

   private int internalMethod06529(String localValue1) {
      try {
         Pattern localValue2 = Pattern.compile("(\\d+):(\\d+)");
         Matcher localValue3 = localValue2.matcher(localValue1);
         if (localValue3.find()) {
            return Integer.parseInt(localValue3.group(1));
         }
      } catch (Exception localValue4) {
      }

      return -1;
   }

   private void internalMethod06999(HandledScreen<?> localValue1) {
      this.internalField0416.clear();
      this.internalField0193 = Double.MAX_VALUE;
      this.internalField0194 = 0.0;
      boolean localValue2 = this.internalMethod05075(this.internalField0247);
      this.internalField0227 = -1;
      this.internalField1043 = -1.0;
      double localValue3 = 0.0;
      int localValue5 = 0;
      int localValue6 = localValue1.getScreenHandler().slots.size() - 36;

      for (int localValue7 = 0; localValue7 < localValue6; localValue7++) {
         Slot localValue8 = localValue1.getScreenHandler().getSlot(localValue7);
         if (localValue8 != null && localValue8.hasStack()) {
            ItemStack localValue9 = localValue8.getStack();
            List localValue10 = localValue9.getTooltip(
               TooltipContext.create(internalField0149.world),
               internalField0149.player,
               internalField0149.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC
            );
            if (!this.internalMethod07438(localValue9, localValue10)) {
               long localValue11 = -1L;
               long localValue13 = -1L;
               long localValue15 = -1L;
               long localValue17 = -1L;
               if (localValue2) {
                  for (Text localValue20 : (Iterable<Text>)(Iterable<?>)localValue10) {
                     String localValue21 = AuctionTooltipParser.internalMethod04387(localValue20.getString());
                     String localValue22 = localValue21.toLowerCase(Locale.ROOT);
                     if (localValue22.contains("\u0431\u0438\u0440\u0436\u0430 \u0431\u0430\u043b\u0430\u043d\u0441")) {
                        localValue15 = this.internalMethod05074(localValue21);
                     } else if (localValue22.contains("\u043c\u043e\u043d\u0435\u0442")) {
                        localValue17 = this.internalMethod05074(localValue21);
                     } else if (localValue22.contains("\u043a\u0443\u0440\u0441")) {
                        localValue13 = this.internalMethod05074(localValue21);
                     }
                  }
               } else {
                  localValue11 = AuctionTooltipParser.internalMethod02982(localValue10);
               }

               if (localValue15 != -1L) {
                  this.internalField1058 = localValue15;
               }

               if (localValue17 != -1L) {
                  this.internalField1060 = localValue17;
               }

               Double localValue29 = null;
               long localValue30 = -1L;
               if (localValue2) {
                  if (localValue13 > 0L) {
                     localValue29 = (double)(-localValue13);
                     localValue30 = localValue13;
                     if (this.internalField1058 != -1L && this.internalField1058 < localValue13) {
                        continue;
                     }

                     if (localValue13 > this.internalField1043) {
                        this.internalField1043 = localValue13;
                        this.internalField0227 = localValue8.id;
                     }
                  }
               } else if (localValue11 > 0L) {
                  int localValue31 = Math.max(1, localValue9.getCount());
                  int localValue23 = localValue9.getMaxDamage();
                  int localValue24 = localValue23 - localValue9.getDamage();
                  double localValue25 = this.internalField0669.internalMethod06103(this.internalField1066) ? (double)localValue11 / localValue31 : localValue11;
                  double localValue27 = 1.0;
                  if (localValue23 > 0) {
                     localValue27 = (double)localValue24 / localValue23;
                     localValue27 = Math.max(0.1, localValue27);
                  }

                  localValue29 = localValue25 / localValue27;
                  localValue30 = localValue11;
               }

               if (localValue29 != null) {
                  int localValue32 = localValue9.getCount();
                  int localValue33 = localValue9.getMaxDamage();
                  int localValue34 = localValue33 - localValue9.getDamage();
                  this.internalField0416.add(new AuctionModule.InternalType0467(localValue8.id, localValue30, localValue32, localValue33, localValue34, localValue29));
                  localValue3 += localValue29;
                  localValue5++;
                  if (localValue29 < this.internalField0193) {
                     this.internalField0193 = localValue29;
                  }
               }
            }
         }
      }

      if (localValue5 > 0) {
         this.internalField0194 = localValue3 / localValue5;
      } else {
         this.internalMethod10115();
      }
   }

   private void internalMethod10115() {
      this.internalField0416.clear();
      this.internalField0194 = 0.0;
      this.internalField0193 = Double.MAX_VALUE;
      this.internalField0371 = AuctionModule.InternalType0466.internalField0371;
      this.internalField0227 = -1;
      this.internalField1043 = -1.0;
      this.internalField1058 = -1L;
      this.internalField1060 = -1L;
   }

   private List<AuctionModule.InternalType0045> internalMethod05131(ScreenHandler localValue1) {
      ArrayList localValue2 = new ArrayList();
      int localValue3 = Math.max(0, localValue1.slots.size() - 36);

      for (int localValue4 = 0; localValue4 < localValue3; localValue4++) {
         Slot localValue5 = (Slot)localValue1.slots.get(localValue4);
         if (localValue5 != null && localValue5.hasStack()) {
            ItemStack localValue6 = localValue5.getStack();
            if (localValue6.getItem() == this.internalField0152) {
               int localValue7 = Math.max(1, localValue6.getCount());
               double localValue8 = this.internalMethod06996(localValue6, localValue7);
               if (!(localValue8 <= 0.0)) {
                  localValue2.add(new AuctionModule.InternalType0045(Math.round(localValue8 * localValue7), localValue7, localValue8));
               }
            }
         }
      }

      return localValue2;
   }

   private AuctionModule.InternalType0046 internalMethod03745(List<AuctionModule.InternalType0045> localValue1) {
      if (localValue1.isEmpty()) {
         return new AuctionModule.InternalType0046(0, 0, 0L, 0);
      } else {
         List localValue2 = localValue1.stream().sorted(Comparator.comparingDouble(AuctionModule.InternalType0045::internalMethod03052)).toList();
         double localValue3 = ((AuctionModule.InternalType0045)localValue2.getFirst()).internalField0194;
         double localValue5 = localValue3 * 1.6;
         long localValue7 = 0L;
         int localValue9 = 0;
         int localValue10 = 0;

         for (AuctionModule.InternalType0045 localValue12 : (Iterable<AuctionModule.InternalType0045>)(Iterable<?>)localValue2) {
            if (localValue12.internalField0194 > localValue5 && localValue10 > 0) {
               break;
            }

            localValue7 += localValue12.internalField0229;
            localValue9 += localValue12.internalField0227;
            localValue10++;
         }

         if (localValue10 == 1 && localValue2.size() > 1) {
            AuctionModule.InternalType0045 localValue13 = (AuctionModule.InternalType0045)localValue2.get(1);
            localValue7 += localValue13.internalField0229;
            localValue9 += localValue13.internalField0227;
            localValue10++;
         }

         return new AuctionModule.InternalType0046(localValue10, localValue9, localValue7, localValue1.size() - localValue10);
      }
   }

   private double internalMethod06996(ItemStack localValue1, int localValue2) {
      if (this.internalField0668.internalMethod06103(this.internalField0238)) {
         double localValue3 = this.internalMethod02162(localValue1);
         if (localValue3 > 0.0) {
            return localValue3;
         }
      }

      for (Text localValue4 : this.internalMethod00900(localValue1)) {
         String localValue5 = localValue4.getString();
         if (localValue5.contains("$") && !localValue5.contains("%")) {
            long localValue6 = this.internalMethod05074(localValue5);
            if (localValue6 > 0L) {
               return (double)localValue6 / localValue2;
            }
         }
      }

      return -1.0;
   }

   private double internalMethod02162(ItemStack localValue1) {
      for (Text localValue3 : this.internalMethod00900(localValue1)) {
         String localValue4 = localValue3.getString();
         String localValue5 = localValue4.toLowerCase(Locale.ROOT);
         if (localValue5.contains("\u0446\u0435\u043d\u0430 \u0437\u0430 1") || localValue5.contains("price per 1")) {
            long localValue6 = this.internalMethod06530(localValue4);
            if (localValue6 > 0L) {
               return localValue6;
            }
         }
      }

      return -1.0;
   }

   private int internalMethod03906(ScreenHandler localValue1) {
      int localValue2 = Math.max(0, localValue1.slots.size() - 36);

      for (int localValue3 = 0; localValue3 < localValue2; localValue3++) {
         Slot localValue4 = (Slot)localValue1.slots.get(localValue3);
         if (localValue4 != null && localValue4.hasStack()) {
            ItemStack localValue5 = localValue4.getStack();
            if (this.internalMethod07341(localValue5)) {
               return localValue3;
            }
         }
      }

      return -1;
   }

   private AuctionModule.InternalType0044 internalMethod03153(ScreenHandler localValue1) {
      if (internalField0149.currentScreen != null) {
         AuctionModule.InternalType0044 localValue2 = this.internalMethod04701(internalField0149.currentScreen.getTitle().getString());
         if (localValue2 != null) {
            return localValue2;
         }
      }

      int localValue6 = Math.max(0, localValue1.slots.size() - 36);

      for (int localValue3 = 0; localValue3 < localValue6; localValue3++) {
         Slot localValue4 = (Slot)localValue1.slots.get(localValue3);
         if (localValue4 != null && localValue4.hasStack()) {
            AuctionModule.InternalType0044 localValue5 = this.internalMethod06789(localValue4.getStack());
            if (localValue5 != null) {
               return localValue5;
            }
         }
      }

      return null;
   }

   private AuctionModule.InternalType0044 internalMethod06789(ItemStack localValue1) {
      for (Text localValue3 : this.internalMethod00900(localValue1)) {
         AuctionModule.InternalType0044 localValue4 = this.internalMethod04701(localValue3.getString());
         if (localValue4 != null) {
            return localValue4;
         }
      }

      return null;
   }

   private AuctionModule.InternalType0044 internalMethod04701(String localValue1) {
      Matcher localValue2 = internalField0293.matcher(localValue1);
      if (!localValue2.find()) {
         return null;
      } else {
         try {
            return new AuctionModule.InternalType0044(Integer.parseInt(localValue2.group(1)), Integer.parseInt(localValue2.group(2)));
         } catch (NumberFormatException localValue4) {
            return null;
         }
      }
   }

   private boolean internalMethod07341(ItemStack localValue1) {
      String localValue2 = localValue1.getName().getString().toLowerCase(Locale.ROOT);

      for (Text localValue4 : this.internalMethod00900(localValue1)) {
         localValue2 = localValue2 + " " + localValue4.getString().toLowerCase(Locale.ROOT);
      }

      return localValue2.contains("\u0441\u043b\u0435\u0434")
         || localValue2.contains("\u0434\u0430\u043b\u0435\u0435")
         || localValue2.contains("\u0432\u043f\u0435\u0440")
         || localValue2.contains("next");
   }

   private List<Text> internalMethod00900(ItemStack localValue1) {
      return localValue1.getTooltip(
         TooltipContext.create(internalField0149.world),
         internalField0149.player,
         internalField0149.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC
      );
   }

   private long internalMethod06530(String localValue1) {
      int localValue2 = localValue1.lastIndexOf(58);
      return this.internalMethod05074(localValue2 == -1 ? localValue1 : localValue1.substring(localValue2 + 1));
   }

   private int internalMethod09876() {
      try {
         String localValue1 = this.internalField0384.internalMethod08926();
         if (localValue1 != null && !localValue1.isBlank()) {
            String localValue2 = localValue1.replaceAll("[^\\d]", "");
            return localValue2.isEmpty() ? 1 : Math.max(1, Integer.parseInt(localValue2));
         } else {
            return 1;
         }
      } catch (NumberFormatException localValue3) {
         return 1;
      }
   }

   private ScreenHandler internalMethod06493() {
      if (!(internalField0149.currentScreen instanceof HandledScreen)) {
         return null;
      } else if (internalField0149.player == null) {
         return null;
      } else {
         ScreenHandler localValue1 = internalField0149.player.currentScreenHandler;
         return localValue1 == internalField0149.player.playerScreenHandler ? null : localValue1;
      }
   }

   private void internalMethod09941() {
      if (internalField0149.player != null) {
         if (internalField0149.player.currentScreenHandler != null
            && internalField0149.player.currentScreenHandler != internalField0149.player.playerScreenHandler) {
            internalField0149.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(internalField0149.player.currentScreenHandler.syncId));
         }

         if (internalField0149.currentScreen instanceof HandledScreen) {
            internalField0149.player.closeHandledScreen();
         }
      }
   }

   private String internalMethod02614(long localValue1) {
      return String.format(Locale.US, "%,d", localValue1);
   }

   private long internalMethod05074(String localValue1) {
      return AuctionTooltipParser.internalMethod02205(localValue1);
   }

   @Generated
   public List<AuctionModule.InternalType0467> internalMethod06171() {
      return this.internalField0416;
   }

   @Generated
   public double internalMethod01866() {
      return this.internalField0194;
   }

   @Generated
   public double internalMethod01868() {
      return this.internalField0193;
   }

   @Generated
   public String internalMethod07984() {
      return this.internalField0247;
   }

   @Generated
   public int internalMethod08816() {
      return this.internalField0227;
   }

   @Generated
   public double internalMethod08815() {
      return this.internalField1043;
   }

   @Generated
   public long internalMethod01867() {
      return this.internalField1058;
   }

   @Generated
   public long internalMethod01869() {
      return this.internalField1060;
   }

   @Generated
   public ColorSetting internalMethod07292() {
      return this.internalField0665;
   }

   @Generated
   public ColorSetting internalMethod00734() {
      return this.internalField0664;
   }

   @Generated
   public BooleanSetting internalMethod06436() {
      return this.internalField0650;
   }

   @Generated
   public BooleanSetting internalMethod00675() {
      return this.internalField0651;
   }

   @Generated
   public KeybindSetting internalMethod06434() {
      return this.internalField0648;
   }

   @Generated
   public BooleanSetting internalMethod08116() {
      return this.internalField1261;
   }

   @Generated
   public BooleanSetting internalMethod08250() {
      return this.internalField1263;
   }

   @Generated
   public ModeSetting internalMethod07334() {
      return this.internalField0668;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod04751() {
      return this.internalField0237;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod04959() {
      return this.internalField0238;
   }

   @Generated
   public TextSetting internalMethod05839() {
      return this.internalField0384;
   }

   @Generated
   public KeybindSetting internalMethod00674() {
      return this.internalField0647;
   }

   @Generated
   public ModeSetting internalMethod00740() {
      return this.internalField0669;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod08947() {
      return this.internalField1066;
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod08984() {
      return this.internalField1067;
   }

   @Generated
   public MultiSelectSetting internalMethod07335() {
      return this.internalField0675;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod04815() {
      return this.internalField0245;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod05015() {
      return this.internalField0244;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08959() {
      return this.internalField1075;
   }

   @Generated
   public SliderSetting internalMethod05838() {
      return this.internalField0383;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08993() {
      return this.internalField1074;
   }

   @Generated
   public MultiSelectSetting internalMethod00741() {
      return this.internalField0674;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08529() {
      return this.internalField1073;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08557() {
      return this.internalField1072;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09877() {
      return this.internalField1491;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09907() {
      return this.internalField1488;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09636() {
      return this.internalField1490;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09643() {
      return this.internalField1489;
   }

   @Generated
   public MultiSelectSetting internalMethod08128() {
      return this.internalField1276;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09581() {
      return this.internalField1493;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09596() {
      return this.internalField1487;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod09340() {
      return this.internalField1486;
   }

   @Generated
   public boolean internalMethod09879() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod09881() {
      return this.internalField0276;
   }

   @Generated
   public Stopwatch internalMethod07019() {
      return this.internalField0519;
   }

   @Generated
   public Stopwatch internalMethod00655() {
      return this.internalField0518;
   }

   @Generated
   public Stopwatch internalMethod07916() {
      return this.internalField1189;
   }

   @Generated
   public List<AuctionModule.InternalType0045> internalMethod05384() {
      return this.internalField0417;
   }

   @Generated
   public AuctionModule.InternalType0043 internalMethod04405() {
      return this.internalField0538;
   }

   @Generated
   public Item internalMethod01630() {
      return this.internalField0152;
   }

   @Generated
   public ItemStack internalMethod06951() {
      return this.internalField0878;
   }

   @Generated
   public String internalMethod08863() {
      return this.internalField1077;
   }

   @Generated
   public int internalMethod08817() {
      return this.internalField0228;
   }

   @Generated
   public int internalMethod08830() {
      return this.internalField1053;
   }

   @Generated
   public int internalMethod08831() {
      return this.internalField1055;
   }

   @Generated
   public HudContextMenu internalMethod07622() {
      return this.internalField0574;
   }

   @Generated
   public boolean internalMethod09217() {
      return this.internalField1099;
   }

   @Generated
   public AuctionModule.InternalType0466 internalMethod05755() {
      return this.internalField0371;
   }

   @Generated
   public EventListener<KeyPressEvent> internalMethod05050() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod06301() {
      return this.internalField0158;
   }

   @Generated
   public EventListener<HudRenderEvent> internalMethod07824() {
      return this.internalField1028;
   }

   @Generated
   public EventListener<ScreenRenderEvent> internalMethod08063() {
      return this.internalField1029;
   }

   @Generated
   public EventListener<ContainerClickEvent> internalMethod07946() {
      return this.internalField1030;
   }

   @Generated
   public EventListener<ContainerReleaseEvent> internalMethod08199() {
      return this.internalField1027;
   }

   static enum InternalType0043 {
      internalField0538,
      internalField0537,
      internalField1194,
      internalField1193;
   }

   static final class InternalType0044 {
      final int internalField0227;
      final int internalField0228;

      InternalType0044(int localValue1, int localValue2) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0044[current=" + this.internalField0227 + ", total=" + this.internalField0228 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionModule.InternalType0044 other = (AuctionModule.InternalType0044) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228);
      }

      public int internalMethod04832() {
         return this.internalField0227;
      }

      public int internalMethod04860() {
         return this.internalField0228;
      }
   }

   static final class InternalType0045 {
      final long internalField0229;
      final int internalField0227;
      final double internalField0194;

      InternalType0045(long localValue1, int localValue3, double localValue4) {
         this.internalField0229 = localValue1;
         this.internalField0227 = localValue3;
         this.internalField0194 = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0045[totalPrice=" + this.internalField0229 + ", count=" + this.internalField0227 + ", unitPrice=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionModule.InternalType0045 other = (AuctionModule.InternalType0045) localValue1;
         return java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public long internalMethod03054() {
         return this.internalField0229;
      }

      public int internalMethod03053() {
         return this.internalField0227;
      }

      public double internalMethod03052() {
         return this.internalField0194;
      }
   }

   static final class InternalType0046 {
      private final int internalField0227;
      final int internalField0228;
      private final long internalField0229;
      private final int internalField1053;

      InternalType0046(int localValue1, int localValue2, long localValue3, int localValue5) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField0229 = localValue3;
         this.internalField1053 = localValue5;
      }

      double internalMethod04921() {
         return this.internalField0228 <= 0 ? 0.0 : (double)this.internalField0229 / this.internalField0228;
      }

      @Override
      public final String toString() {
         return "InternalType0046[lots=" + this.internalField0227 + ", units=" + this.internalField0228 + ", totalPrice=" + this.internalField0229 + ", skippedLots=" + this.internalField1053 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionModule.InternalType0046 other = (AuctionModule.InternalType0046) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053);
      }

      public int internalMethod04922() {
         return this.internalField0227;
      }

      public int internalMethod04924() {
         return this.internalField0228;
      }

      public long internalMethod04923() {
         return this.internalField0229;
      }

      public int internalMethod07973() {
         return this.internalField1053;
      }
   }

   static enum InternalType0466 {
      internalField0371,
      internalField0370,
      internalField1136,
      internalField1135;
   }

   public static final class InternalType0467 {
      final int internalField0227;
      private final long internalField0229;
      private final int internalField0228;
      private final int internalField1053;
      private final int internalField1055;
      final double internalField0194;

      public InternalType0467(int localValue1, long localValue2, int localValue4, int localValue5, int localValue6, double localValue7) {
         this.internalField0227 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0228 = localValue4;
         this.internalField1053 = localValue5;
         this.internalField1055 = localValue6;
         this.internalField0194 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0467[slotId=" + this.internalField0227 + ", totalPrice=" + this.internalField0229 + ", count=" + this.internalField0228 + ", maxDurability=" + this.internalField1053 + ", currentDurability=" + this.internalField1055 + ", effectivePrice=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionModule.InternalType0467 other = (AuctionModule.InternalType0467) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public int internalMethod00101() {
         return this.internalField0227;
      }

      public long internalMethod00102() {
         return this.internalField0229;
      }

      public int internalMethod00103() {
         return this.internalField0228;
      }

      public int internalMethod08026() {
         return this.internalField1053;
      }

      public int internalMethod08027() {
         return this.internalField1055;
      }

      public double internalMethod00100() {
         return this.internalField0194;
      }
   }
}
