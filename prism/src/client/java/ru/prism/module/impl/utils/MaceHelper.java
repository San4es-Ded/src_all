package ru.prism.module.impl.utils;

import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.utils.math.ChatUtils;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Mace Helper",
        desc = "Помощник для булавы: подскажет заряд в чате и подсветит руку с хотбаром.",
        category = Category.UTILITIES
)
public class MaceHelper extends Module {

    public static MaceHelper get() {
        return Instance.get(MaceHelper.class);
    }

    public final BooleanSetting chatMessage = new BooleanSetting(this, "Сообщение в чат", true);
    public final BooleanSetting handHighlight = new BooleanSetting(this, "Подсветка руки", true);
    public final BooleanSetting hotbarHighlight = new BooleanSetting(this, "Подсветка хотбара", true);

    public static boolean isMace(ItemStack stack) {
        return stack != null && !stack.isEmpty()
                && (stack.isOf(Items.MACE) || stack.getItem().toString().toLowerCase().contains("mace"));
    }

    public static boolean isHoldingMace() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null
                && (isMace(client.player.getMainHandStack()) || isMace(client.player.getOffHandStack()));
    }

    public static int getChargePercentage() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return 0;
        }
        float progress = client.player.getAttackCooldownProgress(0.0F);
        return (int) Math.min(100.0F, Math.max(0.0F, progress * 100.0F));
    }

    public static Color getChargeColor() {
        return getChargeColor(getChargePercentage());
    }

    public static Color getChargeColor(int charge) {
        if (charge >= 80) {
            return new Color(40, 220, 50);
        }
        return charge >= 41 ? new Color(255, 140, 0) : new Color(240, 40, 40);
    }

    @EventHandler
    public void onAttack(AttackEvent event) {
        if (mc.player == null || event.getTarget() == null) {
            return;
        }
        if (!isMace(mc.player.getMainHandStack()) || !chatMessage.getValue()) {
            return;
        }
        ChatUtils.addChatMessage("Булава заряжена на " + getChargePercentage() + "%");
    }
}
