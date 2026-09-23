package globals.client.ui;











import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.compat.RenderSystem;
import globals.client.Activities;
import globals.client.Chat;
import globals.client.Cosmetics;
import globals.client.Information;
import globals.client.Mentions;
import globals.client.RocknetListener;
import globals.client.WorldKey;
import globals.client.messages.CordsMessage;
import globals.client.messages.Message;
import globals.client.messages.ReplyMessage;
import globals.client.messages.ShareMessage;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.Insets;
import rockstar.modules.other.GlobalsMenuModule;
import rockstar.modules.other.InventoryBuilderModule;
import rockstar.modules.other.SoundsModule;
import rockstar.modules.player.GuiMoveModule;
import rockstar.client.ui.LayoutAlignment;
import rockstar.client.render.SizedFont;
import rockstar.modules.visual.MenuModule;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.NumberWheelPicker;
import rockstar.client.ui.TextAlignment;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.core.RelativeTimeFormatter;
import rockstar.client.internal.core.ScrollPolicy;
import rockstar.client.internal.script.EmojiTextNode;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.AbstractMenuScreen;
import rockstar.client.ui.UiTransition;
import rockstar.client.ui.UiElement;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.internal.game.SmoothScrollHandler;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.DragConstraint;
import rockstar.client.render.RenderPipeline;
import rockstar.client.render.ScissorStack;
import rockstar.client.render.UiBatchRenderer;
import rockstar.client.internal.config.SwingPresetConfig;
import rockstar.client.internal.config.SwingPresetManager;
import rockstar.client.ui.UiNode;
import rockstar.client.internal.core.ClientSoundEvents;
import rockstar.client.ui.UiContainer;

public class RocknetMenu extends AbstractMenuScreen implements MinecraftClientAccess {
   private static final Easing CIRC_OUT = Easing.internalMethod05127(0.0, 0.55, 0.45, 1.0);
   private static final Motion STATE = Motion.internalMethod01328(150L, Easing.internalField1626);
   private static final Motion LIST = Motion.internalMethod07185(420.0F, 34.0F);
   private static final Motion MOVE = Motion.internalMethod01328(260L, CIRC_OUT);
   private static final Motion MENU_HOVER = Motion.internalMethod01328(120L, Easing.internalField1626);
   private static final Motion MENU_MOTION = Motion.internalMethod01328(160L, Easing.internalMethod05127(0.0, 0.0, 0.58, 1.0));
   private static final UiTransition MENU_OPEN = (localValue0, localValue1, localValue2) -> {
      localValue2.internalField0205 = localValue0;
      localValue2.internalField1047 = 0.97F + 0.03F * localValue0;
      localValue2.internalField1048 = (1.0F - localValue2.internalField1047) * localValue1.h() / 2.0F - (1.0F - localValue0) * 3.0F;
   };
   private static final ColorRGBA DANGER = new ColorRGBA(214.0F, 102.0F, 102.0F);
   private static final SimpleDateFormat DATE = new SimpleDateFormat("dd.MM.yyyy");
   private static final SimpleDateFormat TIME = new SimpleDateFormat("dd.MM HH:mm");
   private static final String GLOBAL_CHAT_ID = Information.GLOBAL_CHAT;
   private final Rect menuWindow = new Rect(0.0F, 0.0F, 500.0F, 320.0F);
   private final CustomTextField sendField;
   private final CustomTextField friendField;
   private final CustomTextField searchField;
   private final CustomTextField peopleField;
   private final AuthForm form = new AuthForm();
   private UiContainer activeMenu;
   private boolean peopleOpen;
   private UiContainer peopleList;
   private final Map<String, UiContainer> personRows = new HashMap<>();
   private List<String> peopleSnapshot = List.of();
   private String muteTarget;
   private int muteDays;
   private int muteHours;
   private int muteMinutes = 30;
   private String pendingQuery = "";
   private long queryChangedAt;
   private RocknetMenu.InternalType0382 tab = RocknetMenu.InternalType0382.CHATS;
   Chat currentChat = Information.chats().getFirst();
   private long reply = -1L;
   private long lastReply = -1L;
   private List<String> mentionOptions = List.of();
   private List<String> mentionShown = List.of();
   private int mentionIndex;
   private String mentionDismissed;
   private String mentionPending;
   private final AnimatedValue mentionAppear = new AnimatedValue(160L, Easing.internalField1626);
   private UiContainer window;
   private UiContainer sidebarList;
   private UiElement messagesBox;
   private final Map<String, UiContainer> userRows = new HashMap<>();
   private final Map<String, UiContainer> requestRows = new HashMap<>();
   private final Map<String, UiElement> placeholders = new HashMap<>();
   private UiContainer globalRow;
   private List<String> sidebarSnapshot = List.of();
   private long sidebarCheckedAt;
   private long peopleCheckedAt;
   private List<String> dialogs = List.of();
   private float windowX = Float.NaN;
   private float windowY = Float.NaN;
   private long openStart;
   private float lastMouseX;
   private float lastMouseY;

   public RocknetMenu() {
      this.sendField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0F));
      this.sendField.internalMethod09000(LanguageManager.internalMethod07214("message") + "...");
      this.friendField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0F));
      this.friendField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.menu.friend_placeholder"));
      this.searchField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0F));
      this.searchField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.menu.search_placeholder"));
      this.peopleField = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0F));
      this.peopleField.internalMethod09000(LanguageManager.internalMethod07214("rocknet.people.search"));
      this.setReply(-1L);
   }

   @Override
   public void init() {
      super.init();
      this.closing = false;
      this.contentAlpha = 1.0F;
      this.openStart = System.currentTimeMillis();
      this.clearRoots();
      this.overlays.clear();
      this.activeMenu = null;
      this.userRows.clear();
      this.requestRows.clear();
      this.placeholders.clear();
      this.sidebarSnapshot = List.of();
      this.personRows.clear();
      this.peopleSnapshot = List.of();
      this.mentionOptions = List.of();
      this.mentionShown = List.of();
      this.mentionDismissed = null;
      this.mentionPending = null;
      this.mentionAppear.internalMethod07060(0.0F);
      this.peopleOpen = false;
      this.muteTarget = null;
      Information.closeProfile();
      if (Float.isNaN(this.windowX)) {
         this.windowX = Math.round((this.width - 500.0F) / 2.0F);
         this.windowY = Math.round((this.height - 320.0F) / 2.0F);
      }

      this.windowX = Math.max(-460.0F, Math.min(this.width - 40.0F, this.windowX));
      this.windowY = Math.max(0.0F, Math.min(Math.max(0.0F, this.height - 40.0F), this.windowY));
      this.build();
      this.rebuildSidebar(true);
   }

   private void build() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod05895()
         .internalMethod03062(7.0F)
         .internalMethod09213()
         .internalMethod03855(() -> Information.getUser() != null)
         .internalMethod03907(this.buildSidebar())
         .internalMethod03907(this.buildContent());
      UiContainer localValue2 = new UiContainer().internalMethod09213().internalMethod03855(() -> Information.getUser() == null).internalMethod07178((localValue1x, localValue2x) -> {
         float localValue3 = 160.0F;
         float localValue4 = this.form.isLogin() ? 109.0F : 132.0F;
         this.form.set(localValue2x.x() + localValue2x.w() / 2.0F - localValue3 / 2.0F, localValue2x.y() + localValue2x.h() / 2.0F - localValue4 / 2.0F, localValue3, localValue4);
         this.form.render(localValue1x);
      });
      this.window = new UiContainer()
         .internalMethod08791()
         .internalMethod07351(7.0F)
         .internalMethod03995(500.0F, 320.0F)
         .internalMethod03754(Motion.internalField1384)
         .internalMethod03715(DragConstraint.internalField0631)
         .internalMethod07178((localValue0, localValue1x) -> {
            localValue0.drawShadow(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 26.0F, CornerRadii.internalMethod03908(12.0F), ColorRGBA.BLACK.withAlpha(45.9F));
            localValue0.drawRoundedRect(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), CornerRadii.internalMethod03908(12.0F), page());
         })
         .internalMethod03907(localValue1)
         .internalMethod03907(localValue2)
         .internalMethod03907(this.buildProfileCard());
      this.window.internalMethod09801();
      this.window.snapSize();
      this.window.snapAt(this.windowX, this.windowY);
      applyMotion(this.window);
      this.add(this.window);
   }

   private static void applyMotion(UiNode localValue0) {
      localValue0.motion(MOVE);
      if (localValue0 instanceof UiContainer localValue1) {
         localValue1.internalMethod01401().forEach(RocknetMenu::applyMotion);
      }
   }

   private UiContainer buildSidebar() {
      this.sidebarList = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod03514(Insets.internalMethod00105(5.0F, 4.0F, 5.0F, 4.0F))
         .internalMethod09339(138.0F)
         .internalMethod09186()
         .internalMethod08163(120.0F)
         .internalMethod08755()
         .internalMethod01416(ScrollPolicy.internalField0916)
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(-2.0F)
               .internalMethod09005(3.0F)
               .internalMethod00894(2.5F)
               .internalMethod08056(16.0F)
               .internalMethod07954(1.25F)
               .internalMethod08313(1000.0F)
               .internalMethod04404(localValue0x -> stroke().mix(second(), localValue0x.internalMethod05170() + localValue0x.internalMethod05173()))
         );
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod09339(138.0F)
         .internalMethod09186()
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> card())
         .internalMethod03907(this.sidebarList);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod03062(7.0F)
         .internalMethod09339(138.0F)
         .internalMethod09186()
         .internalMethod03907(this.buildTabs())
         .internalMethod03907(this.buildSearchRow())
         .internalMethod03907(localValue1)
         .internalMethod03907(this.buildSelfCard());
   }

   private UiContainer buildTabs() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod05895()
         .internalMethod03062(2.0F)
         .internalMethod07351(2.0F)
         .internalMethod09266(18.0F)
         .internalMethod09609()
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> card());

      for (RocknetMenu.InternalType0382 localValue5 : RocknetMenu.InternalType0382.values()) {
         localValue1.internalMethod03907(
            new UiElement()
               .fillWidth()
               .fillHeight()
               .radius(5.0F)
               .text(
                  Fonts.internalField0449.internalMethod01432(7.0F),
                  () -> this.tabTitle(localValue5),
                  localValue0 -> second().mix(text(), localValue0.hover()).mix(onAccent(), localValue0.sig("active"))
               )
               .textAlign(TextAlignment.internalField0621)
               .background(localValue0 -> accent().mulAlpha(localValue0.sig("active")))
               .bind("active", () -> this.tab == localValue5, STATE)
               .hoverMotion(STATE)
               .cursor(CursorType.internalField0567)
               .onClick(() -> this.selectTab(localValue5))
         );
      }

      return localValue1;
   }

   private String tabTitle(RocknetMenu.InternalType0382 localValue1) {
      return switch (localValue1) {
         case CHATS -> LanguageManager.internalMethod07214("rocknet.menu.chats");
         case FRIENDS -> Information.getRequests().isEmpty()
            ? LanguageManager.internalMethod07214("rocknet.menu.tab_friends")
            : LanguageManager.internalMethod07214("rocknet.menu.tab_friends") + " " + Information.getRequests().size();
      };
   }

   private void selectTab(RocknetMenu.InternalType0382 localValue1) {
      if (this.tab != localValue1) {
         this.tab = localValue1;
         if (this.sidebarList != null) {
            this.sidebarList.internalMethod03631();
         }

         this.rebuildSidebar(true);
      }
   }

   private UiContainer buildSelfCard() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(3.0F)
         .internalMethod09609()
         .internalMethod03907(
            this.nickBox(
               Fonts.internalField0449.internalMethod01432(9.0F),
               9.0F,
               () -> Information.getUser() == null ? "" : Information.getUser().username(),
               Cosmetics::selfNick,
               Cosmetics::selfBadge,
               localValue0 -> text()
            )
         )
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(
                  Fonts.internalField1154.internalMethod01432(7.0F),
                  () -> Activities.translate(RockstarClient.getInstance().internalMethod06050().getActivity()),
                  localValue0 -> second()
               )
               .interactive(false)
         );
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(32.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 7.0F))
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> card())
          .internalMethod03715(DragConstraint.internalField0631)
          .internalMethod03907(localValue1);
   }

   private UiContainer buildSearchRow() {
      UiElement localValue1 = new UiElement()
         .height(18.0F)
         .fillWidth()
         .radius(7.0F)
         .background(localValue0 -> card())
         .cursor(CursorType.internalField1206)
         .onClick(() -> this.activeField().internalMethod07508(true))
         .paint(
            (localValue1x, localValue2x) -> {
               float localValue3 = (localValue2x.h() - Fonts.internalField1154.internalMethod01432(8.0F).internalMethod04890()) / 2.0F;
               localValue1x.drawIcon(
                  this.tab == RocknetMenu.InternalType0382.FRIENDS ? "plus" : "search", localValue2x.x() + localValue3, localValue2x.y() + localValue2x.h() / 2.0F - 3.0F, 6.0F, second()
               );
               CustomTextField localValue4 = this.activeField();
               (localValue4 == this.friendField ? this.searchField : this.friendField).internalMethod05191(0.0F, 0.0F, 0.0F, 0.0F);
               localValue4.internalMethod05191(localValue2x.x() + 10.0F, localValue2x.y(), localValue2x.w() - 14.0F, localValue2x.h());
               localValue4.internalMethod00143(text());
               localValue4.internalMethod03398(localValue1x);
            }
         );
      UiElement localValue2 = this.iconButton("plus", 18.0F, 7.0F, this::sendFriendRequest);
      localValue2.visibleWhen(() -> this.tab == RocknetMenu.InternalType0382.FRIENDS);
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(5.0F)
         .internalMethod09266(18.0F)
         .internalMethod09609()
         .internalMethod03907(localValue1)
         .internalMethod03907(localValue2);
   }

   private CustomTextField activeField() {
      return this.tab == RocknetMenu.InternalType0382.FRIENDS ? this.friendField : this.searchField;
   }

   private void rebuildSidebar(boolean localValue1) {
      if (this.sidebarList != null && Information.getUser() != null) {
         long localValue2 = System.currentTimeMillis();
         if (localValue1 || localValue2 - this.sidebarCheckedAt >= 200L) {
            this.sidebarCheckedAt = localValue2;
            String localValue4 = this.searchField.internalMethod06202().trim().toLowerCase();
            ArrayList localValue5 = new ArrayList();
            localValue5.add(this.tab.name() + ":" + localValue4);
            List localValue6 = List.of();
            List localValue7 = List.of();
            if (this.tab == RocknetMenu.InternalType0382.CHATS) {
               this.dialogs = this.openDialogs();

               for (String localValue9 : this.dialogs) {
                  localValue5.add("c:" + localValue9);
               }
            } else {
               localValue6 = Information.getRequests();
               localValue7 = Information.getSortedFriends();

               for (String localValue14 : (Iterable<String>)(Iterable<?>)localValue6) {
                  localValue5.add("r:" + localValue14);
               }

               for (Packets.InternalType0018 localValue15 : (Iterable<Packets.InternalType0018>)(Iterable<?>)localValue7) {
                  localValue5.add("f:" + localValue15.username());
               }
            }

            if (localValue1 || !localValue5.equals(this.sidebarSnapshot)) {
               this.sidebarSnapshot = localValue5;
               ArrayList localValue13 = new ArrayList();
               switch (this.tab) {
                  case CHATS:
                     if (this.globalRow == null) {
                        this.globalRow = this.buildGlobalRow();
                     }

                     if (localValue4.isEmpty() || LanguageManager.internalMethod07214("rocknet.chat.global.name").toLowerCase().contains(localValue4)) {
                        localValue13.add(this.globalRow);
                     }

                     for (String localValue20 : this.dialogs) {
                        if (localValue4.isEmpty() || localValue20.toLowerCase().contains(localValue4)) {
                           localValue13.add(this.userRows.computeIfAbsent(localValue20, this::buildUserRow));
                        }
                     }

                     if (localValue13.isEmpty()) {
                        localValue13.add(this.placeholder("rocknet.menu.nothing_found"));
                     }
                     break;
                  case FRIENDS:
                     for (String localValue10 : (Iterable<String>)(Iterable<?>)localValue6) {
                        localValue13.add(this.requestRows.computeIfAbsent(localValue10, this::buildRequestRow));
                     }

                     for (Packets.InternalType0018 localValue19 : (Iterable<Packets.InternalType0018>)(Iterable<?>)localValue7) {
                        localValue13.add(this.userRows.computeIfAbsent(localValue19.username(), this::buildUserRow));
                     }

                     if (localValue13.isEmpty()) {
                        localValue13.add(this.placeholder("rocknet.menu.no_friends"));
                     }
               }

               this.sidebarList.internalMethod07849(localValue13);
            }
         }
      }
   }

   private List<String> openDialogs() {
      List localValue1 = Information.dialogs();
      String localValue2 = this.currentChat.getName();
      if (!localValue2.equals(GLOBAL_CHAT_ID) && !localValue1.contains(localValue2)) {
         localValue1.add(0, localValue2);
      }

      return localValue1;
   }

   private void syncPeers() {
      String localValue1 = this.currentChat.getName();
      if (!localValue1.equals(GLOBAL_CHAT_ID) && this.friend(localValue1) == null) {
         Information.refreshPeer(localValue1);
      }

      if (this.tab == RocknetMenu.InternalType0382.CHATS) {
         for (String localValue3 : this.dialogs) {
            if (this.friend(localValue3) == null) {
               Information.refreshPeer(localValue3);
            }
         }
      }
   }

   private UiElement placeholder(String localValue1) {
      return this.placeholders
         .computeIfAbsent(
            localValue1,
            localValue0 -> new UiElement()
               .height(28.0F)
               .fillWidth()
               .text(Fonts.internalField1154.internalMethod01432(7.0F), () -> LanguageManager.internalMethod07214(localValue0), localValue0x -> second())
               .textAlign(TextAlignment.internalField0621)
               .motion(MOVE)
               .interactive(false)
         );
   }

   private UiContainer buildGlobalRow() {
      return this.chatRow(
         () -> LanguageManager.internalMethod07214("rocknet.chat.global.name"),
         () -> null,
         () -> null,
         this::globalPreview,
         () -> this.currentChat.getName().equals(GLOBAL_CHAT_ID),
         localValue1 -> this.openChat(GLOBAL_CHAT_ID)
      );
   }

   private UiContainer buildUserRow(String localValue1) {
      return this.chatRow(
         () -> localValue1,
         () -> this.nickStyle(localValue1),
         () -> this.badge(localValue1),
         () -> this.rowStatus(localValue1),
         () -> this.currentChat.getName().equals(localValue1),
         localValue2 -> {
            if (localValue2 == MouseButton.internalField0101) {
               this.openFriendMenu(localValue1);
            } else {
               this.openChat(localValue1);
            }
         }
      );
   }

   private UiContainer chatRow(
      Supplier<String> localValue3,
      Supplier<String> localValue4,
      Supplier<String> localValue5,
      Supplier<String> localValue6,
      BooleanSupplier localValue7,
      Consumer<MouseButton> localValue8
   ) {
      UiElement localValue9 = this.nickBox(
         Fonts.internalField0449.internalMethod01432(8.0F), 8.0F, localValue3, localValue4, localValue5, localValue0 -> text().mulAlpha(0.72F + 0.28F * localValue0.sig("active"))
      );
      localValue9.bind("active", localValue7, STATE);
      UiContainer localValue10 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(3.0F)
         .internalMethod09609()
         .internalMethod03907(localValue9)
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(Fonts.internalField1154.internalMethod01432(7.0F), throttled(localValue6), localValue0 -> second())
               .interactive(false)
         );
      UiContainer localValue11 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(28.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 5.0F))
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> highlight(0.05F * localValue0.sig("active") + 0.03F * localValue0.hover()))
         .internalMethod01258("active", localValue7, STATE)
         .internalMethod08336(STATE)
         .internalMethod04332(CursorType.internalField0567)
          .internalMethod03907(localValue10)
          .internalMethod02525(localValue8);
      applyMotion(localValue11);
      localValue11.internalMethod07914(UiTransition.internalField1661).internalMethod05305(LIST).internalMethod03754(LIST);
      return localValue11;
   }

   private static Supplier<String> throttled(final Supplier<String> localValue0) {
      return new Supplier<String>() {
         private String value;
         private long at;

         public String get() {
            long localValue1 = System.currentTimeMillis();
            if (this.value == null || localValue1 - this.at >= 200L) {
               this.value = (String)localValue0.get();
               this.at = localValue1;
            }

            return this.value;
         }
      };
   }

   private UiContainer buildRequestRow(String localValue1) {
      UiElement localValue2 = this.iconButton("check", 14.0F, 7.0F, () -> {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0277(localValue1));
         this.dropRequest(localValue1);
      });
      UiElement localValue3 = this.iconButton("xmark", 14.0F, 7.0F, () -> {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0377(localValue1));
         this.dropRequest(localValue1);
      });
      localValue3.icon("xmark", 7.0F, localValue0 -> DANGER.mulAlpha(0.75F + 0.25F * localValue0.hover()));
      UiContainer localValue4 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(28.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 5.0F))
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> highlight(0.03F * localValue0.hover()))
         .internalMethod08336(STATE)
          .internalMethod03907(
             this.nickBox(Fonts.internalField0449.internalMethod01432(8.0F), 8.0F, () -> localValue1, () -> null, () -> null, localValue0 -> text()).fillWidth()
          )
         .internalMethod03907(localValue2)
         .internalMethod03907(localValue3);
      applyMotion(localValue4);
      localValue4.internalMethod07914(UiTransition.internalField1661).internalMethod05305(LIST).internalMethod03754(LIST);
      return localValue4;
   }

   private void dropRequest(String localValue1) {
      ArrayList localValue2 = new ArrayList<>(Information.getRequests());
      if (localValue2.remove(localValue1)) {
         Information.setRequests(localValue2);
         this.rebuildSidebar(true);
      }
   }

   private UiContainer buildContent() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(7.0F)
         .internalMethod09213()
         .internalMethod03907(this.buildChatHeader())
         .internalMethod03907(this.buildChatBody())
         .internalMethod03907(this.buildComposer());
      return new UiContainer().internalMethod08791().internalMethod09213().internalMethod03907(localValue1).internalMethod03907(this.buildPeoplePanel());
   }

   private UiContainer buildChatHeader() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(3.0F)
         .internalMethod09609()
         .internalMethod03907(
            this.nickBox(
               Fonts.internalField0449.internalMethod01432(9.0F),
               9.0F,
               this::chatTitle,
               () -> this.currentChat.getName().equals(GLOBAL_CHAT_ID) ? null : this.nickStyle(this.currentChat.getName()),
               () -> this.currentChat.getName().equals(GLOBAL_CHAT_ID) ? null : this.badge(this.currentChat.getName()),
               localValue0 -> text()
            )
         )
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(Fonts.internalField1154.internalMethod01432(7.0F), this::chatSubtitle, localValue0 -> second())
               .interactive(false)
         );
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09339(130.0F)
         .internalMethod09186()
         .internalMethod04332(CursorType.internalField0567)
          .internalMethod03907(localValue1)
          .internalMethod02525(localValue1x -> {
            if (localValue1x == MouseButton.internalField0102) {
               if (this.currentChat.getName().equals(GLOBAL_CHAT_ID)) {
                  this.togglePeople();
               } else {
                  this.openProfile(this.currentChat.getName());
               }
            }
         });
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(32.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> card())
         .internalMethod03715(DragConstraint.internalField0631)
         .internalMethod03907(localValue2);
   }

   private UiContainer buildPeoplePanel() {
      this.peopleList = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod03514(Insets.internalMethod00105(4.0F, 4.0F, 4.0F, 4.0F))
         .internalMethod09213()
         .internalMethod08163(120.0F)
         .internalMethod08755()
         .internalMethod01416(ScrollPolicy.internalField0916)
         .internalMethod05391(
            localValue0 -> localValue0.internalMethod02712(-2.0F)
               .internalMethod09005(3.0F)
               .internalMethod00894(2.5F)
               .internalMethod08056(16.0F)
               .internalMethod07954(1.25F)
               .internalMethod08313(1000.0F)
               .internalMethod04404(localValue0x -> stroke().mix(second(), localValue0x.internalMethod05170() + localValue0x.internalMethod05173()))
         );
      UiElement localValue1 = new UiElement()
         .height(18.0F)
         .fillWidth()
         .radius(7.0F)
         .background(localValue0 -> highlight(0.04F))
         .cursor(CursorType.internalField1206)
         .onClick(() -> this.peopleField.internalMethod07508(true))
         .paint((localValue1x, localValue2x) -> {
            float localValue3x = (localValue2x.h() - Fonts.internalField1154.internalMethod01432(8.0F).internalMethod04890()) / 2.0F;
            localValue1x.drawIcon("search", localValue2x.x() + localValue3x, localValue2x.y() + localValue2x.h() / 2.0F - 3.0F, 6.0F, second());
            this.peopleField.internalMethod05191(localValue2x.x() + 10.0F, localValue2x.y(), localValue2x.w() - 14.0F, localValue2x.h());
            this.peopleField.internalMethod00143(text());
            this.peopleField.internalMethod03398(localValue1x);
         });
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(32.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 9.0F))
         .internalMethod03907(
            new UiContainer()
               .internalMethod01863()
               .internalMethod03062(3.0F)
               .internalMethod09609()
               .internalMethod03907(
                  new UiElement()
                     .fillWidth()
                     .fade()
                     .text(
                        Fonts.internalField0449.internalMethod01432(9.0F),
                        () -> LanguageManager.internalMethod07214("rocknet.people.title"),
                        localValue0 -> text()
                     )
                     .interactive(false)
               )
               .internalMethod03907(
                  new UiElement()
                     .fillWidth()
                     .fade()
                     .text(
                        Fonts.internalField1154.internalMethod01432(7.0F),
                        () -> LanguageManager.internalMethod00160("rocknet.menu.subtitle_online", RocknetListener.getSiteOnline(), RocknetListener.getOnline()),
                        localValue0 -> second()
                     )
                     .interactive(false)
               )
         )
         .internalMethod03907(this.iconButton("xmark", 14.0F, 7.0F, this::togglePeople));
      UiContainer localValue3 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(4.0F)
         .internalMethod03514(Insets.internalMethod00105(5.0F, 5.0F, 5.0F, 5.0F))
         .internalMethod09213()
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> card())
         .internalMethod02525(localValue0 -> {})
         .internalMethod03907(localValue2)
         .internalMethod03907(localValue1)
         .internalMethod03907(this.peopleList);
      localValue3.internalMethod09625().internalMethod03855(() -> this.peopleOpen).internalMethod07914(UiTransition.internalField1389);
      applyMotion(localValue3);
      return localValue3;
   }

   private void togglePeople() {
      this.peopleOpen = !this.peopleOpen;
      this.closeMenu();
      if (this.peopleOpen) {
         this.peopleField.internalMethod09126();
         this.personRows.clear();
         this.peopleSnapshot = List.of();
         this.peopleCheckedAt = 0L;
         Information.resetPeople();
         Information.requestPeople("", 0);
      } else {
         this.peopleField.internalMethod07508(false);
      }
   }

   private void rebuildPeople() {
      if (this.peopleList != null && this.peopleOpen) {
         long localValue1 = System.currentTimeMillis();
         if (localValue1 - this.peopleCheckedAt >= 200L) {
            this.peopleCheckedAt = localValue1;
            List localValue3 = Information.getPeople();
            ArrayList localValue4 = new ArrayList();

            for (Packets.InternalType0047 localValue6 : (Iterable<Packets.InternalType0047>)(Iterable<?>)localValue3) {
               localValue4.add(localValue6.username());
            }

            localValue4.add(Information.isPeopleMore() ? "+" : "-");
            localValue4.add(this.peopleState());
            if (!localValue4.equals(this.peopleSnapshot)) {
               this.peopleSnapshot = localValue4;
               ArrayList localValue8 = new ArrayList();

               for (Packets.InternalType0047 localValue7 : (Iterable<Packets.InternalType0047>)(Iterable<?>)localValue3) {
                  localValue8.add(this.personRows.computeIfAbsent(localValue7.username(), this::buildPersonRow));
               }

               if (localValue3.isEmpty()) {
                  localValue8.add(this.placeholder(this.peopleState()));
               } else if (Information.isPeopleMore()) {
                  localValue8.add(this.moreRow());
               }

               this.peopleList.internalMethod07849(localValue8);
            }
         }
      }
   }

   private String peopleState() {
      if (Information.peopleFailed()) {
         return "rocknet.people.failed";
      } else {
         return Information.isPeopleLoading() ? "rocknet.people.loading" : "rocknet.menu.nothing_found";
      }
   }

   private UiElement moreRow() {
      return new UiElement()
         .height(20.0F)
         .fillWidth()
         .radius(7.0F)
         .text(
            Fonts.internalField0449.internalMethod01432(7.0F),
            () -> LanguageManager.internalMethod07214(Information.isPeopleLoading() ? "rocknet.people.loading" : "rocknet.people.more"),
            localValue0 -> second().mix(text(), localValue0.hover())
         )
         .textAlign(TextAlignment.internalField0621)
         .background(localValue0 -> highlight(0.03F * localValue0.hover()))
         .hoverMotion(STATE)
         .cursor(CursorType.internalField0567)
         .onClick(() -> Information.requestPeople(Information.getPeopleQuery(), Information.getPeoplePage() + 1));
   }

   private UiContainer buildPersonRow(String localValue1) {
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09266(28.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 5.0F))
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> highlight(0.03F * localValue0.hover()))
         .internalMethod08336(STATE)
         .internalMethod04332(CursorType.internalField0567)
          .internalMethod03907(
             new UiContainer()
                .internalMethod01863()
                .internalMethod03062(3.0F)
                .internalMethod09609()
                .internalMethod03907(
                   this.nickBox(
                     Fonts.internalField0449.internalMethod01432(8.0F),
                     8.0F,
                     () -> localValue1,
                     () -> Information.person(localValue1) == null ? null : Information.person(localValue1).nickStyle(),
                     () -> Information.person(localValue1) == null ? null : Information.person(localValue1).badge(),
                     localValue0 -> text()
                  )
               )
               .internalMethod03907(
                  new UiElement()
                     .fillWidth()
                     .fade()
                     .text(Fonts.internalField1154.internalMethod01432(7.0F), throttled(() -> this.personStatus(localValue1)), localValue0 -> second())
                     .interactive(false)
               )
         )
         .internalMethod02525(localValue2x -> {
            if (localValue2x == MouseButton.internalField0101) {
               this.openModerationMenu(localValue1);
            } else {
               this.openProfile(localValue1);
            }
         });
      applyMotion(localValue2);
      localValue2.internalMethod07914(UiTransition.internalField1661).internalMethod05305(LIST).internalMethod03754(LIST);
      return localValue2;
   }

   private void syncPeopleQuery() {
      if (this.peopleOpen) {
         String localValue1 = this.peopleField.internalMethod06202().trim();
         if (!localValue1.equals(this.pendingQuery)) {
            this.pendingQuery = localValue1;
            this.queryChangedAt = System.currentTimeMillis();
         } else if (!localValue1.equals(Information.getPeopleQuery()) && this.queryChangedAt != 0L) {
            if (System.currentTimeMillis() - this.queryChangedAt >= 350L) {
               this.queryChangedAt = 0L;
               this.personRows.clear();
               this.peopleCheckedAt = 0L;
               Information.resetPeople();
               Information.requestPeople(localValue1, 0);
               if (this.peopleList != null) {
                  this.peopleList.internalMethod03631();
               }
            }
         }
      }
   }

   private String personStatus(String localValue1) {
      Packets.InternalType0047 localValue2 = Information.person(localValue1);
      if (localValue2 == null) {
         return "";
      } else if (localValue2.online()) {
         return LanguageManager.internalMethod07214("rocknet.people.online");
      } else {
         return localValue2.lastSeen() > 0L ? RelativeTimeFormatter.internalMethod06419(localValue2.lastSeen()) : LanguageManager.internalMethod07214("rocknet.status.offline");
      }
   }

   private UiContainer buildChatBody() {
      this.messagesBox = new UiElement() {
         @Override
         public boolean mouseScrolled(float localValue1, float localValue2, float localValue3, float localValue4) {
            if (!this.contains(localValue1, localValue2)) {
               return false;
            } else {
               RocknetMenu.this.currentChat.getScrollHandler().internalMethod04249(-localValue4 * 2.5);
               return true;
            }
         }
      };
      this.messagesBox.fill().paint(this::paintMessages).onClick(this::clickMessages);
      return new UiContainer()
         .internalMethod01863()
         .internalMethod09213()
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> card())
         .internalMethod03907(this.messagesBox);
   }

   private UiContainer buildComposer() {
      SizedFont localValue1 = Fonts.internalField0449.internalMethod01432(7.0F);
      UiElement localValue2 = new UiElement()
         .height(localValue1.internalMethod04890())
         .fillWidth()
         .paint(
            (localValue2x, localValue3x) -> {
               Message localValue4x = this.currentChat.getMessage(this.lastReply);
               if (localValue4x != null) {
                  CosmeticRender.draw(
                     localValue2x,
                     localValue1,
                     LanguageManager.internalMethod00160("rocknet.menu.reply_to", localValue4x.author().username()),
                     localValue3x.x(),
                     localValue3x.y(),
                     null,
                     null,
                     text(),
                     0.0F,
                     localValue3x.w()
                  );
               }
            }
         )
         .interactive(false);
      UiElement localValue3 = new UiElement().fillWidth().fade().text(Fonts.internalField1154.internalMethod01432(7.0F), () -> {
         Message localValue1x = this.currentChat.getMessage(this.lastReply);
         return localValue1x == null ? "" : localValue1x.text();
      }, localValue0 -> second()).interactive(false);
      UiContainer localValue4 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(6.0F)
         .internalMethod09266(22.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 7.0F))
         .internalMethod09018(7.0F)
         .internalMethod06812(localValue0 -> card())
         .internalMethod03907(new UiContainer().internalMethod01863().internalMethod03062(3.0F).internalMethod09609().internalMethod03907(localValue2).internalMethod03907(localValue3))
         .internalMethod03907(this.iconButton("xmark", 12.0F, 6.0F, () -> this.setReply(-1L)));
      localValue4.internalMethod06712(() -> this.reply != -1L, Easing.internalField1626, 260L)
         .internalMethod09936()
         .internalMethod07914(UiTransition.internalField1389);
      UiElement localValue5 = new UiElement()
         .height(22.0F)
         .fillWidth()
         .radius(7.0F)
         .background(localValue0 -> card())
         .cursor(CursorType.internalField1206)
         .onClick(() -> this.sendField.internalMethod07508(!Information.muted()))
         .paint((localValue1x, localValue2x) -> {
            if (Information.muted()) {
               this.sendField.internalMethod05191(0.0F, 0.0F, 0.0F, 0.0F);
               float localValue3x = (localValue2x.h() - Fonts.internalField1154.internalMethod01432(8.0F).internalMethod04890()) / 2.0F;
               localValue1x.drawText(Fonts.internalField1154.internalMethod01432(8.0F), this.muteNotice(), localValue2x.x() + localValue3x, localValue2x.y() + localValue3x, second());
            } else {
               this.sendField.internalMethod05191(localValue2x.x(), localValue2x.y(), localValue2x.w() - 4.0F, localValue2x.h());
               this.sendField.internalMethod00143(text());
               this.sendField.internalMethod03398(localValue1x);
            }
         });
      UiElement localValue6 = this.iconButton("plus", 22.0F, 8.0F, this::openActionsMenu);
      UiElement localValue7 = new UiElement()
         .size(22.0F, 22.0F)
         .padding(7.0F)
         .radius(7.0F)
         .icon("play", 8.0F, localValue0 -> second().mix(onAccent(), localValue0.sig("ready")))
         .background(localValue0 -> card().mix(accent().mix(accentHover(), localValue0.hover()), localValue0.sig("ready")))
         .bind("ready", () -> !this.sendField.internalMethod06202().isBlank(), STATE)
         .hoverMotion(STATE)
         .cursor(CursorType.internalField0567)
         .onClick(this::sendMessage);
      UiContainer localValue8 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(5.0F)
         .internalMethod09266(22.0F)
         .internalMethod09609()
         .internalMethod03907(localValue6)
         .internalMethod03907(localValue5)
         .internalMethod03907(localValue7);
      UiContainer localValue9 = new UiContainer().internalMethod01863().internalMethod03062(4.0F).internalMethod09609().internalMethod03907(localValue4).internalMethod03907(localValue8);
      localValue9.internalMethod03855(() -> !this.peopleOpen).internalMethod09936().internalMethod07914(UiTransition.internalField1389);
      return localValue9;
   }

   private void syncMentions() {
      if (this.mentionPending != null) {
         this.applyMention(this.mentionPending);
         this.mentionPending = null;
      }

      String localValue1 = this.sendField.internalMethod00342() && !Information.muted() ? Mentions.typed(this.sendField.internalMethod06202()) : null;
      if (localValue1 == null) {
         this.mentionDismissed = null;
         this.setMentionOptions(List.of());
      } else if (!localValue1.equals(this.mentionDismissed)) {
         this.mentionDismissed = null;
         this.setMentionOptions(this.mentionCandidates(localValue1));
      }
   }

   private List<String> mentionCandidates(String localValue1) {
      int localValue2 = this.mentionFit();
      String localValue3 = Information.getPreferUser() == null ? "" : Information.getPreferUser().username();
      LinkedHashSet localValue4 = new LinkedHashSet();
      List localValue5 = this.currentChat.messagesSnapshot();

      for (int localValue6 = localValue5.size() - 1; localValue6 >= 0; localValue6--) {
         localValue4.add(((Message)localValue5.get(localValue6)).author().username());
      }

      for (Packets.InternalType0018 localValue7 : Information.getFriends()) {
         localValue4.add(localValue7.username());
      }

      for (Packets.InternalType0047 localValue12 : Information.getPeople()) {
         localValue4.add(localValue12.username());
      }

      ArrayList localValue11 = new ArrayList();

      for (String localValue8 : (Iterable<String>)(Iterable<?>)localValue4) {
         if (localValue8 != null && !localValue8.equalsIgnoreCase(localValue3) && (localValue1.isEmpty() || localValue8.regionMatches(true, 0, localValue1, 0, localValue1.length()))) {
            localValue11.add(localValue8);
            if (localValue11.size() >= localValue2) {
               break;
            }
         }
      }

      return List.copyOf(localValue11);
   }

   private int mentionFit() {
      if (this.messagesBox != null && !(this.messagesBox.h() <= 0.0F)) {
         float localValue1 = this.messagesBox.h() - 16.0F - 6.0F;
         int localValue2 = (int)((localValue1 + 2.0F) / 22.0F);
         return Math.max(1, Math.min(5, localValue2));
      } else {
         return 5;
      }
   }

   private void setMentionOptions(List<String> localValue1) {
      if (!localValue1.equals(this.mentionOptions)) {
         this.mentionOptions = localValue1;
         this.mentionIndex = 0;
         if (!localValue1.isEmpty()) {
            this.mentionShown = localValue1;
         }
      }
   }

   private Rect mentionArea() {
      float localValue1 = Math.min(150.0F, this.messagesBox.w() - 20.0F);
      float localValue2 = 6.0F + this.mentionShown.size() * 20.0F + Math.max(0, this.mentionShown.size() - 1) * 2.0F;
      return new Rect(this.messagesBox.x() + 10.0F, this.messagesBox.y() + this.messagesBox.h() - 8.0F - localValue2, localValue1, localValue2);
   }

   private boolean mentionsVisible() {
      return !this.mentionShown.isEmpty() && this.mentionAppear.internalMethod02881() > 0.01F;
   }

   private void drawMentions(UiRenderContext localValue1) {
      this.mentionAppear.internalMethod07059(this.mentionOptions.isEmpty() ? 0.0F : 1.0F);
      if (this.mentionsVisible()) {
         Rect localValue2 = this.mentionArea();
         float localValue3 = RenderSystem.getShaderColor()[3];
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.mentionAppear.internalMethod02881() * localValue3);
         localValue1.drawShadow(
            localValue2.getX(), localValue2.getY(), localValue2.getWidth(), localValue2.getHeight(), 20.0F, CornerRadii.internalMethod03908(8.0F), ColorRGBA.BLACK.withAlpha(191.25F)
         );
         localValue1.drawRoundedRect(localValue2.getX(), localValue2.getY(), localValue2.getWidth(), localValue2.getHeight(), CornerRadii.internalMethod03908(8.0F), inset());
         SizedFont localValue4 = Fonts.internalField0449.internalMethod01432(7.0F);
         float localValue5 = localValue2.getWidth() - 6.0F;
         float localValue6 = 12.0F;
         float localValue7 = localValue2.getY() + 3.0F;

         for (int localValue8 = 0; localValue8 < this.mentionShown.size(); localValue8++) {
            String localValue9 = this.mentionShown.get(localValue8);
            boolean localValue10 = UiUtils.internalMethod06450(localValue2.getX() + 3.0F, localValue7, localValue5, 20.0, localValue1);
            float localValue11 = localValue8 == this.mentionIndex ? 0.07F : (localValue10 ? 0.04F : 0.0F);
            if (localValue11 > 0.0F) {
               localValue1.drawRoundedRect(localValue2.getX() + 3.0F, localValue7, localValue5, 20.0F, CornerRadii.internalMethod03908(5.0F), highlight(localValue11));
            }

            if (localValue10) {
               CursorManager.internalMethod06882(CursorType.internalField0567);
            }

            CosmeticRender.draw(
               localValue1,
               localValue4,
               "@" + localValue9,
               localValue2.getX() + 3.0F + 4.0F + localValue6 + 5.0F,
               localValue7 + (20.0F - localValue4.internalMethod04890()) / 2.0F,
               null,
               null,
               text(),
               0.0F,
               localValue5 - localValue6 - 13.0F
            );
            localValue7 += 22.0F;
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue3);
      }
   }

   private boolean clickMentions(MouseButton localValue1, float localValue2, float localValue3) {
      if (this.mentionsVisible() && !this.mentionOptions.isEmpty()) {
         Rect localValue4 = this.mentionArea();
         if (!UiUtils.internalMethod01807(localValue4, localValue2, localValue3)) {
            return false;
         } else if (localValue1 != MouseButton.internalField0102) {
            return true;
         } else {
            int localValue5 = (int)((localValue3 - localValue4.getY() - 3.0F) / 22.0F);
            if (localValue5 >= 0 && localValue5 < this.mentionShown.size()) {
               this.mentionPending = this.mentionShown.get(localValue5);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private void applyMention(String localValue1) {
      String localValue2 = Mentions.complete(this.sendField.internalMethod06202(), localValue1);
      if (localValue2 != null) {
         this.sendField.internalMethod00484(localValue2);
         this.sendField.internalMethod07508(true);
         this.setMentionOptions(List.of());
         this.mentionDismissed = null;
         this.mentionPending = null;
      }
   }

   private UiContainer buildProfileCard() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(6.0F)
         .internalMethod03514(Insets.internalMethod00105(9.0F, 9.0F, 9.0F, 9.0F))
         .internalMethod09339(210.0F)
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> inset())
         .internalMethod02525(localValue0 -> {})
         .internalMethod03907(this.profileHead())
         .internalMethod03907(this.profileMutual())
         .internalMethod03907(this.profileRow("rocknet.profile.status", this::profileStatus))
         .internalMethod03907(this.profileRow("rocknet.profile.playtime", this::profilePlaytime))
         .internalMethod03907(this.profileRow("rocknet.profile.registered", this::profileRegistered))
         .internalMethod03907(this.profileMuteRow())
         .internalMethod03907(this.profileActions());
      UiContainer localValue2 = new UiContainer()
         .internalMethod01863()
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod09213()
         .internalMethod07178(this::paintProfileBackdrop)
         .internalMethod02525(localValue1x -> this.closeProfile())
         .internalMethod09625()
         .internalMethod03907(localValue1);
      localValue2.internalMethod03855(this::profileOpen).internalMethod07914(UiTransition.internalField1389);
      applyMotion(localValue2);
      return localValue2;
   }

   private void paintProfileBackdrop(UiRenderContext localValue1, UiContainer localValue2) {
      float localValue3 = localValue2.x() - 7.0F;
      float localValue4 = localValue2.y() - 7.0F;
      float localValue5 = localValue2.w() + 14.0F;
      float localValue6 = localValue2.h() + 14.0F;
      CornerRadii localValue7 = CornerRadii.internalMethod03908(12.0F);
      UiBatchRenderer.internalMethod02576();
      RenderPipeline.internalField0312.internalMethod03955(1, 2.0F);
      RenderPipeline.internalMethod07025(1);

      try {
         localValue1.drawBlurredRect(localValue3, localValue4, localValue5, localValue6, 8.0F, localValue7, ColorRGBA.WHITE);
      } finally {
         RenderPipeline.internalMethod01905();
      }

      localValue1.drawRoundedRect(localValue3, localValue4, localValue5, localValue6, localValue7, page().mulAlpha(0.55F));
   }

   private UiContainer profileHead() {
      UiContainer localValue1 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(3.0F)
         .internalMethod09609()
         .internalMethod03907(
            this.nickBox(
               Fonts.internalField0449.internalMethod01432(10.0F),
               10.0F,
               this::profileName,
               () -> this.profile() == null ? null : this.profile().nickStyle(),
               () -> this.profile() == null ? null : this.profile().badge(),
               localValue0 -> text()
            )
         )
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(Fonts.internalField1154.internalMethod01432(7.0F), this::profileTitleLine, localValue0 -> second())
               .interactive(false)
         );
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09609()
          .internalMethod03907(localValue1);
   }

   private UiContainer profileMutual() {
      UiElement localValue1 = new UiElement()
         .size(40.32F, 18.0F)
         .interactive(false)
         .paint(
            (localValue1x, localValue2x) -> {
               Packets.InternalType0306 localValue3 = this.profile();
               if (localValue3 != null) {
                  float localValue4 = 11.16F;

                  for (int localValue5 = 0; localValue5 < localValue3.mutual().size(); localValue5++) {
                     float localValue6 = localValue2x.x() + localValue5 * localValue4;
                     localValue1x.drawRoundedRect(localValue6 - 1.0F, localValue2x.y() - 1.0F, 20.0F, 20.0F, CornerRadii.internalMethod03908(10.0F), inset());
                  }
               }
            }
         );
      UiContainer localValue2 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(7.0F)
         .internalMethod09609()
         .internalMethod03907(localValue1)
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(Fonts.internalField1154.internalMethod01432(8.0F), this::profileMutualText, localValue0 -> second())
               .interactive(false)
         );
      localValue2.internalMethod03855(() -> this.profile() != null && this.profile().mutualCount() > 0).internalMethod09936();
      return localValue2;
   }

   private UiContainer profileRow(String localValue1, Supplier<String> localValue2) {
      return new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(5.0F)
         .internalMethod09266(14.0F)
         .internalMethod09609()
         .internalMethod07178(
            (localValue0, localValue1x) -> localValue0.drawRoundedRect(localValue1x.x(), localValue1x.y() + localValue1x.h() - 1.0F, localValue1x.w(), 1.0F, CornerRadii.internalField0098, stroke10())
         )
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(Fonts.internalField1154.internalMethod01432(8.0F), () -> LanguageManager.internalMethod07214(localValue1), localValue0 -> second())
               .interactive(false)
         )
         .internalMethod03907(
            new UiElement()
               .height(Fonts.internalField0449.internalMethod01432(8.0F).internalMethod04890())
               .width(70.0F)
               .text(Fonts.internalField0449.internalMethod01432(8.0F), localValue2, localValue0 -> text())
               .textAlign(TextAlignment.internalField1242)
               .interactive(false)
         );
   }

   private UiContainer profileMuteRow() {
      UiContainer localValue1 = this.profileRow("rocknet.mod.title", () -> {
         Packets.InternalType0306 localValue1x = this.profile();
         if (localValue1x == null) {
            return "";
         } else {
            return localValue1x.mutedUntil() < 0L ? LanguageManager.internalMethod07214("rocknet.mod.forever") : TIME.format(new Date(localValue1x.mutedUntil()));
         }
      });
      localValue1.internalMethod03855(() -> this.profile() != null && this.profile().mutedUntil() != 0L).internalMethod09936();
      return localValue1;
   }

   private UiContainer profileActions() {
      UiContainer localValue1 = this.profileButton("plane", "rocknet.profile.message", false, () -> {
         String localValue1x = this.profileTarget();
         this.closeProfile();
         if (this.peopleOpen) {
            this.togglePeople();
         }

         this.openChat(localValue1x);
      });
      localValue1.internalMethod03855(() -> this.profile() != null && !this.isSelfProfile()).internalMethod09936();
      UiContainer localValue2 = this.profileButton("plus", "rocknet.profile.add_friend", false, () -> {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0007(this.profileTarget()));
         this.closeProfile();
      });
      localValue2.internalMethod03855(() -> this.profile() != null && !this.isSelfProfile() && "none".equals(this.profile().relationship())).internalMethod09936();
      UiContainer localValue3 = this.profileButton("trash", "remove", true, () -> {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0236(this.profileTarget()));
         this.closeProfile();
      });
      localValue3.internalMethod03855(() -> this.profile() != null && "friends".equals(this.profile().relationship())).internalMethod09936();
      UiContainer localValue4 = this.profileButton("path", "rocknet.profile.site", false, () -> {
         Util.getOperatingSystem().open("https://rockstar.pub/users/" + this.profileTarget());
         this.closeProfile();
      });
      UiContainer localValue5 = this.profileButton("hud/handcuffs", "rocknet.mod.title", false, () -> this.openModerationMenu(this.profileTarget()));
      localValue5.internalMethod03855(() -> Information.staff() && this.profile() != null && !this.isSelfProfile()).internalMethod09936();
      return new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod09609()
         .internalMethod03907(localValue1)
         .internalMethod03907(localValue2)
         .internalMethod03907(localValue3)
         .internalMethod03907(localValue4)
         .internalMethod03907(localValue5);
   }

   private UiContainer profileButton(String localValue1, String localValue2, boolean localValue3, Runnable localValue4) {
      UiContainer localValue5 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(5.0F)
         .internalMethod09266(16.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 4.0F))
         .internalMethod09018(5.0F)
         .internalMethod08336(MENU_HOVER)
         .internalMethod04332(CursorType.internalField0567);
      localValue5.internalMethod06812(localValue0 -> highlight(0.04F * localValue0.hover()))
         .internalMethod03907(new UiElement().size(6.0F, 6.0F).icon(localValue1, 6.0F, localValue2x -> menuItemColor(localValue5, localValue3)).interactive(false))
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(
                  Fonts.internalField1154.internalMethod01432(7.0F), () -> LanguageManager.internalMethod07214(localValue2), localValue2x -> menuItemColor(localValue5, localValue3)
               )
               .interactive(false)
         )
         .internalMethod05690(localValue4);
      return localValue5;
   }

   private Packets.InternalType0306 profile() {
      return Information.getProfile();
   }

   private String profileTarget() {
      Packets.InternalType0306 localValue1 = this.profile();
      if (localValue1 != null) {
         return localValue1.username();
      } else {
         String localValue2 = Information.getProfileLoading();
         return localValue2 == null ? "" : localValue2;
      }
   }

   private boolean isSelfProfile() {
      return Information.getUser() != null && Information.getUser().username().equals(this.profileTarget());
   }

   private String profileName() {
      return this.profileTarget();
   }

   private String profileTitleLine() {
      Packets.InternalType0306 localValue1 = this.profile();
      return localValue1 == null
         ? LanguageManager.internalMethod07214(Information.profileFailed() ? "rocknet.profile.failed" : "rocknet.people.loading")
         : "#" + localValue1.uid() + " \u2022 " + LanguageManager.internalMethod07214("rocknet.role." + localValue1.role());
   }

   private String profileMutualText() {
      Packets.InternalType0306 localValue1 = this.profile();
      return localValue1 == null ? "" : LanguageManager.internalMethod00160("rocknet.profile.mutual", localValue1.mutualCount());
   }

   private String profilePlaytime() {
      Packets.InternalType0306 localValue1 = this.profile();
      if (localValue1 == null) {
         return "\u2014";
      } else {
         long localValue2 = localValue1.playtime();
         long localValue4 = localValue2 / 3600L;
         long localValue6 = localValue2 % 3600L / 60L;
         if (localValue4 >= 24L) {
            return localValue4 / 24L
               + LanguageManager.internalMethod07214("time.short.days")
               + " "
               + localValue4 % 24L
               + LanguageManager.internalMethod07214("time.short.hours");
         } else {
            return localValue4 > 0L
               ? localValue4 + LanguageManager.internalMethod07214("time.short.hours") + " " + localValue6 + LanguageManager.internalMethod07214("time.short.minutes")
               : localValue6 + LanguageManager.internalMethod07214("time.short.minutes");
         }
      }
   }

   private String profileStatus() {
      Packets.InternalType0306 localValue1 = this.profile();
      return localValue1 == null ? "\u2014" : this.peerStatus(localValue1, false);
   }

   private String profileRegistered() {
      Packets.InternalType0306 localValue1 = this.profile();
      return localValue1 != null && localValue1.registered() > 0L ? DATE.format(new Date(localValue1.registered())) : "\u2014";
   }

   private boolean profileOpen() {
      return Information.getProfile() != null || Information.getProfileLoading() != null;
   }

   private void openProfile(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank() && !localValue1.equals(GLOBAL_CHAT_ID)) {
         this.closeMenu();
         this.sendField.internalMethod07508(false);
         this.friendField.internalMethod07508(false);
         this.searchField.internalMethod07508(false);
         this.peopleField.internalMethod07508(false);
         Information.requestProfile(localValue1);
      }
   }

   private void closeProfile() {
      Information.closeProfile();
   }

   private void openModerationMenu(String localValue1) {
      if (Information.staff() && localValue1 != null && !localValue1.isBlank()) {
         if (Information.getUser() == null || !Information.getUser().username().equals(localValue1)) {
            ArrayList localValue2 = new ArrayList();
            localValue2.add(
               new RocknetMenu.InternalType0364("hud/player", LanguageManager.internalMethod07214("rocknet.profile.title"), false, () -> this.openProfile(localValue1))
            );
            localValue2.add(new RocknetMenu.InternalType0364("plane", LanguageManager.internalMethod07214("rocknet.menu.open_chat"), false, () -> this.openChat(localValue1)));
            localValue2.add(
               new RocknetMenu.InternalType0364("hud/handcuffs", LanguageManager.internalMethod07214("rocknet.mod.mute"), false, () -> this.openMutePicker(localValue1))
            );
            localValue2.add(
               new RocknetMenu.InternalType0364(
                  "check",
                  LanguageManager.internalMethod07214("rocknet.mod.unmute"),
                  false,
                  () -> RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0249(localValue1))
               )
            );
            this.showMenu(localValue1, localValue2);
         }
      }
   }

   private void openMutePicker(String localValue1) {
      this.muteTarget = localValue1;
      NumberWheelPicker localValue2 = new NumberWheelPicker()
         .internalMethod05960(Fonts.internalField0449.internalMethod01432(8.0F))
         .internalMethod07655(Fonts.internalField1154.internalMethod01432(6.0F))
         .internalMethod03157(11.0F)
         .internalMethod01310(3)
         .internalMethod04403(localValue0 -> highlight(0.06F))
         .internalMethod06195(text())
         .internalMethod08253(text().mulAlpha(0.35F))
         .internalMethod08391(second());
      localValue2.internalMethod05476(31, () -> this.muteDays, localValue1x -> this.muteDays = localValue1x, () -> LanguageManager.internalMethod07214("time.unit.days"));
      localValue2.internalMethod05476(24, () -> this.muteHours, localValue1x -> this.muteHours = localValue1x, () -> LanguageManager.internalMethod07214("time.unit.hours"));
      localValue2.internalMethod05476(60, () -> this.muteMinutes, localValue1x -> this.muteMinutes = localValue1x, () -> LanguageManager.internalMethod07214("time.unit.minutes"));
      localValue2.internalMethod07555().internalMethod08037(38.0F);
      UiContainer localValue3 = new UiContainer()
         .internalMethod05895()
         .internalMethod03062(4.0F)
         .internalMethod09609()
         .internalMethod03907(
            this.muteButton("rocknet.mod.mute_apply", false, () -> this.applyMute(this.muteDays * 86400L + this.muteHours * 3600L + this.muteMinutes * 60L))
         )
         .internalMethod03907(this.muteButton("rocknet.mod.forever", true, () -> this.applyMute(0L)))
         .internalMethod03907(this.muteButton("cancel", false, this::closeMutePicker));
      UiContainer localValue4 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(6.0F)
         .internalMethod03514(Insets.internalMethod00105(9.0F, 9.0F, 9.0F, 9.0F))
         .internalMethod09339(210.0F)
         .internalMethod09018(10.0F)
         .internalMethod06812(localValue0 -> inset())
         .internalMethod02525(localValue0 -> {})
         .internalMethod03907(
            new UiElement()
               .fillWidth()
               .fade()
               .text(
                  Fonts.internalField0449.internalMethod01432(9.0F),
                  () -> LanguageManager.internalMethod00160("rocknet.mod.mute_title", localValue1),
                  localValue0 -> text()
               )
               .interactive(false)
         )
         .internalMethod03907(localValue2)
         .internalMethod03907(localValue3);
      UiContainer localValue5 = new UiContainer()
         .internalMethod01863()
         .internalMethod07607(LayoutAlignment.internalField0912)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03995(this.window.w(), this.window.h())
         .internalMethod07178(
            (localValue0, localValue1x) -> localValue0.drawRoundedRect(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), CornerRadii.internalMethod03908(12.0F), page().mulAlpha(0.7F))
         )
         .internalMethod02525(localValue1x -> this.closeMutePicker())
         .internalMethod03907(localValue4);
      localValue5.snapAt(this.window.x(), this.window.y());
      this.closeMenu();
      localValue5.beginEnter(0.0F);
      this.activeMenu = this.openWindow(localValue5);
   }

   private UiElement muteButton(String localValue1, boolean localValue2, Runnable localValue3) {
      ColorRGBA localValue4 = localValue2 ? DANGER : null;
      return new UiElement()
         .height(16.0F)
         .fillWidth()
         .radius(7.0F)
         .text(
            Fonts.internalField0449.internalMethod01432(7.0F),
            () -> LanguageManager.internalMethod07214(localValue1),
            localValue1x -> second().mix(localValue4 == null ? onAccent() : text(), localValue1x.hover())
         )
         .textAlign(TextAlignment.internalField0621)
         .background(localValue1x -> card().mix(localValue4 == null ? accent() : localValue4, localValue1x.hover()))
         .hoverMotion(STATE)
         .cursor(CursorType.internalField0567)
         .onClick(localValue3);
   }

   private void applyMute(long localValue1) {
      if (this.muteTarget != null) {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0061(this.muteTarget, localValue1, ""));
      }

      this.closeMutePicker();
   }

   private void closeMutePicker() {
      this.muteTarget = null;
      this.closeMenu();
   }

   private void paintMessages(UiRenderContext localValue1, UiElement localValue2) {
      Chat localValue3 = this.currentChat;
      SmoothScrollHandler localValue4 = localValue3.getScrollHandler();
      localValue4.internalMethod02322();
      float localValue5 = 10.0F;
      float localValue6 = 8.0F;
      float localValue7 = localValue2.x() + localValue5;
      float localValue8 = localValue2.x() + localValue2.w() - localValue5;
      float localValue9 = localValue2.y() + localValue2.h() - localValue6;
      float localValue10 = localValue2.h() - localValue6 * 2.0F;
      float localValue11 = (localValue8 - localValue7) * 0.66F;
      SizedFont localValue12 = Fonts.internalField1154.internalMethod01432(8.0F);
      SizedFont localValue13 = Fonts.internalField1157.internalMethod01432(8.0F);
      float localValue14 = 3.0F;
      ScissorStack.internalMethod06303(localValue1.getMatrices(), localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h());
      float localValue15 = (float)localValue4.internalMethod02321();
      float localValue16 = 0.0F;
      String localValue17 = "";
      float localValue18 = RenderSystem.getShaderColor()[3];
      List localValue19 = localValue3.entriesSnapshot();
      if (localValue19.isEmpty()) {
         localValue1.drawCenteredText(
            Fonts.internalField1154.internalMethod01432(8.0F),
            LanguageManager.internalMethod07214("rocknet.menu.no_messages"),
            localValue2.x() + localValue2.w() / 2.0F,
            localValue2.y() + localValue2.h() / 2.0F - 3.0F,
            second()
         );
      }

      for (int localValue20 = localValue19.size() - 1; localValue20 >= 0; localValue20--) {
         Chat.InternalType0217 localValue21 = (Chat.InternalType0217)localValue19.get(localValue20);
         long localValue22 = localValue21.id();
         Message localValue24 = localValue21.message();
         localValue24.animation().internalMethod07059(1.0F);
         float localValue25 = localValue24.animation().internalMethod02881();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue25 * localValue18);
         boolean localValue26 = localValue24.self();
         boolean localValue27 = localValue17.isEmpty() || !localValue17.equals(localValue24.author().username());
         boolean localValue28 = localValue3.isFirstMessage(localValue22);
         boolean localValue29 = localValue28 && !localValue26;
         if (localValue24 instanceof ShareMessage localValue46) {
            String localValue48 = LanguageManager.internalMethod07214(localValue46.titleKey());
            float localValue51 = Math.max(120.0F, Math.min(localValue11, Math.max(localValue12.internalMethod00965(localValue46.text()), localValue12.internalMethod00965(localValue48)) + 16.0F));
            float localValue53 = 49.0F;
            float localValue55 = localValue9 - localValue53 * localValue25 - localValue16 + localValue15;
            float localValue57 = localValue26 ? localValue8 - localValue51 : localValue7 + 18.0F + 5.0F;
            boolean localValue59 = localValue55 + localValue53 > localValue2.y() && localValue55 < localValue2.y() + localValue2.h();
            float localValue61 = localValue57 + 8.0F;
            Rect localValue63 = new Rect(localValue61, localValue55 + 29.0F, localValue51 - 16.0F, 13.0F);
            localValue46.actionRect(localValue63);
            if (localValue59) {
               this.drawBubble(localValue1, localValue57, localValue55, localValue51, localValue53, localValue26, localValue28, localValue27);
                ColorRGBA localValue66 = this.bubbleText(localValue26);
               localValue1.drawIcon(localValue46.icon(), localValue61, localValue55 + 7.0F, 6.0F, localValue66);
               localValue1.drawText(Fonts.internalField0449.internalMethod01432(8.0F), localValue48, localValue61 + 9.0F, localValue55 + 7.0F, localValue66);
               localValue1.drawFadeoutText(localValue12, localValue46.text(), localValue61, localValue55 + 18.0F, localValue66.mulAlpha(0.7F), 0.9F, 1.0F, localValue51 - 16.0F);
               boolean localValue67 = UiUtils.internalMethod06450(localValue63.getX(), localValue63.getY(), localValue63.getWidth(), localValue63.getHeight(), localValue1);
               localValue1.drawRoundedRect(
                  localValue63.getX(), localValue63.getY(), localValue63.getWidth(), localValue63.getHeight(), CornerRadii.internalMethod03908(4.0F), localValue66.mulAlpha(localValue67 ? 0.22F : 0.14F)
               );
               localValue1.drawCenteredText(
                  Fonts.internalField1154.internalMethod01432(7.0F),
                  LanguageManager.internalMethod07214("rocknet.share.add"),
                  localValue63.getX() + localValue63.getWidth() / 2.0F,
                  localValue63.getY() + 4.0F,
                  localValue66
               );
               if (localValue67) {
                  CursorManager.internalMethod06882(CursorType.internalField0567);
               }
            }

            localValue24.rect(new Rect(localValue57, localValue55, localValue51, localValue53));
            localValue24.nameRect(Rect.EMPTY);
            localValue16 += (localValue53 + 2.0F) * localValue25;
            localValue17 = localValue24.author().username();
         } else {
            if (localValue24 instanceof CordsMessage localValue30) {
               String localValue31 = LanguageManager.internalMethod00160("rocknet.menu.coordinates", localValue30.pos().getX(), localValue30.pos().getY(), localValue30.pos().getZ());
               float localValue32 = Math.max(120.0F, Math.min(localValue11, localValue12.internalMethod00965(localValue31) + 16.0F));
               float localValue33 = 49.0F;
               float localValue34 = localValue9 - localValue33 * localValue25 - localValue16 + localValue15;
               float localValue35 = localValue26 ? localValue8 - localValue32 : localValue7 + 18.0F + 5.0F;
               boolean localValue36 = localValue34 + localValue33 > localValue2.y() && localValue34 < localValue2.y() + localValue2.h();
               if (localValue36) {
                  this.drawBubble(localValue1, localValue35, localValue34, localValue32, localValue33, localValue26, localValue28, localValue27);
                   float localValue37 = localValue35 + 8.0F;
                  ColorRGBA localValue38 = this.bubbleText(localValue26);
                  localValue1.drawIcon("hud/world", localValue37, localValue34 + 7.0F, 6.0F, localValue38);
                  localValue1.drawText(
                     Fonts.internalField0449.internalMethod01432(8.0F),
                     LanguageManager.internalMethod07214("rocknet.menu.geolocation"),
                     localValue37 + 9.0F,
                     localValue34 + 7.0F,
                     localValue38
                  );
                  localValue1.drawText(localValue12, localValue31, localValue37, localValue34 + 18.0F, localValue38.mulAlpha(0.7F));
                  boolean localValue39 = UiUtils.internalMethod06450(localValue37, localValue34 + 29.0F, localValue32 - 16.0F, 13.0, localValue1);
                  localValue1.drawRoundedRect(localValue37, localValue34 + 29.0F, localValue32 - 16.0F, 13.0F, CornerRadii.internalMethod03908(4.0F), localValue38.mulAlpha(localValue39 ? 0.22F : 0.14F));
                  localValue1.drawCenteredText(
                     Fonts.internalField1154.internalMethod01432(7.0F),
                     LanguageManager.internalMethod07214("rocknet.menu.create_waypoint"),
                     localValue37 + (localValue32 - 16.0F) / 2.0F,
                     localValue34 + 33.0F,
                     localValue38
                  );
                  if (localValue39) {
                     CursorManager.internalMethod06882(CursorType.internalField0567);
                  }
               }

               localValue24.rect(new Rect(localValue35, localValue34, localValue32, localValue33));
               localValue16 += (localValue33 + 2.0F) * localValue25;
               localValue17 = localValue24.author().username();
               if (localValue24.text().isBlank()) {
                  continue;
               }

               localValue27 = false;
               localValue28 = localValue3.isFirstMessage(localValue22);
               localValue29 = localValue28 && !localValue26;
            }

            Message.InternalType0020 localValue45 = layout(localValue24, localValue12, localValue11 - 16.0F);
            List localValue47 = localValue45.lines();
            float localValue49 = localValue45.width() + 16.0F;
            float localValue52 = localValue29 ? nameWidth(localValue24, localValue13) : 0.0F;
            if (localValue29) {
               localValue49 = Math.max(localValue49, localValue52 + 16.0F);
            }

            if (localValue24 instanceof ReplyMessage) {
               localValue49 = Math.max(localValue49, 120.0F);
            }

            localValue49 = Math.min(localValue49, localValue11);
            float localValue54 = 14.0F
               + (localValue12.internalMethod04890() + localValue14) * localValue47.size()
               - (localValue47.isEmpty() ? 0.0F : localValue14)
               + (localValue29 ? localValue13.internalMethod04890() + 4.0F : 0.0F)
               + (localValue24 instanceof ReplyMessage ? 27.0F : 0.0F);
            float localValue56 = localValue9 - localValue54 * localValue25 - localValue16 + localValue15;
            float localValue58 = localValue26 ? localValue8 - localValue49 : localValue7 + 18.0F + 5.0F;
            boolean localValue60 = localValue56 + localValue54 > localValue2.y() && localValue56 < localValue2.y() + localValue2.h();
            float localValue62 = localValue58 + 8.0F;
            if (localValue60) {
               this.drawBubble(localValue1, localValue58, localValue56, localValue49, localValue54, localValue26, localValue28, localValue27);
            }

            localValue24.nameRect(localValue29 ? new Rect(localValue62, localValue56 + 7.0F, Math.min(localValue49 - 16.0F, localValue52), localValue13.internalMethod04890()) : Rect.EMPTY);
            if (!(localValue24 instanceof CordsMessage)) {
               localValue24.rect(new Rect(localValue58, localValue56, localValue49, localValue54));
            }

            if (localValue24 instanceof ReplyMessage localValue64) {
               localValue64.quoteRect(new Rect(localValue62, localValue56 + 7.0F + (localValue29 ? localValue13.internalMethod04890() + 4.0F : 0.0F), localValue49 - 16.0F, 23.0F));
            }

            if (localValue60) {
               ColorRGBA localValue65 = this.bubbleText(localValue26);
               float localValue40 = localValue56 + 7.0F;
               if (localValue29) {
                  CosmeticRender.draw(localValue1, localValue13, localValue24.author().username(), localValue62, localValue40, localValue24.author().nickStyle(), localValue24.author().badge(), localValue65, 8.0F);
                  localValue40 += localValue13.internalMethod04890() + 4.0F;
               }

               if (localValue24 instanceof ReplyMessage localValue41) {
                  Message localValue42 = this.currentChat.getMessage(localValue41.reply());
                  if (localValue42 != null) {
                     Rect localValue43 = localValue41.quoteRect();
                     boolean localValue44 = UiUtils.internalMethod06450(localValue43.getX(), localValue43.getY(), localValue43.getWidth(), localValue43.getHeight(), localValue1);
                      localValue1.drawRoundedRect(localValue62, localValue40, localValue49 - 16.0F, 23.0F, CornerRadii.internalMethod03908(5.0F), localValue65.mulAlpha(localValue44 ? 0.18F : 0.1F));
            CosmeticRender.draw(
                        localValue1,
                        Fonts.internalField0449.internalMethod01432(7.0F),
                        localValue42.author().username(),
                        localValue62 + 5.0F,
                        localValue40 + 6.0F,
                        null,
                        null,
                        localValue65,
                        0.0F,
                        localValue49 - 26.0F
                     );
                     localValue1.drawFadeoutText(
                        Fonts.internalField0449.internalMethod01432(7.0F),
                        localValue42.text(),
                        localValue62 + 5.0F,
                        localValue40 + 13.0F,
                        localValue65.mulAlpha(0.5F),
                        0.9F,
                        1.0F,
                        localValue49 - 26.0F
                     );
                     if (localValue44) {
                        CursorManager.internalMethod06882(CursorType.internalField0567);
                     }
                  }

                  localValue40 += 27.0F;
               }

               List localValue68 = null;

               for (String localValue70 : (Iterable<String>)(Iterable<?>)localValue47) {
                  localValue68 = this.drawMessageLine(localValue1, localValue12, localValue70, localValue62, localValue40, localValue65, localValue26, localValue68);
                  localValue40 += localValue12.internalMethod04890() + localValue14;
               }

               localValue24.mentions(localValue68 == null ? List.of() : localValue68);
            }

            localValue16 += (localValue54 + 2.0F) * localValue25;
            localValue17 = localValue24.author().username();
         }
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, localValue18);
      this.drawMentions(localValue1);
      localValue4.internalMethod04308(localValue10 - localValue16);
      ScissorStack.internalMethod07643();
      this.requestHistoryIfNeeded(localValue3, localValue16, localValue10);
   }

   private List<Message.InternalType0423> drawMessageLine(
      UiRenderContext localValue1, SizedFont localValue2, String localValue3, float localValue4, float localValue5, ColorRGBA localValue6, boolean localValue7, List<Message.InternalType0423> localValue8
   ) {
      if (!Mentions.possible(localValue3)) {
         EmojiTextNode.internalMethod06314(localValue1, localValue2, localValue3, localValue4, localValue5, localValue2.internalMethod04890(), localValue6);
         return (List<Message.InternalType0423>)localValue8;
      } else {
         ColorRGBA localValue9 = localValue7 ? localValue6 : accent();
         boolean localValue10 = localValue1.internalMethod05261() >= this.messagesBox.y() && localValue1.internalMethod05261() <= this.messagesBox.y() + this.messagesBox.h();
         float localValue11 = localValue4;

         for (Mentions.InternalType0355 localValue13 : Mentions.split(localValue3)) {
            float localValue14 = EmojiTextNode.internalMethod06011(localValue2, localValue13.text());
            if (!localValue13.isMention()) {
               EmojiTextNode.internalMethod06314(localValue1, localValue2, localValue13.text(), localValue11, localValue5, localValue2.internalMethod04890(), localValue6);
               localValue11 += localValue14;
            } else {
               boolean localValue15 = Mentions.isMe(localValue13.mention());
               localValue1.drawRoundedRect(
                  localValue11 - 1.5F,
                  localValue5 - 1.5F,
                  localValue14 + 3.0F,
                  localValue2.internalMethod04890() + 3.0F,
                  CornerRadii.internalMethod03908(3.0F),
                  localValue9.mulAlpha(localValue15 ? 0.32F : 0.16F)
               );
               EmojiTextNode.internalMethod06314(localValue1, localValue2, localValue13.text(), localValue11, localValue5, localValue2.internalMethod04890(), localValue9);
               Rect localValue16 = new Rect(localValue11, localValue5, localValue14, localValue2.internalMethod04890());
               if (localValue8 == null) {
                  localValue8 = new ArrayList(2);
               }

               localValue8.add(new Message.InternalType0423(localValue13.mention(), localValue16));
               if (localValue10 && UiUtils.internalMethod01807(localValue16, localValue1.internalMethod05259(), localValue1.internalMethod05261())) {
                  CursorManager.internalMethod06882(CursorType.internalField0567);
               }

               localValue11 += localValue14;
            }
         }

         return (List<Message.InternalType0423>)localValue8;
      }
   }

   private static Message.InternalType0020 layout(Message localValue0, SizedFont localValue1, float localValue2) {
      Message.InternalType0020 localValue3 = localValue0.layout();
      if (localValue3 != null && Math.abs(localValue3.maxWidth() - localValue2) < 0.01F) {
         return localValue3;
      } else {
         List localValue4 = wrapText(localValue0.text(), localValue2);
         float localValue5 = 0.0F;

         for (String localValue7 : (Iterable<String>)(Iterable<?>)localValue4) {
            localValue5 = Math.max(localValue5, lineWidth(localValue1, localValue7));
         }

         Message.InternalType0020 localValue8 = new Message.InternalType0020(localValue2, localValue4, localValue5);
         localValue0.layout(localValue8);
         return localValue8;
      }
   }

   private static float nameWidth(Message localValue0, SizedFont localValue1) {
      float localValue2 = localValue0.nameWidth();
      if (localValue2 >= 0.0F) {
         return localValue2;
      } else {
         float localValue3 = CosmeticRender.width(localValue1, localValue0.author().username(), localValue0.author().badge(), 8.0F);
         localValue0.nameWidth(localValue3);
         return localValue3;
      }
   }

   private static float lineWidth(SizedFont localValue0, String localValue1) {
      if (!Mentions.possible(localValue1)) {
         return EmojiTextNode.internalMethod06011(localValue0, localValue1);
      } else {
         float localValue2 = 0.0F;

         for (Mentions.InternalType0355 localValue4 : Mentions.split(localValue1)) {
            localValue2 += EmojiTextNode.internalMethod06011(localValue0, localValue4.text());
         }

         return localValue2;
      }
   }

   private void requestHistoryIfNeeded(Chat localValue1, float localValue2, float localValue3) {
      if (localValue1.isMoreHistory() && localValue1.size() < 400) {
         boolean localValue4 = localValue2 <= localValue3 || localValue1.getScrollHandler().internalMethod02321() >= localValue2 - localValue3 - 60.0F;
         if (localValue4) {
            long localValue5 = System.currentTimeMillis();
            if (localValue5 - localValue1.getHistoryRequestedAt() >= 1500L) {
               localValue1.setHistoryRequestedAt(localValue5);
               long localValue7 = localValue1.oldestId();
               RockstarClient.getInstance()
                  .internalMethod06050()
                  .send(
                     (Packet)(localValue1.getName().equals(GLOBAL_CHAT_ID) ? new Packets.InternalType0182(localValue7) : new Packets.InternalType0067(localValue1.getName(), localValue7))
                  );
            }
         }
      }
   }

   private void drawBubble(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4, float localValue5, boolean localValue6, boolean localValue7, boolean localValue8) {
      float localValue9 = 8.0F;
      float localValue10 = 3.0F;
      CornerRadii localValue11 = localValue6
         ? new CornerRadii(localValue9, localValue7 ? localValue9 : localValue10, localValue8 ? localValue9 : localValue10, localValue9)
         : new CornerRadii(localValue7 ? localValue9 : localValue10, localValue9, localValue9, localValue8 ? localValue9 : localValue10);
      localValue1.drawRoundedRect(localValue2, localValue3, localValue4, localValue5, localValue11, localValue6 ? accent() : inset());
   }

   private ColorRGBA bubbleText(boolean localValue1) {
      return localValue1 ? onAccent() : text();
   }

   private void clickMessages(MouseButton localValue1, float localValue2, float localValue3) {
      if (!this.clickMentions(localValue1, localValue2, localValue3)) {
         List localValue4 = this.currentChat.messageIdsSnapshot();

         for (int localValue5 = localValue4.size() - 1; localValue5 >= 0; localValue5--) {
            long localValue6 = (Long)localValue4.get(localValue5);
            Message localValue8 = this.currentChat.getMessage(localValue6);
            if (localValue8 != null) {
               Rect localValue9 = localValue8.rect();
               if (!(localValue9.getY() + localValue9.getHeight() < this.messagesBox.y()) && !(localValue9.getY() > this.messagesBox.y() + this.messagesBox.h())) {
                  if (localValue1 == MouseButton.internalField0102) {
                     for (Message.InternalType0423 localValue11 : localValue8.mentions()) {
                        if (UiUtils.internalMethod01807(localValue11.rect(), localValue2, localValue3)) {
                           this.openProfile(localValue11.username());
                           return;
                        }
                     }
                  }

                  if (UiUtils.internalMethod01807(localValue8.nameRect(), localValue2, localValue3)) {
                     String localValue16 = localValue8.author().username();
                     if (localValue1 == MouseButton.internalField0101) {
                        this.openModerationMenu(localValue16);
                     } else if (localValue1 == MouseButton.internalField0102) {
                        this.openProfile(localValue16);
                     }

                     return;
                  }

                  if (localValue8 instanceof CordsMessage localValue12
                     && localValue1 == MouseButton.internalField0102
                     && UiUtils.internalMethod05785(localValue9.getX() + 8.0F, localValue9.getY() + 29.0F, localValue9.getWidth() - 16.0F, 13.0, localValue2, localValue3)) {
                     RockstarClient.getInstance()
                        .internalMethod06121()
                        .internalMethod05412(localValue12.author().username(), localValue12.pos().getX(), localValue12.pos().getY(), localValue12.pos().getZ());
                     return;
                  }

                  if (localValue8 instanceof ReplyMessage localValue13
                     && localValue1 == MouseButton.internalField0102
                     && UiUtils.internalMethod01807(localValue13.quoteRect(), localValue2, localValue3)) {
                     this.scrollToMessage(localValue13.reply());
                     return;
                  }

                  if (localValue8 instanceof ShareMessage localValue14
                     && localValue1 == MouseButton.internalField0102
                     && UiUtils.internalMethod01807(localValue14.actionRect(), localValue2, localValue3)) {
                     RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0127(localValue14.shareId()));
                     return;
                  }

                  if (localValue1 == MouseButton.internalField0101 && UiUtils.internalMethod01807(localValue9, localValue2, localValue3) && !localValue8.text().isBlank()) {
                     ArrayList localValue15 = new ArrayList();
                     localValue15.add(new RocknetMenu.InternalType0364("back", LanguageManager.internalMethod07214("rocknet.menu.reply_button"), false, () -> {
                        this.setReply(localValue6);
                        this.lastReply = localValue6;
                     }));
                     if (Information.staff()) {
                        localValue15.add(
                           new RocknetMenu.InternalType0364(
                              "trash",
                              LanguageManager.internalMethod07214("rocknet.mod.delete"),
                              true,
                              () -> RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0383(localValue6))
                           )
                        );
                     }

                     this.showMenu(localValue8.text(), localValue15);
                     return;
                  }
               }
            }
         }
      }
   }

   private void scrollToMessage(long localValue1) {
      Message localValue3 = this.currentChat.getMessage(localValue1);
      if (localValue3 != null && this.messagesBox != null) {
         Rect localValue4 = localValue3.rect();
         if (localValue4 != null && !(localValue4.getHeight() <= 0.0F)) {
            SmoothScrollHandler localValue5 = this.currentChat.getScrollHandler();
            float localValue6 = this.messagesBox.y() + (this.messagesBox.h() - localValue4.getHeight()) / 2.0F;
            double localValue7 = -localValue5.internalMethod02321() - (localValue6 - localValue4.getY());
            localValue5.internalMethod09052(Math.max(localValue5.internalMethod02327(), Math.min(0.0, localValue7)));
            localValue5.internalMethod09059(0.0);
         }
      }
   }

   private String muteNotice() {
      long localValue1 = Information.getMuteUntil();
      return localValue1 < 0L
         ? LanguageManager.internalMethod07214("rocknet.mod.muted_forever")
         : LanguageManager.internalMethod00160("rocknet.mod.muted_until", TIME.format(new Date(localValue1)));
   }

   private void sendMessage() {
      if (!Information.muted()) {
         String localValue1 = renderable(this.sendField.internalMethod06202());
         if (!localValue1.isBlank()) {
            String localValue2 = this.reply == -1L ? localValue1 : replyPrefix(this.reply) + localValue1;
            if (localValue2.length() <= this.chatLimit()) {
               this.setReply(-1L);
               this.send(localValue2);
               this.sendField.internalMethod09126();
            }
         }
      }
   }

   private void send(String localValue1) {
      try {
         RockstarClient.getInstance()
            .internalMethod06050()
            .send(
               (Packet)(this.currentChat.getName().equals(GLOBAL_CHAT_ID)
                  ? new Packets.InternalType0073(localValue1)
                  : new Packets.InternalType0420(this.currentChat.getName(), localValue1))
            );
      } catch (RuntimeException localValue3) {
         RockstarClient.internalField0572
            .warn(
               "Globals: \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u043d\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e",
               localValue3
            );
         return;
      }

      this.currentChat.scrollToBottom();
   }

   private int chatLimit() {
      return this.currentChat.getName().equals(GLOBAL_CHAT_ID) ? 200 : 500;
   }

   private static String replyPrefix(long localValue0) {
      return "${reply=" + localValue0 + "}";
   }

   private void setReply(long localValue1) {
      this.reply = localValue1;
      int localValue3 = this.chatLimit() - (localValue1 == -1L ? 0 : replyPrefix(localValue1).length());
      this.sendField.internalMethod07506(Math.max(1, localValue3));
   }

   private static String renderable(String localValue0) {
      return localValue0 == null ? "" : localValue0.trim();
   }

   private void sendFriendRequest() {
      if (!this.friendField.internalMethod06202().isBlank()) {
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0007(this.friendField.internalMethod06202().trim()));
         this.friendField.internalMethod09126();
      }
   }

   private void openChat(String localValue1) {
      Chat localValue2 = Information.byName(localValue1);
      if (localValue2 != this.currentChat) {
         this.currentChat = localValue2;
         this.currentChat.resetMessageAnimations();
         this.currentChat.getScrollHandler().internalMethod02328();
         this.setReply(-1L);
         this.rebuildSidebar(true);
      }
   }

   private void openActionsMenu() {
      this.showMenu(
         LanguageManager.internalMethod07214("rocknet.menu.actions"),
         List.of(
            new RocknetMenu.InternalType0364(
               "hud/world",
               LanguageManager.internalMethod07214("rocknet.menu.coordinates_button"),
               false,
               () -> {
                  this.send(
                     String.format(
                           "${cords=%s,%s,%s}",
                           internalField0149.player.getBlockPos().getX(),
                           internalField0149.player.getBlockPos().getY(),
                           internalField0149.player.getBlockPos().getZ()
                        )
                        + this.sendField.internalMethod06202()
                  );
                  this.sendField.internalMethod09126();
               }
            ),
            new RocknetMenu.InternalType0364("setting", LanguageManager.internalMethod07214("rocknet.share.config"), false, this::openConfigPicker),
            new RocknetMenu.InternalType0364("hud/target", LanguageManager.internalMethod07214("rocknet.share.swing"), false, this::openSwingPicker),
            new RocknetMenu.InternalType0364("menu/builder", LanguageManager.internalMethod07214("rocknet.share.invbuilder"), false, this::openInvBuilderPicker)
         )
      );
   }

   private void openConfigPicker() {
      ArrayList localValue1 = new ArrayList();

      for (Packets.InternalType0490 localValue3 : RockstarClient.getInstance().internalMethod02152().internalMethod01753()) {
         localValue1.add(
            new RocknetMenu.InternalType0364(
               "setting",
               localValue3.name(),
               false,
               () -> RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0072(localValue3.id(), this.shareTarget()))
            )
         );
      }

      this.showMenu(LanguageManager.internalMethod07214("rocknet.share.config"), localValue1);
   }

   private void openSwingPicker() {
      SwingPresetManager localValue1 = RockstarClient.getInstance().internalMethod01001();
      localValue1.internalMethod08675();
      ArrayList localValue2 = new ArrayList();

      for (SwingPresetConfig localValue4 : localValue1.internalMethod01720()) {
         if (!localValue4.internalMethod02141().equals("autosave")) {
            localValue2.add(new RocknetMenu.InternalType0364("hud/target", localValue4.internalMethod02141(), false, () -> this.sendSwingPreset(localValue4)));
         }
      }

      this.showMenu(LanguageManager.internalMethod07214("rocknet.share.swing"), localValue2);
   }

   private void openInvBuilderPicker() {
      InventoryBuilderModule.internalMethod09170();
      ArrayList localValue1 = new ArrayList();

      for (InventoryBuilderModule.InternalType0024 localValue3 : InventoryBuilderModule.internalMethod05853()) {
         if (localValue3.internalMethod01393() != 0) {
            localValue1.add(
               new RocknetMenu.InternalType0364(
                  "menu/builder",
                  localValue3.internalField0248,
                  false,
                  () -> RockstarClient.getInstance()
                     .internalMethod06050()
                     .send(new Packets.InternalType0071(localValue3.internalField0248, InventoryBuilderModule.internalMethod03299(localValue3), this.shareTarget()))
               )
            );
         }
      }

      this.showMenu(LanguageManager.internalMethod07214("rocknet.share.invbuilder"), localValue1);
   }

   private void sendSwingPreset(SwingPresetConfig localValue1) {
      try {
         String localValue2 = Files.readString(localValue1.internalMethod03356().toPath(), StandardCharsets.UTF_8);
         RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0496(localValue1.internalMethod02141(), localValue2, this.shareTarget()));
      } catch (IOException localValue3) {
         RockstarClient.internalField0572
            .error(
               "rocknet: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0440\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u043f\u0440\u0435\u0441\u0435\u0442 \u2014 {}",
               localValue3.getMessage()
            );
      }
   }

   private String shareTarget() {
      return this.currentChat.getName().equals(GLOBAL_CHAT_ID) ? "" : this.currentChat.getName();
   }

   private void openFriendMenu(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         ArrayList localValue3 = new ArrayList();
         localValue3.add(new RocknetMenu.InternalType0364("plane", LanguageManager.internalMethod07214("rocknet.menu.open_chat"), false, () -> this.openChat(localValue1)));
         String localValue4 = this.serverAddress(localValue2);
         if (localValue4 != null && !WorldKey.sameWorld(localValue2.gameInfo())) {
            localValue3.add(
               new RocknetMenu.InternalType0364(
                  "hud/world",
                  LanguageManager.internalMethod07214("rocknet.menu.connect"),
                  false,
                  () -> {
                     ServerInfo localValue2x = new ServerInfo(localValue2.gameInfo().ip(), localValue4, ServerType.OTHER);
                     internalField0149.disconnect(net.minecraft.client.world.ClientWorld.QUITTING_MULTIPLAYER_TEXT);
                     internalField0149.execute(
                        () -> ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), internalField0149, ServerAddress.parse(localValue4), localValue2x, false, null)
                     );
                  }
               )
            );
         }

         localValue3.add(
            new RocknetMenu.InternalType0364(
               "trash",
               LanguageManager.internalMethod07214("remove"),
               true,
               () -> RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0236(localValue1))
            )
         );
         this.showMenu(localValue1, localValue3);
      }
   }

   private String serverAddress(Packets.InternalType0018 localValue1) {
      if (Activities.onServer(localValue1.activity()) && localValue1.gameInfo() != null) {
         String localValue2 = localValue1.gameInfo().ip();
         if (localValue2 != null && !localValue2.isBlank() && !localValue2.equals("single") && !localValue2.equals("???")) {
            return localValue2.indexOf(58) < 0 ? localValue2 + ":25565" : localValue2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void showMenu(String localValue1, List<RocknetMenu.InternalType0364> localValue2) {
      SizedFont localValue3 = Fonts.internalField1154.internalMethod01432(7.0F);
      float localValue4 = 84.0F;

      for (RocknetMenu.InternalType0364 localValue6 : localValue2) {
         localValue4 = Math.max(localValue4, 25.0F + localValue3.internalMethod00965(localValue6.label()) + 4.0F);
      }

      if (localValue1 != null) {
         localValue4 = Math.max(localValue4, 14.0F + Fonts.internalField0449.internalMethod01432(7.0F).internalMethod00965(localValue1) + 4.0F);
      }

      localValue4 = Math.min(150.0F, localValue4);
      float localValue17 = localValue2.isEmpty() ? 22.0F : localValue2.size() * 16.0F + (localValue2.size() - 1) * 2.0F;
      int localValue18 = Math.max(1, 5);
      float localValue7 = Math.min(localValue18 * 18.0F - 2.0F, localValue17);
      float localValue8 = 6.0F + localValue7 + (localValue1 == null ? 0.0F : 16.0F);
      UiContainer localValue9 = new UiContainer().internalMethod01863().internalMethod03062(2.0F).internalMethod09609().internalMethod09266(localValue7);
      if (localValue17 > localValue7) {
         localValue9.internalMethod08755()
            .internalMethod01416(ScrollPolicy.internalField0917)
            .internalMethod05391(
               localValue0 -> localValue0.internalMethod02712(-1.0F)
                  .internalMethod09005(2.0F)
                  .internalMethod00894(2.0F)
                  .internalMethod08056(12.0F)
                  .internalMethod07954(1.0F)
                  .internalMethod08313(1000.0F)
                  .internalMethod04404(localValue0x -> stroke().mix(second(), localValue0x.internalMethod05170() + localValue0x.internalMethod05173()))
            );
      }

      if (localValue2.isEmpty()) {
         localValue9.internalMethod03907(
            new UiElement()
               .height(22.0F)
               .fillWidth()
               .text(localValue3, () -> LanguageManager.internalMethod07214("rocknet.share.empty"), localValue0 -> second())
               .textAlign(TextAlignment.internalField0621)
               .interactive(false)
         );
      }

      for (RocknetMenu.InternalType0364 localValue11 : localValue2) {
         localValue9.internalMethod03907(this.menuItem(localValue11, localValue3));
      }

      UiContainer localValue19 = new UiContainer()
         .internalMethod01863()
         .internalMethod03062(2.0F)
         .internalMethod07351(3.0F)
         .internalMethod03995(localValue4, localValue8)
         .internalMethod07178((localValue0, localValue1x) -> {
            localValue0.drawShadow(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), 20.0F, CornerRadii.internalMethod03908(8.0F), ColorRGBA.BLACK.withAlpha(191.25F));
            localValue0.drawRoundedRect(localValue1x.x(), localValue1x.y(), localValue1x.w(), localValue1x.h(), CornerRadii.internalMethod03908(8.0F), inset());
         })
         .internalMethod07914(MENU_OPEN)
         .internalMethod05305(MENU_MOTION);
      if (localValue1 != null) {
         localValue19.internalMethod03907(
            new UiElement()
               .height(14.0F)
               .fillWidth()
               .padding(0.0F, 4.0F)
               .text(Fonts.internalField0449.internalMethod01432(7.0F), localValue1, localValue0 -> second())
               .fade()
               .interactive(false)
         );
      }

      localValue19.internalMethod03907(localValue9);
      float localValue20 = this.window.x() + 4.0F;
      float localValue12 = this.window.y() + 4.0F;
      float localValue13 = this.window.x() + this.window.w() - localValue4 - 4.0F;
      float localValue14 = this.window.y() + this.window.h() - localValue8 - 4.0F;
      float localValue15 = this.lastMouseY > localValue14 ? this.lastMouseY - localValue8 : this.lastMouseY;
      localValue19.snapAt(Math.max(localValue20, Math.min(localValue13, this.lastMouseX)), Math.max(localValue12, Math.min(Math.max(localValue12, localValue14), localValue15)));
      this.closeMenu();
      localValue19.beginEnter(0.0F);
      this.activeMenu = this.openWindow(localValue19);
   }

   private UiContainer menuItem(RocknetMenu.InternalType0364 localValue1, SizedFont localValue2) {
      UiContainer localValue3 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(5.0F)
         .internalMethod09266(16.0F)
         .internalMethod09609()
         .internalMethod03514(Insets.internalMethod05266(0.0F, 4.0F))
         .internalMethod09018(5.0F)
         .internalMethod08336(MENU_HOVER)
         .internalMethod04332(CursorType.internalField0567);
      localValue3.internalMethod06812(localValue0 -> highlight(0.04F * localValue0.hover()))
         .internalMethod03907(new UiElement().size(6.0F, 6.0F).icon(localValue1.icon(), 6.0F, localValue2x -> menuItemColor(localValue3, localValue1.danger())).interactive(false))
         .internalMethod03907(new UiElement().fillWidth().fade().text(localValue2, localValue1.label(), localValue2x -> menuItemColor(localValue3, localValue1.danger())).interactive(false))
         .internalMethod05690(() -> {
            this.closeMenu();
            localValue1.run().run();
         });
      return localValue3;
   }

   private static ColorRGBA menuItemColor(UiContainer localValue0, boolean localValue1) {
      return second().mix(localValue1 ? DANGER : text(), localValue0.hover());
   }

   private void closeMenu() {
      if (this.activeMenu != null) {
         this.activeMenu.beginExit(0.0F);
         this.activeMenu = null;
      }
   }

   private boolean closeMenuIfOutside(float localValue1, float localValue2) {
      if (this.activeMenu == null) {
         return false;
      } else if (!this.activeMenu.alive()) {
         this.activeMenu = null;
         return false;
      } else if (this.activeMenu.contains(localValue1, localValue2)) {
         return false;
      } else {
         this.closeMenu();
         return true;
      }
   }

   public void tick() {
      GuiMoveModule.internalMethod09597();
      super.tick();
   }

   @Override
   public void render(UiRenderContext localValue1) {
      if (this.window != null) {
         this.lastMouseX = localValue1.internalMethod05259();
         this.lastMouseY = localValue1.internalMethod05261();
         if (!this.closing) {
            this.rebuildSidebar(false);
            this.syncPeers();
            this.syncPeopleQuery();
            this.rebuildPeople();
            this.syncMentions();
         }

         this.windowX = this.window.x();
         this.windowY = this.window.y();
         this.menuWindow.set(this.windowX, this.windowY, this.window.w(), this.window.h());
         this.menuAnimation.internalMethod06645(CIRC_OUT);
         this.menuAnimation.internalMethod07061((long)(this.closing ? 200.0F : 280.0F));
         this.menuAnimation.internalMethod07059(this.closing ? 0.0F : 1.0F);
         float localValue2 = Math.max(0.0F, Math.min(1.0F, this.menuAnimation.internalMethod02881()));
         this.contentAlpha = localValue2;
         float localValue3 = this.closing ? 0.96F : 0.93F;
         float localValue4 = this.closing ? 10.0F : 14.0F;
         float localValue5 = localValue3 + (1.0F - localValue3) * localValue2;
         float localValue6 = (1.0F - localValue2) * localValue4;
         boolean localValue7 = Math.abs(localValue5 - 1.0F) > 1.0E-4F || Math.abs(localValue6) > 1.0E-4F;
         if (localValue7) {
            float localValue8 = this.windowX + this.window.w() / 2.0F;
            float localValue9 = this.windowY + this.window.h() / 2.0F;
            localValue1.getMatrices().pushMatrix();
            localValue1.getMatrices().translate(localValue8, localValue9 + localValue6);
            localValue1.getMatrices().scale(localValue5, localValue5);
            localValue1.getMatrices().translate(-localValue8, -localValue9);
         }

         super.render(localValue1);
         if (localValue7) {
            localValue1.getMatrices().popMatrix();
         }
      }
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      this.lastMouseX = (float)localValue1;
      this.lastMouseY = (float)localValue3;
      if (Information.getUser() == null) {
         this.form.onMouseClicked(localValue1, localValue3, localValue5);
         super.onMouseClicked(localValue1, localValue3, localValue5);
      } else if (!this.closeMenuIfOutside((float)localValue1, (float)localValue3)) {
         if (this.activeMenu != null) {
            super.onMouseClicked(localValue1, localValue3, localValue5);
         } else {
            if (localValue5 != MouseButton.internalField0990) {
               this.sendField.internalMethod01643(localValue1, localValue3, localValue5);
               this.friendField.internalMethod01643(localValue1, localValue3, localValue5);
               this.searchField.internalMethod01643(localValue1, localValue3, localValue5);
               this.peopleField.internalMethod01643(localValue1, localValue3, localValue5);
            }

            super.onMouseClicked(localValue1, localValue3, localValue5);
         }
      }
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      boolean localValue6 = localValue5 == MouseButton.internalField0102 && this.window != null && this.window.dragging();
      if (Information.getUser() == null) {
         this.form.onMouseReleased(localValue1, localValue3, localValue5);
         super.onMouseReleased(localValue1, localValue3, localValue5);
         if (localValue6) {
            this.returnWindowToCenterIfOutside();
         }
      } else {
         if (this.sendField.internalMethod00342()) {
            this.sendField.internalMethod02863(localValue1, localValue3, localValue5);
         }

         if (this.friendField.internalMethod00342()) {
            this.friendField.internalMethod02863(localValue1, localValue3, localValue5);
         }

         if (this.searchField.internalMethod00342()) {
            this.searchField.internalMethod02863(localValue1, localValue3, localValue5);
         }

         if (this.peopleField.internalMethod00342()) {
            this.peopleField.internalMethod02863(localValue1, localValue3, localValue5);
         }

         super.onMouseReleased(localValue1, localValue3, localValue5);
         if (localValue6) {
            this.returnWindowToCenterIfOutside();
         }
      }
   }

   private void returnWindowToCenterIfOutside() {
      float localValue1 = this.window.x();
      float localValue2 = this.window.y();
      float localValue3 = this.window.w();
      float localValue4 = this.window.h();
      float localValue5 = Math.max(0.0F, Math.min((float)this.width, localValue1 + localValue3) - Math.max(0.0F, localValue1));
      float localValue6 = Math.max(0.0F, Math.min((float)this.height, localValue2 + localValue4) - Math.max(0.0F, localValue2));
      if (!(1.0F - localValue5 * localValue6 / Math.max(1.0F, localValue3 * localValue4) < 0.35F)) {
         this.window.internalMethod08296(Math.round((this.width - localValue3) / 2.0F), Math.round((this.height - localValue4) / 2.0F));
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      int keyCode = input.key();
      int scanCode = input.scancode();
      int modifiers = input.modifiers();
      if (keyCode == 256 && this.activeMenu != null && this.activeMenu.alive()) {
         this.closeMutePicker();
         return true;
      } else if (keyCode == 256 && this.profileOpen()) {
         this.closeProfile();
         return true;
      } else if (this.profileOpen()) {
         return true;
      } else if (keyCode == 256 && this.peopleOpen) {
         this.togglePeople();
         return true;
      } else if (Information.getUser() == null) {
         this.form.keyPressed(keyCode, scanCode, modifiers);
         return super.keyPressed(input);
      } else {
         if (!this.mentionOptions.isEmpty() && this.sendField.internalMethod00342()) {
            if (keyCode == 256) {
               this.mentionDismissed = Mentions.typed(this.sendField.internalMethod06202());
               this.setMentionOptions(List.of());
               return true;
            }

            if (keyCode == 264 || keyCode == 265) {
               int localValue4 = keyCode == 264 ? 1 : -1;
               this.mentionIndex = (this.mentionIndex + localValue4 + this.mentionOptions.size()) % this.mentionOptions.size();
               return true;
            }

            if (keyCode == 258 || keyCode == 257) {
               this.applyMention(this.mentionOptions.get(Math.min(this.mentionIndex, this.mentionOptions.size() - 1)));
               return true;
            }
         }

         if (keyCode == 257 && this.sendField.internalMethod00342() && !this.sendField.internalMethod06202().isBlank()) {
            this.sendMessage();
         } else if (this.sendField.internalMethod00342()) {
            this.sendField.internalMethod05727(keyCode, scanCode, modifiers);
         }

         if (keyCode == 257 && this.friendField.internalMethod00342() && !this.friendField.internalMethod06202().isBlank()) {
            this.sendFriendRequest();
         } else if (this.friendField.internalMethod00342()) {
            this.friendField.internalMethod05727(keyCode, scanCode, modifiers);
         }

         if (this.searchField.internalMethod00342()) {
            this.searchField.internalMethod05727(keyCode, scanCode, modifiers);
         }

         if (this.peopleField.internalMethod00342()) {
            this.peopleField.internalMethod05727(keyCode, scanCode, modifiers);
         }

         this.currentChat.getScrollHandler().internalMethod04250(keyCode);
         return super.keyPressed(input);
      }
   }

   @Override
   public boolean charTyped(CharInput input) {
      char chr = (char)input.codepoint();
      int modifiers = input.modifiers();
      if (Information.getUser() == null) {
         this.form.charTyped(chr, modifiers);
         return false;
      } else if (this.profileOpen()) {
         return false;
      } else {
         if (this.sendField.internalMethod00342()) {
            this.sendField.internalMethod05413(chr, modifiers);
         }

         if (this.friendField.internalMethod00342()) {
            this.friendField.internalMethod05413(chr, modifiers);
         }

         if (this.searchField.internalMethod00342()) {
            this.searchField.internalMethod05413(chr, modifiers);
         }

         if (this.peopleField.internalMethod00342()) {
            this.peopleField.internalMethod05413(chr, modifiers);
         }

         return false;
      }
   }

   public void close() {
      this.closing = true;
      RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class).disable();
      RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class).disable();
      SoundsModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class);
      if (localValue1.isEnabled()) {
         ClientSoundEvents.internalField0130.internalMethod03132(localValue1.internalMethod01798(), 1.0F);
      }

      RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
      RockstarClient.getInstance().internalMethod02152().internalMethod07804();
      if (CustomTextField.internalField0936 != null) {
         CustomTextField.internalField0936.internalMethod07508(false);
      }

      super.close();
   }

   public boolean shouldCloseOnEsc() {
      return true;
   }

   private UiElement nickBox(
      SizedFont localValue1, float localValue2, Supplier<String> localValue3, Supplier<String> localValue4, Supplier<String> localValue5, Function<UiElement, ColorRGBA> localValue6
   ) {
      return new UiElement()
         .height(localValue1.internalMethod04890())
         .fillWidth()
         .interactive(false)
         .paint(
            (localValue6x, localValue7) -> CosmeticRender.draw(
               localValue6x, localValue1, (String)localValue3.get(), localValue7.x(), localValue7.y(), (String)localValue4.get(), (String)localValue5.get(), (ColorRGBA)localValue6.apply(localValue7), localValue2, localValue7.w()
            )
         );
   }

   private UiElement iconButton(String localValue1, float localValue2, float localValue3, Runnable localValue4) {
      return new UiElement()
         .size(localValue2, localValue2)
         .padding((localValue2 - localValue3) / 2.0F)
         .radius(7.0F)
         .icon(localValue1, localValue3, localValue0 -> second().mix(onAccent(), localValue0.hover()))
         .background(localValue0 -> card().mix(accent(), localValue0.hover()))
         .hoverMotion(STATE)
         .cursor(CursorType.internalField0567)
         .onClick(localValue4);
   }

   static ColorRGBA page() {
      return new ColorRGBA(8.0F, 8.0F, 11.0F);
   }

   static ColorRGBA card() {
      return new ColorRGBA(12.0F, 12.0F, 15.0F);
   }

   static ColorRGBA inset() {
      return new ColorRGBA(19.0F, 19.0F, 21.0F);
   }

   static ColorRGBA stroke() {
      return new ColorRGBA(113.0F, 113.0F, 123.0F, 63.75F);
   }

   static ColorRGBA stroke10() {
      return new ColorRGBA(113.0F, 113.0F, 123.0F, 25.5F);
   }

   static ColorRGBA text() {
      return new ColorRGBA(231.0F, 231.0F, 253.0F);
   }

   static ColorRGBA second() {
      return new ColorRGBA(113.0F, 113.0F, 123.0F);
   }

   static ColorRGBA accent() {
      return ThemeColors.internalMethod02531().withAlpha(255.0F);
   }

   static ColorRGBA accentHover() {
      return accent().mix(ColorRGBA.BLACK, 0.34F);
   }

   static ColorRGBA onAccent() {
      return ThemeColors.internalMethod03795(accent(), text(), page());
   }

   static ColorRGBA highlight(float localValue0) {
      return ColorRGBA.WHITE.withAlpha(255.0F * localValue0);
   }

   private Packets.InternalType0018 friend(String localValue1) {
      return Information.friend(localValue1);
   }

   private String chatTitle() {
      return this.currentChat.getName().equals(GLOBAL_CHAT_ID) ? LanguageManager.internalMethod07214("rocknet.chat.global.name") : this.currentChat.getName();
   }

   private String chatSubtitle() {
      return !this.currentChat.getName().equals(GLOBAL_CHAT_ID)
         ? this.headStatus(this.currentChat.getName())
         : LanguageManager.internalMethod00160("rocknet.menu.subtitle_online", RocknetListener.getSiteOnline(), RocknetListener.getOnline());
   }

   private String globalPreview() {
      String localValue1 = this.lastMessagePreview(GLOBAL_CHAT_ID);
      return localValue1.isEmpty() ? LanguageManager.internalMethod07214("rocknet.menu.no_messages") : localValue1;
   }

   private String friendStatus(Packets.InternalType0018 localValue1) {
      return this.lastSeenKnown(localValue1) ? RelativeTimeFormatter.internalMethod06419(localValue1.lastSeen()) : Activities.translate(localValue1.activity());
   }

   private String chatStatus(Packets.InternalType0018 localValue1) {
      return this.lastSeenKnown(localValue1)
         ? LanguageManager.internalMethod00160("rocknet.status.last_seen", RelativeTimeFormatter.internalMethod06419(localValue1.lastSeen()))
         : Activities.translate(localValue1.activity());
   }

   private String rowStatus(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         return this.friendStatus(localValue2);
      } else {
         Packets.InternalType0306 localValue3 = Information.peer(localValue1);
         return localValue3 == null ? this.lastMessagePreview(localValue1) : this.peerStatus(localValue3, false);
      }
   }

   private String headStatus(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         return this.chatStatus(localValue2);
      } else {
         Packets.InternalType0306 localValue3 = Information.peer(localValue1);
         return localValue3 == null ? LanguageManager.internalMethod07214("rocknet.people.loading") : this.peerStatus(localValue3, true);
      }
   }

   private String peerStatus(Packets.InternalType0306 localValue1, boolean localValue2) {
      if (!localValue1.status().isBlank()) {
         return Activities.translate(localValue1.status());
      } else if (localValue1.inGame()) {
         return LanguageManager.internalMethod07214("rocknet.people.online");
      } else if (localValue1.online()) {
         return LanguageManager.internalMethod07214("rocknet.status.website");
      } else if (localValue1.lastSeen() <= 0L) {
         return LanguageManager.internalMethod07214("rocknet.status.offline");
      } else {
         return localValue2
            ? LanguageManager.internalMethod00160("rocknet.status.last_seen", RelativeTimeFormatter.internalMethod06419(localValue1.lastSeen()))
            : RelativeTimeFormatter.internalMethod06419(localValue1.lastSeen());
      }
   }

   private boolean online(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         return !Activities.offline(localValue2.activity());
      } else {
         Packets.InternalType0306 localValue3 = Information.peer(localValue1);
         return localValue3 != null && localValue3.online();
      }
   }

   private String nickStyle(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         return localValue2.nickStyle();
      } else {
         Packets.InternalType0306 localValue3 = Information.peer(localValue1);
         return localValue3 == null ? null : localValue3.nickStyle();
      }
   }

   private String badge(String localValue1) {
      Packets.InternalType0018 localValue2 = this.friend(localValue1);
      if (localValue2 != null) {
         return localValue2.badge();
      } else {
         Packets.InternalType0306 localValue3 = Information.peer(localValue1);
         return localValue3 == null ? null : localValue3.badge();
      }
   }

   private String lastMessagePreview(String localValue1) {
      Message localValue2 = Information.byName(localValue1).getLatestMessage();
      if (localValue2 == null) {
         return "";
      } else {
         String localValue3 = localValue2.self() ? LanguageManager.internalMethod07214("rocknet.menu.you") : localValue2.author().username();
         return LanguageManager.internalMethod00160("rocknet.menu.last_message", localValue3, localValue2.text());
      }
   }

   private boolean lastSeenKnown(Packets.InternalType0018 localValue1) {
      return Activities.offline(localValue1.activity()) && localValue1.lastSeen() > 0L;
   }

   public static List<String> wrapText(String localValue0, float localValue1) {
      ArrayList localValue2 = new ArrayList();
      String[] localValue3 = localValue0.split(" ");
      String localValue4 = "";

      for (String localValue8 : localValue3) {
         String localValue9 = localValue4.isEmpty() ? localValue8 : localValue4 + " " + localValue8;
         float localValue10 = EmojiTextNode.internalMethod06011(Fonts.internalField1154.internalMethod01432(8.0F), localValue9);
         if (localValue10 <= localValue1) {
            localValue4 = localValue9;
         } else {
            if (!localValue4.isEmpty()) {
               localValue2.add(localValue4);
               localValue4 = "";
            }

            float localValue11 = EmojiTextNode.internalMethod06011(Fonts.internalField1154.internalMethod01432(8.0F), localValue8);
            if (localValue11 <= localValue1) {
               localValue4 = localValue8;
            } else {
               String localValue12 = "";
               Matcher localValue13 = Pattern.compile("\\X").matcher(localValue8);

               while (localValue13.find()) {
                  String localValue14 = localValue13.group();
                  String localValue15 = localValue12 + localValue14;
                  float localValue16 = EmojiTextNode.internalMethod06011(Fonts.internalField1154.internalMethod01432(8.0F), localValue15);
                  if (localValue16 <= localValue1) {
                     localValue12 = localValue15;
                  } else {
                     if (!localValue12.isEmpty()) {
                        localValue2.add(localValue12);
                     }

                     localValue12 = localValue14;
                  }
               }

               if (!localValue12.isEmpty()) {
                  localValue4 = localValue12;
               }
            }
         }
      }

      if (!localValue4.isEmpty()) {
         localValue2.add(localValue4);
      }

      if (localValue2.isEmpty()) {
         localValue2.add("");
      }

      return localValue2;
   }

   @Generated
   public Rect getMenuWindow() {
      return this.menuWindow;
   }

   static final class InternalType0364 {
      private final String icon;
      private final String label;
      private final boolean danger;
      private final Runnable run;

      InternalType0364(String localValue1, String localValue2, boolean localValue3, Runnable localValue4) {
         this.icon = localValue1;
         this.label = localValue2;
         this.danger = localValue3;
         this.run = localValue4;
      }

      @Override
      public final String toString() {
         return "InternalType0364[icon=" + this.icon() + ", label=" + this.label() + ", danger=" + this.danger() + ", run=" + this.run() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.icon());
         result = 31 * result + java.util.Objects.hashCode(this.label());
         result = 31 * result + java.util.Objects.hashCode(this.danger());
         result = 31 * result + java.util.Objects.hashCode(this.run());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         RocknetMenu.InternalType0364 other = (RocknetMenu.InternalType0364) localValue1;
         return java.util.Objects.equals(this.icon(), other.icon())
            && java.util.Objects.equals(this.label(), other.label())
            && java.util.Objects.equals(this.danger(), other.danger())
            && java.util.Objects.equals(this.run(), other.run());
      }

      public String icon() {
         return this.icon;
      }

      public String label() {
         return this.label;
      }

      public boolean danger() {
         return this.danger;
      }

      public Runnable run() {
         return this.run;
      }
   }

   static enum InternalType0382 {
      CHATS,
      FRIENDS;
   }
}
