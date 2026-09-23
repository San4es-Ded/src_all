package moscow.rockstar.mixin.minecraft.client.gui.screen;




import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.animation.*;
import rockstar.client.compat.RenderSystem;
import moscow.rockstar.mixin.accessors.ScreenAccessor;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.render.ScreenRenderEvent;
import pyrock.events.window.ContainerClickEvent;
import pyrock.events.window.ContainerReleaseEvent;
import pyrock.utility.render.CustomDrawContext;
import rockstar.modules.player.InventoryUtilsModule;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.animation.Easing;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.util.Stopwatch;

@Mixin(value={HandledScreen.class})
public abstract class HandledScreenMixin
implements MinecraftClientAccess {
    @Unique
    private Stopwatch timer;
    @Unique
    private long rockstar$openTime;
    @Unique
    private boolean rockstar$scaled;
    @Unique
    private float rockstar$progress;
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    @Shadow
    protected int backgroundWidth;
    @Shadow
    protected int backgroundHeight;

    @Unique
    private Stopwatch rockstar$timer() {
        if (this.timer == null) {
            this.timer = new Stopwatch();
        }
        return this.timer;
    }

    @Shadow
    protected abstract boolean isPointOverSlot(Slot localValue1, double localValue2, double localValue4);

    @Shadow
    protected abstract void onMouseClick(Slot localValue1, int localValue2, int localValue3, SlotActionType localValue4);

    @Inject(method={"init"}, at={@At(value="HEAD")})
    private void rockstar$startOpenAnimation(CallbackInfo callbackInfo) {
        this.rockstar$openTime = System.currentTimeMillis();
    }

    @Inject(method={"renderBackground"}, at={@At(value="HEAD")})
    private void rockstar$beginOpenAnimation(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        this.rockstar$scaled = false;
        if (!BeautifullyModule.internalMethod09825()) {
            return;
        }
        float f2 = (float)(System.currentTimeMillis() - this.rockstar$openTime) / 200.0f;
        if (f2 >= 1.0f || f2 < 0.0f) {
            return;
        }
        this.rockstar$progress = Easing.internalField0811.ease(f2, 0.0f, 1.0f, 1.0f);
        this.rockstar$scaled = true;
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)MathHelper.clamp((float)(f2 * 2.0f), (float)0.0f, (float)1.0f));
    }

    @Inject(method={"renderBackground"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V", shift=At.Shift.BEFORE)})
    private void rockstar$pushOpenAnimation(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        if (!this.rockstar$scaled) {
            return;
        }
        float f2 = this.x + this.backgroundWidth / 2.0f;
        float f3 = this.y + this.backgroundHeight / 2.0f;
        float f4 = 0.88f + 0.12f * this.rockstar$progress;
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(f2, f3 + (1.0f - this.rockstar$progress) * 10.0f);
        drawContext.getMatrices().scale(f4, f4);
        drawContext.getMatrices().translate(-f2, -f3);
    }

    @Unique
    private void rockstar$popOpenAnimation(DrawContext drawContext) {
        if (!this.rockstar$scaled) {
            return;
        }
        this.rockstar$scaled = false;
        drawContext.getMatrices().popMatrix();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void init(CallbackInfo callbackInfo) {
        if (!(RockstarClient.getInstance().internalMethod06896() || HandledScreenMixin.internalField0149.currentScreen instanceof InventoryScreen || HandledScreenMixin.internalField0149.currentScreen instanceof CreativeInventoryScreen || HandledScreenMixin.internalField0149.currentScreen instanceof CraftingScreen || HandledScreenMixin.internalField0149.currentScreen instanceof AnvilScreen || HandledScreenMixin.internalField0149.currentScreen.getTitle().getString().toLowerCase().contains("\u0430\u0443\u043a\u0446\u0438\u043e\u043d\u044b") || HandledScreenMixin.internalField0149.currentScreen.getTitle().getString().toLowerCase().contains("\u0445\u0440\u0430\u043d\u0438\u043b\u0438\u0449\u0435"))) {
            Text text = Text.of((String)LanguageManager.internalMethod07214("inventory.button.move"));
            int n = HandledScreenMixin.internalField0149.textRenderer.getWidth((StringVisitable)text) + 20;
            int n2 = 80;
            int n3 = 200;
            int n4 = Math.max(n2, Math.min(n3, n));
            ButtonWidget buttonWidget2 = ButtonWidget.builder((Text)text, buttonWidget -> this.stealItems()).dimensions(this.x + this.backgroundWidth / 2 - n4 / 2, this.y - 20, n4, 18).build();
            ((ScreenAccessor)((Object)this)).invokeAddDrawableChild(buttonWidget2);
            Text text2 = Text.of((String)LanguageManager.internalMethod07214("inventory.button.steal"));
            int n5 = HandledScreenMixin.internalField0149.textRenderer.getWidth((StringVisitable)text2) + 20;
            int n6 = 80;
            int n7 = 200;
            int n8 = Math.max(n6, Math.min(n7, n5));
            ButtonWidget buttonWidget3 = ButtonWidget.builder((Text)text2, buttonWidget -> this.moveItems()).dimensions(this.x + this.backgroundWidth / 2 - n8 / 2, this.y - 40, n8, 18).build();
            ((ScreenAccessor)((Object)this)).invokeAddDrawableChild(buttonWidget3);
        }
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRender(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        this.rockstar$popOpenAnimation(drawContext);
        CustomDrawContext customDrawContext = CustomDrawContext.of(drawContext);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ScreenRenderEvent(customDrawContext, f));
        DefaultedList defaultedList = HandledScreenMixin.internalField0149.player.currentScreenHandler.slots;
        for (Slot slot : (Iterable<Slot>)(Iterable<?>)defaultedList) {
            InventoryUtilsModule typedValue278 = RockstarClient.getInstance().getModuleManager().getModule(InventoryUtilsModule.class);
            if (!this.isPointOverSlot(slot, n, n2) || !slot.isEnabled() || !typedValue278.isEnabled() || !typedValue278.internalMethod00653().isSelected() || !this.rockstar$timer().internalMethod02365((long)typedValue278.internalMethod02644().internalMethod08576()) || !InputUtil.isKeyPressed(internalField0149.getWindow(), 340) || GLFW.glfwGetMouseButton((long)internalField0149.getWindow().getHandle(), (int)0) != 1) continue;
            this.onMouseClick(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
            this.rockstar$timer().internalMethod00701();
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    private void onMouseClick(Click click, boolean doubled, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ContainerClickEvent((float)click.x(), (float)click.y(), click.button()));
    }

    @Inject(method={"mouseReleased"}, at={@At(value="HEAD")})
    public void mouseReleased(Click click, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ContainerReleaseEvent((float)click.x(), (float)click.y(), click.button()));
    }

    @Unique
    private void moveItems() {
        if (HandledScreenMixin.internalField0149.player != null && HandledScreenMixin.internalField0149.interactionManager != null) {
            int n = HandledScreenMixin.internalField0149.player.currentScreenHandler.slots.size() - 36;
            int n2 = HandledScreenMixin.internalField0149.player.currentScreenHandler.slots.size() - 1;
            for (int i = n; i <= n2; ++i) {
                Slot slot = HandledScreenMixin.internalField0149.player.currentScreenHandler.getSlot(i);
                if (slot == null) continue;
                HandledScreenMixin.internalField0149.interactionManager.clickSlot(HandledScreenMixin.internalField0149.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)HandledScreenMixin.internalField0149.player);
            }
        }
    }

    @Unique
    private void stealItems() {
        if (HandledScreenMixin.internalField0149.player != null && HandledScreenMixin.internalField0149.interactionManager != null) {
            int n = HandledScreenMixin.internalField0149.player.currentScreenHandler.slots.size() - 36;
            for (int i = 0; i < n; ++i) {
                Slot slot = HandledScreenMixin.internalField0149.player.currentScreenHandler.getSlot(i);
                if (slot == null) continue;
                HandledScreenMixin.internalField0149.interactionManager.clickSlot(HandledScreenMixin.internalField0149.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, (PlayerEntity)HandledScreenMixin.internalField0149.player);
            }
        }
    }
}

