package haron.gui.core;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.audio.SoundPlayer;
import haron.client.MinecraftClientAccess;
import haron.gui.config.ConfigsTab;
import haron.gui.core.LogoOverlay;
import haron.gui.core.ClickGuiFrame;
import haron.gui.core.DockPosition;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.core.GuiLayerRegistry;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiTabType;
import haron.gui.core.DockTab;
import haron.gui.events.CommandBindsPanel;
import haron.gui.friends.FriendsTab;
import haron.gui.markers.MarkersPanel;
import haron.gui.modules.ModuleSettingsOverlay;
import haron.gui.modules.ModulesTab;
import haron.gui.settings.ModeSettingRow;
import haron.gui.settings.TextSettingRow;
import haron.hud.core.HudManager;
import haron.modules.hud.ClientColor;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ScaledGuiProjection;
import haron.render.icons.HaronIcons;
import haron.render.ScreenPoint;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.haron.Haron;

public class ClickGuiScreen
extends Screen {
    public static Color BG_MAIN;
    public static Color BG_GREY;
    public static Color BG_TOP;
    public static Color BG_BOT;
    public static Color DOCK_CELL_BG_TOP;
    public static Color DOCK_CELL_BG_BOT;
    public static Color DOCK_CELL_SEL_TOP;
    public static Color DOCK_CELL_SEL_BOT;
    public static Color DOCK_ACCENT;
    private static final Color DOCK_BACKDROP;
    private static final float GUI_WIDTH = 436.0f;
    private static final float GUI_HEIGHT = 262.0f;
    private static final float DOCK_CELL_HEIGHT = 10.5f;
    private static final float DOCK_CELL_WIDTH = 28.0f;
    private static final double ANIMATION_SPEED = 0.31;
    private static final float GUI_Y_OFFSET = 0.0f;
    private static final float GUI_X_OFFSET = 21.0f;
    private static final float DOCK_CORNER = 14.0f;
    private static final float DOCK_CELL_S = 26.0f;
    private static final float DOCK_PAD = 8.0f;
    private static final float DOCK_GAP = 8.0f;
    private static final float DOCK_JOIN_OVERLAP = 0.0f;
    private static final float DOCK_HEADER_H = 40.0f;
    private static final int DOCK_ICON_FONT_SIZE = 20;
    private static final float DOCK_ICON_VISUAL_H = 13.0f;
    private static final float DOCK_LOGO_SIZE = 24.0f;
    private static final float DOCK_LOGO_PAD = 6.0f;
    private static final float DOCK_INDICATOR_W = 2.0f;
    private static final float DOCK_INDICATOR_H = 10.0f;
    private static final int DOCK_INDICATOR_ALPHA = 130;
    private final ClickGuiFrame frameOverlay;
    private final LogoOverlay logoOverlay;
    private final ModuleSettingsOverlay settingsOverlay;
    private final Map<ClickGuiTabType, ClickGuiTab> tabs;
    private ClickGuiTabType currentTabType;
    private ClickGuiTabType previousTabType = null;
    private final AnimatedValue animationState;
    private final AnimatedValue openAnim;
    private boolean isSwitching = false;
    private int switchDirection = 1;
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean dockOpen;
    private boolean dockDragging;
    private final AnimatedValue[] dockHoverAnims;
    private DockPosition dock = DockPosition.LEFT;
    private int dockHoveredTab = -1;
    public static int someStaticInt;
    public static boolean someStaticBool;
    public int clickBtn = 0;
    private static final DockTab[] DOCK_TABS;

    public ClickGuiTab getCurrentTab() {
        return this.tabs.get((Object)this.currentTabType);
    }

    public ClickGuiTab getPreviousTab() {
        if (this.previousTabType != null) {
            return this.tabs.get((Object)this.previousTabType);
        }
        return null;
    }

    public static void updateColors() {
        Color color = ClientColor.currentColor();
        if (color == null) {
            color = new Color(255, 155, 35);
        }
        pryrvd.update(color);
        BG_MAIN = ClickGuiScreen.withAlpha(color, 80);
        BG_GREY = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 1.2f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 1.2f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 1.2f)), 85);
        BG_TOP = ClickGuiScreen.withAlpha(color, 80);
        BG_BOT = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 1.3f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 1.3f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 1.3f)), 80);
        DOCK_CELL_BG_TOP = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 1.5f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 1.5f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 1.5f)), 90);
        DOCK_CELL_BG_BOT = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 1.7f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 1.7f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 1.7f)), 90);
        DOCK_CELL_SEL_TOP = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 0.9f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 0.9f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 0.9f)), 100);
        DOCK_CELL_SEL_BOT = new Color(ClickGuiScreen.clamp((int)((float)color.getRed() * 0.7f)), ClickGuiScreen.clamp((int)((float)color.getGreen() * 0.7f)), ClickGuiScreen.clamp((int)((float)color.getBlue() * 0.7f)), 100);
        DOCK_ACCENT = ClickGuiScreen.withAlpha(color, 255);
    }

    public boolean isDockActive() {
        return this.dockOpen || this.dockDragging;
    }

    private boolean isInsideClickGui(int n, int n2, float f, float f2) {
        return GuiInput.a(f, f2, 436.0f, 262.0f, (double)n, (double)n2);
    }

    public void renderDockPanel(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3 = this.dockPanelW();
        float f4 = this.dockPanelH();
        float f5 = this.dockPanelX(f);
        float f6 = this.dockPanelY(f2);
        s7swsm2.a(f5, f6, f3, f4, 14.0f, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, matrixStack);
        float f7 = f5 + f3 - 14.0f;
        float f8 = f - f7 + 1.0f;
        if (f8 > 0.0f) {
            s7swsm2.a(f7, f6, f8, f4, 0.0f, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, matrixStack);
        }
        float f9 = 18.0f;
        float f10 = 14.0f;
        s7swsm2.a(f - 1.0f, f6, 18.0f, 14.0f, 0.0f, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, matrixStack);
        s7swsm2.a(f - 1.0f, f6 + f4 - 14.0f, 18.0f, 14.0f, 0.0f, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, matrixStack);
        Identifier identifier = HaronIcons.get("logo");
        float f11 = f5 + (f3 - 24.0f) / 2.0f;
        float f12 = f6 + 8.0f;
        this.renderDockLogo(s7swsm2, identifier, f11, f12, matrixStack);
        float f13 = f6 + 40.0f;
        Color color = pryrvd.a(pryrvd.ACCENT, 40);
        s7swsm2.a(f5 + 6.0f, f13, f3 - 12.0f, 1.5f, 0.0f, color, color, color, color, matrixStack);
        float f14 = f6 + 40.0f;
        this.dockHoveredTab = -1;
        for (int i = 0; i < DOCK_TABS.length; ++i) {
            float f15 = f5 + 8.0f;
            float f16 = f14 + 8.0f + (float)i * 34.0f;
            if (!((float)n >= f15 - 4.0f) || !((float)n <= f15 + 26.0f + 4.0f) || !((float)n2 >= f16 - 4.0f) || !((float)n2 <= f16 + 26.0f + 4.0f)) continue;
            this.dockHoveredTab = i;
        }
        FontRenderer v6hnga2 = ClientFonts.MEDIUM[20];
        for (int i = 0; i < DOCK_TABS.length; ++i) {
            float f17;
            Color color2;
            Object object;
            DockTab xwbbj72 = DOCK_TABS[i];
            float f18 = f5 + 8.0f;
            float f19 = f14 + 8.0f + (float)i * 34.0f;
            boolean bl = this.isDockTabSelected(xwbbj72);
            this.dockHoverAnims[i].a(this.dockHoveredTab == i ? 1.0 : 0.0, 0.12, Easings.n);
            this.dockHoverAnims[i].a();
            float f20 = (float)this.dockHoverAnims[i].j();
            if (bl) {
                s7swsm2.a(f18, f19, 26.0f, 26.0f, 9.0f, pryrvd.ACCENT_SOFT, pryrvd.ACCENT_SOFT, pryrvd.ACCENT_DARK, pryrvd.ACCENT_DARK, matrixStack);
                object = pryrvd.a(pryrvd.ACCENT, 130);
                float f21 = f19 + 8.0f;
                s7swsm2.a(f5 + 2.5f, f21, 2.0f, 10.0f, 1.0f, (Color)object, (Color)object, (Color)object, (Color)object, matrixStack);
            } else {
                s7swsm2.a(f18 - 2.0f, f19 - 2.0f, 30.0f, 30.0f, 10.0f, pryrvd.CELL_BG_TOP, pryrvd.CELL_BG_TOP, pryrvd.CELL_BG_BOT, pryrvd.CELL_BG_BOT, matrixStack);
                if (f20 > 0.01f) {
                    int n3 = (int)(f20 * 28.0f);
                    color2 = new Color(pryrvd.ACCENT.getRed(), pryrvd.ACCENT.getGreen(), pryrvd.ACCENT.getBlue(), n3);
                    f17 = 18.0f;
                    s7swsm2.a(f18 - 2.0f, f19 - 2.0f, 18.0f, 30.0f, 10.0f, color2, color2, color2, color2, matrixStack);
                }
            }
            object = xwbbj72.icon;
            color2 = bl ? new Color(255, 255, 255, 230) : pryrvd.a(pryrvd.ACCENT, 210);
            f17 = v6hnga2.a((String)object);
            float f22 = f18 + (26.0f - f17) / 2.0f;
            float f23 = f19 + 6.5f - 1.0f;
            v6hnga2.a((String)object, f22, (double)f23, color2, matrixStack);
        }
    }

    public void onDockClick(float f, float f2, int n, int n2) {
        this.dockOpen = true;
        this.dockDragging = true;
        float f3 = this.dockPanelX(f);
        float f4 = this.dockPanelY(f2);
        float f5 = f4 + 40.0f;
        for (int i = 0; i < DOCK_TABS.length; ++i) {
            float f6 = f3 + 8.0f;
            float f7 = f5 + 8.0f + (float)i * 34.0f;
            if (!((float)n >= f6 - 4.0f) || !((float)n <= f6 + 26.0f + 4.0f) || !((float)n2 >= f7 - 4.0f) || !((float)n2 <= f7 + 26.0f + 4.0f)) continue;
            DockTab xwbbj72 = DOCK_TABS[i];
            if (xwbbj72.tab == null) {
                return;
            }
            SoundPlayer.play("disabled1", 0.5f);
            if (xwbbj72.subCategory >= 0) {
                this.a(xwbbj72.tab, xwbbj72.subCategory, true);
            } else {
                this.a(xwbbj72.tab);
            }
            return;
        }
    }

    public void onDockReleaseEnd(float f, float f2, int n, int n2) {
        this.dockDragging = false;
    }

    private void switchTabLeft() {
        ClickGuiTabType[] xtnc35Array = ClickGuiTabType.values();
        int n = this.currentTabType.ordinal();
        int n2 = (n + 1) % xtnc35Array.length;
        this.a(xtnc35Array[n2], 1, false);
    }

    private void switchTabRight() {
        ClickGuiTabType[] xtnc35Array = ClickGuiTabType.values();
        int n = this.currentTabType.ordinal();
        int n2 = Math.floorMod(n - 1, xtnc35Array.length);
        this.a(xtnc35Array[n2], -1, false);
    }

    public static float getGuiWidth() {
        return 436.0f;
    }

    public static float getGuiHeight() {
        int n = 802;
        return 262.0f;
    }

    public static float getDockCellWidth() {
        return 28.0f;
    }

    public static String someStaticMethod(String string, String string2, int n, int n2, int n3, int n4) {
        return null;
    }

    public boolean isDockDragging() {
        return this.dockDragging;
    }

    public static float getDockCellHeight() {
        return 10.5f;
    }

    public FriendsTab getFriendsTab() {
        ClickGuiTab ta3d0p2 = this.tabs.get((Object)ClickGuiTabType.FRIENDS);
        return ta3d0p2 instanceof FriendsTab ? (FriendsTab)ta3d0p2 : null;
    }

    public ConfigsTab getConfigsTab() {
        return null;
    }

    private boolean isDockTabSelected(DockTab xwbbj72) {
        if (xwbbj72.tab == null) {
            return false;
        }
        ClickGuiTabType xtnc352 = this.k();
        if (xwbbj72.tab != xtnc352) {
            return false;
        }
        if (xwbbj72.subCategory >= 0) {
            return this.getCurrentSubCategory() == xwbbj72.subCategory;
        }
        return true;
    }

    private boolean isOverOpenSettings(int n, int n2) {
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        return ta3d0p2 instanceof ModulesTab && ((ModulesTab)ta3d0p2).hasOpenSettingsAt(n, n2);
    }

    private void renderDockLogo(ShapeRenderer s7swsm2, Identifier identifier, float f, float f2, MatrixStack matrixStack) {
        try {
            Color color = ClientColor.currentColor();
            s7swsm2.a(identifier, f, f2, 24.0f, 24.0f, 7.2000003f, 0.0f, 0.0f, 1.0f, 1.0f, color != null ? color : new Color(255, 255, 255, 255), matrixStack);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void onDockRelease(float f, float f2, int n, int n2) {
        this.dockDragging = false;
    }

    public void removed() {
        int n = 581;
        this.cleanup();
        HudManager.a().a(false);
        super.removed();
    }

    public boolean shouldPause() {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }
        if (HudManager.a().b(mouseX, mouseY, button)) {
            return true;
        }
        ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
        int n = screenPoint.x();
        int n2 = screenPoint.y();
        float f = this.getGuiX();
        float f2 = this.getGuiY();
        GuiLayerRegistry.a().a(n, n2);
        this.onDockReleaseEnd(f, f2, n, n2);
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        if (ta3d0p2 == null) {
            return true;
        }
        ta3d0p2.c(f, f2, n, n2);
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button != 0) {
            return false;
        }
        HudManager.a().a(mouseX, mouseY, deltaX, deltaY);
        if (HudManager.a().b(mouseX, mouseY)) {
            return true;
        }
        ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
        int n = screenPoint.x();
        int n2 = screenPoint.y();
        float f = this.getGuiX();
        float f2 = this.getGuiY();
        try {
            this.onDockDrag(f, f2, n, n2, deltaX, deltaY);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            ClickGuiTab ta3d0p2 = this.getCurrentTab();
            if (ta3d0p2 != null) {
                ta3d0p2.a(f, f2, n, n2, deltaX, deltaY);
            }
            return true;
        }
        catch (Throwable throwable) {
            return true;
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        double d;
        ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
        int n = screenPoint.x();
        int n2 = screenPoint.y();
        GuiLayerRegistry.a().a(n, n2);
        float f = this.getGuiX();
        float f2 = this.getGuiY();
        double d2 = d = verticalAmount != 0.0 ? verticalAmount : horizontalAmount;
        if (d == 0.0) {
            return false;
        }
        boolean bl = ModeSettingRow.isAnyDropdownOpen();
        if (bl) {
            ModeSettingRow tn8pw72 = ModeSettingRow.getOpenDropdown();
            if (tn8pw72 != null) {
                tn8pw72.scrollDropdown(d);
            }
            return true;
        }
        if (this.isOverOpenSettings(n, n2)) {
            try {
                ClickGuiTab ta3d0p2 = this.getCurrentTab();
                if (ta3d0p2 instanceof ModulesTab) {
                    ((ModulesTab)ta3d0p2).a((float)d, n, n2);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return true;
        }
        if (!this.isInsideClickGui(n, n2, f, f2) && HudManager.a().a(mouseX, mouseY, d)) {
            return true;
        }
        boolean bl2 = false;
        try {
            ClickGuiTab ta3d0p3 = this.getCurrentTab();
            if (ta3d0p3 instanceof ModulesTab) {
                ((ModulesTab)ta3d0p3).a((float)d, n, n2);
                bl2 = true;
            } else if (ta3d0p3 != null) {
                ta3d0p3.a((float)d);
                bl2 = true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            this.onDockDrag(f, f2, n, n2, 0.0, -d * 20.0);
            bl2 = true;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return bl2;
    }

    public boolean charTyped(char chr, int modifiers) {
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        if (ta3d0p2 instanceof ModulesTab && ((ModulesTab)ta3d0p2).a(chr, modifiers)) {
            return true;
        }
        if (ta3d0p2 instanceof MarkersPanel && ((MarkersPanel)ta3d0p2).a(chr, modifiers)) {
            return true;
        }
        if (ta3d0p2 instanceof FriendsTab && ((FriendsTab)ta3d0p2).a(chr, modifiers)) {
            return true;
        }
        return ta3d0p2 instanceof CommandBindsPanel && ((CommandBindsPanel)ta3d0p2).a(chr, modifiers);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 || button == 1 || button == 2) {
            return this.dispatchPointerClick(mouseX, mouseY, button == 2 ? 0 : button);
        }
        return false;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        s7swsm2.a(0.0f, 0.0f, (float)minecraftClient.getWindow().getScaledWidth(), (float)minecraftClient.getWindow().getScaledHeight(), DOCK_BACKDROP, matrixStack);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            ClickGuiTab ta3d0p2 = this.getCurrentTab();
            if (ta3d0p2 != null) {
                try {
                    Object object = ta3d0p2.getClass().getMethod("a", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)ta3d0p2, keyCode, scanCode, modifiers);
                    if (object instanceof Boolean && ((Boolean)object).booleanValue()) {
                        return true;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            this.close();
            return true;
        }
        ClickGuiTab ta3d0p3 = this.getCurrentTab();
        if (ta3d0p3 == null) {
            return false;
        }
        try {
            Object object = ta3d0p3.getClass().getMethod("a", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke((Object)ta3d0p3, keyCode, scanCode, modifiers);
            if (object instanceof Boolean) {
                return (Boolean)object;
            }
            return false;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static Color withAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    private void renderTab(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, ClickGuiTab ta3d0p2) {
        this.frameOverlay.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.frameOverlay.b(matrixStack, s7swsm2, f, f2, n, n2);
        if (ta3d0p2 != null) {
            ta3d0p2.a(matrixStack, s7swsm2, f, f2, n, n2);
        }
    }

    private float dockPanelX(float f) {
        return f - this.dockPanelW() + 0.0f;
    }

    private float dockPanelY(float f) {
        int n = 352;
        return f;
    }

    public boolean isDockOpen() {
        return this.dockOpen;
    }

    public void closeDock() {
        this.dockOpen = false;
        this.dockDragging = false;
    }

    private float dockPanelH() {
        return 262.0f;
    }

    public void renderDock(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.renderDockPanel(matrixStack, s7swsm2, f, f2, n, n2);
    }

    private float dockPanelW() {
        return 42.0f;
    }

    private float getGuiX() {
        float f = 218.0f;
        return (float)MinecraftClientAccess.d.getWidth() / 4.0f - 218.0f + 21.0f;
    }

    public void onDockDrag(float f, float f2, int n, int n2, double d, double d2) {
    }

    private float getGuiY() {
        float f = 131.0f;
        return (float)MinecraftClientAccess.d.getHeight() / 4.0f - 131.0f + 0.0f;
    }

    public void resetAll() {
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        if (ta3d0p2 instanceof ModulesTab) {
            ((ModulesTab)ta3d0p2).m();
            ((ModulesTab)ta3d0p2).n();
        }
        if (ta3d0p2 instanceof MarkersPanel) {
            ((MarkersPanel)ta3d0p2).e();
            ((MarkersPanel)ta3d0p2).f();
        }
        if (this.isDockActive()) {
            this.closeDock();
        }
        InteractionOverlayController.a().a(false);
        InteractionOverlayController.a().c(false);
        GuiInput.a();
    }

    protected void init() {
        super.init();
        ClickGuiScreen.updateColors();
        HudManager.a().a(true);
        this.openAnim.d(0.0);
        this.openAnim.a(1.0, 0.8, Easings.h);
        try {
            SoundEvent soundEvent = SoundEvent.of((Identifier)Identifier.of((String)"haron", (String)"clickgui_open"));
            MinecraftClient.getInstance().getSoundManager().play((SoundInstance)PositionedSoundInstance.master((SoundEvent)soundEvent, (float)1.0f));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void close() {
        SoundPlayer.play("disabled3", 0.5f);
        this.cleanup();
        InteractionOverlayController.a().f();
        TextSettingRow.o();
        GuiInput.j();
        GuiInput.a();
        super.close();
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ClickGuiScreen.updateColors();
        this.openAnim.a();
        float f = (float)this.openAnim.j();
        float f2 = Math.max(0.0f, Math.min(1.0f, f));
        MatrixStack matrixStack = context.getMatrices();
        GuiInput.h();
        ScaledGuiProjection.a(2.0);
        matrixStack.push();
        RenderSystem.setShaderColor((float)f2, (float)f2, (float)f2, (float)1.0f);
        try {
            float f3;
            float f4;
            ClickGuiTab ta3d0p2;
            ScreenPoint screenPoint = ScaledGuiProjection.a(mouseX, mouseY);
            int n = screenPoint.x();
            int n2 = screenPoint.y();
            this.mouseX = n;
            this.mouseY = n2;
            GuiLayerRegistry.a().b();
            GuiLayerRegistry.a().a(n, n2);
            float f5 = 218.0f;
            float f6 = 131.0f;
            float f7 = this.getGuiX();
            float f8 = this.getGuiY();
            ShapeRenderer s7swsm2 = Haron.getInstance().getRender();
            HaronIcons.get("crypt");
            if (this.isSwitching && this.animationState.d()) {
                this.isSwitching = false;
                this.previousTabType = null;
            }
            float f9 = (float)this.animationState.j();
            boolean bl = this.getDock() == DockPosition.BOTTOM;
            float f10 = (float)MinecraftClientAccess.d.getWidth() / 4.0f + 218.0f + 50.0f;
            float f11 = (float)MinecraftClientAccess.d.getHeight() / 4.0f + 131.0f + 50.0f;
            if (this.isSwitching && this.previousTabType != null && (ta3d0p2 = this.getPreviousTab()) != null) {
                f4 = 0.0f;
                f3 = 0.0f;
                if (bl) {
                    f3 = f9 * f11 * (float)(-this.switchDirection);
                } else {
                    f4 = f9 * f10 * (float)(-this.switchDirection);
                }
                this.renderTab(matrixStack, s7swsm2, f7 + f4, f8 + f3, n, n2, ta3d0p2);
            }
            if ((ta3d0p2 = this.getCurrentTab()) != null) {
                if (this.isSwitching) {
                    f4 = 0.0f;
                    f3 = 0.0f;
                    if (bl) {
                        f3 = (1.0f - f9) * f11 * (float)this.switchDirection;
                    } else {
                        f4 = (1.0f - f9) * f10 * (float)this.switchDirection;
                    }
                    this.renderTab(matrixStack, s7swsm2, f7 + f4, f8 + f3, n, n2, ta3d0p2);
                } else {
                    this.renderTab(matrixStack, s7swsm2, f7, f8, n, n2, ta3d0p2);
                }
            }
            if (this.isDockActive()) {
                if (ta3d0p2 instanceof ModulesTab) {
                    ((ModulesTab)ta3d0p2).a(matrixStack, s7swsm2, n, n2);
                }
                this.settingsOverlay.a(matrixStack, s7swsm2, f7, f8, n, n2);
                InteractionOverlayController.a().a(matrixStack, s7swsm2);
                this.renderDockPanel(matrixStack, s7swsm2, f7, f8, n, n2);
                this.settingsOverlay.a(matrixStack, s7swsm2, n, n2);
            } else {
                this.renderDockPanel(matrixStack, s7swsm2, f7, f8, n, n2);
                this.settingsOverlay.a(matrixStack, s7swsm2, f7, f8, n, n2);
                InteractionOverlayController.a().a(matrixStack, s7swsm2);
                if (ta3d0p2 instanceof ModulesTab) {
                    ((ModulesTab)ta3d0p2).a(matrixStack, s7swsm2, n, n2);
                }
                if (ta3d0p2 instanceof MarkersPanel) {
                    ((MarkersPanel)ta3d0p2).a(matrixStack, s7swsm2, n, n2);
                }
                this.settingsOverlay.a(matrixStack, s7swsm2, n, n2);
            }
            GuiInput.i();
        }
        finally {
            matrixStack.pop();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        super.render(context, mouseX, mouseY, delta);
        ScaledGuiProjection.a();
    }

    public DockPosition getDock() {
        return this.dock;
    }

    public void setDock(DockPosition dock) {
        if (dock != null) {
            this.dock = dock;
        }
    }

    public int getCurrentSubCategory() {
        ClickGuiTab ta3d0p2 = this.tabs.get((Object)this.currentTabType);
        if (ta3d0p2 instanceof ModulesTab) {
            return ((ModulesTab)ta3d0p2).getCurrentSubCategory();
        }
        return -1;
    }

    public boolean dispatchPointerClick(double d, double d2, int n) {
        this.clickBtn = n;
        GuiInput.a();
        ScreenPoint screenPoint = ScaledGuiProjection.a(d, d2);
        int n2 = screenPoint.x();
        int n3 = screenPoint.y();
        float f = this.getGuiX();
        float f2 = this.getGuiY();
        boolean bl = this.isOverOpenSettings(n2, n3);
        boolean bl2 = this.isInsideClickGui(n2, n3, f, f2);
        GuiLayerRegistry.a().a(n2, n3);
        this.onDockClick(f, f2, n2, n3);
        if (!bl && !bl2) {
            HudManager.a().a(d, d2, n);
            return true;
        }
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        if (ta3d0p2 == null) {
            return true;
        }
        if (!(ta3d0p2 instanceof ModulesTab)) {
            ta3d0p2.a(f, f2, n2, n3);
            return true;
        }
        if (n == 1) {
            ta3d0p2.b(f, f2, n2, n3);
            return true;
        }
        if (bl || (float)n3 >= f2 + 48.0f) {
            ta3d0p2.b(f, f2, n2, n3);
            return true;
        }
        ta3d0p2.a(f, f2, n2, n3);
        return true;
    }

    public ClickGuiScreen() {
        super((Text)Text.literal((String)"Haron"));
        this.currentTabType = ClickGuiTabType.MODULES;
        this.animationState = new AnimatedValue();
        this.openAnim = new AnimatedValue();
        this.frameOverlay = new ClickGuiFrame();
        this.logoOverlay = new LogoOverlay();
        this.tabs = new HashMap<ClickGuiTabType, ClickGuiTab>();
        this.tabs.put(ClickGuiTabType.MODULES, new ModulesTab());
        this.tabs.put(ClickGuiTabType.MARKERS, new MarkersPanel());
        this.tabs.put(ClickGuiTabType.FRIENDS, new FriendsTab());
        this.tabs.put(ClickGuiTabType.EVENTS, new CommandBindsPanel());
        this.settingsOverlay = new ModuleSettingsOverlay(this);
        this.animationState.d(1.0);
        this.openAnim.d(0.0);
        this.openAnim.a(1.0, 0.8, Easings.h);
        this.dockHoverAnims = new AnimatedValue[DOCK_TABS.length];
        for (int i = 0; i < DOCK_TABS.length; ++i) {
            this.dockHoverAnims[i] = new AnimatedValue();
        }
    }

    static {
        DOCK_BACKDROP = new Color(0, 0, 0, 10);
        ClickGuiScreen.updateColors();
        DOCK_TABS = new DockTab[]{DockTab.MODULES_VISUALS, DockTab.MODULES_HUD, DockTab.MODULES_UTILITIES, DockTab.MARKERS, DockTab.FRIENDS, DockTab.EVENTS};
    }

    public static float e() {
        return 262.0f;
    }

    private static int clamp(int n) {
        return Math.min(255, Math.max(0, n));
    }

    public ClickGuiTab b() {
        return this.getPreviousTab();
    }

    public void c() {
        this.resetAll();
    }

    public static float f() {
        return 10.5f;
    }

    public boolean l() {
        return this.isSwitching;
    }

    public static float d() {
        return 436.0f;
    }

    private int a(ClickGuiTabType xtnc352, ClickGuiTabType xtnc353) {
        return xtnc353.ordinal() <= xtnc352.ordinal() ? -1 : 1;
    }

    public ClickGuiTab a() {
        return this.getCurrentTab();
    }

    public void a(ClickGuiTabType xtnc352, int n, boolean bl) {
        ClickGuiTab ta3d0p2;
        if (xtnc352 == ClickGuiTabType.CONFIGS) {
            return;
        }
        if (this.currentTabType != xtnc352) {
            ta3d0p2 = this.getCurrentTab();
            if (ta3d0p2 instanceof ModulesTab) {
                ((ModulesTab)ta3d0p2).k();
            }
            if (ta3d0p2 instanceof MarkersPanel) {
                ((MarkersPanel)ta3d0p2).e();
            }
            this.currentTabType = xtnc352;
            this.previousTabType = null;
            this.isSwitching = false;
            this.animationState.d(1.0);
        }
        if (xtnc352 == ClickGuiTabType.MODULES && bl && (ta3d0p2 = this.tabs.get((Object)xtnc352)) instanceof ModulesTab) {
            ((ModulesTab)ta3d0p2).a(n);
        }
    }

    public void a(ClickGuiTabType xtnc352, int n) {
        if (xtnc352 == ClickGuiTabType.CONFIGS) {
            return;
        }
        if (this.currentTabType != xtnc352) {
            ClickGuiTab ta3d0p2 = this.getCurrentTab();
            if (ta3d0p2 instanceof ModulesTab) {
                ((ModulesTab)ta3d0p2).k();
            }
            if (ta3d0p2 instanceof MarkersPanel) {
                ((MarkersPanel)ta3d0p2).e();
            }
            this.currentTabType = xtnc352;
            this.previousTabType = null;
            this.isSwitching = false;
            this.animationState.d(1.0);
        }
    }

    public void a(ClickGuiTabType xtnc352) {
        if (xtnc352 == ClickGuiTabType.CONFIGS) {
            return;
        }
        if (this.currentTabType != xtnc352) {
            ClickGuiTab ta3d0p2 = this.getCurrentTab();
            if (ta3d0p2 instanceof ModulesTab) {
                ((ModulesTab)ta3d0p2).k();
            }
            if (ta3d0p2 instanceof MarkersPanel) {
                ((MarkersPanel)ta3d0p2).e();
            }
            this.currentTabType = xtnc352;
            this.previousTabType = null;
            this.isSwitching = false;
            this.animationState.d(1.0);
        }
    }

    public ClickGuiTabType k() {
        return this.currentTabType;
    }

    public static float g() {
        return 28.0f;
    }

    private void cleanup() {
        ClickGuiTab ta3d0p2 = this.getCurrentTab();
        if (ta3d0p2 instanceof ModulesTab) {
            ((ModulesTab)ta3d0p2).o();
        }
        if (ta3d0p2 instanceof MarkersPanel) {
            ((MarkersPanel)ta3d0p2).e();
            ((MarkersPanel)ta3d0p2).f();
        }
        if (this.isDockActive()) {
            this.closeDock();
        }
    }
}
