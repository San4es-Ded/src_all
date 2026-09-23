package ru.prism.module.impl.utils;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;

@ModuleInfo(
        name = "Fast Exp",
        desc = "Швыряет бутылки опыта пачками за тик, чтобы быстро набить уровни — держи их в руке и кидай без остановки.",
        category = Category.UTILITIES
)
public class FastExp extends Module {

    private final SliderSetting throwsPerTick = new SliderSetting(this, "Бросков за тик", 5.0F, 1.0F, 20.0F, 1.0F);
    private final BooleanSetting onlyInHand = new BooleanSetting(this, "Только в руке", true);

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;

        int amount = throwsPerTick.getValue().intValue();

        for (int i = 0; i < amount; i++) {
            boolean thrown = false;

            if (mc.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) {
                mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
                thrown = true;
            }

            if (!thrown && !onlyInHand.getValue() && mc.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.EXPERIENCE_BOTTLE) {
                mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND, 0, mc.player.getYaw(), mc.player.getPitch()));
            }
        }
    }
}
