package haron.hud.notifications;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.config.ClientConfigCoordinator;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.settings.SettingsList;
import haron.gui.settings.ToggleSettingRow;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class NotificationSettingsPopup {
    public static final float DEFAULT_WIDTH = 150.0f;
    public static final float MIN_HEIGHT = 30.0f;
    public static final float MAX_HEIGHT = 120.0f;
    public static final float CORNER_RADIUS = 9.5f;
    public static final float CONTENT_TOP_PADDING = 7.0f;
    private static final int FADE_HEIGHT = 25;
    private static final float FADE_OFFSET = 5.0f;
    private float popupX;
    private float popupY;
    private float anchorX;
    private float anchorY;
    private float anchorWidth;
    private float anchorHeight;
    private final ScrollFadeOverlay bottomFade;
    private final ToggleSettingRow notificationToggle;
    private final ToggleSettingRow musicToggle;
    private final ToggleSettingRow performanceToggle;
    private final ToggleSettingRow eventToggle;
    private final ToggleSettingRow moduleToggle;
    private final AnimatedValue visibilityAnimation = new AnimatedValue();
    private boolean open = false;
    private boolean closing = false;
    private float width = 150.0f;
    private final AnimatedValue fadeAnimation = new AnimatedValue();
    private boolean fadeVisible = true;
    private final SettingsList settingsList = new SettingsList();

    public NotificationSettingsPopup() {
        this.settingsList.b(CONTENT_TOP_PADDING);
        this.settingsList.setMaxHeight(MAX_HEIGHT);
        this.notificationToggle = new ToggleSettingRow("Уведомления", true);
        this.musicToggle = new ToggleSettingRow("Музыка", true);
        this.performanceToggle = new ToggleSettingRow("FPS & Ping", true);
        this.eventToggle = new ToggleSettingRow("Ивенты", true);
        this.moduleToggle = new ToggleSettingRow("Функции", true);
        this.settingsList.add(this.notificationToggle);
        this.settingsList.add(this.musicToggle);
        this.settingsList.add(this.performanceToggle);
        this.settingsList.add(this.eventToggle);
        this.settingsList.add(this.moduleToggle);
        this.bottomFade = new ScrollFadeOverlay(25, 5.0f, 9.5f);
        this.visibilityAnimation.set(0.0);
        this.fadeAnimation.set(1.0);
    }

    private boolean isCloseAnimationFinished() {
        return this.closing && this.visibilityAnimation.isFinished() && this.visibilityAnimation.value() < 0.01;
    }

    public void setModuleNotificationsEnabled(boolean enabled) {
        this.moduleToggle.setEnabled(enabled);
    }

    public boolean areMusicNotificationsEnabled() {
        return this.musicToggle.isEnabled();
    }

    public void updateAnchorBounds(float x, float y, float width, float height) {
        this.anchorX = x;
        this.anchorY = y;
        this.anchorWidth = width;
        this.anchorHeight = height;
    }

    public void setMusicNotificationsEnabled(boolean enabled) {
        this.musicToggle.setEnabled(enabled);
    }

    public boolean mouseReleased(int mouseX, int mouseY) {
        if (!this.open || this.closing || this.visibilityAnimation.value() < 0.5) {
            return false;
        }
        if (this.contains(mouseX, mouseY)) {
            if (!this.settingsList.hasOpenEditor()) {
                return this.settingsList.b(this.popupX, this.popupY, this.width, this.height(), mouseX, mouseY);
            }
            this.settingsList.closeOpenEditors();
            return true;
        }
        if (!this.settingsList.hasOpenEditor()) {
            return false;
        }
        this.settingsList.closeOpenEditors();
        return true;
    }

    public void close() {
        if (this.open) {
            this.closing = true;
            this.settingsList.closeOpenEditors();
            this.visibilityAnimation.animateTo(0.0, 0.15, Easings.EASE_IN_QUAD);
        }
    }

    public void setPerformanceNotificationsEnabled(boolean enabled) {
        this.performanceToggle.setEnabled(enabled);
    }

    public void reset() {
        this.open = false;
        this.closing = false;
        this.visibilityAnimation.set(0.0);
        this.settingsList.closeOpenEditors();
    }

    public void mouseMoved(int mouseX, int mouseY) {
        if (this.open) {
            this.settingsList.c(mouseX, mouseY);
        }
    }

    public boolean areNotificationsEnabled() {
        return this.notificationToggle.isEnabled();
    }

    private void updateAnimations() {
        this.visibilityAnimation.update();
        this.fadeAnimation.update();
        if (this.isCloseAnimationFinished()) {
            this.open = false;
            this.closing = false;
        }
    }

    public boolean areModuleNotificationsEnabled() {
        return this.moduleToggle.isEnabled();
    }

    public boolean contains(int mouseX, int mouseY) {
        return this.open && this.visibilityAnimation.value() >= 0.5
                && GuiInput.contains(this.popupX, this.popupY, this.width, this.height(), mouseX, mouseY);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setEventNotificationsEnabled(boolean enabled) {
        this.eventToggle.setEnabled(enabled);
    }

    public void open(float anchorX, float anchorY, float anchorWidth, float anchorHeight) {
        this.open(anchorX, anchorY, anchorWidth, anchorHeight, DEFAULT_WIDTH);
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationToggle.setEnabled(enabled);
    }

    public boolean mouseScrolled(float amount, int mouseX, int mouseY) {
        if (!this.open || this.closing) {
            return false;
        }
        if (!this.contains(mouseX, mouseY)) {
            return false;
        }
        this.settingsList.a(amount, this.height());
        return true;
    }

    public void open(float anchorX, float anchorY, float anchorWidth, float anchorHeight, float popupWidth) {
        if (this.open && !this.closing) {
            this.close();
            return;
        }
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.anchorWidth = anchorWidth;
        this.anchorHeight = anchorHeight;
        this.width = popupWidth;
        this.popupX = anchorX + anchorWidth / 2.0f - popupWidth / 2.0f;
        this.popupY = anchorY + anchorHeight + 11.0f;
        this.open = true;
        this.closing = false;
        this.visibilityAnimation.animateTo(1.0, 0.25, Easings.EASE_OUT_ELASTIC);
    }

    public float width() {
        return this.width;
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (!this.open || this.closing || this.visibilityAnimation.value() < 0.5) {
            return false;
        }
        if (this.contains(mouseX, mouseY)) {
            boolean handled = this.settingsList.a(this.popupX, this.popupY, this.width, this.height(), mouseX, mouseY);
            if (handled) {
                ClientConfigCoordinator.a().h();
            }
            return handled;
        }
        if (!this.settingsList.hasOpenEditor()) {
            return false;
        }
        this.settingsList.closeOpenEditors();
        return true;
    }

    public void render(MatrixStack matrices, ShapeRenderer renderer, int mouseX, int mouseY) {
        int n3 = 0;
        if (this.open) {
            this.updateAnimations();
            float f = (float)this.visibilityAnimation.value();
            if (f >= 0.01f) {
                float f2;
                float f3 = this.height();
                float f4 = Math.max(0.0f, Math.min(1.0f, f));
                int n4 = (int)(255.0f * f4);
                float f5 = this.popupX + this.width / 2.0f;
                float f6 = this.popupY + f3 / 2.0f;
                float f7 = this.width * f4;
                float f8 = f3 * f4;
                float f9 = f5 - f7 / 2.0f;
                float f10 = f6 - f8 / 2.0f;
                Color color = pryrvd.a(pryrvd.q, n4);
                Color color2 = pryrvd.a(pryrvd.r, n4);
                renderer.a(f9 - 0.5f * f4, f10 - 0.5f * f4, f7 + f4, f8 + f4, 9.5f * f4, color, color, color2, color2, matrices);
                Color color3 = pryrvd.a(pryrvd.e, n4);
                Color color4 = pryrvd.a(pryrvd.f, n4);
                renderer.a(f9, f10, f7, f8, 9.5f * f4, color3, color3, color4, color4, matrices);
                IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
                if (hclqea2 != null) {
                    Identifier identifier = hclqea2.a();
                    RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
                    f2 = (float)hclqea2.b() / 2.0f * f4;
                    float f11 = (float)hclqea2.c() / 2.0f * f4;
                    float f12 = f9 + f7 / 2.0f;
                    float f13 = f10 - f2 / 2.0f - 1.0f;
                    Color color5 = pryrvd.a(pryrvd.aa, n4);
                    matrices.push();
                    matrices.translate(f12, f13, 0.0f);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0f));
                    matrices.translate(-f12, -f13, 0.0f);
                    renderer.a(identifier, f12 - f2 / 2.0f - 0.5f, f13 - f11 / 2.0f, f2, f11, color5, matrices);
                    matrices.pop();
                }
                if (f4 > 0.1f) {
                    int fadeRequired;
                    renderer.b().a(f9, f10 + 7.0f * f4, f7, (f8 - 7.0f - 6.0f) * f4, 9.5f * f4, matrices);
                    n3 = this.closing ? -1 : mouseX;
                    this.settingsList.a(matrices, renderer, f9, f10, f7, f8, n3, !this.closing ? mouseY : -1, f4, f4);
                    fadeRequired = !this.settingsList.contentOverflows(f8) || this.settingsList.editorExtendsBelow(f10 + f8) ? 0 : 1;
                    if (BooleanCoercion.from(fadeRequired) != this.fadeVisible) {
                        this.fadeAnimation.animateTo(fadeRequired == 0 ? 0.0 : 1.0, 0.15, Easings.EASE_OUT_QUAD);
                        this.fadeVisible = BooleanCoercion.from(fadeRequired);
                    }
                    if ((f2 = (float)this.fadeAnimation.value()) > 0.01f) {
                        this.bottomFade.render(matrices, renderer, f9, f10, f7, f8, f4 * f2);
                    }
                    renderer.b().a(matrices);
                }
            }
        }
    }

    public void mouseDragged(int mouseX, int mouseY, double deltaX, double deltaY) {
        if (!this.open || this.closing) {
            return;
        }
        this.settingsList.a(this.height(), mouseX, mouseY, deltaX, deltaY);
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean areEventNotificationsEnabled() {
        return this.eventToggle.isEnabled();
    }

    public float height() {
        return Math.max(30.0f, Math.min(this.settingsList.preferredHeight(), 120.0f));
    }

    public boolean arePerformanceNotificationsEnabled() {
        return this.performanceToggle.isEnabled();
    }
}
