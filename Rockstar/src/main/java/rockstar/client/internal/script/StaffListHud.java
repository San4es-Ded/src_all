package rockstar.client.internal.script;








import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import pyrock.utility.render.ColorRGBA;

public class StaffListHud extends AbstractHudWidget {
   private static final Set<String> internalField0546 = Set.of(
      "\u029c\u1d07\u029f\u1d18\u1d07\u0280",
      "developer",
      "moder",
      "moder+",
      "moderator",
      "ml.moder",
      "st.moder",
      "gl.moder",
      "admin",
      "administrator",
      "st helper",
      "d helper",
      "d.helper",
      "helper",
      "media",
      "owner",
      "staff",
      "support",
      "yt",
      "youtube",
      "youtuber",
      "tiktok",
      "\u0445\u0435\u043b\u043f\u0435\u0440",
      "\u0441\u0442. \u043c\u043e\u0434\u0435\u0440",
      "\u0441\u0442. \u0441\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a",
      "\u0441\u0442. \u0441\u0442\u0430\u0436\u0435\u0440",
      "\u0433\u043b. \u043c\u043e\u0434\u0435\u0440",
      "\u0433\u043b. \u0430\u0434\u043c\u0438\u043d",
      "\u0441\u0442.\u043c\u043e\u0434\u0435\u0440",
      "\u0441\u0442.\u0441\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a",
      "\u0441\u0442.\u0441\u0442\u0430\u0436\u0435\u0440",
      "\u0433\u043b.\u043c\u043e\u0434\u0435\u0440",
      "\u0433\u043b.\u0430\u0434\u043c\u0438\u043d",
      "\u0441\u0442. \u043c\u043e\u0434\u0451\u0440",
      "\u0441\u0442. \u0441\u0442\u0430\u0436\u0451\u0440",
      "\u0433\u043b. \u043c\u043e\u0434\u0451\u0440",
      "\u043c\u043e\u0434\u0435\u0440",
      "\u043co\u0434e\u0440",
      "\u0441\u0442\u0430\u0436\u0435\u0440",
      "\u0441\u0442\u0430\u0436\u0451\u0440",
      "\u0430\u0434\u043c\u0438\u043d",
      "\u0441\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a",
      "\u043a\u0443\u0440\u0430\u0442\u043e\u0440",
      "\u043c\u043b.\u0441\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a",
      "\u043f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430",
      "\u043c\u043e\u0434\u0435\u0440\u0430\u0442\u043e\u0440",
      "\u0441\u043f\u0435\u043a\u0442\u0430\u0442\u043e\u0440",
      "\ua509",
      "\ua513",
      "\ua517",
      "\ua521",
      "\ua525"
   );
   private final BooleanSetting internalField0650 = new BooleanSetting(this, "hud.always_display");
   private final SharedBooleanSetting internalField0381 = new SharedBooleanSetting(this, "hud.uniform_width");
   private final List<StaffListHud.InternalType0298> internalField0416 = new ArrayList<>();
   private final Map<String, StaffListHud.InternalType0298> internalField0543 = new HashMap<>();
   private final Map<String, UiNode> internalField0544 = new HashMap<>();
   private final Set<String> internalField0545 = new HashSet<>();
   private boolean internalField0277;
   private UiContainer internalField0634;
   private UiNode internalField0633;
   private final UiTransition internalField0918 = (localValue1, localValue2, localValue3) -> {
      localValue3.internalField0205 = localValue1;
      localValue3.internalField0206 = (this.internalField0277 ? 6.0F : -6.0F) * (1.0F - localValue1);
   };

   public StaffListHud() {
      super("hud.staff_list", "hud/staff");
   }

   @Override
   public UiContainer build() {
      this.internalField0544.clear();
      this.internalField0545.clear();
      this.internalField0634 = new UiContainer().internalMethod01863().internalMethod01855(this.internalMethod01793()).internalMethod03062(1.0F);
      this.internalField0633 = new UiContainer()
         .internalMethod05895()
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod03062(3.0F)
         .internalMethod09266(11.0F)
         .internalMethod03514(Insets.internalMethod02086(3.0F))
         .internalMethod07178(
            (localValue1, localValue2) -> {
               localValue1.drawClientRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), this.animation.internalMethod02881(), this.dragAnim.internalMethod02881(), 3.0F, 3.0F);
               localValue1.drawSquircle(
                  localValue2.x(),
                  localValue2.y(),
                  23.0F,
                  localValue2.h(),
                  3.0F,
                  CornerRadii.internalMethod03908(3.0F),
                  new VerticalColorGradient(ThemeColors.internalField1610.mulAlpha(0.1F), ThemeColors.internalField1610.mulAlpha(0.0F))
               );
            }
         )
         .internalMethod03907(new UiElement().size(7.0F, 7.0F).interactive(false).icon("hud/staff", 7.0F, ThemeColors.internalField1310))
         .internalMethod03907(
            new UiElement()
               .interactive(false)
               .text(
                  Fonts.internalField0449.internalMethod01432(6.0F),
                  () -> LanguageManager.internalMethod07214(this.name),
                  localValue0 -> ThemeColors.internalField1613
               )
         );
      this.internalField0634.internalMethod03907(this.internalField0633);
      this.internalMethod06238();
      return this.internalField0634;
   }

   private UiNode internalMethod05667(String localValue1) {
      UiNode localValue2 = this.internalField0544
         .computeIfAbsent(
            localValue1,
            localValue1x -> {
               SizedFont localValue2x = Fonts.internalField1154.internalMethod01432(6.0F);
               return new UiContainer()
                  .internalMethod05895()
                  .internalMethod01855(TextAlignment.internalField0621)
                  .internalMethod03062(1.0F)
                  .internalMethod07914(this.internalField0918)
                  .internalMethod03907(
                     new UiElement()
                        .interactive(false)
                        .height(10.0F)
                        .radius(2.0F)
                        .padding(Insets.internalMethod02086(3.0F))
                        .background(localValue0 -> ThemeColors.internalField1612.mulAlpha(0.889F))
                        .text(localValue2x, () -> this.internalMethod04029(localValue1x), localValue2xx -> this.internalMethod06564(localValue1x))
                        .visibleWhen(() -> !this.internalMethod04029(localValue1x).isEmpty())
                  )
                  .internalMethod03907(
                     new UiElement()
                        .interactive(false)
                        .fillWidth()
                        .height(10.0F)
                        .radius(2.0F)
                        .padding(Insets.internalMethod02086(3.0F))
                        .background(localValue0 -> ThemeColors.internalField1612.mulAlpha(0.94F))
                        .text(localValue2x, () -> this.internalMethod05112(localValue1x), localValue0 -> ThemeColors.internalField1613)
                  )
                  .internalMethod03907(
                     new UiElement()
                        .size(10.0F, 10.0F)
                        .interactive(false)
                        .radius(2.0F)
                        .background(localValue0 -> ThemeColors.internalField1612.mulAlpha(0.94F))
                        .paint(
                           (localValue2xx, localValue3) -> {
                              StaffListHud.InternalType0298 localValue4 = this.internalField0543.get(localValue1x);
                              ColorRGBA localValue5 = localValue4 != null && localValue4.internalMethod05435()
                                 ? new ColorRGBA(220.0F, 70.0F, 70.0F)
                                 : new ColorRGBA(70.0F, 210.0F, 120.0F);
                              float localValue6 = 4.0F;
                              localValue2xx.drawRoundedRect(
                                 localValue3.x() + localValue3.w() / 2.0F - localValue6 / 2.0F,
                                 localValue3.y() + localValue3.h() / 2.0F - localValue6 / 2.0F,
                                 localValue6,
                                 localValue6,
                                 CornerRadii.internalMethod03908(localValue6 / 2.0F),
                                 localValue5
                              );
                           }
                        )
                  );
            }
         );
      if (localValue2.phase() == UiNode.InternalType0146.internalField1091
         || localValue2.phase() == UiNode.InternalType0146.internalField1090
         || localValue2.phase() == UiNode.InternalType0146.internalField1089) {
         localValue2.beginEnter(0.0F);
      }

      return localValue2;
   }

   private void internalMethod06238() {
      HashSet localValue1 = new HashSet();

      for (StaffListHud.InternalType0298 localValue3 : this.internalField0416) {
         localValue1.add(localValue3.internalMethod02040());
      }

      if (!localValue1.equals(this.internalField0545)) {
         this.internalField0545.clear();
         this.internalField0545.addAll(localValue1);
         SizedFont localValue9 = Fonts.internalField1154.internalMethod01432(6.0F);
         HashMap localValue10 = new HashMap();

         for (StaffListHud.InternalType0298 localValue5 : this.internalField0416) {
            localValue10.put(
               localValue5.internalMethod02040(),
               localValue9.internalMethod00965(this.internalMethod06894(localValue5).internalMethod03620()) + localValue9.internalMethod00965(this.internalMethod01933(localValue5))
            );
         }

         ArrayList localValue11 = new ArrayList<>(this.internalField0416);
         localValue11.sort(
            Comparator.<StaffListHud.InternalType0298>comparingDouble(localValue1x -> ((Float)localValue10.get(localValue1x.internalMethod02040())).floatValue()).reversed()
         );
         ArrayList localValue12 = new ArrayList();
         localValue12.add(this.internalField0633);
         HashSet localValue6 = new HashSet();

         for (StaffListHud.InternalType0298 localValue8 : (Iterable<StaffListHud.InternalType0298>)(Iterable<?>)localValue11) {
            if (localValue6.add(localValue8.internalMethod02040())) {
               localValue12.add(this.internalMethod05667(localValue8.internalMethod02040()));
            }
         }

         this.internalField0634.internalMethod07849(localValue12);
      }
   }

   @Override
   public void update(UiRenderContext localValue1) {
      this.internalField0277 = this.x + this.width / 2.0F >= ScreenMetricsAccess.internalField0389.internalMethod03585() / 2.0F;
      this.internalMethod06239();
      if (this.internalField0634 != null) {
         this.internalField0634.internalMethod01855(this.internalMethod01793());
         this.internalMethod06238();
      }

      super.update(localValue1);
   }

   private TextAlignment internalMethod01793() {
      return this.internalField0381.internalMethod04496()
         ? TextAlignment.internalField1243
         : (this.internalField0277 ? TextAlignment.internalField1242 : TextAlignment.internalField0622);
   }

   private void internalMethod06239() {
      this.internalField0416.clear();
      if (internalField0149.player != null && internalField0149.player.networkHandler != null) {
         if (internalField0149.player.networkHandler.getServerInfo() == null) {
            this.internalField0416
               .add(
                  new StaffListHud.InternalType0298(
                     Text.literal("[ADMIN] ").setStyle(Style.EMPTY.withColor(Formatting.RED)), LanguageManager.internalMethod07214("staff.herobrine"), false
                  )
               );
         } else {
            internalField0149.player.networkHandler.getPlayerList().forEach(localValue1 -> {
               Team localValue2x = internalField0149.world.getScoreboard().getScoreHolderTeam(localValue1.getProfile().name());
               if (localValue2x != null) {
                  MutableText localValue3x = localValue2x.getPrefix().copy();
                  String localValue4x = localValue1.getProfile().name().replace("\u26a1 ", "");
                  boolean localValue5 = localValue1.getGameMode() == GameMode.SPECTATOR;
                  String localValue6x = this.internalMethod07820(localValue3x.getString());
                  if (localValue6x != null && !localValue4x.isBlank()) {
                     MutableText localValue7 = Text.literal(localValue6x).setStyle(Style.EMPTY.withColor(localValue3x.getStyle().getColor()));
                     this.internalField0416.add(new StaffListHud.InternalType0298(localValue7, localValue4x.trim(), localValue5));
                  }
               }
            });
         }

         for (StaffListStore.InternalType0181 localValue2 : RockstarClient.getInstance().internalMethod04407().internalMethod07279()) {
            if (!localValue2.internalMethod00138().isBlank()) {
               String localValue3 = localValue2.internalMethod04906().isBlank() ? "MODER" : localValue2.internalMethod04906().trim();
               MutableText localValue4 = Text.literal("[" + localValue3 + "] ").setStyle(Style.EMPTY.withColor(Formatting.BLUE));
               this.internalField0416.add(new StaffListHud.InternalType0298(localValue4, localValue2.internalMethod00138(), false));
            }
         }

         this.internalField0543.clear();

         for (StaffListHud.InternalType0298 localValue6 : this.internalField0416) {
            this.internalField0543.put(localValue6.internalMethod02040(), localValue6);
         }
      } else {
         this.internalField0543.clear();
      }
   }

   private String internalMethod04029(String localValue1) {
      StaffListHud.InternalType0298 localValue2 = this.internalField0543.get(localValue1);
      return localValue2 == null ? "" : this.internalMethod06894(localValue2).internalMethod03620();
   }

   private ColorRGBA internalMethod06564(String localValue1) {
      StaffListHud.InternalType0298 localValue2 = this.internalField0543.get(localValue1);
      return localValue2 == null ? ThemeColors.internalMethod08459() : this.internalMethod06894(localValue2).internalMethod07537();
   }

   private String internalMethod05112(String localValue1) {
      StaffListHud.InternalType0298 localValue2 = this.internalField0543.get(localValue1);
      return localValue2 == null ? "" : this.internalMethod01933(localValue2);
   }

   @Override
   public boolean show() {
      return !this.internalField0416.isEmpty() || internalField0149.currentScreen instanceof ChatScreen || this.internalField0650.internalMethod04496();
   }

   private String internalMethod07820(String localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         String localValue2 = Normalizer.normalize(localValue1, Form.NFKC).toLowerCase(Locale.ROOT).trim();
         int localValue3 = 0;
         int localValue4 = localValue2.length();

         while (localValue3 < localValue4) {
            int localValue5 = localValue2.codePointAt(localValue3);
            if (Character.isLetterOrDigit(localValue5)) {
               break;
            }

            localValue3 += Character.charCount(localValue5);
         }

         while (localValue4 > localValue3) {
            int localValue9 = localValue2.codePointBefore(localValue4);
            if (Character.isLetterOrDigit(localValue9) || localValue9 == 43) {
               break;
            }

            localValue4 -= Character.charCount(localValue9);
         }

         StringBuilder localValue10 = new StringBuilder(localValue4 - localValue3);
         boolean localValue6 = false;
         int localValue7 = localValue3;

         while (localValue7 < localValue4) {
            int localValue8 = localValue2.codePointAt(localValue7);
            localValue7 += Character.charCount(localValue8);
            if (!Character.isWhitespace(localValue8) && !Character.isSpaceChar(localValue8)) {
               if (localValue6) {
                  localValue10.append(' ');
               }

               localValue10.appendCodePoint(localValue8);
               localValue6 = false;
            } else {
               localValue6 = localValue10.length() > 0;
            }
         }

         String localValue11 = localValue10.toString();
         return !internalField0546.contains(localValue11)
            ? null
            : localValue11.replace("\ua509", "helper")
               .replace("\ua513", "ml.moder")
               .replace("\ua517", "moder")
               .replace("\ua521", "moder+")
               .replace("\ua525", "st.moder");
      }
   }

   private StaffListHud.InternalType0297 internalMethod06894(StaffListHud.InternalType0298 localValue1) {
      Text localValue2 = this.internalMethod02467(localValue1);
      String localValue3 = localValue2.getString().trim().toUpperCase();
      TextColor localValue4 = localValue2.getStyle().getColor();
      ColorRGBA localValue5 = localValue4 != null ? internalMethod06762(localValue4.getRgb()) : ThemeColors.internalMethod08459();
      return new StaffListHud.InternalType0297(localValue3, localValue5);
   }

   private static ColorRGBA internalMethod06762(int localValue0) {
      return new ColorRGBA(localValue0 >> 16 & 0xFF, localValue0 >> 8 & 0xFF, localValue0 & 0xFF);
   }

   private Text internalMethod02467(StaffListHud.InternalType0298 localValue1) {
      String localValue2 = localValue1.internalMethod03484().getString();
      String localValue3 = localValue2.toLowerCase()
         .replace("\ua509", "HELPER")
         .replace("\ua513", "ML.MODER")
         .replace("\ua517", "MODER")
         .replace("\ua521", "MODER+")
         .replace("\ua525", "st.MODER")
         .trim();
      boolean localValue4 = localValue2.contains("\ua509") || localValue2.contains("\ua513") || localValue2.contains("\ua517") || localValue2.contains("\ua521") || localValue2.contains("\ua525");
      TextColor localValue5 = localValue1.internalMethod03484().getStyle().getColor();
      TextColor localValue6 = localValue5;
      if (localValue4 || localValue5 == null) {
         Formatting localValue7;
         if (localValue3.contains("HELPER")) {
            localValue7 = Formatting.YELLOW;
         } else if (localValue3.contains("MODER")) {
            localValue7 = Formatting.BLUE;
         } else {
            localValue7 = Formatting.GRAY;
         }

         localValue6 = TextColor.fromFormatting(localValue7);
      }

      return Text.literal(localValue3).setStyle(Style.EMPTY.withColor(localValue6));
   }

   private String internalMethod01933(StaffListHud.InternalType0298 localValue1) {
      NameProtectModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NameProtectModule.class);
      return localValue2.isEnabled() ? localValue2.internalMethod08287(localValue1.internalMethod02040()) : localValue1.internalMethod02040();
   }

   static final class InternalType0297 {
      private final String internalField0248;
      private final ColorRGBA internalField0777;

      InternalType0297(String localValue1, ColorRGBA localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0777 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0297[text=" + this.internalField0248 + ", color=" + this.internalField0777 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0777);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         StaffListHud.InternalType0297 other = (StaffListHud.InternalType0297) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0777, other.internalField0777);
      }

      public String internalMethod03620() {
         return this.internalField0248;
      }

      public ColorRGBA internalMethod07537() {
         return this.internalField0777;
      }
   }

   static final class InternalType0298 {
      private final Text internalField0125;
      private final String internalField0248;
      private final boolean internalField0277;

      InternalType0298(Text localValue1, String localValue2, boolean localValue3) {
         this.internalField0125 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0277 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0298[prefix=" + this.internalField0125 + ", name=" + this.internalField0248 + ", isSpec=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0125);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         StaffListHud.InternalType0298 other = (StaffListHud.InternalType0298) localValue1;
         return java.util.Objects.equals(this.internalField0125, other.internalField0125)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public Text internalMethod03484() {
         return this.internalField0125;
      }

      public String internalMethod02040() {
         return this.internalField0248;
      }

      public boolean internalMethod05435() {
         return this.internalField0277;
      }
   }
}
