package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;

@ModuleAnnotations(name = "Item Release", category = Category.PLAYER)
public class ItemReleaseModule extends Module {
    @Subscribe
    public void onEvent(EventUpdate e) {
        if (mc.player != null && mc.player.isSneaking())
            return;
        ItemStack stack = mc.player.getMainHandStack();
        Item item = stack.getItem();
        int useTime = stack.getMaxUseTime((LivingEntity)mc.player) - mc.player.getItemUseTime();
        if (item instanceof net.minecraft.item.TridentItem && useTime >= 10) {
            releaseUse();
        } else if (item instanceof net.minecraft.item.CrossbowItem && useTime >= stack.getMaxUseTime((LivingEntity)mc.player) - 1) {
            releaseUse();
        }
    }

    private void releaseUse() {
        // Исправлено: field_12974 -> RELEASE_USE_ITEM
        // Исправлено: field_11033 -> DOWN
        mc.player.networkHandler.sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
        mc.player.stopUsingItem();
    }
}