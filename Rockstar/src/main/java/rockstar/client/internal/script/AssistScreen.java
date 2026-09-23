package rockstar.client.internal.script;












import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import pyrock.events.window.MouseEvent;

public class AssistScreen extends RockstarScreen implements MinecraftClientAccess, ScreenMetricsAccess {
   private int internalField0227;
   private int internalField0228;
   private AssistItemCategory internalField0459;
   private float internalField1049;
   private float internalField1046;
   private float internalField1456;
   private float internalField1457;
   private AssistSwapItem internalField0347;
   private AssistSwapItem internalField0348;
   private AssistSwapItem internalField1129;
   private float internalField1458;
   private float internalField1459;
   private float internalField1460;
   private float internalField1461;
   private float internalField1462;
   private float internalField1455;
   private float internalField1723;
   private float internalField1731;
   private float internalField1727;
   private float internalField1728;
   private float internalField1717;
   private float internalField1718;
   private float internalField1719;
   private float internalField1721;
   private final AnimatedValue internalField0808;
   private final AnimatedValue internalField0809;
   private final AnimatedValue internalField1321;
   private final AnimatedValue internalField1322;
   private final AnimatedValue internalField1323;
   private final AnimatedValue internalField1324;
   private final AnimatedValue internalField1623;
   private final AnimatedValue internalField1618;
   private boolean internalField0277;
   private boolean internalField0276;
   private final List<AssistCategoryGroup> internalField0416;
   private final SmoothScrollHandler internalField0004;
   private final SmoothScrollHandler internalField0005;
   private final CustomTextField internalField0936;
   private final Map<AssistSwapItem, ItemSlotAnimation> internalField0543;
   private final Map<AssistSwapItem, AnimatedValue> internalField0544;
   private final Map<AssistItemCategory, AnimatedValue> internalField1197;
   private final Map<AssistSwapItem, MacroConfigPanel> internalField1196;
   private final Map<AssistSwapItem, AnimatedValue> internalField1195;
   private final Map<AssistSwapItem, AnimatedValue> internalField1198;
   private final Map<AssistItemPickerEntry, AnimatedValue> internalField1560;
   private final Map<AssistSwapItem, AnimatedValue> internalField1559;
   private final ItemPickerRenderer internalField0456;
   private final MacroPickerRenderer internalField0506;
   private final MacroSettingsPopup internalField0074;
   private final EventListener<MouseEvent> internalField0157;

   public AssistScreen() {
      this.internalField0459 = AssistItemCategory.internalField0459;
      this.internalField0808 = new AnimatedValue(300L, 0.0F, Easing.internalField0812);
      this.internalField0809 = new AnimatedValue(200L, 0.0F, Easing.internalField1626);
      this.internalField1321 = new AnimatedValue(200L, 0.0F, Easing.internalField1626);
      this.internalField1322 = new AnimatedValue(300L, 0.0F, Easing.internalField1814);
      this.internalField1323 = new AnimatedValue(300L, 0.0F, Easing.internalField1814);
      this.internalField1324 = new AnimatedValue(300L, 0.0F, Easing.internalField1814);
      this.internalField1623 = new AnimatedValue(200L, 0.0F, Easing.internalField1626);
      this.internalField1618 = new AnimatedValue(200L, 0.0F, Easing.internalField1626);
      this.internalField0276 = true;
      this.internalField0416 = new ArrayList<>();
      this.internalField0004 = new SmoothScrollHandler();
      this.internalField0005 = new SmoothScrollHandler();
      this.internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0F));
      this.internalField0543 = new HashMap<>();
      this.internalField0544 = new HashMap<>();
      this.internalField1197 = new HashMap<>();
      this.internalField1196 = new HashMap<>();
      this.internalField1195 = new HashMap<>();
      this.internalField1198 = new HashMap<>();
      this.internalField1560 = new HashMap<>();
      this.internalField1559 = new HashMap<>();
      this.internalField0456 = new ItemPickerRenderer();
      this.internalField0506 = new MacroPickerRenderer();
      this.internalField0074 = new MacroSettingsPopup();
      this.internalField0157 = localValue1 -> {
         if (localValue1.getAction() == 1) {
            int localValue2 = localValue1.getButton();
            if (localValue2 >= 3) {
               if (this.internalField1129 != null) {
                  this.internalField1129.internalMethod01910(KeybindUtils.internalMethod08541(localValue2));
                  this.internalField1129 = null;
               } else {
                  if (this.internalField0347 != null) {
                     MacroConfigPanel localValue3 = this.internalField1196.get(this.internalField0347);
                     if (localValue3 != null && localValue3.internalMethod02558()) {
                        localValue3.internalMethod03326(KeybindUtils.internalMethod08541(localValue2));
                        localValue3.internalMethod03327(false);
                        return;
                     }
                  }

                  if (!this.internalField0277) {
                     AssistSlotLayout localValue4 = this.internalField0506
                        .internalMethod06716(
                           this.internalField0227,
                           this.internalField0228,
                           this.internalField1458,
                           this.internalField1459,
                           238.0F,
                           223.0F,
                           this.internalField0005,
                           this.internalField0459,
                           this.internalMethod02341()
                        );
                     if (localValue4 != null
                        && UiUtils.internalMethod05786(
                           localValue4.internalMethod08978(),
                           localValue4.internalMethod08979(),
                           localValue4.internalMethod09187(),
                           localValue4.internalMethod09188(),
                           this.internalField0227,
                           this.internalField0228
                        )) {
                        localValue4.internalMethod05477().internalMethod01910(KeybindUtils.internalMethod08541(localValue2));
                     }
                  }
               }
            }
         }
      };
   }

   private List<AssistSwapItem> internalMethod04755() {
      return RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class).internalMethod08262();
   }

   public final void init() {
      this.internalField0277 = false;
      this.internalField0276 = true;
      this.internalField0936.internalMethod09000("\u041f\u043e\u0438\u0441\u043a");
      this.internalMethod00832();
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
      super.init();
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalField0227 = localValue1.internalMethod05259();
      this.internalField0228 = localValue1.internalMethod05261();
      this.internalField0808.internalMethod07062(true);
      this.internalMethod00834();
      this.internalMethod08943();
      this.internalField1324.internalMethod07062(this.internalField0277);
      if (this.internalField0277) {
         this.internalField0004.internalMethod02322();
      } else {
         this.internalField0005.internalMethod02322();
      }

      boolean localValue2 = this.internalField0347 != null && !this.internalField0277;
      this.internalField1322.internalMethod07062(localValue2);
      if (this.internalField0347 != null) {
         this.internalField0348 = this.internalField0347;
      }

      float localValue3 = 238.0F;
      if (localValue2 || this.internalField1322.internalMethod02881() > 0.01F) {
         localValue3 += 137.0F * this.internalField1322.internalMethod02881();
      }

      if (this.internalField0276) {
         this.internalField1323.internalMethod07060(localValue3);
         this.internalField0276 = false;
      } else {
         this.internalField1323.internalMethod07059(localValue3);
      }

      float localValue4 = internalField0389.internalMethod03585() / 2.0F;
      float localValue5 = this.internalField1323.internalMethod02881();
      this.internalField1458 = localValue4 - localValue5 / 2.0F;
      this.internalField1459 = internalField0389.internalMethod03589() / 2.0F - 111.5F;
      HudRenderUtils.internalMethod08976(
         localValue1.getMatrices(), this.internalField1458 + 119.0F, this.internalField1459 + 111.5F, 0.5F + 0.5F * this.internalField0808.internalMethod02881()
      );
      this.internalMethod01191(localValue1, this.internalField1458, this.internalField1459, 223.0F);
      float localValue6 = this.internalField1324.internalMethod02881();
      float localValue7 = 1.0F - localValue6;
      if (localValue7 > 0.01F) {
         MacroPickerRenderer.InternalType0450 localValue8 = this.internalField0506
            .internalMethod02409(
               localValue1,
               this.internalField1458,
               this.internalField1459,
               238.0F,
               localValue7,
               this.internalField0808,
               this.internalField0459,
               this.internalField1197,
               this.internalField0809
            );
         this.internalField1049 = localValue8.internalMethod03446();
         this.internalField1046 = localValue8.internalMethod03467();
         this.internalField1456 = localValue8.internalMethod09069();
         this.internalField1457 = localValue8.internalMethod09071();
         this.internalField0506
            .internalMethod01678(
               localValue1,
               this.internalField1458,
               this.internalField1459,
               238.0F,
               223.0F,
               localValue7,
               this.internalField0808,
               this.internalField0005,
               this.internalField0459,
               this.internalMethod02341(),
               this.internalField0347,
               this.internalField1129,
               this.internalField0227,
               this.internalField0228,
               this.internalField0543,
               this.internalField0544,
               this.internalField1559,
               localValue1x -> this.internalMethod07032(localValue1x) * this.internalMethod00933(localValue1x)
            );
      }

      if (localValue6 > 0.01F) {
         ItemPickerRenderer.InternalType0395 localValue9 = this.internalField0456
            .internalMethod07117(
               localValue1,
               this.internalField1458,
               this.internalField1459,
               238.0F,
               localValue6,
               this.internalField0808,
               this.internalField1618,
               this.internalField1623,
               this.internalField0936
            );
         this.internalField1717 = localValue9.internalMethod03523();
         this.internalField1718 = localValue9.internalMethod03525();
         this.internalField1719 = localValue9.internalMethod08114();
         this.internalField1721 = localValue9.internalMethod08115();
         this.internalField0456
            .internalMethod00057(
               localValue1,
               this.internalField1458,
               this.internalField1459,
               238.0F,
               223.0F,
               localValue6,
               this.internalField0808,
               this.internalField0416,
               this.internalField0004,
               this.internalField1560,
               this.internalField0936,
               this::internalMethod05897
            );
      }

      HudRenderUtils.internalMethod00012(localValue1.getMatrices());
      if (this.internalField1322.internalMethod02881() > 0.01F) {
         this.internalMethod03964(localValue1);
      }
   }

   private void internalMethod01191(UiRenderContext localValue1, float localValue2, float localValue3, float localValue4) {
      float localValue5 = this.internalField0808.internalMethod02881();
      localValue1.drawShadow(localValue2, localValue3, 238.0F, localValue4, 25.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1309.mulAlpha(0.5F * localValue5));
      localValue1.drawBlurredRect(localValue2, localValue3, 238.0F, localValue4, 5.0F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1312.mulAlpha(localValue5));
      localValue1.drawSquircle(localValue2, localValue3, 238.0F, localValue4, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1612.mulAlpha(localValue5));
      localValue1.drawSquircleBorder(localValue2, localValue3, 238.0F, localValue4, 0.5F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1616.mulAlpha(localValue5));
   }

   private void internalMethod03964(UiRenderContext localValue1) {
      if (!(this.internalField1322.internalMethod02881() <= 0.01F)) {
         AssistSwapItem localValue2 = this.internalField0347 != null ? this.internalField0347 : this.internalField0348;
         if (localValue2 != null) {
            MacroConfigPanel localValue3 = this.internalField1196.computeIfAbsent(localValue2, localValue1x -> new MacroConfigPanel(localValue1x, () -> this.internalMethod08339(localValue1x)));
            MacroSettingsPopup.InternalType0094 localValue4 = this.internalField0074
               .internalMethod05692(
                  localValue1,
                  this.internalField1458,
                  this.internalField1459,
                  238.0F,
                  5.0F,
                  132.0F,
                  18.0F,
                  5.0F,
                  this.internalField0808,
                  this.internalField1322,
                  this.internalField1321,
                  localValue3,
                  localValue2,
                  this.internalMethod07032(localValue2)
               );
            this.internalField1460 = localValue4.internalMethod02269();
            this.internalField1461 = localValue4.internalMethod02270();
            this.internalField1462 = localValue4.internalMethod08178();
            this.internalField1455 = localValue4.internalMethod08179();
            this.internalField1723 = localValue4.internalMethod08186();
            this.internalField1731 = localValue4.internalMethod08189();
            this.internalField1727 = localValue4.internalMethod09456();
            this.internalField1728 = localValue4.internalMethod09458();
         }
      }
   }

   public void tick() {
      GuiMoveModule.internalMethod09597();
      super.tick();
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public void close() {
      RockstarClient.getInstance().internalMethod03317().internalMethod07237(this);
      RockstarClient.getInstance().internalMethod02152().internalMethod07804();
      super.close();
      MenuModule.internalMethod09723();
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.internalField1129 != null) {
         this.internalField1129.internalMethod01910(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
         this.internalField1129 = null;
      } else {
         if (this.internalField0347 != null) {
            MacroConfigPanel localValue6 = this.internalField1196.get(this.internalField0347);
            if (localValue6 != null && localValue6.internalMethod02558()) {
               localValue6.internalMethod03326(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
               localValue6.internalMethod03327(false);
               return;
            }
         }

         if (this.internalField0277) {
            this.internalField0936.internalMethod01643(localValue1, localValue3, localValue5);
            if (localValue5 == MouseButton.internalField0102) {
               if (UiUtils.internalMethod05786(this.internalField1717, this.internalField1718, this.internalField1719, this.internalField1721, (int)localValue1, (int)localValue3)) {
                  this.internalField0277 = false;
                  return;
               }

               AssistItemTemplate localValue15 = this.internalField0456
                  .internalMethod00363(
                     (float)localValue1,
                     (float)localValue3,
                     this.internalField1458,
                     this.internalField1459,
                     238.0F,
                     223.0F,
                     this.internalField0004,
                     this.internalField0416,
                     this.internalField0936,
                     this::internalMethod05897
                  );
               if (localValue15 != null) {
                  AssistSwapItem localValue16 = localValue15.internalMethod07428().internalMethod04960();
                  if (localValue16 != null && this.internalMethod04755().stream().noneMatch(localValue1x -> localValue1x.internalMethod06026().equals(localValue16.internalMethod06026()))) {
                     this.internalMethod04755().add(localValue16);
                     this.internalMethod07033(localValue16);
                  }
               }
            }
         } else {
            SizedFont localValue14 = Fonts.internalField1154.internalMethod01432(7.0F);
            float localValue7 = this.internalField1458 + 7.0F;
            float localValue8 = this.internalField1459 + 24.0F;

            for (AssistItemCategory localValue12 : AssistItemCategory.values()) {
               float localValue13 = localValue14.internalMethod00965(localValue12.internalMethod06548()) + 8.0F;
               if (UiUtils.internalMethod05786(localValue7, localValue8, localValue13, 13.0, (int)localValue1, (int)localValue3)) {
                  this.internalField0459 = localValue12;
                  this.internalField0005.internalMethod09052(0.0);
                  this.internalField0347 = null;
                  break;
               }

               localValue7 += localValue13 + 4.0F;
            }

            if (localValue5 == MouseButton.internalField0101) {
               AssistSlotLayout localValue17 = this.internalField0506
                  .internalMethod06716(
                     (float)localValue1,
                     (float)localValue3,
                     this.internalField1458,
                     this.internalField1459,
                     238.0F,
                     223.0F,
                     this.internalField0005,
                     this.internalField0459,
                     this.internalMethod02341()
                  );
               if (localValue17 != null) {
                  if (UiUtils.internalMethod05785(
                     localValue17.internalMethod08978(), localValue17.internalMethod08979(), localValue17.internalMethod09187(), localValue17.internalMethod09188(), localValue1, localValue3
                  )) {
                     localValue17.internalMethod05477().internalMethod01910(-1);
                     return;
                  }

                  this.internalMethod00934(localValue17.internalMethod05477());
                  return;
               }
            }

            if (this.internalField0347 != null
               && UiUtils.internalMethod05785(this.internalField1460, this.internalField1461, this.internalField1462, this.internalField1455, localValue1, localValue3)) {
               MacroConfigPanel localValue19 = this.internalField1196.get(this.internalField0347);
               if (localValue19 != null) {
                  if (localValue5 == MouseButton.internalField0102
                     && UiUtils.internalMethod05785(this.internalField1723, this.internalField1731, this.internalField1727, this.internalField1728, localValue1, localValue3)) {
                     localValue19.internalMethod03327(true);
                     return;
                  }

                  if (localValue5 == MouseButton.internalField0101
                     && UiUtils.internalMethod05785(this.internalField1723, this.internalField1731, this.internalField1727, this.internalField1728, localValue1, localValue3)) {
                     localValue19.internalMethod03326(-1);
                     localValue19.internalMethod03327(false);
                     return;
                  }

                  for (SettingComponent localValue21 : localValue19.internalMethod01283()) {
                     localValue21.internalMethod01643(localValue1, localValue3, localValue5);
                  }
               }
            } else {
               AssistSlotLayout localValue18 = this.internalField0506
                  .internalMethod06716(
                     (float)localValue1,
                     (float)localValue3,
                     this.internalField1458,
                     this.internalField1459,
                     238.0F,
                     223.0F,
                     this.internalField0005,
                     this.internalField0459,
                     this.internalMethod02341()
                  );
               if (localValue18 != null) {
                  if (UiUtils.internalMethod05785(
                     localValue18.internalMethod08978(), localValue18.internalMethod08979(), localValue18.internalMethod09187(), localValue18.internalMethod09188(), localValue1, localValue3
                  )) {
                     if (localValue5 == MouseButton.internalField0102) {
                        this.internalField1129 = localValue18.internalMethod05477();
                     } else if (localValue5 != MouseButton.internalField0101) {
                        localValue18.internalMethod05477().internalMethod01910(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
                     }
                  } else {
                     if (localValue5 == MouseButton.internalField0102) {
                        this.internalMethod00934(localValue18.internalMethod05477());
                     }
                  }
               } else if (localValue5 == MouseButton.internalField0102
                  && UiUtils.internalMethod05786(this.internalField1049, this.internalField1046, this.internalField1456, this.internalField1457, (int)localValue1, (int)localValue3)) {
                  this.internalField0347 = null;
                  this.internalField0277 = true;
                  this.internalField0004.internalMethod09052(0.0);
                  this.internalField0936.internalMethod00484("");
                  this.internalMethod00832();
               } else {
                  super.onMouseClicked(localValue1, localValue3, localValue5);
               }
            }
         }
      }
   }

   @Override
   public void onMouseReleased(double localValue1, double localValue3, MouseButton localValue5) {
      if (this.internalField0277) {
         this.internalField0936.internalMethod02863(localValue1, localValue3, localValue5);
      }

      if (this.internalField0347 != null) {
         MacroConfigPanel localValue6 = this.internalField1196.get(this.internalField0347);
         if (localValue6 != null) {
            for (SettingComponent localValue8 : localValue6.internalMethod01283()) {
               localValue8.internalMethod02863(localValue1, localValue3, localValue5);
            }
         }
      }

      super.onMouseReleased(localValue1, localValue3, localValue5);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.internalField0277) {
         this.internalField0004.internalMethod04249(verticalAmount);
         return true;
      } else if (UiUtils.internalMethod05786(this.internalField1458, this.internalField1459, 238.0, 223.0, (int)mouseX, (int)mouseY)) {
         this.internalField0005.internalMethod04249(verticalAmount);
         return true;
      } else {
         if (this.internalField0347 != null
            && UiUtils.internalMethod05785(this.internalField1460, this.internalField1461, this.internalField1462, this.internalField1455, mouseX, mouseY)) {
            MacroConfigPanel localValue9 = this.internalField1196.get(this.internalField0347);
            if (localValue9 != null) {
               for (SettingComponent localValue11 : localValue9.internalMethod01283()) {
                  localValue11.internalMethod02890(mouseX, mouseY, horizontalAmount, verticalAmount);
               }

               return true;
            }
         }

         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.internalField1129 == null && !this.internalMethod00833()) {
         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 90 && SettingHistoryManager.internalMethod07603()) {
            return true;
         }

         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 89 && SettingHistoryManager.internalMethod07605()) {
            return true;
         }
      }

      if (this.internalField1129 != null) {
         if (keyCode == 256) {
            this.internalField1129 = null;
         } else if (keyCode == 261) {
            this.internalField1129.internalMethod01910(-1);
            this.internalField1129 = null;
         } else {
            int localValue7 = KeybindUtils.internalMethod06041(keyCode, modifiers);
            if (localValue7 == Integer.MIN_VALUE) {
               return true;
            }

            this.internalField1129.internalMethod01910(localValue7);
            this.internalField1129 = null;
         }

         return true;
      } else if (this.internalField0277) {
         if (this.internalField0936.internalMethod00342()) {
            if (keyCode == 256) {
               this.internalField0936.internalMethod07508(false);
               return true;
            } else {
               this.internalField0936.internalMethod05727(keyCode, scanCode, modifiers);
               return true;
            }
         } else if (keyCode == 256) {
            this.internalField0277 = false;
            return true;
         } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
         }
      } else {
         if (this.internalField0347 != null) {
            MacroConfigPanel localValue4 = this.internalField1196.get(this.internalField0347);
            if (localValue4 != null && localValue4.internalMethod02558()) {
               if (keyCode == 256) {
                  localValue4.internalMethod03327(false);
               } else if (keyCode == 261) {
                  localValue4.internalMethod03326(-1);
                  localValue4.internalMethod03327(false);
               } else {
                  int localValue8 = KeybindUtils.internalMethod06041(keyCode, modifiers);
                  if (localValue8 == Integer.MIN_VALUE) {
                     return true;
                  }

                  localValue4.internalMethod03326(localValue8);
                  localValue4.internalMethod03327(false);
               }

               return true;
            }

            if (localValue4 != null) {
               for (SettingComponent localValue6 : localValue4.internalMethod01283()) {
                  localValue6.internalMethod05727(keyCode, scanCode, modifiers);
               }
            }
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      int localValue4 = KeybindUtils.internalMethod08281(keyCode, modifiers);
      if (localValue4 != Integer.MIN_VALUE) {
         if (this.internalField1129 != null) {
            this.internalField1129.internalMethod01910(localValue4);
            this.internalField1129 = null;
            return true;
         }

         if (this.internalField0347 != null) {
            MacroConfigPanel localValue5 = this.internalField1196.get(this.internalField0347);
            if (localValue5 != null && localValue5.internalMethod02558()) {
               localValue5.internalMethod03326(localValue4);
               localValue5.internalMethod03327(false);
               return true;
            }
         }
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   private boolean internalMethod00833() {
      if (this.internalField0347 == null) {
         return false;
      } else {
         MacroConfigPanel localValue1 = this.internalField1196.get(this.internalField0347);
         return localValue1 != null && localValue1.internalMethod02558();
      }
   }

   public boolean charTyped(char chr, int modifiers) {
      if (this.internalField0277 && this.internalField0936.internalMethod05413(chr, modifiers)) {
         return true;
      } else {
         if (this.internalField0347 != null) {
            MacroConfigPanel localValue3 = this.internalField1196.get(this.internalField0347);
            if (localValue3 != null) {
               for (SettingComponent localValue5 : localValue3.internalMethod01283()) {
                  if (localValue5.internalMethod05413(chr, modifiers)) {
                     return true;
                  }
               }
            }
         }

         return super.charTyped(chr, modifiers);
      }
   }

   private List<AssistSwapItem> internalMethod02341() {
      ArrayList<AssistSwapItem> localValue1 = new ArrayList<>(this.internalMethod04755());
      localValue1.removeIf(localValue0 -> !localValue0.internalMethod03236());
      return localValue1;
   }

   private boolean internalMethod05897(AssistItemPickerEntry localValue1) {
      AssistSwapItem localValue2 = localValue1.internalMethod04960();
      return localValue2 == null ? false : this.internalMethod04755().stream().anyMatch(localValue1x -> localValue1x.internalMethod06026().equals(localValue2.internalMethod06026()));
   }

   private void internalMethod00832() {
      this.internalField0416.clear();
      AssistModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AssistModule.class);
      Object localValue2 = localValue1 == null ? new ArrayList() : localValue1.internalMethod03313();

      for (AssistItemCategory localValue6 : AssistItemCategory.values()) {
         if (localValue6 != AssistItemCategory.internalField0459) {
            ArrayList localValue7 = new ArrayList();

            for (AssistSwapItem localValue9 : (Iterable<AssistSwapItem>)(Iterable<?>)localValue2) {
               if (localValue9.internalMethod06886() == localValue6 && localValue9.internalMethod03236()) {
                  localValue7.add(
                     new AssistItemPickerEntry(
                        localValue9, LanguageManager.internalMethod07214(localValue9.internalMethod06026()), localValue9.internalMethod06489(), localValue9.internalMethod03234()
                     )
                  );
               }
            }

            if (!localValue7.isEmpty()) {
               this.internalField0416.add(new AssistCategoryGroup(localValue6.internalMethod06548(), localValue7));
            }
         }
      }
   }

   private void internalMethod00934(AssistSwapItem localValue1) {
      this.internalField0347 = localValue1;
      this.internalField1196.computeIfAbsent(localValue1, localValue1x -> new MacroConfigPanel(localValue1x, () -> this.internalMethod08339(localValue1x)));
   }

   private void internalMethod08339(AssistSwapItem localValue1) {
      if (localValue1 != null) {
         AnimatedValue localValue2 = this.internalField1195
            .computeIfAbsent(localValue1, localValue0 -> new AnimatedValue(200L, 1.0F, Easing.internalField1626));
         localValue2.internalMethod07060(1.0F);
         localValue2.internalMethod07059(0.0F);
      }
   }

   private void internalMethod00834() {
      if (!this.internalField1195.isEmpty()) {
         ArrayList localValue1 = new ArrayList();

         for (Entry localValue3 : this.internalField1195.entrySet()) {
            AnimatedValue localValue4 = (AnimatedValue)localValue3.getValue();
            localValue4.internalMethod07059(0.0F);
            if (localValue4.internalMethod02884() && localValue4.internalMethod02881() <= 0.01F) {
               localValue1.add((AssistSwapItem)localValue3.getKey());
            }
         }

         for (AssistSwapItem localValue6 : (Iterable<AssistSwapItem>)(Iterable<?>)localValue1) {
            this.internalMethod04755().remove(localValue6);
            this.internalField1196.remove(localValue6);
            this.internalField0544.remove(localValue6);
            this.internalField0543.remove(localValue6);
            this.internalField1195.remove(localValue6);
            if (this.internalField0347 == localValue6) {
               this.internalField0347 = null;
               SliderSettingComponent.internalMethod04360();
            }
         }
      }
   }

   private float internalMethod07032(AssistSwapItem localValue1) {
      AnimatedValue localValue2 = this.internalField1195.get(localValue1);
      return localValue2 == null ? 1.0F : localValue2.internalMethod02881();
   }

   private void internalMethod08943() {
      if (!this.internalField1198.isEmpty()) {
         ArrayList localValue1 = new ArrayList();

         for (Entry localValue3 : this.internalField1198.entrySet()) {
            AnimatedValue localValue4 = (AnimatedValue)localValue3.getValue();
            localValue4.internalMethod07059(1.0F);
            if (localValue4.internalMethod02884() && localValue4.internalMethod02881() >= 0.99F) {
               localValue1.add((AssistSwapItem)localValue3.getKey());
            }
         }

         for (AssistSwapItem localValue6 : (Iterable<AssistSwapItem>)(Iterable<?>)localValue1) {
            this.internalField1198.remove(localValue6);
         }
      }
   }

   private float internalMethod00933(AssistSwapItem localValue1) {
      AnimatedValue localValue2 = this.internalField1198.get(localValue1);
      return localValue2 == null ? 1.0F : localValue2.internalMethod02881();
   }

   public void internalMethod07033(AssistSwapItem localValue1) {
      AnimatedValue localValue2 = new AnimatedValue(300L, 0.0F, Easing.internalField1814);
      this.internalField1198.put(localValue1, localValue2);
      this.internalMethod00832();
   }
}
