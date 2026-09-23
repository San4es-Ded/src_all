package ru.prism.mixin;

import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import ru.prism.Client;
import ru.prism.module.impl.render.Animations;
import ru.prism.module.impl.utils.ChatHelper;
import ru.prism.module.impl.utils.CommandSafe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow
    protected TextFieldWidget chatField;

    @Unique private List<String> prism$suggestions = Collections.emptyList();
    @Unique private int prism$selected = 0;
    @Unique private boolean prism$chatSlidePushed;

    // геометрия попапа (для кликов) — заполняется в render
    @Unique private int prism$boxX, prism$boxY, prism$boxW, prism$boxCount;
    @Unique private static final int PRISM_LINE_H = 12;

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void interceptMessage(String message, boolean addToHistory, CallbackInfo ci) {
        if (message.isEmpty()) return;

        ChatHelper chatHelper = ChatHelper.get();
        String fixed = chatHelper != null ? chatHelper.fixLayout(message) : message;

        CommandSafe commandSafe = CommandSafe.get();
        if (commandSafe != null && commandSafe.handle(fixed)) {
            ci.cancel();
            return;
        }

        if (!fixed.equals(message)) {
            ci.cancel();
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.player.networkHandler != null) {
                if (fixed.startsWith("/")) {
                    client.player.networkHandler.sendChatCommand(fixed.substring(1));
                } else {
                    client.player.networkHandler.sendChatMessage(fixed);
                }
            }
            return;
        }

        char prefix = Client.get().commandManager().getPrefix();
        if (message.charAt(0) != prefix) return;
        ci.cancel();
        Client.get().commandManager().handleMessage(message);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void prism$renderSuggestions(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        prism$boxCount = 0;

        String text = chatField.getText();
        char prefix = Client.get().commandManager().getPrefix();
        if (text.isEmpty() || text.charAt(0) != prefix) {
            prism$suggestions = Collections.emptyList();
            return;
        }

        prism$suggestions = Client.get().commandManager().getSuggestions(text);
        if (prism$suggestions.isEmpty()) return;

        if (prism$selected >= prism$suggestions.size()) prism$selected = 0;

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int count = Math.min(prism$suggestions.size(), 10);

        int width = 0;
        for (int i = 0; i < count; i++) {
            width = Math.max(width, tr.getWidth(prism$suggestions.get(i)));
        }
        width += 6;

        int boxH = count * PRISM_LINE_H;
        int x = chatField.getX() - 2;
        int y = chatField.getY() - boxH - 1;

        prism$boxX = x;
        prism$boxY = y;
        prism$boxW = width;
        prism$boxCount = count;

        context.fill(x, y, x + width, y + boxH, 0xE6000000);
        context.fill(x, y, x + width, y + 1, 0x40FFFFFF);

        for (int i = 0; i < count; i++) {
            int ly = y + i * PRISM_LINE_H;
            boolean sel = i == prism$selected;
            if (sel) context.fill(x, ly, x + width, ly + PRISM_LINE_H, 0x55FFFFFF);
            context.drawText(tr, prism$suggestions.get(i), x + 3, ly + 2,
                    sel ? 0xFFFFFF55 : 0xFFBBBBBB, false);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void prism$keyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (prism$suggestions.isEmpty()) return;
        int n = prism$suggestions.size();

        switch (input.getKeycode()) {
            case GLFW.GLFW_KEY_TAB -> {
                prism$apply(prism$suggestions.get(Math.min(prism$selected, n - 1)));
                cir.setReturnValue(true);
            }
            case GLFW.GLFW_KEY_UP -> {
                prism$selected = (prism$selected - 1 + n) % n;
                cir.setReturnValue(true);
            }
            case GLFW.GLFW_KEY_DOWN -> {
                prism$selected = (prism$selected + 1) % n;
                cir.setReturnValue(true);
            }
            default -> {}
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void prism$mouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {

        int button = click.button();

        double mouseX = click.x();
        double mouseY = click.y();

        if (prism$suggestions.isEmpty() || prism$boxCount == 0 || button != 0) return;

        if (mouseX >= prism$boxX && mouseX <= prism$boxX + prism$boxW
                && mouseY >= prism$boxY && mouseY <= prism$boxY + prism$boxCount * PRISM_LINE_H) {
            int idx = (int) ((mouseY - prism$boxY) / PRISM_LINE_H);
            if (idx >= 0 && idx < prism$boxCount && idx < prism$suggestions.size()) {
                prism$apply(prism$suggestions.get(idx));
                cir.setReturnValue(true);
            }
        }
    }

    @Unique
    private void prism$apply(String suggestion) {
        chatField.setText(suggestion);
        chatField.setCursorToEnd(false);
        prism$selected = 0;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void prism$startChatAnimation(CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations != null && animations.isEnabled() && animations.chat.getValue()) {
            animations.startChatInput();
        }
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void prism$resetChatAnimation(CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations != null) {
            animations.resetChatInput();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void prism$renderChatAnimationHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations == null || !animations.isEnabled() || !animations.chat.getValue()) {
            prism$chatSlidePushed = false;
            return;
        }
        animations.updateChatInput();
        float offset = animations.chatInputOffset();
        if (offset <= 0.0F) {
            prism$chatSlidePushed = false;
            return;
        }
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0.0F, offset);
        prism$chatSlidePushed = true;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void prism$renderChatAnimationTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (prism$chatSlidePushed) {
            context.getMatrices().popMatrix();
            prism$chatSlidePushed = false;
        }
    }
}
