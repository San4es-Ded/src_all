package haron.gui.core;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.client.MinecraftClientAccess;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import net.minecraft.client.util.math.MatrixStack;

public final class InteractionOverlayController {
    private static final InteractionOverlayController INSTANCE = new InteractionOverlayController();
    private static final int DEFAULT_OVERLAY_ALPHA = 80;
    private static final int STRONG_OVERLAY_ALPHA = 150;
    private final AnimatedValue overlayAnimation = new AnimatedValue();
    private boolean overlayVisible;
    private boolean draggingMainPanel;
    private boolean draggingCategoryList;
    private boolean draggingConfigMenu;
    private boolean draggingPopup;
    private boolean strongOverlay;

    private void updateOverlayAnimation() {
        boolean bl = this.b();
        if (bl == this.overlayVisible) {
            return;
        }
        this.overlayAnimation.a(bl ? 1.0 : 0.0, bl ? 0.2 : 0.15, Easings.h);
        this.overlayVisible = bl;
    }

    private void setDraggingMainPanel(boolean bl) {
        int n = 665;
        this.draggingMainPanel = bl;
        this.updateOverlayAnimation();
    }

    private InteractionOverlayController() {
        this.overlayAnimation.d(0.0);
    }

    public void e(boolean bl) {
        this.strongOverlay = bl;
        this.updateOverlayAnimation();
    }

    public boolean e() {
        return this.draggingConfigMenu;
    }

    public void b(boolean bl) {
        this.draggingCategoryList = bl;
        this.updateOverlayAnimation();
    }

    public boolean b() {
        return this.draggingMainPanel || this.draggingCategoryList || this.draggingConfigMenu || this.draggingPopup || this.strongOverlay;
    }

    public boolean c() {
        return this.draggingPopup;
    }

    public void c(boolean bl) {
        this.draggingConfigMenu = bl;
        this.updateOverlayAnimation();
    }

    public void f() {
        this.draggingMainPanel = false;
        this.draggingCategoryList = false;
        this.draggingConfigMenu = false;
        this.draggingPopup = false;
        this.strongOverlay = false;
        this.overlayVisible = false;
        this.overlayAnimation.d(0.0);
    }

    public void d(boolean bl) {
        this.draggingPopup = bl;
        this.updateOverlayAnimation();
    }

    public boolean d() {
        return this.strongOverlay;
    }

    public static InteractionOverlayController a() {
        return INSTANCE;
    }

    public void a(boolean bl) {
        this.setDraggingMainPanel(bl);
    }

    /*
     * Handled unverifiable bytecode (illegal stack merge).
     */
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        this.overlayAnimation.a();
        float f = (float)this.overlayAnimation.j();
        if (f <= 0.01f) {
            return;
        }
        s7swsm2.a(0.0f, 0.0f, (float)(MinecraftClientAccess.d.getWidth() / 2), (float)(MinecraftClientAccess.d.getHeight() / 2), pryrvd.a(pryrvd.FriendCard, (this.strongOverlay ? 150 : (int)80.0f) * f), matrixStack);
    }

    public float g() {
        return (float)this.overlayAnimation.j();
    }
}

