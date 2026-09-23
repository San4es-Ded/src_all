package wtf.wyvern.client.modules.impl.player;

import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "FastUse", category = Category.PLAYER,
        description = "Позволяет использовать предметы быстрее")
@FastNative
public class FastUse extends Module {
    public static final FastUse INSTANCE = new FastUse();

    private final BooleanSetting experience = new BooleanSetting("Опыт", true);
    private final BooleanSetting blocks = new BooleanSetting("Быстрая установка блоков", true);

    private FastUse() {
    }

    @EventTarget
    private void onUpdate(EventGameUpdate event) {
        if (mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }

        ItemStack mainHand = mc.player.getStackInHand(Hand.MAIN_HAND);
        if (experience.isEnabled() && mainHand.getItem() instanceof ExperienceBottleItem
                && mc.options.useKey.isPressed()) {
            mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(
                    Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
        }
    }

    public boolean shouldRemoveBlockCooldown() {
        return isEnabled() && blocks.isEnabled() && mc.player != null
                && mc.options.useKey.isPressed()
                && mc.player.getMainHandStack().getItem() instanceof BlockItem;
    }

}
