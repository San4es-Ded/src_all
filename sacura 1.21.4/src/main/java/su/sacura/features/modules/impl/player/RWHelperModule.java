package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import java.awt.Color;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import su.sacura.events.player.EventMotion;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.util.impl.render.providers.ColorProvider;

@ModuleAnnotations(name="RW Helper", category=Category.PLAYER)
public class RWHelperModule
        extends Module {
    private final BooleanSetting dragonFly = new BooleanSetting("Полет на драгоне", true).setDescription("Позволяет летать быстрее на драгоне");

    public RWHelperModule() {
        this.addSettings(this.dragonFly);
    }

    @Subscribe
    public void onEvent(EventMotion event) {
        boolean noForward;
        String serverAddress;
        // method_31549() -> getAbilities()
        if (!((Boolean)this.dragonFly.get()).booleanValue() || !RWHelperModule.mc.player.getAbilities().flying) {
            return;
        }
        if (mc.getCurrentServerEntry() != null && ((serverAddress = RWHelperModule.mc.getCurrentServerEntry().address.toLowerCase()).contains("holyworld") || serverAddress.contains("spacetimes"))) {
            if (this.enable) {
                this.toggle();
                RWHelperModule.message(String.valueOf(Formatting.RED) + "Эта функция запрещена на этом сервере!");
            }
            return;
        }
        RWHelperModule.setSpeed(1.0f);
        float y = 0.0f;
        // field_6250 -> forwardSpeed
        boolean bl = noForward = RWHelperModule.mc.player.forwardSpeed == 0.0f && !RWHelperModule.mc.options.leftKey.isPressed() && !RWHelperModule.mc.options.rightKey.isPressed();
        if (RWHelperModule.mc.options.jumpKey.isPressed()) {
            y = noForward ? 0.5f : 0.25f;
        } else if (RWHelperModule.mc.options.sneakKey.isPressed()) {
            y = noForward ? -0.5f : -0.25f;
        }
        // method_18800 -> setVelocity, method_18798 -> getVelocity
        RWHelperModule.mc.player.setVelocity(RWHelperModule.mc.player.getVelocity().x, (double)y, RWHelperModule.mc.player.getVelocity().z);
    }

    public static void setSpeed(float motion) {
        float forward = RWHelperModule.mc.player.input.movementForward;
        float strafe = RWHelperModule.mc.player.input.movementSideways;
        // method_36454() -> getYaw()
        float yaw = RWHelperModule.mc.player.getYaw();
        if (forward == 0.0f && strafe == 0.0f) {
            // method_18800 -> setVelocity, method_18798 -> getVelocity
            RWHelperModule.mc.player.setVelocity(0.0, RWHelperModule.mc.player.getVelocity().y, 0.0);
        } else {
            if (forward != 0.0f) {
                if (strafe > 0.0f) {
                    yaw += forward > 0.0f ? -45.0f : 45.0f;
                } else if (strafe < 0.0f) {
                    yaw += forward > 0.0f ? 45.0f : -45.0f;
                }
                strafe = 0.0f;
                forward = forward > 0.0f ? 1.0f : -1.0f;
            }
            double rad = Math.toRadians(yaw + 90.0f);
            double x = (double)(forward * motion) * Math.cos(rad) + (double)(strafe * motion) * Math.sin(rad);
            double z = (double)(forward * motion) * Math.sin(rad) - (double)(strafe * motion) * Math.cos(rad);
            // method_18800 -> setVelocity, method_18798 -> getVelocity
            RWHelperModule.mc.player.setVelocity(x, RWHelperModule.mc.player.getVelocity().y, z);
        }
    }

    public static void message(String string) {
        if (mc == null || RWHelperModule.mc.player == null || RWHelperModule.mc.world == null || RWHelperModule.mc.inGameHud == null) {
            return;
        }
        int start = ColorProvider.getColorStyle(1.0f);
        int end = ColorProvider.getColorStyle(100.0f);
        RWHelperModule.mc.inGameHud.getChatHud().addMessage(RWHelperModule.applyGradient(string, start, end));
    }

    private static Text applyGradient(String string, int startColor, int endColor) {
        MutableText component = Text.empty();
        String name = "(sacura)";
        int length = "(sacura)".length();
        float inv = length <= 1 ? 0.0f : 1.0f / (float)(length - 1);
        for (int i = 0; i < length; ++i) {
            int rgb = ColorProvider.blendColors(startColor, endColor, length == 1 ? 0.5f : (float)i * inv) & 0xFFFFFF;
            component.append((Text)Text.literal((String)String.valueOf("(sacura)".charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)rgb)).withBold(Boolean.valueOf(true))));
        }
        int gray = Color.GRAY.getRGB() & 0xFFFFFF;
        component.append((Text)Text.literal((String)" >> ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)gray)).withBold(Boolean.valueOf(true))));
        component.append((Text)Text.literal((String)string).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)));
        return component;
    }
}