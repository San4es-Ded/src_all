package ru.haron.mixin;

import haron.modules.hud.ClientColor;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.haron.Haron;

@Mixin(value={Screen.class})
public class MenuBackgroundMixin {
    private static final Identifier HARON_MENU_BG = Identifier.of((String)"haron", (String)"textures/menu.png");

    @Inject(method={"renderBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$renderMenuBg(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Object self = this;
        if (self instanceof SelectWorldScreen || self instanceof MultiplayerScreen || self instanceof OptionsScreen || self instanceof ChatScreen) {
            ci.cancel();
            float sw = context.getScaledWindowWidth();
            float sh = context.getScaledWindowHeight();
            MinecraftClient client = MinecraftClient.getInstance();
            ShapeRenderer r = Haron.getInstance().getRender();
            if (r != null) {
                Color tint = ClientColor.currentColor();
                if (tint == null) {
                    tint = new Color(255, 255, 255, 255);
                }
                r.a(HARON_MENU_BG, 0.0f, 0.0f, sw, sh, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, tint, context.getMatrices());
            }
        }
    }
}
