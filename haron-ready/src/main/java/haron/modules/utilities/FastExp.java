package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

@ModuleInfo(a="Fast EXP", b="Ускоряет выбрасывание бутылок опыта при ПКМ", c=ModuleCategory.UTILITIES)
public class FastExp
extends HaronModule {
    private final NumberSetting throwsPerTick = new NumberSetting("Бросков в тик", 5.0f, 1.0f, 20.0f, 1.0f);
    private final BooleanSetting onlyInHand = new BooleanSetting("Только в руке", "Бросать только из основной руки", true);
    public static int a;
    public static boolean b;

    @EventHandler
    public void onTick(ClientTickEvent q8krcw2) {
        if (FastExp.c.player == null || FastExp.c.world == null || FastExp.c.currentScreen != null) {
            return;
        }
        if (!FastExp.c.options.useKey.isPressed()) {
            return;
        }
        Hand hand = FastExp.c.player.getActiveHand();
        if (FastExp.c.player.getStackInHand(hand).getItem() != Items.EXPERIENCE_BOTTLE) {
            if (this.onlyInHand.get()) {
                return;
            }
            if (FastExp.c.player.getMainHandStack().getItem() != Items.EXPERIENCE_BOTTLE) {
                return;
            }
            hand = Hand.MAIN_HAND;
        }
        int n = (int)this.throwsPerTick.a();
        for (int i = 0; i < n; ++i) {
            FastExp.c.player.networkHandler.sendPacket((Packet)new PlayerInteractItemC2SPacket(hand, 0, FastExp.c.player.getYaw(), FastExp.c.player.getPitch()));
        }
    }
}

