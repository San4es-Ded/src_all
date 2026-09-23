package rockstar.client.internal.script;













import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.utility.render.ColorRGBA;

public class WorldPanelScreen extends LayeredRockstarScreen implements MenuLayoutProvider {
   private static final Motion internalField0913 = Motion.internalMethod01328(140L, Easing.internalField1822);
   private static final Easing internalField0812 = Easing.internalField1822;
   private static final ColorRGBA internalField0777 = new ColorRGBA(255.0F, 120.0F, 120.0F);
   private IntConsumer internalField0540;
   private ModuleSettingsPanel internalField0096;
   private final Map<ModuleEntry, UiNode> internalField0543 = new HashMap<>();
   private final Map<Setting, UiNode> internalField0544 = new HashMap<>();
   private final Map<ModuleCategory, UiContainer> internalField1197 = new HashMap<>();
   private final Map<ModuleCategory, Float> internalField1196 = new HashMap<>();
   private final Map<ModuleCategory, float[]> internalField1195 = new HashMap<>();
   private final Map<ModuleEntry, WorldPanelScreen.InternalType0216> internalField1198 = new HashMap<>();
   private final Map<ModuleCategory, ModuleEntry[]> internalField1560 = new HashMap<>();
   private final Map<ModuleCategory, UiNode> internalField1559 = new HashMap<>();
   private final Map<ModuleCategory, List<ModuleEntry>> internalField1558 = new HashMap<>();
   private final Map<ModuleEntry, List<Setting>> internalField1561 = new HashMap<>();
   private boolean internalField0277 = true;
   private int internalField0227 = -1;
   private Runnable internalField0659;
   private UiNode internalField0633;
   private long internalField0230;
   private WorldPanelScreen.InternalType0215 internalField0279;
   private ModuleEntry internalField0403;
   private long internalField1059;
   private float internalField1048;
   private long internalField1058;
   private boolean internalField0276;
   private boolean internalField1099;
   private long internalField1060;
   Vec3d internalField0283;
   private Vec3d internalField0282;
   Vec3d internalField1104;
   Vec3d internalField1106;
   static WorldPanelScreen internalField0551;
   private final List<WorldPanelScreen.InternalType0267> internalField0416 = new ArrayList<>();
   private final HudTooltip internalField0565 = new HudTooltip(
         Fonts.internalField1154.internalMethod01432(10.0F), 10.0F, 300L, Easing.internalField0812
      )
      .internalMethod01888();
   static final ManagedFramebuffer internalField0769 = new ManagedFramebuffer(false).internalMethod06013();
   static boolean internalField1100;
   static boolean internalField1102;

   @Override
   public boolean lowDrawBatching() {
      return true;
   }

   @Override
   public void onMouseClicked(double localValue1, double localValue3, MouseButton localValue5) {
      if (KeybindButton.internalMethod01436(localValue5)) {
         this.internalMethod05969();
      } else {
         if (this.internalField0403 != null) {
            if (localValue5 == MouseButton.internalField0102) {
               this.internalField0403 = null;
               return;
            }

            if (localValue5 != MouseButton.internalField0990) {
               this.internalField0403.setKeybind(KeybindUtils.internalMethod08541(localValue5.internalMethod02957()));
               this.internalField0403 = null;
               this.internalMethod05969();
               return;
            }
         }

         if (this.internalField0540 == null || localValue5 != MouseButton.internalField0991 && localValue5 != MouseButton.internalField0989) {
            if (this.internalField0096 != null && this.overlays.stream().noneMatch(localValue4 -> localValue4.alive() && localValue4.contains((float)localValue1, (float)localValue3))) {
               this.internalField0096.internalMethod04148((float)localValue1, (float)localValue3);
            }

            super.onMouseClicked(localValue1, localValue3, localValue5);
         } else {
            this.internalField0540.accept(localValue5 == MouseButton.internalField0991 ? -1 : 1);
         }
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0403 == null && !KeybindButton.internalMethod06277()) {
         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 90 && SettingHistoryManager.internalMethod07603()) {
            return true;
         }

         if (rockstar.client.compat.InputCompat.hasControlDown() && keyCode == 89 && SettingHistoryManager.internalMethod07605()) {
            return true;
         }
      }

      if (this.internalField0403 != null) {
         if (keyCode != 256 && keyCode != 261) {
            int localValue4 = KeybindUtils.internalMethod06041(keyCode, modifiers);
            if (localValue4 == Integer.MIN_VALUE) {
               return true;
            } else {
               this.internalField0403.setKeybind(localValue4);
               this.internalField0403 = null;
               this.internalMethod05969();
               return true;
            }
         } else {
            this.internalField0403.setKeybind(-1);
            this.internalField0403 = null;
            this.internalMethod05969();
            return true;
         }
      } else if (super.keyPressed(keyCode, scanCode, modifiers)) {
         return true;
      } else if (MenuModule.internalMethod01859(keyCode)) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (this.internalField0403 != null) {
         int localValue4 = KeybindUtils.internalMethod08281(keyCode, modifiers);
         if (localValue4 != Integer.MIN_VALUE) {
            this.internalField0403.setKeybind(localValue4);
            this.internalField0403 = null;
            this.internalMethod05969();
            return true;
         }
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   private void internalMethod05969() {
      RockstarClient.getInstance().internalMethod02152().internalMethod07804();
   }

   @Override
   public void render(UiRenderContext localValue1) {
      this.internalMethod08481();
      this.internalMethod08482();
      GuiMoveModule.internalMethod09597();
      if (!this.internalField1099) {
         this.internalMethod08490();
      }

      float localValue2 = this.internalField1099 ? 1.0F : 0.7F + 0.3F * this.internalMethod08491();
      boolean localValue3 = Math.abs(localValue2 - 1.0F) > 1.0E-4F;
      if (localValue3) {
         localValue1.getMatrices().pushMatrix();
         localValue1.getMatrices().translate(this.width / 2.0F, this.height / 2.0F);
         localValue1.getMatrices().scale(localValue2, localValue2);
         localValue1.getMatrices().translate(-this.width / 2.0F, -this.height / 2.0F);
      }

      MenuRenderDispatcher.internalMethod05609(this, localValue1);
      super.render(localValue1);
      MenuRenderDispatcher.internalMethod03319(this, localValue1);
      if (localValue3) {
         localValue1.getMatrices().popMatrix();
      }
   }

   @Override
   public String internalMethod03999() {
      return "panel";
   }

   @Override
   public float internalMethod04389() {
      return this.internalMethod08491();
   }

   @Override
   public float internalMethod04391() {
      return this.internalField1099 ? this.internalMethod09809() : 0.0F;
   }

   @Override
   public boolean internalMethod04390() {
      return this.internalField1099;
   }

   @Override
   public float internalMethod08283() {
      return this.contentAlpha;
   }

   @Override
   public float internalMethod08285() {
      return this.internalField1099 ? 1.0F : 0.7F + 0.3F * this.internalMethod08491();
   }

   @Override
   public List<MenuLayoutProvider.InternalType0257> internalMethod05973() {
      ArrayList localValue1 = new ArrayList(this.internalField1197.size());

      for (ModuleCategory localValue5 : ModuleCategory.values()) {
         UiContainer localValue6 = this.internalField1197.get(localValue5);
         if (localValue6 != null) {
            localValue1.add(new MenuLayoutProvider.InternalType0257(localValue5.name().toLowerCase(Locale.ROOT), localValue6.x(), localValue6.y(), localValue6.w(), localValue6.h()));
         }
      }

      return localValue1;
   }

   public static WorldPanelScreen internalMethod03219() {
      return internalField0551;
   }

   private float internalMethod08491() {
      float localValue1 = Math.min(1.0F, Math.max(0.0F, (float)(System.currentTimeMillis() - this.internalField1059) / 300.0F));
      return Easing.internalField0812.ease(localValue1, 0.0F, 1.0F, 1.0F);
   }

   private void internalMethod08481() {
      long localValue1 = System.currentTimeMillis();
      if (this.internalField1099) {
         this.internalField1048 = 0.0F;
         this.internalField1058 = localValue1;
         this.contentAlpha = 1.0F;
      } else {
         float localValue3 = this.internalField1058 == 0L ? 16.0F : Math.min(64.0F, (float)(localValue1 - this.internalField1058));
         this.internalField1058 = localValue1;
         int localValue4 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class).internalMethod00598().internalMethod07477();
         float localValue5 = internalMethod04239(localValue4) ? 1.0F : 0.0F;
         float localValue6 = localValue3 / 300.0F;
         if (this.internalField1048 < localValue5) {
            this.internalField1048 = Math.min(localValue5, this.internalField1048 + localValue6);
         } else if (this.internalField1048 > localValue5) {
            this.internalField1048 = Math.max(localValue5, this.internalField1048 - localValue6);
         }

         this.contentAlpha = 1.0F - Easing.internalField1626.ease(this.internalField1048, 0.0F, 1.0F, 1.0F);
      }
   }

   private void internalMethod08482() {
      if (!this.internalField1099 && !(this.contentAlpha >= 0.999F)) {
         long localValue1 = MinecraftClient.getInstance().getWindow().getHandle();
         boolean localValue3 = GLFW.glfwGetMouseButton(localValue1, 0) == 1;
         if (!localValue3 || UiNode.spotlight() == null) {
            UiNode localValue4 = null;

            for (Entry localValue6 : this.internalField0544.entrySet()) {
               UiNode localValue7 = (UiNode)localValue6.getValue();
               if (localValue7.inFlow() && localValue7.hovered() && SettingComponentFactory.internalMethod00080((Setting)localValue6.getKey())) {
                  localValue4 = localValue7;
                  break;
               }
            }

            UiNode.spotlight(localValue4);
         }
      } else {
         UiNode.spotlight(null);
      }
   }

   private static boolean internalMethod04239(int localValue0) {
      return KeybindUtils.internalMethod08521(localValue0);
   }

   @Override
   public void removed() {
      this.internalMethod05966();
      super.removed();
   }

   public void internalMethod05966() {
      if (!this.internalField1099) {
         this.internalField1099 = true;
         UiNode.spotlight(null);
         RockstarClient.getInstance().internalMethod02152().internalMethod07804();
         this.internalField1060 = System.currentTimeMillis();
         internalField1100 = false;
         internalField1102 = false;
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         Camera localValue2 = localValue1.gameRenderer.getCamera();
         if (localValue2 != null && localValue1.player != null) {
            double localValue3 = Math.toRadians(localValue2.getYaw());
            double localValue5 = Math.toRadians(localValue2.getPitch());
            Vec3d localValue7 = new Vec3d(-Math.sin(localValue3) * Math.cos(localValue5), -Math.sin(localValue5), Math.cos(localValue3) * Math.cos(localValue5)).normalize();
            this.internalField0282 = localValue7;
            this.internalField1104 = localValue7.crossProduct(new Vec3d(0.0, 1.0, 0.0)).normalize();
            this.internalField1106 = this.internalField1104.crossProduct(localValue7).normalize();
            this.internalField0283 = localValue2.getCameraPos().add(localValue7.multiply(1.5));
         }

         internalField0551 = this;
      }
   }

   public float internalMethod09809() {
      return Math.min(1.0F, Math.max(0.0F, (float)(System.currentTimeMillis() - this.internalField1060) / 1600.0F));
   }

   public static void internalMethod01216(HudRenderEvent localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      float localValue2 = localValue1.getWindow().getScaledWidth();
      float localValue3 = localValue1.getWindow().getScaledHeight();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderTexture(0, internalField0769.getColorAttachmentView());
      Matrix4f localValue4 = rockstar.client.render.GuiMatrixCompat.toMatrix4f(localValue0.getContext().getMatrices());
      int localValue5 = ColorRGBA.WHITE.getRGB();
      BufferBuilder localValue6 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      localValue6.vertex(localValue4, 0.0F, 0.0F, 0.0F).texture(0.0F, 1.0F).color(localValue5);
      localValue6.vertex(localValue4, 0.0F, localValue3, 0.0F).texture(0.0F, 0.0F).color(localValue5);
      localValue6.vertex(localValue4, localValue2, localValue3, 0.0F).texture(1.0F, 0.0F).color(localValue5);
      localValue6.vertex(localValue4, localValue2, 0.0F, 0.0F).texture(1.0F, 1.0F).color(localValue5);
      BufferRenderer.drawWithGlobalProgram(localValue6.end());
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public void init() {
      super.init();
      this.internalField1059 = System.currentTimeMillis();
      this.internalField1099 = false;
      this.internalField0403 = null;
      KeybindButton.internalMethod06276();
      this.internalField1048 = 0.0F;
      this.internalField1058 = 0L;
      this.contentAlpha = 1.0F;
      this.internalField0277 = true;
      if (!this.internalField0276) {
         this.internalField0276 = true;
         this.clearRoots();
         this.internalField0543.clear();
         this.internalField0544.clear();
         this.internalField1197.clear();
         this.internalField1198.clear();
         this.internalField1560.clear();
         this.internalField1559.clear();
         this.internalField1558.clear();
         this.internalField1561.clear();
         this.internalField0416.clear();
         this.internalField0633 = null;
         this.internalField0279 = null;
         HashMap localValue1 = new HashMap();
         UiContainer localValue2 = new UiContainer()
            .internalMethod01192(FlexDirection.internalField1246)
            .internalMethod03062(10.0F)
            .internalMethod09266(243.0F)
            .internalMethod09784()
            .internalMethod07178(
               (localValue2x, localValue3x) -> {
                  for (UiNode localValue5x : localValue3x.internalMethod01401()) {
                     localValue2x.drawShadow(
                        localValue5x.x(),
                        localValue5x.y(),
                        localValue5x.w(),
                        localValue5x.h(),
                        25.0F,
                        CornerRadii.internalMethod03908(11.0F),
                        ThemeColors.internalField1309.mulAlpha(0.15F)
                     );
                  }

                  for (UiNode localValue10x : localValue3x.internalMethod01401()) {
                     localValue2x.drawBlurredRect(
                        localValue10x.x(), localValue10x.y(), localValue10x.w(), localValue10x.h(), 5.0F, 3.0F, CornerRadii.internalMethod03908(11.0F), ThemeColors.internalField1312
                     );
                  }

                  for (UiNode localValue11x : localValue3x.internalMethod01401()) {
                     if (localValue11x instanceof UiContainer localValue6) {
                        ModuleCategory localValue7 = (ModuleCategory)localValue1.get(localValue6);
                        if (localValue7 != null) {
                           this.internalMethod03388(localValue2x, localValue6, localValue7);
                        }
                     }
                  }
               }
            );
         ArrayList localValue3 = new ArrayList();
         ArrayList localValue4 = new ArrayList();
         int[] localValue5 = new int[]{-1};
         this.internalField0659 = () -> {
            this.internalField0277 = true;
            ModuleEntry[] localValue4x = new ModuleEntry[localValue3.size()];

            for (int localValue5x = 0; localValue5x < localValue3.size(); localValue5x++) {
               localValue4x[localValue5x] = ((ModuleEntry[])localValue3.get(localValue5x))[0];
            }

            while (localValue4.size() > localValue5[0] + 1) {
               localValue4.remove(localValue4.size() - 1);
            }

            localValue4.add(localValue4x);
            localValue5[0] = localValue4.size() - 1;
         };

         for (ModuleCategory localValue9 : ModuleCategory.values()) {
            UiContainer localValue10 = new UiContainer()
               .internalMethod09339(115.0F)
               .internalMethod05391(
                  localValue0 -> localValue0.internalMethod02712(3.0F)
                     .internalMethod02066(1.0F, 6.0F)
                     .internalMethod00894(2.5F)
                     .internalMethod04404(
                        localValue0x -> ColorRGBA.BLACK
                           .mix(ColorRGBA.WHITE, 0.3F)
                           .withAlpha(255.0F * (0.32F + 0.28F * localValue0x.internalMethod05170() + 0.3F * localValue0x.internalMethod05173()))
                     )
               )
               .internalMethod03514(Insets.internalMethod00105(25.0F, 0.0F, 1.0F, 0.0F))
               .internalMethod08755()
               .internalMethod09186();
            ModuleEntry[] localValue11 = new ModuleEntry[]{null};
            localValue3.add(localValue11);
            this.internalField1560.put(localValue9, localValue11);
            this.internalField1197.put(localValue9, localValue10);
            localValue1.put(localValue10, localValue9);
            List localValue12 = RockstarClient.getInstance()
               .getModuleManager()
               .getModules()
               .stream()
               .sorted(Comparator.comparing(ModuleEntry::getName))
               .filter(localValue1x -> localValue1x.getCategory() == localValue9 && localValue1x.isAvailable())
               .toList();
            UiElement localValue13 = new UiElement().height(2.0F).visibleWhen(() -> localValue11[0] == null);
            this.internalField1559.put(localValue9, localValue13);
            localValue10.internalMethod03907(localValue13);

            for (ModuleEntry localValue15 : (Iterable<ModuleEntry>)(Iterable<?>)localValue12) {
               WorldPanelScreen.InternalType0216 localValue16 = this.internalMethod02859(localValue9, localValue15);
               this.internalField1198.put(localValue15, localValue16);
               localValue10.internalMethod03907(localValue16.internalMethod03073());
               localValue10.internalMethod03907(localValue16.internalMethod03069());
               localValue10.internalMethod03907(localValue16.internalMethod07166());
            }

            this.internalField1558.put(localValue9, localValue12);
            localValue2.internalMethod03907(localValue10);
         }

         this.internalField0659.run();
         this.internalField0540 = localValue4x -> {
            int localValue5x = localValue5[0] + localValue4x;
            if (localValue5x >= 0 && localValue5x < localValue4.size()) {
               localValue5[0] = localValue5x;
               this.internalField0277 = true;
               ModuleEntry[] localValue6 = (ModuleEntry[])localValue4.get(localValue5x);

               for (int localValue7 = 0; localValue7 < localValue3.size(); localValue7++) {
                  ((ModuleEntry[])localValue3.get(localValue7))[0] = localValue6[localValue7];
               }
            }
         };
         BiConsumer<ModuleEntry, Setting> localValue17 = (localValue1x, localValue2x) -> {
            if (this.internalField1560.containsKey(localValue1x.getCategory())) {
               this.internalMethod07276(localValue1x.getCategory(), localValue1x);
               this.internalField0279 = new WorldPanelScreen.InternalType0215(localValue1x, localValue2x);
            }
         };
         this.internalField0096 = new ModuleSettingsPanel(localValue2, localValue17);
         this.add(localValue2);
         this.add(this.internalField0096);
         this.add(this.internalField0096.internalMethod10099());
         this.add(this.internalField0096.internalMethod09983());
      }
   }

   private WorldPanelScreen.InternalType0216 internalMethod02859(ModuleCategory localValue1, ModuleEntry localValue2) {
      ModuleEntry[] localValue3 = this.internalField1560.get(localValue1);
      UiElement localValue4 = new UiElement()
         .text(
            Fonts.internalField1154.internalMethod01432(8.0F),
            () -> {
               if (this.internalField0403 != localValue2) {
                  return localValue2.getName();
               } else {
                  int localValue2x = KeybindUtils.internalMethod06867();
                  if (localValue2x != 0) {
                     return LanguageManager.internalMethod07214("key") + ": " + KeybindUtils.internalMethod07434(localValue2x) + "...";
                  } else {
                     int localValue3x = localValue2.getKeybind();
                     return localValue3x == -1
                        ? LanguageManager.internalMethod07214("menu.binding")
                        : LanguageManager.internalMethod07214("key") + ": " + TextUtils.internalMethod04982(localValue3x);
                  }
               }
            },
            localValue0 -> {
               ColorRGBA localValue1x = ThemeColors.internalField1613
                  .mix(ThemeColors.internalField1310, localValue0.sig("enabled", internalField0812) * (1.0F - localValue0.sig("open", internalField0812)))
                  .mulAlpha(0.75F + 0.25F * localValue0.sig("enabled", internalField0812));
               float localValue2x = localValue0.shakeAmount();
               return localValue2x > 0.0F ? localValue1x.mix(internalField0777.withAlpha(localValue1x.getAlpha()), localValue2x) : localValue1x;
            }
         )
         .bind("enabled", localValue2::isEnabled, 140L)
         .bind("open", () -> localValue3[0] == localValue2, 140L)
         .height(6.0F)
         .animatePosition()
         .motion(internalField0913)
         .interactive(false);
      UiElement localValue5 = new UiElement()
         .size(18.0F, 18.0F)
         .icon("back", 6.0F, ThemeColors.internalField1613)
         .cursor(CursorType.internalField0567)
         .onClick(() -> this.internalMethod07645(localValue1))
         .visibleWhen(() -> localValue3[0] == localValue2);
      UiElement localValue6 = new UiElement()
         .size(6.0F, 6.0F)
         .icon("check", 6.0F, localValue0 -> ThemeColors.internalField1613.mulAlpha(localValue0.sig("enabled", internalField0812)))
         .bind("enabled", () -> localValue2.isEnabled() && this.internalField0403 != localValue2, 140L)
         .visibleWhen(() -> localValue3[0] != localValue2)
         .interactive(false);
      UiElement localValue7 = new ToggleSwitchWidget(localValue2::isEnabled)
         .internalMethod05792(() -> ThemeColors.internalField1614)
         .size(13.0F, 8.0F)
         .onClick(localValue2::toggle)
         .visibleWhen(() -> localValue3[0] == localValue2);
      UiContainer localValue8 = new UiContainer()
         .internalMethod01863()
         .internalMethod09609()
         .internalMethod03754(internalField0913)
         .internalMethod09801()
         .internalMethod01619(UiTransition.internalMethod02229(20.0F));
      localValue8.internalMethod03855(() -> localValue3[0] == localValue2);
      UiContainer localValue9 = new UiContainer()
         .internalMethod01192(FlexDirection.internalField1246)
         .internalMethod01855(TextAlignment.internalField0621)
         .internalMethod07607(LayoutAlignment.internalField1377)
         .internalMethod03514(Insets.internalMethod00105(0.0F, 9.0F, 0.0F, 9.0F))
         .internalMethod09266(18.0F)
         .internalMethod04332(CursorType.internalField0567)
         .internalMethod03907(
            new UiContainer()
               .internalMethod01192(FlexDirection.internalField1246)
               .internalMethod01855(TextAlignment.internalField0621)
               .internalMethod03062(-9.0F)
               .internalMethod03907(localValue5)
               .internalMethod03907(localValue4)
         )
         .internalMethod03907(localValue6)
         .internalMethod03907(localValue7);
      localValue9.internalMethod05382((localValue6x, localValue7x, localValue8x) -> {
         if (localValue6x == MouseButton.internalField0990) {
            this.internalField0403 = this.internalField0403 == localValue2 ? null : localValue2;
         } else if (localValue6x == MouseButton.internalField0101) {
            if (localValue3[0] != localValue2) {
               if (localValue2.getSettings().isEmpty()) {
                  this.internalMethod02212(localValue4);
               } else {
                  this.internalMethod07276(localValue1, localValue2);
               }
            }
         } else if (localValue6x == MouseButton.internalField0102) {
            if (localValue3[0] == localValue2) {
               if (localValue7x < localValue9.x() + localValue9.w() / 2.0F) {
                  this.internalMethod07645(localValue1);
               } else {
                  localValue2.toggle();
               }
            } else {
               localValue2.toggle();
            }
         }
      });
      localValue9.internalMethod09609().internalMethod03855(() -> localValue3[0] == null || localValue3[0] == localValue2).internalMethod09801().internalMethod08226(() -> localValue3[0] == localValue2);
      UiElement localValue10 = new UiElement()
         .fillWidth()
         .height(1.0F)
         .background(ThemeColors.internalField1616)
         .visibleWhen(() -> localValue3[0] == localValue2)
         .snapPosition()
         .sticky(() -> localValue3[0] == localValue2);
      this.internalField0543.put(localValue2, localValue9);
      this.internalField0416.add(new WorldPanelScreen.InternalType0267(localValue9, localValue2::internalMethod05655));
      return new WorldPanelScreen.InternalType0216(localValue9, localValue10, localValue8);
   }

   private void internalMethod08490() {
      if (this.internalField0276) {
         int localValue1 = ModuleManager.internalMethod03045();
         if (localValue1 != this.internalField0227) {
            this.internalField0227 = localValue1;
            this.internalField0277 = true;
         }

         if (this.internalField0277) {
            this.internalField0277 = false;
            ModuleManager localValue2 = RockstarClient.getInstance().getModuleManager();
            if (this.internalField0403 != null && !localValue2.getModules().contains(this.internalField0403)) {
               this.internalField0403 = null;
            }

            for (ModuleCategory localValue6 : ModuleCategory.values()) {
               UiContainer localValue7 = this.internalField1197.get(localValue6);
               ModuleEntry[] localValue8 = this.internalField1560.get(localValue6);
               if (localValue7 != null && localValue8 != null) {
                  List localValue9 = localValue2.getModules()
                     .stream()
                     .sorted(Comparator.comparing(ModuleEntry::getName))
                     .filter(localValue1x -> localValue1x.getCategory() == localValue6 && localValue1x.isAvailable())
                     .toList();
                  if (!internalMethod04748(this.internalField1558.get(localValue6), localValue9)) {
                     if (localValue8[0] != null && !localValue9.contains(localValue8[0])) {
                        ModuleEntry localValue10 = internalMethod06618(localValue9, localValue8[0]);
                        localValue8[0] = localValue10;
                        if (localValue10 == null) {
                           this.internalMethod00193(localValue6);
                        }
                     }

                     List localValue16 = this.internalField1558.get(localValue6);
                     if (localValue16 != null) {
                        for (ModuleEntry localValue12 : (Iterable<ModuleEntry>)(Iterable<?>)localValue16) {
                           if (!localValue9.contains(localValue12)) {
                              WorldPanelScreen.InternalType0216 localValue13 = this.internalField1198.get(localValue12);
                              if (localValue13 != null) {
                                 localValue7.internalMethod01401().removeAll(List.of(localValue13.internalMethod03073(), localValue13.internalMethod03069(), localValue13.internalMethod07166()));
                              }

                              this.internalMethod05906(localValue12);
                           }
                        }
                     }

                     ArrayList localValue18 = new ArrayList();
                     UiNode localValue19 = this.internalField1559.get(localValue6);
                     if (localValue19 != null) {
                        localValue18.add(localValue19);
                     }

                     for (ModuleEntry localValue14 : (Iterable<ModuleEntry>)(Iterable<?>)localValue9) {
                        WorldPanelScreen.InternalType0216 localValue15 = this.internalField1198.computeIfAbsent(localValue14, localValue2x -> this.internalMethod02859(localValue6, localValue2x));
                        localValue18.add(localValue15.internalMethod03073());
                        localValue18.add(localValue15.internalMethod03069());
                        localValue18.add(localValue15.internalMethod07166());
                     }

                     localValue7.internalMethod07849(localValue18);
                     this.internalField1558.put(localValue6, localValue9);
                  }

                  if (localValue8[0] != null) {
                     WorldPanelScreen.InternalType0216 localValue17 = this.internalField1198.get(localValue8[0]);
                     if (localValue17 != null) {
                        this.internalMethod02314(localValue8[0], localValue17.internalMethod07166());
                     }
                  }
               }
            }
         }
      }
   }

   private void internalMethod02314(ModuleEntry localValue1, UiContainer localValue2) {
      List localValue3 = localValue1.getSettings();
      List localValue4 = this.internalField1561.get(localValue1);
      if (!internalMethod04748(localValue4, localValue3)) {
         ArrayList localValue5 = new ArrayList();

         for (Setting localValue7 : (Iterable<Setting>)(Iterable<?>)localValue3) {
            Object localValue8 = this.internalField0544.get(localValue7);
            if (localValue8 == null) {
               UiContainer localValue9 = SettingComponentFactory.internalMethod03724(localValue7);
               this.internalField0544.put(localValue7, localValue9);
               this.internalField0416.add(new WorldPanelScreen.InternalType0267(localValue9, () -> LanguageManager.internalMethod00095(localValue7.getDescriptionKey())));
               localValue8 = localValue9;
            }

            localValue5.add(localValue8);
         }

         if (localValue4 != null) {
            for (Setting localValue11 : (Iterable<Setting>)(Iterable<?>)localValue4) {
               if (!localValue3.contains(localValue11)) {
                  UiNode localValue12 = this.internalField0544.remove(localValue11);
                  if (localValue12 != null) {
                     localValue2.internalMethod01401().remove(localValue12);
                     this.internalField0416.removeIf(localValue1x -> localValue1x.internalMethod04173() == localValue12);
                  }
               }
            }
         }

         localValue2.internalMethod07849(localValue5);
         this.internalField1561.put(localValue1, new ArrayList<>(localValue3));
      }
   }

   private void internalMethod05906(ModuleEntry localValue1) {
      WorldPanelScreen.InternalType0216 localValue2 = this.internalField1198.remove(localValue1);
      this.internalField0543.remove(localValue1);
      if (localValue2 != null) {
         this.internalField0416.removeIf(localValue1x -> localValue1x.internalMethod04173() == localValue2.internalMethod03073());
      }

      List localValue3 = this.internalField1561.remove(localValue1);
      if (localValue3 != null) {
         for (Setting localValue5 : (Iterable<Setting>)(Iterable<?>)localValue3) {
            UiNode localValue6 = this.internalField0544.remove(localValue5);
            if (localValue6 != null) {
               this.internalField0416.removeIf(localValue1x -> localValue1x.internalMethod04173() == localValue6);
            }
         }
      }
   }

   private static ModuleEntry internalMethod06618(List<ModuleEntry> localValue0, ModuleEntry localValue1) {
      for (ModuleEntry localValue3 : localValue0) {
         if (localValue3.getName().equals(localValue1.getName())) {
            return localValue3;
         }
      }

      return null;
   }

   private static boolean internalMethod04748(List<?> localValue0, List<?> localValue1) {
      if (localValue0 == null) {
         return localValue1.isEmpty();
      } else if (localValue0.size() != localValue1.size()) {
         return false;
      } else {
         for (int localValue2 = 0; localValue2 < localValue0.size(); localValue2++) {
            if (localValue0.get(localValue2) != localValue1.get(localValue2)) {
               return false;
            }
         }

         return true;
      }
   }

   private void internalMethod00428(UiRenderContext localValue1) {
      if (!(this.contentAlpha <= 0.01F)) {
         float localValue2 = localValue1.internalMethod05259();
         float localValue3 = localValue1.internalMethod05261();
         String localValue4 = "";
         boolean localValue5 = this.internalField0096 != null
               && (
                  this.internalField0096.contains(localValue2, localValue3)
                     || this.internalField0096.internalMethod10099().inFlow() && this.internalField0096.internalMethod10099().contains(localValue2, localValue3)
                     || this.internalField0096.internalMethod09983().inFlow() && this.internalField0096.internalMethod09983().contains(localValue2, localValue3)
               )
            || this.overlays.stream().anyMatch(localValue2x -> localValue2x.alive() && localValue2x.contains(localValue2, localValue3));
         if (!localValue5) {
            for (WorldPanelScreen.InternalType0267 localValue7 : this.internalField0416) {
               if (localValue7.internalMethod04173().hovered()) {
                  localValue4 = localValue7.internalMethod01214().get();
                  break;
               }
            }
         }

         this.internalField0565.internalMethod04932(this.width / 2.0F, this.height / 2.0F - 145.0F);
         if (!localValue4.contains(".description")) {
            this.internalField0565.internalMethod06105(localValue4);
            this.internalField0565.internalMethod03398(localValue1);
         }
      }
   }

   private void internalMethod03388(UiRenderContext localValue1, UiContainer localValue2, ModuleCategory localValue3) {
      localValue1.drawClientRect(localValue2.x(), localValue2.y(), localValue2.w(), localValue2.h(), 1.0F, 0.0F, 3.0F, 11.0F, !this.internalField1099);
      float localValue4 = 9.0F;
      float localValue5 = localValue2.x() + 8.0F;
      float localValue6 = localValue2.y() + 8.0F;
      localValue1.drawShadow(localValue5, localValue6, localValue4, localValue4, 11.0F, CornerRadii.internalMethod03908(localValue4 / 2.0F), ThemeColors.internalField1610);
      localValue1.drawIcon("category/" + localValue3.internalMethod05277().toLowerCase(), localValue5, localValue6, localValue4, ThemeColors.internalField1310);
      localValue1.drawText(
         Fonts.internalField1157.internalMethod01432(8.0F),
         localValue3.internalMethod05277(),
         localValue2.x() + 21.0F,
         localValue2.y() + 9.5F,
         ThemeColors.internalField1613
      );
      localValue1.drawRect(localValue2.x() + 1.0F, localValue2.y() + 24.0F, localValue2.w() - 2.0F, 1.0F, ThemeColors.internalField1616);
      this.internalMethod07641(localValue1, localValue2);
   }

   private void internalMethod07276(ModuleCategory localValue1, ModuleEntry localValue2) {
      ModuleEntry[] localValue3 = this.internalField1560.get(localValue1);
      if (localValue3 != null && localValue3[0] != localValue2) {
         UiContainer localValue4 = this.internalField1197.get(localValue1);
         if (localValue4 != null && localValue3[0] == null) {
            this.internalField1196.put(localValue1, localValue4.internalMethod09429());
         }

         this.internalField1195.remove(localValue1);
         localValue3[0] = localValue2;
         this.internalField0659.run();
      }
   }

   private void internalMethod07645(ModuleCategory localValue1) {
      ModuleEntry[] localValue2 = this.internalField1560.get(localValue1);
      if (localValue2 != null && localValue2[0] != null) {
         localValue2[0] = null;
         this.internalField0659.run();
         this.internalMethod00193(localValue1);
      }
   }

   private void internalMethod00193(ModuleCategory localValue1) {
      Float localValue2 = this.internalField1196.remove(localValue1);
      if (localValue2 != null && localValue2 > 0.5F) {
         this.internalField1195.put(localValue1, new float[]{localValue2, 6.0F});
      }
   }

   private void internalMethod08492() {
      if (!this.internalField1195.isEmpty()) {
         Iterator localValue1 = this.internalField1195.entrySet().iterator();

         while (localValue1.hasNext()) {
            Entry localValue2 = (Entry)localValue1.next();
            UiContainer localValue3 = this.internalField1197.get(localValue2.getKey());
            float[] localValue4 = (float[])localValue2.getValue();
            if (localValue3 != null && !(localValue3.internalMethod09429() >= localValue4[0] - 0.5F)) {
               if (--localValue4[1] <= 0.0F) {
                  localValue1.remove();
               }

               localValue3.internalMethod05009(localValue4[0]);
            } else {
               localValue1.remove();
            }
         }
      }
   }

   @Override
   public void afterRender(UiRenderContext localValue1) {
      this.internalMethod00428(localValue1);
      this.internalMethod08492();
      WorldPanelScreen.InternalType0215 localValue2 = this.internalField0279;
      if (localValue2 != null) {
         UiNode localValue3 = localValue2.internalField0644 != null ? this.internalField0544.get(localValue2.internalField0644) : this.internalField0543.get(localValue2.internalField0403);
         if (localValue3 == null) {
            if (--localValue2.internalField0228 <= 0) {
               this.internalField0279 = null;
            }
         } else if (--localValue2.internalField0227 <= 0) {
            UiContainer localValue4 = this.internalField1197.get(localValue2.internalField0403.getCategory());
            if (localValue4 != null) {
               if (localValue2.internalField0644 != null) {
                  localValue4.internalMethod01272(localValue3, 5.0F);
               } else {
                  localValue4.internalMethod03631();
               }
            }

            this.internalField0633 = localValue3;
            this.internalField0230 = System.currentTimeMillis() + 1600L;
            this.internalField0279 = null;
         }
      }
   }

   private void internalMethod07641(UiRenderContext localValue1, UiContainer localValue2) {
      if (this.internalField0633 != null) {
         long localValue3 = this.internalField0230 - System.currentTimeMillis();
         if (localValue3 <= 0L) {
            this.internalField0633 = null;
         } else if (this.internalField0633.inFlow() && internalMethod02696(this.internalField0633, localValue2)) {
            float localValue5 = Math.min(1.0F, (float)localValue3 / 400.0F);
            float localValue6 = this.internalField0633.isSticky() ? 0.0F : localValue2.internalMethod09429();
            float localValue7 = this.internalField0633.y() - localValue6;
            float localValue8 = this.internalField0633.h();
            float localValue9 = localValue2.y() + 25.0F;
            float localValue10 = localValue2.y() + localValue2.h() - 1.0F;
            if (localValue7 < localValue9) {
               localValue8 -= localValue9 - localValue7;
               localValue7 = localValue9;
            }

            if (localValue7 + localValue8 > localValue10) {
               localValue8 = localValue10 - localValue7;
            }

            if (!(localValue8 <= 0.0F)) {
               localValue1.drawRoundedRect(
                  localValue2.x() + 2.0F,
                  localValue7,
                  localValue2.w() - 4.0F,
                  localValue8,
                  CornerRadii.internalMethod03908(4.0F),
                  ThemeColors.internalField1310.mulAlpha(0.18F * localValue5)
               );
            }
         }
      }
   }

   private static boolean internalMethod02696(UiNode localValue0, UiNode localValue1) {
      for (UiNode localValue2 = localValue0; localValue2 != null; localValue2 = localValue2.parent()) {
         if (localValue2 == localValue1) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod02212(UiNode localValue1) {
      if (!localValue1.shaking()) {
         localValue1.shake();
         if (RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class).isEnabled()) {
            ClientSoundEvents.internalField1014.internalMethod03132(1.0F, 1.0F);
         }
      }
   }

   static {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(new WorldPanelScreen.InternalType0266());
   }

   static final class InternalType0215 {
      final ModuleEntry internalField0403;
      final Setting internalField0644;
      int internalField0227 = 2;
      int internalField0228 = 40;

      InternalType0215(ModuleEntry localValue1, Setting localValue2) {
         this.internalField0403 = localValue1;
         this.internalField0644 = localValue2;
      }
   }

   static final class InternalType0216 {
      private final UiContainer internalField0634;
      private final UiElement internalField0626;
      private final UiContainer internalField0635;

      InternalType0216(UiContainer localValue1, UiElement localValue2, UiContainer localValue3) {
         this.internalField0634 = localValue1;
         this.internalField0626 = localValue2;
         this.internalField0635 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0216[header=" + this.internalField0634 + ", divider=" + this.internalField0626 + ", settings=" + this.internalField0635 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0634);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0626);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0635);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldPanelScreen.InternalType0216 other = (WorldPanelScreen.InternalType0216) localValue1;
         return java.util.Objects.equals(this.internalField0634, other.internalField0634)
            && java.util.Objects.equals(this.internalField0626, other.internalField0626)
            && java.util.Objects.equals(this.internalField0635, other.internalField0635);
      }

      public UiContainer internalMethod03073() {
         return this.internalField0634;
      }

      public UiElement internalMethod03069() {
         return this.internalField0626;
      }

      public UiContainer internalMethod07166() {
         return this.internalField0635;
      }
   }

   static final class InternalType0266 {
      final EventListener<HudRenderEvent> internalField0157 = localValue0 -> {
         WorldPanelScreen localValue1 = WorldPanelScreen.internalField0551;
         if (localValue1 != null) {
            MinecraftClient localValue2 = MinecraftClient.getInstance();
            if (localValue1.internalMethod09809() >= 1.0F) {
               WorldPanelScreen.internalField0551 = null;
               WorldPanelScreen.internalField1100 = false;
               WorldPanelScreen.internalField1102 = false;
            } else if (localValue2.currentScreen != null) {
               WorldPanelScreen.internalField1102 = false;
            } else {
               if (!WorldPanelScreen.internalField1100) {
                  WorldPanelScreen.internalField0769.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                  WorldPanelScreen.internalField0769.internalMethod02227(true);
                  UiBatchRenderer.internalField0277 = true;

                  try {
                     localValue1.render(UiRenderContext.internalMethod02316(localValue0.getContext(), -1, -1, localValue0.getTickDelta()));
                  } finally {
                     UiBatchRenderer.internalField0277 = false;
                     WorldPanelScreen.internalField0769.internalMethod03248();
                  }

                  WorldPanelScreen.internalField1100 = true;
               }

               if (!WorldPanelScreen.internalField1102) {
                  WorldPanelScreen.internalMethod01216(localValue0);
               }

               WorldPanelScreen.internalField1102 = false;
            }
         }
      };
      final EventListener<Render3DEvent> internalField0158 = EventListener.internalMethod05136(
         Integer.MIN_VALUE,
         localValue0 -> {
            WorldPanelScreen localValue1 = WorldPanelScreen.internalField0551;
            if (localValue1 != null && WorldPanelScreen.internalField1100 && localValue1.internalField0283 != null) {
               MinecraftClient localValue2 = MinecraftClient.getInstance();
               float localValue3 = localValue1.internalMethod09809();
               float localValue4 = Math.min(1.0F, localValue3 / 0.4F);
               float localValue5 = localValue3 <= 0.4F ? 0.0F : (localValue3 - 0.4F) / 0.6F;
               float localValue6 = Easing.internalField1328.ease(localValue4, 0.0F, 1.0F, 1.0F);
               float localValue7 = 1.0F - localValue6;
               float localValue8 = Math.min(1.0F, Math.max(0.0F, (localValue5 - 0.15F) / 0.85F));
               float localValue10 = (float)(
                  Math.tan(Math.toRadians(((Integer)localValue2.options.getFov().getValue()).intValue()) / 2.0) / Math.tan(Math.toRadians(110.0) / 2.0)
               );
               float localValue11 = (3.3F + localValue7) * localValue10;
               float localValue12 = localValue11 * ((float)localValue2.getWindow().getFramebufferWidth() / localValue2.getWindow().getFramebufferHeight());
               Vec3d localValue13 = localValue2.gameRenderer.getCamera().getCameraPos();
               Vec3d localValue14 = localValue1.internalField0283;
               Vec3d localValue15 = localValue1.internalField1104.multiply(localValue12 / 2.0);
               Vec3d localValue16 = localValue1.internalField1106.multiply(localValue11 / 2.0);
               Vec3d localValue17 = localValue14.subtract(localValue15).add(localValue16).subtract(localValue13);
               Vec3d localValue18 = localValue14.subtract(localValue15).subtract(localValue16).subtract(localValue13);
               Vec3d localValue19 = localValue14.add(localValue15).subtract(localValue16).subtract(localValue13);
               Vec3d localValue20 = localValue14.add(localValue15).add(localValue16).subtract(localValue13);
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderSystem.disableDepthTest();
               RenderSystem.disableCull();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, WorldPanelScreen.internalField0769.getColorAttachmentView());
               Matrix4f localValue21 = localValue0.getMatrices().peek().getPositionMatrix();
               int localValue22 = ColorRGBA.WHITE.withAlpha(255.0F * localValue7).getRGB();
               BufferBuilder localValue23 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               localValue23.vertex(localValue21, (float)localValue17.x, (float)localValue17.y, (float)localValue17.z).texture(0.0F, 1.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue18.x, (float)localValue18.y, (float)localValue18.z).texture(0.0F, 0.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue19.x, (float)localValue19.y, (float)localValue19.z).texture(1.0F, 0.0F).color(localValue22);
               localValue23.vertex(localValue21, (float)localValue20.x, (float)localValue20.y, (float)localValue20.z).texture(1.0F, 1.0F).color(localValue22);
               BufferRenderer.drawWithGlobalProgram(localValue23.end());
               WorldPanelScreen.internalField1102 = true;
               RenderSystem.enableDepthTest();
               RenderSystem.enableCull();
               RenderSystem.disableBlend();
               RenderSystem.setShaderTexture(0, 0);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               if (localValue3 >= 1.0F) {
                  WorldPanelScreen.internalField0551 = null;
                  WorldPanelScreen.internalField1100 = false;
               }
            }
         }
      );
   }

   static final class InternalType0267 {
      private final UiNode internalField0633;
      private final Supplier<String> internalField0017;

      InternalType0267(UiNode localValue1, Supplier<String> localValue2) {
         this.internalField0633 = localValue1;
         this.internalField0017 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0267[view=" + this.internalField0633 + ", text=" + this.internalField0017 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0633);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0017);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WorldPanelScreen.InternalType0267 other = (WorldPanelScreen.InternalType0267) localValue1;
         return java.util.Objects.equals(this.internalField0633, other.internalField0633)
            && java.util.Objects.equals(this.internalField0017, other.internalField0017);
      }

      public UiNode internalMethod04173() {
         return this.internalField0633;
      }

      public Supplier<String> internalMethod01214() {
         return this.internalField0017;
      }
   }
}
