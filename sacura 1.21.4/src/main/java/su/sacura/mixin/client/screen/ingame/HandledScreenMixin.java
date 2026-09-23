package su.sacura.mixin.client.screen.ingame;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.player.ItemScrollerModule;
import su.sacura.util.impl.math.helper.TimerUtil;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({HandledScreen.class})
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>, MinecraftWrapper {
    @Unique private final TimerUtil timerUtil = new TimerUtil();

    @Shadow @Nullable protected Slot field_2787;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void onMouseClick(Slot paramSlot, int paramInt1, int paramInt2, SlotActionType paramSlotActionType);

    @Inject(method = {"drawMouseoverTooltip"}, at = {@At("HEAD")})
    private void onDrawMouseoverTooltip(DrawContext context, int x, int y, CallbackInfo ci) {
        if (this.field_2787 == null || !this.field_2787.hasStack())
            return;
        long windowHandle = mc.getWindow().getHandle();
        boolean leftMousePressed = (GLFW.glfwGetMouseButton(windowHandle, 0) == 1);
        boolean shiftPressed = (InputUtil.isKeyPressed(windowHandle, 340) || InputUtil.isKeyPressed(windowHandle, 344));
        // Исправлено: this.field_22787.currentScreen -> mc.currentScreen
        if (Sacura.getInstance().getModuleManager().getModule(ItemScrollerModule.class) != null && ((ItemScrollerModule)Sacura.getInstance().getModuleManager().getModule(ItemScrollerModule.class)).enable && leftMousePressed && shiftPressed && mc.currentScreen != null &&
                this.timerUtil.hasTimeElapsed(((Float)((ItemScrollerModule)Sacura.getInstance().getModuleManager().getModule(ItemScrollerModule.class)).speed.get()).longValue()) && this.field_2787.hasStack()) {
            onMouseClick(this.field_2787, this.field_2787.id, 0, SlotActionType.QUICK_MOVE);
            this.timerUtil.reset();
        }
    }
}